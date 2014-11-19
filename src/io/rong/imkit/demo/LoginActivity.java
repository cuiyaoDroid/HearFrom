package io.rong.imkit.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sea_monster.core.exception.BaseException;
import com.sea_monster.core.network.AbstractHttpRequest;
import com.tingshuo.hearfrom.HearFromTabMainActivity;
import com.tingshuo.hearfrom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RCloudContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.demo.model.User;
import io.rong.imkit.demo.ui.LoadingDialog;
import io.rong.imkit.demo.ui.WinToast;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.UserInfo;

public class LoginActivity extends BaseApiActivity implements OnClickListener, Callback {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mRegisterBtn;
    private Button mLoginBtn;

    private String mDeviceId;
    private LoadingDialog mDialog;

    private TextView mVersionTextView;
    private TextView mBuildTextView;

    private Handler mHandler;
    private static final int HANDLER_LOGIN_SUCCESS = 1;
    private static final int HANDLER_LOGIN_FAILURE = 2;
    private static final int REQUEST_CODE_REGISTER = 2001;

    public static final String INTENT_EMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";

    private AbstractHttpRequest<User> loginHttpRequest;
    private AbstractHttpRequest<ArrayList<User>> getFriendsHttpRequest;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_login_test;
    }

    @Override
    protected void initView() {

        mUserNameEditText = getViewById(android.R.id.text1);
        mPasswordEditText = getViewById(android.R.id.text2);
        mRegisterBtn = getViewById(android.R.id.button1);
        mLoginBtn = getViewById(android.R.id.button2);
        mLoginBtn = getViewById(android.R.id.button2);
        mVersionTextView = getViewById(R.id.version_code);
        mBuildTextView = getViewById(R.id.build_code);

    }

    @Override
    protected void initData() {

        String email = DemoContext.getInstance().getSharedPreferences().getString(INTENT_EMAIL, "");
        String password = DemoContext.getInstance().getSharedPreferences().getString(INTENT_PASSWORD, "");

        mUserNameEditText.setText(email);
        mPasswordEditText.setText(password);

        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceId = mTelephonyManager.getDeviceId();

        mDialog = new LoadingDialog(this);
        mHandler = new Handler(this);

        String[] versionInfo = getVersionInfo();
        mBuildTextView.setText(String.format(getResources().getString(R.string.login_version_date), versionInfo[0]));
        mVersionTextView.setText(String.format(getResources().getString(R.string.login_version_code), versionInfo[1]));

    }


    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {

        //打开注册页面
        if (v == mRegisterBtn) {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGISTER);
        } else if (v == mLoginBtn) {
            String username = mUserNameEditText.getEditableText().toString();
            String password = mPasswordEditText.getEditableText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                WinToast.toast(LoginActivity.this, R.string.login_erro_is_null);
                return;
            }

            if (mDialog != null && !mDialog.isShowing())
                mDialog.show();

            //发起登录 http请求 (注：非融云SDK接口，是demo接口)
            loginHttpRequest = DemoContext.getInstance().getDemoApi().login(username, password, mDeviceId, this);

        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

        //登录成功  返回数据
        if (loginHttpRequest == request) {

            if (obj instanceof User) {
                final User user = (User) obj;

                /**
                 * IMKit SDK调用第二步
                 *
                 * 建立与服务器的连接
                 *
                 * 详见API
                 * http://docs.rongcloud.cn/api/android/imkit/index.html
                 */
                RongIM.connect(user.getToken(), new ConnectCallback() {

                    @Override
                    public void onSuccess(String userId) {
                        Log.d("LoginActivity", "---------userId----------:" + userId);
                        mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        mHandler.obtainMessage(HANDLER_LOGIN_FAILURE).sendToTarget();
                    }

                });

                Editor editor = DemoContext.getInstance().getSharedPreferences().edit();
                editor.putString(INTENT_PASSWORD, mPasswordEditText.getText().toString());
                editor.putString(INTENT_EMAIL, mUserNameEditText.getText().toString());
                editor.commit();


                //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
                getFriendsHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(mUserNameEditText.getText().toString(), this);


                DemoContext.getInstance().setCurrentUser(user);


            } else {
                WinToast.toast(this, R.string.login_failure);

                if (mDialog != null)
                    mDialog.dismiss();
            }

            //获取好友列表接口  返回好友数据  (注：非融云SDK接口，是demo接口)
        } else if (getFriendsHttpRequest == request) {

            @SuppressWarnings("unchecked")
            final ArrayList<UserInfo> friends = (ArrayList<UserInfo>) getFriends((ArrayList<User>) obj);

            DemoContext.getInstance().setFriends(friends);

        }

    }

    /**
     * 把自己系统中的用户对象转换成融云中的用户对象
     *
     * @param users
     * @return
     */
    public List<UserInfo> getFriends(ArrayList<User> users) {

        if (users == null)
            return null;

        ArrayList<UserInfo> friends = new ArrayList<UserInfo>();

        for (User user : users) {
            UserInfo info = new UserInfo(String.valueOf(user.getId()), user.getUsername(), user.getPortrait());
            friends.add(info);
        }

        return friends;
    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

        if (loginHttpRequest == request) {

            WinToast.toast(this, R.string.login_failure);

            if (mDialog != null)
                mDialog.dismiss();
        }

    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == HANDLER_LOGIN_FAILURE) {
            WinToast.toast(LoginActivity.this, R.string.login_failure);

            if (mDialog != null)
                mDialog.dismiss();

        } else if (msg.what == HANDLER_LOGIN_SUCCESS) {

            DemoContext.getInstance().setGroupInfoProvider();
            DemoContext.getInstance().receviceMessage();

            WinToast.toast(LoginActivity.this, R.string.login_success);

            if (mDialog != null)
                mDialog.dismiss();

            startActivity(new Intent(this, HearFromTabMainActivity.class));

            initGroupInfo();
        }
        return false;
    }

    private void initGroupInfo() {

        RongIMClient.Group group1 = new RongIMClient.Group("group001", "群组一", "http://docs.rongcloud.cn/assets/img/logo@2x.png");
        RongIMClient.Group group2 = new RongIMClient.Group("group002", "群组二", "http://cn.bing.com/fd/s/a/k_zh_cn_s2.png");
        RongIMClient.Group group3 = new RongIMClient.Group("group003", "群组三", "http://www.baidu.com/img/bdlogo.png");
        List<RongIMClient.Group> groups = new ArrayList<RongIMClient.Group>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);

        HashMap<String, RongIMClient.Group> groupM = new HashMap<String, RongIMClient.Group>();
        groupM.put("group001", group1);
        groupM.put("group002", group2);
        groupM.put("group003", group3);


        RongIM.getInstance().syncGroup(groups, new RongIM.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.d("RONG_SDK", "GROUP_SYNC_SUCCESS");
            }

            @Override
            public void onError(ErrorCode errorCode) {
                Log.d("RONG_SDK", "GROUP_SYNC_FAIL:" + errorCode.getMessage());
            }
        });

        DemoContext.getInstance().setGroupMap(groupM);
        DemoContext.getInstance().setGroupInfoProvider();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                mUserNameEditText.setText(data.getStringExtra(INTENT_EMAIL));
                mPasswordEditText.setText(data.getStringExtra(INTENT_PASSWORD));
            }
        }
    }


    private String[] getVersionInfo() {
        String[] version = new String[2];

        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


}
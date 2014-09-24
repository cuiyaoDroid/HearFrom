package com.tingshuo.hearfrom;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.tingshuo.service.IConnectionStatusCallback;
import com.tingshuo.service.XXService;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.T;

public class LoginActivity extends SherlockActivity implements IConnectionStatusCallback{
	public static final String LOGIN_ACTION = "com.tingshuo.hearfrom.action.LOGIN";
	public static final String INTENT_EXTRA_USERNAME = HearFromMainActivity.class
			.getName() + ".username";
	private XXService mXxService;
	private CheckBox save_check;
	private ProgressDialog progreeDialog;
	private Button login_btn;
	private EditText usr_edit;
	private EditText pass_edit;
	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXxService = ((XXService.XXBinder) service).getService();
			mXxService.registerConnectionStatusCallback(LoginActivity.this);
			// 开始连接xmpp服务器
			if(mXxService.isAuthenticated()){
				Intent intent = new Intent(LoginActivity.this,HearFromMainActivity.class);
				startActivity(intent);
				progreeDialog.dismiss();
				finish();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}

	};
	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		// TODO Auto-generated method stub
		if (connectedState == XXService.CONNECTED) {
			T.showLong(this, "登录成功");
			Intent intent = new Intent(this,HearFromMainActivity.class);
			startActivity(intent);
			progreeDialog.dismiss();
			finish();
		} else if (connectedState == XXService.DISCONNECTED)
			T.showLong(this, "登录失败"+ reason);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		startService(new Intent(LoginActivity.this, XXService.class));
		bindXMPPService();

		
		save_check=(CheckBox)findViewById(R.id.save_check);
		login_btn=(Button)findViewById(R.id.login_btn);
		usr_edit=(EditText)findViewById(R.id.usr_edit);
		pass_edit=(EditText)findViewById(R.id.pass_edit);
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		String loginName = userInfo.getString("loginName", "");
		String password = userInfo.getString("password", "");
		int savepassword = userInfo.getInt("savepassword", 0);
		usr_edit.setText(loginName);
		pass_edit.setText(password);
		save_check.setChecked(savepassword==1?true:false);
		initProgressDialog();
		login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String loginName=usr_edit.getText().toString();
				String password=pass_edit.getText().toString();
				startLogin(loginName,password);
			}
		});
		
	}
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在登录请稍等...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	private String splitAndSaveServer(String account) {
		if (!account.contains("@"))
			return account;
		String customServer = PreferenceUtils.getPrefString(this,
				PreferenceConstants.CUSTOM_SERVER, "");
		String[] res = account.split("@");
		String userName = res[0];
		String server = res[1];
		// check for gmail.com and other google hosted jabber accounts
		if ("gmail.com".equals(server) || "googlemail.com".equals(server)
				|| PreferenceConstants.GMAIL_SERVER.equals(customServer)) {
			// work around for gmail's incompatible jabber implementation:
			// send the whole JID as the login, connect to talk.google.com
			userName = account;

		}
		PreferenceUtils.setPrefString(this, PreferenceConstants.Server, server);
		return userName;
	}
	private void startLogin(final String loginName,final String password){
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		userInfo.edit().putString("loginName", loginName).commit();
		if (save_check.isChecked()) {
			userInfo.edit().putString("password", password).commit();
			userInfo.edit().putInt("savepassword", 1).commit();
		} else {
			userInfo.edit().putString("password", "").commit();
			userInfo.edit().putInt("savepassword", 0).commit();
		}
		save2Preferences(loginName,password);
		progreeDialog.show();
		splitAndSaveServer(loginName+"@www.wangjiaqi666.cn");
		if (mXxService != null) {
			mXxService.Login(loginName, password);
		}
	}
	private void save2Preferences(String loginName,String password) {
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
				loginName);// 帐号是一直保存的
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					password);
	}
	private void unbindXMPPService() {
		try {
			unbindService(mServiceConnection);
			L.i(HearFromMainActivity.class, "[SERVICE] Unbind");
		} catch (IllegalArgumentException e) {
			L.e(HearFromMainActivity.class, "Service wasn't bound!");
		}
	}

	private void bindXMPPService() {
		L.i(HearFromMainActivity.class, "[SERVICE] bind");
		Intent mServiceIntent = new Intent(this, XXService.class);
		mServiceIntent.setAction(LOGIN_ACTION);
		bindService(mServiceIntent, mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindXMPPService();
	}
}

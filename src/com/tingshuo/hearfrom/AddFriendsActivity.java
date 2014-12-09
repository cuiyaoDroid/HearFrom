package com.tingshuo.hearfrom;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tingshuo.hearfrom.base.BaseSwipeFragmentActivity;
import com.tingshuo.tool.T;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.im.RongIMTool;
import com.tingshuo.tool.im.RongMessageTYPE;
import com.tingshuo.web.http.HttpJsonTool;

public class AddFriendsActivity extends BaseSwipeFragmentActivity {
	private EditText userIdEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		initProgressDialog("正在发送好友请求");
		initContentView();
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		titleback.setVisibility(View.GONE);
		title_middle.setText("添加好友");
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("发送");
		userIdEdit = (EditText)findViewById(R.id.user_id_edit);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private ProgressDialog mProgressDialog;

	private void initProgressDialog(String content) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("");
		mProgressDialog.setMessage(content);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	private void sendFriendInvite() {
		final String user_id=userIdEdit.getText().toString();
		if(user_id.length()==0){
			T.show(getApplicationContext(), "请输入对方账号",Toast.LENGTH_LONG);
			return;
		}
		UserInfoHelper helper=new UserInfoHelper(getApplicationContext());
		UserInfoHolder holder=helper.selectData_Id(HearFromApp.user_id);
		helper.close();
		final String nickname;
		if(holder==null){
			nickname="";
		}else{
			nickname=holder.getNickname();
		}
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				return HttpJsonTool.getInstance().friend_sendAdd(
						getApplicationContext(), Integer.parseInt(user_id));
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result.startsWith(HttpJsonTool.ERROR)){
					
				}else if(result.startsWith(HttpJsonTool.ERROR403)){
					
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					RongIMTool.getInstance().sendMessage(RongMessageTYPE.MESSAGE_TYPE_ADDFRIENDS,nickname+"请求添加你为好友", String.valueOf(user_id));
					T.show(getApplicationContext(), "发送请求成功，等待对方答复", Toast.LENGTH_SHORT);
					finish();
				}
			}

		};
		task.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_right:
			sendFriendInvite();
			break;
		case R.id.title_img_back:
			finish();
		default:
			break;
		}
	}
}

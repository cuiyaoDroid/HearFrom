package com.tingshuo.hearfrom.setting;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.ActivityTool;
import com.tingshuo.tool.T;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.http.HttpStringMD5;

public class authenticationActivity extends BaseSwipeBaceActivity{
	private View setting_account;
	private TextView setting_account_title;
	private EditText setting_account_editer;
	
	private View setting_password;
	private TextView setting_password_title;
	private EditText setting_password_editer;
	
	private View setting_password_re;
	private TextView setting_password_re_title;
	private EditText setting_password_re_editer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		initContentView();
		initProgressDialog();
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		titleback.setVisibility(View.VISIBLE);
		title_middle.setText("认证");
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("完成");
		
		setting_account=findViewById(R.id.setting_account);
		setting_account_title=(TextView)setting_account.findViewById(R.id.title);
		setting_account_title.setText("账号");
		setting_account_editer=(EditText)setting_account.findViewById(R.id.editer);
		setting_account_editer.setHint("输入账号");
		
		setting_password=findViewById(R.id.setting_password);
		setting_password_title=(TextView)setting_password.findViewById(R.id.title);
		setting_password_title.setText("密码");
		setting_password_editer=(EditText)setting_password.findViewById(R.id.editer);
		setting_password_editer.setHint("输入密码");
		setting_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_password_re=findViewById(R.id.setting_password_re);
		setting_password_re_title=(TextView)setting_password_re.findViewById(R.id.title);
		setting_password_re_title.setText("重复密码");
		setting_password_re_editer=(EditText)setting_password_re.findViewById(R.id.editer);
		setting_password_re_editer.setHint("重复密码");
		setting_password_re_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

	private void identifUser(){
		final String account = setting_account_editer.getText().toString();
		final String pass = setting_password_editer.getText().toString();
		final String pass_re = setting_password_re_editer.getText().toString();
		if(!pass.equals(pass_re)){
			T.show(getApplicationContext(), "两次输入的密码不同", Toast.LENGTH_LONG);
			return;
		}
		progreeDialog.show();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().identifUser(getApplicationContext()
						, account, HttpStringMD5.md5(pass));
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if(result.startsWith(HttpJsonTool.SUCCESS)){
					T.show(getApplicationContext(), "认证成功", Toast.LENGTH_LONG);
					finishActivity();
				}else if(result.startsWith(HttpJsonTool.ERROR403)){
					ActivityTool.gotoLoginView(getApplicationContext());
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.show(getApplicationContext(), "认证失败，请求超时", Toast.LENGTH_LONG);
				}
			}
		};
		task.execute();
	}
	ProgressDialog progreeDialog;
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在提交请求...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finishActivity();
			break;
		case R.id.title_txt_right:
			identifUser();
			
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finishActivity();
	}
	private void finishActivity(){
		finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}
}

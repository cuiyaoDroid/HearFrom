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

public class editPasswordActivity extends BaseSwipeBaceActivity{
	private View setting_old_password;
	private TextView setting_old_password_title;
	private EditText setting_old_password_editer;
	
	private View setting_new_password;
	private TextView setting_new_password_title;
	private EditText setting_new_password_editer;
	
	private View setting_new_password_re;
	private TextView setting_new_password_re_title;
	private EditText setting_new_password_re_editer;
	private ProgressDialog progreeDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_password);
		initContentView();
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		initProgressDialog();
		titleback.setVisibility(View.VISIBLE);
		title_middle.setText("修改密码");
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("完成");
		
		setting_old_password=findViewById(R.id.setting_old_password);
		setting_old_password_title=(TextView)setting_old_password.findViewById(R.id.title);
		setting_old_password_title.setText("旧密码");
		setting_old_password_editer=(EditText)setting_old_password.findViewById(R.id.editer);
		setting_old_password_editer.setHint("输入旧密码");
		setting_old_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_new_password=findViewById(R.id.setting_new_password);
		setting_new_password_title=(TextView)setting_new_password.findViewById(R.id.title);
		setting_new_password_title.setText("新密码");
		setting_new_password_editer=(EditText)setting_new_password.findViewById(R.id.editer);
		setting_new_password_editer.setHint("输入新密码");
		setting_new_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_new_password_re=findViewById(R.id.setting_new_password_re);
		setting_new_password_re_title=(TextView)setting_new_password_re.findViewById(R.id.title);
		setting_new_password_re_title.setText("重复新密码");
		setting_new_password_re_editer=(EditText)setting_new_password_re.findViewById(R.id.editer);
		setting_new_password_re_editer.setHint("重复新密码");
		setting_new_password_re_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

	private void editPassWord(){
		final String new_pass = setting_new_password_editer.getText().toString();
		String new_pass_re = setting_new_password_re_editer.getText().toString();
		final String old_pass = setting_old_password_editer.getText().toString();
		if(!new_pass.equals(new_pass_re)){
			T.show(getApplicationContext(), "两次输入的密码不同", Toast.LENGTH_LONG);
			return;
		}
		progreeDialog.show();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().editPassword(getApplicationContext()
						, HttpStringMD5.md5(old_pass), HttpStringMD5.md5(new_pass));
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if(result.startsWith(HttpJsonTool.SUCCESS)){
					T.show(getApplicationContext(), "修改密码成功", Toast.LENGTH_LONG);
					finishActivity();
				}else if(result.startsWith(HttpJsonTool.ERROR403)){
					ActivityTool.gotoLoginView(getApplicationContext());
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.show(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""), Toast.LENGTH_LONG);
				}
			}
		};
		task.execute();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finishActivity();
			break;
		case R.id.title_txt_right:
			editPassWord();
			break;
		default:
			break;
		}
	}
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在提交请求...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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

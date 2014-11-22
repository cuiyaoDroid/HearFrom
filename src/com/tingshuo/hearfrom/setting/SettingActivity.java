package com.tingshuo.hearfrom.setting;

import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.SelectHeadImageActivity;
import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.DensityUtil;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.SexTool;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.view.CustomTimeSeterDialog;
import com.tingshuo.tool.view.CustomTimeSeterDialog.TimeDialogListener;
import com.tingshuo.web.http.HttpJsonTool;

public class SettingActivity extends BaseSwipeBaceActivity {
	private View setting_commit;
	private TextView setting_commit_title;
	private TextView setting_commit_content;
	private View setting_head;
	private TextView setting_head_title;
	private View setting_nick_name;
	private TextView setting_nick_name_title;
	private EditText setting_nick_name_editer;
	private View setting_sex;
	private TextView setting_sex_title;
	private TextView setting_sex_content;
	private View setting_birthday;
	private TextView setting_birthday_title;
	private TextView setting_birthday_content;
	private View setting_clear;
	private TextView setting_clear_title;
	private View setting_logout;
	private TextView setting_logout_title;

	private View setting_account;
	private TextView setting_account_title;
	private TextView setting_account_content;

	private View setting_password;
	private TextView setting_password_title;
	private CustomTimeSeterDialog dialog;

	private View setting_filtclose;
	private TextView setting_filtclose_title;
	private CheckBox setting_filtclose_check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initContentView();
	}

	private boolean authentication;

	@Override
	protected void initContentView() {
		super.initContentView();
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("完成");
		title_middle.setText("设置");

		PreferenceUtils.getPrefBoolean(getApplicationContext(),
				PreferenceConstants.AUTHENTICATION, false);
		setting_commit = findViewById(R.id.setting_commit);
		setting_commit_title = (TextView) setting_commit
				.findViewById(R.id.title);
		setting_commit_title.setText("账户认证");
		setting_commit_content = (TextView) setting_commit
				.findViewById(R.id.content);
		setting_commit_content.setText(authentication ? "已认证" : "未认证");
		if (!authentication) {
			setting_commit.setOnClickListener(this);
		}

		setting_head = findViewById(R.id.setting_head);
		setting_head_title = (TextView) setting_head.findViewById(R.id.title);
		setting_head_title.setText("设置头像");
		setting_head.setOnClickListener(this);

		setting_nick_name = findViewById(R.id.setting_nick_name);
		setting_nick_name_title = (TextView) setting_nick_name
				.findViewById(R.id.title);
		setting_nick_name_title.setText("昵称");
		setting_nick_name_editer = (EditText) setting_nick_name
				.findViewById(R.id.editer);
		setting_nick_name_editer.setText("");

		setting_sex = findViewById(R.id.setting_sex);
		setting_sex_title = (TextView) setting_sex.findViewById(R.id.title);
		setting_sex_title.setText("性别");
		setting_sex_content = (TextView) setting_sex.findViewById(R.id.content);
		setting_sex_content.setText("男");
		setting_sex.setOnClickListener(this);

		setting_birthday = findViewById(R.id.setting_birthday);
		setting_birthday_title = (TextView) setting_birthday
				.findViewById(R.id.title);
		setting_birthday_title.setText("生日");
		setting_birthday_content = (TextView) setting_birthday
				.findViewById(R.id.content);
		setting_birthday_content.setText("1990年01月01日");
		setting_birthday_content.setHint("设置生日");
		setting_birthday.setOnClickListener(this);

		setting_clear = findViewById(R.id.setting_clear);
		setting_clear_title = (TextView) setting_clear.findViewById(R.id.title);
		setting_clear_title.setText("清除缓存");
		setting_clear.setOnClickListener(this);

		setting_logout = findViewById(R.id.setting_logout);
		setting_logout_title = (TextView) setting_logout
				.findViewById(R.id.title);
		setting_logout_title.setText("注销");
		setting_logout.setOnClickListener(this);

		setting_account = findViewById(R.id.setting_account);
		setting_account_title = (TextView) setting_account
				.findViewById(R.id.title);
		setting_account_title.setText("账号");
		setting_account_content = (TextView) setting_account
				.findViewById(R.id.content);
		setting_account_content.setText("1237817294");
		setting_account.findViewById(R.id.img_clickable).setVisibility(
				View.INVISIBLE);

		setting_password = findViewById(R.id.setting_password);
		setting_password_title = (TextView) setting_password
				.findViewById(R.id.title);
		setting_password_title.setText("修改密码");
		setting_password.setOnClickListener(this);

		boolean isFlitClose = PreferenceUtils.getPrefBoolean(
				getApplicationContext(),
				PreferenceConstants.SETTING_FLIT_CLOSE, true);
		setting_filtclose = findViewById(R.id.setting_flit_close);
		setting_filtclose_title = (TextView) setting_filtclose
				.findViewById(R.id.title);
		setting_filtclose_title.setText("开启滑动关闭手势");
		setting_filtclose_check = (CheckBox) setting_filtclose
				.findViewById(R.id.checker);
		setting_filtclose_check.setChecked(isFlitClose);
		setting_filtclose_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						PreferenceUtils.setPrefBoolean(getApplicationContext(),
								PreferenceConstants.SETTING_FLIT_CLOSE,
								isChecked);
						HearFromApp.isFlitClose = isChecked;
						setSwipeBackEnable(isChecked);
					}
				});
		setting_filtclose.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshInfo();
	}

	private void refreshInfo() {
		UserInfoHelper helper = new UserInfoHelper(getApplicationContext());
		UserInfoHolder holder = helper.selectData_Id(HearFromApp.user_id);
		helper.close();
		if (holder == null) {
			return;
		}
		setting_nick_name_editer.setText(holder.getNickname());
		setting_account_content.setText(holder.getAccount());
		setting_sex_content.setText(holder.getSex() == SexTool.FEMELA ? "女"
				: "男");
		setting_birthday_content
				.setText(holder.getBrithday().equals("null") ? "1990年01月01日" : holder
						.getBrithday());

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void changuserInfo(final String nickname, final int sex,
			final String phonenum,final String birthday) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().changuserInfo(
						getApplicationContext(), nickname, sex, phonenum,birthday);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.startsWith(HttpJsonTool.SUCCESS)) {
					refreshInfo();
				}
			}
		};
		task.execute();
	}

	public static final int SETTINGHEAD = 10;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finish();
			break;
		case R.id.title_txt_right:
			String nick_name=setting_nick_name_editer.getText().toString();
			int sex=setting_sex_content.getText().equals("男")?SexTool.MELA:SexTool.FEMELA;
			String birthday = setting_birthday_content.getText().toString();
			changuserInfo(nick_name,sex,null,birthday);
			finish();
			break;
		case R.id.setting_commit:
			Intent intent = new Intent(getApplicationContext(),
					authenticationActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.setting_head:
			intent = new Intent(getApplicationContext(),
					SelectHeadImageActivity.class);
			startActivityForResult(intent, SETTINGHEAD);
			break;
		case R.id.setting_nick_name:
			//ShowEditDialog(setting_nick_name_editer.getText().toString());
			break;
		case R.id.setting_sex:
			ShowDialog(new String[] { "男", "女" }, setting_sex_content);
			break;
		case R.id.setting_birthday:
			if (dialog == null) {
				dialog = new CustomTimeSeterDialog(SettingActivity.this,
						R.style.customDialog, R.layout.dialog_time_seter,
						System.currentTimeMillis());
				dialog.setTimeDialogListener(new TimeDialogListener() {
					@Override
					public void getTimeInMill(long time) {
						// TODO Auto-generated method stub
						SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
						setting_birthday_content.setText(format.format(time));
					}
				});
			}
			dialog.show();
			break;
		case R.id.setting_clear:
			break;
		case R.id.setting_logout:
			break;
		case R.id.setting_password:
			intent = new Intent(getApplicationContext(),
					editPasswordActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		case R.id.setting_flit_close:
			setting_filtclose_check.setChecked(!setting_filtclose_check
					.isChecked());
			break;
		default:
			break;
		}
	}

	private void uploadHead(String path) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case SETTINGHEAD:
			if (data != null) {
				String path = data.getStringExtra("path");
				uploadHead(path);
			}
			break;

		default:
			break;
		}
	}

	private void ShowDialog(final String[] content, final TextView textview) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setItems(content, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				textview.setText(content[which]);
			}
		});
		builder.create().show();
	}

	/*private void ShowEditDialog(final String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		final EditText edit = new EditText(SettingActivity.this);
		builder.setTitle("修改昵称");
		builder.setView(edit);
		int padding=DensityUtil.dip2px(getApplicationContext(), 10);
		edit.setPadding(padding, padding*3, padding, padding*3);
		edit.setText(content);
		
		edit.setBackgroundResource(android.R.color.transparent);
		CharSequence text = edit.getText();
		if (text instanceof Spannable) {
		    Spannable spanText = (Spannable)text;
		    Selection.setSelection(spanText, text.length());
		}
		edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String edited_content=edit.getText().toString().trim();
				if(!content.equals(edited_content)){
					setting_nick_name_editer.setText(edited_content);
					changuserInfo(edited_content,-1,null);
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				edit.requestFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);     

				imm.toggleSoftInputFromWindow(
						edit.getWindowToken(), 0,
						InputMethodManager.HIDE_NOT_ALWAYS);  
			}
		});
		dialog.show();
	}*/
}

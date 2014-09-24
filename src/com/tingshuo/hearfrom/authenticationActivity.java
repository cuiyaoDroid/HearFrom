package com.tingshuo.hearfrom;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class authenticationActivity extends BaseAcivity{
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
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		titleLeft.setVisibility(View.VISIBLE);
		titleLeft.setText("∑µªÿ");
		title_middle.setText("»œ÷§");
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("ÕÍ≥…");
		
		setting_account=findViewById(R.id.setting_account);
		setting_account_title=(TextView)setting_account.findViewById(R.id.title);
		setting_account_title.setText("’À∫≈");
		setting_account_editer=(EditText)setting_account.findViewById(R.id.editer);
		setting_account_editer.setHint(" ‰»Î’À∫≈");
		
		setting_password=findViewById(R.id.setting_password);
		setting_password_title=(TextView)setting_password.findViewById(R.id.title);
		setting_password_title.setText("√‹¬Î");
		setting_password_editer=(EditText)setting_password.findViewById(R.id.editer);
		setting_password_editer.setHint(" ‰»Î√‹¬Î");
		setting_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_password_re=findViewById(R.id.setting_password_re);
		setting_password_re_title=(TextView)setting_password_re.findViewById(R.id.title);
		setting_password_re_title.setText("÷ÿ∏¥√‹¬Î");
		setting_password_re_editer=(EditText)setting_password_re.findViewById(R.id.editer);
		setting_password_re_editer.setHint("÷ÿ∏¥√‹¬Î");
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_left:
			finishActivity();
			break;
		case R.id.title_txt_right:
			finishActivity();
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

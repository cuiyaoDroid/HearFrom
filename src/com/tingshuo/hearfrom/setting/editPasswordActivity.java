package com.tingshuo.hearfrom.setting;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;

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
		titleback.setVisibility(View.VISIBLE);
		title_middle.setText("–ﬁ∏ƒ√‹¬Î");
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("ÕÍ≥…");
		
		setting_old_password=findViewById(R.id.setting_old_password);
		setting_old_password_title=(TextView)setting_old_password.findViewById(R.id.title);
		setting_old_password_title.setText("æ…√‹¬Î");
		setting_old_password_editer=(EditText)setting_old_password.findViewById(R.id.editer);
		setting_old_password_editer.setHint(" ‰»Îæ…√‹¬Î");
		setting_old_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_new_password=findViewById(R.id.setting_new_password);
		setting_new_password_title=(TextView)setting_new_password.findViewById(R.id.title);
		setting_new_password_title.setText("–¬√‹¬Î");
		setting_new_password_editer=(EditText)setting_new_password.findViewById(R.id.editer);
		setting_new_password_editer.setHint(" ‰»Î–¬√‹¬Î");
		setting_new_password_editer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		setting_new_password_re=findViewById(R.id.setting_new_password_re);
		setting_new_password_re_title=(TextView)setting_new_password_re.findViewById(R.id.title);
		setting_new_password_re_title.setText("÷ÿ∏¥–¬√‹¬Î");
		setting_new_password_re_editer=(EditText)setting_new_password_re.findViewById(R.id.editer);
		setting_new_password_re_editer.setHint("÷ÿ∏¥–¬√‹¬Î");
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
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

package com.tingshuo.hearfrom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tingshuo.hearfrom.screen.ScreenChangeListener;
import com.tingshuo.hearfrom.screen.leaderFirstScreen;
import com.tingshuo.hearfrom.screen.leaderFourthScreen;
import com.tingshuo.hearfrom.screen.leaderSecondScreen;
import com.tingshuo.hearfrom.screen.leaderThridScreen;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;

public class RegisterFastActivity extends FragmentActivity implements
		ScreenChangeListener {
	private FragmentManager fragmentManager;
	private leaderFirstScreen leaderfirst_screen;
	private leaderSecondScreen leadersecond_screen;
	private leaderThridScreen leaderthrid_screen;
	private leaderFourthScreen leaderfourth_screen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_fast);
		boolean first_use=PreferenceUtils.getPrefBoolean(getApplicationContext()
				, PreferenceConstants.FIRST_USE, true);
		if(!first_use){
			if(HearFromApp.token.length()>0){
				startMainPage();
				return;
			}else{
				startLoginPage();
				return;
			}
		}
		fragmentManager = getSupportFragmentManager();
		changeToScreen(1);
	}

	private void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		if(index!=1){
			transaction.setCustomAnimations(
	                R.anim.slide_right_in,
	                R.anim.slide_left_out,
	                R.anim.slide_left_in,
	                R.anim.slide_right_out);
		}
		switch (index) {
		case 1:
			
			// 当点击了消息tab时，改变控件的图片和文字颜色
			if (leaderfirst_screen == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				leaderfirst_screen = new leaderFirstScreen();
				leaderfirst_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderfirst_screen);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.replace(R.id.content, leaderfirst_screen);
			}
			break;
		case 2:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			if (leadersecond_screen == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				leadersecond_screen = new leaderSecondScreen();
				leadersecond_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leadersecond_screen);
				transaction.addToBackStack(null);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.replace(R.id.content, leadersecond_screen);
				transaction.addToBackStack(null);
			}
			break;
		case 3:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			if (leaderthrid_screen == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				leaderthrid_screen = new leaderThridScreen();
				leaderthrid_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderthrid_screen);
				transaction.addToBackStack(null);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.replace(R.id.content, leaderthrid_screen);
				transaction.addToBackStack(null);
			}
			break;
		case 4:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			if (leaderfourth_screen == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				leaderfourth_screen = new leaderFourthScreen();
				leaderfourth_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderfourth_screen);
				transaction.addToBackStack(null);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.replace(R.id.content, leaderfourth_screen);
				transaction.addToBackStack(null);
			}
			break;
		default:
			break;
		}

		transaction.commit();
	}

	@Override
	public void changeToScreen(int page) {
		// TODO Auto-generated method stub
		setTabSelection(page);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (fragmentManager.getBackStackEntryCount() != 0) {
			fragmentManager.popBackStack();
			return;
		}
		super.onBackPressed();
	}
	@Override
	public void startMainPage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RegisterFastActivity.this,
				HearFromTabMainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
	private void startLoginPage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RegisterFastActivity.this,
				LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
}

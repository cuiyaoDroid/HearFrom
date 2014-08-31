package com.tingshuo.hearfrom;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.tingshuo.hearfrom.screen.ScreenChangeListener;
import com.tingshuo.hearfrom.screen.leaderFirstScreen;
import com.tingshuo.hearfrom.screen.leaderSecondScreen;
public class RegisterFastActivity extends SherlockActivity implements ScreenChangeListener{
	private FragmentManager fragmentManager;
	private leaderFirstScreen leaderfirst_screen;
	private leaderSecondScreen leadersecond_screen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_fast);
		getSupportActionBar().hide();
		fragmentManager = getFragmentManager();
		changeToScreen(1);
	}
	private int current_index=1;
	private void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		
		//hideFragments(transaction);
		switch (index) {
		case 1:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			if (leaderfirst_screen == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				leaderfirst_screen = new leaderFirstScreen();
				leaderfirst_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content,leaderfirst_screen);  
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.replace(R.id.content,leaderfirst_screen);  
			}
			break;
		case 2:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			if (leadersecond_screen == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				leadersecond_screen = new leaderSecondScreen();
				leadersecond_screen.setScreenChangeListener(this);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				transaction.replace(R.id.content,leadersecond_screen);  
				transaction.addToBackStack(null); 
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				transaction.replace(R.id.content,leadersecond_screen);  
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
		if(fragmentManager.getBackStackEntryCount()!=0){
			fragmentManager.popBackStack();
			return;
		}
		super.onBackPressed();
	}
}

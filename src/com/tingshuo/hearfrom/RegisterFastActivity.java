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
		fragmentManager = getSupportFragmentManager();
		changeToScreen(1);
	}

	private void setTabSelection(int index) {
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		if(index!=1){
			transaction.setCustomAnimations(
	                R.anim.slide_right_in,
	                R.anim.slide_left_out,
	                R.anim.slide_left_in,
	                R.anim.slide_right_out);
		}
		switch (index) {
		case 1:
			
			// ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leaderfirst_screen == null) {
				// ���MessageFragmentΪ�գ��򴴽�һ������ӵ�������
				leaderfirst_screen = new leaderFirstScreen();
				leaderfirst_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderfirst_screen);
			} else {
				// ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.replace(R.id.content, leaderfirst_screen);
			}
			break;
		case 2:
			// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leadersecond_screen == null) {
				// ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������
				leadersecond_screen = new leaderSecondScreen();
				leadersecond_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leadersecond_screen);
				transaction.addToBackStack(null);
			} else {
				// ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.replace(R.id.content, leadersecond_screen);
				transaction.addToBackStack(null);
			}
			break;
		case 3:
			// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leaderthrid_screen == null) {
				// ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������
				leaderthrid_screen = new leaderThridScreen();
				leaderthrid_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderthrid_screen);
				transaction.addToBackStack(null);
			} else {
				// ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.replace(R.id.content, leaderthrid_screen);
				transaction.addToBackStack(null);
			}
			break;
		case 4:
			// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leaderfourth_screen == null) {
				// ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������
				leaderfourth_screen = new leaderFourthScreen();
				leaderfourth_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content, leaderfourth_screen);
				transaction.addToBackStack(null);
			} else {
				// ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
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
				HearFromMainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
}

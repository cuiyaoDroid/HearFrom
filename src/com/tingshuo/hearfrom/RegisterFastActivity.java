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
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		
		//hideFragments(transaction);
		switch (index) {
		case 1:
			// ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leaderfirst_screen == null) {
				// ���MessageFragmentΪ�գ��򴴽�һ������ӵ�������
				leaderfirst_screen = new leaderFirstScreen();
				leaderfirst_screen.setScreenChangeListener(this);
				transaction.replace(R.id.content,leaderfirst_screen);  
			} else {
				// ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.replace(R.id.content,leaderfirst_screen);  
			}
			break;
		case 2:
			// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
			if (leadersecond_screen == null) {
				// ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������
				leadersecond_screen = new leaderSecondScreen();
				leadersecond_screen.setScreenChangeListener(this);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				transaction.replace(R.id.content,leadersecond_screen);  
				transaction.addToBackStack(null); 
			} else {
				// ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
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

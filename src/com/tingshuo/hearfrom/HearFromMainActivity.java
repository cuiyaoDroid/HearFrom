package com.tingshuo.hearfrom;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import cn.jpush.android.api.JPushInterface;

import com.actionbarsherlock.app.SherlockActivity;
import com.tingshuo.hearfrom.screen.leaderFirstScreen;
import com.tingshuo.hearfrom.screen.leaderSecondScreen;
import com.tingshuo.service.IConnectionStatusCallback;
import com.tingshuo.service.XXService;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.T;
import com.tingshuo.tool.view.PinnedHeaderListView;
import com.tingshuo.tool.view.adapter.TestAdapter;

@SuppressLint("NewApi") 
public class HearFromMainActivity extends SherlockActivity implements
IConnectionStatusCallback {
	private TestAdapter adapter;
	private PinnedHeaderListView listView;
	public static final String LOGIN_ACTION = "com.tingshuo.hearfrom.action.LOGIN";
	public static final String INTENT_EXTRA_USERNAME = HearFromMainActivity.class
			.getName() + ".username";
	private XXService mXxService;
	private FragmentManager fragmentManager;
	private leaderFirstScreen leaderfirst_screen;
	private leaderSecondScreen leadersecond_screen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hear_from_main);
		fragmentManager = getFragmentManager();  
		
		//requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		adapter = new TestAdapter(getLayoutInflater());
		((Button)findViewById(R.id.test_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				splitAndSaveServer("cuiyao@www.wangjiaqi666.cn");
				if (mXxService != null) {
					mXxService.Login("cuiyao", "123456");
				}
			}
		});
		listView = (PinnedHeaderListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(adapter);
		listView.setPinnedHeaderView(getLayoutInflater().inflate(
				R.layout.cell_listheader, listView, false));
		startService(new Intent(HearFromMainActivity.this, XXService.class));
		bindXMPPService();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				splitAndSaveServer("cuiyao@www.wangjiaqi666.cn");
//				if (mXxService != null) {
//					mXxService.Login("cuiyao", "123456");
//				}
			}
		});
		
	}
	private String splitAndSaveServer(String account) {
		if (!account.contains("@"))
			return account;
		String customServer = PreferenceUtils.getPrefString(this,
				PreferenceConstants.CUSTOM_SERVER, "");
		String[] res = account.split("@");
		String userName = res[0];
		String server = res[1];
		// check for gmail.com and other google hosted jabber accounts
		if ("gmail.com".equals(server) || "googlemail.com".equals(server)
				|| PreferenceConstants.GMAIL_SERVER.equals(customServer)) {
			// work around for gmail's incompatible jabber implementation:
			// send the whole JID as the login, connect to talk.google.com
			userName = account;

		}
		PreferenceUtils.setPrefString(this, PreferenceConstants.Server, server);
		return userName;
	}
	private void setTabSelection(int index) {  
        // 开启一个Fragment事务  
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
        hideFragments(transaction);  
        switch (index) {  
        case 1:  
            // 当点击了消息tab时，改变控件的图片和文字颜色  
            if (leaderfirst_screen == null) {  
                // 如果MessageFragment为空，则创建一个并添加到界面上  
            	leaderfirst_screen = new leaderFirstScreen();  
                transaction.add(R.id.content, leaderfirst_screen);  
            } else {  
                // 如果MessageFragment不为空，则直接将它显示出来  
                transaction.show(leaderfirst_screen);  
            }  
            break;  
        case 2:  
            // 当点击了联系人tab时，改变控件的图片和文字颜色  
            if (leadersecond_screen == null) {  
                // 如果ContactsFragment为空，则创建一个并添加到界面上  
            	leadersecond_screen = new leaderSecondScreen();  
                transaction.add(R.id.content, leadersecond_screen);  
            } else {  
                // 如果ContactsFragment不为空，则直接将它显示出来  
                transaction.show(leadersecond_screen);  
            }  
            break;  
        default:  
            break;  
        }  
        transaction.commit();  
    }  
  
  
    /** 
     * 将所有的Fragment都置为隐藏状态。 
     *  
     * @param transaction 
     *            用于对Fragment执行操作的事务 
     */  
    private void hideFragments(FragmentTransaction transaction) {  
        if (leaderfirst_screen != null) {  
            transaction.hide(leaderfirst_screen);  
        }  
        if (leadersecond_screen != null) {  
            transaction.hide(leadersecond_screen);  
        }  
    }  
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hear_from_main, menu);
		return true;
	}*/
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.hear_from_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	};
	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXxService = ((XXService.XXBinder) service).getService();
			mXxService.registerConnectionStatusCallback(HearFromMainActivity.this);
			// 开始连接xmpp服务器
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}

	};
	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		// TODO Auto-generated method stub
		if (connectedState == XXService.CONNECTED) {
			T.showLong(this, "登录成功");
		} else if (connectedState == XXService.DISCONNECTED)
			T.showLong(this, "登录失败"+ reason);
	}
	private void unbindXMPPService() {
		try {
			unbindService(mServiceConnection);
			L.i(HearFromMainActivity.class, "[SERVICE] Unbind");
		} catch (IllegalArgumentException e) {
			L.e(HearFromMainActivity.class, "Service wasn't bound!");
		}
	}

	private void bindXMPPService() {
		L.i(HearFromMainActivity.class, "[SERVICE] bind");
		Intent mServiceIntent = new Intent(this, XXService.class);
		mServiceIntent.setAction(LOGIN_ACTION);
		bindService(mServiceIntent, mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindXMPPService();
	}
}

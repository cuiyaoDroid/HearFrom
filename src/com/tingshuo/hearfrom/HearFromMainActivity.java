package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.tingshuo.service.IConnectionStatusCallback;
import com.tingshuo.service.XXService;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.T;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshListView;
@SuppressLint("NewApi") 
public class HearFromMainActivity extends SherlockActivity implements
IConnectionStatusCallback {
	private SimpleAdapter adapter;
	private ArrayList<Map<String,Object>>listdata;
	private PullToRefreshListView listView;
	public static final String LOGIN_ACTION = "com.tingshuo.hearfrom.action.LOGIN";
	public static final String INTENT_EXTRA_USERNAME = HearFromMainActivity.class
			.getName() + ".username";
	
	private String[]textUser={"崔垚","李杨","李晨阳","赵雍","王嘉琦","郭建民"};
	private String[]textUserURL={"cuiyao","liyang","lichenyang","zhaorong","wangjiaqi","guojianmin"};
	private XXService mXxService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hear_from_main);
		//requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		
		listdata=new ArrayList<Map<String,Object>>();
		for(int i=0;i<textUser.length;i++){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("content", textUser[i]);
			data.put("url", textUserURL[i]);
			listdata.add(data);
		}
		
		adapter = new SimpleAdapter(getApplicationContext(), listdata, R.layout.cell_mainhear_list
				, new String[]{"content"}, new int[]{R.id.content_txt});
		
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}

		});
		listView.setAdapter(adapter);
		startService(new Intent(HearFromMainActivity.this, XXService.class));
		bindXMPPService();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String usrUrl=(String) listdata.get(arg2-1).get("url");
				String name=(String) listdata.get(arg2-1).get("content");
				startChatActivity(usrUrl+"@iz25b16f012z",usrUrl,name);
			}
		});
		
	}
	private void save2Preferences() {
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
				"cuiyao");// 帐号是一直保存的
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					"123456");
	}
	private void startChatActivity(String userJid, String userName,String nickname) {
		Intent chatIntent = new Intent(HearFromMainActivity.this, ChatActivity.class);
		Uri userNameUri = Uri.parse(userJid);
		chatIntent.setData(userNameUri);
		chatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME, userName);
		chatIntent.putExtra("nickname", nickname);
		startActivity(chatIntent);
	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			listView.onRefreshComplete();

			super.onPostExecute(result);
		}
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
//		JPushInterface.onResume(this);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
//		JPushInterface.onPause(this);
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
			save2Preferences();
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

package com.tingshuo.service;



import java.util.HashSet;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.tingshuo.exception.XXException;
import com.tingshuo.hearfrom.HearFromMainActivity;
import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.XXBroadcastReceiver;
import com.tingshuo.hearfrom.XXBroadcastReceiver.EventHandler;
import com.tingshuo.smack.SmackImpl;
import com.tingshuo.tool.L;
import com.tingshuo.tool.NetUtil;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.T;


public class XXService extends BaseService implements EventHandler {
	public static final int CONNECTED = 0;
	public static final int DISCONNECTED = -1;
	public static final int CONNECTING = 1;
	public static final String PONG_TIMEOUT = "pong timeout";// ���ӳ�ʱ
	public static final String NETWORK_ERROR = "network error";// �������
	public static final String LOGOUT = "logout";// �ֶ��˳�
	public static final String LOGIN_FAILED = "login failed";// ��¼ʧ��
	public static final String DISCONNECTED_WITHOUT_WARNING = "disconnected without warning";// û�о���ĶϿ�����

	private IBinder mBinder = new XXBinder();
	private IConnectionStatusCallback mConnectionStatusCallback;
	private SmackImpl mSmackable;
	private Thread mConnectingThread;
	private Handler mMainHandler = new Handler();

	private boolean mIsFirstLoginAction;
	// �Զ����� start
	private static final int RECONNECT_AFTER = 5;
	private static final int RECONNECT_MAXIMUM = 10 * 60;// �������ʱ����
	private static final String RECONNECT_ALARM = "com.tingshuo.hearfrom.RECONNECT_ALARM";
	// private boolean mIsNeedReConnection = false; // �Ƿ���Ҫ����
	private int mConnectedState = DISCONNECTED; // �Ƿ��Ѿ�����
	private int mReconnectTimeout = RECONNECT_AFTER;
	private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
	private PendingIntent mPAlarmIntent;
	private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();
	// �Զ����� end
	private ActivityManager mActivityManager;
	private HashSet<String> mIsBoundTo = new HashSet<String>();

	/**
	 * ע��ע������������ʱ����״̬�仯�ص�
	 * 
	 * @param cb
	 */
	public void registerConnectionStatusCallback(IConnectionStatusCallback cb) {
		mConnectionStatusCallback = cb;
	}

	public void unRegisterConnectionStatusCallback() {
		mConnectionStatusCallback = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		L.i(XXService.class, "[SERVICE] onBind");
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
				&& TextUtils.equals(action, HearFromMainActivity.LOGIN_ACTION)) {
			mIsFirstLoginAction = true;
		} else {
			mIsFirstLoginAction = false;
		}
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
		String action = intent.getAction();
		if (!TextUtils.isEmpty(action)
				&& TextUtils.equals(action, HearFromMainActivity.LOGIN_ACTION)) {
			mIsFirstLoginAction = true;
		} else {
			mIsFirstLoginAction = false;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.remove(chatPartner);
		}
		return true;
	}

	public class XXBinder extends Binder {
		public XXService getService() {
			return XXService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		XXBroadcastReceiver.mListeners.add(this);
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		mPAlarmIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		registerReceiver(mAlarmReceiver, new IntentFilter(RECONNECT_ALARM));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null
				&& intent.getAction() != null
				&& TextUtils.equals(intent.getAction(),
						XXBroadcastReceiver.BOOT_COMPLETED_ACTION)) {
			String account = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.ACCOUNT, "");
			String password = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.PASSWORD, "");
			if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password))
				Login(account, password);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		XXBroadcastReceiver.mListeners.remove(this);
		((AlarmManager) getSystemService(Context.ALARM_SERVICE))
				.cancel(mPAlarmIntent);// ȡ����������
		unregisterReceiver(mAlarmReceiver);// ע���㲥����
		logout();
	}

	// ��¼
	public void Login(final String account, final String password) {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (mConnectingThread != null) {
			L.i("a connection is still goign on!");
			return;
		}
		mConnectingThread = new Thread() {
			@Override
			public void run() {
				try {
					postConnecting();
					mSmackable = new SmackImpl(XXService.this);
					if (mSmackable.login(account, password)) {
						// ��½�ɹ�
						L.i("postConnectionScuessed");
						postConnectionScuessed();
					} else {
						// ��½ʧ��
						L.i("postConnectionScuessed");
						postConnectionFailed(LOGIN_FAILED);
					}
				} catch (XXException e) {
					String message = e.getLocalizedMessage();
					// ��½ʧ��
					if (e.getCause() != null)
						message += "\n" + e.getCause().getLocalizedMessage();
					postConnectionFailed(message);
					L.i(XXService.class, "YaximXMPPException in doConnect():");
					e.printStackTrace();
				} finally {
					if (mConnectingThread != null)
						synchronized (mConnectingThread) {
							mConnectingThread = null;
						}
				}
			}

		};
		mConnectingThread.start();
	}

	// �˳�
	public boolean logout() {
		// mIsNeedReConnection = false;// �ֶ��˳��Ͳ���Ҫ����������
		boolean isLogout = false;
		if (mConnectingThread != null) {
			synchronized (mConnectingThread) {
				try {
					mConnectingThread.interrupt();
					mConnectingThread.join(50);
				} catch (InterruptedException e) {
					L.e("doDisconnect: failed catching connecting thread");
				} finally {
					mConnectingThread = null;
				}
			}
		}
		if (mSmackable != null) {
			isLogout = mSmackable.logout();
			mSmackable = null;
		}
		connectionFailed(LOGOUT);// �ֶ��˳�
		return isLogout;
	}

	// ������Ϣ
	public void sendMessage(String user, String message) {
		if (mSmackable != null)
			mSmackable.sendMessage(user, message);
		else
			SmackImpl.sendOfflineMessage(getContentResolver(), user, message);
	}

	// �Ƿ������Ϸ�����
	public boolean isAuthenticated() {
		if (mSmackable != null) {
			return mSmackable.isAuthenticated();
		}

		return false;
	}

	// ���֪ͨ��
	public void clearNotifications(String Jid) {
		clearNotification(Jid);
	}

	/**
	 * ��UI�߳�����ʧ�ܷ���
	 * 
	 * @param reason
	 */
	public void postConnectionFailed(final String reason) {
		mMainHandler.post(new Runnable() {
			public void run() {
				connectionFailed(reason);
			}
		});
	}

	// ��������״̬
	public void setStatusFromConfig() {
		mSmackable.setStatusFromConfig();
	}

	// ������ϵ��
	public void addRosterItem(String user, String alias, String group) {
		try {
			mSmackable.addRosterItem(user, alias, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in addRosterItem(): " + e.getMessage());
		}
	}

	// ��������
	public void addRosterGroup(String group) {
		mSmackable.addRosterGroup(group);
	}

	// ɾ����ϵ��
	public void removeRosterItem(String user) {
		try {
			mSmackable.removeRosterItem(user);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in removeRosterItem(): " + e.getMessage());
		}
	}

	// ����ϵ���ƶ���������
	public void moveRosterItemToGroup(String user, String group) {
		try {
			mSmackable.moveRosterItemToGroup(user, group);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in moveRosterItemToGroup(): " + e.getMessage());
		}
	}

	// ��������ϵ��
	public void renameRosterItem(String user, String newName) {
		try {
			mSmackable.renameRosterItem(user, newName);
		} catch (XXException e) {
			T.showShort(this, e.getMessage());
			L.e("exception in renameRosterItem(): " + e.getMessage());
		}
	}

	// ��������
	public void renameRosterGroup(String group, String newGroup) {
		mSmackable.renameRosterGroup(group, newGroup);
	}

	/**
	 * UI�̷߳�������ʧ��
	 * 
	 * @param reason
	 */
	private void connectionFailed(String reason) {
		L.i(XXService.class, "connectionFailed: " + reason);
		mConnectedState = DISCONNECTED;// ���µ�ǰ����״̬
		if (TextUtils.equals(reason, LOGOUT)) {// ������ֶ��˳�
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}
		// �ص�
		if (mConnectionStatusCallback != null) {
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					reason);
			if (mIsFirstLoginAction)// ����ǵ�һ�ε�¼,�����¼ʧ��Ҳ����Ҫ����
				return;
		}

		// ����������ʱ,ֱ�ӷ���
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
			return;
		}

		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		// �ޱ�����ʺ�����ʱ��Ҳֱ�ӷ���
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
			L.d("account = null || password = null");
			return;
		}
		// ��������ֶ��˳�������Ҫ�������ӣ�������������
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.AUTO_RECONNECT, true)) {
			L.d("connectionFailed(): registering reconnect in "
					+ mReconnectTimeout + "s");
			((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(
					AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
							+ mReconnectTimeout * 1000, mPAlarmIntent);
			mReconnectTimeout = mReconnectTimeout * 2;
			if (mReconnectTimeout > RECONNECT_MAXIMUM)
				mReconnectTimeout = RECONNECT_MAXIMUM;
		} else {
			((AlarmManager) getSystemService(Context.ALARM_SERVICE))
					.cancel(mPAlarmIntent);
		}

	}

	private void postConnectionScuessed() {
		mMainHandler.post(new Runnable() {
			public void run() {
				connectionScuessed();
			}

		});
	}

	private void connectionScuessed() {
		mConnectedState = CONNECTED;// �Ѿ�������
		mReconnectTimeout = RECONNECT_AFTER;// ����������ʱ��

		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	// �����У�֪ͨ�����߳���һЩ����
	private void postConnecting() {
		// TODO Auto-generated method stub
		mMainHandler.post(new Runnable() {
			public void run() {
				connecting();
			}
		});
	}

	private void connecting() {
		// TODO Auto-generated method stub
		mConnectedState = CONNECTING;// ������
		if (mConnectionStatusCallback != null)
			mConnectionStatusCallback.connectionStatusChanged(mConnectedState,
					"");
	}

	// �յ�����Ϣ
	public void newMessage(final String from, final String message) {
		mMainHandler.post(new Runnable() {
			public void run() {
				if (!PreferenceUtils.getPrefBoolean(XXService.this,
						PreferenceConstants.SCLIENTNOTIFY, false))
					MediaPlayer.create(XXService.this, R.raw.office).start();
				if (!isAppOnForeground())
					notifyClient(from, mSmackable.getNameForJID(from), message,
							!mIsBoundTo.contains(from));
				// T.showLong(XXService.this, from + ": " + message);

			}

		});
	}

	// ��ϵ�˸ı�
	public void rosterChanged() {
		// gracefully handle^W ignore events after a disconnect
		if (mSmackable == null)
			return;
		if (mSmackable != null && !mSmackable.isAuthenticated()) {
			L.i("rosterChanged(): disconnected without warning");
			connectionFailed(DISCONNECTED_WITHOUT_WARNING);
		}
	}

	/**
	 * ����֪ͨ��
	 * 
	 * @param message
	 */
	public void updateServiceNotification(String message) {
		if (!PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.FOREGROUND, true))
			return;
		String title = PreferenceUtils.getPrefString(this,
				PreferenceConstants.ACCOUNT, "");
		Notification n = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

		Intent notificationIntent = new Intent(this, HearFromMainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		n.contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(this, title, message, n.contentIntent);
		startForeground(SERVICE_NOTIFICATION, n);
	}


	public boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}

		// List<RunningAppProcessInfo> appProcesses = mActivityManager
		// .getRunningAppProcesses();
		// if (appProcesses == null)
		// return false;
		// for (RunningAppProcessInfo appProcess : appProcesses) {
		// // L.i("liweiping", appProcess.processName);
		// // The name of the process that this object is associated with.
		// if (appProcess.processName.equals(mPackageName)
		// && appProcess.importance ==
		// RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
		// return true;
		// }
		// }
		return false;
	}

	// �Զ������㲥
	private class ReconnectAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
			L.d("Alarm received.");
			if (!PreferenceUtils.getPrefBoolean(XXService.this,
					PreferenceConstants.AUTO_RECONNECT, true)) {
				return;
			}
			if (mConnectedState != DISCONNECTED) {
				L.d("Reconnect attempt aborted: we are connected again!");
				return;
			}
			String account = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.ACCOUNT, "");
			String password = PreferenceUtils.getPrefString(XXService.this,
					PreferenceConstants.PASSWORD, "");
			if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
				L.d("account = null || password = null");
				return;
			}
			Login(account, password);
		}
	}

	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {// ���������Ͽ�����������
			connectionFailed(NETWORK_ERROR);
			return;
		}
		if (isAuthenticated())// ����Ѿ������ϣ�ֱ�ӷ���
			return;
		String account = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.ACCOUNT, "");
		String password = PreferenceUtils.getPrefString(XXService.this,
				PreferenceConstants.PASSWORD, "");
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password))// ���û���ʺţ�Ҳֱ�ӷ���
			return;
		if (!PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.AUTO_RECONNECT, true))// ����Ҫ����
			return;
		Login(account, password);// ����
	}
}

package com.tingshuo.service;

import java.util.List;

import io.rong.imlib.RongIMClient.Message;
import io.rong.message.TextMessage;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.tingshuo.hearfrom.FriendsRequestListActivity;
import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.hearfrom.HearFromTabMainActivity;
import com.tingshuo.tool.db.ChatMessageHelper;
import com.tingshuo.tool.db.ChatMessageHolder;
import com.tingshuo.tool.db.CurMessageListHelper;
import com.tingshuo.tool.db.CurMessageListHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.db.lock;
import com.tingshuo.tool.im.RongIMTool;
import com.tingshuo.tool.im.RongIMTool.ConnectListener;
import com.tingshuo.tool.im.RongIMTool.MessageReceivelistener;
import com.tingshuo.tool.im.RongMessageTYPE;
import com.tingshuo.web.http.HttpJsonTool;

public class NotificationService extends BaseRongYunService implements
		MessageReceivelistener, ConnectListener {
	private ActivityManager mActivityManager;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		initRongSDK();
	}

	private void initRongSDK() {
		resistListener();
		if(HearFromApp.user_id==-1){
			return;
		}
		UserInfoHelper helper =new UserInfoHelper(getApplicationContext());
		UserInfoHolder holder=helper.selectData_Id(HearFromApp.user_id);
		helper.close();
		if(holder==null){
			return;
		}
		RongIMTool.getInstance().connect(String.valueOf(HearFromApp.user_id)
				, holder.getNickname(), holder.getHead());
	}

	private void resistListener() {
		RongIMTool.getInstance().addConnectListener(this);
		RongIMTool.getInstance().addMessageReceivelistener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unResistlistener();
	}

	private void unResistlistener() {
		RongIMTool.getInstance().removeConnectListener(this);
		RongIMTool.getInstance().removeMessageReceivelistener(this);
	}

	@Override
	public void onMessageReceive(Message msg, int status) {
		// TODO Auto-generated method stub
		
		TextMessage text_msg=(TextMessage) msg.getContent();
		boolean isText=text_msg.getExtra().equals(RongMessageTYPE.MESSAGE_TYPE_TEXT);
		boolean isAddMessage=text_msg.getExtra().equals(RongMessageTYPE.MESSAGE_TYPE_ADDFRIENDS);
		if(isText){
			ChatMessageHelper helper=new ChatMessageHelper(getApplicationContext());
			CurMessageListHelper curhelper=new CurMessageListHelper(getApplicationContext());
			ChatMessageHolder holder=new ChatMessageHolder(-1, HearFromApp.user_id
					, msg.getReceivedTime(), msg.getSenderUserId(), String.valueOf(HearFromApp.user_id)
					, text_msg.getExtra(), text_msg.getContent(), ChatMessageHolder.STATUS_RECIVED);
			int i_sender=0;
			try{
				i_sender = Integer.parseInt(msg.getSenderUserId());
				UserInfoHelper userHelper=new UserInfoHelper(getApplicationContext());
				UserInfoHolder userHolder=userHelper.selectData_Id(i_sender);
				userHelper.close();
				if(userHolder==null){
					getUserInfo(i_sender);
				}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			CurMessageListHolder curholder=new CurMessageListHolder(i_sender, HearFromApp.user_id
					, System.currentTimeMillis(), msg.getSenderUserId(), String.valueOf(HearFromApp.user_id)
					, text_msg.getExtra(), text_msg.getContent(), ChatMessageHolder.STATUS_RECIVED);
			synchronized (lock.Lock) {
				helper.insert(holder, helper.getWritableDatabase());
				curhelper.insert(curholder, helper.getWritableDatabase());
			}
			helper.close();
			curhelper.close();
		}
		if(isAppOnForeground()&&!isAddMessage){
			return;
		}
		if(isText){
			Intent mNotificationIntent = new Intent(this, HearFromTabMainActivity.class);
			mNotificationIntent.putExtra(HearFromTabMainActivity.SELECT_TION, 1);
			notifyClient(msg.getSenderUserId(), "��˵����", text_msg.getContent(), mNotificationIntent);
		}else if(isAddMessage){
			Intent mNotificationIntent = new Intent(this, FriendsRequestListActivity.class);
			notifyClient(msg.getSenderUserId(), "��˵����", text_msg.getContent(), mNotificationIntent);
		}
	}
	private void getUserInfo(final int user_id){
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getUserInfo(getApplicationContext(), user_id);
			}
			
		};
		task.execute();
	}
	@Override
	public void connectComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectError() {
		// TODO Auto-generated method stub

	}
	public boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}
		return false;
	}
}

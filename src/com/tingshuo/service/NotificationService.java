package com.tingshuo.service;

import io.rong.imlib.RongIMClient.Message;
import io.rong.message.TextMessage;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.RongIMTool;
import com.tingshuo.tool.RongIMTool.ConnectListener;
import com.tingshuo.tool.RongIMTool.MessageReceivelistener;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;

public class NotificationService extends BaseRongYunService implements
		MessageReceivelistener, ConnectListener {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
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
		notifyClient(msg.getObjectName(), msg.getSenderUserId(), text_msg.getContent(), true);
	}

	@Override
	public void connectComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectError() {
		// TODO Auto-generated method stub

	}
}

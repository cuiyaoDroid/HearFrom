package com.tingshuo.tool.im;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.Message;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.message.TextMessage;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.L;
import com.tingshuo.web.http.ApiHttpClient;
import com.tingshuo.web.http.FormatType;
import com.tingshuo.web.http.SdkHttpResult;

public class RongIMTool implements RongIMClient.SendMessageCallback {

	private static RongIMTool mRongIMTool;

	public static RongIMTool getInstance() {
		if (mRongIMTool == null) {
			mRongIMTool = new RongIMTool();
		}
		return mRongIMTool;
	}

	public interface ConnectListener {
		void connectComplete();

		void connectError();
	}

	public interface MessageReceivelistener {
		void onMessageReceive(Message msg, int status);
	}

	public interface MessageStatusListener {
		public void onError(int arg0, ErrorCode arg1);

		public void onProgress(int arg0, int arg1);

		public void onSuccess(int arg0);
	}

	private RongIMClient client;
	private boolean isConnect = false;
	private ArrayList<ConnectListener> mConnectListeners = new ArrayList<RongIMTool.ConnectListener>();
	private ArrayList<MessageReceivelistener> mMessageReceivelisteners = new ArrayList<RongIMTool.MessageReceivelistener>();
	private ArrayList<MessageStatusListener> mMessageStatusListeners = new ArrayList<RongIMTool.MessageStatusListener>();

	public void addConnectListener(ConnectListener mConnectListener) {
		this.mConnectListeners.add(mConnectListener);
	}

	public void clearListener() {
		mConnectListeners.clear();
		mMessageReceivelisteners.clear();
		mMessageStatusListeners.clear();
	}

	public void removeConnectListener(ConnectListener mConnectListener) {
		mConnectListeners.remove(mConnectListener);
	}

	public void addMessageReceivelistener(
			MessageReceivelistener mMessageReceivelistener) {
		this.mMessageReceivelisteners.add(mMessageReceivelistener);
	}

	public void removeMessageReceivelistener(
			MessageReceivelistener mMessageReceivelistener) {
		mMessageReceivelisteners.remove(mMessageReceivelistener);
	}
	public void addMessageStatusListener (
			MessageStatusListener mMessageStatusListener) {
		this.mMessageStatusListeners.add(mMessageStatusListener);
	}

	public void removeMessageStatusListener(
			MessageStatusListener mMessageStatusListener ) {
		mMessageStatusListeners.remove(mMessageStatusListener);
	}
	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public RongIMClient getRongIMClient() {
		return client;
	}

	public void connect(String account, String nickname, String head) {
		getToken(account, nickname, head);
	}

	public void getToken(final String account, final String nickname,
			final String head) {
		/**
		 * IMKit SDK调用第一步 初始化 第一个参数， context上下文 第二个参数，APPKey换成自己的appkey
		 * 第三个参数，push消息通知所要打个的action页面 第四个参数，push消息中可以自定义push图标
		 */
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String token = "";
				try {
					L.i("=================task=======getToken");
					SdkHttpResult result = ApiHttpClient.getToken(
							HearFromApp.APP_KEY, HearFromApp.APPSECRT, account,
							nickname, head, FormatType.json);
					L.i("=================task=======getToken end " + result);
					JSONObject jsonObject = new JSONObject(result.getResult());
					token = jsonObject.optString("token");
					L.i(token);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return token;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.length() > 0) {
					getClientInstance(result);
				} else {
					for (ConnectListener mConnectListener : mConnectListeners) {
						mConnectListener.connectError();
					}
				}
			}
		};
		task.execute();
	}

	public void getClientInstance(String token) {
		try {// 0RaMpw3jNZw5qjSuRBx1+wBRJ2yDrk/VDUF1HrHfpZlbTZ2un+BZz02qxMJL6xvAIDi2K9IY+tSu/T+F+ybb/FPpweLPj2xD
			client = RongIMClient.connect(token,
					new RongIMClient.ConnectCallback() {
						@Override
						public void onSuccess(String s) {
							// 此处处理连接成功。
							Log.d("Connect:", "Login successfully.");
							isConnect = true;
							for (ConnectListener mConnectListener : mConnectListeners) {
								mConnectListener.connectComplete();
							}
							client.setOnReceiveMessageListener(new OnReceiveMessageListener() {

								@Override
								public void onReceived(Message msg, int status) {
									// TODO Auto-generated method stub
									for (MessageReceivelistener listener : mMessageReceivelisteners) {
										listener.onMessageReceive(msg, status);
									}
								}
							});
							RongIMClient
									.setConnectionStatusListener(new ConnectionStatusListener() {
										@Override
										public void onChanged(
												ConnectionStatus status) {
											// TODO Auto-generated method stub
											if (status == ConnectionStatus.CONNECTED) {
												isConnect = true;
											} else {
												isConnect = false;
											}
										}
									});
						}

						@Override
						public void onError(ErrorCode errorCode) {
							// 此处处理连接错误。
							Log.d("Connect:", "Login failed.");
							isConnect = false;
							for (ConnectListener mConnectListener : mConnectListeners) {
								mConnectListener.connectError();
							}
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int sendMessage(String type, String content, String toUserId) {
		if (client == null) {
			return -1;
		}
		TextMessage textContent = new TextMessage(content);
		textContent.setExtra(type);
		Message msg = client.sendMessage(ConversationType.PRIVATE, toUserId,
				textContent, this);
		return msg.getMessageId();
	}

	@Override
	public void onError(int arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub
		for (MessageStatusListener mMessageStatusListener : mMessageStatusListeners) {
			mMessageStatusListener.onError(arg0, arg1);
		}
	}

	@Override
	public void onProgress(int arg0, int arg1) {
		// TODO Auto-generated method stub
		for (MessageStatusListener mMessageStatusListener : mMessageStatusListeners) {
			mMessageStatusListener.onProgress(arg0, arg1);
		}
	}

	@Override
	public void onSuccess(int arg0) {
		// TODO Auto-generated method stub
		for (MessageStatusListener mMessageStatusListener : mMessageStatusListeners) {
			mMessageStatusListener.onSuccess(arg0);
		}
	}
}

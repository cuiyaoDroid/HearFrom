package com.tingshuo.hearfrom;


import io.rong.imkit.RongIM;
import io.rong.imkit.demo.DefaultExceptionHandler;
import io.rong.imkit.demo.DemoContext;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback.ErrorCode;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.MessageContent;
import io.rong.message.TextMessage;

import java.io.File;
import java.lang.reflect.Method;


import com.tingshuo.tool.CrashHandler;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.db.DBHelper;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.util.Log;

public class HearFromApp extends Application {
	
	//rongyun
	 private static final String IS_FIRST = "is_first";
	 DemoContext mContext;
	    //    public static final String APP_KEY = "e0x9wycfx7flq";
	 public static final String APP_KEY = "cpj2xarljqp0n";
	
	
	
	public static final String appPath = Environment
			.getExternalStorageDirectory() + "/.cyHearFrom/";
	public static String token="";
	public static int user_id=-1;
	private static HearFromApp mApplication;

	
	
	RongIMClient client;
	
	public synchronized static HearFromApp getInstance() {
		return mApplication;
	}
	private void upGradeDBifnessage(){
		DBHelper helper=new DBHelper(getApplicationContext());
		helper.close();
	}
	private void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	public static boolean isFlitClose;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		JPushInterface.setDebugMode(true); 	
//        JPushInterface.init(this);     
		createPath(appPath);
		mApplication = this;
		isFlitClose = PreferenceUtils.getPrefBoolean(getApplicationContext()
				, PreferenceConstants.SETTING_FLIT_CLOSE, true);
		token=PreferenceUtils.getPrefString(this, PreferenceConstants.TOKEN, "");
		user_id=PreferenceUtils.getPrefInt(this, PreferenceConstants.USER_ID, -1);
		upGradeDBifnessage();
		L.isDebug = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.ISNEEDLOG, true);
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.REPORT_CRASH, false))
			CrashHandler.getInstance().init(this);
		
		
		
		
		
		
		
		 /**
         * IMKit SDK调用第一步 初始化
         * 第一个参数，  context上下文
         * 第二个参数，APPKey换成自己的appkey
         * 第三个参数，push消息通知所要打个的action页面
         * 第四个参数，push消息中可以自定义push图标
         */
		RongIMClient.init(this, APP_KEY, R.drawable.ic_launcher);
        try {
        	client=RongIMClient.connect("dtRzw4ZZ7umpSZz0pS6oXwBRJ2yDrk/VDUF1HrHfpZlbTZ2un+BZz+pKH7ypuJw2CTXwq5+pdc2x36DMhFLQpQ=="
					, new RongIMClient.ConnectCallback() {

			    @Override
			    public void onSuccess(String s) {
			        // 此处处理连接成功。
			        Log.d("Connect:", "Login successfully.");
			        L.i("sendMessage Start");
			        client.sendMessage(ConversationType.PRIVATE, "cuiyao2",new TextMessage("test"), new RongIMClient.SendMessageCallback() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							L.i("sendMessage Success");
						}
						
						@Override
						public void onProgress(int arg0) {
							// TODO Auto-generated method stub
							L.i("sendMessage ing");
						}
						
						@Override
						public void onError(
								io.rong.imlib.RongIMClient.SendMessageCallback.ErrorCode arg0) {
							// TODO Auto-generated method stub
							L.i("sendMessage onError code "+arg0);
						}
					});
			    }

			    @Override
			    public void onError(ErrorCode errorCode) {
			        // 此处处理连接错误。
			        Log.d("Connect:", "Login failed.");
			    }
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

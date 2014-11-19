package com.tingshuo.hearfrom;


import io.rong.imkit.RongIM;
import io.rong.imkit.demo.DefaultExceptionHandler;
import io.rong.imkit.demo.DemoContext;
import io.rong.imlib.RongIMClient;

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
import android.util.Log;

public class HearFromApp extends Application {
	
	//rongyun
	 private static final String IS_FIRST = "is_first";
	 DemoContext mContext;
	    //    public static final String APP_KEY = "e0x9wycfx7flq";
	 public static final String APP_KEY = "z3v5yqkbv8v30";
	
	
	
	public static final String appPath = Environment
			.getExternalStorageDirectory() + "/.cyHearFrom/";
	public static String token="";
	public static int user_id=-1;
	private static HearFromApp mApplication;

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
        RongIM.init(this, APP_KEY, R.drawable.ic_launcher);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public void onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo user) {
                Log.d("Begavior", conversationType.getName() + ":" + user.getName() + " context:" + context);
            }

            @Override
            public void onClickMessage(Context context, RongIMClient.Message message) {
                Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId() + " context:" + context);
            }
        });

        mContext = DemoContext.getInstance();
        mContext.init(this);

        try {
            Class c;
            c = Class.forName("com.networkbench.agent.impl.NBSAppAgent");
            Method m = c.getMethod("setLicenseKey", new Class[]{String.class});
            m.invoke(c, new Object[]{"a546c342ba704acf91b27e9603b6860d"});

        } catch (Exception e) {
            e.printStackTrace();
        }

       try {
            System.loadLibrary("imdemo");
        } catch (UnsatisfiedLinkError e) {
//            e.printStackTrace();
        }



        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
		
		
	}
}

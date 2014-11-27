package com.tingshuo.hearfrom;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.Message;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

import java.io.File;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.tingshuo.tool.CrashHandler;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.RongIMTool;
import com.tingshuo.tool.db.DBHelper;
import com.tingshuo.web.http.ApiHttpClient;
import com.tingshuo.web.http.FormatType;
import com.tingshuo.web.http.SdkHttpResult;

public class HearFromApp extends Application {

	// rongyun
	private static final String IS_FIRST = "is_first";
	// public static final String APP_KEY = "e0x9wycfx7flq";
	public static final String APP_KEY = "cpj2xarljqp0n";
	public static final String APPSECRT = "B1NtTUMmxdIR";

	public static final String appPath = Environment
			.getExternalStorageDirectory() + "/.cyHearFrom/";
	public static String token = "";
	public static int user_id = -1;
	private static HearFromApp mApplication;

	public synchronized static HearFromApp getInstance() {
		return mApplication;
	}

	private void upGradeDBifnessage() {
		DBHelper helper = new DBHelper(getApplicationContext());
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
		// JPushInterface.setDebugMode(true);
		// JPushInterface.init(this);
		createPath(appPath);
		mApplication = this;
		isFlitClose = PreferenceUtils.getPrefBoolean(getApplicationContext(),
				PreferenceConstants.SETTING_FLIT_CLOSE, true);
		token = PreferenceUtils.getPrefString(this, PreferenceConstants.TOKEN,
				"");
		user_id = PreferenceUtils.getPrefInt(this, PreferenceConstants.USER_ID,
				-1);
		upGradeDBifnessage();
		L.isDebug = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.ISNEEDLOG, true);
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.REPORT_CRASH, false))
			CrashHandler.getInstance().init(this);
		RongIMClient.init(this, APP_KEY, R.drawable.ic_launcher);
		RongIMTool.getInstance().connect("Cuiyao", "cuiyao", "head");
	}
}

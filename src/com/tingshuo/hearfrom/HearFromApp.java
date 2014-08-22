package com.tingshuo.hearfrom;


import java.io.File;

import cn.jpush.android.api.JPushInterface;

import com.tingshuo.tool.CrashHandler;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.db.DBHelper;

import android.app.Application;
import android.os.Environment;

public class HearFromApp extends Application {
	public static final String appPath = Environment
			.getExternalStorageDirectory() + "/.cyHearFrom/";
	public static String token="";
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
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     
		upGradeDBifnessage();
		createPath(appPath);
		mApplication = this;
		L.isDebug = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.ISNEEDLOG, true);
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.REPORT_CRASH, true))
			CrashHandler.getInstance().init(this);
	}
}

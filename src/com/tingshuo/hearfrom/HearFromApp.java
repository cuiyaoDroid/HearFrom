package com.tingshuo.hearfrom;

import io.rong.imlib.RongIMClient;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.tingshuo.tool.CrashHandler;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.db.DBHelper;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.web.http.HttpJsonTool;

public class HearFromApp extends Application {
	public static final int NUM_PAGE = 6;// �ܹ��ж���ҳ
	public static int NUM = 20;// ÿҳ20������,�������һ��ɾ��button
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
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
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
		
		UserInfoHelper helper = new UserInfoHelper(getApplicationContext());
		UserInfoHolder holder = helper.selectData_Id(HearFromApp.user_id);
		helper.close();
		if (holder == null) {
			getMyInfo();
		}
		
		upGradeDBifnessage();
		
		L.isDebug = PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.ISNEEDLOG, true);
		if (PreferenceUtils.getPrefBoolean(this,
				PreferenceConstants.REPORT_CRASH, false))
			CrashHandler.getInstance().init(this);
		RongIMClient.init(this, APP_KEY, R.drawable.ic_launcher);
		initFaceMap();
		
		//getFriendsList();
	}
	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}
	private void getMyInfo() {
		AsyncTask<Void, Void, String> getFriendsListtask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				HttpJsonTool.getInstance().getUserInfo(getApplicationContext(), user_id);
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
		};
		getFriendsListtask.execute();
	}
	private void initFaceMap() {
		// TODO Auto-generated method stub
		mFaceMap.put("[����]", R.drawable.f_static_000);
		mFaceMap.put("[��Ƥ]", R.drawable.f_static_001);
		mFaceMap.put("[����]", R.drawable.f_static_002);
		mFaceMap.put("[͵Ц]", R.drawable.f_static_003);
		mFaceMap.put("[�ټ�]", R.drawable.f_static_004);
		mFaceMap.put("[�ô�]", R.drawable.f_static_005);
		mFaceMap.put("[����]", R.drawable.f_static_006);
		mFaceMap.put("[��ͷ]", R.drawable.f_static_007);
		mFaceMap.put("[õ��]", R.drawable.f_static_008);
		mFaceMap.put("[����]", R.drawable.f_static_009);
		mFaceMap.put("[���]", R.drawable.f_static_010);
		mFaceMap.put("[��]", R.drawable.f_static_011);
		mFaceMap.put("[��]", R.drawable.f_static_012);
		mFaceMap.put("[ץ��]", R.drawable.f_static_013);
		mFaceMap.put("[ί��]", R.drawable.f_static_014);
		mFaceMap.put("[���]", R.drawable.f_static_015);
		mFaceMap.put("[ը��]", R.drawable.f_static_016);
		mFaceMap.put("[�˵�]", R.drawable.f_static_017);
		mFaceMap.put("[�ɰ�]", R.drawable.f_static_018);
		mFaceMap.put("[ɫ]", R.drawable.f_static_019);
		mFaceMap.put("[����]", R.drawable.f_static_020);

		mFaceMap.put("[����]", R.drawable.f_static_021);
		mFaceMap.put("[��]", R.drawable.f_static_022);
		mFaceMap.put("[΢Ц]", R.drawable.f_static_023);
		mFaceMap.put("[��ŭ]", R.drawable.f_static_024);
		mFaceMap.put("[����]", R.drawable.f_static_025);
		mFaceMap.put("[����]", R.drawable.f_static_026);
		mFaceMap.put("[�亹]", R.drawable.f_static_027);
		mFaceMap.put("[����]", R.drawable.f_static_028);
		mFaceMap.put("[ʾ��]", R.drawable.f_static_029);
		mFaceMap.put("[����]", R.drawable.f_static_030);
		mFaceMap.put("[����]", R.drawable.f_static_031);
		mFaceMap.put("[�ѹ�]", R.drawable.f_static_032);
		mFaceMap.put("[����]", R.drawable.f_static_033);
		mFaceMap.put("[����]", R.drawable.f_static_034);
		mFaceMap.put("[˯]", R.drawable.f_static_035);
		mFaceMap.put("[����]", R.drawable.f_static_036);
		mFaceMap.put("[��Ц]", R.drawable.f_static_037);
		mFaceMap.put("[����]", R.drawable.f_static_038);
		mFaceMap.put("[˥]", R.drawable.f_static_039);
		mFaceMap.put("[Ʋ��]", R.drawable.f_static_040);
		mFaceMap.put("[����]", R.drawable.f_static_041);

		mFaceMap.put("[�ܶ�]", R.drawable.f_static_042);
		mFaceMap.put("[����]", R.drawable.f_static_043);
		mFaceMap.put("[�Һߺ�]", R.drawable.f_static_044);
		mFaceMap.put("[ӵ��]", R.drawable.f_static_045);
		mFaceMap.put("[��Ц]", R.drawable.f_static_046);
		mFaceMap.put("[����]", R.drawable.f_static_047);
		mFaceMap.put("[����]", R.drawable.f_static_048);
		mFaceMap.put("[��]", R.drawable.f_static_049);
		mFaceMap.put("[���]", R.drawable.f_static_050);
		mFaceMap.put("[����]", R.drawable.f_static_051);
		mFaceMap.put("[ǿ]", R.drawable.f_static_052);
		mFaceMap.put("[��]", R.drawable.f_static_053);
		mFaceMap.put("[����]", R.drawable.f_static_054);
		mFaceMap.put("[ʤ��]", R.drawable.f_static_055);
		mFaceMap.put("[��ȭ]", R.drawable.f_static_056);
		mFaceMap.put("[��л]", R.drawable.f_static_057);
		mFaceMap.put("[��]", R.drawable.f_static_058);
		mFaceMap.put("[����]", R.drawable.f_static_059);
		mFaceMap.put("[����]", R.drawable.f_static_060);
		mFaceMap.put("[ơ��]", R.drawable.f_static_061);
		mFaceMap.put("[Ʈ��]", R.drawable.f_static_062);

		mFaceMap.put("[����]", R.drawable.f_static_063);
		mFaceMap.put("[OK]", R.drawable.f_static_064);
		mFaceMap.put("[����]", R.drawable.f_static_065);
		mFaceMap.put("[����]", R.drawable.f_static_066);
		mFaceMap.put("[Ǯ]", R.drawable.f_static_067);
		mFaceMap.put("[����]", R.drawable.f_static_068);
		mFaceMap.put("[��Ů]", R.drawable.f_static_069);
		mFaceMap.put("[��]", R.drawable.f_static_070);
		mFaceMap.put("[����]", R.drawable.f_static_071);
		mFaceMap.put("[�]", R.drawable.f_static_072);
		mFaceMap.put("[ȭͷ]", R.drawable.f_static_073);
		mFaceMap.put("[����]", R.drawable.f_static_074);
		mFaceMap.put("[̫��]", R.drawable.f_static_075);
		mFaceMap.put("[����]", R.drawable.f_static_076);
		mFaceMap.put("[����]", R.drawable.f_static_077);
		mFaceMap.put("[����]", R.drawable.f_static_078);
		mFaceMap.put("[����]", R.drawable.f_static_079);
		mFaceMap.put("[����]", R.drawable.f_static_080);
		mFaceMap.put("[����]", R.drawable.f_static_081);
		mFaceMap.put("[��]", R.drawable.f_static_082);
		mFaceMap.put("[����]", R.drawable.f_static_083);

		mFaceMap.put("[��ĥ]", R.drawable.f_static_084);
		mFaceMap.put("[�ٱ�]", R.drawable.f_static_085);
		mFaceMap.put("[����]", R.drawable.f_static_086);
		mFaceMap.put("[�ܴ���]", R.drawable.f_static_087);
		mFaceMap.put("[��ߺ�]", R.drawable.f_static_088);
		mFaceMap.put("[��Ƿ]", R.drawable.f_static_089);
		mFaceMap.put("[�����]", R.drawable.f_static_090);
		mFaceMap.put("[��]", R.drawable.f_static_091);
		mFaceMap.put("[����]", R.drawable.f_static_092);
		mFaceMap.put("[ƹ����]", R.drawable.f_static_093);
		mFaceMap.put("[NO]", R.drawable.f_static_094);
		mFaceMap.put("[����]", R.drawable.f_static_095);
		mFaceMap.put("[���]", R.drawable.f_static_096);
		mFaceMap.put("[תȦ]", R.drawable.f_static_097);
		mFaceMap.put("[��ͷ]", R.drawable.f_static_098);
		mFaceMap.put("[��ͷ]", R.drawable.f_static_099);
		mFaceMap.put("[����]", R.drawable.f_static_100);
		mFaceMap.put("[����]", R.drawable.f_static_101);
		mFaceMap.put("[����]", R.drawable.f_static_102);
		mFaceMap.put("[����]", R.drawable.f_static_103);
		mFaceMap.put("[��̫��]", R.drawable.f_static_104);

		mFaceMap.put("[��̫��]", R.drawable.f_static_105);
		mFaceMap.put("[����]", R.drawable.f_static_106);
	}
}

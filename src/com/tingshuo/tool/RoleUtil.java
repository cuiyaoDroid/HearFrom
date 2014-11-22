package com.tingshuo.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tingshuo.hearfrom.R;
import com.tingshuo.web.http.HttpJsonTool;

import android.content.Context;
import android.os.AsyncTask;

public class RoleUtil {
	public static int role_id = 1;
	public static final int RESULT_SETTING_ROLE = 1203;
	public static final String ROLE_ID = "role_id";
	public static final String ROLE_NAME = "role_name";
	public static final String ROLE_PIC = "role_pic";
	public static final String[] role_names = { "军人", "逗比", "程序员", "孩他妈", "学生",
			"设计师", "大夫", "律师", "冒险家", "女神", "工人", "主妇" };
	private roleRefreshListener listener;

	private ArrayList<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();

	public void setRoleRefreshListener(roleRefreshListener listener) {
		this.listener = listener;
	}

	public static final int[] role_pic = { R.drawable.role_junren,
			R.drawable.role_dubi, R.drawable.role_chengxuyuan,
			R.drawable.role_haitama, R.drawable.role_xuesheng,
			R.drawable.role_shejishi, R.drawable.role_daifu,
			R.drawable.role_lvshi, R.drawable.role_maoxianjia,
			R.drawable.role_nvshen, R.drawable.role_gongren,
			R.drawable.role_zhufu };
	private static RoleUtil instance;

	public static RoleUtil getInstance() {
		if (instance == null) {
			instance = new RoleUtil();
			instance.refreshRoleData();
		}
		return instance;
	}
	public ArrayList<Map<String, Object>> getRoleData(){
		return gridData;
	}
	private void refreshRoleData() {
		gridData.clear();
		for (int i = 0; i < RoleUtil.role_ids.length; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(ROLE_ID, RoleUtil.role_ids[i]);
			data.put(ROLE_NAME, RoleUtil.role_names[i]);
			data.put(ROLE_PIC, RoleUtil.role_pic[i]);
			gridData.add(data);
		}
	}

	public void refreshRoleContent() {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getRoleList();
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (listener != null) {
					listener.onComplete(result);
				}
			}
		};
		task.execute();
	}

	public static final int[] role_ids = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12 };

	public static final void saveDefaultRoleId(Context context, int role_id) {
		PreferenceUtils.setPrefInt(context,
				PreferenceConstants.DEFUALT_ROLE_ID, role_id);
	}

	public static final int getDefaultRoleId(Context context) {
		int role_id = PreferenceUtils.getPrefInt(context,
				PreferenceConstants.DEFUALT_ROLE_ID, 1);
		return role_id;
	}

	public interface roleRefreshListener {
		void onComplete(String result);
	}
}

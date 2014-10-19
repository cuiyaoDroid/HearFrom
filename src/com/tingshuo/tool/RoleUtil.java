package com.tingshuo.tool;

import com.tingshuo.hearfrom.R;

import android.content.Context;

public class RoleUtil {
	public static int role_id=1;
	public static final int RESULT_SETTING_ROLE=1203;
	public static final String ROLE_ID="role_id";
	public static final String ROLE_NAME="role_name";
	public static final String ROLE_PIC="role_pic";
	public static final String[] role_names={"军人","逗比","程序员","孩他妈"
		,"学生","设计师","大夫","律师"
		,"冒险家","女神","工人","主妇"};
	public static final int[] role_pic={
			R.drawable.role_junren,R.drawable.role_dubi,R.drawable.role_chengxuyuan,R.drawable.role_haitama
			,R.drawable.role_xuesheng,R.drawable.role_shejishi,R.drawable.role_daifu,R.drawable.role_lvshi
			,R.drawable.role_maoxianjia,R.drawable.role_nvshen,R.drawable.role_gongren,R.drawable.role_zhufu};
	private static RoleUtil instance;
	public static RoleUtil getInstance() {
		if(instance==null){
			instance=new RoleUtil();
		}
		return instance;
	}
	public void refreshRoleData(){
		
	}
	public static final int[] role_ids={1,2,3,4,5,6,7,8,9,10,11,12};
	public static final void saveDefaultRoleId(Context context,int role_id){
		PreferenceUtils.setPrefInt(context, PreferenceConstants.DEFUALT_ROLE_ID, role_id);
	}
	public static final int getDefaultRoleId(Context context){
		int role_id=PreferenceUtils.getPrefInt(context, PreferenceConstants.DEFUALT_ROLE_ID, 1);
		return role_id;
	}
}

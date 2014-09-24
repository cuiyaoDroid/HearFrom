package com.tingshuo.tool;

import com.tingshuo.hearfrom.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class ActivityTool {
	public static void gotoLoginView(Context context) {
		try {
			Intent intent = new Intent(context,
					LoginActivity.class);
			T.show(context, "您的账号已在其他设备上登录，请重新登录",
					Toast.LENGTH_LONG);
			context.startActivity(intent);
		} catch (Exception e) {
		}
	}
}

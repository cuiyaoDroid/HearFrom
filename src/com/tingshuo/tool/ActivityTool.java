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
			T.show(context, "�����˺����������豸�ϵ�¼�������µ�¼",
					Toast.LENGTH_LONG);
			context.startActivity(intent);
		} catch (Exception e) {
		}
	}
}

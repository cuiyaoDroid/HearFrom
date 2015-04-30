package com.tingshuo.tool;

import com.baidu.mapapi.map.UiSettings;

public class BaiduMapTool {
	/**
	 * �Ƿ�������������
	 * 
	 * @param v
	 */
	public static void setZoomEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setZoomGesturesEnabled(enable);
	}

	/**
	 * �Ƿ�����ƽ������
	 * 
	 * @param v
	 */
	public static void setScrollEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setScrollGesturesEnabled(enable);
	}

	/**
	 * �Ƿ�������ת����
	 * 
	 * @param v
	 */
	public static void setRotateEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setRotateGesturesEnabled(enable);
	}

	/**
	 * �Ƿ����ø�������
	 * 
	 * @param v
	 */
	public static void setOverlookEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setOverlookingGesturesEnabled(enable);
	}

	/**
	 * �Ƿ�����ָ����ͼ��
	 * 
	 * @param v
	 */
	public static void setCompassEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setCompassEnabled(enable);
	}
}

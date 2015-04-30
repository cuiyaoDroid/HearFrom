package com.tingshuo.tool;

import com.baidu.mapapi.map.UiSettings;

public class BaiduMapTool {
	/**
	 * 是否启用缩放手势
	 * 
	 * @param v
	 */
	public static void setZoomEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setZoomGesturesEnabled(enable);
	}

	/**
	 * 是否启用平移手势
	 * 
	 * @param v
	 */
	public static void setScrollEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setScrollGesturesEnabled(enable);
	}

	/**
	 * 是否启用旋转手势
	 * 
	 * @param v
	 */
	public static void setRotateEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setRotateGesturesEnabled(enable);
	}

	/**
	 * 是否启用俯视手势
	 * 
	 * @param v
	 */
	public static void setOverlookEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setOverlookingGesturesEnabled(enable);
	}

	/**
	 * 是否启用指南针图层
	 * 
	 * @param v
	 */
	public static void setCompassEnable(UiSettings mUiSettings,boolean enable) {
		mUiSettings.setCompassEnabled(enable);
	}
}

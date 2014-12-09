package com.tingshuo.tool;

import java.text.SimpleDateFormat;


public class TimeFormatTool {
	public static final int phpTojava=1000;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String format(long time){
		return format.format(time);
	}
}

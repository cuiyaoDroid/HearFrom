package com.tingshuo.tool.db;

import java.io.File;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "app_db_order.db";
	private final static int DATABASE_VERSION = 8;
	Context context;
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
		synchronized (lock.Lock) {
			onCreate(this.getWritableDatabase());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}
	public void clearCash(){
		SharedPreferences userInfo = context.getSharedPreferences("user_info", 0);
		userInfo.edit().putBoolean("first", true).commit();
		final File file = context.getDatabasePath(DATABASE_NAME);
        file.delete();
        new DBHelper(context);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		SharedPreferences userInfo = context.getSharedPreferences("user_info", 0);
		userInfo.edit().putBoolean("first", true).commit();
		final File file = context.getDatabasePath(DATABASE_NAME);
        file.delete();
        new DBHelper(context);
	}
}

package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FriendsRequestHelper extends DBHelper {
	private final static String TABLE_NAME = "friends_request_order";
	public final static String ID = "_id";
	public final static String USER_ID = "user_id";
	public final static String TIME = "time";
	public final static String TYPE = "type";
	
	
	public FriendsRequestHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(getCreateSql());
	}
	private String getCreateSql(){
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ USER_ID + " integer, " 
				+ TIME + " LONG, " 
				+ TYPE + " integer" 
				+ ");";
		return sql;
	}
	
	public long insert(FriendsRequestHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(USER_ID, content.getUser_id());
		cv.put(TIME, content.getTime());
		cv.put(TYPE, content.getType());
		return db.replace(TABLE_NAME, null, cv);
	}
	private FriendsRequestHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int user_id_column = cursor.getColumnIndex(USER_ID);
		int time_column = cursor.getColumnIndex(TIME);
		int type_column = cursor.getColumnIndex(TYPE);

		int id = cursor.getInt(id_column);
		int user_id = cursor.getInt(user_id_column);
		long time = cursor.getLong(time_column);
		int type = cursor.getInt(type_column);
		FriendsRequestHolder holder = new FriendsRequestHolder(id, user_id, time, type);
		return holder;
	}
	public ArrayList<FriendsRequestHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<FriendsRequestHolder> holderlist = new ArrayList<FriendsRequestHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			FriendsRequestHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public int delete_id(int id){
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME,  ID + "=" + id, null);
		}
	}
	
	public FriendsRequestHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		FriendsRequestHolder holder=getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			db.execSQL(getCreateSql());
		}
	}
}

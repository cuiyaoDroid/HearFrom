package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FriendsListHelper extends DBHelper {
	private final static String TABLE_NAME = "friends_list_order";
	public final static String ID = "_id";
	public final static String USER_ID = "user_id";
	
	
	public FriendsListHelper(Context context) {
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
				+ USER_ID + " integer" 
				+ ");";
		return sql;
	}
	
	public long insert(FriendsListHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(USER_ID, content.getUser_id());
		return db.replace(TABLE_NAME, null, cv);
	}
	private FriendsListHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int user_id_column = cursor.getColumnIndex(USER_ID);

		int id = cursor.getInt(id_column);
		int user_id = cursor.getInt(user_id_column);
		FriendsListHolder holder = new FriendsListHolder(id, user_id);
		return holder;
	}
	public ArrayList<FriendsListHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<FriendsListHolder> holderlist = new ArrayList<FriendsListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			FriendsListHolder holder=getDataCursor(cursor);
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
	
	public FriendsListHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		FriendsListHolder holder=getDataCursor(cursor);
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

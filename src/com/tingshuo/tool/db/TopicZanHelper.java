package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TopicZanHelper extends DBHelper {
	private final static String TABLE_NAME = "topic_zan_order";
	public final static String ID = "_id";
	public final static String STATUS = "status";
	
	public TopicZanHelper(Context context) {
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
				+ STATUS + " integer"
				+ ");";
		return sql;
	}
	
	public long insert(TopicZanHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getTopic_id() != -1) {
			cv.put(ID, content.getTopic_id());
		}
		cv.put(STATUS, content.getStatus());
		return db.replace(TABLE_NAME, null, cv);
	}
	private TopicZanHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int status_coumn = cursor.getColumnIndex(STATUS);

		int id = cursor.getInt(id_column);
		int status = cursor.getInt(status_coumn);
		TopicZanHolder holder = new TopicZanHolder(id, status);
		return holder;
	}
	public ArrayList<TopicZanHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<TopicZanHolder> holderlist = new ArrayList<TopicZanHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			TopicZanHolder holder=getDataCursor(cursor);
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
	
	public TopicZanHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		TopicZanHolder holder=getDataCursor(cursor);
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

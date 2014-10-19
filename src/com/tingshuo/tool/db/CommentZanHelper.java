package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommentZanHelper extends DBHelper {
	private final static String TABLE_NAME = "comment_zan_order";
	public final static String ID = "_id";
	public final static String TOPIC_ID = "topic_id";
	public final static String STATUS = "status";
	
	public CommentZanHelper(Context context) {
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
				+ TOPIC_ID + " integer , "
				+ STATUS + " integer"
				+ ");";
		return sql;
	}
	
	public long insert(CommentZanHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getTopic_id() != -1) {
			cv.put(ID, content.getComment_id());
		}
		cv.put(TOPIC_ID, content.getTopic_id());
		cv.put(STATUS, content.getStatus());
		return db.replace(TABLE_NAME, null, cv);
	}
	private CommentZanHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int topic_id_coumn = cursor.getColumnIndex(TOPIC_ID);
		int status_coumn = cursor.getColumnIndex(STATUS);

		int id = cursor.getInt(id_column);
		int topic_id = cursor.getInt(topic_id_coumn);
		int status = cursor.getInt(status_coumn);
		CommentZanHolder holder = new CommentZanHolder(id, topic_id, status);
		return holder;
	}
	public ArrayList<CommentZanHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<CommentZanHolder> holderlist = new ArrayList<CommentZanHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			CommentZanHolder holder=getDataCursor(cursor);
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
	
	public CommentZanHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		CommentZanHolder holder=getDataCursor(cursor);
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

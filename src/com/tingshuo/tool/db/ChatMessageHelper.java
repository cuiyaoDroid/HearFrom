package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatMessageHelper extends DBHelper {
	private final static String TABLE_NAME = "chat_message_order";
	public final static String ID = "_id";
	public final static String USER_ID = "user_id";
	public final static String TIME = "time";
	public final static String TYPE = "type";
	public final static String FROM_ID = "from_id";
	public final static String TO_ID = "to_id";
	public final static String CONTENT = "content";
	public final static String STATUS = "status";
	
	public ChatMessageHelper(Context context) {
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
				+ FROM_ID + " integer, " 
				+ TO_ID + " integer, " 
				+ CONTENT + " VARCHAR, "
				+ STATUS + " integer, "
				+ TYPE + " VARCHAR" 
				+ ");";
		return sql;
	}
	
	public long insert(ChatMessageHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(USER_ID, content.getUser_id());
		cv.put(TIME, content.getTime());
		cv.put(TYPE, content.getType());
		cv.put(STATUS, content.getStatus());
		cv.put(CONTENT, content.getContent());
		cv.put(FROM_ID, content.getFrom_id());
		cv.put(TO_ID, content.getTo_id());
		return db.replace(TABLE_NAME, null, cv);
	}
	private ChatMessageHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int user_id_column = cursor.getColumnIndex(USER_ID);
		int time_column = cursor.getColumnIndex(TIME);
		int type_column = cursor.getColumnIndex(TYPE);
		int from_id_column = cursor.getColumnIndex(FROM_ID);
		int to_id_column = cursor.getColumnIndex(TO_ID);
		int content_column = cursor.getColumnIndex(CONTENT);
		int status_column = cursor.getColumnIndex(STATUS);

		int id = cursor.getInt(id_column);
		int user_id = cursor.getInt(user_id_column);
		long time = cursor.getLong(time_column);
		String type = cursor.getString(type_column);
		int from_id = cursor.getInt(from_id_column);
		int to_id = cursor.getInt(to_id_column);
		String content = cursor.getString(content_column);
		int status = cursor.getInt(status_column);
		ChatMessageHolder holder = new ChatMessageHolder(id, user_id, time
				, from_id, to_id, type, content,status);
		return holder;
	}
	public ArrayList<ChatMessageHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<ChatMessageHolder> holderlist = new ArrayList<ChatMessageHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ChatMessageHolder holder=getDataCursor(cursor);
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
	public ArrayList<ChatMessageHolder> selectData(int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null,null, null, null, ID + " desc limit "
							+ from + "," + pagesize);
		ArrayList<ChatMessageHolder> holderlist = new ArrayList<ChatMessageHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ChatMessageHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ChatMessageHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		ChatMessageHolder holder=getDataCursor(cursor);
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

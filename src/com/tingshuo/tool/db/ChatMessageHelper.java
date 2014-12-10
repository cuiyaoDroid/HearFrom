package com.tingshuo.tool.db;

import java.util.ArrayList;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.observer.Observable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatMessageHelper extends DBHelper {
	public final static String TABLE_NAME = "chat_message_order";
	public final static String ID = "_id";
	public final static String USER_ID = "user_id";
	public final static String TIME = "time";
	public final static String TYPE = "type";
	public final static String FROM_ID = "from_id";
	public final static String TO_ID = "to_id";
	public final static String CONTENT = "content";
	public final static String STATUS = "status";
	public static Observable mObservable=new Observable();
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
				+ FROM_ID + " VARCHAR, " 
				+ TO_ID + " VARCHAR, " 
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
		
		mObservable.setChanged();
		mObservable.notifyObservers(content);
		
		return db.replace(TABLE_NAME, null, cv);
	}
	public static ChatMessageHolder getDataCursor(Cursor cursor) {
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
		String from_id = cursor.getString(from_id_column);
		String to_id = cursor.getString(to_id_column);
		String content = cursor.getString(content_column);
		int status = cursor.getInt(status_column);
		ChatMessageHolder holder = new ChatMessageHolder(id, user_id, time
				, from_id, to_id, type, content,status);
		return holder;
	}
	public ArrayList<ChatMessageHolder> selectData(int from_id,int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, "( "+FROM_ID+"=? OR "+TO_ID+"=? ) AND "+USER_ID+"=?"
				,new String[]{String.valueOf(from_id),String.valueOf(from_id),String.valueOf(HearFromApp.user_id)}
				, null, null, ID + " desc limit "+ from + "," + pagesize);
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
		Cursor cursor = db.query(TABLE_NAME, null, null,null, null, null, ID + " asc limit "
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

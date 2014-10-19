package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class mainPostListHelper extends DBHelper {
	private final static String TABLE_NAME = "mainPostList_order";
	public final static String ID = "_id";
	public final static String USER_ID = "user_id";
	public final static String NICK_NAME = "nickname";
	public final static String HEAD = "head";
	public final static String CONTENT = "content";
	public final static String IMAGE = "image";
	public final static String LONGITUDER = "longitude";
	public final static String LATITUDE = "latitude";
	public final static String TIME = "time";
	public final static String COMMENT_COUNT = "comment_count";
	public final static String ZAN_COUNT = "zan_count";
	public final static String CAI_COUNT = "cai_count";
	public final static String ROLE_ID = "role_id";
	public final static String ROLE = "role";
	
	public mainPostListHelper(Context context) {
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
				+ NICK_NAME + " VARCHAR, " 
				+ HEAD + " VARCHAR, " 
				+ CONTENT + " VARCHAR, " 
				+ IMAGE + " VARCHAR, "
				+ LONGITUDER+ " VARCHAR, " 
				+ LATITUDE + " VARCHAR, "
				+ TIME + " LONG, "
				+ ZAN_COUNT + " integer, "
				+ CAI_COUNT + " integer, "
				+ ROLE_ID + " integer, "
				+ ROLE + " VARCHAR, "
				+ COMMENT_COUNT + " integer" 
				+ ");";
		return sql;
	}

	public long insert(mainPostListHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(USER_ID, content.getUser_id());
		cv.put(NICK_NAME, content.getNickname());
		cv.put(HEAD, content.getHead());
		cv.put(CONTENT, content.getContent());
		cv.put(IMAGE, content.getImage());
		cv.put(LONGITUDER, content.getLongitude());
		cv.put(LATITUDE, content.getLatitude());
		cv.put(TIME, content.getTime());
		cv.put(ZAN_COUNT, content.getZan_count());
		cv.put(CAI_COUNT, content.getCai_count());
		cv.put(COMMENT_COUNT, content.getComment_count());
		cv.put(ROLE_ID, content.getRole_id());
		cv.put(ROLE, content.getRole());
		return db.replace(TABLE_NAME, null, cv);
	}
	public long updataZanCount(int topic_id,int ZanCount, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(ZAN_COUNT, ZanCount);
		return db.update(TABLE_NAME, cv, ID+"=?", new String[]{String.valueOf(topic_id)});
	}
	private mainPostListHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int user_id_column = cursor.getColumnIndex(USER_ID);
		int nick_name_column = cursor.getColumnIndex(NICK_NAME);
		int head_column = cursor.getColumnIndex(HEAD);
		int content_column = cursor.getColumnIndex(CONTENT);
		int image_column = cursor.getColumnIndex(IMAGE);
		int longituder_column = cursor.getColumnIndex(LONGITUDER);
		int latitude_column = cursor.getColumnIndex(LATITUDE);
		int time_column = cursor.getColumnIndex(TIME);
		int zan_count_column = cursor.getColumnIndex(ZAN_COUNT);
		int cai_count_column = cursor.getColumnIndex(CAI_COUNT);
		int comment_count_column = cursor.getColumnIndex(COMMENT_COUNT);
		int role_id_coumn = cursor.getColumnIndex(ROLE_ID);
		int role_coumn = cursor.getColumnIndex(ROLE);

		int id = cursor.getInt(id_column);
		int user_id = cursor.getInt(user_id_column);
		String nick_name = cursor.getString(nick_name_column);
		String head = cursor.getString(head_column);
		String content = cursor.getString(content_column);
		String image = cursor.getString(image_column);
		String longituder = cursor.getString(longituder_column);
		String latitude = cursor.getString(latitude_column);
		long time = cursor.getLong(time_column);
		int zan_count = cursor.getInt(zan_count_column);
		int cai_count = cursor.getInt(cai_count_column);
		int comment_count = cursor.getInt(comment_count_column);
		int role_id = cursor.getInt(role_id_coumn);
		String role = cursor.getString(role_coumn);
		mainPostListHolder holder = new mainPostListHolder(id, user_id,
				nick_name, head, content, image, longituder, latitude, time,
				comment_count, zan_count, cai_count,role_id,role,0);
		return holder;
	}
	public ArrayList<mainPostListHolder> selectData(int from,int pagesize,int role_id,int sex) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc limit "
						+ from + "," + pagesize);
		ArrayList<mainPostListHolder> holderlist = new ArrayList<mainPostListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			mainPostListHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<mainPostListHolder> selectSearchData(String num) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, CONTENT
				+ " like '%" + num + "%'"
				,null, null, null, ID + " desc");
		ArrayList<mainPostListHolder> holderlist = new ArrayList<mainPostListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			mainPostListHolder holder=getDataCursor(cursor);
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
	
	public mainPostListHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		mainPostListHolder holder=getDataCursor(cursor);
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

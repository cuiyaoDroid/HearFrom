package com.tingshuo.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserInfoHelper extends DBHelper {
	private final static String TABLE_NAME = "userinfo_order";
	public final static String ID = "_id";
	public final static String ACCOUNT = "account";
	public final static String NICK_NAME = "nickname";
	public final static String HEAD = "head";
	public final static String SEX = "sex";
	public final static String LOGIN_TIME = "login_time";
	public final static String BRITHDAY = "brithday";
	public final static String PHONENUM = "phonenum";
	public final static String LEVEL_SCORE = "level_score";
	public final static String LEVEL = "level";
	public final static String IS_VIP = "is_vip";
	public final static String VIP_SCORE = "vip_score";
	public final static String VIP_LEVEL = "vip_level";
	public final static String STATUS = "status";
	
	
	public UserInfoHelper(Context context) {
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
				+ ACCOUNT + " integer, " 
				+ NICK_NAME + " VARCHAR, " 
				+ HEAD + " VARCHAR, " 
				+ SEX + " integer, " 
				+ LOGIN_TIME + " LONG, "
				+ BRITHDAY+ " VARCHAR, " 
				+ PHONENUM + " VARCHAR, "
				+ LEVEL_SCORE + " LONG, "
				+ LEVEL + " integer, "
				+ IS_VIP + " integer, "
				+ VIP_SCORE + " LONG, "
				+ STATUS + " integer, "
				+ VIP_LEVEL + " integer" 
				+ ");";
		return sql;
	}
	
	public long insert(UserInfoHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(ACCOUNT, content.getAccount());
		cv.put(NICK_NAME, content.getNickname());
		cv.put(HEAD, content.getHead());
		cv.put(SEX, content.getSex());
		cv.put(LOGIN_TIME, content.getLogin_time());
		cv.put(BRITHDAY, content.getBrithday());
		cv.put(PHONENUM, content.getPhonenum());
		cv.put(LEVEL_SCORE, content.getLevel());
		cv.put(LEVEL, content.getLevel());
		cv.put(IS_VIP, content.getIs_vip());
		cv.put(VIP_SCORE, content.getVip_score());
		cv.put(STATUS, content.getStatus());
		cv.put(VIP_LEVEL, content.getVip_level());
		return db.replace(TABLE_NAME, null, cv);
	}
	private UserInfoHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int account_column = cursor.getColumnIndex(ACCOUNT);
		int nick_name_column = cursor.getColumnIndex(NICK_NAME);
		int head_column = cursor.getColumnIndex(HEAD);
		int sex_column = cursor.getColumnIndex(SEX);
		int login_column = cursor.getColumnIndex(LOGIN_TIME);
		int brithday_column = cursor.getColumnIndex(BRITHDAY);
		int phonenum_column = cursor.getColumnIndex(PHONENUM);
		int level_score_column = cursor.getColumnIndex(LEVEL_SCORE);
		int level_column = cursor.getColumnIndex(LEVEL);
		int is_vip_column = cursor.getColumnIndex(IS_VIP);
		int vip_score_column = cursor.getColumnIndex(VIP_SCORE);
		int status_coumn = cursor.getColumnIndex(STATUS);
		int vip_level_coumn = cursor.getColumnIndex(VIP_LEVEL);

		int id = cursor.getInt(id_column);
		String account = cursor.getString(account_column);
		String nick_name = cursor.getString(nick_name_column);
		String head = cursor.getString(head_column);
		int sex = cursor.getInt(sex_column);
		long login_time = cursor.getLong(login_column);
		String brithday = cursor.getString(brithday_column);
		String phonenum = cursor.getString(phonenum_column);
		long level_score = cursor.getLong(level_score_column);
		int level = cursor.getInt(level_column);
		int is_vip = cursor.getInt(is_vip_column);
		long vip_score = cursor.getInt(vip_score_column);
		int status = cursor.getInt(status_coumn);
		int vip_level = cursor.getInt(vip_level_coumn);
		UserInfoHolder holder = new UserInfoHolder(id, account, nick_name, head, sex, login_time
				, brithday, phonenum, level_score, level, is_vip, vip_score, vip_level, status);
		return holder;
	}
	public ArrayList<UserInfoHolder> selectData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc");
		ArrayList<UserInfoHolder> holderlist = new ArrayList<UserInfoHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UserInfoHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<UserInfoHolder> selectSearchData(String num) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, NICK_NAME
				+ " like '%" + num + "%'"
				,null, null, null, ID + " desc");
		ArrayList<UserInfoHolder> holderlist = new ArrayList<UserInfoHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UserInfoHolder holder=getDataCursor(cursor);
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
	
	public UserInfoHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		UserInfoHolder holder=getDataCursor(cursor);
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

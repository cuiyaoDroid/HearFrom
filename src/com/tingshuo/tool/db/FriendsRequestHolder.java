package com.tingshuo.tool.db;

public class FriendsRequestHolder {
	private int id;
	private int user_id;
	private long time;
	private int type;
	
	public static final int TYPE_FROM_ME = 1;
	public static final int TYPE_TO_ME = 0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public FriendsRequestHolder(int id, int user_id, long time, int type) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.time = time;
		this.type = type;
	}

}

package com.tingshuo.tool.db;

public class ChatMessageHolder {
	private int id;
	private int user_id;
	private long time;
	private int from_id;
	private int to_id;
	private String type;
	private String content;
	private int status;
	public ChatMessageHolder(int id, int user_id, long time, int from_id,
			int to_id, String type, String content,int status) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.time = time;
		this.from_id = from_id;
		this.to_id = to_id;
		this.type = type;
		this.content = content;
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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
	public int getFrom_id() {
		return from_id;
	}
	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}
	public int getTo_id() {
		return to_id;
	}
	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}

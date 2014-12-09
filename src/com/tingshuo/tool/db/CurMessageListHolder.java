package com.tingshuo.tool.db;

public class CurMessageListHolder {
	private int id;
	private int user_id;
	private long time;
	private String from_id;
	private String to_id;
	private String type;
	private String content;
	private int status;
	
	
	public static final int STATUS_RECIVED=0;
	public static final int STATUS_SENDING=1;
	public static final int STATUS_SENDED=2;
	
	
	public CurMessageListHolder(int id, int user_id, long time, String from_id,
			String to_id, String type, String content,int status) {
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
	public String getFrom_id() {
		return from_id;
	}
	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}
	public String getTo_id() {
		return to_id;
	}
	public void setTo_id(String to_id) {
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

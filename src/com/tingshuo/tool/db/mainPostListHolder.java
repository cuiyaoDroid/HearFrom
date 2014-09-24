package com.tingshuo.tool.db;

public class mainPostListHolder {
	private int id;
	private int user_id;
	private String nickname;
	private String head;
	private String content;
	private String image;
	private String longitude;
	private String latitude;
	private long time;
	private int comment_count;
	private int zan_count;
	private int cai_count;
	private int role_id;
	private String role;
	private int isZan;
	public mainPostListHolder(int id, int user_id, String nickname,
			String head, String content, String image, String longitude,
			String latitude, long time, int comment_count, int zan_count,
			int cai_count,int role_id,String role,int isZan) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.nickname = nickname;
		this.head = head;
		this.content = content;
		this.image = image;
		this.longitude = longitude;
		this.latitude = latitude;
		this.time = time;
		this.comment_count = comment_count;
		this.zan_count = zan_count;
		this.cai_count = cai_count;
		this.role_id=role_id;
		this.role=role;
		this.isZan= isZan;
	}
	
	public int getIsZan() {
		return isZan;
	}

	public void setIsZan(int isZan) {
		this.isZan = isZan;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getZan_count() {
		return zan_count;
	}
	public void setZan_count(int zan_count) {
		this.zan_count = zan_count;
	}
	public int getCai_count() {
		return cai_count;
	}
	public void setCai_count(int cai_count) {
		this.cai_count = cai_count;
	}

}

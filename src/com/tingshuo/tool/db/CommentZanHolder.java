package com.tingshuo.tool.db;

public class CommentZanHolder {
	private int comment_id;
	private int topic_id;
	private int status;
	public static final int STATUS_ZAN=1;
	public static final int STATUS_CAI=0;
	public CommentZanHolder(int comment_id, int topic_id, int status) {
		super();
		this.comment_id = comment_id;
		this.topic_id = topic_id;
		this.status = status;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}

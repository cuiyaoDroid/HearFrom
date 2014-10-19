package com.tingshuo.tool.db;

public class TopicZanHolder {
	private int topic_id;
	private int status;
	public static final int STATUS_ZAN=1;
	public static final int STATUS_CAI=0;
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
	public TopicZanHolder(int topic_id, int status) {
		super();
		this.topic_id = topic_id;
		this.status = status;
	}
	
}

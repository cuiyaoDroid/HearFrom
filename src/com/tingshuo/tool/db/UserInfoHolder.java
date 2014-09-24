package com.tingshuo.tool.db;

public class UserInfoHolder {
	private int id;
	private String account;
	private String nickname;
	private String head;
	private int sex;
	private long login_time;
	private String brithday;
	private String phonenum;
	private long level_score;
	private int level;
	private int is_vip;
	private long vip_score;
	private int vip_level;
	private int status;
	public UserInfoHolder(int id, String account, String nickname, String head,
			int sex, long login_time, String brithday, String phonenum,
			long level_score, int level, int is_vip, long vip_score,
			int vip_level, int status) {
		super();
		this.id = id;
		this.account = account;
		this.nickname = nickname;
		this.head = head;
		this.sex = sex;
		this.login_time = login_time;
		this.brithday = brithday;
		this.phonenum = phonenum;
		this.level_score = level_score;
		this.level = level;
		this.is_vip = is_vip;
		this.vip_score = vip_score;
		this.vip_level = vip_level;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getBrithday() {
		return brithday;
	}
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public long getLevel_score() {
		return level_score;
	}
	public void setLevel_score(long level_score) {
		this.level_score = level_score;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getIs_vip() {
		return is_vip;
	}
	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
	}
	public long getVip_score() {
		return vip_score;
	}
	public void setVip_score(long vip_score) {
		this.vip_score = vip_score;
	}
	public int getVip_level() {
		return vip_level;
	}
	public void setVip_level(int vip_level) {
		this.vip_level = vip_level;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getLogin_time() {
		return login_time;
	}
	public void setLogin_time(long login_time) {
		this.login_time = login_time;
	}

}

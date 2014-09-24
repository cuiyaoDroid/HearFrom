package com.tingshuo.tool.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MapHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9032951593683504134L;
	private List<Map<String,Object>> users;
	
	public List<Map<String, Object>> getUsers() {
		return users;
	}
	public void setUsers(List<Map<String, Object>> users) {
		this.users = users;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MapHolder(List<Map<String,Object>> users) {
		super();
		this.users = users;
	}
	
}

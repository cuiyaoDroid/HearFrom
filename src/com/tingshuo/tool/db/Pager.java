package com.tingshuo.tool.db;

public class Pager {
	public int curpage;
	public int pagesize;
	public int minId;
	public static int Default_Page = 20;
	public Pager(int curpage, int pagesize) {
		this.curpage = curpage;
		this.pagesize = pagesize;
	}
	
}

package com.blog.pojo;

public class FeedUser {
	private final int id;
	private final String username;
	private final String dp;

	public FeedUser(int id, String username, String dp) {
		this.id = id;
		this.username = username;
		this.dp = dp;
	}

	public int getId() { return id; }
	public String getUsername() { return username; }
	public String getDp() { return dp; }
}

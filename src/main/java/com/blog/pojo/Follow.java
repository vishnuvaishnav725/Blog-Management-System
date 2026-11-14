package com.blog.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_follows", uniqueConstraints = @UniqueConstraint(
		columnNames = { "follower_id", "followed_id" }))
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "follower_id", nullable = false)
	private int followerId;

	@Column(name = "followed_id", nullable = false)
	private int followedId;

	public Follow() { }

	public Follow(int followerId, int followedId) {
		this.followerId = followerId;
		this.followedId = followedId;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getFollowerId() { return followerId; }
	public void setFollowerId(int followerId) { this.followerId = followerId; }
	public int getFollowedId() { return followedId; }
	public void setFollowedId(int followedId) { this.followedId = followedId; }
}

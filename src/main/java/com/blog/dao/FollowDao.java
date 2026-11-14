package com.blog.dao;

import java.util.Set;

public interface FollowDao {
	boolean follow(int followerId, int followedId);
	boolean unfollow(int followerId, int followedId);
	boolean isFollowing(int followerId, int followedId);
	Set<Integer> getFollowedIds(int followerId);
}

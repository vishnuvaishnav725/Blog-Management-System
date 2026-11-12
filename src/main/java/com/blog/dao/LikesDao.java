package com.blog.dao;

public interface LikesDao {
	
	
	boolean addLike(int pId, int userId);
	boolean removeLike(int pId, int userId);
	boolean isLikedByUser(int pId, int userId);
	int countLikes(int pId);
	

}

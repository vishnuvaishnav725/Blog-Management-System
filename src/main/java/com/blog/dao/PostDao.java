package com.blog.dao;

import java.util.List;

import com.blog.pojo.Post;

public interface PostDao {
	
	boolean addPost(Post p);
	boolean updatePost(Post p);
	boolean deletePost(int pId);
	Post getPostById(int pId);
	List<Post>getAllPosts();
	List<Post> getFeedPosts(int userId, boolean followedOnly);
	
	

}

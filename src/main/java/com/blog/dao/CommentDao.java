package com.blog.dao;

import java.util.List;

import com.blog.pojo.Comment;

public interface CommentDao {
	
	boolean addComment(Comment c);
	boolean updateComment(Comment c);
	boolean deleteComment(Comment c);
	public List<Comment> getCommentByPostId(int pId );
	public List<Comment> getCommentByUserId(int id);
	
	
	
	
	

}

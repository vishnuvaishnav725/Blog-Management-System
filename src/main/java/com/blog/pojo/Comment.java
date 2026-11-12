package com.blog.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="comments")
public class Comment {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;



@Column(name="comment_text", length=500)
private String commentText;


@ManyToOne(fetch=FetchType.LAZY)
@JoinColumn(name="post_id", referencedColumnName = "pId" , nullable=false)
private Post post;



@ManyToOne(fetch=FetchType.LAZY)
@JoinColumn(name="user_id" , referencedColumnName = "id" , nullable = false)
private UserInfo user;



public Comment() {
	super();
	// TODO Auto-generated constructor stub
}



public Comment(Integer id, String commentText, Post post, UserInfo user) {
	super();
	this.id = id;
	this.commentText = commentText;
	this.post = post;
	this.user = user;
}



public Integer getId() {
	return id;
}



public void setId(Integer id) {
	this.id = id;
}



public String getCommentText() {
	return commentText;
}



public void setCommentText(String commentText) {
	this.commentText = commentText;
}



public Post getPost() {
	return post;
}



public void setPost(Post post) {
	this.post = post;
}



public UserInfo getUser() {
	return user;
}



public void setUser(UserInfo user) {
	this.user = user;
}



@Override
public String toString() {
	return  id + "\t"+  commentText + "\t"+  post + "\t"+  user + "\t";
}





}

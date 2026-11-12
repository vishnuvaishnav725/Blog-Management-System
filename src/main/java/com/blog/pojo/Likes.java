package com.blog.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="likes")
public class Likes {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="post_id")
	private int pId;
	
	@Column(name="user_id")
	private int userId;

	public Likes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Likes(int id, int pId, int userId) {
		super();
		this.id = id;
		this.pId = pId;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return  id + "\t"+ pId + "\t"+ userId +"\t";
	}

}

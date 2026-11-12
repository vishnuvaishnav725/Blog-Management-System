package com.blog.dao;

import java.util.List;

import com.blog.pojo.UserInfo;

public interface UserInfoDao {
	
	boolean addUser(UserInfo u);
	public UserInfo checkUserCredential( String email,String password);
	boolean updateUser(UserInfo u);
	boolean deleteUser(int id);
	boolean blockUser(int id);
	boolean unblockUser(int id);
	UserInfo getUserById(int id);
	List<UserInfo> getAllUser();

}

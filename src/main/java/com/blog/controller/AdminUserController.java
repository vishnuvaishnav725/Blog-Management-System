package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.UserInfo;

@Controller
public class AdminUserController {
	
	
	@Autowired
	private UserInfoDaoImpl daoImpl;

	public void setDaoImpl(UserInfoDaoImpl daoImpl) {
		this.daoImpl = daoImpl;
	}
	
	
	@GetMapping("/ManageUsers")
	public String ManageUsersPage(Model m) {
		List<UserInfo> users = daoImpl.getAllUser();
		m.addAttribute("users",users);
		return "ManageUsers";
		
	}
	
	@PostMapping("/deleteUser")
	public String DeleteUsers(@RequestParam("id") int id, Model m){
		
		boolean Status = daoImpl.deleteUser(id);
		if(Status)
		{
			m.addAttribute("msg", "User Deleted Successfully");
	    }
		else {
			m.addAttribute("msg", "User Could not be Deleted");
		}
		return "redirect:/ManageUsers";
		
	}
	
	
	@PostMapping("/blockUser")
	public String blockUserPage(@RequestParam("id") int id, Model m){
		
			
			boolean Status = daoImpl.blockUser(id);
			if(Status) {
				m.addAttribute("msg", "User Blocked Successfully");
			}
			else {
				m.addAttribute("msg", "User Could not be blocked");
			}
			return "redirect:/ManageUsers";
	}
	
	
	
	@PostMapping("/unblockUser")
	public String unblockUser(@RequestParam("id") int id, Model m) {

	    boolean status = daoImpl.unblockUser(id);

	    if (status) {
	        m.addAttribute("msg", "User Unblocked Successfully");
	    } else {
	        m.addAttribute("msg", "User Could Not Be Unblocked");
	    }

	    return "redirect:/ManageUsers";
	}
	
	
		
	}



package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.daoimpl.PostDaoImpl;
import com.blog.pojo.Post;

@Controller
public class AdminController {
	
	@Autowired
	private PostDaoImpl daoImpl;
	
	@GetMapping("/admin")
	public String AdminPage(Model m) {
		List<Post> posts = daoImpl.getAllPosts();
		m.addAttribute("posts", "posts");
		return "admin";
	}

	@GetMapping("/ManageBlog")
	public String BlogPage(Model m) {
		List<Post> posts = daoImpl.getAllPosts();
		m.addAttribute("posts", posts);
		return "ManageBlog";
	}
	
	@PostMapping("/deletePost")
	public String DeletePostPage(@RequestParam("pId") int pId, Model m) {
		
		boolean Status = daoImpl.deletePost(pId);
		if(Status) {
			m.addAttribute("msg","Post Deleted Successfully");
		}
		else {
			m.addAttribute("msg","Post Could not be Deleted");
		}
		return "redirect:/ManageBlog";
		}
		
		
	}


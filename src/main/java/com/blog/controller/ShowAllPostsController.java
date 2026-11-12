package com.blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.blog.daoimpl.CommentDaoImpl;
import com.blog.daoimpl.PostDaoImpl;
import com.blog.pojo.Comment;
import com.blog.pojo.Post;

@Controller
public class ShowAllPostsController {
	
	@Autowired
	private PostDaoImpl daoImpl;
	
	
	@Autowired
	private CommentDaoImpl daoimpl;
	
	
	
	@GetMapping("/showAllPosts" )
    public String showAllPosts(Model model) {
        List<Post> posts = daoImpl.getAllPosts(); 
        model.addAttribute("posts", posts);

       
        Map<Integer, List<Comment>> commentsMap = new HashMap<>();
        if (posts != null) {
            for (Post p : posts) {
                if (p != null ) {
                    List<Comment> cl = daoimpl.getCommentByPostId(p.getPId());
                    commentsMap.put(p.getPId(), cl);
                }
            }
        }
        model.addAttribute("commentsMap", commentsMap);

        return "showAllPosts"; 
    }
}



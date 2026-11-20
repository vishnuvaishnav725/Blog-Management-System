package com.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.daoimpl.CommentDaoImpl;
import com.blog.daoimpl.PostDaoImpl;
import com.blog.pojo.Comment;
import com.blog.pojo.Post;
import com.blog.pojo.UserInfo;

@Controller
public class CommentController {

    @Autowired
    private CommentDaoImpl daoimpl;

    @Autowired
    private PostDaoImpl postDao; 

    @GetMapping("/comment")
    public String showCommentPage(@RequestParam("postId") Integer postId, Model model) {
        model.addAttribute("postId", postId);
        return "comment";   
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam("postId") Integer postId,
                             @RequestParam("comment_text") String commentText,
                             @RequestParam(value = "returnTo", required = false) String returnTo,
                             HttpSession session,
                             Model m) throws Exception {

        UserInfo user = (UserInfo) session.getAttribute("currentUser");

       
        if (user == null) {
            return "redirect:/login";
        }

        
        System.out.println("addComment(): received postId = " + postId + ", comment = " + commentText + ", userId = " + (user != null ? user.getId() : "null"));

      
        Post p = postDao.getPostById(postId);
        if (p == null) {
            m.addAttribute("msg", "Post not found");
            m.addAttribute("postId", postId);
           
            return "comment";
        }

       
        Comment c = new Comment();
        c.setUser(user);
        c.setPost(p);
        c.setCommentText(commentText);

        boolean saved = daoimpl.addComment(c);

        if (saved) {
            if ("liveFeed".equals(returnTo)) return "redirect:/liveFeed";
            return "redirect:/showAllPosts";
        } else {
            m.addAttribute("msg", "comment could not be done");
            m.addAttribute("postId", postId);
            return "comment";
        }
    }
}

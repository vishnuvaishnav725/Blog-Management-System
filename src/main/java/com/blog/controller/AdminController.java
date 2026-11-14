package com.blog.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blog.daoimpl.PostDaoImpl;
import com.blog.pojo.Post;
import com.blog.pojo.UserInfo;

@Controller
public class AdminController {

    @Autowired
    private PostDaoImpl daoImpl;

    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        List<Post> posts = daoImpl.getAllPosts();
        model.addAttribute("posts", posts);
        return "admin";
    }

    @GetMapping("/ManageBlog")
    public String blogPage(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        List<Post> posts = daoImpl.getAllPosts();
        model.addAttribute("posts", posts);
        return "ManageBlog";
    }

    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("pId") int pId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        boolean deleted = daoImpl.deletePost(pId);
        redirectAttributes.addFlashAttribute("msg",
                deleted ? "Post Deleted Successfully" : "Post Could not be Deleted");
        return "redirect:/ManageBlog";
    }

    private boolean isAdmin(HttpSession session) {
        UserInfo currentUser = session == null ? null : (UserInfo) session.getAttribute("currentUser");
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }
}

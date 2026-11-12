package com.blog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blog.daoimpl.LikesDaoImpl;

@Controller
public class LikesController {

    @Autowired
    private LikesDaoImpl daoImpl;

   
    @PostMapping(path = "/addLikeAjax", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> addLikeAjax(@RequestParam("pId") int pId, HttpSession session) {
       
        System.out.println("AJAX addLike -> sessionId=" + session.getId() + " userId=" + session.getAttribute("userId") + " pId=" + pId);

        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null || userIdObj.intValue() <= 0) {
            System.out.println("addLikeAjax: invalid session userId -> " + userIdObj);
            return ResponseEntity.status(401).body("{\"error\":\"login_required\"}");
        }
        int userId = userIdObj.intValue();

        try {
            boolean already = daoImpl.isLikedByUser(pId, userId);
            if (already) {
                daoImpl.removeLike(pId, userId);
            } else {
                daoImpl.addLike(pId, userId);
            }
            int newCount = daoImpl.countLikes(pId);
            
            return ResponseEntity.ok("{\"count\":" + newCount + "}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\":\"server_error\"}");
        }
    }

   
    @PostMapping("/addLike")
    public String addPost(@RequestParam("pId") int pId,
                          HttpSession session,
                          RedirectAttributes ra) {

        System.out.println("IN addLike -> JSESSIONID=" + session.getId()
                + " session.userId=" + session.getAttribute("userId")
                + " pId=" + pId);

        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null || userIdObj.intValue() <= 0) {
            ra.addFlashAttribute("msg", "Login required");
            return "redirect:/login";
        }
        int userId = userIdObj.intValue();

        try {
            boolean already = daoImpl.isLikedByUser(pId, userId);
            if (already) {
                daoImpl.removeLike(pId, userId);
                ra.addFlashAttribute("msg", "Unliked");
            } else {
                daoImpl.addLike(pId, userId);
                ra.addFlashAttribute("msg", "Liked");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("msg", "Server error while liking");
        }

        return "redirect:/home";
    }
}

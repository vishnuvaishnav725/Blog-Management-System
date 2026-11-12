package com.blog.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blog.dao.CommentDao;
import com.blog.daoimpl.PostDaoImpl;
import com.blog.pojo.Comment;
import com.blog.pojo.Post;

@Controller
public class PostController {

    @Autowired
    private PostDaoImpl daoimpl;

    @Autowired
    private CommentDao commentDao;


    // =====================================================
    // SHOW ALL POSTS
    // =====================================================

    @GetMapping("/post")
    public String showPosts(Model model) {

        List<Post> posts = daoimpl.getAllPosts();

        if (posts == null) {
            posts = new ArrayList<>();
        }

        Map<Integer, List<Comment>> commentsMap = new HashMap<>();

        for (Post p : posts) {

            List<Comment> comments =
                    commentDao.getCommentByPostId(p.getPId());

            if (comments == null) {
                comments = new ArrayList<>();
            }

            commentsMap.put(p.getPId(), comments);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("commentsMap", commentsMap);

        return "post";
    }


    // =====================================================
    // ADD NEW POST
    // =====================================================

    @PostMapping("/addPost")
    public String addPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "media", required = false) MultipartFile file,
            Model model) {

        try {

            // Create Post object
            Post p = new Post();

            p.setTitle(title);
            p.setContent(content);
            p.setCategory(category);


            // =================================================
            // IMAGE UPLOAD
            // =================================================

            if (file != null && !file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                p.setMedia(fileName);

                String serverPath =
                    "C:/Users/Vishnu Vaishnav/OneDrive/Attachments/Desktop/Site/Blogger/src/main/webapp/resources/images";

                File uploadFolder = new File(serverPath);

                // Create folder if not exists
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                File serverFile =
                    new File(uploadFolder, fileName);

                file.transferTo(serverFile);

                System.out.println(
                    "Image saved: "
                    + serverFile.getAbsolutePath()
                );

            } else {

                p.setMedia(null);
            }


            // =================================================
            // SAVE POST INTO DATABASE
            // =================================================

            boolean status = daoimpl.addPost(p);


            if (status) {

                System.out.println(
                    "=============================="
                );

                System.out.println(
                    "POST ADDED SUCCESSFULLY"
                );

                System.out.println(
                    "=============================="
                );

                // IMPORTANT
                // Database me save hone ke baad /post par jao
                return "redirect:/post";

            } else {

                System.out.println(
                    "POST COULD NOT BE ADDED"
                );

                model.addAttribute(
                    "msg",
                    "Post Could Not Be Added"
                );

                return "addPost";
            }


        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                "msg",
                "Error while adding post: "
                + e.getMessage()
            );

            return "addPost";
        }
    }
}
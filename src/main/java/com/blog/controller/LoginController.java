package com.blog.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blog.daoimpl.PostDaoImpl;
import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.Post;
import com.blog.pojo.UserInfo;

@Controller
public class LoginController {

    @Autowired
    private UserInfoDaoImpl daoimpl;

    @Autowired
    private PostDaoImpl PostDao;

    @Autowired
    public void setDaoimpl(UserInfoDaoImpl daoimpl) {
        this.daoimpl = daoimpl;
    }

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }

    @RequestMapping("/index")
    public String indexPage1() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

   

    @PostMapping("/login")
    public String checkUserCredential(@RequestParam String email,
                                      @RequestParam String password,
                                      HttpServletRequest request,
                                      Model m) {

        UserInfo user = daoimpl.checkUserCredential(email, password);

        if (user != null) {

           
            if (user.isBlocked()) {
                m.addAttribute("msg", "Your account is blocked by admin.");
                return "login"; 
            }

            System.out.println(">>>>>USER LOGED "+user);
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getUsername());
            session.setAttribute("dp", user.getDp());
            session.setAttribute("id", user.getId());
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());

            
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            } else {
                return "redirect:/home";
            }

        } else {
            m.addAttribute("msg", "Invalid Email or Password");
            return "login";
        }
    }

   

    @RequestMapping(value = "/checkUser", method = RequestMethod.POST)
    public String checkUserCredential(UserInfo u, Model m, HttpServletRequest request) {

        UserInfo user = daoimpl.checkUserCredential(u.getEmail(), u.getPassword());
        System.out.println(user);

        
        if (user == null) {
            m.addAttribute("msg", "Invalid User Credential");
            return "index";
        }

        
        if (user.isBlocked()) {
            m.addAttribute("msg", "Your account is blocked by admin.");
            return "index";   
        }

       
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        session = request.getSession(true);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("id", user.getId());
        session.setAttribute("dp", user.getDp());
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getId());

        m.addAttribute("msg", "Welcome To Blogging Site");

       
        if ("admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin";
        } else {
            return "redirect:/home";
        }
    }

    
    @GetMapping("/home")
    public String homePage(Model m, HttpSession session) {
    	
    	Integer userId = (Integer) session.getAttribute("userId");
    	
    	if(userId == null) {
    		return "redirect:/login";
    	}
        List<Post> lst = PostDao.getAllPosts();
        m.addAttribute("posts", lst);
        return "home";        
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {

        if (session != null) {
            session.invalidate();   
        }

        return "redirect:/login";  
    }


    @PostMapping("/reg")
    public String regPage() {
        return "reg";
    }

    @GetMapping("/reg")
    public String regGetPage() {
        return "reg";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam("dp") MultipartFile file,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("username") String username, Model m) throws Exception {

        UserInfo u = new UserInfo();
        u.setDp(file.getOriginalFilename());
        String serverpath = "C:/Users/Vishnu Vaishnav/OneDrive/Attachments/Desktop/Site/Blogger/src/main/webapp/resources/images/";
        File serverfile = new File(serverpath, file.getOriginalFilename());
        System.out.println(serverfile);
        file.transferTo(serverfile);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole("user");
        u.setUsername(username);

        if (daoimpl.addUser(u)) {
            m.addAttribute("msg", "User Register Successfully");
            return "login";
        } else {
            m.addAttribute("msg", "User Could Not Be Register / Already Exists");
            return "reg";
        }
    }

    @PostMapping("/login/getUser")
    public String getUserById(@RequestParam("id") int id,
                              @RequestParam("dp") MultipartFile file,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              @RequestParam("role") String role,
                              @RequestParam("username") String username, Model m,
                              HttpSession session) throws Exception {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        UserInfo u = daoimpl.getUserById(id);

        u.setId(id);
        u.setDp(file.getOriginalFilename());
        String serverpath = "C:/Users/Vishnu Vaishnav/OneDrive/Attachments/Desktop/Site/Blogger/src/main/webapp/resources/images/";
        File serverfile = new File(serverpath, file.getOriginalFilename());
        System.out.println(serverfile);
        file.transferTo(serverfile);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        u.setUsername(username);

        boolean Updated = daoimpl.updateUser(u);
        if (Updated) {
            m.addAttribute("msg", "User Profile Updated Successfully");
        } else {
            m.addAttribute("msg", "User Profile Could not be Updated");
        }
        return "updateUser";
    }

    private boolean isAdmin(HttpSession session) {
        UserInfo currentUser = session == null ? null : (UserInfo) session.getAttribute("currentUser");
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    @GetMapping("/loginUsers")
    public String getAllUsers(Model m, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        List<UserInfo> list = daoimpl.getAllUser();
        m.addAttribute("users", list);
        return "allUsers";
    }
}

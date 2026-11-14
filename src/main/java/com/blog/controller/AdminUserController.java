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

import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.UserInfo;

@Controller
public class AdminUserController {

    @Autowired
    private UserInfoDaoImpl daoImpl;

    @GetMapping("/ManageUsers")
    public String manageUsersPage(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        List<UserInfo> users = daoImpl.getAllUser();
        model.addAttribute("users", users);
        return "ManageUsers";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        boolean deleted = daoImpl.deleteUser(id);
        redirectAttributes.addFlashAttribute("msg",
                deleted ? "User Deleted Successfully" : "User Could not be Deleted");
        return "redirect:/ManageUsers";
    }

    @PostMapping("/blockUser")
    public String blockUser(@RequestParam("id") int id,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        boolean blocked = daoImpl.blockUser(id);
        redirectAttributes.addFlashAttribute("msg",
                blocked ? "User Blocked Successfully" : "User Could not be blocked");
        return "redirect:/ManageUsers";
    }

    @PostMapping("/unblockUser")
    public String unblockUser(@RequestParam("id") int id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        boolean unblocked = daoImpl.unblockUser(id);
        redirectAttributes.addFlashAttribute("msg",
                unblocked ? "User Unblocked Successfully" : "User Could Not Be Unblocked");
        return "redirect:/ManageUsers";
    }

    private boolean isAdmin(HttpSession session) {
        UserInfo currentUser = session == null ? null : (UserInfo) session.getAttribute("currentUser");
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }
}

package com.blog.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.UserInfo;

@Controller
public class UserProfileController {

    private static final long MAX_PROFILE_PICTURE_BYTES = 5L * 1024L * 1024L;
    private static final long MAX_IMAGE_PIXELS = 20_000_000L;

    @Autowired
    private UserInfoDaoImpl userDao;

    @GetMapping({"/profile", "/editProfile"})
    public String showProfile(HttpSession session, Model model) {
        UserInfo user = signedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        UserInfo current = userDao.getUserById(user.getId());
        if (current == null) {
            session.invalidate();
            return "redirect:/login";
        }
        session.setAttribute("currentUser", current);
        session.setAttribute("username", current.getUsername());
        session.setAttribute("dp", current.getDp());
        model.addAttribute("user", current);
        model.addAttribute("profilePhotoUrl", profilePhotoUrl(current.getDp()));
        return "editProfile";
    }

    @PostMapping("/updateUserProfile")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UserInfo sessionUser = signedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        String cleanUsername = username == null ? "" : username.trim();
        String cleanEmail = email == null ? "" : email.trim();
        if (cleanUsername.length() < 2 || cleanUsername.length() > 40
                || cleanEmail.length() > 254
                || !cleanEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            redirectAttributes.addFlashAttribute("error", "Enter a valid username and email address.");
            return "redirect:/editProfile";
        }
        if (userDao.isEmailUsedByAnotherUser(cleanEmail, sessionUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "That email address is already in use.");
            return "redirect:/editProfile";
        }

        UserInfo user = userDao.getUserById(sessionUser.getId());
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }
        user.setUsername(cleanUsername);
        user.setEmail(cleanEmail);
        if (!userDao.updateUser(user)) {
            redirectAttributes.addFlashAttribute("error", "Profile could not be updated.");
            return "redirect:/editProfile";
        }

        session.setAttribute("currentUser", user);
        session.setAttribute("username", user.getUsername());
        redirectAttributes.addFlashAttribute("msg", "Profile updated successfully.");
        return "redirect:/editProfile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        UserInfo user = signedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        int passwordBytes = newPassword == null ? 0
                : newPassword.getBytes(StandardCharsets.UTF_8).length;
        if (passwordBytes < 8 || passwordBytes > 72) {
            redirectAttributes.addFlashAttribute("error", "New password must be 8 to 72 bytes long.");
            return "redirect:/editProfile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation do not match.");
            return "redirect:/editProfile";
        }
        if (!userDao.changePassword(user.getId(), currentPassword, newPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/editProfile";
        }

        redirectAttributes.addFlashAttribute("msg", "Password changed successfully.");
        return "redirect:/editProfile";
    }

    @PostMapping("/updateProfilePicture")
    public String updateProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        UserInfo sessionUser = signedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        if (file == null || file.isEmpty() || file.getSize() > MAX_PROFILE_PICTURE_BYTES) {
            redirectAttributes.addFlashAttribute("error", "Choose a profile picture smaller than 5 MB.");
            return "redirect:/editProfile";
        }

        String originalName = file.getOriginalFilename();
        String lowerName = originalName == null ? "" : originalName.toLowerCase(Locale.ROOT);
        String format;
        String extension;
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            format = "jpg";
            extension = ".jpg";
        } else if (lowerName.endsWith(".png")) {
            format = "png";
            extension = ".png";
        } else {
            redirectAttributes.addFlashAttribute("error", "Profile pictures must be JPG or PNG images.");
            return "redirect:/editProfile";
        }

        String expectedType = format.equals("png") ? "image/png" : "image/jpeg";
        if (file.getContentType() == null || !expectedType.equalsIgnoreCase(file.getContentType())) {
            redirectAttributes.addFlashAttribute("error", "The selected file does not match its image type.");
            return "redirect:/editProfile";
        }

        Path destination = null;
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0
                    || (long) image.getWidth() * image.getHeight() > MAX_IMAGE_PIXELS) {
                redirectAttributes.addFlashAttribute("error", "The image is invalid or too large.");
                return "redirect:/editProfile";
            }

            Path directory = Paths.get(System.getProperty("user.home"), ".blogger", "profile-pictures");
            Files.createDirectories(directory);
            String fileName = UUID.randomUUID().toString() + extension;
            destination = directory.resolve(fileName).normalize();
            if (!destination.getParent().equals(directory.toAbsolutePath().normalize())) {
                redirectAttributes.addFlashAttribute("error", "The image could not be saved.");
                return "redirect:/editProfile";
            }
            if (!ImageIO.write(image, format, destination.toFile())) {
                redirectAttributes.addFlashAttribute("error", "The image could not be saved.");
                return "redirect:/editProfile";
            }

            UserInfo user = userDao.getUserById(sessionUser.getId());
            if (user == null) {
                Files.deleteIfExists(destination);
                session.invalidate();
                return "redirect:/login";
            }
            user.setDp("/profile-pictures/" + fileName);
            if (!userDao.updateUser(user)) {
                Files.deleteIfExists(destination);
                redirectAttributes.addFlashAttribute("error", "Profile picture could not be updated.");
                return "redirect:/editProfile";
            }

            session.setAttribute("currentUser", user);
            session.setAttribute("dp", user.getDp());
            redirectAttributes.addFlashAttribute("msg", "Profile picture updated successfully.");
        } catch (IOException e) {
            if (destination != null) {
                try {
                    Files.deleteIfExists(destination);
                } catch (IOException ignored) {
                    // Keep the original upload error for the user.
                }
            }
            redirectAttributes.addFlashAttribute("error", "The image could not be processed.");
        }
        return "redirect:/editProfile";
    }

    private UserInfo signedInUser(HttpSession session) {
        return session == null ? null : (UserInfo) session.getAttribute("currentUser");
    }

    private String profilePhotoUrl(String dp) {
        if (dp == null || dp.trim().isEmpty()) {
            return "/resources/images/default-user.png";
        }
        if (dp.matches("^/profile-pictures/[A-Za-z0-9-]+\\.(jpg|png)$")) {
            return dp;
        }
        String filename = new File(dp).getName();
        try {
            return "/resources/images/" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name())
                    .replace("+", "%20");
        } catch (Exception e) {
            return "/resources/images/default-user.png";
        }
    }
}

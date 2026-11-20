package com.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blog.daoimpl.CommentDaoImpl;
import com.blog.daoimpl.FollowDaoImpl;
import com.blog.daoimpl.LikesDaoImpl;
import com.blog.daoimpl.PostDaoImpl;
import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.Comment;
import com.blog.pojo.FeedUser;
import com.blog.pojo.Post;
import com.blog.pojo.UserInfo;

@Controller
public class LiveFeedController {
	@Autowired private PostDaoImpl postDao;
	@Autowired private FollowDaoImpl followDao;
	@Autowired private LikesDaoImpl likesDao;
	@Autowired private CommentDaoImpl commentDao;
	@Autowired private UserInfoDaoImpl userDao;

	@GetMapping("/liveFeed")
	public String liveFeed(@RequestParam(value = "mode", defaultValue = "all") String mode,
			HttpSession session, Model model) {
		Integer userId = currentUserId(session);
		if (userId == null) return "redirect:/login";
		String selectedMode = "following".equals(mode) ? "following" : "all";
		Set<Integer> followedIds = followDao.getFollowedIds(userId);
		List<FeedUser> people = new ArrayList<>();
		List<UserInfo> users = userDao.getAllUser();
		if (users != null) {
			for (UserInfo user : users) {
				if (user != null && user.getId() != userId && !user.isBlocked()) {
					people.add(new FeedUser(user.getId(), user.getUsername(), user.getDp()));
				}
			}
		}
		model.addAttribute("mode", selectedMode);
		model.addAttribute("people", people);
		model.addAttribute("followedIds", followedIds);
		model.addAttribute("currentUserId", userId);
		return "liveFeed";
	}

	@GetMapping("/liveFeedFragment")
	public String liveFeedFragment(@RequestParam(value = "mode", defaultValue = "all") String mode,
			HttpSession session, Model model) {
		Integer userId = currentUserId(session);
		if (userId == null) return "redirect:/login";
		String selectedMode = "following".equals(mode) ? "following" : "all";
		List<Post> posts = postDao.getFeedPosts(userId, "following".equals(selectedMode));
		if (posts == null) posts = new ArrayList<>();
		Set<Integer> followedIds = followDao.getFollowedIds(userId);
		Map<Integer, Boolean> likedMap = new HashMap<>();
		Map<Integer, List<Comment>> commentsMap = new HashMap<>();
		List<Integer> postIds = new ArrayList<>();
		for (Post post : posts) postIds.add(post.getPId());
		Set<Integer> likedIds = likesDao.getLikedPostIds(userId, postIds);
		for (Post post : posts) likedMap.put(post.getPId(), likedIds.contains(post.getPId()));
		for (Integer postId : postIds) commentsMap.put(postId, new ArrayList<>());
		List<Comment> comments = commentDao.getCommentsForPosts(postIds);
		if (comments != null) for (Comment comment : comments) {
			int postId = comment.getPost().getPId();
			commentsMap.computeIfAbsent(postId, ignored -> new ArrayList<>()).add(comment);
		}
		Map<Integer, Integer> likeCounts = likesDao.getLikeCounts(postIds);
		model.addAttribute("posts", posts);
		model.addAttribute("mode", selectedMode);
		model.addAttribute("followedIds", followedIds);
		model.addAttribute("likedMap", likedMap);
		model.addAttribute("commentsMap", commentsMap);
		model.addAttribute("likeCounts", likeCounts);
		model.addAttribute("currentUserId", userId);
		return "feedCards";
	}

	@PostMapping("/toggleFollow")
	public String toggleFollow(@RequestParam("targetId") int targetId,
			@RequestParam(value = "mode", defaultValue = "all") String mode,
			HttpSession session) {
		Integer userId = currentUserId(session);
		String selectedMode = "following".equals(mode) ? "following" : "all";
		if (userId == null) return "redirect:/login";
		UserInfo target = userDao.getUserById(targetId);
		if (target == null || target.isBlocked() || targetId == userId) {
			return "redirect:/liveFeed?mode=" + selectedMode;
		}
		if (followDao.isFollowing(userId, targetId)) followDao.unfollow(userId, targetId);
		else followDao.follow(userId, targetId);
		return "redirect:/liveFeed?mode=" + selectedMode;
	}

	@PostMapping(value = "/addFeedComment", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> addFeedComment(@RequestParam("postId") int postId,
			@RequestParam("comment_text") String text, HttpSession session) {
		Object sessionUser = session.getAttribute("currentUser");
		if (!(sessionUser instanceof UserInfo)) {
			return ResponseEntity.status(401).body("{\"error\":\"login_required\"}");
		}
		String cleaned = text == null ? "" : text.trim();
		if (cleaned.isEmpty() || cleaned.length() > 500) {
			return ResponseEntity.badRequest().body("{\"error\":\"comment_length\"}");
		}
		Post post = postDao.getPostById(postId);
		if (post == null) return ResponseEntity.badRequest().body("{\"error\":\"post_not_found\"}");
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setUser((UserInfo) sessionUser);
		comment.setCommentText(cleaned);
		return commentDao.addComment(comment)
				? ResponseEntity.ok("{\"ok\":true}")
				: ResponseEntity.status(500).body("{\"error\":\"save_failed\"}");
	}

	private Integer currentUserId(HttpSession session) {
		Object value = session == null ? null : session.getAttribute("userId");
		return value instanceof Integer && (Integer) value > 0 ? (Integer) value : null;
	}
}

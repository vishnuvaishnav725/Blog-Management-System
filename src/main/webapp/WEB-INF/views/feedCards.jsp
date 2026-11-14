<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,java.util.Map,java.util.Set,java.text.SimpleDateFormat,com.blog.pojo.Post,com.blog.pojo.Comment,com.blog.pojo.UserInfo" %>
<%!
private String cardEsc(String value) {
    if (value == null) return "";
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;");
}
%>
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    Map<Integer, Boolean> likedMap = (Map<Integer, Boolean>) request.getAttribute("likedMap");
    Map<Integer, Integer> likeCounts = (Map<Integer, Integer>) request.getAttribute("likeCounts");
    Map<Integer, List<Comment>> commentsMap = (Map<Integer, List<Comment>>) request.getAttribute("commentsMap");
    Set<Integer> followedIds = (Set<Integer>) request.getAttribute("followedIds");
    Integer currentUserId = (Integer) request.getAttribute("currentUserId");
    String mode = (String) request.getAttribute("mode");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy · hh:mm a");
    if (posts == null || posts.isEmpty()) {
%>
  <article class="card empty"><%= "following".equals(mode) ? "No posts from followed users yet. Follow people on the right or check All posts." : "No posts available yet. Be the first to publish one." %></article>
<%
    } else {
      for (Post post : posts) {
        UserInfo author = post.getAuthor();
        String authorName = author == null ? "Community post" : author.getUsername();
        String avatar = author == null ? null : author.getDp();
        String avatarUrl = avatar != null && avatar.startsWith("/profile-pictures/") ? avatar : "/resources/images/" + (avatar != null ? avatar : "default-user.png");
        String media = post.getMedia();
        String mediaUrl = media == null ? "" : (media.startsWith("/") ? media : "/resources/images/" + media);
        boolean liked = likedMap != null && Boolean.TRUE.equals(likedMap.get(post.getPId()));
        boolean following = author != null && followedIds != null && followedIds.contains(author.getId());
        List<Comment> comments = commentsMap == null ? null : commentsMap.get(post.getPId());
        int likeCount = likeCounts != null && likeCounts.containsKey(post.getPId())
                ? likeCounts.get(post.getPId()) : 0;
%>
  <article class="card post-card" id="post-<%=post.getPId()%>">
    <header class="post-head"><img class="avatar" src="<%=request.getContextPath()+cardEsc(avatarUrl)%>" alt=""><div><div class="post-author"><%=cardEsc(authorName)%></div><div class="post-time"><%=post.getCreatedAt()==null?"Older post":cardEsc(dateFormat.format(post.getCreatedAt()))%></div></div>
      <% if(author!=null && currentUserId!=null && author.getId()!=currentUserId){ %><form method="post" action="<%=request.getContextPath()%>/toggleFollow"><input type="hidden" name="targetId" value="<%=author.getId()%>"><input type="hidden" name="mode" value="<%=cardEsc(mode)%>"><button class="follow-btn <%=following?"following":""%>" type="submit"><%=following?"Following":"Follow"%></button></form><% } %>
    </header>
    <div class="post-copy"><%=cardEsc(post.getContent())%></div>
    <% if(media!=null&&!media.trim().isEmpty()){ %><img class="post-image" src="<%=request.getContextPath()+cardEsc(mediaUrl)%>" alt="Post image"><% } %>
    <div class="post-actions"><form class="feed-like-form"><input type="hidden" name="pId" value="<%=post.getPId()%>"><button class="action-btn <%=liked?"liked":""%>" type="submit" aria-pressed="<%=liked%>"><%=liked?"♥ Liked":"♡ Like"%></button></form><span class="like-count"><%=likeCount%></span><span>likes</span><span style="margin-left:auto;color:#64748b"><%=comments==null?0:comments.size()%> comments</span></div>
    <div class="comments">
      <% if(comments!=null){int shown=0;for(Comment comment:comments){if(shown++>=3)break;UserInfo commenter=comment.getUser();%><div class="comment"><strong><%=cardEsc(commenter==null?"User":commenter.getUsername())%></strong><%=cardEsc(comment.getCommentText())%></div><%}} %>
    </div>
    <form class="comment-form feed-comment-form" method="post" action="<%=request.getContextPath()%>/addFeedComment"><input type="hidden" name="postId" value="<%=post.getPId()%>"><input name="comment_text" maxlength="500" placeholder="Write a comment…" aria-label="Write a comment"><button class="comment-submit" type="submit">Comment</button></form>
  </article>
<%
      }
    }
%>

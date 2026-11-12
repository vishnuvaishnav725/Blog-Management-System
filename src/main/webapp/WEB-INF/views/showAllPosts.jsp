<%@page import="com.blog.pojo.Post"%>
<%@page import="com.blog.pojo.Comment"%>
<%@page import="com.blog.pojo.UserInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Posts</title>

<style>
.post-media img {
    width:100%;
    height:150px;
    object-fit:contain;
    background:#f1f1f1;
}
.post-card{
    border:1px solid #ccc;
    padding:15px;
    margin-bottom:20px;
}
.comment-box{
    margin-top:10px;
    padding-left:10px;
    border-left:3px solid #999;
}
.post-actions{
    margin-top:10px;
    display:flex;
    gap:10px;
    align-items:center;
}
.like-button{
    border:none;
    background:none;
    cursor:pointer;
    font-size:18px;
    display:inline-flex;
    align-items:center;
    gap:6px;
    padding:4px 6px;
}
.like-count{
    font-weight:bold;
}
.heart{
    font-size:20px;
    line-height:1;
}
a.comment-link{
    text-decoration:underline;
    color:purple;
}

/* ✅ Attractive Back To Home Button */
.back-home{
    display:inline-block;
    margin:30px 0;
    padding:12px 28px;
    background:linear-gradient(135deg, #6a11cb, #2575fc);
    color:white;
    text-decoration:none;
    font-size:16px;
    font-weight:bold;
    border-radius:30px;
    box-shadow:0 4px 10px rgba(0,0,0,0.2);
    transition:all 0.3s ease;
}

.back-home:hover{
    background:linear-gradient(135deg, #2575fc, #6a11cb);
    transform:translateY(-2px);
    box-shadow:0 6px 15px rgba(0,0,0,0.3);
}
</style>

</head>
<body>

<h2>Posts</h2>

<div class="content">

<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    Map<Integer, List<Comment>> commentsMap =
        (Map<Integer, List<Comment>>) request.getAttribute("commentsMap");
    Map<Integer, Integer> likesCountMap =
        (Map<Integer, Integer>) request.getAttribute("likesCountMap");
    Map<Integer, Boolean> likedMap =
        (Map<Integer, Boolean>) request.getAttribute("likedMap");

    UserInfo currentUser = (UserInfo) session.getAttribute("currentUser");
    Integer currentUserIdObj = (Integer) session.getAttribute("userId");
    int currentUserId = (currentUserIdObj != null) ? currentUserIdObj : 0;
%>

<%
if (posts != null && !posts.isEmpty()) {
    for (Post p : posts) {

        Integer lc = (likesCountMap != null) ? likesCountMap.get(p.getPId()) : null;
        int likeCount = (lc != null) ? lc : 0;

        Boolean lb = (likedMap != null) ? likedMap.get(p.getPId()) : null;
        boolean isLiked = (lb != null) ? lb : false;
%>

<div class="post-card" id="post-<%=p.getPId()%>">

    <p><%= p.getContent() %></p>

    <%
    String media = p.getMedia();
    if (media != null && !media.trim().isEmpty()) {
    %>
    <div class="post-media">
        <img src="<%= request.getContextPath() %>/resources/images/<%= media %>"
             alt="post-image">
    </div>
    <% } %>

    <div class="comment-box">
        <strong>Comments:</strong><br>
        <%
        List<Comment> cl =
            (commentsMap != null) ? commentsMap.get(p.getPId()) : null;

        if (cl != null && !cl.isEmpty()) {
            for (Comment c : cl) {
        %>
            <p>➡ <%= c.getCommentText() %></p>
        <%
            }
        } else {
        %>
            <p>No comments yet.</p>
        <%
        }
        %>
    </div>

    <div class="post-actions">
        <a class="comment-link"
           href="<%= request.getContextPath() %>/comment?postId=<%= p.getPId() %>">
            Comment
        </a>

        <form class="like-form"
              action="<%= request.getContextPath() %>/addLike"
              method="post"
              style="display:inline;">
            <input type="hidden" name="pId" value="<%= p.getPId() %>">

            <button type="submit"
                    class="like-button like-btn"
                    data-pid="<%= p.getPId() %>"
                    aria-pressed="<%= isLiked %>">
                <span class="heart"
                      style="color:<%= isLiked ? "red" : "black" %>;">
                    &#10084;
                </span>
                <span class="like-count"
                      id="like-count-<%= p.getPId() %>">
                    <%= likeCount %>
                </span>
            </button>
        </form>
    </div>

</div>

<%
    }
} else {
%>
<p>No posts available.</p>
<%
}
%>

<!-- ✅ Attractive Back To Home Button -->
<center>
    <a href="home" class="back-home">← Back to Home Page</a>
</center>

</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('.like-btn').forEach(function(btn) {

    btn.addEventListener('click', function(e) {
      e.preventDefault();

      var pId = this.dataset.pid;

      fetch('<%= request.getContextPath() %>/addLikeAjax?pId=' + encodeURIComponent(pId), {
        method: 'POST',
        credentials: 'same-origin',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      })
      .then(function(res) {
        if (res.status === 401) {
          alert('Please login to like posts.');
          window.location.href = '<%= request.getContextPath() %>/login';
          throw new Error('login_required');
        }
        return res.json();
      })
      .then(function(data) {
        if (data && data.count !== undefined) {
          document.getElementById('like-count-' + pId).innerText = data.count;
          var heart = btn.querySelector('.heart');
          heart.style.color = (heart.style.color === 'red') ? 'black' : 'red';
        }
      })
      .catch(function(err) {
        if (err.message !== 'login_required') console.error(err);
      });
    });

  });
});
</script>

</body>
</html>

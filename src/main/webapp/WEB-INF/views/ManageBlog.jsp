<%@page import="com.blog.pojo.Post"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Posts</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }

body {
    font-family: 'Poppins', sans-serif;
    background: #f4f5f7;
    padding: 30px;
}

.container {
    max-width: 900px;
    margin: auto;
}

.title {
    font-size: 26px;
    font-weight: 600;
    margin-bottom: 20px;
    color: #333;
    text-align: center;
}

.posts-list {
    display: flex;
    flex-direction: column;
    gap: 18px;
}

.post-card {
    background: #ffffff;
    border-radius: 10px;
    border: 1px solid #e0e0e0;
    padding: 18px 20px;
}

.content-box {
    background: #fafafa;
    padding: 12px 14px;
    border-radius: 8px;
    border: 1px solid #e3e3e3;
    margin-bottom: 12px;
    font-size: 14px;
    color: #444;
    line-height: 1.6;
    white-space: pre-wrap;
}

.image-box {
    background: #fafafa;
    border: 1px solid #e3e3e3;
    padding: 8px;
    border-radius: 8px;
    text-align: center;
    margin-bottom: 12px;
}

.post-img {
    max-width: 100%;
    max-height: 350px;
    object-fit: contain;
    border-radius: 6px;
}

.post-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 8px;
}

.delete-btn {
    background: #dc3545;
    color: white;
    border: none;
    padding: 8px 16px;
    font-size: 13px;
    border-radius: 5px;
    cursor: pointer;
    transition: 0.2s;
}

.delete-btn:hover {
    background: #b71c1c;
}

.no-posts {
    text-align: center;
    color: #666;
    margin-top: 30px;
    font-size: 16px;
}

/* ✅ Attractive Back To Admin Button */
.back-admin {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    margin-top: 30px;
    padding: 10px 24px;
    border-radius: 8px;
    background: linear-gradient(135deg, #2563eb, #3b82f6);
    color: white;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    box-shadow: 0 6px 18px rgba(37,99,235,0.35);
    transition: all 0.3s ease;
}

.back-admin:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 28px rgba(37,99,235,0.45);
    background: linear-gradient(135deg, #1d4ed8, #2563eb);
}
</style>

</head>
<body>

<div class="container">

<h2 class="title">Manage Posts</h2>

<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    if (posts != null && posts.size() > 0) {
%>

<div class="posts-list">

<% for (Post p : posts) { %>

<div class="post-card">

    <div class="content-box">
        <%= p.getContent() %>
    </div>

    <%
        String media = p.getMedia();
        if (media != null && !media.trim().isEmpty()) {
    %>
    <div class="image-box">
        <img class="post-img"
             src="<%= request.getContextPath() %>/resources/images/<%= media %>"
             alt="post image">
    </div>
    <% } %>

    <div class="post-footer">
        <form action="<%= request.getContextPath() %>/deletePost" method="post">
            <input type="hidden" name="pId" value="<%= p.getPId() %>">
            <button class="delete-btn">Delete</button>
        </form>
    </div>

</div>

<% } %>

</div>

<% } else { %>

<p class="no-posts">No posts available.</p>

<% } %>

<!-- ✅ Attractive Back To Admin Page -->
<center>
    <a href="admin" class="back-admin">⬅ Back To Admin Page</a>
</center>

</div>

</body>
</html>

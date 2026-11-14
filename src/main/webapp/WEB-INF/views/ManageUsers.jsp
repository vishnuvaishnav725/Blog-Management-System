<%@page import="java.util.List"%>
<%@page import="com.blog.pojo.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Users</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

<style>
*{
    margin:0;
    padding:0;
    box-sizing:border-box;
}

body{
    font-family:'Poppins',sans-serif;
    background:#f5f7fb;
    padding:30px;
}

.page-title{
    font-size:28px;
    font-weight:600;
    margin-bottom:25px;
    color:#222;
}

/* ✅ GRID LAYOUT */
.users-container{
    display:grid;
    grid-template-columns:repeat(auto-fill,minmax(280px,1fr));
    gap:24px;
}

/* ✅ USER CARD */
.user-card{
    background:#ffffff;
    border-radius:14px;
    padding:18px;
    box-shadow:0 10px 30px rgba(0,0,0,0.06);
    transition:transform .3s ease;
}

.user-card:hover{
    transform:translateY(-6px);
}

/* TOP */
.user-top{
    display:flex;
    align-items:center;
    gap:14px;
}

.user-dp{
    width:60px;
    height:60px;
    border-radius:50%;
    object-fit:cover;
    border:2px solid #e5e7eb;
}

.user-name{
    font-size:18px;
    font-weight:600;
    color:#111;
}

.user-role{
    font-size:11px;
    padding:4px 10px;
    border-radius:20px;
    background:#e8f0fe;
    color:#2563eb;
    font-weight:600;
    display:inline-block;
    margin-top:4px;
}

/* STATUS */
.status{
    margin-top:12px;
    font-size:12px;
    font-weight:600;
    display:inline-block;
    padding:5px 12px;
    border-radius:20px;
}

.active{background:#e8f5e9;color:#2e7d32;}
.blocked{background:#ffebee;color:#c62828;}

/* ACTIONS */
.user-actions{
    margin-top:15px;
    display:flex;
    gap:8px;
    flex-wrap:wrap;
}

.user-actions form{
    display:inline;
}

.btn{
    border:none;
    padding:7px 14px;
    border-radius:8px;
    font-size:13px;
    font-weight:500;
    cursor:pointer;
}

.btn-delete{background:#ef4444;color:#fff;}
.btn-block{background:#f59e0b;color:#fff;}
.btn-unblock{background:#22c55e;color:#fff;}

/* BACK BUTTON */
.back-admin{
    display:inline-flex;
    align-items:center;
    justify-content:center;
    margin-top:40px;
    padding:14px 32px;
    border-radius:12px;
    background:linear-gradient(135deg,#2563eb,#3b82f6);
    color:#fff;
    font-size:15px;
    font-weight:600;
    text-decoration:none;
    box-shadow:0 12px 30px rgba(37,99,235,.35);
}
</style>
</head>

<body>

<h2 class="page-title">Manage Users</h2>

<div class="users-container">

<%
List<UserInfo> users = (List<UserInfo>) request.getAttribute("users");
if(users != null && !users.isEmpty()){
    for(UserInfo u : users){
%>

<div class="user-card">

    <div class="user-top">
        <% String userDp = u.getDp(); String userDpUrl = (userDp != null && userDp.startsWith("/profile-pictures/")) ? userDp : "/resources/images/" + (userDp != null ? userDp : "default-user.png"); %>
        <img src="<%=request.getContextPath()%><%=userDpUrl%>"
             class="user-dp"
             onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/resources/images/default-user.png';">
        <div>
            <div class="user-name"><%=u.getUsername()%></div>
            <span class="user-role"><%=u.getRole()%></span>
        </div>
    </div>

    <% if(u.isBlocked()){ %>
        <span class="status blocked">BLOCKED</span>
    <% } else { %>
        <span class="status active">ACTIVE</span>
    <% } %>

    <div class="user-actions">

        <form action="<%=request.getContextPath()%>/deleteUser" method="post">
            <input type="hidden" name="id" value="<%=u.getId()%>">
            <button class="btn btn-delete">Delete</button>
        </form>

        <% if(!u.isBlocked()){ %>
            <form action="<%=request.getContextPath()%>/blockUser" method="post">
                <input type="hidden" name="id" value="<%=u.getId()%>">
                <button class="btn btn-block">Block</button>
            </form>
        <% } else { %>
            <form action="<%=request.getContextPath()%>/unblockUser" method="post">
                <input type="hidden" name="id" value="<%=u.getId()%>">
                <button class="btn btn-unblock">Unblock</button>
            </form>
        <% } %>

    </div>

</div>

<%
    }
}
%>

</div>

<center>
    <a href="admin" class="back-admin">← Back To Admin Page</a>
</center>

</body>
</html>

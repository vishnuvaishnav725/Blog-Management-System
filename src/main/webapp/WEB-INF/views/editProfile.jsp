<%@page import="com.blog.pojo.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>User Profile Management</title>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<style>
*{box-sizing:border-box}body{margin:0;min-height:100vh;padding:36px 18px;font-family:Inter,Arial,sans-serif;background:linear-gradient(135deg,#eef2ff,#e0f2fe);color:#172033}
.wrap{max-width:1020px;margin:auto}.top{display:flex;align-items:center;gap:20px;margin-bottom:24px}.avatar{width:82px;height:82px;border-radius:50%;object-fit:cover;border:4px solid white;box-shadow:0 8px 25px #33415533}.heading h1{font-size:30px;margin:0 0 6px}.heading p{color:#64748b;margin:0}
.grid{display:grid;grid-template-columns:1fr 1fr;gap:20px}.card{background:#ffffffed;border:1px solid #ffffff;border-radius:20px;padding:24px;box-shadow:0 14px 40px #33415518}.card h2{font-size:19px;margin:0 0 6px}.sub{font-size:13px;color:#64748b;margin:0 0 20px}
label{display:block;font-size:13px;font-weight:600;margin:14px 0 7px}input[type=text],input[type=email],input[type=password],input[type=file]{width:100%;padding:12px 13px;border:1px solid #dbe2ee;border-radius:10px;background:white;font:inherit}input:focus{outline:2px solid #818cf8;border-color:transparent}
button,.back{display:inline-block;margin-top:18px;border:0;border-radius:10px;padding:12px 18px;background:linear-gradient(135deg,#4f46e5,#2563eb);color:#fff;font-weight:700;font-size:14px;text-decoration:none;cursor:pointer}
.msg,.error{padding:12px 14px;border-radius:10px;margin:0 0 18px}.msg{background:#ecfdf5;color:#047857}.error{background:#fef2f2;color:#b91c1c}
.photo-row{display:flex;align-items:center;gap:18px}.photo-row .avatar{width:96px;height:96px}
.full{grid-column:1/-1}.actions{display:flex;gap:12px;align-items:center}.back{background:#e2e8f0;color:#334155}
@media(max-width:700px){.grid{grid-template-columns:1fr}.full{grid-column:auto}.top{align-items:flex-start}.heading h1{font-size:24px}}
</style>
</head>
<body>
<div class="wrap">
<%
UserInfo u = (UserInfo) request.getAttribute("user");
String photoUrl = (String) request.getAttribute("profilePhotoUrl");
String msg = (String) request.getAttribute("msg");
String error = (String) request.getAttribute("error");
%>
<div class="top">
<img class="avatar" src="<%=request.getContextPath()%><%=photoUrl%>" alt="Profile picture">
<div class="heading"><h1>User Profile Management</h1><p>Update your account details, profile picture, and password.</p></div>
</div>
<% if (msg != null) { %><div class="msg"><%=msg%></div><% } %>
<% if (error != null) { %><div class="error"><%=error%></div><% } %>
<div class="grid">
<section class="card">
<h2>Profile details</h2><p class="sub">Change your username and email address.</p>
<form action="<%=request.getContextPath()%>/updateUserProfile" method="post" autocomplete="on">
<label for="username">Username</label>
<input id="username" type="text" name="username" value="<%=u.getUsername()%>" minlength="2" maxlength="40" required>
<label for="email">Email</label>
<input id="email" type="email" name="email" value="<%=u.getEmail()%>" maxlength="254" required>
<div class="actions"><button type="submit">Save profile</button><a class="back" href="<%=request.getContextPath()%>/home">Back to home</a></div>
</form>
</section>
<section class="card">
<h2>Profile picture</h2><p class="sub">Upload a JPG or PNG image up to 5 MB.</p>
<form action="<%=request.getContextPath()%>/updateProfilePicture" method="post" enctype="multipart/form-data">
<div class="photo-row"><img class="avatar" src="<%=request.getContextPath()%><%=photoUrl%>" alt="Current profile picture"><input type="file" name="profilePicture" accept=".jpg,.jpeg,.png,image/jpeg,image/png" required></div>
<button type="submit">Upload picture</button>
</form>
</section>
<section class="card full">
<h2>Change password</h2><p class="sub">Enter your current password, then choose a new password.</p>
<form action="<%=request.getContextPath()%>/changePassword" method="post" autocomplete="off">
<label for="currentPassword">Current password</label><input id="currentPassword" type="password" name="currentPassword" required>
<label for="newPassword">New password (8–72 bytes)</label><input id="newPassword" type="password" name="newPassword" minlength="8" maxlength="72" required>
<label for="confirmPassword">Confirm new password</label><input id="confirmPassword" type="password" name="confirmPassword" minlength="8" maxlength="72" required>
<button type="submit">Change password</button>
</form>
</section>
</div>
</div>
</body>
</html>

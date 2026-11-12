<%@page import="com.blog.pojo.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Profile</title>

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

<style>
*{margin:0;padding:0;box-sizing:border-box}
html,body{height:100%}

body{
    font-family:'Inter',system-ui;
    background:
        radial-gradient(circle at 15% 20%, rgba(99,102,241,.25), transparent 45%),
        radial-gradient(circle at 85% 75%, rgba(14,165,164,.22), transparent 45%),
        linear-gradient(180deg,#f8fafc,#eef2ff,#e0e7ff);
    display:flex;
    align-items:center;
    justify-content:center;
    padding:30px;
}

/* ✨ Floating Card */
.card{
    width:100%;
    max-width:920px;
    background:rgba(255,255,255,.9);
    border-radius:26px;
    padding:42px;
    box-shadow:
        0 40px 90px rgba(2,6,23,.18),
        inset 0 1px 0 rgba(255,255,255,.6);
    backdrop-filter:blur(12px);
    border:1px solid rgba(255,255,255,.4);
    animation: floatIn .8s ease;
}

@keyframes floatIn{
    from{opacity:0;transform:translateY(25px) scale(.96)}
    to{opacity:1;transform:none}
}

/* Header */
.header{
    display:flex;
    align-items:center;
    gap:22px;
    margin-bottom:30px;
}

.logo-circle{
    width:96px;
    height:96px;
    border-radius:50%;
    background:
        linear-gradient(135deg,#6366f1,#3b82f6,#22d3ee);
    display:flex;
    align-items:center;
    justify-content:center;
    color:white;
    font-size:26px;
    font-weight:800;
    box-shadow:0 18px 45px rgba(59,130,246,.45);
    animation:pulse 3s infinite alternate;
}

@keyframes pulse{
    from{box-shadow:0 18px 45px rgba(59,130,246,.45)}
    to{box-shadow:0 26px 70px rgba(34,211,238,.6)}
}

h2{
    font-size:26px;
    margin-bottom:6px;
}

.lead{
    font-size:14px;
    color:#475569;
    line-height:1.6;
}

/* Form Layout */
form{
    display:grid;
    grid-template-columns:1fr 1fr;
    gap:22px;
}

.full{grid-column:1/-1}

/* Fields */
label{
    font-size:13px;
    font-weight:700;
    margin-bottom:8px;
    display:block;
}

.field{
    position:relative;
}

.icon{
    position:absolute;
    left:16px;
    top:50%;
    transform:translateY(-50%);
    font-size:14px;
    color:#64748b;
}

input[type=text],
input[type=email],
input[type=password]{
    width:100%;
    padding:14px 18px 14px 46px;
    border-radius:16px;
    border:1px solid #e5e7eb;
    background:#ffffff;
    font-size:15px;
    transition:.35s;
}

input:focus{
    outline:none;
    border-color:#6366f1;
    box-shadow:
        0 18px 40px rgba(99,102,241,.35);
    transform:translateY(-2px);
}

/* Actions */
.actions{
    display:flex;
    gap:18px;
    align-items:center;
    margin-top:18px;
}

input[type=submit]{
    background:
        linear-gradient(135deg,#6366f1,#4f46e5,#3b82f6);
    color:white;
    border:none;
    padding:14px 26px;
    border-radius:16px;
    cursor:pointer;
    font-weight:800;
    font-size:15px;
    letter-spacing:.3px;
    box-shadow:0 22px 50px rgba(79,70,229,.45);
    transition:.35s;
}

input[type=submit]:hover{
    transform:translateY(-4px) scale(1.03);
    box-shadow:0 30px 70px rgba(79,70,229,.6);
}

a.cancel{
    padding:14px 24px;
    border-radius:16px;
    text-decoration:none;
    background:rgba(15,23,42,.06);
    color:#334155;
    font-weight:700;
    transition:.3s;
}

a.cancel:hover{
    background:rgba(15,23,42,.12);
    transform:translateY(-3px);
}

/* Mobile */
@media(max-width:850px){
    .card{padding:28px}
    form{grid-template-columns:1fr}
    .logo-circle{width:72px;height:72px;font-size:20px}
    h2{font-size:22px}
}
</style>
</head>

<body>

<div class="card">

<div class="header">
    <div class="logo-circle">EP</div>
    <div>
        <h2>Edit Profile</h2>
            </div>
</div>

<%
UserInfo u = (UserInfo)session.getAttribute("user");
if(u != null){
%>

<form action="updateUserProfile" method="post" autocomplete="off">

<div class="field">
    <label>Username</label>
    <span class="icon">👤</span>
    <input type="text" name="username" value="<%=u.getUsername()%>" required>
</div>

<div class="field">
    <label>Email</label>
    <span class="icon">✉</span>
    <input type="email" name="email" value="<%=u.getEmail()%>" required>
</div>

<div class="full field">
    <label>Password</label>
    <span class="icon">🔒</span>
    <input type="password" name="password" value="<%=u.getPassword()%>" required>
</div>

<div class="full actions">
    <input type="submit" value="✨ Update Profile">
    <a class="cancel" href="home">Cancel</a>
</div>

</form>

<%
}
%>

</div>

</body>
</html>

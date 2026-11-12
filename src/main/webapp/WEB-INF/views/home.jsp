<%@page import="com.blog.pojo.Post"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Layout</title>

<style>


:root{
  --bg: #eef6ff;            
  --card: #ffffff;
  --muted: #6b7280;
  --accent: #3b82f6;
  --accent-2: #06b6d4;
  --shadow: 0 8px 28px rgba(15,23,42,0.06);
  --radius: 12px;
  --gap: 18px;
  --sidebar-w: 260px;
  --max-content-w: 1100px;
  --glass: rgba(255,255,255,0.55);
  --gradient-a: linear-gradient(120deg, rgba(59,130,246,0.18), rgba(14,165,164,0.12));
  --gradient-b: radial-gradient(1200px 600px at 10% 10%, rgba(59,130,246,0.08), transparent 20%),
               radial-gradient(1000px 500px at 90% 90%, rgba(14,165,164,0.06), transparent 18%);
}


@keyframes flowGradient {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

* { box-sizing: border-box; }
html,body{ height:100%; }
body {
    margin: 0;
    padding: 0;
    font-family: "Inter", "Segoe UI", Arial, sans-serif;
    color: #0f1720;
    -webkit-font-smoothing:antialiased;
    -moz-osx-font-smoothing:grayscale;

   
    background-color: var(--bg);
    background-image:
     
      linear-gradient(135deg, rgba(255,255,255,0.6) 25%, rgba(0,0,0,0.02) 25%, rgba(0,0,0,0.02) 50%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0.6) 75%, rgba(0,0,0,0.02) 75%, rgba(0,0,0,0.02) 100%),
     
      linear-gradient(90deg, rgba(59,130,246,0.10), rgba(14,165,164,0.06) 35%, rgba(236,72,153,0.04) 70%),
     
      radial-gradient(900px 400px at 10% 25%, rgba(59,130,246,0.06), transparent 20%),
      radial-gradient(800px 450px at 85% 80%, rgba(14,165,164,0.05), transparent 18%),
     
      linear-gradient(180deg, #fbfdff 0%, var(--bg) 100%);
    background-blend-mode: overlay, screen, screen, normal, normal;
    background-size: 120% 120%, 200% 200%, 1200px 600px, 1000px 500px, auto;
    background-position: 0% 50%, 0% 50%, 10% 10%, 90% 90%, 0 0;
    animation: flowGradient 18s linear infinite;

   
    min-height:100vh;
}


body::before {
  content: "";
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  background:
    radial-gradient(400px 200px at 6% 12%, rgba(255,255,255,0.18), transparent 20%),
    radial-gradient(420px 260px at 92% 88%, rgba(255,255,255,0.12), transparent 18%),
    radial-gradient(300px 180px at 50% 5%, rgba(255,255,255,0.06), transparent 20%);
  filter: blur(28px) saturate(105%);
  opacity: 0.95;
}


.container, .sidebar, .main { position: relative; z-index: 1; }


@media (prefers-reduced-motion: reduce) {
  body { animation: none; background-size: cover; }
  body::before { filter: none; transition: none; }
}



.container {
  display: flex;
  min-height: 100vh;
}


.sidebar {
    width: var(--sidebar-w);
    background: linear-gradient(180deg, rgba(255,255,255,0.85), rgba(255,255,255,0.72));
    border-right: 1px solid rgba(15,23,42,0.04);
    padding: 28px 20px;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    gap: 18px;
    align-items: center;
    backdrop-filter: blur(6px) saturate(110%);
    box-shadow: 0 6px 24px rgba(2,6,23,0.04);
}

.sidebar .dp {
    width: 118px;
    height: 118px;
    border-radius: 50%;
    overflow: hidden;
    display: grid;
    place-items: center;
    background: linear-gradient(135deg,#f8fafc,#ffffff);
    border: 4px solid rgba(59,130,246,0.06);
    box-shadow: 0 8px 20px rgba(59,130,246,0.06);
}

.sidebar .dp img {
    width:100%;
    height:100%;
    object-fit: cover;
}


.sidebar h3 {
    margin: 0;
    font-size: 1.05rem;
    font-weight: 700;
    color: #0f1720;
    text-align: center;
}


.nav {
  width:100%;
  display:flex;
  flex-direction:column;
  gap:10px;
  margin-top:12px;
  align-items:center;
}
.nav a {
    width: 92%;
    text-align: center;
    text-decoration: none;
    padding: 10px 12px;
    background: linear-gradient(90deg, rgba(59,130,246,0.06), rgba(14,165,164,0.03));
    border-radius: 10px;
    font-weight: 700;
    font-size: 0.98rem;
    color: #0b1220;
    border: 1px solid rgba(11,17,32,0.04);
    transition: transform .18s ease, box-shadow .18s ease;
}
.nav a:hover {
    transform: translateY(-4px);
    box-shadow: 0 18px 40px rgba(2,6,23,0.06);
}


.main {
  margin-left: var(--sidebar-w);
  width: calc(100% - var(--sidebar-w));
  padding: 32px;
}


.topbar {
    display:flex;
    align-items:center;
    justify-content:space-between;
    gap: 12px;
    max-width: var(--max-content-w);
    margin: 0 auto 18px auto;
    height: 64px;
}
.topbar .title {
    font-size: 1.15rem;
    font-weight: 700;
}
.topbar .sub {
    color: var(--muted);
    font-weight:600;
}


.subbar {
    max-width: var(--max-content-w);
    margin: 0 auto 22px auto;
    padding: 12px 18px;
    border-radius: 12px;
    background: linear-gradient(90deg, rgba(59,130,246,0.04), rgba(14,165,164,0.02));
    border: 1px solid rgba(11,17,32,0.03);
    color: #0b1220;
    font-weight:600;
}


.content {
    max-width: var(--max-content-w);
    margin: 18px auto;
    padding: 6px;
}


.content table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    background: none;
    margin-bottom: var(--gap);
}


.content td {
    padding: 0;
}

.post-card {
    margin: 12px 0;
    background: var(--card);
    border-radius: var(--radius);
    box-shadow: var(--shadow);
    overflow: hidden;
    border: 1px solid rgba(2,6,23,0.04);
    display: flex;
    gap: 14px;
    align-items: flex-start;
    padding: 18px;
    transition: transform .16s ease, box-shadow .16s ease;
}
.post-card:hover {
    transform: translateY(-6px);
    box-shadow: 0 26px 60px rgba(2,6,23,0.08);
}


.post-meta {
    flex: 1 1 auto;
}
.post-meta p {
    margin: 0 0 12px 0;
    line-height: 1.6;
    color: #111827;
    font-size: 0.98rem;
}


.post-media {
    width: 320px;
    max-width: 36%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.post-media img {
    width: 100%;
    height: auto;
    border-radius: 10px;
    display: block;
    object-fit: cover;
}


.post-actions {
    margin-top: 10px;
    display:flex;
    gap:10px;
    align-items:center;
}
.post-actions a {
    text-decoration:none;
    padding: 8px 12px;
    border-radius: 8px;
    font-weight:700;
    border: 1px solid rgba(2,6,23,0.06);
    background: linear-gradient(180deg, #fff, #fbfdff);
    transition: transform .12s ease, box-shadow .12s ease;
    color: #0b1220;
}
.post-actions a:hover { transform: translateY(-3px); box-shadow: 0 10px 30px rgba(2,6,23,0.05); }


@media (max-width: 880px){
  .sidebar { position: relative; width:100%; height:auto; flex-direction:row; padding:12px; gap:12px; justify-content:flex-start; }
  .main{ margin-left:0; width:100%; padding:18px; }
  .container{ flex-direction:column; }
  .topbar{ flex-direction:column; align-items:flex-start; gap:6px; padding:0 6px; }
  .subbar { margin-left:0; padding:10px; }
  .post-card { flex-direction:column; }
  .post-media { width:100%; max-width:100%; }
}


.dp-fallback {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  font-weight:700;
  color: rgba(2,6,23,0.5);
  background: linear-gradient(135deg,#eef2ff,#ffffff);
}
</style>

</head>
<body>

<div class="container">

  
  <div class="sidebar">
      <%
          String dp = (String) session.getAttribute("dp");
          String username = (String) session.getAttribute("username");
      %>

      <div class="dp">
          <% if (dp != null) { %>
              <img src="<%= request.getContextPath() %>/resources/images/<%= dp %>" alt="dp">
          <% } else { %>
              <div class="dp-fallback"><%= (username != null && username.length() > 0) ? ("" + Character.toUpperCase(username.charAt(0))) : "U" %></div>
          <% } %>
      </div>

      <h3><%= username %></h3>

      <div class="nav">
        <a href="home">Home</a>
        <a href="editProfile">Edit Profile</a>
        <a href="post">Blog</a>
        <a href="showAllPosts">Show All Posts</a>
        <a href="logout" style="color:#dc2626; font-weight:800;">
        Logout
       </a>
      </div>
  </div>

  
  <div class="main">
      
      <div class="topbar">
          <div class="title">Welcome — <%= username %></div>
          <div class="sub">Manage and view posts</div>
      </div>

      
      <div class="subbar">Blogging System</div>

     
      <div class="content">

        <%
            List<Post> posts = (List<Post>) request.getAttribute("posts");

            if (posts != null && posts.size() > 0) {
                for (Post p : posts) {
        %>

       
        <table role="list">
          <tr>
            <td>
              <div class="post-card" role="listitem">
                  <div class="post-meta">
                      <p><%= p.getContent() %></p>

                     
                  </div>

                  <%
                      String media = p.getMedia();
                      if (media != null && !media.trim().isEmpty()) {
                  %>
                    <div class="post-media">
                        <img src="<%= request.getContextPath() %>/resources/images/<%= media %>" alt="post-image">
                    </div>
                  <%
                      }
                  %>

              </div>
            </td>
          </tr>
        </table>

        <%
                }
            } else {
        %>
            <p style="color:var(--muted); text-align:center; padding:24px; background:var(--card); border-radius:12px; box-shadow:var(--shadow);">No posts available.</p>
        <%
            }
        %>

      </div>
  </div>

</div>

</body>
</html>







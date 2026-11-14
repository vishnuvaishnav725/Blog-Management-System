<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,java.util.Set,com.blog.pojo.FeedUser" %>
<%@ page isELIgnored="false" %>
<%!
private String feedEsc(String value) {
    if (value == null) return "";
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;");
}
%>
<%
    String mode = (String) request.getAttribute("mode");
    if (mode == null) mode = "all";
    Set<Integer> followedIds = (Set<Integer>) request.getAttribute("followedIds");
    List<FeedUser> people = (List<FeedUser>) request.getAttribute("people");
    Integer currentUserId = (Integer) request.getAttribute("currentUserId");
    String username = (String) session.getAttribute("username");
    String dp = (String) session.getAttribute("dp");
    String dpUrl = dp != null && dp.startsWith("/profile-pictures/") ? dp : "/resources/images/" + (dp != null ? dp : "default-user.png");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Live Social Feed</title>
<style>
:root{--ink:#172033;--muted:#64748b;--blue:#315eea;--line:#e7ebf3;--card:#fff;--bg:#f3f6fb;--shadow:0 12px 35px rgba(28,45,88,.08)}
*{box-sizing:border-box}body{margin:0;background:linear-gradient(145deg,#eef4ff,#f8fafc 52%,#eef7f7);color:var(--ink);font:15px/1.5 "Segoe UI",Arial,sans-serif}.layout{max-width:1220px;margin:auto;padding:28px 18px;display:grid;grid-template-columns:230px minmax(0,1fr) 260px;gap:20px}.panel,.card{background:var(--card);border:1px solid var(--line);border-radius:18px;box-shadow:var(--shadow)}.sidebar{padding:20px;height:max-content;position:sticky;top:18px}.brand{font-weight:800;font-size:18px;margin-bottom:18px}.userbox{text-align:center;padding:10px 0 18px;border-bottom:1px solid var(--line)}.avatar{width:64px;height:64px;border-radius:50%;object-fit:cover;background:#e6edff;display:inline-grid;place-items:center;color:#315eea;font-weight:800}.nav{display:grid;gap:8px;margin-top:16px}.nav a{color:#334155;text-decoration:none;padding:10px 12px;border-radius:10px}.nav a.active,.nav a:hover{background:#edf2ff;color:#244dc9;font-weight:700}.main{min-width:0}.heading{padding:8px 4px 16px}.heading h1{font-size:28px;margin:0 0 4px}.heading p{margin:0;color:var(--muted)}.tabs{display:flex;gap:8px;padding:6px;background:#e9eef9;border-radius:12px;width:max-content;margin-bottom:14px}.tabs a{padding:8px 14px;text-decoration:none;border-radius:9px;color:#475569;font-weight:700}.tabs a.active{background:white;color:var(--blue);box-shadow:0 2px 8px #16294d12}.sync{font-size:12px;color:var(--muted);margin:0 0 12px 4px}.feed{display:grid;gap:14px}.card{padding:18px}.right{padding:18px;height:max-content;position:sticky;top:18px}.right h2{font-size:16px;margin:0 0 6px}.hint{color:var(--muted);font-size:13px;margin:0 0 14px}.person{display:flex;align-items:center;gap:10px;padding:10px 0;border-top:1px solid var(--line)}.person .avatar{width:40px;height:40px;font-size:13px}.person-name{flex:1;min-width:0;font-weight:700;overflow:hidden;text-overflow:ellipsis}.follow-btn,.action-btn,.comment-submit{border:0;border-radius:9px;padding:8px 12px;background:#edf2ff;color:#244dc9;font-weight:700;cursor:pointer}.follow-btn:hover,.action-btn:hover{filter:brightness(.97)}.follow-btn.following{background:#eef2f7;color:#475569}.post-head{display:flex;align-items:center;gap:10px}.post-head .avatar{width:44px;height:44px}.post-author{font-weight:800}.post-time{font-size:12px;color:var(--muted)}.post-copy{white-space:pre-wrap;overflow-wrap:anywhere;margin:16px 0}.post-image{max-width:100%;max-height:440px;object-fit:contain;border-radius:12px;background:#f1f5f9}.post-actions{display:flex;gap:10px;align-items:center;border-top:1px solid var(--line);padding-top:12px;margin-top:12px}.action-btn{background:transparent;color:#475569}.action-btn.liked{color:#e11d48}.comments{margin-top:10px;display:grid;gap:8px}.comment{background:#f7f9fc;border-radius:10px;padding:9px 11px;font-size:13px}.comment strong{margin-right:6px}.comment-form{display:flex;gap:8px;margin-top:12px}.comment-form input{flex:1;min-width:0;border:1px solid var(--line);border-radius:10px;padding:10px 12px;font:inherit}.comment-submit{background:var(--blue);color:#fff}.empty{text-align:center;padding:30px 16px;color:var(--muted)}.back{color:#64748b;text-decoration:none;font-size:13px}@media(max-width:900px){.layout{grid-template-columns:190px minmax(0,1fr)}.right{grid-column:2;position:static;grid-row:2}.main{grid-column:2;grid-row:1}.sidebar{grid-row:1 / span 2}}@media(max-width:620px){.layout{display:flex;flex-direction:column;padding:12px}.sidebar{position:static}.userbox{display:none}.nav{display:flex;overflow:auto}.nav a{white-space:nowrap}.right{order:3}.heading h1{font-size:24px}}
</style>
</head>
<body>
<div class="layout">
  <aside class="panel sidebar">
    <div class="brand">✦ Blogger</div>
    <div class="userbox"><img class="avatar" src="<%= request.getContextPath() + feedEsc(dpUrl) %>" alt="Profile picture"><div><strong><%= feedEsc(username) %></strong></div></div>
    <nav class="nav">
      <a href="<%=request.getContextPath()%>/home">Home</a>
      <a class="active" href="<%=request.getContextPath()%>/liveFeed">Live Social Feed</a>
      <a href="<%=request.getContextPath()%>/editProfile">Edit Profile</a>
      <a href="<%=request.getContextPath()%>/post">Blog</a>
      <a href="<%=request.getContextPath()%>/logout">Logout</a>
    </nav>
  </aside>
  <main class="main">
    <header class="heading"><h1>Live Social Feed</h1><p>Latest posts from the community and people you follow.</p></header>
    <nav class="tabs">
      <a class="<%= "all".equals(mode) ? "active" : "" %>" href="<%=request.getContextPath()%>/liveFeed?mode=all">All posts</a>
      <a class="<%= "following".equals(mode) ? "active" : "" %>" href="<%=request.getContextPath()%>/liveFeed?mode=following">Following</a>
    </nav>
    <p class="sync" id="feed-sync">Auto-refresh is on · updates every 20 seconds</p>
    <section class="feed" id="feed-items" aria-live="polite"><article class="card empty">Loading posts…</article></section>
    <p><a class="back" href="<%=request.getContextPath()%>/home">← Back to home</a></p>
  </main>
  <aside class="panel right">
    <h2>People to follow</h2><p class="hint">Follow an author to see their posts in your Following feed.</p>
    <% if (people == null || people.isEmpty()) { %><p class="hint">No other active users yet.</p><% } else { for (FeedUser person : people) { boolean following = followedIds != null && followedIds.contains(person.getId()); String personDp = person.getDp(); String personDpUrl = personDp != null && personDp.startsWith("/profile-pictures/") ? personDp : "/resources/images/" + (personDp != null ? personDp : "default-user.png"); %>
      <div class="person"><img class="avatar" src="<%=request.getContextPath()+feedEsc(personDpUrl)%>" alt=""><span class="person-name"><%=feedEsc(person.getUsername())%></span><form method="post" action="<%=request.getContextPath()%>/toggleFollow"><input type="hidden" name="targetId" value="<%=person.getId()%>"><input type="hidden" name="mode" value="<%=feedEsc(mode)%>"><button class="follow-btn <%=following?"following":""%>" type="submit"><%=following?"Following":"Follow"%></button></form></div>
    <% } } %>
  </aside>
</div>
<script>
(function(){
  const ctx='<%=request.getContextPath()%>';
  const mode='<%=feedEsc(mode)%>';
  const feed=document.getElementById('feed-items');
  const sync=document.getElementById('feed-sync');
  async function refreshFeed(){
    try{
      const response=await fetch(ctx+'/liveFeedFragment?mode='+encodeURIComponent(mode),{headers:{'X-Requested-With':'XMLHttpRequest'}});
      if(response.status===401||response.redirected){window.location.href=ctx+'/login';return;}
      if(!response.ok) throw new Error('feed_refresh_failed');
      feed.innerHTML=await response.text();
      sync.textContent='Updated just now · auto-refresh every 20 seconds';
    }catch(e){sync.textContent='Could not refresh right now. Retrying soon.';}
  }
  document.addEventListener('submit',async function(event){
    const likeForm=event.target.closest('.feed-like-form');
    if(likeForm){
      event.preventDefault();
      const button=likeForm.querySelector('button');
      button.disabled=true;
      try{
        const response=await fetch(ctx+'/addLikeAjax',{method:'POST',body:new URLSearchParams(new FormData(likeForm)),headers:{'X-Requested-With':'XMLHttpRequest'}});
        if(response.status===401){window.location.href=ctx+'/login';return;}
        const data=await response.json();if(!response.ok)throw new Error('like_failed');
        likeForm.querySelector('.like-count').textContent=data.count;
        const liked=button.classList.toggle('liked');button.setAttribute('aria-pressed',String(liked));button.textContent=liked?'♥ Liked':'♡ Like';
      }catch(e){sync.textContent='Like could not be saved. Please try again.';}finally{button.disabled=false;}
      return;
    }
    const commentForm=event.target.closest('.feed-comment-form');
    if(commentForm){
      event.preventDefault();const input=commentForm.querySelector('input[name="comment_text"]');const button=commentForm.querySelector('button');
      if(!input.value.trim())return;button.disabled=true;
      try{const response=await fetch(ctx+'/addFeedComment',{method:'POST',body:new FormData(commentForm),headers:{'X-Requested-With':'XMLHttpRequest'}});if(response.status===401){window.location.href=ctx+'/login';return;}const data=await response.json();if(!response.ok)throw new Error(data.error||'comment_failed');input.value='';await refreshFeed();}
      catch(e){sync.textContent=e.message==='comment_length'?'Comment must be between 1 and 500 characters.':'Comment could not be saved. Please try again.';}finally{button.disabled=false;}
    }
  });
  refreshFeed();window.setInterval(refreshFeed,20000);
})();
</script>
</body>
</html>

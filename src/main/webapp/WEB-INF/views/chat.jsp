<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Private messages</title>
<link rel="stylesheet" href="<c:url value='/resources/css/chat.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/chat-mobile.css'/>">
</head>
<body>
<main class="chat-app ${not empty peer ? 'has-peer' : ''}">
  <aside class="chat-sidebar" id="chatSidebar">
    <header class="sidebar-head"><a href="<c:url value='/home'/>" class="back-link">← Home</a><h1>Messages</h1></header>
    <form class="user-search" action="<c:url value='/chat'/>" method="get">
      <label for="userSearch">Find a person</label>
      <input id="userSearch" type="search" name="q" value="<c:out value='${search}'/>" placeholder="Search users">
      <button type="submit">Search</button>
    </form>
    <c:if test="${not empty searchUsers}">
      <section class="search-results" aria-label="Users">
        <h2>People</h2>
        <c:forEach items="${searchUsers}" var="person">
          <a class="person-result" href="<c:url value='/chat'><c:param name='peerId' value='${person.id}'/></c:url>">
            <span class="avatar"><c:out value="${person.username.substring(0, 1).toUpperCase()}"/></span>
            <span><strong><c:out value="${person.username}"/></strong><small>Start a private chat</small></span>
          </a>
        </c:forEach>
      </section>
    </c:if>
    <section class="conversation-section"><h2>Recent chats</h2><div id="conversationList"><%@ include file="chatConversations.jsp" %></div></section>
  </aside>
  <section class="chat-main">
    <c:choose>
      <c:when test="${not empty peer}">
        <header class="chat-header">
          <button class="mobile-back" type="button" id="mobileBack" aria-label="Back to chats">←</button>
          <span class="avatar avatar-large"><c:out value="${peer.username.substring(0, 1).toUpperCase()}"/></span>
          <div><h2><c:out value="${peer.username}"/></h2><p>Private conversation</p></div>
        </header>
        <c:if test="${not empty error}"><div class="notice error"><c:out value="${error}"/></div></c:if>
        <div class="messages" id="messages"><%@ include file="chatMessages.jsp" %></div>
        <form class="composer" id="messageForm" action="<c:url value='/chat/send'/>" method="post" enctype="multipart/form-data">
          <input type="hidden" name="peerId" value="${peer.id}">
          <input type="hidden" name="clientToken" id="clientToken" value="">
          <input type="hidden" name="csrfToken" value="<c:out value='${csrfToken}'/>">
          <label class="upload-button" for="imageUpload" title="Attach an image" aria-label="Attach an image">＋</label>
          <input id="imageUpload" class="file-input" type="file" name="image" accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp">
          <textarea id="messageInput" name="message" maxlength="2000" rows="1" placeholder="Write a message…" aria-label="Message"></textarea>
          <button class="send-button" type="submit">Send</button>
          <div class="file-name" id="fileName"></div>
        </form>
        <div class="composer-hint">Enter to send · Shift + Enter for a new line · Images up to 5 MB</div>
      </c:when>
      <c:otherwise>
        <div class="empty-chat"><div class="empty-icon">✉</div><h2>Your private messages</h2><p>Choose someone from the list or search for a person to start a conversation.</p></div>
      </c:otherwise>
    </c:choose>
  </section>
</main>
<script>
(function(){
  const context = '<c:url value="/"/>' .replace(/\/$/, '');
  const peerId = '${peer.id}';
  const messages = document.getElementById('messages');
  const sidebar = document.getElementById('chatSidebar');
  const form = document.getElementById('messageForm');
  const input = document.getElementById('messageInput');
  const file = document.getElementById('imageUpload');
  const token = document.getElementById('clientToken');
  const fileName = document.getElementById('fileName');
  const nearBottom = el => el.scrollHeight - el.scrollTop - el.clientHeight < 130;
  const scrollBottom = () => { if (messages) messages.scrollTop = messages.scrollHeight; };
  if (messages) scrollBottom();
  if (file) file.addEventListener('change', () => { fileName.textContent = file.files.length ? file.files[0].name : ''; });
  if (input) input.addEventListener('keydown', e => {
    if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); form.requestSubmit(); }
  });
  if (form) form.addEventListener('submit', e => {
    const text = input.value.trim();
    if (!text && (!file.files || !file.files.length)) { e.preventDefault(); input.focus(); return; }
    if (window.crypto && crypto.randomUUID) token.value = crypto.randomUUID();
    else token.value = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => { const r=Math.random()*16|0; return (c==='x'?r:(r&3|8)).toString(16); });
    form.querySelector('.send-button').disabled = true;
  });
  const refresh = () => {
    if (!messages || !peerId) return;
    const shouldScroll = nearBottom(messages);
    fetch(context + '/chat/threadFragment?peerId=' + encodeURIComponent(peerId) + '&_=' + Date.now(), {credentials:'same-origin', cache:'no-store'})
      .then(r => { if (r.ok) return r.text(); throw new Error('refresh failed'); })
      .then(html => { messages.innerHTML = html; if (shouldScroll) scrollBottom(); })
      .catch(() => {});
    fetch(context + '/chat/conversationsFragment?peerId=' + encodeURIComponent(peerId) + '&_=' + Date.now(), {credentials:'same-origin', cache:'no-store'})
      .then(r => r.ok ? r.text() : null).then(html => { if (html) document.getElementById('conversationList').innerHTML = html; }).catch(() => {});
  };
  if (messages) window.setInterval(refresh, 3500);
  document.getElementById('mobileBack')?.addEventListener('click', () => sidebar.classList.add('show-mobile'));
  document.querySelectorAll('.person-result,.conversation-link').forEach(link => link.addEventListener('click', () => sidebar.classList.remove('show-mobile')));
})();
</script>
</body>
</html>

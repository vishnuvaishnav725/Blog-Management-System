<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach items="${messages}" var="message">
  <div class="message-row ${message.sender.id == me.id ? 'mine' : 'theirs'}">
    <article class="bubble">
      <c:if test="${not empty message.message}"><p class="message-text"><c:out value="${message.message}"/></p></c:if>
      <c:if test="${not empty message.imagePath}"><img class="chat-image" src="<c:url value='/chat/image/${message.id}'/>" alt="Image sent in chat" loading="lazy"></c:if>
      <footer><time><fmt:formatDate value="${message.createdAt}" pattern="dd MMM, hh:mm a"/></time>
        <c:if test="${message.sender.id == me.id}"><span class="delivery ${message.seen ? 'seen' : ''}"><c:out value="${message.seen ? 'Seen' : 'Sent'}"/></span></c:if>
      </footer>
    </article>
  </div>
</c:forEach>
<c:if test="${empty messages}"><div class="thread-empty">No messages yet. Say hello 👋</div></c:if>

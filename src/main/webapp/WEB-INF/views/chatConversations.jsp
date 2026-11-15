<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
  <c:when test="${not empty conversations}">
    <c:forEach items="${conversations}" var="conversation">
      <a class="conversation-link ${(not empty peer and peer.id == conversation.user.id) or (not empty activePeerId and activePeerId == conversation.user.id) ? 'active' : ''}" href="<c:url value='/chat'><c:param name='peerId' value='${conversation.user.id}'/></c:url>">
        <span class="avatar"><c:out value="${conversation.user.username.substring(0, 1).toUpperCase()}"/></span>
        <span class="conversation-copy"><strong><c:out value="${conversation.user.username}"/></strong>
          <small><c:choose><c:when test="${not empty conversation.latestMessage.message}"><c:out value="${conversation.latestMessage.message}"/></c:when><c:otherwise>📷 Image</c:otherwise></c:choose></small></span>
        <span class="conversation-meta"><time><fmt:formatDate value="${conversation.latestMessage.createdAt}" pattern="hh:mm a"/></time>
          <c:if test="${conversation.unreadCount gt 0}"><b class="unread-badge"><c:out value="${conversation.unreadCount}"/></b></c:if></span>
      </a>
    </c:forEach>
  </c:when>
  <c:otherwise><p class="no-conversations">No conversations yet.</p></c:otherwise>
</c:choose>

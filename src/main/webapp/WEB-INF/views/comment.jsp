<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Comment Page</title>


<style>
    body {
        font-family: "Segoe UI", Arial, sans-serif;
        height: 100vh;
        margin: 0;
        display: flex;
        justify-content: center;
        align-items: center;

       
        background: linear-gradient(135deg, #667eea, #764ba2);
    }

    .container {
        width: 400px;
        padding: 35px;
        border-radius: 15px;

       
        background: rgba(255, 255, 255, 0.15);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);

        box-shadow: 0 8px 25px rgba(0,0,0,0.2);
        text-align: center;
        color: white;
        border: 1px solid rgba(255, 255, 255, 0.2);
    }

    h2 {
        font-size: 26px;
        margin-bottom: 20px;
        color: #ffffff;
        font-weight: 600;
    }

    input[type="text"] {
        width: 88%;
        padding: 13px;
        border-radius: 8px;
        border: none;
        font-size: 16px;
        margin-top: 10px;

       
        background: rgba(255,255,255,0.3);
        color: #fff;

        transition: 0.3s;
    }

    input[type="text"]::placeholder {
        color: #f0f0f0;
    }

    input[type="text"]:focus {
        background: rgba(255,255,255,0.45);
        outline: none;
        box-shadow: 0 0 10px rgba(255,255,255,0.3);
    }

    input[type="submit"] {
        margin-top: 25px;
        padding: 13px 25px;
        font-size: 16px;
        border: none;
        border-radius: 8px;
        cursor: pointer;

       
        background: linear-gradient(135deg, #ff9966, #ff5e62);
        color: white;
        font-weight: bold;
        transition: 0.3s;
    }

    input[type="submit"]:hover {
        transform: scale(1.05);
        box-shadow: 0 4px 15px rgba(0,0,0,0.3);
    }

    .msg {
        margin-bottom: 10px;
        font-weight: bold;
        color: #d1ffc8;
    }
</style>
</head>
<body>

<div class="container">

<h2>Write a Comment</h2>

<%
String msg = (String)request.getParameter("msg");
if(msg!=null){
	out.println("<div class='msg'>" + msg + "</div>");
}
%>

<form action="${pageContext.request.contextPath}/comment" method="post">

    <input type="hidden" name="postId" value="${postId}"><br><br>

    <input type="text" name="comment_text" placeholder="Write Comment" required><br><br>

    <input type="submit" value="Comment Here"><br><br>
 <center> <a href="home">Back To Home Page</a> </center>
</form>

</div>

</body>
</html>

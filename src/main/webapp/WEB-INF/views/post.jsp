<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>Add New Post</title>

<style>

* {
    box-sizing: border-box;
}

body {
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
    background: #f4f6fb;
}


/* Main Container */

.container {
    width: 600px;
    max-width: 90%;
    margin: 50px auto;
    background: white;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.12);
}


/* Heading */

h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #222;
}


/* Label */

label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
    color: #333;
}


/* Text Input */

input[type="text"] {
    width: 100%;
    padding: 12px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 7px;
    font-size: 15px;
}


/* Textarea */

textarea {
    width: 100%;
    min-height: 180px;
    padding: 12px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 7px;
    font-size: 15px;
    resize: vertical;
}


/* File Input */

input[type="file"] {
    width: 100%;
    padding: 10px;
    margin-bottom: 25px;
    border: 1px solid #ccc;
    border-radius: 7px;
    background: #fafafa;
}


/* Button */

button {
    width: 100%;
    padding: 13px;
    border: none;
    border-radius: 7px;
    background: #2563eb;
    color: white;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
}

button:hover {
    background: #1d4ed8;
}


/* Message */

.message {
    text-align: center;
    margin-bottom: 20px;
    padding: 10px;
    background: #dcfce7;
    color: #166534;
    border-radius: 7px;
}


/* Mobile */

@media(max-width: 600px) {

    .container {
        width: 95%;
        margin: 25px auto;
        padding: 20px;
    }

}

</style>

</head>


<body>


<div class="container">


    <h2>Add New Post</h2>


    <%
        String msg = (String) request.getAttribute("msg");

        if (msg != null) {
    %>

        <div class="message">
            <%= msg %>
        </div>

    <%
        }
    %>


    <!-- =========================
         ADD POST FORM
    ========================== -->

    <form action="${pageContext.request.contextPath}/addPost"
          method="post"
          enctype="multipart/form-data">


        <!-- TITLE -->

        <label for="title">
            Title
        </label>

        <input type="text"
               id="title"
               name="title"
               placeholder="Enter post title"
               required>


        <!-- CONTENT -->

        <label for="content">
            Content
        </label>

        <textarea id="content"
                  name="content"
                  placeholder="Write your post content..."
                  required></textarea>


        <!-- CATEGORY -->

        <label for="category">
            Category
        </label>

        <input type="text"
               id="category"
               name="category"
               placeholder="Enter category"
               required>


        <!-- IMAGE -->

        <label for="media">
            Image
        </label>

        <input type="file"
               id="media"
               name="media"
               accept="image/*">


        <!-- ADD POST BUTTON -->

        <button type="submit">
            Add Post
        </button>


    </form>


</div>


</body>

</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Home Page</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

    <style>
    * {
        margin: 0; 
        padding: 0; 
        box-sizing: border-box;
    }

    body {
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        font-family: 'Poppins', sans-serif;
        background: linear-gradient(135deg, #6EE7B7, #3B82F6, #9333EA);
        background-size: 400% 400%;
        animation: gradientFlow 12s ease infinite;
        padding: 20px;
        color: #fff;
    }

    @keyframes gradientFlow {
        0% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
        100% { background-position: 0% 50%; }
    }

    .wrapper {
        width: 90%;
        max-width: 900px;
        background: rgba(255, 255, 255, 0.18);
        border-radius: 25px;
        padding: 40px;
        box-shadow: 0 12px 45px rgba(0, 0, 0, 0.25);
        backdrop-filter: blur(18px);
        border: 1px solid rgba(255, 255, 255, 0.28);
    }

    .header-title {
        font-size: 32px;
        font-weight: 700;
        color: #ffffff;
        text-shadow: 0 2px 10px rgba(0,0,0,0.4);
    }

    .header-subtitle {
        margin-top: 5px;
        font-size: 15px;
        color: #e5e7eb;
    }

    .cards {
        margin-top: 30px;
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
        gap: 25px;
    }

    .box {
        padding: 25px;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.25);
        border: 1px solid rgba(255, 255, 255, 0.4);
        box-shadow: 0 8px 28px rgba(0,0,0,0.2);
        backdrop-filter: blur(10px);
        transition: 0.3s ease-in-out;
        cursor: pointer;
    }

    .box:hover {
        transform: translateY(-8px) scale(1.04);
        box-shadow: 0 15px 40px rgba(0,0,0,0.35);
        background: rgba(255, 255, 255, 0.35);
    }

    .box-icon {
        font-size: 40px;
        margin-bottom: 12px;
        text-shadow: 0 2px 12px rgba(0,0,0,0.4);
    }

    .box-title {
        font-size: 20px;
        font-weight: 600;
        color: #ffffff;
        text-shadow: 0 2px 10px rgba(0,0,0,0.3);
    }

    .box-desc {
        font-size: 14px;
        margin: 10px 0 16px;
        color: #f1f5f9;
        line-height: 1.5;
    }

    .box a {
        padding: 10px 18px;
        border-radius: 10px;
        display: inline-block;
        font-size: 14px;
        text-decoration: none;
        font-weight: 600;
        background: rgba(255, 255, 255, 0.35);
        color: #1e3a8a;
        backdrop-filter: blur(6px);
        transition: 0.25s ease-in-out;
    }

    .box a:hover {
        background: white;
        color: #2563eb;
        box-shadow: 0 5px 16px rgba(0,0,0,0.25);
    }

    /* ✅ Attractive Back To Home Button */
    .back-home {
        grid-column: 1 / -1;
        justify-self: center;
        margin-top: 10px;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 12px 28px;
        border-radius: 14px;
        font-size: 15px;
        font-weight: 600;
        text-decoration: none;
        color: #1e3a8a;
        background: rgba(255, 255, 255, 0.45);
        backdrop-filter: blur(8px);
        box-shadow: 0 8px 25px rgba(0,0,0,0.25);
        transition: all 0.3s ease;
    }

    .back-home:hover {
        background: #ffffff;
        color: #2563eb;
        transform: translateY(-4px);
        box-shadow: 0 14px 40px rgba(0,0,0,0.35);
    }
    </style>

</head>
<body>

<div class="wrapper">

    <div class="header">
        <div class="header-title">Admin Dashboard</div>
        <div class="header-subtitle">
            Manage your blog content and users from quick actions below.
        </div>
    </div>

    <div class="cards">

        <div class="box">
            <div class="box-icon">📝</div>
            <div class="box-title">Blogs Management</div>
            <div class="box-desc">Create, update and manage all blog posts in one place.</div>
            <a href="ManageBlog">Manage Blogs →</a>
        </div>

        <div class="box">
            <div class="box-icon">👥</div>
            <div class="box-title">Users Management</div>
            <div class="box-desc">View, edit and control registered users of the platform.</div>
            <a href="ManageUsers">Manage Users →</a>
        </div>

        <div class="box">
            <div class="box-icon">DM</div>
            <div class="box-title">Private Messages</div>
            <div class="box-desc">Open one-to-one conversations with registered users.</div>
            <a href="chat">Open Messages</a>
        </div>
        <!-- ✅ Attractive Back To Home Page Link -->
        <a href="home" class="back-home">⬅ Back To Home Page</a>

    </div>

</div>

</body>
</html>

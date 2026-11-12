<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Login</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

<style>
*{
  margin:0;
  padding:0;
  box-sizing:border-box;
  font-family:'Poppins',sans-serif;
}

body{
  min-height:100vh;
  display:flex;
  justify-content:center;
  align-items:center;
  background:linear-gradient(135deg,#667eea,#764ba2);
}

.container{
  width:100%;
  max-width:420px;
}

.card{
  background:rgba(255,255,255,0.95);
  padding:35px 30px;
  border-radius:18px;
  box-shadow:0 20px 45px rgba(0,0,0,0.25);
  animation:fadeIn .6s ease;
}

@keyframes fadeIn{
  from{opacity:0;transform:translateY(20px)}
  to{opacity:1;transform:translateY(0)}
}

.logo{
  width:70px;
  height:70px;
  margin:0 auto 15px;
  border-radius:50%;
  background:linear-gradient(135deg,#667eea,#764ba2);
  display:flex;
  align-items:center;
  justify-content:center;
  color:#fff;
  font-size:28px;
  font-weight:600;
}

.card h2{
  text-align:center;
  margin-bottom:6px;
}

.sub{
  text-align:center;
  font-size:13px;
  color:#6b7280;
  margin-bottom:20px;
}

.input-group{
  margin-bottom:15px;
}

.input-group label{
  font-size:13px;
  color:#555;
}

.input-box{
  position:relative;
}

.input-box i{
  position:absolute;
  top:50%;
  left:12px;
  transform:translateY(-50%);
  color:#9ca3af;
}

.input-box input{
  width:100%;
  padding:12px 14px 12px 38px;
  border-radius:10px;
  border:1px solid #e5e7eb;
  margin-top:6px;
  font-size:14px;
}

.input-box input:focus{
  outline:none;
  border-color:#667eea;
  box-shadow:0 6px 18px rgba(102,126,234,.25);
}

.btn{
  width:100%;
  margin-top:10px;
  padding:12px;
  border:none;
  border-radius:10px;
  background:linear-gradient(90deg,#667eea,#764ba2);
  color:#fff;
  font-weight:600;
  font-size:15px;
  cursor:pointer;
  transition:.3s;
}

.btn:hover{
  transform:translateY(-2px);
  box-shadow:0 10px 25px rgba(118,75,162,.4);
}

.error{
  background:#fee2e2;
  color:#b91c1c;
  padding:10px;
  border-radius:8px;
  font-size:13px;
  margin-bottom:12px;
  text-align:center;
}

.back-home{
  display:flex;
  align-items:center;
  justify-content:center;
  gap:8px;
  margin-top:16px;
  padding:10px;
  border-radius:999px;
  text-decoration:none;
  font-size:14px;
  font-weight:500;
  color:#667eea;
  border:1px solid #667eea;
  transition:.3s;
}

.back-home:hover{
  background:#667eea;
  color:#fff;
}
</style>
</head>

<body>

<div class="container">

  <div class="card">

    <div class="logo">L</div>

    <h2>Welcome Back</h2>
    <div class="sub">Login to your account</div>

    <%
      String msg = (String)request.getAttribute("msg");
      if(msg != null){
    %>
      <div class="error"><%= msg %></div>
    <%
      }
    %>

    <form action="checkUser" method="post">

      <div class="input-group">
        <label>Email</label>
        <div class="input-box">
          <i class="fa fa-envelope"></i>
          <input type="email" name="email" placeholder="Enter your email" required>
        </div>
      </div>

      <div class="input-group">
        <label>Password</label>
        <div class="input-box">
          <i class="fa fa-lock"></i>
          <input type="password" name="password" placeholder="Enter your password" required>
        </div>
      </div>

      <button class="btn">Sign In</button>

      <a href="index" class="back-home">
        <i class="fa fa-arrow-left"></i> Back to Home
      </a>

    </form>

  </div>

</div>

</body>
</html>

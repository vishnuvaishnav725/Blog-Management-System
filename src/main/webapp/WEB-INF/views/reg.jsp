<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>User Registration</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

<style>
:root{
  --primary:#6366f1;
  --secondary:#22d3ee;
  --accent:#f97316;
  --bg:#f4f6ff;
  --card:rgba(255,255,255,.85);
  --border:#e5e7eb;
  --text:#0f172a;
  --muted:#64748b;
}

*{margin:0;padding:0;box-sizing:border-box}

body{
  min-height:100vh;
  font-family:'Poppins',sans-serif;
  display:flex;
  align-items:center;
  justify-content:center;
  background:
    radial-gradient(circle at 20% 20%, #e0e7ff, transparent 40%),
    radial-gradient(circle at 80% 80%, #cffafe, transparent 45%),
    linear-gradient(180deg,#f8fafc,#eef2ff);
}

/* Layout */
.stage{
  width:100%;
  max-width:1000px;
  display:grid;
  grid-template-columns:1fr 440px;
  gap:32px;
  align-items:center;
}

/* Promo */
.promo{
  padding:36px;
  border-radius:20px;
  background:linear-gradient(135deg,rgba(99,102,241,.12),rgba(34,211,238,.08));
  box-shadow:0 30px 70px rgba(15,23,42,.08);
}

.brand{
  display:flex;
  align-items:center;
  gap:14px;
  margin-bottom:16px;
}

.brand .mark{
  width:58px;
  height:58px;
  border-radius:16px;
  background:linear-gradient(135deg,var(--primary),var(--secondary));
  display:flex;
  align-items:center;
  justify-content:center;
  color:white;
  font-weight:700;
  font-size:18px;
}

.promo h3{
  font-size:22px;
  margin-bottom:10px;
  color:var(--text);
}

.promo p{
  color:var(--muted);
  line-height:1.7;
}

/* Card */
.card{
  background:var(--card);
  backdrop-filter:blur(14px);
  border-radius:20px;
  padding:32px;
  border:1px solid rgba(255,255,255,.5);
  box-shadow:0 35px 80px rgba(15,23,42,.12);
}

.card h2{
  font-size:22px;
  margin-bottom:6px;
  color:var(--text);
}

.card .sub{
  font-size:13px;
  color:var(--muted);
  margin-bottom:18px;
}

/* Form */
.row{display:flex;gap:14px}
.field{margin-bottom:14px}

label{
  display:block;
  margin-bottom:6px;
  font-size:12px;
  font-weight:500;
  color:var(--muted);
}

input,select{
  width:100%;
  padding:13px 15px;
  border-radius:12px;
  border:1px solid var(--border);
  font-size:14px;
  background:#fff;
  transition:.25s;
}

input:focus,select:focus{
  outline:none;
  border-color:var(--primary);
  box-shadow:0 0 0 3px rgba(99,102,241,.15);
}

/* Password */
.password-box{position:relative}
.password-box .icon{
  position:absolute;
  right:14px;
  top:50%;
  transform:translateY(-50%);
  cursor:pointer;
}

/* File */
.file-input{
  display:flex;
  align-items:center;
  gap:14px;
}

.file-preview{
  width:72px;
  height:72px;
  border-radius:14px;
  background:#f8fafc;
  border:1px dashed #c7d2fe;
  display:flex;
  align-items:center;
  justify-content:center;
  overflow:hidden;
}

.file-preview img{
  width:100%;
  height:100%;
  object-fit:cover;
}

.file-label{
  padding:10px 14px;
  border-radius:12px;
  background:linear-gradient(135deg,#eef2ff,#f8fafc);
  border:1px dashed #c7d2fe;
  font-size:13px;
  cursor:pointer;
}

/* Button */
.actions{margin-top:10px}

.btn{
  width:100%;
  padding:14px;
  border-radius:14px;
  border:none;
  font-size:15px;
  font-weight:600;
  cursor:pointer;
  color:white;
  background:linear-gradient(135deg,var(--primary),var(--secondary));
  box-shadow:0 18px 40px rgba(99,102,241,.35);
  transition:.3s;
}

.btn:hover{
  transform:translateY(-3px);
  box-shadow:0 28px 60px rgba(99,102,241,.5);
}

/* Back */
.back-link{
  display:inline-block;
  margin-top:16px;
  padding:10px 18px;
  border-radius:12px;
  text-decoration:none;
  font-size:13px;
  color:var(--muted);
  border:1px solid #e2e8f0;
}

/* Message */
.msg{
  margin-bottom:14px;
  padding:10px;
  border-radius:10px;
  background:#fff6f6;
  color:#dc2626;
  border:1px solid #fca5a5;
}

/* Responsive */
@media(max-width:900px){
  .stage{grid-template-columns:1fr}
  .promo{order:2}
}
</style>
</head>

<body>

<div class="stage">

  <aside class="promo">
    <div class="brand">
      <div class="mark">BM</div>
      <div>
        <div style="font-weight:600">Blog Management</div>
        <div style="font-size:13px;color:var(--muted)">Create & manage authors securely</div>
      </div>
    </div>
    <h3>Welcome to the team 🚀</h3>
    <p>Create new author accounts with profile photos and secure credentials. Everything is fast, simple and safe.</p>
  </aside>

  <div class="card">
    <h2>User Registration</h2>
    <p class="sub">Fill the form below to add a new user</p>

    <%
      String msg = (String)request.getAttribute("msg");
      if(msg != null){
    %>
      <div class="msg"><%= msg %></div>
    <%
      }
    %>

    <form action="addUser" method="post" enctype="multipart/form-data">

      <div class="row">
        <div class="field" style="flex:1">
          <label>Username</label>
          <input type="text" name="username" required>
        </div>
        <div class="field" style="width:140px">
          <label>Role</label>
          <input type="text" name="role" required>
        </div>
      </div>

      <div class="field">
        <label>Email</label>
        <input type="email" name="email" required>
      </div>

      <div class="field password-box">
        <label>Password</label>
        <input id="passwordField" type="password" name="password" required>
        <span class="icon" onclick="togglePassword()">👁</span>
      </div>

      <div class="field">
        <label>Profile Photo</label>
        <div class="file-input">
          <label class="file-label" for="dpInput">Choose Image</label>
          <input id="dpInput" type="file" name="dp" accept="image/*" required hidden onchange="previewFile(event)">
          <div class="file-preview" id="preview">📷</div>
        </div>
      </div>

      <div class="actions">
        <input type="submit" class="btn" value="Register User">
      </div>
    </form>

    <a href="index" class="back-link">← Back to Index</a>
  </div>

</div>

<script>
function togglePassword(){
  const f = document.getElementById('passwordField');
  f.type = f.type === 'password' ? 'text' : 'password';
}

function previewFile(e){
  const file = e.target.files[0];
  if(!file) return;
  const reader = new FileReader();
  reader.onload = () => {
    document.getElementById('preview').innerHTML =
      '<img src="'+reader.result+'">';
  };
  reader.readAsDataURL(file);
}
</script>

</body>
</html>

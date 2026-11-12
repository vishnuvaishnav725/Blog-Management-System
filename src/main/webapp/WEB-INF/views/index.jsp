<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Happening now — Landing</title>
  <style>
    :root{
      --bg:#020617;
      --bg-soft:#050816;
      --text:#e5e7eb;
      --muted:#9ca3af;
      --accent:#38bdf8;
      --accent-soft:rgba(56,189,248,0.15);
      --pill-border:rgba(148,163,184,0.4);
      --radius:999px;
      --container-max:1100px;
      --shadow-soft:0 24px 60px rgba(15,23,42,0.9);
    }

    *{box-sizing:border-box;margin:0;padding:0}

    html,body{
      height:100%;
      font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif;
      background:radial-gradient(circle at top,#0f172a 0,#020617 55%,#000 100%);
      color:var(--text);
    }

    
    body::before,
    body::after{
      content:"";
      position:fixed;
      inset:auto;
      width:420px;
      height:420px;
      border-radius:50%;
      filter:blur(120px);
      opacity:0.5;
      pointer-events:none;
      z-index:-1;
    }
    body::before{
      background:#22c55e;
      top:-80px;
      left:-60px;
    }
    body::after{
      background:#38bdf8;
      bottom:-120px;
      right:-40px;
    }

    .wrap{
      min-height:100vh;
      display:flex;
      align-items:center;
      justify-content:center;
      padding:32px 16px;
    }

    .container{
      max-width:var(--container-max);
      width:100%;
      display:grid;
      grid-template-columns:1.1fr 0.9fr;
      gap:40px;
      align-items:center;
    }

    
    .logo-wrap{
      display:flex;
      align-items:center;
      justify-content:center;
      padding:40px 20px;
    }

    .logo{
      width:58vmin;
      max-width:520px;
      height:auto;
      filter:drop-shadow(0 30px 60px rgba(15,23,42,0.9));
      animation:floatLogo 6s ease-in-out infinite;
    }

    
    .panel{
      padding:10px 14px;
      position:relative;
    }

    .panel::before{
      content:"";
      position:absolute;
      inset:-1px;
      border-radius:32px;
      border:1px solid rgba(148,163,184,0.35);
      background:radial-gradient(circle at top left,rgba(56,189,248,0.3),transparent 55%);
      opacity:0.4;
      z-index:-1;
    }

    .panel::after{
      content:"";
      position:absolute;
      inset:0;
      border-radius:30px;
      background:linear-gradient(145deg,rgba(15,23,42,0.9),rgba(15,23,42,0.96));
      box-shadow:var(--shadow-soft);
      z-index:-2;
    }

    .eyebrow{
      font-weight:600;
      margin-bottom:10px;
      font-size:18px;
      color:var(--accent);
      letter-spacing:.09em;
      text-transform:uppercase;
    }

    h1{
      font-size:54px;
      line-height:0.98;
      margin:0 0 18px;
      font-weight:800;
      letter-spacing:-0.03em;
    }

    .subtitle{
      font-size:14px;
      color:var(--muted);
      max-width:320px;
      line-height:1.5;
    }

    .card{
      margin-top:18px;
      padding:3px;
      border-radius:26px;
      background:linear-gradient(135deg,rgba(56,189,248,0.32),rgba(15,23,42,0.9));
    }

    .card-inner{
      background:radial-gradient(circle at top,rgba(15,23,42,0.96),rgba(15,23,42,0.96));
      backdrop-filter:blur(26px);
      border-radius:24px;
      padding:18px 18px 20px;
      border:1px solid rgba(15,23,42,0.9);
    }

    .signup{
      margin-top:6px;
      display:flex;
      flex-direction:column;
      gap:10px;
    }

    .btn{
      display:inline-flex;
      align-items:center;
      justify-content:center;
      gap:10px;
      padding:13px 18px;
      border-radius:32px;
      border:1px solid var(--pill-border);
      background:rgba(15,23,42,0.7);
      cursor:pointer;
      font-weight:600;
      font-size:15px;
      text-decoration:none;
      box-shadow:0 10px 25px rgba(15,23,42,0.7);
      color:var(--text);
      transition:
        background .18s ease,
        transform .15s ease,
        box-shadow .18s ease,
        border-color .18s ease;
      position:relative;
      overflow:hidden;
    }

    .btn::before{
      content:"";
      position:absolute;
      inset:0;
      background:radial-gradient(circle at top left,rgba(248,250,252,0.16),transparent 55%);
      opacity:0;
      transition:opacity .2s ease;
    }

    .btn.primary{
      background:linear-gradient(135deg,#38bdf8,#0ea5e9);
      color:#0b1120;
      border-color:transparent;
    }

    .btn.primary:hover{
      background:linear-gradient(135deg,#0ea5e9,#06b6d4);
    }

    .btn:hover{
      transform:translateY(-1px) translateZ(0);
      box-shadow:0 16px 35px rgba(15,23,42,0.85);
      border-color:rgba(148,163,184,0.6);
    }

    .btn:hover::before{
      opacity:1;
    }

    .btn:active{
      transform:translateY(0);
      box-shadow:0 8px 20px rgba(15,23,42,0.9);
    }

    .btn + .btn{
      margin-top:4px;
    }

    .helper{
      margin-top:10px;
      font-size:12px;
      color:var(--muted);
      text-align:left;
    }

    .helper span{
      color:var(--accent);
      font-weight:500;
    }

   
    @media (max-width:1000px){
      .container{
        grid-template-columns:1fr;
        gap:28px;
      }
      .logo-wrap{
        order:-1;
        padding:10px 10px 24px;
      }
      .logo{
        width:46vmin;
        max-width:420px;
      }
      h1{
        font-size:42px;
      }
      .panel{
        padding:12px 14px;
      }
    }

    @media (max-width:640px){
      .wrap{
        padding:18px 10px 24px;
      }
      .logo{
        width:40vmin;
      }
      h1{
        font-size:34px;
      }
      .card-inner{
        padding:16px 14px 18px;
      }
      .btn{
        width:100%;
        padding-inline:16px;
      }
      .btn + .btn{
        margin-top:8px;
      }
    }

    @media (max-width:480px){
      .logo{display:none}
      .container{
        padding:0 2px;
      }
      .panel{
        padding:10px 6px;
      }
      .card-inner{
        padding:14px 10px 16px;
      }
    }

    .muted{color:var(--muted)}

    @keyframes floatLogo{
      0%,100%{transform:translateY(0) translateZ(0) rotate(-1deg);}
      50%{transform:translateY(-10px) translateZ(0) rotate(-3deg);}
    }
  </style>
</head>
<body>
  <div class="wrap">
    <div class="container">

     
      <div class="logo-wrap">
        <svg class="logo" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" aria-label="Blog Manager Logo">
          <defs>
           
            <linearGradient id="luxGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#38bdf8"/>
              <stop offset="50%" stop-color="#0ea5e9"/>
              <stop offset="100%" stop-color="#3b82f6"/>
            </linearGradient>

           
            <linearGradient id="goldGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#fde047"/>
              <stop offset="100%" stop-color="#f59e0b"/>
            </linearGradient>
          </defs>

         
          <circle cx="100" cy="100" r="85" fill="url(#luxGrad)" opacity=".25"/>

          
          <path d="M100 30 L150 55 V110 C150 140 130 165 100 175 C70 165 50 140 50 110 V55 Z"
                fill="#0f172a" stroke="url(#luxGrad)" stroke-width="3" />

          
          <path d="M80 60 L90 72 L100 58 L110 72 L120 60"
                fill="url(#goldGrad)" stroke="#fef08a" stroke-width="2" stroke-linejoin="round"/>

          
          <path d="M125 125 Q105 110 90 135 Q75 160 65 170 Q80 145 72 130 Q70 115 90 110 Q110 105 130 118 Z"
                fill="url(#luxGrad)" opacity="0.95"/>

         
          <text x="100" y="115" font-size="42" font-family="Poppins, sans-serif"
                fill="white" text-anchor="middle" font-weight="700">BM</text>
        </svg>
      </div>

     
      <div class="panel">
        <div class="eyebrow">Join today.</div>
        <h1>Happening now</h1>
        <p class="subtitle">
          Sign in or create an account to manage posts, authors, and everything behind your blog.
        </p>

        <div class="card">
          <div class="card-inner">
            <div class="signup">
              <a class="btn primary" href="login">Login Here</a>
              <a class="btn primary" href="reg">Registration Here</a>
            </div>
            <div class="helper">
              New here? <span>Start with registration</span> to create your admin or author account.
            </div>
          </div>
        </div>

      </div>

    </div>
  </div>
</body>
</html>

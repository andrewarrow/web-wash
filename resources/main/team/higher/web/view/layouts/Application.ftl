<#macro myLayout title="wash">
<!DOCTYPE html>
<html class="mdc-typography">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>WebWash</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="/css/grid5.css" rel="Stylesheet" type="text/css">
  </head>
  <body>
    <#if mobile == true>
          <div id="mobile-menu">
            <a href="/wash/dashboard">Dashboard</a>
            <a href="/wash/all">Wash</a>
            <a href="/wash/all">Menu</a>
            <a href="/wash/all">With</a>
            <a href="/wash/all">Choices</a>
          </div>
      <div class="right" style="margin: 0 0 0 10px;">
    <#else>
      <div class="menu">
        <img style="padding: 0 0 0 40px;" src="/images/logo/washLogo.png" srcset="/images/logo/washLogo.png 1x, /images/logo/washLogo@2x.png 2x"/>
        <ul>
            <li><a href="/wash/dashboard">Dashboard</a></li>
            <li><a href="/wash/all">Wash</a></li>
            <li><a href="/wash/all">Menu</a></li>
            <li><a href="/wash/all">With</a></li>
            <li><a href="/wash/all">Choices</a></li>
        </ul>
      </div>
      <div class="right">
    </#if>

        <div class="right-top">
         ${who} <a class="" onClick="doSignOut(event);" href="#">Logout</a>
        </div>
        <#nested/>
      </div>

    <script>
      function doSignOut(e) {
        e.preventDefault();
        document.getElementById('logout-form').submit();
      }
    </script>
    <form id="logout-form" method="POST" action="/wash/user/logout"></form>

    <script>
    setTimeout(function(){
       window.location.reload(1);
    }, 5*60*1000);
    </script>

  </body>
</html>
</#macro>

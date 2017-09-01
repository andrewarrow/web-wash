<!DOCTYPE html>
<html class="mdc-typography">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WebWash</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="css/grid5.css" rel="Stylesheet" type="text/css">
  </head>
  <body>
    <div id="content">
      wash
    </div>
    <#if id??>
      <script>
        document.location.href = '/wash/dashboard';
      </script>
    <#else>
      <form method="POST" action="/wash/user/login">
        <input name="username" value="" placeholder="username" />
        <br/>
        <input name="password" value="H4!b5at+kWls-8yh4Guq" placeholder="password" />
        <br/>
        <button>Login</button>
      </form>
    </#if>
  </body>
</html>

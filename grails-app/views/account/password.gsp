<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  %{--<title>Kies nieuw wachtwoord</title>--}%
</head>
<body>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>



  <g:form action="password">
    <input type="hidden" name="id" value="${account?.passwordCode}" />
    <table>
      <tbody>
        <tr>
          <td><g:message code="account.password"/></td>
          <td><input type="password" name="password" value="" /></td>
        </tr>
        <tr>
          <td><g:message code="application.repeat.password"/></td>
          <td><input type="password" name="password2" value="" /></td>
        </tr>
        %{--<tr>
          <td>Remember me?:</td>
          <td><g:checkBox name="rememberMe" value="${rememberMe}" /></td>
        </tr>--}%
        <tr>
          <td />
          <td><input type="submit" value="${g.message(code:'default.submit')}" /></td>
        </tr>
      </tbody>
    </table>
  </g:form>
</body>
</html>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="email.forgotpassword.title"/> </title>
</head>
<body>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>



  <g:form action="forgotpassword">
    <table>
      <tbody>
        <tr>
          <td><g:message code="account.username"/> </td>
          <td><input type="text" name="email" value="" /></td>
        </tr>
        <tr>
          <td />
          <td><input type="submit" value=${g.message(code:'default.submit')} /></td>
        </tr>
      </tbody>
    </table>
  </g:form>
</body>
</html>

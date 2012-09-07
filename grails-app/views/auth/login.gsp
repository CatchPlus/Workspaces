<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Workspaces</title>
</head>

<body>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<g:form action="signIn">
    <input type="hidden" name="targetUri" value="${targetUri}"/>
    <table>
        <tbody>
        <tr>
            <td><g:message code="account.username"/> :</td>
            <td><input type="text" name="username" value="${username}"/></td>
        </tr>
        <tr>
            <td><g:message code="account.password"/>:</td>
            <td><input type="password" name="password" value=""/></td>
        </tr>
        %{--<tr>
            <td>User:</td>
            <td><input type="text" name="activeShiroUser" value="${activeShiroUser}"/></td>
        </tr>--}%
        %{--<tr>
          <td>Remember me?:</td>
          <td><g:checkBox name="rememberMe" value="${rememberMe}" /></td>
        </tr>--}%
        <tr>
            <td/>
            <td><input type="submit" value="<g:message code='default.signin'/>"/></td>
        </tr>
        </tbody>
    </table>
</g:form>

<g:link action="forgotpassword" controller="account"><g:message code="application.forgot.password"/>?</g:link>

</body>
</html>

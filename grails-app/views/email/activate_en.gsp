<%--
  Created by IntelliJ IDEA.
  User: Michaell
  Date: 21-11-11
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html"%>
<html>
  <head><title>Simple GSP page</title></head>
  <body>

Dear Mr/Ms  ${account.lastName}, <br/><br/>

Please activate your account for Workspaces with the following link.<br/><br/>
  <a href="${activationUrl}">${activationUrl}</a><br/><br/>

Regards, workspaces.nl<br/><br/>

  </body>
</html>
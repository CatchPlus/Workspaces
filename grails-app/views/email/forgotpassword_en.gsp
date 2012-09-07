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

Dear Mr/Ms ${account.lastName},<br/><br/>

You have requested a new password for Workspaces<br/>

Please click the following link to request a new password<br/><br/>
  <a href="${activationUrl}">${activationUrl}</a><br/><br/>


Regards, workspaces.nl<br/><br/>


  </body>
</html>
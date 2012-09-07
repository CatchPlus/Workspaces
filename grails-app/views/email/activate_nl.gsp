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

Beste heer/mevrouw ${account.lastName}, <br/><br/>

Onlangs bent u op Workspaces aangemeld. Voordat u gebruik kunt maken van Workspaces moet uw aanmelding geactiveerd worden.<br/>
 Zo weten we zeker dat uw e-mailadres klopt. U kunt kunt op onderstaande  link drukken om uw account te activeren.<br/><br/>
 <a href="${activationUrl}">${activationUrl}</a><br/><br/>

Werkt deze link niet? Kopieer dan de link en plak deze in de adresbalk van uw browser.<br/><br/>

Met vriendelijke groet, workspaces.nl<br/><br/>

Ps: stuur geen reply op deze mail, deze wordt niet gelezen.<br/>
Vragen? Kijk op www.workspaces.nl of stuur een mail naar info@workspaces.nl


  </body>
</html>
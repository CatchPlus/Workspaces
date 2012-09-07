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





Beste heer/mevrouw ${account.lastName},<br/><br/>

Er is een nieuw wachtwoord opgevraagd voor de Workspaces website.<br/>
Wanneer u dit niet zelf heeft gedaan, negeer dan deze e-mail. Wanneer u wel een nieuw wachtwoord wilt, klik dan op de onderstaande link om uw wachtwoord te resetten.<br/><br/>
<a href="${activationUrl}">${activationUrl}</a><br/><br/>

Werkt de link niet? Kopieer de link en plak deze vervolgens in de  adresbalk van uw browser.<br/><br/>

Met vriendelijke groet, workspaces.nl<br/><br/>
Ps: stuur geen reply op deze mail, deze wordt niet gelezen.<br/>
Vragen? Kijk op www.workspaces.nl of stuur een mail naar info@workspaces.nl


  </body>
</html>
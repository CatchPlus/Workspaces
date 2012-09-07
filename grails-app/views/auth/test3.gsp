<%--
  Created by IntelliJ IDEA.
  User: Michael
  Date: 18-4-12
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<g:if test="${locale.toString() == 'nl'}">
                                   nlllll
                                </g:if>

${g.message(locale: locale, code: 'application.welcome')}

</html>
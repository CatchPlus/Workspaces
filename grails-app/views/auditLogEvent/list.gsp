<%@ page import="nl.of.catchplus.ShiroUser; org.apache.commons.lang.StringUtils" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>AuditLogEvent List</title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir: '')}">Home</a></span>
</div>

<div class="body">
    <h1>AuditLogEvent List</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id"/>

                <g:sortableColumn property="actor" title="Actor"/>

                <g:sortableColumn property="uri" title="Uri"/>

                <g:sortableColumn property="className" title="Class Name"/>

                <g:sortableColumn property="eventName" title="Event Name"/>

                <g:sortableColumn property="propertyName" title="Property Name"/>

                <g:sortableColumn property="oldValue" title="Old Value"/>

                <g:sortableColumn property="newValue" title="New Value"/>

                <g:sortableColumn property="persistedObjectId" title="Persisted Object Id"/>

                <g:sortableColumn property="persistedObjectVersion" title="Persisted Object Version"/>

                <g:sortableColumn property="dateCreated" title="Date Created"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${auditLogEventInstanceList}" status="i" var="auditLogEventInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${auditLogEventInstance.id}">${fieldValue(bean: auditLogEventInstance, field: 'id')}</g:link></td>

                    %{--<td>${fieldValue(bean: auditLogEventInstance, field: 'actor')}</td>--}%
                    <td>
                        <g:if test="${auditLogEventInstance.actor}">
                            <g:link action="show" controller='account'  id="${auditLogEventInstance.actor}"><cp:renderShiroUser id='${auditLogEventInstance.actor}'/></g:link>
                        </g:if>
                    </td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'uri')}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'className')}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'eventName')}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'propertyName')}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'oldValue').trimLength(100)}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'newValue').trimLength(100)}</td>

                    <%
                        String controllerName = StringUtils.uncapitalise(StringUtils.substringAfter(auditLogEventInstance.className, "nl.of.catchplus."))
                    %>

                    <td><g:link action="show" controller='${controllerName}' id="${auditLogEventInstance.persistedObjectId}">${auditLogEventInstance.persistedObjectId}</g:link></td>


                    <td>${fieldValue(bean: auditLogEventInstance, field: 'persistedObjectVersion')}</td>

                    <td>${fieldValue(bean: auditLogEventInstance, field: 'dateCreated')}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <g:paginate total="${auditLogEventInstanceTotal}"/>
    </div>
</div>
</body>
</html>

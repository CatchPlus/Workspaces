<%--
  Created by IntelliJ IDEA.
  User: Michael
  Date: 22-9-11
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="org.apache.commons.lang.StringUtils; nl.of.catchplus.Bundle; nl.of.catchplus.Collection; nl.of.catchplus.WorkSpace; grails.util.GrailsNameUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'collection.label', default: 'Collection')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
    </span>
    <span class="menuButton"><g:link class="list" controller="workSpace" action="list"><g:message code="default.list.label"
                                                                           args="['WorkSpace']"/></g:link></span>
    <span class="menuButton"><g:link class="list" controller="collection" action="list"><g:message code="default.list.label"
                                                                               args="['Collection']"/></g:link></span>

    <span class="menuButton"><g:link class="list" controller="bundle" action="list"><g:message code="default.list.label"
                                                                               args="['Bundle']"/></g:link></span>

    <span class="menuButton"><g:link class="list" controller="baseContent" action="list"><g:message code="default.list.label"
                                                                               args="['BaseContent']"/></g:link></span>


    <%
            if(domainInstance instanceof WorkSpace ){
            %>
                <shiro:hasPermission permission="collection:create">
                    <span class="menuButton"><g:link class="create" action="create" controller="collection" params="['ownerWorkSpace.id':domainInstance?.id]" ><g:message code="default.new.label" args="['Collection']" /></g:link></span>
                </shiro:hasPermission>
            <%
            }
            else if(domainInstance instanceof Collection ){
            %>
                <shiro:hasPermission permission="bundle:create">
                    <span class="menuButton"><g:link class="create" action="create" controller="bundle" params="['ownerCollection.id':domainInstance?.id]" ><g:message code="default.new.label" args="['Bundle']" /></g:link></span>
                </shiro:hasPermission>

            <%
            }else if(domainInstance instanceof Bundle ){
            %>

            <shiro:hasPermission permission="basecontent:create">
                <span class="menuButton"><g:link class="create" action="create" controller="baseContent" params="['ownerBundle.id':domainInstance?.id]" ><g:message code="default.new.label" args="['BaseContent']" /></g:link></span>
            </shiro:hasPermission>

            <%
            }
            %>


</div>

<g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
</g:if>

<catch:renderShow id='${domainInstance.id}'/>

<div class="buttons">
    <g:form>
        <g:hiddenField name="id" value="${domainInstance?.id}"/>
        <shiro:hasPermission
                permission="${GrailsNameUtils.getShortName(domainInstance.class).toString()}:edit:${domainInstance?.id}">
            <span class="button"><g:actionSubmit class="edit" action="edit"
                                                 value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
        </shiro:hasPermission>
        <shiro:hasPermission
                permission="${GrailsNameUtils.getShortName(domainInstance.class).toString()}:delete:${domainInstance?.id}">
            <span class="button"><g:actionSubmit class="delete" action="delete"
                                                 value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                 onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
        </shiro:hasPermission>


         <g:if test="${session.pin!=domainInstance}">
        <span class="button"><g:actionSubmit class="edit" action="pin"
                                             value="${message(code: 'default.button.pin.label', default: 'Pin')}"/></span>
         </g:if>
        <g:else>
        <span class="button"><g:actionSubmit class="edit" action="unpin"
                                             value="${message(code: 'default.button.unpin.label', default: 'Pin')}"/></span>
       </g:else>
    </g:form>
</div>

</div>

<catch:renderActions id='${domainInstance.id}'/>

<g:if test="${session.pin}">
    <catch:renderShow id='${session.pin?.id}'/>

    <div class="buttons">
        <g:form controller="${StringUtils.uncapitalise(GrailsNameUtils.getShortName(session.pin.class).toString())}">
            <g:hiddenField name="id" value="${session.pin?.id}"/>
            <shiro:hasPermission
                    permission="${GrailsNameUtils.getShortName(session.pin.class).toString()}:edit:${session.pin?.id}">
                <span class="button"><g:actionSubmit class="edit" action="edit"
                                                     value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
            </shiro:hasPermission>
            <shiro:hasPermission
                    permission="${GrailsNameUtils.getShortName(session.pin.class).toString()}:delete:${session.pin?.id}">
                <span class="button"><g:actionSubmit class="delete" action="delete"
                                                     value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                     onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
            </shiro:hasPermission>



            <span class="button"><g:actionSubmit class="edit" action="unpin"
                                                 value="${message(code: 'default.button.unpin.label', default: 'Unpin')}"/></span>

        </g:form>
    </div>
</g:if>

</div>

</body>

</html>
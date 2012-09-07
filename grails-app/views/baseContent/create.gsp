<%@ page import="nl.of.catchplus.BaseContent" %>
<html>
<head>
    <meta http-equiv="Content-Type" baseContent="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'baseContent.label', default: 'BaseContent')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
    </span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label"
                                                                           args="[entityName]"/></g:link></span>
</div>

<div class="body">
<h1><g:message code="default.create.label" args="[entityName]"/></h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<g:hasErrors bean="${baseContentInstance}">
    <div class="errors">
        <g:renderErrors bean="${baseContentInstance}" as="list"/>
    </div>
</g:hasErrors>
<g:form controller='api'  method="POST" action="baseContent" enctype="multipart/form-data">
%{--<g:form  method="POST" action="save" >--}%
<div class="dialog">
<table>
<tbody>

<tr class="prop">
    <td valign="top" class="name">
        <label for="title"><g:message code="basecContent.title.label" default="Title"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'title', 'errors')}">

        %{--<g:textField name="title" maxlength="50" value="${baseContentInstance?.title}"/>--}%
        <g:textField name="string_field" maxlength="50" value="${baseContentInstance?.title}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="description"><g:message code="basecContent.description.label" default="Description"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'description', 'errors')}">

        <g:textArea name="description" cols="40" rows="5" value="${baseContentInstance?.description}"/>
    </td>
</tr>

%{--
<tr class="prop">
    <td valign="top" class="name">
        <label for="DCtitle"><g:message code="basecContent.DCtitle.label" default="DC title"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCtitle', 'errors')}">

        <g:textField name="DCtitle" value="${baseContentInstance?.DCtitle}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCcreator"><g:message code="basecContent.DCcreator.label" default="DC creator"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCcreator', 'errors')}">

        <g:textField name="DCcreator" value="${baseContentInstance?.DCcreator}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCsubject"><g:message code="baseContent.DCsubject.label" default="DC subject"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCsubject', 'errors')}">

        <g:textField name="DCsubject" value="${baseContentInstance?.DCsubject}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCdescription"><g:message code="baseContent.DCdescription.label" default="DC description"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCdescription', 'errors')}">

        <g:textField name="DCdescription" value="${baseContentInstance?.DCdescription}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCpublisher"><g:message code="baseContent.DCpublisher.label" default="DC publisher"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCpublisher', 'errors')}">

        <g:textField name="DCpublisher" value="${baseContentInstance?.DCpublisher}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCcontributor"><g:message code="baseContent.DCcontributor.label" default="DC contributor"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCcontributor', 'errors')}">

        <g:textField name="DCcontributor" value="${baseContentInstance?.DCcontributor}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCdate"><g:message code="baseContent.DCdate.label" default="DC date"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCdate', 'errors')}">

        <g:textField name="DCdate" value="${baseContentInstance?.DCdate}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCtype"><g:message code="baseContent.DCtype.label" default="DC type"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCtype', 'errors')}">

        <g:textField name="DCtype" value="${baseContentInstance?.DCtype}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCformat"><g:message code="baseContent.DCformat.label" default="DC format"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCformat', 'errors')}">

        <g:textField name="DCformat" value="${baseContentInstance?.DCformat}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCidentifier"><g:message code="baseContent.DCidentifier.label" default="DC identifier"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCidentifier', 'errors')}">

        <g:textField name="DCidentifier" value="${baseContentInstance?.DCidentifier}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCsource"><g:message code="baseContent.DCsource.label" default="DC source"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCsource', 'errors')}">

        <g:textField name="DCsource" value="${baseContentInstance?.DCsource}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DClanguage"><g:message code="baseContent.DClanguage.label" default="DC language"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DClanguage', 'errors')}">

        <g:textField name="DClanguage" value="${baseContentInstance?.DClanguage}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCrelation"><g:message code="baseContent.DCrelation.label" default="DC relation"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCrelation', 'errors')}">

        <g:textField name="DCrelation" value="${baseContentInstance?.DCrelation}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCcoverage"><g:message code="baseContent.DCcoverage.label" default="DC coverage"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCcoverage', 'errors')}">

        <g:textField name="DCcoverage" value="${baseContentInstance?.DCcoverage}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="DCrights"><g:message code="baseContent.DCrights.label" default="DC rights"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'DCrights', 'errors')}">

        <g:textField name="DCrights" value="${baseContentInstance?.DCrights}"/>
    </td>
</tr>
--}%

<tr class="prop">
    <td valign="top" class="name">
        <label for="url"><g:message code="baseContent.url.label" default="Url"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'url', 'errors')}">

        <g:textField name="url" maxlength="100" value="${baseContentInstance?.url}"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="ownerBundle"><g:message code="baseContent.ownerBundle.label" default="Owner Bundle"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: baseContentInstance, field: 'ownerBundle', 'errors')}">

        <input type="hidden" name="ownerBundle.id"
               value="${baseContentInstance?.ownerBundle?.id}"/> ${baseContentInstance?.ownerBundle}
    </td>
</tr>


<tr class="prop">
    <td valign="top" class="name">
        <label for="uploadFile"><g:message code="storedFile.uploadFile.label" default="UploadFile"/></label>
    </td>
    <td valign="top" class="value">

    %{--    <input type="file" id="uploadFile" name="uploadFile"/>--}%
        <input type="file" id="uploadFile" name="attachment_fieldd"/>
    </td>
</tr>

</tbody>
</table>
</div>

<div class="buttons">
    <span class="button"><g:submitButton name="create" class="save"
                                         value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
</div>
</g:form>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page import="nl.of.catchplus.Account; nl.of.catchplus.ShiroUser" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Workspaces</title>
    <meta name="layout" content="main"/>
    <z:resources/>
    <style type="text/css" media="screen">



    #nav {
        margin-top: 20px;
        margin-left: 30px;
        width: 228px;
        float: left;

    }

    .homePagePanel * {
        margin: 0px;
    }

    .homePagePanel .panelBody ul {
        list-style-type: none;
        margin-bottom: 10px;
    }

    .homePagePanel .panelBody h1 {
        text-transform: uppercase;
        font-size: 1.1em;
        margin-bottom: 10px;
    }

    .homePagePanel .panelBody {
        background: url(images/leftnav_midstretch.png) repeat-y top;
        margin: 0px;
        padding: 15px;
    }

    .homePagePanel .panelBtm {
        background: url(images/leftnav_btm.png) no-repeat top;
        height: 20px;
        margin: 0px;
    }

    .homePagePanel .panelTop {
        background: url(images/leftnav_top.png) no-repeat top;
        height: 11px;
        margin: 0px;
    }

    h2 {
        margin-top: 15px;
        margin-bottom: 15px;
        font-size: 1.2em;
    }

    #pageBody {
        margin-left: 280px;
        margin-right: 20px;
    }
    </style>
</head>

<body>

<g:if env="development">

    <div id="nav">
        <div class="homePagePanel">
            <div class="panelTop"></div>

            <div class="panelBody">
                <h1>Application Status</h1>
                <ul>
                    <li>App version: <g:meta name="app.version"></g:meta></li>
                    <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
                    <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                    <li>JVM version: ${System.getProperty('java.version')}</li>
                    <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                    <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                    <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                    <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
                </ul>

                <h1>Installed Plugins</h1>
                <ul>
                    <g:set var="pluginManager"
                           value="${applicationContext.getBean('pluginManager')}"></g:set>

                    <g:each var="plugin" in="${pluginManager.allPlugins}">
                        <li>${plugin.name} - ${plugin.version}</li>
                    </g:each>

                </ul>
            </div>

            <div class="panelBtm"></div>
        </div>
    </div>

    <div id="pageBody">
        <h1>Welcome to devmode</h1>

        <p></p>

        <br/>

        <li class="controller"><g:link controller='auth' action="app2">Home</g:link></li>
        <li class="controller"><g:link url="/catchplus/application">Workspaces</g:link></li>

        <div id="controllerList" class="dialog">
            <h2>Available Controllers:</h2>
            <ul>

                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
                    <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.naturalName}</g:link></li>
                </g:each>
            </ul>
        </div>
    </div>


    <div id="pageBody">
        <div class="dialog">
            <h2>Available users:</h2>
            <g:each in="${Account.list()}" var="user" status="i">
                <g:each in="${user.shiroUser.iterator().next().roles.sort()}" var="role" status="j">
                    <g:if test="${j != 0}">
                        ${role} |
                    </g:if>
                </g:each>

                <g:link action="signIn" controller="auth"
                        params="[username: user.username, password: 'qwerty']">${user.toString()}</g:link>


                <br/>

                <br/>
            </g:each>
        </div>

    </div>
</g:if>
<g:else>
    <shiro:hasPermission permission='*:*'>

        <div id="nav">
            <div class="homePagePanel">
                <div class="panelTop"></div>

                <div class="panelBody">
                    <h1>Application Status</h1>
                    <ul>
                        <li>App version: <g:meta name="app.version"></g:meta></li>
                        <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
                        <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                        <li>JVM version: ${System.getProperty('java.version')}</li>
                        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
                    </ul>

                    <h1>Installed Plugins</h1>
                    <ul>
                        <g:set var="pluginManager"
                               value="${applicationContext.getBean('pluginManager')}"></g:set>

                        <g:each var="plugin" in="${pluginManager.allPlugins}">
                            <li>${plugin.name} - ${plugin.version}</li>
                        </g:each>

                    </ul>
                </div>

                <div class="panelBtm"></div>
            </div>
        </div>

        <div id="pageBody">
            <h1>Welcome to Scaffolding</h1>

            %{--   <p>Hallo admin</p>--}%

            <br/>


            <li class="controller"><g:link url="/catchplus/application">Catchplus Application</g:link></li>

            <div id="controllerList" class="dialog">
                <h2>Available Controllers:</h2>
                <ul>

                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
                        <li class="controller"><g:link
                                controller="${c.logicalPropertyName}">${c.naturalName}</g:link></li>
                    </g:each>
                </ul>
            </div>
        </div>


        <div id="pageBody">
            <div class="dialog">
                <h2>Login as:</h2>
                <g:each in="${Account.list()}" var="user" status="i">
                    <g:each in="${user.shiroUser.iterator().next().roles.sort()}" var="role" status="j">
                        <g:if test="${j != 0}">
                            ${role} |
                        </g:if>
                    </g:each>

                    <g:link action="signIn" controller="auth"
                            params="[username: user.username, password: 'qwerty']">${user.toString()}</g:link>


                    <br/>

                    <br/>
                </g:each>
            </div>

        </div>

    </shiro:hasPermission>


    <shiro:lacksPermission permission='*:*'>
        <div class="homePagePanel">
            <g:link controller='auth' action="index" params="[targetUri: '/application']">Aanmelden</g:link>
        </div>

        <br/>
        <br/>



    </shiro:lacksPermission>
</g:else>

<z:window title="Repository" id='headwindow' context="editPopup" apply='nl.of.catchplus.ExplorerComposer'
                   width="400px">

            <z:tree id="mytreeaap" mold="paging" pageSize='15'>
                <z:treecols sizable="true">
                    <z:treecol label="${g.message(code: 'baseRepository.title')}"/>
                    <z:treecol width='60px' label="${g.message(code: 'baseRepository.owner')}"/>
                </z:treecols>

            </z:tree>

        </z:window>

</body>
</html>

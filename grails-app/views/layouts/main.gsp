<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Workspaces" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>

    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />

        </div>
        <div id="grailsLogo" ><a href="http://www.catchplus.nl"><img src="${resource(dir:'images',file:'logo.png')}" alt="Catchplus" border="0" /></a></div>
         %{--<div>Loggedin as : <shiro:principal /> Instituut: <shiro:principal property="institution" /> </div>--}%
        %{--<div>Loggedin as : <cp:displayUser/> </div>--}%
        <g:layoutBody />
    </body>
</html>
package nl.of.catchplus

import org.apache.shiro.SecurityUtils

import org.apache.commons.lang.StringUtils
import grails.util.GrailsNameUtils

class PageTagLib
{


        static namespace = "cp"

        def permissionService
        def shiroService

        def renderShow = { attrs, body ->

                // out << 'render show : ' + attrs.id

                BaseDomain domainObject = BaseDomain.read(attrs.id)

                if (domainObject)
                {

                        //URL externalUrl = new URL("""${grailsApplication.config.grails.serverURL}/${StringUtils.uncapitalise(GrailsNameUtils.getShortName(domainObject.class).toString())}/show/""" + attrs.id+"?tagged=true")

                        URL externalUrl = new URL("""http://localhost:8080/catchplus/${StringUtils.uncapitalise(GrailsNameUtils.getShortName(domainObject.class).toString())}/show/""" + attrs.id + "?tagged=true")

                        BufferedReader extrenalUrlReader = null


                        try
                        {
                                extrenalUrlReader = new BufferedReader(new InputStreamReader(externalUrl.openStream()));

                                StringBuilder sb = new StringBuilder()
                                String inputLine;

                                while ((inputLine = extrenalUrlReader.readLine()) != null)
                                        sb.append(inputLine)

                                String content = sb.toString()
                                content = StringUtils.substringAfter(content, "<div class=\"body\">")
                                content = StringUtils.substringBefore(content, "<div class=\"buttons\">")
                                content = "<div class=\"body\">" + content

                                //System.out.println(content);
                                out << content

                                //Pattern p = Pattern.compile(".*<body[\\s+[a-zA-Z]=\"#?[A-Za-z0-9]*\"]*>(.*)</body>.*");
                                //Matcher m = p.matcher(content)

                                /*if (m.matches())
                                {
                                    out << m.group(1)
                                }
                                else
                                {
                                }*/
                        }
                        catch (Exception e)
                        {
                                e.printStackTrace()
                                //log.error(e.getMessage(), e)
                        }
                        finally
                        {
                                if (extrenalUrlReader != null)
                                        extrenalUrlReader.close();
                        }
                }

        }

        def renderShiroUser = { attrs, body ->

                //println(attrs.id)
                if (attrs.id)
                {
                        //ShiroUser user = ShiroUser.read(Long.parseLong(attrs.id))
                        Account  account = Account.read(Long.parseLong(attrs.id))
                        out << account
                }

        }

        def renderActions = { attrs, body ->

                BaseDomain domainObject = BaseDomain.read(attrs.id)
                BaseDomain sessionObject = BaseDomain.read(session.pin?.id)


                if (sessionObject && domainObject)
                {
                        out << """<div class="body">"""
                        out << """<div class="dialog">"""
                        out << """<h1>Actions</h1>"""

                        String action = ''

                        if ((domainObject instanceof WorkSpace && sessionObject instanceof Collection || domainObject instanceof Collection && sessionObject instanceof WorkSpace))
                        {
                                if (SecurityUtils.subject.isPermitted("workspace:addcollection:${domainObject instanceof WorkSpace ? domainObject.id : sessionObject.id}"))
                                        action = g.link(action: 'addCollection', controller: 'workSpace', id: domainObject instanceof WorkSpace ? domainObject.id : sessionObject.id, params: [returnObjectID: domainObject.id, collectionID: sessionObject instanceof Collection ? sessionObject.id : domainObject.id], 'Add collection to workspace')

                                out << action
                                out << "<br/>"
                        }
                        else if ((domainObject instanceof Collection && sessionObject instanceof Bundle || domainObject instanceof Bundle && sessionObject instanceof Collection))
                        {
                                Bundle bundle = domainObject instanceof Bundle ? domainObject : sessionObject
                                Collection collection = domainObject instanceof Collection ? domainObject : sessionObject

                                if (collection.containsBundle(bundle) && SecurityUtils.subject.isPermitted("collection:removebundle:${domainObject.id}") && SecurityUtils.subject.isPermitted("collection:removebundle:${sessionObject.id}"))
                                {
                                        action = g.link(action: 'removeBundle', controller: 'collection', id: domainObject instanceof Collection ? domainObject.id : sessionObject.id, params: [returnObjectID: domainObject.id, bundleID: sessionObject instanceof Bundle ? sessionObject.id : domainObject.id], 'Remove bundle from collection')
                                }
                                else if (!collection.containsBundle(bundle) && SecurityUtils.subject.isPermitted("collection:addbundle:${domainObject instanceof Collection ? domainObject.id : sessionObject.id}"))
                                        action = g.link(action: 'addBundle', controller: 'collection', id: domainObject instanceof Collection ? domainObject.id : sessionObject.id, params: [returnObjectID: domainObject.id, bundleID: sessionObject instanceof Bundle ? sessionObject.id : domainObject.id], 'Add bundle to collection')

                                out << action
                                out << "<br/>"

                                if (!collection.containsBundle(bundle) && permissionService.bundleMove(bundle, collection))
                                {
                                        out << g.link(action: 'moveBundle', controller: 'collection', id: collection.id, params: [returnObjectID: domainObject.id, bundleID: bundle.id], 'Move bundle to collection')
                                        out << "<br/>"
                                }

                        }
                        else if ((domainObject instanceof Bundle && sessionObject instanceof BaseContent || domainObject instanceof BaseContent && sessionObject instanceof Bundle))
                        {
                                Bundle bundle = domainObject instanceof Bundle ? domainObject : sessionObject
                                BaseContent baseContent = domainObject instanceof BaseContent ? domainObject : sessionObject


                                if (bundle.containsContent(baseContent) && permissionService.baseContentRemove(baseContent, bundle))
                                {
                                        action = g.link(action: 'removeBaseContent', controller: 'bundle', id: bundle.id, params: [returnObjectID: domainObject.id, contentID: baseContent.id], 'Remove content from bundle')
                                }
                                else if (!bundle.containsContent(baseContent) && permissionService.bundleAddBaseContent(bundle))
                                {
                                        action = g.link(action: 'addBaseContent', controller: 'bundle', id: bundle.id, params: [returnObjectID: domainObject.id, baseContentID: baseContent.id], 'Add content to bundle')
                                }

                                out << action
                                out << "<br/>"

                                if (!bundle.containsContent(baseContent) && permissionService.baseContentMove(baseContent, bundle))
                                {
                                        out << g.link(action: 'moveBaseContent', controller: 'bundle', id: bundle.id, params: [returnObjectID: domainObject.id, contentID: baseContent.id], 'Move content to bundle')
                                        out << "<br/>"
                                }
                        }


                        out << """        </div>"""
                        out << """        </div>"""
                }

        }

        def renderContextObject = { attrs, body ->

                if (session.pin)
                {

                        def objectt = BaseDomain.read(session.pin.id)

                        out << """<div class="body">"""
                        out << """<div class="dialog">"""
                        out << """<h1>${objectt.toString()}</h1>"""
                        out << """<div class="dialog">"""
                        out << """<table><tbody>"""
                        out << """RENDER context object :${objectt} """

                        // def excludedProps = Event.allEvents.toList() << 'version'
                        //allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
                        System.out.println("XXXXXXXXXXXXXXXXXX");
                        System.out.println(objectt);
                        System.out.println(objectt.properties);
                        def props = objectt.properties //{ allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
                        //Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                        props.each { p ->
                                System.out.println("PRINT");
                                System.out.println(p);
                                /* <tr class="prop">
                                      <td valign="top" class="name"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></td>
                                      <%  if (p.isEnum()) { %>
                                      <td valign="top" class="value">\${${propertyName}?.${p.name}?.encodeAsHTML()}</td>
                                      <%  } else if (p.oneToMany || p.manyToMany) { %>
                                      <td valign="top" style="text-align: left;" class="value">
                                          <ul>
                                          <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
                                              <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
                                          </g:each>
                                              %{--<li><g:link controller="${p.referencedDomainClass?.propertyName}" action="create">\${'dsfdf'}</g:link></li>--}%

                                          </ul>
                                      </td>
                                      <%  } else if (p.manyToOne || p.oneToOne) { %>
                                      <td valign="top" class="value"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></td>
                                      <%  } else if (p.type == Boolean.class || p.type == boolean.class) { %>
                                      <td valign="top" class="value"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></td>
                                      <%  } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
                                      <td valign="top" class="value"><g:formatDate date="\${${propertyName}?.${p.name}}" /></td>
                                      <%  } else if(!p.type.isArray()) { %>
                                      <td valign="top" class="value">\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</td>
                                      <%  } %>
                                  </tr>
                              <%  }*/

                        }
                        out << """</tbody></table>"""
                        out << """</div>"""
                        out << """</div>"""

                }

        }

        def renderContextActions = { attrs, body ->

                if (session.pin)
                {

                        out << """<div class="body">"""
                        out << """<div class="dialog">"""
                        out << """ACTIONS for left : ${BaseDomain.read(attrs.id)} AND right : ${session.pin}"""
                        out << """        </div>"""
                        out << """        </div>"""

                }

        }

        /*def bedrijvengidsURL = { attrs ->

            String result

            if (attrs.klusser == null)
            {
                throwTagError("Tag [klusserURL] is missing required attribute [klusser]")
            }
            if (!(attrs.klusser instanceof Klusser))
            {
                throwTagError("Tag [klusserURL]  attribute [klusser] must be instanceof Klusser")
            }

            out << klusserService.getKlusserURL(attrs['klusser'], false)
        }*/

        //<span class="menuButton"><g:link class="create" action="create" params="['owner.id':${propertyName}?.owner?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link></span>

        def blabla = { attrs ->
                String username = SecurityUtils.subject.principal

                ShiroUser shiroUser = ShiroUser.findByUsername(username)

                if (shiroUser.institution)
                {
                        out << """<span class="menuButton"> a${shiroUser.institution.id}b </span>"""//   <img src='${resource(dir: "/images/icons", file: "skills-red_25x25_trance.png")}' height='20'  class="style" /> """
                        //out << """<img src='${resource(dir: "/images/icons", file: "skills-red_25x25_trance.png")}' height='20'  class="style" /> """

                        // out << 'yoyyo'+shiroUser.institution.id
                }

                /*if (subject != null) {
                    // Get the principal to print out.
                    def principal
                    if (attrs["type"]) {
                        // A principal of a particular type/class has been
                        // requested.
                        principal = subject.principals.oneByType(attrs["type"])
                    }
                    else {
                        principal = subject.principal
                    }

                    // Now write the principal to the page.
                    if (principal != null) {
                        // If a 'property' attribute has been specified, then
                        // we access the property with the same name on the
                        // principal and write that to the page. Otherwise, we
                        // just use the string representation of the principal
                        // itself.
                        if (attrs["property"]) {
                            out << principal."${attrs['property']}".toString().encodeAsHTML()
                        }
                        else {
                            out << principal.toString().encodeAsHTML()
                        }
                    }
                }*/
        }

}

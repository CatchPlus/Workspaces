<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
  Created by IntelliJ IDEA.
  User: Michael
  Date: 4-10-11
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="nl.of.catchplus.WorkSpace" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Workspaces</title>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <z:resources/>

</head>

<body>

<z:style>
    textarea
{
   resize: none;

}

    div.z-listbox-body .z-listcell {
           padding: 3px;
        }

        div.z-listbox-body .z-listcell {
               padding: 3px;
            }

</z:style>

<z:page id="page_id_1" style="width:100%;height:100%">

<z:window id='headwindow' context="editPopup" apply='nl.of.catchplus.GuiComposer' hflex="1" vflex="1">
%{--<z:button label='test knop' id='test'>


</z:button>--}%


<z:borderlayout>
<z:north border='0'>
    <z:div>
    %{--<z:hbox height="100%" width="100%" >
  <z:menubar autodrop="true" id="menubar" width="100%">
      <z:menu label="File">
          <z:menupopup>
              <z:menu label="New" image="/widgets/menu/drop-down_menu/img/file_new.png">
                  <z:menupopup>
                      <z:menuitem label="Document" onClick="alert(self.label)"
                                  image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_new_writer.png"/>
                      <z:menuitem label="Spreadsheet" onClick="alert(self.label)"
                                  image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_new_spreadsheet.png"/>
                      <z:menuitem label="Presentation" onClick="alert(self.label)"
                                  image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_new_presentation.png"/>
                  </z:menupopup>
              </z:menu>
              <z:menuitem label="Open.." onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_open.png"/>
              <z:menuseparator></z:menuseparator>
              <z:menuitem label="Save" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_save.png"/>
              <z:menuitem label="Save As..." onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_save-as.png"/>
              <z:menuitem label="Save All" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_save-all.png"/>
              <z:menuitem label="Close" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_close.png"/>
              <z:menuitem label="Close All" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_close-all.png"/>
              <z:menuseparator></z:menuseparator>
              <z:menuitem label="Import" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_import.png"/>
              <z:menuitem label="Export" disabled="true"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_export.png"/>
              <z:menuseparator></z:menuseparator>
              <z:menuitem label="Exit" disabled="true"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/file_exit.png"/>
          </z:menupopup>
      </z:menu>
      <z:menu label="Edit">
          <z:menupopup>
              <z:menuitem label="Undo" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_undo.png"/>
              <z:menuitem label="Redo" disabled="true"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_redo.png"/>
              <z:menuseparator></z:menuseparator>
              <z:menuitem label="Cut" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_cut.png"/>
              <z:menuitem label="Copy" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_copy.png"/>
              <z:menuitem label="Paste" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_paste.png"/>
              <z:menuseparator></z:menuseparator>
              <z:menuitem label="Select All" onClick="alert(self.label)"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/edit_select-all.png"/>
          </z:menupopup>
      </z:menu>
      <z:menu label="Help">
          <z:menupopup>
              <z:menuitem label="Issue tracker" target="_zkdemo" href="http://tracker.zkoss.org"
              <z:menuitem label="Issue tracker" target="_zkdemo" href="http://tracker.zkoss.org"
                          image="http://www.zkoss.org/zkdemo/widgets/menu/drop-down_menu/img/bug.png"/>
              <z:menu label="About">
                  <z:menupopup>
                      <z:menuitem label="About Potix" target="_zkdemo" href="http://www.zkoss.org/support/about"
                                  onClick="alert(self.label)"/>
                  </z:menupopup>
              </z:menu>
          </z:menupopup>

      </z:menu>
      <z:menuseparator></z:menuseparator>
      <z:menuitem label="ZK Web Framspacezzework" target="_zkdemo" href="http://www.zkoss.org/download"
                  image="http://www.zkoss.org/zkdemo/images/zk16x16.png" onClick="alert(self.label)"/>
  </z:menubar>
        <div style="background:#E6D92C">
          <z:toolbarbutton id='toolbarbuttonSignOut' label="Afmelden"
                           image="http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png"/>

        </div>
    </z:hbox>--}%

        <z:hbox pack="stretch" style="padding:0" width="100%">
            <z:toolbar align="start" style="float:left" width="100%">

                <z:toolbarbutton id='toolbarbuttonCatchplus' tooltiptext="LOGO" href='http://www.catchplus.nl/'
                                 target='_blank'
                                 image="${g.resource(dir: 'images', file: 'logo-small.png')}"/>

                <shiro:lacksPermission permission='*:*'>
                    <cp:isAllowedInstitutionAdminManage>
                        <z:toolbarbutton id='toolbarbuttonCreateCollection'
                                         tooltiptext="${g.message(locale: locale, code: 'collection.create')}"
                                         image="${g.resource(dir: 'images/16x16', file: 'add.png')}"/>
                    </cp:isAllowedInstitutionAdminManage>
                </shiro:lacksPermission>
                <cp:isAllowedUserManagement>
                    <z:toolbarbutton id='toolbarbuttonUserBeheer'
                                     tooltiptext="${g.message(locale: locale, code: 'account.management')}"
                                     image="${g.resource(dir: 'images/16x16', file: 'group.png')}"/>
                </cp:isAllowedUserManagement>

                <shiro:hasPermission permission='*:*'>
                    <z:toolbarbutton id='toolbarbuttonSuperAdminUserBeheer'
                                     tooltiptext="${g.message(locale: locale, code: 'account.management')}"
                                     image="${g.resource(dir: 'images/16x16', file: 'group.png')}"/>
                </shiro:hasPermission>

            %{--<shiro:hasPermission permission='*:*'>--}%
                <cp:isAllowedInstitutionAdminManage>
                    <z:toolbarbutton id='toolbarbuttonInstituutBeheer'
                                     tooltiptext="${g.message(locale: locale, code: 'institution.management')}"
                                     image="${g.resource(dir: 'images/16x16', file: 'institute_of_management.png')}"/>
                </cp:isAllowedInstitutionAdminManage>

                <shiro:hasPermission permission='*:*'>
                    <z:toolbarbutton id='toolbarbuttonScaffolding'
                                     tooltiptext="${g.message(locale: locale, code: 'application.grails.management')}"
                                     image="${g.resource(dir: 'images/16x16', file: 'grails.png')}"/>
                </shiro:hasPermission>
            %{--</shiro:hasPermission>--}%

            %{--<z:space bar="true"/>--}%

            </z:toolbar>

            <z:toolbar align="end" style="float:right" width="100%">
                <z:toolbarbutton id='toolbalbuttonDisplayUser'
                                 label="${g.message(locale: locale, code: 'account.signed.in.as')}: ${cp.displayUser()}"/>

                <z:toolbarbutton id='toolbarbuttonSignOut'
                                 tooltiptext="${g.message(locale: locale, code: 'account.signout')}"
                                 image="${g.resource(dir: 'images/16x16', file: 'sign_out.png')}"/>



                <cp:canSwichInstitution>

                    <z:toolbarbutton id='toolbarbuttonSwitchInstitution'
                                     tooltiptext="${g.message(locale: locale, code: 'institution.switch')}"
                                     image="${g.resource(dir: 'images/16x16', file: 'arrow_switch.png')}"/>
                </cp:canSwichInstitution>
                <z:space bar="true"/>
                <z:toolbarbutton id='toolbarbuttonHelp' tooltiptext="${g.message(locale: locale, code: 'default.help')}"
                                 href='http://www.catchplus.nl/wiki'
                                 target='_blank'
                                 image="${g.resource(dir: 'images/16x16', file: 'help.png')}">
                </z:toolbarbutton>

            </z:toolbar>
        </z:hbox>

    </z:div>

</z:north>
<z:center border="0">
    <z:borderlayout>
        <z:west id='west' autoscroll='true' title="${g.message(locale: locale, code: 'application.left.menu.title')}"
                maxsize="600" size="30%" flex="true"
                collapsible="true"
                splittable="true">
            <z:div>

                <z:tree id="mytreeaap" mold="paging" pageSize='15'>
                    <z:treecols sizable="true">
                        <z:treecol label="${g.message(locale: locale, code: 'baseRepository.title')}"/>
                        <z:treecol width='60px' label="${g.message(locale: locale, code: 'default.count')}"/>
                        <z:treecol width='60px' label="${g.message(locale: locale, code: 'default.status')}"/>
                    </z:treecols>

                </z:tree>
            %{--<z:vbox>
            <z:hbox>--}%
                <z:image src="${g.resource(dir: 'images/16x16', file: 'zoom.png')}" align='center'  hspace='5'/>
                <z:textbox id="textboxsearch"/>

                <z:button id="buttonsearch" label="${g.message(locale: locale, code: 'default.search')}"/>
            %{--           </z:hbox>--}%
                <z:listbox id="listboxsearchresult" fixedLayout="true" mold="paging" pageSize="9">
                %{--<z:listbox id="listboxsearchresult"  rows="9">--}%

                    <z:listhead>
                        <z:listheader label="${g.message(locale: locale, code: 'baseRepository.title')}"/>
                        <z:listheader label="${g.message(locale: locale, code: 'default.type')}"/>
                    </z:listhead>

                </z:listbox>
            %{--</z:vbox>--}%
            </z:div>
        </z:west>

        <z:center autoscroll='true' border="0">
            <z:div>

                <z:tabbox id='mytabbox'>
                    <z:tabs>
                        <z:tab label="${g.message(locale: locale, code: 'application.welcome')}" closable="true"/>

                    </z:tabs>
                    <z:tabpanels>
                        <z:tabpanel>

                            <g:if test="${locale.toString() == 'nl'}">
                                <z:include src="/welkom.html"/>
                            </g:if>
                            <g:else>
                                <z:include src="/welcome.html"/>
                            </g:else>

                        </z:tabpanel>

                    </z:tabpanels>
                </z:tabbox>

            </z:div>
        </z:center>
        <z:east title="${g.message(locale: locale, code: 'application.right.menu.title')}" size="200px"
                collapsible="true" flex="true" autoscroll="true" splittable="true">
            <z:div>
                <z:tabbox id='tabboxFavorites' mold="accordion">
                    <z:tabs>
                        <z:tab label="${g.message(locale: locale, code: 'collection.self')}" id='tabCollection'/>
                        <z:tab label="${g.message(locale: locale, code: 'bundle.self')}" id='tabBundle'/>
                        <z:tab label="${g.message(locale: locale, code: 'baseContent.self')}" id='tabContent'/>
                    </z:tabs>
                    <z:tabpanels>
                        <z:tabpanel id="tabpanelCollection" style="border: 0">
                        %{--<z:listbox
                                fixedLayout="true">
                            <z:listhead>
                                <z:listheader/>
                                <z:listheader width="30px"/>
                            </z:listhead>

                        </z:listbox>--}%

                        </z:tabpanel>
                        <z:tabpanel id="tabpanelBundle" style="border: 0">
                        %{--<z:listbox
                                   fixedLayout="true">
                            <z:listhead>
                                <z:listheader/>
                                <z:listheader width="30px"/>
                            </z:listhead>

                        </z:listbox>--}%
                        </z:tabpanel>
                        <z:tabpanel id="tabpanelContent" style="border: 0">
                        %{--<z:listbox id="listboxContent">
                            <z:listhead>
                                <z:listheader/>
                                <z:listheader width="30px"/>
                            </z:listhead>

                        </z:listbox>--}%
                        </z:tabpanel>

                    </z:tabpanels>
                </z:tabbox>

            </z:div>
        </z:east>
    </z:borderlayout>
</z:center>
<z:south>
    <z:toolbar id="tb" height="20px" align="end">
    %{--<z:toolbarbutton image="http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png"
    onClick="alert('Java')"/>
<z:toolbarbutton image="http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/issue.png"
    onClick="alert('Issue')"/>
<z:toolbarbutton image="http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/internet.png"
    onClick="alert('Internet')"/>
<z:toolbarbutton image="http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/volumn.png"
    onClick="alert('Volumn')"/>--}%
       %{-- <z:toolbarbutton id='now'
                         label="${new java.text.SimpleDateFormat('dd/MM/yyyy HH:mm:ss').format(new Date())}"
                         onClick="alert('time')"/>--}%
    </z:toolbar>
</z:south>
</z:borderlayout>

%{-- <z:label  id="now"/>--}%
%{--<z:timer id="timer" delay="1000" repeats="true"
         onTimer="now.setLabel(new java.text.SimpleDateFormat('dd/MM/yyyy HH:mm:ss').format( new Date()))"/>--}%

</z:window>

</z:page>
</body>
</html>
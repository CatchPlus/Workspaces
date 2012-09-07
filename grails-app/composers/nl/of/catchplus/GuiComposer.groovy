package nl.of.catchplus

/*import org.zkoss.zul.*
import nl.of.catchplus.*
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.event.**/

import org.zkoss.zul.*
import nl.of.catchplus.*
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.event.*

import org.zkoss.util.media.Media
import grails.util.GrailsNameUtils
import org.apache.commons.lang.RandomStringUtils
import org.springframework.context.MessageSource

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.zk.ui.util.Clients
import grails.util.GrailsUtil
import org.apache.commons.lang.StringUtils
import org.zkoss.zul.ext.Sortable

//import org.springframework.web.servlet.support.RequestContextUtils as RCU

class GuiComposer
{

    def blaService

    def grailsApplication
    GormService gormService
    PermissionService permissionService


    FileVaultService fileVaultService
    def searchableService
    EmailService emailService
    ShiroService shiroService
    TestService testService

    Button test

    Tree mytreeaap

    Tabbox mytabbox
    Tabbox tabboxFavorites

    Textbox textboxsearch

    Toolbarbutton toolbarbuttonCreateCollection
    Toolbarbutton toolbarbuttonUserBeheer
    Toolbarbutton toolbarbuttonSwitchInstitution
    Toolbarbutton toolbarbuttonSuperAdminUserBeheer
    Toolbarbutton toolbarbuttonInstituutBeheer
    Toolbarbutton toolbarbuttonSignOut
    Toolbarbutton toolbarbuttonScaffolding
    Toolbarbutton toolbalbuttonDisplayUser

    Listbox listboxsearchresult

    Listbox collectionBookmarkListbox
    Listbox contentBookmarkListbox
    Listbox bundleBookmarkListbox

    Tab tabContent
    Tab tabBundle
    Tab tabCollection

    Tabpanel tabpanelContent
    Tabpanel tabpanelBundle
    Tabpanel tabpanelCollection

    West west

    //ListModelList lt

    Window headwindow


    Tabpanel selectedTabPanel
    Tab selectedTab

    BaseDomain selectedObject

    WorkSpace selectedWorkSpace


    def composer = this

    HashSet<UpdateListerner> compSet = new HashSet()

    def g

    boolean isManageCollectionTabOpen = false

    Locale locale


    ArrayList treeItems = new ArrayList()

    List baseContentKeys = new ArrayList()
    List bundleKeys = new ArrayList()
    List collectionKeys = new ArrayList()

    HashMap collectionRights = new HashMap()


    def afterCompose = {Component comp ->

        g = CatchplusUtils.getApplicationTagLib()
        locale = new Locale(shiroService.account(true).language)

        /*ProgressWindow progressWindow = new ProgressWindow(headwindow,null)
        progressWindow.doModal()*/



        if (!shiroService.getActiveShiroUser(true))
        {
            new InstitutionChooseWindow(headwindow, shiroService.account(true)).doModal()
        }
        else
        {

            if (GrailsUtil.environment != 'development')
                Clients.confirmClose("Hello ${shiroService.account(true).fullName()}.");

/*
            if (!headwindow.getDesktop().isServerPushEnabled())
            {
                headwindow.getDesktop().enableServerPush(true);
            }
*/

            /*blaService.registerComposer(this)*/



            if (listboxsearchresult)
                listboxsearchresult.itemRenderer = new SearchListItemRenderer()


            mytreeaap.treeitemRenderer = new ExplorerTreeItemRenderer()

            def aIter = WorkSpace.list().iterator()
            if (aIter.hasNext())
                selectedWorkSpace = aIter.next()

            this.baseContentKeys = selectedWorkSpace?.baseContentMetaRecordKey?.sort()*.label
            this.bundleKeys = selectedWorkSpace?.bundleMetaRecordKey?.sort()*.label
            this.collectionKeys = selectedWorkSpace?.collectionMetaRecordKey?.sort()*.label

            mytreeaap.model = new ExplorerTreeModel(selectedWorkSpace)


            contentBookmarkListbox = new BookmarkListbox(new ListModelList(shiroService.contentBookmarks()))
            contentBookmarkListbox.parent = tabpanelContent

            bundleBookmarkListbox = new BookmarkListbox(new ListModelList(shiroService.bundleBookmarks()))
            bundleBookmarkListbox.parent = tabpanelBundle

            collectionBookmarkListbox = new BookmarkListbox(new ListModelList(shiroService.collectionBookmarks()))
            collectionBookmarkListbox.parent = tabpanelCollection

            System.out.println('Demo composed2');

        }

        /*println progressWindow
        if(progressWindow)
            progressWindow.close()
*/


    }

    public void updateComp()
    {
        //System.out.println('updateComp()');
        Iterator iter = this.compSet.iterator()
        while (iter.hasNext())
        {
            UpdateListerner updateListerner = iter.next()
            if (updateListerner.parent)
            {
                updateListerner.update()
            }
            else
            {
                iter.remove()
            }
        }
    }



    def onSelect_mytreeaap(SelectEvent event)
    {
        BaseRepository baseRepository = BaseRepository.get(mytreeaap.selectedItem.value.id)
        showDetail(baseRepository)
    }

    def onClick_mytreeaap(Event event)
    {
        BaseRepository baseRepository = BaseRepository.get(mytreeaap.selectedItem?.value?.id)
        if (baseRepository)
            showDetail(baseRepository)
    }

    @Deprecated
    private void showInTree(BaseRepository object)
    {
        selectedWorkSpace = WorkSpace.list().iterator().next()
        mytreeaap.model = new ExplorerTreeModel(selectedWorkSpace)

        selectedObject = BaseRepository.read(object.id)
        mytreeaap.selectedItem = mytreeaap.renderItemByNode(selectedObject)
        showDetail(selectedObject)
    }

    private void setTab(Tabpanel p, String title)
    {
        if (selectedTab) mytabbox.tabs.removeChild(selectedTab)
        if (selectedTabPanel) mytabbox.tabpanels.removeChild(selectedTabPanel)

        selectedTab = new MyTab(title)
        selectedTabPanel = p
        mytabbox.tabs.insertBefore(selectedTab, null)
        mytabbox.tabpanels.insertBefore(selectedTabPanel, null)
        mytabbox.selectedPanel = selectedTabPanel

    }



    public void showAndSelectDetail(BaseRepository baseRepository)
    {
        mytreeaap.selectedItem = mytreeaap.renderItemByNode(baseRepository)
        SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
        Events.postEvent(selectEventEvent);
    }

    public void showDetail(BaseRepository baseRepository)
    {
        selectedObject = baseRepository
        //   baseRepository.refresh()

        if (baseRepository instanceof Bundle)
        {
            setTab(new BundleTabpanel(headwindow, baseRepository, this), g.message(locale: locale, code: "bundle.show"))
        }
        else if (baseRepository instanceof BaseContent)
        {
            setTab(new ContentTabpanel(headwindow, baseRepository, this), g.message(locale: locale, code: "baseContent.show"))

        }
        else if (baseRepository instanceof Collection)
        {
            setTab(new CollectionTabpanel(headwindow, this, baseRepository), g.message(locale: locale, code: "collection.show"))

        }

    }



    def onSelect_listboxsearchresult(SelectEvent event)
    {
        selectedObject = BaseRepository.read(listboxsearchresult.selectedItem.value.id)
        showDetail(selectedObject)
    }

    def onClick_toolbalbuttonDisplayUser(Event event)
    {
        new MyAccountWindow(headwindow, shiroService.account(true)).doModal()
    }

    def onClick_listboxsearchresult(Event event)
    {
        selectedObject = BaseRepository.read(listboxsearchresult?.selectedItem?.value?.id)
        if (selectedObject)
            showDetail(selectedObject)
    }

    public void updateTree(BaseRepository baseRepository)
    {
        treeItems.each {treeItem ->
            if (treeItem.value == baseRepository)
                treeItem.label = baseRepository.title
        }
    }

    def onClick_test(Event event)
    {

    }

    def onClick_toolbarbuttonCreateCollection(Event event)
    {
        new CollectionWindow(headwindow, null).doModal()
    }


    def onClick_toolbarbuttonSignOut(Event event)
    {
        Executions.sendRedirect("/auth/signOut");
    }

    def onClick_toolbarbuttonScaffolding(Event event)
    {
        Executions.sendRedirect("/");
    }

    def onClick_toolbarbuttonSwitchInstitution(Event event)
    {
        new InstitutionChooseWindow(headwindow, shiroService.account(true)).doModal()
    }

    def onClick_toolbarbuttonInstituutBeheer(Event event)
    {

        headwindow.select("#toolbarbuttonInstituutBeheer")[0].disabled = true
        this.showInstitutionPanel()
    }

    def onClick_toolbarbuttonSuperAdminUserBeheer(Event event)
    {
        //west.open = false
        //west.visible = false
        headwindow.select("#toolbarbuttonSuperAdminUserBeheer")[0].disabled = true
        this.showSuperUserAccountPanel()
    }

    def onClick_toolbarbuttonUserBeheer(Event event)
    {
        //west.open = false
        //west.visible = false
        headwindow.select("#toolbarbuttonUserBeheer")[0].disabled = true
        this.showUserAccountPanel()
    }

    private void showInstitutionPanel()
    {
        Tab t = new UserAdminTab(g.message(locale: locale, code: "institution.management"), "toolbarbuttonInstituutBeheer")
        mytabbox.tabs.insertBefore(t, null)
        Tabpanel p = new InstitutionManageTabpanel(headwindow)
        mytabbox.tabpanels.insertBefore(p, null)
        mytabbox.selectedPanel = p
        //west.open = false
        //west.visible = false

    }

    private void showSuperUserAccountPanel()
    {
        Tab t = new UserAdminTab(g.message(locale: locale, code: "account.management"), 'toolbarbuttonSuperAdminUserBeheer')
        mytabbox.tabs.insertBefore(t, null)
        Tabpanel p = new UserIATabpanel(headwindow)
        mytabbox.tabpanels.insertBefore(p, null)
        mytabbox.selectedPanel = p
    }

    private void showUserAccountPanel()
    {
        Tab t = new UserAdminTab(g.message(locale: locale, code: "account.management"), 'toolbarbuttonUserBeheer')
        mytabbox.tabs.insertBefore(t, null)
        Tabpanel p = new UserManageTabpanel(headwindow)
        mytabbox.tabpanels.insertBefore(p, null)
        mytabbox.selectedPanel = p
    }

    private void showUserRightsPanel(Collection collection)
    {
        isManageCollectionTabOpen = true
        Tab t = new CollectionManageTab(g.message(locale: locale, code: "collection.management"), g.resource(dir: 'images/16x16', file: 'group_key.png', absolute: 'true').toString())
        mytabbox.tabs.insertBefore(t, null)
        Tabpanel p = new CollectionAccessTabpanel(collection)
        mytabbox.tabpanels.insertBefore(p, null)
        mytabbox.selectedPanel = p
    }



    def onClick_buttonsearch(Event event)
    {
        if (textboxsearch.value != '')
        {
            def searchResult = searchableService.search(textboxsearch.value, [offset: 0, max: 10000])


            def iter = searchResult.results.iterator()

            ArrayList repoList = new ArrayList()

            while (iter.hasNext())
            {
                def obj = iter.next()
                if (obj instanceof BaseRepository && obj.deleted)
                    iter.remove()
                else
                if (obj instanceof MetaRecord)
                {
                    MetaRecord metaRecord = MetaRecord.read(obj.id)
                    repoList.add(metaRecord.baseRepository)
                    iter.remove()
                }

            }

            searchResult.results.addAll(repoList)

            listboxsearchresult.setModel(new ListModelList(searchResult.results))
        }

    }

    class SearchListItemRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {
            obj = BaseRepository.read(obj.id)

            listitem.appendChild(new Listcell(obj.title));
            listitem.appendChild(new Listcell(GrailsNameUtils.getShortName(obj.class).toString()));
            listitem.value = obj
        }
    }

    class UserListItemRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            //Account a=Account.read( obj.account.id)
            //a.refresh()

            //System.out.println("RENDER2: "+a.fullName());

            listitem << {
                listcell(label: obj.account.username)
                listcell(label: obj.account.fullName())

                listcell(label: obj.account.activated ? g.message(locale: locale, code: "default.boolean.true") : g.message(locale: locale, code: "default.boolean.false"))
                listcell() {
                    checkbox(checked: obj.active, onCheck: {checkEvent ->
                        ShiroUser.withTransaction {
                            ShiroUser user = ShiroUser.get(obj.id)
                            user.active = (checkEvent as CheckEvent).checked
                            user.store()
                        }
                    })

                }
                listcell(label: obj.institution)
                listitem.value = obj
            }
        }
    }

    class IAUserListItemRenderer implements ListitemRenderer
    {

        private self = this

        private long id

        private InstitutionManageTabpanel institutionTabpanel

        public IAUserListItemRenderer(InstitutionManageTabpanel institutionTabpanel)
        {
            this.institutionTabpanel = institutionTabpanel
        }

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {
            id = obj.id

            listitem << {
                listcell(label: obj.account.username)
                listcell(label: obj.account.fullName())

                listcell() {
                    toolbarbutton(tooltiptext: g.message(locale: locale, code: "default.remove"), image: "${g.resource(dir: 'images/16x16', file: 'remove.png', absolute: 'true')}", onClick: {checkEvent ->

                        Messagebox.show(g.message(locale: locale, code: "default.button.delete.confirm.message"), g.message(locale: locale, code: "shiroUser.remove"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
                        {
                            public void onEvent(Event evt) throws InterruptedException
                            {
                                if (evt.getName().equals("onOK"))
                                {
                                    ListModelList listModelList = self.institutionTabpanel.select("#listboxAdminUsers")[0].model

                                    ShiroUser shiroUser = ShiroUser.get(id)
                                    shiroUser.removeFromRoles(institutionTabpanel.getSelectedInstitution().administratorRole())
                                    shiroUser.save(flush: true)
                                    Iterator iter = listModelList.iterator()
                                    while (iter.hasNext())
                                    {
                                        if (iter.next().id == shiroUser.id)
                                            iter.remove()
                                    }


                                }
                            }
                        });

                        /*ShiroUser.withTransaction {

                            ShiroUser shiroUser = ShiroUser.get(obj.id)
                            System.out.println(institutionTabpanel.getSelectedInstitution().superAdminRole());
                            System.out.println(shiroUser.roles);
                            shiroUser.removeFromRoles(institutionTabpanel.getSelectedInstitution().superAdminRole())
                            shiroUser.store()
                            shiroUser.refresh()
                            System.out.println(shiroUser.roles);



                            System.out.println(shiroUser);                                                       f

                            System.out.println(institutionTabpanel.select("#listboxAdminUsers")[0].model);
                            Iterator iter = listModelList.iterator()
                            while (iter.hasNext()) {
                                if (iter.next().id == shiroUser.id)
                                    iter.remove()
                            }
                            //institutionTabpanel.select("#listboxAdminUsers")[0].model.remove(shiroUser)
                            System.out.println(institutionTabpanel.select("#listboxAdminUsers")[0].model);
                        }*/
                    })

                }
                listitem.value = obj
            }
        }
    }


    class InstitutionListItemRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            listitem << {
                listcell(label: obj.shortName)
                listcell(label: obj.websiteUrl)
                listcell(label: obj.name)
                if (!(obj as Institution).hasAdministrator())
                    listcell() {
                        image(tooltiptext: g.message(locale: locale, code: "institution.no.administrator"), src: "${g.resource(dir: 'images/16x16', file: 'error.png', absolute: 'true')}")
                    }

                else
                    listcell(label: "")

                listitem.value = obj

            }
        }
    }

    class ContentListItemRenderer implements ListitemRenderer
    {

        //private Collection collection

        public ContentListItemRenderer(/*Collection collection*/)
        {
            //this.collection = collection
        }

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            BaseContent content = BaseContent.read(obj.id)

            listitem << {
                listcell() {
                    a(label: content, tooltiptext: g.message(locale: locale, code: "baseContent.show"), onClick: {
                        //content.refresh()
                        composer.showDetail(BaseContent.read(content.id))
                    })
                }
                listcell(label: content.owner)
                listcell(label: content.createdBy)
                listcell()

                /*if (permissionService.baseContentDelete(content))
                                        {
                                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'delete.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.delete"), onClick: {
                                                Messagebox.show(g.message(locale: locale, code: "default.button.delete.confirm.message"), g.message(locale: locale, code: "baseContent.delete"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
                                                {
                                                    public void onEvent(Event evt) throws InterruptedException
                                                    {
                                                        if (evt.getName().equals("onOK"))
                                                        {
                                                            BaseContent object = BaseContent.get(self.content.id)
                                                            Bundle b = Bundle.read(object.ownerBundle.id)



                                                            object.myDelete()

                                                            self.composer.updateComp()

                                                            self.composer.mytreeaap.setModel(self.composer.mytreeaap.model)
                                                            self.composer.mytreeaap.selectedItem = self.composer.mytreeaap.renderItemByNode(b)
                                                            SelectEvent selectEventEvent = new SelectEvent("onSelect", self.composer.mytreeaap, null);
                                                            Events.postEvent(selectEventEvent);

                                                        }
                                                    }
                                                });

                                            })
                                        }*/

                //listcell() {
                /*if (permissionService.baseContentRead(obj))
                {*/
                /*a(tooltiptext: g.message(locale: locale, code: "baseContent.show"), label: g.message(locale: locale, code: "application.show"), onClick: {clickEvent ->

                    BaseContent baseContent = BaseContent.read(obj.id)
                    composer.showDetail(baseContent)

                })*/

                /*toolbarbutton(tooltiptext: g.message(locale: locale, code: "bundle.show"), image: "${g.resource(dir: 'images/16x16', file: 'view.png', absolute: 'true')}", onClick: {clickEvent ->

                    Bundle bundle1 = Bundle.get(obj.id)
                    composer.showAndSelectDetail(bundle1)

                })*/
                // }
                /*if ((obj.ownerCollection.id != collection.id) && permissionService.bundleRemove(bundle, collection))
                {
                    toolbarbutton(tooltiptext: g.message(locale: locale, code: "bundle.unlink"), image: "${g.resource(dir: 'images/16x16', file: 'unlink.png', absolute: 'true')}", onClick: {clickEvent ->


                        Bundle bundle1 = Bundle.get(obj.id)
                        Collection collection1 = Collection.get(collection.id)

                        gormService.removeBundle(bundle1, collection1)

                        mytreeaap.setModel(mytreeaap.model)
                        mytreeaap.selectedItem = mytreeaap.renderItemByNode(collection1)
                        mytreeaap.selectedItem.open = true

                        SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                        Events.postEvent(selectEventEvent);

                    })
                }*/

                //    }
                listitem.value = content
            }
        }


    }

    class CollectionListItemRenderer implements ListitemRenderer
    {

        private Bundle bundle

        public CollectionListItemRenderer(Bundle bundle)
        {
            this.bundle = bundle
        }

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            Collection collection = Collection.read(obj.id)
            //Bundle removeBundle = Bundle.get(self.bundle.id)

            listitem << {
                listcell() {
                    a(label: obj, tooltiptext: g.message(locale: locale, code: "collection.show"), onClick: {
                        //self.composer.showAndSelectDetail(BaseRepository.read(obj.id))
                        composer.showDetail(BaseRepository.read(obj.id))
                    })
                }
                listcell(label: collection.owner)
                listcell(label: collection.createdBy)

                listcell() {
                    //toolbarbutton(tooltiptext: self.composer.g.message(locale: self.composer.locale, code: "collection.show"), image: "${self.composer.g.resource(dir: 'images/16x16', file: 'view.png', absolute: 'true')}", onClick: {clickEvent ->
                    /*a(tooltiptext: self.composer.g.message(locale: self.composer.locale, code: "collection.show"), self.composer.g.message(locale: self.composer.locale, code: "application.show"), onClick: {clickEvent ->
                        self.composer.showAndSelectDetail(Collection.get(obj.id))
                    })*/
                    if (bundle.ownerCollection.id != collection.id && permissionService.bundleRemove(bundle, collection))
                    {
                        toolbarbutton(tooltiptext: g.message(locale: locale, code: "bundle.unlink"), image: g.resource(dir: 'images/16x16', file: 'unlink.png', absolute: 'true'), onClick: {clickEvent ->

                            Bundle bundle1 = Bundle.get(bundle.id)

                            gormService.removeBundle(bundle1, Collection.get(obj.id))

                            mytreeaap.selectedItem = null
                            mytreeaap.setModel(new ExplorerTreeModel(selectedWorkSpace))
                            composer.showDetail(bundle1)
                            /*mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle1)
                            mytreeaap.selectedItem.open = true

                            SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                            Events.postEvent(selectEventEvent);*/

                        })
                    }
                }
                listitem.value = obj
            }
        }

    }


    class BundleListItemRenderer implements ListitemRenderer
    {

        private Collection collection

        public BundleListItemRenderer(Collection collection)
        {
            this.collection = collection
        }

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            /*println  i+" "+listitem + ' '+obj*/
            Bundle bundle = Bundle.read(obj.id)


            listitem << {
                listcell() {
                    a(label: bundle, tooltiptext: g.message(code: "bundle.show"), onClick: {
                        // composer.showAndSelectDetail(BaseRepository.read(bundle.id))
                        composer.showDetail(BaseRepository.read(bundle.id))
                    })
                }
                listcell(label: bundle.owner)
                listcell(label: bundle.createdBy)

                listcell() {
                    if (permissionService.bundleRead(bundle))
                    {
                        a(tooltiptext: g.message(locale: locale, code: "bundle.show"), g.message(locale: locale, code: "application.show"), onClick: {clickEvent ->

                            Bundle bundle1 = Bundle.get(bundle.id)
                            //composer.showAndSelectDetail(bundle1)
                            composer.showDetail(bundle1)

                        })

                        /*toolbarbutton(tooltiptext: g.message(locale: locale, code: "bundle.show"), image: "${g.resource(dir: 'images/16x16', file: 'view.png', absolute: 'true')}", onClick: {clickEvent ->

                            Bundle bundle1 = Bundle.get(obj.id)
                            composer.showAndSelectDetail(bundle1)

                        })*/
                    }
                    if ((bundle.ownerCollection.id != collection.id) && permissionService.bundleRemove(bundle, collection))
                    {
                        toolbarbutton(tooltiptext: g.message(locale: locale, code: "bundle.unlink"), image: "${g.resource(dir: 'images/16x16', file: 'unlink.png', absolute: 'true')}", onClick: {clickEvent ->


                            Bundle bundle1 = Bundle.get(bundle.id)
                            Collection collection1 = Collection.get(collection.id)

                            gormService.removeBundle(bundle1, collection1)
                            mytreeaap.selectedItem = null
                            mytreeaap.setModel(new ExplorerTreeModel(selectedWorkSpace))
                            collection1.refresh()
                            composer.showDetail(collection1)
                        })
                    }

                }
                listitem.value = bundle
            }
        }


    }


    class BookmarkListItemRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            Bookmark bookmark = Bookmark.read(obj.id)

            listitem << {
                listcell(label: bookmark.baseRepository.deleted ? g.message(locale: locale, code: "baseRepository.deleted", args: [bookmark.baseRepository.id]) : bookmark.baseRepository, tooltiptext: bookmark.description)
                listcell() {
                    toolbarbutton(height: "20px", image: "${g.resource(dir: 'images/16x16', file: 'delete.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "bookmark.delete"), onClick: {
                        bookmark.refresh()
                        Listitem li = listitem
                        Listbox lb = li.listbox
                        def parent = lb.parent
                        ListModelList model = lb.model

                        model.remove(bookmark)
                        bookmark.delete(flush: true)

                        /*System.out.println(listitem);
                        System.out.println(listitem.listbox);
                        System.out.println(this.select("#toolbarbuttonAddclipboard")[0]);
                        System.out.println('DSDssad');*/
                        //this.select("#toolbarbuttonAddclipboard")[0].visible = true

                    })
                }
                /*listitem.value = account*/
            }
            listitem.value = obj
        }
    }



    class AccountListItemRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            Account account = Account.read(obj.id)

            String temp = ''
            account.shiroUser.each {user ->
                temp = temp + (user.institution ? user.institution : "") + ', '
            }
            temp = temp.substring(0, temp.length() - 2)
            listitem << {
                listcell(label: account.username)
                listcell(label: temp)
                listitem.value = account
            }
        }
    }


    class MetaRecordListItemRenderer implements ListitemRenderer
    {

        boolean editable

        public MetaRecordListItemRenderer(boolean editable)
        {
            this.editable = editable

        }

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {

            listitem.appendChild(new Listcell(obj.key))

            if (editable || obj.value?.length() > 50)
            {
                Listcell lc = new Listcell()
                lc.parent = listitem
                Textbox tb = new Textbox(obj.value)
                tb.rows = 2
                tb.maxlength = 2000
                tb.width = '97%'
                tb.parent = lc

                if (editable)
                {
                    tb.addEventListener('onChange') {event ->
                        obj.value = tb.value
                    }
                }
                else
                    tb.readonly = true

            }
            else
            {
                listitem.appendChild(new Listcell(obj.value ? obj.value : ""))
            }

            /*if (editable) {
           Listcell lc = new Listcell()
           lc.parent = listitem

           Textbox tb = new Textbox(obj.value)
           tb.addEventListener('onChange') {event ->
               obj.value = tb.value
           }

           tb.rows = 2
           tb.width = '99%'
           tb.parent = lc
       }
       else
           listitem.appendChild(new Listcell(obj.value))*/

            listitem.value = obj
        }
    }



    class RoleObjectRenderer implements ListitemRenderer
    {

        @Override
        public void render(Listitem listitem, Object obj, int i) throws Exception
        {
            Collection collection = BaseRepository.read(selectedObject.id)
            def roleObject = BaseDomain.read(obj.id)


            ShiroRole readRole = collection.userReadRole()
            ShiroRole addRole = collection.userAddRole()
            ShiroRole administratorRole = collection.administratorRole()


            Listbox lb = new Listbox()
            lb.hflex = '1'
            lb.mold = 'select'
            lb.appendItem(g.message(locale: locale, code: "baseRepository.no.access.select"), '0')
            lb.appendItem(g.message(locale: locale, code: "baseRepository.read.access.select"), "${readRole.id}")
            lb.appendItem(g.message(locale: locale, code: "baseRepository.add.access.select"), "${addRole.id}")
            lb.appendItem(g.message(locale: locale, code: "baseRepository.admin.access.select"), "${administratorRole.id}")

            if (roleObject.roles?.contains(readRole))
                lb.selectedIndex = 1
            else if (roleObject.roles?.contains(addRole))
                lb.selectedIndex = 2
            else if (roleObject.roles?.contains(administratorRole))
                lb.selectedIndex = 3
            else
                lb.selectedIndex = 0

            lb.addEventListener('onSelect') {event ->

                BaseDomain.withTransaction {
                    roleObject.refresh()
                    collection.refresh()
                    readRole.refresh()
                    addRole.refresh()
                    administratorRole.refresh()

                    roleObject.removeFromRoles(readRole)
                    roleObject.removeFromRoles(administratorRole)
                    roleObject.removeFromRoles(addRole)

                    ShiroRole selectedRole = ShiroRole.read(Long.parseLong(event.reference.value))
                    if (selectedRole)
                        roleObject.addToRoles(selectedRole)

                    roleObject.store()
                }
            }

            if (roleObject instanceof ShiroGroup)
                listitem.appendChild(new Listcell(obj.name));
            else
            {
                listitem.appendChild(new Listcell(obj.account.username));
                listitem.appendChild(new Listcell(obj.institution.toString()));
            }
            Listcell lc = new Listcell()
            lc.appendChild(lb)
            listitem.appendChild(lc);
            listitem.value = obj

        }

    }


    class InstitutionChooseWindow extends MyWindow
    {

        public InstitutionChooseWindow(Component parent, Account account)
        {

            super(parent)

            def users = account.shiroUser.findAll {it.active}.sort()

            this.width = 300
            this.height = 100 + 20 * users.size()

            this.closable = shiroService.getActiveShiroUser(true) != null

            this.title = g.message(locale: locale, code: "institution.select")
            this << {
                vbox() {
                    radiogroup(id: 'institutionChooseRadiogroup', orient: 'vertical') {
                        users.each {shiroUser ->
                            radio(id: shiroUser.id, label: shiroUser.institution.toString() + ": " + shiroUser.id, checked: shiroService.getActiveShiroUser(true)?.id == shiroUser.id, onCheck: {
                                this.select("#institutionChooseSubmit")[0].disabled = false
                            })
                        }

                    }
                    button(id: 'institutionChooseSubmit', autodisable: "self", disabled: !shiroService.getActiveShiroUser(true), label: g.message(locale: locale, code: "default.select"), width: 100, height: 30, onClick: {

                        ShiroUser selectedShiroUser = ShiroUser.read(Long.parseLong(this.select("#institutionChooseRadiogroup")[0].getSelectedItem().id))

                        if (!shiroService.getActiveShiroUser(true) || (shiroService.getActiveShiroUser(true).id != selectedShiroUser.id))
                        {


                            shiroService.setActiveShiroUser(selectedShiroUser)
                            shiroService.getActiveShiroUser(false).lastLogOn = new Date()
                            Executions.sendRedirect("/application");
                        }
                        else
                            this.close()
                    })
                }
            }
        }

    }

    class ProgressWindow extends MyWindow
    {

        public ProgressWindow(Component parent, Account account)
        {

            super(parent)



            this.closable = false

            this.title = g.message(locale: locale, code: "institution.select")
            this << {
                progressmeter(value: 50)
            }
            /*vbox() {
                    radiogroup(id: 'institutionChooseRadiogroup', orient: 'vertical') {
                        users.each {shiroUser ->
                            radio(id: shiroUser.id, label: shiroUser.institution.toString() + ": " + shiroUser.id, checked: shiroService.getActiveShiroUser(true)?.id == shiroUser.id, onCheck: {
                                this.select("#institutionChooseSubmit")[0].disabled = false
                            })
                        }

                    }
                    button(id: 'institutionChooseSubmit', disabled: !shiroService.getActiveShiroUser(true), label: g.message(locale: locale, code: "default.select"), width: 100, height: 30, onClick: {

                        ShiroUser selectedShiroUser = ShiroUser.read(Long.parseLong(this.select("#institutionChooseRadiogroup")[0].getSelectedItem().id))

                        if (!shiroService.getActiveShiroUser(true) || (shiroService.getActiveShiroUser(true).id != selectedShiroUser.id))
                        {


                            shiroService.setActiveShiroUser(selectedShiroUser)
                            shiroService.getActiveShiroUser(false).lastLogOn = new Date()
                            Executions.sendRedirect("/application");
                        }
                        else
                            this.close()
                    })
                }
            }*/
        }

    }


    class MyAccountWindow extends MyWindow
    {

        public MyAccountWindow(Component parent, Account account)
        {
            super(parent)

            ListModelList model = new ListModelList(['nl', 'en'])

            this.title = g.message(locale: locale, code: "account.edit")
            this << {
                grid {
                    columns {
                        column(width: 150)
                        column()
                    }

                    rows {
                        row {

                            label(value: g.message(locale: locale, code: "account.username") + " *")
                            textbox(name: 'username', width: "99%", value: account.username)
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.firstName") + " *")
                            textbox(name: 'firstName', width: "99%", value: account.firstName)
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.lastName") + " *")
                            textbox(name: 'lastName', width: "99%", value: account.lastName)
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.telephoneNumber"))
                            textbox(name: 'telephoneNumber', width: "99%", value: account.telephoneNumber)
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.language") + " *")
                            listbox(id: 'languageListbox', model: model, mold: 'select', selectedIndex: model.indexOf(account.language))
                        }


                    }
                }
                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: g.message(locale: locale, code: "default.button.update.label"), width: 100, height: 30, onClick: {
                        account.refresh()

                        Listbox languageListbox = this.select("#languageListbox")[0]

                        String selectedLanguage = languageListbox.selectedItem.value
                        boolean switchedLanguage = selectedLanguage != account.language

                        if (selectedLanguage != account.language)
                        {
                            account.language = selectedLanguage
                        }
                        account.properties = self.params
                        account.username = account.username ? account.username : ""
                        account.firstName = account.firstName ? account.firstName : ""
                        account.lastName = account.lastName ? account.lastName : ""
                        account.telephoneNumber = account.telephoneNumber ? account.telephoneNumber : ""



                        if (account.validate() && account.store())
                        {
                            switchedLanguage ? Executions.sendRedirect("/application") : self.close()
                        }
                        else
                            self.renderErrors(bean: account)

                    })
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })
                }

            }
        }


    }



    class AccountWindow extends MyWindow
    {


        Listbox userListbox

        private boolean editMode

        //def listener

        public AccountWindow(Component parent, def userListbox, ShiroUser shiroUser, Institution institution, ShiroRole role)
        {
            super(parent)
            this.userListbox = userListbox

            println institution
            println role


            this.editMode = shiroUser != null

            ListModelList model = new ListModelList('nl', 'en')


            this.title = editMode ? g.message(locale: locale, code: "shiroUser.edit") : g.message(locale: locale, code: "shiroUser.create")
            this << {
                grid {
                    columns {
                        column(width: 150)
                        column()
                    }

                    rows {
                        row {
                            label(value: g.message(locale: locale, code: "account.username") + " *")
                            textbox(name: 'username', width: "99%", value: editMode ? shiroUser.account.username : '')
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.firstName") + " *")
                            textbox(name: 'firstName', width: "99%", value: editMode ? shiroUser.account.firstName : '')
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.lastName") + " *")
                            textbox(name: 'lastName', width: "99%", value: editMode ? shiroUser.account.lastName : '')
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.telephoneNumber"))
                            textbox(name: 'telephoneNumber', width: "99%", value: editMode ? shiroUser.account.telephoneNumber : '')
                        }
                        row {
                            label(value: g.message(locale: locale, code: "account.language") + " *")
                            listbox(id: 'languageListbox', /*name: 'language',*/ model: model, mold: 'select', selectedIndex: editMode ? model.indexOf(shiroUser.account.language) : 0)
                        }


                    }
                }
                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: editMode ? g.message(locale: locale, code: "default.button.update.label") : g.message(locale: locale, code: "default.button.create.label"), width: 100, height: 30, onClick: {

                        Account account = editMode ? Account.get(shiroUser.account.id) : new Account()
                        account.properties = self.params
                        account.username = account.username ? account.username : ""
                        account.firstName = account.firstName ? account.firstName : ""
                        account.lastName = account.lastName ? account.lastName : ""
                        account.telephoneNumber = account.telephoneNumber ? account.telephoneNumber : ""

                        Listbox languageListbox = this.select("#languageListbox")[0]

                        account.language = languageListbox.selectedItem.value


                        if (!editMode)
                            account.activationCode = RandomStringUtils.random(5, true, true)

                        if (account.validate() && account.store())
                        {
                            if (!editMode)
                            {
                                institution.refresh()

                                shiroUser = new ShiroUser(institution: institution, account: account).store()
                                if (role) shiroUser.addToRoles(ShiroRole.read(role.id))

                                /*shiroUser.save(flush: true)
                                shiroUser.refresh()*/

                                //shiroService.getActiveShiroUser(true)?.institution?.group?.addToUsers(shiroUser)
                                /*institution?.group?.addToUsers(shiroUser)
                                institution?.group?.save(flush:true)*/
                                emailService.sendNewUserMail(account)
                            }
                            else
                            {
                                account.store()
                                userListbox.model.remove(shiroUser)
                            }

                            //BUGIX: anders wordt account  niet live geupdate in de itemrenderer
                            shiroUser.account = account

                            //System.out.println('SU4: ' + shiroUser.account.fullName());
                            (userListbox.model as ListModelList).add(0, shiroUser)
                            userListbox.selectedIndex = 0

                            self.close()
                        }
                        else
                        {
                            self.renderErrors(bean: account)
                        }

                    })
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })
                }

            }
        }


    }

    class InstitutionWindow extends MyWindow
    {


        Listbox institutionListbox

        private boolean editMode

        //Institution institution

        //def listener

        public InstitutionWindow(Component parent, def institutionListbox, Institution institution)
        {
            super(parent)
            this.institutionListbox = institutionListbox
            this.editMode = institution != null

            this.title = editMode ? g.message(locale: locale, code: "institution.edit") : g.message(locale: locale, code: "institution.create")
            this << {
                grid {
                    columns {
                        column(width: 150)
                        column()
                    }

                    rows {
                        row {
                            label(value: g.message(locale: locale, code: "institution.name") + " *")
                            textbox(name: 'name', width: "99%", value: editMode ? institution.name : '')
                        }
                        row {
                            label(value: g.message(locale: locale, code: "institution.websiteUrl"))
                            hlayout(width: "100%") {
                                label(value: 'http://', width: '40px', style: 'line-height:21px')
                                textbox(id: 'websiteUrl', name: 'url', value: editMode ? institution.websiteUrl : '', hflex: 1)
                            }
                        }



                        row {
                            label(value: g.message(locale: locale, code: "institution.shortNameExp") + " *")
                            textbox(name: 'shortName', width: "99%", value: editMode ? institution.shortName : '')
                        }
                        /*row {
                            label(value: 'Voornaam:')
                            textbox(name: 'firstName', width: "99%", value: editMode ? shiroUser.account.firstName : '')
                        }
                        row {
                            label(value: 'Achternaam:')
                            textbox(name: 'lastName', width: "99%", value: editMode ? shiroUser.account.lastName : '')
                        }*/

                    }
                }

                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: editMode ? g.message(locale: locale, code: "default.button.update.label") : g.message(locale: locale, code: "default.button.create.label"), width: 100, height: 30, onClick: {
                        institution = editMode ? Institution.get(institution.id) : new Institution()
                        institution.properties = self.params
                        institution.name = institution.name ? institution.name : ""
                        institution.websiteUrl = institution.websiteUrl ? institution.websiteUrl : ""
                        institution.shortName = institution.shortName ? institution.shortName : ""

                        Institution.withTransaction {
                            if (institution.validate() && institution.store())
                            {

                                if (editMode)
                                {

                                    //BUG FIX want dit werkt niet ---> institutionListbox.model.remove(institution)
                                    Iterator iter = (institutionListbox.model as ListModelList).iterator()
                                    while (iter.hasNext())
                                    {
                                        if (iter.next().id == institution.id)
                                            iter.remove()
                                    }
                                }
                                else
                                {
                                    testService.createInstitutionGroup(institution)
                                    testService.createInstitutionAdminRole(institution)
                                }

                                //institutionListbox.setModel(institutionListbox.model)
                                (institutionListbox.model as ListModelList).add(0, institution)
                                institutionListbox.selectedIndex = 0

                                self.close()
                            }
                            else
                                self.renderErrors(bean: institution)
                        }

                    })
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })
                }

            }
        }


    }


    class CollectionWindow extends MyWindow
    {


        Collection editCollection

        public CollectionWindow(Component parent, Collection editCollection)
        {
            super(parent)
            this.editCollection = editCollection

            this.title = editCollection ? "${g.message(locale: locale, code: 'collection.edit')}" : "${g.message(locale: locale, code: 'collection.create')}"
            this << {
                grid {
                    columns {
                        column(width: '120px')
                        column()
                    }

                    rows {
                        row {

                            label(value: g.message(locale: locale, code: "collection.title") + " *")
                            textbox(id: 'title', name: 'title', value: editCollection?.title, width: "99%")
                        }
                        row {
                            label(value: g.message(locale: locale, code: "collection.description") + " *")
                            textbox(name: 'description', value: editCollection?.description, rows: 3, width: "99%")
                        }

                        row(id: 'metaRow', spans: 2) {
                        }

                        /*row(spans: 2, style: 'text-align:center') {

                        }*/


                    }
                }
                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: g.message(locale: locale, code: editCollection ? 'default.button.update.label' : "default.button.create.label"), width: 100, height: 30)
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })
                }

                new MetaDiv(editCollection, composer.collectionKeys, true, g.message(code: 'metaRecord.collection.list', locale: locale)).parent = this.select("#metaRow")[0]

                this.select("#submit")[0].addEventListener('onClick') {event ->

                    WorkSpace workSpace = WorkSpace.get(selectedWorkSpace.id)

                    Collection collection = editCollection ? Collection.get(editCollection.id) : new Collection()
                    collection.properties = self.params

                    if (!editCollection)
                    {
                        //collection.owner = shiroService.getActiveShiroUser(true).institution
                        //collection.ownerWorkSpace = workSpace
                    }

                    collection.title = collection.title ? collection.title : ""
                    collection.description = collection.description ? collection.description : ""

                    //Collection.withTransaction {
                    if (!collection.validate())
                        self.renderErrors(bean: collection)
                    else
                    {
                        if (!editCollection)
                        {
                            collection.store()

                            //workSpace.addToCollection(collection)
                            //gormService.addCollectionToWorkSpace(collection, workSpace)
                            //workSpace.store()
                        }
                        else
                            collection.store()
                        //collection.store()

                        this.select("#metaDiv")[0].save(collection, composer.collectionKeys)

                        //(mytreeaap.model as AbstractTreeModel).get

                        //FIX Root?
                        mytreeaap.setModel(new ExplorerTreeModel(WorkSpace.get(selectedWorkSpace.id)))
                        //mytreeaap.selectedItem = mytreeaap.renderItemByNode(collection)
                        self.close()

                        composer.showDetail(collection)
                        composer.updateTree(collection)

                        //SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                        //Events.postEvent(selectEventEvent);
                    }

                    //}

                }

            }
        }

    }


    class BundleWindow extends MyWindow
    {


        Bundle editBundle

        public BundleWindow(Component parent, Bundle editBundle)
        {
            super(parent)
            this.editBundle = editBundle
            //this.id='contentWindow'
            this.title = editBundle ? "${g.message(locale: locale, code: 'bundle.edit')}" : "${g.message(locale: locale, code: 'bundle.create')}"
            this << {
                grid {
                    columns {
                        column(width: '120px')
                        column()
                    }

                    rows {
                        row {
                            label(value: g.message(locale: locale, code: "bundle.title") + " *")
                            textbox(id: 'title', name: 'title', value: editBundle?.title, width: "99%")
                        }
                        row {
                            label(value: g.message(locale: locale, code: "bundle.description") + " *")
                            textbox(name: 'description', value: editBundle?.description, rows: 3, width: "99%")
                        }

                        row(id: 'metaRow', spans: 2) {
                        }
                    }
                }

                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: g.message(locale: locale, code: "default.button.create.label"), width: 100, height: 30)
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })
                }


                new MetaDiv(editBundle, composer.selectedWorkSpace.bundleMetaRecordKey.sort()*.label, true, g.message(code: 'metaRecord.bundle.list', locale: locale)).parent = this.select("#metaRow")[0]

                this.select("#submit")[0].addEventListener('onClick') {event ->


                    Collection collection = Collection.get(selectedObject.id)

                    Bundle bundle = editBundle ? Bundle.get(editBundle.id) : new Bundle()

                    /*System.out.println(self.params);
                    System.out.println(self.params.class);
                    System.out.println(self.params.title);
                    System.out.println(bundle.title);
                    System.out.println(self.params.title == bundle.title);*/

                    bundle.properties = self.params

                    if (!editBundle)
                    {
                        bundle.owner = shiroService.getActiveShiroUser(true).institution
                        bundle.ownerCollection = collection
                    }

                    bundle.title = bundle.title ? bundle.title : ""
                    bundle.description = bundle.description ? bundle.description : ""

                    if (!bundle.validate())
                        self.renderErrors(bean: bundle)
                    else
                    {
                        if (!editBundle)
                        {
                            bundle.store()
                            /*Bundle.withTransaction {
                                bundle.store()
                                bundle.ownerCollection.addToBundle(bundle)
                            }

                            testService.createOwnerPermission(bundle)
                            testService.createRolePermission(bundle)*/
                        }
                        else
                            bundle.store()



                        this.select("#metaDiv")[0].save(bundle, composer.selectedWorkSpace.bundleMetaRecordKey.sort()*.label)
                        bundle.refresh()

                        //mytreeaap.u

                        //def s = mytreeaap.selectedItem

                        // mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle)
                        self.close()
                        //mytreeaap.setModel(mytreeaap.model)


                        if (editBundle)
                            composer.updateTree(bundle)
                        else
                            mytreeaap.setModel(mytreeaap.model)

                        composer.showDetail(bundle)

                        //    SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                        //  Events.postEvent(selectEventEvent);
                    }

                }

            }
        }

    }


    class ContentBatchWindow extends MyWindow
    {

        private def files


        public ContentBatchWindow(Component parent)
        {
            super(parent)
            this.title = g.message(locale: locale, code: "baseContent.create")
            this << {
                grid {
                    columns {
                        column(width: '120px')
                        column()
                    }

                    rows {
/*
                        row {

                            label(value: g.message(locale: locale, code: "baseContent.title"))
                            textbox(id: 'title', name: 'title', width: "99%")
                        }
*/
                        row {
                            label(value: g.message(locale: locale, code: "baseContent.description"))
                            textbox(name: 'description', rows: 3, width: "97%")
                        }
                        /*if (!this.editContent?.storedFile)
                        {
                            row {
                                label(id: 'urlLabel', value: g.message(locale: locale, code: "baseContent.url"))
                                textbox(id: 'urlTextbox', name: 'url', width: "99%")
                            }
                        }*/
                        row {
                            cell {
                                label(value: g.message(locale: locale, code: "baseContent.storedFile"))
                            }

                            cell {
                                //label(value: editContent?.storedFile ? editContent?.storedFile?.originalFilename : '', id: 'filelabel', visible: editContent?.storedFile ? true : false)
                                button(upload: "true,multiple=true,native", label: g.message(locale: locale, code: "storedFile.upload.multi"), id: 'upload')
                            }
                        }
                        row(id: 'fileRow', visible: false, spans: 2) {
                            listbox(id: 'files', mold: "paging", pageSize: 4 /*multiple: true, checkmark: true, itemRenderer: new AccountListItemRenderer()*/) {
                                /*auxhead() {
                                    auxheader(label:'Uploaded files' *//*g.message(locale: locale, code: "collection.list")*//*, colspan: 2)
                                }*/

                                listhead() {
                                    listheader(label: 'Uploaded files' /*g.message(locale: locale, code: "collection.title")*/)
                                    /*   listheader(label: g.message(locale: locale, code: "collection.owner"))*/
                                }

                            }
                        }
                        row(id: 'metaRow', spans: 2)


                    }
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', disabled: true, autodisable: "self", label: g.message(locale: locale, code: "default.button.create.label"), width: 100, height: 30)
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })


                }
                //MetaDiv(BaseRepository baseRepository, List metaKeys, boolean editable)
                new MetaDiv(null, composer.baseContentKeys, true, g.message(code: 'metaRecord.content.list', locale: locale)).parent = this.select("#metaRow")[0]

                this.select("#upload")[0].addEventListener('onUpload') {event ->

                    HashMap tempMap = new HashMap()

                    UploadEvent uploadEvent = (UploadEvent) event
                    this.files = uploadEvent.medias

                    boolean duplicate = false
                    String aFileName
                    String md5

                    for (int i = 0; i < this.files.size() && !duplicate; i++)
                    {
                        def file = files[i]

                        def bytes = file.binary ? file.inMemory() ? file.byteData : file.streamData.bytes : file.stringData.bytes
                        md5 = org.codehaus.groovy.grails.plugins.codecs.MD5Codec.encode(bytes)

                        aFileName = tempMap.containsKey(md5) ? tempMap.get(md5) : StoredFile.findByMd5AndBundle(md5, selectedObject.id)?.originalFilename

                        if (aFileName)
                        {
                            duplicate = true
                        }
                        else
                            tempMap.put(md5, file.name)

                    }

                    if (!duplicate)
                    {
                        this.select("#submit")[0].disabled = false
                        this.select("#upload")[0].visible = false
                        this.select("#fileRow")[0].visible = true


                        (this.select("#files")[0] as Listbox).setModel(new ListModelList(this.files))
                    }
                    else
                    {
                        Messagebox.show(g.message(locale: locale, code: "storedFile.MD5.unique.error", args: [aFileName, md5]), g.message(locale: locale, code: "storedFile.MD5.unique.error.title"), Messagebox.OK, Messagebox.ERROR, new org.zkoss.zk.ui.event.EventListener()
                        {
                            public void onEvent(Event evt) throws InterruptedException
                            {
                                if (evt.getName().equals("onOK"))
                                {
                                    //println 'exitsssssss'
                                }
                            }
                        });

                    }
                }


                this.select("#submit")[0].addEventListener('onClick') {event ->

                    //BaseDomain.withTransaction {

                    Bundle bundle = Bundle.get(selectedObject.id)

                    BaseContent baseContent = new BaseContent()
                    baseContent.properties = self.params

                    baseContent.owner = shiroService.getActiveShiroUser(true).institution
                    baseContent.ownerBundle = bundle

                    //baseContent.title = baseContent.title ? baseContent.title : ""
                    baseContent.description = baseContent.description ? baseContent.description : ""
                    //baseContent.url = baseContent.url ? baseContent.url : ""

                    if (!baseContent.validate())
                        self.renderErrors(bean: baseContent)
                    else
                    {

                        files.each {file ->


                            BaseContent content = null
                            //gormService.createContent(baseContent.title,baseContent.description,ownerBundle: bundle)
                            //println 'A'
                            BaseDomain.withTransaction {
                                content = new BaseContent(createdBy: shiroService.getActiveShiroUser(true), title: StringUtils.substringBeforeLast(file.name, '.'), description: baseContent.description, ownerBundle: bundle, owner: shiroService.getActiveShiroUser(true).institution).save()
                                //println 'B'
                                //bundle.addContent(content)
                            }
                            /*BaseDomain.withTransaction {
                                testService.createRolePermission(content)
                            }*/
                            //println 'C'

                            /*BaseContent content = new BaseContent()
                            println 'A'
                            content.properties = baseContent.properties
                            println 'B'
                            content.store()*/

                            this.select("#metaDiv")[0].save(content, composer.baseContentKeys)
                            //println 'D'

                            StoredFile storedFile = new StoredFile()
                            storedFile.originalFilename = file.name
                            storedFile.setBytes(file.binary ? file.inMemory() ? file.byteData : file.streamData.bytes : file.stringData.bytes)
                            storedFile.setContentLength(storedFile.bytes.length)
                            storedFile.setContentType(file.contentType)
                            storedFile.md5 = org.codehaus.groovy.grails.plugins.codecs.MD5Codec.encode(storedFile.bytes)

                            storedFile.content = content
                            storedFile.mySave()

                            content.storedFile = storedFile

                            //println 'E'
                            //content.save(flush:true)

                            //println content

                        }

                        composer.updateComp()

                        self.close()
                        composer.showDetail(bundle)

/*
//mytreeaap.setModel(mytreeaap.model)
mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle)
self.close()

SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
Events.postEvent(selectEventEvent);*/

                    }

                    //}
                }
            }

        }
    }







    class ContentWindow extends MyWindow
    {

        //private def media

        BaseContent editContent
        StoredFile storedFile

        public ContentWindow(Component parent, BaseContent editContent)
        {
            super(parent)
            this.editContent = editContent
            //this.id='contentWindow'
            this.title = this.editContent ? g.message(locale: locale, code: "baseContent.edit") : g.message(locale: locale, code: "baseContent.create")
            this << {
                grid {
                    columns {
                        column(width: '120px')
                        column()
                    }

                    rows {
                        row {

                            label(value: g.message(locale: locale, code: "baseContent.title") + " *")
                            textbox(id: 'title', name: 'title', value: editContent?.title, width: "99%")
                        }
                        row {
                            label(value: g.message(locale: locale, code: "baseContent.description") + " *")
                            textbox(name: 'description', value: editContent?.description, rows: 3, width: "97%")
                        }
                        if (!this.editContent?.storedFile)
                        {
                            row {
                                label(id: 'urlLabel', value: g.message(locale: locale, code: "baseContent.url"))
                                //    textbox(id: 'urlTextbox', name: 'url', value: editContent?.url ?  editContent?.url : 'http://', width: "99%")
                                hlayout(width: "100%") {
                                    label(value: 'http://', width: '40px', style: 'line-height:21px')
                                    textbox(id: 'urlTextbox', name: 'url', value: editContent?.url, hflex: 1)
                                }
                            }
                        }
                        row {
                            cell {
                                label(value: g.message(locale: locale, code: "baseContent.storedFile"))
                            }

                            cell {
                                label(value: editContent?.storedFile ? editContent?.storedFile?.originalFilename : '', id: 'filelabel', visible: editContent?.storedFile ? true : false)
                                //fileupload(native:true, maxsize: grailsApplication.config.file.upload.max.size, multiple: true, label: g.message(locale: locale, code: "storedFile.upload"), id: 'upload', visible: editContent?.storedFile ? false : true,onOpload:{
                                button(upload: "true,maxsize=${fileVaultService.getMaxUploadFileSize()},multiple=false,native", label: g.message(locale: locale, code: "storedFile.upload"), id: 'upload', visible: editContent?.storedFile ? false : true)
                            }
                        }
                        row(id: 'metaRow', spans: 2)


                    }
                }

                div(style: 'text-align:center') {
                    label(value: g.message(locale: locale, code: 'application.required'))
                }

                div(style: 'text-align:center') {
                    button(id: 'submit', label: editContent ? g.message(locale: locale, code: "default.button.update.label") : g.message(locale: locale, code: "default.button.create.label"), width: 100, height: 30)
                    button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                        self.close()
                    })


                }

                new MetaDiv(editContent, composer.baseContentKeys, true, g.message(code: 'metaRecord.content.list', locale: locale)).parent = this.select("#metaRow")[0]

                this.select("#upload")[0].addEventListener('onUpload') {event ->
                    //System.out.println("UPLOADED3");
                    UploadEvent uploadEvent = (UploadEvent) event



                    def media = uploadEvent.media
                    if (media)
                    {
                        storedFile = new StoredFile()
                        storedFile.originalFilename = media.name
                        storedFile.setBytes(media.binary ? media.inMemory() ? media.byteData : media.streamData.bytes : media.stringData.bytes)
                        storedFile.setContentLength(storedFile.bytes.length)
                        storedFile.setContentType(media.contentType)
                        storedFile.md5 = org.codehaus.groovy.grails.plugins.codecs.MD5Codec.encode(storedFile.bytes)

                        println 'poep'
                        println selectedObject

                        Bundle temp = editContent?.ownerBundle ? editContent?.ownerBundle : Bundle.get(selectedObject.id)

                        StoredFile aFile = StoredFile.findByMd5AndBundle(storedFile.md5, temp.id)

                        if (aFile)
                        {
                            Messagebox.show(g.message(locale: locale, code: "storedFile.MD5.unique.error", args: [aFile.originalFilename, storedFile.md5]), g.message(locale: locale, code: "storedFile.MD5.unique.error.title"), Messagebox.OK, Messagebox.ERROR, new org.zkoss.zk.ui.event.EventListener()
                            {
                                public void onEvent(Event evt) throws InterruptedException
                                {
                                    if (evt.getName().equals("onOK"))
                                    {
                                        //println 'exitsssssss'
                                    }
                                }
                            });
                            storedFile = null
                        }
                        else
                        {
                            this.select("#filelabel")[0].visible = true
                            this.select("#urlLabel")[0].visible = false
                            this.select("#urlTextbox")[0].visible = false
                            this.select("#upload")[0].visible = false
                            this.select("#filelabel")[0].value = media.name
                        }
                    }

                }


                this.select("#submit")[0].addEventListener('onClick') {event ->

                    /*ShiroUser temp=shiroService.getActiveShiroUser(true)
                   Account temp2=shiroService.account(true)*/

                    //shiroService.setX(shiroService.getActiveShiroUser(true))

                    Bundle bundle = Bundle.get(selectedObject.id)

                    BaseContent baseContent = editContent ? BaseContent.get(editContent.id) : new BaseContent()
                    baseContent.properties = self.params

                    if (!editContent)
                    {
                        baseContent.owner = shiroService.getActiveShiroUser(true).institution
                        baseContent.ownerBundle = bundle
                    }

                    baseContent.title = baseContent.title ? baseContent.title : ""
                    baseContent.description = baseContent.description ? baseContent.description : ""
                    baseContent.url = baseContent.url ? baseContent.url : ""

                    if (!baseContent.validate())
                        self.renderErrors(bean: baseContent)
                    else
                    {

                        if (!editContent)
                        {
                            baseContent.store()
                            /*BaseContent.withTransaction {
                                baseContent.store()
                                baseContent.ownerBundle.addContent(baseContent)
                            }
                            testService.createOwnerPermission(baseContent)
                            testService.createRolePermission(baseContent)*/
                        }
                        else
                        {
                            BaseContent.withTransaction {
                                baseContent.store()
                            }
                        }
                        this.select("#metaDiv")[0].save(baseContent, WorkSpace.read(selectedWorkSpace.id).baseContentMetaRecordKey)

                        if (storedFile)
                        {


                            storedFile.content = baseContent
                            storedFile.mySave()

                            baseContent.storedFile = storedFile
                            baseContent.store()

                        }
                        composer.updateComp()
                        //mytreeaap.setModel(mytreeaap.model)
                        //mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle)
                        self.close()
                        composer.showDetail(baseContent)
                        /*SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                        Events.postEvent(selectEventEvent);*/

                    }

                }

            }

        }
    }



    class AccountChooseWindow extends MyWindow
    {


        public AccountChooseWindow(Component parent, def userListbox, def model, Institution institution, ShiroRole role)
        {
            super(parent)

            this.title = g.message(locale: locale, code: "shiroUser.select.multi")

            //       this.setStyle('background-color:#00ff00;')

            this << {
                vbox(width: '100%'/*,style:'background-color:#00ff00;'*/) {
                    listbox(id: "accountListbox", mold: "paging", pageSize: 10, multiple: true, checkmark: true, itemRenderer: new AccountListItemRenderer(), model: model, onSelect: {
                        self.select("#submit")[0].disabled = false
                    }) {

                        listhead() {
                            listheader(label: g.message(locale: locale, code: "account.username")) {
                            }
                            listheader(label: g.message(locale: locale, code: "institution.self.multi"))
                        }

                    }
                    div(style: 'text-align:center') {
                        button(id: 'submit', disabled: true, label: g.message(locale: locale, code: "default.add"), width: 100, height: 30, onClick: {

                            Institution institution1 = Institution.read(institution.id)

                            Listbox accountListbox = self.select("#accountListbox")[0]

                            accountListbox.selectedItems.each {obj ->

                                ShiroUser shiroUser = null

                                BaseDomain.withTransaction {
                                    Account account = Account.get(obj.value.id)

                                    shiroUser = ShiroUser.findByInstitutionAndAccount(institution1, account)
                                    if (!shiroUser)
                                        shiroUser = new ShiroUser(institution: institution1, account: account).store()

                                    if (role)
                                        shiroUser.addToRoles(ShiroRole.read(role.id))

                                    institution1.group.addToUsers(shiroUser)

                                }
                                (userListbox.model as ListModelList).add(0, shiroUser)

                                this.close()
                                //TODO send mail?

                            }
                            userListbox.selectedIndex = 0
                        })
                        button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                            self.close()
                        })
                    }
                }

            }

        }
    }


    class BundleMoveCopyWindow extends MyWindow
    {


        Bundle bundle

        boolean move

        public BundleMoveCopyWindow(Component parent, Bundle bundle, boolean move)
        {
            super(parent)

            this.width = '350px'
            this.move = move

            this.bundle = bundle
            this.title = move ? g.message(locale: locale, code: "bundle.move") : g.message(locale: locale, code: "bundle.link")


            def list = gormService.getCollectionsBundleAdd()
            //System.out.println(list);

            this << {
                vbox(width: '100%') {
                    listbox(id: "collectionListbox", mold: "paging", pageSize: 8, height: 300, model: new ListModelList(list), onSelect: {/*, itemRenderer: new RoleObjectRenderer()*/ /*, model: new AccountListModel()*/
                        self.select("#submit")[0].disabled = false
                    }
                    ) {

                        listhead() {
                            listheader(label: g.message(locale: locale, code: "collection.list")) {
                            }
                            /*listheader(label: "Rechten")*/
                        }

                    }
                    hbox(style: 'text-align:center') {
                        button(id: 'submit', disabled: true, label: move ? g.message(locale: locale, code: "default.move") : g.message(locale: locale, code: "default.link"), width: 100, height: 30, onClick: {
                            Listbox collectionListbox = self.select("#collectionListbox")[0]




                            Bundle selectedBundle = Bundle.get(bundle.id)
                            Collection selectedCollection = Collection.get(collectionListbox.selectedItem.value.id)


                            BaseDomain.withTransaction {
                                this.move ? gormService.moveBundle(selectedBundle, selectedCollection) : gormService.linkBundle(selectedBundle, selectedCollection)
                            }

                            //mytreeaap.selectedItem=null
                            mytreeaap.setModel(new ExplorerTreeModel(selectedWorkSpace))
                            //mytreeaap.setModel(mytreeaap.model)

                            //println selectedCollection.bundle.size()


                            selectedCollection.refresh()
                            selectedBundle.refresh()

                            self.close()

                            composer.showDetail(selectedBundle)

                            /*mytreeaap.setModel(mytreeaap.model)
                            //mytreeaap.selectedItem = mytreeaap.renderItemByNode(selectedBundle.refresh())
                            //mytreeaap.selectedItem.open = true
                            self.close()

                            selectedCollection.refresh()
                            println 'B'

                            mytreeaap.setModel(mytreeaap.model)
                            mytreeaap.selectedItem = mytreeaap.renderItemByNode(selectedCollection)
                            self.close()

                            SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                            Events.postEvent(selectEventEvent);*/

                            //composer.showAndSelectDetail(selectedCollection)

                            /*SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                            Events.postEvent(selectEventEvent);*/

                        })
                        button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                            self.close()
                        })
                    }
                }

            }

        }
    }




    class ContentMoveCopyWindow extends MyWindow
    {


        BaseContent baseContent

        boolean move

        public ContentMoveCopyWindow(Component parent, BaseContent baseContent, boolean move)
        {
            super(parent)

            this.width = '350px'
            this.move = move

            this.baseContent = baseContent
            this.title = move ? g.message(locale: locale, code: "baseContent.move") : g.message(locale: locale, code: "baseContent.link")

            def list = gormService.getBundlesBaseContentAdd()
            //System.out.println(list);

            this << {
                vbox(width: '100%') {
                    listbox(id: "bundleListbox", mold: "paging", pageSize: 8, height: 300, model: new ListModelList(list), onSelect: {/*, itemRenderer: new RoleObjectRenderer()*/ /*, model: new AccountListModel()*/
                        self.select("#submit")[0].disabled = false
                    }
                    ) {

                        listhead() {
                            listheader(label: g.message(locale: locale, code: "bundle.list")) {
                            }
                            /*listheader(label: "Rechten")*/
                        }

                    }
                    hbox(style: 'text-align:center') {
                        button(id: 'submit', disabled: true, label: move ? g.message(locale: locale, code: "default.move") : g.message(locale: locale, code: "default.link"), width: 100, height: 30, onClick: {
                            Listbox bundleListbox = self.select("#bundleListbox")[0]

                            BaseContent selectedContent = BaseContent.get(baseContent.id)
                            Bundle selectedBundle = Bundle.get(bundleListbox.selectedItem.value.id)
                            this.move ? gormService.moveBaseContent(selectedContent, selectedBundle) : System.out.println("DISABLED CONTENT LINK");/*gormService.addBaseContentToBundle(selectedContent, selectedBundle)*/


                            mytreeaap.setModel(mytreeaap.model)
                            /*mytreeaap.selectedItem = mytreeaap.renderItemByNode(selectedBundle)
                            mytreeaap.selectedItem.open = true*/
                            self.close()

                            composer.showDetail(selectedContent)

                            /*SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                            Events.postEvent(selectEventEvent);*/

                        })
                        button(id: 'cancel', label: g.message(locale: locale, code: "default.cancel"), width: 100, height: 30, onClick: {
                            self.close()
                        })
                    }
                }

            }

        }
    }

    /*public class ExplorerTreeModel extends AbstractTreeModel<Object> {

            public ExplorerTreeModel() {
                super("Root");
            }
            public boolean isLeaf(Object node) {
                return getLevel((String)node) >= 2; //at most 4 levels
            }
            public Object getChild(Object parent, int index) {
                return parent + "." + index;
            }
            public int getChildCount(Object parent) {
                return isLeaf(parent) ? 0: 1000; //each node has 5 children
            }
            public int getIndexOfChild(Object parent, Object child) {
                String data = (String)child;
                int i = data.lastIndexOf('.');
                return Integer.parseInt(data.substring(i + 1));
            }
            private int getLevel(String data) {
                int level = 0
                for (int i = -1;; ++level)
                    if ((i = data.indexOf('.', i + 1)) < 0)
                        return level;
            }
        }*/



    class ExplorerTreeModel extends AbstractTreeModel
    {

        private HashMap ccMap = new HashMap()

        public ExplorerTreeModel(WorkSpace workSpace)
        {
            super(workSpace);
            //this.root = workSpace
        }

        public test()
        {
            (this.root as WorkSpace).refresh()
        }

        /*@Override
        public int getIndexOfChild(Object parent, Object child) {
            println parent +" "+child
            return 0
        }*/

        /*public Object getChild(Object parent, int index) {
                Object[] children = _cache.get(parent); //assume you have a cache for children of a given node
                if (children == null)
                    children = _cache.loadChildren(parent); //ask cache to load all children of a given node
                return children[index];
            }
*/

        public Object getChild(Object parent, int index)
        {

            Object result = null

            if (parent instanceof WorkSpace)
            {
                result = (parent as WorkSpace).collection?.toArray()[index]
            }
            else if (parent instanceof Collection)
            {
                result = (parent as Collection).bundle?.toArray()[index]
            }
            /*else if (parent instanceof Bundle)
            {
                result = (parent as Bundle).content?.toArray()[index]
            }*/

            return BaseRepository.read(result.id)

        }

        /*public Object getChild(Object parent, int index)
        {

            Object result = null

            if (parent instanceof WorkSpace)
            {
                result = parent.collection(index)
            }
            else if (parent instanceof Collection)
            {
                result = parent.bundle(index)
            }
        }*/

/*
        @Override
        public boolean isOpen(java.lang.Object obj)
        {
            println 'open : '+ obj.toString()+" "+super.isOpen(obj)



            return super.isOpen(obj)

        }
*/

        public int getChildCount(Object parent)
        {
            //println 'getChildCount: ' + parent
            if (ccMap.containsKey(parent))
                return ccMap.get(parent)
            else
            {
                int count

                if (parent instanceof WorkSpace)
                {
                    count = parent.countCollection()
                    ccMap.put(parent, count)
                }
                else if (parent instanceof Collection)
                {
                    count = parent.countBundle()
                    ccMap.put(parent, count)
                }
                else
                    count = 0

                return count

            }
            //println 'getChildCount: ' + parent
        }

        public boolean isLeaf(Object node)
        {
            node instanceof Bundle
            //return (getChildCount(node) == 0);
        }

    }
    class ExplorerTreeItemRenderer implements TreeitemRenderer
    {

        @Override
        public void render(Treeitem ti, Object object, int i) throws Exception
        {

            /*
                       BaseRepository baseRepository1 = object as BaseRepository

                                    Treerow tr = new Treerow();
                                    Treecell titleTreecell = new Treecell(baseRepository1.title);
                                    tr.setParent(ti);
                                    titleTreecell.setParent(tr);

                                    Treecell ownerTreecell = new Treecell(baseRepository1.owner.toString());
                                    tr.setParent(ti);
                                    ownerTreecell.setParent(tr);
            */

            Treerow tr = new Treerow();
            Treecell titleTreecell = new Treecell(object.title);


            tr.setParent(ti);
            titleTreecell.setParent(tr);

            Treecell statusTreecell = new Treecell();

            int count = -1

            ShiroUser user = shiroService.getActiveShiroUser(true)

            boolean isRead
            boolean isAdd
            boolean isWrite
            boolean isOwner

            //def time=System.currentTimeMillis()

            if (object instanceof Collection)
            {
                count = object.countBundle()

                if (object.createdBy == user)
                {
                    collectionRights.put(object.id, 0)
                    isOwner = true
                }
                else if (permissionService.collectionEdit(object))
                {
                    collectionRights.put(object.id, 0)
                    isWrite = true
                }
                else if (permissionService.collectionCreateBundle(object))
                {
                    collectionRights.put(object.id, 1)
                    isAdd = true
                }
                else if (permissionService.collectionRead(object))
                {
                    collectionRights.put(object.id, 2)
                    isRead = true
                }
                else
                    collectionRights.put(object.id, 3)

            }
            else if (object instanceof Bundle)
            {

                //count = object.countContent()


                if (object.createdBy == user)
                {
                    isOwner = true
                }
                else
                {
                    def role = collectionRights.get(object.ownerCollection.id)
                    if (role == 0)
                    {
                        isWrite = true
                    }
                    else if (role == 1)
                    {
                        isAdd = true
                    }
                    else if (role == 2)
                    {
                        isRead = true
                    }
                }


            }

            /*def time1=System.currentTimeMillis()-time

            time=System.currentTimeMillis()*/



            if (isOwner)
            {
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'rights_owner.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.owner.access')).parent = statusTreecell
            }
            else if (isWrite)
            {
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'rights_user.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.admin.access')).parent = statusTreecell
            }
            else if (isAdd)
            {
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'rights_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.add.access')).parent = statusTreecell
            }
            else if (isRead)
            {
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'rights_read.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.read.access')).parent = statusTreecell
            }
            else
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'rights_no.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.no.access')).parent = statusTreecell

            if (object.published)
                new Image(src: "${g.resource(dir: 'images/16x16', file: 'published.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: 'baseRepository.published')).parent = statusTreecell

            /*def time2=  System.currentTimeMillis()-time

            println time1+ " - "+time2*/

            Treecell countTreecell = new Treecell(object instanceof Collection ? count + "" : "");
            countTreecell.style = "text-align:right"
            //statusTreecell.style="text-align:center"
            countTreecell.setParent(tr);
            statusTreecell.setParent(tr);
            /*}
            else
            {
                Treecell statusTreecell = new Treecell();
                statusTreecell.setParent(tr);

            }*/

            //  ti.label='sdfdsfdsfsde'

            ti.value = object
            treeItems.add(i, ti)

        }

    }

    class MyWindow extends Window
    {

        protected self = this

        public MyWindow(Component parent)
        {
            super()
            //this.height = 600;
            this.width = '500px';
            this.parent = parent
            this.visible = true
            this.border = 'normal'
            this.closable = true
            this.action = 'show: slideDown;hide: slideUp'
        }

        public void close()
        {
            Event closeEvent = new Event("onClose", self, null);
            Events.postEvent(closeEvent);
        }


    }

    class UserAdminTab extends MyTab
    {

        private String enableOnCloseComponent

        public UserAdminTab(String title, String enableOnCloseComponent)
        {
            super(title)
            this.enableOnCloseComponent = enableOnCloseComponent

        }

        public void onClose()
        {
            super.onClose()
            headwindow.select("#${enableOnCloseComponent}")[0].disabled = false
            //west.open = true
            //west.visible = true
        }
    }

    class MyTab extends Tab
    {
        def self = this

        public MyTab(String title)
        {
            this(title, null)

        }

        public MyTab(String title, String _image)
        {
            super(title)
            this.closable = true
            this.image = _image ? _image : null

        }


    }

    class CollectionManageTab extends MyTab
    {

        public CollectionManageTab(String title, String _image)
        {
            super(title, _image)
        }

        public void close()
        {
            isManageCollectionTabOpen = false
            headwindow.select("#toolbarbuttonCollectionManage")[0].disabled = false
            super.close()
        }


    }

    class MyTabpanel extends Tabpanel
    {

        public MyWindow()
        {
/*super()
this.height = 300;
this.width = 500;
this.parent = parent
this.visible = true
this.width = 450
this.action = 'show: slideDown;hide: slideUp'*/
        }
    }

    class UserManageTabpanel extends Tabpanel
    {
        protected self = this

        public UserManageTabpanel(Component parent)
        {
            super()

            ListModelList model = new ListModelList(shiroService.ownUsers().sort {it.account.username})

            this << {
                toolbar {
                    toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'user_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "account.create"), onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer")[0], null, shiroService.getActiveShiroUser(true).institution, null).doModal()
                    })
                    toolbarbutton(id: 'toolbarbuttonAddUser', image: "${g.resource(dir: 'images/16x16', file: 'group_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "institution.add.user"), onClick: {
                        new AccountChooseWindow(headwindow, self.select("#listboxUserBeheer")[0], new ListModelList(shiroService.otherAccounts().sort {it.username}), shiroService.getActiveShiroUser(true).institution, null).doModal()
                    })
                    toolbarbutton(id: 'toolbarbuttonEditUser', image: "${g.resource(dir: 'images/16x16', file: 'user_edit.png', absolute: 'true')}", visible: false, tooltiptext: g.message(locale: locale, code: "account.edit"), onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer")[0], self.select("#listboxUserBeheer")[0].selectedItem.value, shiroService.getActiveShiroUser(true).institution, null).doModal()
                    })
                }
                div() {
                    listbox(id: "listboxUserBeheer", height: 380, mold: "paging", pageSize: 8, itemRenderer: new UserListItemRenderer(), model: model, onSelect: {
                        self.select("#toolbarbuttonEditUser")[0].visible = true

                    }) {
                        listhead() {
                            listheader(label: g.message(locale: locale, code: "account.username"))
                            listheader(label: g.message(locale: locale, code: "account.fullname"))
                            listheader(label: g.message(locale: locale, code: "account.activated"), width: '80px')
                            listheader(label: g.message(locale: locale, code: "shiroUser.active"), width: '80px')
                        }
                    }
                }
            }
        }


    }

    class UserIATabpanel extends Tabpanel
    {
        protected self = this




        public UserIATabpanel(Component parent)
        {
            super()

            //this.height='600px'
            //this.setStyle('background-color:#00ff00;')

            ListModelList model = new ListModelList(ShiroUser.insitutionAdministrators().sort {it.account.username})



            this << {
                toolbar {
                    /*toolbarbutton(image: "http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png", label: 'Create user', onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer")[0], null, true,null).doModal()
                    })*/
                    toolbarbutton(id: 'toolbarbuttonEditUser', image: "${g.resource(dir: 'images/16x16', file: 'edit.png', absolute: 'true')}", visible: false, tooltiptext: g.message(locale: locale, code: "shiroUser.edit"), onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer2")[0], self.select("#listboxUserBeheer2")[0].selectedItem.value, null, null).doModal()
                    })
                    /*toolbarbutton(id: 'toolbarbuttonAddUser', image: "http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png", label: 'Add existing user', onClick: {
                        new AccountChooseWindow(headwindow).doModal()
                    })*/
                }
                div() {
                    listbox(id: "listboxUserBeheer2", mold: "paging", pageSize: 10, itemRenderer: new UserListItemRenderer(), model: model, onSelect: {
                        self.select("#toolbarbuttonEditUser")[0].visible = true

                    }) {
                        listhead() {
                            listheader(label: g.message(locale: locale, code: "account.username"))
                            listheader(label: g.message(locale: locale, code: "account.fullname"))
                            listheader(label: g.message(locale: locale, code: "account.activated"), width: '80px')
                            listheader(label: g.message(locale: locale, code: "shiroUser.active"), width: '80px')
                            listheader(label: g.message(locale: locale, code: "shiroUser.institution"))
                        }
                    }
                }
            }
        }


    }




    class InstitutionManageTabpanel extends Tabpanel
    {
        protected self = this

        private Institution getSelectedInstitution()
        {
            /*println self.select("#listboxInstituutBeheer")[0]
            println self.select("#listboxInstituutBeheer")[0]?.selectedItem
            println self.select("#listboxInstituutBeheer")[0]?.selectedItem?.value
            println self.select("#listboxInstituutBeheer")[0]?.selectedItem?.value?.id*/
            return Institution.read(self.select("#listboxInstituutBeheer")[0]?.selectedItem?.value?.id)
        }

        public InstitutionManageTabpanel(Component parent)
        {
            super()


            ListModelList model = new ListModelList(permissionService.institutionManangeAdministratorsList().sort {it.name})



            this << {
                toolbar {
                    //println 'A'
                    if (shiroService.account(true).izSuperAdmin())
                    {
                        //println 'B'
                        toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "institution.create"), onClick: {
                            new InstitutionWindow(headwindow, self.select("#listboxInstituutBeheer")[0], null).doModal()
                        })
                        toolbarbutton(id: 'toolbarbuttonEditInstitution', image: "${g.resource(dir: 'images/16x16', file: 'edit.png', absolute: 'true')}", visible: false, tooltiptext: g.message(locale: locale, code: "institution.edit"), onClick: {
                            new InstitutionWindow(headwindow, self.select("#listboxInstituutBeheer")[0], self.select("#listboxInstituutBeheer")[0].selectedItem.value).doModal()
                        })
                    }
                    toolbarbutton(id: 'toolbarbuttonCreateUser', image: "${g.resource(dir: 'images/16x16', file: 'user_add.png', absolute: 'true')}", visible: false, tooltiptext: g.message(locale: locale, code: "institution.create.administrator"), onClick: {
                        new AccountWindow(headwindow, self.select("#listboxAdminUsers")[0], null, this.getSelectedInstitution(), this.getSelectedInstitution().administratorRole()).doModal()
                    })
                    toolbarbutton(id: 'toolbarbuttonAddAdminUser', image: "${g.resource(dir: 'images/16x16', file: 'group_add.png', absolute: 'true')}", visible: false, tooltiptext: g.message(locale: locale, code: "institution.add.administrators"), onClick: {
                        HashSet accounts = new HashSet()

                        this.selectedInstitution.administrators().each {user ->
                            accounts.add(user.account)

                        }

                        def x = Account.list() - accounts.toList()
                        new AccountChooseWindow(headwindow, self.select("#listboxAdminUsers")[0], new ListModelList(x), this.getSelectedInstitution(), this.getSelectedInstitution().administratorRole()).doModal()
                    })

                }

                /*hlayout(width: '100%' *//*,style:'background-color:#00ff00;'*//*) {
listbox(id: "listboxUsers", mold: "paging", pageSize: 10, hflex: 1, vflex: 1, itemRenderer: new RoleObjectRenderer(), model: new UserListModel(shiroService.ownUsers())) {*/

                hlayout(width: '100%' /*,style:'background-color:#00ff00;'*/) {
                    listbox(id: "listboxInstituutBeheer", mold: "paging", hflex: 1, height: '100%', pageSize: 10, itemRenderer: new InstitutionListItemRenderer(), model: model, onSelect: {
                        self.select("#toolbarbuttonEditInstitution")[0]?.visible = true
                        self.select("#toolbarbuttonAddAdminUser")[0].visible = true
                        self.select("#toolbarbuttonCreateUser")[0].visible = true

                        Institution institution = this.getSelectedInstitution()
                        ListModelList model2 = new ListModelList(institution.administratorsExSuper())
                        self.select("#listboxAdminUsers")[0].model = model2

                    }) {

                        auxhead() {
                            auxheader(label: g.message(locale: locale, code: "institution.list"), colspan: '4')
                        }

                        listhead()
                                {
                                    listheader(label: g.message(locale: locale, code: "institution.shortName"))
                                    listheader(label: g.message(locale: locale, code: "institution.websiteUrl"))
                                    listheader(label: g.message(locale: locale, code: "institution.name"))
                                    listheader(label: g.message(locale: locale, code: "default.status"), width: '50px')
                                    /*listheader(label: "Enabled")*/

                                }
                    }


                    listbox(id: "listboxAdminUsers", hflex: 1, height: '100%', mold: "paging", pageSize: 10, itemRenderer: new IAUserListItemRenderer(self), onSelect: {
                        //self.select("#toolbarbuttonEditInstitution")[0].visible = true

                    }) {
                        auxhead() {
                            auxheader(label: g.message(locale: locale, code: "institution.administrators.list"), colspan: '3')
                        }

                        listhead() {
                            listheader(label: g.message(locale: locale, code: "account.username"))
                            listheader(label: g.message(locale: locale, code: "account.fullname")/*,maxlength :'30'*/)
                            listheader(label: g.message(locale: locale, code: "default.actions"), width: '50px')
                        }
                    }

                }

                Listbox lb = self.select("#listboxInstituutBeheer")[0]
                if (lb.model.size != 0)
                {
                    lb.selectedIndex = 0
                    SelectEvent selectEventEvent = new SelectEvent("onSelect", lb, null);
                    Events.postEvent(selectEventEvent);
                }
            }

        }
    }

    class BundleTabpanel extends Tabpanel /*implements MyListener*/
    {
        protected self = this

        Bundle bundle

        GuiComposer composer

        private boolean hasReadPermission

        public BundleTabpanel(Component parent, Bundle bundle, GuiComposer composer)
        {
            super()
            this.bundle = bundle
            this.composer = composer

            this.hasReadPermission = permissionService.bundleRead(bundle)

            this << {

                toolbar {
                    if (!bundle.deleted)
                    {

                        if (hasReadPermission)
                        {
                            toolbarbutton(href: "/api/bundle/${bundle.id}", target: '_blank', image: "${g.resource(dir: 'images/16x16', file: 'xml.jpg', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'application.show.xml')}")
                        }

                        if (permissionService.bundleEdit(bundle))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'edit.png')}", tooltiptext: g.message(locale: locale, code: "bundle.edit"), onClick: {
                                new BundleWindow(headwindow, bundle).doModal()
                            })
                        }
                        if (permissionService.bundleDelete(bundle))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'delete.png')}", tooltiptext: g.message(locale: locale, code: "bundle.delete"), onClick: {
                                Messagebox.show(g.message(locale: locale, code: "default.button.delete.confirm.message"), g.message(locale: locale, code: "bundle.delete"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
                                {
                                    public void onEvent(Event evt) throws InterruptedException
                                    {
                                        if (evt.getName().equals("onOK"))
                                        {
                                            Bundle object = Bundle.get(self.bundle.id)

                                            Collection collection1 = Collection.read(object.ownerCollection.id)
                                            object.myDelete()

                                            //self.composer.updateComp()
                                            self.composer.mytreeaap.setModel(self.composer.mytreeaap.model)
                                            self.composer.showDetail(collection1)
                                            /*self.composer.mytreeaap.selectedItem = self.composer.mytreeaap.renderItemByNode(collection1)
                                            SelectEvent selectEventEvent = new SelectEvent("onSelect", self.composer.mytreeaap, null);
                                            Events.postEvent(selectEventEvent);*/

                                        }
                                    }
                                });

                            })
                        }

                        if (permissionService.bundleLink(bundle))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'link.png')}", tooltiptext: g.message(locale: locale, code: "bundle.link"), onClick: {
                                new BundleMoveCopyWindow(headwindow, bundle, false).doModal()
                            })
                        }


                        Collection parentCollection = Collection.read(mytreeaap?.selectedItem?.parentItem?.value?.id)

                        if (parentCollection && permissionService.bundleMove(bundle, parentCollection) && bundle.ownerCollection.id == parentCollection.id)
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'go.png')}", tooltiptext: g.message(locale: locale, code: "bundle.move"), onClick: {
                                new BundleMoveCopyWindow(headwindow, bundle, true).doModal()
                            })
                        }

                        if (permissionService.bundleCreateBaseContent(bundle))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'package_create.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.create"), onClick: {
                                new ContentWindow(headwindow, null).doModal()
                            })
                        }
                        if (permissionService.bundleCreateBaseContent(bundle))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'batch.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.create.batch"), onClick: {
                                new ContentBatchWindow(headwindow).doModal()
                            })
                        }


                        if (!mytreeaap.selectedItem || mytreeaap.selectedItem?.value != bundle)
                        {
                            toolbarbutton(id: 'toolbarbuttonShowInTree', image: "${g.resource(dir: 'images/16x16', file: 'tree.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseRepository.show.in.tree"), onClick: {
                                self.composer.showAndSelectDetail(bundle)

                                //self.composer.showInTree(bundle)

                                /*selectedObject = BaseRepository.read(bundle.id)
                              mytreeaap.selectedItem = mytreeaap.renderItemByNode(selectedObject)
                              this.select("#toolbarbuttonShowInTree")[0].visible = false*/
                            })
                        }

                        if (Bookmark.countByShiroUserAndBaseRepository(shiroService.getActiveShiroUser(true), bundle) == 0 && permissionService.bundleRead(bundle))
                        {
                            toolbarbutton(id: 'toolbarbuttonAddclipboard', image: "${g.resource(dir: 'images/16x16', file: 'favorites_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "bookmark.create"), onClick: {
                                this.select("#bookmarkpopup")[0].open(this.select("#toolbarbuttonAddclipboard")[0], 'after_start')
                            }, tooltip: 'bookmarkpopup,delay=0,position=after_start')
                        }


                        if (permissionService.bundlePublish(bundle))
                        {
                            toolbarbutton(id: 'toolbarbuttonPublish', visible: !bundle.published, image: "${g.resource(dir: 'images/16x16', file: 'publish_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseRepository.publish"), onClick: {
                                this.select("#toolbarbuttonPublish")[0].visible = false
                                this.select("#toolbarbuttonUnPublish")[0].visible = true
                                this.select("#published")[0].value = g.message(locale: locale, code: "default.boolean.true")

                                bundle.refresh()
                                gormService.bundleSetPublish(bundle, true)

                                mytreeaap.selectedItem = null
                                mytreeaap.setModel(new ExplorerTreeModel(selectedWorkSpace))

                                composer.showDetail(bundle)

                                /*mytreeaap.setModel(mytreeaap.model)*/
                                //mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle)
                                //    mytreeaap.selectedItem.open = true

                                /*SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                                Events.postEvent(selectEventEvent);*/

                            })
                            toolbarbutton(id: 'toolbarbuttonUnPublish', visible: bundle.published, image: "${g.resource(dir: 'images/16x16', file: 'publish_delete.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseRepository.unpublish"), onClick: {
                                this.select("#toolbarbuttonPublish")[0].visible = true
                                this.select("#toolbarbuttonUnPublish")[0].visible = false
                                this.select("#published")[0].value = g.message(locale: locale, code: "default.boolean.false")

                                bundle.refresh()
                                gormService.bundleSetPublish(bundle, false)

                                mytreeaap.selectedItem = null
                                mytreeaap.setModel(new ExplorerTreeModel(selectedWorkSpace))

                                composer.showDetail(bundle)

                                /*mytreeaap.setModel(mytreeaap.model)
                                mytreeaap.selectedItem = mytreeaap.renderItemByNode(bundle)

                                SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                                Events.postEvent(selectEventEvent);*/

                            })
                        }
                    }

                }

                popup(id: 'bookmarkpopup') {
                    vbox() {
                        label(value: g.message(locale: locale, code: "bookmark.description"))
                        textbox(name: 'desciption', id: 'desciption', width: "99%", rows: 4, maxlength: '1000')
                        button(label: g.message(locale: locale, code: "default.button.create.label"), width: "100px", onClick: {

                            Bookmark bookmark = new Bookmark(description: this.select("#desciption")[0].value, shiroUser: shiroService.getActiveShiroUser(true), baseRepository: Bundle.read(bundle.id))
                            bookmark.store()

                            (bundleBookmarkListbox.model as ListModelList).add(0, bookmark)
                            bundleBookmarkListbox.selectedIndex = 0
                            tabboxFavorites.selectedTab = tabBundle

                            this.select("#toolbarbuttonAddclipboard")[0].visible = false
                            this.select("#bookmarkpopup")[0].close()

                        })
                    }
                }

                /*borderlayout(height: "100%", width: "100%") {
              center(border: 0) {*/
                div {
                    grid {
                        columns {
                            column(width: '150px')
                            column()
                        }
                        rows() {
                            if (bundle.deleted)
                            {
                                row(spans: 2) {
                                    label(value: g.message(locale: locale, code: "bundle.deleted", args: [bundle.id]))
                                }
                            }
                            else
                            {
                                row() {
                                    label(value: g.message(locale: locale, code: "bundle.title"))
                                    label(id: 'title', value: bundle.title, width: '99%', style: 'font-weight:bold')
                                }

                                row() {
                                    label(value: g.message(locale: locale, code: "bundle.owner"))
                                    label(id: 'owner', value: bundle.owner, width: '99%')
                                }


                                if (hasReadPermission)
                                {
                                    row() {
                                        label(value: g.message(locale: locale, code: "bundle.description"))
                                        label(id: 'description', value: bundle.description, width: '99%')
                                    }
                                    /*row() {
                                        label(value: g.message(locale: locale, code: "bundle.UUID"))
                                        label(id: 'UUID', value: bundle.UUID, width: '99%')
                                    }*/
                                    row() {
                                        label(value: g.message(locale: locale, code: "bundle.createdBy"))
                                        label(id: 'createdBy', value: bundle.createdBy, width: '99%')
                                    }
                                    row() {
                                        label(value: g.message(locale: locale, code: "bundle.ownerCollection"))
                                        a(label: bundle.ownerCollection, tooltiptext: g.message(locale: locale, code: "collection.show"), onClick: {
                                            //composer.showAndSelectDetail(BaseRepository.read(bundle.ownerCollection.id))
                                            composer.showDetail(BaseRepository.read(bundle.ownerCollection.id))
                                        })
                                    }
                                    row() {
                                        label(value: g.message(locale: locale, code: "baseRepository.published"))
                                        label(id: 'published', value: bundle.published ? g.message(locale: locale, code: "default.boolean.true") : g.message(locale: locale, code: "default.boolean.false"), width: '99%')
                                    }
                                    row(id: 'metaRow', spans: 2) {
                                    }



                                    if (!bundle.collection.isEmpty())
                                    {
                                        def list = bundle.collection.sort {it.title}
                                        ListModelList model = new ListModelList(list)
                                        row(spans: 2) {
                                            listbox(id: "collectionListbox", mold: "paging", pageSize: 5, itemRenderer: new CollectionListItemRenderer(bundle)) {

                                                auxhead() {
                                                    auxheader(colspan: 4) {
                                                        toolbarbutton(id: "showToolbarbutton", label: "${g.message(locale: locale, code: 'collection.list')} (${bundle.countCollection()})", image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-closed.png'), onClick: {
                                                            this.select("#collectionListbox")[0].model = model
                                                            this.select("#showToolbarbutton")[0].visible = false
                                                            this.select("#hideToolbarbutton")[0].visible = true
                                                            this.select("#bundleListhead")[0].visible = true
                                                        })
                                                        toolbarbutton(id: "hideToolbarbutton", label: "${g.message(locale: locale, code: 'collection.list')} (${bundle.countCollection()})", visible: false, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-open.png'), onClick: {
                                                            model = this.select("#collectionListbox")[0].model
                                                            this.select("#collectionListbox")[0].model = new ListModelList()
                                                            this.select("#showToolbarbutton")[0].visible = true
                                                            this.select("#hideToolbarbutton")[0].visible = false
                                                            this.select("#bundleListhead")[0].visible = false
                                                        })
                                                    }
                                                }

                                                listhead(id: 'bundleListhead', visible: false) {
                                                    listheader(label: g.message(locale: locale, code: "collection.title"))
                                                    listheader(label: g.message(locale: locale, code: "collection.owner"))
                                                    listheader(label: g.message(locale: locale, code: "collection.createdBy"))
                                                    listheader(label: g.message(locale: locale, code: "default.actions"))
                                                }

                                            }
                                        }
                                    }
                                    if (!bundle.countContent() != 0)
                                    {
                                        boolean modelSet = false
                                        ListModelList model
                                        //ListModelList model = new ListModelList(bundle.content)
                                        //ListModelList model = null
                                        //ContentListModel model = new ContentListModel(bundle)
                                        row(spans: 2) {
                                            listbox(id: "contentListbox", mold: "paging", pageSize: 5, itemRenderer: new ContentListItemRenderer()) {
                                                auxhead() {
                                                    auxheader(colspan: 4) {
                                                        toolbarbutton(id: "contentShowToolbarbutton", label: "${g.message(locale: locale, code: 'baseContent.list')} (${bundle.countContent()})", image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-closed.png'), onClick: {
                                                            if (!modelSet)
                                                            {
                                                                /*bundle.refresh()
                                                                println bundle.content.size()
                                                                println BaseContent.countByOwnerBundle(bundle)*/
                                                                this.select("#contentListbox")[0].model = new ListModelList(BaseContent.findAllByOwnerBundle(bundle))
                                                                //this.select("#contentListbox")[0].model = new ContentListModel(bundle)
                                                                modelSet = true
                                                            }
                                                            else
                                                                this.select("#contentListbox")[0].model = model

                                                            this.select("#contentShowToolbarbutton")[0].visible = false
                                                            this.select("#contentHideToolbarbutton")[0].visible = true
                                                            this.select("#contentListhead")[0].visible = true


                                                        })
                                                        toolbarbutton(id: "contentHideToolbarbutton", label: "${g.message(locale: locale, code: 'baseContent.list')} (${bundle.countContent()})", visible: false, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-open.png'), onClick: {
                                                            model = this.select("#contentListbox")[0].model
                                                            this.select("#contentListbox")[0].model = new ListModelList()
                                                            this.select("#contentShowToolbarbutton")[0].visible = true
                                                            this.select("#contentHideToolbarbutton")[0].visible = false
                                                            this.select("#contentListhead")[0].visible = false


                                                        })
                                                        //label(value: g.message(locale: locale, code: 'bundle.list'))

                                                    }
                                                    //   auxheader(label: g.message(locale: locale, code: "baseContent.list"), colspan: 4)
                                                }

                                                listhead(id: 'contentListhead', visible: false) {
                                                    listheader(label: g.message(locale: locale, code: "baseContent.title"), sortDescending: new DemoComparator(true, 0), sortAscending: new DemoComparator(false, 0))
                                                    listheader(label: g.message(locale: locale, code: "baseContent.owner"))
                                                    listheader(label: g.message(locale: locale, code: "baseContent.createdBy"))
                                                    listheader(label: g.message(locale: locale, code: "default.actions"))
                                                }

                                            }
                                        }

                                    }


                                }
                            }
                        }
                    }
                }
                new MetaDiv(bundle, composer.bundleKeys, false, g.message(code: 'metaRecord.bundle.list', locale: locale)).parent = this.select("#metaRow")[0]
            }
        }


    }

    class CollectionTabpanel extends Tabpanel implements UpdateListerner
    {


        protected self = this

        Collection collection

        GuiComposer composer

        private boolean hasReadPermission

        public void update()
        {
            //System.out.println('CollectionTabpanel: update model');
        }

        public CollectionTabpanel(Component parent, GuiComposer composer, Collection collection)
        {
            super()

            composer.compSet.add(this)

            this.composer = composer
            this.collection = collection
            this.visible = true

            this.hasReadPermission = permissionService.collectionRead(collection)

            this << {
                toolbar {
                    if (!collection.deleted)
                    {
                        if (hasReadPermission)
                        {
                            toolbarbutton(href: "/api/collection/${collection.id}", target: '_blank', image: "${g.resource(dir: 'images/16x16', file: 'xml.jpg', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'application.show.xml')}")
                        }



                        if (permissionService.collectionEdit(collection))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'edit.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'collection.edit')}", onClick: {
                                new CollectionWindow(headwindow, collection).doModal()
                            })
                        }

                        if (permissionService.collectionDelete(collection))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'delete.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'collection.delete')}", onClick: {
                                Messagebox.show("Weet u zeker dat u deze collectie wilt verwijderen?", "Verwijder collectie", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
                                {
                                    public void onEvent(Event evt) throws InterruptedException
                                    {
                                        if (evt.getName().equals("onOK"))
                                        {
                                            Collection object = Collection.get(self.collection.id)

                                            object.myDelete()

                                            self.composer.updateComp()

                                            self.composer.selectedWorkSpace = WorkSpace.read(self.composer.selectedWorkSpace.id)
                                            self.composer.mytreeaap.setModel(new ExplorerTreeModel(self.composer.selectedWorkSpace))

                                            self.composer.showDetail(object)
                                        }
                                    }
                                });

                            })
                        }

                        if (permissionService.collectionCreateBundle(collection))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'package_create.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'bundle.create')}", onClick: {
                                new BundleWindow(headwindow, null).doModal()
                            })
                        }


                        if (!mytreeaap.selectedItem || mytreeaap.selectedItem.value != collection)
                        {
                            toolbarbutton(id: 'toolbarbuttonShowInTree', image: "${g.resource(dir: 'images/16x16', file: 'tree.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'baseRepository.show.in.tree')}", onClick: {
                                self.composer.showAndSelectDetail(collection)
                                //self.composer.showInTree(collection)
                            })
                        }

                        if (Bookmark.countByShiroUserAndBaseRepository(shiroService.getActiveShiroUser(true), collection) == 0 && permissionService.collectionRead(collection))
                        {
                            toolbarbutton(id: 'toolbarbuttonAddclipboard', image: "${g.resource(dir: 'images/16x16', file: 'favorites_add.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'bookmark.create')}", onClick: {
                                this.select("#bookmarkpopup")[0].open(this.select("#toolbarbuttonAddclipboard")[0], 'after_start')
                            }, tooltip: 'bookmarkpopup,delay=0,position=after_start')
                        }

                        if (permissionService.collectionPublish(collection))
                        {
                            toolbarbutton(id: 'toolbarbuttonPublish', visible: !collection.published, image: "${g.resource(dir: 'images/16x16', file: 'publish_add.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'baseRepository.publish')}", onClick: {
                                this.select("#toolbarbuttonPublish")[0].visible = false
                                this.select("#toolbarbuttonUnPublish")[0].visible = true
                                this.select("#published")[0].value = g.formatBoolean(boolean: true, locale: locale)
                                Collection collection1 = Collection.get(collection.id)
                                gormService.collectionSetPublish(collection1, true)

                                /*composer.showDetail(collection1)
                                composer.updateTree(collection1)*/

                                mytreeaap.setModel(mytreeaap.model)
                                mytreeaap.selectedItem = mytreeaap.renderItemByNode(collection1)
                                //mytreeaap.selectedItem.open = true

                                SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                                Events.postEvent(selectEventEvent);

                            })
                            toolbarbutton(id: 'toolbarbuttonUnPublish', visible: collection.published, image: "${g.resource(dir: 'images/16x16', file: 'publish_delete.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'baseRepository.unpublish')}", onClick: {
                                this.select("#toolbarbuttonPublish")[0].visible = true
                                this.select("#toolbarbuttonUnPublish")[0].visible = false
                                this.select("#published")[0].value = g.formatBoolean(boolean: false, locale: locale)
                                Collection collection1 = Collection.get(collection.id)
                                gormService.collectionSetPublish(collection1, false)

                                /*composer.showDetail(collection1)
                                composer.updateTree(collection1)*/

                                mytreeaap.setModel(mytreeaap.model)
                                mytreeaap.selectedItem = mytreeaap.renderItemByNode(collection1)
                                //mytreeaap.selectedItem.open = true

                                SelectEvent selectEventEvent = new SelectEvent("onSelect", mytreeaap, null);
                                Events.postEvent(selectEventEvent);

                            })

                        }
                        if (permissionService.manageUsers(collection))
                        {
                            space(bar: 'true')
                            toolbarbutton(id: 'toolbarbuttonCollectionManage', disabled: isManageCollectionTabOpen, image: "${g.resource(dir: 'images/16x16', file: 'group_key.png', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'collection.management')}", onClick: {
                                this.select("#toolbarbuttonCollectionManage")[0].disabled = true
                                composer.showUserRightsPanel(collection)
                            })
                        }

                    }

                    /*toolbarbutton(id: 'toolbarbuttonEditUser', image: "http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png", visible: false, label: 'Edit user', onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer")[0], self.select("#listboxUserBeheer")[0].selectedItem.value).doModal()
                    })*/
                }
                /*borderlayout(height: "100%", width: "100%") {
              center(border: 0) {*/

                popup(id: 'bookmarkpopup') {
                    vbox() {
                        label(value: "${g.message(locale: locale, code: 'bookmark.description')}:")
                        textbox(name: 'desciption', id: 'desciption', width: "99%", rows: 4, maxlength: '1000')
                        button(label: "${g.message(locale: locale, code: 'default.button.create.label')}", width: "100px", onClick: {

                            Bookmark bookmark = new Bookmark(description: this.select("#desciption")[0].value, shiroUser: shiroService.getActiveShiroUser(true), baseRepository: Collection.read(collection.id))
                            bookmark.store()

                            (collectionBookmarkListbox.model as ListModelList).add(0, bookmark)
                            collectionBookmarkListbox.selectedIndex = 0
                            tabboxFavorites.selectedTab = tabCollection

                            this.select("#toolbarbuttonAddclipboard")[0].visible = false
                            this.select("#bookmarkpopup")[0].close()

                        })
                    }
                }


                div() {
                    grid {
                        columns {
                            column(width: '150px')
                            column()
                        }
                        rows() {

                            if (collection.deleted)
                            {
                                row(spans: 2) {
                                    label(value: g.message(locale: locale, code: "collection.deleted", args: [collection.id]))
                                }
                            }
                            else
                            {
                                row() {
                                    label(value: "${g.message(locale: locale, code: 'collection.title')}")
                                    label(id: 'title', value: collection.title, width: '99%', style: 'font-weight:bold')
                                }

                                row() {
                                    label(value: "${g.message(locale: locale, code: 'collection.owner')}")
                                    label(id: 'owner', value: collection.owner, width: '99%')
                                }


                                if (hasReadPermission)
                                {
                                    row() {
                                        label(value: "${g.message(locale: locale, code: 'collection.description')}")
                                        label(id: 'description', value: collection.description, width: '99%')
                                    }

                                    /*row() {
                                        label(value: "${g.message(locale: locale, code: 'collection.UUID')}")
                                        label(id: 'UUID', value: collection.UUID, width: '99%')
                                    }*/

                                    row() {
                                        label(value: "${g.message(locale: locale, code: 'collection.createdBy')}")
                                        label(id: 'createdBy', value: collection.createdBy, width: '99%')
                                    }
                                    row() {
                                        label(value: "${g.message(locale: locale, code: 'collection.published')}")
                                        label(id: 'published', value: collection.published ? g.formatBoolean(boolean: true, locale: locale) : g.formatBoolean(boolean: false, locale: locale), width: '99%')
                                    }

                                    row(id: 'metaRow', spans: 2) {
                                    }
                                    if (collection.countBundle() != 0)
                                    {
                                        // def list = collection.bundle.sort {it.title}

                                        //ListModelList model = new ListModelList(list)
                                        BundleListModel model = new BundleListModel(collection)

                                        /*row(spans: 2) {
                                            auxhead() {
                                                auxheader() {
                                                    button(label: '+')
                                                }

                                                auxheader(label: "${g.message(locale: locale, code: 'bundle.list')}", colspan: 3)
                                            }
                                        }*/

                                        /*row(spans: 2, visible: true) {
                                            listbox(id: "bundleListbox", mold: "paging", pageSize: 5, model: model, itemRenderer: new BundleListItemRenderer(collection), onSelect: {
                                                //self.select("#submit")[0].disabled = false
                                            }) {
                                                auxhead() {
                                                    auxheader() {
                                                        button(label: '-')
                                                    }

                                                    auxheader(label: "${g.message(locale: locale, code: 'bundle.list')}", colspan: 3)
                                                }



                                                listhead() {
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.title')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.owner')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.createdBy')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'default.actions')}")
                                                }

                                            }
                                        }*/
                                        row(spans: 2, visible: true) {
                                            listbox(id: "bundleListbox", mold: "paging", pageSize: 5, itemRenderer: new BundleListItemRenderer(collection), onSelect: {
                                                //self.select("#submit")[0].disabled = false
                                            }) {

                                                auxhead() {
                                                    auxheader(colspan: 4) {
                                                        toolbarbutton(id: "showToolbarbutton", label: "${g.message(locale: locale, code: 'bundle.list')} (${collection.countBundle()})", image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-closed.png'), onClick: {
                                                            this.select("#bundleListbox")[0].model = model
                                                            this.select("#showToolbarbutton")[0].visible = false
                                                            this.select("#hideToolbarbutton")[0].visible = true
                                                            this.select("#bundleListhead")[0].visible = true


                                                        })
                                                        toolbarbutton(id: "hideToolbarbutton", label: "${g.message(locale: locale, code: 'bundle.list')} (${collection.countBundle()})", visible: false, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-open.png'), onClick: {
                                                            model = this.select("#bundleListbox")[0].model
                                                            this.select("#bundleListbox")[0].model = new ListModelList()
                                                            this.select("#showToolbarbutton")[0].visible = true
                                                            this.select("#hideToolbarbutton")[0].visible = false
                                                            this.select("#bundleListhead")[0].visible = false


                                                        })
                                                        //label(value: g.message(locale: locale, code: 'bundle.list'))

                                                    }
                                                }

                                                listhead(id: 'bundleListhead', visible: false) {
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.title')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.owner')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'bundle.createdBy')}")
                                                    listheader(label: "${g.message(locale: locale, code: 'default.actions')}")
                                                }

                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
                new MetaDiv(collection, composer.collectionKeys, false, g.message(code: 'metaRecord.collection.list', locale: locale)).parent = this.select("#metaRow")[0]
                /*   }
                }*/
            }
        }


    }

    class ContentTabpanel extends Tabpanel
    {
        protected self = this

        BaseContent content

        GuiComposer composer

        private boolean hasReadPermission

        public ContentTabpanel(Component parent, BaseContent content, GuiComposer composer)
        {
            super()
            this.content = content
            //this.content.refresh()
            this.composer = composer

            this.hasReadPermission = permissionService.baseContentRead(content)

            this << {
                toolbar {

                    if (!content.deleted)
                    {

                        if (hasReadPermission)
                        {
                            toolbarbutton(href: "/api/baseContent/${content.id}", target: '_blank', image: "${g.resource(dir: 'images/16x16', file: 'xml.jpg', absolute: 'true')}", tooltiptext: "${g.message(locale: locale, code: 'application.show.xml')}")
                        }

                        if (permissionService.baseContentEdit(content))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'edit.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.edit"), onClick: {
                                new ContentWindow(headwindow, content).doModal()
                            })
                        }
                        if (permissionService.baseContentDelete(content))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'delete.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.delete"), onClick: {
                                Messagebox.show(g.message(locale: locale, code: "default.button.delete.confirm.message"), g.message(locale: locale, code: "baseContent.delete"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
                                {
                                    public void onEvent(Event evt) throws InterruptedException
                                    {
                                        if (evt.getName().equals("onOK"))
                                        {
                                            BaseContent object = BaseContent.get(self.content.id)
                                            Bundle b = Bundle.read(object.ownerBundle.id)



                                            object.myDelete()

                                            self.composer.updateComp()

                                            self.composer.showDetail(b)

                                            /*self.composer.mytreeaap.setModel(self.composer.mytreeaap.model)
                                            self.composer.mytreeaap.selectedItem = self.composer.mytreeaap.renderItemByNode(b)
                                            SelectEvent selectEventEvent = new SelectEvent("onSelect", self.composer.mytreeaap, null);
                                            Events.postEvent(selectEventEvent);*/

                                        }
                                    }
                                });

                            })
                        }

                        /*Bundle parentBundle
                  if (mytreeaap?.selectedItem?.value instanceof Bundle)
                      parentBundle = Bundle.read(mytreeaap?.selectedItem?.value?.id)*/

                        //if (parentBundle && permissionService.baseContentMove(content, parentBundle) && content.ownerBundle.id == parentBundle.id)
                        if (permissionService.baseContentMove(content))
                        {
                            toolbarbutton(image: "${g.resource(dir: 'images/16x16', file: 'move.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "baseContent.move"), onClick: {
                                new ContentMoveCopyWindow(headwindow, content, true).doModal()
                            })
                        }

                        if (Bookmark.countByShiroUserAndBaseRepository(shiroService.getActiveShiroUser(true), content) == 0 && permissionService.baseContentRead(content))
                        {
                            toolbarbutton(id: 'toolbarbuttonAddclipboard', image: "${g.resource(dir: 'images/16x16', file: 'favorites_add.png', absolute: 'true')}", tooltiptext: g.message(locale: locale, code: "bookmark.create"), onClick: {
                                this.select("#bookmarkpopup")[0].open(this.select("#toolbarbuttonAddclipboard")[0], 'after_start')
                            }, tooltip: 'bookmarkpopup,delay=0,position=after_start')
                        }


                    }
                }

                div {

                    popup(id: 'bookmarkpopup') {
                        vbox() {
                            label(value: g.message(locale: locale, code: "bookmark.description"))
                            textbox(name: 'desciption', id: 'desciption', width: "99%", rows: 4, maxlength: '1000')
                            button(label: g.message(locale: locale, code: "default.button.create.label"), width: "100px", onClick: {

                                Bookmark bookmark = new Bookmark(description: this.select("#desciption")[0].value, shiroUser: shiroService.getActiveShiroUser(true), baseRepository: BaseContent.read(content.id))
                                bookmark.store()

                                (contentBookmarkListbox.model as ListModelList).add(0, bookmark)
                                contentBookmarkListbox.selectedIndex = 0
                                tabboxFavorites.selectedTab = tabContent

                                this.select("#toolbarbuttonAddclipboard")[0].visible = false
                                this.select("#bookmarkpopup")[0].close()

                            })
                        }
                    }

                    grid {
                        columns {
                            column(width: '150px')
                            column()
                        }
                        rows() {
                            if (content.deleted)
                            {
                                row(spans: 2) {
                                    label(value: g.message(locale: locale, code: "baseContent.deleted", args: [content.id]))
                                    //label(id: 'title', value: content.title, width: '99%')
                                }
                            }
                            else
                            {
                                row() {
                                    label(value: g.message(locale: locale, code: "baseContent.id"))
                                    label(id: 'title', value: content.id, width: '99%')
                                }

                                row() {
                                    label(value: g.message(locale: locale, code: "baseContent.title"))
                                    label(id: 'title', value: content.title, width: '99%', style: 'font-weight:bold')
                                }
                                row() {
                                    label(value: g.message(locale: locale, code: "baseContent.owner"))
                                    label(id: 'owner', value: content.owner, width: '99%')
                                }

                                if (hasReadPermission)
                                {
                                    row() {
                                        label(value: g.message(locale: locale, code: "baseContent.createdBy"))
                                        label(id: 'createdBy', value: content.createdBy, width: '99%')
                                    }

                                    row() {
                                        label(value: g.message(locale: locale, code: "baseContent.ownerBundle"))
                                        a(label: content.ownerBundle, onClick: {
                                            //composer.showAndSelectDetail(BaseRepository.read(content.ownerBundle.id))
                                            composer.showDetail(BaseRepository.read(content.ownerBundle.id))
                                        })
                                    }

                                    row() {
                                        label(value: g.message(locale: locale, code: "baseContent.description"))
                                        label(id: 'description', value: content.description, width: '99%')
                                    }


                                    row() {
                                        label(value: g.message(locale: locale, code: "baseContent.url"))
                                        //label(id: 'url', value: content.url, width: '99%')
                                        //a(id: 'storedFileUrl', label: content.url, href: content.storedFile ? grailsApplication.config.grails.serverURL + "/storedFile/download/" + content.storedFile.id : "http://" + content.url, target: '_blank', width: '99%')
                                        a(id: 'storedFileUrl', label: content.url, href: content.storedFile ? content.url : "http://" + content.url, target: '_blank', width: '99%')
                                        //a(label: content.url,href:content.url,target:"_blank")
                                    }



                                    if (content.storedFile)
                                    {
                                        row() {
                                            label(value: g.message(locale: locale, code: "baseContent.storedFile"))
                                            a(id: 'storedFileUrl', label: content.storedFile.originalFilename, href: grailsApplication.config.grails.serverURL + "/storedFile/download/" + content.storedFile.id, width: '99%')
                                        }
                                        row() {
                                            label(value: g.message(locale: locale, code: "storedFile.size"))
                                            label(id: 'fileSize', value: "${Math.ceil(content?.storedFile?.contentLength / 1024).toLong()} kB", width: '99%')
                                        }


                                    }
                                    /*else
                                    {
                                        row() {
                                            label(value: g.message(locale: locale, code: "baseContent.url"))
                                            *//*label(id: 'url', value: content.url, width: '99%')*//*
                                            //a(id: 'storedFileUrl', label: content.url, href: content.storedFile ? grailsApplication.config.grails.serverURL + "/storedFile/download/" + content.storedFile.id : "http://" + content.url, target: '_blank', width: '99%')
                                            a(id: 'storedFileUrl', label: content.url, href: "http://" + content.url, target: '_blank', width: '99%')
                                            //a(label: content.url,href:content.url,target:"_blank")
                                        }
                                    }*/
                                    row(id: 'metaRow', spans: 2) {
                                    }

                                }
                            }

                        }
                    }
                }

                new MetaDiv(content, composer.baseContentKeys, false, g.message(code: 'metaRecord.content.list', locale: locale)).parent = this.select("#metaRow")[0]
            }
        }


    }


    class CollectionAccessTabpanel extends Tabpanel
    {
        protected self = this

        Collection collection



        public CollectionAccessTabpanel(Collection collection)
        {
            super()

            this.collection = collection



            this << {
                toolbar {
                    /*toolbarbutton(image: "http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png", label: 'Create user', onClick: {
                        showUserAccountPanel()
                    })*/
                    /*toolbarbutton(id: 'toolbarbuttonEditUser', image: "http://www.zkoss.org/zkdemo/widgets/menu/toolbar/img/java.png", visible: false, label: 'Edit user', onClick: {
                        new AccountWindow(headwindow, self.select("#listboxUserBeheer")[0], self.select("#listboxUserBeheer")[0].selectedItem.value).doModal()
                    })*/
                }
                hlayout(width: '100%' /*,style:'background-color:#00ff00;'*/) {
                    listbox(id: "listboxUsers", mold: "paging", pageSize: 10, hflex: 1, vflex: 1, itemRenderer: new RoleObjectRenderer(), model: new UserListModel(shiroService.ownUsers())) {

                        listhead() {
                            listheader(label: g.message(locale: locale, code: "shiroUser.list"), hflex: 3) {
                                radiogroup() {
                                    hlayout() {
                                        radio(id: 'radioGebruikers', label: g.message(locale: locale, code: "shiroUser.institution"), checked: true, onCheck: {
                                            this.select("#listboxUsers")[0].setModel(new UserListModel(shiroService.ownUsers()))
                                        })
                                        radio(id: 'radioOverig', label: g.message(locale: locale, code: "default.other"), onCheck: {
                                            this.select("#listboxUsers")[0].setModel(new UserListModel(shiroService.otherUsers()))
                                        })
                                    }

                                }
                            }
                            listheader(label: g.message(locale: locale, code: "shiroUser.institution"), hflex: 1)
                            listheader(label: g.message(locale: locale, code: "shiroUser.permissions"), width: '90px')
                        }

                    }


                    listbox(id: "listboxGroup", mold: "paging", pageSize: 10, hflex: 1, vflex: 1, itemRenderer: new RoleObjectRenderer(), model: new GroupRightsListModel()) {
                        listhead() {
                            listheader(label: g.message(locale: locale, code: "shiroUser.groups"))
                            listheader(label: g.message(locale: locale, code: "shiroUser.permissions"), width: '90px')
                        }
                    }
                }
            }

        }
    }

    class MetaDiv extends Div
    {
        BaseRepository baseRepository

        def self = this

        def metaKeys

        boolean editable

        ListModelList model

        //private boolean modelCreated

        public MetaDiv(BaseRepository baseRepository, List metaKeys, boolean editable, String label)
        {
            super()
            this.id = 'metaDiv'
            this.editable = editable
            this.metaKeys = metaKeys
            this.baseRepository = baseRepository

            //int size = baseRepository.countMetaRecord()

            this << {
                listbox(id: "listboxMetaRecord", mold: "paging", pageSize: 5, itemRenderer: new MetaRecordListItemRenderer(editable)) {

                    auxhead() {
                        auxheader(colspan: 2) {
                            toolbarbutton(id: "showMetaToolbarbutton", label: label, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-closed.png'), onClick: {
                                this.setOrCreateModel()
                                this.select("#showMetaToolbarbutton")[0].visible = false
                                this.select("#hideMetaToolbarbutton")[0].visible = true
                                this.select("#metaListhead")[0].visible = true

                            })
                            toolbarbutton(id: "hideMetaToolbarbutton", label: label, visible: false, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-open.png'), onClick: {
                                model = this.select("#listboxMetaRecord")[0].model
                                this.select("#listboxMetaRecord")[0].model = new ListModelList()
                                this.select("#showMetaToolbarbutton")[0].visible = true
                                this.select("#hideMetaToolbarbutton")[0].visible = false
                                this.select("#metaListhead")[0].visible = false


                            })

                        }

                    }

                    listhead(id: 'metaListhead', visible: false) {
                        listheader(label: g.message(locale: locale, code: "metaRecord.key"), width: '100px')
                        listheader(label: g.message(locale: locale, code: "metaRecord.value"))
                    }

                }
            }
        }

        private void setOrCreateModel()
        {
            if (!model)
            {
                ArrayList modelList = new ArrayList()
                if (baseRepository)
                {
                    baseRepository = BaseRepository.read(baseRepository.id)


                    baseRepository.metaRecord.each {metaRecord ->
                        MetaRecordListItem item = new MetaRecordListItem(metaRecord.key.toString(), metaRecord.value ? metaRecord.value : "")
                        modelList.add(item)
                        metaKeys.remove(metaRecord.key.label)
                    }
                }
                metaKeys.each {keyString ->
                    MetaRecordListItem item = new MetaRecordListItem(keyString, "")
                    modelList.add(item)
                }

                model = new ListModelList(modelList)

            }




            this.select("#listboxMetaRecord")[0].model = model

        }




        public def save(BaseRepository baseRepository1, def metaKeyz)
        {


            self.select("#listboxMetaRecord")[0].model.each { obj ->
                MetaRecordKey metaKey = MetaRecordKey.findByLabel(obj.key)
                MetaRecord record = MetaRecord.findByBaseRepositoryAndKey(baseRepository1, metaKey)

                if (record)
                {
                    record.value = obj.value
                    record.store()
                }
                else if (obj.value && obj.value != "")
                {
                    record = new MetaRecord(key: metaKey, value: obj.value, baseRepository: baseRepository1)
                    record.store()
                    //baseRepository1.addToMetaRecord(record)
                }


            }

            Iterator iter = baseRepository1.metaRecord.iterator()
            while (iter.hasNext())
            {
                MetaRecord metaRecord = iter.next()
                if (!metaRecord.value || metaRecord.value == '')
                {
                    metaRecord.delete()
                }
            }

        }
    }

/*
class MetaDiv extends Div
{
BaseRepository baseRepository

def self = this

boolean editable

public MetaDiv(BaseRepository baseRepository, def metaKeys, boolean editable)
{
super()
this.id = 'metaDiv'
this.editable = editable

ArrayList modelList = new ArrayList()

//create the model
metaKeys.each {key ->
MetaRecord record = MetaRecord.findByBaseRepositoryAndKey(baseRepository, key)
MetaRecordListItem item = new MetaRecordListItem(key.toString(), record ? record.value : "")
modelList.add(item)
}

ListModelList model = new ListModelList(modelList.sort {it.key})


this << {
listbox(id: "listboxMetaRecord", mold: "paging", pageSize: 5, itemRenderer: new MetaRecordListItemRenderer(editable)) {

auxhead() {
auxheader(colspan: 2) {
toolbarbutton(id: "showMetaToolbarbutton", label: "${g.message(locale: locale, code: 'metaRecord.list')} (${modelList.size()})", image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-closed.png'), onClick: {
    this.select("#listboxMetaRecord")[0].model = model
    this.select("#showMetaToolbarbutton")[0].visible = false
    this.select("#hideMetaToolbarbutton")[0].visible = true
    this.select("#metaListhead")[0].visible = true


})
toolbarbutton(id: "hideMetaToolbarbutton", label: "${g.message(locale: locale, code: 'metaRecord.list')} (${modelList.size()})", visible: false, image: g.resource(dir: 'images/16x16', file: 'arrow-toggle-open.png'), onClick: {
    model = this.select("#listboxMetaRecord")[0].model
    this.select("#listboxMetaRecord")[0].model = new ListModelList()
    this.select("#showMetaToolbarbutton")[0].visible = true
    this.select("#hideMetaToolbarbutton")[0].visible = false
    this.select("#metaListhead")[0].visible = false


})
*/
/* label(value: g.message(locale: locale, code: 'metaRecord.list'))*//*


                        }

                        */
/*auxheader(label: g.message(locale: locale, code: "metaRecord.list"), colspan: 2)*//*

                    }

                    listhead(id: 'metaListhead', visible: false) {
                        listheader(label: g.message(locale: locale, code: "metaRecord.key"), width: '100px')
                        listheader(label: g.message(locale: locale, code: "metaRecord.value"))
                    }

                }
            }
        }

        public def save(BaseRepository baseRepository1, def metaKeyz)
        {

            self.select("#listboxMetaRecord")[0].model.each { obj ->

                MetaRecordKey metaKey = MetaRecordKey.findByLabel(obj.key)
                MetaRecord record = MetaRecord.findByBaseRepositoryAndKey(baseRepository1, metaKey)
                if (record)
                {
                    record.value = obj.value
                    record.store()
                }
                else if (obj.value && obj.value != "")
                {
                    record = new MetaRecord(key: metaKey, value: obj.value)
                    record.store()
                    baseRepository1.addToMetaRecord(record)
                }


            }

            Iterator iter = baseRepository1.metaRecord.iterator()
            while (iter.hasNext())
            {
                MetaRecord metaRecord = iter.next()
                println metaRecord.value
                if (!metaRecord.value || metaRecord.value == '')
                {
                    metaRecord.delete()
                }
            }

        }
    }
*/

    class ContentListModel extends AbstractListModel implements Sortable
    {

        private Bundle bundle


        public String getSortDirection(Comparator cmpr)
        {
            return 'asc'

        }

        public void sort(Comparator cmpr, boolean ascending)
        {
            println cmpr
            println ascending

        }


        private int size = -1

        public ContentListModel(Bundle bundle)
        {
            this.bundle = bundle

        }

        public Object getElementAt(int index)
        {
            return bundle.content(index)
        }

        public int getSize()
        {
            if (size == -1)
                size = bundle.countContent()
            return size
        }

    }

    class BundleListModel extends AbstractListModel
    {

        private Collection collection

        private int size = -1

        public BundleListModel(Collection collection)
        {
            this.collection = collection

        }

        public Object getElementAt(int index)
        {
            return collection.bundle(index)
        }

        public int getSize()
        {
            if (size == -1)
                size = collection.countBundle()
            return size
        }

    }


    class UserListModel extends AbstractListModel
    {

        private ShiroUser[] shiroUsers

        public UserListModel(def shiroUsers)
        {
            this.shiroUsers = shiroUsers.sort {it.account.username}.toArray()
            //this.shiroUsers = users.toArray()
        }

        public Object getElementAt(int index)
        {
            return ShiroUser.read(shiroUsers[index].id)
        }

        public int getSize()
        {
            shiroUsers.size()
        }

    }



    class AccountListModel extends AbstractListModel
    {

        private Account[] accounts

        public AccountListModel(def accounts)
        {
            this.accounts = accounts.sort {it.username}.toArray()
            //this.shiroUsers = users.toArray()
        }

        public Object getElementAt(int index)
        {
            return Account.read(accounts[index].id)
        }

        public int getSize()
        {
            accounts.size()
        }

    }



    class GroupRightsListModel extends AbstractListModel
    {

        public Object getElementAt(int index)
        {
            ShiroGroup.read(ShiroGroup.list().get(index).id)
        }

        public int getSize()
        {
            ShiroGroup.list().size()
        }

    }

    class MetaRecordListItem
    {

        String key
        String value

        MetaRecordListItem(String key, String value)
        {
            this.key = key
            this.value = value
        }


    }

    class BookmarkListbox extends Listbox implements UpdateListerner
    {

        public BookmarkListbox(ListModelList model)
        {

            composer.compSet.add(this)

            this.setStyle('border:0')
            this.model = model
            this.itemRenderer = new BookmarkListItemRenderer()
            this.addEventListener('onClick') {event ->
                composer.showDetail(BaseRepository.read(this.selectedItem.value.baseRepository.id))
            }

            this << {
                listhead() {
                    listheader()
                    listheader(width: '30px')

                }
            }
        }

        public void update()
        {
            ListModelList newListModelList = new ListModelList()
            (this.model as ListModelList).each {obj ->
                Bookmark bookmark = Bookmark.read(obj.id)
                if (bookmark)
                    newListModelList.add(bookmark)
            }
            this.model = newListModelList
        }

    }

    public interface UpdateListerner
    {

        void update() throws java.lang.Exception

    }

    class DemoComparator implements Comparator<Object>, Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -2127053833562854322L;

        private boolean asc = true;
        private int type = 0;

        public DemoComparator(boolean asc, int type)
        {
            this.asc = asc;
            this.type = type;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        @Override
        public int compare(Object o1, Object o2)
        {
            BaseRepository contributor1 = (BaseRepository) o1;
            BaseRepository contributor2 = (BaseRepository) o2;

            contributor1.title.compareTo(contributor2.title) * (asc ? 1 : -1)

/*
            switch (type) {
            case 1: // Compare Title
                return contributor1.getTitle().compareTo(contributor2.getTitle()) * (asc ? 1 : -1);
            case 2: // Compare First Name
                return contributor1.getFirstName().compareTo(contributor2.getFirstName()) * (asc ? 1 : -1);
            case 3: // Compare Last Name
                return contributor1.getLastName().compareTo(contributor2.getLastName()) * (asc ? 1 : -1);
            case 4: // Compare Extension
                return contributor1.getExtension().compareTo(contributor2.getExtension()) * (asc ? 1 : -1);
            default: // Full Name
                return contributor1.getFullName().compareTo(contributor2.getFullName()) * (asc ? 1 : -1);
            }
*/

        }

    }


}


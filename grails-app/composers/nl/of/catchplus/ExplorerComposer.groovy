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
import grails.plugin.searchable.SearchableService
import grails.util.GrailsNameUtils
import org.apache.commons.lang.RandomStringUtils
import org.springframework.context.MessageSource

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.zk.ui.util.Clients
import grails.util.GrailsUtil
//import org.springframework.web.servlet.support.RequestContextUtils as RCU

class ExplorerComposer
{

    def blaService

    def grailsApplication
    GormService gormService
    PermissionService permissionService




    Tree mytreeaap


    BaseDomain selectedObject

    WorkSpace selectedWorkSpace


    def composer = this

    //def g


    def afterCompose = {Component comp ->

        //g = CatchplusUtils.getApplicationTagLib()


        mytreeaap.treeitemRenderer = new ExplorerTreeItemRenderer()

        def aIter= WorkSpace.list()?.iterator()

        if(aIter.hasNext())
        {
            selectedWorkSpace = aIter.next()
            mytreeaap.model = new ExplorerTreeModel(selectedWorkSpace)
        }

        mytreeaap.pagingChild.mold='os'


    }

    /*def onSelect_mytreeaap(SelectEvent event)
    {
        BaseRepository baseRepository = BaseRepository.get(mytreeaap.selectedItem.value.id)
        showDetail(baseRepository)
    }*/


}

class ExplorerTreeItemRenderer implements TreeitemRenderer
{

    @Override
    public void render(Treeitem ti, Object obj,int i) throws Exception
    {
        BaseRepository object = obj as BaseRepository

        Treerow tr = new Treerow();
        Treecell titleTreecell = new Treecell(object.title);
        tr.setParent(ti);
        titleTreecell.setParent(tr);

        Treecell ownerTreecell = new Treecell(object.owner.toString());
        tr.setParent(ti);
        ownerTreecell.setParent(tr);

        //println 'render '+object

/*
if (!(object instanceof BaseContent))
{
Treecell statusTreecell = new Treecell();




*/
/*System.out.println(object.createdBy == shiroService.getActiveShiroUser(true));
            System.out.println(object.createdBy);
            System.out.println(shiroService.getActiveShiroUser(true));*//*




            */
/*if (isOwner)
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
*//*

            statusTreecell.setParent(tr);
        }
*/






        ti.value = obj

    }

}
class ExplorerTreeModel extends AbstractTreeModel
{
//private WorkSpace root = null;
    public ExplorerTreeModel(WorkSpace workSpace)
    {
        super(workSpace);
        //this.root = workSpace
    }

    public test()
    {

        (this.root as WorkSpace).refresh()
    }

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

/*
    public boolean isOpen(java.lang.Object obj)
    {
        println 'open : '+ obj.toString()+" "+super.isOpen(obj)



        return super.isOpen(obj)

    }
*/

    public int getChildCount(Object parent)
    {
        int count = 0;

        if (parent instanceof WorkSpace)
        {
            count = parent.collection ? parent.collection.size() : 0
        }
        else if (parent instanceof Collection)
        {
            count = parent.bundle ? parent.bundle.size() : 0
        }
        /*else if (parent instanceof Bundle)
        {
            count = parent.content ? parent.content.size() : 0
        }*/

        return count

    }

    public boolean isLeaf(Object node)
    {
        return (getChildCount(node) == 0);
    }

}

package nl.of.catchplus

import org.apache.shiro.SecurityUtils

class PermissionService
{

    ShiroService shiroService



    static transactional = true

    /*public ShiroUser user(boolean readOnly) {
        readOnly ? ShiroUser.read(SecurityUtils.getSubject()?.principal?.id) : ShiroUser.get(SecurityUtils.getSubject()?.principal?.id)
    }*/

    public boolean manageUsers(Collection collection)
    {
        /*println 'B2'
        println collection.owner.superAdminRole().toString()
        println collection.superAdminRole().toString()*/
        //  println SecurityUtils.subject.hasRole(collection.owner.superAdminRole().toString()) || SecurityUtils.subject.hasRole(collection.superAdminRole().toString())
        /*SecurityUtils.subject.hasRole(collection.superAdminRole().toString()) ||*/ SecurityUtils.subject.hasRole(collection.owner.administratorRole().toString()) || SecurityUtils.subject.hasRole(collection.administratorRole().toString())
    }

    public boolean instituutAdminManage()
    {
        SecurityUtils.subject.isPermitted("instituut:manageadministrator")
    }

    public boolean baseRepositoryRead(BaseRepository baseRepository)
    {
        boolean result = false

        if (baseRepository instanceof Collection)
        {
            result = this.collectionRead(baseRepository)
        }
        else if (baseRepository instanceof Bundle)
        {
            result = this.bundleRead(baseRepository)
        }
        else if (baseRepository instanceof BaseContent)
        {
            result = this.baseContentRead(baseRepository)
        }

        return result

    }

    public boolean baseRepositoryEdit(BaseRepository baseRepository)
    {
        boolean result = false

        if (baseRepository instanceof Collection)
        {
            result = this.collectionEdit(baseRepository)
        }
        else if (baseRepository instanceof Bundle)
        {
            result = this.bundleEdit(baseRepository)
        }
        else if (baseRepository instanceof BaseContent)
        {
            result = this.baseContentEdit(baseRepository)
        }

        return result

    }

    public boolean baseRepositoryDelete(BaseRepository baseRepository)
    {
        boolean result = false

        if (baseRepository instanceof Collection)
        {
            result = this.collectionDelete(baseRepository)
        }
        else if (baseRepository instanceof Bundle)
        {
            result = this.bundleDelete(baseRepository)
        }
        else if (baseRepository instanceof BaseContent)
        {
            result = this.baseContentDelete(baseRepository)
        }

        return result

    }

    //Bundle

    public boolean bundlePublish(Bundle bundle)
    {

        def user = shiroService.getActiveShiroUser(true)

        return user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole())

        // SecurityUtils.subject.isPermitted("bundle:publish:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleRead(Bundle bundle)
    {
        def user = shiroService.getActiveShiroUser(true)

        return bundle.deleted || user == bundle.createdBy || user?.hasRole(bundle.ownerCollection.administratorRole()) || user?.hasRole(bundle.owner.administratorRole()) ||  user?.hasRole(bundle.ownerCollection.userAddRole()) || user?.hasRole(bundle.ownerCollection.userReadRole()) || user?.izSuperAdmin()

        // SecurityUtils.subject.isPermitted("bundle:show:${bundle.UUID}")
    }

    public boolean bundleLink(Bundle bundle)
    {

        def user = shiroService.getActiveShiroUser(true)

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole()) || user.hasRole(bundle.ownerCollection.userAddRole()) || user.hasRole(bundle.ownerCollection.userReadRole())

        //SecurityUtils.subject.isPermitted("bundle:show:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleAddBaseContent(Bundle bundle)
    {

        def user = shiroService.getActiveShiroUser(true)


        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole()) || user.hasRole(bundle.ownerCollection.userAddRole())

        //SecurityUtils.subject.isPermitted("bundle:addbasecontent:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleCreateBaseContent(Bundle bundle)
    {

        def user = shiroService.getActiveShiroUser(true)

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole()) || user.hasRole(bundle.ownerCollection.userAddRole())

        // SecurityUtils.subject.isPermitted("bundle:createbasecontent:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleMove(Bundle bundle, Collection collection)
    {

        def user = shiroService.getActiveShiroUser(true)

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole())

        //SecurityUtils.subject.isPermitted('bundle:move:' + bundle.UUID) && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleRemove(Bundle bundle, Collection collection)
    {
        //System.out.println("TODO:TEST  permissionservie.bundleRemove ");
        def user = shiroService.getActiveShiroUser(true)

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole())

        // SecurityUtils.subject.isPermitted("collection:removebundle:${bundle.UUID}") && SecurityUtils.subject.isPermitted("collection:removebundle:${collection.UUID}")  && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleDelete(Bundle bundle)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole())
        //SecurityUtils.subject.isPermitted("bundle:delete:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean bundleEdit(Bundle bundle)
    {
        def user = shiroService.getActiveShiroUser(true)

        //println bundle.ownerCollection.administratorRole()

        //println user.roles

        return user == bundle.createdBy || user.hasRole(bundle.ownerCollection.administratorRole()) || user.hasRole(bundle.owner.administratorRole())
        //SecurityUtils.subject.isPermitted("bundle:edit:${bundle.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }
    //Collection
    public boolean collectionRead(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

        return collection.deleted || user == collection.createdBy || user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole()) || user.hasRole(collection.userAddRole()) || user.hasRole(collection.userReadRole()) || user.izSuperAdmin()
        //SecurityUtils.subject.isPermitted("collection:show:${collection.UUID}")
    }

    public boolean collectionPublish(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole())
        //SecurityUtils.subject.isPermitted("collection:publish:${collection.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean collectionAddBundle(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user == collection.createdBy || user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole()) || user.hasRole(collection.userAddRole())
        //SecurityUtils.subject.isPermitted("collection:addbundle:${collection?.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }


    public boolean collectionCreateBundle(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user == collection.createdBy || user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole()) ||  user.hasRole(collection.userAddRole())
        //SecurityUtils.subject.isPermitted("collection:createbundle:${collection.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean collectionEdit(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

                return user == collection.createdBy || user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole())

        //SecurityUtils.subject.isPermitted("collection:edit:${collection.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    /*public boolean collectionRemove(Collection collection, WorkSpace workSpace)
    {
        def user = shiroService.getActiveShiroUser(true)

                return user == collection.createdBy || user.hasRole(collection.administratorRole())
        //SecurityUtils.subject.isPermitted("bundle:removebasecontent:${bundle.UUID}") && SecurityUtils.subject.isPermitted("bundle:removebasecontent:${content.UUID}")
    }*/

    public boolean collectionDelete(Collection collection)
    {
        def user = shiroService.getActiveShiroUser(true)

         return user == collection.createdBy || user.hasRole(collection.administratorRole()) || user.hasRole(collection.owner.administratorRole())
        //SecurityUtils.subject.isPermitted("collection:delete:${collection.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    /*public boolean collectionCreate(WorkSpace workSpace) {
        SecurityUtils.subject.isPermitted("instituut:manageadministrator")
    }*/

    public boolean workspaceAddCollection(WorkSpace workSpace)
    {
        //SecurityUtils.subject.isPermitted("instituut:manageadministrator")
        //       SecurityUtils.subject.isPermitted("workspace:createCollection:${workSpace.id}")
        def user = shiroService.getActiveShiroUser(true)

        return ShiroRole.institutionAdminRoles().find {shiroRole->
            return user.hasRole(shiroRole)
        } != null
        //SecurityUtils.subject.isPermitted("collection:create") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    //Content
    public boolean baseContentRead(BaseContent baseContent)
    {
        return baseContent.deleted || this.bundleRead(baseContent.ownerBundle)


        //return (shiroService.getActiveShiroUser(true) == baseContent.createdBy || this.bundleRead(baseContent.ownerBundle))
        //SecurityUtils.subject.isPermitted("baseContent:show:${baseContent.UUID}")
    }

    public boolean baseContentEdit(BaseContent content)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user==content.createdBy ||  this.bundleEdit(content.ownerBundle)
        //return (shiroService.getActiveShiroUser(true) == content.createdBy || this.collectionEdit(content.ownerBundle.ownerCollection)) && !shiroService.getActiveShiroUser(true).izSuperAdmin()
        //SecurityUtils.subject.isPermitted("basecontent:edit:${content.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    public boolean baseContentMove(BaseContent content)
    {
        def user = shiroService.getActiveShiroUser(true)

         return user==content.createdBy || this.bundleMove(content.ownerBundle,content.ownerBundle.ownerCollection)
        //((shiroService.getActiveShiroUser(true) == content.createdBy || this.collectionEdit(content.ownerBundle.ownerCollection)) && SecurityUtils.subject.isPermitted('bundle:addbaseContent:' + bundle.UUID)) && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    /*public boolean baseContentRemove(BaseContent content, Bundle bundle) {
        System.out.println('TODO:PermissionService  baseContentRemove');
        //SecurityUtils.subject.isPermitted("bundle:removebasecontent:${bundle.UUID}") && SecurityUtils.subject.isPermitted("bundle:removebasecontent:${content.UUID}")
        //System.out.println(SecurityUtils.subject.isPermitted("bundle:removebasecontent:${bundle.UUID}") )
        //System.out.println(SecurityUtils.subject.isPermitted("undle:removebasecontent:${content.UUID}"))
        true
    }*/

    public boolean baseContentDelete(BaseContent content)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user==content.createdBy || this.bundleDelete(content.ownerBundle)

       // (shiroService.getActiveShiroUser(true) == content.createdBy || this.collectionEdit(content.ownerBundle.ownerCollection)) && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }

    /*public boolean baseContentCopy(BaseContent content) {
        SecurityUtils.subject.isPermitted("basecontent:copy:${content.UUID}") && !shiroService.getActiveShiroUser(true).izSuperAdmin()
    }*/

    public boolean institutionManangeAdministrators(Institution institution)
    {
        def user = shiroService.getActiveShiroUser(true)

        return user.izSuperAdmin() || institution.administrators().contains(user)
    }

    public List institutionManangeAdministratorsList()
    {

        def result = new ArrayList()

        Institution.institutions().each {institution ->
            if (this.institutionManangeAdministrators(institution))
                result.add(institution)
        }
        return result
    }



    public List collectionListRead()
    {
        ArrayList result = new ArrayList()

        Collection.findAllWhere(deleted: false).each {obj ->
            if (this.collectionRead(obj))
                result.add(obj)
        }

        /*result.each {c->
            System.out.println(c.published);
        }*/

        return result
    }

    public List bundleListRead()
    {
        ArrayList result = new ArrayList()

        Bundle.findAllWhere(deleted: false).each {obj ->
            if (this.bundleRead(obj))
                result.add(obj)
        }
        return result
    }

    public List baseContentListRead()
    {
        ArrayList result = new ArrayList()

        BaseContent.findAllWhere(deleted: false).each {obj ->
            if (this.baseContentRead(obj))
                result.add(obj)
        }
        return result
    }

    public List metaRecordListRead()
    {
        ArrayList result = new ArrayList()

        MetaRecord.list().each {obj ->
            if (this.baseRepositoryRead(obj.baseRepository))
                result.add(obj)
        }
        return result
    }


}

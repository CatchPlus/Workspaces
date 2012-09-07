package nl.of.catchplus

import org.apache.shiro.SecurityUtils
import grails.util.GrailsNameUtils

class GormService
{

    static transactional = true

    def permissionService

    ShiroService shiroService

    TestService testService

    /*public void logDelete(BaseDomain baseDomain,LogType logType)
    {
        log.info("${logType} - DELETE  - ${GrailsNameUtils.getShortName(baseDomain.class).toString()} : ${baseDomain.id} - User : ${shiroService.getActiveShiroUser(true).id}")
    }

    public void logCreate(BaseDomain baseDomain,LogType logType)
    {
        log.info("${logType} - CREATE  - ${GrailsNameUtils.getShortName(baseDomain.class).toString()} : ${baseDomain.id} - User : ${shiroService.getActiveShiroUser(true).id}")
    }

    public void logUpdate(BaseDomain baseDomain,LogType logType)
    {
        log.info("${logType} - UPDATE  - ${GrailsNameUtils.getShortName(baseDomain.class).toString()} : ${baseDomain.id} - User : ${shiroService.getActiveShiroUser(true).id}")
    }*/

    /*public void logDelete(BaseDomain baseDomain,LogType logType)
    {
        log.info("${logType} -    - ${GrailsNameUtils.getShortName(baseDomain.class).toString()} : ${baseDomain.id} - User : ${shiroService.getActiveShiroUser(true).id}")
    }*/

    public void createRolePermission(BaseRepository baseRepository)
    {

        //System.out.println("OC2:" +baseRepository.ownerCollection);

        def institutionAdminRole = baseRepository.owner.administratorRole()
        def collectionAdminRole = baseRepository.administratorRole()
        def userAddRole = baseRepository.userAddRole()
        def userReadRole = baseRepository.userReadRole()
        String permission = GrailsNameUtils.getShortName(baseRepository.class).toString() + ":*:" + baseRepository.UUID




        ShiroPermission pShow = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":show:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pShow)
        collectionAdminRole.addToPermissions(pShow)
        userAddRole.addToPermissions(pShow)
        userReadRole.addToPermissions(pShow)

        ShiroPermission pPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":pin:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pPin)
        collectionAdminRole.addToPermissions(pPin)
        userAddRole.addToPermissions(pPin)
        userReadRole.addToPermissions(pPin)

        ShiroPermission pUnPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":unpin:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pUnPin)
        collectionAdminRole.addToPermissions(pUnPin)
        userAddRole.addToPermissions(pUnPin)
        userReadRole.addToPermissions(pUnPin)

        /*ShiroPermission pPublish = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":publish:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pPublish)
        collectionAdminRole.addToPermissions(pPublish)*/

        ShiroPermission pAll = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":*:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pAll)
        collectionAdminRole.addToPermissions(pAll)

        baseRepository.createdBy.addToPermissions(this.findOrCreateShiroPermission(permission))

    }

    def createContent(String title, String description, Bundle ownerBundle)
    {

        ShiroUser user = shiroService.getActiveShiroUser(true)

        BaseContent content = new BaseContent(title: title, description: description, ownerBundle: ownerBundle, owner: user.institution).save()
        ownerBundle.addToContent(content)


    }


    def setupCollection(Collection collection)
    {
        // System.out.println("START : setupCollection");
        ShiroRole collectionAdminRole = testService.createCollectionAdminRole(collection)
        //TODOShiroUser collectionAdminUser = shiroService.user(false) //new ShiroUser(institution: institution, firstName: "voornaam; setAdminUser", lastName: "achternaam; setAdminUser", username: "${collection.title}@admin.nl", password: new Sha256Hash("qwerty").toHex())
        //collectionAdminUser.verboseSave()

        //collectionAdminUser.addToRoles(collectionAdminRole)
        //institutionGroup.addToUsers(collectionAdminUser)

        ShiroRole userAddRole = testService.createCollectionUserAddRole(collection)
        ShiroRole userReadRole = testService.createCollectionUserReadRole(collection)


        collection.owner.group.addToRoles(userReadRole)

        //testService.createAddBundlePermission(collection)
//        testService.createOwnerPermission(collection)
        //testService.createRolePermission(collection)
        //System.out.println("END : setupCollection");

    }

    def createSaveStoredFile(StoredFile storedFile)
    {
        storedFile.save()
        /*testService.createOwnerPermission(storedFile)
        testService.createStoredFileRolePermission(storedFile)*/
    }

/*
    def saveObject(def aObject) {
        aObject.save(flush: true)

    }
*/

    def addUserToRole(ShiroUser user, ShiroRole role)
    {
        user = ShiroUser.get(user.id)
        user.addToRoles(role)
    }

    def removeUserToRole(ShiroUser user, ShiroRole role)
    {
        user = ShiroUser.get(user.id)
        user.removeFromRoles(role)
    }


    def addCollectionToWorkSpace(Collection collection, WorkSpace workSpace)
    {
        ShiroPermission shiroPermission = this.findOrCreateShiroPermission("workspace:removeCollection:" + collection.id)

        ShiroRole adminRole = collection.owner.administratorRole()// ShiroRole.findByName("Administrator : " + collection.owner.name)
        ShiroUser user = shiroService.getActiveShiroUser(true)//ShiroUser.get(SecurityUtils.subject.principal.id)

        workSpace.addCollection(collection)

        //SecurityUtils.getSubject().isPermitted("institution:confirm:" + collection.owner.id) ? collection.addToBundle(bundle) : collection.addToUnconfirmedBundle(bundle)

        adminRole.addToPermissions(shiroPermission)
        user.addToPermissions(shiroPermission)
    }

    def linkBundle(Bundle bundle, Collection collection)
    {

        /*ShiroPermission shiroPermission = testService.findOrCreateShiroPermission("collection:removebundle:" + bundle.UUID)

        def role1 = collection.owner.administratorRole()
        def role2 = collection.administratorRole()

        role1.addToPermissions(shiroPermission)
        role2.addToPermissions(shiroPermission)*/

        collection.addToBundle(bundle)

    }

    /*def createBundle(Bundle bundle, Collection collection) {

        *//*ShiroPermission shiroPermission = this.findOrCreateShiroPermission("collection:removeBundle:" + bundle.id)
        def role1=collection.owner.superAdminRole()
        def role2=collection.superAdminRole()

        role1.addToPermissions(shiroPermission)
        role2.addToPermissions(shiroPermission)*//*

        collection.addToBundle(bundle)

    }*/

    //DISABLED
    /*def addBaseContentToBundle(BaseContent baseContent, Bundle bundle) {
        ShiroPermission shiroPermission = this.findOrCreateShiroPermission("bundle:removebasecontent:" + baseContent.id)
        bundle.addToContent(baseContent)
    }*/

    def moveBundle(Bundle bundle, Collection collection)
    {
        if (collection != bundle.ownerCollection)
        {

            /*ShiroPermission shiroPermission3 = ShiroPermission.findByPermission("bundle:*:${bundle.UUID}")

         ShiroRole oldCollectionAdminRole = bundle.ownerCollection.administratorRole()
         ShiroRole oldInstitutionAdmin = bundle.ownerCollection.owner.administratorRole()

         ShiroRole newCollectionAdminRole = collection.administratorRole()
         ShiroRole newInstitutionAdmin = collection.owner.administratorRole()*/

            bundle.ownerCollection.removeFromBundle(bundle)
            collection.addToBundle(bundle)


            bundle.ownerCollection = collection
        }

        /*oldCollectionAdminRole.removeFromPermissions(shiroPermission3)
        oldInstitutionAdmin.removeFromPermissions(shiroPermission3)

        newCollectionAdminRole.addToPermissions(shiroPermission3)
        newInstitutionAdmin.addToPermissions(shiroPermission3)*/

    }

/*
    def moveBundle(Bundle bundle, Collection collection) {

        System.out.println("BUNDLE:" +bundle.id);
        System.out.println("OWNER_COLLECTION:" +bundle.ownerCollection.id);

        System.out.println("collection:removebundle:" + bundle.id);

        ShiroPermission shiroPermission =  ShiroPermission.findByPermission( "collection:removebundle:" + bundle.id)
        ShiroPermission shiroPermission2 =  ShiroPermission.findByPermission( "collection:removebundle:" + bundle.ownerCollection.id)
        //ShiroPermission shiroPermission3 =  ShiroPermission.findByPermission( "bundle:delete:${bundle.id}")
        ShiroPermission shiroPermission3 =  ShiroPermission.findByPermission( "bundle:*:${bundle.id}")


        ShiroUser owner=bundle.createdBy
        ShiroRole oldCollectionAdminRole=bundle.ownerCollection.superAdminRole()
        ShiroRole oldInstitutionAdmin=bundle.ownerCollection.owner.superAdminRole()

        ShiroRole newCollectionAdminRole=collection.superAdminRole()
        ShiroRole newInstitutionAdmin=collection.owner.superAdminRole()


        ShiroPermission newShiroPermission = this.findOrCreateShiroPermission("collection:removebundle:" + collection.id)

        collection.addToBundle(bundle)

        bundle.ownerCollection.removeFromBundle(bundle)
        bundle.ownerCollection.removeFromUnconfirmedBundle(bundle)
        bundle.ownerCollection = collection

//        owner.removeFromPermissions(shiroPermission2)
        oldCollectionAdminRole.removeFromPermissions(shiroPermission2)
        oldInstitutionAdmin.removeFromPermissions(shiroPermission2)

        oldCollectionAdminRole.removeFromPermissions(shiroPermission)
        oldInstitutionAdmin.removeFromPermissions(shiroPermission)

        oldCollectionAdminRole.removeFromPermissions(shiroPermission3)
        oldInstitutionAdmin.removeFromPermissions(shiroPermission3)

        owner.addToPermissions(newShiroPermission)
        newCollectionAdminRole.addToPermissions(newShiroPermission)
        newInstitutionAdmin.addToPermissions(newShiroPermission)

        newCollectionAdminRole.addToPermissions(shiroPermission)
        newInstitutionAdmin.addToPermissions(shiroPermission)

        newCollectionAdminRole.addToPermissions(shiroPermission3)
        newInstitutionAdmin.addToPermissions(shiroPermission3)

    }
*/

    def removeBundle(Bundle bundle, Collection collection)
    {
        /*ShiroPermission shiroPermission = ShiroPermission.findByPermission("collection:removebundle:" + bundle.UUID)

        collection.administratorRole().removeFromPermissions(shiroPermission)

        System.out.println("TODO institution admin removeBundle");
        *//*if(collection.owner.superAdminRole() !contains bundle in a collection)
        {
            collection.superAdminRole().removeFromPermissions(shiroPermission)
        }*/

        collection.removeFromBundle(bundle)
        //shiroPermission?.delete()
    }

    def moveBaseContent(BaseContent content, Bundle bundle)
    {

        /*ShiroPermission shiroPermission3 = ShiroPermission.findByPermission("basecontent:*:${content.UUID}")

        ShiroRole oldCollectionAdminRole = content.administratorRole()
        ShiroRole oldInstitutionAdmin = content.owner.administratorRole()

        ShiroRole newCollectionAdminRole = bundle.administratorRole()
        ShiroRole newInstitutionAdmin = bundle.owner.administratorRole()*/

        /*bundle.addToContent(content)

        content.ownerBundle.removeFromContent(content)*/
        content.ownerBundle = bundle

        /*oldCollectionAdminRole.removeFromPermissions(shiroPermission3)
        oldInstitutionAdmin.removeFromPermissions(shiroPermission3)

        newCollectionAdminRole.addToPermissions(shiroPermission3)
        newInstitutionAdmin.addToPermissions(shiroPermission3)*/


    }
/*
    def moveBaseContent(BaseContent content, Bundle bundle) {

        //ShiroPermission shiroPermission = this.findOrCreateShiroPermission("bundle:removebasecontent:" + content.id)
        //ShiroRole collectionAdminRoleOld = content.ownerBundle.superAdminRole()
        //ShiroRole institutionAdminRoleOld = content.ownerBundle.owner.superAdminRole()
        //ShiroRole collectionAdminRoleNew = ShiroRole.findByName("Administrator : " + bundle.owner.name)
        //SecurityUtils.getSubject().isPermitted("institution:confirm:" + bundle.owner.id) ? bundle.addToContent(content) : bundle.addToUnconfirmedBaseContent(content)

        bundle.addToContent(content)

        //collectionAdminRoleOld.removeFromPermissions(shiroPermission)
        content.ownerBundle.removeFromContent(content)
        content.ownerBundle.removeFromUnconfirmedBaseContent(content)
        content.ownerBundle = bundle
        //collectionAdminRoleNew.addToPermissions(shiroPermission)


    }
*/

    /*def attachPermissionToDomain(ShiroPermission permission)
    {
        int c = permission.permission.count(":")
        if (c == 2)
        {
            String idString = StringUtils.substringAfterLast(permission.permission, ":")

            if (NumberUtils.isNumber(idString))
            {
                BaseDomain.get(NumberUtils.toLong(idString)).addToShiroPermission(permission)
            }

        }

    }*/

    public void collectionSetPublish(Collection collection, boolean value)
    {
        collection.published = value
        collection.save(flush: true)
    }



    public void bundleSetPublish(Bundle bundle, boolean value)
    {
        bundle.published = value
        bundle.content.findAll {it.ownerBundle.id == bundle.id}.each {  content ->
            content.published = value
        }
        bundle.save(flush: true)
    }

    public ShiroPermission findOrCreateShiroPermission(String permission)
    {

        ShiroPermission shiroPermission

        if (ShiroPermission.countByPermission(permission.toLowerCase()) == 0)
            shiroPermission = new ShiroPermission(permission: permission).save()
        else
            shiroPermission = ShiroPermission.findByPermission(permission.toLowerCase())


        return shiroPermission


    }

    public List getBundlesBaseContentAdd()
    {
        def list = Bundle.findAllByDeleted(false)

        ArrayList result = new ArrayList()

        list.each {bundle ->
            if (permissionService.bundleAddBaseContent(bundle))
            {
                result.add(bundle)
            }

        }
        return result

    }

    public List getCollectionsBundleAdd()
    {
        def list = Collection.list([deleted: false])

        ArrayList result = new ArrayList()

        list.each {collection ->
            if (permissionService.collectionAddBundle(collection))
            {
                result.add(collection)
            }

        }
        return result

    }

    public void indexAll()
    {
        this.indexAll(Collection.class)
        this.indexAll(Bundle.class)
        this.indexAll(BaseContent.class)
        this.indexAll(MetaRecord.class)
    }

    public void indexAll(Class c)
    {
        int bc = c.count()

        log.info("Start indexing ${bc} ${c.simpleName}")

        int loop = Math.floor(bc / 50)
        int rest = bc - loop * 50

        // println 'Count: ' + bc
        // println 'Loop: ' + loop
        // println 'Rest: ' + rest

        long time = System.currentTimeMillis()

        for (int i = 0; i <= loop; i++)
        {
            println loop
            c.withNewSession {
                c.listOrderById(max: i == loop ? rest : 50, offset: 50 * i).each {obj ->
                    obj.index()
                }
            }
            //println "Time: " + (System.currentTimeMillis() - time) + '  <>  Loop: ' + i + "/" + loop
            //time = System.currentTimeMillis()

        }
        log.info("End  indexing ${bc} ${c.simpleName} - Time ${System.currentTimeMillis() - time}ms")

    }

    /*def addContentToBundle(BaseContent baseContent,Bundle bundle) {
        baseContent.save()
        SecurityUtils.getSubject().isPermitted("institution:confirm:"+baseContent.createdBy.institution.id) ? baseContent.ownerBundle.addToContent(baseContent) : baseContent.ownerBundle.addToUnconfirmedBaseContent(baseContent)
    }*/

    /*def saveNewContent(BaseContent baseContent) {
        baseContent.save()
        SecurityUtils.getSubject().isPermitted("institution:confirm:"+baseContent.createdBy.institution.id) ? baseContent.ownerBundle.addToContent(baseContent) : baseContent.ownerBundle.addToUnconfirmedBaseContent(baseContent)
    }
    def saveNewBundle(Bundle bundle) {
        bundle.save()
        SecurityUtils.getSubject().isPermitted("institution:confirm:"+bundle.createdBy.institution.id) ? bundle.ownerCollection.addToBundle(bundle) : bundle.ownerCollection.addToUnconfirmedBundle(bundle)
    }*/


}


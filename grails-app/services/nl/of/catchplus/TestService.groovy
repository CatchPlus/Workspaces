package nl.of.catchplus

import grails.util.GrailsNameUtils
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.NumberUtils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.zkoss.zul.ListModelList


class TestService implements ApplicationContextAware {

    ApplicationContext applicationContext



    static transactional = true

    public String getAbsolutePath(folderPath, fileName) {
        "${applicationContext.getResource(folderPath).getFile()}${File.separatorChar}${fileName}"
    }

    public def serviceMethod(String s) {
        System.out.println("SERVICE CALLED "+s);
    }

    public ListModelList getListModelList() {

        synchronized (applicationContext.getServletContext()) {

            return applicationContext.getServletContext().listModelList

        }

    }

    /*def save(BaseDomain baseDomain)
    {
        baseDomain.validate()
        if(baseDomain.hasErrors())
        {
            System.out.println(baseDomain.errors);
        }
        baseDomain.save(flush:true,validate:true)
    }*/

    def createAddCollectionPermission(WorkSpace workSpace) {
        ShiroRole.findAllByNameLike("%Administrator%").each {role ->
            String permission = "workspace:addCollection:" + workSpace.UUID
            role.addToPermissions(this.findOrCreateShiroPermission(permission))


        }
    }

    def createAddBundlePermission(Collection collection) {

        ShiroRole institutionAdminRole = collection.owner.administratorRole()
        ShiroRole collectionAdminRole = collection.administratorRole()
        ShiroRole userAddRole = collection.userAddRole()

        ShiroPermission p1 = this.findOrCreateShiroPermission("collection:addbundle:" + collection.UUID)
        ShiroPermission p2 = this.findOrCreateShiroPermission("collection:removebundle:" + collection.UUID)
        ShiroPermission p3 = this.findOrCreateShiroPermission("collection:createbundle:" + collection.UUID)

        institutionAdminRole.addToPermissions(p1)
        institutionAdminRole.addToPermissions(p2)
        institutionAdminRole.addToPermissions(p3)

        collectionAdminRole.addToPermissions(p1)
        collectionAdminRole.addToPermissions(p2)
        collectionAdminRole.addToPermissions(p3)

        userAddRole.addToPermissions(p1)
        userAddRole.addToPermissions(p2)
        userAddRole.addToPermissions(p3)
    }

    def createAddBaseContentPermission(Bundle bundle) {
        ShiroRole institutionAdminRole = bundle.owner.administratorRole()
        ShiroRole collectionAdminRole = bundle.administratorRole()
        ShiroRole userAddRole = bundle.userAddRole()

        ShiroPermission p1 = this.findOrCreateShiroPermission("bundle:addbaseContent:" + bundle.UUID)
        ShiroPermission p2 = this.findOrCreateShiroPermission("bundle:removebaseContent:" + bundle.UUID)
        ShiroPermission p3 = this.findOrCreateShiroPermission("bundle:createbasecontent:" + bundle.UUID)

        institutionAdminRole.addToPermissions(p1)
        institutionAdminRole.addToPermissions(p2)
        institutionAdminRole.addToPermissions(p3)

        collectionAdminRole.addToPermissions(p1)
        collectionAdminRole.addToPermissions(p2)
        collectionAdminRole.addToPermissions(p3)

        userAddRole.addToPermissions(p1)
        userAddRole.addToPermissions(p2)
        userAddRole.addToPermissions(p3)

    }



    /*def createOwnerPermission(BaseRepository baseDomain) {
        String permission = GrailsNameUtils.getShortName(baseDomain.class).toString() + ":*:" + baseDomain.UUID
        baseDomain.createdBy.addToPermissions(this.findOrCreateShiroPermission(permission))
    }*/



    def createEveryonePermission(BaseRepository baseDomain) {

        String permission = GrailsNameUtils.getShortName(baseDomain.class).toString() + ":show:" + baseDomain.UUID


        ShiroRole.everyoneRole().addToPermissions(this.findOrCreateShiroPermission(permission))

        permission = GrailsNameUtils.getShortName(baseDomain.class).toString() + ":pin:" + baseDomain.UUID
        ShiroRole.everyoneRole().addToPermissions(this.findOrCreateShiroPermission(permission))

        permission = GrailsNameUtils.getShortName(baseDomain.class).toString() + ":unpin:" + baseDomain.UUID
        ShiroRole.everyoneRole().addToPermissions(this.findOrCreateShiroPermission(permission))

    }


/*
    def createEveryonePermission(StoredFile storedFile) {

        String permission = GrailsNameUtils.getShortName(storedFile.class).toString() + ":show,download,render:" + storedFile.UUID

        ShiroRole.everyoneRole().addToPermissions(this.findOrCreateShiroPermission(permission))

    }
*/

    def createStoredFileRolePermission(StoredFile storedFile) {
        def institutionAdminRole = storedFile.content.owner.administratorRole()
        def collectionAdminRole = storedFile.content.administratorRole()
        def userAddRole = storedFile.content.userAddRole()
        def userReadRole = storedFile.content.userReadRole()

        ShiroPermission pShow = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(storedFile.class).toString() + ":show:" + storedFile.id)
        institutionAdminRole.addToPermissions(pShow)
        collectionAdminRole.addToPermissions(pShow)
        userAddRole.addToPermissions(pShow)
        userReadRole.addToPermissions(pShow)

        ShiroPermission pPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(storedFile.class).toString() + ":download:" + storedFile.id)
        institutionAdminRole.addToPermissions(pPin)
        collectionAdminRole.addToPermissions(pPin)
        userAddRole.addToPermissions(pPin)
        userReadRole.addToPermissions(pPin)

        ShiroPermission pUnPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(storedFile.class).toString() + ":render:" + storedFile.id)
        institutionAdminRole.addToPermissions(pUnPin)
        collectionAdminRole.addToPermissions(pUnPin)
        userAddRole.addToPermissions(pUnPin)
        userReadRole.addToPermissions(pUnPin)

        ShiroPermission pAll = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(storedFile.class).toString() + ":*:" + storedFile.id)
        institutionAdminRole.addToPermissions(pAll)
        collectionAdminRole.addToPermissions(pAll)

    }

    @Deprecated
    def createRolePermission(BaseRepository baseRepository) {

        //System.out.println("OC2:" +baseRepository.ownerCollection);

        def institutionAdminRole = baseRepository.owner.administratorRole()
        def collectionAdminRole = baseRepository.administratorRole()
        def userAddRole = baseRepository.userAddRole()
        def userReadRole = baseRepository.userReadRole()
       // String permission = GrailsNameUtils.getShortName(baseRepository.class).toString() + ":*:" + baseRepository.UUID




        ShiroPermission pShow = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":show:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pShow)
        collectionAdminRole.addToPermissions(pShow)
        userAddRole.addToPermissions(pShow)
        userReadRole.addToPermissions(pShow)

        /*ShiroPermission pPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":pin:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pPin)
        collectionAdminRole.addToPermissions(pPin)
        userAddRole.addToPermissions(pPin)
        userReadRole.addToPermissions(pPin)

        ShiroPermission pUnPin = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":unpin:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pUnPin)
        collectionAdminRole.addToPermissions(pUnPin)
        userAddRole.addToPermissions(pUnPin)
        userReadRole.addToPermissions(pUnPin)*/

        /*ShiroPermission pPublish = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":publish:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pPublish)
        collectionAdminRole.addToPermissions(pPublish)*/

        ShiroPermission pAll = this.findOrCreateShiroPermission(GrailsNameUtils.getShortName(baseRepository.class).toString() + ":*:" + baseRepository.UUID)
        institutionAdminRole.addToPermissions(pAll)
        collectionAdminRole.addToPermissions(pAll)
        baseRepository.createdBy.addToPermissions(pAll)

        //baseRepository.createdBy.addToPermissions(this.findOrCreateShiroPermission(permission))

    }

/*
    def createStoredFilePermissions(StoredFile storedFile) {

        //owner
        String permission = GrailsNameUtils.getShortName(storedFile.class).toString() + ":show,delete,download,render,pin,unpin:" + storedFile.id
        storedFile.createdBy.addToPermissions(this.findOrCreateShiroPermission(permission))

        //roles
        permission = GrailsNameUtils.getShortName(storedFile.class).toString() + ":show,download,render,pin,unpin:" + storedFile.id

        def userRole = ShiroRole.findByName("User : " + storedFile.content.owner.name)
        def adminRole = ShiroRole.findByName("Administrator : " + storedFile.content.owner.name)

        userRole.addToPermissions(this.findOrCreateShiroPermission(permission))
        adminRole.addToPermissions(this.findOrCreateShiroPermission(permission))

        permission = GrailsNameUtils.getShortName(storedFile.class).toString() + ":show,delete,download,render:" + storedFile.id
        adminRole.addToPermissions(this.findOrCreateShiroPermission(permission))

    }
*/

    ShiroRole createEveryOneRole() {
        def everyOneRole = new ShiroRole(name: "Iedereen").save(flush: true)
        /*String permission = "*:demo:*"
        ShiroPermission shiroPermission = new ShiroPermission(permission: permission).save(flush: true)
        everyOneRole.addToPermissions(shiroPermission)*/

        return everyOneRole

    }

    ShiroGroup createEveryOneGroup() {
        def everyOneGroup = new ShiroGroup(name: "Iedereen").save(flush: true)
        everyOneGroup.addToRoles(ShiroRole.everyoneRole())
        return everyOneGroup

    }




    ShiroRole createInstitutionAdminRole(Institution institution) {
        //def workSpace=WorkSpace.list().iterator().next()
        def adminRole = new ShiroRole(name: "Institution : Administrator : " + institution.id).save(flush: true)
        /*String permission = "bundle,basecontent,collection,shiroUser:create,save,confirm"
        ShiroPermission shiroPermission = new ShiroPermission(permission: permission).save(flush: true)
        adminRole.addToPermissions(shiroPermission)*/
        /*shiroPermission = new ShiroPermission(permission: 'workspace:confirm:*').save(flush: true)
        adminRole.addToPermissions(shiroPermission)
*/

     /*   adminRole.addToPermissions(this.findOrCreateShiroPermission("*:list"))
        adminRole.addToPermissions(this.findOrCreateShiroPermission("instituut:manageadministrators:${institution.id}"))
       // adminRole.addToPermissions(this.findOrCreateShiroPermission("workspace:createcollection:${workSpace.id}"))

        adminRole.addToPermissions(this.findOrCreateShiroPermission("*:index"))
        adminRole.addToPermissions(this.findOrCreateShiroPermission("api:*"))
//        institution.addToRoles(adminRole)*/

        ShiroUser globalAdminUser=new ShiroUser(institution: institution,account: Account.superAdminAccount()).save(flush:true)
        globalAdminUser.addToRoles(adminRole)

        institution.group.addToUsers(globalAdminUser)

        return adminRole

    }

    ShiroRole createCollectionAdminRole(Collection collection) {
        def adminRole = new ShiroRole(name: "Collection : Administrator : " + collection.UUID).save(flush: true)
        /*String permission = "collection:*:${collection.UUID}"
        ShiroPermission shiroPermission = new ShiroPermission(permission: permission).save(flush: true)
        adminRole.addToPermissions(shiroPermission)


        shiroPermission =  findOrCreateShiroPermission('bundle,basecontent:create,save')
        adminRole.addToPermissions(shiroPermission)


        adminRole.addToPermissions(this.findOrCreateShiroPermission("*:list"))
        adminRole.addToPermissions(this.findOrCreateShiroPermission("*:index"))
        adminRole.addToPermissions(this.findOrCreateShiroPermission("api:*"))*/

        return adminRole
    }

    ShiroRole createCollectionUserAddRole(Collection collection) {
        def role = new ShiroRole(name: "Collection : UserAdd : " + collection.UUID)
        role.save(flush: true)
        //String permission = "bundle,basecontent:create,save"
        /*ShiroPermission shiroPermission = new ShiroPermission(permission: permission).save(flush: true)
        role.addToPermissions(shiroPermission)*/


        /*role.addToPermissions(this.findOrCreateShiroPermission("*:list"))
        role.addToPermissions(this.findOrCreateShiroPermission("*:index"))
        role.addToPermissions(this.findOrCreateShiroPermission("api:*"))*/

        return role
    }

    ShiroRole createCollectionUserReadRole(Collection collection) {
        def role = new ShiroRole(name: "Collection : UserRead : " + collection.UUID)
        role.save(flush: true)
        //String permission = "bundle,basecontent:create,save,confirm:${collection.id}"
        //ShiroPermission shiroPermission = new ShiroPermission(permission: permission).save(flush: true)
        //role.addToPermissions(shiroPermission)


        /*role.addToPermissions(this.findOrCreateShiroPermission("*:list"))
        role.addToPermissions(this.findOrCreateShiroPermission("*:index"))
        role.addToPermissions(this.findOrCreateShiroPermission("api:*"))
*/
        return role
    }


    public ShiroGroup createInstitutionGroup(Institution institution) {
        ShiroGroup group = new ShiroGroup(name: """Group : ${institution.name}""")

        //ShiroPermission permission = this.findOrCreateShiroPermission('institution:confirm:' + institution.id)
        //group.addToPermissions(permission)
        group.save(flush: true)
        institution.group=group
        institution.save()
        return group
    }

    public ShiroPermission findOrCreateShiroPermission(String permission) {

        ShiroPermission shiroPermission

        if (ShiroPermission.countByPermission(permission.toLowerCase()) == 0)
            shiroPermission = new ShiroPermission(permission: permission).save()
        else
            shiroPermission = ShiroPermission.findByPermission(permission.toLowerCase())

        int c = permission.count(":")
        if (c == 2) {
            String idString = StringUtils.substringAfterLast(permission, ":")

            if (NumberUtils.isNumber(idString)) {
                //BaseDomain.get(NumberUtils.toLong(idString)).addToShiroPermission(shiroPermission)
            }

        }

        return shiroPermission


    }

    /*public ShiroUser getLoggedInPerson()
    {
         def session




        try
        {
            session = RequestContextHolder.currentRequestAttributes().getSession()

            return ShiroUser.read(session.user.id)
        }
        catch (Exception e)
        {
            return null
        }

    }

    public ShiroUser setLoggedInPerson(ShiroUser shiroUser)
    {
         def session

        try
        {
            session = RequestContextHolder.currentRequestAttributes().getSession()
            session.user=shiroUser
        }
        catch (Exception e)
        {

    public ShiroUser getAdmin(Institution institution)
    {

        ShiroRole adminRole = ShiroRole.findByName("Administrator : " + institution.name)
        def users = ShiroUser.withCriteria {

            and {
                roles {
                    idEq(adminRole.id)
                }
            }

        }
        return users.iterator().next()
    }     return null
        }

    }*/



    public ShiroUser getAdmin(Institution institution) {

        ShiroRole adminRole = ShiroRole.findByName("Administrator : " + institution.name)
        def users = ShiroUser.withCriteria {

            and {
                roles {
                    idEq(adminRole.id)
                }
            }

        }
        return users.iterator().next()
    }

    public void test() {

        System.out.println("CALL");

    }


}

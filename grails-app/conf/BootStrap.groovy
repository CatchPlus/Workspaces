import org.apache.shiro.crypto.hash.Sha256Hash
/*import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent*/

import nl.of.catchplus.*
import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.util.GrailsUtil
//import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
//import grails.plugin.searchable.SearchableService

class BootStrap
{

    TestService testService
    def gormService
    def shiroSecurityManager
    def grailsApplication
    def messageSource

    def sessionFactory

    //  def searchableService

    def init = { servletContext ->



        println "Start Bootstrap"
        //BaseDomain.logging = false

        //searchableService.stopMirroring()

        grailsApplication.domainClasses.each {domainClass ->//iterate over the domainClasses
            if (domainClass.clazz.name.contains("nl.of.catchplus"))
            {//only add it to the domains in my plugin
                domainClass.metaClass.retrieveErrors = {
                    def list = delegate?.errors?.allErrors?.collect {messageSource.getMessage(it, null)}
                    return list
                    // return list?.join('\n')
                }

            }
        }


        Object.metaClass.trimLength = {Integer stringLength ->

            String trimString = delegate?.toString()
            String concatenateString = "..."
            List separators = [".", " "]

            if (stringLength && trimString && stringLength > 0)
            {
                float f = CatchplusUtils.countUpperCase(trimString) / (trimString.length() * 2)
                if (trimString?.length() + (trimString?.length() * f) > stringLength)
                {
                    int reducedStringLength = stringLength - Math.round(stringLength * f)

                    trimString = trimString.substring(0, reducedStringLength)

                    trimString += concatenateString
                }
            }

            return trimString
        }

        grailsApplication.config.'grails.web.disable.multipart' = true

        //Subject subjectShiro = new Subject.Builder(shiroSecurityManager).buildSubject();

        //servletContext.listModelList = new ListModelList()

        //subjectShiro.execute {

        //any code called from within here
        //can successfully call/access
        //SecurityUtils.subject


        if (ShiroRole.countByName('Iedereen') == 0)
        {


            def iedereenRole = testService.createEveryOneRole()
            def iedereenGroup = testService.createEveryOneGroup()

            Institution adminInstitution = new Institution(name: 'Global administrator', shortName: 'G.O.D.')
            adminInstitution.save()



            Account adminAccount = new Account(activated: true, firstName: "Global", lastName: "Admin", username: "super@cp.nl", password: new Sha256Hash("qwerty").toHex()).save(flush: true)

            ShiroUser adminUser = new ShiroUser(account: adminAccount, institution: adminInstitution).save(flush: true)

            def globalAdminRole = new ShiroRole(name: "Administrator").save(flush: true)
            ShiroPermission adminPer = new ShiroPermission(permission: "*:*").save(flush: true)
            globalAdminRole.addToPermissions(adminPer)
            globalAdminRole.save(flush: true)
            adminUser.addToRoles(globalAdminRole)
            adminUser.save(flush: true)

            switch (GrailsUtil.environment)
            {
                case "development":




                    Institution dummyInstitution = new Institution(shortName: 'PTT', name: 'post telefoon telegraaf').save(flush: true)
                    testService.createInstitutionGroup(dummyInstitution)
                    testService.createInstitutionAdminRole(dummyInstitution)









                    MetaRecordKey title = new MetaRecordKey(label: "title").save(flush: true)
                    MetaRecordKey creator = new MetaRecordKey(label: "creator").save(flush: true)
                    MetaRecordKey subject = new MetaRecordKey(label: "subject").save(flush: true)
                    MetaRecordKey description = new MetaRecordKey(label: "description").save(flush: true)
                    MetaRecordKey publisher = new MetaRecordKey(label: "publisher").save(flush: true)
                    MetaRecordKey contributor = new MetaRecordKey(label: "contributor").save(flush: true)
                    MetaRecordKey date = new MetaRecordKey(label: "date").save(flush: true)
                    MetaRecordKey type = new MetaRecordKey(label: "type").save(flush: true)
                    MetaRecordKey format = new MetaRecordKey(label: "format").save(flush: true)
                    MetaRecordKey identifier = new MetaRecordKey(label: "identifier").save(flush: true)
                    MetaRecordKey source = new MetaRecordKey(label: "source").save(flush: true)
                    MetaRecordKey language = new MetaRecordKey(label: "language").save(flush: true)
                    MetaRecordKey relation = new MetaRecordKey(label: "relation").save(flush: true)
                    MetaRecordKey coverage = new MetaRecordKey(label: "coverage").save(flush: true)
                    MetaRecordKey rights = new MetaRecordKey(label: "rights").save(flush: true)

                    /*WorkSpace workSpace = new WorkSpace(createdBy: adminUser.id, title: "Monk", description: "Monk workSpace beschrijving", collectionName: "Set", bundleName: "Boek", contentName: "Pagina")
                                    workSpace.validate()
                                    workSpace.save(flush: true)

                                    workSpace.addToCollectionMetaRecordKey(metaRecordKeyDCTitle)
                                    workSpace.addToCollectionMetaRecordKey(metaRecordKeyDCRights)
                                    testService.createEveryonePermission(workSpace)
                    */
                    for (int i = 0; i < 1; i++)
                    {

                        WorkSpace workSpace = new WorkSpace(createdBy: adminUser, title: "Monk " + i, description: "Monk workSpace beschrijving " + i, collectionName: "Set Naam ${i}", bundleName: "Bundle Naam ${i}", contentName: "Content Naam ${i}")
                        workSpace.validate()
                        workSpace.save(flush: true)

                        MetaRecordKey.list().each {m ->
                            workSpace.addToCollectionMetaRecordKey(m)
                            workSpace.addToBaseContentMetaRecordKey(m)
                        }

                        workSpace.addToBundleMetaRecordKey(title)
                        workSpace.addToBundleMetaRecordKey(creator)

                        // workSpace DISABLED!!!
                        //testService.createEveryonePermission(workSpace)

                        for (int j = 0; j < 2; j++)
                        {
                            Institution institution = new Institution(name: 'instituut' + i + "" + j, shortName: 'I.N.T.' + i + "" + j)
                            institution.validate()
                            institution.save(flush: true)

                            ShiroGroup institutionGroup = testService.createInstitutionGroup(institution)
                            ShiroRole institutionAdminRole = testService.createInstitutionAdminRole(institution)

                            Account institutionAdminAccount = new Account(activated: true, firstName: "voornaam; institutionAdminUser", lastName: "achternaam; institutionAdminUser", username: "${institution.name}@admin.nl", password: new Sha256Hash("qwerty").toHex()).save(flush: true)
                            ShiroUser institutionAdminUser = new ShiroUser(institution: institution, account: institutionAdminAccount).save(flush: true)

                            if (j == 0)
                                new ShiroUser(account: institutionAdminAccount, institution: dummyInstitution).save(flush: true)

                            institutionAdminUser.addToRoles(institutionAdminRole)
                            institutionGroup.addToUsers(institutionAdminUser)

                            for (int k = 0; k < 2; k++)
                            {
                                Collection collection = new Collection(createdBy: institutionAdminUser, title: "collection" + i + "" + j + "" + k, ownerWorkSpace: workSpace, description: "set beschrijving " + i + "" + j + "" + k, owner: institution).save(flush: true)
                                workSpace.addToCollection(collection)

                                new Bookmark(baseRepository: collection, shiroUser: institutionAdminUser, description: "desc" + i + "" + j + "" + k).save(flush: true)

                                //set admin
                                ShiroRole collectionAdminRole = testService.createCollectionAdminRole(collection)
                                Account collectionAdminAccount = new Account(activated: true, firstName: "voornaam; setAdminUser", lastName: "achternaam; setAdminUser", username: "${collection.title}@admin.nl", password: new Sha256Hash("qwerty").toHex()).save(flush: true)
                                ShiroUser collectionAdminUser = new ShiroUser(institution: institution, account: collectionAdminAccount).save(flush: true)

                                collectionAdminUser.addToRoles(collectionAdminRole)
                                institutionGroup.addToUsers(collectionAdminUser)

                                ShiroRole userAddRole = testService.createCollectionUserAddRole(collection)
                                ShiroRole userReadRole = testService.createCollectionUserReadRole(collection)

                                institutionGroup.addToRoles(userReadRole)

                                //   testService.createAddBundlePermission(collection)
                                //             testService.createRolePermission(collection)

                                println 'done'

                                for (int l = 0; l < 2; l++)
                                {
                                    Bundle bundle = new Bundle(ownerCollection: collection, createdBy: collectionAdminUser, title: "bundle titel" + i + "" + j + "" + k + "" + l, description: " bundle beschrijving" + i + "" + j + "" + k + "" + l, owner: institution).save(flush: true)
                                    collection.addToBundle(bundle)

                                    new Bookmark(baseRepository: bundle, shiroUser: institutionAdminUser, description: "desc" + i + "" + j + "" + k + "" + l).save(flush: true)

                                    //testService.createAddBaseContentPermission(bundle)
                                    // testService.createRolePermission(bundle)


                                    for (int m = 0; m < 1; m++)
                                    {
                                        BaseContent content = new BaseContent(createdBy: collectionAdminUser, title: "content titel" + i + "" + j + "" + k + "" + l + "" + m, description: " content beschrijving" + i + "" + j + "" + k + "" + l + "" + m, url: """www.content-url${i + "" + j + "" + k + "" + l + "" + m}.nl""", ownerBundle: bundle, owner: institution).save(flush: true)

                                        bundle.addToContent(content)

                                        MetaRecord metaRecord = new MetaRecord(key: title, value: 'titelllll')
                                        metaRecord.save(flush: true)
                                        content.addToMetaRecord(metaRecord)

                                        //testService.createRolePermission(content)


                                    }
                                }
                            }
                        }


                    }
                    int i = 0
                    Collection.list().each {collection ->

                        Institution institution = collection.owner

                        Account institutionReadAccount = new Account(activated: true, firstName: "voornaam; institutionreadUser", lastName: "achternaam; institutionReadUser", username: "${institution.name + "" + i}@read.nl", password: new Sha256Hash("qwerty").toHex()).save(flush: true)
                        ShiroUser institutionReadUser = new ShiroUser(institution: institution, account: institutionReadAccount).save(flush: true)
                        institutionReadUser.addToRoles(collection.userReadRole())
                        institution.group.addToUsers(institutionReadUser)




                        Account institutionAddAccount = new Account(activated: true, firstName: "voornaam; institutionaddUser", lastName: "achternaam; institutionReadUser", username: "${institution.name + "" + i}@add.nl", password: new Sha256Hash("qwerty").toHex()).save(flush: true)
                        ShiroUser institutionAddUser = new ShiroUser(institution: institution, account: institutionAddAccount).save(flush: true)

                        institutionAddUser.addToRoles(collection.userAddRole())
                        institution.group.addToUsers(institutionAddUser)

                        /*for (int j = 0; j < 1; j++)
                        {
                            Bundle bundle = new Bundle(ownerCollection: collection, createdBy: institutionAddUser, title: "bundle titel add" + i + "" + j, description: " bundle beschrijving" + i + "" + j, owner: institution).save(flush: true)
                            collection.addToBundle(bundle)

                            testService.createAddBaseContentPermission(bundle)
                            testService.createRolePermission(bundle)


                            for (int k = 0; k < 1; k++)
                            {
                                BaseContent content = new BaseContent(createdBy: institutionAddUser, title: "content titel add" + i + "" + j + "" + k, description: " content beschrijving" + i + "" + j + "" + k, url: """www.content-url${i + "" + j + "" + k}.nl""", ownerBundle: bundle, owner: institution).save()

                                bundle.addToContent(content)


                            }
                        }*/
                        i++
                    }
                    //AuditLogEvent.list()*.delete()

                    //}

                    break
            }
        }
        /*  else if (true && ShiroRole.countByName('Iedereen') == 0)
  {
      println "Start Bootstrap CLEAN"



      def iedereenRole = testService.createEveryOneRole()
      def iedereenGroup = testService.createEveryOneGroup()

      Institution adminInstitution = new Institution(name: 'Global administrator', shortName: 'G.O.D.').save()



      Account adminAccount = new Account(activated: true, firstName: "Michael", lastName: "Dobel", username: "super@cp.nl", password: new Sha256Hash("qwerty").toHex()).save()
      println adminAccount.id
      ShiroUser adminUser = new ShiroUser(account: adminAccount, institution: adminInstitution)
      adminUser.validate()
      println adminUser.errors

      println adminUser.id

      def globalAdminRole = new ShiroRole(name: "Administrator").save()
      ShiroPermission adminPer = new ShiroPermission(permission: "*:*").save()
      globalAdminRole.addToPermissions(adminPer)
      adminUser.addToRoles(globalAdminRole)
  }

      //}

      //  AuditLogEvent.executeUpdate('delete from AuditLogEvent')

      Account.list()*.password = '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'*/

        org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
        sessionn.clear()
        sessionn.flush()

        BaseDomain.bootstrap = false
        BaseDomain.logging = true
        //searchableService.stopMirroring()
    }

    def destroy = {
    }

    void clearDB()
    {
        (ApplicationHolder.application.getArtefacts("Domain") as List).each {
            it.newInstance().list()*.delete()
        }
        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()
    }

}

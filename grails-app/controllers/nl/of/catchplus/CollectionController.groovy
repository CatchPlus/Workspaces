package nl.of.catchplus

import groovyx.net.http.*

//import jersey.multipart.demo.model.Project;


import org.zkoss.zul.Window

import org.zkoss.zul.ListModel
import org.zkforge.timeplot.data.PlotDataSource
import org.zkforge.timeplot.geometry.ValueGeometry
import org.zkforge.timeplot.geometry.DefaultValueGeometry
import org.zkforge.timeplot.geometry.DefaultTimeGeometry
import org.zkforge.timeplot.geometry.TimeGeometry
import org.zkoss.zul.SimpleListModel

import grails.converters.XML

import grails.plugin.searchable.SearchableService

/*import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntity

import org.apache.http.entity.mime.content.StringBody
import org.apache.http.entity.mime.content.FileBody
import org.springframework.http.HttpEntity
import org.restlet.engine.http.HttpResponse
import org.apache.http.entity.mime.HttpMultipartMode*/



class CollectionController
{

    def shiroService

    def sessionFactory

    HttpSessionService httpSessionService

    def bundleResourceService

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    GormService gormService

    SearchableService searchableService

    def jmsService

    BlaService blaService
    PermissionService permissionService

    def scaffold = Collection

    /*def upload2=
        {
            final String BASE_URI = "http://localhost:8080/";

        Client c = Client.create();
        WebResource service = c.resource(BASE_URI);

        *//*Project project = new Project();
        project.setName("Jersey");
        project.setDescription("Jersey is the open source, production quality, JAX-RS (JSR 311) Reference Implementation for building RESTful Web services.");
        project.setHomepage("https://jersey.dev.java.net/");
        project.setLicense("dual CDDL+GPL license");
        project.setSvnURL("https://jersey.dev.java.net/svn/jersey/trunk");
    *//*
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        URL url = new URL("https://jersey.dev.java.net/images/Jersey_yellow.png");
        BufferedImage bi = ImageIO.read(url);
        ImageIO.write(bi, "png", bas);
        byte[] logo = bas.toByteArray();

        // Construct a MultiPart with two body parts
        MultiPart multiPart = new MultiPart().
          bodyPart(new BodyPart(logo, MediaType.APPLICATION_OCTET_STREAM_TYPE)).
          //bodyPart(new BodyPart(project, MediaType.APPLICATION_XML_TYPE)).
          bodyPart(new BodyPart(logo, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        // POST the request
        ClientResponse response = service.path("/api/baseContent").
          type("multipart/mixed").post(ClientResponse.class, multiPart);
        System.out.println("Response Status : " + response.getEntity(String.class));


        }
    */

    def search2 = {

        def product = BaseContent.search(
                "Titel",
                [sort: 'dateCreated', order: 'asc', result: 'top']
        )

        render product as XML


    }


    def search = {

        def searchResult = searchableService.search(
                "Titel",
                [offset: 0, max: 20]
        )
        assert searchResult instanceof Map
        println "${searchResult.total} hits:"
        for (i in 0..<searchResult.results.size())
        {
            println "${searchResult.offset + i + 1}: " +
                    "${searchResult.results[i].toString()} " +
                    "(score ${searchResult.scores[i]})"
        }

    }

    def demo2 =
        {
            // def workSpaceList = WorkSpace.list()

            def workSpaceList = WorkSpace.list()

            def collectionList = Collection.list()

            [workSpaceList: workSpaceList, collectionList: collectionList, renderWorkSpace: true]


        }

    def sample =
        {

            String[] data = [
                    "Albert", "Bob", "Candy", "Elva", "Elva2", "Gaby", "Gavin", "Jason", "John",
                    "Jean", "Janet", "Jamie", "Jessica", "Peter", "Rex", "Richard", "Sam", "Sidney",
                    "Simon", "Sky", "Tony", "Vicky", "Winnie", "Wendy", "Zera", "Zenobia"]
            ListModel strset = new SimpleListModel(data);


            def workSpaceList = WorkSpace.list()
            [workSpaceList: workSpaceList, renderWorkSpace: true, strset: strset]
        }




    /*System.out.println('aabbb');
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost('/api/baseContent');

    def fileName ='/csb.log'

    FileBody bin = new FileBody(new File(fileName));
    //StringBody comment = new StringBody("Filename: " + fileName);

    MultipartEntity reqEntity = new MultipartEntity();
    reqEntity.addPart("bin", bin);
    //reqEntity.addPart("comment", comment);
    httppost.setEntity(reqEntity);
    //httppost.se

    HttpResponse response = httpclient.execute(httppost);
    HttpEntity resEntity = response.getEntity();
    System.out.println('aa');*/
    def zk9 =
        {
            PlotDataSource pds = new PlotDataSource();
            pds.setDataSourceUri("http://www.zkoss.org/zkdemo/widaagets/relporting/timeplot/immigration.txt");
            pds.setSeparator(" ");
            ValueGeometry vg = new DefaultValueGeometry();
            vg.setGridColor("#000000");
            TimeGeometry tg = new DefaultTimeGeometry();
            tg.setAxisLabelsPlacement("bottom");

            System.out.println('aaaaaap');

            [pds: pds, vg: vg, tg: tg]

        }

    def zk8 =
        {
            [test: 'bnlabla']

        }


    def zk6 =
        {
            String[] data = [
                    "Albert", "Bob", "Candy", "Elva", "Elva2", "Gaby", "Gavin", "Jason", "John",
                    "Jean", "Janet", "Jamie", "Jessica", "Peter", "Rex", "Richard", "Sam", "Sidney",
                    "Simon", "Sky", "Tony", "Vicky", "Winnie", "Wendy", "Zera", "Zenobia"]
            ListModel strset = new SimpleListModel(data);
            [strset: strset]
        }

    def zk5 =
        {

        }

    def zk4 =
        {

        }

    def zk3 =
        {

        }

    def zk2 = {
        def window = new Window(title: "listbox demo222", border: "normal")
        window << {
            button(label: 'buutton', onClick: "alert('Say hello word! from JS')")
        }
        [window: window]

    }

    def zk =
        {
            def window = new Window(title: "listbox demo", border: "normal")
            window << {
                listbox {
                    listhead(sizable: true) {
                        listheader(label: "name", sort: "auto")
                        listheader(label: "gender", sort: "auto")
                    }
                    listitem {
                        listcell(label: "Mary")
                        listcell(label: "FEMALE")
                    }
                    listitem {
                        listcell(label: "John")
                        listcell(label: "MALE")
                    }
                    listitem {
                        listcell(label: "Jane")
                        listcell(label: "FEMALE")
                    }
                    listitem {
                        listcell(label: "Henry")
                        listcell(label: "MALE")
                    }
                    listfoot {
                        listfooter {
                            label(value: "This is footer1")
                        }
                        listfooter {
                            label(value: "This is footer2")
                        }
                    }
                }
            }
            [window: window]
        }





    def test =
        {

                def b=Bundle.read(803701)
            
            def o=b.content(0)
            def o2=b.content(1)

            println o
            println o2

        }

    def test2 =
        {
            println 'START BaseContent Indexing'



            int bc = BaseContent.count()

            int loop = Math.floor(bc / 50)
            //int loop2 = Math.round(bc / 50) + 1
            int rest = bc - loop * 50

            println 'Count: ' + bc
            println 'Loop: ' + loop
            println 'Rest: ' + rest

            long time = System.currentTimeMillis()

            for (int i = 0; i <= loop; i++)
            {

                //println loop

                BaseContent.withNewSession {
                    BaseContent.listOrderById(max: i == loop ? rest : 50, offset: 50 * i).each {baseContent ->

                        baseContent.index()

                    }
                }

                println "Time: " + (System.currentTimeMillis() - time) + '  <>  Loop: ' + i + "/" + loop
                time = System.currentTimeMillis()


            }

            /*Bundle.list().each {bundle ->
                Bundle.withNewSession {
                    bundle.index()
                    println bundle

                }
            }*/

            /*Bundle.withNewSession {
                def list = Bundle.listOrderById(max: 100, offset: 0)

                list.each {bundle ->
                    println bundle
                    bundle.index()

                }
            }
            Bundle.withNewSession {
                println 'START Bundle2'

                def list2 = Bundle.listOrderById(max: 100, offset: 100)

                list2.each {bundle ->
                    println bundle
                    bundle.index()

                }
            }*/
            /*Bundle.withNewSession {
                println 'START Bundle3'

                def list3 = Bundle.listOrderById(max: 50, offset: 100)

                list3.each {bundle ->
                    println bundle
                    bundle.index()

                }
            }
            Bundle.withNewSession {
                println 'START Bundle4'

                def list4 = Bundle.listOrderById(max: 50, offset: 150)

                list4.each {bundle ->
                    println bundle
                    bundle.index()

                }
            }*/


        }

    def test3 =
        {
            println 'START Bundle Indexing'



            int bc = Bundle.count()

            int loop = Math.floor(bc / 50)
            //int loop2 = Math.round(bc / 50) + 1
            int rest = bc - loop * 50

            println 'Count: ' + bc
            println 'Loop: ' + loop
            println 'Rest: ' + rest

            long time = System.currentTimeMillis()

            for (int i = 0; i <= loop; i++)
            {

                //println loop

                Bundle.withNewSession {
                    Bundle.listOrderById(max: i == loop ? rest : 50, offset: 50 * i).each {obj ->

                        obj.index()

                    }
                }

                println "Time: " + (System.currentTimeMillis() - time) + '  <>  Loop: ' + i + "/" + loop
                time = System.currentTimeMillis()


            }
        }

    def test4 =
        {
            println 'START Collection  Indexing'

            def c = Collection.class


            int bc = c.count()

            int loop = Math.floor(bc / 50)
            //int loop2 = Math.round(bc / 50) + 1
            int rest = bc - loop * 50

            println 'Count: ' + bc
            println 'Loop: ' + loop
            println 'Rest: ' + rest

            long time = System.currentTimeMillis()

            for (int i = 0; i <= loop; i++)
            {

                //println loop

                Collection.withNewSession {
                    Collection.listOrderById(max: i == loop ? rest : 50, offset: 50 * i).each {obj ->

                        obj.index()

                    }
                }

                println "Time: " + (System.currentTimeMillis() - time) + '  <>  Loop: ' + i + "/" + loop
                time = System.currentTimeMillis()


            }
        }

    def test5 =
        {
            println 'sdfds'
            //gormService.indexAll(Collection.class)
            gormService.indexAll(MetaRecord.class)
            println 'sdfdssadsadsad'
        }

    def test6 =
        {
            searchableService.stopMirroring()

            def workSpace = WorkSpace.list().iterator().next()

            def institution = shiroService.getActiveShiroUser(true).institution

            def account = shiroService.account(true)

            def user = shiroService.getActiveShiroUser(false)


            for (int k = 0; k < 1; k++)
            {
                //   BaseDomain.withNewSession {/*sess->*/

                //BaseDomain.withTransaction {


                Collection collection = new Collection(createdBy: user, title: "collection" + k, ownerWorkSpace: workSpace, description: "set beschrijving " + k, owner: institution).save()
                workSpace.addToCollection(collection)

                //set admin
                ShiroRole collectionAdminRole = testService.createCollectionAdminRole(collection)
                //ShiroUser collectionAdminUser = new ShiroUser(institution: institution, account: account).save()
                user.addToRoles(collectionAdminRole)

                //account.addToShiroUser(collectionAdminUser)


                testService.createCollectionUserAddRole(collection)
                ShiroRole userReadRole = testService.createCollectionUserReadRole(collection)

                institution.group.addToRoles(userReadRole)

                //testService.createAddBundlePermission(collection)
       //         testService.createRolePermission(collection)




                for (int l = 0; l < 1; l++)
                {

                    def time = System.currentTimeMillis()

                    Bundle bundle = new Bundle(createdBy: user, ownerCollection: collection, title: "bundle titel" + "" + k + "" + l, description: " bundle beschrijving" + k + "" + l, owner: institution).save()
                    collection.addToBundle(bundle)


                    //testService.createAddBaseContentPermission(bundle)
              //      testService.createRolePermission(bundle)

                    MetaRecordKey key = MetaRecordKey.findByLabel('title')

                    //println 'bundle ' + l

                    for (int m = 0; m < 10; m++)
                    {
                        BaseContent content = new BaseContent(createdBy: user, title: "content titel" + k + "" + l + "" + m, description: " content beschrijving" + k + "" + l + "" + m, url: """www.content-url${ k + "" + l + "" + m}.nl""", ownerBundle: bundle, owner: institution).save()

                        bundle.addToContent(content)

                        MetaRecord metaRecord = new MetaRecord(createdBy: user, key: key, value: 'titelllll')
                        metaRecord.save()
                        content.addToMetaRecord(metaRecord)

                        StoredFile storedFile = new StoredFile(contentType: 'txt', contentLength: 1024, originalFilename: 'dummyfile', md5: 'testmd5', content: content)

                        storedFile.save()
                        content.storedFile = storedFile

                        // println 'content ' + m

                        content.save()

                        /*content=null
                        storedFile=null
                        metaRecord=null*/

                        /*org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                        sessionn.clear()*/

                        //if (m % 100==0)
                        //{
                        //println 'clean'
                        //           org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                        //           sessionn.clear()

                        /*   content.discard()
                          storedFile.discard()
                        metaRecord.discard()*/
                        //sessionn.

                        //}
                        //testService.createRolePermission(content)


                    }
                    //bundle=null
                    /*bundle.discard()
                    key.discard()*/
                    println 'bundle ' + l + " : " + (System.currentTimeMillis() - time)

                    //         System.gc()
                    //}
                    // }
                }
                //collection.discard()
            }
        }

    def test7 =
        {
            def r = permissionService.workspaceAddCollection(null)
            println r

        }

    def test8 =
        {
            searchableService.stopMirroring()

            def workSpace = WorkSpace.list().iterator().next()

            def institutionID = shiroService.getActiveShiroUser(true).institution.id

            //def accountId = shiroService.account(true)

            def userId = shiroService.getActiveShiroUser(false).id


            for (int k = 0; k < 1; k++)
            {
                //   BaseDomain.withNewSession {/*sess->*/

                //BaseDomain.withTransaction {
                long cid
                Collection collection
                Institution institution
                def user = ShiroUser.get(userId)

                Collection.withTransaction {

                    institution = Institution.get(institutionID)
                    user = ShiroUser.get(userId)

                    collection = new Collection(createdBy: user, title: "collection" + k, ownerWorkSpace: workSpace, description: "set beschrijving " + k, owner: institution).save()
                    cid = collection.id
                    workSpace.addToCollection(collection)

                    //set admin
                    ShiroRole collectionAdminRole = testService.createCollectionAdminRole(collection)
                    user.addToRoles(collectionAdminRole)

                    testService.createCollectionUserAddRole(collection)
                    ShiroRole userReadRole = testService.createCollectionUserReadRole(collection)

                    institution.group.addToRoles(userReadRole)

                }
                collection = null
                institution = null
                user = null

                def time = System.currentTimeMillis()

                for (int l = 0; l < 100; l++)
                {
                    /*org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                    sessionn.clear()*/



                    long bid

                    Collection collection1
                    Bundle bundle
                    Institution institution1
                    def user1

                    Bundle.withTransaction {

                        institution1 = Institution.get(institutionID)
                        user1 = ShiroUser.get(userId)

                        collection1 = Collection.get(cid)

                        bundle = new Bundle(createdBy: user1, ownerCollection: collection1, title: "bundle titel" + "" + k + "" + l, description: " bundle beschrijving" + k + "" + l, owner: institution1).save()
                        bid = bundle.id
                        collection1.addToBundle(bundle)

                        //testService.createAddBaseContentPermission(bundle)
                        //testService.createRolePermission(bundle)

                        //MetaRecordKey key = MetaRecordKey.findByLabel('title')
                    }

                    bundle = null
                    collection1 = null
                    institution1 = null
                    user1 = null

                    //println 'bundle ' + l

                    for (int m = 0; m < 1000; m++)
                    {
                        Bundle bundle1
                        BaseContent content
                        StoredFile storedFile
                        def user2
                        Institution institution2

                        BaseContent.withTransaction {

                            //BaseContent.withTransaction {

                            institution2 = Institution.get(institutionID)
                            user = ShiroUser.get(userId)

                            bundle1 = Bundle.get(bid)

                            content = new BaseContent(createdBy: user2, title: "content titel" + k + "" + l + "" + m, description: " content beschrijving" + k + "" + l + "" + m, url: """www.content-url${ k + "" + l + "" + m}.nl""", ownerBundle: bundle1, owner: institution2).save()

                            bundle1.addToContent(content)

                            /*MetaRecord metaRecord = new MetaRecord(createdBy: user, key: key, value: 'titelllll')
                            metaRecord.save()
                            content.addToMetaRecord(metaRecord)*/

                            storedFile = new StoredFile(contentType: 'txt', contentLength: 1024, originalFilename: 'dummyfile', md5: 'testmd5', content: content)

                            storedFile.save()
                            content.storedFile = storedFile

                            /*println 'content ' + m + " : " + (System.currentTimeMillis() - time)
                          time = System.currentTimeMillis()*/

                            //println 'content ' + m

                            content.save()
                        }

                        bundle1 = null
                        institution2 = null
                        content = null
                        storedFile = null
                        user2 = null
                        //}

                        /*content=null
                        storedFile=null
                        metaRecord=null*/

                        /*org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                        sessionn.clear()*/


                    }
                    //    org.hibernate.classic.Session sessionn = sessionFactory.getCurrentSession()
                    // sessionn.clear()

                    println 'bundle ' + l + " : " + (System.currentTimeMillis() - time)
                    time = System.currentTimeMillis()
                }
                println 'collection ' + l + " : " + (System.currentTimeMillis() - time)
                //collection.discard()
            }
        }



    def poep = {
        println 'fdfds'
        def http = new HTTPBuilder('http://localhost:8080/catchplus/auth/content')
        //def http = new HTTPBuilder('http://localhost:8080/manomano/login/auth')
        def postBody = [bundle: '12'] // will be url-encoded



        http.post(body: postBody/*, requestContentType: URLENC*/) { resp ->
            assert resp.statusLine.statusCode == 302
        }
    }



/*def index = {
    redirect(action: "list", params: params)
}

def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [setInstanceList: Collection.list(params), setInstanceTotal: Collection.count()]
}

def create = {
    def setInstance = new Collection()
    setInstance.properties = params
    return [setInstance: setInstance]
}

def test ={
    def setInstance = new Collection()

    System.out.println(setInstance.metaClass.hasProperty(setInstance ,'owner'));
}

def save = {
    System.out.println('AAP');
    def setInstance = new Collection(params)

    if(setInstance instanceof BaseRepository)
    {
        System.out.println('A');
        setInstance.owner = ShiroUser.findByUsername(SecurityUtils.subject.principal.toString()).institution
    }


    if (setInstance.save(flush: true))
    {
        flash.message = "${message(code: 'default.created.message', args: [message(code: 'set.label', default: 'Collection'), setInstance.id])}"
        redirect(action: "show", id: setInstance.id)
    }
    else
    {
        render(view: "create", model: [setInstance: setInstance])
    }
}

def show = {
    def setInstance = Collection.get(params.id)
    if (!setInstance)
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
        redirect(action: "list")
    }
    else
    {
        [setInstance: setInstance]
    }
}

def edit = {
    def setInstance = Collection.get(params.id)
    if (!setInstance)
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
        redirect(action: "list")
    }
    else
    {
        return [setInstance: setInstance]
    }
}

def update = {
    def setInstance = Collection.get(params.id)
    if (setInstance)
    {
        if (params.version)
        {
            def version = params.version.toLong()
            if (setInstance.version > version)
            {

                setInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'set.label', default: 'Collection')] as Object[], "Another user has updated this Collection while you were editing")
                render(view: "edit", model: [setInstance: setInstance])
                return
            }
        }
        setInstance.properties = params
        if (!setInstance.hasErrors() && setInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'set.label', default: 'Collection'), setInstance.id])}"
            redirect(action: "show", id: setInstance.id)
        }
        else
        {
            render(view: "edit", model: [setInstance: setInstance])
        }
    }
    else
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
        redirect(action: "list")
    }
}

def delete = {
    def setInstance = Collection.get(params.id)
    if (setInstance)
    {
        try
        {
            setInstance.delete(flush: true)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
            redirect(action: "list")
        }
        catch (org.springframework.dao.DataIntegrityViolationException e)
        {
            flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
            redirect(action: "show", id: params.id)
        }
    }
    else
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'set.label', default: 'Collection'), params.id])}"
        redirect(action: "list")
    }
}*/
}

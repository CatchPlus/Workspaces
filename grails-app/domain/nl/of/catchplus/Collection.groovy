package nl.of.catchplus

import org.apache.shiro.SecurityUtils
import grails.util.GrailsNameUtils


class Collection extends BaseRepository
{
    WorkSpace ownerWorkSpace

    //String bundleName

    //DublinCore dublinCore

    //Map metadata

    //static hasMany = []

    static belongsTo = WorkSpace

    static searchable = {
        supportUnmarshall false
    }



    SortedSet bundle

    static hasMany = [workSpace: WorkSpace, unconfirmedWorkSpace: WorkSpace, bundle: Bundle, unconfirmedBundle: Bundle]


    static mappedBy = [bundle: "collection", unconfirmedBundle: 'unconfirmedCollection'/*,workSpace:'collection',unconfirmedWorkSpace:'unconfirmedCollection'*/]

    static mapping = {
        bundle lazy: true
        //workSpace joinTable: [name: "work_space_collection", key: 'collection_id']
        //bundle joinTable: [name: "collection_bundle", key: 'collection_id']

    }




    @Override
    public String label()
    {
        return ownerWorkSpace?.collectionName ? ownerWorkSpace?.collectionName : GrailsNameUtils.getShortName(this.class).toString()
    }

    @Override
    public def insert(boolean flush = false, boolean validate = false)
    {
        this.createdBy = shiroService.getActiveShiroUser(true)
        this.owner = shiroService.getActiveShiroUser(true).institution
        this.ownerWorkSpace = WorkSpace.list().iterator().next()

        gormService.setupCollection(this)

        super.insert()

        Collection.withTransaction {
            this.save()

        }
        this.ownerWorkSpace.addCollection(this)


    }



    @Override
    public ShiroRole administratorRole()
    {
        ShiroRole.findByName("Collection : Administrator : " + this.UUID)
    }

    @Override
    public ShiroRole userAddRole()
    {
        ShiroRole.findByName("Collection : UserAdd : " + this.UUID)
    }

    @Override
    public ShiroRole userReadRole()
    {
        ShiroRole.findByName("Collection : UserRead : " + this.UUID)
    }

/*
    public void addBundle(Bundle bundle)
    {

        String permission = "collection:removeBundle:" + bundle.id
        ShiroPermission shiroPermission = testService.findOrCreateShiroPermission(permission)

        ShiroUser user = (ShiroUser) SecurityUtils.subject.principal
        user.addToPermissions(shiroPermission)

        ShiroRole adminRole = ShiroRole.findByName("Administrator : " + this.owner.name)
        adminRole.addToPermissions(shiroPermission)

        SecurityUtils.getSubject().isPermitted("institution:confirm:" + this.owner.id) ? this.addToBundle(bundle) : this.addToUnconfirmedBundle(bundle)


    }
*/

    @Override
    public Set metaRecordKeys()
    {
        return this.ownerWorkSpace.collectionMetaRecordKey
    }

    public boolean containsBundle(Bundle bundle)
    {
        this.bundle.find {it.id == bundle.id} || this.unconfirmedBundle.find {it.id == bundle.id}
    }


    static constraints = {
        workSpace(widget: 'readonly', nullable: true)
        bundle(nullable: true)
        //bundleName(nullable: true)
        ownerWorkSpace(widget: 'readonly', nullable: true)
        title(nullable: true,unique: true, blank: false, maxSize: 50)

    }

    public void addBundle(Bundle bundle)
    {
        this.addToBundle(bundle)
       /* println 'b: '+bundle.id
        println 'c: '+this.id*/
   //     httpSessionService.getSql().call("INSERT INTO COLLECTION_BUNDLE VALUES ( ${this.id},${bundle.id}) ")
    }

    @Override
    public void treeDelete()
    {
        super.treeDelete()

        def aArray = this.bundle.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            Bundle bundle = aArray[i]
            bundle.ownerCollection == this && bundle.owner == this.owner ? bundle.treeDelete() : this.removeFromBundle(bundle)
        }

        aArray = this.unconfirmedBundle.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            Bundle bundle = aArray[i]
            bundle.ownerCollection == this && bundle.owner == this.owner ? bundle.treeDelete() : this.removeFromUnconfirmedBundle(bundle)
        }

    }

    @Override
    public void myDelete()
    {

        Collection.withTransaction {
            super.myDelete()
            this.treeDelete()

            def aArray = this.workSpace.toArray()

            for (int i = 0; i < aArray.length; i++)
            {
                WorkSpace workSpace = aArray[i]
                workSpace.removeFromCollection(this)
                this.removeFromWorkSpace(workSpace)
            }

            aArray = this.unconfirmedWorkSpace.toArray()

            for (int i = 0; i < aArray.length; i++)
            {
                WorkSpace workSpace = aArray[i]
                workSpace.removeFromUnconfirmedCollection(this)
                this.removeFromUnconfirmedWorkSpace(workSpace)
            }

            this.ownerWorkSpace = null

        }

    }

    public Bundle bundle(int index)
    {
        //TODO Bundle.executeQuery

        def result = Bundle.withCriteria(uniqueResult: true) {
            collection{
                idEq(this.id)
            }
            //eq('ownerCollection', this)
            maxResults 1
            firstResult index
            //order 'title', 'desc'
            cache true

        }
        return result
    }

    public int countBundle()
    {
        return Bundle.createCriteria().get {
            and {
                collection {
                    idEq(this.id)
                }
            }
            projections {
                countDistinct "id"
            }
            cache true
        }
    }

    @Override
    protected void onAfterInsert()
    {
        super.onAfterInsert()
        //ownerWorkSpace.addToCollection(this)
        //  this.addToWorkSpace(ownerWorkSpace: )

        //     collection.iterator().next().

        //collection.add(ownerCollection)
        //System.out.println(collection);
        //System.out.println(collection.class);

        //this.createdIn=this.set
    }

    /*@Override
    protected void onBeforeDelete()
    {
        super.onBeforeDelete()
        System.out.println('A');
        Collection c=this

        this.bundle.each { bundle->
                  System.out.println('B');
            if(bundle.ownerCollection==c)
            {
                bundle.delete()
            }

        }



    }*/



    String toString()
    {
        this.title ? this.title : """${GrailsNameUtils.getShortName(this.class).toString()} : ${id}"""
    }
}

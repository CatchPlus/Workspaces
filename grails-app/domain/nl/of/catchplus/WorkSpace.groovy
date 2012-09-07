package nl.of.catchplus

import org.apache.shiro.SecurityUtils
import grails.util.GrailsNameUtils

class WorkSpace extends BaseDomain
{


    String title
    String description

    def testService

    String collectionName
    String bundleName
    String contentName

    //DublinCore dublinCore

    //Map metadata

    boolean deleted = false


    SortedSet collection

    static hasMany = [collection: Collection, unconfirmedCollection: Collection, collectionMetaRecordKey: MetaRecordKey, bundleMetaRecordKey: MetaRecordKey, baseContentMetaRecordKey: MetaRecordKey]

    /*@XmlIDREF
    SortedSet<Collection>  collection
    @XmlIDREF
    SortedSet<Collection>  unconfirmedCollection*/

    static mappedBy = [unconfirmedCollection: 'unconfirmedWorkSpace', collection: "workSpace"]

    static transients = ['testService']

    /*static mapping = {
        collection lazy: true
        //collection joinTable: [name: "work_space_collection", key: 'workspace_id']

    }*/



    @Override
    public void myDelete()
    {

        super.myDelete()

        def aArray = this.collection.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            Collection collection = aArray[i]
            collection.ownerWorkSpace == this ? collection.treeDelete() : this.removeFromCollection(collection)
        }

        aArray = this.unconfirmedCollection.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            Collection collection = aArray[i]
            collection.ownerWorkSpace == this ? collection.treeDelete() : this.removeFromUnconfirmedCollection(collection)
        }

        /*this.collection.each {collection->
            if(collection.ownerWorkSpace==this)
            {
                collection.treeDelete()
            }
            else
            {
                collection.removeFromWorkSpace(this)
            }
        }*/
        /*this.unconfirmedCollection.each {collection->
            if(collection.ownerWorkSpace==this)
            {
                collection.treeDelete()
            }
            else
            {
                collection.removeFromUnconfirmedWorkSpace(this)
            }
        }*/

        this.collectionName = null
        this.title = null
        this.description = null
        this.bundleName = null
        this.contentName = null
        this.deleted = true

    }

    public String label()
    {
        this.toString()
    }

    public void mySave()
    {
        WorkSpace.withTransaction {
            this.save()
            //testService.createEveryonePermission(this)
        }
        //super.insert()
    }

    public Collection collection(int index)
    {
        def result = Collection.withCriteria(uniqueResult: true) {
            eq('ownerWorkSpace', this)
            maxResults 1
            firstResult index
            //order 'displayDate', 'desc'
            cache true

        }
        return result
    }

    public int countCollection()
    {
        return Collection.createCriteria().get {
            and {
                workSpace {
                    idEq(this.id)
                }
            }
            projections {
                countDistinct "id"
            }
            cache true
        }
    }

    public void addCollection(Collection collection)
    {
        this.addToCollection(collection)
        //SecurityUtils.getSubject().isPermitted("workspace:confirm:" + this.id) ? this.addToCollection(collection) : this.addToUnconfirmedCollection(collection)


    }

    static constraints = {
        collectionName(nullable: true, blank: false)
        bundleName(nullable: true, blank: false)
        contentName(nullable: true, blank: false)
    }

    String toString()
    {
        this.title ? this.title : """${GrailsNameUtils.getShortName(this.class).toString()} : ${id}"""
    }


}

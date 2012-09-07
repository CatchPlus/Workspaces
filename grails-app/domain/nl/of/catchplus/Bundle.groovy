package nl.of.catchplus

import grails.util.GrailsNameUtils
import groovy.sql.Sql

class Bundle extends BaseRepository
{

    Collection ownerCollection





    SortedSet content

    static hasMany = [content: BaseContent, collection: Collection, unconfirmedCollection: Collection, unconfirmedBaseContent: BaseContent]

    static belongsTo = [Collection]

    static searchable = {
        supportUnmarshall false
    }

    //static mappedBy = [content: "bundle", unconfirmedBaseContent: 'unconfirmedBundle', collection: "bundle", unconfirmedCollection: 'unconfirmedBundle']
    static mappedBy = [ collection: "bundle", unconfirmedCollection: 'unconfirmedBundle']

    static constraints = {
        title(nullable: true,unique: true, blank: false, maxSize: 50)
        collection(widget: 'readonly', nullable: true)
        //unconfirmedBaseContent(widget:'readonly',nullable: true)
        content(nullable: true)
        ownerCollection(widget: 'readonly', nullable: true)
    }

    static mapping = {
        content lazy: true
        collection lazy: true


    }

    public boolean containsContent(BaseContent content)
    {
        this.content.find {it.id == content.id} || this.unconfirmedBaseContent.find {it.id == content.id}
    }

    public BaseContent content(int index)
    {
        def result=BaseContent.withCriteria(uniqueResult: true){
            eq('ownerBundle',this)
            maxResults 1
            firstResult index
            cache true
            //order 'displayDate', 'desc'

        }
        return result
    }

    public int countContent()
    {
        return BaseContent.createCriteria().get {

            eq('ownerBundle',this)
            /*and {
                bundle {
                    idEq(this.id)
                }
            }*/
            projections {
                countDistinct "id"
            }
            cache true
        }
    }

    /*public void addContent(BaseContent baseContent)
    {
        //this.addToContent(baseContent)
        httpSessionService.getSql().call("INSERT INTO BUNDLE_CONTENT VALUES ( ${this.id},${baseContent.id}) ")
    }*/

    public int countCollection()
        {
            return Collection.createCriteria().get {
                and {
                    bundle {
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
    public void treeDelete()
    {

        super.treeDelete()

        def aArray = this.content.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            BaseContent content = aArray[i]
            content.ownerBundle == this && content.owner == this.owner ? content.treeDelete() : this.removeFromContent(content)
        }

        aArray = this.unconfirmedBaseContent.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            BaseContent content = aArray[i]
            content.ownerBundle == this && content.owner == this.owner ? content.treeDelete() : this.removeFromUnconfirmedBaseContent(content)
        }

    }

    @Override
    public String label()
    {
        return this.ownerCollection?.ownerWorkSpace?.bundleName ? this.ownerCollection?.ownerWorkSpace?.bundleName : GrailsNameUtils.getShortName(this.class).toString()
    }

    @Override
    public void myDelete()
    {
        Bundle.withTransaction {

            super.myDelete()
            this.treeDelete()



            def aArray = this.collection.toArray()

            for (int i = 0; i < aArray.length; i++)
            {
                Collection collection = aArray[i]
                collection.removeFromBundle(this)
                this.removeFromCollection(collection)
            }

            aArray = this.unconfirmedCollection.toArray()

            for (int i = 0; i < aArray.length; i++)
            {
                Collection collection = aArray[i]
                collection.removeFromUnconfirmedBundle(this)
                this.removeFromUnconfirmedCollection(collection)
            }

            this.ownerCollection = null
        }
    }

    @Override
    public def insert(boolean flush = false, boolean validate = false)
    {
      //  this.createdBy = shiroService.getActiveShiroUser(true)
        this.owner = shiroService.getActiveShiroUser(true).institution

        super.insert()

        Bundle.withTransaction {
            this.save(flush:true)

        }
        this.ownerCollection.addBundle(this)
    }

    @Override
    public Set metaRecordKeys()
    {
        return this.ownerCollection.ownerWorkSpace.bundleMetaRecordKey
    }

    @Override
    public ShiroRole administratorRole()
    {
        ownerCollection?.administratorRole()
    }

    @Override
    public ShiroRole userAddRole()
    {
        ownerCollection.userAddRole()
    }

    @Override
    public ShiroRole userReadRole()
    {
        ownerCollection.userReadRole()
    }

/*
    public void addContent(BaseContent baseContent) {
        SecurityUtils.getSubject().isPermitted("institution:confirm:" + this.owner.id) ? this.addToContent(baseContent) : this.addToUnconfirmedBaseContent(baseContent)
    }
*/

    String toString()
    {
        this.title ? this.title : """${GrailsNameUtils.getShortName(this.class).toString()} : ${id}"""
    }

    @Override
    protected void onAfterInsert()
    {
        super.onAfterInsert()
        //ownerCollection.addToBundle(this)

        //     collection.iterator().next().

        //collection.add(ownerCollection)
        //System.out.println(collection);
        //System.out.println(collection.class);

        //this.createdIn=this.set
    }


}

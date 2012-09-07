package nl.of.catchplus

import grails.util.GrailsNameUtils


class BaseContent extends BaseRepository {

    Bundle ownerBundle

    String url


    def grailsApplication

    //String MD5

    //static searchable = true

    static searchable = {
        supportUnmarshall  false

        //bundle component : true

       // unconfirmedBundle component : true
    }

    static transients = ['fileVaultService'/*,'md5'*/]

    //static hasMany = [bundle: Bundle, unconfirmedBundle: Bundle]

     //Bundle bundle

    static belongsTo = [storedFile:StoredFile]

    static constraints = {
        url(nullable: true, blank: true, maxSize: 100)
      //  bundle(widget: 'readonly', nullable: true)
        //    unconfirmedBundle(widget:'readonly',nullable: true)
        storedFile(nullable: true, blank: true)/*,validator: { val, obj ->
            println 'sdfdsfsf'
            return false//['invalid.collectionmetamecordKey']
        })*/

        ownerBundle(widget: 'readonly', nullable: true)
    }

    //static mappedBy = [bundle: 'content', unconfirmedBundle: 'unconfirmedBaseContent']

    static mapping = {
      //  bundle lazy: true
        //workSpace joinTable: [name: "work_space_collection", key: 'collection_id']
        //    bundle joinTable: [name: "bundle_content", key: 'content_
        //  unconfirmedBundle joinTable:[name:"unconfirmed_bundle_base_content", key:'content_id' ]

    }

    String toString() {
        this.title ? this.title : """${GrailsNameUtils.getShortName(this.class).toString()} : ${id}"""
    }

    /*public String getMD5()
    {
        storedFile ? storedFile.md5 : md5
    }*/



    /*public boolean getPublished() {
        BaseContent.withTransaction {
            return ownerBundle?.published
        }
    }*/

    @Override
    public String label() {
        return this.ownerBundle?.ownerCollection?.ownerWorkSpace?.collectionName ? this.ownerBundle?.ownerCollection?.ownerWorkSpace?.collectionName : GrailsNameUtils.getShortName(this.class).toString()
    }

    @Override
    public void treeDelete() {
        super.treeDelete()
        this.url = null
        this.storedFile?.treeDelete()
        this.storedFile = null
    }

    @Override
    public void myDelete() {
        BaseContent.withTransaction {
            super.myDelete()
            this.treeDelete()

            /*def aArray = this.bundle.toArray()

            for (int i = 0; i < aArray.length; i++) {
                Bundle bundle = aArray[i]
                bundle.removeFromContent(this)
                this.removeFromBundle(bundle)
            }

            aArray = this.unconfirmedBundle.toArray()

            for (int i = 0; i < aArray.length; i++) {
                Bundle bundle = aArray[i]
                bundle.removeFromUnconfirmedBaseContent(this)
                this.removeFromUnconfirmedBundle(bundle)
            }*/

            //bundle.each {bundle->}

            ownerBundle.removeFromContent(this)

            this.ownerBundle = null
        }
        //this.url = null
        //this.storedFile?.delete()
        //this.storedFile = null
    }

    @Override
    public String className()
        {
            return GrailsNameUtils.getShortName(this.class)

        }

    @Override
    public def insert(boolean flush = false,boolean validate = false) {

        //this.createdBy = shiroService.getActiveShiroUser(true)
        this.owner = shiroService.getActiveShiroUser(true).institution

        //super.insert()

        BaseContent.withTransaction {
            this.save()

        }
        //this.ownerBundle.addContent(this)
    }

    public String getUrl() {
        storedFile ? grailsApplication.config.grails.serverURL + "/storedFile/render/" + storedFile.id : this.url
    }

    @Override
    protected void onBeforeInsert() {
        super.onBeforeInsert()
        this.published = ownerBundle ?  ownerBundle.published  : false
    }

/*
    @Override
    protected void onAfterInsert() {
        super.onAfterInsert()
        // if(SecurityUtils.getSubject().isPermitted("institution:confirm:"+ShiroUser.read(SecurityUtils.subject?.principal?.id)?.institution))
        //   System.out.println('OK');
        //ownerBundle.addToContent(this)

        //     collection.iterator().next().

        //collection.add(ownerCollection)
        //System.out.println(collection);
        //System.out.println(collection.class);


        //this.createdIn=this.set
    }
*/

    @Override
    public Set metaRecordKeys() {
        return this.ownerBundle.ownerCollection.ownerWorkSpace.baseContentMetaRecordKey
    }

    @Override
    public ShiroRole administratorRole() {
        ownerBundle?.administratorRole()
    }

    @Override
    public ShiroRole userAddRole() {
        ownerBundle.userAddRole()
    }

    @Override
    public ShiroRole userReadRole() {
        ownerBundle.userReadRole()
    }

    /*@Override
    protected void onBeforeInsert()
    {
        super.onBeforeInsert()

        if(storedFile)
        {
            System.out.println(grailsApplication.config.grails.serverURL+"content/render/"+storedFile.id);
            url=grailsApplication.config.grails.serverURL+"content/render/"+storedFile.id
        }
    }

    @Override
    protected void onBeforeUpdate()
    {
        super.onBeforeUpdate()
        if(storedFile)
        {
            System.out.println(grailsApplication.config.grails.serverURL+"content/render/"+storedFile.id);
            url=grailsApplication.config.grails.serverURL+"content/render/"+storedFile.id
        }
    }*/

    /*public String getUrl()
    {
        if(storedFile)
            return storedFile
    }*/

}

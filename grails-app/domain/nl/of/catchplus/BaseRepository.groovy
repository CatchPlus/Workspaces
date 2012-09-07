package nl.of.catchplus

import grails.plugin.searchable.SearchableService


class BaseRepository extends BaseDomain
{

    def testService
    def gormService

    String title
    String description
    Institution owner

    String UUID = java.util.UUID.randomUUID().toString()

    //SearchableService searchableService

    /*static mapping = {
        tablePerHierarchy false
    }*/

    //static belongsTo = [owner:Institution]

    /*String DCtitle
    String DCcreator
    String DCsubject
    String DCdescription
    String DCpublisher
    String DCcontributor
    String DCdate
    String DCtype
    String DCformat
    String DCidentifier
    String DCsource
    String DClanguage
    String DCrelation
    String DCcoverage
    String DCrights
*/
    //BaseRepositoryStatus status

    boolean published = false

    boolean deleted = false

    static hasMany = [metaRecord: MetaRecord]

    /*static mapping = {
        metaRecord lazy: true

        }*/

    //static hasMany = [repoChange: RepoChange]

    //static belongsTo = [RepoChange]

    static constraints = {
        title(nullable: true, blank: false, maxSize: 50)
        description(nullable: true, blank: false, maxSize: 10000)
        owner(nullable: true)
        UUID(nullable: true, blank: false, unique: true, matches: /[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}/)
        //status(nullable: true,blank: false)

        /*DCtitle(nullable: true)
        DCcreator(nullable: true)
        DCsubject(nullable: true)
        DCdescription(nullable: true)
        DCpublisher(nullable: true)
        DCcontributor(nullable: true)
        DCdate(nullable: true)
        DCtype(nullable: true)
        DCformat(nullable: true)
        DCidentifier(nullable: true)
        DCsource(nullable: true)
        DClanguage(nullable: true)
        DCrelation(nullable: true)
        DCcoverage(nullable: true)
        DCrights(nullable: true)
*/

    }
    //TODO Grails 1.4 : abstract class + method
    @Override
    public def insert()
    {

        /*BaseRepository.withTransaction {
            testService.createRolePermission(this)
        }*/
        return this

    }

    @Override
    public void myDelete()
    {
        super.myDelete()

/*
        Bookmark.findAllByBaseRepository(this).each {  bookmark ->
            bookmark.delete()
        }
*/

        // this.deleted = true

    }

    public Set metaRecordKeys()
    {

    }

    /*public void setUUID(String _dummy)
    {

    }*/


    public void treeDelete()
    {
        this.title = null
        this.description = null
        /*this.DCcontributor = null
        this.DCcoverage = null
        this.DCcreator = null
        this.DCdate = null
        this.DCdescription = null
        this.DCformat = null
        this.DCidentifier = null
        this.DClanguage = null
        this.DCpublisher = null
        this.DCrelation = null
        this.DCrights = null
        this.DCsource = null
        this.DCsubject = null
        this.DCtitle = null
        this.DCtype = null
*/

        deleted = true
    }

    @Override
    protected void onAfterUpdate()
    {


        super.onAfterUpdate()
        this.unindex()

        //searchableService.unindex([this])
//        RepoChange repoChange = new RepoChange(baseRepository: this, changeType: RepoChangeType.UPDATE).save()

    }

    public ShiroRole administratorRole()
    {

    }

    public ShiroRole userAddRole()
    {

    }

    public ShiroRole userReadRole()
    {

    }

    public int countMetaRecord()
    {
        return MetaRecord.createCriteria().get {
            and {
                baseRepository {
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
    int compareTo(that)
    {
        /*//    System.out.println("this: "+this.naam +"    that: "+that.naam);
        if (this.naam.startsWith('Overig'))
            return 1
        else if (that.naam.startsWith('Overig'))
            return -1
        *//*  else if (this.naam.equals(that.naam)) {
                System.out.println("this2: "+this.naam +"    that2: "+that.naam);
            return -1
        }*//*
        else*/

        //System.out.println(this.id+" c :c "+that.id);

        /*  if(!this.title)
            return -1
        else if(!that.title)
            return 1
        else*/
        if (this.equals(that))
            return 0
        else if (!this.title)
            return -1
        else if (!that.title)
            return 1
        else
            return this?.title?.compareTo(that?.title)
    }









    public String label()
    {

    }

    /*@Override
    protected void onAfterInsert()
    {

    }*/
}

/*enum BaseRepositoryStatus {
    PUBLISHED, UNCONFIRMED, CONFIRMED,  DELETED
}*/




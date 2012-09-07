package nl.of.catchplus

import org.apache.shiro.SecurityUtils
import grails.util.GrailsNameUtils


class BaseDomain implements Comparable
{

    def shiroService
    def httpSessionService

    static boolean bootstrap = true
    static boolean logging = false

    //  static auditable = true
    static auditable = [handlersOnly: true]

    Date dateCreated
    Date lastUpdated
    ShiroUser createdBy
    ShiroUser lastModifiedBy

    /*static transients = ['afterDelete']*/


    static mapping = {
        tablePerHierarchy false
    }

    static constraints = {
        lastUpdated(display: false, nullable: true)
        createdBy(display: false, nullable: true)
        lastModifiedBy(display: false, nullable: true)
        dateCreated(display: false, nullable: true)

        // shiroPermission( nullable: true)

    }

    /*void verboseSave() {
        this.validate()
        if (this.hasErrors()) {
            System.out.println(this.errors);
        }
        this.save(flush: true, validate: true)

    }*/

    /*public static void indexXXX()
    {
        println this
    }*/

    def beforeInsert = {
        this.onBeforeInsert()

    }

    def afterInsert = {
        this.onAfterInsert()
    }

    def afterUpdate = {
        this.onAfterUpdate()

    }

    def afterDelete = {
        this.onAfterDelete()
    }

    def beforeDelete = {
        this.onBeforeDelete()
    }

    protected void onAfterDelete()
    {

    }

    protected void onAfterUpdate()
    {

    }

    protected void onBeforeDelete()
    {

    }

    protected void onBeforeInsert()
    {
        dateCreated = new Date()
        //lastModifiedBy=-1
        lastUpdated = null

        /*if(shiroService.isActiveShiroUser())
        {
            println 'Y'
        }
        else*/
        //println 'Z'
        if (!BaseDomain.bootstrap || this.createdBy != null)
            createdBy = createdBy != null ? createdBy : shiroService.getActiveShiroUser(true)

        //createdBy = shiroService.isActiveShiroUser() ? shiroService.activeShiroUserId : this.createdBy
        /*if(SecurityUtils.getSubject()?.principal)
            System.out.println(SecurityUtils.getSubject()?.principal);
        else if(!createdBy)
            System.out.println("NO");*/
    }

    protected void onAfterInsert()
    {

    }

    public String className()
    {
        return GrailsNameUtils.getShortName(this.class)

    }

    def onSave = {
        if (BaseDomain.logging)
            log.info "${this.id} - ${this.className()}  INSERT by: ${org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()?.id}  "
          //  log.info "${this.id} - ${this.className()}  inserted by: ${123}  "
        // may optionally refer to newState map
    }
    def onDelete = {
        if (BaseDomain.logging)
            log.info "${this.id} - ${this.className()}  DELETE by: ${org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()?.id}  "
            //log.info "person was deleted"
        // may optionally refer to oldState map
    }
    def onChange = { oldMap, newMap ->
        if (BaseDomain.logging)
        {
            //log.info "${GrailsNameUtils.getShortName(this.class)}  was changed ..."
            log.info "${this.id} - ${this.className()}  UPDATE by: ${org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()?.id}  "
            oldMap.each({ key, oldVal ->
                if (oldVal != newMap[key])
                {
                    log.info " * $key changed from $oldVal to " + newMap[key]
                }
            })
        }
    }




    def beforeUpdate = {
        this.onBeforeUpdate()
    }

    protected void onBeforeUpdate()
    {
        lastUpdated = new Date()

        /*if (!BaseDomain.bootstrap)
            lastModifiedBy = shiroService.account(true) ? shiroService.getActiveShiroUser(true) : this.lastModifiedBy*/
        //  lastModifiedBy=shiroService.isActiveShiroUser() ? shiroService.activeShiroUserId : this.lastModifiedBy
        //      lastModifiedBy=shiroService.isActiveShiroUser() ? shiroService. : this.lastModifiedBy
    }

    public void myDelete()
    {
        //this.shiroPermission*.myDelete()
        //this.shiroPermission.clear()


    }

    public def insert(boolean flush = false, boolean validate = false)
    {
        //this.createdBy = !createdBy && shiroService.account(true) ? shiroService.getActiveShiroUser(true) : createdBy
        return this.save(flush: flush, validate: validate)
    }

    public def update(boolean flush = false, boolean validate = false)
    {

        this.lastModifiedBy = shiroService.getActiveShiroUser(true)
        return this.save(flush: flush, validate: validate)
    }


    public def store(boolean flush = false, boolean validate = false)
    {

        this.id ? this.update(flush, validate) : this.insert(flush, validate)
    }

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

        return this.id <=> that.id
    }

    /*@Override
    public boolean equals(Object obj)
    {
//    def thatt=obj.class.name
//    def thiss=this.class.name
//    System.out.println("this: "+thiss)
//    System.out.println("that: "+thatt)

        if (obj.id == this.id)
            return true
        else
            return false
    }*/

    /*def getControllerName()
    {
        return StringUtils.uncapitalize(StringUtils.substringAfterLast(this.metaClass.theClass.name, '.'))
    }*/

    /*static def getExlist()
    {
        return new ArrayList()
    }*/

    @Override
    public boolean equals(Object obj)
    {
        //      System.out.println(this.id+" e:e "+obj.id);
        return obj.id == this.id
/*
        if (obj.id == this.id)
            return true
        else
            return false*/
    }
}

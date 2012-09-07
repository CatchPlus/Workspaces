package nl.of.catchplus

import grails.util.GrailsNameUtils

class ShiroRole extends BaseDomain
{

    static String READ_ROLE
    static String ADD_ROLE
    static String ADMIN_ROLE
    static String NO_ROLE

    String name

    //Institution institution

    static hasMany = [groups: ShiroGroup, users: ShiroUser, permissions: ShiroPermission]
    static belongsTo = [ShiroUser, ShiroGroup, ShiroPermission]


    static mapping = {
        users lazy: true
        groups lazy: true
        permissions lazy: true
    }


    static constraints = {
        name(nullable: false, blank: false, unique: true)
        users(nullable: true)
        permissions(nullable: true)
        //    institution(nullable: true)
    }

    public static ShiroRole everyoneRole()
    {
        return ShiroRole.findByName("Iedereen")
    }

    public static ShiroRole superAdminRole()
    {
        return ShiroRole.findByName("Administrator")
    }

    public  static Set institutionAdminRoles()
    {
        return Institution.institutions()*.administratorRole()


    }

    @Override
    protected void onBeforeInsert()
    {
        dateCreated = new Date()
        lastModifiedBy = null
        lastUpdated = null
        createdBy = null
    }

    @Override
    protected void onBeforeUpdate()
    {
        lastUpdated = new Date()
        lastModifiedBy = null
    }

    /*public List getShiroUsers()
    {
        def list=ShiroUser.withCriteria {
            roles{
                idEq(this.id)
            }
        }
        return  list
    }*/

    public String toString()
    {
        // GrailsNameUtils.getShortName(this.class).toString()+ " :  "+this.id
        name
    }
}

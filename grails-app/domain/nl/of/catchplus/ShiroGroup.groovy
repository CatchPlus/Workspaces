package nl.of.catchplus


class ShiroGroup extends BaseDomain
{

    String name

    static hasMany = [users: ShiroUser, roles: ShiroRole, permissions: ShiroPermission]

    static belongsTo = [ShiroPermission]

    static constraints = {

        //resourceType(nullable: true,blank: true)
        //action(nullable: true,blank: true)
        //instanceId(nullable: true,blank: true)
        name(nullable: true, blank: false)

    }

    static mapping = {
        users lazy: true
        roles lazy: true
        permissions lazy: true
    }

    /*static mapping = {
            permissions cache: true
        }
*/

    public static ShiroGroup everyoneGroup()
    {
        return ShiroGroup.findByName("Iedereen")
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

    /*public static ShiroGroup findByInstitution(Institution institution)
    {
        return ShiroGroup.findByName("Group : "+institution.name)
    }*/

    public String toString()
    {
        name
    }


}

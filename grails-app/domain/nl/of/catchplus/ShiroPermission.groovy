package nl.of.catchplus


class ShiroPermission extends BaseDomain
{

    //String resourceType

    //String action

    //String instanceId

    String permission

    boolean deleted = false

    //static transients = ['permission']


    static hasMany = [users: ShiroUser, roles: ShiroRole, groups: ShiroGroup]

    //static belongsTo = [ShiroUser,ShiroRole,ShiroGroup]

    static constraints = {

        //resourceType(nullable: true,blank: true)
        //action(nullable: true,blank: true)
        //instanceId(nullable: true,blank: true)
        permission(nullable: false, blank: false)

    }

    public String toString()
    {
        this.permission //"""${resourceType}:${action}:${instanceId}"""
    }

    /*public void attachToBaseDomain()
    {
        int c = this.permission.count(":")
        if (c == 2)
        {
            String idString = StringUtils.substringAfterLast(this.permission, ":")

            if (NumberUtils.isNumber(idString))
            {
                BaseDomain.get(NumberUtils.toLong(idString)).addToShiroPermission(this)
            }
        }
    }*/


    @Override
    public void myDelete()
    {
        super.myDelete()

        def aArray = this.users.toArray()

        for (int i = 0; i < aArray.length; i++)
        {
            ShiroUser user = aArray[i]
            user.removeFromPermissions(this)
        }

        def aArray2 = this.roles.toArray()

        for (int i = 0; i < aArray2.length; i++)
        {
            ShiroRole role = aArray2[i]
            role.removeFromPermissions(this)
        }

        def aArray3 = this.groups.toArray()

        for (int i = 0; i < aArray3.length; i++)
        {
            ShiroGroup group = aArray3[i]
            group.removeFromPermissions(this)
        }

        this.delete()
    }

    @Override
    protected void onBeforeInsert()
    {
        this.permission = this.permission.toLowerCase()
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


}

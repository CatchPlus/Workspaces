package nl.of.catchplus

class Institution extends BaseDomain
{

    String name

    String shortName

    //String telephoneNumber

    String websiteUrl

    static hasMany = [users: ShiroUser]

    static belongsTo = [ShiroUser]

    ShiroGroup group

    boolean active = true

    /*static  belongsTo = [ShiroUser]*/

    static constraints = {
        //   telephoneNumber(nullable: true, blank: true, maxSize: 255, matches: Constants._REGEX_VALID_TELEFOONUMMER)
        websiteUrl(nullable: true, blank: true)
        name(unique: true, nullable: true, blank: false)
        shortName(maxSize: 15, nullable: true, blank: false)
    }

    public ShiroUser superUser()
    {
        ShiroUser.findByAccountAndInstitution(Account.superAdminAccount(),this)
    }

    public ShiroRole administratorRole()
    {
        ShiroRole.findByName("Institution : Administrator : " + this.id)
    }

    public static List institutions()
    {
        def list = Institution.withCriteria() {
            ne('name', 'Global administrator')
        }
        return list
    }

    public boolean hasAdministrator()
    {
        return !administrators().isEmpty()
    }

    public List administrators()
    {
        def list = ShiroUser.withCriteria() {
            and {
                roles {
                    idEq(this.administratorRole()?.id)
                }
            }
        }

        /*Iterator iter=list.iterator()
        while(iter.hasNext())
        {
            ShiroUser user=iter.next()
            if(user.account.izSuperAdmin())
                iter.remove()
        }*/
        return list

    }

    public List administratorsExSuper()
    {
        def list = this.administrators()

        Iterator iter = list.iterator()
        while (iter.hasNext())
        {
            ShiroUser user = iter.next()
            if (user.account.izSuperAdmin())
                iter.remove()
        }
        return list

    }

    /*public ShiroGroup group() {
        return ShiroGroup.findByName("Group : " + this.name)
    }*/

    public String toString()
    {
        this.shortName
    }

}

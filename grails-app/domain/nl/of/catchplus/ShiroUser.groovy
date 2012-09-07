package nl.of.catchplus

class ShiroUser extends BaseDomain
{
    /*String firstName
    String lastName
    String username
    String password
    Date lastLogOn*/

    /*boolean activated = false

    String activationCode
    String passwordCode*/

    Date lastLogOn

    Institution institution

    Account account

    boolean active = true

    //static transients = ['activeInstitution']

    static hasMany = [groups: ShiroGroup, roles: ShiroRole, permissions: ShiroPermission]

    static belongsTo = [ShiroGroup, ShiroPermission, Account]

    /*static mapping = {
        cache usage:'read-only', include:'all'
        //permissions cache: true
    }*/

    static mapping = {
        roles lazy: true
        groups lazy: true
        permissions lazy: true
    }


    static constraints = {
        /*firstName(nullable: true, blank: false, maxSize: 50)
        lastName(nullable: true, blank: false, maxSize: 50)
        username(nullable: true, blank: false, unique: true, email: true, maxSize: 50)
        activationCode(nullable: true, blank: false, unique: true, maxSize: 5)
        passwordCode(nullable: true, blank: false, unique: true, maxSize: 5)
        password(password: true, nullable: true, blank: false)
        lastLogOn(nullable: true)*/
        //institution(nullable: true, blank: false)
        //groups(nullable: true, blank: false)
    }

    public String toString()
    {
        "${this.account}  : ${this.institution}"
    }

    public static ShiroUser superAdminUser()
    {
        def user = ShiroUser.withCriteria(uniqueResult: true) {
            and {
                roles {
                    idEq(ShiroRole.superAdminRole().id)
                }
            }
        }
        return user
    }

    public boolean hasRole(ShiroRole shiroRole)
    {
        return this.roles.contains(shiroRole) || this.groups.find {group ->
            return group.roles.contains(shiroRole)
        }

    }

    public boolean izInstitutionAdministrator()
    {
        ShiroUser.insitutionAdministrators().contains(this)
    }

    public static List insitutionAdministrators()
    {
        HashSet users = new HashSet()

        Institution.institutions().each {institution ->
            institution.administrators().each {adminUser ->
                users.add(adminUser)
            }
        }
        return users.toList()
    }

    public void printPermissions()
    {
        System.out.println("-----START PERMISSIONS----");
        this.permissions.each {per ->
            System.out.println(per);
        }
        System.out.println("-----START ROLES----");
        this.roles.each {role ->
            role.permissions.each {per ->
                System.out.println(per);
            }
        }
        System.out.println("-----START GROUPS----");
        this.groups.each {group ->
            group.permissions.each {per ->
                System.out.println(per);
            }
            this.roles.each {role ->
                role.permissions.each {per ->
                    System.out.println(per);
                }
            }
        }


    }



    public boolean izSuperAdmin()
    {
        this.id == ShiroUser.superAdminUser().id
    }

/*public Institution activeInstitution() {
    ShiroUser.withTransaction {
        this.activeInstitution ? this.activeInstitution : this.institution?.iterator()?.next()
    }
}

public void setActiveInstitution(Institution institution) {
    this.activeInstitution = institution
}*/

    @Override
    protected void onBeforeInsert()
    {
        super.onBeforeInsert()
        this.addToGroups(ShiroGroup.everyoneGroup())

    }

    static mappedBy = [groups: "users", roles: "users", permissions: 'users']
}

package nl.of.catchplus

import org.apache.shiro.SecurityUtils

class Account extends BaseDomain {
    String firstName
    String lastName
    String username
    String password
    Date lastLogOn

    String language="nl"

    String telephoneNumber

    boolean activated = false

    String activationCode
    String passwordCode



    static hasMany = [shiroUser: ShiroUser]

//    static belongsTo = [ShiroUser]

    static constraints = {
        telephoneNumber(nullable: true, blank: true, maxSize: 255, matches: Constants._REGEX_VALID_TELEFOONUMMER)
        firstName(nullable: true, blank: false, maxSize: 50)
        lastName(nullable: true, blank: false, maxSize: 50)
        username(nullable: true, blank: false, unique: true, email: true, maxSize: 50)
        activationCode(nullable: true, blank: false, unique: true, maxSize: 5)
        passwordCode(nullable: true, blank: false, unique: true, maxSize: 5)
        password(password: true, nullable: true, blank: false)
        lastLogOn(nullable: true)
    }

    static mapping = {
       shiroUser lazy: true
    }

    public String toString() {
        username
    }

    public boolean hasNoActiveShiroUser() {
        //this.refresh()
        !this.shiroUser || this.shiroUser.isEmpty()
    }

    public boolean hasMoreActiveShiroUsers() {
        //this.refresh()

        ShiroUser.countByAccountAndActive(this,true) > 1

        //this.shiroUser?.findAll {it.active}?.size() > 1
    }

    public boolean hasSingleActiveShiroUser() {
        //this.refresh()
        this.shiroUser?.findAll {it.active}?.size() == 1
    }

    public String fullName() {
        "${this.firstName}  ${this.lastName}"
    }

    public static Account superAdminAccount() {
        def account = Account.withCriteria(uniqueResult: true) {
            shiroUser {
                idEq(ShiroUser.superAdminUser().id)
            }
        }

        return account

    }



    public static List insitutionAdministrators()
    {
        HashSet  accounts=new HashSet()

        Institution.institutions().each {institution->
            institution.administrators().each {adminUser->
                accounts.add(adminUser.account)
            }
        }
        return accounts.toList()
    }

    public boolean izSuperAdmin()
    {
        SecurityUtils.subject.isPermitted("*:*")
        //this.id==Account.superAdminAccount().id
    }

    /*@Override
protected void onBeforeInsert() {
super.onBeforeInsert()
this.addToGroups(ShiroGroup.everyoneGroup())

}

static mappedBy = [groups: "users", roles: "users", permissions: 'users' ,institution:'users']*/
}

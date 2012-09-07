import org.apache.shiro.authc.AccountException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.SimpleAccount

import nl.of.catchplus.ShiroUser

import nl.of.catchplus.Account
import nl.of.catchplus.ShiroService
import nl.of.catchplus.EmailService
import org.apache.shiro.authc.NotActivatedAccountException

class ShiroDbRealm
{
    static authTokenClass = org.apache.shiro.authc.UsernamePasswordToken

    def testService

    ShiroService shiroService

    def credentialMatcher
    def shiroPermissionResolver
    //EmailService emailService

    def authenticate(authToken)
    {
        log.info "Attempting to authenticate ${authToken.username} in DB realm..."
        def username = authToken.username

        // Null username is invalid
        if (username == null)
        {
            throw new AccountException("Null usernames are not allowed by this realm.")
        }

        // Get the user with the given username. If the user is not
        // found, then they don't have an account and we throw an
        // exception.
        def user = Account.findByUsername(username)
        if (!user)
        {
            throw new UnknownAccountException("No account found for user [${username}]")
        }

        if (!user.activated)
        {
            //emailService.sendNewUserMail(user)
            throw new NotActivatedAccountException("Account is nog niet geactiveerd, er is nogmaals een activatie mail verstuurd.")
        }

        boolean hasActiveShiroUser = false
        user.shiroUser.each {shiroUser ->
            if (shiroUser.active)
                hasActiveShiroUser = true
        }
        if (!hasActiveShiroUser)
        {
            throw new AccountException("Uw account heeft geen toestemming om aan te melden code 302")
        }

        if (user.izSuperAdmin())
        {
            shiroService.setActiveShiroUser(ShiroUser.superAdminUser())
        }
        else if (user.hasSingleActiveShiroUser())
        {
            shiroService.setActiveShiroUser(user.shiroUser.find {it.active})
        }






        log.info "Found user '${user.username}' in DB"

        // Now check the user's password against the hashed value stored
        // in the database.
        def account = new SimpleAccount(user, user.password, "ShiroDbRealm")
        if (!credentialMatcher.doCredentialsMatch(authToken, account))
        {
            log.info "Invalid password (DB realm)"
            throw new IncorrectCredentialsException("Invalid password for user '${username}'")
        }



        return account
    }

    def hasRole(principal, roleName)
    {

        //Account account = Account.read(principal.id)
        ShiroUser user = shiroService.getActiveShiroUser(true)

        if (!user)
            return false

        def roles = ShiroUser.withCriteria {
            roles {
                eq("name", roleName)
            }
            idEq(user?.id)
        }

        return roles.size() > 0
    }

    def hasAllRoles(principal, roles)
    {

        //Account account = Account.read(principal.id)
        ShiroUser user = shiroService.getActiveShiroUser(true)

        if (!user)
            return false

        def r = ShiroUser.withCriteria {
            roles {
                'in'("name", roles)
            }
            idEq(user?.id)
            //eq("username", principal.username)
        }

        return r.size() == roles.size()
    }


    def isPermitted(principal, requiredPermission)
    {
//return true
//  System.out.println(requiredPermission);

// Does the user have the given permission directly associated
// with himself?
//
// First find all the permissions that the user has that match
// the required permission's type and project code.


        ShiroUser user = shiroService.getActiveShiroUser(true)

        if (!user)
            return false

        //def user = ShiroUser.read((principal as Account).activeShiroUser().id)


        def permissions = user.permissions

        // Try each of the permissions found and see whether any of
        // them confer the required permission.
        def retval = permissions?.find { shiroPermission ->
            // Create a real permission instance from the database
            // permission.
            def perm = shiroPermissionResolver.resolvePermission(shiroPermission.permission)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission))
            {
                // User has the permission!
                return true
            }
            else
            {
                return false
            }
        }

        if (retval != null)
        {
            // Found a matching permission!
            return true
        }

        // If not, does he gain it through a role?
        //
        // Get the permissions from the roles that the user does have.
        //def results = ShiroUser.executeQuery("select distinct p from ShiroUser as user join user.roles as role join role.permissions as p where user.id = '$user.id'",[],[readOnly: true,cache:true])
        def results = ShiroUser.executeQuery("select distinct p from ShiroUser as user join user.roles as role join role.permissions as p where user.id = '$user.id'")

        retval = results.find { shiroPermission ->
            // Create a real permission instance from the database
            // permission.

            def perm = shiroPermissionResolver.resolvePermission(shiroPermission.permission)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission))
            {
                // User has the permission!
                return true
            }
            else
            {
                return false
            }
        }

        if (retval != null)
        {
            // Found a matching permission!
            return true
        }

        // Get the permissions from the groups that the user does have.
        results = ShiroUser.executeQuery("select distinct p from ShiroUser as user join user.groups as groep join groep.permissions as p where user.id = '$user.id'")

        retval = results.find { shiroPermission ->
            // Create a real permission instance from the database
            // permission.

            def perm = shiroPermissionResolver.resolvePermission(shiroPermission.permission)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission))
            {
                // User has the permission!
                return true
            }
            else
            {
                return false
            }
        }

        if (retval != null)
        {
            // Found a matching permission!
            return true
        }

        //For all user.groups
        // Get the permissions from the roles that the group does have.

        results = new ArrayList()

        user.groups.each {group ->
            def temp = ShiroUser.executeQuery("select distinct p from ShiroGroup as groep join groep.roles as role join role.permissions as p where groep.id = '$group.id'")
            results.addAll(temp)
        }
        //System.out.println(results3);


        //results.addAll(results2)
        //results.addAll(results3)

        //System.out.println(results);

        //System.out.println(results2.class);

        // There may be some duplicate entries in the results, but
        // at this stage it is not worth trying to remove them. Now,
        // create a real permission from each result and check it
        // against the required one.
        retval = results.find { shiroPermission ->
            // Create a real permission instance from the database
            // permission.

            def perm = shiroPermissionResolver.resolvePermission(shiroPermission.permission)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission))
            {
                // User has the permission!
                return true
            }
            else
            {
                return false
            }
        }

        if (retval != null)
        {
            // Found a matching permission!
            return true
        }
        else
        {
            return false
        }
    }

}

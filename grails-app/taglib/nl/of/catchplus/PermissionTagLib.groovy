package nl.of.catchplus

class PermissionTagLib {
    static namespace = "cp"

    def permissionService

    def shiroService

    def isAllowedUserManagement = { attrs, body ->

        def collectionList = Collection.list()
        for (int i = 0; i < collectionList.size(); i++) {
            if (permissionService.manageUsers(collectionList.get(i))) {
                out << body()
                break;
            }
        }
    }

    def isAllowedInstitutionAdminManage = { attrs, body ->

        if(shiroService.account(true)?.izSuperAdmin() || shiroService.getActiveShiroUser(true)?.izInstitutionAdministrator())
            out << body()

    }


    def displayUser = { attrs, body ->
        ShiroUser.withTransaction {
            out << shiroService.getActiveShiroUser(true)
        }
    }

    def canSwichInstitution = { attrs, body ->

        if (shiroService.account(true)?.hasMoreActiveShiroUsers()) {
            out << body()
        }
    }
}

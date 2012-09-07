package nl.of.catchplus

import org.apache.shiro.SecurityUtils
import org.springframework.web.context.request.RequestContextHolder

class ShiroService {

    static transactional = true



    def serviceMethod() {

    }

    public Account account(boolean readOnly) {
        readOnly ? Account.read(SecurityUtils.getSubject()?.principal?.id) : Account.get(SecurityUtils.getSubject()?.principal?.id)
    }



    public void setActiveShiroUser(ShiroUser shiroUser) {

        this.session.activeShiroUser = shiroUser.id
    }

    public void setX(ShiroUser shiroUser)
    {
        System.out.println("SDSD");
        this.session.x=shiroUser
    }

    public ShiroUser getX()
    {
        System.out.println('AAAAAP');
        return this.session.x
    }

    public boolean isActiveShiroUser()
    {
        this.session?.activeShiroUser
    }

    public long  getActiveShiroUserId() {
        this.session.activeShiroUser
    }

    public long  getActiveShiroUserId2() {
            this.session.activeShiroUserId2
        }

    public ShiroUser getActiveShiroUser(boolean readOnly = false) {
        this.session.activeShiroUser ? readOnly ? ShiroUser.read(this.session.activeShiroUser) : ShiroUser.get(this.session.activeShiroUser) : null
    }

    public def getSession() {
        def session = null
        try {
            session = RequestContextHolder.currentRequestAttributes().getSession()
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        return session
    }

    public  List ownUsers() {
        /*println getActiveShiroUser(true).institution.administrators()
        println getActiveShiroUser(true).institution.group().users.toList()
        println 'XXX'*/

        ShiroUser user=getActiveShiroUser(true)

        return user.institution.group.users.toList() - user.institution.superUser()   /*- getActiveShiroUser(true).institution.administrators()  //- getActiveShiroUser(true)*/
    }

    public  List otherUsers() {
        return ShiroUser.list() - ownUsers()
    }

    public  List ownAccounts() {
         def accountSet= new HashSet()

         ownUsers().each {user->
             accountSet.add(user.account)
         }
        return accountSet.toList()

    }

    public  List otherAccounts() {
        return Account.list() - ownAccounts()
    }

    public List collectionBookmarks() {
        def bookmarks = Bookmark.findAllByShiroUser(this.getActiveShiroUser(true))

        Iterator iter = bookmarks.iterator()
        while (iter.hasNext()) {
            def obj = iter.next()
            if (!(obj.baseRepository instanceof Collection)) {
                iter.remove()
            }
        }
        return bookmarks
    }

    public List bundleBookmarks() {
        def bookmarks = Bookmark.findAllByShiroUser(this.getActiveShiroUser(true))

        Iterator iter = bookmarks.iterator()
        while (iter.hasNext()) {
            def obj = iter.next()
            if (!(obj.baseRepository instanceof Bundle)) {
                iter.remove()

            }
        }
        return bookmarks

    }

    public List contentBookmarks() {
        def bookmarks = Bookmark.findAllByShiroUser(this.getActiveShiroUser(true))

        Iterator iter = bookmarks.iterator()
        while (iter.hasNext()) {
            def obj = iter.next()
            if (!(obj.baseRepository instanceof BaseContent)) {
                iter.remove()

            }
        }
        return bookmarks


    }







}

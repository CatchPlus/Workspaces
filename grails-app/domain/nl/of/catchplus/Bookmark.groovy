package nl.of.catchplus

class Bookmark extends BaseDomain{

    String description
    BaseRepository baseRepository
    ShiroUser shiroUser


    static constraints = {
        description(nullable: true, blank: true, maxSize: 1000)
    }

    public String toString()
    {
        return baseRepository.toString()
    }

}

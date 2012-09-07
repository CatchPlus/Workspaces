package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.grails.jaxrs.provider.UnAuthorizedException

class StoredFileResourceService
{

    def permissionService

    def create(StoredFile dto)
    {
        dto.merge() // workaround for http://jira.grails.org/browse/GRAILS-6459
        dto.save()
    }

    def read(def id)
    {

        def obj = StoredFile.read(id)
        if (!obj)
        {
            throw new DomainObjectNotFoundException(StoredFile.class, id)
        }

        if (!permissionService.baseContentRead(obj.content))
        {
            throw new UnAuthorizedException(StoredFile.class, id)
        }

        obj

        /*def obj = StoredFile.get(id)
        if (!obj)
        {
            throw new DomainObjectNotFoundException(StoredFile.class, id)
        }
        obj*/
    }

    /*def readAll()
    {
        StoredFile.findAll()
    }*/

    /*def update(StoredFile dto)
    {
        def obj = StoredFile.get(dto.id)
        if (!obj)
        {
            throw new DomainObjectNotFoundException(StoredFile.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(def id)
    {
        def obj = StoredFile.get(id)
        if (obj)
        {
            obj.delete()
        }
    }*/

}


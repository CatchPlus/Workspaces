package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.grails.jaxrs.provider.UnAuthorizedException
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.grails.jaxrs.provider.DomainInstanceHasErrorsException

class BundleResourceService {

    def permissionService

    def create(Bundle dto) {
        /*println 'dffdf'
        def authToken = new UsernamePasswordToken('instituut01@admin.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)*/
        dto.title = dto.title ? dto.title : ""
        dto.description = dto.description ? dto.description : ""


        if (Collection.countById(dto.ownerCollection.id)==0) {
            throw new DomainObjectNotFoundException(Collection.class, dto.ownerCollection.id)
        }

        if (!permissionService.collectionAddBundle(dto.ownerCollection)) {
            throw new UnAuthorizedException(Collection.class, dto?.ownerCollection?.id)
        }

        dto.merge()

        if(!dto.validate())
        {
            throw new DomainInstanceHasErrorsException(Bundle.class, dto.retrieveErrors())
        }

        dto.store()

        return dto
    }


    def read(def id) {
        def obj = Bundle.read(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Bundle.class, id)
        }

        if (!permissionService.bundleRead(obj)) {
            throw new UnAuthorizedException(Bundle.class, id)
        }

        obj
    }

    def readAll() {
        permissionService.bundleListRead()
    }

    def update(Bundle dto) {


        def obj = Bundle.findByIdAndDeleted(dto.id,false)
        if (!obj) {
            throw new DomainObjectNotFoundException(Bundle.class, dto.id)
        }

        if (!permissionService.bundleEdit(obj)) {
            throw new UnAuthorizedException(Bundle.class, obj.id)
        }

        obj.properties['title', 'description'] = dto.properties

        obj.title = obj.title ? obj.title : ""
        obj.description = obj.description ? obj.description : ""

        if(!obj.validate())
        {
            throw new DomainInstanceHasErrorsException(Bundle.class, obj.retrieveErrors())
        }

        obj.store()

        obj
    }

    void delete(def id) {
        def obj = Bundle.findByIdAndDeleted(id,false)

        if (!obj) {
            throw new DomainObjectNotFoundException(Bundle.class, id)
        }

        if (!permissionService.bundleDelete(obj)) {
            throw new UnAuthorizedException(Bundle.class, id)
        }

        obj.myDelete()



    }
}


package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import org.grails.jaxrs.provider.UnAuthorizedException
import org.grails.jaxrs.provider.DomainInstanceHasErrorsException

class CollectionResourceService {

    static transactional = false

    def permissionService

    def create(Collection dto) {
        /*println 'sadsadsadad'
        def authToken = new UsernamePasswordToken('instituut01@admin.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)*/

        dto.title = dto.title ? dto.title : ""
        dto.description = dto.description ? dto.description : ""
     //   dto.ownerWorkSpace = WorkSpace.list().iterator().next()

        if (!permissionService.workspaceAddCollection(WorkSpace.list().iterator().next())) {
            throw new UnAuthorizedException(WorkSpace.class, WorkSpace.list().iterator().next().id)
        }

        //dto.merge()

        if(!dto.validate())
        {
            throw new DomainInstanceHasErrorsException(Collection.class, dto.retrieveErrors())
        }

        dto.store()

        return dto
    }


    def read(def id) {
        def obj = Collection.read(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Collection.class, id)
        }

        if (!permissionService.collectionRead(obj)) {
            throw new UnAuthorizedException(Collection.class, id)
        }

        obj
    }

    def readAll() {
        permissionService.collectionListRead()
    }

    def update(Collection dto) {


        def obj = Collection.findByIdAndDeleted(dto.id,false)
        if (!obj) {
            throw new DomainObjectNotFoundException(Collection.class, dto.id)
        }

        if (!permissionService.collectionEdit(obj)) {
            throw new UnAuthorizedException(Collection.class, obj.id)
        }

        obj.properties['title', 'description'] = dto.properties

        obj.title = obj.title ? obj.title : ""
        obj.description = obj.description ? obj.description : ""

        if(!obj.validate())
        {
            throw new DomainInstanceHasErrorsException(Collection.class, obj.retrieveErrors())
        }

        obj.store()

        obj
    }

    void delete(def id) {
        def obj = Collection.findByIdAndDeleted(id,false)

        if (!obj) {
            throw new DomainObjectNotFoundException(Collection.class, id)
        }

        if (!permissionService.collectionDelete(obj)) {
            throw new UnAuthorizedException(Collection.class, id)
        }

        obj.myDelete()

        //obj

    }
}

    


package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class WorkSpaceResourceService {

    def create(WorkSpace dto) {
        dto.merge() // workaround for http://jira.grails.org/browse/GRAILS-6459
        dto.save()
    }

    def read(def id) {
        def obj = WorkSpace.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(WorkSpace.class, id)
        }
        obj
    }

    def readAll() {
        WorkSpace.findAll()
    }

    def update(WorkSpace dto) {
        def obj = WorkSpace.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(WorkSpace.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(def id) {
        def obj = WorkSpace.get(id)
        if (obj) {
            obj.delete()
        }
    }

}


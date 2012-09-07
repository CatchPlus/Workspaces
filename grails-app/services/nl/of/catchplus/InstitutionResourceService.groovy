package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class InstitutionResourceService {

    def create(Institution dto) {
        dto.merge() // workaround for http://jira.grails.org/browse/GRAILS-6459
        dto.save()
    }

    def read(def id) {
        def obj = Institution.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Institution.class, id)
        }
        obj
    }

    def readAll() {
        Institution.findAll()
    }

    def update(Institution dto) {
        def obj = Institution.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Institution.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(def id) {
        def obj = Institution.get(id)
        if (obj) {
            obj.delete()
        }
    }

}


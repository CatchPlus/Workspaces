package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class MetaRecordKeyResourceService {

    def create(MetaRecordKey dto) {
        dto.merge() // workaround for http://jira.grails.org/browse/GRAILS-6459
        dto.save()
    }

    def read(def id) {
        def obj = MetaRecordKey.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MetaRecordKey.class, id)
        }
        obj
    }

    def readAll() {
        MetaRecordKey.findAll()
    }

    def update(MetaRecordKey dto) {
        def obj = MetaRecordKey.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MetaRecordKey.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(def id) {
        def obj = MetaRecordKey.get(id)
        if (obj) {
            obj.delete()
        }
    }

}


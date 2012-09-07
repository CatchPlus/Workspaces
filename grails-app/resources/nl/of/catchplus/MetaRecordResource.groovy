package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class MetaRecordResource {

    def metaRecordResourceService
    def id

    @GET
    Response read() {
        ok metaRecordResourceService.read(id)
    }

    @PUT
    Response update(MetaRecord dto) {
        dto.id = id
        ok metaRecordResourceService.update(dto)
    }

    @DELETE
    void delete() {
        metaRecordResourceService.delete(id)
    }

}


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
class BaseContentResource {

    def baseContentResourceService
    def id

    @GET
    Response read() {
        ok baseContentResourceService.read(id)
    }

    @PUT
    Response update(BaseContent dto) {
        dto.id = id
        ok baseContentResourceService.update(dto)
    }

    @DELETE
    void delete() {
        baseContentResourceService.delete(id)
    }

}


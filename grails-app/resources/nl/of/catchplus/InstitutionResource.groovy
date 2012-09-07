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
class InstitutionResource {

    def institutionResourceService
    def id

    @GET
    Response read() {
        ok institutionResourceService.read(id)
    }

    /*@PUT
    Response update(Institution dto) {
        dto.id = id
        ok institutionResourceService.update(dto)
    }

    @DELETE
    void delete() {
        institutionResourceService.delete(id)
    }*/

}


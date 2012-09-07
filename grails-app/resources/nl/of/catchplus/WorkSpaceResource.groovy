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
class WorkSpaceResource {

    def workSpaceResourceService
    def id

    @GET
    Response read() {
        ok workSpaceResourceService.read(id)
    }

    /*@PUT
    Response update(WorkSpace dto) {
        dto.id = id
        ok workSpaceResourceService.update(dto)
    }

    @DELETE
    void delete() {
        workSpaceResourceService.delete(id)
    }*/

}


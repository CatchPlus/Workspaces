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
class StoredFileResource
{

    def storedFileResourceService
    def id

    @GET
    Response read()
    {
        ok storedFileResourceService.read(id)
    }

    /*@PUT
    Response update(StoredFile dto)
    {
        dto.id = id
        ok storedFileResourceService.update(dto)
    }

    @DELETE
    void delete()
    {
        storedFileResourceService.delete(id)
    }*/

}


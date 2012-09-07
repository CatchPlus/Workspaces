package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/storedFile')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class StoredFileCollectionResource
{

    def storedFileResourceService

    /*@POST
    Response create(StoredFile dto)
    {
        created storedFileResourceService.create(dto)
    }*/

    /*@GET
    Response readAll()
    {
        ok storedFileResourceService.readAll()
    }*/

    @Path('/{id}')
    StoredFileResource getResource(@PathParam('id') Long id)
    {
        new StoredFileResource(storedFileResourceService: storedFileResourceService, id: id)
    }

}

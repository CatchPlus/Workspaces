package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/collection')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class CollectionCollectionResource {

    def collectionResourceService
    
    @POST
    Response create(Collection dto) {
        created collectionResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok collectionResourceService.readAll()
    }
    
    @Path('/{id}')
    CollectionResource getResource(@PathParam('id') Long id) {
        new CollectionResource(collectionResourceService: collectionResourceService, id:id)
    }
        
}

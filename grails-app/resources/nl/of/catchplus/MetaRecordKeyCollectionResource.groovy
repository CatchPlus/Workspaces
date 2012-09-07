package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/metaRecordKey')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class MetaRecordKeyCollectionResource {

    def metaRecordKeyResourceService

    /*@POST
    Response create(MetaRecordKey dto) {
        created metaRecordKeyResourceService.create(dto)
    }*/

    @GET
    Response readAll() {
        ok metaRecordKeyResourceService.readAll()
    }

    @Path('/{id}')
    MetaRecordKeyResource getResource(@PathParam('id') Long id) {
        new MetaRecordKeyResource(metaRecordKeyResourceService: metaRecordKeyResourceService, id: id)
    }

}

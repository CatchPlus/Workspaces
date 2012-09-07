package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/metaRecord')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class MetaRecordCollectionResource {

    def metaRecordResourceService

    @POST
    Response create(MetaRecord dto) {
        created metaRecordResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok metaRecordResourceService.readAll()
    }

    @Path('/{id}')
    MetaRecordResource getResource(@PathParam('id') Long id) {
        new MetaRecordResource(metaRecordResourceService: metaRecordResourceService, id: id)
    }

}

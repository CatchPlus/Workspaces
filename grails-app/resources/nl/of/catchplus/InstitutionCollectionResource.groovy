package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/institution')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class InstitutionCollectionResource {

    def institutionResourceService

    /*@POST
    Response create(Institution dto) {
        created institutionResourceService.create(dto)
    }*/

    @GET
    Response readAll() {
        ok institutionResourceService.readAll()
    }

    @Path('/{id}')
    InstitutionResource getResource(@PathParam('id') Long id) {
        new InstitutionResource(institutionResourceService: institutionResourceService, id: id)
    }

}

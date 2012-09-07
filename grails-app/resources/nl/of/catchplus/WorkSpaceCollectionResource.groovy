package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/workSpace')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class WorkSpaceCollectionResource {

    def workSpaceResourceService

    /*@POST
    Response create(WorkSpace dto) {
        created workSpaceResourceService.create(dto)
    }*/

    @GET
    Response readAll() {
        ok workSpaceResourceService.readAll()
    }

    @Path('/{id}')
    WorkSpaceResource getResource(@PathParam('id') Long id) {
        new WorkSpaceResource(workSpaceResourceService: workSpaceResourceService, id: id)
    }

}

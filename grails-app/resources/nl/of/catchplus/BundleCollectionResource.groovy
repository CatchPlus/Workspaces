package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import javax.ws.rs.core.Response.ResponseBuilder
import javax.ws.rs.core.Response.StatusType
import javax.ws.rs.core.Response.Status

@Path('/api/bundle')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BundleCollectionResource {

    def bundleResourceService
    def testService
    def shiroService

    @POST
    Response create(Bundle dto) {
        created bundleResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok bundleResourceService.readAll()
    }

    @Path('/{id}')
    BundleResource getResource(@PathParam('id') Long id) {
        new BundleResource(bundleResourceService: bundleResourceService, id: id)
    }

}

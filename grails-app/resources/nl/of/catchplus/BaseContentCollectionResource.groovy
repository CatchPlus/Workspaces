package nl.of.catchplus

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

import com.sun.jersey.multipart.MultiPart
import javax.ws.rs.core.UriInfo
import javax.ws.rs.core.Context
import javax.ws.rs.core.MultivaluedMap
import org.restlet.representation.Representation
import com.sun.jersey.multipart.BodyPartEntity
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import javax.ws.rs.core.MediaType
import com.sun.jersey.multipart.FormDataParam
import com.sun.jersey.core.header.FormDataContentDisposition
import org.apache.commons.io.FileUtils
import javax.servlet.http.HttpServletRequest
import org.springframework.web.multipart.MultipartHttpServletRequest
import com.sun.jersey.multipart.FormDataBodyPart

@Path('/api/baseContent')
//@Consumes(['multipart/mixed'])
//@Consumes(['application/xml', 'application/json', 'multipart/mixed', 'multipart/form-data'])
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BaseContentCollectionResource extends  BaseResource
{
//    TestService testService

    def baseContentResourceService

    def grailsApplication


/*
    @POST
    public Response  create(@Context HttpServletRequest request,@FormDataParam("attachment_fieldd") FormDataBodyPart p
) {
        System.out.println(p);

        MultipartHttpServletRequest r= (MultipartHttpServletRequest)request
        System.out.println(r);
        System.out.println(r.class);


    }
*/
      @Consumes(['multipart/form-data'])
     @POST
    public Response create(
            @FormDataParam("xml") String xml,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("file") byte[] content
          /*  @FormDataParam("attachment_fieldd") InputStream uploadedInputStream*/
    )
    {
    /*    System.out.println("GOGOGO");
        System.out.println(fileDetail.fileName);
        def mimeType= URLConnection.guessContentTypeFromName(fileDetail.name);
        System.out.println(mimeType);

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
String mimeType2 = fileNameMap.getContentTypeFor(fileDetail.fileName);
              System.out.println(mimeType2);



*//*System.out.println(uploadedInputStream.);

        System.out.println(formDataContentDisposition.parameters.size());
        System.out.println(formDataContentDisposition.parameters);
*/
        created baseContentResourceService.create(xml,fileDetail?.fileName ,content)


    }


    @GET
    Response readAll() {
        ok baseContentResourceService.readAll()
    }

    @Path('/{id}')
    BaseContentResource getResource(@PathParam('id') Long id) {
        new BaseContentResource(baseContentResourceService: baseContentResourceService, id: id)
    }

}
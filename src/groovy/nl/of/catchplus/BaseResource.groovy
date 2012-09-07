package nl.of.catchplus

import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by IntelliJ IDEA.
 * User: Michaell
 * Date: 30-11-11
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
class BaseResource {


    PermissionService permissionService

    public  static Response UnAuthorized(def clazz, def id) {

        Response.status(Status.UNAUTHORIZED).entity(UnAuthorizedMessage(clazz, id)).type(MediaType.APPLICATION_XML).build()
    }

    public  static String UnAuthorizedMessage(def clazz, def id) {
        "<error>UnAuthorized action for ${clazz.simpleName} with id $id </error>"
    }

}

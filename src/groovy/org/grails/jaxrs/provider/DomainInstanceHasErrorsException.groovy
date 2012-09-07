/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.jaxrs.provider

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import org.springframework.validation.Errors
import org.apache.commons.lang.StringUtils
import grails.util.GrailsNameUtils
import nl.of.catchplus.CatchplusUtils

/**
 * Thrown to create a 404 response containing an XML error message. 
 * 
 * @author Martin Krasser
 */
class DomainInstanceHasErrorsException extends WebApplicationException {

    def messageSource

    /**
     * @param clazz Grails domain object clazz.
     * @param id Grails domain object id.
     */
    DomainInstanceHasErrorsException(def clazz, def errors) {
        super(hasErrors(clazz, errors))
    }

    /**
     * Creates a NOT FOUND response (status code 404) and response entity 
     * containing an error message including the id and clazz of the requested
     * resource.
     * 
     * @param clazz Grails domain object clazz.
     * @param id Grails domain object id.
     * @return JAX-RS response.
     */
    /*private static Response notFound(def clazz, def id) {
        Response.status(NOT_FOUND).entity(notFoundMessage(clazz, id)).type(APPLICATION_XML).build()
    }

    private static String notFoundMessage(def clazz, def id) {
        "<error>${clazz.simpleName} with id $id not found</error>"
    }*/

    private  static Response hasErrors(def clazz, def  errors) {

        Response.status(Status.BAD_REQUEST).entity(hasErrorsMessage(clazz, errors)).type(MediaType.APPLICATION_XML).build()
    }

    private  static String hasErrorsMessage(def clazz, def errors) {
        def sb=new StringBuilder()

        String name=StringUtils.uncapitalise( GrailsNameUtils.getShortName(clazz).toString())

        //sb.append("<${name}>")
        errors.each {error->
            sb.append('<error>')
            sb.append(error)
                   /*  System.out.println(mess);
            sb.append(messageSource.getMessage(error, null))
            System.out.println(messageSource.getMessage(error null));
            System.out.println(error.code);
            System.out.println(error.value);
            System.out.println(CatchplusUtils.getApplicationTagLib().message(code:error.code,arg:error.arguments));*/
            sb.append('</error>')
        }
        //sb.append("</${name}>")
        return sb.toString()

    }
    
}

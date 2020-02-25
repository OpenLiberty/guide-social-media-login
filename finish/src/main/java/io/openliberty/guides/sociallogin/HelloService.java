// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
// tag::newImports[]
// tag::rolesAllowedImport[]
import javax.annotation.security.RolesAllowed;
// end::rolesAllowedImport[]
// tag::httpServletRequestContextImport[]
// tag::httpServletRequestImport[]
import javax.servlet.http.HttpServletRequest;
// end::httpServletRequestImport[]
// tag::contextImport[]
import javax.ws.rs.core.Context;
// end::contextImport[]
// end::httpServletRequestContextImport[]
// end::newImports[]

@Path("hello")
// tag::helloService[]
public class HelloService {

    // tag::httpServletRequestContext[]
    // tag::contextAnnotation[]
    @Context
    // end::contextAnnotation[]
    HttpServletRequest request;
    // end::httpServletRequestContext[]

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    // tag::rolesAllowed[]
    @RolesAllowed({"users"})
    // end::rolesAllowed[]
    // tag::userPrincipal[]
    public String greet() {
        if (request.getUserPrincipal() == null) return "Hello, friend!";
        return "Hello, "
        		+ request.getUserPrincipal().getName() 
        		+ '\n' + request.getUserPrincipal().toString();
    }
    // end::userPrincipal[]
}
// end::helloService[]

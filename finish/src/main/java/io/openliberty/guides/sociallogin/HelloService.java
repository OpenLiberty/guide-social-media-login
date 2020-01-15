// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
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

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("hello")
public class HelloService {

    // tag::httpServletRequestContext[]
    @Context
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

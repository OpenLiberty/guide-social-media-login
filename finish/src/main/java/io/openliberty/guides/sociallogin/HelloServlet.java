// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
// tag::ServletSecurityImport[]
// tag::HttpConstraintImport[]
import javax.servlet.annotation.HttpConstraint;
// end::HttpConstraintImport[]
import javax.servlet.annotation.ServletSecurity;
// end::ServletSecurityImport[]
// tag::WebServletImport[]
import javax.servlet.annotation.WebServlet;
// end::WebServletImport[]
// tag::HttpServletImport[]
import javax.servlet.http.HttpServlet;
// end::HttpServletImport[]
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// tag::WebServlet[]
@WebServlet(name = "HelloServlet", urlPatterns = "/hello")
// end::WebServlet[]
// tag::ServletSecurity[]
@ServletSecurity(
        // tag::HttpConstraint[]
        value = @HttpConstraint(
                // tag::rolesAllowed[]
                rolesAllowed = {"users"},
                // end::rolesAllowed[]
                // tag::transportGuarantee[]
                transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL
                // end::transportGuarantee[]
        )
        // end::HttpConstraint[]
)
// end::ServletSecurity[]
// tag::HelloServlet[]
// tag::HttpServlet[]
public class HelloServlet extends HttpServlet {
// end::HttpServlet[]

    // tag::doGet[]
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // tag::getUsername[]
        String username = request.getUserPrincipal().getName();
        request.setAttribute("username", username);
        // end::getUsername[]

        // tag::forwardRequest[]
        request
                .getRequestDispatcher("securedHello.jsp")
                .forward(request,response);
        // end::forwardRequest[]
    }
    // end::doGet[]
}
// end::HelloServlet[]

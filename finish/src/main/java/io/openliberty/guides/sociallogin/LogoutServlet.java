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

import com.ibm.websphere.security.social.UserProfileManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
// tag::WebServlet[]
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
// end::WebServlet[]
// tag::ServletSecurity[]
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
// end::ServletSecurity[]
// tag::LogoutServlet[]
public class LogoutServlet extends HttpServlet {

    // tag::clientId[]
    @Inject
    // tag::propClientId[]
    @ConfigProperty(name = "github.client.id")
    // end::propClientId[]
    private String clientId;
    // end::clientId[]

    // tag::clientSecret[]
    @Inject
    // tag::propClientSecret[]
    @ConfigProperty(name = "github.client.secret")
    // end::propClientSecret[]
    private String clientSecret;
    // end::clientSecret[]

    // tag::handlePost[]
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // tag::unauthorizeUrl[]
        final String unauthorizeUrl = "https://api.github.com/" +
                "applications/{client_id}/grant";
        // end::unauthorizeUrl[]

        // Get access token
        // tag::accessToken[]
        String accessToken = UserProfileManager
                .getUserProfile()
                .getAccessToken();
        // end::accessToken[]

        // tag::requestBody[]
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", accessToken);
        // end::requestBody[]

        // tag::encodeAuth[]
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuthStream = Base64
                .getEncoder()
                .encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String encodedAuth = new String(encodedAuthStream);
        // end::encodeAuth[]

        // Add headers and body to DELETE request to delete OAuth2 grant
        // tag::revokePermissions[]
        Response logoutResponse = ClientBuilder
                .newClient()
                .target(unauthorizeUrl)
                .resolveTemplate("client_id", clientId)
                .request()
                // tag::authHeader[]
                .header("Authorization", "Basic " + encodedAuth)
                // end::authHeader[]
                // tag::accessTokenBody[]
                .method("DELETE", Entity.json(requestBody));
                // end::accessTokenBody[]

        if (logoutResponse.getStatus() != 204) {
            throw new ServletException("Could not delete OAuth2 application grant");
        }
        // end::revokePermissions[]

        // tag::logout[]
        request.logout();
        // end::logout[]
        // tag::redirect[]
        response.sendRedirect("hello.html");
        // end::redirect[]
    }
    // end::handlePost[]
}
// end::LogoutServlet[]

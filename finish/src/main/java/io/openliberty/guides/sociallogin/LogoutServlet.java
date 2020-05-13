package io.openliberty.guides.sociallogin;

import com.ibm.websphere.security.social.UserProfileManager;

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

// tag::WebServlet[]
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
// end::WebServlet[]
// tag::ServletSecurity[]
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
// end::ServletSecurity[]
// tag::LogoutServlet[]
public class LogoutServlet extends HttpServlet {

    // tag::handlePost[]
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // GitHub endpoint and required credentials
        // tag::GitHubInfo[]
        // tag::clientId[]
        final String clientId = "[github-client-id]";
        // end::clientId[]
        // tag::clientSecret[]
        final String clientSecret = "[github-client-secret]";
        // end::clientSecret[]
        // tag::unauthorizeUrl[]
        final String unauthorizeUrl = "https://api.github.com/" +
                "applications/{client_id}/grant";
        // end::unauthorizeUrl[]
        // end::GitHubInfo[]

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
                // tag::target[]
                .target(unauthorizeUrl)
                .resolveTemplate("client_id", clientId)
                // end::target[]
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

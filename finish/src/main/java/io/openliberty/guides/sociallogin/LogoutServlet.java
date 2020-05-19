package io.openliberty.guides.sociallogin;

import com.ibm.websphere.security.social.UserProfileManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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

// tag::WebServlet[]
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
// end::WebServlet[]
// tag::ServletSecurity[]
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
// end::ServletSecurity[]
// tag::LogoutServlet[]
public class LogoutServlet extends HttpServlet {

    @Inject
    @ConfigProperty(name = "github.client.id")
    private String clientId;

    @Inject
    @ConfigProperty(name = "github.client.secret")
    private String clientSecret;

    // tag::handlePost[]
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        final String unauthorizeUrl = "https://api.github.com/" +
                "applications/{client_id}/grant";

        // Get access token
        String accessToken = UserProfileManager
                .getUserProfile()
                .getAccessToken();


        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", accessToken);

        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuthStream = Base64
                .getEncoder()
                .encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String encodedAuth = new String(encodedAuthStream);

        // Add headers and body to DELETE request to delete OAuth2 grant
        Response logoutResponse = ClientBuilder
                .newClient()
                .target(unauthorizeUrl)
                .resolveTemplate("client_id", clientId)
                .request()
                .header("Authorization", "Basic " + encodedAuth)
                .method("DELETE", Entity.json(requestBody));

        if (logoutResponse.getStatus() != 204) {
            throw new ServletException("Could not delete OAuth2 application grant");
        }

        request.logout();
        response.sendRedirect("hello.html");
    }
    // end::handlePost[]
}
// end::LogoutServlet[]

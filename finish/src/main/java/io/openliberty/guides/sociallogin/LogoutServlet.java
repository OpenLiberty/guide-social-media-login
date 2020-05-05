package io.openliberty.guides.sociallogin;

import com.ibm.websphere.security.social.UserProfile;
import com.ibm.websphere.security.social.UserProfileManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Required values
        final String clientId = "3e65aff2ddd6425d6f4f";
        final String clientSecret = "2a82e498143453a2763ecb97665371e51c34befd";
        final String unauthorize_url = "https://api.github.com/applications/{client_id}/grant";

        // Get access token
        UserProfile userProfile = UserProfileManager.getUserProfile();
        String accessToken = userProfile.getAccessToken();

        // Request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", accessToken);

        // Request header
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuthStream = Base64
                .getEncoder()
                .encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String encodedAuth = new String(encodedAuthStream);

        // Add headers and body to DELETE request to delete OAuth2 grant
        Client client = ClientBuilder.newClient();
        Response logoutResponse = client
                .target(unauthorize_url)
                .resolveTemplate("client_id", clientId)
                .request()
                .header("Authorization", "Basic " + encodedAuth)
                .method("DELETE", Entity.json(requestBody));

        if (logoutResponse.getStatus() != 204) {
            throw new ServletException("Could not delete OAuth2 application grant");
        }

        // Log out of application and redirect to unsecured page
        request.logout();
        response.sendRedirect("hello.html");
    }
}

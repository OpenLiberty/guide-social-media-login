package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@RequestScoped
public class FacebookLogout implements ILogout {

    public Response logout() {

        final String unauthorizeUrl = "https://graph.facebook.com/v2.8" +
                "/{user_id}/permissions";

        String userId = UserProfileManager
                .getUserProfile()
                .getClaims()
                .getClaim("id", String.class);

        System.out.println(userId);

        // Get access token
        String accessToken = UserProfileManager
                .getUserProfile()
                .getAccessToken();

        // Add headers and body to DELETE request to delete OAuth2 grant
        return ClientBuilder
                .newClient()
                .target(unauthorizeUrl)
                .resolveTemplate("user_id", userId)
                .queryParam("access_token", accessToken)
                .request()
                .delete();
    }
}

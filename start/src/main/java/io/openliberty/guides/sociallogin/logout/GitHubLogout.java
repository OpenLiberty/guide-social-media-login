package io.openliberty.guides.sociallogin.logout;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

@RequestScoped
public class GitHubLogout implements ILogout {

    public Response logout() {
        return null;
    }
}

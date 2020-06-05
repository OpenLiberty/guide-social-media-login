package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class LogoutHandler {

    @Inject
    private GitHubLogout gitHubLogout;

    private static final String GITHUB_LOGIN = "githubLogin";

    public ILogout getLogout() {

        String socialMediaName = UserProfileManager.getUserProfile().getSocialMediaName();
        switch (socialMediaName) {
            case GITHUB_LOGIN:
                return gitHubLogout;
            default:
                throw new UnsupportedOperationException("Cannot find the right logout " +
                        "service for social media name " + socialMediaName);
        }
    }
}

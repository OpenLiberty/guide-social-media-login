package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class LogoutHandler {

    @Inject
    private FacebookLogout facebookLogout;

    @Inject
    private GitHubLogout gitHubLogout;

    // The values returned by UserProfileManager for social media name is id of configuration element
    private static final String GITHUB_LOGIN = "githubLogin";
    private static final String FACEBOOK_LOGIN = "facebookLogin";

    public ILogout getLogout() {
        String socialMediaName = UserProfileManager.getUserProfile().getSocialMediaName();
        switch (socialMediaName) {
            case GITHUB_LOGIN:
                return gitHubLogout;
            case FACEBOOK_LOGIN:
                return facebookLogout;
            default:
                throw new IllegalArgumentException("Cannot find the right logout service " +
                        "for social media name " + socialMediaName);
        }
    }
}

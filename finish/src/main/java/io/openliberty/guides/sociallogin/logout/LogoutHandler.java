package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class LogoutHandler {

    @Inject
    private FacebookLogout facebookLogout;

    // tag::inject[]
    @Inject
    private GitHubLogout gitHubLogout;
    // end::inject[]

    // The values returned by UserProfileManager for social media name is id of configuration element
    // tag::githubLoginName[]
    private static final String GITHUB_LOGIN = "githubLogin";
    // end::githubLoginName[]

    public ILogout getLogout() {
        // tag::socialMediaName[]
        String socialMediaName = UserProfileManager.getUserProfile().getSocialMediaName();
        // end::socialMediaName[]
        // tag::switch[]
        switch (socialMediaName) {
            // tag::handleGithubLogout[]
            case GITHUB_LOGIN:
                return gitHubLogout;
            // end::handleGithubLogout[]
            default:
                throw new IllegalArgumentException("Cannot find the right logout service " +
                        "for social media name " + socialMediaName);
        // end::switch[]
        }
    }
}

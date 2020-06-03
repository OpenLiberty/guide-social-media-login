package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class LogoutHandler {

    public ILogout getLogout() {
        String socialMediaName = UserProfileManager.getUserProfile().getSocialMediaName();
        switch (socialMediaName) {
            default:
                throw new IllegalArgumentException("Cannot find the right logout service " +
                        "for social media name " + socialMediaName);
        }
    }
}

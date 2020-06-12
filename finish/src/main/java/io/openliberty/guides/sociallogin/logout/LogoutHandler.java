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
package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class LogoutHandler {

    // tag::inject[]
    @Inject
    private GitHubLogout gitHubLogout;
    // end::inject[]

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
                throw new UnsupportedOperationException("Cannot find the right logout " +
                        "service for social media name " + socialMediaName);
        // end::switch[]
        }
    }
}

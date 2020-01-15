// tag::comment[]
/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
// end::comment[]
package it.io.openliberty.guides.sociallogin;

import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EndpointIT {
    // tag::test[]
    @Test
    // end::test[]
    void testHelloRedirectsToSocialLoginForm() {

        // tag::systemProperties[]
        String httpPort = System.getProperty("http.port");
        String contextRoot = System.getProperty("context.root");
        // end::systemProperties[]

        // Construct URL for protected service
        String url = "http://localhost:" + httpPort + "/" + contextRoot + "/hello";

        // GET response from service
        // tag::target[]
        WebTarget target = ClientBuilder
        		.newClient()
        		.target(url);
        // end::target[]
        // tag::requestget[]
        Response response = target
                .request()
                .get();
        // end::requestget[]

        // Service must get 200
        // tag::assertequals[]
        assertEquals(Response.Status.OK.getStatusCode(),
        		response.getStatus(),
        		"Incorrect response code from " + url);
        // end::assertequals[]
        // The response must be the selection form for social media login provider
        // tag::assertredirect[]
        String message = response.readEntity(String.class);
        String expectedMessage = "Social Media Selection Form";
        assertTrue(message.contains(expectedMessage),
        		"Incorrect response from " + url + ". Did not redirect to social login form");
        // end::assertredirect[]
        response.close();
    }
}

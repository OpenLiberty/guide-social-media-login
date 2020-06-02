package io.openliberty.guides.sociallogin.logout;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

public interface ILogout {
    Response logout() throws ServletException;
}

package com.jsuszynski.hypefit.login.servlets;

import com.jsuszynski.hypefit.login.domain.Credentials;
import com.jsuszynski.hypefit.login.domain.User;
import com.jsuszynski.hypefit.login.tools.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String ADDRESS = "localhost:8080/hypefitapi/authenticate";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Credentials credentials = new Credentials(req.getParameter("login"), PasswordHash.hashPassword(req));

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(ADDRESS);

        Response response = webTarget.request().post(Entity.json(credentials));

        if (response.getStatus() == 200) {
            User user = response.readEntity(User.class);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("index.jsp");
        } else {
            req.setAttribute("loginMessage", false);
            resp.sendRedirect("users/login.jsp");
        }
        return;
    }

}

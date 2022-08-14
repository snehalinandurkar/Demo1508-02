/*
 * LoginAction.java
 *
 * Created on February 21, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for logging out
 *
 */

package com.splwg.selfservice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author  Jacek Ziabicki
 */
public class LogoutAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
        request.getSession(true);
        HttpSession session = request.getSession();
        ServletContext context = servlet.getServletContext();

        String jspName = Util.getCMFile("SSvcLogIn.jsp", context);

        String loginURL = jspName;

        session.removeAttribute("SelfService_validUser");
        session.removeAttribute("AccountsForPerson");

        forward(loginURL, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

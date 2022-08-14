/*
 * CMSSvcSULoginAction.java
 *
 * Created on Dec 28, 2011, by Edrik Pagaduan
 * Part of CorDaptix Web Self Service 
 * Implements the Action for logging in a super user;
 *
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

public class CMSSvcSULoginAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        String jspName = Util.getCMFile("CMSSvcSUserLogIn.jsp", context);
        String loginURL = jspName;

        session.setAttribute("accountName","");
        session.setAttribute("password", "");
        session.setAttribute("suName", "");
        session.setAttribute("userId", "");
        session.setAttribute("superUser", "");	

        String unauthorized = (String) request.getAttribute("unauthorized");
        if (unauthorized != null) {
            String origURL = HttpUtils.getRequestURL(request).toString();
            String queryString = request.getQueryString();
            if (queryString != null) {
                origURL += "?" + queryString;
            }
            loginURL += "?origURL=" + URLEncoder.encode(origURL) + "&errorMsg=" + URLEncoder.encode(getMessage(11306, session, properties));
        }
        forward(loginURL, request, response);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private String getMessage(int nbr, HttpSession session, Properties properties) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr);
    }
}

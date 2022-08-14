/*
 * LoginAction.java
 *
 * Created on February 21, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for logging in; used when the user tried to access a page after the session expired
 *
 * 2015-05-08   MCeripul    Security Fix.
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
import com.splwg.selfservice.WebStringUtilities;

public class LoginAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
        String loginURL = jspName;

        String unauthorized = (String) request.getAttribute("unauthorized");
        if (unauthorized != null) {
            String origURL = HttpUtils.getRequestURL(request).toString();
            
            // Start Add - 2015-05-08 - MCeripul - Security Fix
            try {
                if(!WebStringUtilities.validInputField(origURL)) {
                    String error = "Invalid character found on Orig URL";
                    jspName = Util.getCMFile("SSvcLogIn.jsp", context);
                    forward(jspName + "?errorMsg=" + error, request, response);
                }
            }
            catch(Exception e) {
                String error = "Invalid character found on Orig URL";
                jspName = Util.getCMFile("SSvcLogIn.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            }
            // End Add - 2015-05-08 - MCeripul - Security Fix            
            
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

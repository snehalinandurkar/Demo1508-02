/*
 * EnterMeterReadAction.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for reminding someones password ('Forgot your password' page)
 *
 * Date         Author      Description 
 *
 * 2015-05-12   SMedina     Security Fix.
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// Start Add - 2015-05-12 - SMedina - Security Fix
import com.splwg.selfservice.WebStringUtilities;
// End Add - 2015-05-12 - SMedina - Security Fix

/**
 *
 * @author  Jacek Ziabicki
 */
public class PasswordReminderAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }

        if (step.equalsIgnoreCase("entryform")) {
            showEntryForm(servlet, request, response);
        } else if (step.equalsIgnoreCase("process")) {
            processRequest(servlet, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    private void showEntryForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        String personId = "";

        RetrievePersonFromUserIdBean ret = new RetrievePersonFromUserIdBean(properties);
        ret.setUserId(XMLEncoder.encode(request.getParameter("personId")));
        if (ret.retrievePerson()) {
            personId = ret.getPersonId();
        } else {
            String error = ret.getErrorMessage();
            error = ret.getErrorMessage();
            if (error.equals("NOUSER")) {
                error = getMessage(11804, session, properties);
            }
            String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
            return;
        }

        PasswordReminderBean reminder = new PasswordReminderBean(properties);
        reminder.setPersonId(personId);
        reminder.setUserId(XMLEncoder.encode(request.getParameter("personId")));
        if (reminder.retrieveData() && reminder.getPasswordQuestion() != null
                && !reminder.getPasswordQuestion().equals("null")) {
            request.setAttribute("reminder", reminder);
            session.setAttribute("reminder", reminder);
            String jspName = Util.getCMFile("SSvcForgotPassword.jsp", context);
            forward(jspName, request, response);
        } else {
            //String error = msg.getMessage(1801);
            String error = reminder.getErrorMessage();
            String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
    }

    private void processRequest(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        PasswordReminderBean reminder = (PasswordReminderBean) session.getAttribute("reminder");
		
		// Start Add - 2015-05-12 - SMedina - Security Fix
		// Password Answer
        try {
            if(!WebStringUtilities.validInputField(request.getParameter("passwordAnswer").trim())) {
				String error = "Invalid character found in Password Answer.";
				String jspName = Util.getCMFile("SSvcForgotPassword.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
        }
        catch(Exception e) {
            String error = "Invalid character found in Password Answer.";
            String jspName = Util.getCMFile("SSvcForgotPassword.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
		// End Add - 2015-05-12 - SMedina - Security Fix

        if (reminder.getPasswordAnswer().equalsIgnoreCase(request.getParameter("passwordAnswer").trim())) {
            EmailBean em = new EmailBean(properties);
            if (em.postMail(reminder.getEmail(), Util.decode(reminder.getUserId()), reminder.getPasswordPlain(),
                    "passwordReminder", context, reminder.getPersonId())) {
                String jspName = Util.getCMFile("SSvcReminderOK.jsp", context);
                forward(jspName, request, response);
            } else {
                String error = (getMessage(11803, session, properties) + " " + em.getErrorMessage()).replaceAll(";", "");
                error = error.replaceAll("\t", "");
                String jspName = Util.getCMFile("SSvcForgotPassword.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            }
        } else {
            String error = getMessage(11802, session, properties);
            String jspName = Util.getCMFile("SSvcForgotPassword.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
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

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        System.out.println("PasswordReminderAction [" + url + "]");
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

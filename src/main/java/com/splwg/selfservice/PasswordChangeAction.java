/*
 * PasswordChangeAction.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for the Change Password page
 *
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

public class PasswordChangeAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        String requestCharEncoding = request.getParameter("_charset_");
        if (requestCharEncoding != null && !requestCharEncoding.equalsIgnoreCase("UTF-8")) {
            try {
                throw new InvalidParameterException("Invalid character encoding.");
            } catch (InvalidParameterException e) {
                String jspName = "";
                String step = request.getParameter("step");
                String invalidParamError = e.getMessage();
                if (step != null && !step.equals("") && step.equalsIgnoreCase("process")) {
                    jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
                    forward(jspName + "?errorMsg=" + invalidParamError, request, response);
                    return;
                }
            }
        }

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

    /** Display registration entry form.
     */
    private void showEntryForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String personId = "";

        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        PasswordHintBean phb = new PasswordHintBean(properties);
        session.setAttribute("passwordHint", phb);
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        RetrievePersonFromUserIdBean ret = new RetrievePersonFromUserIdBean(properties);
        ret.setUserId(XMLEncoder.encode(validUser.getUserId()));
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
        }

        PasswordReminderBean reminder = new PasswordReminderBean(properties);
        reminder.setPersonId(personId);
        reminder.setUserId(XMLEncoder.encode(validUser.getUserId()));
        if (reminder.retrieveData() && !reminder.getPasswordQuestion().equals("null")) {
            request.setAttribute("user", reminder);
            session.setAttribute("reminder", reminder);
            String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
            forward(jspName, request, response);
        } else {
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
        request.setAttribute("user", reminder);
        if (reminder.getPasswordPlain().equals(request.getParameter("priorPassword"))) {
            String newPassword = request.getParameter("newPassword");
            if (!newPassword.equals(request.getParameter("newPasswordConfirm"))) {
                String error = getMessage(11701, session, properties);
                String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            } else if (request.getParameter("newPassword").equals("") || request.getParameter("newPassword") == null) {
                String error = getMessage(11702, session, properties);
                String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            } else if (request.getParameter("passwordAnswer").equals("")
                    || request.getParameter("passwordAnswer") == null) {
                String error = getMessage(11303, session, properties);
                String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            } else {
                PasswordBean pw = new PasswordBean(properties, session);
                pw.setPersonId(reminder.getPersonId());
                pw.setEMail(reminder.getEmail());
                pw.setReceiveMarketingInfo(reminder.getReceiveMarketingInfo());
                pw.setPasswordPlain(request.getParameter("newPassword"));
                pw.setPasswordQuestion(request.getParameter("passwordQuestion"));
                pw.setPasswordAnswer(request.getParameter("passwordAnswer"));

                if (pw.updatePerson()) {
                    EmailBean em = new EmailBean(properties);
                    em.postMail(reminder.getEmail(), Util.decode(reminder.getUserId()), request
                            .getParameter("newPassword"), "passwordChange", context, reminder.getPersonId());
                    String jspName = Util.getCMFile("SSvcPasswordChangeOK.jsp", context);
                    forward(jspName, request, response);
                } else {
                    String error = pw.getErrorMessage();
                    String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
                    forward(jspName + "?errorMsg=" + error, request, response);
                }
            }
        } else {
            String error = "You entered an incorrect password";
            String jspName = Util.getCMFile("SSvcPasswordChange.jsp", context);
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
        System.out.println("PasswordChange ["+ url +"]");
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

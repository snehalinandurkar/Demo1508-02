/*
 * Registration.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for registering the customer
 * There are three screens:
 * 1.) Customer Data input screen
 * 2.) Password Data input screen
 * 3.) Registration Confirmation screen
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

public class RegistrationAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //make sure to process UTF-8 characters correctly
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
                if (step != null && !step.equals("")) {
                    if (step.equalsIgnoreCase("process")) jspName = Util.getCMFile("SSvcRegistration.jsp", context);
                    if (step.equalsIgnoreCase("passwordProcess"))
                        jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
                    forward(jspName + "?errorMsg=" + invalidParamError, request, response);
                    return;
                }
            }
        }

        // Get the sub-action (step) from the request parameters.
        // Delegate the request to the appropriate method.

        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }
        boolean newId = false;
        if (step.equalsIgnoreCase("entryform")) {
            showEntryForm(servlet, request, response);
        } else if (step.equalsIgnoreCase("process")) {
            newId = processRequest(servlet, request, response);
        } else if (step.equalsIgnoreCase("passwordProcess")) {
            processPassword(servlet, request, response, newId);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    /** Display registration entry form.
     */
    private void showEntryForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        PasswordHintBean phb = new PasswordHintBean(properties);
        session.setAttribute("passwordHint", phb);

        /*        CountryBean country = new CountryBean();
         country.setProperties(properties);
         String co = request.getParameter("country");
         if (co == null)
         country.setDefaultCountry();
         else
         country.setCountry(co);

         request.setAttribute("SelfService_country", country);
         session.setAttribute("SelfService_country", country);
         */
        String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
        forward(jspName, request, response);
    }

    private boolean processRequest(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        boolean newId = false;
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        PersonsForAccountBean service = new PersonsForAccountBean(properties);

		// Start Add - 2015-05-12 - SMedina - Security Fix
		// Account Id
        try {
            if(!WebStringUtilities.validInputField(request.getParameter("accountId"))) {
				String error = "Invalid character found in Account Id.";
				String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(request.getParameter("accountId"))) {
                String error = "Invalid character found in Account Id.";
                String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            String error = "Invalid character found in Account Id.";
            String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
		
		// First Name
        try {
            if(!WebStringUtilities.validInputField(request.getParameter("firstName"))) {
				String error = "Invalid character found in First Name.";
				String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(request.getParameter("firstName"))) {
                String error = "Invalid character found in First Name.";
                String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            String error = "Invalid character found in First Name.";
            String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
		
		// Last Name
        try {
            if(!WebStringUtilities.validInputField(request.getParameter("lastName"))) {
				String error = "Invalid character found in Last Name.";
				String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(request.getParameter("lastName"))) {
                String error = "Invalid character found in Last Name.";
                String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            String error = "Invalid character found in Last Name.";
            String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
		// End Add - 2015-05-12 - SMedina - Security Fix
		
        service.setAccountId(request.getParameter("accountId"));
        service.setFirstName(request.getParameter("firstName").toUpperCase());
        service.setLastName(request.getParameter("lastName").toUpperCase());
        service.setAddress("");
        service.setCity("");
        service.setCountry("");
        service.setPostalCode("");
        service.setState("");

        if (request.getParameter("receiveMktChk").equals("true"))
            service.setReceiveMarketingInfo("RCVE");
        else
            service.setReceiveMarketingInfo("NRCV");

        if (service.callPersonsForAccountService(servlet, request) && service.getPersonFound()) {
            String warning = "";
            warning = service.getWarningMsg();
            if (!service.getWebAccess()) {
                //            if (!warning.equals(""))
                //              warning = warning + "\n\n" + getMessage(11305);
                //            else
                warning = getMessage(11305, session, properties);
            }
            session.setAttribute("personsForAccount", service);
            request.setAttribute("personsForAccount", service);

            // Check if user is already registered
            PasswordReminderBean reminder = new PasswordReminderBean(properties);
            reminder.setPersonId(service.getPersonId());
            reminder.retrieveData();
            newId = reminder.getNewId();
            String pw = reminder.getPasswordPlain();
            if (pw != null && pw != "") {
                String error = getMessage(11304, service.getPersonId(), session, properties);
                String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
            } else {
                String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
                forward(jspName + "?warningMsg=" + warning, request, response);
            }
        } else {
            String error = service.getErrorMessage();
            String jspName = Util.getCMFile("SSvcRegistration.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
        return newId;
    }

    private void processPassword(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response,
            boolean newId) throws IOException, ServletException {
        String email = "";
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        PersonsForAccountBean pfa = (PersonsForAccountBean) session.getAttribute("personsForAccount");
        if (pfa.getEmailExists() == false) {
            email = request.getParameter("emailAddress");
            if (email.equals("") || email == null) {
                String error = getMessage(11301, session, properties);
                String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
                return;
            }
        } else {
            email = pfa.getEMail();
        }

        PasswordBean pw = new PasswordBean(properties, session);
		
		// Start Add - 2015-05-12 - SMedina - Security Fix
		// Password
        try {
            if(WebStringUtilities.checkXssInjection(request.getParameter("password"))) {
				String error = "Invalid character found in Password.";
				String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
        }
        catch(Exception e) {
            String error = "Invalid character found in Password.";
			String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
			forward(jspName + "?errorMsg=" + error, request, response);
        }
		
		// Password Answer
		try {
            if(WebStringUtilities.checkXssInjection(request.getParameter("passwordAnswer"))) {
				String error = "Invalid character found in Password Answer.";
				String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
        }
        catch(Exception e) {
            String error = "Invalid character found in Password Answer.";
			String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
			forward(jspName + "?errorMsg=" + error, request, response);
        }
        
        // Password Confirm
		try {
            if(WebStringUtilities.checkXssInjection(request.getParameter("passwordConfirm"))) {
				String error = "Invalid character found in Password Confirm.";
				String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
        }
        catch(Exception e) {
            String error = "Invalid character found in Password Confirm.";
			String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
			forward(jspName + "?errorMsg=" + error, request, response);
        }
		
		// User Id
		try {
            if(!WebStringUtilities.validInputField(request.getParameter("user"))) {
				String error = "Invalid character found in User Id.";
				String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(request.getParameter("user"))) {
                String error = "Invalid character found in User Id.";
                String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
				forward(jspName + "?errorMsg=" + error, request, response);
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            String error = "Invalid character found in User Id.";
			String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
			forward(jspName + "?errorMsg=" + error, request, response);
        }
		// End Add - 2015-05-12 - SMedina - Security Fix

        if (!request.getParameter("password").equals(request.getParameter("passwordConfirm"))) {
            String error = getMessage(11302, session, properties);
            String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
            return;
        }
        if (request.getParameter("passwordAnswer").equals("") || request.getParameter("passwordAnswer") == null) {
            String error = getMessage(11303, session, properties);
            String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
            return;
        }
        pw.setPersonId(pfa.getPersonId());
        pw.setEMail(email);
        pw.setReceiveMarketingInfo(pfa.getReceiveMarketingInfo());
        pw.setPasswordQuestion(request.getParameter("passwordQuestion"));	
        pw.setPasswordPlain(request.getParameter("password"));
        pw.setPasswordAnswer(XMLEncoder.encode(request.getParameter("passwordAnswer")));
        pw.setUserId(XMLEncoder.encode(request.getParameter("user")));
        if (newId == true) {
            pw.setPrimaryIdentifier("true");
        } else {
            pw.setPrimaryIdentifier("false");
        }

        boolean isUpdateSuccessful = false;

        // Synchronize this code block to prevent duplicate Web User IDs
        synchronized (this) {
            isUpdateSuccessful = pw.updatePerson();
        }

        if (isUpdateSuccessful) {
            ValidUserBean validUser = new ValidUserBean();
            validUser.setPersonId(pfa.getPersonId());
            validUser.setUserId(XMLEncoder.encode(request.getParameter("user")));
            validUser.setPassword(request.getParameter("password"));
            request.setAttribute("validUser", validUser);
            session.setAttribute("validUser", validUser);
            session.setAttribute("SelfService_validUser", validUser);
            AccountsForPersonBean accountPerson = new AccountsForPersonBean();
            accountPerson.setPersonId(pfa.getPersonId());
            accountPerson.setProperties(properties);
            accountPerson.getAccountList();
            session.setAttribute("AccountsForPerson", accountPerson);
            EmailBean em = new EmailBean(properties);
            if (!em.postMail(email, request.getParameter("user"), request.getParameter("password"), "Registration",
                    context, pw.getPersonId())) {
                String jspName = Util.getCMFile("SSvcRegistrationOKNoEmail.jsp", context);
                forward(jspName, request, response);
            } else {
                // Addition of Customer Contact

                CustomerContactBean cb = new CustomerContactBean();
                cb.setPersonId(pfa.getPersonId());
                cb.setProperties(properties);

                String contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeRegistration");

                if (!contactType.equals("NONE") && !contactType.equals("")) {
                    String contactClass = properties
                            .getProperty("com.splwg.selfservice.CustomerContactClassRegistration");

                    cb.setContactClass(contactClass);
                    cb.setContactType(contactType);
                    cb.addCustomerContact();
                }

                String jspName = Util.getCMFile("SSvcRegistrationOK.jsp", context);
                forward(jspName, request, response);
            }
        } else {
            String error = pw.getErrorMessage();
            if (error.equals("USEREXISTS")) {
                error = getMessage(11805, session, properties);
            }
            String jspName = Util.getCMFile("SSvcPasswordEntry.jsp", context);
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

    private String getMessage(int nbr, String param, HttpSession session, Properties properties) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr, param);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        System.out.println("RegistrationAction forward [" + url + "]");
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

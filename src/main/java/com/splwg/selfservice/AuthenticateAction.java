/*
 * AutenticateAction.java
 *
 * Created on Feb. 21, 2002, 12:01 PM by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Authentication of the user
 *
 * Date         Author      Description 
 *
 * 2015-05-08   MCeripul    Security Fix.
 */
package com.splwg.selfservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.splwg.selfservice.WebStringUtilities;
import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

public class AuthenticateAction
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
                String invalidParamError = e.getMessage();
                String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
                forward(jspName + "?errorMsg=" + invalidParamError, request, response);
                return;
            }
        }

        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache("userId-cache");

        String contactType;
        String contactClass;
        String error;
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        session.removeAttribute("SelfService_validUser");
        String inputUserId = XMLEncoder.encode(request.getParameter("userId"));
        System.out.println("encoded user id:[" + inputUserId + "] plain user id[" + request.getParameter("userId") + "]");
        if (inputUserId == null || inputUserId.equals("")) {
            sendError(request, response, getMessage(11603, session, properties), context);
            return;
        }
        
        // Start Add - 2015-05-08 - MCeripul - Security Fix
        try {
            if(!WebStringUtilities.validInputField(inputUserId)) {
                error = "Invalid character found in User Id.";
                sendError(request, response, error, context);
                return;
            }
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(inputUserId)) {
                error = "Invalid character found in User Id.";
                sendError(request, response, error, context);
                return;
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            error = "Invalid character found in User Id.";
            sendError(request, response, error, context);
            return;
        }
        // End Add - 2015-05-08 - MCeripul - Security Fix

        String inputPassword = request.getParameter("password");
        if (inputPassword == null || inputPassword.equals("")) {
            sendError(request, response, getMessage(11604, session, properties), context);
            return;
        }
        
        // Start Add - 2015-05-08 - MCeripul - Security Fix
        try {
            
            // Start Delete - 2015-05-19 - MCeripul - Security Fix
            //if(!WebStringUtilities.validInputField(inputPassword)) {
            //    String error = "Invalid character found in Password.";
            //    sendError(request, response, error, context);
            //    return;
            //}
            // End Delete - 2015-05-19 - MCeripul - Security Fix
            
            // Start Add - 2015-05-19 - MCeripul - Security Fix
            if(WebStringUtilities.checkXssInjection(inputPassword)) {
                error = "Invalid character found in Password.";
                sendError(request, response, error, context);
                return;
            }
            // End Add - 2015-05-19 - MCeripul - Security Fix
        }
        catch(Exception e) {
            error = "Invalid character found in Password.";
            sendError(request, response, error, context);
            return;
        }
        // End Add - 2015-05-08 - MCeripul - Security Fix

        boolean isAuthenticated = false;

        String entityName = "";
        String errorMessage = "";
        String personId = "";

        String sFails = properties.getProperty("com.splwg.selfservice.lockout.Failures");
        String sMins  = properties.getProperty("com.splwg.selfservice.lockout.Minutes");

        int fails = parseInt(sFails);
        int mins  = parseInt(sMins);

        String authenticate = properties.getProperty("com.splwg.selfservice.Authentication");
        if (authenticate != null && authenticate.equals("true")) {
            String className = properties.getProperty("com.splwg.selfservice.AuthenticationClassName");
            try {

                IAuthentication auth = (IAuthentication) Class.forName(className).newInstance();
                auth.setPersonId(inputUserId);
                auth.setPassword(inputPassword);
                if (auth.validatePassword() == true) {
                    isAuthenticated = true;
                    PasswordBean pw = new PasswordBean(properties, session);
                    pw.setPersonId(inputUserId);
                    if (pw.retrieveEntityName()) {
                        entityName = pw.getEntityName();
                    } else {
                        sendError(request, response, pw.getErrorMessage(), context);
                    }
                } else {
                    errorMessage = getMessage(11602, session, properties);
                }
            } catch (Exception e) {
                sendError(request, response, e.getMessage(), context);
            }
        } else {
            PasswordBean pw = new PasswordBean(properties, session);
            pw.setUserId(inputUserId);
            
            pw.setPasswordPlain(inputPassword);

            Integer count = 0;

            if (fails > 0 && mins > 0) {
                count = (Integer) cache.get(inputUserId);
                if (count == null)
                    count = 1;
                else
                    count++;

                cache.put(inputUserId, count, 1000*60*mins);
                System.out.println("update coherence cache [" + inputUserId + ":" + count + "]");
            }
            // if (count > fails)
            //    sendEmail(properties, pw.getPersonId(), inputUserId);

            if (count <= fails && pw.validatePassword()) {
                isAuthenticated = true;
                entityName = pw.getEntityName();
                personId = pw.getPersonId();
                if (fails>0 && mins > 0)
                    cache.remove(inputUserId);
            } else {
                errorMessage = pw.getErrorMessage();
                if (errorMessage == null || errorMessage.equals(""))
                    errorMessage = getMessage(11602, session, properties);
                if (errorMessage.equals("NOUSER")) {
                    errorMessage = getMessage(11804, session, properties);
                }
            }
        }

        if (isAuthenticated == true) {
            regenrateSession(request); 
            session = request.getSession();
            // user authenticated; create ValidUserBean in session scope
            AccountsForPersonBean accountPerson = new AccountsForPersonBean();
            accountPerson.setPersonId(personId);
            accountPerson.setProperties(properties);
            accountPerson.getAccountList();
            ValidUserBean validUser = new ValidUserBean();
            if (accountPerson.getAccountOccurences() == 1) {
                validUser.setAccountId(accountPerson.getAccountId());
                validUser.setPersonId(personId);
                validUser.setUserId(inputUserId);
                validUser.setEntityName(entityName);
                session.setAttribute("SelfService_validUser", validUser);
                session.setAttribute("AccountsForPerson", accountPerson);

                // redirect to Account Information
                // or to the page the user tried to access
                String origURL = request.getParameter("origURL");
                String redirectURL;
                if (origURL == null || origURL.equals("")) {
                    redirectURL = "Account";
                } else {
                
                    // Start Add - 2015-05-08 - MCeripul - Security Fix
                    try {
                        if(!WebStringUtilities.validInputField(origURL)) {
                            error = "Invalid character found in URL.";
                            sendError(request, response, error, context);
                            return;
                        }
                    }
                    catch(Exception e) {
                        error = "Invalid character found in URL.";
                        sendError(request, response, error, context);
                        return;
                    }
                    // End Add - 2015-05-08 - MCeripul - Security Fix
                    
                    //redirectURL = origURL;   // changed, since we always want the customer to go to the account,
                    // since otherwise we won't load required data into the session
                    // (like currency symbol), Arie, 05/28/2003.
                    redirectURL = "Account";
                }

                isAuthenticated = true;
                response.sendRedirect(response.encodeRedirectURL(redirectURL));
            } else if (accountPerson.getAccountOccurences() > 1) {
                validUser.setPersonId(personId);
                validUser.setUserId(inputUserId);
                validUser.setEntityName(entityName);
                session.setAttribute("SelfService_validUser", validUser);
                session.setAttribute("AccountsForPerson", accountPerson);

                //String error = accountPerson.getErrorMessage();
                forward("/SSvcController/myaccounts", request, response);
            } else {
                request.setAttribute("accountPerson", accountPerson);
                error = accountPerson.getErrorMessage();
                if (error == null || error.equals("")) error = getMessage(11601, session, properties);
                String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
                forward(jspName + "?errorMsg=" + error, request, response);
                return;
            }

            // Addition of Customer Contact
            CustomerContactBean cb = new CustomerContactBean();
            cb.setPersonId(personId);
            cb.setProperties(properties);

            if (isAuthenticated && accountPerson.getAccountOccurences() > 0) {
                contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeLoginSuccess");
                contactClass = properties.getProperty("com.splwg.selfservice.CustomerContactClassLoginSuccess");
            } else {
                contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeLoginFailure");
                contactClass = properties.getProperty("com.splwg.selfservice.CustomerContactClassLoginFailure");
            }

            if (!contactType.equals("NONE") || contactType == null) {
                cb.setContactClass(contactClass);
                cb.setContactType(contactType);
                cb.addCustomerContact();
            }
        } else {
            sendError(request, response, errorMessage, context);
        }

        return;
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMsg,
            ServletContext context) throws IOException, ServletException {
        if (errorMsg == null) errorMsg = "Undefined Error";
        String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
        String loginURL = jspName + "?errorMsg=" + URLEncoder.encode(errorMsg);

        forward(loginURL, request, response);
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
        System.out.println("sendError URL: [" + url + "]");
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    static int parseInt(String str) {
        int ret=0;
        try {
            ret = Integer.parseInt(str);
        } catch (Exception e) {
            ret = 0;
        }
        return ret;
    }


    private void regenrateSession(HttpServletRequest request) {	   
        HttpSession oldSession = request.getSession();
	   
        Enumeration attrNames = oldSession.getAttributeNames();
        Properties props = new Properties();
	   
        while (attrNames != null && attrNames.hasMoreElements()) {
            String key = (String) attrNames.nextElement();
            props.put(key, oldSession.getAttribute(key));
        }
	   
        oldSession.invalidate();
        HttpSession newSession = request.getSession(true);
        attrNames = props.keys();
   
        while (attrNames != null && attrNames.hasMoreElements()) {
            String key = (String) attrNames.nextElement();
            newSession.setAttribute(key, props.get(key));
        }
    }
}

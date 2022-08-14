/*
 * LoginAction.java
 *
 * Created on April 02, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for creating a payment via credit card
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

public class PayOnlineAction
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
                String jspName = Util.getCMFile("SSvcPayOnlineEntryForm.jsp", context);
                forward(jspName + "?errorMsg=" + invalidParamError, request, response);
                return;
            }
        }

        // Get the sub-action (step) from the request parameters.
        // Delegate the request to the appropriate method.
        // Note that if the request was forwarded from the ProcessPayment
        // step back to EntryForm, the request parameter will have two values.
        // The first one will be EntryForm, which is what we are using.

        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }
        HttpServletRequest request2 = null;
        if (step.equalsIgnoreCase("entryform")) {
            showEntryForm(servlet, request, response);
        } else if (step.equalsIgnoreCase("process")) {
            request2 = processPayment(servlet, request, response);
        } else if (step.equalsIgnoreCase("results")) {
            showResults(servlet, request2, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    /** Display payment entry form.
     */
    private void showEntryForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        createControlCentralBean(servlet, request, response);
        String jspName = Util.getCMFile("SSvcPayOnlineEntryForm.jsp", context);
        forward(jspName, request, response);
    }

    /** This method does initial setup of the back-end processing.
     *  1. Create a NewPaymentBean, and store all the parameters from the entry
     *     form (plus Account Id) in the bean.
     *  2. Validate the bean.
     *  3. If validation was successful,
     *    a) check if there is already a newPayment bean saved as a session
     *       attribute;
     *    b) if there is, that means an earlier thread processing the
     *       same form stored it -- don't touch it, just forward the request to
     *       the "Please wait" page;
     *    c) if there isn't a bean in session scope -- save the bean that was
     *       just validated and forward to the "Please wait" page.
     *  4. If validation failed,
     *    a) set error message in the request parameters;
     *    b) forward the request back to the entry form.
     */
    private HttpServletRequest processPayment(HttpServlet servlet, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpServletRequest request2 = request;

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        // Package the input data in a NewPaymentBean
        NewPaymentBean newPayment = new NewPaymentBean(properties);
        newPayment.setAccountId(validUser.getAccountId());
        newPayment.setPersonId(validUser.getPersonId());
        newPayment.setPaymentAmount(request.getParameter("paymentAmount"));
        newPayment.setCreditCardNumber(request.getParameter("creditCardNumber"));
        newPayment.setExpMonth(request.getParameter("expMonth"));
        newPayment.setExpYear(request.getParameter("expYear"));
        newPayment.setCreditCardName(request.getParameter("creditCardName"));
        newPayment.setCardType(request.getParameter("cardType"));
        newPayment.setCurrencyCode(validUser.getCurrencyCode());
        newPayment.setCurrencySymbol(validUser.getCurrencySymbol());
        newPayment.setCurrencyPosition(validUser.getCurrencyPosition());

        // Validate the bean
        if (newPayment.isPaymentEntryFormValid(request)) {
            if (session.getAttribute("newPayment") == null) {
                session.setAttribute("newPayment", newPayment);
            }

            // Pass the bean to the wait page as well
            request.setAttribute("newPayment", newPayment);

            // forward to the wait page, then redirect to results page
            String resultsURL = response.encodeRedirectURL("Pay?step=results");
            response.setHeader("Refresh", "0; URL=" + resultsURL);
            String jspName = Util.getCMFile("SSvcPayOnlineWait.jsp", context);
            forward(jspName, request, response);
        } else {
            String url = "/SSvcController/Pay?step=EntryForm&errorMsg="
                    + URLEncoder.encode(newPayment.getErrorMessage());

            createControlCentralBean(servlet, request, response);
            forward(url, request, response);
        }
        return request2;
    }

    /** This method calls the newPayment bean to store a payment.
     *  1. Create a NewPaymentBean, and store all the parameters from the entry
     *     form (plus Account Id) in the bean.
     *  2. Validate the bean.
     *  3. If validation was successful,
     *    a) check if there is already a newPayment bean saved as a session
     *       attribute;
     *    b) if there is, that means an earlier thread processing the
     *       same form stored it -- don't touch it, just forward the request to
     *       the "Please wait" page;
     *    c) if there isn't a bean in session scope -- save the bean that was
     *       just validated and forward to the "Please wait" page.
     *  4. If validation failed,
     *    a) set error message in the request parameters;
     *    b) forward the request back to the entry form.
     */
    private void showResults(HttpServlet servlet, HttpServletRequest request2, HttpServletRequest request3,
            HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();

        boolean paymentOK = false;
        String errorMsg = "";
        HttpServletRequest request;
        request = request2;

        HttpSession session = request3.getSession();
        NewPaymentBean newPayment = (NewPaymentBean) session.getAttribute("newPayment");

        if (newPayment == null) {
            errorMsg = "in showResults, newPayment is null";
        } else {
            if (newPayment.isBusy()) {
                errorMsg = "in showResults, newPayment is busy";
            } else {
                newPayment.setBusy(true);
                if (newPayment.isPaymentAddSuccessful()) {
                    paymentOK = true;
                } else {
                    errorMsg = newPayment.getErrorMessage();
                }
            }
            request3.setAttribute("newPayment", newPayment);
            session.removeAttribute("newPayment");
        }

        // Forward to the appropriate page.
        String url;
        if (paymentOK) {
            String jspName = Util.getCMFile("SSvcPayOnlineOK.jsp", context);
            url = jspName;
        } else if (errorMsg.equals("The entered credit card number is not valid")) {
            // Entered for the CIS show on Jacek's request
            String jspName = Util.getCMFile("SSvcPayOnlineEntryForm.jsp", context);
            url = jspName + "?errorMsg=" + URLEncoder.encode(errorMsg);
            createControlCentralBean(servlet, request3, response);
        } else {
            String jspName = Util.getCMFile("SSvcPayOnlineError.jsp", context);
            url = jspName + "?errorMsg=" + URLEncoder.encode(errorMsg);
        }
        forward(url, request3, response);
    }

    private void createControlCentralBean(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        ControlCentralBean accountInfo = new ControlCentralBean();
        accountInfo.setCurrencyPosition(validUser.getCurrencyPosition());
        accountInfo.setCurrencySymbol(validUser.getCurrencySymbol());
        accountInfo.setAccountId(validUser.getAccountId());
        accountInfo.setProperties(properties);

        request.setAttribute("accountInfo", accountInfo);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

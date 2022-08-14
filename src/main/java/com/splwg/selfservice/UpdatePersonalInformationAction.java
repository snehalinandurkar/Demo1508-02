/*
 * LoginAction.java
 *
 * Created on November 11, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for updating personal information
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

public class UpdatePersonalInformationAction
        implements SSvcAction {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String errorMsg = "";

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
                String jspName = Util.getCMFile("SSvcUpdatePersonalInfoError.jsp", context);
                forward(jspName + "?errorMsg=" + invalidParamError, request, response);
            }
        }

        // Get the sub-action (step) from the request parameters.
        // Delegate the request to the appropriate method.
        // Note that if the request was forwarded from the Process Info Change
        // step back to EntryForm, the request parameter will have two values.
        // The first one will be EntryForm, which is what we are using.

        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }
        if (step.equalsIgnoreCase("entryform")) {
            showEntryForm(servlet, request, response);
        } else if (step.equalsIgnoreCase("process")) {
            processInfoChange(servlet, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    /** Display information change entry form.
     */
    private void showEntryForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //System.out.println("WSS - Before CreateBeans");
        ServletContext context = servlet.getServletContext();
        createBeans(servlet, request, response);

        //System.out.println("WSS - After CreateBeans");
        if (!errorMsg.equals("")) {
            String jspName = Util.getCMFile("SSvcTransactionError.jsp", context);
            forward(jspName + "?errorMsg=" + errorMsg, request, response);
        } else {
            String jspName = Util.getCMFile("SSvcUpdatePersonalInfoEntryForm.jsp", context);
            forward(jspName, request, response);
        }
    }

    /** This method does initial setup of the back-end processing.
     *  1. Create a PersonBean, and store all the parameters from the entry
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
    private void processInfoChange(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Package the input data in an UpdatePersonBean. If country is the only
        // address field filled in, and it is equal to the default country,
        // treat it as blank.
        String homePhone = request.getParameter("homePhone");
        String homePhoneExt = request.getParameter("homePhoneExt");
        String businessPhone = request.getParameter("businessPhone");
        String businessPhoneExt = request.getParameter("businessPhoneExt");
        String email = request.getParameter("email");
        String addr1 = check(request.getParameter("addr1"));
        String addr2 = check(request.getParameter("addr2"));
        String addr3 = check(request.getParameter("addr3"));
        String addr4 = check(request.getParameter("addr4"));
        String houseType = check(request.getParameter("houseType"));
        String number1 = check(request.getParameter("number1"));
        String number2 = check(request.getParameter("number2"));
        String inCityLimit = check(request.getParameter("inCityLimit"));
        String geoCode = check(request.getParameter("geoCode"));
        String city = check(request.getParameter("city"));
        String county = check(request.getParameter("county"));
        String state = check(request.getParameter("state"));
        String postal = check(request.getParameter("postal"));
        String country = check(request.getParameter("country"));
        String billLanguage = request.getParameter("billLanguage");
        String receive;
        if (request.getParameter("Check1").equals("true"))
            receive = "RCVE";
        else
            receive = "NRCV";

        String previousVersion = request.getParameter("previousVersion");

        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        String defaultCountry = properties.getProperty("com.splwg.selfservice.DefaultCountry");

        UpdatePersonBean upb = new UpdatePersonBean(homePhone, homePhoneExt, businessPhone, businessPhoneExt, email,
                addr1, addr2, addr3, addr4, houseType, number1, number2, inCityLimit, geoCode, city, county, state,
                postal, country, billLanguage, receive, previousVersion);

        // Validate the bean
        createBeans(servlet, request, response);
        AccountBean account = (AccountBean) request.getAttribute("account");
        PersonBean person = (PersonBean) request.getAttribute("person");

        if (person.isUpdateValid(upb, servlet, request)) {
            // If validation OK, make the update.
            // We need to pass the account bean as well in order to change the
            // billing address source on account.

            if (person.executeUpdate(upb, account)) {
                // If update was successful, display the "success" result
                String jspName = Util.getCMFile("SSvcUpdatePersonalInfoOK.jsp", context);
                forward(jspName, request, response);
            } else {
                // Update not successful - send user to error page
                //forward("/SSvcUpdatePersonalInfoError.jsp", request, response);
                String url = "/SSvcController/UpdatePersonalInformation?step=EntryForm&errorMsg="
                        + person.getErrorMessage();

                //                    URLEncoder.encode(person.getErrorMessage());
                forward(url, request, response);
            }
        } else {
            // There was a validation error - send user back to entry form
            String url = "/SSvcController/UpdatePersonalInformation?step=EntryForm&errorMsg="
                    + URLEncoder.encode(person.getErrorMessage());
            forward(url, request, response);
        }
    }

    private void createBeans(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        errorMsg = "";
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        AccountBean account = new AccountBean();
        account.setAccountId(validUser.getAccountId());
        account.setProperties(properties);
        if (!account.callAccountService()) {
            errorMsg = account.getErrorMessage();
            return;
        }
        PersonBean person = new PersonBean(properties);
        person.setPersonId(account.getCustomerId());
        person.setProperties(properties);
        if (!person.retrievePerson()) {
            errorMsg = person.getErrorMessage();
            return;
        }

        LanguageBean lang = new LanguageBean(properties);
        session.setAttribute("language", lang);

        CountryBean country = new CountryBean();
        country.setProperties(properties);

        String co = request.getParameter("country");
        if (co == null) {
            String mailingCountry = person.getCountry();
            if (mailingCountry == null || mailingCountry.equals("")) {
                country.setDefaultCountry();
            } else {
                country.setCountry(mailingCountry);
            }
        } else {
            country.setCountry(co);
        }

        request.setAttribute("account", account);
        request.setAttribute("person", person);
        request.setAttribute("SelfService_country", country);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private String check(String text) {
        if (text == null) text = "";
        if (text.startsWith(" ")) text = text.substring(1);

        return text;
    }
}

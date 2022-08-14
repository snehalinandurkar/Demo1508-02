/*
 * CMSSvcUpdatePersonalInformationAction.java
 *
 * Created on December 7, 2011, by Jordan Fernandez
 * Part of CorDaptix Web Self Service (10321)
 * Cloned UpdatePersonalInformationAction.java
 * Implements the Action for updating personal information
 * Added processing for super user access
 * 
 * 01/23/2012 - EPagaduan - SR511 EBill Enhancements
 *                        - Supplied additional processing for E-Bill enrolment and unenrolment
 * 21/12/2017 - LMoral    - Security Vulnerability Fix - Validate E-Mail Address
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;


public class CMSSvcUpdatePersonalInformationAction 
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
        HttpSession session = request.getSession();

        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        String personId = validUser.getPersonId();

        String accountId = request.getParameter("accountId");
        if (accountId == null) accountId = validUser.getAccountId();
        else validUser.setAccountId(accountId);

        Properties properties = (Properties) context.getAttribute("properties");        
        CMAccountPersonBean acctPer = new CMAccountPersonBean();
	acctPer.setProperties(properties);
	acctPer.setBillRouteType(acctPer.retrieveFields(accountId,personId));
	System.out.println ("BillRouteType: " + acctPer.getBillRouteType());

		
        //session.setAttribute("superUser", "Y");
        
        String billRouteType = acctPer.getBillRouteType();
        String mainCustomer = acctPer.getMainCustomer();
        session.setAttribute("billRouteType", billRouteType);
        session.setAttribute("mainCustomer", mainCustomer);
        session.getAttribute("billRouteType").toString();

        //System.out.println("WSS - After CreateBeans");
        String jspName = Util.getCMFile("SSvcUpdatePersonalInfoEntryView.jsp", context);
        if (!errorMsg.equals("")) {
            jspName = Util.getCMFile("SSvcTransactionError.jsp", context);
            forward(jspName + "?errorMsg=" + errorMsg, request, response);
        } else {
        	if(isNull(session.getAttribute("superUser"))){
        		jspName = Util.getCMFile("SSvcUpdatePersonalInfoEntryForm.jsp", context);
        	}
        	else{
        		jspName = Util.getCMFile("CMSSvcUpdatePersonalInfoEntryView.jsp", context);
        	}
        	forward(jspName, request, response);
        }
    }
    
    private boolean isNull(Object object){
    	try {
			if(object.equals(null)){
				return true;
			}
			return false;
		} catch (NullPointerException npe) {
			return true;
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
        /*
         * Start Add - LMoral - 2017-12-21 - Security Vulnerability fix
         */
        if(!email.equals("")) {
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
            }
            catch (AddressException ae) {
                String errorMsg = "Invalid E-Mail Address";
                String url = "/SSvcController/UpdatePersonalInformation?step=EntryForm&errorMsg="
                        + errorMsg;
                forward(url, request, response);
            } 
        }
        /*
         * End Add - LMoral - 2017-12-21 - Security Vulnerability fix
         */
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
        String previousBillRouteType = request.getParameter("previousCheck2");

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
            	// Update was successful for Person Info, check if EBill settings need to be updated
            	String eBillUpdate = request.getParameter("eBillUpdate");
                String contactClass = properties.getProperty("com.splwg.selfservice.CustomerContactClassForEBill");

            	System.out.println("eBillUpdate["+eBillUpdate+"]");
            	System.out.println("contactClass["+contactClass+"]");
            	if (eBillUpdate.equals("true")){
            		String billRouteType = "",contactType="",source="";
            		if (request.getParameter("Check2").equals("true")){
            			contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeEBillEnrolment");            			
            			billRouteType = "E-BILL";
            		} else {
            			contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeEBillUnenrolment");
            			billRouteType = "CRYSTAL";
            		}
            		System.out.println("ContactType["+contactType+"]");
            		System.out.println("BillRouteType["+billRouteType+"]");

            		try {
            			
            			String xml = updateRoutingType(account.getAccountId(), person.getPersonId(), billRouteType);
            			System.out.println(xml);
            			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            			String httpResponse = httpCall.callXAIServer(xml);                       
                        Document document = DocumentHelper.parseText(httpResponse);
                        Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                        if (faultEle != null) {
                            Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                            errorMsg = error.getText();
                        }
            		} catch (Exception e) {
                        errorMsg = "Caught exception: " + e.getMessage();
            		}
            		
            		//Create customer contact record
            		CMCustomerContactBean cb = new CMCustomerContactBean();
                    cb.setPersonId(person.getPersonId());
                    cb.setProperties(properties);

                    cb.setContactClass(contactClass);
                    cb.setContactType(contactType);
		    cb.setAccountId(account.getAccountId());
                    cb.addEBillCustomerContact();
                    
            		//sendEmail();
                    
                    CMEmailBean em = new CMEmailBean(properties);
                    em.postEBillMail(person.getEmail(), person.getEntityName(), 
                    		contactType, context, person.getPersonId());
            	}
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

    private void createCustomerContact(String acctId) {

    }

    private void sendEmail() {

    }
    
    public String updateRoutingType(String accountId, String personId, String billRouteType) {
    	String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>")
    				.append("<SOAP-ENV:Body>")
    				.append("<CmSSvcAccountMaintenance dateTimeTagFormat='CdxDateTime'  transactionType ='UPDATE' >")
    				.append("<CmSSvcAccountMaintenanceService>")
    				.append("<CmSSvcAccountMaintenanceHeader AccountID='").append(accountId).append("'/>")
    				.append("<CmSSvcAccountMaintenanceDetails AccountID='").append(accountId)
//				.append("' BillRouteType='").append(billRouteType)
				.append("'>")
    				.append("<AccountPersons>")
    				.append("<AccountPersonsHeader AccountID='").append(accountId).append("'/>")
    				.append("<AccountPersonsRow rowAction='Change' ")
    				.append("AccountID='").append(accountId).append("' PersonID='").append(personId)
				.append("' BillRouteType='").append(billRouteType)
				.append("'>")
    				.append("</AccountPersonsRow>")
    				.append("</AccountPersons>")
    				.append("</CmSSvcAccountMaintenanceDetails>")
    				.append("</CmSSvcAccountMaintenanceService>")
    				.append("</CmSSvcAccountMaintenance>")
    				.append("</SOAP-ENV:Body>")
    				.append("</SOAP-ENV:Envelope>")
    				.toString();
    	
    	return xml;
    }

}

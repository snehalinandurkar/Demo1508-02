/*
 ***********************************************************************
 *                 Confidentiality Information:
 *
 * This module is the confidential and proprietary information of
 * Oracle Corporation; it is not to be copied, reproduced, or
 * transmitted in any form, by any means, in whole or in part,
 * nor is it to be used for any purpose other than that for which
 * it is expressly provided without the written permission of
 * Oracle Corporation.
 *
 ***********************************************************************
 *
 * PROGRAM DESCRIPTION:
 *
 * This Java Action will be the main class of the Payment Options
 * Module. This class is invoked for every page submission that takes
 * place in the Payment Options Process and the value of the actionCode
 * parameter set in the JSPs determine what appropriate work needs to be
 * done and the subsequent page to be rendered based on the user action.
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2012-07-25  SMedin  CEd10. Payment Options Module. Initial Version.
 ************************************************************************
 */ 

package com.splwg.selfservice;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CMSSvcPaymentOptionsAction implements SSvcAction {

	// ~ Instance fields
	// --------------------------------------------------------------------------------------

	// ~ Methods
	// ----------------------------------------------------------------------------------------------

	public void perform(HttpServlet servlet, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
    	
		ServletContext context = servlet.getServletContext();

		String jspName = "";
		String fwdPage = "";
		String actionCode = request.getParameter("actionCode");
		
		System.out.println("actionCode: " + actionCode);
		
		HttpSession session = request.getSession();
		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();

		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

		if (actionCode == null || actionCode.equals("")) {
			actionCode = "showPaymentOptions";
		}

		// Payment Options Page
		if (actionCode.equalsIgnoreCase("showPaymentOptions")) {
			fwdPage = showPaymentOptions(servlet, request, response, validUser);
		}

		// Initiate Auto Pay Enrollment Page
		if (actionCode.equalsIgnoreCase("initiateAutoPayEnroll")) {
			fwdPage = initiateAutoPayEnroll(servlet, request, response);
		}

		// Verify Auto Pay Enrollment Page
		if (actionCode.equalsIgnoreCase("verifyAutoPayEnroll")) {
			fwdPage = verifyAutoPayEnroll(servlet, request, response);
		}

		// Confirm Auto Pay Enrollment Page
		if (actionCode.equalsIgnoreCase("confirmAutoPayEnroll")) {
			paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
			fwdPage = confirmAutoPayEnroll(servlet, request, response, paymentOption, validUser);
		}

		// Initiate Auto Pay Update Page
		if (actionCode.equalsIgnoreCase("initiateAutoPayUpdate")) {
			fwdPage = initiateAutoPayUpdate(servlet, request, response, validUser);
		}

		// Verify Auto Pay Update Page
		if (actionCode.equalsIgnoreCase("verifyAutoPayUpdate")) {
			fwdPage = verifyAutoPayUpdate(servlet, request, response);
		}

		// Confirm Auto Pay Update Page
		if (actionCode.equalsIgnoreCase("confirmAutoPayUpdate")) {
			paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
			fwdPage = confirmAutoPayUpdate(servlet, request, response, paymentOption, validUser);
		}

		// Initiate One Time Payment Page
		if (actionCode.equalsIgnoreCase("initiateOneTimePayment")) {
			fwdPage = initiateOneTimePayment(servlet, request, response);
		}

		// Verify One Time Payment Page
		if (actionCode.equalsIgnoreCase("verifyOneTimePayment")) {
			fwdPage = verifyOneTimePayment(servlet, request, response);
		}

		// Confirm One Time Payment Page
		if (actionCode.equalsIgnoreCase("confirmOneTimePayment")) {
			paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
			fwdPage = confirmOneTimePayment(servlet, request, response, paymentOption, validUser);
		}

		// Avoid double submission code
		if (!(fwdPage == null || "".equalsIgnoreCase(fwdPage.trim()))) {
			actionCode = "";
			request.setAttribute("actionCode", actionCode);
		}
		
		// Forwards to JSP Page
		String error = "";
		jspName = Util.getCMFile(fwdPage, context);
		forward(jspName + "?errorMsg=" + error, request, response);
	}

	private String showPaymentOptions(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response, ValidUserBean validUser) throws IOException, ServletException {
   	
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();

		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		paymentOption.setProperties(properties);

		// Get Account ID, Account Status and Latest Bill from Account Summary
		String accountId = validUser.getAccountId();

		// Check if Customer is Enrolled in Auto Pay - get Account Id from Account Summary
		paymentOption.getBankInformation(accountId);

		System.out.println("Is Auto Pay Enrolled: " + paymentOption.getIsAutoPayEnrolled());

		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
		
		return "CMSSvcPaymentOptions.jsp";
	}

	private String initiateAutoPayEnroll(final HttpServlet servlet, final HttpServletRequest request, 
			final HttpServletResponse response)	throws IOException, ServletException {

		return "CMSSvcAutoPayEnrollInitiation.jsp";
	}

	private String verifyAutoPayEnroll(final HttpServlet servlet, final HttpServletRequest request, 
			final HttpServletResponse response)	throws IOException, ServletException {

		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();

		System.out.println("hello verifyEnrollAutoPay!");
		System.out.println("bankAccountType1: "	+ request.getParameter("bankAccountType"));
		System.out.println("bankAccountNumber1: " + request.getParameter("bankAccountNumber"));
		System.out.println("bankRouteNumber1: "	+ request.getParameter("bankRouteNumber"));

		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		paymentOption.setProperties(properties);

		// Bank Account Type / Tender Type Description
		paymentOption.setBankAccountTypeCode(request.getParameter("bankAccountType"));

		// Bank Account Number / External Account ID
		paymentOption.setBankAccountNumber(request.getParameter("bankAccountNumber"));

		// Bank Routing Number / External Source ID
		paymentOption.setBankRoutingNumber(request.getParameter("bankRouteNumber"));

		// Bank Name / Auto Pay Source Code
		String description = "";
		if (request.getParameter("bankAccountType").equals("APSV")) {
			description = "%SAV";
			paymentOption.setBankAccountTypeDesc("Savings");
		} else {			
			description = "%CHK";
			paymentOption.setBankAccountTypeDesc("Checking");
		}
		
		paymentOption.setIsAutoPayInError(false);
		paymentOption.setIsInvalidBankRouteNumber(false);

		if (!paymentOption.searchAutoPaySourceCode(paymentOption, description)) {
			paymentOption.setIsInvalidBankRouteNumber(true);
		} else {
			if (paymentOption.getBankName() == null	|| paymentOption.getBankName().equals("")) {
				paymentOption.setIsInvalidBankRouteNumber(true);
			} else {
				if (!paymentOption.getBankAccountType(paymentOption.getBankName())) {
					paymentOption.setIsAutoPayInError(true);
					paymentOption.setAutoPaySourceName("");
					paymentOption.setBankAccountTypeCode("");
				}	
			}
		}
		
		String fwdPage = "";
		if (paymentOption.getIsInvalidBankRouteNumber()) {
			fwdPage = "CMSSvcAutoPayEnrollInitiation.jsp"; 		
		} else {
			if (paymentOption.getIsAutoPayInError()) {
				fwdPage = "CMSSvcAutoPayEnrollError.jsp";;
			} else {			
				fwdPage = "CMSSvcAutoPayEnrollVerification.jsp";
			}
		}

		session.removeAttribute("CMSSvcPaymentOptionsBean");
		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
		
		return fwdPage;
	}

	private String confirmAutoPayEnroll(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response,	CMSSvcPaymentOptionsBean paymentOption, ValidUserBean validUser)
			throws IOException, ServletException {

		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();
		boolean isChangeOk = false;

		System.out.println("hello confirmEnrollAutoPay!");

		CustomerContactBean customerContact = new CustomerContactBean();
		paymentOption.setProperties(properties);
		customerContact.setProperties(properties);

		// Get Person ID from Account Summary
		String personId = validUser.getPersonId();
		String accountId = validUser.getAccountId();
		System.out.println("get personId: " + personId);
		System.out.println("get accountId: " + accountId);

		// Get Customer's Other Details
		paymentOption.getPersonDetails(personId, paymentOption);

		paymentOption.setMaximumWithdrawalAmount("9999999");
		paymentOption.setAutoPayMethod("C1DD");
		
		// Create Auto Pay Record and Customer Contact
		if (paymentOption.createBankInformation(accountId, paymentOption)) {
			paymentOption.setIsAutoPayDateConflict(false);
			paymentOption.setIsAutoPayInError(false);

			// Bank Draft Set-Up Customer Contact
			customerContact.setPersonId(personId);
			customerContact.setContactClass("WEB");
			customerContact.setContactType("SETUPBD");
			customerContact.addCustomerContact();
		} else {
			int index1 = paymentOption.getErrorMessage().indexOf("Description: ");
			int index2 = paymentOption.getErrorMessage().indexOf("Table:");
			if (paymentOption.getErrorMessage().substring(index1 + 13, index2).trim()
					.equalsIgnoreCase("End Date must be on or after Start Date.")) {
				paymentOption.setIsAutoPayDateConflict(true);
			} else {
				paymentOption.setIsAutoPayInError(true);
			}
		}
		
		String fwdPage = "";
		if (paymentOption.getIsAutoPayDateConflict() || paymentOption.getIsAutoPayInError()) {
			fwdPage = "CMSSvcAutoPayEnrollError.jsp";
			isChangeOk = false;			
		} else {
			ControlCentralBean accountInfo = new ControlCentralBean();
			accountInfo.setAccountId(accountId);
        	accountInfo.setProperties(properties);
        	accountInfo.getPremiseInfo();
			request.setAttribute("accountInfo", accountInfo);
		
			fwdPage = "CMSSvcAutoPayEnrollConfirmation.jsp";
			isChangeOk = true;
		}
		
		if (isChangeOk) {
			CMEmailBean email = new CMEmailBean(properties);
            email.postAutoPayMail(paymentOption.getCustomerEmail(), "ENROLL", context, personId, accountId, paymentOption.getCustomerName());
		}
		
		session.removeAttribute("CMSSvcPaymentOptionsBean");
		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

		return fwdPage;
	}

	private String initiateAutoPayUpdate(final HttpServlet servlet, final HttpServletRequest request, 
			final HttpServletResponse response, ValidUserBean validUser) throws IOException, ServletException {
		
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();

		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		paymentOption.setProperties(properties);
		
		paymentOption.getBankInformation(validUser.getAccountId());	
		paymentOption.getBankAccountType(paymentOption.getBankName());

		session.removeAttribute("CMSSvcPaymentOptionsBean");
		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

		String fwdPage = "CMSSvcAutoPayUpdateInitiation.jsp";
		return fwdPage;
	}

	private String verifyAutoPayUpdate(final HttpServlet servlet, final HttpServletRequest request, 
			final HttpServletResponse response) throws IOException, ServletException {

		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();

		System.out.println("hello verifyChangeAutoPay!");
		System.out.println("bankAccountType2: "	+ request.getParameter("bankAccountType"));
		System.out.println("bankAccountNumber2: " + request.getParameter("bankAccountNumber"));
		System.out.println("bankRouteNumber2: "	+ request.getParameter("bankRouteNumber"));

		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		paymentOption.setProperties(properties);

		// Bank Account Type / Tender Type Description
		paymentOption.setBankAccountTypeCode(request.getParameter("bankAccountType"));

		// Bank Account Number / External Account ID
		paymentOption.setBankAccountNumber(request.getParameter("bankAccountNumber"));

		// Bank Routing Number / External Source ID
		paymentOption.setBankRoutingNumber(request.getParameter("bankRouteNumber"));

		// Bank Name / Auto Pay Source Code
		String description = "";
		if (request.getParameter("bankAccountType").equals("APSV")) {
			description = "%SAV";
			paymentOption.setBankAccountTypeDesc("Savings");
		} else {			
			description = "%CHK";
			paymentOption.setBankAccountTypeDesc("Checking");
		}
		
		paymentOption.setIsAutoPayInError(false);
		paymentOption.setIsInvalidBankRouteNumber(false);

		if (!paymentOption.searchAutoPaySourceCode(paymentOption, description)) {
			paymentOption.setIsInvalidBankRouteNumber(true);
		} else {
			if (paymentOption.getBankName() == null	|| paymentOption.getBankName().equals("")) {
				paymentOption.setIsInvalidBankRouteNumber(true);
			} else {
				if (!paymentOption.getBankAccountType(paymentOption.getBankName())) {
					paymentOption.setIsAutoPayInError(true);
					paymentOption.setAutoPaySourceName("");
					paymentOption.setBankAccountTypeCode("");
				}	
			}
		}		
		
		String fwdPage = "";
		if (paymentOption.getIsInvalidBankRouteNumber()) {
			fwdPage = "CMSSvcAutoPayUpdateInitiation.jsp"; 		
		} else {
			if (paymentOption.getIsAutoPayInError()) {
				fwdPage = "CMSSvcAutoPayUpdateError.jsp";;
			} else {			
				fwdPage = "CMSSvcAutoPayUpdateVerification.jsp";
			}
		}

		session.removeAttribute("CMSSvcPaymentOptionsBean");
		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

		return fwdPage;
	}

	private String confirmAutoPayUpdate(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response, CMSSvcPaymentOptionsBean paymentOption, ValidUserBean validUser)
			throws IOException, ServletException {

		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();
		boolean isChangeOk = false;

		System.out.println("hello confirmChangeAutoPay!");

		CustomerContactBean customerContact = new CustomerContactBean();
		paymentOption.setProperties(properties);
		customerContact.setProperties(properties);

		// Get Person ID from Account Summary
		String personId = validUser.getPersonId();
		String accountId = validUser.getAccountId();
		System.out.println("get personId: " + personId);
		System.out.println("get accountId: " + accountId);

		// Get Customer's Other Details
		paymentOption.getPersonDetails(personId, paymentOption);

		paymentOption.getAutoPayInformation(accountId);

		paymentOption.setMaximumWithdrawalAmount("9999999");
		paymentOption.setAutoPayMethod("C1DD");
		
		// Create Auto Pay Record and Customer Contact
		if (paymentOption.updateBankInformation(accountId, paymentOption) 
				&& paymentOption.createBankInformation(accountId, paymentOption)) {
					
			paymentOption.setIsAutoPayDateConflict(false);
			paymentOption.setIsAutoPayInError(false);

			// Bank Draft Update Customer Contact
			customerContact.setPersonId(personId);
			customerContact.setContactClass("WEB");
			customerContact.setContactType("UPDATEBD");
			customerContact.addCustomerContact();
		} else {
			int index1 = paymentOption.getErrorMessage().indexOf("Description: ");
			int index2 = paymentOption.getErrorMessage().indexOf("Table:");
			if (paymentOption.getErrorMessage().substring(index1 + 13, index2).trim()
					.equalsIgnoreCase("End Date must be on or after Start Date.")) {
				paymentOption.setIsAutoPayDateConflict(true);
			} else {
				paymentOption.setIsAutoPayInError(true);
			}
		}
		
		String fwdPage = "";
		if (paymentOption.getIsAutoPayDateConflict() || paymentOption.getIsAutoPayInError()) {
			fwdPage = "CMSSvcAutoPayUpdateError.jsp";
			isChangeOk = false;			
		} else {
			ControlCentralBean accountInfo = new ControlCentralBean();
			accountInfo.setAccountId(accountId);
        	accountInfo.setProperties(properties);
        	accountInfo.getPremiseInfo();
			request.setAttribute("accountInfo", accountInfo);
		
			fwdPage = "CMSSvcAutoPayUpdateConfirmation.jsp";
			isChangeOk = true;
		}
		
		if (isChangeOk) {
			CMEmailBean email = new CMEmailBean(properties);
            email.postAutoPayMail(paymentOption.getCustomerEmail(), "UPDATE", context, personId, accountId, paymentOption.getCustomerName());
		}

		session.removeAttribute("CMSSvcPaymentOptionsBean");
		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

		return fwdPage;
	}

	private String initiateOneTimePayment(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {

		return "CMSSvcOneTimePaymentInitiation.jsp";
	}

	private String verifyOneTimePayment(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {


		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();

		System.out.println("hello verifyOneTimePayment!");
		System.out.println("bankAccountType1: "	+ request.getParameter("bankAccountType"));
		System.out.println("bankAccountNumber1: " + request.getParameter("bankAccountNumber"));
		System.out.println("bankRouteNumber1: "	+ request.getParameter("bankRouteNumber"));
		System.out.println("paymentAmount: "	+ request.getParameter("paymentAmount"));

		CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		paymentOption.setProperties(properties);

		// Bank Account Type / Tender Type Description
		paymentOption.setBankAccountTypeCode(request.getParameter("bankAccountType"));

		// Bank Account Number / External Account ID
		paymentOption.setBankAccountNumber(request.getParameter("bankAccountNumber"));

		// Bank Routing Number / External Source ID
		paymentOption.setBankRoutingNumber(request.getParameter("bankRouteNumber"));

		// Bank Name / Auto Pay Source Code
		String description = "";
		if (request.getParameter("bankAccountType").equals("APSV")) {
			description = "%SAV";
			paymentOption.setBankAccountTypeDesc("Savings");
		} else {			
			description = "%CHK";
			paymentOption.setBankAccountTypeDesc("Checking");
		}
		
		paymentOption.setIsAutoPayInError(false);
		paymentOption.setIsInvalidBankRouteNumber(false);

		if (!paymentOption.searchAutoPaySourceCode(paymentOption, description)) {
			paymentOption.setIsInvalidBankRouteNumber(true);
		} else {
			if (paymentOption.getBankName() == null	|| paymentOption.getBankName().equals("")) {
				paymentOption.setIsInvalidBankRouteNumber(true);
			} else {
				if (!paymentOption.getBankAccountType(paymentOption.getBankName())) {
					paymentOption.setIsAutoPayInError(true);
					paymentOption.setAutoPaySourceName("");
					paymentOption.setBankAccountTypeCode("");
				}	
			}
		}
		
		String fwdPage = "";
		if (paymentOption.getIsInvalidBankRouteNumber()) {
			fwdPage = "CMSSvcOneTimePaymentInitiation.jsp"; 		
		} else {
			if (paymentOption.getIsAutoPayInError()) {
				fwdPage = "CMSSvcOneTimePaymentError.jsp";;
			} else {			
				fwdPage = "CMSSvcOneTimePaymentVerification.jsp";
			}
		}

		session.removeAttribute("CMSSvcPaymentOptionsBean");
		// check if auto pay needs to be created
		paymentOption.setCreateAutoPay(false);
		System.out.println("enrolAutoPay[" + request.getParameter("enrolAutoPay")+"]");
		if (request.getParameter("enrolAutoPay")!=null) {
			if (request.getParameter("enrolAutoPay").equals("on")){
				paymentOption.setCreateAutoPay(true);
			}
		}

		// Payment Amount / payment Amount
		paymentOption.setPaymentAmount(request.getParameter("paymentAmount"));
		System.out.println("Amount from bean[" + paymentOption.getPaymentAmount() + "]");
		System.out.println("Create from bean[" + paymentOption.getCreateAutoPay() + "]");

		session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
		
		return fwdPage;

	}

	private String confirmOneTimePayment(final HttpServlet servlet, final HttpServletRequest request,
			final HttpServletResponse response, CMSSvcPaymentOptionsBean paymentOption, ValidUserBean validUser)
			throws IOException, ServletException {

		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();
		boolean isChangeOk = false;


		CustomerContactBean customerContact = new CustomerContactBean();
		//paymentOption.setProperties(properties);
		customerContact.setProperties(properties);

		// Get Person ID from Account Summary
		String personId = validUser.getPersonId();
		String accountId = validUser.getAccountId();
		// Get Customer's Other Details
		paymentOption.getPersonDetails(personId, paymentOption);

		System.out.println("ENTITY NAME[" + paymentOption.getCustomerName() + "]");
		System.out.println("EMAIL[" + paymentOption.getCustomerEmail() + "]");
		System.out.println("APAY SRC CD[" + paymentOption.getBankName() + "]");
		System.out.println("EXT ACCT ID[" + paymentOption.getBankAccountNumber() + "]");
		System.out.println("EXT SRC ID[" + paymentOption.getBankRoutingNumber() + "]");
		System.out.println("PAY AMOUNT[" + paymentOption.getPaymentAmount() + "]");
	

		String fwdPage = "CMSSvcOneTimePaymentConfirmation.jsp";
		//add the payment event
		paymentOption.setProperties(properties);

		if (!paymentOption.createPaymentEvent(accountId)){
			System.out.println("you're dead meat");
			fwdPage = "CMSSvcOneTimePaymentError.jsp";
		} else {
			System.out.println("success");
			// Create Customer Contact and send email for payment			
			// Payment Customer Contact
			customerContact.setPersonId(personId);
			customerContact.setContactClass("WEB");
			customerContact.setContactType("OTPAY");
			customerContact.addCustomerContact();
			// Send email for payment
			CMEmailBean email = new CMEmailBean(properties);
           		email.postPaymentMail(paymentOption.getCustomerEmail(), "PAYMENT", context, personId, accountId, paymentOption.getCustomerName());


			// Check if customer opted to enroll in auto pay
			if (paymentOption.getCreateAutoPay())
			{

			paymentOption.setMaximumWithdrawalAmount("9999999");
			paymentOption.setAutoPayMethod("C1DD");
		
			// Create Auto Pay Record and Customer Contact
			if (paymentOption.createBankInformation(accountId, paymentOption)) {
				paymentOption.setIsAutoPayDateConflict(false);
				paymentOption.setIsAutoPayInError(false);

				// Bank Draft Set-Up Customer Contact
				customerContact.setPersonId(personId);
				customerContact.setContactClass("WEB");
				customerContact.setContactType("SETUPBD");
				customerContact.addCustomerContact();
			} else {
				int index1 = paymentOption.getErrorMessage().indexOf("Description: ");
				int index2 = paymentOption.getErrorMessage().indexOf("Table:");
				if (paymentOption.getErrorMessage().substring(index1 + 13, index2).trim()
						.equalsIgnoreCase("End Date must be on or after Start Date.")) {
					paymentOption.setIsAutoPayDateConflict(true);
				} else {
					paymentOption.setIsAutoPayInError(true);
				}
			}
		
			if (paymentOption.getIsAutoPayDateConflict() || paymentOption.getIsAutoPayInError()) {
				fwdPage = "CMSSvcAutoPayEnrollError.jsp";
				isChangeOk = false;			
			} else {
				ControlCentralBean accountInfo = new ControlCentralBean();
				accountInfo.setAccountId(accountId);
		        	accountInfo.setProperties(properties);
        			accountInfo.getPremiseInfo();
				request.setAttribute("accountInfo", accountInfo);
		
				fwdPage = "CMSSvcOneTimePaymentConfirmation.jsp";
				isChangeOk = true;
			}
		
			if (isChangeOk) {
				email = new CMEmailBean(properties);
            		email.postAutoPayMail(paymentOption.getCustomerEmail(), "ENROLL", context, personId, accountId, paymentOption.getCustomerName());
			}
			}
		
			session.removeAttribute("CMSSvcPaymentOptionsBean");
			session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

		}

		return fwdPage;
	}
	
	private void forward(String url, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}
}


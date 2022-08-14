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
 * This Java Bean will contain public constructors (get and set methods)
 * that will hold different Payment Options variables.
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2012-07-25  SMedin  CEd10. Payment Options Module. Initial Version.
 * 2014-02-26  ETiamzon SR-0003284.  Change CreatepaymentEvent to handle
 *                      customer names with '&'
 ************************************************************************
 */

package com.splwg.selfservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class CMSSvcPaymentOptionsBean implements java.io.Serializable {

	// ~ Instance fields
	// --------------------------------------------------------------------------------------

	private static final long serialVersionUID = 2L;
	private String bankName;
	private String bankRouteNumber;
	private String bankAcctNumber;
	private String bankAcctTypeCode;
	private String bankAcctTypeDesc;
	private String customerEmail;
	private String customerPerBus;
	private String customerName;
	private String customerAutoPayId;
	private String maximumWithdrawalAmount;
	private String autoPaySourceName;
	private String paymentAmount;
        private String paymentAmountOriginal;
    	private DecimalFormat numInput;
    	private DecimalFormat numOutput;
	public String formattedPaymentAmount;
	private boolean createAutoPay;
	private boolean isAutoPayEnrolled;
	private boolean isAutoPayDateConflict;
    	private boolean isAutoPayInError;
    	private boolean isInvalidBankRouteNumber;
        private String paymentTenderId;
	private Properties properties;
	private String errorMessage;

        private String autoPayMethod;

	// ~ Methods
	// ----------------------------------------------------------------------------------------------

	void setBankName(String value) {
		bankName = value;
	}

	public String getBankName() {
		return bankName;
	}

	void setBankRoutingNumber(String value) {
		bankRouteNumber = value;
	}

	public String getBankRoutingNumber() {
		return bankRouteNumber;
	}

	void setBankAccountNumber(String value) {
		bankAcctNumber = value;
	}

	public String getBankAccountNumber() {
		return bankAcctNumber;
	}

	void setBankAccountTypeCode(String value) {
		bankAcctTypeCode = value;
	}

	public String getBankAccountTypeCode() {
		return bankAcctTypeCode;
	}

	void setBankAccountTypeDesc(String value) {
		bankAcctTypeDesc = value;
	}

	public String getBankAccountTypeDesc() {
		return bankAcctTypeDesc;
	}

	void setMaximumWithdrawalAmount(String value) {
		maximumWithdrawalAmount = value;
	}

	public String getMaximumWithdrawalAmount() {
		return maximumWithdrawalAmount;
	}

	public String getPaymentAmount() {
        	return paymentAmount;
	}

	public void setPaymentAmount(String value) {
        	paymentAmount = value;
	}

	void setCustomerEmail(String value) {
		customerEmail = value;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	void setCustomerPerBus(String value) {
		customerPerBus = value;
	}

	public String getCustomerPerBus() {
		return customerPerBus;
	}

	void setCustomerName(String value) {
		customerName = value;
	}

	public String getCustomerName() {
		return customerName;
	}

	void setCustomerAutoPayId(String value) {
		customerAutoPayId = value;
	}

	public String getCustomerAutoPayId() {
		return customerAutoPayId;
	}

        void setAutoPaySourceName(String value){
    		autoPaySourceName = value;
	}

	public String getAutoPaySourceName() {
            	return autoPaySourceName;
	}

	void setPaymentTenderId(String value) {
		paymentTenderId = value;
	}

	public String getPaymentTenderId() {
		return paymentTenderId;
	}

	void setAutoPayMethod(String value) {
		autoPayMethod = value;
	}

	public String getAutoPayMethod() {
		return autoPayMethod;
	}

	void setIsAutoPayEnrolled(boolean value) {
		isAutoPayEnrolled = value;
	}

	public boolean getIsAutoPayEnrolled() {
		return isAutoPayEnrolled;
	}

	void setIsAutoPayDateConflict(boolean value){
    		isAutoPayDateConflict = value;
    	}

    	public boolean getIsAutoPayDateConflict() {
        	return isAutoPayDateConflict;
	}

	void setIsAutoPayInError(boolean value){
    		isAutoPayInError = value;
    	}

    	public boolean getIsAutoPayInError() {
        	return isAutoPayInError;
    	}

	void setCreateAutoPay(boolean value){
    		createAutoPay = value;
    	}

    	public boolean getCreateAutoPay() {
        	return createAutoPay;
    	}

    	void setIsInvalidBankRouteNumber(boolean value){
    		isInvalidBankRouteNumber = value;
	}

	public String getErrorMessage() {
        	return errorMessage;
    	}

    	public boolean getIsInvalidBankRouteNumber() {
        	return isInvalidBankRouteNumber;
    	}


	void setProperties(Properties value) {
		properties = value;
	}

	void getBankInformation(String accountId) {

		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CMSSvcAccountAutoPayListMaintenance transactionType='LIST'>\n")
				.append("<Autopayments>\n")
				.append("<AutopaymentsHeader AccountID = '").append(accountId).append("'/>\n")
				.append("</Autopayments>\n")
				.append("</CMSSvcAccountAutoPayListMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			System.out.println("\ngetBankInformation xml:\n" + xml);
			System.out.println("\ngetBankInformation response:\n" + httpResponse);
			Document document = DocumentHelper.parseText(httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
			}

			List list = document.selectNodes("//*[local-name()='AutopaymentsRow']");
			String endDate = "";

			SimpleDateFormat sdfInput;
			sdfInput = new SimpleDateFormat("yyyy-MM-dd");
			String formatCurrentDate = sdfInput.format(new Date());
			Date baseDate = sdfInput.parse("1800-01-01");
			Date startDate = sdfInput.parse(formatCurrentDate);

			if (list.size() == 0) {
				isAutoPayEnrolled = false;
			} else {
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Element ele = (Element) iter.next();

					endDate = Util.getValue(ele, "@EndDate");
					startDate = sdfInput.parse(Util.getValue(ele, "@StartDate"));

					if (startDate.after(baseDate)) {
						baseDate = startDate;

						if (endDate.equals("") || endDate == null || endDate.equals(" ")) {
							isAutoPayEnrolled = true;
							customerAutoPayId = Util.getValue(ele, "@AutoPayID");
							bankRouteNumber = Util.getValue(ele, "@ExternalSourceID");
			    				bankAcctNumber = Util.getValue(ele, "@ExternalAccountID");
				    			bankName = Util.getValue(ele, "@AutoPaySourceCode");
						} else {
							isAutoPayEnrolled = false;
						}
					}
				}
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	void getAutoPayInformation(String accountId) {

		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CMSSvcAccountAutoPayListMaintenance transactionType='LIST'>\n")
				.append("<Autopayments>\n")
				.append("<AutopaymentsHeader AccountID = '").append(accountId).append("'/>\n")
				.append("</Autopayments>\n")
				.append("</CMSSvcAccountAutoPayListMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			System.out.println("\ngetBankInformation xml:\n" + xml);
			System.out.println("\ngetBankInformation response:\n" + httpResponse);
			Document document = DocumentHelper.parseText(httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
			}

			List list = document.selectNodes("//*[local-name()='AutopaymentsRow']");
			String endDate = "";

			SimpleDateFormat sdfInput;
			sdfInput = new SimpleDateFormat("yyyy-MM-dd");
			String formatCurrentDate = sdfInput.format(new Date());
			Date baseDate = sdfInput.parse("1800-01-01");
			Date startDate = sdfInput.parse(formatCurrentDate);

			if (list.size() == 0) {
				isAutoPayEnrolled = false;
			} else {
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Element ele = (Element) iter.next();

					endDate = Util.getValue(ele, "@EndDate");
					startDate = sdfInput.parse(Util.getValue(ele, "@StartDate"));

					if (startDate.after(baseDate)) {
						baseDate = startDate;

						if (endDate.equals("") || endDate == null || endDate.equals(" ")) {
							isAutoPayEnrolled = true;
							customerAutoPayId = Util.getValue(ele, "@AutoPayID");
						} else {
							isAutoPayEnrolled = false;
						}
					}
				}
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	boolean searchAutoPaySourceCode(CMSSvcPaymentOptionsBean paymentOption, String description) {

		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CMSSvcAutoPaySourceSearch transactionType='SRCH' searchType='ALT'>\n")
				.append("<ApsSrch>\n")
				.append("<ApsSrchHeader ExternalSourceID = '").append(paymentOption.getBankRoutingNumber())
				.append("' Description='").append(description).append("'/>\n")
				.append("</ApsSrch>\n")
				.append("</CMSSvcAutoPaySourceSearch>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			Document document = DocumentHelper.parseText(httpResponse);
			System.out.println("\nsearchAutoPaySourceCode xml:\n" + xml);
			System.out.println("\nsearchAutoPaySourceCode response:\n" + httpResponse);
			System.out.println("hi3 searchAutoPaySourceCode");
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
				return (false);
			}

			List list = document.selectNodes("//*[local-name()='ApsSrchRow']");

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Element ele = (Element) iter.next();

				paymentOption.setBankName(Util.getValue(ele, "@AutoPaySourceCode"));
				bankName = Util.getValue(ele, "@AutoPaySourceCode");
			}

			if (bankName.equals("") || bankName == null || bankName.equals(" ")) {
				return (false);
			} else {
				return (true);
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return (false);
		}
	}

	boolean getBankAccountType(String autoPaySourceCode) {

    	String xml = new StringBuffer(512)
    			.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                .append("<SOAP-ENV:Body>\n")
                .append("<SSvcAutoPaySourceTypeMaintenance transactionType='READ'>\n")
                .append("<SSvcAutoPaySourceTypeMaintenanceService>\n")
                .append("<SSvcAutoPaySourceTypeMaintenanceHeader AutoPaySourceCode = '")
                .append(autoPaySourceCode.replaceAll("&", "&amp;")).append("'/>\n")
                .append("</SSvcAutoPaySourceTypeMaintenanceService>\n")
                .append("</SSvcAutoPaySourceTypeMaintenance>\n")
                .append("</SOAP-ENV:Body>\n")
                .append("</SOAP-ENV:Envelope>\n")
                .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            System.out.println ("\ngetBankAccountType xml:\n" + xml);
            System.out.println ("\ngetBankAccountType response:\n" + httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
                return (false);
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAutoPaySourceTypeMaintenanceDetails']");
            bankAcctTypeCode = Util.getValue(ele, "@TenderType");
            autoPaySourceName = Util.getValue(ele, "@AutoPaySourceName");
            return (true);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            return (false);
        }
    }

	boolean createBankInformation(String accountId, CMSSvcPaymentOptionsBean paymentOption) {

		String dtNow = getDate();

		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CDxAccountMaintenance transactionType='UPDATE'>\n")
				.append("<CDxAccountMaintenanceService>\n")
				.append("<CDxAccountMaintenanceHeader AccountID = '").append(accountId).append("'/>\n")
				.append("<CDxAccountMaintenanceDetails DefaultFlag='A' AccountID = '").append(accountId).append("'>\n")
				.append("<Autopayments>\n")
				.append("<AutoPaymentsHeader AccountID = '").append(accountId).append("'/>\n")
				.append("<AutoPaymentsRow rowAction='ADD' AccountID = '").append(accountId)
				.append("' AutoPaySourceCode='").append(paymentOption.getBankName().replaceAll("&", "&amp;"))
				.append("' ExternalAccountID='").append(paymentOption.getBankAccountNumber())
				.append("' ExternalSourceID='").append(paymentOption.getBankRoutingNumber())
				.append("' AutoPayMethod='").append(paymentOption.getAutoPayMethod())
				.append("' MaximumWithdrawalAmount='").append(paymentOption.getMaximumWithdrawalAmount())
				.append("' Name='").append(paymentOption.getCustomerName().replaceAll("&", " "))
				.append("' EntityName='").append(paymentOption.getCustomerName().replaceAll("&", " "))
				.append("' StartDate ='").append(dtNow).append("'/>\n")
				.append("</Autopayments>\n")
				.append("</CDxAccountMaintenanceDetails>\n")
				.append("</CDxAccountMaintenanceService>\n")
				.append("</CDxAccountMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			System.out.println("\ncreateBankInformation xml:\n" + xml);
			String httpResponse = httpCall.callXAIServer(xml);
			System.out.println("\ncreateBankInformation response:\n" + httpResponse);
			Document document = DocumentHelper.parseText(httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
				return (false);
			}
			return (true);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return (false);
		}
	}


	boolean updateBankInformation(String accountId,	CMSSvcPaymentOptionsBean paymentOption) {

		String dtNow = getYesterdayDate();

                String xml = new StringBuffer(512)
                                .append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                .append("<SOAP-ENV:Body>\n")
                                .append("<CDxAccountMaintenance transactionType='UPDATE'>\n")
                                .append("<CDxAccountMaintenanceService>\n")
                                .append("<CDxAccountMaintenanceHeader AccountID='").append(accountId).append("'/>\n")
                                .append("<CDxAccountMaintenanceDetails DefaultFlag='C' AccountID='").append(accountId).append("'>\n")
                                .append("<Autopayments>\n")
                                .append("<AutoPaymentsHeader AccountID='").append(accountId).append("'/>\n")
                                .append("<AutoPaymentsRow rowAction='CHANGE' AccountID='").append(accountId)
                                .append("' AutoPayID='").append(paymentOption.getCustomerAutoPayId())
                                .append("' EndDate='").append(dtNow).append("'/>\n")
                                .append("</Autopayments>\n")
                                .append("</CDxAccountMaintenanceDetails>\n")
                                .append("</CDxAccountMaintenanceService>\n")
                                .append("</CDxAccountMaintenance>\n")
                                .append("</SOAP-ENV:Body>\n")
                                .append("</SOAP-ENV:Envelope>\n")
                                .toString();


		String xml1 = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CmSSvcAccountMaintenance transactionType='UPDATE'>\n")
				.append("<CmSSvcAccountMaintenanceService>\n")
				.append("<CmSSvcAccountMaintenanceHeader AccountID = '").append(accountId).append("'/>\n")
				.append("<CmSSvcAccountMaintenanceDetails AccountID = '").append(accountId).append("'>\n")
				.append("<AutoPayments>\n")
				.append("<AutoPaymentsHeader AccountID = '").append(accountId).append("'/>\n")
				.append("<AutoPaymentsRow rowAction='CHANGE' AccountID = '").append(accountId)
				.append("' AutoPayID='").append(paymentOption.getCustomerAutoPayId())
				.append("' EndDate ='").append(dtNow).append("'/>\n")
				.append("</AutoPayments>\n")
				.append("</CmSSvcAccountMaintenanceDetails>\n")
				.append("</CmSSvcAccountMaintenanceService>\n")
				.append("</CmSSvcAccountMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		String xmlx = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CMSSvcAccountAutoPayListMaintenance transactionType='UPDATE'>\n")
				.append("<Autopayments>\n")
				.append("<AutopaymentsHeader AccountID = '").append(accountId).append("'/>\n")
				.append("<AutopaymentsRow rowAction='CHANGE' AccountID = '").append(accountId)
				.append("' AutoPayID='").append(paymentOption.getCustomerAutoPayId())
				.append("' EndDate ='").append(dtNow).append("'/>\n")
				.append("</Autopayments>\n")
				.append("</CMSSvcAccountAutoPayListMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			System.out.println("\nupdateBankInformation xml:\n" + xml);

			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);

			System.out.println("\nupdateBankInformation response:\n" + httpResponse);

			Document document = DocumentHelper.parseText(httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
				return (false);
			}
			return (true);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return (false);
		}
	}

	void getPersonDetails(String personId, CMSSvcPaymentOptionsBean paymentOption) {

		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<SSvcPersonMaintenance transactionType = 'READ'>\n")
				.append("<SSvcPersonMaintenanceService>\n")
				.append("<SSvcPersonMaintenanceHeader PersonID='").append(personId).append("'/>\n")
				.append("</SSvcPersonMaintenanceService>\n")
				.append("</SSvcPersonMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			Document document = DocumentHelper.parseText(httpResponse);
			System.out.println("\ngetCustomerId xml:\n" + xml);
			System.out.println("\ngetCustomerId response:\n" + httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
			}
			// If Customer Class = Residential
			Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");

			System.out.println("EmailID: " + Util.getValue(ele, "@EmailID"));
			System.out.println("PersonBusiness: " + Util.getValue(ele, "@PersonBusiness"));
			System.out.println("Name: " + Util.getValue(ele, "@Name"));

			paymentOption.setCustomerEmail(Util.getValue(ele, "@EmailID"));
			paymentOption.setCustomerPerBus(Util.getValue(ele, "@PersonBusiness"));
			paymentOption.setCustomerName(Util.getValue(ele, "@Name"));
			customerEmail = Util.getValue(ele, "@EmailID");
			customerPerBus = Util.getValue(ele, "@PersonBusiness");
			customerName = Util.getValue(ele, "@Name");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	boolean createPaymentEvent(String accountId) {

		String dtNow = getDate();
		String xml = new StringBuffer(512)
				.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
				.append("<SOAP-ENV:Body>\n")
				.append("<CDxPaymentEventMaintenance dateTimeTagFormat='CdxDateTime' transactionType ='ADD'>\n")
				.append("<CDxPaymentEventMaintenanceService>\n")
				.append("<CDxPaymentEventMaintenanceHeader/>\n")
				.append("<CDxPaymentEventMaintenanceDetails PayorAccountID='").append(accountId)
				.append("' PaymentDate='").append(dtNow)
				.append("' AmountTendered='").append(paymentAmount).append("'>\n")
				.append("<PaymentTender><PaymentTenderRow rowAction='Add' PayorAccountID='").append(accountId)
				.append("' TenderType='").append(bankAcctTypeCode)
				.append("' TenderAmount='").append(paymentAmount)
				.append("' CustomerID='").append(accountId)
				.append("' Name='").append(customerName.replaceAll("&", " "))
				.append("' ExternalReferenceID='").append(bankRouteNumber)
				.append("' ScheduleExtractDate='").append(dtNow)
				.append("' AutoPaySourceCode='").append(bankName)
				.append("' ExternalAccountID='").append(bankAcctNumber)
				.append("' Name2='").append(getCustomerName().replaceAll("&", " "))
				.append("' AutoPayDistributionandFreezeDate='").append(dtNow)
				.append("'></PaymentTenderRow></PaymentTender>\n")
				.append("<TenderType><TenderTypeRow rowAction='Add' TenderedAmount='").append(paymentAmount)
				.append("' TenderType='").append(bankAcctTypeCode).append("' ")
				.append("Comment='Test payment using Apay checking'>")
				.append("</TenderTypeRow></TenderType>\n")
				.append("</CDxPaymentEventMaintenanceDetails>\n")
				.append("</CDxPaymentEventMaintenanceService>\n")
				.append("</CDxPaymentEventMaintenance>\n")
				.append("</SOAP-ENV:Body>\n")
				.append("</SOAP-ENV:Envelope>\n")
				.toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			Document document = DocumentHelper.parseText(httpResponse);
			System.out.println("\ncreatePaymentEvent xml:\n" + xml);
			System.out.println("\ncreatePaymentEvent response:\n" + httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				errorMessage = error.getText();
				return (false);
			}

			Element ele = (Element) document.selectSingleNode("//*[local-name()='PaymentTenderRow']");
			setPaymentTenderId(Util.getValue(ele, "@PayTenderID"));
			System.out.println("PaymentTender Id["+getPaymentTenderId()+"]");

			if (!updateAPayStagingRecord(getPaymentTenderId())){
				System.out.println("Update failed");
			}else {
				System.out.println("Update successful");
			}
			return (true);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return (false);
		}
	}

	boolean updateAPayStagingRecord(String paymentTenderId) {
		String xml = new StringBuffer(512)
			.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>")
                	.append("<SOAP-ENV:Body>")
                        .append("<CmUpdateAutoPayClearStagingBatchCode dateTimeTagFormat='CdxDateTime'  transactionType ='UPDATE'>")
                        .append("<CmUpdateAutoPayClearStagingBatchCodeService>")
                        .append("<CmUpdateAutoPayClearStagingBatchCodeHeader")
                        .append(" BatchControl='CMAPYCLT'").append(" PayTenderID='").append(paymentTenderId)
                        .append("' /><CmUpdateAutoPayClearStagingBatchCodeDetails PayTenderID='").append(paymentTenderId)
			.append("' BatchControl='CMAPYCLT'>")
                        .append("</CmUpdateAutoPayClearStagingBatchCodeDetails>")
                        .append("</CmUpdateAutoPayClearStagingBatchCodeService>")
                        .append("</CmUpdateAutoPayClearStagingBatchCode>")
                	.append("</SOAP-ENV:Body>")
			.append("</SOAP-ENV:Envelope>")
			.toString();
		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
			String httpResponse = httpCall.callXAIServer(xml);
			Document document = DocumentHelper.parseText(httpResponse);
			System.out.println("\nupdatestage xml:\n" + xml);
			System.out.println("\nupdatestage response:\n" + httpResponse);
			Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
			if (faultEle != null) {
				//Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
				//errorMessage = error.getText();
				System.out.println("Update failed");
				return (false);
			}
			List list = document.selectNodes("//*[local-name()='PaymentTenderRow']");

			//setPaymentTenderId(Util.getValue(ele, "@PayTenderID"));
			System.out.println("Update successful");

			return (true);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return (false);
		}

	}


	public String getDate() {

		// yyyy-MM-dd Format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getYesterdayDate() {

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Today's date is " + dateFormat.format(cal.getTime()));

		cal.add(Calendar.DATE, -1);
		System.out.println("Yesterday's date was " + dateFormat.format(cal.getTime()));
		return dateFormat.format(cal.getTime());
	}
}

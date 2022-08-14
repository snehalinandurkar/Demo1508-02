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
 * This Java Bean is a clone of AccountPersonBean.java
 * with additional custom functions added to the base code. 
 *
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2011-12-20  SMedin  SR0511. Initial Version.
 ************************************************************************
 */

package com.splwg.selfservice;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class CMAccountPersonBean extends AccountPersonBean {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String personId;
    private String accountInfo;
    private String currentBalance;
    private String mainCustomer;
    private String billRouteType;
    private String billRouteMessage;
    private Properties properties;
    private String errorMsg;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getPersonId() {
        return accountId;
    }

    void setPersonId(String value) {
        personId = value;
    }


    public String getAccountInfo() {
        return Util.decode(accountInfo);
    }

    void setAccountInfo(String value) {
        accountInfo = value;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    void setCurrentBalance(String value) {
        currentBalance = value;
    }
    
    public String getMainCustomer() {
        return mainCustomer;
    }

    public String getBillRouteType() {
        return billRouteType;
    }

    void setBillRouteType(String value) {
        billRouteType = value;
    }
    
    public String getBillRouteMessage() {
        return billRouteMessage;
    }

    void setBillRouteMessage(String value) {
    	billRouteMessage = value;
    }
        
    void setProperties(Properties value) {
        properties = value;
    }
    
    public String getErrorMessage() {
        return errorMsg;
    }
    
    public String retrieveBillRouteType(String accountId) {
    	String tempBillRouteType="NOT E-BILL";
    	
    	String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
        								  .append("		<SOAP-ENV:Body>\n")
        								  .append("			<CMSSvcAccountPersonListMaintenance transactionType ='LIST'  trace ='no'>")
        								  .append("		    	<AccountPersons>\n")
        								  .append("		        	<AccountPersonsHeader AccountID='").append(accountId).append("'/>\n")
        								  .append("		    	</AccountPersons>\n")
        								  .append("			</CMSSvcAccountPersonListMaintenance>\n")
        								  .append("		</SOAP-ENV:Body>\n")
        								  .append("</SOAP-ENV:Envelope>\n")
        								  .toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            System.out.println("xml retrieveBillRouteType:\n" + xml);
            System.out.println("response retrieveBillRouteType:\n" + httpResponse);
            
            if (faultEle != null) {
            	Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (errorMsg);
            }
            List list = document.selectNodes("//*[local-name()='AccountPersonsRow']");
    	    for (Iterator iter = list.iterator(); iter.hasNext();) {
        	    Element ele = (Element) iter.next();
        	    if(Util.getValue(ele, "@AccountRelationshipType").equalsIgnoreCase("main") &&
        	    		Util.getValue(ele, "@MainCustomer").equalsIgnoreCase("true") &&
        	    		Util.getValue(ele, "@FinanciallyResponsible").equalsIgnoreCase("true") &&
        	    		Util.getValue(ele, "@ReceivesCopyofBill").equalsIgnoreCase("true")){ 
        	    	System.out.println("retrieveBillRouteType5");
        	    	tempBillRouteType = Util.getValue(ele, "@BillRouteType");
        	    }
    	    }
    	    return (tempBillRouteType);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (errorMsg);
        }	
    }
    public String retrieveFields(String accountId, String personId) {
    	
    	String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
        								  .append("		<SOAP-ENV:Body>\n")
        								  .append("			<CMSSvcAccountPersonListMaintenance transactionType ='LIST'  trace ='no'>")
        								  .append("		    	<AccountPersons>\n")
        								  .append("		        	<AccountPersonsHeader AccountID='").append(accountId).append("'/>\n")
        								  .append("		    	</AccountPersons>\n")
        								  .append("			</CMSSvcAccountPersonListMaintenance>\n")
        								  .append("		</SOAP-ENV:Body>\n")
        								  .append("</SOAP-ENV:Envelope>\n")
        								  .toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            System.out.println("xml retrieveBillRouteType:\n" + xml);
            System.out.println("response retrieveBillRouteType:\n" + httpResponse);
            
            if (faultEle != null) {
            	Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (errorMsg);
            }
            List list = document.selectNodes("//*[local-name()='AccountPersonsRow']");
            System.out.println("personId ["+personId+"]");

    	    for (Iterator iter = list.iterator(); iter.hasNext();) {
        	    Element ele = (Element) iter.next();
		    if (Util.getValue(ele, "@PersonID").equalsIgnoreCase(personId)){
                      System.out.println("personId ["+personId+"]");
			    mainCustomer = Util.getValue(ele, "@MainCustomer");
			    billRouteType = Util.getValue(ele, "@BillRouteType");
                      System.out.println("Person ID found");
                      return(billRouteType);
        	    }
    	    }
          System.out.println("Person ID not found");
          return(billRouteType);

        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (errorMsg);
        }	
    }
}

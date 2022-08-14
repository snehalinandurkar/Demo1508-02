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
 * This Java Bean is a new module to retrieve the Person Identifier
 * clue in unmasked form to replace retrieval using the base person 
 * maintenance service which returns masked data.
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2012-03-29  EPagad  CEd9. Initial Version.
 ************************************************************************
 */

package com.splwg.selfservice;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class CMPersonIdentifierBean 
		implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String personIdentifierType;
    private String personIdentifierValue;
    private Properties properties;
    private String errorMsg;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    void setPersonId(String value) {
        personId = value;
    }

    public String getPersonIdentifierType() {
        return personIdentifierType;
    }

    void setPersonIdentifierType(String value) {
        personIdentifierType = value;
    }

    public String getPersonIdentifierValue() {
        return retrievePersonIdentifier(personId,personIdentifierType);
    }

    void setProperties(Properties value) {
        properties = value;
    }
    
    public String getErrorMessage() {
        return errorMsg;
    }
    
    public String retrievePersonIdentifier(String personId, String personIdentifierType) {
    	String tempPersonIdentifierValue="";
    	
    	String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
        								  .append("		<SOAP-ENV:Body>\n")
        								  .append("			<CmSSvcPersonIdentifiersList transactionType ='LIST'>\n")
        								  .append("		    	<PersonIdentifiers>\n")
        								  .append("		        	<PersonIdentifiersHeader PersonID='").append(personId).append("'/>\n")
        								  .append("		    	</PersonIdentifiers>\n")
        								  .append("			</CmSSvcPersonIdentifiersList>\n")
        								  .append("		</SOAP-ENV:Body>\n")
        								  .append("</SOAP-ENV:Envelope>\n")
        								  .toString();

		try {
			XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            	String httpResponse = httpCall.callXAIServer(xml);
	            Document document = DocumentHelper.parseText(httpResponse);
      	      Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            	System.out.println("xml retrievePersonIdentifier:\n" + xml);
	            System.out.println("response retrievePersonIdentifier:\n" + httpResponse);
            
      	      if (faultEle != null) {
            		Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
	            	errorMsg = error.getText();
	                	return (errorMsg);
	            }
            	List list = document.selectNodes("//*[local-name()='PersonIdentifiersRow']");
    	    		for (Iterator iter = list.iterator(); iter.hasNext();) {
        	    	Element ele = (Element) iter.next();
        	    	if(Util.getValue(ele, "@IDType").equalsIgnoreCase(personIdentifierType)) { 
        	    		System.out.println("retrieveBillRouteType5");
        	    		tempPersonIdentifierValue = Util.getValue(ele, "@IDNumber");
        	    	}
    	    	}
    	    	return (tempPersonIdentifierValue);
        	
		} catch (Exception e) {
            	errorMsg = "Caught exception: " + e.getMessage();
            	return (errorMsg);
        	}	
	}
}

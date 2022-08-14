/*
 * PasswordBean.java
 *
 * Created in January 11, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used for storing and retrieving the password onto the person record
 *
 */

package com.splwg.selfservice;

import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class PasswordBean
        implements IAuthentication {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String userId = "";
    private String entityName;
    private String passwordPlain;
    private String passwordEncrypted;
    private String passwordQuestion;
    private String passwordAnswer;
    private String receiveMarketingInfo;
    private String eMail;
    private String primaryIdentifier;

    private HttpSession session;
    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private boolean personFound;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountsForPersonBean */
    public PasswordBean() {
    }

    public PasswordBean(Properties value, HttpSession session) {
        properties = value;
        this.session = session;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setPersonId(String value) {
        personId = value;
        infoRetrieved = false;
    }

    public void setUserId(String value) {
        userId = value;
    }

    public void setPrimaryIdentifier(String value) {
        primaryIdentifier = value;
    }

    public void setPassword(String value) {
        passwordPlain = value;
        infoRetrieved = false;
    }

    void setPasswordPlain(String value) {
        passwordPlain = value;
        infoRetrieved = false;
    }

    void setPasswordQuestion(String value) {
        passwordQuestion = value;
        infoRetrieved = false;
    }

    void setPasswordAnswer(String value) {
        passwordAnswer = value;
        infoRetrieved = false;
    }

    void setReceiveMarketingInfo(String value) {
        receiveMarketingInfo = value;
        infoRetrieved = false;
    }

    void setEMail(String value) {
        eMail = value;
        infoRetrieved = false;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public boolean updatePerson() {
        boolean webIdExists = false;

        EncryptionBean encrypt = new EncryptionBean();
        try {
            encrypt.setPasswordPlain(passwordPlain);
            passwordEncrypted = encrypt.encryptPassword();
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }

        if (!userId.equals("")) {
            RetrievePersonFromUserIdBean ret = new RetrievePersonFromUserIdBean(properties);
            ret.setUserId(userId);

            if (ret.retrievePerson()) {
                errorMsg = "USEREXISTS";
                return (false);
            }
        }

        String idType = properties.getProperty("com.splwg.selfservice.IdType");

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonMaintenance transactionType='UPDT'>\n").append("<SSvcPersonMaintenanceService>\n")
                .append("<SSvcPersonMaintenanceHeader PersonID = '").append(personId).append("'/>\n").append(
                        "<SSvcPersonMaintenanceDetails PersonID = '").append(personId).append("' WebPassword='")
                .append(passwordEncrypted).append("' WebPasswordHintFlag='").append(passwordQuestion).append(
                        "' WebPasswordAnswer='").append(XMLEncoder.encode(check(passwordAnswer))).append(
                        "' ReceiveMarketingInfoFlag='").append(receiveMarketingInfo).append("' EmailID='").append(
                        XMLEncoder.encode(check(eMail))).append("'>\n").toString();

        // First we delete any possibly existing web id, then we add it (functional review item #57)
        if (!userId.equals("")) {
            xml = xml.concat("<PersonIdentifiers>\n");
            if (webIdExists) {
                xml = xml.concat("<PersonIdentifiersRow rowAction='Delete' PersonID='");
                xml = xml.concat(personId);
                xml = xml.concat("' IDType='");
                xml = xml.concat(idType);
                xml = xml.concat("'/>\n");
            }
            xml = xml.concat("<PersonIdentifiersRow rowAction='Add' IDNumber='");
            xml = xml.concat(userId);
            xml = xml.concat("' IDType='");
            xml = xml.concat(idType);
            xml = xml.concat("' PrimaryID='");
            xml = xml.concat(primaryIdentifier);
            xml = xml.concat("' PersonID='");
            xml = xml.concat(personId);
            xml = xml.concat("'/>\n");
            xml = xml.concat("</PersonIdentifiers>\n");
        }

        xml = xml.concat("</SSvcPersonMaintenanceDetails>\n");
        xml = xml.concat("</SSvcPersonMaintenanceService>\n");
        xml = xml.concat("</SSvcPersonMaintenance>\n");
        xml = xml.concat("</SOAP-ENV:Body>\n");
        xml = xml.concat("</SOAP-ENV:Envelope>\n");

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }

    public boolean validatePassword() {
        String databasePassword;
        EncryptionBean encrypt = new EncryptionBean();
        try {
            encrypt.setPasswordPlain(passwordPlain);
            passwordEncrypted = encrypt.encryptPassword();
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }

        RetrievePersonFromUserIdBean ret = new RetrievePersonFromUserIdBean(properties);
        ret.setUserId(userId);
	  System.out.println("RetrievePerson is: [" + ret.retrievePerson() + "]");
	  System.out.println("RetrievePerson is: [" + ret.getPersonId() + "]");
        if (ret.retrievePerson()) {
            personId = ret.getPersonId();
        } else {
            errorMsg = ret.getErrorMessage();
            return (false);
        }

        String xml2 = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonMaintenance transactionType='READ'>\n").append("<SSvcPersonMaintenanceService>\n")
                .append("<SSvcPersonMaintenanceHeader PersonID = '").append(personId).append("'/>\n").append(
                        "</SSvcPersonMaintenanceService>\n").append("</SSvcPersonMaintenance>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();
        ;

	System.out.println("xml2: [" + xml2 + "]");

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml2);
            Document document = DocumentHelper.parseText(httpResponse);
		System.out.println("httpResponse is: [" + httpResponse + "]");
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");
		
            databasePassword = Util.getValue(ele, "@WebPassword");
		System.out.println("databasePassword is: [" + databasePassword + "]");
            entityName = Util.getValue(ele, "@Name");
            if (passwordEncrypted.equals(databasePassword))
                return (true);
            else {
                return (false);
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }

    public boolean retrieveEntityName() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonMaintenance transactionType='READ'>\n").append("<SSvcPersonMaintenanceService>\n")
                .append("<SSvcPersonMaintenanceHeader PersonID = '").append(personId).append("'/>\n").append(
                        "</SSvcPersonMaintenanceService>\n").append("</SSvcPersonMaintenance>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");
            entityName = Util.getValue(ele, "@Name");
            return true;
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return false;
        }
    }

    private String check(String text) {
        if (text == null) text = "";
        if (text.startsWith(" ")) text = text.substring(1);

        return text;
    }

}

/*
 * PasswordReminderBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves password information used for the 'Forgot Password' page
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PasswordReminderBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String userId;
    private String passwordPlain;
    private String passwordEncrypted;
    private String passwordQuestion;
    private String passwordAnswer;
    private String email;
    private String receiveMarketingInfo;

    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private boolean personFound;
    private boolean newId;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountsForPersonBean */
    public PasswordReminderBean() {}

    public PasswordReminderBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    public String getUserId() {
        return Util.decode(userId);
    }

    public String getPasswordQuestion() {
        return Util.decode(passwordQuestion);
    }

    public String getPasswordAnswer() {
        return Util.decode(passwordAnswer);
    }

    public String getPasswordPlain() {
        return passwordPlain;
    }

    public String getEmail() {
        return email;
    }

    public String getReceiveMarketingInfo() {
        return receiveMarketingInfo;
    }

    public boolean getNewId() {
        return newId;
    }

    void setPersonId(String value) {
        personId = value;
        infoRetrieved = false;
    }

    void setUserId(String value) {
        userId = value;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public boolean retrieveData() {
        String xml = new StringBuffer(512).append("<?xml version='1.0' encoding='UTF-16'?>\n")
                                              .append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcPersonMaintenance transactionType='READ'>\n")
                                              .append("<SSvcPersonMaintenanceService>\n")
                                              .append("<SSvcPersonMaintenanceHeader PersonID = '")
                                              .append(personId).append("'/>\n")
                                              .append("</SSvcPersonMaintenanceService>\n")
                                              .append("</SSvcPersonMaintenance>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();
        ;

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            List list = document.selectNodes("//*[local-name()='PersonIdentifiersRow']");
            if (list.size() > 0) {
                newId = false;
            } else {
                newId = true;
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");

            passwordQuestion = Util.decode(Util.getValue(ele, "@WebPasswordHintFlag"));

            // if (passwordQuestion.equals(" ")) {             // since the change not to use the person id as user, this is unnecessary
            //   errorMsg = "No password has been stored for this user";
            //   return (false);
            // }
            PasswordHintBean phb = new PasswordHintBean(properties);
            HashMap map = phb.getQuestions();
            passwordQuestion = String.valueOf(map.get(passwordQuestion));

            passwordAnswer = Util.decode(Util.getValue(ele, "@WebPasswordAnswer"));
            passwordEncrypted = Util.getValue(ele, "@WebPassword");
            email = Util.getValue(ele, "@EmailID");
            receiveMarketingInfo = Util.getValue(ele, "@ReceiveMarketingInfoFlag");
            if (! passwordEncrypted.equals("")) {
                EncryptionBean decrypt = new EncryptionBean();
                decrypt.setEncryptedInput(passwordEncrypted);
                passwordPlain = decrypt.decryptPassword();
            } else {
                passwordPlain = "";
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }
}

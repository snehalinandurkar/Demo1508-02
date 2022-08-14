/*
 * StopServiceBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used for changing the status of an SA to Pending Stop.
 *
 */

package com.splwg.selfservice;

import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class StopServiceBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String personId;
    private String saId;
    private String stopDate;
    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new SAforAccountBean */
    public StopServiceBean() {
    }

    public StopServiceBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getPersonId() {
        return personId;
    }

    void setPersonId(String value) {
        personId = value;
    }

    public String getSAId() {
        return saId;
    }

    void setSAId(String value) {
        saId = value;
    }

    public String getStopDate() {
        return stopDate;
    }

    void setStopDate(String value) {
        stopDate = value;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public boolean callStopSAService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcStartStopMaintenance transactionType='UPDT'>\n").append(
                        "<SSvcStartStopMaintenanceService>\n").append("<SSvcStartStopMaintenanceHeader AccountID = '")
                .append(accountId).append("'/>\n").append(
                        "<SSvcStartStopMaintenanceDetails StopMethod='SA' ACTIONSTOPSW='true' AccountID = '").append(
                        accountId).append("' StopServiceagreementID = '").append(saId).append("' STOPENDDT = '")
                .append(stopDate).append("'/>\n").append("</SSvcStartStopMaintenanceService>\n").append(
                        "</SSvcStartStopMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

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
            addCustomerContact();
            addToDoEntry();
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }

    private void addCustomerContact() {
        String contactClass = properties.getProperty("com.splwg.selfservice.CustomerContactClassServiceDisconnect");

        String contactType = properties.getProperty("com.splwg.selfservice.CustomerContactTypeServiceDisconnect");

        if (!contactType.equals("NONE") && !contactType.equals("")) {
            String comments;
            comments = "SA Id: " + saId;

            CustomerContactBean cb = new CustomerContactBean();
            cb.setPersonId(personId);
            cb.setProperties(properties);
            cb.setContactClass(contactClass);
            cb.setContactType(contactType);
            cb.setComments(comments);
            cb.addCustomerContact();
        }
    }

    private void addToDoEntry() {
        String toDoType = properties.getProperty("com.splwg.selfservice.ServiceStopToDoType");

        if (!toDoType.equals("NONE") && !toDoType.equals("")) {
            ToDoBean td = new ToDoBean();
            td.setToDoType(toDoType);
            td.setDrillKey(saId);
            td.setSource("SA");
            td.setSortKey1(saId);
            td.setProperties(properties);
            td.addToDoEntry();
        }
    }
}

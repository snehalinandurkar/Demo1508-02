/*
 * CustomerContactBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Creates a customer contact according to input, like customer class, type and comments
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

public class CustomerContactBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String contactType;
    private String contactClass;
    private Properties properties;
    private String errorMsg;
    private String customerContactId;
    private String comments;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new SAforAccountBean */
    public CustomerContactBean() {}

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    void setPersonId(String value) {
        personId = value;
    }

    public String getCustomerContactId() {
        return customerContactId;
    }

    public String getContactType() {
        return Util.decode(contactType);
    }

    void setContactType(String value) {
        contactType = value;
    }

    public String getContactClass() {
        return Util.decode(contactClass);
    }

    void setContactClass(String value) {
        contactClass = value;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    void setComments(String value) {
        comments = XMLEncoder.encode(value);
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public boolean addCustomerContact() {
        String user = properties.getProperty(
                                             "com.splwg.selfservice.CorDaptixUser");
        SimpleDateFormat sdf = new SimpleDateFormat(
                                                    properties.getProperty(
                                                                           "com.splwg.selfservice.XAIDateTimeFormat"));
        String contactDateTime = sdf.format(new Date());

        if (comments == null) comments = "";

        if (comments.length() > 254) comments = comments.substring(0, 254);

        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcCustomerContactMaintenance transactionType = 'Add'>\n")
                                              .append("<SSvcCustomerContactMaintenanceService>\n")
                                              .append("<SSvcCustomerContactMaintenanceDetails ")
                                              .append("User = '").append(user).append("' ")
                                              .append("PersonID = '").append(personId).append("' ")
                                              .append("ContactDateTime = '").append(contactDateTime)
                                              .append("' ").append("ContactClass = '").append(contactClass)
                                              .append("' ").append("ContactType = '").append(contactType)
                                              .append("' LongDescription='").append(comments).append("'/>\n")
                                              .append("</SSvcCustomerContactMaintenanceService>\n")
                                              .append("</SSvcCustomerContactMaintenance>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                errorMsg = errorMsg.replace('\012', ' '); // replace new line by blank

                System.out.println("SOAP Fault at Customer Contact: " + errorMsg);
                errorMsg = errorMsg.replace('\047', '\042'); // replace ' by "
                int len = errorMsg.length();
                if (len > 202) {
                    errorMsg = errorMsg.substring(0, 202);
                }
                addToDoEntry();
                return false;
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcCustomerContactMaintenanceDetails']");
            customerContactId = Util.getValue(ele, "@CustomerContactID");
        } catch (Exception e) {
            System.out.println("Caught exception in Customer Contact Creation: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void addToDoEntry() {
        String toDoType = properties.getProperty("com.splwg.selfservice.AdminToDoType");
        if (! toDoType.equals("NONE") && ! toDoType.equals("")) {
            ToDoBean td = new ToDoBean();
            td.setToDoType(toDoType);
            td.setDrillKey("11111111");
            td.setProperties(properties);
            td.setSource("ADMIN");
            td.setComments("Customer Contact could not be created due to error: " + errorMsg);
            td.addToDoEntry();
        }
    }
}

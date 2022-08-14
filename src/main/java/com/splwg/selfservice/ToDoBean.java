/*
 * ToDoBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Creates a to do entry
 *
 */

package com.splwg.selfservice;

import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class ToDoBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String toDoType;
    private String drillKey;
    private String sortKey1;
    private String sortKey2;
    private String sortKey3;
    private String source;
    private String comments = "";
    private String errorMsg;
    private Properties properties;
    private String errorMessage;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new SAforAccountBean */
    public ToDoBean() {
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    void setToDoType(String value) {
        toDoType = value;
    }

    void setDrillKey(String value) {
        drillKey = value;
    }

    void setSortKey1(String value) {
        sortKey1 = value;
    }

    void setSortKey2(String value) {
        sortKey2 = value;
    }

    void setSortKey3(String value) {
        sortKey3 = value;
    }

    void setComments(String value) {
        comments = value;
    }

    void setSource(String value) {
        source = value;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMessage);
    }

    public boolean addToDoEntry() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcToDoEntryMaintenance transactionType = 'Add'>\n").append(
                        "<SSvcToDoEntryMaintenanceService >\n").append(
                        "<SSvcToDoEntryMaintenanceDetails ToDoEntryStatus='O' ToDoType= '").append(toDoType).append(
                        "' Comments='").append(comments).append("' SendTo='").append("USER").append(
                        "' MessageParameterText='").append("WSS To Do Entry").append("'>\n").toString();

        if (source.equals("MR")) {
            xml = xml.concat("<TdEntrySrtky>\n");
            xml = xml.concat("<TdEntrySrtkyRow rowAction='add' Sequence='1' KeyValue='");
            xml = xml.concat(sortKey1);
            xml = xml.concat("'/>\n");
            xml = xml.concat("<TdEntrySrtkyRow rowAction='add' Sequence='2' KeyValue='");
            xml = xml.concat(sortKey2);
            xml = xml.concat("'/>\n");
            xml = xml.concat("<TdEntrySrtkyRow rowAction='add' Sequence='3' KeyValue='");
            xml = xml.concat(sortKey3);
            xml = xml.concat("'/>\n");
            xml = xml.concat("</TdEntrySrtky>\n");
        } else if (source.equals("PER") || source.equals("SA")) {
            xml = xml.concat("<TdEntrySrtky>\n");
            xml = xml.concat("<TdEntrySrtkyRow rowAction='add' Sequence='1' KeyValue='");
            xml = xml.concat(sortKey1);
            xml = xml.concat("'/>\n");
            xml = xml.concat("</TdEntrySrtky>\n");
        }
        xml = xml.concat("<TdEntryDrlky>\n");
        xml = xml.concat("<TdEntryDrlkyRow rowAction='add' Sequence='1' KeyValue='");
        xml = xml.concat(drillKey);
        xml = xml.concat("'/>\n");
        xml = xml.concat("</TdEntryDrlky>\n");
        xml = xml.concat("</SSvcToDoEntryMaintenanceDetails>\n");
        xml = xml.concat("</SSvcToDoEntryMaintenanceService>\n");
        xml = xml.concat("</SSvcToDoEntryMaintenance>\n");

        xml = xml.concat("</SOAP-ENV:Body>\n");
        xml = xml.concat("</SOAP-ENV:Envelope>\n");

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                System.out.println("SOAP Fault at To Do Entry Creation with error: " + errorMsg);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Caught exception in To Do Entry Creation: " + e.getMessage());
            return false;
        }
        return true;
    }
}

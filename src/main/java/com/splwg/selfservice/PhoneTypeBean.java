/*
 * PhoneTypeBean.java
 *
 * Created on June 12th 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieve the phone type description
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Properties;

public class PhoneTypeBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String phoneType;
    private String description;
    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    public PhoneTypeBean() {}

    /** Creates new Bean */
    public PhoneTypeBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    void setPhoneType(String value) {
        phoneType = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public String getDescription() {
        return Util.decode(description);
    }

    public boolean retrievePhoneType() {
        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcPhoneTypeMaintenance>\n")
                                              .append("<SSvcPhoneTypeMaintenanceService>\n")
                                              .append("<SSvcPhoneTypeMaintenanceHeader PhoneType='")
                                              .append(phoneType).append("'/>\n")
                                              .append("</SSvcPhoneTypeMaintenanceService>\n")
                                              .append("</SSvcPhoneTypeMaintenance>\n")
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

            Element ele = (Element) document.selectSingleNode("//*[local-name()='PhoneTypeRow']");

            description = Util.decode(Util.getValue(ele, "@Description"));
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
        return (true);
    }
}

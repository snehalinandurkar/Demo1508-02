/*
 * AdjustmentBean.java
 *
 * Created in April 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves the adjustment type in order to read the PrintbyDefault attribute
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Properties;

public class AdjustmentTypeBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String adjustmentType;
    private String printFlag;
    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new Bean */
    public AdjustmentTypeBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    void setAdjustmentType(String value) {
        adjustmentType = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public String getPrintFlag() {
        return Util.decode(printFlag);
    }

    public boolean retrieveAdjustmentType() {
        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcAdjustmentTypeMaintenance>\n")
                                              .append("<SSvcAdjustmentTypeMaintenanceService>\n")
                                              .append("<SSvcAdjustmentTypeMaintenanceHeader AdjustmentType='")
                                              .append(adjustmentType).append("'/>\n")
                                              .append("</SSvcAdjustmentTypeMaintenanceService>\n")
                                              .append("</SSvcAdjustmentTypeMaintenance>\n")
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

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAdjustmentTypeMaintenanceDetails']");
            printFlag = Util.getValue(ele, "@PrintbyDefault");
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
        return (true);
    }
}

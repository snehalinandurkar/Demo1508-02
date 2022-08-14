/*
 * ViewBillBean.java
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

public class ViewBillBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String billId;
    private String outputPdf;
    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    public ViewBillBean() {}

    /** Creates new Bean */
    public ViewBillBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    void setBillId(String value) {
        billId = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public String getOutputPdf() {
        return Util.decode(outputPdf);
    }

    public boolean retrieveOutputPdf() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CM-COMPBILLEXTRACT>\n")
                                              .append("<billId>")
                                              .append(billId)
                                              .append("</billId>\n")
                                              .append("</CM-COMPBILLEXTRACT>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();
        ;

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-CompletedBillExtractService", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='CM-COMPBILLEXTRACT']");

            outputPdf = Util.decode(Util.getValue(ele, "@outputPdf"));
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
        return (true);
    }
}

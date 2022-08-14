/*
 * AccountHostAllocationsBean.java
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SubscriberStatementBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String isSuccessful;
    private String filePath;
    private String resultId;
    

    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    //~ Constructors -----------------------------------------------------------------------------------------

    public SubscriberStatementBean() {
        infoRetrieved = false;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getCurrencyPosition() {
        return currencyPosition;
    }

    void setCurrencyPosition(String value) {
        currencyPosition = value;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    void setCurrencySymbol(String value) {
        currencySymbol = value;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String value) {
        infoRetrieved = false;
        resultId = value;
    }

    public String getIsSuccessful() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberStatementService();
        }
        return isSuccessful;
    }

    public void setIsSuccessful(String value) {
        isSuccessful = value;
    }

    public String getFilePath() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberStatementService();
        }
        return filePath;
    }

    public void setFilePath(String value) {
        filePath = value;
    }

    void setProperties(Properties value) {
        properties = value;
        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));
        numInput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private String formatCurrencyValue(String value) {
        if (currencyPosition.equals("PR")) return currencySymbol + value;
        else return value + currencySymbol;
    }

    private boolean callSubscriberStatementService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CmViewStmt>\n")
                                              .append("<input>\n")
                                              .append("<inputSubAllocResultId>")
                                              .append(resultId)
                                              .append("</inputSubAllocResultId>\n")
                                              .append("</input>\n")
                                              .append("</CmViewStmt>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ViewStatement", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            Element det = (Element) document.selectSingleNode("//*[local-name()='processingResultDetails']");
            if (det != null) {
                setIsSuccessful(Util.getValue(det, "isProcessSuccessful"));
                setFilePath(Util.getValue(det, "pdfFilePath"));
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

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

public class StatementHistoryBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountNumber;
    private String cssAcctId;
    private ArrayList resultList;
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

    public StatementHistoryBean() {
        resultList = new ArrayList();
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String value) {
        accountNumber = value;
        infoRetrieved = false;
    }

    public String getCssAcctId() {
        return cssAcctId;
    }

    public void setCssAcctId(String value) {
        cssAcctId = value;
    }

    public ArrayList getResultList() {
        if (! infoRetrieved) {
            infoRetrieved = callStatementHistoryService();
        }
        return resultList;
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

    private boolean callStatementHistoryService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CMRetStmtHis>\n")
                                              .append("<input>\n")
                                              .append("<inputAccountId>")
                                              .append(accountNumber)
                                              .append("</inputAccountId>\n")
                                              .append("</input>\n")
                                              .append("</CMRetStmtHis>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            resultList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ReturnStatementHistory", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
	    Element det = (Element) document.selectSingleNode("//*[local-name()='statementHistoryDetails']");
	    if (det != null) {
		setCssAcctId(Util.getValue(det, "cssAcctId"));
	    }

            List list = document.selectNodes("//*[local-name()='statementHistoryList']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                AllocationResultBean ar = new AllocationResultBean();
                ar.setResultId(Util.getValue(ele, "subAllocResultId"));
                ar.setPeriod(Util.getValue2(ele, "creditPeriod", sdfInput, sdfOutput));
                ar.setStatementDate(Util.getValue(ele, "statementDate", sdfInput, sdfOutput));
                ar.setCreditAmount(Util.getValue(ele, "creditAmount"));
                ar.setBalance(Util.getValue(ele, "balance"));
                ar.setSubscriberPlanId(Util.getValue(ele, "subAllocPlanId"));
                ar.setHostAllocationId(Util.getValue(ele, "hostAllocationId"));

                resultList.add(ar);
            }

            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

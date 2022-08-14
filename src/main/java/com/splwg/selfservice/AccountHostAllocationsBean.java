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

public class AccountHostAllocationsBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String cssAcctId;
    private String lastStatementDate;
    private ArrayList transactionList;
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

    /** Creates new AccountHostAllocationsBean */
    public AccountHostAllocationsBean() {
        transactionList = new ArrayList();
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

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
        infoRetrieved = false;
    }
    
    public String getcssAcctId() {
        return cssAcctId;
    }

    void setcssAcctId(String value) {
        cssAcctId = value;
        infoRetrieved = false;
    }

    public String getLastStatementDate() {
        return lastStatementDate;
    }

    void setLastStatementDate(String value) {
        lastStatementDate = value;
    }

    public ArrayList getTransactionList() {
        if (! infoRetrieved) {
            infoRetrieved = callAccountHostAllocationsService();
        }
        return transactionList;
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

    private boolean callAccountHostAllocationsService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CMRetAcHsAlc>\n")
                                              .append("<input>\n")
                                              .append("<inputHostAccountId>")
                                              .append(accountId)
                                              .append("</inputHostAccountId>\n")
                                              .append("</input>\n")
                                              .append("</CMRetAcHsAlc>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            transactionList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("Cm-ReturnAccountHostAllocations", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element det = (Element) document.selectSingleNode("//*[local-name()='hostAccountDetails']");
            if (det != null) {
		setcssAcctId(Util.getValue(det, "cssAcctId"));
	    }

            det = (Element) document.selectSingleNode("//*[local-name()='contactDetails']");
            if (det != null) {
		setLastStatementDate(Util.getValue(det, "lastStatementDate", sdfInput, sdfOutput));
	    }

 
            String createDateString = "";
            String effectiveDateString = "";
            List list = document.selectNodes("//*[local-name()='hostAllocationList']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                HostAllocationBean ha = new HostAllocationBean();
                ha.setAllocationId(Util.getValue(ele, "hostAllocationId"));
                ha.setStatus(Util.getValue(ele, "hostAllocationStatus"));
                ha.setAllocationTypeDescription(Util.getValue(ele, "hostAllocationTypeDescr"));
                createDateString = Util.getValue(ele, "hostAllocationCreDttm");
                if (! createDateString.equals(" ")) {
                    Date createDate = sdfInput.parse(Util.getValue(ele, "hostAllocationCreDttm"));
                    ha.setCreateDate(sdfOutput.format(createDate));
                } else {
                    ha.setCreateDate(" ");
                }

                effectiveDateString = Util.getValue(ele, "hostAllocationEffDttm");
                if (! effectiveDateString.equals(" ")) {
                    Date effDate = sdfInput.parse(Util.getValue(ele, "hostAllocationEffDttm"));
                    ha.setEffectiveDate(sdfOutput.format(effDate));
                } else {
                    ha.setEffectiveDate(" ");
                }

                transactionList.add(ha);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

/*
 * BillingHistoryBean.java
 *
 * Created on March 8 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads billing history information
 */

package com.splwg.selfservice;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class BillingHistoryBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _AccountId;
    private ArrayList _BillingHistoryList;
    private Properties _Properties;
    private String errorMsg;
    private boolean _AllInfoRetrieved;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new BillingHistoryBean */
    public BillingHistoryBean() {
        _BillingHistoryList = new ArrayList();
        _AllInfoRetrieved = false;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return _AccountId;
    }

    void setAccountId(String value) {
        _AccountId = value;
        _AllInfoRetrieved = false;
    }

    public ArrayList getBillingHistoryList() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callAccountSummaryService();
        }
        return _BillingHistoryList;
    }

    void setProperties(Properties value) {
        _Properties = value;
        sdfInput = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));
        numInput = new DecimalFormat(_Properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(_Properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    private boolean callAccountSummaryService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcBillsForAccount>\n").append("<Bills>\n").append("<BillsHeader AccountID = '").append(
                        _AccountId).append("'/>\n").append("</Bills>\n").append("</SSvcBillsForAccount>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

        try {
            _BillingHistoryList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return false;
            }
            List list = document.selectNodes("//*[local-name()='BillsRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                String billDate;
                String billId = Util.getValue(ele, "@BillID");
                String dateString = Util.getValue(ele, "@BillDate");
		    System.out.println("Bill Date Raw HistBean[" + dateString + "]");
                if (!dateString.equals(" ")) {
                    Date billDt = sdfInput.parse(Util.getValue(ele, "@BillDate"));
                    billDate = sdfOutput.format(billDt);
			  System.out.println("Bill Date Parsed HistBean[" + billDate + "]");
                } else {
                    billDate = "";
                }
                Number amount = numInput.parse(Util.getValue(ele, "@CurrentBillAmount"));
                String currentAmount = numOutput.format(amount);
                Number balance = numInput.parse(Util.getValue(ele, "@CurrentAmount"));
                String currentBalance = numOutput.format(balance);
                BillBean bill = new BillBean(billId, billDate, currentAmount, currentBalance);
                bill.setCurrencyPosition(currencyPosition);
                bill.setCurrencySymbol(currencySymbol);
                _BillingHistoryList.add(bill);
            }
            return true;
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }
    }

    public String getCurrencyPosition() {
        return currencyPosition;
    }

    public void setCurrencyPosition(String currencyPosition) {
        this.currencyPosition = currencyPosition;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}

/*
 * AccountFinancialHistoryBean.java
 *
 * Created on February 26, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads the account financial history
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

public class AccountFinancialHistoryBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
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

    /** Creates new AccountFinancialHistoryBean */
    public AccountFinancialHistoryBean() {
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

    public ArrayList getTransactionList() {
        if (! infoRetrieved) {
            infoRetrieved = callAccountFinancialHistoryService();
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

    private boolean callAccountFinancialHistoryService() {
        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcAccountFinancialHistoryList>\n")
                                              .append("<AccountFinancialTransactions>\n")
                                              .append("<AccountFinancialTransactionsHeader AccountID = '")
                                              .append(accountId).append("'/>\n")
                                              .append("</AccountFinancialTransactions>\n")
                                              .append("</SSvcAccountFinancialHistoryList>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();
        ;

        try {
            transactionList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            String arrearsDateString = "";
            List list = document.selectNodes("//*[local-name()='AccountFinancialTransactionsRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                FinancialTransactionBean ft = new FinancialTransactionBean();
                ft.setAccountId(Util.getValue(ele, "@AccountID"));
                arrearsDateString = Util.getValue(ele, "@ArrearsDate");
                if (! arrearsDateString.equals(" ")) {
                    Date arrearsDate = sdfInput.parse(Util.getValue(ele, "@ArrearsDate"));
                    ft.setArrearsDate(sdfOutput.format(arrearsDate));
                } else {
                    ft.setArrearsDate("New Charge");
                }
                String tranType = Util.getValue(ele, "@FinancialTransactionType");
                if (tranType.equals("BS")) {
                    ft.setTransactionTypeDescription("Bill");
                } else if (tranType.equals("PS")) {
                    ft.setTransactionTypeDescription("Payment");
                } else {
                    ft.setTransactionTypeDescription(Util.decode(Util.getValue(ele, "@Description")));
                }

                Number amount = numInput.parse(Util.getValue(ele, "@CurrentAmount"));
                ft.setCurrentAmount(formatCurrencyValue(numOutput.format(amount)));
                Number balance = numInput.parse(Util.getValue(ele, "@CurrentBalance"));
                ft.setCurrentBalance(formatCurrencyValue(numOutput.format(balance)));
                if (tranType.equals("AD")) {
                    AdjustmentTypeBean ad = new AdjustmentTypeBean(properties);
                    ad.setAdjustmentType(Util.getValue(ele, "@Parent"));
                    if (! ad.retrieveAdjustmentType()) {
                        errorMsg = ad.getErrorMessage();
                        return (false);
                    }
                    if (ad.getPrintFlag().equals("true")) {
                        transactionList.add(ft);
                    }
                } else {
                    transactionList.add(ft);
                }
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

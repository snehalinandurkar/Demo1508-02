/*
 * ControlCentralBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads account and all related information displayed on the Account information screen (excluding SA info)
 */

package com.splwg.selfservice;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class ControlCentralBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String mainCustomerPersonId;
    private String premiseId;
    private String entityName;
    private String premiseInfo;
    private String lastPayment;
    private String lastBill;
    private String lastBillId;
    private String nextBillDate;
    private String currentBalance;
    private String currentBalanceWithoutCurrency;
    private String currentBalanceUnformatted;
    private String homePhone;
    private String businessPhone;
    private String email;
    private Properties properties;
    private String errorMsg;
    private String notes;
    private boolean accountSummaryRetrieved;
    private boolean customerInfoRetrieved;
    private boolean financialInfoRetrieved;
    private boolean alertZoneRetrieved;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    private final String LAST_PAYMENT = "Last Payment";
    private final String LAST_BILLED = "Last Billed";
    private final String NEXT_BILL_DATE = "Next Bill Date";
    private final String BILL_ID = "BILL_ID";

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
        accountSummaryRetrieved = false;
        customerInfoRetrieved = false;
        financialInfoRetrieved = false;
    }

    public String getMainCustomerPersonId() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return mainCustomerPersonId;
    }

    public String getPremiseId() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return premiseId;
    }

    public String getEntityName() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return entityName;
    }

    public String getPremiseInfo() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return premiseInfo;
    }

    public String getNotes() {
        if (!alertZoneRetrieved) {
            alertZoneRetrieved = callAlertZoneService();
        }
        return notes;
    }

    public String getLastPayment() {
        if (!financialInfoRetrieved) {
            financialInfoRetrieved = callFinancialInfoZoneService();
        }
        return lastPayment;
    }

    public String getLastBill() {
        if (!financialInfoRetrieved) {
            financialInfoRetrieved = callFinancialInfoZoneService();
        }
        return lastBill;
    }

    public String getLastBillId() {
        if (!financialInfoRetrieved) {
            financialInfoRetrieved = callFinancialInfoZoneService();
        }
        return lastBillId;
    }

    public String getNextBillDate() {
        if (!financialInfoRetrieved) {
            financialInfoRetrieved = callFinancialInfoZoneService();
        }
        return nextBillDate;
    }

    public String getCurrentBalance() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return currentBalance;
    }

    public String getCurrentBalanceWithoutCurrency() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return currentBalanceWithoutCurrency;
    }

    public String getCurrentBalanceUnformatted() {
        if (!accountSummaryRetrieved) {
            accountSummaryRetrieved = callAccountSummaryService();
        }
        return currentBalanceUnformatted;
    }

    public String getHomePhone() {
        if (!customerInfoRetrieved) {
            customerInfoRetrieved = callCustomerInfoZoneService();
        }
        return homePhone;
    }

    public String getBusinessPhone() {
        if (!customerInfoRetrieved) {
            customerInfoRetrieved = callCustomerInfoZoneService();
        }
        return businessPhone;
    }

    public String getEmail() {
        if (!customerInfoRetrieved) {
            customerInfoRetrieved = callCustomerInfoZoneService();
        }
        return email;
    }

    void setProperties(Properties value) {
        properties = value;
        numInput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private boolean callAccountSummaryService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcAccountSummary>\n").append("<SSvcAccountSummaryService>\n").append(
                        "<SSvcAccountSummaryHeader AccountID = '").append(accountId).append("'/>\n").append(
                        "</SSvcAccountSummaryService>\n").append("</SSvcAccountSummary>\n")
                .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

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
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAccountSummaryDetails']");
            mainCustomerPersonId = Util.getValue(ele, "@PersonID");
            entityName = Util.decode(Util.getValue(ele, "@Name"));
            premiseId = Util.getValue(ele, "@PremiseID");
            premiseInfo = Util.decode(Util.getValue(ele, "@PremiseInfo"));
            premiseInfo = retrieveServiceAddress(premiseInfo);
            Number balance = numInput.parse(Util.getValue(ele, "@CurrentBalance"));
            currentBalanceWithoutCurrency = numOutput.format(balance);
            currentBalance = formatCurrencyValue(numOutput.format(balance));
            currentBalanceUnformatted = formatCurrencyValue(Util.getValue(ele, "@CurrentBalance"));
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }

    private String formatCurrencyValue(String value) {
        String strCurrencyValue = "";
        if (currencyPosition.equals("PR"))
            strCurrencyValue = currencySymbol + value;
        else
            strCurrencyValue = value + currencySymbol;
        return strCurrencyValue;
    }

    private boolean callCustomerInfoZoneService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcCustomerInfoZone>\n").append("<SSvcCustomerInfoZoneService>\n").append(
                        "<SSvcCustomerInfoZoneHeader AccountID = '").append(accountId).append("' PersonID = '").append(
                        getMainCustomerPersonId()).append("'/>\n").append("</SSvcCustomerInfoZoneService>\n").append(
                        "</SSvcCustomerInfoZone>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

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
            List list = document.selectNodes("//*[local-name()='ZoneRow']");

            String homePhoneLabel = properties.getProperty("com.splwg.selfservice.CustomerInformationHomePhone");
            String businessPhoneLabel = properties.getProperty("com.splwg.selfservice.CustomerInformationBusinessPhone");
            String emailAddressLabel = properties.getProperty("com.splwg.selfservice.CustomerInformationEmailAddress");

            homePhone = "";
            businessPhone = "";
            email = "";
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                String label = Util.getValue(ele, "@FieldLabel");
                String value = Util.getValue(ele, "@FieldValue");
                // CEd9 - SMedina - Start Modify
                //if (label.equals(homePhoneLabel)) homePhone = value;
                //if (label.equals(businessPhoneLabel)) businessPhone = value;
                if (label.equals(homePhoneLabel)) {
                    homePhone = Util.decode(value);
		    }
                if (label.equals(businessPhoneLabel)){
			  businessPhone = Util.decode(value);
		    }
                // CEd9 - SMedina - End Modify

                //if (label.equals(emailAddressLabel)) email = Util.replaceChar(value, "&apos;", "'");
                if (label.equals(emailAddressLabel)) email = Util.decode(value);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }

    private boolean callFinancialInfoZoneService() {
        Date paymentDate;
        Date billDate;

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcFinancialInfoZone>\n").append("<SSvcFinancialInfoZoneService>\n").append(
                        "<SSvcFinancialInfoZoneHeader AccountID = '").append(accountId).append("'/>\n").append(
                        "</SSvcFinancialInfoZoneService>\n").append("</SSvcFinancialInfoZone>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

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
            List list = document.selectNodes("//*[local-name()='ZoneRow']");
            lastPayment = "";
            lastBill = "";
            lastBillId = "";
            nextBillDate = "";
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                String fieldValue = Util.getValue(ele, "@FieldValue");

                //Get Last Payment
                int fieldPosition = fieldValue.indexOf(LAST_PAYMENT);
                if (fieldPosition >= 0) {
                    lastPayment = fieldValue.substring(fieldPosition + LAST_PAYMENT.length() + 1, fieldValue.length());
                }

                //Get Last Bill
                fieldPosition = fieldValue.indexOf(LAST_BILLED);
                if (fieldPosition >= 0) {
                    lastBill = fieldValue.substring(fieldPosition + LAST_BILLED.length() + 1, fieldPosition
                            + LAST_BILLED.length() + 11);
                    List keyList = document.selectNodes("//*[local-name()='KeyRow']");
                    for (Iterator keyIter = keyList.iterator(); keyIter.hasNext();) {
                        Element keyEle = (Element) keyIter.next();
                        Element parentEle = keyEle.getParent().getParent();
                        if (Util.getValue(parentEle, "@FieldValue").indexOf(LAST_BILLED) >= 0
                                && keyEle.valueOf("@KeyName").equals(BILL_ID)) {
                            lastBillId = keyEle.valueOf("@KeyValue");
                        }
                    }
                }

                //Get Next Bill Date
                fieldPosition = fieldValue.indexOf(NEXT_BILL_DATE);
                if (fieldPosition >= 0) {
                    nextBillDate = fieldValue.substring(fieldPosition + NEXT_BILL_DATE.length() + 1, fieldValue
                            .length());
                }
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }

    private boolean callAlertZoneService() {
        String value;
        notes = "";
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcAlertZone>\n").append("<SSvcAlertZoneService>\n").append(
                        "<SSvcAlertZoneHeader AccountID = '").append(accountId).append("' PersonID = '").append(
                        getMainCustomerPersonId()).append("' PremiseID = '").append(getPremiseId()).append("'/>")
                .append("</SSvcAlertZoneService>\n").append("</SSvcAlertZone>\n").append("</SOAP-ENV:Body>\n").append(
                        "</SOAP-ENV:Envelope>\n").toString();

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

            List list = document.selectNodes("//*[local-name()='ZoneRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                value = Util.getValue(ele, "@FieldValue").trim();
                notes = notes.concat(value) + "; ";
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }

    private String retrieveServiceAddress(String accountServiceAddress) {
        String addressString = accountServiceAddress;
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPremisesForAccountList>\n").append("<AccountPremise>\n").append(
                        "<AccountPremiseHeader AccountID = '").append(accountId).append("'/>").append(
                        "</AccountPremise>\n").append("</SSvcPremisesForAccountList>\n").append("</SOAP-ENV:Body>\n")
                .append("</SOAP-ENV:Envelope>\n").toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
            }

            List list = document.selectNodes("//*[local-name()='AccountPremiseRow']");
            if (list.size() > 1) {
                addressString = "Multiple";
            }
            return addressString;
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return "";
        }
    }
}

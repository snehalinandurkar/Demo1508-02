/*
 * AccountBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads account information
 *
 */

package com.splwg.selfservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class AccountBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String customerId;
    private String currencySymbol;
    private String currencyPosition;
    private String currencyCode;
    private String billAddressSource;
    private String mailingPremiseId;
    private String entityName;
    private HashMap accountCharacteristics;
    private String webPass;
    private Properties properties;
    private String errorMessage;
    private boolean infoRetrieved;
    private String quoteRouteType;
    private String receivesCopyOfQuote;
    private String billRouteType;
    private String receivesCopyOfBill;
    private String numberOfBillCopies;
    private String billFormat;
    private String country;
    private String language;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountBean */
    public AccountBean() {
        accountCharacteristics = new HashMap();
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getCurrencyPosition() {
        return currencyPosition;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
        infoRetrieved = false;
    }

    public String getCustomerId() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return customerId;
    }

    public String getBillAddressSource() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return billAddressSource;
    }

    public String getMailingPremiseId() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return mailingPremiseId;
    }

    public String getEntityName() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return entityName;
    }

    public HashMap getAccountCharacteristics() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return accountCharacteristics;
    }

    public String getWebPass() {
        if (!infoRetrieved) {
            infoRetrieved = callAccountService();
        }
        return webPass;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setBillAddressSource(String personId, String value) {
        billAddressSource = value;

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcAccountMaintenance transactionType='Update'>\n").append(
                        "<SSvcAccountMaintenanceService>\n").append("<SSvcAccountMaintenanceHeader AccountID = '")
                .append(accountId).append("'/>\n").append("<SSvcAccountMaintenanceDetails AccountID='").append(
                        accountId).append("'>\n").append("<AccountPersons>\n").append(
                        "<AccountPersonsRow rowAction='Change' AccountID='").append(accountId).append("' AccountID2='")
                .append(accountId).append("' PersonID='").append(personId).append("' ChildPerson='").append(personId)
                .append("' Country2='").append(country).append("' Language='").append(language).append(
                        "' AccountRelationshipType='MAIN' BillAddressSource='").append(billAddressSource).append(
                        "' QuoteRouteType='").append(quoteRouteType).append("' ReceivesCopyofQuote='").append(
                        receivesCopyOfQuote).append("' BillRouteType='").append(billRouteType).append(
                        "' ReceivesCopyofBill='").append(receivesCopyOfBill).append("' NumberofBillCopies='").append(
                        numberOfBillCopies).append("' BillFormat='").append(billFormat).append("'/>\n").append(
                        "</AccountPersons>\n").append("</SSvcAccountMaintenanceDetails>\n").append(
                        "</SSvcAccountMaintenanceService>\n").append("</SSvcAccountMaintenance>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

        String httpResponse = null;
        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    public boolean callAccountService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcAccountMaintenance transactionType = 'Read'>\n").append(
                        "<SSvcAccountMaintenanceService>\n").append("<SSvcAccountMaintenanceHeader AccountID = '")
                .append(accountId).append("'/>\n").append("</SSvcAccountMaintenanceService>\n").append(
                        "</SSvcAccountMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

        String httpResponse = null;
        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
                return false;
            }

            infoRetrieved = true;
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAccountMaintenanceDetails']");
            mailingPremiseId = Util.decode(Util.getValue(ele, "@MailingPremise"));
            entityName = Util.decode(Util.getValue(ele, "@Name"));

            billAddressSource = "";
            customerId = "";
            List list = document.selectNodes("//*[local-name()='AccountPersonsRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                ele = (Element) iter.next();
                if (Util.getValue(ele, "@MainCustomer").equals("true")) {
                    billAddressSource = Util.decode(Util.getValue(ele, "@BillAddressSource"));
                    customerId = Util.decode(Util.getValue(ele, "@PersonID"));
                    quoteRouteType = Util.decode(Util.getValue(ele, "@QuoteRouteType"));
                    receivesCopyOfQuote = Util.decode(Util.getValue(ele, "@ReceivesCopyofQuote"));
                    billRouteType = Util.decode(Util.getValue(ele, "@BillRouteType"));
                    receivesCopyOfBill = Util.decode(Util.getValue(ele, "@ReceivesCopyofBill"));
                    numberOfBillCopies = Util.decode(Util.getValue(ele, "@NumberofBillCopies"));
                    billFormat = Util.decode(Util.getValue(ele, "@BillFormat"));
                    country = Util.decode(Util.getValue(ele, "@Country"));
                    language = Util.decode(Util.getValue(ele, "@Language"));

                }
            }
            accountCharacteristics = new CharacteristicUtilities(document
                    .selectNodes("//*[local-name()='CharacteristicsRow']"), "@CharacteristicType",
                    "@CharacteristicValue", "@AdhocCharacteristicValue", "@EffectiveDate", "@CharTypeFlag", properties)
                    .getCharacteristics();

            if (accountCharacteristics.containsKey("WEBPASS")) {
                webPass = (String) accountCharacteristics.get("WEBPASS");
            } else {
                webPass = "";
            }
            return true;
        } catch (Exception e) {
            errorMessage = e.getMessage();
            return false;
        }
    }

    public boolean retrieveCurrencySymbol() {
        String httpResponse = null;

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcAccountMaintenance transactionType = 'Read'>\n").append(
                        "<SSvcAccountMaintenanceService>\n").append("<SSvcAccountMaintenanceHeader AccountID = '")
                .append(accountId).append("'/>\n").append("</SSvcAccountMaintenanceService>\n").append(
                        "</SSvcAccountMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
                return false;
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAccountMaintenanceDetails']");
            currencyCode = Util.getValue(ele, "@CurrencyCode");
        } catch (Exception e) {
            errorMessage = e.getMessage();
            return false;
        }

        String xml2 = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcCurrencyMaintenance transactionType = 'Read'>\n").append(
                        "<SSvcCurrencyMaintenanceService>\n").append("<SSvcCurrencyMaintenanceHeader CurrencyCode = '")
                .append(currencyCode).append("'/>\n").append("</SSvcCurrencyMaintenanceService>\n").append(
                        "</SSvcCurrencyMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            httpResponse = httpCall.callXAIServer(xml2);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
                return false;
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='CurrencyCodeRow']");
            currencySymbol = Util.getValue(ele, "@Symbol");
            currencyPosition = Util.getValue(ele, "@CurrencySymbolPosition");
            return true;
        } catch (Exception e) {
            errorMessage = e.getMessage();
            return false;
        }
    }

    public String getBillRouteType() {
        return billRouteType;
    }

    public void setBillRouteType(String billRouteType) {
        this.billRouteType = billRouteType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNumberOfBillCopies() {
        return numberOfBillCopies;
    }

    public void setNumberOfBillCopies(String numberOfBillCopies) {
        this.numberOfBillCopies = numberOfBillCopies;
    }

    public String getQuoteRouteType() {
        return quoteRouteType;
    }

    public void setQuoteRouteType(String quoteRouteType) {
        this.quoteRouteType = quoteRouteType;
    }

    public String getReceivesCopyOfBill() {
        return receivesCopyOfBill;
    }

    public void setReceivesCopyOfBill(String receivesCopyOfBill) {
        this.receivesCopyOfBill = receivesCopyOfBill;
    }

    public String getReceivesCopyOfQuote() {
        return receivesCopyOfQuote;
    }

    public void setReceivesCopyOfQuote(String receivesCopyOfQuote) {
        this.receivesCopyOfQuote = receivesCopyOfQuote;
    }
}

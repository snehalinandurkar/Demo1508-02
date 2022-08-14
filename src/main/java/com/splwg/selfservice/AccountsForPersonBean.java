/*
 * AccountsForPersonBean.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Searches for accounts, which have the input person as person related to the them, with Web Access Flag set to allowed
 *
 */
package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author  Jacek Ziabicki
 */
public class AccountsForPersonBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String accountId;
    private ArrayList accountList;
    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private int accountOccurences;
    private DecimalFormat numOutput;
    private DecimalFormat numInput;
    private String currencySymbol;
    private String currencyPosition;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountsForPersonBean */
    public AccountsForPersonBean() {
        accountList = new ArrayList();
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    private String formatCurrencyValue(String value) {
        if ("PR".equals(currencyPosition)) return currencySymbol + value;
        else return value + currencySymbol;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getAccountOccurences() {
        return accountOccurences;
    }

    void setPersonId(String value) {
        personId = value;
        infoRetrieved = false;
    }

    public ArrayList getAccountList() {
        if (! infoRetrieved) {
            infoRetrieved = callAccountsForPersonService();
        }
        return accountList;
    }

    void setProperties(Properties value) {
        properties = value;
        numInput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private boolean callAccountsForPersonService() {
        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n")
                                              .append("<SSvcAccountsForPersonSearch>\n")
                                              .append("<AccountPersons>\n")
                                              .append("<AccountPersonsHeader WebAccessFlag='ALWD' PersonID = '")
                                              .append(personId).append("'/>\n").append("</AccountPersons>\n")
                                              .append("</SSvcAccountsForPersonSearch>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();
        ;

        accountOccurences = 0;
        try {
            accountList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            List list = document.selectNodes("//*[local-name()='AccountPersonsRow']");
            accountOccurences = list.size();
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                if (accountId == null) accountId = Util.getValue(ele, "@AccountID");
                AccountPersonBean acc = new AccountPersonBean();
                acc.setAccountId(Util.getValue(ele, "@AccountID"));
                acc.setAccountInfo(Util.decode(Util.getValue(ele, "@AccountInfo")));
                AccountBean tmpAcct = new AccountBean();
                tmpAcct.setAccountId(Util.getValue(ele, "@AccountID"));
                tmpAcct.setProperties(properties);

                if (tmpAcct.retrieveCurrencySymbol()) {
                    currencySymbol = tmpAcct.getCurrencySymbol();
                    currencyPosition = tmpAcct.getCurrencyPosition();
                } else {
                    errorMsg = tmpAcct.getErrorMessage();
                    return (false);
                }

                tmpAcct = null;

                acc.setCurrentBalance(formatCurrencyValue(numOutput.format(numInput.parse(Util.getValue(ele,
                                                                                                        "@CurrentBalance")))));
                accountList.add(acc);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

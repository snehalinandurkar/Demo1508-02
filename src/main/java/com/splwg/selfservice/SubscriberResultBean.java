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

public class SubscriberResultBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountNumber;
    private String cssAcctId;
    private String customerName;
    private String creditPeriod;
    private String allocationType;
    private String creditsEarned;
    private String creditsAllocated;
    private String hostBankContribution;
    private String creditsBanked;
    private String previousBalance;
    private String contributions;
    private String withdrawals;
    private String endingBalance;

    private String subAllocationResultId;
    private ArrayList monthlyList;
    private ArrayList demandList;
    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat stfInput;
    private SimpleDateFormat stfOutput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    //~ Constructors -----------------------------------------------------------------------------------------

    public SubscriberResultBean() {
        monthlyList = new ArrayList();
        demandList = new ArrayList();
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
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }

        return accountNumber;
    }

    void setAccountNumber(String value) {
        accountNumber = value;
    }

    public String getCssAcctId() {
        return cssAcctId;
    }

    void setCssAcctId(String value) {
        cssAcctId = value;
    }

    public String getCustomerName() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return customerName;
    }

    void setCustomerName(String value) {
        customerName = value;
    }

    public String getCreditPeriod() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return creditPeriod;
    }

    void setCreditPeriod(String value) {
        creditPeriod = value;
    }

    public String getAllocationType() {
        return allocationType;
    }

    void setAllocationType(String value) {
        allocationType = value;
    }

    public String getCreditsEarned() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return creditsEarned;
    }

    void setCreditsEarned(String value) {
        creditsEarned = value;
    }

    public String getCreditsAllocated() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return creditsAllocated;
    }

    void setCreditsAllocated(String value) {
        creditsAllocated = value;
    }

    public String getHostBankContribution() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return hostBankContribution;
    }

    void setHostBankContribution(String value) {
        hostBankContribution = value;
    }

    public String getCreditsBanked() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return creditsBanked;
    }

    void setCreditsBanked(String value) {
        creditsBanked = value;
    }

    public String getPreviousBalance() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return previousBalance;
    }

    void setPreviousBalance(String value) {
        previousBalance = value;
    }

    public String getContributions() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return contributions;
    }

    void setContributions(String value) {
        contributions = value;
    }

    public String getWithdrawals() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return withdrawals;
    }

    void setWithdrawals(String value) {
        withdrawals = value;
    }

    public String getEndingBalance() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return endingBalance;
    }

    void setEndingBalance(String value) {
        endingBalance = value;
    }

    public String getSubAllocationResultId() {
        return subAllocationResultId;
    }

    void setSubAllocationResultId(String value) {
        subAllocationResultId = value;
        infoRetrieved = false;
    }

    public ArrayList getMonthlyList() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return monthlyList;
    }

    public ArrayList getDemandList() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return demandList;
    }

    void setProperties(Properties value) {
        properties = value;
        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));
        stfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateTimeFormat"));
        stfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateTimeFormat"));
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

    private boolean callSubscriberPlansService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CMRetAlReDet>\n")
                                              .append("<input>\n")
                                              .append("<inputSubAllocResultId>")
                                              .append(subAllocationResultId)
                                              .append("</inputSubAllocResultId>\n")
                                              .append("</input>\n")
                                              .append("</CMRetAlReDet>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            demandList.clear();
            monthlyList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ReturnSubscriberAllocationResultDetails", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element det = (Element) document.selectSingleNode("//*[local-name()='subscriberAllocationDetails']");
            if (det != null) {
                setAccountNumber(Util.getValue(det, "accountNumber"));
                setCustomerName(Util.getValue(det, "customerName"));
                setCssAcctId(Util.getValue(det, "cssAcctId"));
            }
            det = (Element) document.selectSingleNode("//*[local-name()='subscriberAllocationResultDetails']");
            if (det != null) {
                setCreditPeriod(Util.getValue2(det, "creditPeriod", sdfInput, sdfOutput));
                setCreditsEarned(Util.getValue(det, "creditsEarned"));
                setCreditsAllocated(Util.getValue(det, "creditsAllocated"));
                setHostBankContribution(Util.getValue(det, "hostBankDistribution"));
                setCreditsBanked(Util.getValue(det, "creditsBanked"));
            }
            det = (Element) document.selectSingleNode("//*[local-name()='hostBankDetails']");
            if (det != null) {
                setAllocationType(Util.getValue(det, "allocationTypeFlag"));
                setPreviousBalance(Util.getValue(det, "previousBalance"));
                setContributions(Util.getValue(det, "contributions"));
                setWithdrawals(Util.getValue(det, "withdrawals"));
                setEndingBalance(Util.getValue(det, "endingBalance"));
            }

            List list = document.selectNodes("//*[local-name()='subscriberMonthlyAllocation']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                MonthlyAllocationBean ma = new MonthlyAllocationBean();
                ma.setAccountId(Util.getValue(ele, "subscriberCssAccountId"));
                ma.setSubscriberName(Util.getValue(ele, "subscriberName"));
                ma.setStatus(Util.getValue(ele, "status"));
                ma.setPercent(Util.getValue(ele, "allocationPct"));
                ma.setServiceClass(Util.getValue(ele, "serviceClass"));
                ma.setValueStackCredit(Util.getValue(ele, "valueStackCredit"));
                ma.setEnergyCredit(Util.getValue(ele, "energyCredit"));
                ma.setCapacityCredit(Util.getValue(ele, "capacityCredit"));
                ma.setEnvironmentalCredit(Util.getValue(ele, "environmentalCredit"));
                ma.setDsrvCredit(Util.getValue(ele, "drvCredit"));
                ma.setLsrvCredit(Util.getValue(ele, "lsrvCredit"));
                ma.setMtcCredit(Util.getValue(ele, "mtcCredit"));
                ma.setCommunityCredit(Util.getValue(ele, "communityCredit"));
                ma.setKwh(Util.getValue(ele, "kwh"));
                ma.setFromDate(Util.getValue(ele, "fromDate", sdfInput, sdfOutput));
                ma.setToDate(Util.getValue(ele, "toDate", sdfInput, sdfOutput));
                ma.setConedBill(Util.getValue(ele, "conedBill"));
                ma.setEscoCharges(Util.getValue(ele, "escoCharges"));
                ma.setBilledAmount(Util.getValue(ele, "billAmount"));
                ma.setBilledKwh(Util.getValue(ele, "billedKwh"));
                ma.setCreditAppliedAmount(Util.getValue(ele, "creditAppliedToCss"));
                ma.setCreditAppliedDate(Util.getValue(ele, "rcreditAppliedDate", sdfInput, sdfOutput));
                ma.setPreviousBalance(Util.getValue(ele, "previousBalance"));
                ma.setMonthlyBankContribution(Util.getValue(ele, "monthlyBankContribution"));
                ma.setBankWithdrawal(Util.getValue(ele, "bankWithdrawal"));
                ma.setBankedBalance(Util.getValue(ele, "bankedBalance"));

                monthlyList.add(ma);
            }
            list = document.selectNodes("//*[local-name()='subscriberOnDemandAllocation']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                OnDemandAllocationBean od = new OnDemandAllocationBean();
                od.setAccountId(Util.getValue(ele, "subscriberCssAccountId"));
                od.setSubscriberName(Util.getValue(ele, "subscriberName"));
                od.setPercent(Util.getValue(ele, "allocationPct"));
                od.setCredit(Util.getValue(ele, "valueStackCredit"));

                demandList.add(od);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

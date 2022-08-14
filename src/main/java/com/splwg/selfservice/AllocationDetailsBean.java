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

public class AllocationDetailsBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountNumber;
    private String cssAcctId;
    private String allocationId;
    private ArrayList creditList;
    private ArrayList planList;

    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    private String projectType;
    private String projectTypeDesc;
    private String projectStartDate;
    private String masterCaseNumber;
    private String projectStatus;
    private String monthlyBankBalance;
    private String hostBankBalance;
    private String bankBalance;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountHostAllocationsBean */
    public AllocationDetailsBean() {
        planList = new ArrayList();
        creditList = new ArrayList();
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
    }

    public String getCssAcctId() {
        return cssAcctId;
    }

    public void setCssAcctId(String value) {
        cssAcctId = value;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String value) {
        allocationId = value;
        infoRetrieved = false;
    }

    public ArrayList getPlanList() {
        if (! infoRetrieved) {
            infoRetrieved = callAllocationDetailsService();
        }
        return planList;
    }

    public ArrayList getCreditList() {
        if (! infoRetrieved) {
            infoRetrieved = callAllocationDetailsService();
        }
        return creditList;
    }

    public String getProjectTypeDesc() {
        return projectTypeDesc;
    }

    void setProjectTypeDesc(String value) {
        projectTypeDesc = value;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    void setProjectStartDate(String value) {
        projectStartDate = value;
    }

    public String getMasterCaseNumber() {
        return masterCaseNumber;
    }

    void setMasterCaseNumber(String value) {
        masterCaseNumber = value;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    void setProjectStatus(String value) {
        projectStatus = value;
    }

    public String getBankBalance() {
        return bankBalance;
    }

    void setBankBalance(String value) {
        bankBalance = value;
    }

    public String getMonthlyBankBalance() {
        return monthlyBankBalance;
    }

    void setMonthlyBankBalance(String value) {
        monthlyBankBalance = value;
    }

    public String getHostBankBalance() {
        return hostBankBalance;
    }

    void setHostBankBalance(String value) {
        hostBankBalance = value;
    }

    public String getProjectType() {
        return projectType;
    }

    void setProjectType(String value) {
        projectType = value;
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

    private boolean callAllocationDetailsService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CMRetHsAlDet>\n")
                                              .append("<input>\n")
                                              .append("<allocationId>")
                                              .append(allocationId)
                                              .append("</allocationId>\n")
                                              .append("</input>\n")
                                              .append("</CMRetHsAlDet>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            planList.clear();
            creditList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ReturnHostAllocationDetails", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element allocDet = (Element) document.selectSingleNode("//*[local-name()='hostAllocationDetails']");
            if (allocDet != null) {
                setCssAcctId(Util.getValue(allocDet, "cssAcctId"));
	    } 

	    String bankBalanceString = "";
            Element det = (Element) document.selectSingleNode("//*[local-name()='projectDetails']");
            if (det != null) {
                setProjectTypeDesc(Util.getValue(det, "projectTypeDescr"));
                setProjectType(Util.getValue(det, "projectType"));
                setProjectStatus(Util.getValue(det, "projectStatus"));
                //setMonthlyBankBalance(Util.getValue(det, "monthlyBankBalance"));
                //setHostBankBalance(Util.getValue(det, "hostBankBalance"));
                setBankBalance(Util.getValue(det, "hostBankBalance"));
		setMonthlyBankBalance(Util.getValue(det, "monthlyBankBalance"));
                setProjectStartDate(Util.getValue(det, "projectStartDate", sdfInput, sdfOutput));
                setMasterCaseNumber(Util.getValue(det, "mcNumber"));
            }

            String effectiveDateString = "";
            List list = document.selectNodes("//*[local-name()='creditBillingOption']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                CreditOptionBean co = new CreditOptionBean();
                co.setDescription(Util.getValue(ele, "creditBillingOptionDescr"));
                co.setValue(Util.getValue(ele, "value"));
                co.setEffectiveDate(Util.getValue(ele, "effectiveDate", sdfInput, sdfOutput));

                creditList.add(co);
            }

            list = document.selectNodes("//*[local-name()='subscriberPlanList']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                SubscriberPlanBean sp = new SubscriberPlanBean();
                sp.setPlanId(Util.getValue(ele, "subAllocPlanId"));
                sp.setPlanType(Util.getValue(ele, "subAllocPlanType"));
                sp.setCreateDate(Util.getValue(ele, "subAllocCreDate", sdfInput, sdfOutput));
                sp.setEffectiveDate(Util.getValue(ele, "subAllocEffDate", sdfInput, sdfOutput));
                sp.setStatus(Util.getValue(ele, "subAllocStatus"));

                planList.add(sp);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

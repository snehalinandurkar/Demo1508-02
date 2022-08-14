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

public class SubscriberPlansAndResultsBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountNumber;
    private String cssAcctId;
    private String customerName;
    private String planType;
    private String status;
    private String createDateTime;
    private String effectiveDate;
    private String submissionDate;
    private String hostAllocationId;
    private String hostAllocationType;
    private String projectType;

    private String subAllocationPlanId;
    private ArrayList planList;
    private ArrayList resultList;
    private ArrayList typeList;
    private Properties properties;
    private String errorMsg;
    private boolean infoRetrieved;
    private boolean infoRetrieved2;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat stfInput;
    private SimpleDateFormat stfOutput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;
    private boolean isListBased;

    //~ Constructors -----------------------------------------------------------------------------------------

    public SubscriberPlansAndResultsBean() {
        planList = new ArrayList();
        resultList = new ArrayList();
        typeList = new ArrayList();
        isListBased = false;
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

    public String getPlanType() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return planType;
    }

    void setPlanType(String value) {
        if (value == null)
           value = "";

        planType = value;

        isListBased = (planType.toLowerCase().contains("list"));
    }

    public boolean getIsListBased() {
        return isListBased;
    }

    public String getStatus() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return status;
    }

    void setStatus(String value) {
        status = value;
    }

    public String getCreateDateTime() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return createDateTime;
    }

    public String getProjectType() {
        return projectType;
    }

    void setProjectType(String value) {
        infoRetrieved2 = false;
        projectType = value;
    }

    void setCreateDateTime(String value) {
        createDateTime = value;
    }

    public String getEffectiveDate() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return effectiveDate;
    }

    void setEffectiveDate(String value) {
        effectiveDate = value;
    }

    public String getSubmissionDate() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return submissionDate;
    }

    void setSubmissionDate(String value) {
        submissionDate = value;
    }

    public String getHostAllocationId() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return hostAllocationId;
    }

    void setHostAllocationId(String value) {
        hostAllocationId = value;
    }

    public String getHostAllocationType() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return hostAllocationType;
    }

    void setHostAllocationType(String value) {
        hostAllocationType = value;
    }

    public String getSubAllocationPlanId() {
        return subAllocationPlanId;
    }

    void setSubAllocationPlanId(String value) {
        subAllocationPlanId = value;
        infoRetrieved = false;
    }

    public ArrayList getPlanList() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return planList;
    }

    public ArrayList getResultList() {
        if (! infoRetrieved) {
            infoRetrieved = callSubscriberPlansService();
        }
        return resultList;
    }

    public ArrayList getTypeList() {
        if (! infoRetrieved2) {
            infoRetrieved2 = callAllocationPlanTypeService();
        }
        return typeList;
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
                                              .append("<CMRetSAPDet>\n")
                                              .append("<input>\n")
                                              .append("<subAllocPlanId>")
                                              .append(subAllocationPlanId)
                                              .append("</subAllocPlanId>\n")
                                              .append("</input>\n")
                                              .append("</CMRetSAPDet>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            planList.clear();
            resultList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ReturnSubscriberAllocationPlanDetails", xml);
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
            
            det = (Element) document.selectSingleNode("//*[local-name()='subscriberAllocationPlanDetails']");
            if (det != null) {
                setSubAllocationPlanId(Util.getValue(det, "allocationPlanId"));
                setPlanType(Util.getValue(det, "allocationPlanType"));
                setStatus(Util.getValue(det, "boStatus"));
                setHostAllocationId(Util.getValue(det, "hostAllocationId"));
                //setCreateDateTime(Util.getValue(det, "creationDateTime", stfInput, stfOutput));
                //setEffectiveDate(Util.getValue(det, "effectiveDate", stfInput, sdfOutput));
                
		String dateString = "";
		dateString = Util.getValue(det, "creationDateTime");
		if (! dateString.equals(" ")) {
                    Date createDate = sdfInput.parse(dateString);
                    setCreateDateTime(sdfOutput.format(createDate));
                } else {
                    setCreateDateTime(" ");
                } 

		dateString = Util.getValue(det, "effectiveDate");
		if (! dateString.equals(" ")) {
                    Date effectiveDate = sdfInput.parse(dateString);
                    setEffectiveDate(sdfOutput.format(effectiveDate));
                } else {
                    setCreateDateTime(" ");
                } 
           }
            det = (Element) document.selectSingleNode("//*[local-name()='additionalPlanDetails']");
            if (det != null) {
                setSubmissionDate(Util.getValue(det, "submissionDate", sdfInput, sdfOutput));
            }

            List list = document.selectNodes("//*[local-name()='subscriberList']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                SubscriberAllocationBean sa = new SubscriberAllocationBean();
                String errMsg = Util.getValue(ele, "subscriberCssAcctId");
                sa.setAccountId(Util.getValue(ele, "subscriberCssAcctId"));
                sa.setName(Util.getValue(ele, "subscriberName"));
                sa.setAddress(Util.getValue(ele, "subscriberAddress"));
                sa.setPercent(Util.getValue(ele, "subscriberAllocation"));
                sa.setStatus(Util.getValue(ele, "subscriberStatus"));
                sa.setEndDate(Util.getValue(ele, "subscriberEndDate", sdfInput, sdfOutput));

                String serrMsg = Util.getValue(ele, "subscriberError");
                if (serrMsg.trim().length() > 0) {
                    sa.setName(serrMsg);
                }

                planList.add(sa);
            }
            list = document.selectNodes("//*[local-name()='subscriberResultsList']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                AllocationResultBean hr = new AllocationResultBean();
                hr.setResultId(Util.getValue(ele, "subAllocResultId"));
                hr.setPeriod(Util.getValue2(ele, "creditPeriod", sdfInput, sdfOutput));
                hr.setCreditAmount(Util.getValue(ele, "creditAmount"));
                hr.setBalance(Util.getValue(ele, "balance"));
                hr.setStatementDate(Util.getValue(ele, "statementDate", sdfInput, sdfOutput));

                resultList.add(hr);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
    
    private boolean callAllocationPlanTypeService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CM-GetSAPT>\n")
                                              .append("<input>\n")
                                              .append("<inputHostAllocType>")
                                              .append(projectType)
                                              .append("</inputHostAllocType>\n")
                                              .append("</input>\n")
                                              .append("</CM-GetSAPT>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            typeList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-GetAllocationPlanType", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            List list = document.selectNodes("//*[local-name()='validSubAllocPlanType']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                AllocationPlanTypeBean pt = new AllocationPlanTypeBean();
                pt.setSubAllocTypeCd(Util.getValue(ele,     "subAllocTypeCd"));
                pt.setSubAllocTypeDesc(Util.getValue(ele,  "subAllocTypeDesc"));
                pt.setPctOrListBasedFlag(Util.getValue(ele, "pctOrListBasedFlag"));

                typeList.add(pt);
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }

}

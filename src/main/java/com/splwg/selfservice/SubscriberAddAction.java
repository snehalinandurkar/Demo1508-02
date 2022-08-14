/*
 * AccountInformationAction.java
 *
 * Created on March 02, 2002, 12:01 PM
 * Part of CorDaptix Web Self Service
 * Implements the action for the account information page
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author  Steve Verlander
 */
public class SubscriberAddAction
    implements SSvcAction {

    private String newSubAllocPlanId;
    private String subAllocPlanId;
    private String allocationId;
    private String projectType;
    private String planType;
    private Properties properties;
    private ArrayList typeList;
    private String errorMsg;

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        projectType = request.getParameter("pt");
        allocationId = request.getParameter("allocId");
        planType = request.getParameter("planType");
        String accountId = request.getParameter("accountId");
        if (accountId == null) accountId = validUser.getAccountId();
        else validUser.setAccountId(accountId);
 
        String error = "";

        // Setting of Currency Code Symbol
        AccountBean account = new AccountBean();
        account.setAccountId(accountId);
        account.setProperties(properties);

        if (account.retrieveCurrencySymbol()) {
            account.retrieveCurrencySymbol();
            validUser.setCurrencySymbol(account.getCurrencySymbol());
            validUser.setCurrencyPosition(account.getCurrencyPosition());
        } else {
            error = account.getErrorMessage();
        }

        ControlCentralBean accountInfo = new ControlCentralBean();
        accountInfo.setCurrencyPosition(validUser.getCurrencyPosition());
        accountInfo.setCurrencySymbol(validUser.getCurrencySymbol());

        accountInfo.setAccountId(accountId);
        accountInfo.setProperties(properties);
        accountInfo.getMainCustomerPersonId();

        validUser.setEntityName(accountInfo.getEntityName());

        if (session.getAttribute("accountInfo") == null) {
            session.setAttribute("accountInfo", accountInfo);
        }

        if (planType != null && allocationId != null){
           if (callAddSubscriberPlanService())
              forward("/SSvcController/subscriberedit?planId="+newSubAllocPlanId, request, response);
        }

        SubscriberPlansAndResultsBean alPlan = new SubscriberPlansAndResultsBean();
        alPlan.setCurrencyPosition(validUser.getCurrencyPosition());
        alPlan.setCurrencySymbol(validUser.getCurrencySymbol());
        alPlan.setProperties(properties);
        alPlan.setProjectType(projectType);
        alPlan.setHostAllocationId(allocationId);
        alPlan.setStatus("");
        alPlan.setPlanType("");
        alPlan.setCreateDateTime("");
        alPlan.setSubAllocationPlanId("");
        alPlan.getTypeList();

        // Setting of Phone Type Labels
        
        PhoneTypeBean phoneType = new PhoneTypeBean(properties);
        phoneType.setPhoneType(properties.getProperty("com.splwg.selfservice.PhoneTypeHome"));
        if (phoneType.retrievePhoneType()) {
            validUser.setHomePhoneLabel(phoneType.getDescription());
        } else {
            error = phoneType.getErrorMessage();
        }

        phoneType.setPhoneType(properties.getProperty("com.splwg.selfservice.PhoneTypeBusiness"));
        if (phoneType.retrievePhoneType()) {
            validUser.setBusinessPhoneLabel(phoneType.getDescription());
        } else {
            error = phoneType.getErrorMessage();
        }

        session.setAttribute("SelfService_validUser", validUser);

        request.setAttribute("accountInfo", accountInfo);
        request.setAttribute("alPlan", alPlan);

        error = accountInfo.getErrorMessage();

        if (error == null) error = alPlan.getErrorMessage();
        String jspName = Util.getCMFile("SSvcSubscriberAdd.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private boolean callAddSubscriberPlanService() {
        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CMAdUpDupSAP>\n")
                                              .append("<input>\n")
                                              .append("<subscriberPlanType>")
                                              .append(planType)
                                              .append("</subscriberPlanType>\n")
                                              .append("<hostAllocationPlanId>\n")
                                              .append(allocationId)
                                              .append("</hostAllocationPlanId>\n")
                                              .append("<action>A</action>\n")
                                              .append("</input>\n")
                                              .append("</CMAdUpDupSAP>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("Cm-AddUpdateDuplicateSubscriberPlan", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            Element resultDet = (Element) document.selectSingleNode("//*[local-name()='processingResultDetails']");
            if (resultDet != null) {
                newSubAllocPlanId = Util.getValue(resultDet, "newSubscriberAllocationPlanId");
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

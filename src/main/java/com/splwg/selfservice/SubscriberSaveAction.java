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
public class SubscriberSaveAction
    implements SSvcAction {

    private String subAllocPlanId;
    private String projectType;
    private Properties properties;
    private ArrayList modList;
    private String errorMsg;
    private String isInError;

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        subAllocPlanId = request.getParameter("planId");

        String accountId = request.getParameter("accountId");
        if (accountId == null) accountId = validUser.getAccountId();
        else validUser.setAccountId(accountId);
 
        String error = "";

        // Setting of Currency Code Symbol
        AccountBean account = new AccountBean();
        account.setAccountId(accountId);
        account.setProperties(properties);

        ControlCentralBean accountInfo = new ControlCentralBean();

        accountInfo.setAccountId(accountId);
        accountInfo.setProperties(properties);
        accountInfo.getMainCustomerPersonId();

        validUser.setEntityName(accountInfo.getEntityName());

        modList = new ArrayList();
        int i=1;

        while (true) {
            String acct = request.getParameter("acct"+i);
            String perc = request.getParameter("perc"+i);
            String seq  = request.getParameter("seq"+i);

            if (acct != null && perc != null && seq != null) {
                if (acct.trim().length() > 0) {
                    ModifySubscriberBean sb = new ModifySubscriberBean();
                    sb.setCssAcctId(acct.trim());
                    sb.setPercent(perc.trim());
                    sb.setSequence(seq.trim());
                    modList.add(sb);
                }
                i++;
            }
            else
                break; 
        }

        if (callUpdateSubscriberPlanService()) {
            if (isInError == null || isInError.equals("false")) {
                forward("/SSvcController/subscriberplan?planId="+subAllocPlanId, new HttpServletRequestWrapper(request), response);
            }
        }

        session.setAttribute("SelfService_validUser", validUser);

        error = errorMsg;

        // String jspName = Util.getCMFile("SSvcSubscriberEdit.jsp", context);
        forward("/SSvcController/subscriberedit?planId="+subAllocPlanId+"&errorMessage=" + error, new HttpServletRequestWrapper(request), response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private boolean callUpdateSubscriberPlanService() {
        StringBuffer mods = new StringBuffer(512);
        for (Iterator it = modList.iterator(); it.hasNext();) {
            ModifySubscriberBean sa = (ModifySubscriberBean) it.next(); 

                    mods.append("<subscriberList>\n")
                        .append("<subscriberCssAcctid>")
                        .append(sa.getCssAcctId())
                        .append("</subscriberCssAcctid>\n")
                        .append("<allocationPct>")
                        .append(sa.getPercent())
                        .append("</allocationPct>\n")
                        .append("<sequence>")
                        .append(sa.getSequence())
                        .append("</sequence>\n")
                        .append("</subscriberList>\n");
        }


        String xml = new StringBuffer(512).append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n")
                                              .append("<soapenv:Header/>\n")
                                              .append("<soapenv:Body>\n")
                                              .append("<CM-ModSubLst>\n")
                                              .append("<input>\n")
                                              .append("<inputSubAllocPlanId>")
                                              .append(subAllocPlanId)
                                              .append("</inputSubAllocPlanId>\n")
                                              .append(mods.toString())
                                              .append("</input>\n")
                                              .append("</CM-ModSubLst>\n")
                                              .append("</soapenv:Body>\n").append("</soapenv:Envelope>\n")
                                              .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callIWSServer("CM-ModifySubscriberList", xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//env:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            Element resultDet = (Element) document.selectSingleNode("//*[local-name()='errorInformation']");
            if (resultDet != null) {
                isInError = Util.getValue(resultDet, "isInError");
                errorMsg = Util.getValue(resultDet, "errorMessage");
            }

            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

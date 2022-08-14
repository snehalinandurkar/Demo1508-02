/*
 ***********************************************************************
 *                 Confidentiality Information:
 *
 * This module is the confidential and proprietary information of
 * Oracle Corporation; it is not to be copied, reproduced, or
 * transmitted in any form, by any means, in whole or in part,
 * nor is it to be used for any purpose other than that for which
 * it is expressly provided without the written permission of
 * Oracle Corporation.
 *
 ***********************************************************************
 *
 * PROGRAM DESCRIPTION:
 *
 * This Java Bean is a clone of AccountInformationAction.java
 * with additional custom functions added to the base code. 
 *
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2011-12-20  SMedin  SR0511. Initial Version.
 ************************************************************************
 */

package com.splwg.selfservice;

import java.io.IOException;

import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CMAccountInformationAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

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

        BillBean lastBill = new BillBean();
        lastBill.setBillId(accountInfo.getLastBillId());
        lastBill.setCurrencyPosition(validUser.getCurrencyPosition());
        lastBill.setCurrencySymbol(validUser.getCurrencySymbol());
        lastBill.setProperties(properties);
        lastBill.getBillId();

        SAforAccountBean saList = new SAforAccountBean();
        saList.setAccountId(accountId);
        saList.setCurrencyPosition(validUser.getCurrencyPosition());
        saList.setCurrencySymbol(validUser.getCurrencySymbol());
        saList.setProperties(properties);
        saList.getSAList();

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

		CMAccountPersonBean acctPer = new CMAccountPersonBean();
		acctPer.setProperties(properties);
		acctPer.setBillRouteType(acctPer.retrieveBillRouteType(accountId));
		acctPer.setBillRouteMessage(properties.getProperty("com.splwg.selfservice.BillRouteMessage"));
		System.out.println ("BillRouteType: " + acctPer.getBillRouteType());

        session.setAttribute("SelfService_validUser", validUser);
        request.setAttribute("accountInfo", accountInfo);
        request.setAttribute("lastBill", lastBill);
        request.setAttribute("saList", saList);
        request.setAttribute("acctPer", acctPer);

        error = accountInfo.getErrorMessage();

        if (error == null) {
            error = lastBill.getErrorMessage();
        }
        if (error == null) error = saList.getErrorMessage();
        
        String jspName = Util.getCMFile("CMSSvcAccountInformation.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

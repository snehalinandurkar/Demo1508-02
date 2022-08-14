/*
 * AccountInformationAction.java
 *
 * Created on March 02, 2002, 12:01 PM
 * Part of CorDaptix Web Self Service
 * Implements the action for the account information page
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

/**
 *
 * @author  Jacek Ziabicki
 */
public class AllocationInformationAction
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

        AccountHostAllocationsBean alList = new AccountHostAllocationsBean();
        alList.setAccountId(accountId);
        alList.setCurrencyPosition(validUser.getCurrencyPosition());
        alList.setCurrencySymbol(validUser.getCurrencySymbol());
        alList.setProperties(properties);
        alList.getTransactionList();

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
        request.setAttribute("alList", alList);

        error = accountInfo.getErrorMessage();

        if (error == null) error = alList.getErrorMessage();
        String jspName = Util.getCMFile("SSvcAllocationHistory.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

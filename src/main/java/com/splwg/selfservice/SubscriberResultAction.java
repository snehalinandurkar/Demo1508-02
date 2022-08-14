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
public class SubscriberResultAction
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

        String resultId = request.getParameter("resultId");

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

        SubscriberResultBean results = new SubscriberResultBean();
        results.setAccountNumber(accountId);
        results.setSubAllocationResultId(resultId);
        results.setCurrencyPosition(validUser.getCurrencyPosition());
        results.setCurrencySymbol(validUser.getCurrencySymbol());
        results.setProperties(properties);
        results.getMonthlyList();

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
        request.setAttribute("results", results);

        error = accountInfo.getErrorMessage();

        if (error == null) error = results.getErrorMessage();
        String jspName = Util.getCMFile("SSvcSubscriberResult.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

/*
 * AccountFinancialHistoryAction.java
 *
 * Created on March 02, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the action for the Account financial history transaction
 *
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

public class AccountFinancialHistoryAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        AccountFinancialHistoryBean accountFinHist = new AccountFinancialHistoryBean();
        accountFinHist.setAccountId(validUser.getAccountId());
        accountFinHist.setCurrencyPosition(validUser.getCurrencyPosition());
        accountFinHist.setCurrencySymbol(validUser.getCurrencySymbol());
        accountFinHist.setProperties(properties);
        accountFinHist.getTransactionList();

        request.setAttribute("accountFinHist", accountFinHist);

        String error = accountFinHist.getErrorMessage();
        String jspName = Util.getCMFile("SSvcAccountFinancialHistory.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

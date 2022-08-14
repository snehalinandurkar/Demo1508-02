/*
 * BillingHistoryAction.java
 *
 * Created on March 09, 2002, 12:01 PM by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for the Billing History page
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

public class BillingHistoryAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        BillingHistoryBean billHist = new BillingHistoryBean();
        billHist.setCurrencyPosition(validUser.getCurrencyPosition());
        billHist.setCurrencySymbol(validUser.getCurrencySymbol());
        billHist.setAccountId(validUser.getAccountId());
        billHist.setProperties(properties);
        billHist.getBillingHistoryList();

        request.setAttribute("billHist", billHist);

        String error = billHist.getErrorMessage();
        String jspName = Util.getCMFile("SSvcBillingHistory.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

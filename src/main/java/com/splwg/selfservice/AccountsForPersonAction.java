/*
 * AccountsForPersonAction.java
 *
 * Created on March 02, 2002, 12:01 PM by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for the 'My Accounts' page
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

public class AccountsForPersonAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        MessageHelper messageHelper = new MessageHelper(session, properties);
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        AccountsForPersonBean accountPerson = (AccountsForPersonBean) session.getAttribute("AccountsForPerson");
        accountPerson.setProperties(properties);

        if (accountPerson.getAccountOccurences() == 1) {
            String id = validUser.getAccountId();
            if (id == null) {
                validUser.setAccountId(accountPerson.getAccountId());
                session.setAttribute("SelfService_validUser", validUser);
            }
            forward("/SSvcController/account", request, response);
        } else if (accountPerson.getAccountOccurences() > 1) {
            if (validUser.getAccountId() == null) {
                validUser.setAccountId(accountPerson.getAccountId()); // if user should choose an item from the menu, without choosing first an account
                session.setAttribute("SelfService_validUser", validUser);
            }
            request.setAttribute("accountPerson", accountPerson);
            String error = accountPerson.getErrorMessage();
            String jspName = Util.getCMFile("SSvcAccountsForPerson.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        } else {
            String error = accountPerson.getErrorMessage();
            if (error == null) error = messageHelper.getMessage(11601);
            String jspName = Util.getCMFile("SSvcLogIn.jsp", context);
            forward(jspName + "?errorMsg=" + error, request, response);
        }
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

/*
 * AccountInformationAction.java
 *
 * Created on March 02, 2002, 12:01 PM
 * Part of CorDaptix Web Self Service
 * Implements the action for the account information page
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
public class SubscriberStatementAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        String resultId = request.getParameter("resultId");

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

        session.setAttribute("SelfService_validUser", validUser);

        // error = accountInfo.getErrorMessage();

        SubscriberStatementBean statement = new SubscriberStatementBean();
        statement.setProperties(properties);
        statement.setResultId(resultId);
        String filename = statement.getFilePath();
        String success = statement.getIsSuccessful();

        error = statement.getErrorMessage();

        if ("true".equals(success)) {
            File file = new File(filename);
            response.setHeader("Content-Type", context.getMimeType(file.getName()));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"statement.pdf\"");
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            PrintWriter writer = response.getWriter();
            writer.println("<html><body><h2>Error retrieving statement</h2></body></html>");
        }

/*
        Path src = Paths.get(statement.getFilePath());

        String stmt_dst = properties.getProperty("com.splwg.selfservice.BillDestination",
           "/spl/app/Middleware2/user_projects/domains/CCB26TST1/servers/ccb26tst1_server/tmp/_WL_user/SPLWeb/n3hr9b/war/");

        Path dst = Paths.get(stmt_dst);

        Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
 
        String pdfName = src.getFileName().toString();
        forward(pdfName, request, response);
*/
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

/*
 * StopServiceAction.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for disconnecting services
 *
 */

package com.splwg.selfservice;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StopServiceAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        // Get the sub-action (step) from the request parameters.
        // Delegate the request to the appropriate method.
        
        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }

        if (step.equalsIgnoreCase("entryform")) {
            showEntryForm(servlet, request, response);
        } else if (step.equalsIgnoreCase("process")) {
            processRequest(servlet, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    /** Display stop service entry form.
     */
    private void showEntryForm(HttpServlet servlet,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();

        createAccountBean(servlet, request, response);
        createSABean(servlet, request, response);
        String jspName = Util.getCMFile("SSvcStopService.jsp", context);
        forward(jspName, request, response);
    }

    private void processRequest(HttpServlet servlet,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        SimpleDateFormat sdfInput;
        SimpleDateFormat sdfOutput;

        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        int i = 1;
        String count = (request.getParameter("SACount"));
        Integer saCount = new Integer(count);
        int intCount;
        intCount = saCount.intValue();

        boolean success;
        Boolean Success;
        String radioName = "";
        String saName = "";
        String checkedName = "";
        String startName = "";
        String endName = "";
        String errorMessage = "";
        String serviceName = "";
        String addressName = "";
        String error = null;

        SAforAccountBean saList = new SAforAccountBean();
        for (i = 1; i <= intCount; i++) {
            radioName = "Radio" + i;
            saName = "SA" + i;
            checkedName = "Checked" + i;
            startName = "Start" + i;
            endName = "End" + i;
            addressName = "Address" + i;
            serviceName = "Service" + i;

            if (request.getParameter(checkedName).equals("true")) {
                StopServiceBean service = new StopServiceBean(properties);
                ServiceAgreementBean sa = new ServiceAgreementBean();

                sa.setAddress(request.getParameter(addressName));
                sa.setServiceDesc(request.getParameter(serviceName));
                sa.setStartDate(request.getParameter(startName));
                sa.setEndDate(request.getParameter(endName));
                sa.setSAId(request.getParameter(saName));

                service.setAccountId(validUser.getAccountId());
                service.setPersonId(validUser.getPersonId());
                service.setSAId(request.getParameter(saName));
                try {
                    Date endDate = sdfOutput.parse(request.getParameter("StopDate"));
                    service.setStopDate(sdfInput.format(endDate));
                } catch (Exception e) {
                    error = e.toString();
                }
                success = service.callStopSAService();
                if (success == true) {
                    sa.setDisconnectMessage("Pending Stop");
                    sa.setEndDate(request.getParameter("StopDate"));
                } else {
                    sa.setDisconnectMessage("Could not disconnect");
                    error = service.getErrorMessage();
                }
                saList.addToSAList(sa);
            }
        }

        request.setAttribute("saList", saList);
        createAccountBean(servlet, request, response);
        String jspName = Util.getCMFile("SSvcStopServiceResult.jsp", context);
        forward(jspName + "?errorMsg=" + error, request, response);
    }

    private void createAccountBean(HttpServlet servlet,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        ControlCentralBean accountInfo = (ControlCentralBean) session.getAttribute("accountInfo");

        request.setAttribute("accountInfo", accountInfo);
    }

    private void createSABean(HttpServlet servlet,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

        String accountId = validUser.getAccountId();

        SAforAccountBean saList = new SAforAccountBean();
        saList.setAccountId(accountId);
        saList.setOnlyActive(true);
        saList.setProperties(properties);
        AccountBean account = new AccountBean();
        account.setAccountId(accountId);
        account.setProperties(properties);

        if (account.retrieveCurrencySymbol()) {
            account.retrieveCurrencySymbol();
        } else {
            throw new ServletException(account.getErrorMessage());
        }

        saList.setCurrencyPosition(account.getCurrencyPosition());
        saList.setCurrencySymbol(account.getCurrencySymbol());

        request.setAttribute("saList", saList);
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

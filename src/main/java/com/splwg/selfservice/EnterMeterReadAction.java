/*
 * EnterMeterReadAction.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for entering meter reads in the View Meter Read page
 *
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EnterMeterReadAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ArrayList regList = new ArrayList();
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        MessageHelper messageHelper = new MessageHelper(session, properties);

        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        MeterReadEntryBean read = new MeterReadEntryBean();
        int i = 1;
        String count = (request.getParameter("RegCount"));
        Integer regCount = new Integer(count);
        int intCount;
        intCount = regCount.intValue();

        String regField;
        String readField;

        read.setPersonId(validUser.getPersonId());
        read.setMeterConfigurationId(request.getParameter("MeterConfigurationId"));
        String meterRead;
        String errorMessage = "";
        for (i = 1; i <= intCount; i++) {
            MeterRegisterBean reg = new MeterRegisterBean();
            regField = "RegisterId" + i;
            readField = "Read" + i;

            // Check if all reads have been entered:
            meterRead = request.getParameter(readField);

            if (meterRead.equals(null) || meterRead.equals("")) {
                errorMessage = messageHelper.getMessage(11401);
                String saId = request.getParameter("SAId");
                String url = "/SSvcController/viewMeterRead?saId=" + saId + "&errorMsg=" + errorMessage;
                forward(url, request, response);
            }

            try {
                Float floatNbr;
                floatNbr = new Float(meterRead);
            } catch (NumberFormatException nfe) {
                errorMessage = messageHelper.getMessage(11402);
                String saId = request.getParameter("SAId");
                String url = "/SSvcController/viewMeterRead?saId=" + saId + "&errorMsg=" + errorMessage;
                forward(url, request, response);
            }
            reg.setRegisterId(request.getParameter(regField));
            reg.setRegisterRead(request.getParameter(readField));
            regList.add(reg);
        }

        read.setProperties(properties);
        read.setRegList(regList);

        if (session.getAttribute("read") == null) {
            session.setAttribute("read", read);
        }
        request.setAttribute("read", read);

        String saId = request.getParameter("SAId");

        if (read.isMeterReadAddSuccessful() == false) {
            errorMessage = read.getErrorMessage();
            String url = "/SSvcController/viewMeterRead?saId=" + saId + "&errorMsg=" + errorMessage;
            forward(url, request, response);
        } else {
            String url = "/SSvcController/viewMeterRead?saId=" + saId + "&errorMsg=" + messageHelper.getMessage(11403);
            forward(url, request, response);
        }

        //        String resultsURL = response.encodeRedirectURL("viewMeterRead?saId=" + saId);
        //        response.setHeader("Refresh", "0; URL=" + resultsURL);
        //        forward("/SSvcMeterReadEntryWait.jsp", request, response);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

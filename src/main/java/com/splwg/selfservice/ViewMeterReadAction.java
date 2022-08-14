/*
 * ViewMeterReadAction.java
 *
 * Created in January, 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for viewing meter reads in the View Meter Read page
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

/**
 *
 * @author  Jacek Ziabicki
 */
public class ViewMeterReadAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");

        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        ViewMeterReadBean viewMeterRead = new ViewMeterReadBean();

        String name;
        String saId = request.getParameter("saId");
        String spId = request.getParameter("spId");
        if (saId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        viewMeterRead.setSAId(saId);
        viewMeterRead.setSelectedSPId(spId);
        viewMeterRead.setProperties(properties);
        if (! viewMeterRead.callSPforSAService()) {
            String jspName = Util.getCMFile("SSvcViewMeterReadNone.jsp", context);
            forward(jspName, request, response);
        } else {
            request.setAttribute("viewMeterRead", viewMeterRead);
            String jspName = Util.getCMFile("SSvcViewMeterRead.jsp", context);
            forward(jspName, request, response);
        }
    }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

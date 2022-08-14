/*
 * CMIntervalDataTabularAction.java
 *
 * Created on February 21, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Implements the Action for logging out
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

/**
 *
 * @author  Jacek Ziabicki
 */


public class CMIntervalDataTabularAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        
    	//request.getSession(true);
        HttpSession session = request.getSession();
        ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
	
		
		  String acctId = request.getParameter("accountId");
	        if (acctId == null) acctId = validUser.getAccountId();
	        else validUser.setAccountId(acctId);

		CMBillPeriodBean billPeriodBean = new CMBillPeriodBean(properties,acctId);
		ArrayList<CMBillPeriodBean> list =  billPeriodBean.fetchBillPeriodData(acctId,properties);
		request.setAttribute("billPeriodBean",billPeriodBean);
		request.setAttribute("billPeriodList",list);
		request.setAttribute("accId",acctId);
		request.setAttribute("properties",properties);

        String jspName = Util.getCMFile("CMSSvcIntervalDataTabular.jsp", context);

        String loginURL = jspName;



        forward(loginURL, request, response);
       }

    private void forward(String url,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

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

public class CMDataIntervalAction implements SSvcAction{

	@Override
	public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		ServletContext context = servlet.getServletContext();

		String jspName = "";
		String fwdPage = "";
		String actionCode = request.getParameter("actionCode");
		
		System.out.println("actionCode: " + actionCode);
		
		HttpSession session = request.getSession();
		//CMSSvcPaymentOptionsBean paymentOption = new CMSSvcPaymentOptionsBean();
		CMBillPeriodBean billPeriodBean= (CMBillPeriodBean) session.getAttribute("CMBillPeriodBean");
		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
		CMDataIntervalChartBean cmDataIntervalChartBean= (CMDataIntervalChartBean) session.getAttribute("CMDataIntervalChartBean");
		Properties properties = (Properties) context.getAttribute("properties");

		if (actionCode == null || actionCode.equals("")) {
			actionCode = " ";
		}

		if (actionCode.equalsIgnoreCase("intervalDataTabular")) {
			fwdPage = "CMSSvcIntervalDataTabular";
		}else if(actionCode.equalsIgnoreCase("intervalDataChart")) {
			fwdPage = "CMSSvcIntervalDataChart";
		}else if(actionCode.equalsIgnoreCase("peakDemandTabular")) {
			fwdPage = "CMSSvcPeakDemandTabular";
		}else if(actionCode.equalsIgnoreCase("peakDemandChart")){
			fwdPage = "CMSSvcPeakDemandChart";
		}
		
		//cmDataIntervalChartBean.isDemandUser(validUser.getAccountId());
		
		//billPeriodBean.fetchBillPeriodData(validUser.getAccountId(), properties);
		fwdPage = "CMSSvcIntervalDataTabular";
		String error = "";
		jspName = Util.getCMFile(fwdPage, context);
		forward(jspName , request, response);
		
	}
	

	private void forward(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}
}

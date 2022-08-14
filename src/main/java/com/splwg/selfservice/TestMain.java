package com.splwg.selfservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.TreeMap;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestMain {

	public static void main(String[] args) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		
		Properties properties = new Properties();
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(
		    new File("C:/SelfServiceDev/dev/WEB-INF/SelfServiceConfig.properties"))
        );
        properties.load(in);
        System.out.println("Server URL: " + properties.getProperty("com.splwg.selfservice.RESTServerURL"));
		
		
		String authURL = "https://ccbtst.conedison.net:7501/ouaf/j_security_check?j_username=SYSUSER&j_password=sysuser00";
		String ccbUrl = "https://ccbtst.conedison.net:7501/ouaf/rest/ouaf/script/CMStmBillPd/";
		String jsonInputString = "{\"CMStmBillPd\":{\"input\":{\"accountId\":\"2542000000\"}}}";

		XAIHTTPCall call = new XAIHTTPCall(properties);
		
		System.out.println(call.callRESTServer("script", ccbUrl, jsonInputString));
		//HttpServlet servlet= 
		
		//showBasicForm(servlet, request, response);
		

	}
	
	private static void showBasicForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
		// CMPeakDemandTabReportBean reportBean = (CMPeakDemandTabReportBean)
		// session.getAttribute("reportBean");
		CMBillPeriodBean billPeriodBean = (CMBillPeriodBean) session.getAttribute("BillPeriod");

		billPeriodBean = new CMBillPeriodBean(properties, validUser.getAccountId());
		TreeMap t=billPeriodBean.getBillPeriod();
		if(t.isEmpty()){
			System.out.println("Error");
		}
		String jspName = Util.getCMFile("CMSSvcPeakDemandTabReportSelect.jsp", context);

		//forward(jspName, request, response);
	}

}

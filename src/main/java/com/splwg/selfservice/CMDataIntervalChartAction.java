package com.splwg.selfservice;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author RIA-Admin
 * 
 *         This class is for Interval Data Charts
 * 
 * 
 * @param data
 *            Unit
 * @param Granularity
 * @param From
 *            Date
 * @param To
 *            Date
 * @param Bill
 *            Period
 *
 */
public class CMDataIntervalChartAction implements SSvcAction {

	@Override

	public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		HttpSession session = request.getSession();
		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");

		CMDataIntervalChartBean chartBean = new CMDataIntervalChartBean();
		chartBean.setAccountId(validUser.getAccountId());
		chartBean.setProperties(properties);

		chartBean.setDatUnit(request.getAttribute("dataUnit").toString());
		chartBean.setFromDate((SimpleDateFormat) request.getAttribute("fromDate"));
		chartBean.setToDate((SimpleDateFormat) request.getAttribute("toDate"));
		chartBean.setGranularity(request.getAttribute("granularity").toString());
		chartBean.setBillPeriod(request.getAttribute("billPeriod").toString());
		// request.getAttribute("");

		CMDataIntervalChartOutputBean outputChartBean = new CMDataIntervalChartOutputBean();
		Boolean demandUserFlag = chartBean.isDemandUser(validUser.getAccountId());
		if(demandUserFlag){
			outputChartBean = (CMDataIntervalChartOutputBean) session.getAttribute("CMDataIntervalChartOutputBean");
			chartBean.fetchIntervalData(outputChartBean);
			
		}
		
	
		request.setAttribute("chartBean", chartBean);

		String error = chartBean.getErrorMsg();
		String jspName = Util.getCMFile("SSvcDataChart.jsp", context);
		forward(jspName + "?errorMsg=" + error, request, response);
	}

	private void forward(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

}

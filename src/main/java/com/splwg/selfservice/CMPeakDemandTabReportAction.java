package com.splwg.selfservice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CMPeakDemandTabReportAction implements SSvcAction {

	@Override
	public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// request.getSession(true);

		HttpSession session = request.getSession();
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
		CMPeakDemandTabReportBean reportBean = (CMPeakDemandTabReportBean) session.getAttribute("reportBean");
		CMBillPeriodBean billPeriodBean = (CMBillPeriodBean) session.getAttribute("CMBillPeriodBean");
		// String filePath = reportBean.getReortFile(properties);
		// reportBean.downLoadFile(filePath);

		String step = request.getParameter("step");
		if (step == null || step.equals("")) {
			step = "entryform";
		}
		if (step.equalsIgnoreCase("entryform")) {
			showBasicForm(servlet, request, response);
		} else if (step.equalsIgnoreCase("process")) {
			process(servlet, request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
		// newDownloadFile(filePath, servlet, request, response);
		// String jspName =
		// Util.getCMFile("CMSSvcPeakDemandTabReportSelect.jsp", context);

		// forward(jspName, request, response);

	}

	private void forward(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

	private void showBasicForm(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
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
		TreeMap t = billPeriodBean.getBillPeriod();
		if (t.isEmpty()) {
			System.out.println("Empty Bill List Error");
		}
		String jspName = Util.getCMFile("CMSSvcPeakDemandTabReportSelect.jsp", context);

		forward(jspName, request, response);
	}

	private void process(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ServletContext context = servlet.getServletContext();
		Properties properties = (Properties) context.getAttribute("properties");
		CMPeakDemandTabReportBean peakReportBean = (CMPeakDemandTabReportBean) context
				.getAttribute("peakDemandReportBean");
		String fileName = peakReportBean.getReortFile(properties);
		newDownloadFile(fileName, servlet, request, response);
		String jspName = Util.getCMFile("CMSSvcPeakDemandTabReportReady.jsp", context);

		forward(jspName, request, response);
	}

	void newDownloadFile(String fileName, HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		byte[] file = getFileOnServer(fileName);

		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setHeader("charset", "iso-8859-1");
		response.setContentType("application/octet-stream");
		response.setContentLength(file.length);
		response.setStatus(HttpServletResponse.SC_OK);

		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(file, 0, file.length);
			outputStream.flush();
			outputStream.close();
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] getFileOnServer(String fileName) throws IOException {
		// implement your method to get the file in byte[]
		File file = new File(fileName);
		return Files.readAllBytes(file.toPath());
		// return null;
	}
}

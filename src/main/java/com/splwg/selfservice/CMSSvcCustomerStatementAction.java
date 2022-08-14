/*
 ***********************************************************************
 *                 Confidentiality Information:
 *
 * This module is the confidential and proprietary information of
 * Oracle Corporation; it is not to be copied, reproduced, or
 * transmitted in any form, by any means, in whole or in part,
 * nor is it to be used for any purpose other than that for which
 * it is expressly provided without the written permission of
 * Oracle Corporation.
 *
 ***********************************************************************
 *
 * PROGRAM DESCRIPTION:
 *
 * This Java Action will be the main class of the Customer Statement
 * Module. This class is invoked for every page submission that takes
 * place in the Customer Statement Process and the value of the 
 * actionCode parameter set in the JSPs determine what appropriate work 
 * needs to be done and the subsequent page to be rendered based on the 
 * user action.
 *
 **********************************************************************
 *
 * CHANGE HISTORY:
 *
 * Date:       by:     Reason:
 * 2014-11-27  SMedina CEd12. Customer Statement Module. Initial Version.
 ************************************************************************
 */ 
 
package com.splwg.selfservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FilenameFilter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * @author SMedina
 */
public class CMSSvcCustomerStatementAction implements SSvcAction {

    public void perform(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {   	
    	
        ServletContext context = servlet.getServletContext();

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }

        String requestCharEncoding = request.getParameter("_charset_");
        if (requestCharEncoding != null && !requestCharEncoding.equalsIgnoreCase("UTF-8")) {
            try {
                throw new InvalidParameterException("Invalid character encoding.");
            } catch (InvalidParameterException e) {
                String invalidParamError = e.getMessage();
                String jspName = Util.getCMFile("CMSSvcCustomerStatement.jsp", context);
                forward(jspName + "?errorMsg=" + invalidParamError, request, response);
            }
        }

        // Get the sub-action (step) from the request parameters.
        // Delegate the request to the appropriate method.
        // Note that if the request was forwarded from the Process Info Change
        // step back to EntryForm, the request parameter will have two values.
        // The first one will be EntryForm, which is what we are using.

        String step = request.getParameter("step");
        if (step == null || step.equals("")) {
            step = "entryform";
        }
        System.out.println ("Action step: " + step);
        if (step.equalsIgnoreCase("entryform")) {
            System.out.println ("before showCustomerStatement");
            showCustomerStatement(servlet, request, response);
            System.out.println ("after showCustomerStatement");
        } else if (step.equalsIgnoreCase("process")) {
            System.out.println ("before submitCustomerStatement");
            submitCustomerStatement(servlet, request, response);
            System.out.println ("after submitCustomerStatement");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }		
	
    private void showCustomerStatement(HttpServlet servlet, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();

        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        CMSSvcCustomerStatementBean customerStatement = new CMSSvcCustomerStatementBean(properties);

        // Get Account ID of Customer.
        String accountId = validUser.getAccountId();

        // Set Account ID of Customer on Customer Statement.
        customerStatement.setAccountId(accountId);

        System.out.println("Show CMSSvcCustomerStatement Page");

        session.setAttribute("CMSSvcCustomerStatementBean", customerStatement);
		
        String jspName = Util.getCMFile("CMSSvcCustomerStatement.jsp", context);
        forward(jspName, request, response);
    }

    private void submitCustomerStatement(HttpServlet servlet, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
		
        // Get account id from the validUser bean.
		ServletContext context = servlet.getServletContext();
        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        String accountId = validUser.getAccountId();
        Properties properties = (Properties) servlet.getServletContext().getAttribute("properties");
		
        String billHistStart = request.getParameter("billingHistoryStart");
        String billHistEnd = request.getParameter("billingHistoryEnd");
        System.out.println ("billHistStart: " + billHistStart);
        System.out.println ("billHistEnd: " + billHistEnd);

        if (billHistStart == null || billHistEnd == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
		
        // Get the temporary directory for PDF files from configuration file.
        // If the PDFCacheLocation parameter is not defined, we'll take the default system temp directory.
        // PDF files will be stored in subdirectory SelfService/PDFCache under the temp directory.
        String tmpdir = properties.getProperty("com.splwg.selfservice.PDFCacheLocation", System.getProperty("java.io.tmpdir"));
        File cachedir = new File(new File(tmpdir, "SelfService"), "PDFCache");
        // Create directory if it does not exist.
        cachedir.mkdirs(); 

        // This is the name of the cached PDF file 
        // - a concatenation of account id and billing history start and end months, with extension ".pdf".
		String startDate = billHistStart.replaceAll("/", "");
		String endDate = billHistEnd.replaceAll("/", "");
        File file = new File(cachedir, accountId + "-" + startDate + "-" + endDate + ".pdf");

        // PDFCacheTimeout is a configuration parameter. Default is 60 minutes.
        long timeout = 60000L * Long.parseLong(properties.getProperty("PDFCacheTimeout", "60"));

        if (!haveCachedPDF(file, timeout)) {
            // If the PDF is not cached, it needs to be generated
            // on the CorDaptix server, retrieved and stored locally.
            if (!isGeneratePDFSuccessful(accountId, billHistStart, billHistEnd, file, properties, request, response)) {
                return;
            }
        }

        // Generate response from the cached PDF file.
        sendResponse(file, timeout, request, response);
    }


    private boolean isGeneratePDFSuccessful(String accountId, String billHistStart, String billHistEnd,
            File file, Properties properties, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       String hp_pdf = properties.getProperty("com.splwg.selfservice.PDFCacheHP", "HP");

       if (hp_pdf.equals("HP"))

           return isGeneratePDFSuccessfulHP(accountId, billHistStart, billHistEnd, file, properties, request, response);
       else
           return isGeneratePDFSuccessfulCS(accountId, billHistStart, billHistEnd, file, properties, request, response);
    }


    /**
     * Generate PDF on the server by calling shell script to HP exstream, and store it
     * in the PDF cache on this server.
     *
     * @throws IOException
     */
    private boolean isGeneratePDFSuccessfulHP(String accountId, String billHistStart, String billHistEnd, 
            File file, Properties properties, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        System.out.println ("Start isGeneratePDFSuccessfulHP");

        String rpt_dir = properties.getProperty("com.splwg.selfservice.ReportLocation",
                                        "/spl/hp/exstream/reportsAndBills/applications/steam/output/reports/consltb/");

        String rpt_cmd = properties.getProperty("com.splwg.selfservice.ReportCommand",
                             "/spl/hp/exstream/reportsAndBills/applications/steam/setup/runengineLinuxRPT_CONSLTB.sh");


        String rpt_csv = properties.getProperty("com.splwg.selfservice.ReportParams",
                                   "/spl/hp/exstream/reportsAndBills/applications/steam/input/RPT_Driver_CONSLTB.csv");

        try {
            File csv_file = new File(rpt_csv);

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(csv_file));
            out.write("100,Report Name,Stored Procedure Name, Parameter 1, Parameter 2, Parameter 3,\n".getBytes());
            out.write("101,CM_CONSLTB_LIST,CM_CONSLTB_LIST,".getBytes());
            out.write(accountId.getBytes());
            out.write(",NEWACCT,".getBytes());
            out.write(billHistStart.getBytes());
            out.write(",".getBytes());
            out.write(billHistEnd.getBytes());

            out.close();
            String[] env = {"PATH=/bin:/usr/bin"};
            Process proc = Runtime.getRuntime().exec(rpt_cmd, env);
            int exitVal = proc.waitFor();

            File dir = new File(rpt_dir);
            File[] files = dir.listFiles(new FilenameFilter() {
               public boolean accept(File dir, String name) {
                  return name.toLowerCase().endsWith(".pdf");
               }
            });
            File source = files[0];
            Files.copy(source.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new ServletException("Cannot write report PDF to local cache:\n" + e.getMessage());
        }
        return true;
    }


    /**
     * Generate PDF on the CorDaptix server, retrieve it by HTTP, and store it
     * in the PDF cache on this server.
     *
     * @throws IOException
     */
    private boolean isGeneratePDFSuccessfulCS(String accountId, String billHistStart, String billHistEnd, 
            File file, Properties properties, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        System.out.println ("Start isGeneratePDFSuccessfulCS");
		CMSSvcCustomerStatementBean crystalRequest = new CMSSvcCustomerStatementBean(properties);
        crystalRequest.setAccountId(accountId);
        crystalRequest.setBillingHistoryStartMonth(billHistStart);
        crystalRequest.setBillingHistoryEndMonth(billHistEnd);
        crystalRequest.initializeCrystalRequestBean();
        BufferedInputStream in = null;
        HttpURLConnection httpConnection = null;

        try {
            // Construct data
            String data = URLEncoder.encode("rptEngParms", "UTF-8") + "=" + URLEncoder.encode(crystalRequest.getReportEngineParameters(), "UTF-8");
            data += "&" + URLEncoder.encode("parms", "UTF-8") + "=" + URLEncoder.encode(crystalRequest.getReportParameters(), "UTF-8");
            data +="\r\n";
			
            // Send data
            URL url = new URL(crystalRequest.getActionURL());
            httpConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setDoOutput(true);

            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.setRequestProperty("Content-length", String.valueOf(data.length()));
            DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());

            dos.writeBytes(data);
            dos.flush();

            String headerName;
            String sessionId = "";
			
            for (int i = 1; (headerName = httpConnection.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equalsIgnoreCase("Set-Cookie")) {
                    if (httpConnection.getHeaderField(i).startsWith("JSESSIONID", 0))
                        sessionId = httpConnection.getHeaderField(i);
                }
            }

            int httpResponseCode = httpConnection.getResponseCode();
			
            String redirectedURLString = httpConnection.getHeaderField("Location");		
            boolean redirected = ((httpResponseCode >= 300) && (httpResponseCode <= 399));

            if (redirected && null != redirectedURLString) {
                url = new URL(redirectedURLString);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setDoInput(true);
                httpConnection.setRequestProperty("Cookie", sessionId);
                httpConnection.setRequestMethod("GET");
                httpResponseCode = httpConnection.getResponseCode();
            }

            if (httpResponseCode != HttpServletResponse.SC_OK) {
                response.sendError(httpResponseCode);
            }

            in = new BufferedInputStream(httpConnection.getInputStream());
        } catch (IOException e) {
            throw new ServletException("Cannot read bill PDF on server:\n" + e.getMessage());
        }

        String contentType = httpConnection.getHeaderField("content-type");
        int c;
        int crResponseLength = httpConnection.getContentLength();

        if (!contentType.startsWith("application/pdf")) {
            response.setContentType(contentType);
            response.setContentLength(crResponseLength);
            ServletOutputStream out = response.getOutputStream();
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            return false;
        }

        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            throw new ServletException("Cannot write bill PDF to local cache:\n" + e.getMessage());
        }
        return true;
    }

    /**
     * Check if the cached PDF exists and if it has not expired.
     */
    private boolean haveCachedPDF(File file, long timeout) {
        if (file.isFile() && file.canRead()) {
            if (System.currentTimeMillis() - file.lastModified() < timeout) {
                return true;
            }
        }
		
        return false;
    }

    /**
     * Retrieve the PDF from cache and include it in the response.
     */
    private void sendResponse(File file, long timeout, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        long lastModified = file.lastModified();
        if (request.getDateHeader("If-Modified-Since") >= lastModified) {
            // Handle conditional GET
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
		
        response.setContentType("application/pdf");
        response.setContentLength((int) file.length());
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", lastModified + timeout);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        ServletOutputStream out = response.getOutputStream();
		
        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
		
        in.close();
        out.close();
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("CMSSvcCustomerStatementAction [" + url + "]");
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

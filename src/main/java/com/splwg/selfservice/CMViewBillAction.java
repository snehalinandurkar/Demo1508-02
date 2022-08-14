package com.splwg.selfservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author RRadziszewski
 *
 */
public class CMViewBillAction implements SSvcAction {

    /*
     * (non-Javadoc)
     *
     * @see com.splwg.selfservice.SSvcAction#perform(javax.servlet.http.HttpServlet,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void perform(HttpServlet servlet, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        String billId = request.getParameter("billId");

        // Get bill id from the request, account id from the validUser bean.
        // Account id will be used to validate that the user is requesting
        // a bill for his account.
        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        String accountId = validUser.getAccountId();
        Properties properties = (Properties) servlet.getServletContext().getAttribute("properties");
		
        if (billId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get the temporary directory for PDF files from configuration file.
        // If the PDFCacheLocation parameter is not defined, we'll take the
        // default system temp directory.
        // PDF files will be stored in subdirectory SelfService/PDFCache under
        // the temp directory.

        String tmpdir = properties.getProperty("com.splwg.selfservice.PDFCacheLocation",
                                        System.getProperty("java.io.tmpdir"));
        File cachedir = new File(new File(tmpdir, "SelfService"), "PDFCache");
        cachedir.mkdirs(); // create directory if it does not exist

        // This is the name of the cached PDF file -- a concatenation of account
        // id and bill id, with extension ".pdf".
        File file = new File(cachedir, accountId + "-" + billId + ".pdf");

        // PDFCacheTimeout is a configuration parameter.
        // Default is 60 minutes.
        long timeout = 60000L * Long.parseLong(properties.getProperty("PDFCacheTimeout", "60"));

        if (!haveCachedPDF(file, timeout)) {
            // If the PDF is not cached, it needs to be generated
            // on the CorDaptix server, retrieved and stored locally.
            if (!isGeneratePDFSuccessful(accountId, billId, file, properties, request, response)) {
                return;
            }
        }

        // Generate response from the cached PDF file.
        sendResponse(file, timeout, request, response);
    }


    private boolean isGeneratePDFSuccessful(String accountId, String billId,
            File file, Properties properties, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

       String hp_pdf = properties.getProperty("com.splwg.selfservice.PDFCacheHP", "HP");

       if (hp_pdf.equals("HP"))
          return isGeneratePDFSuccessfulHP(accountId, billId, file, properties, request, response);
       else
          return isGeneratePDFSuccessfulCS(accountId, billId, file, properties, request, response);
     } 

    /**
     * Generate PDF on the server by calling shell script to HP exstream, and store it
     * in the PDF cache on this server.
     *
     * @throws IOException
     */
    private boolean isGeneratePDFSuccessfulHP(String accountId, String billId,
            File file, Properties properties, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String bill_dir = properties.getProperty("com.splwg.selfservice.BillIDLocation",
                                        "/spl/CCB26TST1/splapp/applications/root/cm/");

        String bill_cmd = properties.getProperty("com.splwg.selfservice.BillCommand",
         "/spl/hp/exstream/reportsAndBills/applications/steam/setup/runEngineLinuxBill_onDemand.sh");


        String bill_dst = properties.getProperty("com.splwg.selfservice.BillDestination",
         "/spl/app/Middleware2/user_projects/domains/CCB26TST1/servers/ccb26tst1_server/tmp/_WL_user/SPLWeb/n3hr9b/war/");

        BillBean bill = new BillBean();
        bill.setBillId(billId);
        bill.setProperties(properties);
        String billAccountId = bill.getAccountId();

        ViewBillBean viewBill = new ViewBillBean(properties);
        viewBill.setBillId(billId);
        viewBill.retrieveOutputPdf();

        String outputPdf = viewBill.getOutputPdf();

        if (billAccountId == null || !billAccountId.equals(accountId)) {
            // The user typed in a bill id that does not exist in CorDaptix.
            // or the user requested a bill from someone else's account by
            // typing a bill id directly in the browser.
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        try {
            File billid_file = new File(bill_dir, accountId + "-" + billId + ".txt");
            File source = new File(bill_dst, billId + ".pdf");

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(billid_file));
            out.write(billId.getBytes());
            out.close();

            String[] env = {"PATH=/bin:/usr/bin"};
            Process proc = Runtime.getRuntime().exec(bill_cmd+" "+outputPdf+" "+source.toPath(), env);
            int exitVal = proc.waitFor();

            Files.copy(source.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new ServletException("Cannot write bill PDF to local cache:\n" + e.getMessage());
        }
        return true;
    }


    /**
     * Generate PDF on the CorDaptix server, retrieve it by HTTP, and store it
     * in the PDF cache on this server.
     *
     * @throws IOException
     */
    private boolean isGeneratePDFSuccessfulCS(String accountId, String billId,
            File file, Properties properties, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        BillBean bill = new BillBean();
        bill.setBillId(billId);
        bill.setProperties(properties);
        String billAccountId = bill.getAccountId();

        if (billAccountId == null || !billAccountId.equals(accountId)) {
            // The user typed in a bill id that does not exist in CorDaptix.
            // or the user requested a bill from someone else's account by
            // typing a bill id directly in the browser.
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        CMCrystalRequestBean crystalRequest = new CMCrystalRequestBean(properties);
        crystalRequest.initializeCrystalRequestBean(billId);
        BufferedInputStream in = null;
        HttpURLConnection httpConnection = null;

        try {
            // Construct data
            String data = URLEncoder.encode("rptEngParms", "UTF-8") + "="
                    + URLEncoder.encode(crystalRequest.getReportEngineParameters(), "UTF-8");
            data += "&" + URLEncoder.encode("parms", "UTF-8") + "="
                    + URLEncoder.encode(crystalRequest.getReportParameters(), "UTF-8");
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
    private void sendResponse(File file, long timeout,
            HttpServletRequest request, HttpServletResponse response)
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

}

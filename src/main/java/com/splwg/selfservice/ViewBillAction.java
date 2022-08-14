/*
 * ViewBillAction.java
 *
 * Created on March 11, 2002, 12:01 PM
 */

package com.splwg.selfservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author  Jacek Ziabicki
 */
public class ViewBillAction
    implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    /** Validate user, check if the bill requested is for the user's account.
     *  Check if a recent copy of the bill PDF is available in the cache.
     *  The filename of the cached PDF will include account id and bill id.
     *  If a cached copy is not available, generate the PDF, HTTP GET it from
     *  the CorDaptix server and cache it on this server.
     *  Forward the HTTP request to the PDF file in the cache.
     */
    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
        // Get bill id from the request, account id from the validUser bean.
        // Account id will be used to validate that the user is requesting
        // a bill for his account.
        HttpSession session = request.getSession();
        ValidUserBean validUser = (ValidUserBean) session.getAttribute("SelfService_validUser");
        String accountId = validUser.getAccountId();
        String billId = request.getParameter("billId");
        if (billId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get the temporary directory for PDF files from configuration file.
        // If the PDFCacheLocation parameter is not defined, we'll take the
        // default system temp directory.
        // PDF files will be stored in subdirectory SelfService/PDFCache under
        // the temp directory.
        ServletContext context = servlet.getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        String tmpdir = properties.getProperty("com.splwg.selfservice.PDFCacheLocation",
                                               System.getProperty("java.io.tmpdir"));
        File cachedir = new File(new File(tmpdir, "SelfService"), "PDFCache");
        cachedir.mkdirs(); // create directory if it does not exist

        // This is the name of the cached PDF file -- a concatenation of account
        // id and bill id, with extension ".pdf".
        File file = new File(cachedir, accountId + billId + ".pdf");

        // PDFCacheTimeout is a configuration parameter.
        // Default is 60 minutes.
        long timeout = 60000L * Long.parseLong(
                                                   properties.getProperty("PDFCacheTimeout", "60"));

        if (! haveCachedPDF(file, timeout)) {
            // If the PDF is not cached, it needs to be generated
            // on the CorDaptix server, retrieved and stored locally.
            if (! isGeneratePDFSuccessful(accountId, billId, file, properties,
                                          request, response)) {
                return;
            }
        }

        // Generate response from the cached PDF file.
        sendResponse(file, timeout, request, response);
    }

    /** Check if the cached PDF exists and if it has not expired.
     */
    private boolean haveCachedPDF(File file, long timeout) {
        if (file.isFile() && file.canRead()) {
            if (System.currentTimeMillis() - file.lastModified() < timeout) {
                return true;
            }
        }
        return false;
    }

    /** Generate PDF on the CorDaptix server, retrieve it by HTTP, and store it
     *  in the PDF cache on this server.
     */
    private boolean isGeneratePDFSuccessful(String accountId, String billId,
                                            File file, Properties properties, HttpServletRequest request,
                                            HttpServletResponse response) throws IOException,
                                                                                 ServletException {
        BillBean bill = new BillBean();
        bill.setBillId(billId);
        bill.setProperties(properties);

        AccountBean tmpAcct = new AccountBean();
        tmpAcct.setAccountId(accountId);
        tmpAcct.setProperties(properties);

        if (tmpAcct.retrieveCurrencySymbol()) {
            bill.setCurrencySymbol(tmpAcct.getCurrencySymbol());
            bill.setCurrencyPosition(tmpAcct.getCurrencyPosition());
        } else {
            return (false);
        }

        tmpAcct = null;

        String onlineBillURL = bill.getOnlineBillURL(); // this generates PDF
        String billAccountId = bill.getAccountId();

        if (billAccountId == null) {
            // The user typed in a bill id that does not exist in CorDaptix.
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        if (! billAccountId.equals(accountId)) {
            // The user requested a bill from someone else's account by typing
            // a bill id directly in the browser.
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        BufferedInputStream in;
        try {
            /* URL billPDF = new URL(properties.getProperty(
                "com.splwg.selfservice.OnlineBillURLPrefix"
                ) + onlineBillURL);
                in = new BufferedInputStream(billPDF.openStream());
                */
            URL url = new URL(properties.getProperty(
                                                     "com.splwg.selfservice.OnlineBillURLPrefix")
                              + onlineBillURL);
            String authCookie = properties.getProperty("com.splwg.selfservice.XAICookie");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("Authorization", "Basic " + authCookie);
            connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");

            in = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new ServletException(
                                       "Cannot read bill PDF on CorDaptix server:\n" + e.getMessage());
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            throw new ServletException(
                                       "Cannot write bill PDF to local cache:\n" + e.getMessage());
        }
        return true;
    }

    /** Retrieve the PDF from cache and include it in the response.
     */
    private void sendResponse(File file, long timeout,
                              HttpServletRequest request, HttpServletResponse response) throws IOException,
                                                                                               ServletException {
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

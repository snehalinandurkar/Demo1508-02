/*
 * RESTHTTPCall.java
 *
 * Created on October 8, 2021, 10:10 AM
 */

package com.splwg.selfservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import javax.servlet.ServletContext;

/**
 *
 * @author Jacek Ziabicki
 */
public class RESTHTTPCall {

    //~ Instance fields --------------------------------------------------------------------------------------

    private URL url;
    private String rest;
    private String authCookie;
    private String userId;
    private String password;
    private ArrayList<String> cookieList = new ArrayList<String>();

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates a new instance of RESTHTTPCall */
    public RESTHTTPCall(Properties properties) {
        rest = properties.getProperty("com.splwg.selfservice.RESTServerURL");

        authCookie = properties.getProperty("com.splwg.selfservice.XAICookie");
        String decodedCookie = new String(Base64.getDecoder().decode(authCookie));
        String[] credentials = decodedCookie.split(":");
        userId = credentials[0];
        password = credentials[1];
        System.out.println("authCookie = " + authCookie);
        System.out.println("userId     = " + userId);
        System.out.println("password   = " + password);
        System.out.println("curr time  = " + String.valueOf(System.currentTimeMillis()));
    }


    //~ Methods ----------------------------------------------------------------------------------------------

    public String callRESTServer(String json, String serviceType, String service) throws  MalformedURLException, IOException {
//        ServletContext context = getServletContext();
//        if (context.getAttribute("CCBSessionCookie") == null) {
//            System.out.println("CCBSessionCookie is null");
//        } else {
//            System.out.println("CCBSessionCookie: " + context.getAttribute("CCBSessionCookie"));
//        }
//        Properties properties = (Properties) context.getAttribute("properties");

        callLoginService();
        return callTargetService(json, serviceType, service);
        
        

    }


    public String callLoginService() throws IOException {
        url = new URL("https://ccbtst.conedison.net:7501/ouaf/j_security_check");
        String body = "j_username=" + userId + "&j_password=" + password;
        System.out.println(body);
        String ResponseCharset;
        byte[] postData = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int postLen = postData.length;
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postLen));
        connection.setRequestProperty("Connection", "keep-alive");

        System.out.println("callXAIServer - " + authCookie + " [" + connection.getURL() +"]" );

        // PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection
        //                                                                                .getOutputStream(),
        //                                                                            "UTF-8")));
        // out.println(xml);
        // out.flush();
        // out.close();
        //
        connection.getOutputStream().write(postData);

        System.out.println("Reading response...");

        ResponseCharset = connection.getContentType();
        System.out.println(ResponseCharset);
        System.out.println(connection.getResponseCode());
        System.out.println("Headers:");
        cookieList.clear();
        for (int i=0; i<=200; i++) {
            String headerKey = connection.getHeaderFieldKey(i);
            String headerField = connection.getHeaderField(i);
            System.out.println(headerKey + ":  " + headerField);
            if (headerKey == null && headerField == null) break;
            if (headerKey != null && headerKey.equals("Set-Cookie")) cookieList.add(headerField);
        }
        if (ResponseCharset != null) {
            int i = ResponseCharset.indexOf("charset=");
            if (i != -1)
                ResponseCharset = ResponseCharset.substring(i + 8).toUpperCase(); // 8 - len of charset=
            else ResponseCharset = null;
        }

        BufferedReader in;
        if (ResponseCharset == null) {
            in = new BufferedReader(new InputStreamReader(
                                                          connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(
                                                          connection.getInputStream(),
                                                          ResponseCharset));
        }

        int c;
        StringWriter s = new StringWriter(8192);
        while ((c = in.read()) != -1) {
            s.write(c);
        }
//        return connection.getHeaderField(0);
        return cookieList.toString();
    }



    public String callTargetService(String json, String serviceType, String service) throws IOException {
        url = new URL(rest + "/" + serviceType + "/" + service);
        String ResponseCharset;
        byte[] postData = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int postLen = postData.length;
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postLen));
        connection.setRequestProperty("Authorization", "Basic " + authCookie);
        String sb = "";
        for (String cookie : cookieList) {
            //String[] c = cookie.split(";");
            String[] cookieValue = cookie.split("=", 2);
            //sb.append(c[0]);
            sb = sb.concat(cookieValue[0]).concat("=").concat(cookieValue[1].split(";")[0]).concat(";");
        }
 //       for (String cookie: cookies) {
 //           String[] cookieValue = cookie.split("=", 2);
 //           temp = temp.concat(cookieValue[0]).concat("=").concat(cookieValue[1].split(";")[0]).concat(";");
 //       }
        sb = sb.substring(0, sb.length() - 1);
        connection.setRequestProperty("Cookie", sb); 
        System.out.println("Cookie: " + sb);
        System.out.println("callXAIServer - " + authCookie + " [" + connection.getURL() +"]" );

        // PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection
        //                                                                                .getOutputStream(),
        //                                                                            "UTF-8")));
        // out.println(xml);
        // out.flush();
        // out.close();
        //
        connection.getOutputStream().write(postData);

        System.out.println("Reading response...");

        ResponseCharset = connection.getContentType();
        System.out.println(ResponseCharset);
        System.out.println(connection.getResponseCode());
        System.out.println("Headers:");

        for (int i=0; i<=200; i++) {
            String headerKey = connection.getHeaderFieldKey(i);
            String headerField = connection.getHeaderField(i);
            System.out.println(headerKey + ":  " + headerField);
            if (headerKey == null && headerField == null) break;
        }
        if (ResponseCharset != null) {
            int i = ResponseCharset.indexOf("charset=");
            if (i != -1)
                ResponseCharset = ResponseCharset.substring(i + 8).toUpperCase(); // 8 - len of charset=
            else ResponseCharset = null;
        }

        BufferedReader in;
        if (ResponseCharset == null) {
            in = new BufferedReader(new InputStreamReader(
                                                          connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(
                                                          connection.getInputStream(),
                                                          ResponseCharset));
        }

        int c;
        StringWriter s = new StringWriter(8192);
        while ((c = in.read()) != -1) {
            s.write(c);
        }
        return s.toString();
    }
}

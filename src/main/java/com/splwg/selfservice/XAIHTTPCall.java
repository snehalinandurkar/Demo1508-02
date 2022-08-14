/*
 * XAIHTTPCall.java
 *
 * Created on March 22, 2002, 4:10 PM
 */

package com.splwg.selfservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jacek Ziabicki
 */
public class XAIHTTPCall {

    //~ Instance fields --------------------------------------------------------------------------------------

    private URL url;
    private String iws, xai, rest;
    private String authCookie;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates a new instance of XAIHTTPCall */
    public XAIHTTPCall(Properties properties) {
        xai = properties.getProperty("com.splwg.selfservice.XAIServerURL");
        iws = properties.getProperty("com.splwg.selfservice.IWSServerURL");
		rest= properties.getProperty("com.splwg.selfservice.RESTServerURL");

        authCookie = properties.getProperty("com.splwg.selfservice.XAICookie");
    }

    public XAIHTTPCall( ) {
    	
    	//String authURL = "https://ccbtst.conedison.net:7501/ouaf/j_security_check?j_username=SYSUSER&j_password=sysuser00";
		//rest = "https://ccbtst.conedison.net:7501/ouaf/rest/ouaf/script/CMStmBillPd/";

    }
    //~ Methods ----------------------------------------------------------------------------------------------

    public String callXAIServer(String xml) throws  MalformedURLException, IOException {
        url = new URL(xai);
        return callServer(xml, "text/xml;charset=UTF-8");
    }


    public String callIWSServer(String service, String xml) throws MalformedURLException, IOException {
        url = new URL(iws + "/" + service);
        return callServer(xml, "text/xml;charset=UTF-8");
    }


    public String callRESTServer(String serviceType, String service, String json) throws MalformedURLException, IOException {
        
		System.out.println("called Rest");
    	//url = new URL(rest + "/" + serviceType + "/" + service);
       // return callServer(json, "application/json;charset=UTF-8");
    	
    	String authURL = "https://ccbtst.conedison.net:7501/ouaf/j_security_check?j_username=SYSUSER&j_password=sysuser00";
		String ccbUrl = rest + "/" + serviceType + "/" + service;
		//String jsonInputString = "{\"CMStmBillPd\":{\"input\":{\"accountId\":\"3742000000\"}}}";

		String retrunResponse="";
    	HttpClient httpClient = null;

		HttpPost httpRequest = new HttpPost(authURL);
		StringEntity body;
		try {
			body = new StringEntity("enitiy body");
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setEntity(body);

			CookieStore httpCookieStore = new BasicCookieStore();
			HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
			httpClient = builder.build();
			HttpResponse response = httpClient.execute(httpRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				// log.error("Error: Unable to Login! Call to login service
				// failed!");
				System.out.println("failed");
				System.out.println(response.getStatusLine().getStatusCode());
			} else {
				System.out.println("Login Success!");
				//Header[] header= response.getAllHeaders();
				httpRequest = new HttpPost(ccbUrl);
				body = new StringEntity(json);
				httpRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
				httpRequest.setHeader("Accept", "application/json;charset=UTF-8");
				// httpRequest.setHeaders(header);
				httpRequest.setEntity(body);

				response = httpClient.execute(httpRequest);
				System.out.println(response.getStatusLine().getStatusCode());
				// System.out.println(response);
				HttpEntity e = response.getEntity();
				retrunResponse = EntityUtils.toString(e);
				return retrunResponse;

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retrunResponse;
        
    }


    public String callServer(String msg, String contentType) throws IOException {
        String ResponseCharset;
        byte[] postData = msg.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int postLen = postData.length;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + authCookie);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Length", Integer.toString(postLen));

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

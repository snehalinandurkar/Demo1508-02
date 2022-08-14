
package com.splwg.selfservice;

import java.io.*;
import java.util.Properties;

public class ServiceTest {
    public static void main(String[] args) throws Exception {
	    Properties properties = new Properties();
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(
		    new File("C:/SelfServiceDev/dev/WEB-INF/SelfServiceConfig.properties"))
        );
        properties.load(in);
        System.out.println("Server URL: " + properties.getProperty("com.splwg.selfservice.RESTServerURL"));
		
		String msg = "{\"CMStmBillPd\" : {\"input\" : { \"accountId\" : \"2542000000\" }}}";


		RESTHTTPCall httpCall = new RESTHTTPCall(properties);
        String httpResponse = httpCall.callRESTServer("script", "CMStmBillPd", msg);
        System.out.println(httpResponse);
    }
}

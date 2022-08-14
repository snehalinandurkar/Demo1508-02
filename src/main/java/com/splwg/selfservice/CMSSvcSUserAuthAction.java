/*
 * CMSSvcSUserAuthAction.java
 *
 * Created on Dec 28, 2011, 12:01 PM by Edrik Pagaduan
 * Part of CorDaptix Web Self Service
 * Implements CEd9 Authentication of the super user
 *
 */
package com.splwg.selfservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;


public class CMSSvcSUserAuthAction
        implements SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

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
                String jspName = Util.getCMFile("CMSSvcSUserLogIn.jsp", context);
                forward(jspName + "?errorMsg=" + invalidParamError, request, response);
                return;
            }
            
        }


        //~  Authenticate using ConEd ADS
        //   Details of ADS are stored in the properties file
        
        Properties properties = (Properties) context.getAttribute("properties");
        HttpSession session = request.getSession();
        String inputPassword = request.getParameter("password");
        System.out.println("password["+ inputPassword +"]");
        String personId = request.getParameter("personId");
        System.out.println("personId["+ personId +"]");

        String errorMessage = "";
        String inputUserId = request.getParameter("userId");
        System.out.println("inputUserId["+ inputUserId +"]");

	if (inputUserId.length()>8) {
       		errorMessage = "Invalid CCB User ID";
        	sendError(request, response, errorMessage, context);
        	return;
	}

        String dn = properties.getProperty("com.splwg.selfservice.ADSdn");
        System.out.println("DN["+ dn +"]");
        String domain = properties.getProperty("com.splwg.selfservice.ADSdomain");
        System.out.println("domain["+ domain +"]");
        String host = properties.getProperty("com.splwg.selfservice.ADShost");
        System.out.println("host["+ host +"]");
        String attributeForUser = properties.getProperty("com.splwg.selfservice.ADSaMAccountName");
        System.out.println("aMAccountName["+ attributeForUser +"]");
                
        boolean authenticate = authenticateCCBUser(inputUserId, inputPassword, domain, host, dn, attributeForUser);
        System.out.println("authenticate["+ authenticate +"]");
        String entityName = "";
        //authenticate = true;
        System.out.println("authenticate["+ authenticate +"]");
        if (!authenticate){
        	errorMessage = "Authentication error";
        	sendError(request, response, errorMessage, context);
        	return;
        }
        

        boolean authorized = false;
        String suName = "";
        
        String xml1 = new StringBuffer(512).append(
		"<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
		.append("<CmSSvcUserMaintenance transactionType='READ'>\n").append("<CmSSvcUserMaintenanceService>\n")
		.append("<CmSSvcUserMaintenanceHeader User = '").append(inputUserId).append("'/>\n").append(
		"</CmSSvcUserMaintenanceService>\n").append("</CmSSvcUserMaintenance>\n").append(
		"</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();
        ;
        
        String suGroup = properties.getProperty("com.splwg.selfservice.suGroup");

        try {
		XAIHTTPCall httpCall1 = new XAIHTTPCall(properties);
		String httpResponse1 = httpCall1.callXAIServer(xml1);
		Document document1 = DocumentHelper.parseText(httpResponse1);
		Element faultEle1 = (Element) document1.selectSingleNode("//SOAP-ENV:Fault");
		if (faultEle1 != null) {
			Node error = (Element) document1.selectSingleNode("//*[local-name()='ResponseText']");
		      errorMessage = error.getText();
	        	sendError(request, response, errorMessage, context);
		      return;
		}
		
		Element ele1 = (Element) document1.selectSingleNode("//*[local-name()='CmSSvcUserMaintenanceDetails']");
		suName = Util.getValue(ele1, "@FirstName") + " " + Util.getValue(ele1, "@LastName");
	        List list = document1.selectNodes("//*[local-name()='UserUsrGrpRow']");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ele1 = (Element) iter.next();
		    	if(Util.getValue(ele1, "@UserGroup").equalsIgnoreCase(suGroup)){ 
	    			System.out.println("User is a super user");
	    	    		authorized = true;
		    	    	System.out.println("CCB user id [" + inputUserId + "] is a super user");
		    	}
		}
        } catch (Exception e) {
	     	//errorMessage = e.getMessage();
	     	errorMessage = "CCB user id does not exist.";
        	sendError(request, response, errorMessage, context);
        	return;
        }
        System.out.println(suName);
        if (!authorized) {
        	errorMessage = "CCB user id provided does not have super user privileges.";
        	sendError(request, response, errorMessage, context);
        	return;        	
        }
        
	  PersonBean person = new PersonBean();
    	
    	  person.setPersonId(personId);
        person.setProperties(properties);
        if (!person.retrievePerson()) {
        	errorMessage = "Person does not exist";
        	sendError(request, response, errorMessage, context);
        	return;
        } 

        System.out.println("No person retrieval error");
    	
        String xml = new StringBuffer(512).append(
        			"<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
        			.append("<SSvcPersonMaintenance transactionType='READ'>\n").append("<SSvcPersonMaintenanceService>\n")
        			.append("<SSvcPersonMaintenanceHeader PersonID = '").append(personId).append("'/>\n").append(
        			"</SSvcPersonMaintenanceService>\n").append("</SSvcPersonMaintenance>\n").append(
        			"</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();
        ;
	    String passwordPlain="", webUserId="", databasePassword="";
        try {
		XAIHTTPCall httpCall = new XAIHTTPCall(properties);
		String httpResponse = httpCall.callXAIServer(xml);
		Document document = DocumentHelper.parseText(httpResponse);
		Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
		if (faultEle != null) {
			Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
		      errorMessage = error.getText();
	        	sendError(request, response, errorMessage, context);
		      return;
		}
		
		Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");
		
		databasePassword = Util.getValue(ele, "@WebPassword");
		
		entityName = Util.getValue(ele, "@Name");
		
		CMPersonIdentifierBean personIdentifier = new CMPersonIdentifierBean();
		personIdentifier.setPersonId(personId);
		personIdentifier.setPersonIdentifierType("WEB");
		personIdentifier.setProperties(properties);
		
		webUserId = personIdentifier.getPersonIdentifierValue();

		//List list = document.selectNodes("//*[local-name()='PersonIdentifiersRow']");
    	    	//for (Iterator iter = list.iterator(); iter.hasNext();) {
        	//	ele = (Element) iter.next();
        	//    	if(Util.getValue(ele, "@IDType").equalsIgnoreCase("WEB")){ 
        	//    		System.out.println("Web user id retrieved");
        	//    		webUserId = Util.getValue(ele, "@IDNumber");
        	//    		System.out.println("Web user id [" + webUserId + "]");
        	//    	}
    	    	//}
		    
    	    	if (webUserId.equals("")||databasePassword.equals(null)||databasePassword.equals("")) {
            	errorMessage = "Person does not have a WSS account set up.";
            	sendError(request, response, errorMessage, context);
            	return;    	    	
    	    	}
    	    
    	    
	      EncryptionBean encrypt = new EncryptionBean();
	      try {
	            encrypt.setEncryptedInput(databasePassword);
	            passwordPlain = encrypt.decryptPassword();
	        } catch (Exception e) {
	            errorMessage = "Caught exception: " + e.getMessage();
	        	sendError(request, response, errorMessage, context);
	            return;
	        }
        
        } catch (Exception e){
	        errorMessage = e.getMessage();
        	sendError(request, response, errorMessage, context);
        }

        session.setAttribute("accountName",entityName);
        session.setAttribute("password", passwordPlain);
        session.setAttribute("suName", suName);
        session.setAttribute("userId", webUserId);
        session.setAttribute("superUser", "Y");
        String jspName = Util.getCMFile("CMSSvcSUserLanding.jsp", context);
        forward(jspName, request, response);
        return;
    }

    private boolean authenticateCCBUser(String username, String password, String domain, String host, String dn, String attributeForUser) {
	
        String returnedAtts[] ={ "sn", "givenName", "mail" };
        String searchFilter = "(&(objectClass=user)(" + attributeForUser + "=" + username + "))";
        //	Create the search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);
        //	Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
              
        Hashtable environment = new Hashtable();
        
        String searchBase = dn;

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //Using starndard Port, check your instalation
        environment.put(Context.PROVIDER_URL, host);
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, username + domain);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        LdapContext ctxGC = null;
        try {
        	System.out.println("Got here.  Going to initial ldap context");
            ctxGC = new InitialLdapContext(environment, null);

            Thread.sleep(450);

            //    Search for objects in the GC using the filter
		
            NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
            System.out.println("Naming enumeration count[" + answer.hasMoreElements() + "]");
            
            while (answer.hasMoreElements()) {
                System.out.println("Naming enumeration count[" + answer.hasMoreElements() + "]");
                SearchResult sr = (SearchResult)answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null){
                    return true;
                }
            }
        } catch (NamingException ne) {
            	System.out.println("Just reporting naming exception error");
            	ne.printStackTrace();
        } catch (Exception e) {
      			System.out.println("Just reporting general error");
      			e.printStackTrace();
        }
        //Error encountered during authentication, return false to signal authentication
        //failure
        return false;
    }

    private String getPersonLoginDetails(String personId){
    	//Retrieve person's WSS user name and password and store
    	//to a session variable
    	return "";
    }
    
    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMsg,
            ServletContext context) throws IOException, ServletException {
        if (errorMsg == null) errorMsg = "Undefined Error";
        String jspName = Util.getCMFile("CMSSvcSUserLogIn.jsp", context);
        String loginURL = jspName + "?errorMsg=" + URLEncoder.encode(errorMsg);
        System.out.println("loginURL[" + loginURL + "]");
        forward(loginURL, request, response);
    }

    private String getMessage(int nbr, HttpSession session, Properties properties) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}

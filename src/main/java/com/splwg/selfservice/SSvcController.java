/*
 * SSvcController.java
 *
 * Created on February 20, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * This is the servlet holding all pages together and implementing security.
 * It is initially being called by index.html.
 **************************************************************************
 *                                                                
 * CHANGE HISTORY:                                                
 *                                                                
 * Date:       	by:    	Reason:         
 *                             
 * 2011-12-28 	JFerna	CEd9. Update to include actions for WSS Super Users
 * 2012-07-23   SMedin  CEd10. Update to include action for Payment Options
 **************************************************************************
 
 */

package com.splwg.selfservice;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SSvcController
    extends HttpServlet {

    //~ Instance fields --------------------------------------------------------------------------------------

    private HashMap actions;
    Properties properties;
    
    private String uRLParmValue;


    //~ Methods ----------------------------------------------------------------------------------------------

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("Inside SSvcController init - Steve was here!!!");
        readConfiguration(config);
        initActions(config);
       setupPhoneFormat(config);
    }

    /** This method will create a Properties object, load it from the
     *  configuration file and save it in the servlet context.
     *  The configuration file will be located in the WEB-INF directory,
     *  the name of the file is set by an init parameter of the servlet.
     */
    private void readConfiguration(ServletConfig config) throws ServletException {
        ServletContext context = getServletContext();
        String configFileName = config.getInitParameter("ConfigFileName");
        if (configFileName == null || configFileName.equals("")) {
            throw new ServletException(
                                       "ConfigFileName parameter missing in web.xml");
        }
        properties = new Properties();
        context.setAttribute("properties", properties);
        try {
            /*BufferedInputStream in = new BufferedInputStream(
                                                             context.getResourceAsStream("/WEB-INF/"
                                                                                         + configFileName));*/
        	BufferedInputStream  in = new BufferedInputStream(
    			    getClass().getClassLoader().getResourceAsStream(configFileName));
        	//BufferedInputStream in = new BufferedInputStream(this .getClass () .getClassLoader () .getResourceAsStream ( "C:/Users/NANDURKARS/eclipse-workspace22n/Demo/properites/" + configFileName ));
        	//BufferedInputStream in = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("/Demo/properites/"+configFileName));
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new ServletException(
                                       "Could not load configuration file:/properties/" + configFileName
                                       + "\n" + e.getMessage());
        }
        String overridePropFileName = properties.getProperty("com.splwg.selfservice.OverrideProperties");
        try {
            /*BufferedInputStream in = new BufferedInputStream(
                                                             context.getResourceAsStream("/WEB-INF/"
           
                                                                                         + overridePropFileName));*/
        	BufferedInputStream  in = new BufferedInputStream(
    			    getClass().getClassLoader().getResourceAsStream(overridePropFileName));
        	/*BufferedInputStream in = new BufferedInputStream(
                    context.getResourceAsStream(overridePropFileName));*/
        	//BufferedInputStream in = new BufferedInputStream(this .getClass () .getClassLoader () .getResourceAsStream ( "C:/Users/NANDURKARS/eclipse-workspace22n/Demo/properites/" + overridePropFileName ));
        	//BufferedInputStream in = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("/Demo/properites/"+overridePropFileName));
        	
        	properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new ServletException(
                                       "Could not load override properties file:\n/WEB-INF/"
                                       + overridePropFileName
                                       + "\n" + e.getMessage());
        }
    }

    /** This method will initialize the list of actions. Actions are implemented
     *  as separate classes. The list of class names is read from a file.
     */
    private void initActions(ServletConfig config) throws ServletException {
        actions = new HashMap();

        actions.put("authenticate", new AuthenticateAction());
        actions.put("login", new LoginAction());
        actions.put("logout", new LogoutAction());
        /*
         * 2012-05-06 - SR511 - EPagaduan - Start Change
         */
        //actions.put("account", new AccountInformationAction());ccountHostAllocationsBean.javaAccountHostAllocationsBean.java
        actions.put("account", new CMAccountInformationAction());
        /*
         * 2012-05-06 - SR511 - EPagaduan - End Change
         */
        actions.put("myaccounts", new AccountsForPersonAction());
        actions.put("financialhistory", new AccountFinancialHistoryAction());
        actions.put("billinghistory", new BillingHistoryAction());
        actions.put("pay", new PayOnlineAction());
        actions.put("stopservice", new StopServiceAction());
	  //actions.put("viewbill", new ViewBillAction());     
	  actions.put("viewbill", new CMViewBillAction());
        actions.put("registration", new RegistrationAction());
        actions.put("passwordreminder", new PasswordReminderAction());
        /*
         * 2011-12-28 - CEd9 - JFern - Start Change
         */
        //actions.put("passwordchange", new SSvcPasswordChangeAction());
        actions.put("passwordchange", new CMSSvcPasswordChangeAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Change
         */
        actions.put("viewmeterread", new ViewMeterReadAction());
        actions.put("entermeterread", new EnterMeterReadAction());
        /*
         * 2011-12-28 - CEd9 - JFern - Start Change
         */
        //actions.put("updatepersonalinformation", new SSvcUpdatePersonalInformationAction());
        actions.put("updatepersonalinformation", new CMSSvcUpdatePersonalInformationAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Change
         */
       //CM- actions.put("promptebillregistration", new PromptEbillRegistrationAction());
        /*
         * 2011-12-28 - CEd9 - JFern - Start Add
         */
        actions.put("suauthenticate", new CMSSvcSUserAuthAction());
        actions.put("sulogin", new CMSSvcSULoginAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Add
         */
        /*
         * 2012-07-25 - CEd10 - SMedin - Start Add
         */
        actions.put("paymentoptions", new CMSSvcPaymentOptionsAction());
        /*
         * 2012-07-25 - CEd10 - SMedin - End Add
         */

        actions.put("customerstatement", new CMSSvcCustomerStatementAction());

        actions.put("allocationhistory", new AllocationInformationAction());
        actions.put("subscriberplan", new SubscriberPlanAction());
        actions.put("subscriberedit", new SubscriberEditAction());
        actions.put("subscriberadd", new SubscriberAddAction());
        actions.put("subscribersave", new SubscriberSaveAction());
        actions.put("allocationinformation", new AllocationDetailAction());
        actions.put("statementhistory", new StatementHistoryAction());
        actions.put("subscriberresult", new SubscriberResultAction());
        actions.put("subscriberstatement", new SubscriberStatementAction());
		
		actions.put("subscriberstatement", new SubscriberStatementAction());
		
		actions.put("intervaldatamenu", new CMIntervalDataMenuAction());
		actions.put("cmssvcintervaldatatabular", new CMDataIntervalAction());
		actions.put("CMSSvcPeakDemandTabular", new CMPeakDemandTabReportAction());

        System.out.println("In SSvcController/initActions, actions=" + actions);

        /* DEBUG commented out Begin
        Enumeration propertyName = properties.propertyNames();
        String val = "";
        String name = "";
        String className = "";
        while (propertyName.hasMoreElements()) {
            name = propertyName.nextElement().toString();

            System.out.println("name: " + name);
            if (name.indexOf("selfservice.Action") > 0) {
                val = properties.getProperty(name);
                className = name.substring(name.indexOf("Action") + 7);
                try {
                    actions.put(className, (SSvcAction) Class.forName(val).newInstance());
                    System.out.println("Added action " + className + " with class name " + val);
                } catch (Exception e) {
                    System.out.println("Exception for creation of class " + className + ", with value " + val + " with exc " + e.toString());
                }
            }
        }
        /* DEBUG commented out End */
    }
    
    private void reinitializeActions() throws ServletException{
        actions = new HashMap();

        actions.put("authenticate", new AuthenticateAction());
        actions.put("login", new LoginAction());
        actions.put("logout", new LogoutAction());
        /*
         * 2012-05-06 - SR511 - EPagaduan - Start Change
         */
        //actions.put("account", new AccountInformationAction());
        actions.put("account", new CMAccountInformationAction());
        /*
         * 2012-05-06 - SR511 - EPagaduan - End Change
         */
        actions.put("myaccounts", new AccountsForPersonAction());
        actions.put("financialhistory", new AccountFinancialHistoryAction());
        actions.put("billinghistory", new BillingHistoryAction());
        actions.put("pay", new PayOnlineAction());
        actions.put("stopservice", new StopServiceAction());
        //actions.put("viewbill", new ViewBillAction());
        actions.put("viewbill", new CMViewBillAction());
        actions.put("registration", new RegistrationAction());
        actions.put("passwordreminder", new PasswordReminderAction());
        
        /*
         * 2011-12-28 - CEd9 - JFern - Start Change
         */
        //actions.put("passwordchange", new SSvcPasswordChangeAction());
        actions.put("passwordchange", new CMSSvcPasswordChangeAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Change
         */
        
        actions.put("viewmeterread", new ViewMeterReadAction());
        actions.put("entermeterread", new EnterMeterReadAction());
        
        /*
         * 2011-12-28 - CEd9 - JFern - Start Change
         */
        //actions.put("updatepersonalinformation", new SSvcUpdatePersonalInformationAction());
        actions.put("updatepersonalinformation", new CMSSvcUpdatePersonalInformationAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Change
         */
        
       //CM- actions.put("promptebillregistration", new PromptEbillRegistrationAction());
        
        /*
         * 2011-12-28 - CEd9 - JFern - Start Add
         */
        actions.put("suauthenticate", new CMSSvcSUserAuthAction());
        actions.put("sulogin", new CMSSvcSULoginAction());
        /*
         * 2011-12-28 - CEd9 - JFern - End Add
         */

        /*
         * 2012-07-25 - CEd10 - SMedin - Start Add
         */
        actions.put("paymentoptions", new CMSSvcPaymentOptionsAction());
        /*
         * 2012-07-25 - CEd10 - SMedin - End Add
         */
        actions.put("customerstatement", new CMSSvcCustomerStatementAction());
        actions.put("allocationhistory", new AllocationInformationAction());
        actions.put("subscriberplan", new SubscriberPlanAction());
        actions.put("subscriberedit", new SubscriberEditAction());
        actions.put("subscriberadd", new SubscriberAddAction());
        actions.put("subscribersave", new SubscriberSaveAction());
        actions.put("allocationinformation", new AllocationDetailAction());
        actions.put("statementhistory", new StatementHistoryAction());
        actions.put("subscriberresult", new SubscriberResultAction());
        actions.put("subscriberstatement", new SubscriberStatementAction());
		
		
		actions.put("intervaldatareports", new CMIntervalDataMenuAction());
		//actions.put("cmssvcintervaldatatabular", new CMDataIntervalAction());
		actions.put("cmssvcintervaldatatabular", new CMIntervalDataTabularAction());
		actions.put("cmssvcpeakdemandtabreportselect", new CMPeakDemandTabReportAction());

	}

    /** This method will read the PhoneFormat field in the properties file and
     *  generate a regular expression to be used in validating phone numbers.
     *  The Phone Format and Email Format will be compiled as Perl5 regular
     *  expressions and saved in the servlet context.
     */
    private void setupPhoneFormat(ServletConfig config) throws ServletException {
        ServletContext context = getServletContext();
        Properties properties = (Properties) context.getAttribute("properties");
        String phoneFormat = properties.getProperty("com.splwg.selfservice.PhoneFormat");
        StringBuffer phoneFormatRegex = new StringBuffer(32);
        char c;
        for (int i = 0; i < phoneFormat.length(); i++) {
            c = phoneFormat.charAt(i);
            switch (c) {
                case '0': // any digit 0-9
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    phoneFormatRegex.append("\\d");
                    break;

                case '(':
                    phoneFormatRegex.append("\\(");
                    break;

                case ')':
                    phoneFormatRegex.append("\\)");
                    break;

                case '.':
                    phoneFormatRegex.append("\\.");
                    break;

                default:
                    phoneFormatRegex.append(c);
                    break;
            }
        }

        Pattern phonePattern;
        Pattern emailPattern;
        try {
            phonePattern = Pattern.compile(phoneFormatRegex.toString());
            emailPattern = Pattern.compile("[\\w\\.]+@[\\w\\.]+\\.[\\w\\.]+");
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        context.setAttribute("phonePattern", phonePattern);
        context.setAttribute("emailPattern", emailPattern);
    }

    /** Destroys the servlet.
     */
    public void destroy() {
        getServletContext().removeAttribute("properties");
        actions = null;
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * The servlet should be called with a path:
     * either /SSvcController/action or /SSvcController/action/args.
     * If present, args will be passed to the class implementing the action as
     * a request attribute.
     */
    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response) throws ServletException, IOException {

        System.out.println("In SSvcController/processRequest before reinitializeActions(), actions=" + actions);
        reinitializeActions();
        System.out.println("In SSvcController/processRequest after reinitializeActions(), actions=" + actions);

        HttpSession session = request.getSession(true);
        String pathInfo = request.getPathInfo();
      /*  if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }

        String referer = request.getHeader("referer");
        System.out.println("Referer:" + referer);
        if ((referer != null)&&(referer.indexOf("coned")<0)) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }*/

        String actionName;
        int delimiterIndex = pathInfo.indexOf("/", 1);
        if (delimiterIndex == -1) {
            actionName = pathInfo.substring(1);
        } else {
            actionName = pathInfo.substring(1, delimiterIndex);
            String args = pathInfo.substring(delimiterIndex + 1);
            if (! args.equals("")) {
                request.setAttribute("args", args);
            }
        }
        SSvcAction action = (SSvcAction) actions.get(actionName.toLowerCase());        
        System.out.println("In SSvcController/processRequest, actionName=" + actionName);
        
    /*    if (action == null) {
        	System.out.println("In SSvcController/processRequest, leky action==null");
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }*/

        // Use the login action if the user is not authenticated
        if (! isAuthenticated(request)
                && ! actionName.equalsIgnoreCase("authenticate")
                /*
                 * 2011-12-28 - CEd9 - JFern - Start Add
                 */
                && ! actionName.equalsIgnoreCase("suauthenticate")
                && ! actionName.equalsIgnoreCase("sulogin")
                /*
                 * 2011-12-28 - CEd9 - JFern - End Add
                 */
        /*
         * 2012-07-25 - CEd10 - SMedin - Start Add
         */
        && ! actionName.equalsIgnoreCase("paymentoptions")

        /*
         * 2012-07-25 - CEd10 - SMedin - End Add
         */
                && ! actionName.equalsIgnoreCase("login")
                && ! actionName.equalsIgnoreCase("registration")
                && ! actionName.equalsIgnoreCase("passwordReminder")
                && ! actionName.equalsIgnoreCase("passwordChange")
                && ! actionName.equalsIgnoreCase("logout")
				
				&& ! actionName.equalsIgnoreCase("intervaldatareports")
				&& ! actionName.equalsIgnoreCase("cmssvcintervaldatatabular")
				&& ! actionName.equalsIgnoreCase("CMSSvcPeakDemandTabular")
				
				
				
				) {
        	
            System.out.println("In SSvcController/processRequest, not yet authenticated");
            action = (SSvcAction) actions.get("login");
            request.setAttribute("unauthorized", "true");
        }
        System.out.println("In SSvcController/processRequest, before action.perform");
    	System.out.println("In SSvcController/processRequest, AuthCode=" + uRLParmValue);
        action.perform(this, request, response);
        System.out.println("In SSvcController/processRequest, after action.perform");        
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                          IOException {
    	System.out.println("Inside SSvcController doGet method");
    	uRLParmValue = request.getParameter("AuthCode");
    	System.out.println("AuthCode=" + uRLParmValue);
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                           IOException {
    	System.out.println("Inside SSvcController doPost method");
    	uRLParmValue = request.getParameter("AuthCode");
    	System.out.println("AuthCode=" + uRLParmValue);
        processRequest(request, response);
    }

    /**
     * Returns true if the session contains the authentication token.
     */
    private boolean isAuthenticated(HttpServletRequest request) {
      /*  boolean isAuthenticated = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("SelfService_validUser") != null) {
            isAuthenticated = true;
        }
        return isAuthenticated;*/
    	return true;
    }
}

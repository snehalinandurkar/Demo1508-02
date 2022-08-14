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
 * This Java Bean will contain public constructors (get and set methods)
 * that will hold different Customer Statement variables. This will also 
 * contain public methods that will be used to invoke Crystal.
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

import java.util.Properties;

/**
 * @author SMedina
 */
public class CMSSvcCustomerStatementBean implements java.io.Serializable {

    private final String ENG_PARM_SEP = "_SPLDLM1_";
    private final String ENG_PARM_EQ = "_SPLDLM2_";
    private final String INT_PARM_SEP = "_SPLDLM3_";
    private final String INT_PARM_EQ = "_SPLDLM4_";

    // ~ Instance fields
    // --------------------------------------------------------------------------------------

    private String userId;
    private String password;
    private String reportServer;
    private String reportDirectory;
    private String reportName;
    private String cdxUser;
    private String languageCode;
    private String reportAction;

    private Properties properties;
    private String accountId;
    private String billingHistoryStartMonth;
    private String billingHistoryEndMonth;
    private String actionURL;
    private String reportEngineParameters;
    private String reportParameters;
    private boolean initialized = false;

	public CMSSvcCustomerStatementBean() {
	}
	
    public CMSSvcCustomerStatementBean(Properties properties) {
        this.properties = properties;
        getReportOptionInformation();
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
	
    public void setBillingHistoryStartMonth(String billingHistoryStartMonth) {
        this.billingHistoryStartMonth = billingHistoryStartMonth;
    }
 
    public void setBillingHistoryEndMonth(String billingHistoryEndMonth) {
        this.billingHistoryEndMonth = billingHistoryEndMonth;
    }

    public boolean initializeCrystalRequestBean() {
        System.out.println ("Inside initializeCrystalRequestBean");
		if (!initialized) {
            reportEngineParameters = createReportEngineParameters();
            reportParameters = createReportParameters();
            actionURL = createUrl();
            initialized = true;
        }

        return initialized;
    }

    /**
     *  Retrieve connection parameters from CMProperties file
     */
    private void getReportOptionInformation() {
        // Get Report Options info
        userId = properties.getProperty("com.splwg.selfservice.REPORTING_TOOL_USER_ID");
        password = properties.getProperty("com.splwg.selfservice.REPORTING_TOOL_PASSWORD");
        reportServer = properties.getProperty("com.splwg.selfservice.REPORTING_SERVER");
        reportDirectory = properties.getProperty("com.splwg.selfservice.REPORTING_FOLDER");
        reportName = properties.getProperty("com.splwg.selfservice.REPORT_CD_CSB");
        cdxUser = properties.getProperty("com.splwg.selfservice.REPORT_CDXUSER");
        languageCode = properties.getProperty("com.splwg.selfservice.REPORT_LANGUAGE");
        reportAction = properties.getProperty("com.splwg.selfservice.REPORT_ACTION");
		System.out.println ("userId: " + userId);
		System.out.println ("password: " + password);
		System.out.println ("reportServer: " + reportServer);
		System.out.println ("reportDirectory: " + reportDirectory);
		System.out.println ("reportName: " + reportName);
		System.out.println ("cdxUser: " + cdxUser);
		System.out.println ("languageCode: " + languageCode);
		System.out.println ("reportAction: " + reportAction);
    }

    private String createUrl() {
        StringBuffer buffer = new StringBuffer(reportServer);
        buffer.append('/');
        buffer.append(reportDirectory);
        buffer.append("/logon.jsp");
        return buffer.toString();
    }

    public String getReportEngineParameters() {
        return reportEngineParameters;
    }

    public String getReportParameters() {
        return reportParameters;
    }

    public String getActionURL() {
        return actionURL;
    }
	
    private String createReportEngineParameters() {
        StringBuffer sb = new StringBuffer("repositoryAction");
        sb.append(ENG_PARM_EQ + reportAction + ENG_PARM_SEP);
        sb.append("cdxUser" + ENG_PARM_EQ + cdxUser + ENG_PARM_SEP);
        sb.append("cisLanguage" + ENG_PARM_EQ + languageCode + ENG_PARM_SEP);
        sb.append("usr" + ENG_PARM_EQ + userId + ENG_PARM_SEP);
        sb.append("pwd" + ENG_PARM_EQ + password + ENG_PARM_SEP);
        sb.append("reportCode" + ENG_PARM_EQ + reportName + ENG_PARM_SEP);
        sb.append("reportSrv" + ENG_PARM_EQ + reportServer + ENG_PARM_SEP);
        sb.append("reportSrvFromSrv" + ENG_PARM_EQ + reportServer + ENG_PARM_SEP);
        sb.append("reportDir" + ENG_PARM_EQ + reportDirectory + ENG_PARM_SEP);
        sb.append("rmsName" + ENG_PARM_EQ + "" + ENG_PARM_SEP);
        sb.append("localFields" + ENG_PARM_EQ);

        appendMessages(sb);
        appendFieldLabels(sb);
        appendProfile(sb);

        return sb.toString();
    }

    private void appendMessages(StringBuffer sb) {
        sb.append("ACTIONINVALID" + INT_PARM_EQ + "Invalid action.  Please review information passed to the reporting engine." + INT_PARM_SEP);
        sb.append("NOTVALIDRESTART" + INT_PARM_EQ + "Your session is no longer valid.  Please restart your connection." + INT_PARM_SEP);
        sb.append("INVALIDPARMTYPE" + INT_PARM_EQ + "Report parameter is invalid.  Error details:" + INT_PARM_SEP);
        sb.append("LOGONNOTVALID" + INT_PARM_EQ + "Enterprise authentication could not log you on.  Please make sure your logon information is correct." + INT_PARM_SEP);
        sb.append("FOLDERNOTFOUND" + INT_PARM_EQ + "Report folder not found.  Please check Reporting Options table." + INT_PARM_SEP);
        sb.append("REPORTNOTFOUND" + INT_PARM_EQ + "Report not found.  Please check external report code." + INT_PARM_SEP);
        sb.append("NOINST" + INT_PARM_EQ + "This report has no historical instances." + INT_PARM_SEP);
    }

    private void appendFieldLabels(StringBuffer sb) {
        sb.append("UNKNOWN" + INT_PARM_EQ + "Unknown" + INT_PARM_SEP);
        sb.append("CANCEL" + INT_PARM_EQ + "Cancel" + INT_PARM_SEP);
        sb.append("FAILED" + INT_PARM_EQ + "Failed" + INT_PARM_SEP);
        sb.append("HISTORY" + INT_PARM_EQ + "History" + INT_PARM_SEP);
        sb.append("FORMAT" + INT_PARM_EQ + "Format" + INT_PARM_SEP);
        sb.append("PARAMETERS" + INT_PARM_EQ + "Parameters" + INT_PARM_SEP);
        sb.append("PAUSED" + INT_PARM_EQ + "Paused" + INT_PARM_SEP);
        sb.append("PENDING" + INT_PARM_EQ + "Pending" + INT_PARM_SEP);
        sb.append("ERRORPAGE" + INT_PARM_EQ + "Error Page" + INT_PARM_SEP);
        sb.append("STATUS" + INT_PARM_EQ + "Status" + INT_PARM_SEP);
        sb.append("RUNNING" + INT_PARM_EQ + "Running" + INT_PARM_SEP);
        sb.append("RECURRING" + INT_PARM_EQ + "Recurring" + INT_PARM_SEP);
        sb.append("EXIT" + INT_PARM_EQ + "Cancel" + INT_PARM_SEP);
        sb.append("SUCCESS" + INT_PARM_EQ + "Success" + INT_PARM_SEP);
        sb.append("GENERAL_ERROR" + INT_PARM_EQ + "An error has occurred:" + INT_PARM_SEP);
        sb.append("RUNBY" + INT_PARM_EQ + "Run By" + INT_PARM_SEP);
        sb.append("REFRESH" + INT_PARM_EQ + "Refresh" + INT_PARM_SEP);
        sb.append("STOPPED" + INT_PARM_EQ + "Stopped" + INT_PARM_SEP);
        sb.append("SCHEDULETIME" + INT_PARM_EQ + "Schedule Time" + INT_PARM_SEP);
    }

    private void appendProfile(StringBuffer sb) {
        sb.append(ENG_PARM_SEP + "dateFormat" + ENG_PARM_EQ + "MM-dd-yyyy");
        sb.append(ENG_PARM_SEP + "timeFormat" + ENG_PARM_EQ + "hh:mma");
    }

    private String createReportParameters() {
        StringBuffer sb = new StringBuffer("parmCount");
        sb.append(ENG_PARM_EQ + "4" + ENG_PARM_SEP);
        sb.append("parms" + ENG_PARM_EQ);
        sb.append("P_ACCT_ID" + INT_PARM_EQ + accountId + INT_PARM_SEP);
        sb.append("P_START_DATE" + INT_PARM_EQ + billingHistoryStartMonth + INT_PARM_SEP);
        sb.append("P_END_DATE" + INT_PARM_EQ + billingHistoryEndMonth + INT_PARM_SEP);
        sb.append("P_REPORT_CD" + INT_PARM_EQ + reportName + INT_PARM_SEP);
        System.out.println ("Report Parameters: " + sb.toString());
        return sb.toString();
    }
}
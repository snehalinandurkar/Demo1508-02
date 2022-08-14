package com.splwg.selfservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletException;

import org.json.JSONObject;

public class CMPeakDemandTabReportBean implements Serializable {

	String accountId;
	SimpleDateFormat fromDate;
	SimpleDateFormat toDate;
	String fileName;
	Properties properties;
	String error;
	Boolean errorFlag;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public SimpleDateFormat getFromDate() {
		return fromDate;
	}

	public void setFromDate(SimpleDateFormat fromDate) {
		this.fromDate = fromDate;
	}

	public SimpleDateFormat getToDate() {
		return toDate;
	}

	public void setToDate(SimpleDateFormat toDate) {
		this.toDate = toDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Boolean getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getReortFile(Properties properties) {

		String reportFilePath = "";
		String iwsString = "CMPkDmdRep";
		String type = "script";
		String jsonInputString = "{\"CMPkDmdRep\":{\"input\":{\"accountId\":\"" + getAccountId() + "\",\"fromDate\":\""
				+ getFromDate() + "\",\"toDate\":\"" + getToDate() + "\"}}}";

		XAIHTTPCall httpCall = new XAIHTTPCall(properties);
		try {
			String httpResponse = httpCall.callRESTServer(type, iwsString, jsonInputString);

			JSONObject result = new JSONObject(httpResponse); // Convert String
																// to json
			// JSONObject output = (JSONObject) result.getJSONObject("output");
			JSONObject resulIws = result.getJSONObject("CMPkDmdRep");
			JSONObject output = resulIws.getJSONObject("output");
			JSONObject errorInfo = output.getJSONObject("errorInformation");
			String error = errorInfo.getString("isInError");
			if (error.equals("false")) {
				setError("");
				setErrorFlag(Boolean.FALSE);
				JSONObject fileName = output.getJSONObject("fullFIlePath");
				setFileName(fileName.toString());
				reportFilePath = fileName.toString();
			} else {
				setError(errorInfo.getString("errorMessage"));
				setErrorFlag(Boolean.TRUE);
				setFileName("");
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}

		return reportFilePath;
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

}

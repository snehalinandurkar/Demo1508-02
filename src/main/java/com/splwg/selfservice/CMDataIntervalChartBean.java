package com.splwg.selfservice;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class CMDataIntervalChartBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String datUnit;
	private String granularity;
	private SimpleDateFormat fromDate;
	private SimpleDateFormat toDate;
	private Properties _Properties;
	private String errorMsg;
	private String accountId;
	
	private boolean demUser;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	private String billPeriod;

	public String getDatUnit() {
		return datUnit;
	}

	public void setDatUnit(String datUnit) {
		this.datUnit = datUnit;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
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

	public String getBillPeriod() {
		return billPeriod;
	}

	public void setBillPeriod(String billPeriod) {
		this.billPeriod = billPeriod;
	}

	 public boolean isDemUser() {
		return demUser;
	}

	public void setDemUser(boolean demUser) {
		this.demUser = demUser;
	}

	void setProperties(Properties value) {
	        _Properties = value;
	        fromDate = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
	        toDate = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
	 }
	 
	 Boolean isDemandUser(String accountId){
		 
		String service = "CMStmDmdInd";
		String serviceType="script";
		String validationJsonString = "{\"CMStmDmdInd\":{\"input\":{\"accountId\":\"" + accountId+ "\"}}}";
		XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
		Boolean demandUserFlag= Boolean.FALSE;
        try {
			String httpResponse = httpCall.callRESTServer(serviceType, service, validationJsonString);
			JSONObject result = new JSONObject(httpResponse); // Convert String to json
			JSONObject output =  result.getJSONObject("output");
			JSONObject errorInfo = output.getJSONObject("errorInformation");
			String error = errorInfo.getString("isInError");
			if(error.equals("false")){
				String isDemand =  output.getString("isDemand");
				if(isDemand.equals("Y")){
					setDemUser(Boolean.TRUE);
					demandUserFlag= Boolean.TRUE;
				}else{
					setDemUser(Boolean.FALSE);
					demandUserFlag= Boolean.FALSE;
				}
				
			}else{
				error=errorInfo.getString("errorMessage");
				//logger
				setDemUser(Boolean.FALSE);
				demandUserFlag= Boolean.FALSE;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Logger
			e.printStackTrace();
		} catch(Exception e){
			// Logger
		}
		return demandUserFlag; 
	 }

	 void fetchIntervalData(CMDataIntervalChartOutputBean outputChartBean ){
		 String service = "CMIntvlData";
		 String serviceType ="script";
		 String jsonString = "{\"CMIntvlData\":{\"input\":{\"accountId\":\"" + getAccountId()
					+ "\",\"granularity\":\"" + getGranularity() + "\",\"fromDate\":\""
					+ getFromDate() + "\",\"toDate\":\"" + getToDate() + "\"}}}";

		 XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
	     try {
				String httpResponse = httpCall.callRESTServer(serviceType, service, jsonString);
				JSONObject result = new JSONObject(httpResponse); // Convert String to json
				JSONObject output =  result.getJSONObject("output");
				JSONObject errorInfo = output.getJSONObject("errorInformation");
				String error = errorInfo.getString("isInError");
				if(error.equals("false")){
					
					JSONArray intervalReads = output.getJSONArray("intervalReads");
					
					ArrayList<CMDataIntervalChartOutputBean> intervalReadsList = new ArrayList<CMDataIntervalChartOutputBean>();
					//Iterate through  intervalReadsList
					for (int i=0; i < intervalReads.length(); i++) {
						
						CMDataIntervalChartOutputBean intervalReadObject= new CMDataIntervalChartOutputBean();
						
						JSONObject intervalReadJsonObject = intervalReads.getJSONObject(i);
						
						intervalReadObject.setDateTime(intervalReadJsonObject.get("dateTime").toString());
						intervalReadObject.setLegalDateTime(intervalReadJsonObject.get("legalDateTime").toString());
						intervalReadObject.setValue(intervalReadJsonObject.get("value").toString()); 
						intervalReadObject.setPercentofTotal(intervalReadJsonObject.get("percentOfTotal").toString());
						intervalReadObject.setPeakDemand(intervalReadJsonObject.get("peakDemand").toString());
						intervalReadObject.setIsAllTimePeak(intervalReadJsonObject.get("isAllTimePeak").toString());
						intervalReadObject.setIsOneTimePeak(intervalReadJsonObject.get("isOnTimePeak").toString());
						intervalReadObject.setIsPeriodPeak(intervalReadJsonObject.get("isPeriodPeak").toString());
						
						intervalReadsList.add(intervalReadObject);
					}
					
					JSONArray dailyValues = output.getJSONArray("dailyValues");
					ArrayList<CMDataIntervalChartOutputBean> dailyValuesList = new ArrayList<CMDataIntervalChartOutputBean>();

					for (int i=0; i < dailyValues.length(); i++) {
						
						CMDataIntervalChartOutputBean dailyValuesObject= new CMDataIntervalChartOutputBean();
						
						JSONObject dailyValuesJsonObject = intervalReads.getJSONObject(i);
						
						dailyValuesObject.setDateTime(dailyValuesJsonObject.get("date").toString());
						dailyValuesObject.setLegalDateTime(dailyValuesJsonObject.get("min").toString());
						dailyValuesObject.setValue(dailyValuesJsonObject.get("max").toString()); 
						dailyValuesObject.setPercentofTotal(dailyValuesJsonObject.get("avg").toString());
						dailyValuesObject.setPeakDemand(dailyValuesJsonObject.get("total").toString());
						dailyValuesList.add(dailyValuesObject);
					}
					//CMDataIntervalChartOutputBean outputBean= new CMDataIntervalChartOutputBean();
					outputChartBean.setIntervalReads(intervalReadsList);
					outputChartBean.setDailyValues(dailyValuesList);
				}else{
					error=errorInfo.getString("errorMessage");
					//logger
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//Logger
				e.printStackTrace();
			} catch(Exception e){
				// Logger
			}
	 }
}

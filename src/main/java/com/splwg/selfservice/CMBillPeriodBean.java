package com.splwg.selfservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class CMBillPeriodBean {

	ArrayList<CMBillPeriodBean> billPeriodList = new ArrayList<CMBillPeriodBean>();
	String billPeriodStartDate;
	String billPeriodEndDate;
	String period;
	private TreeMap billPeriod;
	private Properties properties;
	private String errorMsg;
	private String accountId;

	public CMBillPeriodBean() {
	}

	public CMBillPeriodBean(Properties value, String acId) {
		properties = value;
		accountId= acId;
	}

	public String getErrorMessage() {
		return Util.decode(errorMsg);
	}
	
	 public TreeMap getBillPeriod() {
	        retrieveBillPeriods();
	        return billPeriod;
	    }

	 
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ArrayList<CMBillPeriodBean> getBillPeriodList() {
		return billPeriodList;
	}

	public void setBillPeriodList(ArrayList<CMBillPeriodBean> billPeriodList) {
		this.billPeriodList = billPeriodList;
	}

	public String getBillPeriodStartDate() {
		return billPeriodStartDate;
	}

	public void setBillPeriodStartDate(String billPeriodStartDate) {
		this.billPeriodStartDate = billPeriodStartDate;
	}

	public String getBillPeriodEndDate() {
		return billPeriodEndDate;
	}

	public void setBillPeriodEndDate(String billPeriodEndDate) {
		this.billPeriodEndDate = billPeriodEndDate;
	}

	

	public CMBillPeriodBean(String billPeriodStartDate, String billPeriodEndDate) {
		super();
		this.billPeriodStartDate = billPeriodStartDate;
		this.billPeriodEndDate = billPeriodEndDate;
	}
	
	public boolean retrieveBillPeriods(){
		billPeriod = new TreeMap();
		boolean retVal=false;
		String iwsString = "CMStmBillPd";
		String type = "script";
		String jsonInputString = "{\"CMStmBillPd\":{\"input\":{\"accountId\":\"" + accountId + "\"}}}";

		XAIHTTPCall httpCall = new XAIHTTPCall(properties);
		try {
			String httpResponse = httpCall.callRESTServer(type, iwsString, jsonInputString);

			JSONObject result = new JSONObject(httpResponse); // Convert String
																// to json
			JSONObject iwsResponse = (JSONObject) result.getJSONObject("CMStmBillPd");
			
			JSONObject output = (JSONObject) iwsResponse.getJSONObject("output");
					
			JSONObject errorInfo = output.getJSONObject("errorInformation");
			String isError= errorInfo.get("isInError").toString();
			if(isError.toString().equals("false")){
				
				Object billJsonObject = output.get("bills");
				
				if(billJsonObject instanceof JSONArray){
					
					JSONArray billJsonArray =output.getJSONArray("bills");
					for (int i = 0; i < billJsonArray.length(); i++) {
						JSONObject obj = (JSONObject) billJsonArray.get(i);
						//CMBillPeriodBean cmBillPeriodBean = new CMBillPeriodBean();
						//cmBillPeriodBean
						//		.setBillPeriodStartDate(new String(obj.get("billPeriodStartDate").toString()));
						//cmBillPeriodBean.setBillPeriodEndDate(new String(obj.get("billPeriodEndDate").toString()));
						//billPeriodList.add(cmBillPeriodBean);
						billPeriod.put(obj.get("billPeriodStartDate").toString(),obj.get("billPeriodEndDate").toString());
						
					}
				}else{
					JSONObject billJson = (JSONObject)billJsonObject;
					billPeriod.put(billJson.get("billPeriodStartDate").toString(),billJson.get("billPeriodEndDate").toString());

				}
				
				//JSONArray billJsonArray = output.getJSONArray("bills");

				
				retVal= true;
				return retVal;
			}else{
				errorMsg=errorInfo.getJSONObject("errorMessage").toString();
				
				retVal=false;
				return retVal;
			}
			
		} catch (Exception e) {
			 errorMsg = "Caught exception: " + e.getMessage();
			 retVal=false;
			 return retVal;
		}
		
	}
	

	ArrayList<CMBillPeriodBean> fetchBillPeriodData(String accountId, Properties _Properties) {
		String iwsString = "CMStmBillPd";
		String type = "script";
		String jsonInputString = "{\"CMStmBillPd\":{\"input\":{\"accountId\":\"" + accountId + "\"}}}";

		XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
		try {
			String httpResponse = httpCall.callRESTServer(type, iwsString, jsonInputString);

			JSONObject result = new JSONObject(httpResponse); // Convert String
																// to json
			JSONObject iwsResponse = (JSONObject) result.getJSONObject("CMStmBillPd");
			JSONObject output = (JSONObject) iwsResponse.getJSONObject("output");
			Object billJsonObject = output.get("bills");
			
			if(billJsonObject instanceof JSONArray){
				
				JSONArray billJsonArray =output.getJSONArray("bills");
				for (int i = 0; i < billJsonArray.length(); i++) {
					JSONObject obj = (JSONObject) billJsonArray.get(i);

				CMBillPeriodBean cmBillPeriodBean = new CMBillPeriodBean();
				cmBillPeriodBean
						.setBillPeriodStartDate(obj.get("billPeriodStartDate").toString());
				cmBillPeriodBean.setBillPeriodEndDate(obj.get("billPeriodEndDate").toString());
				billPeriodList.add(cmBillPeriodBean);
			} 
			}else{
				JSONObject billJson = (JSONObject)billJsonObject;
				CMBillPeriodBean cmBillPeriodBean = new CMBillPeriodBean();
				cmBillPeriodBean
						.setBillPeriodStartDate(billJson.get("billPeriodStartDate").toString());
				cmBillPeriodBean.setBillPeriodEndDate(billJson.get("billPeriodEndDate").toString());
				billPeriodList.add(cmBillPeriodBean);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}
		return billPeriodList;

	}

}

package com.splwg.selfservice;

import java.util.ArrayList;
import java.util.List;

public class CMDataIntervalChartOutputBean {
	
	
	List<CMDataIntervalChartOutputBean> intervalReads = new ArrayList<CMDataIntervalChartOutputBean>();
	List<CMDataIntervalChartOutputBean> dailyValues = new ArrayList<CMDataIntervalChartOutputBean>();
	private String date;
	private String dateTime;
	private String legalDateTime;
	private String peakDemand;
	private String isAllTimePeak;
	private String isOneTimePeak;
	private String isPeriodPeak;

	private String value;
	private String percentofTotal;
	
	private String monthYear;
	private String min;
	private String max;
	private String avg;
	private String total;
	

	CMDataIntervalChartOutputBean(){
		//default COnstructor
	}
	
	
	
	public List<CMDataIntervalChartOutputBean> getIntervalReads() {
		return intervalReads;
	}



	public void setIntervalReads(List<CMDataIntervalChartOutputBean> intervalReads) {
		this.intervalReads = intervalReads;
	}



	public List<CMDataIntervalChartOutputBean> gwtDailyValues() {
		return dailyValues;
	}



	public void setDailyValues(List<CMDataIntervalChartOutputBean> dailyValues) {
		this.dailyValues = dailyValues;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public String getPercentofTotal() {
		return percentofTotal;
	}



	public void setPercentofTotal(String percentofTotal) {
		this.percentofTotal = percentofTotal;
	}



	public String getMonthYear() {
		return monthYear;
	}



	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}



	public String getMin() {
		return min;
	}



	public void setMin(String min) {
		this.min = min;
	}



	public String getMax() {
		return max;
	}



	public void setMax(String max) {
		this.max = max;
	}



	public String getAvg() {
		return avg;
	}



	public void setAvg(String avg) {
		this.avg = avg;
	}



	public String getTotal() {
		return total;
	}



	public void setTotal(String total) {
		this.total = total;
	}

	
	public String getDateTime() {
		return dateTime;
	}



	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}



	public String getLegalDateTime() {
		return legalDateTime;
	}



	public void setLegalDateTime(String legalDateTime) {
		this.legalDateTime = legalDateTime;
	}



	public String getPeakDemand() {
		return peakDemand;
	}



	public void setPeakDemand(String peakDemand) {
		this.peakDemand = peakDemand;
	}



	public String getIsAllTimePeak() {
		return isAllTimePeak;
	}



	public void setIsAllTimePeak(String isAllTimePeak) {
		this.isAllTimePeak = isAllTimePeak;
	}



	public String getIsOneTimePeak() {
		return isOneTimePeak;
	}



	public void setIsOneTimePeak(String isOneTimePeak) {
		this.isOneTimePeak = isOneTimePeak;
	}



	public String getIsPeriodPeak() {
		return isPeriodPeak;
	}



	public void setIsPeriodPeak(String isPeriodPeak) {
		this.isPeriodPeak = isPeriodPeak;
	}






}

/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class MonthlyAllocationBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String subscriberName;
    private String status;
    private String percent;
    private String serviceClass;
    private String valueStackCredit;
    private String energyCredit;
    private String capacityCredit;
    private String environmentalCredit;
    private String dsrvCredit;
    private String lsrvCredit;
    private String mtcCredit;
    private String communityCredit;
    private String kwh;
    private String fromDate;
    private String toDate;
    private String conedBill;
    private String escoCharges;
    private String billedAmount;
    private String billedKwh;
    private String creditAppliedAmount;
    private String creditAppliedDate;
    private String previousBalance;
    private String monthlyBankContribution;
    private String bankWithdrawal;
    private String bankedBalance;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    void setSubscriberName(String value) {
        subscriberName = value;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String value) {
        status = value;
    }

    public String getPercent() {
        return percent;
    } 

    void setPercent(String value) {
        percent = value;
    }

    public String getServiceClass() {
        return serviceClass;
    } 

    void setServiceClass(String value) {
        serviceClass = value;
    }

    public String getValueStackCredit() {
        return valueStackCredit;
    } 

    void setValueStackCredit(String value) {
        valueStackCredit = value;
    }

    public String getEnergyCredit() {
        return energyCredit;
    } 

    void setEnergyCredit(String value) {
        energyCredit = value;
    }

    public String getCapacityCredit() {
        return capacityCredit;
    } 

    void setCapacityCredit(String value) {
        capacityCredit = value;
    }

    public String getEnvironmentalCredit() {
        return environmentalCredit;
    } 

    void setEnvironmentalCredit(String value) {
        environmentalCredit = value;
    }

    public String getDsrvCredit() {
        return dsrvCredit;
    } 

    void setDsrvCredit(String value) {
        dsrvCredit = value;
    }

    public String getLsrvCredit() {
        return lsrvCredit;
    } 

    void setLsrvCredit(String value) {
        lsrvCredit = value;
    }

    public String getMtcCredit() {
        return mtcCredit;
    } 

    void setMtcCredit(String value) {
        mtcCredit = value;
    }

    public String getCommunityCredit() {
        return communityCredit;
    } 

    void setCommunityCredit(String value) {
        communityCredit = value;
    }

    public String getKwh() {
        return kwh;
    } 

    void setKwh(String value) {
        kwh = value;
    }

    public String getFromDate() {
        return fromDate;
    } 

    void setFromDate(String value) {
        fromDate = value;
    }

    public String getToDate() {
        return toDate;
    } 

    void setToDate(String value) {
        toDate = value;
    }

    public String getConedBill() {
        return conedBill;
    } 

    void setConedBill(String value) {
        conedBill = value;
    }

    public String getEscoCharges() {
        return escoCharges;
    } 

    void setEscoCharges(String value) {
        escoCharges = value;
    }

    public String getBilledAmount() {
        return billedAmount;
    } 

    void setBilledAmount(String value) {
        billedAmount = value;
    }

    public String getBilledKwh() {
        return billedKwh;
    } 

    void setBilledKwh(String value) {
        billedKwh = value;
    }

    public String getCreditAppliedAmount() {
        return creditAppliedAmount;
    }

    void setCreditAppliedAmount(String value) {
        creditAppliedAmount = value;
    }

    public String getCreditAppliedDate() {
        return creditAppliedDate;
    }

    void setCreditAppliedDate(String value) {
        creditAppliedDate = value;
    }

    public String getPreviousBalance() {
        return previousBalance;
    }

    void setPreviousBalance(String value) {
        previousBalance = value;
    }

    public String getMonthlyBankContribution() {
        return monthlyBankContribution;
    }

    void setMonthlyBankContribution(String value) {
        monthlyBankContribution = value;
    }

    public String getBankWithdrawal() {
        return bankWithdrawal;
    }

    void setBankWithdrawal(String value) {
        bankWithdrawal = value;
    }

    public String getBankedBalance() {
        return bankedBalance;
    }

    void setBankedBalance(String value) {
        bankedBalance = value;
    }
}

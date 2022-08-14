/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class AllocationResultBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String resultId;
    private String period;
    private String statementDate;
    private String creditAmount;
    private String balance;
    private String subscriberPlanId;
    private String hostAllocationId;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getResultId() {
        return resultId;
    }

    void setResultId(String value) {
        resultId = value;
    }

    public String getPeriod() {
        return period;
    }

    void setPeriod(String value) {
        period = value;
    }

    public String getStatementDate() {
        return statementDate;
    }

    void setStatementDate(String value) {
        statementDate = value;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    void setCreditAmount(String value) {
        creditAmount = value;
    }

    public String getBalance() {
        return balance;
    }

    void setBalance(String value) {
        balance = value;
    }

    public String getSubscriberPlanId() {
        return subscriberPlanId;
    }

    void setSubscriberPlanId(String value) {
        subscriberPlanId = value;
    }

    public String getHostAllocationId() {
        return hostAllocationId;
    }

    void setHostAllocationId(String value) {
        hostAllocationId = value;
    }

}

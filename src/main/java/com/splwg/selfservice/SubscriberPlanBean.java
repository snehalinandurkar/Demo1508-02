/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class SubscriberPlanBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String planId;
    private String planType;
    private String createDate;
    private String effectiveDate;
    private String status;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getPlanId() {
        return planId;
    }

    void setPlanId(String value) {
        planId = value;
    }

    public String getPlanType() {
        return planType;
    }

    void setPlanType(String value) {
        planType = value;
    }

    public String getCreateDate() {
        return createDate;
    }

    void setCreateDate(String value) {
        createDate = value;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    void setEffectiveDate(String value) {
        effectiveDate = value;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String value) {
        status = value;
    }
}

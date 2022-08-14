/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class HostAllocationBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String cssAcctId;
    private String allocationId;
    private String createDate;
    private String effectiveDate;
    private String allocationTypeDescription;
    private String status;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getcssAcctId() {
        return cssAcctId;
    }

    void setcssAcctId(String value) {
        cssAcctId = value;
    }

  public String getAllocationId() {
        return allocationId;
    }

    void setAllocationId(String value) {
        allocationId = value;
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

    public String getAllocationTypeDescription() {
        return Util.decode(allocationTypeDescription);
    }

    void setAllocationTypeDescription(String value) {
        allocationTypeDescription = value;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String value) {
        status = value;
    }
}

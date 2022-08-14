/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class SubscriberAllocationBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String name;
    private String address;
    private String percent;
    private String status;
    private String endDate;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getName() {
        return name;
    }

    void setName(String value) {
        name = value;
    }

    public String getEndDate() {
        return endDate;
    }

    void setEndDate(String value) {
        endDate = value;
    }

    public String getPercent() {
        return percent;
    }

    void setPercent(String value) {
        percent = value;
    }

    public String getAddress() {
        return Util.decode(address);
    }

    void setAddress(String value) {
        address = value;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String value) {
        status = value;
    }
}

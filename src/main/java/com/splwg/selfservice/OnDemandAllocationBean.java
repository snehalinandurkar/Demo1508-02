/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class OnDemandAllocationBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String subscriberName;
    private String percent;
    private String credit;

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

    public String getPercent() {
        return percent;
    }

    void setPercent(String value) {
        percent = value;
    }

    public String getCredit() {
        return credit;
    }

    void setCredit(String value) {
        credit = value;
    }
}

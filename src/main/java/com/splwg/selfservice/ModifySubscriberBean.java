/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class ModifySubscriberBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String cssAcctId;
    private String percent;
    private String sequence;

    //~ Methods ----------------------------------------------------------------------------------------------


    public String getCssAcctId() {
        return cssAcctId;
    }

    void setCssAcctId(String value) {
        cssAcctId = value;
    }

    public String getPercent() {
        return percent;
    }

    void setPercent(String value) {
        percent = value;
    }

    public String getSequence() {
        return sequence;
    }

    void setSequence(String value) {
        sequence = value;
    }
}

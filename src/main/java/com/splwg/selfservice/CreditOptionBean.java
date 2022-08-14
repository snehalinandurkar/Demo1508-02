/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class CreditOptionBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String description; 
    private String effectiveDate;
    private String value;

    //~ Methods ----------------------------------------------------------------------------------------------


    public String getDescription() {
        return description;
    }

    void setDescription(String value) {
        description = value;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    void setEffectiveDate(String value) {
        effectiveDate = value;
    }

    public String getValue() {
        return value;
    }

    void setValue(String _value) {
        value = _value;
    }
}

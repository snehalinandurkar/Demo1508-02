/*
 * HostAllocationBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class AllocationPlanTypeBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String subAllocTypeCd;
    private String subAllocTypeDesc;
    private String pctOrListBasedFlag;


    //~ Methods ----------------------------------------------------------------------------------------------

    public String getSubAllocTypeCd() {
        return subAllocTypeCd;
    }

    void setSubAllocTypeCd(String value) {
        subAllocTypeCd = value;
    }

    public String getSubAllocTypeDesc() {
        return subAllocTypeDesc;
    }

    void setSubAllocTypeDesc(String value) {
        subAllocTypeDesc = value;
    }

    public String getPctOrListBaseFlag() {
        return pctOrListBasedFlag;
    }

    void setPctOrListBasedFlag(String value) {
        pctOrListBasedFlag = value;
    }

}

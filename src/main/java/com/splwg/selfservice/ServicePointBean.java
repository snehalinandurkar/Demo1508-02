/*
 * ServicePointBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Holds service point data
 *
 */

package com.splwg.selfservice;

public class ServicePointBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String spId;
    private String saId;
    private String spInfo;
    private String badgeNbr;
    private String selected;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getSelected() {
        return Util.decode(selected);
    }

    void setSelected(String value) {
        selected = value;
    }

    public String getSPId() {
        return spId;
    }

    void setSPId(String value) {
        spId = value;
    }

    public String getSAId() {
        return saId;
    }

    void setSAId(String value) {
        saId = value;
    }

    public String getBadgeNbr() {
        return Util.decode(badgeNbr);
    }

    void setBadgeNbr(String value) {
        badgeNbr = value;
    }

    void setStartDate(String value) {
        spId = value;
    }

    public String getSPInfo() {
        return Util.decode(spInfo);
    }

    void setSPInfo(String value) {
        spInfo = value;
    }
}

/*
 * ServiceAgreementBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Holds SA data
 *
 */

package com.splwg.selfservice;

public class ServiceAgreementBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String startDate;
    private String activeStatus;
    private String endDate;
    private String serviceDesc;
    private String saType;
    private String saBalance;
    private String address;
    private String saId;
    private String status;
    private String disconnectMessage;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getStartDate() {
        return startDate;
    }

    void setStartDate(String value) {
        startDate = value;
    }

    public String getEndDate() {
        return endDate;
    }

    void setEndDate(String value) {
        endDate = value;
    }

    public String getActiveStatus() {
        return Util.decode(activeStatus);
    }

    void setActiveStatus(String value) {
        activeStatus = value;
    }

    public String getDisconnectMessage() {
        return Util.decode(disconnectMessage);
    }

    void setDisconnectMessage(String value) {
        disconnectMessage = value;
    }

    public String getStatus() {
        return Util.decode(status);
    }

    void setStatus(String value) {
        status = value;
    }

    public String getServiceDesc() {
        return Util.decode(serviceDesc);
    }

    void setServiceDesc(String value) {
        serviceDesc = value;
    }

    public String getSAType() {
        return Util.decode(saType);
    }

    void setSAType(String value) {
        saType = value;
    }

    public String getSABalance() {
        return saBalance;
    }

    void setSABalance(String value) {
        saBalance = value;
    }

    public String getAddress() {
        return Util.decode(address);
    }

    void setAddress(String value) {
        address = value;
    }

    public String getSAId() {
        return saId;
    }

    void setSAId(String value) {
        saId = value;
    }
}

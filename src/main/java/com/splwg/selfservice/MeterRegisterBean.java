/*
 * MeterRegisterBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Holds meter register data
 *
 */

package com.splwg.selfservice;

public class MeterRegisterBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String registerId;
    private String meterConfigurationId;
    private String registerReadId;
    private String registerRead;
    private String readDateTime;
    private String readSequence;
    private String UOM;
    private String TOU;
    private String lowLimit;
    private String highLimit;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getRegisterId() {
        return registerId;
    }

    void setRegisterId(String value) {
        registerId = value;
    }

    public String getMeterConfigurationId() {
        return meterConfigurationId;
    }

    void setMeterConfigurationId(String value) {
        meterConfigurationId = value;
    }

    public String getReadSequence() {
        return readSequence;
    }

    void setReadSequence(String value) {
        readSequence = value;
    }

    public String getUOM() {
        return Util.decode(UOM);
    }

    void setUOM(String value) {
        UOM = value;
    }

    public String getTOU() {
        return Util.decode(TOU);
    }

    void setTOU(String value) {
        TOU = value;
    }

    public String getReadDateTime() {
        return readDateTime;
    }

    void setReadDateTime(String value) {
        readDateTime = value;
    }

    public String getRegisterReadId() {
        return registerReadId;
    }

    void setRegisterReadId(String value) {
        registerReadId = value;
    }

    public String getRegisterRead() {
        return Util.decode(registerRead);
    }

    void setRegisterRead(String value) {
        registerRead = value;
    }

    public String getLowLimit() {
        return lowLimit;
    }

    void setLowLimit(String value) {
        lowLimit = value;
    }

    public String getHighLimit() {
        return highLimit;
    }

    void setHighLimit(String value) {
        highLimit = value;
    }
}

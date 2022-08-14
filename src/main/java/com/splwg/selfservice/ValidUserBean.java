/*
 * Valid User Bean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds user information used throughout the application
 *
 */

package com.splwg.selfservice;

public class ValidUserBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String allocationId;
    private String personId;
    private String password;
    private String userId;
    private String entityName;
    private String currencyPosition;
    private String currencySymbol;
    private String currencyCode;
    private String homePhoneLabel;
    private String businessPhoneLabel;

    //~ Constructors -----------------------------------------------------------------------------------------

    public ValidUserBean() {}

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getAllocationId() {
        return allocationId;
    }

    void setAllocationId(String value) {
        allocationId = value;
    }

    public String getPersonId() {
        return personId;
    }

    void setPersonId(String value) {
        personId = value;
    }

    void setUserId(String value) {
        userId = value;
    }

    public String getUserId() {
        return Util.decode(userId);
    }

    public String getEntityName() {
        return Util.decode(entityName);
    }

    void setEntityName(String value) {
        entityName = value;
    }

    public String getHomePhoneLabel() {
        return Util.decode(homePhoneLabel);
    }

    void setHomePhoneLabel(String value) {
        homePhoneLabel = value;
    }

    public String getBusinessPhoneLabel() {
        return Util.decode(businessPhoneLabel);
    }

    void setBusinessPhoneLabel(String value) {
        businessPhoneLabel = value;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String value) {
        password = value;
    }

    public String getCurrencySymbol() {
        return Util.decode(currencySymbol);
    }

    void setCurrencySymbol(String value) {
        currencySymbol = value;
    }

    public String getCurrencyPosition() {
        return Util.decode(currencyPosition);
    }

    void setCurrencyPosition(String value) {
        currencyPosition = value;
    }

    public String getCurrencyCode() {
        return Util.decode(currencyCode);
    }

    void setCurrencyCode(String value) {
        currencyCode = value;
    }
}

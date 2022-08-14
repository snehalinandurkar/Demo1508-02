/*
 * AccountPersonBean.java
 *
 * Created in January 2003 by A. Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used for the account being returned from the AccountsForPerson service
 */

package com.splwg.selfservice;

/**
 *
 *
 */
public class AccountPersonBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String accountInfo;
    private String currentBalance;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getAccountInfo() {
        return Util.decode(accountInfo);
    }

    void setAccountInfo(String value) {
        accountInfo = value;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    void setCurrentBalance(String value) {
        currentBalance = value;
    }

    public boolean isVDER() {
        return (accountInfo != null && accountInfo.indexOf("Value of DER")>=0);
    }
}

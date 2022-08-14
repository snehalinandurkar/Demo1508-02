/*
 * FinancialTransactionBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds financial transaction data
 *
 */

package com.splwg.selfservice;

public class FinancialTransactionBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private String arrearsDate;
    private String transactionTypeDescription;
    private String currentAmount;
    private String currentBalance;

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
    }

    public String getArrearsDate() {
        return arrearsDate;
    }

    void setArrearsDate(String value) {
        arrearsDate = value;
    }

    public String getTransactionTypeDescription() {
        return Util.decode(transactionTypeDescription);
    }

    void setTransactionTypeDescription(String value) {
        transactionTypeDescription = value;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    void setCurrentAmount(String value) {
        currentAmount = value;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    void setCurrentBalance(String value) {
        currentBalance = value;
    }
}

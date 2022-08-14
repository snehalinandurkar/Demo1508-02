/*
 * NewPaymentBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Creates a payment
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class NewPaymentBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    /**
     */
    private String _AccountId;
    private String personId;
    private String _PaymentAmount;
    private String paymentAmountOriginal;
    private String _CreditCardNumber;
    private String _ExpMonth;
    private String _ExpYear;
    private String _CreditCardName;
    private String _CardType;
    private Calendar _PaymentDate;
    private Properties _Properties;
    private SimpleDateFormat _SDF;
    private String _PaymentEventId;
    private String _AccountBalanceAfterPayment;
    private String errorMsg;
    private boolean _IsBusy = false;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;

//    private String currencyCode;
    public String formattedPaymentAmount;
    private HttpSession session;
    private Calendar expDate;
    private boolean hyphenExists = true;
    private boolean blankExists = true;
    private String currencyPosition;
    private String currencySymbol;
    private String currencyCode;

    //~ Constructors -----------------------------------------------------------------------------------------

    public NewPaymentBean() {}

    /** Creates new NewPaymentBean with properties */
    public NewPaymentBean(Properties properties) {
        setProperties(properties);
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getCurrencyPosition() {
        return Util.decode(currencyPosition);
    }

    void setCurrencyPosition(String value) {
        currencyPosition = value;
    }

    public String getCurrencySymbol() {
        return Util.decode(currencySymbol);
    }

    void setCurrencySymbol(String value) {
        currencySymbol = value;
    }

    public String getCurrencyCode() {
        return Util.decode(currencyCode);
    }

    void setCurrencyCode(String value) {
        currencyCode = value;
    }

    public String getAccountId() {
        return _AccountId;
    }

    public void setAccountId(String value) {
        _AccountId = value;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String value) {
        personId = value;
    }

    public String getPaymentAmount() {
        return _PaymentAmount;
    }

    public void setPaymentAmount(String value) {
        paymentAmountOriginal = value;
        try {
            Number payment = numOutput.parse(value);
            _PaymentAmount = numInput.format(payment);
            formattedPaymentAmount = numOutput.format(payment);
        } catch (Exception e) {
            _PaymentAmount = "";
        }
    }

    public String getCreditCardNumber() {
        return Util.decode(_CreditCardNumber);
    }

    public void setCreditCardNumber(String value) {
        _CreditCardNumber = value;
    }

    public String getCreditCardNumberDigits() {
        // Scan the credit card number string and throw away any characters that
        // are not digits [0-9].
        StringBuffer creditCardNumberDigits = new StringBuffer();
        for (int i = 0; i <= _CreditCardNumber.length() - 1; i++) {
            char c = _CreditCardNumber.charAt(i);
            if (Character.isDigit(c)) {
                creditCardNumberDigits.append(c);
            }
        }
        return creditCardNumberDigits.toString();
    }

    public String getExpMonth() {
        return _ExpMonth;
    }

    public void setExpMonth(String value) {
        _ExpMonth = value;
    }

    public String getExpYear() {
        return _ExpYear;
    }

    public void setExpYear(String value) {
        _ExpYear = value;
    }

    public String getCreditCardName() {
        return Util.decode(_CreditCardName);
    }

    public void setCreditCardName(String value) {
        _CreditCardName = value;
    }

    public String getCardType() {
        return Util.decode(_CardType);
    }

    public void setCardType(String value) {
        _CardType = value;
    }

    public String getPaymentDateFormatted() {
        return _SDF.format(_PaymentDate.getTime());
    }

    public void setPaymentDate() {
        _PaymentDate = Calendar.getInstance();
    }

    public void setProperties(Properties value) {
        _Properties = value;
        _SDF = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        numInput = new DecimalFormat(_Properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(_Properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
    }

    public String getPaymentEventId() {
        return _PaymentEventId;
    }

    public String getAccountBalanceAfterPayment() {
        return formatCurrencyValue(_AccountBalanceAfterPayment);
    }

    private String formatCurrencyValue(String value) {
        if (currencyPosition.equals("PR")) return currencySymbol + value;
        else return value + currencySymbol;
    }

    public String getPaymentAmountFormatted() {
        if (_PaymentAmount == null) {
            return "";
        }
        try {
            Number amt = numInput.parse(_PaymentAmount);

            String bdAmount = numOutput.format(amt);
            return formatCurrencyValue(bdAmount);
        } catch (Exception e) {
            return "0";
        }
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public boolean isBusy() {
        return _IsBusy;
    }

    public boolean getBusy() {
        return isBusy();
    }

    public void setBusy(boolean value) {
        _IsBusy = value;
    }

    private String deleteHyphen(String input) {
        String returnString = "";
        int i = input.indexOf("-");
        if (i == 0) {
            returnString = input.substring(1);
        } else if (i > 0) {
            returnString = input.substring(0, i).concat(input.substring(i + 1));
        } else if (i < 0) {
            hyphenExists = false;
            return input;
        }
        return returnString;
    }

    private String deleteBlank(String input) {
        String returnString = "";
        int i = input.indexOf(" ");
        if (i > 0) {
            returnString = input.substring(0, i).concat(input.substring(i + 1));
        } else {
            blankExists = false;
            return input;
        }
        return returnString;
    }

    /** This method validates the bean.
     */
    public boolean isPaymentEntryFormValid(HttpServletRequest request) {
        session = request.getSession();

        // Validate Payment Amount
        if (_PaymentAmount == null || _PaymentAmount.equals("")) {
            errorMsg = getMessage(11201);
            return false;
        }
        BigDecimal bdAmount;

        try {
            bdAmount = new BigDecimal(_PaymentAmount);
            bdAmount.setScale(2);
        } catch (NumberFormatException nfe) {
            errorMsg = getMessage(11202);
            return false;
        } catch (ArithmeticException ae) {
            errorMsg = getMessage(11203);
            return false;
        }

        double dAmount = bdAmount.doubleValue();
        if (dAmount <= 0) {
            errorMsg = getMessage(11204);
            return false;
        }
        if (dAmount >= 1E6) {
            errorMsg = getMessage(11205);
            return false;
        }

        // Validate Credit Card Number
        if (_CreditCardNumber == null || _CreditCardNumber.equals("")) {
            errorMsg = getMessage(11206);
            return false;
        }

        // If Credit Card Number contains any characters other than digits,
        // spaces (' ') and dashes ('-'), it is not valid. The number of digits
        // should be between 13 and 16.
        int digitCount = 0;
        for (int i = 0; i <= _CreditCardNumber.length() - 1; i++) {
            char c = _CreditCardNumber.charAt(i);
            if (Character.isDigit(c)) {
                digitCount++;
            } else {
                if (c != ' ' && c != '-') {
                    errorMsg = getMessage(11207);
                    return false;
                }
            }
        }

        _CreditCardNumber = _CreditCardNumber.trim();

        // Delete any dashes
        while (hyphenExists == true) {
            _CreditCardNumber = deleteHyphen(_CreditCardNumber);
        }

        // Delete any blanks
        while (blankExists == true) {
            _CreditCardNumber = deleteBlank(_CreditCardNumber);
        }

        if (digitCount < 13 || digitCount > 16) {
            errorMsg = getMessage(11207);
            return false;
        }

        // Check that the expiration date has been selected.
        if (_ExpMonth == null || _ExpMonth.equals("") || _ExpYear == null || _ExpYear.equals("")
                || _ExpMonth.equals("XX") || _ExpYear.equals("XXXX")) {
            errorMsg = getMessage(11208);
            return false;
        }

        String validate = _Properties.getProperty("com.splwg.selfservice.CreditCardValidation");
        if (validate != null) {
            if (validate.equals("true")) {
                String className = _Properties.getProperty("com.splwg.selfservice.CreditCardValidationClassName");
                try {
                    ICreditCardValidation cc = (ICreditCardValidation) Class.forName(className).newInstance();
                    cc.setAmount(_PaymentAmount);
                    cc.setCreditCardNumber(_CreditCardNumber);
                    cc.setExpiryMonth(_ExpMonth);
                    cc.setExpiryYear(_ExpYear);
                    cc.setNameOnCard(_CreditCardName);
                    cc.setCreditCardCompany(_CardType);
                    if (cc.validateCard() == false) {
                        errorMsg = getMessage(11207);
                        return false;
                    }
                } catch (Exception e) {
                    errorMsg = e.getMessage();
                    return false;
                }
            }
        }
        int intExpMonth;
        int intExpYear;
        try {
            intExpMonth = Integer.parseInt(_ExpMonth);
            intExpYear = Integer.parseInt(_ExpYear);
        } catch (NumberFormatException nfe) {
            errorMsg = getMessage(11209);
            return false;
        }

        // Check that the expiration date is in the future.
        setPaymentDate();
        expDate = Calendar.getInstance();
        expDate.setLenient(false);

        expDate.clear(); // clear the time fields
        switch (intExpMonth) {
            case 1:
                expDate.set(intExpYear, Calendar.FEBRUARY, 1);
                break;

            case 2:
                expDate.set(intExpYear, Calendar.MARCH, 1);
                break;

            case 3:
                expDate.set(intExpYear, Calendar.APRIL, 1);
                break;

            case 4:
                expDate.set(intExpYear, Calendar.MAY, 1);
                break;

            case 5:
                expDate.set(intExpYear, Calendar.JUNE, 1);
                break;

            case 6:
                expDate.set(intExpYear, Calendar.JULY, 1);
                break;

            case 7:
                expDate.set(intExpYear, Calendar.AUGUST, 1);
                break;

            case 8:
                expDate.set(intExpYear, Calendar.SEPTEMBER, 1);
                break;

            case 9:
                expDate.set(intExpYear, Calendar.OCTOBER, 1);
                break;

            case 10:
                expDate.set(intExpYear, Calendar.NOVEMBER, 1);
                break;

            case 11:
                expDate.set(intExpYear, Calendar.DECEMBER, 1);
                break;

            case 12:
                expDate.set(intExpYear + 1, Calendar.JANUARY, 1);
                break;

            default:
                errorMsg = "Invalid Expiration Date.";
                return false;
        }
        if (! _PaymentDate.before(expDate)) {
            errorMsg = getMessage(11210);
            return false;
        }

        // Validate Name on the Card
        if (_CreditCardName == null || _CreditCardName.equals("")) {
            errorMsg = getMessage(11211);
            return false;
        }

        // Verify the card type
        if (_CardType == null || _CardType.equals("")) {
            errorMsg = getMessage(11212);
            return false;
        }
        if (_CardType.equals("VISA") || _CardType.equals("MC") || _CardType.equals("Discover")
                || _CardType.equals("AmEx")) {
            // card type is valid, nothing to do...
        } else {
            errorMsg = getMessage(11213);
            return false;
        }
        return true;
    }

    /** This method calls XAI to add a payment.
     */
    public boolean isPaymentAddSuccessful() {
        String tenderControlId = _Properties.getProperty("com.splwg.selfservice.TenderControlId");
        String tenderType = _Properties.getProperty("com.splwg.selfservice.TenderType");
        //       String currencyCode =
        //           _Properties.getProperty("com.splwg.selfservice.CurrencyCode");

        String autoPaySourceCode = "";

        if (_CardType.equals("VISA"))
            autoPaySourceCode = _Properties.getProperty("com.splwg.selfservice.AutoPay.Visa");
        if (_CardType.equals("MC"))
            autoPaySourceCode = _Properties.getProperty("com.splwg.selfservice.AutoPay.MasterCard");
        if (_CardType.equals("Discover"))
            autoPaySourceCode = _Properties.getProperty("com.splwg.selfservice.AutoPay.Discover");
        if (_CardType.equals("AmEx"))
            autoPaySourceCode = _Properties.getProperty("com.splwg.selfservice.AutoPay.AmericanExpress");

        String xml2 = new StringBuffer(1024).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                                .append("<SOAP-ENV:Body>\n")
                                                .append("<SSvcAutoPaySourceTypeMaintenance transactionType='Read'>\n")
                                                .append("<SSvcAutoPaySourceTypeMaintenanceService>\n")
                                                .append("<SSvcAutoPaySourceTypeMaintenanceHeader AutoPaySourceCode='")
                                                .append(autoPaySourceCode).append("'>\n")
                                                .append("</SSvcAutoPaySourceTypeMaintenanceHeader>\n")
                                                .append("</SSvcAutoPaySourceTypeMaintenanceService>\n")
                                                .append("</SSvcAutoPaySourceTypeMaintenance>\n")
                                                .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                                .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml2);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return false;
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcAutoPaySourceTypeMaintenanceDetails']");
            tenderType = Util.getValue(ele, "@TenderType");
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }

        // Setting of expiry date for CorDaptix
        
        String strExpiry = "";
        Date expiry = expDate.getTime();
        SimpleDateFormat sdfInput;
        sdfInput = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        strExpiry = sdfInput.format(expiry);

        String xml = new StringBuffer(1024).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                               .append("<SOAP-ENV:Body>\n")
                                               .append("<SSvcPaymentEventMaintenance transactionType='Add'>\n")
                                               .append("<SSvcPaymentEventMaintenanceService>\n")
                                               .append("<SSvcPaymentEventMaintenanceDetails ")
                                               .append("Distribute='true' AllPaymentsFreezeSwitch='true' ")
                                               .append("PaymentDate='").append(getPaymentDateFormatted())
                                               .append("'>\n").append("<Payment>\n")
                                               .append("<PaymentRow rowAction='Add' AccountID='")
                                               .append(_AccountId).append("' PaymentAmount='")
                                               .append(_PaymentAmount).append("'/>\n").append("</Payment>\n")
                                               .append("<PaymentTender>\n")
                                               .append("<PaymentTenderRow rowAction='Add' PayorAccountID='")
                                               .append(_AccountId).append("' TenderType='").append(tenderType)
                                               .append("' ExternalAccountID='").append(_CreditCardNumber)
                                               .append("' AutoPaySourceCode='").append(autoPaySourceCode)
                                               .append("' ExpirationMonth='").append(_ExpMonth)
                                               .append("' ExpirationYear='").append(_ExpYear)
                                               .append("' ENTITY_NAME2='")
                                               .append(XMLEncoder.encode(_CreditCardName))
                                               .append("' ScheduleExtractDate='")
                                               .append(getPaymentDateFormatted()).append("' TenderAmount='")
                                               .append(_PaymentAmount).append("' CurrencyCode='")
                                               .append(currencyCode).append("'/>")
                                               .append("</PaymentTender>\n")
                                               .append("</SSvcPaymentEventMaintenanceDetails>\n")
                                               .append("</SSvcPaymentEventMaintenanceService>\n")
                                               .append("</SSvcPaymentEventMaintenance>\n")
                                               .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                               .toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                if (errorMsg.indexOf("Number: 24101") > 0) {
                    errorMsg = getMessage(11214);
                }
                return false;
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPaymentEventMaintenanceDetails']");
            _PaymentEventId = Util.getValue(ele, "@PaymentEventID");
            ele = (Element) document.selectSingleNode("//*[local-name()='PaymentRow']");
            Number amt = numInput.parse(Util.getValue(ele, "@AccountBalance"));
            _AccountBalanceAfterPayment = numOutput.format(amt);

            addCustomerContact();
            return true;
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }
    }

    private void addCustomerContact() {
        String contactClass = _Properties.getProperty(
                                                      "com.splwg.selfservice.CustomerContactClassPaymentReceived");

        String contactType = _Properties.getProperty(
                                                     "com.splwg.selfservice.CustomerContactTypePaymentReceived");

        if (! contactType.equals("NONE") && ! contactType.equals("")) {
            String comments;
            comments = "Payment Id: " + _PaymentEventId;

            CustomerContactBean cb = new CustomerContactBean();
            cb.setPersonId(personId);
            cb.setProperties(_Properties);
            cb.setContactClass(contactClass);
            cb.setContactType(contactType);
            cb.setComments(comments);
            cb.addCustomerContact();
        }
    }

    private String getMessage(int nbr) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(_Properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr);
    }
}

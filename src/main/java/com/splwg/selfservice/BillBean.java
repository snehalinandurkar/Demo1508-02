/*
 * BillBean.java
 *
 * Created on March 8 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads bill information
 */

package com.splwg.selfservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class BillBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    /** These fields represent fields returned by the BillMaintenance service.
     *  They may or may not be populated. The getter methods will call the
     *  service if necessary.
     */
    private String _BillId;
    private String _AccountId;
    private String _BillDate;
    private String _DueDate;
    private String _BeginningBalance;
    private String _TotalPayments;
    private String _TotalAdjustments;
    private String _TotalCurrentCharges;
    private String _TotalCorrections;
    private String _EndingBalance;
    private String _BillInfo;
    private String _OnlineBillURL;
    private Properties _Properties;
    private String errorMsg;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;
    private String currencyPosition;
    private String currencySymbol;

    /** These fields are completely internal to the class. They describe the
     *  state of the bean (which of the data field have been retrieved and which
     *  need to be retrieved by calling the XAI service.
     */
    private boolean _AllInfoRetrieved;
    private boolean _SummaryInfoRetrieved;
    private boolean _DisplayInfoGenerated;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new empty BillBean */
    public BillBean() {
        _AllInfoRetrieved = false;
        _SummaryInfoRetrieved = false;
        _DisplayInfoGenerated = false;
    }

    /** Creates new BillBean with summary info */
    public BillBean(String billId, String billDate, String totalCurrentCharges, String endingBalance) {
        _BillId = billId;
        _BillDate = billDate;
        _TotalCurrentCharges = totalCurrentCharges;
        _EndingBalance = endingBalance;
        _AllInfoRetrieved = false;
        _SummaryInfoRetrieved = true;
        _DisplayInfoGenerated = false;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getCurrencyPosition() {
        return currencyPosition;
    }

    void setCurrencyPosition(String value) {
        currencyPosition = value;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    void setCurrencySymbol(String value) {
        currencySymbol = value;
    }

    public String getBillId() {
        return _BillId;
    }

    void setBillId(String value) {
        _BillId = value;
        _AllInfoRetrieved = false;
        _SummaryInfoRetrieved = false;
        _DisplayInfoGenerated = false;
    }

    public String getAccountId() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return _AccountId;
    }

    public String getBillDate() {
        if (!_SummaryInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return _BillDate;
    }

    public String getDueDate() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return _DueDate;
    }

    public String getBeginningBalance() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_BeginningBalance);
    }

    public String getTotalPayments() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_TotalPayments);
    }

    public String getTotalAdjustments() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_TotalAdjustments);
    }

    public String getTotalCurrentCharges() {
        if (!_SummaryInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_TotalCurrentCharges);
    }

    public String getTotalCorrections() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_TotalCorrections);
    }

    public String getEndingBalance() {
        if (!_SummaryInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return formatCurrencyValue(_EndingBalance);
    }

    public String getBillInfo() {
        if (!_AllInfoRetrieved) {
            _AllInfoRetrieved = callBillMaintenanceService(false);
            _SummaryInfoRetrieved = _AllInfoRetrieved;
        }
        return _BillInfo;
    }

    public String getOnlineBillURL() {
        if (!_DisplayInfoGenerated) {
            _DisplayInfoGenerated = callBillMaintenanceService(true);
            _SummaryInfoRetrieved = _AllInfoRetrieved = _DisplayInfoGenerated;
        }
        return _OnlineBillURL;
    }

    void setProperties(Properties value) {
        _Properties = value;
        sdfInput = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(_Properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private String formatCurrencyValue(String value) {
        if (currencyPosition.equals("PR"))
            return currencySymbol + value;
        else
            return value + currencySymbol;
    }

    private boolean callBillMaintenanceService(boolean generateDisplayInfo) {
        StringBuffer xmlBuff = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcBillMaintenance transactionType='");
        if (generateDisplayInfo) {
            xmlBuff.append("Update");
        } else {
            xmlBuff.append("Read");
        }
        xmlBuff.append("'>\n").append("<SSvcBillMaintenanceService>\n").append("<SSvcBillMaintenanceHeader BillID='")
                .append(_BillId).append("'/>\n");
        if (generateDisplayInfo) {
            xmlBuff.append("<SSvcBillMaintenanceDetails BillID='").append(_BillId).append("' DisplayBill='true'/>\n");
        }
        xmlBuff.append("</SSvcBillMaintenanceService>\n").append("</SSvcBillMaintenance>\n").append(
                "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n");

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xmlBuff.toString());
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return false;
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcBillMaintenanceDetails']");
            _AccountId = Util.getValue(ele, "@AccountID");
            Date billDate = sdfInput.parse(Util.getValue(ele, "@BillDate"));
            _BillDate = sdfOutput.format(billDate);
		System.out.println("Edrik BillDateParsed[" + _BillDate +"]");
            Date due = sdfInput.parse(Util.getValue(ele, "@DueDate"));
            _DueDate = sdfOutput.format(due);
		System.out.println("DueDateParsed[" + _DueDate +"]");


		_BillDate = Util.getValue(ele, "@BillDate");
		System.out.println("BillDateXML[" + _BillDate +"]");
            _DueDate = Util.getValue(ele, "@DueDate");
		System.out.println("DueDateXML[" + _DueDate +"]");
            _BeginningBalance = Util.getValue(ele, "@BeginningBalance");
            _TotalPayments = Util.getValue(ele, "@TotalPayments");
            _TotalAdjustments = Util.getValue(ele, "@TotalAdjustments");
            _TotalCurrentCharges = Util.getValue(ele, "@TotalCurrentCharges");
            _TotalCorrections = Util.getValue(ele, "@TotalCorrections");
            _EndingBalance = Util.getValue(ele, "@EndingBalance");

            _BillInfo = Util.decode(Util.getValue(ele, "@BillInfo"));
            if (generateDisplayInfo) {
                _OnlineBillURL = Util.decode(Util.getValue(ele, "@OnlineBillURL"));
            }
            return true;
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }
    }
}

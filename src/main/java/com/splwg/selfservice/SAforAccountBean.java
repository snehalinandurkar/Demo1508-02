/*
 * SAforAccountBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves service agreements belonging to an account
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SAforAccountBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String accountId;
    private ArrayList saList;
    private boolean onlyActive;
    private Properties properties;
    private String errorMsg;
    private String dateFormat;
    private boolean infoRetrieved;
    private Calendar today;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;
    private DecimalFormat numInput;
    private DecimalFormat numOutput;
    private String currencyPosition;
    private String currencySymbol;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new SAforAccountBean */
    public SAforAccountBean() {
        saList = new ArrayList();
        saList.clear();
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

    public String getAccountId() {
        return accountId;
    }

    void setAccountId(String value) {
        accountId = value;
        infoRetrieved = false;
    }

    public String getToday() {
        today = Calendar.getInstance();
        return sdfOutput.format(today.getTime());
    }

    public String getDateFormat() {
        return dateFormat;
    }

    void setOnlyActive(boolean value) {
        onlyActive = value;
    }

    void addToSAList(ServiceAgreementBean sa) {
        saList.add(sa);
    }

    public ArrayList getDisconnectSAList() {
        return saList;
    }

    public ArrayList getSAList() {
        if (! infoRetrieved) {
            infoRetrieved = callSAforAccountService();
        }
        return saList;
    }

    void setProperties(Properties value) {
        properties = value;
        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat"));
        numInput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.XAINumberFormat"));
        numOutput = new DecimalFormat(properties.getProperty("com.splwg.selfservice.CorDaptixNumberFormat"));
        dateFormat = properties.getProperty("com.splwg.selfservice.CorDaptixDateFormat");
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private String formatCurrencyValue(String value) {
        if (currencyPosition.equals("PR")) return currencySymbol + value;
        else return value + currencySymbol;
    }

    private boolean callSAforAccountService() {
        String saID;
        String status;
        String statusDesc;

        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n").append("<SSvcSAPremiseListZone>\n")
                                              .append("<SSvcSAPremiseListZoneService>\n")
                                              .append("<SSvcSAPremiseListZoneHeader AccountID = '")
                                              .append(accountId).append("'/>\n")
                                              .append("</SSvcSAPremiseListZoneService>\n")
                                              .append("</SSvcSAPremiseListZone>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();
        ;
        try {
            saList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            List list = document.selectNodes("//*[local-name()='SAPremisesRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                String address = Util.decode(Util.getValue(ele, "@SAsPremiseDescription"));
                int occ = address.indexOf("-");
                if (occ > 10) {
                    address = address.substring(0, occ - 1);
                }
                saID = Util.getValue(ele, "@ServiceAgreement");

                String xml2 = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                                       .append("<SOAP-ENV:Body>\n")
                                                       .append("<SSvcServiceAgreementMaintenance>\n")
                                                       .append("<SSvcServiceAgreementMaintenanceService>\n")
                                                       .append("<SSvcServiceAgreementMaintenanceHeader ServiceAgreement = '")
                                                       .append(saID).append("'/>\n")
                                                       .append("</SSvcServiceAgreementMaintenanceService>\n")
                                                       .append("</SSvcServiceAgreementMaintenance>\n")
                                                       .append("</SOAP-ENV:Body>\n")
                                                       .append("</SOAP-ENV:Envelope>\n").toString();
                ;

                try {
                    httpResponse = httpCall.callXAIServer(xml2);
                    document = DocumentHelper.parseText(httpResponse);
                    faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                    if (faultEle != null) {
                        Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                        errorMsg = error.getText();
                        return (false);
                    }

                    ele = (Element) document.selectSingleNode("//*[local-name()='SSvcServiceAgreementMaintenanceDetails']");

                    Element ele2 = (Element) document.selectSingleNode("//*[local-name()='ServiceagreementRsHistoryRow']");

                    status = Util.decode(Util.getValue(ele, "@SAStatus"));

                    //status = Util.getValue(ele, "@SAStatus");
                    if (onlyActive == true && (status.equals("20") == false)) continue;

                    ServiceAgreementBean sa = new ServiceAgreementBean();

                    if (ele2 != null) {
                        sa.setSAType(ele2.valueOf("@RateSchedule"));
                    } else {
                        sa.setSAType(" ");
                    }

                    Number balance = numInput.parse(Util.getValue(ele, "@CurrentBalance"));
                    sa.setSABalance(formatCurrencyValue(numOutput.format(balance)));

                    if (status.equals("20")) sa.setActiveStatus("true");
                    else sa.setActiveStatus("false");

                    Date startDate;
                    Date endDate;
                    String endDateString;

                    startDate = sdfInput.parse(Util.getValue(ele, "@StartDate"));
                    sa.setStartDate(sdfOutput.format(startDate));
                    endDateString = Util.getValue(ele, "@EndDate");
                    if (! endDateString.equals(" ")) {
                        endDate = sdfInput.parse(Util.getValue(ele, "@EndDate"));
                        sa.setEndDate(sdfOutput.format(endDate));
                    } else {
                        sa.setEndDate("");
                    }

//                  List list2 = (List) document.selectNodes("//*[local-name()='ServiceagreementServicepointRow']");
                    //if (list2.size() > 1)
                    //  sa.setAddress("Multiple");
                    //else
                    sa.setAddress(address);

                    sa.setServiceDesc(Util.decode(Util.getValue(ele, "@SATypeInfo")));
                    sa.setSAId(Util.getValue(ele, "@ServiceAgreement"));
                    sa.setStatus(Util.decode(Util.getValue(ele, "@SAStatusDescription")));

                    // The following according to functional review item 31 to exclude item SA's
                    Element ele4 = (Element) document.selectSingleNode("//*[local-name()='ServiceagreementServicepointRow']");
                    if (ele4 != null) {
                        String startRead = ele4.valueOf("@StartMeterRead");
                        if (startRead == null || startRead.equals("")) {
                            sa.setActiveStatus("false");
                        }
                    }
                    saList.add(sa);
                } catch (Exception e) {
                    errorMsg = e.getMessage();
                    return (false);
                }
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }
}

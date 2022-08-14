/*
 * MeterReadEntryBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Adds a meter read
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MeterReadEntryBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    /**
     */
    private String meterConfigurationId;
    private String personId;
    private String meterReadId;
    private String regId1;
    private String regId2;
    private String regRead1;
    private String regRead2;
    private Properties properties;
    private String _ErrorMessage;
    private boolean _IsBusy = false;
    private ArrayList regList;
    private int regCount;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new empty MeterReadEntryBean */
    public MeterReadEntryBean() {}

    /** Creates new MeterReadEntryBean with properties */
    public MeterReadEntryBean(Properties prop) {
        setProperties(prop);
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    void setProperties(Properties value) {
        properties = value;
        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateTimeFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateTimeFormat"));
    }

    public void setRegList(ArrayList value) {
        regList = value;
    }

    public void setRegCount(int value) {
        regCount = value;
    }

    public String getMeterConfigurationId() {
        return meterConfigurationId;
    }

    public void setMeterConfigurationId(String value) {
        meterConfigurationId = value;
    }

    public void setPersonId(String value) {
        personId = value;
    }

    public String getRegId1() {
        return regId1;
    }

    public void setRegId1(String value) {
        regId1 = value;
    }

    public String getRegId2() {
        return regId2;
    }

    public void setRegId2(String value) {
        regId2 = value;
    }

    public String getRegRead1() {
        return Util.decode(regRead1);
    }

    public void setRegRead1(String value) {
        regRead1 = value;
    }

    public String getRegRead2() {
        return Util.decode(regRead2);
    }

    public void setRegRead2(String value) {
        regRead2 = value;
    }

    public String getErrorMessage() {
        return Util.decode(_ErrorMessage);
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

    /** This method calls XAI to add a meter read.
     */
    public boolean isMeterReadAddSuccessful() {
        int i;

        Date today = new Date();

        String dateString = sdfInput.format(today);
        String meterReadSource = properties.getProperty("com.splwg.selfservice.MeterReadSource");

        String xml = new StringBuffer(1024).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                               .append("<SOAP-ENV:Body>\n")
                                               .append("<SSvcMeterReadMaintenance  transactionType='Add'>\n")
                                               .append("<SSvcMeterReadMaintenanceService >\n")
                                               .append("<SSvcMeterReadMaintenanceDetails  \n")
                                               .append("UseOnBill='true' ReadDateTime='").append(dateString)
                                               .append("' MeterReadSource='").append(meterReadSource)
                                               .append("' MeterConfigurationID='")
                                               .append(meterConfigurationId).append("'>\n")
                                               .append("<RegisterReading >\n").toString();

        int j = 1;
        List list = regList;
        for (Iterator it = list.iterator(); it.hasNext();) {
            MeterRegisterBean reg = (MeterRegisterBean) it.next();
            Integer k = new Integer(j);

            xml = xml.concat("<RegisterReadingRow rowAction='Add' ReadSequence='");
            xml = xml.concat(k.toString());
            xml = xml.concat("' RegisterID='");
            xml = xml.concat(reg.getRegisterId());
            xml = xml.concat("' ReadType='50");
            xml = xml.concat("' RegisterReading='");
            xml = xml.concat(reg.getRegisterRead());
            xml = xml.concat("'/>\n");
            j++;
        }
        xml = xml.concat("</RegisterReading>\n");
        xml = xml.concat("</SSvcMeterReadMaintenanceDetails>\n");
        xml = xml.concat("</SSvcMeterReadMaintenanceService>\n");
        xml = xml.concat("</SSvcMeterReadMaintenance>\n");
        xml = xml.concat("</SOAP-ENV:Body>\n");
        xml = xml.concat("</SOAP-ENV:Envelope>\n");

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                _ErrorMessage = error.getText();
                return false;
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcMeterReadMaintenanceDetails']");
            meterReadId = Util.decode(Util.getValue(ele, "@MeterReadID"));

            list = document.selectNodes("//*[local-name()='RegisterReadingRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                ele = (Element) iter.next();

                String regRead = Util.decode(Util.getValue(ele, "@RegisterReading"));
                String lowLimit = Util.decode(Util.getValue(ele, "@LowLimit"));
                String highLimit = Util.decode(Util.getValue(ele, "@HighLimit"));

                float read = Float.parseFloat(regRead);
                float low = Float.parseFloat(lowLimit);
                float high = Float.parseFloat(highLimit);

                if (read < low || read > high) addToDoEntry(regRead, lowLimit, highLimit);
            }

            addCustomerContact();

            return true;
        } catch (Exception e) {
            _ErrorMessage = e.getMessage();
            return false;
        }
    }

    private void addCustomerContact() {
        String contactClass = properties.getProperty("com.splwg.selfservice.CustomerContactClassMeterRead");

        String contactType = properties.getProperty(
                                                    "com.splwg.selfservice.CustomerContactTypeMeterRead");

        if (! contactType.equals("NONE") && ! contactType.equals("")) {
            String comments;
            comments = "Meter Read Id: " + meterReadId;

            CustomerContactBean cb = new CustomerContactBean();
            cb.setPersonId(personId);
            cb.setProperties(properties);
            cb.setContactClass(contactClass);
            cb.setContactType(contactType);
            cb.setComments(comments);
            cb.addCustomerContact();
        }
    }

    private void addToDoEntry(String regRead, String lowLimit, String highLimit) {
        String toDoType = properties.getProperty("com.splwg.selfservice.MeterReadToDoType");

        if (! toDoType.equals("NONE") && ! toDoType.equals("")) {
            ToDoBean td = new ToDoBean();
            td.setToDoType(toDoType);
            td.setDrillKey(meterReadId);
            td.setSortKey1(regRead);
            td.setSortKey2(lowLimit);
            td.setSortKey3(highLimit);
            td.setProperties(properties);
            td.setSource("MR");
            td.addToDoEntry();
        }
    }
}

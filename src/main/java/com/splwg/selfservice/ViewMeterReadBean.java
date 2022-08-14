/*
 * ViewMeterReadBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used to retrieve meter read data belonging to the register of the meter of the SP related to the selected SA
 *
 */

package com.splwg.selfservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class ViewMeterReadBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String saId;
    private String selectedSPId;
    private ArrayList regList;
    private ArrayList spList;

    //private ArrayList registerList;
    private Properties properties;
    private String errorMessage;
    private boolean infoRetrieved;
    public String dateString;
    private SimpleDateFormat sdfInput;
    private SimpleDateFormat sdfOutput;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new SAforAccountBean */
    public ViewMeterReadBean() {
        regList = new ArrayList();
        spList = new ArrayList();
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getSAId() {
        return saId;
    }

    void setSAId(String value) {
        saId = value;
        infoRetrieved = false;
    }

    public String getSelectedSPId() {
        return selectedSPId;
    }

    void setSelectedSPId(String value) {
        selectedSPId = value;
        infoRetrieved = false;
    }

    public ArrayList getSPList() {
        if (!infoRetrieved) {
            infoRetrieved = callSPforSAService();
        }
        return spList;
    }

    public ArrayList getRegisterList() {
        if (!infoRetrieved) {
            infoRetrieved = callSPforSAService();
        }
        return regList;
    }

    void setProperties(Properties value) {
        properties = value;
        sdfInput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.XAIDateTimeFormat"));
        sdfOutput = new SimpleDateFormat(properties.getProperty("com.splwg.selfservice.CorDaptixDateTimeFormat"));
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean callSPforSAService() {
        String spId;
        String meterId;
        String meterConfigurationId;
        XAIHTTPCall httpCall;
        String httpResponse;
        String xml;
        String registerId;
        String effDateTime;
        Node error;
        String startRead;
        int spCount = 0;

        Date today = new Date();
        dateString = sdfOutput.format(today);

        int i = 1;

        xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                .append("<SOAP-ENV:Body>\n").append("<SSvcServicePointsForAgreement>\n").append(
                        "<ServiceagreementServicepoint>\n").append(
                        "<ServiceagreementServicepointHeader ServiceAgreement = '").append(saId).append("'/>\n")
                .append("</ServiceagreementServicepoint>\n").append("</SSvcServicePointsForAgreement>\n").append(
                        "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

        try {
            regList.clear();
            httpCall = new XAIHTTPCall(properties);
            httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMessage = error.getText();
                return (false);
            }
            List list = document.selectNodes("//*[local-name()='ServiceagreementServicepointRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                spId = Util.getValue(ele, "@ServicePointID");
                startRead = Util.getValue(ele, "@StartMeterRead");
                if (spId != null && !startRead.equals(" ")) {
                    spCount++;

                    ServicePointBean sp = new ServicePointBean();
                    sp.setSPInfo(Util.getValue(ele, "@SPInfo"));
                    sp.setSPId(spId);
                    sp.setSAId(saId);
                    if ((selectedSPId == null && i == 1) || (selectedSPId != null && spId.equals(selectedSPId))) {
                        sp.setSelected("true");

                        String xml2 = new StringBuffer(512).append(
                                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append(
                                "<SOAP-ENV:Body>\n").append("<SSvcSPMeterHistorySearch>\n").append("<Smh>\n").append(
                                "<SmhHeader ServicePointID = '").append(spId).append("'/>\n").append("</Smh>\n")
                                .append("</SSvcSPMeterHistorySearch>\n").append("</SOAP-ENV:Body>\n").append(
                                        "</SOAP-ENV:Envelope>\n").toString();

                        try {
                            httpResponse = httpCall.callXAIServer(xml2);
                            document = DocumentHelper.parseText(httpResponse);
                            faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                            if (faultEle != null) {
                                error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                                errorMessage = error.getText();
                                return (false);
                            }
                            list = document.selectNodes("//*[local-name()='SmhRow']");
                            Iterator iter1 = list.iterator();
                            ele = (Element) iter1.next();

                            meterId = Util.getValue(ele, "@MeterID");
                            meterConfigurationId = Util.getValue(ele, "@MeterConfigurationID");
                            sp.setBadgeNbr(Util.getValue(ele, "@BadgeNumber"));

                            String xml22 = new StringBuffer(512).append(
                                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append(
                                    "<SOAP-ENV:Body>\n").append("<SSvcGetEffectiveMeterConfiguration>\n").append(
                                    "<SSvcGetEffectiveMeterConfigurationService>\n").append(
                                    "<SSvcGetEffectiveMeterConfigurationHeader MeterConfigurationID = '").append(
                                    meterConfigurationId).append("'/>\n").append(
                                    "</SSvcGetEffectiveMeterConfigurationService>\n").append(
                                    "</SSvcGetEffectiveMeterConfiguration>\n").append("</SOAP-ENV:Body>\n").append(
                                    "</SOAP-ENV:Envelope>\n").toString();

                            try {
                                httpResponse = httpCall.callXAIServer(xml22);
                                document = DocumentHelper.parseText(httpResponse);
                                faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                                if (faultEle != null) {
                                    error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                                    errorMessage = error.getText();
                                    return (false);
                                }

                                Element eleConfiguration = (Element) document
                                        .selectSingleNode("//*[local-name()='SSvcGetEffectiveMeterConfigurationDetails']");
                                effDateTime = eleConfiguration.valueOf("@EffectiveDateTime");

                            } catch (Exception e) {
                                errorMessage = e.getMessage();
                                return (false);
                            }

                            String xml3 = new StringBuffer(512).append(
                                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append(
                                    "<SOAP-ENV:Body>\n").append("<SSvcMeterRegistrationList>\n")
                                    .append("<Registers>\n").append("<RegistersHeader MeterID = '").append(meterId)
                                    .append("' EffectiveDateTime = '").append(effDateTime).append("'/>\n").append(
                                            "</Registers>\n").append("</SSvcMeterRegistrationList>\n").append(
                                            "</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

                            try {
                                httpResponse = httpCall.callXAIServer(xml3);
                                document = DocumentHelper.parseText(httpResponse);
                                faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                                if (faultEle != null) {
                                    error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                                    errorMessage = error.getText();
                                    return (false);
                                }

                                List listReg = document.selectNodes("//*[local-name()='RegistersRow']");
                                for (Iterator iter2 = listReg.iterator(); iter2.hasNext();) {
                                    Element eleReg = (Element) iter2.next();
                                    MeterRegisterBean reg = new MeterRegisterBean();

                                    registerId = eleReg.valueOf("@RegisterID");
                                    reg.setRegisterId(eleReg.valueOf("@RegisterID"));
                                    reg.setMeterConfigurationId(meterConfigurationId);
                                    reg.setReadSequence(eleReg.valueOf("@Seq"));
                                    reg.setUOM(eleReg.valueOf("@UnitofMeasure"));

                                    reg.setTOU(eleReg.valueOf("@TimeOfUse"));

                                    String xml4 = new StringBuffer(512).append(
                                            "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                            .append("<SOAP-ENV:Body>\n").append("<SSvcMeterBillHistory>\n").append(
                                                    "<MeterreadBillhist>\n").append(
                                                    "<MeterreadBillhistHeader RegisterID = '").append(registerId)
                                            .append("' MeterConfigurationID = '").append(meterConfigurationId).append(
                                                    "'").append(" FilterBy = 'BILL'").append("/>\n").append(
                                                    "</MeterreadBillhist>\n").append("</SSvcMeterBillHistory>\n")
                                            .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

                                    try {
                                        httpResponse = httpCall.callXAIServer(xml4);
                                        document = DocumentHelper.parseText(httpResponse);
                                        faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                                        if (faultEle != null) {
                                            error = (Element) document
                                                    .selectSingleNode("//*[local-name()='ResponseText']");
                                            errorMessage = error.getText();
                                            return (false);
                                        }

                                        List listRead = document
                                                .selectNodes("//*[local-name()='MeterreadBillhistRow']");
                                        Iterator iter3 = listRead.iterator();
                                        iter3.hasNext();
                                        Element eleRead = (Element) iter3.next();

                                        reg.setRegisterReadId(eleRead.valueOf("@RegisterReadingID"));
                                        reg.setRegisterRead(eleRead.valueOf("@RegisterReading"));
                                        Date regRead = sdfInput.parse(eleRead.valueOf("@ReadDateTime"));

                                        reg.setReadDateTime(sdfOutput.format(regRead));
                                    } catch (Exception e) {
                                        errorMessage = e.getMessage();
                                        return (false);
                                    }

                                    regList.add(reg);
                                }
                            } catch (Exception e) {
                                errorMessage = e.getMessage();
                                return (false);
                            }
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                            return (false);
                        }
                    }
                    i++;
                    spList.add(sp);
                }
            }
            if (spCount == 0) {
                errorMessage = "No service point is existing for the SA, or no meter is existing for the service point";
                return (false);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            return (false);
        }
        infoRetrieved = true;
        return (true);
    }
}

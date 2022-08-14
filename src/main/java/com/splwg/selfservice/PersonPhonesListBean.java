/*
 * PersonPhonesListBean.java
 *
 * Created on February 22 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads person phones for update personal information screenb
 *
 */

package com.splwg.selfservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author  Jacek Ziabicki
 */
public class PersonPhonesListBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _PersonId;
    private ArrayList _PersonPhonesList;
    private Properties _Properties;
    private String _ErrorMessage = "";
    private boolean _AllInfoRetrieved = false;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new PersonPhonesListBean */
    public PersonPhonesListBean() {
        _PersonPhonesList = new ArrayList();
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return _PersonId;
    }

    void setPersonId(String value) {
        _PersonId = value;
        _AllInfoRetrieved = false;
    }

    public ArrayList getPersonPhonesList(String personId) {
        if (!_AllInfoRetrieved) {
            _PersonId = personId;
            _AllInfoRetrieved = callPersonPhonesService();
        }
        return _PersonPhonesList;
    }

    public void setProperties(Properties value) {
        _Properties = value;
    }

    public String getErrorMessage() {
        return _ErrorMessage;
    }

    private boolean callPersonPhonesService() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>").append("<SOAP-ENV:Body>")
                .append("<SSvcPersonPhoneList>").append("<PersonPhones>").append("<PersonPhonesHeader PersonID='")
                .append(_PersonId).append("'/>").append("</PersonPhones>").append("</SSvcPersonPhoneList>").append(
                        "</SOAP-ENV:Body>").append("</SOAP-ENV:Envelope>").toString();

        try {
            _PersonPhonesList.clear();
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                System.out.println("SOAP Fault");
                _ErrorMessage = httpResponse;
                return false;
            }
            List list = document.selectNodes("//*[local-name()='PersonPhonesRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                String phoneType = Util.getValue(ele, "@PhoneType");
                String phoneNumber = Util.getValue(ele, "@PhoneNumber");
                String extension = Util.getValue(ele, "@Extension");
                String intlPrefix = Util.getValue(ele, "@IntlPrefix");
                PersonPhoneBean phone = new PersonPhoneBean(_PersonId, phoneType, phoneNumber, extension, intlPrefix);
                _PersonPhonesList.add(phone);
            }
            return true;
        } catch (Exception e) {
            _ErrorMessage = "Caught exception: " + e.getMessage();
            return false;
        }
    }
}

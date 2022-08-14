/*
 * RetrievePersonFromUserIdBean.java
 *
 * Created in April 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves the person id according to the input user id existing on the identifier table of the person
 *
 */

package com.splwg.selfservice;

import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class RetrievePersonFromUserIdBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String personId;
    private String userId;
    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    public RetrievePersonFromUserIdBean() {
    } /* Creates new Bean */

    /** Creates new Bean */
    public RetrievePersonFromUserIdBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    void setUserId(String value) {
        userId = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public boolean retrievePerson() {
        String idType = properties.getProperty("com.splwg.selfservice.IdType");

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonSearchByID>\n").append("<PersonSrch>\n").append("<PersonSrchHeader IDNumber= '")
                .append(userId).append("' IDType='").append(idType).append("'/>\n").append("</PersonSrch>\n").append(
                        "</SSvcPersonSearchByID>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();
        ;

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
		System.out.println("xml[" + xml + "]");
		System.out.println("httpResponse[" +httpResponse + "]");
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='PersonSrchRow']");
            if (ele != null) {
                personId = Util.getValue(ele, "@PersonID");
            } else {
                errorMsg = "NOUSER";
                return (false);
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            System.out.println("retrievePerson Exception: " + errorMsg);
            return (false);
        }
        return (true);
    }
}

/*
 * PasswordHintBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves from the lookup service the values for the password hint
 *
 */

package com.splwg.selfservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class PasswordHintBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private Properties properties;
    private String errorMsg;
    private HashMap questions;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new PasswordHintBean */
    public PasswordHintBean() {
    }

    public PasswordHintBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public HashMap getQuestions() {
        retrieveQuestions();
        return questions;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    private boolean retrieveQuestions() {
        questions = new HashMap();

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcLookupList>\n").append("<LookList>\n").append(
                        "<LookListHeader FieldName = 'WEB_PWD_HINT_FLG'/>\n").append("</LookList>\n").append(
                        "</SSvcLookupList>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();
        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }

            List list = document.selectNodes("//*[local-name()='LookListRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                questions.put(Util.decode(Util.getValue(ele, "@FieldValue")), Util.decode(Util.getValue(ele,
                        "@Description")));
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }
}

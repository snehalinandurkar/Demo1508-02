/*
 * LanguageBean.java
 *
 * Created on February 10th 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Retrieves from the lookup service the values for the password hint
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class LanguageBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private Properties properties;
    private String errorMsg;
    private TreeMap languages;

    //~ Constructors -----------------------------------------------------------------------------------------

    public LanguageBean() {}

    /** Creates new SAforAccountBean */
    public LanguageBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public TreeMap getLanguages() {
        retrieveLanguages();
        return languages;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    private boolean retrieveLanguages() {
        languages = new TreeMap();

        String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                              .append("<SOAP-ENV:Body>\n").append("<SSvcLanguageSearch>\n")
                                              .append("<Language>\n")
                                              .append("<LanguageHeader LanguageCode=''/>\n")
                                              .append("</Language>\n").append("</SSvcLanguageSearch>\n")
                                              .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                                              .toString();
        ;
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

            List list = document.selectNodes("//*[local-name()='LanguageRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                languages.put(Util.decode(Util.getValue(ele, "@Description")),
                              Util.decode(Util.getValue(ele, "@LanguageCode")));
            }
            return (true);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return (false);
        }
    }
}

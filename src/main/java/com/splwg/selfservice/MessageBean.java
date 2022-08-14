/*
 * MessageBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Creates a message according to input message number with optional parameter.
 * Message text is read of SelfServiceConfig.properties file.
 *
 */

package com.splwg.selfservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MessageBean
    implements java.io.Serializable {

    //~ Static fields/initializers ---------------------------------------------------------------------------

    private static final int INITIAL_WSS_MSG_NBR = 11000;
    private static final int LAST_WSS_MSG_NBR = 13000;

    //~ Instance fields --------------------------------------------------------------------------------------

    private Properties properties;
    private HashMap messages;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new MessageBean */
    
    public MessageBean() {}

    public MessageBean(Properties value) {
        properties = value;
        messages = new HashMap();

        loadMessages();
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getMessage(int value) {
        return retrieveMessage(value);
    }

    public String getMessage(int value, String parameter) {
        return retrieveMessage(value, parameter);
    }

    private String retrieveMessage(int value) {
        if (messages.containsKey(String.valueOf(value))) {
            return Util.decode(messages.get(String.valueOf(value)).toString());
        }
        return "No error message found";
    }

    private String retrieveMessage(int value, String parameter) {
        String language;
        String prop;
        String msg;
        int i;

        msg = retrieveMessage(value);
        i = msg.indexOf("%1");
        if (i > 0) {
            msg = msg.substring(0, i) + parameter + msg.substring(i + 2);
        }

        return msg;
    }

    private boolean loadMessages() {
        int initial = INITIAL_WSS_MSG_NBR;
        int previnit = initial;

        try {
            while (initial <= LAST_WSS_MSG_NBR) {
                String xml = new StringBuffer(512).append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n")
                                                      .append("<SOAP-ENV:Body>\n")
                                                      .append("<SSvcMessageList>\n").append("<MessageList>\n")
                                                      .append("<MessageListHeader MessageCategory='28' StartingMessageNumber='"
                                                                  + initial
                                                                  + "' LastMessageNumber='12000'/>\n")
                                                      .append("</MessageList>\n")
                                                      .append("</SSvcMessageList>\n")
                                                      .append("</SOAP-ENV:Body>\n")
                                                      .append("</SOAP-ENV:Envelope>\n").toString();

                String text;
                XAIHTTPCall httpCall = new XAIHTTPCall(properties);
                String httpResponse = httpCall.callXAIServer(xml);
                Document document = DocumentHelper.parseText(httpResponse);
                Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
                if (faultEle != null) {
                    Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                    errorMsg = error.getText();
                    return (false);
                }
                List list = document.selectNodes("//*[local-name()='MessageListRow']");
                for (Iterator iter = list.iterator(); iter.hasNext();) {
                    Element ele = (Element) iter.next();
                    text = Util.decode(Util.getValue(ele, "@CustomerSpecificMessageText"));
                    if (text == null || text.equals(" ")) {
                        text = Util.decode(Util.getValue(ele, "@MessageText"));
                    }
                    String messageNumber = Util.decode(Util.getValue(ele, "@MessageNumber"));
                    messages.put(messageNumber, text);
                    initial = Integer.parseInt(messageNumber) + 1;
                }
                if (previnit != initial) previnit = initial;
                else initial = LAST_WSS_MSG_NBR + 1;
            }
            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

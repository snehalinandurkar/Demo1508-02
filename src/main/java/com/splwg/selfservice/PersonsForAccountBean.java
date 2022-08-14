/*
 * PersonsForAccountBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Searches for a person fitting the input parameters (account id, last name, first name, address, etc.
 *
 */

package com.splwg.selfservice;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class PersonsForAccountBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private HttpSession session;
    private Properties properties;

    private String accountId;
    private String personId;
    private String lastName;
    private String firstName;
    private String eMail;
    private String address;
    private String city;
    private String postalCode;
    private String state;
    private String country;
    private String receiveMarketingInfo;
    private String warningMsg;
    private String errorMsg;
    private boolean infoRetrieved;
    private boolean personFound;
    private boolean emailExists;
    private boolean webAccess;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new AccountsForPersonBean */
    public PersonsForAccountBean() {
    }

    public PersonsForAccountBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return personId;
    }

    public String getWarningMsg() {
        return Util.decode(warningMsg);
    }

    public boolean getPersonFound() {
        return personFound;
    }

    public boolean getEmailExists() {
        return emailExists;
    }

    void setAccountId(String value) {
        accountId = value;
        infoRetrieved = false;
    }

    void setFirstName(String value) {
        firstName = value;
        infoRetrieved = false;
    }

    void setLastName(String value) {
        lastName = value;
        infoRetrieved = false;
    }

    void setEmail(String value) {
        eMail = value;
        infoRetrieved = false;
    }

    void setAddress(String value) {
        address = value;
        infoRetrieved = false;
    }

    void setCity(String value) {
        city = value;
        infoRetrieved = false;
    }

    void setPostalCode(String value) {
        postalCode = value;
        infoRetrieved = false;
    }

    void setState(String value) {
        state = value;
        infoRetrieved = false;
    }

    void setCountry(String value) {
        country = value;
        infoRetrieved = false;
    }

    void setReceiveMarketingInfo(String value) {
        receiveMarketingInfo = value;
        infoRetrieved = false;
    }

    void setProperties(Properties value) {
        properties = value;
    }

    public String getErrorMessage() {
        return Util.decode(errorMsg);
    }

    public String getEMail() {
        return Util.decode(eMail);
    }

    public String getReceiveMarketingInfo() {
        return receiveMarketingInfo;
    }

    public boolean getWebAccess() {
        return webAccess;
    }

    public boolean callPersonsForAccountService(HttpServlet servlet, HttpServletRequest request) {
        ServletContext context = servlet.getServletContext();
        properties = (Properties) context.getAttribute("properties");
        session = request.getSession();

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonsForAccount>\n").append("<SSvcPersonsForAccountService>\n").append(
                        "<SSvcPersonsForAccountHeader AccountID = '").append(accountId).append("' FirstName='").append(
                        XMLEncoder.encode(check(firstName))).append("' LastName='").append(
                        XMLEncoder.encode(check(lastName))).append("' City='").append(city).append("' Postal='")
                .append(postalCode).append("' Address='").append(address).append("' State='").append(state).append(
                        "' Country='").append(country).append("' RecvMktgInfoFlag='").append(receiveMarketingInfo)
                .append("'/>\n").append("</SSvcPersonsForAccountService>\n").append("</SSvcPersonsForAccount>\n")
                .append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n").toString();

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
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonsForAccountDetails']");
            personId = ele.valueOf("@PersonID");
            eMail = ele.valueOf("@EmailID");
            String access = Util.getValue(ele, "@WebAccessFlag");
            if (access.equals("NALW"))
                webAccess = false;
            else
                webAccess = true;
            warningMsg = ele.valueOf("@WarningMessage");
            warningMsg = warningMsg.toUpperCase();

            if (personId != null && !("".equals(personId))) {
                personFound = true;
                if (warningMsg.equals("DUPLICATE")) {
                    personFound = false;
                    errorMsg = getMessage(11903);
                }
                /*              if (!warningMsg.equals("") || warningMsg == null){
                 String modMsg = getMessage(11903) + " ";
                 int j = 0;

                 int i = warningMsg.indexOf("ADDRESS");
                 if (i > 0) {
                 modMsg = modMsg.concat(getMessage(11905));
                 j++;
                 }

                 i = warningMsg.indexOf("CITY");
                 if (i > 0) {
                 if (j > 0)
                 modMsg = modMsg.concat(", ");
                 modMsg = modMsg.concat(getMessage(11906));
                 j++;
                 }

                 i = warningMsg.indexOf("POSTAL");
                 if (i > 0) {
                 if (j > 0)
                 modMsg = modMsg.concat(", ");
                 modMsg = modMsg.concat(getMessage(11907));
                 j++;
                 }

                 i = warningMsg.indexOf("STATE");
                 if (i > 0) {
                 if (j > 0)
                 modMsg = modMsg.concat(", ");
                 modMsg = modMsg.concat(getMessage(11908));
                 j++;
                 }

                 i = warningMsg.indexOf("COUNTRY");
                 if (i > 0) {
                 if (j > 0)
                 modMsg = modMsg.concat(", ");
                 modMsg = modMsg.concat(getMessage(11909));
                 j++;
                 }

                 modMsg = modMsg + ".\n" + getMessage(11904);
                 warningMsg = modMsg;
                 }
                 */
            } else if (warningMsg.indexOf("ACCOUNT ID") > 0) {
                errorMsg = getMessage(11901);
            } else {
                errorMsg = getMessage(11902);
            }

            if (eMail != "" && eMail != null) emailExists = true;

            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }

    private String getMessage(int nbr) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr);
    }

    private String check(String text) {
        if (text == null) text = "";
        if (text.startsWith(" ")) text = text.substring(1);

        return text;
    }
}

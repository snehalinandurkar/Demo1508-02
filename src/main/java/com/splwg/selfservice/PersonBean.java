/*
 * PersonBean.java
 *
 * Created on November 04 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads person information
 *
 */

package com.splwg.selfservice;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 *
 * @author  Jacek Ziabicki
 */
public class PersonBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _PersonId;
    private String _EntityName;
    private String _HomePhone;
    private String _HomePhoneExt;
    private String _HomePhoneSeq;
    private String _BusinessPhone;
    private String _BusinessPhoneExt;
    private String _BusinessPhoneSeq;
    private String _Email;
    private String _Addr1;
    private String _Addr2;
    private String _Addr3;
    private String _Addr4;
    private String _HouseType;
    private String _Number1;
    private String _Number2;
    private String _InCityLimit;
    private String _GeoCode;
    private String _City;
    private String _County;
    private String _State;
    private String _Postal;
    private String _Country;
    private String _BillLanguage;
    private String _Receive;
    private String _Version;
    private String customerContactId;
    private boolean _HomePhoneChanged;
    private boolean _BusinessPhoneChanged;
    private boolean _EmailChanged;
    private boolean _MailingAddressChanged;
    private Properties _Properties;
    private String errorMsg;
    private MessageBean msg;
    private HttpSession session;

    //~ Constructors -----------------------------------------------------------------------------------------

    public PersonBean(Properties value) {
        _Properties = value;
        msg = new MessageBean(value);
    }

    public PersonBean() {
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return _PersonId;
    }

    void setPersonId(String value) {
        _PersonId = value;
        //_InfoRetrieved = false;
    }

    public void DisplayMessage(String value) {
        System.out.println(value);
    }

    public String getEntityName() {
        return Util.decode(_EntityName);
    }

    public String getHomePhone() {
        return _HomePhone;
    }

    public String getHomePhoneExt() {
        return _HomePhoneExt;
    }

    public String getHomePhoneSeq() {
        return _HomePhoneSeq;
    }

    public String getBusinessPhone() {
        return _BusinessPhone;
    }

    public String getBusinessPhoneExt() {
        return _BusinessPhoneExt;
    }

    public String getBusinessPhoneSeq() {
        return _BusinessPhoneSeq;
    }

    public String getEmail() {
        return Util.decode(_Email);
    }

    public String getAddr1() {
        return Util.decode(_Addr1);
    }

    public String getAddr2() {
        return Util.decode(_Addr2);
    }

    public String getAddr3() {
        return Util.decode(_Addr3);
    }

    public String getAddr4() {
        return Util.decode(_Addr4);
    }

    public String getHouseType() {
        return Util.decode(_HouseType);
    }

    public String getNumber1() {
        return _Number1;
    }

    public String getNumber2() {
        return _Number2;
    }

    public String getInCityLimit() {
        return _InCityLimit;
    }

    public String getGeoCode() {
        return Util.decode(_GeoCode);
    }

    public String getCity() {
        return Util.decode(_City);
    }

    public String getCounty() {
        return Util.decode(_County);
    }

    public String getState() {
        return Util.decode(_State);
    }

    public String getPostal() {
        return _Postal;
    }

    public String getBillLanguage() {
        return Util.decode(_BillLanguage);
    }

    public String getReceive() {
        return Util.decode(_Receive);
    }

    public String getCountry() {
        return Util.decode(_Country);
    }

    public String getVersion() {
        return Util.decode(_Version);
    }

    void setProperties(Properties value) {
        _Properties = value;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public boolean retrievePerson() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonMaintenance transactionType = 'Read'>\n")
                .append("<SSvcPersonMaintenanceService>\n").append("<SSvcPersonMaintenanceHeader PersonID = '").append(
                        _PersonId).append("'/>\n").append("</SSvcPersonMaintenanceService>\n").append(
                        "</SSvcPersonMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();
        return callPersonService(xml);
    }

    private String formatPhone(String number) {
        String outString = new String();
        String pattern = _Properties.getProperty("com.splwg.selfservice.PhoneFormat");
        int inIndex = 0;
        boolean isNumber = false;

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) != '9')
                outString += pattern.charAt(i);
            else
                while (!isNumber && (inIndex < number.length())) {
                    try {
                        isNumber = true;

                        Integer iVal = new Integer("" + number.charAt(inIndex));
                        iVal = null;
                    } catch (Exception ex) {
                        isNumber = false;
                    }

                    if (isNumber)
                        outString += number.charAt(inIndex++);
                    else
                        inIndex++;
                }
            isNumber = false;
        }
        return (outString);
    }

    /**
     * This routine will validate against data entry mistakes.
     * An error discovered here will result in forwarding the user back to the
     * entry form, which will keep all the data entered by the user.
     * The routine uses Jakarta-ORO classes for regular expression matching.
     */
    public boolean isUpdateValid(UpdatePersonBean upb, HttpServlet servlet, HttpServletRequest request) {
        session = request.getSession();

        CountryBean country = new CountryBean();
        country.setProperties(_Properties);
        country.setCountry(upb.getCountry());

        // Check that the country code is valid
        if (!country.isCountryValid()) {
            errorMsg = getMessage(11101);
            return false;
        }

        // Check that the fields that are not available according to the country
        // rules are blank.

        if (!upb.getCity().equals("") && !country.isCityAvailable()) {
            errorMsg = getMessage(11102);
            return false;
        }
        if (!upb.getCounty().equals("") && !country.isCountyAvailable()) {
            errorMsg = getMessage(11103);
        }
        if (!upb.getState().equals("") && !country.isStateAvailable()) {
            errorMsg = getMessage(11104);
            return false;
        }
        if (!upb.getPostal().equals("") && !country.isPostalAvailable()) {
            errorMsg = getMessage(11105);
            return false;
        }

        // Unless all fields of the mailing address are blank, the following
        // fields are required: Addr1, City, County, State, Postal Code, Country
        // -- provided they are available according to the country rules.

        if (!upb.getAddr1().equals("") || !upb.getAddr2().equals("") || !upb.getCity().equals("")
                || !upb.getCounty().equals("") || !upb.getState().equals("") || !upb.getPostal().equals("")
                || !upb.getCountry().equals("")) {
            if (upb.getCountry().equals("")) {
                errorMsg = getMessage(11106);
                return false;
            }
        }

        // Extension is not allowed without a phone number
        if ((upb.getHomePhone().equals("") && !upb.getHomePhoneExt().equals(""))
                || (upb.getBusinessPhone().equals("") && !upb.getBusinessPhoneExt().equals(""))) {
            errorMsg = getMessage(11111);
            return false;
        }

        // Phone number must be in the format defined in the properties file
        ServletContext context = servlet.getServletContext();
        Pattern phonePattern = (Pattern) context.getAttribute("phonePattern");

        if (upb.getHomePhone().equals("") || Pattern.matches(phonePattern.pattern(), upb.getHomePhone())) {
            // the phone number is OK
        } else {
            errorMsg = getMessage(11112) + " " + _Properties.getProperty("com.splwg.selfservice.PhoneFormat") + ".";
            return false;
        }
        if (upb.getBusinessPhone().equals("") || Pattern.matches(phonePattern.pattern(), upb.getBusinessPhone())) {
            // the phone number is OK
        } else {
            errorMsg = getMessage(11113) + " " + _Properties.getProperty("com.splwg.selfservice.PhoneFormat") + ".";
            return false;
        }

        // Email must be in the format: userid@server.domain. Userid and server
        // may contain periods.
        /* This validation is taken out because ConEd allows special characters in email address
        Pattern emailPattern = (Pattern) context.getAttribute("emailPattern");
	  System.out.println("emailPattern[" + emailPattern.pattern()+"]");
	  System.out.println("email" + upb.getEmail().trim() + "]");
        if (upb.getEmail().equals("") || Pattern.matches(emailPattern.pattern(), upb.getEmail().trim())) {
            // the email is OK
        } else {
            errorMsg = getMessage(11114);
            return false;
        } */
        return true;
    }

    /**
     * This routine will check for intervening updates before sending the update
     * request. If an intervening update is detected, or if there is a problem
     * with the update request, it will send the user to the error screen.
     * The error message will NOT be displayed to the user.
     * The user will have to refresh the screen and type all the updates again
     * in order to retry.
     */
    public boolean executeUpdate(UpdatePersonBean upb, AccountBean account) {
        // Check for intervening updates.
        boolean status = retrievePerson();
        if (!status) {
            return false;
        }

        if (!upb.getVersion().equals(getVersion())) {
            errorMsg = getMessage(12000);
            return false;
        }

        // Mark which fields are being updated - we'll include it in a Customer
        // Contact.
        _HomePhoneChanged = false;
        _BusinessPhoneChanged = false;
        _EmailChanged = false;
        _MailingAddressChanged = false;
        if (!upb.getHomePhone().equals(getHomePhone()) || !upb.getHomePhoneExt().equals(getHomePhoneExt())) {
            _HomePhoneChanged = true;
        }
        if (!upb.getBusinessPhone().equals(getBusinessPhone())
                || !upb.getBusinessPhoneExt().equals(getBusinessPhoneExt())) {
            _BusinessPhoneChanged = true;
        }
        if (!upb.getEmail().equals(getEmail())) {
            _EmailChanged = true;
        }
        
        // CEd9 - SMedina - Start Modify
        String billRouteTypeCrystal = _Properties.getProperty("com.splwg.selfservice.BillRouteTypeCrystal");
        String billRouteTypeEbill = _Properties.getProperty("com.splwg.selfservice.BillRouteTypeEbill");
        
        // if (account.getQuoteRouteType().equals("POSTAL") || (account.getBillRouteType().equals("POSTAL"))) {
        if ((account.getQuoteRouteType().equals(billRouteTypeCrystal) || account.getBillRouteType().equals(billRouteTypeCrystal))
        		|| (account.getQuoteRouteType().equals(billRouteTypeEbill) || account.getBillRouteType().equals(billRouteTypeEbill))) {
        // CEd9 - SMedina - Start Modify	
            if (!upb.getAddr1().trim().equals(getAddr1().trim()) || !upb.getAddr2().trim().equals(getAddr2().trim())
                    || !upb.getAddr3().trim().equals(getAddr3().trim())
                    || !upb.getAddr4().trim().equals(getAddr4().trim())
                    || !upb.getCity().trim().equals(getCity().trim())
                    || !upb.getCounty().trim().equals(getCounty().trim())
                    || !upb.getState().trim().equals(getState().trim())
                    || !upb.getPostal().trim().equals(getPostal().trim())
                    || !upb.getCountry().trim().equals(getCountry().trim())) {
                _MailingAddressChanged = true;
            }
        } else {
            _MailingAddressChanged = false;
            errorMsg = getMessage(11115);
            return false;

        }

        // If mailing address was blank but will be non-blank, set the billing
        // address source on account to "Person".
        // If the mailing address was non-blank but will now be blank, set the
        // billing address on account to "Premise".
        if (_MailingAddressChanged) {
            if (upb.getAddr1().equals("") && upb.getAddr2().equals("") && upb.getAddr3().equals("")
                    && upb.getAddr4().equals("") && upb.getCity().equals("") && upb.getCounty().equals("")
                    && upb.getState().equals("") && upb.getPostal().equals("") && upb.getCountry().equals("")) { // address changing to blank
                //
                setBillAddressSource(account, "PREM");
            } else { // address changing to non-blank
                setBillAddressSource(account, "PER");
            }
        }
        String phoneTypeHome = _Properties.getProperty("com.splwg.selfservice.PhoneTypeHome");
        String phoneTypeBusiness = _Properties.getProperty("com.splwg.selfservice.PhoneTypeBusiness");

        /* Build the XML request. */
        StringBuffer personPhones = new StringBuffer(512);

        if (upb.getHomePhone().equals("") || upb.getHomePhone().equals(" ")) {
            if (!getHomePhoneSeq().equals("")) {
                personPhones.append("<PersonPhonesRow rowAction='Delete' PersonID='").append(_PersonId).append(
                        "' Sequence='").append(getHomePhoneSeq()).append("'/>\n");
            }
        } else {
            personPhones.append("<PersonPhonesRow rowAction='UpAdd' PersonID='").append(_PersonId).append(
                    "' Sequence='").append(getHomePhoneSeq()).append("' PhoneNumber='").append(
                    check(upb.getHomePhone())).append("' PhoneType='").append(phoneTypeHome).append("' Extension='")
                    .append(check(upb.getHomePhoneExt())).append("'/>\n");
        }

        if (upb.getBusinessPhone().equals("") || upb.getHomePhone().equals(" ")) {
            if (!getBusinessPhoneSeq().equals("")) {
                personPhones.append("<PersonPhonesRow rowAction='Delete' PersonID='").append(_PersonId).append(
                        "' Sequence='").append(getBusinessPhoneSeq()).append("'/>\n");
            }
        } else {
            personPhones.append("<PersonPhonesRow rowAction='UpAdd' PersonID='").append(_PersonId).append(
                    "' Sequence='").append(getBusinessPhoneSeq()).append("' PhoneNumber='").append(
                    check(upb.getBusinessPhone())).append("' PhoneType='").append(phoneTypeBusiness).append(
                    "' Extension='").append(check(upb.getBusinessPhoneExt())).append("'/>\n");
        }

        StringBuffer xml = new StringBuffer(1024).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcPersonMaintenance transactionType='Update'>\n")
                .append("<SSvcPersonMaintenanceService>\n").append("<SSvcPersonMaintenanceHeader PersonID='").append(
                        _PersonId).append("'/>\n").append("<SSvcPersonMaintenanceDetails EmailID='").append(
                        check(upb.getEmail())).append("' Address='").append(XMLEncoder.encode(check(upb.getAddr1())))
                .append("' Address2='").append(XMLEncoder.encode(check(upb.getAddr2()))).append("' Address3='").append(
                        XMLEncoder.encode(check(upb.getAddr3()))).append("' AddressLine4='").append(
                        XMLEncoder.encode(check(upb.getAddr4()))).append("' HouseType='").append(
                        XMLEncoder.encode(check(upb.getHouseType()))).append("' Number1='").append(
                        XMLEncoder.encode(check(upb.getNumber1()))).append("' Number2='").append(
                        XMLEncoder.encode(check(upb.getNumber2()))).append("' InCityLimit='").append(
                        XMLEncoder.encode(check(upb.getInCityLimit()))).append("' TaxVendorGeographicalCode='").append(
                        XMLEncoder.encode(check(upb.getGeoCode()))).append("' City='").append(
                        XMLEncoder.encode(check(upb.getCity()))).append("' County='").append(
                        XMLEncoder.encode(check(upb.getCounty()))).append("' State='").append(
                        XMLEncoder.encode(check(upb.getState()))).append("' Postal='").append(
                        XMLEncoder.encode(check(upb.getPostal()))).append("' LanguageCode='").append(
                        XMLEncoder.encode(check(upb.getBillLanguage()))).append("' ReceiveMarketingInfoFlag='").append(
                        XMLEncoder.encode(check(upb.getReceive()))).append("' Country='").append(
                        XMLEncoder.encode(check(upb.getCountry()))).append("'>\n");
        if (personPhones.length() > 0) {
            xml.append("<PersonPhones>\n").append(personPhones.toString()).append("</PersonPhones>\n");
        }
        xml.append("</SSvcPersonMaintenanceDetails>\n").append("</SSvcPersonMaintenanceService>\n").append(
                "</SSvcPersonMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n");

        /* Execute the update. */
        boolean updateOK = callPersonService(xml.toString());

        if (updateOK) {
            addCustomerContact();
            if (customerContactId != null) addToDoEntry();
        }

        return updateOK;
    }

    private void setBillAddressSource(AccountBean account, String addressSource) {
        //if either bill or quote route is postal change address sources
        if ((account.getQuoteRouteType().equals("POSTAL")) || (account.getBillRouteType().equals("POSTAL")))
            account.setBillAddressSource(_PersonId, addressSource);

        //if neither bill nor quote route is postal change address source to blank
        else
            account.setBillAddressSource(_PersonId, "");
    }

    private boolean callPersonService(String httpRequest) {
        String httpResponse = null;
        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            httpResponse = httpCall.callXAIServer(httpRequest);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return false;
            }
            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcPersonMaintenanceDetails']");
            _EntityName = Util.getValue(ele, "@Name");
            _Email = Util.getValue(ele, "@EmailID");
            _Addr1 = Util.getValue(ele, "@Address");
            _Addr2 = Util.getValue(ele, "@Address2");
            _Addr3 = Util.getValue(ele, "@Address3");
            _Addr4 = Util.getValue(ele, "@AddressLine4");
            _HouseType = Util.getValue(ele, "@HouseType");
            _Number1 = Util.getValue(ele, "@PremiseNumber");
            _Number2 = Util.getValue(ele, "@Number2");
            _InCityLimit = Util.getValue(ele, "@InCityLimit");
            _GeoCode = Util.getValue(ele, "@TaxVendorGeographicalCode");
            _City = Util.getValue(ele, "@City");
            _County = Util.getValue(ele, "@County");
            _State = Util.getValue(ele, "@State");
            _Postal = Util.getValue(ele, "@Postal");
            _Country = Util.getValue(ele, "@Country");
            _BillLanguage = Util.getValue(ele, "@LanguageCode");
            _Receive = Util.getValue(ele, "@ReceiveMarketingInfoFlag");
            _Version = Util.getValue(ele, "@Version");

            _HomePhone = "";
            _HomePhoneExt = "";
            _HomePhoneSeq = "";
            _BusinessPhone = "";
            _BusinessPhoneExt = "";
            _BusinessPhoneSeq = "";

            List list = document.selectNodes("//*[local-name()='PersonPhonesRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                ele = (Element) iter.next();
                if (Util.getValue(ele, "@PhoneType").equals(
                        _Properties.getProperty("com.splwg.selfservice.PhoneTypeHome"))) {
                    _HomePhone = formatPhone(Util.getValue(ele, "@PhoneNumber"));
                    _HomePhoneExt = Util.getValue(ele, "@Extension");
                    if (_HomePhoneExt.equals(" ")) _HomePhoneExt = "";
                    _HomePhoneSeq = Util.getValue(ele, "@Sequence");
                }
                if (Util.getValue(ele, "@PhoneType").equals(
                        _Properties.getProperty("com.splwg.selfservice.PhoneTypeBusiness"))) {
                    _BusinessPhone = formatPhone(Util.getValue(ele, "@PhoneNumber"));
                    _BusinessPhoneExt = Util.getValue(ele, "@Extension");
                    if (_BusinessPhoneExt.equals(" ")) _BusinessPhoneExt = "";
                    _BusinessPhoneSeq = Util.getValue(ele, "@Sequence");
                }
            }
            return true;
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }
    }

    private void addCustomerContact() {
        String contactClass = _Properties.getProperty("com.splwg.selfservice.CustomerContactClassPersonalInfoUpdate");

        String contactType = _Properties.getProperty("com.splwg.selfservice.CustomerContactTypePersonalInfoUpdate");

        if (!contactType.equals("NONE") && !contactType.equals("")) {
            String comments;
            StringBuffer comment = new StringBuffer("Personal information changed: ");
            boolean listStarted = false;
            if (_HomePhoneChanged) {
                comment.append("Home Phone");
                listStarted = true;
            }
            if (_BusinessPhoneChanged) {
                if (listStarted) {
                    comment.append(", ");
                }
                comment.append("Business Phone");
                listStarted = true;
            }
            if (_EmailChanged) {
                if (listStarted) {
                    comment.append(", ");
                }
                comment.append("Email");
                listStarted = true;
            }
            if (_MailingAddressChanged) {
                if (listStarted) {
                    comment.append(", ");
                }
                comment.append("Mailing Address");
                listStarted = true;
            }
            if (!listStarted) {
                comment.append("None");
            }
            comment.append(".");
            comments = comment.toString();

            CustomerContactBean cb = new CustomerContactBean();
            cb.setPersonId(_PersonId);
            cb.setProperties(_Properties);
            cb.setContactClass(contactClass);
            cb.setContactType(contactType);
            cb.setComments(comments);
            cb.addCustomerContact();
            customerContactId = cb.getCustomerContactId();
        }
    }

    private void addToDoEntry() {
        String toDoType = _Properties.getProperty("com.splwg.selfservice.PersonalInfoToDoType");
        if (!toDoType.equals("NONE") && !toDoType.equals("")) {
            ToDoBean td = new ToDoBean();
            td.setToDoType(toDoType);
            td.setDrillKey(_PersonId);
            td.setSortKey1(customerContactId);
            td.setProperties(_Properties);
            td.setSource("PER");
            td.addToDoEntry();
        }
    }

    private String getMessage(int nbr) {
        MessageBean msg;
        try {
            if (session.getAttribute("Messages") != null) {
                msg = (MessageBean) session.getAttribute("Messages");
            } else {
                msg = new MessageBean(_Properties);
                session.setAttribute("Messages", msg);
            }
            return msg.getMessage(nbr);
        } catch (Exception e) {
            return ("no Message Found");
        }
    }

    private String check(String text) {
        if (text == null) text = "";
        if (text.startsWith(" ")) text = text.substring(1);

        return text;
    }
}

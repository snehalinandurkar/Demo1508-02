/*
 * CountryBean.java
 *
 * Created on November 19 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Reads country information
 */

package com.splwg.selfservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class CountryBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _Country;
    private String installOptionCountry;
    private boolean address1Available;
    private String address1Label;
    private boolean address2Available;
    private String address2Label;
    private boolean address3Available;
    private String address3Label;
    private boolean address4Available;
    private String address4Label;
    private boolean houseTypeAvailable;
    private String houseTypeLabel;
    private boolean number1Available;
    private String number1Label;
    private boolean number2Available;
    private String number2Label;
    private boolean inCityLimitAvailable;
    private String inCityLimitLabel;
    private boolean geoCodeAvailable;
    private String geoCodeLabel;
    private boolean cityAvailable;
    private String cityLabel;
    private boolean countyAvailable;
    private String countyLabel;
    private boolean stateAvailable;
    private String stateLabel;
    private boolean postalAvailable;
    private String postalLabel;
    private HashMap _States = new HashMap();
    private Properties _Properties;
    private String errorMsg;
    private boolean _InfoRetrieved = false;
    private TreeMap countries;

    //~ Constructors -----------------------------------------------------------------------------------------

    public CountryBean() {
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getCountry() {
        return Util.decode(_Country);
    }

    public TreeMap getCountryList() {
        retrieveCountries();
        return countries;
    }

    void setCountry(String value) {
        _Country = value;
        _InfoRetrieved = false;
    }

    void setDefaultCountry() {
        if (selectCountry()) {
            _Country = installOptionCountry;
            _InfoRetrieved = false;
        }
    }

    public boolean isAddress1Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address1Available;
    }

    public String getAddress1Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address1Label;
    }

    public boolean isAddress2Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address2Available;
    }

    public String getAddress2Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address2Label;
    }

    public boolean isAddress3Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address3Available;
    }

    public String getAddress3Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address3Label;
    }

    public boolean isAddress4Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address4Available;
    }

    public String getAddress4Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return address4Label;
    }

    public boolean isHouseTypeAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return houseTypeAvailable;
    }

    public String getHouseTypeLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return houseTypeLabel;
    }

    public boolean isNumber1Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return number1Available;
    }

    public String getNumber1Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return number1Label;
    }

    public boolean isNumber2Available() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return number2Available;
    }

    public String getNumber2Label() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return number2Label;
    }

    public boolean isInCityLimitAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return inCityLimitAvailable;
    }

    public String getInCityLimitLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return inCityLimitLabel;
    }

    public boolean isGeoCodeAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return geoCodeAvailable;
    }

    public String getGeoCodeLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return geoCodeLabel;
    }

    public boolean isCityAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return cityAvailable;
    }

    public String getCityLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return cityLabel;
    }

    public boolean isCountyAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return countyAvailable;
    }

    public String getCountyLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return countyLabel;
    }

    public boolean isStateAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return stateAvailable;
    }

    public String getStateLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return stateLabel;
    }

    public boolean isPostalAvailable() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return postalAvailable;
    }

    public String getPostalLabel() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return postalLabel;
    }

    public HashMap getStates() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return _States;
    }

    public boolean isStateValid(String value) {
        return getStates().containsKey(value);
    }

    public boolean isCountryValid() {
        if (!_InfoRetrieved) {
            _InfoRetrieved = retrieveCountry();
        }
        return _InfoRetrieved;
    }

    void setProperties(Properties value) {
        _Properties = value;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    private boolean retrieveCountry() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcCountryMaintenance transactionType='Read'>\n")
                .append("<SSvcCountryMaintenanceService>\n").append("<SSvcCountryMaintenanceHeader Country='").append(
                        _Country).append("'/>\n").append("</SSvcCountryMaintenanceService>\n").append(
                        "</SSvcCountryMaintenance>\n").append("</SOAP-ENV:Body>\n").append("</SOAP-ENV:Envelope>\n")
                .toString();

        String httpResponse = null;
        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                errorMsg = httpResponse;
                return false;
            }

            Element ele = (Element) document.selectSingleNode("//*[local-name()='SSvcCountryMaintenanceDetails']");

            String attr = Util.decode(Util.getValue(ele, "@Address1Available"));
            if (attr != null && attr.equals("true")) {
                address1Available = true;
            } else {
                address1Available = false;
            }
            address1Label = Util.decode(Util.getValue(ele, "@Address1"));

            attr = Util.decode(Util.getValue(ele, "@Address2Available"));
            if (attr != null && attr.equals("true")) {
                address2Available = true;
            } else {
                address2Available = false;
            }
            address2Label = Util.decode(Util.getValue(ele, "@Address2"));

            attr = Util.decode(Util.getValue(ele, "@Address3Available"));
            if (attr != null && attr.equals("true")) {
                address3Available = true;
            } else {
                address3Available = false;
            }
            address3Label = Util.decode(Util.getValue(ele, "@Address3"));

            attr = Util.decode(Util.getValue(ele, "@Address4Available"));
            if (attr != null && attr.equals("true")) {
                address4Available = true;
            } else {
                address4Available = false;
            }
            address4Label = Util.decode(Util.getValue(ele, "@Address4"));

            attr = Util.decode(Util.getValue(ele, "@HouseTypeAvailable"));
            if (attr != null && attr.equals("true")) {
                houseTypeAvailable = true;
            } else {
                houseTypeAvailable = false;
            }
            houseTypeLabel = Util.decode(Util.getValue(ele, "@HouseType"));

            attr = Util.getValue(ele, "@Number1Available");
            if (attr != null && attr.equals("true")) {
                number1Available = true;
            } else {
                number1Available = false;
            }
            number1Label = Util.decode(Util.getValue(ele, "@Number1"));

            attr = Util.decode(Util.getValue(ele, "@Number2Available"));
            if (attr != null && attr.equals("true")) {
                number2Available = true;
            } else {
                number2Available = false;
            }
            number2Label = Util.decode(Util.getValue(ele, "@Number2"));

            attr = Util.decode(Util.getValue(ele, "@InCityLimitAvailable"));
            if (attr != null && attr.equals("true")) {
                inCityLimitAvailable = true;
            } else {
                inCityLimitAvailable = false;
            }
            inCityLimitLabel = Util.decode(Util.getValue(ele, "@InCityLimit"));

            attr = Util.decode(Util.getValue(ele, "@GeoCodeAvailable"));
            if (attr != null && attr.equals("true")) {
                geoCodeAvailable = true;
            } else {
                geoCodeAvailable = false;
            }
            geoCodeLabel = Util.decode(Util.getValue(ele, "@GeoCode"));

            attr = Util.decode(Util.getValue(ele, "@CityAvailable"));
            if (attr != null && attr.equals("true")) {
                cityAvailable = true;
            } else {
                cityAvailable = false;
            }
            cityLabel = Util.decode(Util.getValue(ele, "@City"));

            attr = Util.decode(Util.getValue(ele, "@CountyAvailable"));
            if (attr != null && attr.equals("true")) {
                countyAvailable = true;
            } else {
                countyAvailable = false;
            }
            countyLabel = Util.decode(Util.getValue(ele, "@County"));

            attr = Util.decode(Util.getValue(ele, "@StateAvailable"));
            if (attr != null && attr.equals("true")) {
                stateAvailable = true;
            } else {
                stateAvailable = false;
            }
            stateLabel = Util.decode(Util.getValue(ele, "@State"));

            attr = Util.decode(Util.getValue(ele, "@PostalAvailable"));
            if (attr != null && attr.equals("true")) {
                postalAvailable = true;
            } else {
                postalAvailable = false;
            }
            postalLabel = Util.decode(Util.getValue(ele, "@Postal"));

            _States.clear();
            List list = document.selectNodes("//*[local-name()='StateRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                ele = (Element) iter.next();
                _States.put(Util.decode(Util.getValue(ele, "@State")), Util.decode(Util.getValue(ele, "@Description")));
            }
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return false;
        }
        return true;
    }

    private boolean retrieveCountries() {
        String country;
        String desc;

        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcCountrySearch>\n").append("<Country>\n").append("<CountryHeader/>")
                .append("</Country>\n").append("</SSvcCountrySearch>\n").append("</SOAP-ENV:Body>\n").append(
                        "</SOAP-ENV:Envelope>\n").toString();

        try {
            countries = new TreeMap();
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            List list = document.selectNodes("//*[local-name()='CntStateRow']");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();

                country = Util.decode(ele.valueOf("@Country"));
                desc = Util.decode(ele.valueOf("@Description"));
                countries.put(desc, country);
            }

            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }

    private boolean selectCountry() {
        String xml = new StringBuffer(512).append(
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='urn:schemas-xmlsoap-org:envelope'>\n").append("<SOAP-ENV:Body>\n")
                .append("<SSvcInstallationOptionsMaintenance>\n").append(
                        "<SSvcInstallationOptionsMaintenanceService>\n").append(
                        "<SSvcInstallationOptionsMaintenanceHeader/>").append(
                        "</SSvcInstallationOptionsMaintenanceService>\n").append(
                        "</SSvcInstallationOptionsMaintenance>\n").append("</SOAP-ENV:Body>\n").append(
                        "</SOAP-ENV:Envelope>\n").toString();

        try {
            XAIHTTPCall httpCall = new XAIHTTPCall(_Properties);
            String httpResponse = httpCall.callXAIServer(xml);
            Document document = DocumentHelper.parseText(httpResponse);
            Element faultEle = (Element) document.selectSingleNode("//SOAP-ENV:Fault");
            if (faultEle != null) {
                Node error = (Element) document.selectSingleNode("//*[local-name()='ResponseText']");
                errorMsg = error.getText();
                return (false);
            }
            Element ele = (Element) document
                    .selectSingleNode("//*[local-name()='SSvcInstallationOptionsMaintenanceDetails']");

            installOptionCountry = Util.decode(Util.getValue(ele, "@Country"));

            return (true);
        } catch (Exception e) {
            errorMsg = "Caught exception: " + e.getMessage();
            return (false);
        }
    }
}

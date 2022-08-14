/*
 * UpdatePersonBean.java
 *
 * Created on November 11 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Holds data entered in the Update Personal Information screen
 *
 */

package com.splwg.selfservice;

public class UpdatePersonBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    /**
     */
    private String _HomePhone;
    private String _HomePhoneExt;
    private String _BusinessPhone;
    private String _BusinessPhoneExt;
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

    //~ Constructors -----------------------------------------------------------------------------------------

    public UpdatePersonBean() {}

    /** Creates new UpdatePersonBean */
    public UpdatePersonBean(String newHomePhone, String newHomePhoneExt,
                            String newBusinessPhone, String newBusinessPhoneExt, String newEmail,
                            String newAddr1, String newAddr2, String newAddr3, String newAddr4,
                            String newHouseType,
                            String newNumber1,
                            String newNumber2,
                            String newInCityLimit,
                            String newGeoCode,
                            String newCity,
                            String newCounty,
                            String newState,
                            String newPostal,
                            String newCountry,
                            String billLanguage,
                            String receive,
                            String previousVersion) {
        _HomePhone = newHomePhone;
        _HomePhoneExt = newHomePhoneExt;
        _BusinessPhone = newBusinessPhone;
        _BusinessPhoneExt = newBusinessPhoneExt;
        _Email = newEmail;
        _Addr1 = newAddr1;
        _Addr2 = newAddr2;
        _Addr3 = newAddr3;
        _Addr4 = newAddr4;
        _HouseType = newHouseType;
        _Number1 = newNumber1;
        _Number2 = newNumber2;
        _InCityLimit = newInCityLimit;
        _GeoCode = newGeoCode;
        _City = newCity;
        _County = newCounty;
        _State = newState;
        _Postal = newPostal;
        _Country = newCountry;
        _BillLanguage = billLanguage;
        _Receive = receive;
        _Version = previousVersion;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getHomePhone() {
        return _HomePhone;
    }

    public String getHomePhoneExt() {
        return _HomePhoneExt;
    }

    public String getBusinessPhone() {
        return _BusinessPhone;
    }

    public String getBusinessPhoneExt() {
        return _BusinessPhoneExt;
    }

    public String getEmail() {
        return _Email;
    }

    public String getAddr1() {
        return _Addr1;
    }

    public String getAddr2() {
        return _Addr2;
    }

    public String getAddr3() {
        return _Addr3;
    }

    public String getAddr4() {
        return _Addr4;
    }

    public String getHouseType() {
        return _HouseType;
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
        return _GeoCode;
    }

    public String getCity() {
        return _City;
    }

    public String getCounty() {
        return _County;
    }

    public String getState() {
        return _State;
    }

    public String getPostal() {
        return _Postal;
    }

    public String getCountry() {
        return _Country;
    }

    public String getBillLanguage() {
        return _BillLanguage;
    }

    public String getReceive() {
        return _Receive;
    }

    public String getVersion() {
        return _Version;
    }
}

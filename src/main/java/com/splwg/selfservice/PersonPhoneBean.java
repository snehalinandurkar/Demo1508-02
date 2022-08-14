/*
 * PersonPhoneBean.java
 *
 * Created on November 19, 2002
 */

package com.splwg.selfservice;

import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author  Michel Benoliel
 */
public class PersonPhoneBean
    implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _PersonId;
    private String _PhoneType;
    private String _PhoneNumber;
    private String _Extension;
    private String _IntlPrefix;
    private HashMap _Phones = new HashMap();
    private Properties _Properties;
    private String _ErrorMessage;

    //~ Constructors -----------------------------------------------------------------------------------------

    public PersonPhoneBean() {}

    /** Creates new PersonPhone with summary info */
    public PersonPhoneBean(String personId, String phoneType, String phoneNumber, String extension,
                           String intlPrefix) {
        _PersonId = personId;
        _PhoneType = phoneType;
        _PhoneNumber = phoneNumber;
        _Extension = extension;
        _IntlPrefix = intlPrefix;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getPersonId() {
        return _PersonId;
    }

    public String getPhoneType() {
        return _PhoneType;
    }

    public String getPhoneNumber() {
        return _PhoneNumber;
    }

    public String getExtension() {
        return _Extension;
    }

    public String getIntlPrefix() {
        return _IntlPrefix;
    }

    public String getErrorMessage() {
        return Util.decode(_ErrorMessage);
    }
}

/*
 * DummyAuthentication.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used as a placeholder for any authentication class plug in. Class name set in SelfServiceConfig.properties file.
 *
 */

package com.splwg.selfservice;

import java.util.Properties;


public class DummyAuthentication implements IAuthentication {

    private Properties properties;
    private String personId;
    private String password;

    public DummyAuthentication() {
    }

    public DummyAuthentication(Properties value) {
        properties = value;
    }

    public void setPersonId(String value) {
      personId = value;
    }

    public void setPassword(String value) {
      password = value;
    }

    public boolean validatePassword() {
        return true;
    }
}

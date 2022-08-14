/*
 * InvalidParameterException.java
 *
 * Created on February 8 2005, by Eric Herrera
 * Exception Class thrown when the encoding used is invalid
 *
 */
package com.splwg.selfservice;

public class InvalidParameterException
    extends Exception {

    //~ Constructors -----------------------------------------------------------------------------------------

    InvalidParameterException(String errorMessage) {
        super(errorMessage);
    }
}

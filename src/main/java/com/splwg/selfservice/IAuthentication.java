/*
 * IAuthentication.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Interface for Authentication class plug in.  (DummyAuthentication.java is its implementation.)
 *
 */

package com.splwg.selfservice;


public interface IAuthentication
{



     // Methods: setPersonId and setPassword
     //
     // Function: Set the respective value
     //
     // Parameters: Credit Card Number as Strings.
     //
     // Returns: boolean.

    // Method: validatePassword
    //
    // Function: validate the user and password
    //
    // Parameters: none
    //
    // Returns: boolean.
   //
   void setPersonId(String value);
   public void setPassword(String value);
   public boolean validatePassword();
}

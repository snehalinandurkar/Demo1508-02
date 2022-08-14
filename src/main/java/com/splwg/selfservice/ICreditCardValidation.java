/*
 * ICreditCardValidation.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Interface for Credit Card Validation class plug in.  (DummyCreditCardValidation.java is its implementation.)
 *
 */

package com.splwg.selfservice;


public interface ICreditCardValidation
{

  public void setNameOnCard(String value);
  public void setExpiryMonth(String value);
  public void setExpiryYear(String value);
  public void setAmount(String value);
  public void setCreditCardNumber(String value);
  public void setCreditCardCompany(String value);


   // Method: validateCard
   //
   // Function: Returns true, if credit card ok, otherwise false
   //
   // Parameters: Credit Card Number as Strings.
   //
   // Returns: boolean.
   //

   public boolean validateCard();
}

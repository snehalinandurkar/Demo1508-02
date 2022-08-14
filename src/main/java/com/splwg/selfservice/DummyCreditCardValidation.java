/*
 * DummyCreditCardValidation.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used as a placeholder for a credit card validation class plug in. Classname defined in SelfServiceConfig.properties
 * file
 *
 * Validation code in this class was copied from http://mindprod.com, author Roedy Green
 */

package com.splwg.selfservice;

import java.util.Properties;


public class DummyCreditCardValidation implements ICreditCardValidation {


    private String nameOnCard;
    private String expiryMonth;
    private String expiryYear;
    private String amount;
    private String creditCardNumber;
    private String creditCardCompany;

    private Properties properties;

    public DummyCreditCardValidation() {
    }

    public DummyCreditCardValidation(Properties value) {
        properties = value;
    }

    public void setNameOnCard(String value) {
      nameOnCard = value;
    }

    public void setExpiryMonth(String value) {
      expiryMonth = value;
    }

    public void setExpiryYear(String value) {
      expiryYear = value;
    }

    public void setAmount(String value) {
      amount = value;
    }

    public void setCreditCardNumber(String value) {
      creditCardNumber = value;
    }

    public void setCreditCardCompany(String value) {
      creditCardCompany = value;
    }

    public static final boolean debugging = false;

    public static final int NOT_ENOUGH_DIGITS = -3;
    public static final int TOO_MANY_DIGITS = -2;
    public static final int UNKNOWN_VENDOR = -1;
    /**
     * Credit card vendors supported
     */
    public static final int AMEX = 1;
    public static final int DINERS = 2; // includes Carte Blanche
    public static final int DISCOVER = 3;
    public static final int ENROUTE = 4;
    public static final int JCB = 5;
    public static final int MASTERCARD = 6;
    public static final int VISA = 7;

    public boolean validateCard() {
        long longNumber = 0;

        longNumber = Long.parseLong(creditCardNumber);
        if (isValid(longNumber)) {
            int ret = recognizeVendor(longNumber);
            if (vendorToString(ret).equals(creditCardCompany)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * used by vendorToString to describe the enumerations
     */
    private static final String[] vendors =
    {
       "Error: not enough digits",
       "Error: too many digits",
       "Error: unknown credit card company",
       "dummy",
       "AmEx",
       "Diners/Carte Blanche",
       "Discover",
       "enRoute",
       "JCB",
       "MC",
       "VISA"};

    /**
     * Determine the credit card company.
     * Does NOT validate checkdigit.
     *
     * @param creditCardNumber number on card.
     *
     * @return credit card vendor enumeration constant.
     *
     */
    public static int recognizeVendor(long creditCardNumber)
       {

       int i = findMatchingRange(creditCardNumber);
       if ( i < 0 )
          {
          return i;
          }
       else
          {
          return ranges[i].vendor;
          }
       } // end recognize

    /**
     * Determine if the credit card number is valid,
     * i.e. has good prefix and checkdigit.
     * Does _not_ ask the credit card company
     * if this card has been issued or is in good standing.
     *
     * @param creditCardNumber number on card.
     *
     * @return true if card number is good.
     *
     *
     */
    public static boolean isValid( long creditCardNumber)
       {
       int i = findMatchingRange(creditCardNumber);
       if ( i < 0 )
          {
          return false;
          }
       else
          {
          // we have a match
          if ( ranges[i].hasCheckDigit )
             {
             // there is a checkdigit to be validated
             /*
             Manual method MOD 10 checkdigit
             706-511-227

             7   0   6   5   1   1   2   2   7
               * 2     * 2     * 2     * 2
             ---------------------------------
             7 + 0 + 6 +1+0+ 1 + 2 + 2 + 4 = 23

             23 MOD 10 = 3

             10 - 3 = 7 -- the check digit

             Note digits of multiplication results
             must be added before sum.

             Computer Method MOD 10 checkdigit
             706-511-227

             7   0   6   5   1   1   2   2   7
                 Z       Z       Z       Z
             ---------------------------------
             7 + 0 + 6 + 1 + 1 + 2 + 2 + 4 + 7 = 30

             30 MOD 10 had better = 0

             */
             long number = creditCardNumber;
             int checksum = 0;
             for ( int place = 0; place<16; place++ )
                {
                int digit = (int) (number % 10);
                number /= 10;
                if ( (place & 1) == 0 )
                   {
                   // even position, just add digit
                   checksum += digit;
                   }
                else
                   { // odd position, must double and add
                   checksum += z(digit);
                   }
                if ( number == 0 )
                   {
                   break;
                   }
                } // end for
             // good checksum should be 0 mod 10
             return(checksum % 10) == 0;

             }
          else
             {
             return true; // no checksum needed
             }
          } // end if have match
       } // end isValid

    /**
      * Convert a creditCardNumber as long to a formatted String.
      * Currently it breaks 16-digit numbers into groups of 4.
      *
      * @param creditCardNumber number on card.
      *
      * @return String representation of the credit card number.
      */
    public static String toPrettyString( long creditCardNumber )
       {
       String plain = Long.toString(creditCardNumber);
       int i = findMatchingRange(creditCardNumber);
       int length = plain.length();

       switch ( length )
          {
          case 12:
             // 12 pattern 3-3-3-3
             return plain.substring(0,3)
             + ' ' + plain.substring(3,6)
             + ' ' + plain.substring(6,9)
             + ' ' + plain.substring(9,12);

          case 13:
             // 13 pattern 4-3-3-3
             return plain.substring(0,4)
             + ' ' + plain.substring(4,7)
             + ' ' + plain.substring(7,10)
             + ' ' + plain.substring(10,13);

          case 14:
             // 14 pattern 2-4-4-4
             return plain.substring(0,2)
             + ' ' + plain.substring(2,6)
             + ' ' + plain.substring(6,10)
             + ' ' + plain.substring(10,14);

          case 15:
             // 15 pattern 3-4-4-4
             return plain.substring(0,3)
             + ' ' + plain.substring(3,7)
             + ' ' + plain.substring(7,11)
             + ' ' + plain.substring(11,15);

          case 16:
             // 16 pattern 4-4-4-4
             return plain.substring(0,4)
             + ' ' + plain.substring(4,8)
             + ' ' + plain.substring(8,12)
             + ' ' + plain.substring(12,16);

          case 17:
             // 17 pattern 1-4-4-4-4
             return plain.substring(0,1)
             + ' ' + plain.substring(1,5)
             + ' ' + plain.substring(5,9)
             + ' ' + plain.substring(9,13)
             + ' ' + plain.substring(13,17);

          default:
             // 0..11, 18+ digits long
             // plain
             return plain;
          } // end switch

       } // end toPrettyString

    /**
     * Converts a vendor index enumeration to the equivalent words.
     * It will trigger an ArrayIndexOutOfBoundsException
     * if you feed it an illegal value.
     *
     * @param vendorEnum e.g. AMEX, UNKNOWN_VENDOR, TOO_MANY_DIGITS
     *
     * @return equivalent string in words, e.g. "Amex" "Error: unknown vendor".
     */
    public static String vendorToString(int vendorEnum)
       {
       return vendors[vendorEnum - NOT_ENOUGH_DIGITS];
       } // end vendorToString

    /**
     * Used to speed up findMatchingRange by caching the last hit.
     */
    private static int cachedLastFind = 0;

    /**
      * Finds a matching range in the ranges array
      * for a given creditCardNumber.
      *
      * @param creditCardNumber number on card.
      *
      * @return index of matching range,
      * or NOT_ENOUGH_DIGITS or UNKNOWN_VENDOR on
      * failure.
      */
    protected static int findMatchingRange (long creditCardNumber)
       {

       if ( creditCardNumber < 1000000000000L )
          {
          return NOT_ENOUGH_DIGITS;
          }
       if ( creditCardNumber > 9999999999999999L )
          {
          return TOO_MANY_DIGITS;
          }
       // check the cached index first, where we last found a number.
       if ( ranges[cachedLastFind].low <= creditCardNumber
            && creditCardNumber <= ranges[cachedLastFind].high )
          {
          return cachedLastFind;
          }
       for ( int i=0; i<ranges.length; i++ )
          {
          if ( ranges[i].low <= creditCardNumber
               && creditCardNumber <= ranges[i].high )
             {
             // we have a match
             cachedLastFind = i;
             return i;
             }
          } // end for
       return UNKNOWN_VENDOR;

       } // end findMatchingRange

    /**
      * used in computing checksums, doubles and adds resulting digits.
      *
      * @param digit the digit to be doubled, and digit summed.
      *
      * @return // 0->0 1->2 2->4 3->6 4->8 5->1 6->3 7->5 8->7 9->9
      */
    private static int z (int digit)
       {
       if ( digit == 0 ) return 0;
       else return(digit*2-1) % 9 + 1;
       }

    /**
      * convert a String to a long.  The routine is very forgiving.
      * It ignores invalid chars, lead trail,
      * embedded spaces, decimal points etc, AND minus signs.
      *
      * @param numstr the String containing the number
      * to be converted to long.
      *
      * @return long value of the string found,
      * ignoring junk characters. May be negative.
      *
      * @exception NumberFormatException
      * if the number is too big to fit in a long.
      *
      * @see Misc.parseDirtyLong It is similar, but treats
      * dash as a minus sign.
      *
      */
    public static long parseDirtyLong( String numStr )
       {
       numStr = numStr.trim();
       // strip commas, spaces, + etc, AND -
       StringBuffer b = new StringBuffer(numStr.length());
       for ( int i=0; i<numStr.length(); i++ )
          {
          char c = numStr.charAt(i);
          if ( '0' <= c && c <= '9' )
             {
             b.append(c);
             }
          } // end for
       numStr = b.toString();
       if ( numStr.length() == 0 )
          {
          return 0;
          }
       return Long.parseLong (numStr);
       } // end parseDirtyLong

    /*
     From http://www.icverify.com/
     Vendor        Prefix len      checkdigit
     MASTERCARD    51-55  16       mod 10
     VISA          4      13, 16   mod 10
     AMEX          34,37  15       mod 10
     Diners Club/
     Carte Blanche
                  300-305 14
                  36      14
                  38      14       mod 10
     Discover     6011    16       mod 10
     enRoute      2014    15
                  2149    15       any
     JCB          3       16       mod 10
     JCB          2131    15
                  1800    15       mod 10  */

    private static LCR[] ranges =
    {  // careful, no lead zeros allowed
       //                    low               high  len  vendor      mod-10?
       new LCR(    4000000000000L,    4999999999999L, 13, VISA,       true),
       new LCR(   30000000000000L,   30599999999999L, 14, DINERS,     true),
       new LCR(   36000000000000L,   36999999999999L, 14, DINERS,     true),
       new LCR(   38000000000000L,   38999999999999L, 14, DINERS,     true),
       new LCR(  180000000000000L,  180099999999999L, 15, JCB,        true),
       new LCR(  201400000000000L,  201499999999999L, 15, ENROUTE,    false),
       new LCR(  213100000000000L,  213199999999999L, 15, JCB,        true),
       new LCR(  214900000000000L,  214999999999999L, 15, ENROUTE,    false),
       new LCR(  340000000000000L,  349999999999999L, 15, AMEX,       true),
       new LCR(  370000000000000L,  379999999999999L, 15, AMEX,       true),
       new LCR( 3000000000000000L, 3999999999999999L, 16, JCB,        true),
       new LCR( 4000000000000000L, 4999999999999999L, 16, VISA,       true),
       new LCR( 5100000000000000L, 5599999999999999L, 16, MASTERCARD, true),
       new LCR( 6011000000000000L, 6011999999999999L, 16, DISCOVER,   true)
    }; // end table initialisation

    /**
     * test harness
     */

    }


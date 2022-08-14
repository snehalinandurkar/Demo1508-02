/*
 * LCR.java
 *
 * Created on May, 28th 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Used for Credit Card Validation
 *
 * This class was copied from http://mindprod.com, author Roedy Green
 */

package com.splwg.selfservice;

/**
 * Describes a single Legal Card Range
 */
public class LCR
   {

   /**
    * public constructor
    */
   public LCR (long low,
               long high,
               int length,
               int vendor,
               boolean hasCheckDigit)
      {
      this.low = low;
      this.high = high;
      this.length = length;
      this.vendor = vendor;
      this.hasCheckDigit = hasCheckDigit;
      } // end public constructor

   /**
     * low and high bounds on range covered by this vendor
     */
   public long low, high;

   /**
    * how many digits in this type of number.
    */
   public int length;

   /**
   * enumeration credit card service
   */
   public int vendor;

   /**
    * does this range have a MOD-10 checkdigit?
    */
   public boolean hasCheckDigit;

   } // end class LCR

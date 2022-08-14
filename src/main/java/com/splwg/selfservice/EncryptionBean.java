/*
 * EncryptionBean.java
 *
 * Created in January 2003, by Arie Rave
 * Part of CorDaptix Web Self Service (10321)
 * Encrypts and decrypts the password
 *
 * Uses the encryption in use by XAI
 * (PBE encryption with MD5 and DES has been tested with providers sunjce and BouncyCastle, and both worked fine
 * with WebLogic 6.1, but caused problems with WebLogic 7, and therefore they are not being used.
 * WebLogic documentation notes that the jce package has not been tested with WebLogic 7.)
 *
 */

package com.splwg.selfservice;

public class EncryptionBean
    implements java.io.Serializable {

    //~ Static fields/initializers ---------------------------------------------------------------------------

    private static final String FIX_USER = "internaluser";

    //~ Instance fields --------------------------------------------------------------------------------------

    private String passwordPlain;
    private String passwordEncrypted;
    private byte[] encryptedInput;

    //~ Methods ----------------------------------------------------------------------------------------------

    public void setPasswordPlain(String value) {
        passwordPlain = value;
    }

    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    public String getPasswordPlain() {
        return passwordPlain;
    }

    public void setEncryptedInput(String value) {
        passwordEncrypted = value;
    }

    public String encryptPassword() {
        try {
            return cipher(FIX_USER, passwordPlain);
        } catch (Throwable e) {
            return "";
        }
    }

    private static String cipher(String pUser, String pPassword) {
        String encodedValue;
        int rotate;
        char oneChar;
        int numValue;
        String str1;
        String str2;
        String cch = " ";

        rotate = getRotate(pUser);
        str2 = Integer.toHexString(rotate);
        if (str2.length() == 1) str2 = "0" + cch;

        str1 = str2;
        for (int i = 0; i < pPassword.length(); i += 1) {
            oneChar = pPassword.charAt(i);
            numValue = (int) oneChar - rotate;
            if (numValue < 0) numValue = numValue + 255;

            cch = Long.toHexString(numValue);
            if (cch.length() == 1) cch = "0" + cch;

            str1 = str1 + cch;
        }

        encodedValue = str1 + str2;

        return encodedValue.toUpperCase();
    }

    public String decryptPassword() {
        try {
            return decipherPassword(FIX_USER, passwordEncrypted);
        } catch (Throwable e) {
            return "";
        }
    }

    private static String decipherPassword(String pUser, String pCodedString) {
        String decodedValue = "";
        int rotate;
        String twoChar;
        int numValue = 0;

        rotate = getRotate(pUser);
        String rotstr = Integer.toHexString(rotate);
        if (rotstr.length() == 1) rotstr = "0 ";

        pCodedString = pCodedString.substring(rotstr.length(), pCodedString.length() - rotstr.length()); // ????

        for (int i = 0; i < pCodedString.length(); i += 2) {
            twoChar = pCodedString.substring(i, i + 2);
            numValue = hexToNum(twoChar);
            numValue = numValue + rotate;
            if (numValue > 255) numValue = numValue - 255;
            decodedValue = decodedValue + (char) numValue;
        }

        return decodedValue;
    }

    //
    // Method: getRotate
    //
    // Function: Gets Rotate.
    //
    // Parameters: 1) Internal user (constant)
    //
    //
    // Returns: Rotate.
    //
    private static int getRotate(String pUser) {
        double rotate = 0;
        double MAX_ASCI = 255;

        byte[] bytes = pUser.getBytes();

        for (int i = 0; i < pUser.length(); i++) {
            rotate = rotate + bytes[i];
        }

        rotate = Math.IEEEremainder(rotate, MAX_ASCI);
        return (int) rotate;
    }
    // Method: hexDigit
    //
    // Function: Gets the integer value of char.
    //
    // Parameters: Char value.
    //
    // Returns: Integer value of char.
    //

    private static int hexDigit(char pChar) {
        int intValue;

        if ((int) pChar >= 65) intValue = (int) pChar - 55;
        else intValue = (int) pChar - 48;

        return intValue;
    }

    // Method: hexToNum
    //
    // Function: Gets the integer value from hexadecimal.
    //
    // Parameters: hexadecimal value.
    //
    // Returns: Integer value of hexadecimal.
    //
    private static int hexToNum(String oHexValue) {
        int firstDigit;
        int secondDigit;
        int NumberValue;

        char[] HexCharsArray;
        HexCharsArray = oHexValue.toCharArray();
        firstDigit = hexDigit(HexCharsArray[0]);
        secondDigit = hexDigit(HexCharsArray[1]);

        NumberValue = 16 * firstDigit + secondDigit;

        return NumberValue;
    }
}

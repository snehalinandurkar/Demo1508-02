package com.splwg.selfservice;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebStringUtilities {

    //~ Static fields/initializers ---------------------------------------------------------------------------

    private static final String NULL_STRING = "(null)";
    private static final char MAX_ASCII = 0x7F;

    private static final String[] JAVASCRIPT_ASCII_ESCAPES;
    private static final String[] ATTRIBUTE_ASCII_ESCAPES;

    //Valid field characters whitespace,a-z,A-Z,0-9,-,_
    private static final Pattern VALID_FIELD_PATTERN = Pattern.compile("[\\s\\w-_]*");

    //possible XSS injection, matching < something >
    private static final Pattern POSSIBLE_XSS_PATTERN = Pattern.compile(".*<(.*?)>.*");
    
    // Start Add - 2015-05-19 - MCeripul - Security Fix
    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("@._-");
    // End Add - 2015-05-19 - MCeripul - Security Fix

    // CSOFF: MagicNumberCheck
    static {
        JAVASCRIPT_ASCII_ESCAPES = new String[128];
        // 0 - 9 are 48 - 57
        // A-Z are 65 - 90
        // a-z are 97 - 122
        for (int i = 1; i < 48; i++) {
            populateJavaScriptEscape(i, JAVASCRIPT_ASCII_ESCAPES);
        }
        for (int i = 58; i < 65; i++) {
            populateJavaScriptEscape(i, JAVASCRIPT_ASCII_ESCAPES);
        }
        for (int i = 91; i < 97; i++) {
            populateJavaScriptEscape(i, JAVASCRIPT_ASCII_ESCAPES);
        }
        for (int i = 123; i < 128; i++) {
            populateJavaScriptEscape(i, JAVASCRIPT_ASCII_ESCAPES);
        }

        ATTRIBUTE_ASCII_ESCAPES = new String[128];
        for (int i = 1; i < 48; i++) {
            populateAttributeEscape(i, ATTRIBUTE_ASCII_ESCAPES);
        }
        for (int i = 58; i < 65; i++) {
            populateAttributeEscape(i, ATTRIBUTE_ASCII_ESCAPES);
        }
        for (int i = 91; i < 97; i++) {
            populateAttributeEscape(i, ATTRIBUTE_ASCII_ESCAPES);
        }
        for (int i = 123; i < 128; i++) {
            populateAttributeEscape(i, ATTRIBUTE_ASCII_ESCAPES);
        }
    }

    // CSON: MagicNumberCheck

    //~ Constructors -----------------------------------------------------------------------------------------

    // Prevent instantiation;
    private WebStringUtilities() {
        // empty
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    // Accepts characters < 0x80
    // CSOFF: MagicNumberCheck
    private static void populateJavaScriptEscape(int charIndex, String[] target) {
        if (charIndex > MAX_ASCII) {
            throw new IllegalArgumentException("Input charIndex cannot exceed 0x7F");
        }
        char[] chars = new char[4];
        chars[0] = '\\';
        chars[1] = 'x';
        String hexString = Integer.toHexString(charIndex).toUpperCase(Locale.ENGLISH);
        if (charIndex < 0x10) {
            chars[2] = '0';
            chars[3] = hexString.charAt(0);
        } else {
            chars[2] = hexString.charAt(0);
            chars[3] = hexString.charAt(1);
        }
        target[charIndex] = new String(chars);
    }

    // Accepts characters < 0x80
    private static void populateAttributeEscape(int charIndex, String[] target) {
        if (charIndex > 0x7F) {
            throw new IllegalArgumentException("Input charIndex cannot exceed 0x7F");
        }
        char[] chars = new char[6];
        String hexString = Integer.toHexString(charIndex).toUpperCase(Locale.ENGLISH);
        chars[0] = '&';
        chars[1] = '#';
        chars[2] = 'x';
        if (charIndex < 0x10) {
            chars[3] = '0';
            chars[4] = hexString.charAt(0);
        } else {
            chars[3] = hexString.charAt(0);
            chars[4] = hexString.charAt(1);
        }
        chars[5] = ';';
        target[charIndex] = new String(chars);
    }

    // CSON: MagicNumberCheck

    /**
     * Convert the given string to HTML by escaping various HTML-unsafe characters to
     * escape sequences.
     *
     * @param string input string
     * @return HTML-safe string
     */
    public static String asHTML(String string) {
        if (null == string) {
            return "";
        }
        // Allocate 20% excess capacity allowing for growth without reallocation
        StringBuilder buf = new StringBuilder(initialBufferCapacity(string.length()));
        char ch;
        int max = string.length();
        for (int i = 0; i < max; i++) {
            ch = string.charAt(i);
            switch (ch) {
            case '<':
                // left angle bracket
                buf.append("&lt;");
                break;
            case '>':
                // right angle bracket
                buf.append("&gt;");
                break;
            case '"':
                // double-quote
                buf.append("&quot;");
                break;
            case '&':
                // ampersand
                buf.append("&amp;");
                break;
            case '\'':
                // apostrophe
                buf.append("&#x27;");
                break;
            case '/':
                // forward slash
                buf.append("&#x2F;");
                break;
            default:
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    /**
     * Convert the given string to a javascript-safe literal, replacing escape characters, including '\'
     * @param string a string to place in javascript
     * @return the javascript-safe string
     */
    // CSOFF: CyclomaticComplexityCheck
    public static String asJavaScriptLiteral(String aString) {
        if (aString == null) return NULL_STRING;
        String string = aString.trim();
        // Allocate 20% excess capacity to allow for growth without reallocation
        StringBuilder buf = new StringBuilder(initialBufferCapacity(string.length()));
        char ch;
        int max = string.length();
        for (int i = 0; i < max; i++) {
            ch = string.charAt(i);
            switch (ch) {
            case '\r':
                // eliminate carriage returns
                break;
            case '\000':
                // blow up on null character
                //log and throw an exception
                //                throw LoggedException.raised(logger,
                //                      "Found illegal null character in string while emitting literal javascript, '"
                //                            + StringUtilities.toLiteralUnicode(string) + "'");
            default:
                escapeJavascript(ch, buf);
            }
        }
        return buf.toString();
    }

    // CSON: CyclomaticComplexityCheck

    /**
     * Convert the given string to a javascript-safe literal, replacing escape characters, excluding '\'
     * @param string a string to place in javascript
     * @return the javascript-safe string
     */
    // CSOFF: CyclomaticComplexityCheck|ModifiedControlVariableCheck

    public static String asMessageJavaScriptLiteral(String string) {
        /* This variant method is expected to process strings that are already supposed to be (mostly) JavaScript encoded.
         This means that bare backslashes should already be escaped, in order to avoid confusion with explicitly escaped
         carriage returns.

         For security reasons, only \n escapes are allowed to pass through, the method will escape all other
         backslashes. Exception: doubled backslashes are allowed and will be converted to single escaped backslashes.
         */

        if (string == null) return NULL_STRING;
        //
        StringBuilder buf = new StringBuilder(initialBufferCapacity(string.length()));
        char ch;
        int max = string.length();
        for (int i = 0; i < max; i++) {
            ch = string.charAt(i);
            switch (ch) {
            case '\r':
                // eliminate carriage returns
                break;
            case '\\':
                // back-slash
                // first see if we're at the end
                if (i == max - 1) {
                    // escape single ending backslash
                    escapeJavascript(ch, buf);
                } else {
                    // peek ahead
                    char next = string.charAt(i + 1);
                    if (next == '\\') {
                        // double backslash, escape as single backslash
                        escapeJavascript(ch, buf);
                        // skip ahead by one
                        i++;
                    } else if (next == 'n') {
                        // escaped newline, preserve intact
                        buf.append(ch);
                        buf.append(next);
                        // skip ahead by one
                        i++;
                    } else {
                        // bare backslash, escape it
                        escapeJavascript(ch, buf);
                    }
                }
                break;
            case '\000':
                // blow up on null character
                //log and throw an exception
                // throw LoggedException.raised(logger,
                //        "Found illegal null character in string while emitting literal javascript, '"
                //                + StringUtilities.toLiteralUnicode(string) + "'");
            default:
                escapeJavascript(ch, buf);
            }
        }
        return buf.toString();
    }

    // CSON: CyclomaticComplexityCheck|ModifiedControlVariableCheck

    /*
     *  Escape input to comply with JSON standard, see http://json.org
     *  Important: Only use for JSON output, not for general-purpose Javascript escaping!
     */

    public static String asJSON(String string) {
        if (string == null) return NULL_STRING;
        // Allocate 20% excess capacity to allow for growth without reallocation
        StringBuilder filtered = new StringBuilder(initialBufferCapacity(string.length()));
        char ch;
        int max = string.length();
        for (int i = 0; i < max; i++) {
            ch = string.charAt(i);
            switch (ch) {
            case '\b':
                // eliminate backspace
                break;
            case '\n':
                // escape linefeed
                filtered.append("\\n");
                break;
            case '\r':
                // eliminate carriage returns
                break;
            case '\"':
                // escape double-quote
                filtered.append("\\\"");
                break;
            case '\f':
                // eliminate form-feed
                break;
            case '\t':
                // escape tab
                filtered.append("\\t");
                break;
            case '\\':
                // escape backslash
                filtered.append("\\\\");
                break;
            case '/':
                // escape forward slash
                filtered.append("\\/");
                break;
            case '\000':
                // blow up on null character
                //log and throw an exception
                //throw LoggedException.raised(logger,
                //      "Found illegal null character in string while emitting literal javascript, '"
                //            + StringUtilities.toLiteralUnicode(string) + "'");
            default:
                filtered.append(ch);
            }
        }
        return filtered.toString().trim();
    }

    // Always trim leading/trailing whitespace from attributes

    public static String asAttributeLiteral(String aString) {
        if (aString == null) return NULL_STRING;
        String string = aString.trim();
        // Allocate 20% excess capacity to allow for growth without reallocation
        StringBuilder buf = new StringBuilder(initialBufferCapacity(string.length()));
        char ch;
        int max = string.length();
        for (int i = 0; i < max; i++) {
            ch = string.charAt(i);
            switch (ch) {
            case '\r':
                // eliminate carriage returns
                break;
            case '\000':
                // blow up on null character
                //log and throw an exception
                //                throw LoggedException.raised(logger,
                //                      "Found illegal null character in string while emitting literal attribute, '"
                //                            + StringUtilities.toLiteralUnicode(string) + "'");
            default:
                escapeAttribute(ch, buf);
            }
        }
        return buf.toString();
    }

    /**
     * Sort a list of list bodies
     * @param listBodies the list bodies to sort
     * @param sortAttribute the attribute to sort on
     * @param language the language to sort under
     */
    // CSOFF: UnusedParameterCheck

    private static int initialBufferCapacity(int originalStringSize) {
        // CSOFF: MagicNumberCheck
        //  Heuristic calculation ~20%
        return originalStringSize + (originalStringSize / 5) + 5;
    }

    // CSON: MagicNumberCheck

    // CSOFF: MagicNumberCheck
    private static void escapeJavascript(char ch, StringBuilder buf) {
        if (ch >= 48 && ch <= 57 || ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122 || ch > 127) {
            buf.append(ch);
            return;
        }
        buf.append(JAVASCRIPT_ASCII_ESCAPES[ch]);
    }

    private static void escapeAttribute(char ch, StringBuilder buf) {
        if (ch >= 48 && ch <= 57 || ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122 || ch > 127) {
            buf.append(ch);
            return;
        }
        buf.append(ATTRIBUTE_ASCII_ESCAPES[ch]);
    }

    // CSON: MagicNumberCheck

    public static Boolean validInputField(String aString) {
        Matcher matcher = VALID_FIELD_PATTERN.matcher(aString);
        return matcher.matches();
    }

    public static Boolean checkXssInjection(String aString) {
        Matcher matcher = POSSIBLE_XSS_PATTERN.matcher(aString);
        return matcher.matches();
    }
    
    // Start Add - 2015-05-19 - MCeripul - Security Fix
    public static Boolean checkValidEmail(String aString) {
        Matcher matcher = VALID_EMAIL_PATTERN.matcher(aString);
        return matcher.matches();
    }
    // End Add - 2015-05-19 - MCeripul - Security Fix

    public static String escapeMessage(String aString) {
        if (checkXssInjection(aString)) {
            return WebStringUtilities.asJavaScriptLiteral(aString);
        }
        return aString;
    }

}

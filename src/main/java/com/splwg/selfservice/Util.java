/*
 * Util.java
 *
 * Contains methods being used by other beans
 *
 * Method getValue retrieves the requested value from the element, and if the value is null, replaces it
 * with a blank
 *
 */

package com.splwg.selfservice;

import org.dom4j.Element;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates a new instance of Util */
    public Util() {}

    //~ Methods ----------------------------------------------------------------------------------------------

    public static String getValue(Element ele, String text) {
        String value = ele.valueOf(text);
        if (value == null || value.equals("")) value = " ";

        return value;
    }

    public static String getValue(Element ele, String text, SimpleDateFormat in, SimpleDateFormat out) {
        String ret = " ";
        try {
            String dateString = getValue(ele, text);
            if (! dateString.equals(" ")) {
                Date endDate = in.parse(dateString);
                ret = out.format(endDate);
            }
        } catch (Exception exc) {
            ret = " ";
        }
        return ret;
    }

    public static String getValue2(Element ele, String text, SimpleDateFormat in, SimpleDateFormat out) {
        String ret = " ";
        try {
            String dateString = getValue(ele, text);
            if (! dateString.equals(" ")) {
                int sep = dateString.indexOf(" - ");

                if (sep > 0) {
                    Date date1 = in.parse(dateString.substring(0, sep));
                    Date date2 = in.parse(dateString.substring(sep+3));

                    ret = out.format(date1) + " - " + out.format(date2);
                } else {
                    ret = dateString;
                }
            }
        } catch (Exception exc) {
            ret = " ";
        }
        return ret;
    }


    public static String getCMFile(String fileName, ServletContext context) {
        java.io.InputStream inputStream = context.getResourceAsStream("CM" + fileName);
        try {
            if (inputStream != null) {
                return "/CM" + fileName;
            } else {
                return "/" + fileName;
            }
        } catch (Exception exc) {
            System.out.println("Error: " + exc);
            return "/" + fileName;
        }
    }

    public static String ShowField(String fldValue) {
        if (fldValue == "" || fldValue == null) return ("&nbsp;");
        else return (fldValue);
    } // End Function

    public static float toFloat(String val) {
        float ret = 0.0f;

        try {
            ret = Float.parseFloat(val);
        } catch (Exception e) {
            ret = 0.0f;
        }
        return ret;
    }

    public static String decode(String source) {
        String apos = "&apos;";
        String lt = "&lt;";
        if (source != null) {
            final int len = apos.length();
            StringBuffer sb = new StringBuffer();
            int found = -1;
            int start = 0;

            if (apos.equals("&apos;")) {
                while ((found = source.indexOf(apos, start)) != -1) {
                    sb.append(source.substring(start, found));
                    sb.append("'");
                    start = found + len;
                }
            } else if (lt.equals("&lt;")) {
                while ((found = source.indexOf(lt, start)) != -1) {
                    sb.append(source.substring(start, found));
                    sb.append("<");
                    start = found + lt.length();
                }
            } else if (lt.equals("&gt;")) {
                while ((found = source.indexOf(lt, start)) != -1) {
                    sb.append(source.substring(start, found));
                    sb.append(">");
                    start = found + lt.length();
                }
            } else if (lt.equals("&amp;")) {
                while ((found = source.indexOf(lt, start)) != -1) {
                    sb.append(source.substring(start, found));
                    sb.append("&");
                    start = found + lt.length();
                }
            } else if (lt.equals("&quot;")) {
                while ((found = source.indexOf(lt, start)) != -1) {
                    sb.append(source.substring(start, found));
                    sb.append('"');
                    start = found + lt.length();
                }
            }

            sb.append(source.substring(start));

            return sb.toString();
        } else return "";
    }
}

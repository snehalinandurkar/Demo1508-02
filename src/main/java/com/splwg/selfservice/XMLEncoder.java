/*
 * XMLEncoder.java
 *
 * Created on November 16, 2002, 3:51 PM
 */

package com.splwg.selfservice;

/**
 *
 * @author  Jacek Ziabicki
 */
public class XMLEncoder {

    public static String encode(String text) {
        StringBuffer textbuf = new StringBuffer(2*text.length());
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '<':  textbuf.append("&lt;");   break;
                case '>':  textbuf.append("&gt;");   break;
                case '&':  textbuf.append("&amp;");  break;
                case '"':  textbuf.append("&quot;"); break;
                case '\'': textbuf.append("&apos;"); break;
                default:   textbuf.append(text.charAt(i));
            }
        }
        return textbuf.toString();
    }

    public static String encodeHTML(String text) {
        StringBuffer textbuf = new StringBuffer(2*text.length());
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '"':  textbuf.append("&quot;"); break;
                default:   textbuf.append(text.charAt(i));
            }
        }
        return textbuf.toString();
    }
}

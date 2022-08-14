package com.splwg.selfservice;

import java.util.Properties;

import javax.servlet.http.HttpSession;

public class MessageHelper {
    private HttpSession session;
    private Properties properties;

    public MessageHelper(HttpSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
    }

    public String getMessage(int nbr) {
        MessageBean msg;
        if (session.getAttribute("Messages") != null) {
            msg = (MessageBean) session.getAttribute("Messages");
        } else {
            msg = new MessageBean(properties);
            session.setAttribute("Messages", msg);
        }
        return msg.getMessage(nbr);
    }

}

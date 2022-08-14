/*
 * EmailBean.java
 *
 * Created on January 21, 2003 by A. Rave
 * Part of CorDaptix Web Self Service
 * This class sends an email
 */

package com.splwg.selfservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

/**
 *
 */

public class EmailBean
        implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------------------------------------

    private Properties properties;
    private String errorMsg;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new EmailBean with properties loaded */
    public EmailBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getErrorMessage() {
        return errorMsg;
    }

    /*
     *  Input: Recipient email address, the SelfService user and password
     *  Read from the properties file: From Address, Subject, SMTP host, and Email File location (where text is stored)
     */

    public boolean postMail(String recipient, String user, String password, String source, ServletContext context,
            String personId) {
        boolean isEmailOk = true;

        //check if all required properties are present
        boolean isSendEmail = properties.getProperty("com.splwg.selfservice.SendEmail").equalsIgnoreCase("true") ? true
                : false;
        if (!isSendEmail) errorMsg = "Email sending has been disabled.";
        String serverHost = properties.getProperty("com.splwg.selfservice.SMTPHost");
        System.out.println("server host[" + serverHost + "]");
        if (serverHost.trim().equals("")) {
            errorMsg = ("com.splwg.selfservice.SMTPHost property missing.");
            isEmailOk = false;
        }
        String subject = "";
        String fileLocation = "";
        String from = "";
        if (source.equals("Registration")) {
            subject = properties.getProperty("com.splwg.selfservice.RegistrationEmailSubject");
            fileLocation = properties.getProperty("com.splwg.selfservice.RegistrationEmailFileLocation");
        } else {
            subject = properties.getProperty("com.splwg.selfservice.EmailSubject");
            fileLocation = properties.getProperty("com.splwg.selfservice.EmailFileLocation");
        }
        if (subject.length() == 0) {
            errorMsg = "com.splwg.selfservice.RegistrationEmailSubject property missing.";
            isEmailOk = false;
        }
        if (fileLocation.length() == 0) {
            errorMsg = "com.splwg.selfservice.RegistrationEmailFileLocation property missing.";
            isEmailOk = false;
        }

        from = properties.getProperty("com.splwg.selfservice.FromAddress");
        if (from.length() == 0) {
            errorMsg = "com.splwg.selfservice.FromAddress property missing.";
            isEmailOk = false;
        }

        if (isSendEmail && isEmailOk) {
            //Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", serverHost);
            System.out.println("server host[" + serverHost + "]");
		System.out.println("SMTP Host [" + props.get("mail.smtp.host") + "]");
            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);
		System.out.println("SMTP Host [" + props.get("mail.smtp.host") + "]");		
            session.setDebug(true);

            try {
		    Transport trans = null;
                String message = "";
                Message msg = new MimeMessage(session);
                InternetAddress addressFrom = new InternetAddress(from);
                msg.setFrom(addressFrom);
                InternetAddress addressTo = new InternetAddress(recipient);
                msg.setRecipient(Message.RecipientType.TO, addressTo);
                java.io.InputStream inputStream = context.getResourceAsStream(fileLocation);
                message = readStream(inputStream);
                int ind = message.indexOf("@user@");
                message = message.substring(0, ind).concat(user).concat(message.substring(ind + 6));
                ind = message.indexOf("@password@");
                message = message.substring(0, ind).concat(password).concat(message.substring(ind + 10));
                // Optional : You can also set your custom headers in the Email if you Want
                //      msg.addHeader("MyHeaderName", "myHeaderValue");

                msg.setSubject(subject);
                msg.setContent(message, "text/plain");
		    System.out.println("SMTP Host [" + props.get("mail.smtp.host") + "]");
                trans.send(msg);
            } catch (IOException ioe) {
                errorMsg = "The file with the email text at location " + fileLocation + " could not be  read.";
                addToDoEntry(personId);
                System.out.println("Email could not be sent due to error: " + errorMsg);
                isEmailOk = false;
            } catch (javax.mail.MessagingException me) {
		    me.printStackTrace();
                errorMsg = "Mail Server could not be found.";
                addToDoEntry(personId);
                System.out.println("Email could not be sent due to error: " + errorMsg);
                isEmailOk = false;
            } catch (Exception e) {
                errorMsg = e.toString();
                errorMsg = errorMsg.replace('\012', ' '); // replace new line by blank
                errorMsg = errorMsg.replace('\047', '\042'); // replace ' by "
                if (errorMsg.length() > 212) {
                    errorMsg = errorMsg.substring(0, 212);
                }
                addToDoEntry(personId);
                System.out.println("Email could not be sent due to error: " + errorMsg);
                isEmailOk = false;
            }

        }
        return isEmailOk & isSendEmail;
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuffer stringBuff = null;

        InputStreamReader streamReader;
        char[] charBuffer = new char[2000]; // Arbitrary number
        int readCount;
        stringBuff = new StringBuffer();
        streamReader = new InputStreamReader(inputStream);

        readCount = streamReader.read(charBuffer);
        while (readCount != -1) {
            stringBuff.append(charBuffer, 0, readCount);
            readCount = streamReader.read(charBuffer);
        }
        return stringBuff.toString();
    }

    private void addToDoEntry(String personId) {
        String toDoType = properties.getProperty("com.splwg.selfservice.AdminToDoType");
        if (!toDoType.equalsIgnoreCase("NONE") && !toDoType.equals("")) {
            ToDoBean td = new ToDoBean();
            td.setToDoType(toDoType);
            td.setDrillKey(personId);
            td.setProperties(properties);
            td.setSource("ADMIN");
            if (errorMsg == null)
                errorMsg = "";
            else
                errorMsg = XMLEncoder.encode(errorMsg);
            td.setComments("Email could not be sent due to error: " + errorMsg);
            td.addToDoEntry();
        }
    }
}

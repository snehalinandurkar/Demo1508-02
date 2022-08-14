/*
 * CMEmailBean.java
 *
 * Created on Jan 19, 2012, by Edrik Pagaduan
 * CEd SR-0511
 * Extends base email bean to handle EBill email sending
 *
 *********************
 *
 * 2012-08-01  SMedin  CEd10. Add Email Set-up for Auto Pay.
 * 2012-08-11  EPagad  CEd10. Add Email Set-up for One-Time Payment.
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

public class CMEmailBean
		implements java.io.Serializable {

    //~ Additional instance fields --------------------------------------------------------------------------------------

    //private 
	private Properties properties;
    private String errorMsg;
    

    //~ Constructors -----------------------------------------------------------------------------------------

    ///** Creates new CMEmailBean with properties loaded */
    public CMEmailBean(Properties value) {
        properties = value;
    }

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getErrorMessage() {
        return errorMsg;
    }

    /*
     *  Input: Recipient email address, the Entity Name of the Person, the type of email to be sent, person id
     *  Read from the properties file: From Address, Subject, SMTP host, and Email File location (where text is stored)
     */
    public boolean postEBillMail(String recipient, String user, String source, ServletContext context, String personId) {
        boolean debug = true;
        String message = "";
        String fileLoc = "";
        String subject = "";

        String prop = properties.getProperty("com.splwg.selfservice.SendEmail");
        System.out.println("SendEmail[" + properties.getProperty("com.splwg.selfservice.SendEmail")+"]");
        prop = prop.trim();

        if (prop.equals("false") || prop.equals(null)) {
            return (true); // for demonstration purposes, when we can't send a mail
        }

        if (source.equals("REGEBILL")) {
		subject = properties.getProperty("com.splwg.selfservice.EBillEnrolmentEmailSubject");
       		fileLoc = properties.getProperty("com.splwg.selfservice.EBillEnrolEmailFileLocation");
        } else {
        	if (source.equals("UNREBILL")){
        		System.out.println("UNREBILL");
           		subject = properties.getProperty("com.splwg.selfservice.EBillUnenrolmentEmailSubject");
           		System.out.println("Subject["+subject+"]");
           		fileLoc = properties.getProperty("com.splwg.selfservice.EBillUnenrolEmailFileLocation");
        			
        	} else {
        		if (source.equals("ACCTLOCK")){
        			System.out.println("ACCTLOCK");
				subject = properties.getProperty("com.splwg.selfservice.EmailLockSubject");
       				fileLoc = properties.getProperty("com.splwg.selfservice.EmailLockFileLocation");
			}
                }
        }
        
	  String from=""; 
     	  from = properties.getProperty("com.splwg.selfservice.EBillFromAddress");

        //Set the host smtp address

        Properties props = new Properties();
        props.put("mail.smtp.host", properties.getProperty("com.splwg.selfservice.SMTPHost"));

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

       
        try {
            // create a message
            Message msg = new MimeMessage(session);

            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(recipient);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            try {

                java.io.InputStream inputStream = context.getResourceAsStream(fileLoc);
                message = readStream(inputStream);

            } catch (Exception e) {

                errorMsg = "The file with the email text at location " + fileLoc
                           + " could not be read.";
                addToDoEntry(personId);
                System.out.println(errorMsg);
            }

            message = message.replaceAll("@acct@", user);

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
            System.out.println("Email Sent Successfully");

        } catch (Exception e) {
            errorMsg = e.toString();
            errorMsg = errorMsg.replace('\012', ' '); // replace new line by blank
            errorMsg = errorMsg.replace('\047', '\042'); // replace ' by "
            int len = errorMsg.length();
            if (len > 212) {
                errorMsg = errorMsg.substring(0, 212);
            }
            addToDoEntry(personId);
            System.out.println("Email could not be sent due to error: " + errorMsg);
            return (false);
        }
        return (true);

    }
    
    /*
     * 2012-08-01 - CEd10 - SMedin - Start Add
     */
	public boolean postAutoPayMail(String recipient, String type, ServletContext context, String personId, String accountId, String customerName) {
        
        boolean isEmailOk = true;
        
        //check if all required properties are present
        boolean isSendEmail = properties.getProperty("com.splwg.selfservice.SendEmail").equalsIgnoreCase("true") ? true
                : false;
        if (!isSendEmail) errorMsg = "Email sending has been disabled.";
        String serverHost = properties.getProperty("com.splwg.selfservice.SMTPHost");

        if (serverHost.trim().equals("")) {
            errorMsg = ("com.splwg.selfservice.SMTPHost property missing.");
            isEmailOk = false;
        }
        
        String subject = "";
        String fileLocation = "";
        String from = "";
              
        if (type.equals("ENROLL")) {
			subject = properties.getProperty("com.splwg.selfservice.AutoPayEnrollEmailSubject");
      		fileLocation = properties.getProperty("com.splwg.selfservice.AutoPayEnrollEmailFileLocation");
        } else {
        	if (type.equals("UPDATE")){   
           		subject = properties.getProperty("com.splwg.selfservice.AutoPayUpdateEmailSubject");
           		fileLocation = properties.getProperty("com.splwg.selfservice.AutoPayUpdateEmailFileLocation");        			
        	}
        }
        
        if (subject.length() == 0) {
            errorMsg = "Auto Pay Email Subjet property missing.";
            isEmailOk = false;
        }
        if (fileLocation.length() == 0) {
            errorMsg = "Auto Pay Email File Location property missing.";
            isEmailOk = false;
        }

        from = properties.getProperty("com.splwg.selfservice.AutoPayFromAddress");
        if (from.length() == 0) {
            errorMsg = "Auto Pay Email From Address property missing.";
            isEmailOk = false;
        }

        if (isSendEmail && isEmailOk) {
            //Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", serverHost);

            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);	
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
                if (type.equals("ENROLL")) {
                	int ind = message.indexOf("@acct@");
                	message = message.substring(0, ind).concat(accountId).concat(message.substring(ind + 6));           	
				}

                msg.setSubject(subject);
                msg.setContent(message, "text/plain");
                trans.send(msg);
            } catch (IOException ioe) {
                errorMsg = "The file with the email text at location " + fileLocation + " could not be  read.";
                addToDoEntry(personId);
                isEmailOk = false;
            } catch (javax.mail.MessagingException me) {
		    	me.printStackTrace();
                errorMsg = "Mail Server could not be found.";
                addToDoEntry(personId);
                isEmailOk = false;
            } catch (Exception e) {
                errorMsg = e.toString();
                errorMsg = errorMsg.replace('\012', ' '); // replace new line by blank
                errorMsg = errorMsg.replace('\047', '\042'); // replace ' by "
                if (errorMsg.length() > 212) {
                    errorMsg = errorMsg.substring(0, 212);
                }
                addToDoEntry(personId);
                isEmailOk = false;
            }
        }
        return isEmailOk & isSendEmail;
    }
    /*
     * 2012-08-01 - CEd10 - SMedin - End Add
     */


    /*
     * 2012-08-11 - CEd10 - SMedin - Start Add
     */
	public boolean postPaymentMail(String recipient, String type, ServletContext context, String personId, String accountId, String customerName) {
        
        boolean isEmailOk = true;
        
        //check if all required properties are present
        boolean isSendEmail = properties.getProperty("com.splwg.selfservice.SendEmail").equalsIgnoreCase("true") ? true
                : false;
        if (!isSendEmail) errorMsg = "Email sending has been disabled.";
        String serverHost = properties.getProperty("com.splwg.selfservice.SMTPHost");

        if (serverHost.trim().equals("")) {
            errorMsg = ("com.splwg.selfservice.SMTPHost property missing.");
            isEmailOk = false;
        }
        
        String subject = "";
        String fileLocation = "";
        String from = "";
              
        if (type.equals("PAYMENT")) {
			subject = properties.getProperty("com.splwg.selfservice.PaymentEmailSubject");
      		fileLocation = properties.getProperty("com.splwg.selfservice.PaymentEmailFileLocation");
        }
        
        if (subject.length() == 0) {
            errorMsg = "Payment Email Subject property missing.";
            isEmailOk = false;
        }
        if (fileLocation.length() == 0) {
            errorMsg = "Payment File Location property missing.";
            isEmailOk = false;
        }

        from = properties.getProperty("com.splwg.selfservice.PaymentFromAddress");
        if (from.length() == 0) {
            errorMsg = "Payment Email From Address property missing.";
            isEmailOk = false;
        }

        if (isSendEmail && isEmailOk) {
            //Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", serverHost);

            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);	
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
                if (type.equals("PAYMENT")) {
                	int ind = message.indexOf("@acct@");
                	message = message.substring(0, ind).concat(accountId).concat(message.substring(ind + 6));           	
				}

                msg.setSubject(subject);
                msg.setContent(message, "text/plain");
                trans.send(msg);
            } catch (IOException ioe) {
                errorMsg = "The file with the email text at location " + fileLocation + " could not be  read.";
                addToDoEntry(personId);
                isEmailOk = false;
            } catch (javax.mail.MessagingException me) {
		    	me.printStackTrace();
                errorMsg = "Mail Server could not be found.";
                addToDoEntry(personId);
                isEmailOk = false;
            } catch (Exception e) {
                errorMsg = e.toString();
                errorMsg = errorMsg.replace('\012', ' '); // replace new line by blank
                errorMsg = errorMsg.replace('\047', '\042'); // replace ' by "
                if (errorMsg.length() > 212) {
                    errorMsg = errorMsg.substring(0, 212);
                }
                addToDoEntry(personId);
                isEmailOk = false;
            }
        }
        return isEmailOk & isSendEmail;
    }
    /*
     * 2012-08-11 - CEd10 - EPagad - End Add
     */


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

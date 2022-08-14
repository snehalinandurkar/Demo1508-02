package com.splwg.selfservice;

import java.io.File;
import java.io.FileWriter;

public class test {

	public static void main(String[] args) {
		// String databasePassword = "21224E4D244348524E4D21";
		String databasePassword = "21522A17134240004D21";
                /// String databasePassword = "213652525248531221";
		String passwordPlain = "";
                String billId = "425209569705";
                String billIdTxtFile = "BILL_ID.txt";
                FileWriter fileWriter = null;

               EncryptionBean encrypt = new EncryptionBean();
              try {
                    encrypt.setEncryptedInput(databasePassword);
                    passwordPlain = encrypt.decryptPassword();

                    fileWriter = new FileWriter(billIdTxtFile);
                    fileWriter.write(billId);
                    fileWriter.close();

                } catch (Exception e) {
                    String errorMessage = "Caught exception: " + e.getMessage();
                        System.out.println(errorMessage);
                    return;
                }
		System.out.println("password:" + passwordPlain);
	}
}

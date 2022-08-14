/*
 * ServicePointBean.java
 *
 * Created on February 26, 2002
 */
package com.splwg.selfservice;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 *
 */
public class EncryptionMichel
    implements java.io.Serializable {

    //~ Methods ----------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        // Install SunJCE provider
        Provider sunJce = new com.sun.crypto.provider.SunJCE();
        Security.addProvider(sunJce);

        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Salt
        byte[] salt = {
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee,
            (byte) 0x99
        };

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);

        // Prompt user for encryption password.
        // Collect user password as char array (using the
        // "readPasswd" method from above), and convert
        // it into a SecretKey object, using a PBE key
        // factory.
        System.out.print("Enter text for encryption:  ");
        System.out.flush();
        char[] str = new char[10];
        str[0] = 'c';
        str[1] = 'o';
        str[2] = 'r';
        str[3] = 'd';
        str[4] = 'a';
        str[5] = 'p';
        str[6] = 't';
        str[7] = 'i';
        str[8] = 'x';
        str[9] = '1';

//    pbeKeySpec = new PBEKeySpec(readPasswd(System.in));
        pbeKeySpec = new PBEKeySpec(str);
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Our cleartext
//    byte[] cleartext = "cordaptix".getBytes();

        // Encrypt the cleartext
        byte[] cleartext = (byte[]) String.valueOf("anatarie").getBytes();
        byte[] ciphertext = pbeCipher.doFinal(cleartext);

        ByteArrayInputStream buf = new ByteArrayInputStream(ciphertext);
        java.io.InputStreamReader read = new InputStreamReader(buf);
        char[] st = new char[70];

        read.read(st, 0, 70);
        System.out.println("Encrypted: " + String.valueOf(st));

        //DECYPHER

        //System.out.println("cyphered=" + String.valueOf(ciphertext));
        
        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        byte[] cleartext1 = pbeCipher.doFinal(ciphertext);

        buf = new ByteArrayInputStream(cleartext1);
        read = new InputStreamReader(buf);
        st = new char[70];
        read.read(st, 0, 70);
        System.out.println("Decrypted: " + String.valueOf(st));
    }
}

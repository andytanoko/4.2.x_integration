/**
 *
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityServiceBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 06-May -2002    Jagadeesh           Created.
 * 20-JUNE-2002    Jagadeesh           Added TestCases for SMIME
 * 08 July 2009    Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.security.ejb;


import java.io.File;
import java.security.PrivateKey;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceHome;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceObj;
import com.gridnode.pdip.base.security.helpers.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
//import com.rsa.jsafe.JSAFE_PrivateKey;

public class SecurityServiceBeanTest extends TestCase
{
  ISecurityServiceHome _home;
  ISecurityServiceObj _remote;
  SMIMESecurityServiceFacade _facade;
  private static final String ENCRYPT = "Encrypt";
  private static final String DECRYPT = "Decrypt";
  private static final String SMIME_ENCRYPT = "SMIMEEncrypt";
  private static final String SMIME_DECRYPT = "SMIMEDecrypt";
  private static final String SMIME_SIGN = "SMIMESign";
  private static final String SMIME_VERIFY = "SMIMEVerify";
  private static byte[] encryptedData = null;
  public static byte[] signedData = null;

  private static final String PP_CER = "data" + File.separator + "pp.cer";
  private static final String MTES_P12 = "data" + File.separator + "mtes.p12";
  private static final String MYDOC_TXT = "data" + File.separator + "mydoc.txt";
  private static final String ENCYPT_TXT = "data" + File.separator + "encypt.txt";
  private static final String MTES_CER = "data" + File.separator + "mtes.cer";
  private static final String PP12_P12 = "data" + File.separator + "pp12.p12";
  private static final String DECRYPTF_TXT = "data" + File.separator + "decryptf.txt";

  public SecurityServiceBeanTest(String name)
  {
    super(name);
  }

  public static Test suit()
  {
    return new TestSuite(SecurityServiceBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    SecurityLogger.log("TEST SecurityServiecs Setup");
    _facade = new SMIMESecurityServiceFacade();

    _home = (ISecurityServiceHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             ISecurityServiceHome.class);

    assertNotNull("Home Shld Not be null", _home);

    _remote = _home.create();
    assertNotNull("Remot Shld Not be null", _remote);

    SecurityLogger.log("Test SecurityServices Successful.");
  }

  /**
   * This method crates test value Object.
   * @param testString - String identifier - to Encrypt / Decrypt.
   * @return SecurityInfo - Value Object returend containing Data.
   */


  public SecurityInfo createTestInfo(String testString)
  {
    SecurityInfo info;
    try
    {
      if(testString.equals(SecurityServiceBeanTest.ENCRYPT))
      {
        info = new SecurityInfo();
        info.setReceipentCertificateFile(PP_CER);
        PKCS12Reader reader = new PKCS12Reader(MTES_P12,"xx".toCharArray());
        reader.read();
        PrivateKey prKey = reader.getPrivateKey();
        if(prKey != null)
        {
            byte a[] = GridCertUtilities.writePKCS8PrivateKeyData("pw",prKey);

            if(a != null)
            {
              info.setOwnPrivateKey(a);
            }
            else
            {
               SecurityLogger.log("from reading byte array is null ");
            }
        }
        else
        {
          SecurityLogger.log("Private Key Cannot Be Loaded .. ");
        }
        info.setPassword("pw".toCharArray());
        info.setFileToEncrypt(MYDOC_TXT);
        info.setOutputFile(ENCYPT_TXT);
        info.setSign(true);
        return info;
      }
      else if(testString.equals(SecurityServiceBeanTest.DECRYPT))
      {
        info = new SecurityInfo();
        info.setSenderCertificateFile(MTES_CER);
        PKCS12Reader reader = new PKCS12Reader(PP12_P12,"xx".toCharArray());
        reader.read();
        PrivateKey prKey = reader.getPrivateKey();
        if(prKey != null)
        {
            byte a[] = GridCertUtilities.writePKCS8PrivateKeyData("pw",prKey);
            if(a != null)
            {
              info.setOwnPrivateKey(a);
            }
            else
            {
               SecurityLogger.log("from reading byte array is null ");
            }
        }
        else
        {
          SecurityLogger.log("Private Key Cannot Be Loaded .. ");
        }
        info.setPassword("pw".toCharArray());
        info.setFileToDecrypt(ENCYPT_TXT);
        info.setOutputFile(DECRYPTF_TXT);
        SecurityLogger.log("Set All Parameters");
        return info;
      }

    }
    catch(Exception ex)
    {
      SecurityLogger.log("Exception in creating SecurityInfo "+ex.getMessage());
    }
    return null;
  }

/*
  public SMimeSecurityInfo createSMIMETestInfo(String testString)
  {
    SMimeSecurityInfo secinfo = null;
    try
    {
        if(testString.equals(SecurityServiceBeanTest.SMIME_ENCRYPT))
        {
            secinfo = new SMimeSecurityInfo();
            X509Certificate cert = GridCertUtilities.loadX509Certificate(MTES_CER);
            PKCS12Reader reader = new PKCS12Reader(PP12_P12,"xx".toCharArray());
            reader.read();
            JSAFE_PrivateKey prKey = reader.getPrivateKey();
            String content = "This is the message to do ...";
            secinfo.setDataToEncrypt(content.getBytes());
            secinfo.setReceipentCertificate(cert);
            secinfo.setPrivateKey(prKey);
            return secinfo;
        }
        else if(testString.equals(SecurityServiceBeanTest.SMIME_DECRYPT))
        {
             secinfo = new SMimeSecurityInfo();
             X509Certificate cert1 = GridCertUtilities.loadX509Certificate(MTES_CER);
             PKCS12Reader reader = new PKCS12Reader(MTES_P12,"xx".toCharArray());
             reader.read();
             JSAFE_PrivateKey prKey = reader.getPrivateKey();
             secinfo.setReceipentCertificate(cert1);
             secinfo.setPrivateKey(prKey);
             assertNotNull("Encrypted Data cannot be null ",encryptedData);
             secinfo.setDataToDecrypt(encryptedData);
             return secinfo;
        }
        else if(testString.equals(SecurityServiceBeanTest.SMIME_SIGN))
        {
             secinfo = new SMimeSecurityInfo();
             String content = "This is the message to do ...";
             X509Certificate cert1 = GridCertUtilities.loadX509Certificate(MTES_CER);
             PKCS12Reader reader = new PKCS12Reader(MTES_P12,"xx".toCharArray());
             reader.read();
             JSAFE_PrivateKey prKey = reader.getPrivateKey();
             secinfo.setReceipentCertificate(cert1);

             secinfo.setPrivateKey(prKey);
             //GNBodypart part1 = new GNBodypart();
             //part1.setContent(content,"text/plain");
             //secinfo.setIMailpartToSign(part1);
             //SecurityLogger.log("Main Content  "+new String(part1.getContentByte(false)));
             secinfo.setDataToSign(content.getBytes());
             return secinfo;
        }
        else if(testString.equals(SecurityServiceBeanTest.SMIME_VERIFY))
        {
             secinfo = new SMimeSecurityInfo();
             X509Certificate cert1 = GridCertUtilities.loadX509Certificate(MTES_CER);
             PKCS12Reader reader = new PKCS12Reader(PP12_P12,"xx".toCharArray());
             reader.read();
             JSAFE_PrivateKey prKey = reader.getPrivateKey();
             byte[] prvkey = GridCertUtilities.writePKCS8PrivateKeyData("xx",prKey);
             secinfo.setReceipentCertificate(cert1);
             secinfo.setPrivateKey(prKey);
             GNMimepart part = new GNMimepart();
             String content = "This is the message to do ...";
             GNBodypart part1 = new GNBodypart();
             part1.setContent(content,"text/plain");
             GNMimepart mmp = new GNMimepart();
             mmp.setParameter("type", "multipart/*");
             mmp.addPart(part1);
             GNBodypart partx = new GNBodypart();
             partx.setContent(mmp);
             GNBodypart part2 = new GNBodypart(SecurityServiceBeanTest.signedData,"application/pkcs7-signature");
             part.addPart(partx);
             part.addPart(part2);
             secinfo.setPartToVerify(part);
             return secinfo;
        }




    }
    catch(Exception ex)
    {
       SecurityLogger.log("Cannot Create SMIME Security Info ");
    }
    return secinfo;
  }



*/


  /********** Test encrypt and sign ***********/


/*
  public void testencryptAndSign()
  {
    try
    {

        assertNotNull("Home Shld Not be null", _home);

        SecurityInfo info = createTestInfo(SecurityServiceBeanTest.ENCRYPT);
        if(_remote == null)
        {
            SecurityLogger.log("Remote is null");
        }
        else
        {
            SecurityLogger.log("Remote is Not .. null");
        }
        SecurityLogger.log(info.getPassword().toString());
        assertNotNull("shld not be null",info);

        System.out.println("Calling sign");
        SecurityInfo iinfo = _remote.encryptAndSign(info);
        assertNotNull("Encrypted String ",iinfo.getEncryptedOutputFile());
        SecurityLogger.log("Encrypted String "+iinfo.getEncryptedOutputByte().toString());
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        fail(ex.getMessage() + " Copy data directory to <jboss_home>/bin directory");
        SecurityLogger.log("Cannot Perform TestEncryptAndSign "+ex.getMessage());
    }

  }

*/
/***************** Test Decrypt and Verify **********************/

/*

 public void testdecryptAndVerify()
  {
    try
    {
        SecurityInfo info = createTestInfo(SecurityServiceBeanTest.DECRYPT);
        assertNotNull("Info Object Cannot Be Null ",info);
        SecurityLogger.log(info.getPassword().toString());
        SecurityInfo iinfo = _remote.decryptAndVerify(info);
        assertNotNull("Encrypted File Cannot be Null ",iinfo.getDecryptedOutputFile());
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        fail(ex.getMessage() + " Copy data directory to <jboss_home>/bin directory");
        SecurityLogger.log("Cannot Perform DecryptAndVerify "+ex.getMessage());
    }

  }


  public void testSMIMEEncrypt()
  {
    try
    {
      SMimeSecurityInfo info = createSMIMETestInfo(SecurityServiceBeanTest.SMIME_ENCRYPT);
      assertNotNull("Info Object Cannot be Null ",info);

      SMimeSecurityInfo iinfo = _facade.encrypt(info);
      //      SMimeSecurityInfo iinfo = _remote.encrypt(info);
      assertNotNull("Encrypted Byte Array Cannot be Null ",iinfo.getEncryptedData());
      SecurityLogger.log("Encrypted String is  "+new String(iinfo.getEncryptedData()));
      SecurityServiceBeanTest.encryptedData = iinfo.getEncryptedData();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.log("Cannot Perform SMIME Encrypt "+ex.getMessage());
    }
  }

  public void testSMIMEDecrypt()
  {
    try
    {
      SMimeSecurityInfo info = createSMIMETestInfo(SecurityServiceBeanTest.SMIME_DECRYPT);
      assertNotNull("Info Object Cannot be Null ",info);
      SMimeSecurityInfo iinfo = _facade.decrypt(info);
      assertNotNull("Decrypted Byte Array Cannot be Null ",iinfo.getDecryptedData());
      SecurityLogger.log("Decrypted String is  "+new String(iinfo.getDecryptedData()));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.log("Cannot Perform SMIME Encrypt "+ex.getMessage());
    }
  }

  public void testSMIMESign()
  {

    try
    {
      SMimeSecurityInfo info = createSMIMETestInfo(SecurityServiceBeanTest.SMIME_SIGN);
      assertNotNull("Info Object Cannot be Null ",info);
      SMimeSecurityInfo iinfo = _facade.sign(info);
      assertNotNull("Signed Byte Array Cannot be Null ",iinfo.getSignedData());
      SecurityLogger.log("Signed String is  "+new String(iinfo.getSignedData()));
      SecurityServiceBeanTest.signedData = iinfo.getSignedData();
      SecurityLogger.log("!!!!  "+new String(SecurityServiceBeanTest.signedData));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.log("Cannot Perform SMIME Sign "+ex.getMessage());
    }

  }

/*  public void testSMIMEVerify()
  {
    try
    {
        SMimeSecurityInfo info = createSMIMETestInfo(SecurityServiceBeanTest.SMIME_VERIFY);
        assertNotNull("Info Object Cannot be Null ",info);
        SMimeSecurityInfo iinfo = _facade.verify(info);
        assertNotNull("Verified Part Cannot Be Null ",iinfo);
        SecurityLogger.log("Verified the Content "+new String(iinfo.getVerifiedPart().getContentByte(false)));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.log("Cannot Perform SMIME Verify "+ex.getMessage());
    }

  }
*/


  public static void main(String args[])
  {
      junit.textui.TestRunner.run(suit());
  }


}
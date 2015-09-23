/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateManagerBeanTest
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * JUL 03 2002    Jagadeesh        Created
 * Aug 07 2006    Tam Wei Xiang    Amend the way we access SecurityDB
 * Jul 23 2009    Tam Wei Xiang    #560 - Commented JSafe/Cert J related code
 */



package com.gridnode.pdip.base.certificate.ejb;

import java.io.File;
import java.security.cert.X509Certificate;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.CertificateLogger;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.helpers.SecurityDB;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBManager;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
//import com.rsa.certj.CertJ;
//import com.rsa.certj.Provider;
//import com.rsa.certj.cert.Certificate;

public class CertificateManagerBeanTest extends TestCase
{

  ICertificateManagerHome _home=null;
  ICertificateManagerObj _obj = null;

  /**
   * String GRIDNODE_DB
   */
  public static final String GRIDNODE_DB = "GridNodeSecurityDB";

  /**
   * MemoryDB memDB
   */
  //private MemoryDB memDB;

  /**
   * DatabaseService dbService
   */
  //private DatabaseService dbService;

  //private SecurityDB.SecurityDBImplementation implementation;
  /**
   * CertJ certJ
   */
//  private CertJ certJ;
//
//  private SecurityDB securityDB;

  private X509Certificate certificate;


  public CertificateManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CertificateManagerBeanTest.class);
  }

  private void lookup()  throws Exception
  {
    _home =(ICertificateManagerHome) ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             ICertificateManagerHome.class);
    _obj =  _home.create();
  }
  protected void setUp()
  {
    try
    {
      CertificateLogger.log("Setup Begin ");
      lookup();

//      certJ = new CertJ();
//      initDB();
//      implementation =
//          (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
      createTestData();
      //assertNotNull("Home is null", implementation);
    }
    catch(Exception ex)
    {
      CertificateLogger.warn("Exception in SetUp "+ex.getMessage(),ex);
      ex.printStackTrace();
    }
    finally
    {
      CertificateLogger.log("[CertificateManagerBeanTest.setUp] Exit");
    }
  }

//  private void initDB()
//  {
//  	SecurityDB secDB = null;
//   try
//   {
//  	  secDB = SecurityDBManager.getInstance().getSecurityDB();
//       Provider[] providers = secDB.createProviders(GRIDNODE_DB);
//       securityDB = (SecurityDB)providers[0];
//      for (int i = 0; i < providers.length; i++)
//      {
//        certJ.addProvider(providers[i]);
//      }
//
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("Could Not Create Providers ",ex);
//    }
//    finally
//    {
//    	if(secDB != null)
//    	{
//    		SecurityDBManager.getInstance().releaseSecurityDB(secDB);
//    	}
//    }
//  }

  public static Test suit()
  {
    return new TestSuite(CertificateManagerBeanTest.class);
  }

  public static void main(String args[])
  {
      junit.textui.TestRunner.run(suit());
  }

  private void createTestData() throws Exception
  {
     certificate =  GridCertUtilities.loadX509Certificate("data" + File.separator + "pp.cer");

//     certificate =  GridCertUtilities.loadX509Certificate("data" + File.separator + "mtes.cer");
  }
/*
   public void testgetCertificate() throws Exception
   {
//      _obj.insertCertificate("d:/security/data/mtes.cer");
//        com.gridnode.pdip.base.certificate.model.Certificate
        //cert = _obj.findCertificateByUID(new Long(25));
//      _obj.getCertificate(new Long(25));
    //    _obj.importCertificate("testCert1","D:/bchatjava/certj10/certs/ca/PCA1ssv4.cer",new Integer(50));
//        _obj.importCertificate("p12Cert","d:/security/data/pp12.p12",new Integer(10),"xx");
       //X509Certificate cert = _obj.exportCertificate(10,"p12Cert");
       Vector ovec= _obj.displayX500Names(certificate);
       System.out.println("Display Value  "+ovec.get(0));
       System.out.println("Display Value  "+ovec.get(1));

//      String privateKey = cert.getPrivateKey();
//      JSAFE_PrivateKey pKey = GridCertUtilities.loadPKCS8PrivateKeyData(
//      "xx".toCharArray(),GridCertUtilities.decode(privateKey));
//      CertificateLogger.debug(privateKey);
//      CertificateLogger.debug(pKey.getAlgorithm());
//      String certificate = cert.getCertificate();
//      byte[] certificateData = new byte[certificate.getDERLen (0)];
//      certificate.getDEREncoding (certificateData, 0, 0);
//      return certificateData;

//      X509Certificate c = GridCertUtilities.loadX509Certificate(GridCertUtilities.decode(certificate));
//      implementation.insertCertificate(c);
      //      new X509Certificate(GridCertUtilities.decode(cert.getCertificate()),0,0);
//      X509Certificate ce = GridCertUtilities.loadX509Certificate(GridCertUtilities.decode(cert.getCertificate()));

      //      X509Certificate ce = new X509Certificate(GridCertUtilities.l ,0,0);
   }

*/
//public void testInsertCertificate()
//  {
//    try
//    {
//        implementation.insertCertificate(certificate);
//        CertificateLogger.log("Certificate Inserted ");
//    }
//    catch(Exception ex)
//    {
//      fail("Exception in insert Certificate " + ex.getMessage());
//    }
//
//  }

//  public void testDeleteCertificate()
//  {
//    try
//    {
//        implementation.insertCertificate(certificate);
//        implementation.deleteCertificate(0,"gridnode");
//    }
//    catch(Exception ex)
//    {
//        CertificateLogger.warn("testDeleteCertificate : Cannot Delete Certificate ");
//        ex.printStackTrace();
//    }
//
//  }

//  public void testDeletePrivateKeyByIDName()
//  {
//    try
//    {
//  implementation.insertCertificate(certificate);
//  implementation.insertIdAndNameByCertificate(0,"gridnode",certificate);
//      implementation.deletePrivateKeyByIdAndName(0,"gridnode");
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("DeletePrivateKeyByIDName : Cannot Delete By Naames ",ex);
//    }
//
//  }

//  public void testDeleteByNameAndSerialNo()
//  {
//    try
//    {
//        implementation.insertCertificate(certificate);
//        implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
//    }
//    catch(Exception ex)
//    {
//      fail("testDeleteByName : Cannot Delete By Naames " + ex);
//    }
//
//  }


//  public void testInserIDAndNameByCertificate()
//  {
//    try
//    {
//      implementation.insertCertificate(certificate);
//      implementation.insertIdAndNameByCertificate(0,"gridnode",certificate);
//      implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("testInserIDAndNameByCertificate : CannotInsert By Certificate",ex);
//    }
//
//  }


//  public void testSelectCertByIDAndName()
//  {
//    try
//    {
//      implementation.insertCertificate(certificate);
//      implementation.insertIdAndNameByCertificate(0,"gridnode",certificate);
//      Certificate cer =
//      implementation.selectCertificateByIdAndName(0,"gridnode");
//      CertificateLogger.log("Certificate  "+cer);
//      assertNotNull("Certificate Cannot be Null ",cer);
//      implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("testSelectCertByIDAndName : Cannot Select the Record ",ex);
//    }
//
//  }

//   public void testgetFirstCertificates()
//   {
//      try
//      {
//         implementation.insertCertificate(certificate);
//          com.rsa.certj.cert.Certificate cert = implementation.firstCertificate();
//          CertificateLogger.log("First Certificate  "+cert);
//          implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
//      }
//      catch(Exception ex)
//      {
//          CertificateLogger.warn("testInsertPrivateKeyByIDAndName : Cannot Insert Private Key ",ex);
//      }
//
//   }

/*
  //Testcases depending on security module: Must have base-security.jar in classpath

   public void testInsertPrivateKeyByCertificate()
   {
     try
     {
        implementation.insertCertificate(certificate);
        com.gridnode.pdip.base.security.helpers.PKCS12Reader reader =
        new com.gridnode.pdip.base.security.helpers.PKCS12Reader("data" + File.separator + "mtes.p12","xx".toCharArray());
        reader.read();
        implementation.insertPrivateKeyByCertificate(certificate,reader.getPrivateKey());
        CertificateLogger.log("Private Key Inserted Successfuly ");
        implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
     }
     catch(Exception ex)
     {
        CertificateLogger.err("testInsertPrivateKeyByCertificate : Cannot Insert Private Key ",ex);
     }
   }

   public void testSelectPrivateKeyByCertificate()
   {
     try
     {
      implementation.insertCertificate(certificate);
      com.gridnode.pdip.base.security.helpers.PKCS12Reader reader =
      new com.gridnode.pdip.base.security.helpers.PKCS12Reader("data" + File.separator + "mtes.p12","xx".toCharArray());
      reader.read();
      implementation.insertPrivateKeyByCertificate(certificate,reader.getPrivateKey());
      JSAFE_PrivateKey privateKey =   implementation.selectPrivateKeyByCertificate(certificate);
      assertNotNull("Private Key Cannot be null ",privateKey);
      CertificateLogger.log("Certifiate "+privateKey);
      implementation.deleteCertificate(certificate.getIssuerName(),certificate.getSerialNumber());
     }
     catch(Exception ex)
     {
      CertificateLogger.err("testSelectCertByIssuerAndSerialNo : Cannot Select the Record ",ex);
     }

   }

   public void testInsertPrivateKeyByIDAndName()
   {
     try
     {
         implementation.insertCertificate(certificate);
         com.gridnode.pdip.base.security.helpers.PKCS12Reader reader =
         new com.gridnode.pdip.base.security.helpers.PKCS12Reader("data" + File.separator + "mtes.p12","xx".toCharArray());
         reader.read();
         implementation.insertIdAndNameByCertificate(0,"gridnode",certificate);
         implementation.insertPrivateKeyByIdAndName(0,"gridnode",reader.getPrivateKey());
         CertificateLogger.log("Private Key Inserted Successfuly ");
     }
     catch(Exception ex)
     {
          CertificateLogger.err("testInsertPrivateKeyByIDAndName : Cannot Insert Private Key ",ex);
     }

   }
*/

/*  public void testRevokeCertificate() throws Exception
  {
    //_obj.revokeCertificateByUId(31);
    _obj.revokeCertificateByUId(new Long(31));
  }
*/
}
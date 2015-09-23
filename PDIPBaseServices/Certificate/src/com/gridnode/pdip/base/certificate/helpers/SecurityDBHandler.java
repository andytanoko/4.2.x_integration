/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityDBHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2002    Jagadeesh           Created
 * Sep 23 2002    Jagadeesh           Modified private constructor- initDB() being
 *                                    called from constructor - insted of checking
 *                                    the flag.
 * Feb 09 2007		Alain Ah Ming				Add error code to error log. 
 *                                    Replace no-code error logging with warning message
 * Jul 23 2009    Tam Wei Xiang       #560 - Commented out JSAFE, CertJ related method
 *                                    as no operational related class reference to it.
 */
package com.gridnode.pdip.base.certificate.helpers;

import com.gridnode.pdip.base.certificate.exceptions.CertificateException;

/**
 * This Class is used by other module's to perform basic operation's on
 * Certificate Entity.
 *
 *   1. InsertCertificate
 *   2. InsertPrivateKeyByCertificate.
 *   3. DelteCertificate.
 *
 * Please refer to Certificate Facade to perform Various other operation's on
 * Certificate Entity.
 * 
 * @author Jagadeesh
 * @since
 * @version GT 4.0 VAN
 */
public final class SecurityDBHandler
{

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

  //public static SecurityDB.SecurityDBImplementation implementation;
//  /**
//   * CertJ certJ
//   */
//  private CertJ certJ;
//
//  private SecurityDB securityDB;

  //private boolean isInitialized = false;

  private static SecurityDBHandler _self;



//  public void setPassword(String password)
//  {
//    securityDB.setPassword(password);
//  }

//  private SecurityDBHandler() throws CertificateException
//  {
//    try
//    {
//       CertificateLogger.log("In Constructor -- ");
//       synchronized(this)
//       {
//           certJ = new CertJ();
//           initDB();
//       }
//      if(implementation == null)
//      {
//        implementation =
//          (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//      }
//    }
//    catch(ProviderManagementException mex)
//    {
//      throw new CertificateException(mex);
//    }
//
//  }

//  private void initDB() throws CertificateException
//  {
//  	SecurityDB secDB = null;
//   try
//   {
//  	 secDB = SecurityDBManager.getInstance().getSecurityDB();
//       Provider[] providers = secDB.createProviders(GRIDNODE_DB);
//       securityDB = (SecurityDB)providers[0];
//      for (int i = 0; i < providers.length; i++)
//      {
//        certJ.addProvider(providers[i]);
//      }
////      isInitialized = true;
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("Could Not Create Providers ",ex);
//      throw new CertificateException("[SecurityDBHandler] "+"[initDB()] "+
//      "Could Not Create Provider",ex);
//    }
//    finally
//    {
//    	if(secDB != null)
//    	{
//    		SecurityDBManager.getInstance().releaseSecurityDB(secDB);
//    	}
//    }
//  }



  public static synchronized SecurityDBHandler getInstance()
  throws CertificateException
  {
    try
    {
       if(_self == null)
       {
         _self = new SecurityDBHandler();
       }
       CertificateLogger.log("[SecurityDBHandler] "+"[getInstance()]"+
       "SecurityDBHandler Created ");
       return _self;
    }
    catch(Exception ex)
    {
      CertificateLogger.warn("Could Not Instantiate SecurityDBHandler ",ex);
      throw new CertificateException("[SecurityDBHandler] "+"[getInstance()] "+
      "Could Not Instantiate SecurityDBHandler",ex);
    }
  }




  /**
   * Insert a Certificate with the String FileName.
   * @param certFile - String FileName representing Certificate.(Ex."c:/cert/mycert.cer");
   * @throws CertificateException - thrown when cannot insert a certificate.
   * @version 2.0 I5
   * @since 2.0 I5
   */


  public void insertCertificate(String certFile) throws CertificateException
  {
//    X509Certificate cert = null;
//    try
//    {
//      cert =  GridCertUtilities.loadX509Certificate(certFile);
//      if(implementation == null)
//      {
//        implementation =
//          (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//      }
//      implementation.insertCertificate(cert);
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("Could Not Insert Certificate By File Name",ex);
//      throw new CertificateException("[SecurityDBHandler] "+"[insertCertificate()] "+
//      "Could Not Insert Certificate",ex);
//    }

  }


  /**
   * Insert Certificate by ID Name and Certificate.
   * @param gridnodeID - ID of this GridNode
   * @param name - Name
   * @param certFile - Certificate file
   * @throws CertificateException - thrown when cannot insert a certificate.
   *
   * @version 2.0 I5
   * @since 2.0 I5
   */

  public void insertCertificate(Number gridnodeID,String name,String certFile) throws CertificateException
  {
//    X509Certificate cert = null;
//    try
//    {
//      CertificateLogger.debug("[SecurityDBHandler] "+"[insertCertificate()]"+
//      "In insertCertificate  ID="+gridnodeID+"Name="+name+"CertFileName"+certFile);
//      cert =  GridCertUtilities.loadX509Certificate(certFile);
//      insertCertificate(gridnodeID,name,cert);
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.warn("Could Not Insert Certificate By File Name ID and Name",ex);
//      throw new CertificateException("[SecurityDBHandler] "+"[insertCertificate()] "+
//      "Could Not Insert Certificate",ex);
//    }

  }




  /**
   * Insert's Certificate with ID, Name and Certificate in Byte Array.
   * For Backward Compatibility the parameter names uses gridnodeID.
   * @param gridnodeID - ID for this Certificate beeing identified.
   * @param name - Name of the Certificate.
   * @param certData - Certificate Data in ByteArray.
   * @return X509Certificate.
   * @throws CertificateException - thrown when cannot insert the Certificate.
   *
   * @version 2.0 I5
   * @since 2.0 I5
   */


//  public X509Certificate insertCertificate(Number gridnodeID, String name, byte[] certData)
//    throws CertificateException
//  {
//    X509Certificate cert = null;
//    try
//    {
//      CertificateLogger.debug("[SecurityDBHandler] "+"[insertCertificate()]"+
//      "In insertCertificate  ID="+gridnodeID+"Name="+name);
//      cert = new X509Certificate(certData, 0, 0);
//    }
//    catch (Exception ex)
//    {
//      CertificateLogger.warn("Could Not Insert Certificate ",ex);
//      throw new CertificateException("[SecurityDBHandler] "+"[insertCertificate()] "+
//      "Could Not Insert Certificate",ex);
//    }
//    insertCertificate(gridnodeID, name, cert);
//    return cert;
//  }

  /**
   * Insert's Certificate with ID, Name and Certificate.
   * For Backward Compatibility the parameter names uses gridnodeID.
   * @param gridnodeID - ID of this Certificate.
   * @param name - Name for this Certificate.
   * @param cert - Certificate - RSA.
   * @throws CertificateException thrown when cannot insert a Certificate.
   *
   * @version 2.0 I5
   * @since 2.0 I5
   */


//  public void insertCertificate(Number gridnodeID, String name, Certificate cert)
//    throws CertificateException
//  {
//    try
//    {
//      if(implementation == null)
//      {
//        implementation =
//          (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//      }
//      implementation.insertCertificate(gridnodeID.intValue(), name, cert);
//    }
//    catch (Exception ex)
//    {
//      throw new CertificateException("[SecurityDBHandler] "+"[insertCertificate()] "+
//      "Could Not insert certificate for " + gridnodeID , ex);
//    }
//  }

//  public void insertPrivateKeyByCert(JSAFE_PrivateKey prvKey, X509Certificate cert/*, Number nodeID*/)
//    throws CertificateException
//  {
//    try
//    {
//      if(implementation == null)
//      {
//        implementation =
//          (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//      }
//
//      implementation.insertPrivateKeyByCertificate(cert, prvKey);
//    }
//    catch (Exception ex)
//    {
//      throw new CertificateException("[SecurityDBHandler] "+"[insertCertificate()] "+
//      "Could Not insert certificate for " , ex);
//    }
//  }

//  public void deleteCertificate(int gNId, String name) throws CertificateException
//  {
//     try
//     {
//        if(implementation == null)
//        {
//          implementation =
//            (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//        }
//        implementation.deleteCertificate(gNId,name);
//     }
//     catch(Exception ex)
//     {
//       throw new CertificateException("[SecurityDBHandler] "+"[deleteCertificate()] "+
//       "Could Not delete certificate for " , ex);
//     }
//  }




/*  public static void main(String args[]) throws Exception
  {
    SecurityDBHandler handler = SecurityDBHandler.getInstance();
    handler.insertCertificate("mtes.cer");
  }
*/
}
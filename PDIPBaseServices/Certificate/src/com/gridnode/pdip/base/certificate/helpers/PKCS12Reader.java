/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PKCS12Reader.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 12 Oct 2001    Lim Soon Hsiung     Initial creation for GT 1.2
 * 07 Jun 2002    Jagadeesh           (GridTalk 2.0)Modified to include SecurityServicesException
 *                                    and Log Error Messages.
 * 03 Aug 2006    Tam Wei Xiang       Amend the way we access SecurityDB. This class
 *                                    is responsible for removing the provider itself.
 * Feb 09 2007		Alain Ah Ming				Log error codes or log warning message.                                                                          
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.util.Vector;

import com.gridnode.pdip.base.certificate.exceptions.CertificateException;
import com.rsa.certj.CertJ;
import com.rsa.certj.CertJException;
import com.rsa.certj.DatabaseService;
import com.rsa.certj.InvalidParameterException;
import com.rsa.certj.Provider;
import com.rsa.certj.ProviderManagementException;
import com.rsa.certj.cert.X509Certificate;
import com.rsa.certj.pkcs12.PKCS12;
import com.rsa.certj.pkcs12.PKCS12Exception;
import com.rsa.certj.provider.db.MemoryDB;
import com.rsa.jsafe.JSAFE_PrivateKey;

/**
 * 
 * @author Lim Soon Hsiung
 * @since
 * @version GT 4.0 VAN
 */
public class PKCS12Reader
{
//  private String sampleCert = "sample.p12";
//  private String pw = "Gridnode1!";
  private String pkcs12Filename;
  private char[] password;
  private DatabaseService dbService;
  private CertJ certJ;
  //private JSAFE_PublicKey pubkey;
  private JSAFE_PrivateKey ptekey;
  private X509Certificate subjectCert;

  //private JSAFE_PrivateKey[] pteKeys;
  private X509Certificate[] certificates;

  private String serviceName;
  private boolean isInitialized = false;
  
  private SecurityDB _secDB = null; //TWX 03082006
  
  public PKCS12Reader(String pkcs12Filename, char[] password)
  {
    this.pkcs12Filename = pkcs12Filename;
    this.password = password;
  }


  public void read() throws PKCS12Exception,CertificateException
  {
    initService();
    try
    {
      PKCS12 p12 = loadFile(pkcs12Filename);
      loadCerts(p12);
      loadPteKeys(p12);

      findSubjectCert();
      checkKeyMatch();
    }
    catch(PKCS12Exception p12Ex)
    {
      CertificateLogger.warn("Exception in P12 read "+p12Ex.getMessage());
      throw p12Ex;
    }
    catch (CertificateException seEx)
    {
      CertificateLogger.warn("Exception in read "+seEx.getMessage());
      throw seEx;
    }
    catch (Exception ex)
    {
      CertificateLogger.warn("Exception in Read ");
      throw new CertificateException("Exception in Read ",ex);
    }
  }

  private void checkKeyMatch() throws CertificateException
  {
    if(subjectCert == null || ptekey == null)
      return;

    if(!GridCertUtilities.isMatchingPair(subjectCert, ptekey))
      throw new CertificateException("Certificate and private key is not a matching pair");
  }

  private void findSubjectCert()
  {
    CertificateLogger.log("findSubjectCert Searching for subject key .....");

    CertVerifier cv = new CertVerifier();
    int count = certificates.length;
    boolean found = true;
    for (int i = 0; i < count; i++)
    {
      found = true;
      cv.setIssuerCert(certificates[i]);
      for (int j = 0; j < count; j++)
      {
        if(i == j)
          continue;
        cv.setSubjectCert(certificates[j]);
        try
        {
          if(cv.isIssuedBy() == CertVerifier.ERR_NO_ERROR)
            found = false;
        }
        catch (Exception ex)
        {

        }
      }// end of for j

      if(found)
      {
        subjectCert = certificates[i];
        break;
      }
    }// end of for i

    CertificateLogger.log("findSubjectCert End searching for subject key .....");
  }

  private PKCS12 loadFile(String filename)
    throws PKCS12Exception,CertificateException
  {
    PKCS12 p12 = null;
    try
    {

      /* With the Cert-J PKCS 12 class, there are two options for
       * reading a PKCS 12 file.  If valid CertJ and DatabaseService
       * objects are passed, then any certificate, private keys, and
       * CRLs found in the PKCS 12 file will be inserted into the
       * databases bound by the DatabaseService.  The following line
       * of code, will read the specified PKCS 12 file, storing any
       * information it finds in the database. */
      CertificateLogger.log("Instantiating com.rsa.certj.pkcs12.PKCS12 object. File: " +
        pkcs12Filename);
      p12 = new PKCS12 (certJ, dbService, password, filename);
      CertificateLogger.log("loadFile Done creating pkcs12 object");

    }
    catch(PKCS12Exception p12Ex)
    {
      throw p12Ex;
    }
    catch (Exception ex)
    {
      CertificateLogger.warn("Exception creating PKCS12 object");
      throw new CertificateException("Unable to load PKCS12 Object:  ",ex);
    }
    return p12;
  }
  /*
  private void go ()
    throws CertificateException
  {
    try {
//
//      //Now, view the CRLs that were inserted (if any). 
//      debug("", "Viewing inserted CRLs:");
//      CRL crl = dbService.firstCRL();
//      while (crl != null) {
//        // Print the issuer and time of the CRL. 
//        debug("", "CRL:");
//        debug("", "  Issuer Name:");
//        NameUtilities.printNameInfo (((X509CRL)crl).getIssuerName(), this);
//        debug("", "  This update:  " + ((X509CRL)crl).getThisUpdate());
//        debug("", "");
//        //pause();
//        debug("", "");
//
//        crl = dbService.nextCRL ();
//      }
      CertificateLogger.log("End of operation.");

    } catch (Exception anyException) {
      CertificateLogger.err("Caught an exception.");
      throw new CertificateException("Exception in go ",anyException);
    }
  }*/

  private void initService() throws CertificateException
  {
    if(isInitialized)
      return;

    try {

      CertificateLogger.log("initService Creating CertJ object and binding database services.");


      String sName = (serviceName == null)? "In-Memory Provider" : serviceName;
      /* Create the providers.  For simplicity, this sample will only
       * use an in-memory provider.  */
      Provider provider = new MemoryDB (sName);

      /* Print a list of the instantiated providers. */
//      System.out.println("Providers:");
//      for (int i = 0 ; i < providers.length ; i++) {
//        debug("", "  " + providers[i].getName ());
//      }
//      System.out.println();

      CertificateLogger.log("initService Instantiating CertJ object.");
      if(certJ == null)
      {
        certJ = new CertJ ();
      }
      certJ.addProvider(provider);

      if(dbService == null)
      {
        CertificateLogger.log("initService Binding to database services.");
        dbService =
          (DatabaseService)certJ.bindService(CertJ.SPT_DATABASE, sName);
      }
      CertificateLogger.log("initService Done creating CertJ object and binding services.");
    }
    catch (CertJException ex)
    {
      CertificateLogger.warn("Exception caught while instantiating CertJ object.");
      throw new CertificateException("Exception caught while instantiating CertJ object.", ex);
    }
    isInitialized = true;
  }

  public JSAFE_PrivateKey getPrivateKey()
  {
    return ptekey;
  }

//  public JSAFE_PublicKey getPublicKey()
//  {
//    return pubkey;
//  }

  public X509Certificate getCertificate()
  {
    return subjectCert;
  }

  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }

  public void setCertJ(CertJ certJ)
  {
    this.certJ = certJ;
  }

//  public void setDbService(DatabaseService dbService)
//  {
//    this.dbService = dbService;
//  }

  private void loadCerts(PKCS12 p12) throws CertificateException
  {
    /* Since the database started out empty, it is very easy to view
     * the items that were added to the database.  First, view the
     * certificates that were inserted.
     */
      CertificateLogger.log("loadCerts Loading cert.....");
//      debug("", "");

    Vector res = new Vector();

    try
    {
      X509Certificate cert = (X509Certificate)dbService.firstCertificate();
//      this.certificate = cert;
      while (cert != null)
      {
        res.addElement(cert);
//        /* Print the issuer and subject name of the certificate. */
//        debug("", "$$$$$$$$$$$$$$$ Certificate #" + count + " :");
//        debug("", "  Issuer Name:");
//        NameUtilities.printNameInfo (((X509Certificate)cert).getIssuerName(),
//                                     this);
//        debug("", "  Subject Name:");
//        NameUtilities.printNameInfo (((X509Certificate)cert).getSubjectName(),
//                                     this);
//        pubkey = cert.getSubjectPublicKey("Java");
//        debug("", "-----BEGIN PUBLIC KEY DATA-----", false);
//        printBase64 (pubkey.getKeyData ()[0]);
//        debug("", "-----END PUBLIC KEY DATA-----", false);
//        debug("", "");
        //pause();
//        debug("", "");
//
//        count++;

        cert = (X509Certificate)dbService.nextCertificate ();
      }
      CertificateLogger.log("loadCerts Done loading cert, count: " + res.size());
      certificates = new X509Certificate[res.size()];
      certificates = (X509Certificate[])res.toArray(certificates);

    }
    catch (Exception ex)
    {
      CertificateLogger.warn("loadCerts Excepton ");
      throw new CertificateException("loadCerts Excepton ",ex);
    }


  }

  private void loadPteKeys(PKCS12 p12) throws CertificateException
  {
    CertificateLogger.log("loadPteKeys Loading private key .....");
    try
    {
//      /* Now, view the private keys that were inserted (if any). */
//      debug("", "Viewing inserted private keys:");
      JSAFE_PrivateKey key = dbService.firstPrivateKey();
      ptekey = key;
//      while (key != null) {
//        /* Print the Base64-encoded private key info. */
//        debug("", "Private Key Data:");
//        debug("", "-----BEGIN PRIVATE KEY DATA-----", false);
//        printBase64 (key.getKeyData (key.getAlgorithm() + "PrivateKeyBER")[0]);
//        debug("", "-----END PRIVATE KEY DATA-----", false);
//        debug("", "");
        //pause();
//        debug("", "");
//
//        key = dbService.nextPrivateKey ();
//      }
      CertificateLogger.log("loadPteKeys Done loading private key .....");
    }
    catch (Exception ex)
    {
      CertificateLogger.warn("loadPteKeys Exception ");
      throw new CertificateException("loadPteKeys Exception ",ex);
    }
  }
  
//TWX 03082006
  public void setSecurityDB(SecurityDB secDB)
  {
  	_secDB = secDB;
  }
  
  public SecurityDB getSecurityDB()
  {
  	return _secDB;
  }
  
  public void removeProvider(String serviceName)
  	throws ProviderManagementException, InvalidParameterException
  {
  	if(_secDB != null)
  	{
  		CertJ certJ = _secDB.getCertJ();
  		certJ.removeProvider(CertJ.SPT_DATABASE, serviceName);
  	}
  }
  //end
  
// Logging methods ***********************************************************
//  public static void log(String methodName, String msg)
//  {
//    System.out.println("LOG [PKCS12Reader." + methodName + "] " + msg);
//  }
//  public static void debug(String methodName, String msg)
//  {
//    System.out.println("DEBUG [PKCS12Reader." + methodName + "] " + msg);
//  }
//  public static void err(String methodName, String msg, Exception ex)
//  {
//    System.out.println("ERR [PKCS12Reader." + methodName + "] " + msg);
//    ex.printStackTrace();
//  }


//  public static void main (String[] args)
//    throws Exception
//  {
//    try
//    {
//
//      PKCS12Reader reader = new PKCS12Reader ("gridnodecert.pfx", "".toCharArray());
//      reader.read ();
//      System.out.println("Cert: " + reader.getCertificate());
//      System.out.println("pk: " + reader.getPrivateKey());
//    CertUtilities.writeX509Certificate("nettrustgridnode.cer", reader.getCertificate());
//    CertUtilities.writePKCS8PrivateKey("nettrustgridnode.prv", reader.getPrivateKey(), reader);

//    PKCS12Reader reader2 = new PKCS12Reader ("sample.p12", "Gridnode1!".toCharArray());
//    reader2.read ();
//    CertUtilities.writeX509Certificate("idsafegridnodepw.cer", reader2.getCertificate());
//    CertUtilities.writePKCS8PrivateKey("idsafegridnode.prv", reader2.getPrivateKey(), reader);
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
//  }
}

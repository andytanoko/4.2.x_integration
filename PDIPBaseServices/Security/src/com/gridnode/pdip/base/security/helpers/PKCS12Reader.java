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
 * 29 Jul 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.security.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

public class PKCS12Reader
{
//  private String sampleCert = "sample.p12";
//  private String pw = "Gridnode1!";
  private String pkcs12Filename;
  private char[] password;
  private PrivateKey _ptekey;
  private X509Certificate _subjectCert;

  private X509Certificate[] _certificates;

  private String serviceName;
  private boolean isInitialized = false;

  public PKCS12Reader(String pkcs12Filename, char[] password)
  {
    this.pkcs12Filename = pkcs12Filename;
    this.password = password;
  }


  public void read() throws SecurityServiceException
  {
    try
    {
      KeyStore keyStore = loadFile(pkcs12Filename);
      loadCerts(keyStore);
      loadPteKeys(keyStore);

      findSubjectCert();
      checkKeyMatch();
    }
    catch (SecurityServiceException seEx)
    {
      SecurityLogger.warn("Exception in read "+seEx.getMessage());
      throw seEx;
    }
    catch (Exception ex)
    {
      SecurityLogger.warn("Exception in Read ");
      throw new SecurityServiceException("Exception in Read ",ex);
    }
  }

  private void checkKeyMatch() throws SecurityServiceException
  {
    if(_subjectCert == null || _ptekey == null)
    {
      return;
    }
      
    if(!GridCertUtilities.isMatchingPair(_subjectCert, _ptekey))
      throw new SecurityServiceException("Certificate and private key is not a matching pair");
  }

  private void findSubjectCert()
  {
    SecurityLogger.log("findSubjectCert Searching for subject key .....");

    CertVerifier cv = new CertVerifier();
    int count = _certificates.length;
    boolean found = true;
    
    for (int i = 0; i < count; i++)
    {
      found = true;
      cv.setIssuerCert(_certificates[i]);
      for (int j = 0; j < count; j++)
      {
        if(i == j)
          continue;
        cv.setSubjectCert(_certificates[j]);    
        try
        {
          if(cv.isIssuedBy() == CertVerifier.ERR_NO_ERROR)
          {
            found = false;
          }
        }
        catch (Exception ex)
        {

        }
      }// end of for j

      if(found)
      {
        _subjectCert = _certificates[i];
        break;
      }
    }// end of for i

    SecurityLogger.log("findSubjectCert End searching for subject key ....."+_subjectCert);
  }

  private KeyStore loadFile(String filename) throws SecurityServiceException
  {
    KeyStore keystore = null;
    FileInputStream in = null;
    try
    {   
      SecurityLogger.log("Instantiating keystore. File: " +filename);
      in = new FileInputStream(new File(filename));
      keystore = KeyStore.getInstance("PKCS12", "BC");
      keystore.load(in, password);
      SecurityLogger.log("load PKCS12 file Done.");
      return keystore;
    }
    catch (Exception ex)
    {
      SecurityLogger.warn("Exception creating PKCS12 object");
      throw new SecurityServiceException("Unable to load PKCS12 Object:  ",ex);
    }
    finally
    {
      if(in != null)
      {
        try
        {
          in.close();
        }
        catch(IOException ioex)
        {
          ioex.printStackTrace();
        }
      }
    }
  }
  /*
  private void go ()
    throws SecurityServiceException
  {
    try {
//
//      // Now, view the CRLs that were inserted (if any). 
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
      SecurityLogger.log("End of operation.");

    } catch (Exception anyException) {
      SecurityLogger.err("Caught an exception.");
      throw new SecurityServiceException("Exception in go ",anyException);
    }
  }*/

  /*
  private void initService() throws SecurityServiceException
  {
    if(isInitialized)
      return;

    try {

      SecurityLogger.log("initService Creating CertJ object and binding database services.");


      String sName = (serviceName == null)? "In-Memory Provider" : serviceName;
//      Create the providers.  For simplicity, this sample will only
//      use an in-memory provider. 
      Provider provider = new MemoryDB (sName);

      //Print a list of the instantiated providers.
//      System.out.println("Providers:");
//      for (int i = 0 ; i < providers.length ; i++) {
//        debug("", "  " + providers[i].getName ());
//      }
//      System.out.println();

      SecurityLogger.log("initService Instantiating CertJ object.");
      if(certJ == null)
      {
        certJ = new CertJ ();
      }
      certJ.addProvider(provider);

      if(dbService == null)
      {
        SecurityLogger.log("initService Binding to database services.");
        dbService =
          (DatabaseService)certJ.bindService(CertJ.SPT_DATABASE, sName);
      }
      SecurityLogger.log("initService Done creating CertJ object and binding services.");
    }
    catch (CertJException ex)
    {
      SecurityLogger.warn("Exception caught while instantiating CertJ object.");
      throw new SecurityServiceException("Exception caught while instantiating CertJ object.", ex);
    }
    isInitialized = true;
  } */

  public PrivateKey getPrivateKey()
  {
    return _ptekey;
  }

//  public JSAFE_PublicKey getPublicKey()
//  {
//    return pubkey;
//  }

  public X509Certificate getCertificate()
  {
    return _subjectCert;
  }

  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }

//  public void setCertJ(CertJ certJ)
//  {
//    this.certJ = certJ;
//  }

//  public void setDbService(DatabaseService dbService)
//  {
//    this.dbService = dbService;
//  }

  private void loadCerts(KeyStore keyStore) throws SecurityServiceException
  {

    SecurityLogger.log("loadCerts Loading cert.....");
    Vector<X509Certificate> res = new Vector<X509Certificate>();

    try
    {
      
      Enumeration<String> aliases = keyStore.aliases();
     
      while (aliases.hasMoreElements())
      {
        String certAlias = aliases.nextElement();

        Certificate cert = keyStore.getCertificate(certAlias);
        if(cert instanceof X509Certificate)
        {
          res.add((X509Certificate)cert);
          SecurityLogger.debug("PKCS12Reader: Load Certificate:"+((X509Certificate)cert));
        }
        else
        {
          SecurityLogger.debug("Non X509Certificate found: "+cert+". ignored");
        }
      }
      SecurityLogger.log("loadCerts Done loading cert, count: " + res.size());
      _certificates = new X509Certificate[res.size()];
      _certificates = (X509Certificate[])res.toArray(_certificates);

    }
    catch (Exception ex)
    {
      SecurityLogger.warn("loadCerts Excepton ");
      throw new SecurityServiceException("loadCerts Excepton ",ex);
    }


  }

  private void loadPteKeys(KeyStore keyStore) throws SecurityServiceException
  {
    SecurityLogger.log("loadPteKeys Loading private key .....");
    try
    {
      Enumeration<String> aliases = keyStore.aliases();
      while(aliases.hasMoreElements())
      {
        String alias = aliases.nextElement();
        boolean isKeyEntry = keyStore.isKeyEntry(alias);
        if(isKeyEntry)
        {
          Key key = keyStore.getKey(alias, password);
          if(key instanceof PrivateKey)
          {
            _ptekey = (PrivateKey)key;
            break;
          }
        }
      }

      SecurityLogger.log("loadPteKeys Done loading private key .....");
    }
    catch (Exception ex)
    {
      SecurityLogger.warn("loadPteKeys Exception ");
      throw new SecurityServiceException("loadPteKeys Exception ",ex);
    }
  }



// Logging methods ***********************************************************
  public static void log(String methodName, String msg)
  {
//    System.out.println("LOG [PKCS12Reader." + methodName + "] " + msg);
  }
  public static void debug(String methodName, String msg)
  {
//    System.out.println("DEBUG [PKCS12Reader." + methodName + "] " + msg);
  }
  public static void err(String methodName, String msg, Exception ex)
  {
//    System.out.println("ERR [PKCS12Reader." + methodName + "] " + msg);
//    ex.printStackTrace();
  }


  public static void main (String[] args)
//    throws Exception
  {
    try
    {

      PKCS12Reader reader = new PKCS12Reader ("gridnodecert.pfx", "".toCharArray());
      reader.read ();
      System.out.println("Cert: " + reader.getCertificate());
      System.out.println("pk: " + reader.getPrivateKey());
//    CertUtilities.writeX509Certificate("nettrustgridnode.cer", reader.getCertificate());
//    CertUtilities.writePKCS8PrivateKey("nettrustgridnode.prv", reader.getPrivateKey(), reader);

//    PKCS12Reader reader2 = new PKCS12Reader ("sample.p12", "Gridnode1!".toCharArray());
//    reader2.read ();
//    CertUtilities.writeX509Certificate("idsafegridnodepw.cer", reader2.getCertificate());
//    CertUtilities.writePKCS8PrivateKey("idsafegridnode.prv", reader2.getPrivateKey(), reader);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

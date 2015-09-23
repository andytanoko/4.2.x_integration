/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityServiceFacade
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 21-June-2002    Jagadeesh           Created.
 * 10 Nov 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 * 08 July 2009      Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */



package com.gridnode.pdip.base.security.helpers;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import com.gridnode.pdip.base.security.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceHome;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceObj;
import com.gridnode.pdip.base.security.mime.GNBodypart;
import com.gridnode.pdip.base.security.mime.IMailpart;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class SMIMESecurityServiceFacade
{

  //private ISecurityServiceHome _home;
  private ISecurityServiceObj _remote;

//  private CertJ certJ;
//  private Provider memProvider;
  //private JSAFE_PublicKey  publicKey;
  //private JSAFE_PrivateKey privateKey;
  //private CertPathCtx pathCtx;


  private void init() throws java.lang.Exception
  {

    SecurityLogger.log("TEST SecurityServiecs Setup");

    //_home = (ISecurityServiceHome)ServiceLookup.getInstance(
    //        ServiceLookup.CLIENT_CONTEXT).getHome(
    //         ISecurityServiceHome.class);
    //_remote = _home.create();
    _remote = (ISecurityServiceObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ISecurityServiceHome.class.getName(),
                                                                                                 ISecurityServiceHome.class,
                                                                                                 new Object[0]);

    SecurityLogger.log("Test SecurityServices Successful.");
  }
  public SMimeSecurityInfo encrypt(SMimeSecurityInfo info) throws SecurityServiceException
  {
    try
    {
        SecurityLogger.log("Encrypt Start of SMIME ");
        //CertJ certJ = info.getCertJ();
        X509Certificate recpCertificate = info.getReceipentCertificate();
        PrivateKey privateKey = info.getPrivateKey();
        //initCertDBService(recpCertificate,privateKey);
        byte[] partToEncrypt = info.getDataToEncrypt();

        if(partToEncrypt == null)
        {
          SecurityLogger.log("Part to Encrypt From IMailpart ");
          partToEncrypt = info.getIMailpartToEncrypt().getContentByte(false);
        }
        SecurityLogger.log("Before Calling Encrypt Byte Array "+partToEncrypt);
        init();
        byte[] encryptData = _remote.encrypt(recpCertificate,partToEncrypt,recpCertificate,privateKey);
        SecurityLogger.log("After Encryption   "+encryptData);
        SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
        iinfo.setEncryptedData(encryptData);
        return iinfo;
    }
    catch(Exception ex)
    {
       SecurityLogger.log("Exception in Encrypt Main  " + ex.getMessage());
       ex.printStackTrace();
    }
    return null;

  }


  public SMimeSecurityInfo decrypt(SMimeSecurityInfo info) throws SecurityServiceException
  {
    try
    {
          X509Certificate recpCertificate = info.getReceipentCertificate();
          PrivateKey privateKey = info.getPrivateKey();

          byte[] partToDecrypt = info.getDataToDecrypt();
          if(partToDecrypt == null)
          {
             IPart part = info.getIPartToDecrypt();
             partToDecrypt = (byte[])part.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
         }

         init();
         byte[] decryptData =  _remote.decrypt(partToDecrypt,recpCertificate,privateKey);
         SecurityLogger.log("Decrypted Data  "+decryptData);
         SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
         iinfo.setDecryptedData(decryptData);
         return iinfo;
    }
    catch(Exception ex)
    {
       ex.printStackTrace();
    }
    return null;

  }


  public SMimeSecurityInfo sign(SMimeSecurityInfo info) throws SecurityServiceException
  {
    SMimeSecurityInfo signInfo = null;
    try
    {
        X509Certificate recpCertificate = info.getReceipentCertificate();
        PrivateKey privateKey = info.getPrivateKey();
        byte[] partToSign = info.getDataToSign();
        signInfo = new SMimeSecurityInfo();
        if(partToSign == null)
        {
          partToSign = info.getPartToSign().getContentByte(false);
          signInfo.setIMailpartToSign(info.getPartToSign());
        }
        SecurityLogger.log("Begin of Sign ");
        init();
        byte[] signedData = _remote.sign(recpCertificate,partToSign,privateKey);
        signInfo.setSignedData(signedData);
        return signInfo;
    }
    catch(Exception ex)
    {
        SecurityLogger.warn("Cannot Sign Data ",ex);
        throw new SecurityServiceException("Cannot Sign Data ",ex);
    }

  }


   public SMimeSecurityInfo verify(SMimeSecurityInfo info) throws SecurityServiceException
   {
     SMimeSecurityInfo vinfo=null;
     try
     {
        X509Certificate recpCertificate = info.getReceipentCertificate();
        PrivateKey privateKey = info.getPrivateKey();
        IMime partToVerify = info.getPartToVerify();
        GNBodypart    bodyPart = (GNBodypart)partToVerify.getPart(0);
        IMime mimepart = bodyPart.getMultipart();
        byte[] contentInfo = (byte[])mimepart.getContentByte(false);
        GNBodypart    signatureBodyPart = (GNBodypart)partToVerify.getPart(1);
        byte[] signature  = (byte[])signatureBodyPart.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
        init();
        if(_remote.verify(contentInfo, signature,recpCertificate,privateKey))
        {
           SecurityLogger.log(" Verified and Got It ... ");
           vinfo = new SMimeSecurityInfo();
           //vinfo.setVerifiedPart(mimepart);
           return vinfo;
        }
        else
        {
            throw new SecurityServiceException("Verify failed");
        }
    }
     catch(Exception ex)
     {
        SecurityLogger.warn("Verification Failed  ",ex);
        throw new SecurityServiceException("Verification Failed  ");
     }

   }
}
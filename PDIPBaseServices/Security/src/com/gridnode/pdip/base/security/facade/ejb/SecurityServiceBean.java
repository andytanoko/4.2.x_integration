/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 29-May-2002    Jagadeesh           Created.
 * 25-Feb-2003    Jagadeesh           Modified: To Add all Certs to TrustStore
 *                                    when Verifying.
 * 09-MAR-2003    Jagadeesh           Modified: To Use CertPathCtx initilized in
 *                                    SecurityDB Service.
 *
 * 25-Aug-2003   Jagadeesh            Enabled-if(sign), dontknow who disabled if(sign).
 * 04 Nov 2003    Zou Qingsong        Enhancement for Compression
 * 10 Nov 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 * 24 Jul 2006    Tam Wei Xiang       Modified : To enable the decryption on the doc
 *                                               we encrypted.
 * 03 Aug 2006    Tam Wei Xiang       Amend the way we access SecurityDB.
 *                                    Modified method: decrypt(...), encrypt(...), 
 *                                                     sign(...), verify(...)                                              
 * 30 Aug 2006    Neo Sok Lay         GNDB00027767: Set Filename in MimeBodyPart
 *                                    during packSMIME().
 * 12 Mar 2007    Neo Sok Lay         Use UUID for unique filename.   
 * 08 July 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                
 */

package com.gridnode.pdip.base.security.facade.ejb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.bouncycastle.cms.CMSCompressedData;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import com.gridnode.pdip.base.certificate.helpers.CertificateEntityHandler;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.security.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.helpers.GNCMSCompressedDataGenerator;
import com.gridnode.pdip.base.security.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.helpers.ISecurityInfo;
import com.gridnode.pdip.base.security.helpers.SMimeSecurityInfo;
import com.gridnode.pdip.base.security.helpers.SecurityInfo;
import com.gridnode.pdip.base.security.helpers.SecurityLogger;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.security.mime.smime.ISMimePackager;
import com.gridnode.pdip.base.security.mime.smime.SMimeFactory2;
import com.gridnode.pdip.base.security.mime.smime.helpers.SMimeHelper;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.UUIDUtil;


public class SecurityServiceBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5117635040538921166L;
	private SessionContext _ctx; //SessionContext provided by AppServer.

  /**
  * This method encrypt and sign the data/content passed in as
  * part of the Value Object - Message
  *
  * @param  securityInfo - Value Object encapsulating the security settings.
  * @param  message - Value Object encapsulating the data to be encrypted.
  *
  * @return Message - Value Object encapsulating the encrypted data.
  * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
  * @throws RemoteException - Thrown when the method failed due to system-level exception.
  */
  public Message encryptAndSign(SecurityInfo securityInfo, Message message)
    throws SecurityServiceException
  {
    SecurityLogger.log(
      "[SecurityServiceBean][encryptAndSign][Start Encrypt ...]");
    /** @todo Below Log Statements are for Test purpose only .... */
    SecurityLogger.log("[SecurityLevel]=" + securityInfo.getSecuritylevel());
    //SecurityLogger.log("[Message]\n"+message.toString());

    try
    {
      Map commonHeaders = message.getCommonHeaders();
      Boolean isSMIME = (Boolean)(commonHeaders.get(ICommonHeaders.IS_SMIME));
      if ((isSMIME != null) && (isSMIME.booleanValue() == true) )
      {
        return packSMIME(securityInfo, message);
      }

      File tempFile = null;
      //byte[] content = null;

      int securityLevel = securityInfo.getSecuritylevel();
      if ((securityLevel == ISecurityInfo.SECURITY_LEVEL_1)
        || (securityLevel == ISecurityInfo.SECURITY_LEVEL_3))
      {
        // encrypt and sign data only
        String[] data = message.getData();
        if (data == null || data.length == 0)
        {
          SecurityLogger.debug(
            "[SecurityServiceBean][encryptAndSign]"
              + "[No Encryption Done Since Data is Null]");
          message.setData(message.getData());
        }
        else
        {
          tempFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          String[] encryptedData = new String[data.length];
          for (int i = 0; i < data.length; i++)
          {
            if (data[i] == null)
            {
              encryptedData[i] = null;
            }
            else
            {
            	tempFile =
            		File.createTempFile(SecurityInfo.PREFIX + getRandom(), "tmp");
            	SecurityLogger.debug("[String B4 Encrypting][" + data[i] + "]");
              byte[] content = data[i].getBytes(ISecurityInfo.ENCODING);
              GridCertUtilities.writeByteTOFile(
                tempFile,
                content,
                content.length);
              content = encryptAndSign(tempFile, securityInfo);
              encryptedData[i] = new String(content, ISecurityInfo.ENCODING);
              SecurityLogger.debug("[String After Encrypting][" +encryptedData[i]+"]");
              if (tempFile != null && tempFile.exists())
              {
              	tempFile.delete();
              }

            }
          }
          message.setData(encryptedData);
        }
      }

      if ((securityLevel == ISecurityInfo.SECURITY_LEVEL_1)
        || (securityLevel == ISecurityInfo.SECURITY_LEVEL_2))
      {
        // encrypt and sign payload only
        byte[] content = message.getPayLoadData();
        if ((content == null) || (content.length == 0))
          message.setPayLoad(content);
        else
        {
          SecurityLogger.debug("[B4 Encrypting PayLoad Length=]["+content.length+"]");
                                        tempFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          GridCertUtilities.writeByteTOFile(tempFile, content, content.length);
          content = encryptAndSign(tempFile, securityInfo);
                                        SecurityLogger.debug("[After Encrypting PayLoad Length=]["+content.length+"]");
          message.setPayLoad(content); //Set BytePayload
          if (tempFile != null && tempFile.exists())
                                            tempFile.delete();
          //message.setFilePayLoad(null);
        }
      }
      return message;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.warn(ex.getMessage());
      throw new SecurityServiceException("[Unable to encrypt and sign]", ex);
    }
  }

  /**
   * This method decrypts and verify the data/content passed in as
   * a part of Value Object - Message.
   *
   * @param  securityInfo - Value Object encapsulating the security settings.
   * @param  message - Value Object encapsulating the encrypted data.
   *
   * @return Message - Value Object encapsulating the decrypted data.
   * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
   * @throws RemoteException - Thrown when the method failed due to system-level exception.
   */
  public Message decryptAndVerify(SecurityInfo securityInfo, Message message)
    throws SecurityServiceException
  {
    SecurityLogger.debug(
      "[SecurityServiceBean][decryptAndVerify()] In Decrypt ");
    try
    {
      SecurityLogger.debug(
        "[Begin to import signature, envelope from input file]");
      SecurityLogger.debug(
        "[SecurityLvL= " + securityInfo.getSecuritylevel() + "]");

      int securityLevel = securityInfo.getSecuritylevel();
      if ((securityLevel == ISecurityInfo.SECURITY_LEVEL_1)
        || (securityLevel == ISecurityInfo.SECURITY_LEVEL_3))
      {
        String decryptedData[] = {
        };
        File encryptedFile;
        String[] data = message.getData();
        if (data == null || data.length == 0)
          message.setData(data);
        else
        {
          decryptedData = new String[data.length];
          for (int i = 0; i < data.length; i++)
          {
            if (data[i] == null) //don't decrypt if null
            {
              decryptedData[i] = null;
            }
            else
            {
              SecurityLogger.debug("[String B4 Decrypting][" + data[i] + "]");
              encryptedFile =
                File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
              byte[] byteData = data[i].getBytes(ISecurityInfo.ENCODING);
              GridCertUtilities.writeByteTOFile(
                encryptedFile,
                byteData,
                byteData.length);
              byte[] encryptedString =
                decryptAndVerify(encryptedFile, securityInfo);
              String encStr =
                new String(encryptedString, ISecurityInfo.ENCODING);
              decryptedData[i] = encStr;
              SecurityLogger.debug(
                "[String B4 Decrypting][" + decryptedData[i] + "]");
              if ( encryptedFile!= null && encryptedFile.exists())
                encryptedFile.delete();
            }
          }
          message.setData(decryptedData);
        }
      }

      if ((securityLevel == ISecurityInfo.SECURITY_LEVEL_1)
        || (securityLevel == ISecurityInfo.SECURITY_LEVEL_2))
      {
        // encrypt and sign payload only
        byte[] content = message.getPayLoadData();
        if ((content == null) || (content.length == 0))
          message.setPayLoad(content);
        else
        {
          File tempFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          GridCertUtilities.writeByteTOFile(tempFile, content, content.length);
          content = decryptAndVerify(tempFile, securityInfo);
          message.setPayLoad(content); //Set BytePayload
          if (tempFile != null && tempFile.exists())
            tempFile.delete();
        }
      }
      return message;
    }
    catch (Exception ex)
    {
      SecurityLogger.warn("[decryptAndVerify()][Cannot Decrypt And Verify]", ex);
      throw new SecurityServiceException(
        "Cannot Perform Decrypt And Verify ",
        ex);
    }
  }

  /**
   * This method encrypt's and sign the data/content. SecurityInfo is (implementation/reference)
   * of the Value Object, which encapsulates security information.
   *
   * All GridTalk user documents are encrypted and signed using RSA BSAFE Crypto-J library.
   *
   * @param securityInfo - Value Object which is passed as a coarse-grained Object.
   * @return -  Value Object which containes the encrypted data.
   * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
   */

  public SecurityInfo encryptAndSign(SecurityInfo securityInfo)
    throws SecurityServiceException
  {
    // File tempDir = new File("/");  // TempDir -- Should actually be from FileServices.
    /** Check for Encrypting the String (or) File **/

    SecurityLogger.log(
      "[SecurityServiceBean][encryptAndSign] Start Encrypt ............... ");
    try
    {
      File inputFile;
      String encryptedData[] = {
      };
      String[] stringArrayToEncrypt = securityInfo.getStringArrayToEncrypt();
      if (stringArrayToEncrypt != null)
      {
        encryptedData = new String[stringArrayToEncrypt.length];
        SecurityLogger.log(
          "[SecurityServiceBean]"
            + "[encryptAndSign]"
            + " Length of StringArray is "
            + stringArrayToEncrypt.length);

        for (int i = 0; i < stringArrayToEncrypt.length; i++)
        {
          inputFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          SecurityLogger.log(
            "[SecurityServiceBean]"
              + "[encryptAndSign]"
              + "String B4 Encrypting "
              + stringArrayToEncrypt[i]);
          if (stringArrayToEncrypt[i] == null)
            encryptedData[i] = null;
          else
          {
            byte[] data =
              stringArrayToEncrypt[i].getBytes(ISecurityInfo.ENCODING);
            GridCertUtilities.writeByteTOFile(inputFile, data, data.length);
            byte[] encryptedString = encryptAndSign(inputFile, securityInfo);
            String encStr = new String(encryptedString, ISecurityInfo.ENCODING);
            SecurityLogger.log(
              "[SecurityServiceBean]"
                + "[encryptAndSign]"
                + "String After Encrypting "
                + encStr);
            encryptedData[i] = encStr;
          }
        }
        //securityInfo.setEncryptedStringArray(encryptedData);
      }

      /** Check if SecurityInfo is set with String or File . If not set throw Exception **/

      String inputFileString = securityInfo.getFileToEncrypt();

      /*Check for dataContent[] in String array to encryptAndSign if SecurityInfo
       * is set with dataContent[] the do so. Else check
       */
      SecurityInfo tinfo = new SecurityInfo();
      byte[] data1;
      if (inputFileString != null
        || securityInfo.getStringToEncrypt() != null
        || securityInfo.getDataToEncrypt() != null)
      {
        if (inputFileString == null)
        {
          byte[] data;
          inputFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          inputFile.deleteOnExit(); //Pls Delete this file on exit
          String stringtoEncrypt = securityInfo.getStringToEncrypt();
          if (stringtoEncrypt != null)
            data = stringtoEncrypt.getBytes(ISecurityInfo.ENCODING);
          else
            data = securityInfo.getDataToEncrypt();
          GridCertUtilities.writeByteTOFile(inputFile, data, data.length);
          data1 = encryptAndSign(inputFile, securityInfo);
        }
        else
        {
          inputFile = new File(inputFileString);
          data1 = encryptAndSign(inputFile, securityInfo);
        }

        if (securityInfo.getOutputFile() != null)
        {
          SecurityLogger.log("Setting the Output File ");
          tinfo.setOutputFile(securityInfo.getOutputFile());
        }
        SecurityLogger.log("Setting Encrypted Data in Byte Array ");
        tinfo.setEncryptedData(data1);
      }
      if (stringArrayToEncrypt != null)
      {
        SecurityLogger.log("Setting the StringArray ..");
        tinfo.setEncryptedStringArray(encryptedData);
      }
      return tinfo;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.warn(ex.getMessage());
      throw new SecurityServiceException("Cannot Perform Encrypt And Sign ", ex);
    }

  }

  /**
   * This method decrypts's and Verify's the data/content. SecurityInfo is (implementation/reference)
   * of the Value Object, which encapsulates security information.
   *
   * All GridTalk user documents are decypted and verified using RSA BSAFE Crypto-J library.
   *
   * @param securityInfo  - Value Object which is passed as a coarse-grained Object.
   * @return SecurityInfo -  Value Object which containes the decrypted data.
   * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
   */

  public SecurityInfo decryptAndVerify(SecurityInfo securityInfo)
    throws SecurityServiceException
  {
    //     File tempDir = new File("/");
    SecurityLogger.debug(
      "[SecurityServiceBean][decruptAndVerify()] In Decrypt ");
    try
    {
      SecurityLogger.log(
        "Begin to import signature, envelope from input file ");

      File encryptedFile;
      String decryptedData[] = {
      };
      String[] encryptedStringArray = securityInfo.getStringArrayToDecrypt();
      if (encryptedStringArray != null)
      {
        decryptedData = new String[encryptedStringArray.length];
        SecurityLogger.log(
          "[SecurityServiceBean]"
            + "[decruptAndVerify()]"
            + " Length of StringArray is "
            + encryptedStringArray.length);
        for (int i = 0; i < encryptedStringArray.length; i++)
        {
          SecurityLogger.log(
            "[SecurityServiceBean]"
              + "[decryptAndVerify()]"
              + "String B4 Decrypting "
              + encryptedStringArray[i]);
          if (encryptedStringArray[i] == null) //don't decrypt if null
            decryptedData[i] = null;
          else
          {
            encryptedFile =
              File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
            byte[] data =
              encryptedStringArray[i].getBytes(ISecurityInfo.ENCODING);
            GridCertUtilities.writeByteTOFile(encryptedFile, data, data.length);
            byte[] encryptedString =
              decryptAndVerify(encryptedFile, securityInfo);
            String encStr = new String(encryptedString, ISecurityInfo.ENCODING);
            SecurityLogger.log(
              "[SecurityServiceBean]"
                + "[decryptAndVerify]"
                + "String After Decrypting "
                + encStr);
            decryptedData[i] = encStr;
          }
        }
      }

      SecurityInfo tinfo = new SecurityInfo();
      String encryptedFileString = securityInfo.getFileToDecrypt();
      byte[] data1;

      if (encryptedFileString != null
        || securityInfo.getStringToDecrypt() != null
        || securityInfo.getDataToDecrypt() != null)
      {
        if (encryptedFileString == null)
        {
          byte[] data;
          encryptedFile =
            File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
          encryptedFile.deleteOnExit();
          String stringtoEncrypt = securityInfo.getStringToDecrypt();
          if (stringtoEncrypt != null)
            data = stringtoEncrypt.getBytes(ISecurityInfo.ENCODING);
          else
            data = securityInfo.getDataToDecrypt();
          GridCertUtilities.writeByteTOFile(encryptedFile, data, data.length);
        }
        else
        {
          encryptedFile = new File(encryptedFileString);
        }
        data1 = decryptAndVerify(encryptedFile, securityInfo);
        if (securityInfo.getOutputFile() != null)
        {
          SecurityLogger.log("Setting the OutputFile in SecurityInfo ");
          tinfo.setOutputFile(securityInfo.getOutputFile());
        }
        if (data1 != null)
          tinfo.setDecryptedData(data1);
        else
          SecurityLogger.log("Data is Null");
      }
      if (decryptedData != null)
        tinfo.setDecryptedStringArray(decryptedData);
      SecurityLogger.log("Set the Decrypted Data Before Returing");
      return tinfo;
    }
    catch (Exception ex)
    {
      SecurityLogger.debug(ex.getMessage());
      throw new SecurityServiceException(
        "Cannot Perform DecryptAndVerify ",
        ex);
    }

  }

  private boolean checkFieldID(byte[] fileIdentifier, byte[] fileIDBuffer)
  {
    // check the identifier
    for (int i = 0; i < fileIdentifier.length; i++)
    {
      if (fileIdentifier[i] != fileIDBuffer[i])
      {
        return false;
      }
    }
    return true;
  }
  
  public SMimeSecurityInfo encrypt(SMimeSecurityInfo info)
    throws SecurityServiceException
  {
    try
    {
      SecurityLogger.debug("Encrypt Start of SMIME ");
      X509Certificate recpCertificate = info.getReceipentCertificate();
      byte[] partToEncrypt = info.getDataToEncrypt();
      if (partToEncrypt == null)
      {
        SecurityLogger.debug("Part to Encrypt From IMailpart ");
        partToEncrypt = info.getIMailpartToEncrypt().getContentByte(false);
      }
      
      byte[] encryptData =
        encrypt(
          recpCertificate,
          info.getOwnCertificate(),
          partToEncrypt,
          info.getEncryptionAlgorithm(),
          info.getEncryptionLevel());
      SecurityLogger.log("After Encryption   " + encryptData);
      SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
      iinfo.setEncryptedData(encryptData);
      return iinfo;
    }
    catch (Exception ex)
    {
      SecurityLogger.log("Exception in Encrypt Main  " + ex.getMessage());
      ex.printStackTrace();
    }
    return null;

  }
  
  public byte[] encrypt(
                        X509Certificate partnerCertificate,
                        X509Certificate ownCertificate,
                        byte[] contentToEncry,
                        String encryptionAlgorithm,
                        int encryptionLevel)
                        throws SecurityServiceException
  {
    try
    {
      CMSProcessable content = createDataMessageJava(contentToEncry);
      CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
      gen.addKeyTransRecipient(partnerCertificate);
      
      SecurityLogger.debug("Recipent Information "
                                + partnerCertificate.getIssuerX500Principal()
                                + "Serail No "
                                + partnerCertificate.getSerialNumber()
                                + "Length    "
                                + partnerCertificate.getSerialNumber().toByteArray().length);
      
      //TWX 24072006: We can decrypt the document we encrypted. RSA support multiple recipient
      if(ownCertificate != null)
      {
        //the own cert can be null eg in AS2's SecurityProfile, we can specify encryption only, without sign cert
        gen.addKeyTransRecipient(ownCertificate);
        SecurityLogger.debug("Self As Recipent Information "
                             + ownCertificate.getIssuerX500Principal()
                             + "Serail No "
                             + ownCertificate.getSerialNumber()
                             + "Length    "
                             + ownCertificate.getSerialNumber().toByteArray().length);
      }

      
      CMSEnvelopedData enveloped = gen.generate(content, encryptionAlgorithm, encryptionLevel, GridCertUtilities.getSecurityProvider());
      return enveloped.getEncoded();
    }
    catch (Exception e)
    {
      SecurityLogger.log("Exception in encryptData  " + e.getMessage());
      throw new SecurityServiceException("Encrypt exception ", e);
    }
 }
  
  
  public byte[] encrypt(
    X509Certificate partnerCertificate,
    byte[] contentToEncry,
    X509Certificate recpCertificate,
    PrivateKey privateKey)
    throws SecurityServiceException
  {
    try
    {
      CMSProcessable content = createDataMessageJava(contentToEncry);
      CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
      gen.addKeyTransRecipient(partnerCertificate);
      
      SecurityLogger.log(
                         " Recipent Information "
                           + partnerCertificate.getIssuerX500Principal()
                           + " "
                           + partnerCertificate.getSerialNumber()
                           + " "
                           + partnerCertificate.getSerialNumber().toByteArray().length);
      
      //data.setEncryptionAlgorithm("RC5-0x10-32-16/CBC/PKCS5Padding", 128);
      //TWX 20090716 CertJ IMPL is using the RC5 algo for generating the symmetrix key; but we could not find
      //the replacement in BC impl. Such method is refered by SMIMESecurityServiceFacade.encrypt(...)
      //and the SMIMESecurityServiceFacade.encrypt(...) is not refered by anyone else.
      
      CMSEnvelopedData enveloped = gen.generate(content, CMSEnvelopedDataGenerator.DES_EDE3_CBC, 168, GridCertUtilities.getSecurityProvider());
      return enveloped.getEncoded();
    }
    catch (Exception e)
    {
      SecurityLogger.log("Exception in encryptData  " + e.getMessage());
      throw new SecurityServiceException("Encrypt exception ", e);
    }
  }

  public SMimeSecurityInfo decrypt(SMimeSecurityInfo info)
    throws SecurityServiceException
  {
    try
    {
      SecurityLogger.log("[SecurityServiceBean][decrypt()] Begin Decrypt ");
      
      byte[] decryptData =
        decrypt(info.getDataToDecrypt());
      SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
      iinfo.setDecryptedData(decryptData);
      SecurityLogger.log("[SecurityServiceBean][decrypt()] Decrypt Success ");
      return iinfo;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.log("Exception in DecryptData  " + ex.getMessage());
      throw new SecurityServiceException("Decryption exception ", ex);
    }
  }
  
  public byte[] decrypt(byte[] contentForDecrypt,X509Certificate ownCert, PrivateKey privateKey)throws SecurityServiceException
  {
    try
    {
      CMSEnvelopedData envelop = new CMSEnvelopedData(contentForDecrypt);
      RecipientInformationStore recInfoStore = envelop.getRecipientInfos();
      
      
      if(ownCert != null && privateKey != null)
      {
        SecurityLogger.debug("decrypt via pre-allocated cert: subjectCert--> "+ownCert.getSubjectX500Principal()+" issuerCert --> "+ownCert.getIssuerX500Principal());
        RecipientId recID = new RecipientId();
        recID.setIssuer(ownCert.getIssuerX500Principal().getEncoded());
        recID.setSerialNumber(ownCert.getSerialNumber());
        RecipientInformation recInfo = recInfoStore.get(recID);
        if(recInfo != null)
        {
          return recInfo.getContent(privateKey, GridCertUtilities.getSecurityProvider());
        }
        else
        {
          throw new SecurityServiceException("RecipientId "+recID+" is not appropriate for decryption !");
        }
      }

      else
      {
        
        SecurityLogger.log("Either ownCert or privateKey is null No enough cert info for decryption! Try decrypt from db ");
        return decrypt(contentForDecrypt);
      }
    }
    catch(SecurityServiceException ex)
    {
      SecurityLogger.warn("Exception in Decrypt ", ex);
      throw ex;  
    }
    catch(Throwable ex)
    {
      SecurityLogger.warn("Exception in Decrypt ", ex);
      throw new SecurityServiceException("Error in decrypting the content. Error is "+ex.getMessage(), ex);
    }
  }
  
  public byte[] decrypt(byte[] contentForDecrypt) throws SecurityServiceException
  {
    try
    {
      CMSEnvelopedData envelop = new CMSEnvelopedData(contentForDecrypt);
      RecipientInformationStore recInfoStore = envelop.getRecipientInfos();

      //The previous RSA CertJ impl can infer the cert used to be decrypted although the GT Cert Mapping is configured
      //a wrong cert. We follow the approach and apply it to BC impl.
      
      //Let's try check whether any cert in DB suitable for decryption      
      //load the certificate based on the recipient info, we support decrypt given multiple encryptor info
      Collection recipients = recInfoStore.getRecipients();
      if(recipients != null && recipients.size() > 0)
      {
        Iterator recipientInfoIte = recipients.iterator();
        while(recipientInfoIte.hasNext())
        {
          RecipientInformation recInfo = (RecipientInformation)recipientInfoIte.next();
          Certificate cert = loadCertificateByCertSelector(recInfo.getRID(), false); //TWX 20090924 retrieve the our own cert only, as partner can potentially
                                                                                     //use their cert to encrypt, and we will have such partner cert in
                                                                                     //our system for verification.
          
          if(cert != null) 
          {
            return recInfo.getContent(GridCertUtilities.loadPrivateKeyFromString(cert.getPrivateKey()), GridCertUtilities.getSecurityProvider()); 
          }
          else //can not find the corresponding cert for decryption, let's try the other
          {
            continue;
          }
        }
        
        throw new SecurityServiceException("No appropriate cert can be found for decryption ");
      }
      else
      {
        //unreachable step, unless the sender construct the Enveloped data wrongly.
        throw new SecurityServiceException("No recipientInfo found from EnvelopedData! ");
      }
    }
    catch(SecurityServiceException ex)
    {
      SecurityLogger.warn("Exception in Decrypt ", ex);
      throw ex;  
    }
    catch(Throwable ex)
    {
      SecurityLogger.warn("Exception in Decrypt ", ex);
      throw new SecurityServiceException("Error in decrypting the content. Error is "+ex.getMessage(), ex);
    }
    
  }

//  public byte[] decrypt(
//    byte[] partToDecryped)
//    throws SecurityServiceException
//  {
//    return decrypt(partToDecryped);
//  }

  public SMimeSecurityInfo sign(SMimeSecurityInfo info)
    throws SecurityServiceException
  {  	
    try
    {
      X509Certificate ownCertificate = info.getOwnCertificate();
      String encryptionAlgorithm = info.getEncryptionAlgorithm();
      String digestAlgorithm = info.getDigestAlgorithm();
      byte[] partToSign = info.getDataToSign();
      //signInfo = new SMimeSecurityInfo();
      if (partToSign == null)
      {
        partToSign = info.getPartToSign().getContentByte(false);
        SecurityLogger.debug("[SecurityServiceBean][sign] Data from MailPart");
      }
      SecurityLogger.debug("Begin of Sign ");
      SecurityLogger.debug(
        "ownCertificate " + ownCertificate.getSerialNumber().toString());
      SecurityLogger.debug("EncryptionAlgorithm " + encryptionAlgorithm);
      SecurityLogger.debug("digestAlgorithm " + digestAlgorithm);

      byte[] signedData = sign(info.getPrivateKey(), ownCertificate, digestAlgorithm, partToSign);
      info.setSignedData(signedData);
      return info;
    }
    catch (Exception ex)
    {
      SecurityLogger.warn("Cannot Sign Data ", ex);
      throw new SecurityServiceException("Cannot Sign Data ", ex);
    }
  }

  private byte[] sign(PrivateKey privateKey, X509Certificate ownCertificate,
                           String digestAlgo, byte[] dataToSign) throws SecurityServiceException
  {
    
    try
    {
      SecurityLogger.debug("Start sign content, digestAlgo: "+digestAlgo);
      CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
      gen.addSigner(privateKey, ownCertificate, digestAlgo);
      
      //As per RFC3852, certs and CRLs are optional in the SignedData
      CertStore certs = CertStore.getInstance(
                            "Collection", new CollectionCertStoreParameters(
                                                  Arrays.asList(ownCertificate)), GridCertUtilities.getSecurityProvider());
      gen.addCertificatesAndCRLs(certs);
      
      //Generate detach signed data
      CMSProcessable data = new CMSProcessableByteArray(dataToSign);
      CMSSignedData signedData = gen.generate(data, GridCertUtilities.getSecurityProvider()); 
      
      return signedData.getEncoded();
    }
    catch(Exception ex)
    {
      SecurityLogger.log("Can't sign content in CMS: "+ex.getMessage());
      throw new SecurityServiceException("Can't sign content in CMS :"+ex.getMessage(), ex);
    }
  }
  
  /**
   * This method verify the SMIME Message
   *
   * @param info - SMIMESecurityInfo -value object, to encapsulates the Security Information.
   * @return SMimeSecurityInfo
   * @throws SecurityServiceException - thrown when cannot verify the message.

   */
  public SMimeSecurityInfo verify(SMimeSecurityInfo info) throws SecurityServiceException
  {
    
    SecurityLogger.debug("Verifying cms");
    byte[] contentInfo = info.getDataToVerify();
    byte[] signature = info.getSignatureToVerify();
    
    try
    {
      boolean isVerifySuccess = verify(contentInfo, signature, info.getReceipentCertificate());
      if(isVerifySuccess)
      {
        return info;
      }
      else
      {
        throw new SecurityServiceException("CMS Verification failed.");
      }
    }
    catch(SecurityServiceException ex)
    {
      SecurityLogger.error(ILogErrorCodes.CMS_VERIFICATION_FAILED,"CMS verification failed:"+ex.getMessage(), ex);
      throw ex;
    }
  }
  
  private boolean verify(byte[] contentInfo, byte[] signature, X509Certificate partnerCert) throws SecurityServiceException
  { 
    try
    {
      SecurityLogger.debug("Verifying content and signature");     
      CMSProcessable data = null;
      boolean isVerifiedSuccess = false;
      
      if(contentInfo != null)
      {
        data = new CMSProcessableByteArray(contentInfo);
      }
      
      CMSSignedData signedData = null;
      if(data == null) //content is part of the signature
      {
        SecurityLogger.debug("Signature encapsulated case");
        signedData = new CMSSignedData(signature);
      }
      else //signature detached case : signature and content to be verified is seperated
      {
        signedData = new CMSSignedData(data, signature);
      }
      
      SignerInformationStore signInfoStore = signedData.getSignerInfos();      
      
      if(signInfoStore != null && signInfoStore.getSigners() != null && signInfoStore.getSigners().size() > 0 )
      {
        Iterator signInfoIte = signInfoStore.getSigners().iterator();
        while(signInfoIte.hasNext())
        {
          SignerInformation signerInfo = (SignerInformation)signInfoIte.next();
          X509CertSelector signerConstraint = signerInfo.getSID();
          
          X509Certificate certForVerficiation = loadX509CertificateByCertSelector(signerConstraint, true);//we will use partner cert for verification
  
          //TWX 20090924 continue search for the cert if no cert associated with signer constraint 
          if(certForVerficiation == null)
          {
            continue;
          }
          
          //We can ignore the validation of the public cert by building a cert path since 
          //we already trusted the cert (user has imported into the system)
  //        PKIXCertPathBuilderResult certPathBuildResult = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.
  //                                                                  getCertPathBuilderResult(signerConstraint, false, partnerCert, GridCertUtilities.SEC_PROVIDER_BC);
          isVerifiedSuccess = signerInfo.verify(certForVerficiation, GridCertUtilities.getSecurityProvider());
          if(isVerifiedSuccess)
          {
            break;
          }
          else
          {
            continue;
          }
        }  
        return isVerifiedSuccess;
      }
      else
      {
        SecurityLogger.log("No signer information found !");
        return false;
      }
    }
    catch(Throwable ex)
    {
      throw new SecurityServiceException("Can't verify CMS content: "+ex.getMessage(), ex);
    }
  }
  
  /**
   * Load the certificate from DB given the cert selector info in the Signature
   * @param certSelector cert selector info in the signature
   * @return cert selector corresponding X509Cert
   * @throws Throwable if error in loading the cert from db.
   */
  private X509Certificate loadX509CertificateByCertSelector(X509CertSelector certSelector, boolean isPartner) throws Throwable
  {    
    Certificate retrievedCert = loadCertificateByCertSelector(certSelector, isPartner);
    if(retrievedCert != null)
    {
      return GridCertUtilities.loadX509Certificate(GridCertUtilities.loadByteArrayFromString(retrievedCert.getCertificate()));
    }
    else
    {
      return null;
    }
  }
  
  /**
   * Load the certificate from DB given the cert selector info in the Signature
   * @param certSelector cert selector info in the signature
   * @return cert selector corresponding Certificate entity
   * @throws Throwable if error in loading the cert from db.
   */
  private Certificate loadCertificateByCertSelector(X509CertSelector certSelector, boolean isPartnerCert) throws Throwable
  {
    SecurityLogger.debug("Cert Selector Info: "+certSelector);
    
    String issuerName = GridCertUtilities.writeIssuerNameToString(certSelector.getIssuer());
    String serialNumber = GridCertUtilities.writeByteArrayToString(certSelector.getSerialNumber().toByteArray());
    SecurityLogger.debug("Cert Issuer name: "+issuerName);
    SecurityLogger.debug("Cert Serial number: "+serialNumber);
    CertificateEntityHandler certHandler = CertificateEntityHandler.getInstance();
    Certificate cert = certHandler.findCertificateByIssureAndSerialNum(issuerName, serialNumber, isPartnerCert);
    return cert;
  }
  
  public byte[] sign(
    X509Certificate signerCertificate,
    byte[] partToSign,
    PrivateKey privateKey)
    throws SecurityServiceException
  {
    try
    {
      return sign(privateKey, signerCertificate, CMSSignedDataGenerator.DIGEST_SHA1, partToSign);
    }
    catch (Exception e)
    {
      SecurityLogger.log("Cannot Sign :  Sign Exception  ");
      throw new SecurityServiceException("Sign Exception", e);
    }
  }

  public boolean verify(
    byte[] contentInfoEncoding,
    byte[] signature,
    X509Certificate recpCertificate,
    PrivateKey privateKey)
    throws SecurityServiceException
  {
    try
    {

      return verify(contentInfoEncoding, signature, recpCertificate);
    }
    catch (Exception e)
    {
      SecurityLogger.warn("Verify Raw Data: Exception", e);
      throw new SecurityServiceException("Verify failed : Verify Raw Data: Exception");
    }
  }

  private CMSProcessable createDataMessageJava(byte[] content)
  {
    return new CMSProcessableByteArray(content);
  }

  public void setSessionContext(SessionContext scx)
  {
    _ctx = scx;
  }

  public void ejbCreate()
  {
    //SecurityLogger.log("[SecurityServiceBean][ejbCreate()] In EJB Create ");
    //_fileAccess = new FileAccess(IPathConfig.APP_DOMAIN);
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  private String getRandom()
  {
    //return Double.toString(new Random(System.currentTimeMillis()).nextDouble());
    return UUIDUtil.getRandomUUIDInStr();
  }

  /* This method is not a part of EJB Spec., Only here for test purpose. */

  /*  public static void main(String args[])
    {
      try
      {
          SMimeSecurityInfo secinfo = new SMimeSecurityInfo();
          Provider memProvider = new MemoryDB("Test Memory DB");
          Provider pkixCertPathProvider = new PKIXCertPath ("PKIX Cert Path Verifier");
          Provider[] providers ={ memProvider, pkixCertPathProvider };
          CertJ certJ = new CertJ (providers);
          X509Certificate cert = GridCertUtilities.loadX509Certificate("d:/security/classes/data/mtes.cer");
          JSAFE_PublicKey  publickey = cert.getSubjectPublicKey("Java");
          DatabaseService dbService =
            (DatabaseService)certJ.bindServices (CertJ.SPT_DATABASE);
          PKCS12Reader reader = new PKCS12Reader("d:/security/classes/data/pp12.p12","xx".toCharArray());
          reader.read();
          JSAFE_PrivateKey prKey = reader.getPrivateKey();
          dbService.insertCertificate(cert);
  //        dbService.insertPrivateKeyByCertificate(cert,prKey);
          Certificate [] certs = new Certificate[1];
          certs[0] = cert;
          CertPathCtx pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
                                   certs,
                                   null, new Date(), dbService);
          String content = "This is the message to do ...";
          secinfo.setCertJ(certJ);
          secinfo.setDataToEncrypt(content.getBytes());
          secinfo.setReceipentCertificate(cert);
          secinfo.setCertPathContext(pathCtx);

          SecurityServiceBean bean = new SecurityServiceBean();
          //SMimeSecurityInfo ii = bean.encrypt(secinfo);
          //SecurityLogger.log(" In Main Return  "+ii.getEncryptedData());

  //        providers ={ memProvider };
  //        certJ = new CertJ (providers);
            X509Certificate cert1 = GridCertUtilities.loadX509Certificate("d:/security/classes/data/mtes.cer");
            publickey = cert1.getSubjectPublicKey("Java");
            dbService =
           (DatabaseService)certJ.bindServices (CertJ.SPT_DATABASE);
            reader = new PKCS12Reader("d:/security/classes/data/mtes.p12","xx".toCharArray());
            reader.read();
            prKey = reader.getPrivateKey();
            dbService.insertCertificate(cert1);
            dbService.insertPrivateKeyByCertificate(cert1,prKey);
            certs = new Certificate[1];
            certs[0] = cert1;
            pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
                                   certs,
                                   null, new Date(), dbService);


  //        SMimeSecurityInfo decrypt = new SMimeSecurityInfo();
  //        decrypt.setCertJ(certJ);
  //        decrypt.setDataToDecrypt(ii.getEncryptedData());
  //        decrypt.setReceipentCertificate(cert);
  //        decrypt.setCertPathContext(pathCtx);
  //        bean.decrypt(decrypt);
            SMimeSecurityInfo signinfo = new SMimeSecurityInfo();
            signinfo.setCertJ(certJ);
            signinfo.setReceipentCertificate(cert1);
            signinfo.setCertPathContext(pathCtx);
            signinfo.setDataToSign(content.getBytes());
            SMimeSecurityInfo ipinfo = bean.sign(signinfo);
            SecurityLogger.log("Signed info  "+new String(ipinfo.getSignedData()));
            //IMailpart part = ipinfo.getSignedPart();
            X509Certificate cert2 = GridCertUtilities.loadX509Certificate("d:/security/classes/data/pp.cer");
            publickey = cert2.getSubjectPublicKey("Java");
            dbService =
           (DatabaseService)certJ.bindServices (CertJ.SPT_DATABASE);
            reader = new PKCS12Reader("d:/security/classes/data/mtes.p12","xx".toCharArray());
            reader.read();
            prKey = reader.getPrivateKey();
            dbService.insertCertificate(cert2);
            dbService.insertPrivateKeyByCertificate(cert2,prKey);
            certs = new Certificate[1];
            certs[0] = cert2;
            pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
                                   certs,
                                   null, new Date(), dbService);


            boolean b = bean.verify(certJ,pathCtx,content.getBytes(),ipinfo.getSignedData());
            SecurityLogger.log(" What is it Verified  "+b);
            //SecurityLogger.log("From Part  "+part.getContentByte(false));
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }



   //  try
   //  {
  /*
        SecurityInfo info = new SecurityInfo();
        info.setSenderCertificateFile("d:/mtes.cer");
        PKCS12Reader reader = new PKCS12Reader("d:/pp12.p12","xx".toCharArray());
        reader.read();
        JSAFE_PrivateKey prKey = reader.getPrivateKey();
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
        info.setFileToDecrypted(new File("d:/security/encypt.txt"));
        SecurityLogger.log("File Size is [[]]]] "+new File("d:/security/derencypt.txt").length());
        info.setOutputFile(new File("d:/security/decryptf.txt"));

        SecurityServiceBean bean = new SecurityServiceBean();
        SecurityInfo iinfo = bean.decryptAndVerify(info);
        iinfo.getDecryptedOutputFile();
        System.out.println("Decrypted String from main"+iinfo.getDecryptedOutputString());


        //JSAFE_PrivateKey pk = GridCertUtilities.loadPKCS8PrivateKeyFile("new","d:/security/netrust.pfx");
       // GridCertUtilities.writePKCS8PrivateKeyFile("new","d:/security/pk.pri",pk);

  //      info.setReceipentCertificateFile("d:/pp.cer");
  //      PKCS12Reader reader = new PKCS12Reader("d:/mtes.p12","xx".toCharArray());
  //      reader.read();
  //      JSAFE_PrivateKey prKey = reader.getPrivateKey();
  //      if(prKey != null)
  //      {
  //          byte a[] = GridCertUtilities.writePKCS8PrivateKeyData("pw",prKey);
  //          if(a != null)
  //          {
  //            info.setOwnPrivateKey(a);
  //          }
  //          else
  //          {
  //             SecurityLogger.log("from reading byte array is null ");
  //          }
  //      }
  //      else
  //      {
  //        SecurityLogger.log("Private Key Cannot Be Loaded .. ");
  //      }
  //      info.setPassword("pw".toCharArray());
  //      //info.setOwnPrivateKey(GridCertUtilities.writePKCS8PrivateKeyData("",prKey));
  //      info.setFileToEncrypt(new File("d:/security/mydoc.txt"));
  //      info.setOutputFile(new File("d:/security/encypt.txt"));
  //      info.setSign(true);

  /*      info.setSenderCertificateFile("d:/security/netrust.cer");
        info.setOwnPrivateKeyFile("d:/security/idsafe.p12");
        info.setFileToDecrypted(new File("d:/security/encypt.txt"));
        info.setOutputFile(new File("d:/security/decrypt22.txt"));
    */
  //info.setSign(false);

  //      SecurityServiceBean bean = new SecurityServiceBean();
  //      SecurityInfo iinfo = bean.encryptAndSign(info);
  //      System.out.println("Encrypted String from main"+iinfo.getEncryptedOutputString());

  //     }
  /*     catch(Exception ex)
       {
         ex.printStackTrace();
       }

        /*      try{
        FileInputStream fis = new FileInputStream(iinfo.getOutputFile());
        byte b[] = new byte[166];
        int i;
        while((i = fis.read(b)) != -1)
        {
          System.out.println("Read From File  "+b.length);

        }
        }catch(Exception ex)
        {
          ex.printStackTrace();
        }*/

  //}
  
  private byte[] encryptAndSign(File inputFile, SecurityInfo securityInfo)
    throws SecurityServiceException
  {
    try
    {
      PublicKey publicKey = null;
      PrivateKey privateKey = null;
      Signature signer = null;
      FileInputStream fis = null;
      FileOutputStream fos = null;
      DataInputStream dis = null;
      DataOutputStream dos = null;
      Key rc4Key = null;
      
      byte[] signature = null;

      int RC4KeyLength = 512; //default key length
      byte[][] rc4KeyData = null;
      SecureRandom random = null;
      int blockSize = 4096;
      byte[] inputBlock = new byte[blockSize];
      byte[] outputBlock = new byte[blockSize];

      byte[] envelopeOutput = null;
      int envelopeOutputLen;
      File tempFile =
        File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
      //tempFile.deleteOnExit(); //Delete this file after Processing, as this may
      //eat up disk space.

      /** Load Public Key  **/
      SecurityLogger.log("Loading Certificate  Step #1");
      byte[] certificateData = securityInfo.getReceipentCertificate();
      X509Certificate cert;
      if (certificateData != null)
      {
        cert = GridCertUtilities.loadX509Certificate(certificateData);
        SecurityLogger.log("[Recipient Certificate From Certificate Data]");
      }
      else
      {
        cert =
          GridCertUtilities.loadX509Certificate(
            securityInfo.getReceipentCertificateFile());
        SecurityLogger.log("Certificate From Certificate File ");
      }
      /** Get Public Key From Certificate **/
      SecurityLogger.log("Loading Public Key  Step #2");
      if (cert != null)
      {
        publicKey = cert.getPublicKey();
      }

      /** Get Private Key **/

      if (securityInfo.isSign())
      {
        String exceptionMessage = null;
                                SecurityLogger.log("[Loading Own Private Key  Step #3]");
        byte ar[] = securityInfo.getOwnPrivateKey();
        char[] privatepassword = securityInfo.getPassword();
        if (ar == null)
          SecurityLogger.log("[Own Private Key is Null]");
        if (privatepassword != null)
        {
          if ((securityInfo.getOwnPrivateKey() == null)
            && (securityInfo.getOwnPrivateKeyFile() == null))
          {
            exceptionMessage = "[SecurityServiceBean][encryptAndSign()][Cannot Sign as PrivateKey is Null or Not Set]";
            SecurityLogger.warn(exceptionMessage);
            throw new SecurityServiceException(exceptionMessage);
                                                /** @todo Lets throw this as a Exception. After Testing... */
          }
          if (securityInfo.getOwnPrivateKey() != null)
          {
            SecurityLogger.log("[Private Key Data Source = [DB or Bytes] set to SecurityInfo]");
//            SecurityLogger.debug(
//              "[****]" + String.valueOf(securityInfo.getPassword()));
            privateKey =
              GridCertUtilities.loadPKCS8PrivateKeyData(privatepassword, ar);
          }
          else
          {
            SecurityLogger.log("[Private Key Data Source=[File]]");
            privateKey =
              GridCertUtilities.loadPKCS8PrivateKeyFile(
                String.valueOf(privatepassword),
                securityInfo.getOwnPrivateKeyFile());
          }
        }
        else
        {
          //Here the password is empty String as you never know the password of which has encrypted with.
          exceptionMessage = "[Please Provide a password of this CertStore File]";
                                        SecurityLogger.log(exceptionMessage);
          throw new SecurityServiceException(exceptionMessage);
        }
      }
      /** Set Key Length **/

      RC4KeyLength = securityInfo.getKeyLength();
      SecurityLogger.log("[Key Length Step #3 = ]" + RC4KeyLength);

      if (securityInfo.isSign())
      {
        //LET'S SIGN IT!
        try
        {
          //Get The Signature Algorithm . And Pass it to JSAFE_Signature.
          //Please wait .... to be confirmed ...07/10/2002
          fis = new FileInputStream(inputFile);
          String digestAlgo = securityInfo.getDigestAlgorithm();
          if (digestAlgo != null)
          {
            if (digestAlgo.equals(ISecurityInfo.DIGEST_ALGORITHM_MD5))
            {
              signer = Signature.getInstance("MD5withRSA", GridCertUtilities.getSecurityProvider());
            }
            else if (digestAlgo.equals(ISecurityInfo.DIGEST_ALGORITHM_SHA1))
            {
              signer = Signature.getInstance("SHA1withRSA", GridCertUtilities.getSecurityProvider());
            }
          }
          else
          {
            SecurityLogger.log(
              "[SecurityServiceBean][encryptAndSign]"
                + "[Using Default DigetsAlgorithm - MD5]");
            signer = Signature.getInstance("MD5withRSA", GridCertUtilities.getSecurityProvider());
          } 
          signer.initSign(privateKey);//Priavate key of the signer.
          
          
          int bytesRead = 0;
          while ((bytesRead = fis.read(inputBlock)) != -1)
          {
            signer.update(inputBlock, 0, bytesRead);
          }
          signature = signer.sign();
          fis.close();
        }
        catch (Exception ex)
        {
          SecurityLogger.warn("Exception during Signing " + ex.getMessage());
          throw new SecurityServiceException("Exception during Signing  ", ex);
        }
      } // end if sign
      //MULTI-BLOCK Bulk Data Symmetric encryption
      try
      {
        SecurityLogger.log("[Data Symmetric encryption ##]");
        Cipher rc4Encryptor = Cipher.getInstance("RC4", GridCertUtilities.getSecurityProvider());
        // random num generate
        try
        {
          SecurityLogger.log("[Random Number Generator ##]");
          random = getRandomNum();
//            (JSAFE_SecureRandom) JSAFE_SecureRandom.getInstance(
//              "SHA1Random",
//              "Java");
          random.setSeed(System.currentTimeMillis());
        }
        catch (NoSuchAlgorithmException nsaException)
        {
          SecurityLogger.warn(
            "[Unable to instantiate and seed ]" + nsaException.getMessage());
          throw new SecurityServiceException(
            "[Unable to instantiate and seed ]",
            nsaException);
        }
        // session key generate
        //int[] keyParams = { RC4KeyLength };
        try
        {
            rc4Key = generateSymmKey("RC4", RC4KeyLength, random);
            
        }
        catch (Exception ex)
        {
          SecurityLogger.warn(
            "[Unable to Generate RC4 key generation.]" + ex.getMessage());
          throw new SecurityServiceException(
            "[Unable to instantiate and seed ]",
            ex);
        }
        // bulk encrypted begin
        SecurityLogger.log("[Bulk Encryption Begin]");
        fis = new FileInputStream(inputFile);
        dis = new DataInputStream(fis);

        fos = new FileOutputStream(tempFile);
        dos = new DataOutputStream(fos);

        //rc4Encryptor.encryptInit(rc4Key, random);
        rc4Encryptor.init(Cipher.ENCRYPT_MODE, rc4Key, random); 
        
        int bytesRead = 0;
        int partOut = 0;

        while ((bytesRead = dis.read(inputBlock)) != -1)
        {
          partOut =
            rc4Encryptor.update(
              inputBlock,
              0,
              bytesRead,
              outputBlock,
              0);
          dos.write(outputBlock, 0, partOut);
        }
        
        
        int finalOut = rc4Encryptor.doFinal(outputBlock, 0);
        dos.write(outputBlock, 0, finalOut);
        dos.close();
        dis.close();
      }
      catch (Exception ex)
      {
        SecurityLogger.warn(
          "[Exception encountered during bulk encryption.]" + ex.getMessage());
        throw new SecurityServiceException(
          "[Exception encountered during bulk encryption.]",
          ex);
      }

      //END of MULTI-BLOCK Symmetric encryption
      //BEGIN to wrap the session key
      try
      {
        SecurityLogger.log("[Wrap Session Key]");
//        JSAFE_AsymmetricCipher rsaEncryptor =
//          JSAFE_AsymmetricCipher.getInstance("RSA/PKCS1Block02Pad", "Java");
        Cipher rsaEncryptor = Cipher.getInstance("RSA/NONE/PKCS1Padding", GridCertUtilities.getSecurityProvider());
        
        /* Initialize the random number generator.  The following method
         * of seeding is insecure, and should not be used in a finished
         * application. */
        rsaEncryptor.init(Cipher.ENCRYPT_MODE, publicKey, random);
        
        //rsaEncryptor.encryptInit(publicKey, random);
        
        //TODO: check the replacement of the following... BC impl is using feedback RSA OAEP, thus need IV
        byte[] rc4KeyInByte = rc4Key.getEncoded();
        int dataLen = rsaEncryptor.getOutputSize(rc4KeyInByte.length);
        
        // int dataLen = 1000;

        envelopeOutput = new byte[dataLen];
        int partOut =
          rsaEncryptor.update(
            rc4KeyInByte,
            0,
            rc4KeyInByte.length,
            envelopeOutput,
            0);
        int finalOut = rsaEncryptor.doFinal(envelopeOutput, partOut);
        envelopeOutputLen = partOut + finalOut;
        //rsaEncryptor.clearSensitiveData();
      }
      catch (Exception ex)
      {
        SecurityLogger.warn("Exception during WRAPKEY. " + ex.getMessage());
        throw new SecurityServiceException("Exception during WRAPKEY. ", ex);
      }
      //END of wrap the session key

      //BEGIN to gererate output file

      SecurityLogger.log("[Generate Output File]");
      byte[] fileIdentifier = null;

      if (securityInfo.isSign())
        fileIdentifier = SecurityInfo.FILE_ID_SIGN;
      else
        fileIdentifier = SecurityInfo.FILE_ID_NOT_SIGN;

      SecurityLogger.log("[File Identifier is]" + fileIdentifier);
      File outputFile;
      //String outputFileString = securityInfo.getOutputFile();
      /*      if(outputFileString == null) Pls note this part is commented to make this FileServices Friendly.
      *                                     (Due to Domain Issues - And due consideration to EJB Specification).
            {
      */
      outputFile = File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);
      SecurityLogger.log("Output file: "+outputFile.getAbsolutePath());
      
      //outputFile.deleteOnExit(); //Delete this file on exit as the contents are written back to
      //      }                            // byte[].
      /*     else  Please not that this part is commented out to make this FileServices Friendly.
                   (Due to Domain Issues - And due consideration to EJB Specification)
            {
              outputFile = new File(outputFileString);//Do not delete this file, as this the actual file the
            }                                         // user wishes us to write.
       */
      try
      {
        /*********** READ FROM TEMPFILE**************/
        inputFile = tempFile;
        fis = new FileInputStream(inputFile);
        dis = new DataInputStream(fis);

        fos = new FileOutputStream(outputFile);
        dos = new DataOutputStream(fos);

        dos.write(fileIdentifier, 0, fileIdentifier.length);
        // write file identifier

        if (securityInfo.isSign())
        {
          // write signature length
          dos.writeInt(signature.length);
        }

        // write envolope length
        dos.writeInt(envelopeOutputLen);

        if (securityInfo.isSign())
        {
          // write signature Data
          dos.write(signature, 0, signature.length);
        }
        // write envelope Data
        dos.write(envelopeOutput, 0, envelopeOutputLen);

        // write cipher data
        int bytesRead = 0;
        while ((bytesRead = dis.read(inputBlock)) != -1)
        {
          dos.write(inputBlock, 0, bytesRead);
        }
        dis.close();
        dos.close();
      }
      catch (Exception ex)
      {
        SecurityLogger.warn(
          "[Exception encountered during Save output file.]" + ex.getMessage());
        throw new SecurityServiceException(
          "[Exception encountered during Save output file.]",
          ex);
      }

      byte data1[] = GridCertUtilities.loadFileToByte(outputFile);
      if (tempFile != null)
      {
        if (tempFile.exists())
          tempFile.delete();
      }
      if (outputFile != null)
      {
        if (outputFile.exists())
          outputFile.delete();
      }
      return data1;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.warn(ex.getMessage());
      throw new SecurityServiceException("Cannot Perform Encrypt And Sign ", ex);
    }

  }
  
  private byte[] decryptAndVerify(
    File encryptedFile,
    SecurityInfo securityInfo)
    throws SecurityServiceException
  {

    try
    {

      PublicKey publicKey = null;
      PrivateKey privateKey = null;
      X509Certificate certificate = null;

      FileInputStream fis = null;
      FileOutputStream fos = null;
      DataInputStream dis = null;
      DataOutputStream dos = null;

      byte[] envelope = null;
      byte[] signature = null;
      int envelopeLen = 0;

      int signatureLen = 0;
      byte[] secretKey = null;
      int secretKeyLen = 0;
      int blockSize = 4096;
      byte[] inputBlock = new byte[blockSize];
      byte[] outputBlock = new byte[blockSize];

      byte fileIdentifier[] = ISecurityInfo.FILE_ID_SIGN;
      byte[] fileIDBuffer = new byte[fileIdentifier.length];
      boolean sign = true;

      File decryptedOutputFile =
        File.createTempFile(SecurityInfo.PREFIX + getRandom(), null);

      fis = new FileInputStream(encryptedFile);
      dis = new DataInputStream(fis);

      SecurityLogger.log("Loading Certificate from SecurityInfo ");
      byte[] certificateData = securityInfo.getSenderCertificate();
      if (certificateData != null)
      {
        certificate = GridCertUtilities.loadX509Certificate(certificateData);
        SecurityLogger.log("Certificate from Data ");
      }
      else
      {
        if (securityInfo.getSenderCertificateFile() != null)
        {
          certificate =
            GridCertUtilities.loadX509Certificate(
              securityInfo.getSenderCertificateFile());
        }
        else
        {
          SecurityLogger.log("Certificate from File - But No File Set.");
        }

      }
      /** Get Public Key From Certificate **/
      if (certificate != null)
      {
        //publicKey = certificate.getSubjectPublicKey("Java");
        publicKey = certificate.getPublicKey(); 
      }

      SecurityLogger.log("After Getting PublicKey ");
      char[] password = securityInfo.getPassword();
      byte[] prkey = securityInfo.getOwnPrivateKey();
      if (securityInfo.getPassword() != null)
      {
        if (securityInfo.getOwnPrivateKey() != null)
        {
          //SecurityLogger.log("Own Password " + String.copyValueOf(password));
          privateKey = GridCertUtilities.loadPKCS8PrivateKeyData(password, prkey);
        }
        else
        {
          SecurityLogger.log("Private Key From File");
          privateKey =
            GridCertUtilities.loadPKCS8PrivateKeyFile(
              new String(),
              securityInfo.getOwnPrivateKeyFile());
        }
      }
      else
      {
        SecurityLogger.log("Please Provide a password of this CertStore File");
        throw new SecurityServiceException("Please Provide a password of this CertStore File");
      }
      // read file identifier
      if (dis.read(fileIDBuffer) != fileIdentifier.length)
        // read file identifier
      {
        SecurityLogger.warn("Error Reading Content, Format is Invalid ");
        throw new SecurityServiceException("Error Reading Content, Format is Invalid ");
      }

      if (checkFieldID(ISecurityInfo.FILE_ID_SIGN, fileIDBuffer))
        sign = true;
      else if (checkFieldID(ISecurityInfo.FILE_ID_NOT_SIGN, fileIDBuffer))
        sign = false;
      else
      {
        SecurityLogger.warn(" Error reading content, format is invalid ");
        throw new SecurityServiceException("Error reading content, format is invalid");
      }
      SecurityLogger.log("Sign Status " + sign);
      if (sign)
      {
        // read signature length
        signatureLen = dis.readInt();
        SecurityLogger.log("Signature Len: "+signatureLen);
      }
      // read signature & envelope length
      envelopeLen = dis.readInt();
      SecurityLogger.log("Envelope Len: "+envelopeLen);

      if (sign)
      {
        // read signature
        signature = new byte[signatureLen];
        if (dis.read(signature, 0, signatureLen) != signatureLen)
          // write file identifier
        {
          SecurityLogger.warn("Error verify content, signature is wrong");
          throw new SecurityServiceException("Error verify content, signature is wrong");
        }
      }
      // read envelope
      envelope = new byte[envelopeLen];
      if (dis.read(envelope, 0, envelopeLen) != envelopeLen) // write envelope
      {
        SecurityLogger.warn("Error reading envelope, wrong format");
        throw new SecurityServiceException("Error reading envelope, wrong format");
      }
      // UNSEAL The Envelope with the RSA private key
      try
      {
        SecurityLogger.log("UNSEAL The Envelope with the RSA private key");
        Cipher rsaDecryptor = Cipher.getInstance("RSA/NONE/PKCS1Padding", GridCertUtilities.getSecurityProvider());
        rsaDecryptor.init(Cipher.DECRYPT_MODE, privateKey);

        int outputSize = rsaDecryptor.getOutputSize(envelopeLen);
        secretKey = new byte[outputSize];

        SecurityLogger.log("Start the Decryption ");
        secretKey = rsaDecryptor.doFinal(envelope);
        secretKeyLen = secretKey.length;
        

      }
      catch (Exception ex)
      {
        SecurityLogger.warn(
          "Exception encountered while unsealing the envelope.");
        throw new SecurityServiceException(
          "Exception encountered while unsealing the envelope.",
          ex);
      }

      // Decrypt the bulk data with the recovered rc4 key
      try
      {
        SecurityLogger.log("Decrypt the bulk data with the recovered rc4 key");
        fos = new FileOutputStream(decryptedOutputFile);
        dos = new DataOutputStream(fos);
        Cipher rc4Decryptor = Cipher.getInstance("RC4", GridCertUtilities.getSecurityProvider());
        
        
        // Now we create another rc4 symmetric key
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, "RC4");
        Key rerc4Key = (Key)keySpec;
       
        rc4Decryptor.init(Cipher.DECRYPT_MODE, rerc4Key);
        

        // Begin the decryption process with a call to decryptUpdate().

        int bytesRead = 0;
        int partOut = 0;

        while ((bytesRead = dis.read(inputBlock)) != -1)
        {
          partOut =
            rc4Decryptor.update(
              inputBlock,
              0,
              bytesRead,
              outputBlock,
              0);
          dos.write(outputBlock, 0, partOut);
        }

        int finalOut = rc4Decryptor.doFinal(outputBlock, 0);
        dos.write(outputBlock, 0, finalOut);
      }
      catch (Exception ex)
      {
        SecurityLogger.warn("Exception encountered during bulk decryption.");
        throw new SecurityServiceException(
          "Exception encountered during bulk decryption.",
          ex);
      }
      finally
      {
        try
        {
          if (dos != null)
            dos.close();
          if (dis != null)
            dis.close();
        }
        catch (Exception ex)
        {
          SecurityLogger.log("No File Found " + ex.getMessage());
        }
      }
      /** Temporary disable for demo */
      /** @todo Check on disable of ifsign, dont know why this is disabled, and when was this disabled */
            // 190104NSL: temporary disable for publicKey=null
            // 'coz will hit exception if using own channel's securityinfo
            // as no signcert will be populated here, hence no publicKey for verification
            // will need to set the signcert somewhere
      if (sign && publicKey!=null)
      {
        // Now that we have the signature and the plain text, let's verify it.
        try
        {
          SecurityLogger.log("Verify Signature and text ");
          // open plaintext file again
          fis = new FileInputStream(decryptedOutputFile);
          // This is the easy way to verify something you just signed.
          String digetsAlgo = securityInfo.getDigestAlgorithm();
          Signature verifier = null;
          if (digetsAlgo != null)
          {
            if (digetsAlgo.equals(ISecurityInfo.DIGEST_ALGORITHM_MD5))
            {
              verifier =
                Signature.getInstance("MD5withRSA", GridCertUtilities.getSecurityProvider());
            }
            else if (digetsAlgo.equals(ISecurityInfo.DIGEST_ALGORITHM_SHA1))
            {
              verifier =
                Signature.getInstance("SHA1withRSA", GridCertUtilities.getSecurityProvider());
            }
          }
          else
          {
            SecurityLogger.log(
              "[SecurityServiceBean][decryptAndVerify()]"
                + "[Using Default DigestAlgorithm - MD5] ");
            verifier =
              Signature.getInstance("MD5withRSA", GridCertUtilities.getSecurityProvider());
          }
          verifier.initVerify(publicKey);
          
          int bytesRead = 0;
          while ((bytesRead = fis.read(inputBlock)) != -1)
          {
            verifier.update(inputBlock, 0, bytesRead);
          }
          boolean signatureCheck = verifier.verify(signature);
          fis.close();
          if (signatureCheck != true)
          {
            SecurityLogger.warn("Signature is wrong ");
            throw new SecurityServiceException("Incorrect Signature ");
          }

        }
        catch (Exception ex)
        {
          SecurityLogger.warn("Exception encountered during Verifying.");
          throw new SecurityServiceException(
            "Exception encountered during Verifying.",
            ex);
        }
        finally
        {
          try
          {
            if (fis != null)
              fis.close();
          }
          catch (Exception ex)
          {
            SecurityLogger.warn("Cannot Close File " + ex.getMessage());
          }
        }
      }

      byte decyptData[] = GridCertUtilities.loadFileToByte(decryptedOutputFile);
      if (decryptedOutputFile.exists())
        decryptedOutputFile.delete();
      return decyptData;
      //return decryptedOutputFile;

    }
    catch (Exception ex)
    {
      SecurityLogger.debug(ex.getMessage());
      throw new SecurityServiceException("Cannot Perform   ", ex);
    }

  }
  
  java.security.cert.Certificate[] addMoreTrustedCertsJava(java.security.cert.Certificate[] certs, java.security.cert.Certificate newCert)
  {
    java.security.cert.Certificate[] newCerts = new java.security.cert.Certificate[certs.length + 1];
    System.arraycopy(certs, 0, newCerts, 0, certs.length);
    newCerts[certs.length] = newCert;
    return newCerts;
  }
  
  /*
  private String printBuffer(byte[] byteArray)
  {
    return null;
    //return GridCertUtilities.printHex(byteArray);
  }*/

  /*
  private ICertificateManagerObj getCertificateManager() throws Exception
  {
    SecurityLogger.debug("Getting CertificateManagerBean: ");
    //ICertificateManagerHome certHome =
    //  (ICertificateManagerHome) ServiceLookup.getInstance().getHome(
    //    ICertificateManagerHome.class);
    //return certHome.create();
    return (ICertificateManagerObj)ServiceLocator.instance().getObj(ICertificateManagerHome.class.getName(),
                                                                    ICertificateManagerHome.class,
                                                                    new Object[0]);
  }*/
  public SMimeSecurityInfo deCompress(SMimeSecurityInfo info) throws SecurityServiceException
  {
    try
    {
        SecurityLogger.log("DeCompress Start of SMIME ");
        byte[] partToDeCompress = info.getDataToDeCompress();
        if(partToDeCompress == null)
        {
          SecurityLogger.log("Part to DeCompress From IMailpart ");
          partToDeCompress = info.getIParttoDeCompress().getContentByte(false);
        }
        SecurityLogger.log("Before Calling DeCompress Byte Array "+partToDeCompress);
        byte[] deCompressData = deCompress(partToDeCompress);
        SecurityLogger.log("After DeCompress   "+deCompressData);
        SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
        iinfo.setDeCompressedData(deCompressData);
        return iinfo;
    }
    catch(Exception ex)
    {
       SecurityLogger.log("Exception in DeCompress Main  " + ex.getMessage());
       throw new SecurityServiceException("Exception in DeCompress Main  ", ex);
    }
  }

  public SMimeSecurityInfo compress(SMimeSecurityInfo info) throws SecurityServiceException
  {
    try
    {
        SecurityLogger.log("Compress Start of SMIME ");
        byte[] partToCompress = info.getDataToCompress();
        int method = info.getCompressMethod();
        int level = info.getCompressLevel();
        if(partToCompress == null)
        {
          SecurityLogger.log("Part to Compress From IMailpart ");
          partToCompress = info.getIMailPartToCompress().getContentByte(false);
        }
        SecurityLogger.log("Before Calling Compress Byte Array "+partToCompress);
        byte[] compressData = compress(method, level, partToCompress);
        SecurityLogger.log("After Compress   "+compressData);
        SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
        iinfo.setCompressedData(compressData);
        return iinfo;
    }
    catch(Exception ex)
    {
       SecurityLogger.log("Exception in Compress Main  " + ex.getMessage());
       throw new SecurityServiceException("Compression exception ", ex);
    }
  }

  public byte[] compress(int method, int level, byte[] contentToCompress)
    throws SecurityServiceException
  {
    try
    {
        //int len = contentToCompress.length;
        GNCMSCompressedDataGenerator gen = new GNCMSCompressedDataGenerator();
        CMSCompressedData data = gen.generate(new CMSProcessableByteArray(contentToCompress), GNCMSCompressedDataGenerator.ZLIB, level);
        return data.getEncoded();

//      ByteArrayOutputStream dest = new ByteArrayOutputStream();
//      CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
//      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
//
//      out.setMethod(method);
//      out.setLevel(level);
//
//      ZipEntry entry = new ZipEntry("Gridnode Compression");
//      entry.setSize(len);
//      out.putNextEntry(entry);
//      out.write(contentToCompress, 0, len);
//      out.close();
//      return dest.toByteArray();
    }
    catch(Exception e)
    {
      SecurityLogger.log("Exception in compression  "+e.getMessage());
      throw new SecurityServiceException("Compression exception ", e);
    }
  }

  public byte[] deCompress(byte[] contentToDeCompress)
    throws SecurityServiceException
  {
      try
      {
          CMSCompressedData data = new CMSCompressedData(contentToDeCompress);
          return data.getContent();
//        ByteArrayOutputStream dest = new ByteArrayOutputStream();
//        ByteArrayInputStream  source = new ByteArrayInputStream(contentToDeCompress);
//        CheckedInputStream checksum = new CheckedInputStream(source, new Adler32());
//        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
//        ZipEntry entry = zis.getNextEntry();
//        if (entry != null)
//        {
//            int count;
//            int BUFFER = 1024* 100;
//            byte data[] = new byte[BUFFER];
//            while ((count = zis.read(data, 0, BUFFER)) != -1)
//            {
//              dest.write(data, 0, count);
//            }
//            dest.flush();
//            dest.close();
//        }
//        zis.close();
//        return dest.toByteArray();
      }
      catch (Exception ex)
      {
        SecurityLogger.log("Exception in deCompression  "+ex.getMessage());
        throw new SecurityServiceException("Decompression exception ", ex);
      }
  }

  public Message packSMIME(SecurityInfo securityInfo, Message message)
    throws Exception
  {
    X509Certificate partnerCert = GridCertUtilities.loadX509Certificate(
      securityInfo.getReceipentCertificate());

    X509Certificate ownCert = GridCertUtilities.loadX509Certificate(
      securityInfo.getSenderCertificate());

    SMimeFactory sf = SMimeFactory.newInstance(ownCert, partnerCert);
    
    if(securityInfo.getOwnPrivateKey() != null)
    {
      sf.setPrivateKey(GridCertUtilities.loadPrivateKeyFromByte(securityInfo.getOwnPrivateKey()));
    }
    
    ISMimePackager smimePackager = SMimeFactory2.getSMimePackager("AS2", sf);
    smimePackager.setPKCS7Encoding(SMimeHelper.ENCODING_BINARY);

    byte[] datatoencrypt = message.getPayLoadData();
    Map header = message.getMessageHeaders();

    SecurityLogger.debug("SecurityServiceBean.packSMIME()" + "header is " + header);
    
    MimeBodyPart msg = SMimeHelper.createPart(datatoencrypt,
      (String)header.get(IAS2Headers.CONTENT_TYPE));
    
    //NSL20060830 Set Filename in MimePart header
    String payloadFilename = (String)header.get(IAS2Headers.PAYLOAD_FILENAME);
    if (payloadFilename != null)
    {
      msg.setFileName(payloadFilename);
      header.remove(IAS2Headers.PAYLOAD_FILENAME); //no need anymore
    }
    
    smimePackager.setContent(msg);

    Map commonHeaders = message.getCommonHeaders();
    String sequence = (String)commonHeaders.get(ICommonHeaders.SMIME_ACTION_SEQUENCE);

    SecurityLogger.debug("SecurityServiceBean.packSMIME()" + "sequence is " + sequence);
    if (sequence != null && !sequence.equals(""))
    {
      StringTokenizer tokenizer = new StringTokenizer(sequence, ";");
      while (tokenizer.hasMoreTokens())
      {
        String nextAction = tokenizer.nextToken();
        SecurityLogger.debug("SecurityServiceBean.packSMIME()" + "nextAction is " + nextAction);
        if (nextAction.equals("E")) //encryption
        {
          SecurityLogger.debug("SecurityServiceBean.packSMIME()" +  "to encrypt");
          smimePackager.appendAction(ISMimePackager.ACTION_ENCRYPT);
          smimePackager.setActionProperty(ISMimePackager.ACTION_ENCRYPT,
                  ISMimePackager.SCOPE_ALL);
          sf.setEncryptionAlgorithm(securityInfo.getEncryptionAlgorithm());
          sf.setEncryptionLevel(securityInfo.getKeyLength());
        }
        else if (nextAction.equals("S")) //signature
        {
          SecurityLogger.debug("SecurityServiceBean.packSMIME()" +  "to sign");
          smimePackager.appendAction(ISMimePackager.ACTION_SIGN);
          smimePackager.setActionProperty(ISMimePackager.ACTION_SIGN,
                  ISMimePackager.SCOPE_ALL);
          sf.setDigestAlgorithm(securityInfo.getDigestAlgorithm());
          smimePackager.setDigestAlgorithm(securityInfo.getDigestAlgorithm());
        }
        else if (nextAction.equals("C")) //compression
        {
          SecurityLogger.debug("SecurityServiceBean.packSMIME()" +  "to compress");
          smimePackager.appendAction(ISMimePackager.ACTION_COMPRESS);
          smimePackager.setActionProperty(ISMimePackager.ACTION_COMPRESS,
                  ISMimePackager.SCOPE_ALL);
          sf.setCompressMethod(SMimeFactory.DEFAULT_COMPRESS_METHOD);

          //zlib has a compression level 0-3 while Deflater 0-9, hence the conversion.
          sf.setCompressLevel(securityInfo.getCompressionLevel()*3);
        }
        else // not possible for now
        {
          SecurityLogger.warn("SecurityServiceBean.packSMIME()" +
                  nextAction + " is not a legitimate SMIME action.");
        }
      }
    }

    if (header.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS))
    {
      String optionStr = (String)header.get(
        IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS);
/*            StringTokenizer tokenizer = new StringTokenizer(optionStr, ",");
            while (tokenizer.hasMoreTokens())
            {
              String token = tokenizer.nextToken().trim();
              if (token.equalsIgnoreCase("sha1") || token.equalsIgnoreCase("md5"))
              {
                sf.setDigestAlgorithm(token.toUpperCase());
                break;
              }
            }
*/
      String algo = SMimeFactory.getExternalDigestAlgo(securityInfo.getDigestAlgorithm());
      if (algo != null && algo.length() > 0)
        optionStr = optionStr + algo.toLowerCase();
      else
        optionStr = optionStr + "sha1";
      header.remove(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS);
      header.put(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS, optionStr);
    }

    MimeMessage packedMsg = smimePackager.packDocument();

    if (header.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS))
    {
      //20090804 set the digest algo for generating msg digest for internal keep for non repudiation
      String algo = SMimeFactory.getExternalDigestAlgo(securityInfo.getDigestAlgorithm());
      smimePackager.setDigestAlgorithm(algo);
      SecurityLogger.debug("SecurityServiceBean.packSmime digestAlgo:"+algo);
      
      byte[] digest = smimePackager.getMessageDigest();
      if (digest != null && digest.length>0)
      {
        String digestStr = GridCertUtilities.encode(digest);
        header.put(IAS2Headers.MIC, digestStr);
      }      
    }
    else
      header.put(IAS2Headers.MIC, "");
/*          try
          {
            java.io.FileOutputStream fos = new java.io.FileOutputStream("c:/temp/packedMsg");
            packedMsg.writeTo(fos);
          }
          catch(Exception e)
          {
            ChannelLogger.errorLog(CLASS_NAME, "encryptData",
              "error writing to packedMsg", e);
          }
*/
    header.remove(IAS2Headers.CONTENT_TYPE);
    header.put(IAS2Headers.MIME_VERSION, "1.0");

    header.put(IAS2Headers.CONTENT_TYPE, packedMsg.getContentType());

    Enumeration keys = ((Hashtable)header).keys();
    while (keys.hasMoreElements())
    {
      //remove newlines as folded headers are not allowed in AS2
      String key = (String)keys.nextElement();
      String value = (String)header.get(key);
      StringTokenizer st = new StringTokenizer(value, "\r\n");
      String newValue = "";
      while (st.hasMoreTokens())
      {
        newValue = newValue + st.nextToken();
      }
      header.put(key, newValue);
    }

    Enumeration allHeaders = packedMsg.getAllHeaders();
    while (allHeaders.hasMoreElements())
    {
      javax.mail.Header aHeader = (javax.mail.Header)allHeaders.nextElement();
      packedMsg.removeHeader(aHeader.getName());
    }

    SecurityLogger.debug("SecurityServiceBean.packSMIME()" +  "Now header is " + header);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    packedMsg.writeTo(bos);
    byte[] theBytes = bos.toByteArray();

    byte[] returnBytes = new byte[theBytes.length - 2];
    //remove the leading "\r\n" characters
    System.arraycopy(theBytes, 2, returnBytes, 0, returnBytes.length);

    message.setPayLoad(returnBytes);
    return message;
  }
  
  /**
   * Get a Random number generator. This uses the SHA-1 algorithm to create
   * a random number generator.
   *
   * @return An instance of a SecureRandom object.
   */
  private SecureRandom getRandomNum()
    throws NoSuchAlgorithmException
  {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    
    // Note that this way of seeding the random number generator is not secure
    // A more probable way of obtaining a guaranteed unique seed should be
    // deviced.
    random.setSeed(System.currentTimeMillis());

    return random;
  }
  
  private Key generateSymmKey(String algo,int keySize, SecureRandom random) throws Exception
  {
    KeyGenerator gen = KeyGenerator.getInstance(algo, GridCertUtilities.getSecurityProvider());
    gen.init(keySize, random);
    return gen.generateKey();
  }
}

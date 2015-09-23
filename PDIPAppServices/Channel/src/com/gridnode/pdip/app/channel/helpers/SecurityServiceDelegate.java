/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: SecurityServiceDelegate.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Oct 07 2002    Jagadeesh         Created
 * Jul 22 2003    Jagadeesh         Added: additional static methods to encrypt.
 * Aug 04 2003    Jagadeesh         Added: additional static methods to decrypt.
 * Aug 07 2006    Tam Wei Xiang     Amend the way we access SecurityDB. 
 *                                  Added method : getPrivatePassword()
 *                                  Modified: getBaseSecurityInfoForDecrypt(...)
 *                                            getBAseSecurityInfoForEncrypt(...)
 *
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.helpers.SecurityDB;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBManager;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceHome;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceObj;
import com.gridnode.pdip.base.security.helpers.SecurityInfo;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.*;

public class SecurityServiceDelegate
{
  private static final String CLASS_NAME = "SecurityServiceDelegate";

  public SecurityServiceDelegate()
  {
  }

  public static Message encrypt(
    com.gridnode.pdip.app.channel.model.SecurityInfo securityInfo,
    Message message)
    throws SecurityException
  {
    try
    {
      /** @todo Get Message Header's -- AFter Test's Completed */
      ChannelLogger.debugLog(CLASS_NAME, "encrypt()", message.toString());
      //List baseSecInfoAndHeader =  getBaseSecurityInfoForEncrypt(securityInfo,message);
      Iterator secIterator =
        getBaseSecurityInfoForEncrypt(securityInfo, message).iterator();
      Map header = (Map) secIterator.next();
      SecurityInfo secInfo = (SecurityInfo) secIterator.next();

      if (isSMIME(header))
      {
        header.put(ICommonHeaders.IS_SMIME, Boolean.TRUE);
        header.put(ICommonHeaders.SMIME_ACTION_SEQUENCE, getSequence(securityInfo));
      }
      else
        header.put(ICommonHeaders.IS_SMIME, Boolean.FALSE);

      message.setCommonHeaders(header);

      return getSecurityServiceFacade().encryptAndSign(secInfo, message);
      /*return getSecurityServiceFacade().encryptAndSign(
               getBaseSecurityInfoForEncrypt(securityInfo,message),
               message);*/
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "encrypt()",
        "Unable to encrypt message");

      throw new SecurityException("Unable to encrypt message", ex);
    }
  }

  public static Message decrypt(
    com.gridnode.pdip.app.channel.model.SecurityInfo securityInfo,
    Message message)
    throws SecurityException
  {
    try
    {
      if (securityInfo.getEncryptionCertificateID() == null
        && securityInfo.getSignatureEncryptionCertificateID() == null)
        return message;
      else
        return getSecurityServiceFacade().decryptAndVerify(
          getBaseSecurityInfoForDecrypt(securityInfo),
          message);
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "encrypt()",
        "Unable to decrypt message");

      throw new SecurityException("Unable to decrypt message", ex);
    }
  }

  /**
   * Refactored getBaseSecurityInfoForEncrypt to return modified Header and SecurityInfo.
   * @param securityInfo
   * @param message
   * @return
   * @throws SecurityException
   */

  private static List getBaseSecurityInfoForEncrypt(
    com.gridnode.pdip.app.channel.model.SecurityInfo securityInfo,
    Message message)
    throws SecurityException
  {
    List listSecInfoAndHeader = new ArrayList();

    com.gridnode.pdip.base.security.helpers.SecurityInfo baseSecurityInfo =
      new com.gridnode.pdip.base.security.helpers.SecurityInfo();
    // Set the security level to base security info.
    baseSecurityInfo.setSecuritylevel(securityInfo.getSecuritylevel());
    if (!com
      .gridnode
      .pdip
      .app
      .channel
      .model
      .ISecurityInfo
      .SIGNATURE_TYPE_NONE
      .equals(securityInfo.getSignatureType()))
      baseSecurityInfo.setSign(true); //Set the Sign to be true.

    Map headers = message.getCommonHeaders();
    if (headers == null)
      headers = new HashMap();
    /** @todo Check if this can be done here, after refactor. */

    Long certUID = null;
    Certificate certificate = null;

    headers.put(ICommonHeaders.ENCRYPT_TYPE, securityInfo.getEncryptionType());
    headers.put(ICommonHeaders.SIGN_TYPE, securityInfo.getSignatureType());
    headers.put(ICommonHeaders.COMPRESSION_TYPE, securityInfo.getCompressionType());

    baseSecurityInfo.setCompressionLevel(securityInfo.getCompressionLevel());

    certUID = securityInfo.getEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      baseSecurityInfo.setReceipentCertificate(
        GridCertUtilities.decode(certificate.getCertificate()));
      baseSecurityInfo.setKeyLength(securityInfo.getEncryptionLevel());
      baseSecurityInfo.setEncryptionAlgorithm(securityInfo.getEncryptionAlgorithm());

      headers.put(
        ICommonHeaders.ENCRYPT_LEVEL,
        String.valueOf(securityInfo.getEncryptionLevel()));
      //Modified to be consistent String type.27.12.03
      headers.put(
        ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME,
        certificate.getIssuerName());
      headers.put(
        ICommonHeaders.ENCRYPT_CERT_SERIAL_NUM,
        certificate.getSerialNumber());
    }

    certUID = securityInfo.getSignatureEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      baseSecurityInfo.setSenderCertificate(
        GridCertUtilities.decode(certificate.getCertificate()));

      String private_key = certificate.getPrivateKey();
      if (private_key == null || private_key.equals(""))
      {
        baseSecurityInfo.setSign(false);
        baseSecurityInfo.setOwnPrivateKey(null);
      }
      else
      {
        byte[] privateKey =
          GridCertUtilities.decode(certificate.getPrivateKey());
        baseSecurityInfo.setOwnPrivateKey(privateKey);
      }
      baseSecurityInfo.setPassword(
                                   	getPrivatePassword().toCharArray());
      //baseSecurityInfo.setSign(true);
      baseSecurityInfo.setDigestAlgorithm(securityInfo.getDigestAlgorithm());

      headers.put(
        ICommonHeaders.SIGN_DIGEST_TYPE,
        securityInfo.getDigestAlgorithm());
      headers.put(
        ICommonHeaders.SIGN_CERT_ISSUER_NAME,
        certificate.getIssuerName());
      headers.put(
        ICommonHeaders.SIGN_CERT_SERIAL_NUM,
        certificate.getSerialNumber());
    }
    listSecInfoAndHeader.add(headers);
    listSecInfoAndHeader.add(baseSecurityInfo);
    return listSecInfoAndHeader;
  }

  /** @todo  This method needs to be refactored after final test. Since the headers are not set to Message headers.*/

  private static com
    .gridnode
    .pdip
    .base
    .security
    .helpers
    .SecurityInfo getBaseSecurityInfoForDecrypt(
      com.gridnode.pdip.app.channel.model.SecurityInfo securityInfo)
    throws SecurityException
  {
    com.gridnode.pdip.base.security.helpers.SecurityInfo baseSecurityInfo =
      new com.gridnode.pdip.base.security.helpers.SecurityInfo();
    // Set the security level to base security info.
    baseSecurityInfo.setSecuritylevel(securityInfo.getSecuritylevel());
    //This indicates which of the  data to operate on.

    if (!com
      .gridnode
      .pdip
      .app
      .channel
      .model
      .ISecurityInfo
      .SIGNATURE_TYPE_NONE
      .equals(securityInfo.getSignatureType()))
      baseSecurityInfo.setSign(true); //Set the Sign to be true.

    Long certUID = null;
    Certificate certificate = null;

    certUID = securityInfo.getEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      String private_key = certificate.getPrivateKey();
      if (private_key == null || private_key.equals(""))
      {
        baseSecurityInfo.setSign(false);
        baseSecurityInfo.setOwnPrivateKey(null);
      }
      else
      {
        byte[] privateKey =
          GridCertUtilities.decode(certificate.getPrivateKey());
        baseSecurityInfo.setOwnPrivateKey(privateKey);
      }
      baseSecurityInfo.setPassword(
                                   getPrivatePassword().toCharArray());
      baseSecurityInfo.setKeyLength(securityInfo.getEncryptionLevel());
    }

    certUID = securityInfo.getSignatureEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      baseSecurityInfo.setSenderCertificate(
        GridCertUtilities.decode(certificate.getCertificate()));
      //baseSecurityInfo.setSign(true);
      baseSecurityInfo.setDigestAlgorithm(securityInfo.getDigestAlgorithm());
    }

    return baseSecurityInfo;
  }

  private static Certificate getCertificate(Long certUID)
    throws SecurityException
  {
    try
    {
      return getCertificateServiceFacade().findCertificateByUID(certUID);
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCertificate()",
        "Unable to get Certificate with uid" + certUID);

      throw new SecurityException(
        "Unable to get Certificate with uid" + certUID,
        ex);
    }
  }

  private static ISecurityServiceObj getSecurityServiceFacade()
    throws ServiceLookupException
  {
    return (ISecurityServiceObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        ISecurityServiceHome.class.getName(),
        ISecurityServiceHome.class,
        new Object[0]);
  }

  private static ICertificateManagerObj getCertificateServiceFacade()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        ICertificateManagerHome.class.getName(),
        ICertificateManagerHome.class,
        new Object[0]);
  }

  private static boolean isSMIME(Map headers)
  {
    int flag = 0;

    String encryptType = (String)headers.get(ICommonHeaders.ENCRYPT_TYPE);
    if (ISecurityInfo.ENCRYPTION_TYPE_SMIME.equals(encryptType))
      flag = 1;
    else if (!ISecurityInfo.ENCRYPTION_TYPE_NONE.equals(encryptType))
      return false;

    String signType = (String)headers.get(ICommonHeaders.SIGN_TYPE);
    if (ISecurityInfo.SIGNATURE_TYPE_SMIME.equals(signType))
      flag = 1;
    else if (!ISecurityInfo.SIGNATURE_TYPE_NONE.equals(signType))
      return false;

    String compressType = (String)headers.get(ICommonHeaders.COMPRESSION_TYPE);
    if (ISecurityInfo.COMPRESSION_TYPE_SMIME.equals(compressType))
      flag = 1;
    else if (!ISecurityInfo.COMPRESSION_TYPE_NONE.equals(compressType))
      return false;

    if (flag == 1)
      return true;
    else
      return false;
  }

  private static String getSequence(com.gridnode.pdip.app.channel.model.SecurityInfo _securityInfo)
  {
    String sequence = _securityInfo.getSequence();

    if ((sequence != null) && (!sequence.equals("")))
    {
      String temp = "";
      if (!ISecurityInfo.ENCRYPTION_TYPE_NONE.equals(_securityInfo.getEncryptionType()))
        temp = temp + "E";
      if (!ISecurityInfo.SIGNATURE_TYPE_NONE.equals(_securityInfo.getSignatureType()))
        temp = temp + "S";
      if (!ISecurityInfo.COMPRESSION_TYPE_NONE.equals(_securityInfo.getCompressionType()))
        temp = temp + "C";
      if (temp.length() > 1)
        return sequence;
      else
        return temp;
    }
    /* if sequence string is null or empty, it means at most one of the following three
     * actions is intended: encryption, sign and compression, otherwise a sequence
     * would have been set.
     */
    if (ISecurityInfo.ENCRYPTION_TYPE_SMIME.equals(_securityInfo.getEncryptionType()))
      return "E";

    if (ISecurityInfo.SIGNATURE_TYPE_SMIME.equals(_securityInfo.getSignatureType()))
      return "S";

    if (ISecurityInfo.COMPRESSION_TYPE_SMIME.equals(_securityInfo.getCompressionType()))
      return "C";

    return "";

  }
  
  //TWX 07082006
  private static String getPrivatePassword()
	{
		SecurityDBManager secDBManager = SecurityDBManager.getInstance();
		SecurityDB secDB = null;
		try
		{
			secDB = secDBManager.getSecurityDB();
			return secDB.getPrivatePassword();
		}
		finally
		{
			if(secDB != null)
			{
				secDBManager.releaseSecurityDB(secDB);
			}
		}
	}
  
}
	
/*

  private static com.gridnode.pdip.base.security.helpers.SecurityInfo getBaseSecurityInfoForEncrypt(
    com.gridnode.pdip.app.channel.model.SecurityInfo securityInfo,
    Message message)
    throws SecurityException
  {
    com.gridnode.pdip.base.security.helpers.SecurityInfo baseSecurityInfo =
       new com.gridnode.pdip.base.security.helpers.SecurityInfo();
    baseSecurityInfo.setSecuritylevel(securityInfo.getSecuritylevel());
    if (!com.gridnode.pdip.app.channel.model.ISecurityInfo.SIGNATURE_TYPE_NONE.equals(securityInfo.getSignatureType()))
      baseSecurityInfo.setSign(true); //Set the Sign to be true.

  Map headers = message.getCommonHeaders();
  if (headers == null)
    headers = new HashMap();

    Long certUID = null;
    Certificate certificate = null;

  headers.put(ICommonHeaders.ENCRYPT_TYPE,
         securityInfo.getEncryptionType());
  headers.put(ICommonHeaders.SIGN_TYPE,
         securityInfo.getSignatureType());


    certUID = securityInfo.getEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      baseSecurityInfo.setReceipentCertificate(
        GridCertUtilities.decode(certificate.getCertificate()));
      baseSecurityInfo.setKeyLength(securityInfo.getEncryptionLevel());

    headers.put(ICommonHeaders.ENCRYPT_LEVEL,
          new Integer(securityInfo.getEncryptionLevel()));
      headers.put(ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME,
                  certificate.getIssuerName());
    headers.put(ICommonHeaders.ENCRYPT_CERT_SERIAL_NUM,
          certificate.getSerialNumber());
    }

    certUID = securityInfo.getSignatureEncryptionCertificateID();
    if (certUID != null)
    {
      certificate = getCertificate(certUID);
      String private_key = certificate.getPrivateKey();
      if (private_key == null || private_key.equals(""))
      {
         baseSecurityInfo.setSign(false);
         baseSecurityInfo.setOwnPrivateKey(null);
      }
      else
      {
        byte[] privateKey = GridCertUtilities.decode(certificate.getPrivateKey());
        baseSecurityInfo.setOwnPrivateKey(privateKey);
      }
      baseSecurityInfo.setPassword(SecurityDB.getPrivatePassword().toCharArray());
      //baseSecurityInfo.setSign(true);
      baseSecurityInfo.setDigestAlgorithm(securityInfo.getDigestAlgorithm());

    headers.put(ICommonHeaders.SIGN_DIGEST_TYPE,
          securityInfo.getDigestAlgorithm());
    headers.put(ICommonHeaders.SIGN_CERT_ISSUER_NAME,
          certificate.getIssuerName());
    headers.put(ICommonHeaders.SIGN_CERT_SERIAL_NUM,
          certificate.getSerialNumber());
    }
    return baseSecurityInfo;
  }
*/

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
 * 03-June-2002    Jagadeesh           Created.
 * 04 Nov 2003    Zou Qingsong        Enhancement for Compression
 * 20 Oct 2005    Neo Sok Lay         No corresponding business method in the 
 *                                    bean class com.gridnode.pdip.base.security.facade.ejb.SecurityServiceBean 
 *                                    was found for method:
 *                                    - encryptAndSign
 *                                    - decryptAndVerify
 * 17 Jul 2009    Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */


package com.gridnode.pdip.base.security.facade.ejb;

import java.rmi.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.ejb.*;

import com.gridnode.pdip.framework.messaging.Message;

import com.gridnode.pdip.base.security.exceptions.*;
import com.gridnode.pdip.base.security.helpers.*;


/**
 * The ISecurityServiceObj extends EJBObject, and defines the enterprise bean
 * business methods.
 *
 * The enterprise bean remote interface is defined by the enterprise bean provider.
 */


public interface ISecurityServiceObj extends EJBObject
{


  /**
  * This method encrypt and sign the data/content passed in as
  * a part of the Value Object - Message
  *
  * @param  securityInfo - Value Object encapsulating the security settings.
  * @param  message - Value Object encapsulating the data to be encrypted.
  * @return Message - Value Object encapsulating the encrypted data.
  * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
  * @throws RemoteException - Thrown when the method failed due to system-level exception.
  */
  public Message encryptAndSign(
    SecurityInfo securityInfo, Message message)
    throws SecurityServiceException, RemoteException;



  /**
   * This method decrypts and verify the data/content passed in as
   * a part of Value Object - Message.
   *
   * @param  securityInfo - Value Object encapsulating the security settings.
   * @param  message - Value Object encapsulating the encrypted data.
   * @param  target - Targeted segment(s) in message for decryption
   * @return Message - Value Object encapsulating the decrypted data.
   * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
   * @throws RemoteException - Thrown when the method failed due to system-level exception.
   */
  public Message decryptAndVerify(
    SecurityInfo securityInfo, Message message)
    throws SecurityServiceException, RemoteException;




    /**
     * This method encrypt and sign the data/content passed in as
     * a part of Value Object - SecurityInfo.
     *
     * SecurityInfo is (implementation/reference) to the Value Object, which
     * encapsulates security information, insted of passing each value - parameter,
     * which tightly couples the remote mehtod and its implementation.
     *
     *
     * @param securityInfo - Value Object which is passed as a coarse-grained Object.
     * @return SecurityInfo - Value Object which containes the encrypted data.
     * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
     * @throws RemoteException - Thrown when the method failed due to system-level exception.
     */

     public SecurityInfo encryptAndSign(SecurityInfo securityInfo)
                            throws SecurityServiceException,RemoteException;


    /**
     * This method decrypts and verify the data/content passed in as
     * a part of Value Object - SecurityInfo.
     *
     * SecurityInfo is (implementation/reference) to the Value Object, which
     * encapsulates security information, insted of passing each value - parameter,
     * which tightly couples the remote mehtod and its implementation.
     *
     *
     * @param securityInfo - Value Object which is passed as a coarse-grainedObject.
     * @return SecurityInfo - Value Object which containes the decrypted data.
     * @throws SecurityServiceException - Thrown when the method failed to perfrom encrypt/sign.
     * @throws RemoteException - Thrown when the method failed due to system-level exception.
     */



     public SecurityInfo decryptAndVerify(SecurityInfo securityInfo)
                            throws SecurityServiceException,RemoteException;



     public SMimeSecurityInfo encrypt(SMimeSecurityInfo info)
            throws SecurityServiceException,RemoteException;
//
//
     public SMimeSecurityInfo decrypt(SMimeSecurityInfo info)
            throws SecurityServiceException,RemoteException;
//
//
     public SMimeSecurityInfo sign(SMimeSecurityInfo info)
            throws SecurityServiceException,RemoteException;
//
//
//
     public SMimeSecurityInfo verify(SMimeSecurityInfo info)
           throws SecurityServiceException,RemoteException;



     public byte[] encrypt(X509Certificate partnerCertificate, byte[] contentToEncry,X509Certificate recpCertificate,PrivateKey privateKey)
       throws SecurityServiceException,RemoteException;

     public byte[] decrypt(byte[] partToDecryped)
         throws SecurityServiceException,RemoteException;

     public byte[] decrypt(byte[] partToDecryped,X509Certificate recpCertificate,PrivateKey privateKey)
                    throws SecurityServiceException,RemoteException;

     public  byte[] sign(X509Certificate signerCertificate, byte[] partToSign,PrivateKey privateKey)
              throws SecurityServiceException,RemoteException;

     public boolean verify(byte[] contentInfoEncoding, byte[] signature,X509Certificate recpCertificate,PrivateKey privateKey)
              throws SecurityServiceException,RemoteException;

    public byte[] compress(int method, int level, byte[] contentToCompress)
      throws SecurityServiceException,RemoteException;

    public SMimeSecurityInfo compress(SMimeSecurityInfo info)
      throws SecurityServiceException,RemoteException;

    public SMimeSecurityInfo deCompress(SMimeSecurityInfo info)
      throws SecurityServiceException,RemoteException;

    public byte[] deCompress(byte[] contentToDeCompress)
      throws SecurityServiceException,RemoteException;

}
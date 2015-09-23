/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICertificateManager
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-July-2002    Jagadeesh           Created.
 * 13-Sept-2002    Jagadeesh           Added getCertificate(IDataFilter filter), to
 *                                     retrieve Certificate Entity.
 * 27-Jan-2003     Jagadeesh           Modified: To return Exported Certificate File Name insted of

 * 28-Apr-2003     Qingsong            Added: issetPrivatePassword, setPrivatePassword, changePrivatePassword, changeCertificateName, removeCertificate
 *                                     Certificate Entity
 * 14 Jul 2003     Neo Sok Lay         Add method: getCertificateKeys(IDataFilter)
 * 08 Jan 2004     Neo Sok Lay         Modified: 
 *                                     - displayX500Names(Name) to displayX500Names(UID)
 *                                     - exportCertificate(Name,Filename) to exportCertificate(UID,Filename)
 * 30-March-2004   Guo Jianyu          Added updateCertificate() and changed method signature
 *                                     for two importCertificate() by adding "relatedCertUid"
 * 02-April-2004   Guo Jianyu          Added "getOwnSignCert()" and "getPartnerEncryptCert"
 * 17-Oct-2005     Neo Sok Lay         Added: insertPrivateKeyByCertificate(byte[],JSAFE_PrivateKey)
 * Oct 20 2005     Neo Sok Lay         No corresponding business method in the bean class com.gridnode.pdip.base.certificate.facade.ejb.CertificateManagerBean 
 *                                     was found for method importCertificate.
 *                                     Business methods of the remote interface must throw java.rmi.RemoteException
 *                                     - The business method issetPrivatePassword does not throw java.rmi.RemoteException
 *                                     - The business method setPrivatePassword does not throw java.rmi.RemoteException
 *                                     - The business method changePrivatePassword does not throw java.rmi.RemoteException
 *                                     - The business method updateCertificate does not throw java.rmi.RemoteException
 *                                     - The business method getOwnSignCert does not throw java.rmi.RemoteException
 *                                     - The business method getPartnerEncryptCert does not throw java.rmi.RemoteException
 *                                     - The business method getX509Certificate does not throw java.rmi.RemoteException
 *                                     - The business method getPendingX509Cert does not throw java.rmi.RemoteException
 * Jul 26 2006     Tam Wei Xiang       New methods for retrieving X500Name detail and CertDetail(
 *                                     consist of serialNum, startDate, endDAte). The original methods
 *                                     displayX500Names are keeped.   
 *                                     Change method signature for createCertificate(...) 
 *                                     to include cert's startDate, endDate   
 * Jul 27 2006     Tam Wei Xiang       change method signature for importCertificate(...) to include
 *                                     isCA 
 * Aug 01 2008	   Wong Yee Wah 	   #38  Modified method: updateCertificate(), add two param of swapDate and swapTime
 * Jul 02 2009     Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                                                 
 */



package com.gridnode.pdip.base.certificate.facade.ejb;

import com.gridnode.pdip.base.certificate.exceptions.*;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;


import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public interface ICertificateManagerObj extends EJBObject
{

/**************** Methods for Import/Export of Certificates *****************/
  public Certificate importCertificate(
    String name,
    String fileName,
    Integer partnerId)
    throws CertificateException, SystemException, RemoteException;

  public Certificate importCertificate(
    String name,
    String fileName,
    Integer ownId,
    String password)
    throws
      CertificateException,
      SystemException,
      RemoteException;
  
  //TWX 27072006
  public Certificate importCertificate(String name, String fileName, Long relatedCertUid,
                                       Boolean isCA)
    throws
      CertificateException,
      DuplicateCertificateException,
      InvalidFileTypeException,
      FileAccessException,
      SystemException,
      RemoteException;

  public Certificate importCertificate(
    String name,
    String fileName,
    String password,
    Long relatedCertUid)
    throws CertificateException, InvalidPasswordOrFileTypeException,
    DuplicateCertificateException,
    FileAccessException, SystemException, RemoteException;

  public String exportCertificateTrustStore(Long uid)
    throws CertificateException, SystemException, RemoteException;

  public String exportCertificateKeyStore(Long uid)
    throws CertificateException, SystemException, RemoteException;

  //public String exportCertificate(String name, String fileName)
  public String exportCertificate(Long uid, String fileName)
    throws CertificateException, SystemException, RemoteException;

  public Vector displayX500Names(Long uid)
    throws
      InvalidFileTypeException,
      CertificateException,
      SystemException,
      RemoteException;
  /*080104NSL
  public Vector displayX500Names(String name)
         throws InvalidFileTypeException,
                CertificateException,
                SystemException,
                RemoteException;
  */
  public Vector displayX500Names(String fileName, String password)
    throws
      InvalidPasswordOrFileTypeException,
      CertificateException,
      SystemException,
      RemoteException;
  
  //TWX 20060726 to include the serialNum, start date, end date
  public Vector getX500NamesAndCertDetail(Long uid)
  	throws InvalidFileTypeException, CertificateException, SystemException, RemoteException;
  
  public Vector getX500NamesAndCertDetail(String fileName, String password)
		throws InvalidPasswordOrFileTypeException, CertificateException, SystemException, RemoteException;
  
/****************   Finder Methods  **************************/

  public Certificate findCertificateByIDAndName(int id, String name)
    throws FindEntityException, SystemException, RemoteException;

  public Certificate findCertificateByIssureAndSerialNum(
    String issuerName,
    String serialNum)
    throws FindEntityException, SystemException, RemoteException;

  public Collection getAllCertificates()
    throws FindEntityException, SystemException, RemoteException;

   /**
   * To retrieve a collection of <code>Certificate</code> entity with the specified filter.
   */
  public Collection getCertificate(IDataFilter filter)
    throws    FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of <code>Certificate</code> entity with the specified filter.
   * 
   * @parma filter The filtering condition
   * @return A Collection of UIDs of the Certificate entities that satisfy the filter condition.
   */
  public Collection getCertificateKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>Certificate</code> entity with the specified UID.
   */
  public Certificate findCertificateByUID(Long uid)
    throws FindEntityException, SystemException, RemoteException;

/*****************   Create Certificate  *******************/

  public void createCertificate(
    String serialNum,
    String issuerName,
    String cert,
    String publicKey,
    Date startDate, Date endDate)
    throws CreateEntityException, SystemException, RemoteException;

/****************   Insert Data by Criteria *************/
  public boolean issetPrivatePassword() throws RemoteException;

  public void setPrivatePassword(String password)
        throws InvalidPasswordException, ApplicationException, RemoteException;

  public void changePrivatePassword(String oldpassword, String newpassword)
               throws InvalidPasswordException, ResetPasswordException, RemoteException;

  public void updatePrivateKeyByCertificate(String privateKey, String cert)
    throws UpdateEntityException, SystemException, RemoteException;

  public void updatePrivateKeyByPublicKey(String privateKey, String publicKey)
    throws UpdateEntityException, SystemException, RemoteException;

  public void updateIdAndNameByCertificate(int Id, String name, String cert)
    throws UpdateEntityException, SystemException, RemoteException;

  public void updateMasterAndPartnerByUId(
    Long uid,
    boolean isMaster,
    boolean isPartner)
    throws UpdateEntityException, SystemException, RemoteException;

/****************   Delete Entity by Criteria *************/

  public void deleteCertificateByIssuerAndSerialNum(
    String issuerName,
    String serialNum)
    throws DeleteEntityException, SystemException, RemoteException;

  public String removeCertificate(Long uid)
    throws
      EntityModifiedException,
      UpdateEntityException,
      SystemException,
      RemoteException;

  public void removePrivateKeyByCertificate(String cert)
    throws
      EntityModifiedException,
      UpdateEntityException,
      SystemException,
      RemoteException;

  public void removePrivateKeyByPublicKey(String publicKey)
    throws
      EntityModifiedException,
      UpdateEntityException,
      SystemException,
      RemoteException;

  public void revokeCertificateByUId(Long uid)
    throws CertificateException, SystemException, RemoteException;

  /** Methods Added form SecurityDB ***********/

  public void insertCertificate(String certFile)
    throws CertificateException, SystemException, RemoteException;

  public void insertCertificate(
    Number gridnodeID,
    String name,
    String certFile)
    throws CertificateException, SystemException, RemoteException;

  public java.security.cert.Certificate insertCertificate(
    Number gridnodeID,
    String name,
    byte[] certData)
    throws CertificateException, SystemException, RemoteException;

  public void insertCertificate(
    int gNId,
    String name,
    java.security.cert.Certificate cert)
    throws CertificateException, SystemException, RemoteException;

  public void insertCertificate(
    Number gridnodeID,
    String name,
    java.security.cert.Certificate cert)
    throws CertificateException, RemoteException;

  public void insertIdAndNameByCertificate(
    int gNId,
    String name,
    java.security.cert.Certificate cert)
    throws CertificateException, SystemException, RemoteException;

  public void insertPrivateKeyByCertificate(
    java.security.cert.Certificate cert,
    PrivateKey privateKey)
    throws CertificateException, SystemException, RemoteException;

  public void insertPrivateKeyByCertificate(byte[] certData,
                                            PrivateKey privateKey)
                                            throws CertificateException, SystemException, RemoteException;

  public void deleteCertificate(int gNId, String name)
    throws DeleteEntityException, SystemException, RemoteException;

  public void updateCertificate(Long uid, String newName, Long relatedCertUid, String swapDate, String swapTime)
    throws EntityModifiedException,UpdateEntityException, SystemException, RemoteException;

  public X509Certificate getOwnSignCert(Certificate signCert, long threshold)
    throws CertificateException, RemoteException;

  public X509Certificate getPartnerEncryptCert(Certificate encryptCert)
    throws CertificateException, RemoteException;

  public X509Certificate getX509Certificate(Certificate cert)
    throws CertificateException, RemoteException;

  public X509Certificate getPendingX509Cert(Certificate cert)
    throws CertificateException, RemoteException;
/*
  public Certificate getGNCertificateByGNIdAndName(int Id, String name)
                     throws CertificateException,RemoteException;

  public Certificate getGNCertificateByIssuerAndSerialNum(String issuerName, String serialNum)
                     throws CertificateException,RemoteException;
*/
}

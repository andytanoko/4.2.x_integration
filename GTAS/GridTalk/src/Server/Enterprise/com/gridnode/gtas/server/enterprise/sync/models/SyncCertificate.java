/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncCertificate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add method: 
 *                                    - sync(int mode)
 * Jan 19 2004    Neo Sok Lay         Add revocation of previous cert.
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
//import com.gridnode.pdip.base.certificate.helpers.SecurityDBHandler;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;

/**
 * This data object is a modified model of Certificate for data transfer &
 * synchronization purpose. This object encapsulates the Certificate to
 * be synchronized. The certificate content is converted to bytes for transfer
 * purpose.<p>
 * 
 * The additional modes assigned to this model is 0x1000000,0x2000000,0x4000000,0x8000000.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class SyncCertificate extends AbstractSyncModel
{
  //public static final int MODE_OVERWRITE_EXISTING = 0x20;
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8043183105582277160L;
	private transient ICertificateManagerObj _certMgr;
  private Certificate _cert;
  private byte[]      _certBytes;
  private byte[]      _issuerName;
  private byte[]      _serialNumber;

  public SyncCertificate()
  {
  }

  public SyncCertificate(Certificate cert)
  {
    setCert(cert);
    if (cert != null)
    {
      setCertBytes(cert.getCertificate().getBytes());
      setIssuerName(cert.getIssuerName().getBytes());
      setSerialNumber(cert.getSerialNumber().getBytes());
    }
  }

  // ********************* Getters & Setters *******************************

  public Certificate getCert()
  {
    return _cert;
  }

  public void setCert(Certificate cert)
  {
    _cert = cert;
  }

  public byte[] getCertBytes()
  {
    return _certBytes;
  }

  public void setCertBytes(byte[] certBytes)
  {
    _certBytes = certBytes;
  }

  public byte[] getIssuerName()
  {
    return _issuerName;
  }

  public void setIssuerName(byte[] issuerName)
  {
    _issuerName = issuerName;
  }

  public byte[] getSerialNumber()
  {
    return _serialNumber;
  }

  public void setSerialNumber(byte[] serialNumber)
  {
    _serialNumber = serialNumber;
  }

  public boolean isMaster()
  {
    if (_cert != null)
      return _cert.isMaster();
    else
      return false;
  }

  public void setMaster(boolean master)
  {
    if (_cert != null)
      _cert.setMaster(master);
  }

  // ******************** Overrides DataObject *****************************
  public void postDeserialize()
  {
    //System.out.println("[SyncCertificate.postDeserialize] Enter");
    if (_cert != null)
    {
      _cert.setIssuerName(new String(getIssuerName()));
      _cert.setSerialNumber(new String(getSerialNumber()));
      _cert.setCertificate(new String(getCertBytes()));
    //System.out.println("[SyncCertificate.postDeserialize] issuerName="+_cert.getIssuerName());
    //System.out.println("[SyncCertificate.postDeserialize] issuerName="+_cert.getSerialNumber());
    }
  }


  // ********************* Implement AbstractSyncModel *********************

  /**
   * Synchronize the Certificate. If the Certificate
   * does not already exist in the database, a record will be created for it.
   * Otherwise, ???
   *
   * @exception Throwable Error in creating or updating to database.
   * @since 2.0 I6
   */
  public void sync() throws java.lang.Throwable
  {
    ICertificateManagerObj mgr = getCertMgr();
    
    Certificate existCert = getCertificate(mgr, _cert.getIssuerName(), _cert.getSerialNumber());

    if (existCert == null && isMaster())
    {
      existCert =  getCertificate(mgr, _cert.getID(), _cert.getCertName());
      if (existCert != null) 
      { // only revoke under this situation, probably the existing cert was from previous activation
        revokeCertificate(mgr, (Long)existCert.getKey());
        existCert = null;
      }
    }

    if (existCert == null)
    {
  //System.out.println("CertContent="+new String(getCertBytes()));

      /* 211002 NSL Changed CertificateManagerBean interface.
      SecurityDBHandler.getInstance().insertCertificate(
        new Integer(_cert.getID()), _cert.getCertName(),
        GridCertUtilities.loadX509Certificate(decode(_cert.getCertificate())));
        */
      mgr.insertCertificate(
        new Integer(_cert.getID()), _cert.getCertName(),
        decode(_cert.getCertificate()));

      existCert = getCertificate(mgr, _cert.getIssuerName(), _cert.getSerialNumber());
    }

    mgr.updateMasterAndPartnerByUId(
      (Long)existCert.getKey(), isMaster(), true);

    //no update at the moment for existing cert
    _cert.setUId(existCert.getUId());
  }

  /**
   * Modes not supported.
   * 
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   */
  public void sync(int mode) throws Throwable
  {
    sync();
  }

  // ************** Own methods ******************************************

  /**
   * Retrieve certificate based on issuer name and serial number.
   *
   * @param mgr The CertificateManager 
   * @param issuer The issuer name of the cerificate.
   * @param serialNum The serial number of the certificate.
   *
   * @return The certificate retrieved, or <b>null</b> if none exists.
   */
  private Certificate getCertificate(ICertificateManagerObj mgr, String issuer, String serialNum)
  {
    Certificate cert= null;

    try
    {
      cert = mgr.findCertificateByIssureAndSerialNum(
                                  issuer, serialNum);

    }
    catch (Throwable ex)
    {
      Logger.log("[SyncCertificate.getCerticate] No existing certificate found by issuer & serialno: "+
        ex.getMessage());
    }

    return cert;
  }

  /**
   * Retrieve certificate based on cert id & name.
   *
   * @param mgr The CertificateManager 
   * @param certId The certificate id
   * @param certName The certificate name
   *
   * @return The certificate retrieved, or <b>null</b> if none exists.
   */
  private Certificate getCertificate(ICertificateManagerObj mgr, int certId, String certName)
  {
    Certificate cert= null;

    try
    {
      cert = mgr.findCertificateByIDAndName(certId, certName);

    }
    catch (Throwable ex)
    {
      Logger.log("[SyncCertificate.getCerticate] No existing certificate found by id & name: "+
        ex.getMessage());
    }

    return cert;
  }

  /**
   * Revoke certificate based on UID
   *
   * @param mgr The CertificateManager 
   * @param certUid The UID of the cerificate to revoke.
   *
   * @return The certificate retrieved, or <b>null</b> if none exists.
   */
  private void revokeCertificate(ICertificateManagerObj mgr, Long certUid)
  {
    try
    {
      mgr.revokeCertificateByUId(certUid);

    }
    catch (Throwable ex)
    {
      Logger.log("[SyncCertificate.revokeCertificate] Unable to revoke certificate: "+
        ex.getMessage());
    }
  }

  /**
   * Get a handle to the ChannelManagerBean. No lookup would be done if
   * one has already been done before.
   *
   * @return A handle to ChannelManagerBean.
   * @exception Throwable Error in obtaining a handle to the ChannelManagerBean.
   *
   * @since 2.0 I6
   */
  private ICertificateManagerObj getCertMgr()
    throws Throwable
  {
    if (_certMgr == null)
    {
      _certMgr = ServiceLookupHelper.getCertificateManager();
    }

    return _certMgr;
  }

  private byte[] decode(String data)
  {
    return GridCertUtilities.decode(data);
  }
  /*
  private String encode(byte[] data)
  {
    return GridCertUtilities.encode(data);
  }*/

}
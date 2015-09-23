/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncSecurityInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 01 2002    Neo Sok Lay         Created
 * Jan 21 2003    Ang Meng Hua        Check for null UID in constructEncryptionCert
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - sync(int mode)
 * May 17 2004    Neo Sok Lay         Add EncryptionAlgorithm
 */
package com.gridnode.gtas.server.enterprise.sync.models;

import com.gridnode.gtas.model.channel.ISecurityInfo;
import com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;

import com.gridnode.pdip.base.certificate.model.Certificate;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This data object is a modified model of SecurityInfo for data transfer &
 * synchronization purpose. This object encapsulates the SecurityInfo to
 * be synchronized and, in addition, related information can be encapsulated
 * as well, such as the Encryption Certificate.<p>
 * <p>
 * The additional modes assigned to this model is 0x10,0x20,0x40,0x80.
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class SyncSecurityInfo extends AbstractSyncModel
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6738234292882179110L;

	/**
   * Turn on to overwrite existing SecurityInfo record in database.
   */
  public static final int MODE_OVERWRITE_EXISTING = 0x10;

  private transient IChannelManagerObj _channelMgr;
  private SecurityInfo    _securityInfo;
  private SyncCertificate _encryptionCert;

  private static final Number[]   SECURITYINFO_SYNC_FIELDS =
          {
            SecurityInfo.DESCRIPTION,
            SecurityInfo.DIGEST_ALGORITHM,
            SecurityInfo.ENCRYPTION_CERTIFICATE_ID,
            SecurityInfo.ENCRYPTION_LEVEL,
            SecurityInfo.ENCRYPTION_TYPE,
            SecurityInfo.NAME,
            SecurityInfo.IS_PARTNER,
            SecurityInfo.PARTNER_CAT,
            SecurityInfo.IS_DISABLE,
            SecurityInfo.ENCRYPTION_ALGORITHM,
          };

  public SyncSecurityInfo()
  {
  }

  public SyncSecurityInfo(SecurityInfo securityInfo)
  {
    setSecurityInfo(securityInfo);
    constructEncryptionCert(securityInfo.getEncryptionCertificateID());
  }

  // ******************* Getters & Setters ********************************

  public SecurityInfo getSecurityInfo()
  {
    return _securityInfo;
  }

  public void setSecurityInfo(SecurityInfo securityInfo)
  {
    _securityInfo = securityInfo;
  }

  public SyncCertificate getEncryptionCert()
  {
    return _encryptionCert;
  }

  public void setEncryptionCert(SyncCertificate cert)
  {
    _encryptionCert = cert;
  }


  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    if (_encryptionCert != null)
      _encryptionCert.preSerialize();
    _securityInfo.preSerialize();
  }

  public void postSerialize()
  {
    if (_encryptionCert != null)
      _encryptionCert.postSerialize();
    _securityInfo.postSerialize();
  }

  public void postDeserialize()
  {
    if (_encryptionCert != null)
      _encryptionCert.postDeserialize();
    if (_securityInfo != null)
      _securityInfo.postDeserialize();
  }

  // ***********************Implement AbstractSyncModel *********************

  /**
   * Synchronize the content for a SecurityInfo to the database. The SecurityInfo
   * will either be created (if not already exist) or updated. Prior to that,
   * the encapsulated Encryption certificate is first synchronized.<p>
   * By default, the synchronization mode is: <pre>
   *   IS_PARTNER | GT_PARTNER | OVERWRITE_EXISTING
   * </pre>
   *
   * @exception Throwable Error in performing the operation.
   * @since 2.0 I6
   */
  public void sync() throws Throwable
  {
    // default is IS_PARTNER AND GRIDTALK_PARTNER AND CANNOT DELETE AND OVERWRITE_EXISTING
    sync(MODE_IS_PARTNER | MODE_GT_PARTNER | MODE_OVERWRITE_EXISTING);

    /*030908NSL
    //sync the certificate
    if (_encryptionCert != null)
    {
      _encryptionCert.sync();
      _securityInfo.setEncryptionCertificateID(
        (Long)_encryptionCert.getCert().getKey());
    }

    _securityInfo.setIsPartner(true);
    _securityInfo.setPartnerCategory(ISecurityInfo.CATEGORY_GRIDTALK);
    _securityInfo.setCanDelete(false);

    //check securityInfo
    SecurityInfo existSecurityInfo = getSecurityInfo(_securityInfo.getName(),
                                       _securityInfo.getReferenceId());

    if (existSecurityInfo == null)
    {
      //add securityInfo
      Long uID = getChannelMgr().createSecurityInfo(_securityInfo);
      _securityInfo.setUId(uID.longValue());
    }
    else
    {
      //update securityInfo
      copyFields(_securityInfo, existSecurityInfo, SECURITYINFO_SYNC_FIELDS);
      getChannelMgr().updateSecurityInfo(existSecurityInfo);
      _securityInfo.setUId(existSecurityInfo.getUId());
    }
    */
  }

  /**
   * Supports combinations of MODE_IS_PARTNER, MODE_GT_PARTNER, MODE_CAN_DELETE, MODE_OVERWRITE_EXISTING.
   * @see com.gridnode.gtas.server.enterprise.sync.AbstractSyncModel#sync(int)
   * @see #MODE_OVERWRITE_EXISTING
   */
  public void sync(int mode) throws Throwable
  {
    //sync the certificate
    if (_encryptionCert != null)
    {
      _encryptionCert.sync(mode);
      _securityInfo.setEncryptionCertificateID(
        (Long)_encryptionCert.getCert().getKey());
    }

    _securityInfo.setCanDelete(isSet(mode, MODE_CAN_DELETE));
    _securityInfo.setIsPartner(isSet(mode, MODE_IS_PARTNER));
    if (_securityInfo.isPartner())
    {
      _securityInfo.setPartnerCategory(
        isSet(mode, MODE_GT_PARTNER)?
        ISecurityInfo.CATEGORY_GRIDTALK :
        ISecurityInfo.CATEGORY_OTHERS);
    }

    //check securityInfo
    SecurityInfo existSecurityInfo = getSecurityInfo(_securityInfo.getName(),
                                       _securityInfo.getReferenceId());

    if (existSecurityInfo == null)
    {
      //add securityInfo
      Long uID = getChannelMgr().createSecurityInfo(_securityInfo);
      _securityInfo.setUId(uID.longValue());
    }
    else
    {
      //update securityInfo
      if (_securityInfo.getUId() != existSecurityInfo.getUId())
      {
        if (isSet(mode, MODE_OVERWRITE_EXISTING))
        {
          if (  _securityInfo.getSignatureType() != null &&
               _securityInfo.getDigestAlgorithm() != null &&
              _securityInfo.getSignatureEncryptionCertificateID() != null
             )
          {
            existSecurityInfo.setSignatureType(_securityInfo.getSignatureType());
            existSecurityInfo.setDigestAlgorithm(_securityInfo.getDigestAlgorithm());
            existSecurityInfo.setSignatureEncryptionCertificateID(_securityInfo.getSignatureEncryptionCertificateID());
          }
          else
            copyFields(_securityInfo, existSecurityInfo, SECURITYINFO_SYNC_FIELDS);
          getChannelMgr().updateSecurityInfo(existSecurityInfo);
        }
        else
        {
          copyFields(existSecurityInfo, _securityInfo, SECURITYINFO_SYNC_FIELDS);
        }
        _securityInfo.setUId(existSecurityInfo.getUId());
      }
    }
  }

  // ********************* Own Methods *************************************
  private void constructEncryptionCert(Long certUID)
  {
    try
    {
      if (certUID != null)
      {
        Certificate cert = ServiceLookupHelper.getCertificateManager().findCertificateByUID(
                           certUID);
        _encryptionCert = new SyncCertificate(cert);
      }
    }
    catch (Exception ex)
    {
      Logger.err("[SyncSecurityInfo.getEncryptionCertBytes] Unable to retrieve certificate "+certUID, ex);
    }
  }

  /**
   * Set the enterprise ID that the SecurityInfo belongs to. The
   * enterprise ID will be used as ReferenceId of the Packaging.
   *
   * @param enterpriseID The enterpriseID.
   */
  public void setEnterpriseID(String enterpriseID)
  {
    if (_securityInfo != null)
      _securityInfo.setReferenceId(enterpriseID);
  }

  /**
   * Set the prefix for the names of the encapsulated SecurityInfo.
   *
   * @param prefix The name prefix.
   */
  public void setNamePrefix(String prefix)
  {
    if (_securityInfo != null)
      _securityInfo.setName(prefix + _securityInfo.getName());
  }

  /**
   * Get a SecurityInfo with the specified name.
   *
   * @param name The name of the SecurityInfo.
   * @param refId The referenceId of the SecurityInfo.
   * @return The SecurityInfo retrieved, or null if none exist with that name.
   * @exception Throwable Error during retrieval.
   *
   * @since 2.0 I6
   */
  private SecurityInfo getSecurityInfo(String name, String refId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SecurityInfo.NAME, filter.getEqualOperator(),
      name, false);
    filter.addSingleFilter(filter.getAndConnector(), SecurityInfo.REF_ID,
      filter.getEqualOperator(), refId, false);

    Collection results = getChannelMgr().getSecurityInfo(filter);

    if (results != null && results.size() > 0)
      return (SecurityInfo)results.iterator().next();

    return null;
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
  private IChannelManagerObj getChannelMgr()
    throws Throwable
  {
    if (_channelMgr == null)
    {
      _channelMgr = ServiceLookupHelper.getChannelManager();
    }

    return _channelMgr;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Sep 16 2002    Jagadeesh               Modified FieldId's for Certificates.
 * Oct 03 2002    Neo Sok Lay             Init boolean fields to false. Without
 *                                        this FMI cannot be found.
 * Oct 04 2002    Ang Meng Hua            Change partnerCategory from String to Short
 *                                        Added isPartner field
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 * Nov 23 2003    Guo Jianyu              Added compression
 * Nov 26 2003    Guo Jianyu              Added encryptionAlgorithm
 */
package com.gridnode.pdip.app.channel.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for PackagingInfo entity. A PackagingInfo keeps the inforamtion about
 * a Packaging Profile.<P>
 *
 * The Model:<BR><PRE>
 *   UId                            - UID for a  SecurityInfo entity instance.
 *   Name                           - Name of this SecurityInfo entity.
 *   Description                    - Description of this SecurityInfo entity.
 *   EncryptionType                 - EncryptionType - (None/Asymmetric/SMIME).
 *   EncryptionLevel                - Encryption Level - (64/256/512/1024).
 *   EncryptionCertificate          - Encryption Certificate.
 *   SignatureType                  - Signature Type- (None/SMIME).
 *   DigestAlgorithm                - Digest Algorithm -(MD5/SHA1).
 *   SignatureEncryptionCertificate - Signature Encryption Certificate.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * Note :- The two Certificate (1. Receipient Certificate 2. Sender's Certificate.) are represented
 * with there UID's. This UID represnets to a Certificate Entity in the Data Store.
 *
 *             ??   Points to Consider / Not Yet Known ??
 *                   ------------- ------------------
 *
 *  1. A Channel is associated  with Security Profile and Packaging Profile.
 *     When a BE(Business Entity) is exchanged with Partner's, as a part of
 *     exchange the Channels assigned to the BE is sent over.
 *
 *     The question here is -- Are Certificate's associated with the Security Profile
 *     will be sent over as a part of exchange.......??
 *
 *     If YES then Who will retrieve the Certificate Entity's associated with this
 *     Security Profile(Note: Only UID's are stored as Security Profile)
 *     and which Object holds this references. And How are they Sent...>??
 *
 *
 * @author Jagadeesh
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class SecurityInfo extends AbstractEntity implements ISecurityInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1659164216803476466L;
	private String _name = null;
  private String _description = null;
  private String _encryptionType = null;
  private int _encryptionLevel = 0;
  private Long _encryptionCertificateId = null;
  // The Receipient Certificate -used to "Encrypt"
  // the SessionKey with  receipient PublicKey - (PublicKey taken from Partner/Recipent Certificate).
  private String _signatureType = null;
  private String _digestAlgorithm = null;
  private Long _signatureEncryptionCertificateId = null;
  //Own Certificate of this User -  used to
  // "sign" the UserDocument.
  //private String _partnerCategory = null;
  protected Short _partnerCategory;
  private String _refId = null;
  private boolean _isDisable = false;
  protected boolean _isPartner = false;

  private String _compressionType = null;
  private String _compressionMethod = null;
  private int _compressionLevel = 0;

  private String _sequence = null;
  private String _encryptionAlgorithm = null;

  private transient int _securitylevel;

  public String getEntityDescr()
  {
    return new StringBuffer()
      .append(getName())
      .append('/')
      .append(getDescription())
      .toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  public String getDescription()
  {
    return _description;
  }

  public String getName()
  {
    return _name;
  }

  public String getEncryptionType()
  {
    return _encryptionType;
  }

  public int getEncryptionLevel()
  {
    return _encryptionLevel;
  }

  public Long getEncryptionCertificateID()
  {
    return _encryptionCertificateId;
  }

  public String getSignatureType()
  {
    return _signatureType;
  }

  public String getDigestAlgorithm()
  {
    return _digestAlgorithm;
  }

  public Long getSignatureEncryptionCertificateID()
  {
    return _signatureEncryptionCertificateId;
  }

  public Short getPartnerCategory()
  {
    return _partnerCategory;
  }

  public String getReferenceId()
  {
    return _refId;
  }

  public String getCompressionType()
  {
    return _compressionType;
  }

  public String getCompressionMethod()
  {
    return _compressionMethod;
  }

  public int getCompressionLevel()
  {
    return _compressionLevel;
  }

  public String getSequence()
  {
    return _sequence;
  }

  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }

  public boolean isDisable()
  {
    return _isDisable;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  // ******************** Setters for attributes ***************************

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public void setEncryptionType(String encryptionType)
  {
    _encryptionType = encryptionType;
  }

  public void setEncryptionLevel(int encryptionLevel)
  {
    _encryptionLevel = encryptionLevel;
  }

  public void setEncryptionCertificateID(Long encryptionCertificate)
  {
    _encryptionCertificateId = encryptionCertificate;
  }

  public void setSignatureType(String signatureType)
  {
    _signatureType = signatureType;
  }

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    _digestAlgorithm = digestAlgorithm;
  }

  public void setSignatureEncryptionCertificateID(Long signatureEncryptionCertificate)
  {
    _signatureEncryptionCertificateId = signatureEncryptionCertificate;
  }

  public void setPartnerCategory(Short partnerCategory)
  {
    _partnerCategory = partnerCategory;
  }

  public void setReferenceId(String refId)
  {
    _refId = refId;
  }

  public void setIsDisable(boolean isDisable)
  {
    _isDisable = isDisable;
  }

  public void setIsPartner(boolean isPartner)
  {
    _isPartner = isPartner;
  }

  public void setSecuritylevel(int level)
  {
    _securitylevel = level;
  }

  public int getSecuritylevel()
  {
    return _securitylevel;
  }

  public void setCompressionType(String compressionType)
  {
    _compressionType = compressionType;
  }

  public void setCompressionMethod(String compressionMethod)
  {
    _compressionMethod = compressionMethod;
  }

  public void setCompressionLevel(int compressionLevel)
  {
    _compressionLevel = compressionLevel;
  }

  public void setSequence(String sequence)
  {
    _sequence = sequence;
  }

  public void setEncryptionAlgorithm(String encryptionAlgorithm)
  {
    _encryptionAlgorithm = encryptionAlgorithm;
  }
}

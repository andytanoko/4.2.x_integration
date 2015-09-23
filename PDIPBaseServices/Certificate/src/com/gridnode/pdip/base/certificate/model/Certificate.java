/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Certificate
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-July-2002    Jagadeesh           Created.
 * 13-Sept-2002    Jagadeesh           Modified to Add isMaster and isPartner
 *
 * 15-April-2003   Qingsong            Added: Fields for Keystore, Truststore.
 * 18 Jul 2003    Neo Sok Lay         Change EntityDescr.
 * 30-March-2004   Guo Jianyu          Added: relatedCertUid
 * 26-Jul-2006     Tam Wei Xiang       Added: startDate, endDate
 * 27-Jul-2006     Tam Wei Xiang       Added: isCA
 * 24-Aug-2006     Tam Wei Xiang       Added: replacementCertUID
 * 01-Aug-2008		Wong Yee Wah		#38  Added: certificateSwapping
 * 01-Aug-2008		Wong Yee Wah		#38  Added: isForcedGetReplacementCert
 */
package com.gridnode.pdip.base.certificate.model;

import java.util.Date;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Certificate entity.
 *
 * The Model:<BR><PRE>
 *   UId        - UID for a Certificate entity instance.
 *   Id         - Id for each Certificate.
 *   Name       - Name.
 *   SerialNum  - Serial Number. 
 *   IssuerName - Issuer Name.
 *   Certificate- Certificate.
 *   PublicKey  - Public Key.
 *   PrivateKey - PrivateKey.
 *   RevokeID   - Revoke ID.
 *   StartDate  - cert valid from
 *   EndDate    - cert valid to
 *   isCA       - indicate whether the cert entity is CA cert
 *   replacementCertUID - indicate the replacement cert while this cert has expired
 *   certificateSwapping - CertificateSwapping Entity (contain certificate swap date and swap time)
 *   isForcedGetReplacementCert - a flag used to force the certificate to be swap
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 */
public class Certificate
       extends AbstractEntity
       implements ICertificate
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1689150779116064530L;
	protected int _id;
  protected String _name;
  protected String _serialNum;
  protected String _issuerName;
  protected String _certificate;
  protected String _publicKey;
  protected String _privateKey;
  protected int _revokeID;
  protected boolean _isMaster;
  protected boolean _isInKeyStore;
  protected boolean _isInTrustStore;
  protected boolean _isPartner;
  protected Short _category;
  protected Long _relatedCertUid;
  protected Date _startDate;
  protected Date _endDate;
  protected Boolean _isCA = new Boolean(false);
  protected Long _replacementCertUid;
  protected CertificateSwapping _certificateSwapping;
  protected boolean _isForcedGetReplacementCert = new Boolean(false);
  
  public Certificate()
  {
  }

   // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
    return UID;
  }
/*
  public String getDescription()
  {
    return getID() + "-" + getIssuerName();
  }
*/

  public String getEntityDescr()
  {
    String revoked = (getRevokeId() != 0)?"Revoked":"Valid";
    
    return new StringBuffer().append(getID()).append('/').append(getCertName())
                .append('/').append(revoked).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }
/*
  public String getName()
  {
    return ENTITY_NAME;
  }
*/

  // ******************** Getters for attributes ***************************

  public int getID()
  {
    return _id;
  }

  public String getCertName()
  {
    return _name;
  }

  public String getIssuerName()
  {
    return _issuerName;
  }

  public String getSerialNumber()
  {
    return _serialNum;
  }

  public String getCertificate()
  {
   return _certificate;
  }

  public String getPublicKey()
  {
    return _publicKey;
  }

  public String getPrivateKey()
  {
    return _privateKey;
  }

  public int getRevokeId()
  {
    return _revokeID;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  public boolean isMaster()
  {
    return _isMaster;
  }

  public boolean isInKeyStore()
  {
    return _isInKeyStore;
  }

  public boolean isInTrustStore()
  {
    return _isInTrustStore;
  }

  public Short getCategory()
  {
    return _category;
  }

  public Long getRelatedCertUid()
  {
    return _relatedCertUid;
  }
  
  public CertificateSwapping getCertificateSwapping()
  {
    return _certificateSwapping;
  }

  public boolean getIsForcedGetReplacementCert()
  {
    return _isForcedGetReplacementCert;
  }
  
  // ******************** Setters for attributes ***************************

  public void setID(int id)
  {
    _id = id;
  }

  public void setCertName(String name)
  {
    _name = name;
  }

  public void setIssuerName(String issuername)
  {
    _issuerName = issuername;
  }

  public void setSerialNumber(String serialNum)
  {
    _serialNum = serialNum;
  }


  public void setCertificate(String certificate)
  {
    _certificate = certificate;
  }

  public void setPublicKey(String publickey)
  {
    _publicKey = publickey;
  }

  public void setPrivateKey(String privatekey)
  {
    _privateKey = privatekey;
  }

  public void setRevokeId(int revokeid)
  {
    _revokeID = revokeid;
  }

  public void setPartner(boolean isPartner)
  {
    _isPartner = isPartner;
  }

  public void setMaster(boolean isMaster)
  {
    _isMaster = isMaster;
  }

 public void setInKeyStore(boolean isInKeyStore)
  {
    _isInKeyStore = isInKeyStore;
  }

  public void setInTrustStore(boolean isInTrustStore)
  {
    _isInTrustStore = isInTrustStore;
  }

  public void setCategory(Short category)
  {
    _category = category;
  }

  public void setRelatedCertUid(Long relatedCertUid)
  {
    _relatedCertUid = relatedCertUid;
  }

	public Date getEndDate()
	{
		return _endDate;
	}

	public void setEndDate(Date date)
	{
		_endDate = date;
	}

	public Date getStartDate()
	{
		return _startDate;
	}

	public void setStartDate(Date date)
	{
		_startDate = date;
	}

	public Boolean isCA()
	{
		return _isCA;
	}

	public void setCA(Boolean _isca)
	{
		_isCA = _isca;
	}

	public Long getReplacementCertUid()
	{
		return _replacementCertUid;
	}

	public void setReplacementCertUid(Long certUid)
	{
		_replacementCertUid = certUid;
	}
  
   public void setCertificateSwapping(CertificateSwapping certificateSwapping)
   {
     _certificateSwapping = certificateSwapping;
   }
   
   public void setIsForcedGetReplacementCert(boolean isForcedGetReplacementCert)
   {
	   _isForcedGetReplacementCert = isForcedGetReplacementCert;
   }
  
}
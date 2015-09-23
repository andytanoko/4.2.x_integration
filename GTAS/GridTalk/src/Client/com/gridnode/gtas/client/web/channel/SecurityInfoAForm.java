/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "PartnerCat" stuff
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.ctrl.IGTSecurityInfoEntity;

import org.apache.struts.action.*;
import javax.servlet.http.*;

public class SecurityInfoAForm extends GTActionFormBase
{
  private boolean _isNewEntity;

  private String _name;
  private String _description;
  private String _encType;
  private String _encLevel;
  private String _encCert;
  private String _sigType;
  private String _digestAlgorithm;
  private String _sigEncCert;
  private String _isPartner;
  private String _partnerCategory;
  private String _refId;
  private String _encryptionAlgorithm;  // 20031126 DDJ
  private String _sequence;             // 20031126 DDJ
  private String _compressionType;      // 20031126 DDJ
  private String _compressionMethod;    // 20031126 DDJ
  private String _compressionLevel;     // 20031126 DDJ


  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {

    // Following as workaround only
    /*_name = "";
    _description = "";
    _encType = "";
    _encLevel = "0";
    _encCert = "0";
    _sigType = "";
    _digestAlgorithm = "";
    _sigEncCert = "0";*/
    //-----------

    // Only reset if new entity (when field is editable)
    if(_isNewEntity)
    { // Field readonly once saved, so only reset if new
      _isPartner = "false";
    }
  }

  void setIsNewEntity(boolean flag)
  { _isNewEntity = flag; }

  public boolean isNewEntity()
  { return _isNewEntity; }

  public void setRefId(String value)
  { _refId = value; }

  public String getRefId()
  { return _refId; }

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getEncType()
  { return _encType; }

  public void setEncType(String encType)
  { _encType=encType; }

  public String getEncLevel()
  { return _encLevel; }

  public void setEncLevel(String encLevel)
  { _encLevel=encLevel; }

  public String getEncCert()
  { return _encCert; }

  public void setEncCert(String encCert)
  { _encCert=encCert; }

  public String getSigType()
  { return _sigType; }

  public void setSigType(String sigType)
  { _sigType=sigType; }

  public String getDigestAlgorithm()
  { return _digestAlgorithm; }

  public void setDigestAlgorithm(String digestAlgorithm)
  { _digestAlgorithm=digestAlgorithm; }

  public String getSigEncCert()
  { return _sigEncCert; }

  public void setSigEncCert(String sigEncCert)
  { _sigEncCert=sigEncCert; }

  public void setIsPartner(String value)
  { _isPartner = value; }

  public String getIsPartner()
  { return _isPartner; }

  public void setPartnerCategory(String value)
  { _partnerCategory = value; }

  public String getPartnerCategory()
  { return _partnerCategory; }

  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }

  public void setEncryptionAlgorithm(String string)
  {
    _encryptionAlgorithm = string;
  }

  public String getSequence()
  {
    return _sequence;
  }

  public void setSequence(String string)
  {
    _sequence = string;
  }

  public String getCompressionType()
  {
    return _compressionType;
  }

  public void setCompressionType(String string)
  {
    _compressionType = string;
  }

  public String getCompressionMethod()
  {
    return _compressionMethod;
  }

  public void setCompressionMethod(String string)
  {
    _compressionMethod = string;
  }

  public String getCompressionLevel()
  {
    return _compressionLevel;
  }

  public Integer getCompressionLevelAsInteger()
  {
    return StaticUtils.integerValue(_compressionLevel);
  }

  public void setCompressionLevel(String string)
  {
    _compressionLevel = string;
  }

  boolean encTypeHasValue()
  {
    if( (_encType == null) || "".equals(_encType) )
    {
      return false;
    }
    return !(IGTSecurityInfoEntity.ENC_TYPE_NONE.equals(_encType));
  }

  boolean sigTypeHasValue()
  {
    if( (_sigType == null) || "".equals(_sigType) )
    {
      return false;
    }
    return !(IGTSecurityInfoEntity.SIG_TYPE_NONE.equals(_sigType));
  }

  boolean compressionTypeHasValue()
  {
    if( (_compressionType == null) || "".equals(_compressionType) )
    {
      return false;
    }
    return !(IGTSecurityInfoEntity.COMPRESSION_TYPE_NONE.equals(_compressionType));
  }
}
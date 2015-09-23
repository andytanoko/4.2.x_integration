/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Oct 03 2002    Neo Sok Lay             Init boolean fields to false. Without
 *                                        this FMI cannot be found.
 * Oct 04 2002    Ang Meng Hua            Change partnerCategory from String to Short
 *                                        Added isPartner field
 * Jan 07 2002    Goh Kan Mun             Added split, splitSize and splitThreshold fields.
 * Jan 30 2002    Goh Kan Mun             Added transient fields and methods for isRelay and isSplitAck.
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 * Oct 30 2003    Guo Jianyu              Added pkgInfoExtension.
 */
package com.gridnode.pdip.app.channel.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for PackagingInfo entity. A PackagingInfo keeps the inforamtion about
 * a Packaging Profile.<P>
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a  PackagingInfo entity instance.
 *   Name             - Name of this PackagingInfo entity.
 *   Description      - Description of this PackagingInfo entity.
 *   Envelope         - Envelope type - (None/RNIF1/RNIF2).
 *   Zip              - Zip/or NoZip.
 *   ZipThreshold     - Zip Threshold.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Jagadeesh
 *
 * @version GT 2.2 I1
 * @since 2.0
 *
 */
public class PackagingInfo extends AbstractEntity implements IPackagingInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 647444366983125451L;
	private String _name = null;
  private String _description = null;
  private String _envelope = null;
  private boolean _zip = true;
  private int _zipThreshold = 0;
  private String _refId = null;
  private boolean _isDisable = false;
  //private String _partnerCategory = null;
  protected Short _partnerCategory;
  protected boolean _isPartner = false;
  private int _splitThreshold = 5000;
  private int _splitSize = 5000;
  private boolean _split = false;
  private PackagingInfoExtension _pkgInfoExtension;
  transient private boolean _isRelay = false;
  transient private boolean _isSplitAck = false;

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

  public String getEnvelope()
  {
    return _envelope;
  }

  public boolean isZip()
  {
    return _zip;
  }

  public int getZipThreshold()
  {
    return _zipThreshold;
  }

  public Short getPartnerCategory()
  {
    return _partnerCategory;
  }

  public String getReferenceId()
  {
    return _refId;
  }

  public boolean isDisable()
  {
    return _isDisable;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  public boolean isSplit()
  {
    return _split;
  }

  public boolean isSplitAck()
  {
    return _isSplitAck;
  }

  public int getSplitThreshold()
  {
    return _splitThreshold;
  }

  public int getSplitSize()
  {
    return _splitSize;
  }

  public boolean isRelay()
  {
    return _isRelay;
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

  public void setEnvelope(String envelope)
  {
    _envelope = envelope;
  }

  public void setZip(boolean zip)
  {
    _zip = zip;
  }

  public void setZipThreshold(int zipThreshold)
  {
    _zipThreshold = zipThreshold;
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

  public void setSplit(boolean split)
  {
    _split = split;
  }

  public void setSplitThreshold(int splitThreshold)
  {
    _splitThreshold = splitThreshold;
  }

  public void setSplitSize(int splitSize)
  {
    _splitSize = splitSize;
  }

  public void setIsRelay(boolean isRelay)
  {
    _isRelay = isRelay;
  }

  public void setSplitAck(boolean splitAck)
  {
    _isSplitAck = splitAck;
  }

  public PackagingInfoExtension getPkgInfoExtension()
  {
    return _pkgInfoExtension;
  }

  public void setPkgInfoExtension(PackagingInfoExtension pkgInfoExtension)
  {
    _pkgInfoExtension = pkgInfoExtension;
  }
}

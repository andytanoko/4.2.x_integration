/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Partner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;

/**
 * This is an object model for Partner entity. A Partner contains the partner-related
 * information.<P>
 *
 * The Model:<BR><PRE>
 *   PartnerID    - ID of the Partner.
 *   Name         - Name of the Partner
 *   Description  - Description of the Partner.
 *   PartnerType  - PartnerType of the Partner.
 *   PartnerGroup - PartnerGroup of the Partner.
 *   CreateTime   - Time of creation of the partner
 *   CreateBy     - User ID of the user who created this partner.
 *   State        - State of the Partner: Enabled, Disabled, Deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public class Partner extends AbstractEntity
  implements IPartner
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8833275574721603166L;
	protected String       _partnerID;
  protected String       _name;
  protected String       _description;
  protected PartnerType  _partnerType;
  protected PartnerGroup _partnerGroup;
  protected Date         _createTime;
  protected String       _createBy;
  protected short        _state = STATE_ENABLED;

  // ***************** Getters for generic attributes ***********************

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getPartnerID() + "/" + getDescription();
  }

  // ********************** Getters for attributes **************************

  public String getPartnerID()
  {
    return _partnerID;
  }

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public PartnerType getPartnerType()
  {
    return _partnerType;
  }

  public PartnerGroup getPartnerGroup()
  {
    return _partnerGroup;
  }

  public Date getCreateTime()
  {
    return _createTime;
  }

  public String getCreateBy()
  {
    return _createBy;
  }

  public short getState()
  {
    return _state;
  }

  // *********************** Setters for attributes **************************

  public void setPartnerID (String partnerID)
  {
    _partnerID = partnerID;
  }

  public void setName (String name)
  {
    _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setPartnerType(PartnerType partnerType)
  {
    _partnerType = partnerType;
  }

  public void setPartnerGroup (PartnerGroup partnerGroup)
  {
    _partnerGroup = partnerGroup;
  }

  public void setCreateTime(Date createTime)
  {
    _createTime = createTime;
  }

  public void setCreateBy(String createBy)
  {
    _createBy = createBy;
  }

  public void setState(short state)
  {
    _state = state;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 04 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Partner entity. A Partner contains the partner-related
 * information.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a PartnerGroup entity instance.
 *   Version      - Version.
 *   CanDelete    - Flag indicating whether this data can be deleted.
 *   Name         - Name of the PartnerGroup.
 *   Description  - Description of the PartnerGroup.
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
public class PartnerGroup extends AbstractEntity
  implements IPartnerGroup
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7501460054255249875L;
	protected String      _name;
  protected String      _description;
  protected PartnerType _partnerType;

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
    return getName() + "/" + getDescription();
  }

  // ********************** Getters for attributes **************************

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

  // *********************** Setters for attributes **************************

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
}
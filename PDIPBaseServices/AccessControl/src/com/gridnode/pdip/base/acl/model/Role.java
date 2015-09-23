/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Role.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 */
package com.gridnode.pdip.base.acl.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Role entity. A Role contains the role-related
 * information.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a Role entity instance.
 *   Role         - Name of the Role.
 *   Description  - Description of the Role.
 *   canDelete    - Flag indicating whether this data be deleted.
 *   Version      - Version.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 *
 */

public class Role extends AbstractEntity implements IRole
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4933787716324150785L;
	protected String _role = null;
  protected String _description = null;
  protected boolean _canDelete = true;

  public Role()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public String getEntityDescr()
  {
    return new StringBuffer(getRole()).append('/').append(getDescr()).toString();
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

  public String getRole()
  {
    return _role;
  }

  public String getDescr()
  {
    return _description;
  }

  public boolean canDelete()
  {
    return _canDelete;
  }

  // ******************** Setters for attributes ***************************

  public void setRole(String role)
  {
    this._role = role;
  }

  public void setDescr(String descr)
  {
    this._description = descr;
  }

  public void setCanDelete(boolean canDelete)
  {
    this._canDelete = canDelete;
  }

}
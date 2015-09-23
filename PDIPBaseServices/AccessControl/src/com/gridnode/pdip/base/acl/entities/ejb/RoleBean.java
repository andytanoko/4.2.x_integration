/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * The class implements the Entity Bean for the Role.
 *
 * @author Goh Kan Mun
 *
 * @since 2.0
 * @version 2.0
 */

public class RoleBean extends AbstractEntityBean
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2477750794439783054L;

	public RoleBean()
  {
  }

  /**
   * To retrieve the entity name of this bean.
   *
   * @return the entity name.
   *
   */
  public String getEntityName()
  {
    return Role.ENTITY_NAME;
  }

}
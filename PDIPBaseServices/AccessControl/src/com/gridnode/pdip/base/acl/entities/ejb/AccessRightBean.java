/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.base.acl.model.AccessRight;

import com.gridnode.pdip.framework.db.ejb.*;

/**
 * This bean manages the persistency for AccessRight entities to the database.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AccessRightBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3876584253339864048L;

	public String getEntityName()
  {
    return AccessRight.ENTITY_NAME;
  }
}
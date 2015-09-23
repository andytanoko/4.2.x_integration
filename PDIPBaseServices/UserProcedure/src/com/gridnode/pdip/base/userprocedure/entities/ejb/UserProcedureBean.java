/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 */
package com.gridnode.pdip.base.userprocedure.entities.ejb;

import com.gridnode.pdip.base.userprocedure.model.UserProcedure;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A ProcedureDefBean provides persistency services for UserProcedure Definition.
 *
 * @author Jagadeesh
 * @version 2.0
 */

public class UserProcedureBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1304270070175157906L;

	/**
   * To retrieve the entity name of this bean.
   *
   * @return the entity name.
   * @version 2.0
   */

  public String getEntityName()
  {
     return UserProcedure.ENTITY_NAME;
  }
}
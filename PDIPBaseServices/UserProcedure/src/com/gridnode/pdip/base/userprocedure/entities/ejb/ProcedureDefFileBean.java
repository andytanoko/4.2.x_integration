/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 */



package com.gridnode.pdip.base.userprocedure.entities.ejb;

import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A ProcedureDefFileBean provides persistency services for Procedure Definition
 * File.
 * @author Jagadeesh
 * @version 2.0
 */


public class ProcedureDefFileBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6425748016247950484L;

	/**
   * To retrieve the entity name of this bean.
   *
   * @return the entity name.
   * @version 2.0
   */

  public String getEntityName()
  {
     return ProcedureDefFile.ENTITY_NAME;
  }

}



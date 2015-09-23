/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * The class implements the Entity Bean for the Action
 *
 * @author Srinath
 *
 */

public class ActionBean extends AbstractEntityBean
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8901654793884851750L;

	public ActionBean()
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
    return Action.ENTITY_NAME;
  }

}
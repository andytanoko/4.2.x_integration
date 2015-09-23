/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertActionBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	             Created
 * Mar 03 2003    Neo Sok Lay             No version check required for this.
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.framework.db.ejb.*;


/**
 * The class implements the Entity Bean for the AlertAction.
 *
 * @author Srinath
 *
 */

public class AlertActionBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2190198116458136175L;

	public AlertActionBean()
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
    return AlertAction.ENTITY_NAME;
  }

  protected boolean isVersionCheckRequired()
  {
    return false;
  }

}
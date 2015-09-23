/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTypeBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 05 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.AlertType;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * The class implements the Entity Bean for the AlertType.
 *
 * @author Srinath
 *
 */

public class AlertTypeBean extends AbstractEntityBean
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5531214002709388632L;

	public AlertTypeBean()
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
    return AlertType.ENTITY_NAME;
  }

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertListBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 26 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.AlertList;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * The class implements the Entity Bean for the AlertList.
 *
 * @author Srinath
 *
 */

public class AlertListBean extends AbstractEntityBean
{
 
  /**
	 */
	private static final long serialVersionUID = 1418680222161265513L;

	public AlertListBean()
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
    return AlertList.ENTITY_NAME;
  }

}
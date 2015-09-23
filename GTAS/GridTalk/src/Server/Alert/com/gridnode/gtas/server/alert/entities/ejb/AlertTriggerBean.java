/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.alert.entities.ejb;

import com.gridnode.gtas.server.alert.helpers.AlertTriggerDAOHelper;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
 
/**
 * A AlertTriggerBean provides persistency services for AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class AlertTriggerBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5540827775327248901L;

	public String getEntityName()
  {
    return AlertTrigger.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity trigger)
    throws Exception
  {
    AlertTriggerDAOHelper.getInstance().checkDuplicate(
      (AlertTrigger)trigger, false);
  }

}
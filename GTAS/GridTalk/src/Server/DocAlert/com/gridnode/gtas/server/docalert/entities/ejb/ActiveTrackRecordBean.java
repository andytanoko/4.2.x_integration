/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActiveTrackRecordBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.entities.ejb;

import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A ActiveTrackRecordBean provides persistency services for ActiveTrackRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ActiveTrackRecordBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6926495136851848244L;

	public String getEntityName()
  {
    return ActiveTrackRecord.ENTITY_NAME;
  }

  protected boolean isVersionCheckRequired()
  {
    return false;
  }


}
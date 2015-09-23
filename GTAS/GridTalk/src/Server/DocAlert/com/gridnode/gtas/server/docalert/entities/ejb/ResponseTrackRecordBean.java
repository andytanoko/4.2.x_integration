/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.entities.ejb;

import com.gridnode.gtas.server.docalert.helpers.ResponseTrackRecordDAOHelper;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A ResponseTrackRecordBean provides persistency services for ResponseTrackRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ResponseTrackRecordBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5408236821848307852L;

	public String getEntityName()
  {
    return ResponseTrackRecord.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity trackRecord)
    throws Exception
  {
    ResponseTrackRecordDAOHelper.getInstance().checkDuplicate(
      (ResponseTrackRecord)trackRecord, false);
  }

}
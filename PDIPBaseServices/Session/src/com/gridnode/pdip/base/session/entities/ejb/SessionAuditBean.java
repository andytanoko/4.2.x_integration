/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionAuditBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Ooi Hui Linn        Created
 * Jun 07 2002    Neo Sok Lay         Version checking not required.
 */

package com.gridnode.pdip.base.session.entities.ejb;
import com.gridnode.pdip.base.session.model.SessionAudit;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A SessionAuditBean provides persistency services for Session management.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public class SessionAuditBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2931490300855647327L;

	public String getEntityName()
  {
    return SessionAudit.ENTITY_NAME;
  }

  protected boolean isVersionCheckRequired()
  {
    return false;
  }
}

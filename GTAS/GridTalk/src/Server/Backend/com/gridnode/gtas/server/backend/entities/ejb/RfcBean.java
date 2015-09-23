/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.entities.ejb;

import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
/**
 * A RfcBean provides persistency services for Rfc.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class RfcBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5158218698269144792L;

	public String getEntityName()
  {
    return Rfc.ENTITY_NAME;
  }
}
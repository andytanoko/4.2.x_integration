/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.entities.ejb;

import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A PartnerFunctionBean provides persistency services for PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerFunctionBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1069835375733791752L;

	public String getEntityName()
  {
    return PartnerFunction.ENTITY_NAME;
  }
}
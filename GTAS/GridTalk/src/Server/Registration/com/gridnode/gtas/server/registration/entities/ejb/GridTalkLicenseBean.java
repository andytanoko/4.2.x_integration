/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkLicenseBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.entities.ejb;

import com.gridnode.gtas.server.registration.model.GridTalkLicense;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/** 
 * A GridTalkLicenseBean provides persistency services for GridTalkLicense.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class GridTalkLicenseBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9156594726305070577L;

	public String getEntityName()
  {
    return GridTalkLicense.ENTITY_NAME;
  }
}
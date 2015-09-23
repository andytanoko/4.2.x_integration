/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
/**
 * A GridDocumentBean provides persistency services for GridDocument.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GridDocumentBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7734739231152444834L;

	public String getEntityName()
  {
    return GridDocument.ENTITY_NAME;
  }

  protected boolean isVersionCheckRequired()
  {
    return false;
  }
}
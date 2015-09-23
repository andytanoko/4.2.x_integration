/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentRegistryBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.gtas.server.document.model.AttachmentRegistry;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A AttachmentRegistryBean provides persistency services for AttachmentRegistry.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class AttachmentRegistryBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7359266868405141850L;

	public String getEntityName()
  {
    return AttachmentRegistry.ENTITY_NAME;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
/**
 * A AttachmentBean provides persistency services for Attachment.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class AttachmentBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2861933818559391046L;

	public String getEntityName()
  {
    return Attachment.ENTITY_NAME;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Attachment.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 22 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import java.util.ArrayList;
import java.util.Collection;

public class AttachmentInfo extends ArrayList
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1685957163526759739L;

	public void setAttachments(Collection list)
  {
    clear();
    addAll(list);
  }

  public Collection getAttachments()
  {
    return this;
  }
}
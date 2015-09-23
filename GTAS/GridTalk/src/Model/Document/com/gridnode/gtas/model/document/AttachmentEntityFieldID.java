/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.document;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Attachment module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class AttachmentEntityFieldID
{
  private Hashtable _table;
  private static AttachmentEntityFieldID _self = null;

  private AttachmentEntityFieldID()
  {
    _table = new Hashtable();

    //Attachment
    _table.put(IAttachment.ENTITY_NAME,
      new Number[]
      {
        IAttachment.FILENAME,
        IAttachment.ORIGINAL_FILENAME,
        IAttachment.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new AttachmentEntityFieldID();
    }
    return _self._table;
  }
}
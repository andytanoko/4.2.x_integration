/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented IDocumentType.CAN_DELETE
 */
package com.gridnode.gtas.model.document;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the DocumentType module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class DocumentTypeEntityFieldID
{
  private Hashtable _table;
  private static DocumentTypeEntityFieldID _self = null;

  private DocumentTypeEntityFieldID()
  {
    _table = new Hashtable();

    //DocumentType
    _table.put(IDocumentType.ENTITY_NAME,
      new Number[]
      {
//        IDocumentType.CAN_DELETE,
        IDocumentType.DESCRIPTION,
        IDocumentType.DOC_TYPE,
        IDocumentType.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new DocumentTypeEntityFieldID();
    }
    return _self._table;
  }
}
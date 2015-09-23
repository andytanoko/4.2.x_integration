/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-09-03    Wong Yee Wah        Created
 */
package com.gridnode.gtas.model.document;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the DocumentType module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Wong Yee Wah
 *
 * @version 4.0.2
 * @since 4.0.2
 */
public class AS2DocTypeMappingEntityFieldID
{
  private Hashtable _table;
  private static AS2DocTypeMappingEntityFieldID _self = null;

  private AS2DocTypeMappingEntityFieldID()
  {
    _table = new Hashtable();

    //AS2DocTypeMapping
    _table.put(IAS2DocTypeMapping.ENTITY_NAME,
      new Number[]
      {
        IAS2DocTypeMapping.CAN_DELETE,
        IAS2DocTypeMapping.AS2_DOC_TYPE,
        IAS2DocTypeMapping.DOC_TYPE,
        IAS2DocTypeMapping.PARTNER_ID,
        IAS2DocTypeMapping.UID
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new AS2DocTypeMappingEntityFieldID();
    }
    return _self._table;
  }
}
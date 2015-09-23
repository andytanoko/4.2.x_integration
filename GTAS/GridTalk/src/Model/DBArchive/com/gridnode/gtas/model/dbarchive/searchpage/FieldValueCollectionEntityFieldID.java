/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldValueCollectionEntityFieldID.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 20 Oct 2005      	Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.model.dbarchive.searchpage;

import java.util.Hashtable;

public class FieldValueCollectionEntityFieldID
{
  private static Hashtable table;
  
  static
  {
    table = new Hashtable();
    table.put(IFieldValueCollection.ENTITY_NAME,
        new Number[] 
        {
    			IFieldValueCollection.UID,
    			IFieldValueCollection.Value
        });
  }
  
  public static Hashtable getEntityFieldID()
  {
    return table;
  }
}

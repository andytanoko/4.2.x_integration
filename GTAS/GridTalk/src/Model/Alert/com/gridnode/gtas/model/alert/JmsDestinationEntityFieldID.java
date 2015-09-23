/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 * Feb 06 2007    Chong SoonFui       Commented IJmsDestination.CAN_DELETE
 */
package com.gridnode.gtas.model.alert;

import java.util.Hashtable;

public class JmsDestinationEntityFieldID
{
  private static Hashtable _table;

  static
  {
    _table = new Hashtable();

    _table.put(IJmsDestination.ENTITY_NAME,
      new Number[]
      {
    		IJmsDestination.UID,
//    		IJmsDestination.CAN_DELETE,
    		IJmsDestination.NAME,
    		IJmsDestination.TYPE,
    		IJmsDestination.JNDI_NAME,
    		IJmsDestination.DELIVERY_MODE,
    		IJmsDestination.CONNECTION_USER,
    		IJmsDestination.CONNECTION_PASSWORD,
    		IJmsDestination.LOOKUP_PROPERTIES,
    		IJmsDestination.RETRY_INTERVAL,
    		IJmsDestination.MAXIMUM_RETRIES,
    		IJmsDestination.PRIORITY,
    		IJmsDestination.CONNECTION_FACTORY_JNDI,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    return _table;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedureEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 23 2003    Koh Han Sing            Created
 */
package com.gridnode.gtas.model.userprocedure;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of SoapProcedure.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */


public class SoapProcedureEntityFieldID
{
  private Hashtable _table;
  private static SoapProcedureEntityFieldID _self = null;

  private SoapProcedureEntityFieldID()
  {
      _table = new Hashtable();

      _table.put(ISoapProcedure.ENTITY_NAME,
      new Number[]
      {
        ISoapProcedure.METHOD_NAME,
        ISoapProcedure.TYPE,
        ISoapProcedure.USER_NAME,
        ISoapProcedure.PASSWORD,
      });

 }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new SoapProcedureEntityFieldID();
    }
    return _self._table;
  }


}
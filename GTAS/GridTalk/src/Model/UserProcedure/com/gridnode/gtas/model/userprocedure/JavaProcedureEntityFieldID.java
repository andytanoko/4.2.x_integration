package com.gridnode.gtas.model.userprocedure;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of JavaProcedure.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public class JavaProcedureEntityFieldID
{
  private Hashtable _table;
  private static JavaProcedureEntityFieldID _self = null;

  private JavaProcedureEntityFieldID()
  {
      _table = new Hashtable();

      _table.put(IJavaProcedure.ENTITY_NAME,
      new Number[]
      {
        IJavaProcedure.CLASS_NAME,
        IJavaProcedure.CLASS_PATH,
        IJavaProcedure.IS_LOCAL,
        IJavaProcedure.JVM_OPTIONS,
        IJavaProcedure.METHOD_NAME,
        IJavaProcedure.TYPE,
        //IJavaProcedure.CMD_LINE_EXPR,
        IJavaProcedure.ARGUMENTS
      });

 }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new JavaProcedureEntityFieldID();
    }
    return _self._table;
  }


}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay        Created.
 */
package com.gridnode.gtas.server.activation.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.gridnode.gtas.model.activation.ActivationEntityFieldID;
import com.gridnode.gtas.model.activation.SearchEntityFieldID;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.activation.model.SearchGridNodeQuery;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the Activation module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public final class ActionHelper
{
  // ***************** Get Manager Helpers *****************************


  // ********************** Verification Helpers **********************

  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
  public static boolean isEmpty(String val)
  {
    return val==null || val.trim().length() == 0;
  }

  /**
   * Assert that a GridNode is Inactive. A GridNode is Inactive if it does not
   * exist in the local database or it's State=Inactive.
   *
   * @param gridnodeID The GridNode ID of the GridNode to check.
   * @throws Exception If the GridNode is not Inactive.
   */
  public static void assertInactivePartner(Integer gridnodeID)
    throws Exception
  {
    try
    {
      GridNode gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(
                      gridnodeID.toString());

      if (gn.getState() != gn.STATE_INACTIVE)
        throw new Exception("GridNode "+gridnodeID+ "is Not an Inactive GridNode!");
    }
    catch (FindEntityException ex)
    {

    }

  }

  /**
   * Assert that a GridNode is Active. A GridNode is Active if it
   * exists in the local database or it's State=Active.
   *
   * @param gridnodeID The GridNode ID of the GridNode to check.
   * @throws Exception If the GridNode is not Active.
   */
  public static void assertActivePartner(Integer gridnodeID)
    throws Exception
  {
    try
    {
      GridNode gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(
                      gridnodeID.toString());

      if (gn.getState() != gn.STATE_ACTIVE)
        throw new Exception("GridNode "+gridnodeID+ "is Not an Active GridNode!");
    }
    catch (FindEntityException ex)
    {

    }
  }

  // ******************** Conversion Helpers ******************************

  public static void copyEntityFields(Map from, AbstractEntity entity)
  {
    if (from != null)
    {
      for (Iterator i=from.keySet().iterator(); i.hasNext(); )
      {
        Number fieldID = (Number)i.next();
        entity.setFieldValue(fieldID, from.get(fieldID));
      }
    }
  }

  /**
   * Convert a SearchGridNodeQuery to Map object.
   *
   * @param query The SearchGridNodeQuery to convert.
   * @return A Map object converted from the specified SearchGridNodeQuery.
   *
   * @since 2.0 I6
   */
  public static Map convertSearchQueryToMap(SearchGridNodeQuery query)
  {
    return SearchGridNodeQuery.convertToMap(
             query,
             SearchEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a ActivationRecord to Map object.
   *
   * @param record The ActivationRecord to convert.
   * @return A Map object converted from the specified ActivationRecord.
   *
   * @since 2.0 I6
   */
  public static Map convertActivationRecordToMap(ActivationRecord record)
  {
    return ActivationRecord.convertToMap(
             record,
             ActivationEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of ActivationRecord(s) to Map objects.
   *
   * @param recList The collection of ActivationRecord(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ActivationRecord(s).
   *
   * @since 2.0 I6
   */
  public static Collection convertActRecordsToMapObjects(Collection recList)
  {
    return ActivationRecord.convertEntitiesToMap(
             (ActivationRecord[])recList.toArray(new ActivationRecord[recList.size()]),
             ActivationEntityFieldID.getEntityFieldID(),
             null);
  }


  // ************************ Finder Helpers *******************************

}
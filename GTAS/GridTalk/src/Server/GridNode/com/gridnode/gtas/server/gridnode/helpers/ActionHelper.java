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
 * Sep 05 2002    Neo Sok Lay         Created
 * Oct 28 2002    Neo Sok Lay         Add services for GridNode.
 * Nov 05 2002    Neo Sok Lay         Add services for ConnectionStatus.
 */
package com.gridnode.gtas.server.gridnode.helpers;

import java.util.*;

import com.gridnode.gtas.model.gridnode.GridNodeEntityFieldID;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GnCategory;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerHome;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerObj;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the GridNode module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class ActionHelper
{
  public static final EntityOrderComparator CATEGORY_COMPARATOR =
    new EntityOrderComparator(GnCategory.CATEGORY_NAME);

  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the CoyProfileManagerBean.
   *
   * @return The EJBObject to the CoyProfileManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I5
   */
  public static ICoyProfileManagerObj getCoyProfileManager()
    throws ServiceLookupException
  {
    return (ICoyProfileManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ICoyProfileManagerHome.class.getName(),
      ICoyProfileManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the GridNodeManagerBean.
   *
   * @return The EJBObject to the GridNodeManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I5
   */
  public static IGridNodeManagerObj getGridNodeManager()
    throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridNodeManagerHome.class.getName(),
      IGridNodeManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of a CompanyProfile based on the specified uID.
   *
   * @param uID The UID of the CompanyProfile.
   * @return The CompanyProfile retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the CoyProfileManagerBean.
   * @exception Exception Bad BE UID. No CompanyProfile exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static CompanyProfile verifyCompanyProfile(Long uID)
    throws Exception
  {
    try
    {
      return getCoyProfileManager().findCompanyProfile(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad CompanyProfile UID: "+uID);
    }
  }

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

  // ******************** Conversion Helpers ******************************

  /**
   * Convert an CompanyProfile to Map object.
   *
   * @param coyProfile The CompanyProfile to convert.
   * @return A Map object converted from the specified CompanyProfile.
   *
   * @since 2.0
   */
  public static Map convertCompanyProfileToMap(CompanyProfile coyProfile)
  {
    return CompanyProfile.convertToMap(
             coyProfile,
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an GnCategory to Map object.
   *
   * @param category The GnCategory to convert.
   * @return A Map object converted from the specified GnCategory.
   *
   * @since 2.0 I5
   */
  public static Map convertGnCategoryToMap(GnCategory category)
  {
    return GnCategory.convertToMap(
             category,
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an GridNode to Map object.
   *
   * @param gridnode The GridNode to convert.
   * @return A Map object converted from the specified GridNode.
   *
   * @since 2.0 I6
   */
  public static Map convertGridNodeToMap(GridNode gridnode)
  {
    return GridNode.convertToMap(
             gridnode,
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an ConnectionStatus to Map object.
   *
   * @param gridnode The ConnectionStatus to convert.
   * @return A Map object converted from the specified ConnectionStatus.
   *
   * @since 2.0 I6
   */
  public static Map convertConnStatusToMap(ConnectionStatus connStatus)
  {
    return ConnectionStatus.convertToMap(
             connStatus,
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of GnCategory(s) to Map objects.
   *
   * @param categoryList The collection of GnCategory(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of GnCategory(s).
   *
   * @since 2.0 I4
   */
  public static Collection convertGnCategoriesToMapObjects(Collection categoryList)
  {
    return GnCategory.convertEntitiesToMap(
             (GnCategory[])categoryList.toArray(new GnCategory[categoryList.size()]),
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of GridNode(s) to Map objects.
   *
   * @param gridnodeList The collection of GridNode(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of GridNode(s).
   *
   * @since 2.0 I4
   */
  public static Collection convertGridNodesToMapObjects(Collection gridnodeList)
  {
    return GridNode.convertEntitiesToMap(
             (GridNode[])gridnodeList.toArray(new GridNode[gridnodeList.size()]),
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of ConnectionStatus(s) to Map objects.
   *
   * @param gridnodeList The collection of ConnectionStatus(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ConnectionStatus(s).
   *
   * @since 2.0 I4
   */
  public static Collection convertConnStatusesToMapObjects(Collection conStatusList)
  {
    return ConnectionStatus.convertEntitiesToMap(
             (ConnectionStatus[])conStatusList.toArray(new ConnectionStatus[conStatusList.size()]),
             GridNodeEntityFieldID.getEntityFieldID(),
             null);
  }

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

  // ************************ Finder Helpers *******************************
  public static Collection findGnCategories(IDataFilter filter)
    throws Exception
  {
    ArrayList results = new ArrayList(getGridNodeManager().findGnCategoriesByFilter(filter));

    Collections.sort(results, CATEGORY_COMPARATOR);

    return results;
  }

  public static GnCategory findGnCategoryByCode(String catCode)
    throws Exception
  {
    GnCategory category = getGridNodeManager().findGnCategoryByCode(catCode);

    if (category == null)
      throw new ApplicationException("Bad GnCategory Code: "+catCode);

    return category;
  }

  public static GridNode findGridNodeByUID(Long uid)
    throws Exception
  {
    GridNode gridnode = getGridNodeManager().findGridNode(uid);

    if (gridnode == null)
      throw new ApplicationException("Bad GridNode UID: "+uid);

    return gridnode;
  }

  public static Collection findGridNodes(IDataFilter filter)
    throws Exception
  {
    return getGridNodeManager().findGridNodesByFilter(filter);
  }

  /**
   * Retrieve the ConnectionStatus for a particular GridNode.
   *
   * @param gridnodeID the GridNode ID of the GridNode.
   * @return ConnectionStatus for the GridNode.
   * @throws Exception No such GridNodeID.
   */
  public static ConnectionStatus findConnectionStatus(String gridnodeID)
    throws Exception
  {
    ConnectionStatus connStatus = null;
    try
    {
      connStatus = ServiceLookupHelper.getGridNodeManager().findConnectionStatusByNodeID(
                    gridnodeID);
    }
    catch (FindEntityException ex)
    {
      Logger.warn("[ActionHelper.findConnectionStatus] Error ", ex);
    }

    if (connStatus == null)
      throw new ApplicationException("Bad GridNode ID: "+gridnodeID);

    return connStatus;
  }

  /**
   * Retrieve the ConnectionStatus using UID.
   *
   * @param uid the UID of the ConnectionStatus.
   * @return ConnectionStatus retrieved based on specified uid.
   * @throws Exception No such UID.
   */
  public static ConnectionStatus findConnectionStatus(Long uid)
    throws Exception
  {
    ConnectionStatus connStatus = null;
    try
    {
      connStatus = ServiceLookupHelper.getGridNodeManager().findConnectionStatus(
                    uid);
    }
    catch (FindEntityException ex)
    {
      Logger.warn("[ActionHelper.findConnectionStatus] Error ", ex);
    }

    if (connStatus == null)
      throw new ApplicationException("Bad ConnectionStatus UID: "+uid);

    return connStatus;
  }


}
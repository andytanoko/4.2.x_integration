/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Neo Sok Lay         Created
 * Oct 28 2002    Neo Sok Lay         Add fieldIDs for GridNode.
 * Nov 05 2002    Neo Sok Lay         Add fieldIDs for ConnectionStatus.
 * Feb 06 2007    Chong SoonFui       Commented ICompanyProfile.CAN_DELETE
 */
package com.gridnode.gtas.model.gridnode;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the GridNode module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I5
 */
public class GridNodeEntityFieldID
{
  private Hashtable _table;
  private static GridNodeEntityFieldID _self = null;

  private GridNodeEntityFieldID()
  {
    _table = new Hashtable();

    //CompanyProfile
    _table.put(ICompanyProfile.ENTITY_NAME,
      new Number[]
      {
        ICompanyProfile.ADDRESS,
        ICompanyProfile.ALT_EMAIL,
        ICompanyProfile.ALT_TEL,
//        ICompanyProfile.CAN_DELETE,
        ICompanyProfile.CITY,
        ICompanyProfile.COUNTRY,
        ICompanyProfile.COY_NAME,
        ICompanyProfile.EMAIL,
        ICompanyProfile.FAX,
        ICompanyProfile.IS_PARTNER,
        ICompanyProfile.LANGUAGE,
        ICompanyProfile.STATE,
        ICompanyProfile.TEL,
        ICompanyProfile.UID,
        ICompanyProfile.ZIP_CODE,
      });

    //GnCategory
    _table.put(IGnCategory.ENTITY_NAME,
      new Number[]
      {
        IGnCategory.CATEGORY_CODE,
        IGnCategory.CATEGORY_NAME,
      });

    //GridNode
    _table.put(IGridNode.ENTITY_NAME,
      new Number[]
      {
        IGridNode.ACTIVATION_REASON,
        IGridNode.CATEGORY,
        IGridNode.COY_PROFILE_UID,
        IGridNode.DT_ACTIVATED,
        IGridNode.DT_CREATED,
        IGridNode.DT_DEACTIVATED,
        IGridNode.DT_REQ_ACTIVATE,
        IGridNode.ID,
        IGridNode.NAME,
        IGridNode.STATE,
        IGridNode.UID,
      });

    //ConnectionStatus
    _table.put(IConnectionStatus.ENTITY_NAME,
      new Number[]
      {
        IConnectionStatus.CONNECTED_SERVER_NODE,
        IConnectionStatus.DT_LAST_OFFLINE,
        IConnectionStatus.DT_LAST_ONLINE,
        IConnectionStatus.GRIDNODE_ID,
        IConnectionStatus.STATUS_FLAG,
        IConnectionStatus.UID,
      });


  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new GridNodeEntityFieldID();
    }
    return _self._table;
  }
}
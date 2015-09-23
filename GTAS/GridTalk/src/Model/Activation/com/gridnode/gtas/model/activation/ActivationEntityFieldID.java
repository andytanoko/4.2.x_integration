/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created.
 */
package com.gridnode.gtas.model.activation;

import com.gridnode.gtas.model.bizreg.BizRegEntityFieldID;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Activation module for
 * Activation functions.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationEntityFieldID
{
  private Hashtable _table;
  private static ActivationEntityFieldID _self = null;

  private ActivationEntityFieldID()
  {
    _table = new Hashtable();

    //ActivationRecord
    _table.put(IActivationRecord.ENTITY_NAME,
      new Number[]
      {
        IActivationRecord.ACTIVATION_DETAILS,
        IActivationRecord.ACT_DIRECTION,
        IActivationRecord.CURRENT_TYPE,
        IActivationRecord.DEACT_DIRECTION,
        IActivationRecord.DT_ABORTED,
        IActivationRecord.DT_APPROVED,
        IActivationRecord.DT_DEACTIVATED,
        IActivationRecord.DT_DENIED,
        IActivationRecord.DT_REQUESTED,
        IActivationRecord.GRIDNODE_ID,
        IActivationRecord.GRIDNODE_NAME,
        IActivationRecord.IS_LATEST,
        IActivationRecord.TRANS_COMPLETED,
        IActivationRecord.UID,
      });

    //GridNodeActivation
    _table.put(IGridNodeActivation.ENTITY_NAME,
      new Number[]
      {
        IGridNodeActivation.ACTIVATE_REASON,
        IGridNodeActivation.APPROVER_BE_LIST,
        IGridNodeActivation.REQUESTOR_BE_LIST,
      });

    //BusinessEntity
    _table.putAll(BizRegEntityFieldID.getEntityFieldID());
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ActivationEntityFieldID();
    }
    return _self._table;
  }
}
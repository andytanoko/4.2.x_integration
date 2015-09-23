/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented IPartnerFunction.CAN_DELETE,IWorkflowActivity.CAN_DELETE
 */
package com.gridnode.gtas.model.partnerfunction;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the PartnerFunction entity.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerFunctionEntityFieldID
{
  private Hashtable _table;
  private static PartnerFunctionEntityFieldID _self = null;

  private PartnerFunctionEntityFieldID()
  {
    _table = new Hashtable();

    // PartnerFunction
    _table.put(IPartnerFunction.ENTITY_NAME,
      new Number[]
      {
//        IPartnerFunction.CAN_DELETE,
        IPartnerFunction.DESCRIPTION,
        IPartnerFunction.PARTNER_FUNCTION_ID,
        IPartnerFunction.TRIGGER_ON,
        IPartnerFunction.UID,
        IPartnerFunction.WORKFLOW_ACTIVITIES
      });

    // WorkflowActivity
    _table.put(IWorkflowActivity.ENTITY_NAME,
      new Number[]
      {
        IWorkflowActivity.ACTIVITY_TYPE,
//        IWorkflowActivity.CAN_DELETE,
        IWorkflowActivity.DESCRIPTION,
        IWorkflowActivity.PARAM_LIST,
        IWorkflowActivity.UID
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PartnerFunctionEntityFieldID();
    }
    return _self._table;
  }
}
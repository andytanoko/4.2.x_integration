/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBusinessEntityListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * 2008-07-17	  Teh Yu Phei		  Change retrieveEntityList() by adding if statement
 * Aug 09 2008    Tam Wei Xiang       #31 Fix filter comparison.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityListEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import java.util.BitSet;

/**
 * This Action class handles the retrieving of a list of BusinessEntities.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBusinessEntityListAction
  extends    AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4984128810216266420L;
	public static final String CURSOR_PREFIX = "BizEntityListCursor.";
  public static final String ACTION_NAME = "GetBusinessEntityListAction";

  private static final BitSet _stateSet = new BitSet();
  static
  {
    _stateSet.set((int)BusinessEntity.STATE_DELETED);
  }

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
	  //exclude those marked deleted
	  if( filter != null && (filter.getValueFilter()) != null && ((filter.getValueFilter().getFilterField()).equals(IBusinessEntity.STATE)) ) //09082008 TWX fix filter comparison.
	  {
		  return ActionHelper.getBizRegManager().findBusinessEntities(filter);		  
	  }
	  else
	  {
		  return ActionHelper.getBizRegManager().findBusinessEntities(filter, _stateSet);
	  }
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertBEsToMapObjects(entityList);
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  // ***************** AbstractGridTalkAction methods ***********************

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetBusinessEntityListEvent.class;
  }

}

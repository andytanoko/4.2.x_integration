/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteJmsDestinationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.events.alert.DeleteJmsDestinationEvent;
import com.gridnode.gtas.events.bizreg.DeleteBusinessEntityEvent;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.ArrayList;
import java.util.Collection;

public class DeleteJmsDestinationAction extends AbstractDeleteEntityAction
{ 
	public static final String ACTION_NAME = "DeleteJmsDestinationAction";

//  private Collection _beListToDelete;

  // **************** AbstractDeleteEntityAction methods *******************

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
  	return true;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
  	ServiceLookupHelper.getAlertManager().deleteJmsDestination((Long) entity.getFieldValue(keyId));
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().getJmsDestinationKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().getJmsDestinations(filter);
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return DeleteJmsDestinationEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}
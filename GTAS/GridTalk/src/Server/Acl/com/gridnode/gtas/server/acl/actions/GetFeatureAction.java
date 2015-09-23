/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFeatureAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 19 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Map;

import com.gridnode.gtas.events.acl.GetFeatureEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class GetFeatureAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4507440428820749402L;
	public static final String ACTION_NAME = "GetFeatureAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertFeatureToMap((Feature)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetFeatureEvent getEvent = (GetFeatureEvent)event;

    if (getEvent.getFeatureUID() == null)
      return ActionHelper.getACLManager().getFeatureByFeatureName(
               getEvent.getFeatureName());
    else
      return ActionHelper.getACLManager().getFeatureByFeatureUId(
               getEvent.getFeatureUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetFeatureEvent getEvent = (GetFeatureEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             Feature.ENTITY_NAME,
             String.valueOf(getEvent.getFeatureUID()),
             String.valueOf(getEvent.getFeatureName()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetFeatureEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}
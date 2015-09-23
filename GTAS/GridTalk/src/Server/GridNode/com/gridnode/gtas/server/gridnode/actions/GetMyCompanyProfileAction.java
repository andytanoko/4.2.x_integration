/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMyCompanyProfileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 * Sep 11 2002    Neo Sok Lay         Moved getEntity() logic to the
 *                                    GridNodeManagerBean.
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.server.gridnode.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.events.gridnode.GetMyCompanyProfileEvent;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one MyCompanyProfile.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetMyCompanyProfileAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7618133494070545662L;
	public static final String ACTION_NAME = "GetMyCompanyProfileAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertCompanyProfileToMap((CompanyProfile)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetMyCompanyProfileEvent getEvent = (GetMyCompanyProfileEvent)event;

    return ServiceLookupHelper.getGridNodeManager().getMyCompanyProfile();
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetMyCompanyProfileEvent getEvent = (GetMyCompanyProfileEvent)event;
    return new Object[]
           {
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetMyCompanyProfileEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}
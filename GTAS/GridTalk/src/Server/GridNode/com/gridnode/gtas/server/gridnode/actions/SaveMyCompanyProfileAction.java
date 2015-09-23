/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveMyCompanyProfileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Sep 11 2002    Neo Sok Lay         Moved saving logic to GridNodeManagerBean.
 */
package com.gridnode.gtas.server.gridnode.actions;

import java.util.Map;

import com.gridnode.gtas.events.gridnode.SaveMyCompanyProfileEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.server.gridnode.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the update of this Gtas's CompanyProfile.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class SaveMyCompanyProfileAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 934489646943941918L;
	public static final String ACTION_NAME = "SaveMyCompanyProfileAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return SaveMyCompanyProfileEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                       {
                         "My"+CompanyProfile.ENTITY_NAME,
                       };

    return constructEventResponse(
             IErrorCode.SAVE_COY_PROFILE_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SaveMyCompanyProfileEvent updEvent = (SaveMyCompanyProfileEvent)event;

    ServiceLookupHelper.getGridNodeManager().saveMyCompanyProfile(
        updEvent.getUpdProfile());

    CompanyProfile prof = ServiceLookupHelper.getGridNodeManager().getMyCompanyProfile();

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params, convertToMap(prof));
  }

  // **************************** Own Methods *****************************

  private Map convertToMap(CompanyProfile profile)
  {
    return ActionHelper.convertCompanyProfileToMap(profile);
  }

}
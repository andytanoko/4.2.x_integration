/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated meth
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if noP2P is on.
 * 2008-07-17	  Teh Yu Phei 		  Add getIsStateFieldId() (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.IGTChannelInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class ChannelInfoListAction extends AbstractProfileListAction
{
  private static final Object[] _columns = 
  { //20030708AH
    IGTChannelInfoEntity.NAME,
    IGTChannelInfoEntity.DESCRIPTION,
    IGTChannelInfoEntity.IS_PARTNER,
    IGTChannelInfoEntity.PARTNER_CAT,
    IGTChannelInfoEntity.TPT_PROTOCOL_TYPE,
    IGTChannelInfoEntity.TPT_COMM_INFO_UID,
    IGTChannelInfoEntity.REF_ID,
  };

  private static final Object[] _noP2Pcolumns = 
  { //20030708AH
    IGTChannelInfoEntity.NAME,
    IGTChannelInfoEntity.DESCRIPTION,
    IGTChannelInfoEntity.IS_PARTNER,
    IGTChannelInfoEntity.TPT_PROTOCOL_TYPE,
    IGTChannelInfoEntity.TPT_COMM_INFO_UID,
    IGTChannelInfoEntity.REF_ID,
  };

  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
  	//NSL20060425 check for P2P
  	if (isNoP2P(actionContext))
  	{
  		return _noP2Pcolumns;
  	}
  	//20030708AH
    return _columns;
  }

  protected Number getIsPartnerFieldId()
  {
    return IGTChannelInfoEntity.IS_PARTNER;
  }

  protected Number getPartnerCategoryFieldId()
  {
    return IGTChannelInfoEntity.PARTNER_CAT;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_CHANNEL_INFO;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_CHANNEL_INFO; //20030708AH
  }

@Override
protected Number getIsStateFieldId() {
	// TODO Auto-generated method stub
	return null;
}
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntityListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-07     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated methods
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if NoP2P is on.
 * 2008-04-17	  Teh Yu Phei		  Add getIsStateFieldId() (Ticket 31)
 * 									  Change if statement in processPipeline (Ticket 31)
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.channel.AbstractProfileListAction;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class BusinessEntityListAction extends AbstractProfileListAction
{
  private static final Object[] _columns =
  { //20030708AH
    IGTBusinessEntityEntity.ID,
    IGTBusinessEntityEntity.ENTERPRISE_ID,
    IGTBusinessEntityEntity.DESCRIPTION,
    IGTBusinessEntityEntity.STATE,
    IGTBusinessEntityEntity.IS_PARTNER,
    IGTBusinessEntityEntity.PARTNER_CAT,
  };

  private static final Object[] _noP2Pcolumns =
  { 
    IGTBusinessEntityEntity.ID,
    IGTBusinessEntityEntity.ENTERPRISE_ID,
    IGTBusinessEntityEntity.DESCRIPTION,
    IGTBusinessEntityEntity.STATE,
    IGTBusinessEntityEntity.IS_PARTNER,
  };

  protected Number getIsPartnerFieldId()
  {
    return IGTBusinessEntityEntity.IS_PARTNER;
  }

  protected Number getIsStateFieldId()
  {
    return IGTBusinessEntityEntity.STATE;
  }
  
  protected Number getPartnerCategoryFieldId()
  {
    return IGTBusinessEntityEntity.PARTNER_CAT;
  }
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
  	//NSL20060425 check for NoP2P
  	if (isNoP2P(actionContext))
  	{
  		return _noP2Pcolumns;
  	}
  	//20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_BUSINESS_ENTITY;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_BUSINESS_ENTITY; //20030708AH
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    ActionForward invokeSendForward = actionContext.getMapping().findForward("invokeSend");
    ActionForward invokePublishForward = actionContext.getMapping().findForward("invokePublish");
    if(invokeSendForward == null) throw new NullPointerException("invokeSendForward is null"); //20030416AH
    if(invokePublishForward == null) throw new NullPointerException("invokePublishForward is null");
    if(Boolean.FALSE.equals(getIsPartner(actionContext)) || (getIsState(actionContext) != null && getIsState(actionContext).equals("1")))
    {
      rPipe.addRenderer(new BusinessEntityListDecoratorRenderer(rContext, invokeSendForward.getPath(), invokePublishForward.getPath()));
    }
    super.processPipeline(actionContext, rContext, rPipe);
  }

}
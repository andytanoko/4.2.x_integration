/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractProfileListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-09     Andrew Hill         Created
 * 2002-12-11     Andrew Hill         Autorefresh support (appendRefreshParameters)
 * 2003-03-21     Andrew Hill         Paging Support
 * 2006-04-25     Neo Sok Lay         Support NoP2P mode
 * 2008-07-17	  Teh Yu Phei		  Change getListPager by adding else if( (isState != null) && (isState.equals("1")) ) (Ticket 31)
 * 									  Add protected String getIsState(ActionContext actionContext) (Ticket 31)
 * 									  Change processPipeline by adding if condition (Ticket 31)
 * 									  Change appendRefreshParameters (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public abstract class AbstractProfileListAction extends EntityListAction
{
	protected boolean isNoP2P(ActionContext actionContext)
	{
		IGTSession gtasSession = getGridTalkSession(actionContext);
		return gtasSession.isNoP2P();
	}
	
  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTManager manager = getManager(actionContext);
    Boolean isPartner = getIsPartner(actionContext);
    String isState = getIsState(actionContext);
    Short partnerCategory = getPartnerCategory(actionContext);
    IGTListPager pager = null;
        
    if( (isPartner == null) && (partnerCategory == null) && (isState == null))
    {
      pager = manager.getListPager();
    }
    else if( (isState != null) && (isState.equals("1")) )
    {
    	pager = manager.getListPager(isState, getIsStateFieldId());
    }
    else if( isState == null )
    {
    	//NSL20060425 Check for P2P option
    	if (isNoP2P(actionContext) && (isPartner != null))
    	{
    		//if noP2P, do not differentiate GT and Non-GT partner
    		pager = manager.getListPager(isPartner,getIsPartnerFieldId());
    	}
    	else
    	{
    		if( (isPartner != null) && (partnerCategory == null) )
    		{
    			pager = manager.getListPager(isPartner,getIsPartnerFieldId());
    		}
    		else if( (isPartner == null) && (partnerCategory != null) )
    		{
    			pager = manager.getListPager(partnerCategory,getPartnerCategoryFieldId());
    		}
    		else
    		{
    			throw new java.lang.IllegalStateException("View filtering based on both isPartner & partnerCategory having non null values is not currently supported");
    		}
    	}
    }
    if(pager == null)
    {
      throw new NullPointerException("Internal assertion failure - pager == null");
    }
    return pager;
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Boolean isPartner = getIsPartner(actionContext);
    Short partnerCategory = getPartnerCategory(actionContext);
    Collection retList = null;

    if( (isPartner == null) && (partnerCategory == null) )
    {
      retList = manager.getAll();
    }
    else
    {
      if( (isPartner != null) && (partnerCategory == null) )
      {
        retList = manager.getByKey(isPartner,getIsPartnerFieldId());
      }
      else if( (isPartner == null) && (partnerCategory != null) )
      {
        retList = manager.getByKey(partnerCategory,getPartnerCategoryFieldId());
      }
      else
      {
        throw new java.lang.IllegalStateException("View filtering based on both isPartner & partnerCategory having non null values is not currently supported");
      }
    }

    return retList;
  }*/

  protected abstract Number getIsPartnerFieldId();
  protected abstract Number getPartnerCategoryFieldId();
  protected abstract Number getIsStateFieldId();

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    Boolean isPartner = getIsPartner(actionContext);
    String isState = getIsState(actionContext);
    
    if(isPartner != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,"isPartner",isPartner.toString());
    }

    Short partnerCategory = getPartnerCategory(actionContext);
    if(partnerCategory != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,"partnerCategory",partnerCategory.toString());
    }

    if(isState != null && isState.equals("1"))
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,"isState",isState);
    }
    
    return refreshUrl;
  }

  protected Boolean getIsPartner(ActionContext actionContext)
  {
    String isPartnerStr = actionContext.getRequest().getParameter("isPartner");
    if(isPartnerStr == null)
    {
      return null;
    }
    else
    {
      return StaticUtils.booleanValue(isPartnerStr);
    }
  }

  protected String getIsState(ActionContext actionContext)
  {
	  String isStateStr = actionContext.getRequest().getParameter("isState");
	  if(isStateStr == null)
	  {
		  return null;
	  }
	  else
	  {
		  return isStateStr;
	  }	  
  }
  
  protected Short getPartnerCategory(ActionContext actionContext)
  {
    String partnerCategoryStr = actionContext.getRequest().getParameter("partnerCategory");
    if(partnerCategoryStr == null)
    {
      return null;
    }
    else
    {
      return StaticUtils.shortValue(partnerCategoryStr);
    }
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {	  
    if( (getIsState(actionContext)) != null && (getIsState(actionContext)).equals("1") )
    {
    	rPipe.addRenderer(new GenericProfileListDecoratorRenderer(
                rContext,
                getIsState(actionContext),
                getResourcePrefix(actionContext))
                );
    }
    else
    {
	  rPipe.addRenderer(new GenericProfileListDecoratorRenderer(
                      rContext,
                      getIsPartner(actionContext),
                      getPartnerCategory(actionContext),
                      getResourcePrefix(actionContext))
                      );
    }
  }
}
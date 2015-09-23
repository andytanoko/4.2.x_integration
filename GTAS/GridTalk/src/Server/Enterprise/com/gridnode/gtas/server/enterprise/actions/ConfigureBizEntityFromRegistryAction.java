/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigureBizEntityFromRegistryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 8 2003     Neo Sok Lay         Created
 * Sep 23 2003    Neo Sok Lay         RegistryInfoConvertor.toSearchedBusinessEntity()
 *                                    is no longer statically accessible.
 * Oct 17 2005    Neo Sok Lay         Change IBizRegistryManager to IBizRegistryManagerObj                                   
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.events.enterprise.ConfigureBizEntityFromRegistryEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.helpers.*;
import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This action handles the ConfigureBizEntityFromRegistryEvent.<p>
 * Configuration is only application to searched Business entity(s) not
 * belonging to this GridTalk's GridNode.<p>
 * The event response may be one of following form:<p>
 * <ol>
 *   <li>Successful :- MsgCode=NO_ERROR</li>
 *   <li>Unexpected Error :- MsgCode=CONFIGURE_BE_ERROR and IsException=true and ErrorTrace is non-empty.
 *       e.g. Invalid SearchId</li>
 *   <li>Synchronization fail :- MsgCode=CONFIGURE_BE_ERROR and IsException=false and 
 *       ReturnData=List of UUIDs that are not successful</li> 
 * </ol>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ConfigureBizEntityFromRegistryAction
  extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -30503091750677213L;
	private static final String ACTION_NAME = "ConfigureBizEntityFromRegistryAction";

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    ConfigureBizEntityFromRegistryEvent confEvent = (ConfigureBizEntityFromRegistryEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        confEvent.getSearchId(),
                        confEvent.getUuids(),
                      };

    return constructEventResponse(
             IErrorCode.CONFIGURE_BE_ERROR,
             params,
             ex);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(com.gridnode.pdip.framework.rpf.event.IEvent)
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ConfigureBizEntityFromRegistryEvent confEvent = (ConfigureBizEntityFromRegistryEvent)event;
    
    IBizRegistryManagerObj mgr = ServiceLookupHelper.getBizRegManager();

    SearchRegistryQuery query = mgr.retrieveSearchQuery(confEvent.getSearchId());
    String myNodeId = ActionHelper.getMyGridNodeId();
    Collection selectedKeys = confEvent.getUuids();
     
    RegistryInfoConvertor convertor = new RegistryInfoConvertor(
                                        PublishDelegate.getTechnicalSpecifications());
    SyncRegistryDelegate syncDelegate = new SyncRegistryDelegate(query.getSearchCriteria().getQueryUrl());
    
    ArrayList problemList = new ArrayList();
    OrganizationInfo orgInfo;
    SearchedBusinessEntity searchedBe;
    boolean myBe;
    for (Iterator i=query.getRawSearchResults().iterator(); i.hasNext(); )
    {
      orgInfo = (OrganizationInfo)i.next();
      if (selectedKeys.contains(orgInfo.getKey()))
      {
        searchedBe = convertor.toSearchedBusinessEntity(orgInfo);
        myBe = myNodeId.equals(searchedBe.getEnterpriseId());
        
        if (!myBe) // don't process own business entities
        {
          try
          {
            syncDelegate.synchronize(searchedBe, orgInfo);
          }
          catch (SynchronizationFailException e)
          {
            problemList.add(orgInfo.getKey());
            Logger.warn("[ConfigureBizEntityFromRegistryAction.doProcess] Error synchronizing ", e);
          }
        }
      }
    }

    if (!problemList.isEmpty())
    {
      Object[] params = new Object[]
                        {
                          confEvent.getSearchId(),
                          confEvent.getUuids(),
                        };
      return constructEventResponse(IErrorCode.CONFIGURE_BE_ERROR, params, problemList);
    }
          
    return constructEventResponse();
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   */
  protected Class getExpectedEventClass()
  {
    return ConfigureBizEntityFromRegistryEvent.class;
  }

  // *********************** Own Methods ********************
  
   
}

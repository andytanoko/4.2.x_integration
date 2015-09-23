/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Retrieve RegistryConnectInfo from BizRegistryManager.
 * Oct 06 2003    Neo Sok Lay         Publish ServiceInfo together with OrganizationInfo
 *                                    -- otherwise, publishing orgInfo second time will 
 *                                    -- remove the service automatically by the registry.
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.PublishBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.bizreg.model.TechnicalSpecs;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.RegistryInfoConvertor;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo;
import com.gridnode.pdip.app.bizreg.pub.model.ServiceInfo;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Collection;
import java.util.Iterator;

/**
 * This action handles the PublishBusinessEntityEvent to publish
 * one or more BusinessEntity(s) to a specified target registry.
 * The Channel(s) associated with each BusinessEntity are also published,
 * based on the applicable messaging standards.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class PublishBusinessEntityAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3550255033101795296L;
	public static final String ACTION_NAME = "PublishBusinessEntityAction";
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(IEvent, TypedException)
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    PublishBusinessEntityEvent publishEvent = (PublishBusinessEntityEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        publishEvent.getBeUIdS(),
                        publishEvent.getConnInfoName(),
                      };

    return constructEventResponse(
             IErrorCode.REGISTRY_PUBLISH_ERROR,
             params,
             ex);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(IEvent)
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    PublishBusinessEntityEvent publishEvent = (PublishBusinessEntityEvent)event;

    RegistryConnectInfo regConnInfo = ServiceLookupHelper.getBizRegManager().findRegistryConnectInfoByName(
                                        publishEvent.getConnInfoName());
    if (regConnInfo == null)
      throw new FindEntityException("Invalid Registry Connect Info: "+publishEvent.getConnInfoName());
    if (regConnInfo.getPublishUrl() == null || regConnInfo.getPublishUrl().trim().length() == 0)
      throw new FindEntityException("The specified RegistryConnectInfo does not contain a Publish URL!");
    if (regConnInfo.getPublishUser() == null || regConnInfo.getPublishUser().trim().length() == 0)
      throw new FindEntityException("The specified RegistryConnectInfo does not contain a Publish User!");
    if (regConnInfo.getPublishPassword() == null || regConnInfo.getPublishPassword().trim().length() == 0)
      throw new FindEntityException("The specified RegistryConnectInfo does not contain a Publish Password!");
        
    String[] connInfo = RegistryInfoConvertor.toConnectionInfo(regConnInfo);
                          
    IPublicRegistryManager mgr = PublishDelegate.getPublicRegistryMgr();
    
    try
    {
      mgr.connectToRegistry(connInfo); 
    
      TechnicalSpecs techspecs = PublishDelegate.getTechnicalSpecifications();
      techspecs = PublishDelegate.publishTechnicalSpecs(techspecs, connInfo);
    
      Collection bes = ActionHelper.getBusinessEntities(publishEvent.getBeUIdS());
      RegistryInfoConvertor convertor = new RegistryInfoConvertor(techspecs);

      OrganizationInfo orgInfo;
      ServiceInfo serviceInfo;
      BusinessEntity be;
      Collection channels;
      for (Iterator i=bes.iterator(); i.hasNext(); )
      {
        be = (BusinessEntity)i.next();
        orgInfo = convertor.toOrganizationInfo(be);
        //031006NSL
        //orgInfo = mgr.publish(orgInfo, connInfo);
      
        channels = ActionHelper.getChannelsAttachedToBE((Long)be.getKey());
      
        serviceInfo = convertor.toServiceInfo(
                        be.getBusEntId(),
                        orgInfo.getKey(), 
                        channels);
        //031006NSL                
        //mgr.publish(serviceInfo, connInfo);                
        
        orgInfo.getServices().add(serviceInfo);
        mgr.publish(orgInfo, connInfo);
      }
    }
    finally
    {
      mgr.disconnectFromRegistry(connInfo);
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
    return PublishBusinessEntityEvent.class;
  }

}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJMSCommInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh           Created
 * Nov 03 2003    Guo Jianyu          Modified prepareUpdData() to include
 *                                    packagingInfoExtension
 * Dec 22 2003    Jagadeesh           Modified : Commented isZip and ZipThreshold,
 *                                               is now available with FlowControl Profile.
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction                                              
 */


package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.UpdatePackagingInfoEvent;
import com.gridnode.gtas.model.channel.AS2PkgInfoExtensionEntityFieldId;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.model.channel.PackagingInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfoExtension;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class UpdatePackagingInfoAction extends AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7545493296348858288L;

	private static final String ACTION_NAME = "UpdatePackagingInfoAction";

  private PackagingInfo _packagingInfo;

  protected Map convertToMap(AbstractEntity entity)
	{
		return PackagingInfo.convertToMap(entity, PackagingInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdatePackagingInfoEvent updEvent = (UpdatePackagingInfoEvent)event;
		return new Object[] {PackagingInfo.ENTITY_NAME, updEvent.getUId()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdatePackagingInfoEvent updEvent = (UpdatePackagingInfoEvent)event;
    _packagingInfo.setName(updEvent.getName());
    _packagingInfo.setDescription(updEvent.getDescription());
    _packagingInfo.setEnvelope(updEvent.getEnvelope());

    if(IPackagingInfo.AS2_ENVELOPE_TYPE.equalsIgnoreCase(updEvent.getEnvelope()))
    {
       AS2PackagingInfoExtension pkgInfoExtension = (AS2PackagingInfoExtension)
        PackagingInfoExtension.convertMapToEntity(
        AS2PkgInfoExtensionEntityFieldId.getEntityFieldID(),updEvent.getPkgInfoExtension());
       _packagingInfo.setPkgInfoExtension(pkgInfoExtension);
    }
    else
    {
      _packagingInfo.setPkgInfoExtension(null);
    }
    return _packagingInfo;

	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getPackagingInfo(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updatePackagingInfo((PackagingInfo)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdatePackagingInfoEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdatePackagingInfoEvent updEvent = (UpdatePackagingInfoEvent)event;
    _packagingInfo = verifyValidPackagingInfo(updEvent);
  }

  private PackagingInfo verifyValidPackagingInfo(UpdatePackagingInfoEvent updEvent) throws Exception
  {
    try
    {
      PackagingInfo packagingInfo = (PackagingInfo) getManager().getPackagingInfo(updEvent.getUId());
      return packagingInfo;
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid PackagingInfo specified!");
    }
  }

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]
           );
  }
}




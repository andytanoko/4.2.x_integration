/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateChannelInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? ????    ????                     ???
 * Dec 22 2003    Jagadeesh                Modified : Commented isZip and ZipThreshold, as
 *                                                    these attributes are part of FlowControl.
 * Sep 01 2005    Neo Sok Lay              Change to extend from AbstractCreateEntityAction                                                   
 */

package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.CreatePackagingInfoEvent;
import com.gridnode.gtas.model.channel.AS2PkgInfoExtensionEntityFieldId;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.model.channel.PackagingInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfoExtension;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

 
public class CreatePackagingInfoAction extends AbstractCreateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -962424761844659157L;
	public static final String ACTION_NAME = "CreatePackagingInfoAction";

  public CreatePackagingInfoAction()
  {
  }

  protected Map convertToMap(AbstractEntity entity)
	{
		return PackagingInfo.convertToMap(entity,PackagingInfoEntityFieldId.getEntityFieldID(),null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createPackagingInfo((PackagingInfo)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {PackagingInfo.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreatePackagingInfoEvent _event = (CreatePackagingInfoEvent)event;
    PackagingInfo packagingInfo = new PackagingInfo();
    packagingInfo.setName(_event.getName());
    packagingInfo.setDescription(_event.getDescription());
    packagingInfo.setEnvelope(_event.getEnvelope());
    packagingInfo.setIsPartner((_event.isPartner()).booleanValue());
    if (_event.isPartner().booleanValue())
      // can only create for partner of "Other" category.
      packagingInfo.setPartnerCategory(IPackagingInfo.CATEGORY_OTHERS);
    else
      packagingInfo.setReferenceId(getEnterpriseID());

    if (IPackagingInfo.AS2_ENVELOPE_TYPE.equalsIgnoreCase(_event.getEnvelope()))
    {
      AS2PackagingInfoExtension pkgInfoExtension =
        (AS2PackagingInfoExtension)PackagingInfoExtension.convertMapToEntity(
        AS2PkgInfoExtensionEntityFieldId.getEntityFieldID(),_event.getPkgInfoExtension());
      packagingInfo.setPkgInfoExtension(pkgInfoExtension);
    }
    else
    {
      packagingInfo.setPkgInfoExtension(null);
    }

    return packagingInfo;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getPackagingInfo(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreatePackagingInfoEvent.class;
	}

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    //_event = (CreatePackagingInfoEvent) event;
//    if (_event.getDestType() != CreateJMSCommInfoEvent.QUEUE &&
//       _event.getDestType() != CreateJMSCommInfoEvent.TOPIC)
//      throw new EventException("Destination type not defined: " + _event.getDestType());
  }

  private IChannelManagerObj getManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IChannelManagerHome.class.getName(),
               IChannelManagerHome.class,
               new Object[0]);
  }


}



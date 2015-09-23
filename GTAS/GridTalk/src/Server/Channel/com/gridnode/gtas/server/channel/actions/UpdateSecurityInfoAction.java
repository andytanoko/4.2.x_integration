/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateSecurityInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh           Created
 * Nov 23 2003    Guo Jianyu          Added support for compression
 * Nov 26 2003    Guo Jianyu          Added encryptionALgorithm
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */


package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.UpdateSecurityInfoEvent;
import com.gridnode.gtas.model.channel.SecurityInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class UpdateSecurityInfoAction extends AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7664627011611192129L;

	private static final String ACTION_NAME = "UpdateSecurityInfoAction";

  private SecurityInfo _securityinfo;

  protected Map convertToMap(AbstractEntity entity)
	{
		return SecurityInfo.convertToMap(entity, SecurityInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateSecurityInfoEvent updEvent = (UpdateSecurityInfoEvent)event;
		return new Object[] {SecurityInfo.ENTITY_NAME, updEvent.getUId()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateSecurityInfoEvent updEvent = (UpdateSecurityInfoEvent)event;
    _securityinfo.setName(updEvent.getName());
    _securityinfo.setDescription(updEvent.getDescription());
    _securityinfo.setDigestAlgorithm(updEvent.getDigestAlgorithm());
    _securityinfo.setEncryptionCertificateID(updEvent.getEncyptionCertificateID());
    _securityinfo.setEncryptionLevel(updEvent.getEncryptionLevel());
    _securityinfo.setEncryptionType(updEvent.getEncryptionType());
    _securityinfo.setSignatureType(updEvent.getSignatureType());
    _securityinfo.setSignatureEncryptionCertificateID(updEvent.getSignatureEncyptCertificateID());
    _securityinfo.setCompressionType(updEvent.getCompressionType());
    _securityinfo.setCompressionMethod(updEvent.getCompressionMethod());
    if (updEvent.getCompressionLevel() != null)
      _securityinfo.setCompressionLevel((updEvent.getCompressionLevel()).intValue());
    _securityinfo.setSequence(updEvent.getSequence());
    _securityinfo.setEncryptionAlgorithm(updEvent.getEncryptionAlgorithm());
    return _securityinfo;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getSecurityInfo(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		 getManager().updateSecurityInfo((SecurityInfo)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateSecurityInfoEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateSecurityInfoEvent updEvent = (UpdateSecurityInfoEvent)event;
    _securityinfo = verifyValidSecurityInfo(updEvent);
  }


  private SecurityInfo verifyValidSecurityInfo(UpdateSecurityInfoEvent updEvent) throws Exception
  {
    try
    {
      SecurityInfo securityInfo = (SecurityInfo) getManager().getSecurityInfo(updEvent.getUId());
      return securityInfo;
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid SecurityInfo specified!");
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






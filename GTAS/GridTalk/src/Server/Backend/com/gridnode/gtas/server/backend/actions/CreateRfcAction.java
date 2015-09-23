/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRfcAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.actions;

import java.util.Map;

import com.gridnode.gtas.events.backend.CreateRfcEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new Rfc.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateRfcAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3590164722090094504L;
	public static final String ACTION_NAME = "CreateRfcAction";

  protected Class getExpectedEventClass()
  {
    return CreateRfcEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findRfc(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateRfcEvent createEvent = (CreateRfcEvent)event;

    Rfc newRfc = new Rfc();
    newRfc.setName(createEvent.getName());
    newRfc.setDescription(createEvent.getDescription());
    newRfc.setHost(createEvent.getHost());
    newRfc.setPortNumber(createEvent.getPortNumber());
    newRfc.setConnectionType(createEvent.getConnectionType());

    Boolean useCommandFile = createEvent.getUseCommandFile();
    newRfc.setUseCommandFile(useCommandFile);
    if (useCommandFile.booleanValue())
    {
      newRfc.setCommandFile(createEvent.getCommandFile());
      newRfc.setCommandFileDir(createEvent.getCommandFileDir());
      newRfc.setCommandLine(createEvent.getCommandLine());
    }
    else
    {
      newRfc.setCommandFile(null);
      newRfc.setCommandFileDir(null);
      newRfc.setCommandLine(null);
    }

    return newRfc;
  }


  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateRfcEvent createEvent = (CreateRfcEvent)event;
    return new Object[]
           {
             Rfc.ENTITY_NAME,
             createEvent.getName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createRfc((Rfc)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRfcToMap((Rfc)entity);
  }

}
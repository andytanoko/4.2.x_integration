/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRfcAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.actions;

import java.util.Map;

import com.gridnode.gtas.events.backend.UpdateRfcEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a Rfc.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateRfcAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4585836080183574218L;

	private Rfc _rfc;

  public static final String ACTION_NAME = "UpdateRfcAction";

  protected Class getExpectedEventClass()
  {
    return UpdateRfcEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRfcToMap((Rfc)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateRfcEvent updEvent = (UpdateRfcEvent)event;
    _rfc = ActionHelper.getManager().findRfc(updEvent.getRfcUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateRfcEvent updEvent = (UpdateRfcEvent)event;

    _rfc.setDescription(updEvent.getDescription());
    _rfc.setHost(updEvent.getHost());
    _rfc.setPortNumber(updEvent.getPortNumber());
    _rfc.setConnectionType(updEvent.getConnectionType());

    Boolean useCommandFile = updEvent.getUseCommandFile();
    _rfc.setUseCommandFile(useCommandFile);
    if (useCommandFile.booleanValue())
    {
      _rfc.setCommandFile(updEvent.getCommandFile());
      _rfc.setCommandFileDir(updEvent.getCommandFileDir());
      _rfc.setCommandLine(updEvent.getCommandLine());
    }
    else
    {
      _rfc.setCommandFile(null);
      _rfc.setCommandFileDir(null);
      _rfc.setCommandLine(null);
    }

    return _rfc;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateRfc((Rfc)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findRfc(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateRfcEvent updEvent = (UpdateRfcEvent)event;
    return new Object[]
           {
             Rfc.ENTITY_NAME,
             String.valueOf(updEvent.getRfcUid())
           };
  }

}
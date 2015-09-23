/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePortAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Created
 * Aug 23 2005    Tam Wei Xiang       _port.setAttachmentDir(updEvent.getAttachmentDir())
 *                                    in method prepareUpdateData() has been removed
 * Mar 03 2006    Tam Wei Xiang       Added field 'FileGrouping' in Port                                   
 */
package com.gridnode.gtas.server.backend.actions;

import java.util.Map;

import com.gridnode.gtas.events.backend.UpdatePortEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a Port.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdatePortAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8092891114302138485L;

	private Port _port;

  public static final String ACTION_NAME = "UpdatePortAction";

  protected Class getExpectedEventClass()
  {
    return UpdatePortEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPortToMap((Port)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdatePortEvent updEvent = (UpdatePortEvent)event;
    _port = ActionHelper.getManager().findPort(updEvent.getPortUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdatePortEvent updEvent = (UpdatePortEvent)event;

    _port.setDescription(updEvent.getDescription());
    _port.setHostDir(updEvent.getHostDir());
    //Commented by Wei Xiang
    //_port.setAttachmentDir(updEvent.getAttachmentDir());

    Boolean isAddFileExt = updEvent.getIsAddFileExt();
    _port.setIsAddFileExt(isAddFileExt);
    if (isAddFileExt.booleanValue())
    {
      _port.setFileExtType(updEvent.getFileExtType());
      _port.setFileExtValue(updEvent.getFileExtValue());
    }
    else
    {
      _port.setFileExtType(null);
      _port.setFileExtValue(null);
    }
    _port.setOverwrite(updEvent.getIsOverwrite());

    Boolean isDiffFilename = updEvent.getIsDiffFileName();
    _port.setIsDiffFileName(isDiffFilename);
    if (isDiffFilename.booleanValue())
    {
      _port.setFileName(updEvent.getFilename());
    }
    else
    {
      _port.setFileName(null);
    }
    _port.setIsExportGdoc(updEvent.getIsExportGdoc());

    Boolean isRfc = updEvent.getIsRfc();
    _port.setIsRfc(isRfc);
    if (isRfc.booleanValue())
    {
      Rfc rfc = new Rfc();
      rfc.setUId(updEvent.getRfc().longValue());
      _port.setRfc(rfc);
    }
    else
    {
      _port.setRfc(null);
    }

    Integer fileExtType = updEvent.getFileExtType();
    if( isAddFileExt.booleanValue() &&
         Port.SEQ_RUNNING_NUM.equals(fileExtType) ) //Update these fileds only if
                                                    // FileExtension type is SEQ_RUNNING_NO
    {
      _port.setStartNumber(updEvent.getStartNumber());
      _port.setRolloverNumber(updEvent.getRollOverNumber());
      _port.setNextNumber(updEvent.getNextNumber());

      Boolean isPadded = updEvent.getIsPadded();
      _port.setIsPadded(isPadded);

      if(isPadded.booleanValue())
      {
        _port.setFixedNumberLen(updEvent.getFixedNumberLength());
      }
      else
      {
        _port.setFixedNumberLen(null);
      }
    }
    
    _port.setFileGrouping(updEvent.getFileGrouping());
    
    return _port;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updatePort((Port)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findPort(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdatePortEvent updEvent = (UpdatePortEvent)event;
    return new Object[]
           {
             Port.ENTITY_NAME,
             String.valueOf(updEvent.getPortUid())
           };
  }

}
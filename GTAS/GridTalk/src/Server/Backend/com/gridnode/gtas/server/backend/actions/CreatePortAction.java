/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePortAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * May 26 2003    Jagadeesh           Added : Fileds for Port Enhancement.
 * Aug 23 2005    Tam Wei Xiang       newPort.setAttachmentDir() under method
 *                                    prepareCreationData() has been removed
 * Mar 03 2006    Tam Wei Xiang       A new field 'FileGrouping' has been added
 *                                    into Port                                   
 *                                    
 */

package com.gridnode.gtas.server.backend.actions;

import java.util.Map;

import com.gridnode.gtas.events.backend.CreatePortEvent;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new Port.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreatePortAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3960225012143090580L;
	public static final String ACTION_NAME = "CreatePortAction";

  protected Class getExpectedEventClass()
  {
    return CreatePortEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findPort(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreatePortEvent createEvent = (CreatePortEvent)event;
	
	//By Wei Xiang: To enhance GTAS attachment feature, the variable of class Port
	//has been removed. 
    Port newPort = new Port();
    newPort.setName(createEvent.getName());
    newPort.setDescription(createEvent.getDescription());
    newPort.setHostDir(createEvent.getHostDir());

    Boolean isAddFileExt = createEvent.getIsAddFileExt();
    newPort.setIsAddFileExt(isAddFileExt);
    if (isAddFileExt.booleanValue())
    {
      newPort.setFileExtType(createEvent.getFileExtType());
      newPort.setFileExtValue(createEvent.getFileExtValue());
    }
    else
    {
      newPort.setFileExtType(null);
      newPort.setFileExtValue(null);
    }
    newPort.setOverwrite(createEvent.getIsOverwrite());

    Boolean isDiffFilename = createEvent.getIsDiffFileName();
    newPort.setIsDiffFileName(isDiffFilename);
    if (isDiffFilename.booleanValue())
    {
      newPort.setFileName(createEvent.getFilename());
    }
    else
    {
      newPort.setFileName(null);
    }
    newPort.setIsExportGdoc(createEvent.getIsExportGdoc());

    Boolean isRfc = createEvent.getIsRfc();
    newPort.setIsRfc(isRfc);
    if (isRfc.booleanValue())
    {
      Rfc rfc = new Rfc();
      rfc.setUId(createEvent.getRfcUid().longValue());
      newPort.setRfc(rfc);
    }
    else
    {
      newPort.setRfc(null);
    }

    newPort.setStartNumber(createEvent.getStartNumber());
    newPort.setRolloverNumber(createEvent.getRollOverNumber());
    newPort.setNextNumber(createEvent.getNextNumber());

    Boolean isPadded = createEvent.getIsPadded();
    newPort.setIsPadded(isPadded);

    if(isPadded.booleanValue())
    {
      newPort.setFixedNumberLen(createEvent.getFixedNumberLength());
    }
    else
    {
      newPort.setFixedNumberLen(null);
    }
    
    //TWX 03032006
    newPort.setFileGrouping(createEvent.getFileGrouping());
    
    return newPort;
  }


  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreatePortEvent createEvent = (CreatePortEvent)event;
    return new Object[]
           {
             Port.ENTITY_NAME,
             createEvent.getName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createPort((Port)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPortToMap((Port)entity);
  }

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportEntitiesAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 13 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.exportconfig.actions;

import java.io.File;

import com.gridnode.gtas.events.exportconfig.ExportEntitiesEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.exportconfig.helpers.ExportConfigHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the exporting of entities choosen by the user
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ExportEntitiesAction
  extends    AbstractGridTalkAction
  implements IExportConfigConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4195438469415530299L;
	public static final String ACTION_NAME = "ExportEntitiesAction";

  protected Class getExpectedEventClass()
  {
    return ExportEntitiesEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ExportEntitiesEvent exportEvent = (ExportEntitiesEvent)event;
    File zipFile =
      ExportConfigHelper.exportConfig(exportEvent.getConfigEntitiesContainer());

    String zipFilename = zipFile.getName();
//    String zipFilename = FileUtil.move(IPathConfig.PATH_TEMP, "",
//                                       IPathConfig.PATH_TEMP, _userID+"/out/",
//                                       zipFile.getName());
    return constructEventResponse(zipFilename);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    return new Object[] {};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    return new Object[] {};
  }
}
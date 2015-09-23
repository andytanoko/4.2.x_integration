/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResolveEntitiesAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 13 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.exportconfig.actions;

import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.gtas.events.exportconfig.ResolveEntitiesEvent;
import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;
import com.gridnode.gtas.server.exportconfig.helpers.ExportConfigHelper;
import com.gridnode.gtas.server.exportconfig.helpers.Logger;

import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

//import java.io.File;

/**
 * This Action class handles the exporting of entities choosen by the user
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ResolveEntitiesAction
  extends    AbstractGridTalkAction
  implements IExportConfigConstants
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1590547803514344896L;
	public static final String ACTION_NAME = "ResolveEntitiesAction";

  protected Class getExpectedEventClass()
  {
    return ResolveEntitiesEvent.class;
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
    ResolveEntitiesEvent resolveEvent = (ResolveEntitiesEvent)event;
    ImportRegistry registry = (ImportRegistry)sm.getAttribute(REGISTRY_KEY);
    ConfigEntitiesContainer overwriteList =
      resolveEvent.getConfigEntitiesContainer();

    ExportConfigHelper.resolveAndImport(registry, overwriteList);

    try
    {
      FileUtil.deleteFolder(IPathConfig.PATH_TEMP, registry.getUnzipDir().getName());
    }
    catch (Exception ex)
    {
      Logger.debug("[ResolveEntitiesAction.doProcess] Unable to delete unzip directory "+
                    registry.getUnzipDir().getAbsolutePath());
    }

    return constructEventResponse();
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
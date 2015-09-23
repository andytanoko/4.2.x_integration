/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportEntitiesAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 13 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.exportconfig.actions;

import java.io.File;
import java.util.List;

import com.gridnode.gtas.events.exportconfig.ImportEntitiesEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;
import com.gridnode.gtas.server.exportconfig.helpers.ExportConfigHelper;
import com.gridnode.gtas.server.exportconfig.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
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
public class ImportEntitiesAction
  extends    AbstractGridTalkAction
  implements IExportConfigConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3393237376457364353L;
	public static final String ACTION_NAME = "ImportEntitiesAction";

  protected Class getExpectedEventClass()
  {
    return ImportEntitiesEvent.class;
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
    ImportEntitiesEvent importEvent = (ImportEntitiesEvent)event;
    String configFilename = importEvent.getConfigurationFilename();
    boolean isOverwrite = importEvent.isOverwite();

    File configFile = FileUtil.getFile(IPathConfig.PATH_TEMP, _userID+"/in/",
                                       configFilename);
    if ((configFile == null) || (!configFile.exists()))
    {
      throw new ImportConfigException("Configuration file not found");
    }

    List resultList = ExportConfigHelper.importConfig(configFile, isOverwrite);

    ConfigEntitiesContainer conflicts = (ConfigEntitiesContainer)resultList.get(0);
    ImportRegistry registry = (ImportRegistry)resultList.get(1);
    if (!conflicts.isEmpty())
    {
      sm.setAttribute(REGISTRY_KEY, registry);
    }
    else
    {
      try
      {
        FileUtil.deleteFolder(IPathConfig.PATH_TEMP, registry.getUnzipDir().getName());
      }
      catch (Exception ex)
      {
        Logger.debug("[ImportEntitiesAction.doProcess] Unable to delete unzip directory "+
                      registry.getUnzipDir().getAbsolutePath());
      }
    }

    try
    {
      FileUtil.delete(IPathConfig.PATH_TEMP,  _userID+"/in/", configFilename);
    }
    catch (Exception ex)
    {
      Logger.debug("[ImportEntitiesAction.doProcess] Unable to delete config file"+
                    configFilename);
    }

    return constructEventResponse(conflicts);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ImportEntitiesEvent importEvent = (ImportEntitiesEvent)event;
    Object[] params = new Object[]
                      {
                        importEvent.getConfigurationFilename(),
                      };
    return params;
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    ImportEntitiesEvent importEvent = (ImportEntitiesEvent)event;
    Object[] params = new Object[]
                      {
                        importEvent.getConfigurationFilename(),
                      };
    return params;
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-15     Andrew Hill         Created
 * 2002-12-11     Andrew Hill         Autorefresh nonsense
 * 2003-03-21     Andrew Hill         Support for Paging
 * 2003-07-08     Andrew Hill         Remove / refactor deprecated methods
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.ctrl.IGTMappingFileManager;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class MappingFileListAction extends EntityListAction
{
  Object[] _columns =
  { //20030708AH
    IGTMappingFileEntity.NAME,
    IGTMappingFileEntity.DESCRIPTION,
    IGTMappingFileEntity.FILENAME,
    IGTMappingFileEntity.TYPE,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTMappingFileManager manager = (IGTMappingFileManager)getManager(actionContext);
    Short type = getMappingFileType(actionContext);
    if(type == null)
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(type, IGTMappingFileEntity.TYPE);
    }
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    String param = "type";
    Short value = getMappingFileType(actionContext);
    if(value != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,param,value.toString());
    }
    return refreshUrl;
  }

  private Short getMappingFileType(ActionContext actionContext)
  {
    String type = actionContext.getRequest().getParameter("type");
    if(type == null)
    {
      type = (String)actionContext.getRequest().getAttribute("mappingFile.type");
    }
    if(type != null)
    {
      try
      {
        return new Short(type);
      }
      catch(Exception e)
      {
        
      }
    }
    return null;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_MAPPING_FILE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_MAPPING_FILE; //20030708AH
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(
              new MappingFileListDecoratorRenderer(rContext,getMappingFileType(actionContext),
                                                   actionContext.getMapping()));
  }
}
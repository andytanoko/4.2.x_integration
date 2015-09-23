/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 * 2005-03-15     Andrew Hill         Two-level access control changes
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class SearchQueryListAction extends EntityListAction
{
  private static final Object[] _columns =
  {
    IGTSearchQueryEntity.NAME,
    IGTSearchQueryEntity.DESCRIPTION,
    IGTSearchQueryEntity.CREATED_BY,
    IGTSearchQueryEntity.IS_PUBLIC
  };

  protected Object[] getColumnReferences(ActionContext actionContext)
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_SEARCH_QUERY;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_SEARCH_QUERY;
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  { // 20031203 DDJ
    rPipe.addRenderer(new SearchQueryListDecoratorRenderer(rContext));
  }
  
  /*
  protected IListViewOptions getListOptions(ActionContext actionContext) throws GTClientException
  { //20050315AH
    IListViewOptions options = super.getListOptions(actionContext);
    if(options instanceof ListViewOptionsImpl)
    {
      IGTSession gtasSession = getGridTalkSession(actionContext);
      if(!gtasSession.isAdmin())
      {
        ListViewOptionsImpl lvo = (ListViewOptionsImpl)options;
        lvo.setAllowsEdit(false);
        lvo.setAllowsSelection(false);
      }
    }
    return options;
  }*/

}
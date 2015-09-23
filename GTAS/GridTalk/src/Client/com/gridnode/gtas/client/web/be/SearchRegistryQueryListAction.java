/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryQueryListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchRegistryQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchRegistryQueryManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.VirtualListPager;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class SearchRegistryQueryListAction extends EntityListAction
{
  public static final String SEARCHES_KEY = "SearchRegistryQueryList";

  private static final Object[] _columns =
  { 
    IGTSearchRegistryQueryEntity.SEARCH_ID,
    IGTSearchRegistryQueryEntity.DT_SUBMITTED,
    IGTSearchRegistryQueryEntity.DT_RESPONDED,
    IGTSearchRegistryQueryEntity.IS_EXCEPTION,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_SEARCH_REGISTRY_QUERY;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY;
  }

  protected IGTListPager getListPager(ActionContext actionContext) throws GTClientException
  { 
    try
    { // Obtain list of searchIds from session and query the manager for each individually.
      List listItems = getSearches(actionContext);
      // Create a listpager based on results
      IGTSession gtasSession = getGridTalkSession(actionContext);
      VirtualListPager listPager = new VirtualListPager(gtasSession);
      listPager.setItems(listItems);
      return listPager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting list of SearchRegistryQuery entities",t);
    }
  }

  protected void freePager(ActionContext actionContext, IGTListPager pager) throws GTClientException
  {
    if(pager instanceof VirtualListPager)
    {
      List listItems = getSearches(actionContext);
      ((VirtualListPager)pager).setItems(listItems);
      pager.free();
    }
    else
    {
      pager.free();
    }
  }

  protected List getSearches(ActionContext actionContext) throws GTClientException
  {
    try
    { // Obtain list of searchIds from session and query the manager for each individually.
      HttpSession session = actionContext.getSession();
      IGTSearchRegistryQueryManager manager = (IGTSearchRegistryQueryManager)getManager(actionContext);
      long[] searchIds = null;
      synchronized(session)
      { // We need to copy the searchIds into an array so we can retrieve the entities at our leisure
        // without fear that another thread will modify the list (which btw: is why we synchronize here.
        // We copy into an array so that the critical section doesnt take forever to execute.
        Collection searches = (Collection)session.getAttribute(SEARCHES_KEY);
        if(searches == null)
        {
          searchIds = null;
        }
        else
        {
          searchIds = new long[searches.size()];
          Iterator i = searches.iterator();
          int index = 0;
          while(i.hasNext())
          {
            long searchId = ((Long)i.next()).longValue();
            searchIds[index] = searchId;
            index++;
          }
        }
      }
      List listItems = null; // Use a List
      if(searchIds == null)
      {
        listItems = new ArrayList(0);
      }
      else
      {
        listItems = new ArrayList(searchIds.length);
        for(int j=0; j < searchIds.length; j++)
        {
          IGTSearchRegistryQueryEntity entity = (IGTSearchRegistryQueryEntity)manager.getByUid(searchIds[j]);
          listItems.add(entity);
        }
      }
      return listItems;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting list of SearchRegistryQuery entities",t);
    }
  }

  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    // Modify listoptions to hide the delete button
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
    listOptions.setDeleteURL(null);
    listOptions.setDeleteLabelKey(null);
    return listOptions;
  }
}
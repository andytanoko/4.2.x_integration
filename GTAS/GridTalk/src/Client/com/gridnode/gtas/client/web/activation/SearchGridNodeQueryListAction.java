/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeQueryListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-17     Andrew Hill         Created
 * 2002-11-11     Andrew Hill         Use list of searchIds in the session
 * 2003-03-24     ANdrew Hill         ListPager support
 */
package com.gridnode.gtas.client.web.activation;

import java.util.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;

public class SearchGridNodeQueryListAction extends EntityListAction
{
  public static final String SEARCHES_KEY = "SearchGridNodeQueryList";

  public static final Object[] _columns = {
    IGTSearchGridNodeQueryEntity.SEARCH_ID,
    IGTSearchGridNodeQueryEntity.DT_SUBMITTED,
    IGTSearchGridNodeQueryEntity.DT_RESPONDED,
    "criteria.criteriaType",
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_be";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "searchGridNodeQuery";
  }

  protected IGTListPager getListPager(ActionContext actionContext) throws GTClientException
  { //20030321AH
    try
    { //20021111AH - Obtain list of searchIds from session and query the manager for each
      //individually.
      List listItems = getSearches(actionContext); //20030324AH
      //20030324AH - Create a listpager based on results
      IGTSession gtasSession = getGridTalkSession(actionContext); //20030331AH
      VirtualListPager listPager = new VirtualListPager(gtasSession); //20030331AH
      listPager.setItems(listItems);
      return listPager;
      //...
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting list of SearchGridNodeQuery entities",t);
    }
  }

  protected void freePager(ActionContext actionContext, IGTListPager pager) throws GTClientException
  { //20030324AH
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
  { //20030324AH
    try
    { //20021111AH - Obtain list of searchIds from session and query the manager for each
      //individually.
      HttpSession session = actionContext.getSession();
      IGTSearchGridNodeQueryManager manager = (IGTSearchGridNodeQueryManager)getManager(actionContext);
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
      List listItems = null; //20030324AH - Use a List
      if(searchIds == null)
      {
        listItems = new ArrayList(0);
      }
      else
      {
        listItems = new ArrayList(searchIds.length);
        for(int j=0; j < searchIds.length; j++)
        {
          IGTSearchGridNodeQueryEntity entity = (IGTSearchGridNodeQueryEntity)manager.getByUid(searchIds[j]);
          listItems.add(entity);
        }
      }
      return listItems;
      //...
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting list of SearchGridNodeQuery entities",t);
    }
  }


  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    // Modify listoptions to hide the delete button
    ListViewOptionsImpl listOptions= (ListViewOptionsImpl)super.getListOptions(actionContext);
    listOptions.setDeleteURL(null);
    listOptions.setDeleteLabelKey(null);
    return listOptions;
  }
}
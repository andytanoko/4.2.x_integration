/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DynamicNavTreeNodeInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.navigation.DynamicNavTreeNodeInfo;
import com.gridnode.gtas.client.web.navigation.NavTreeNode;

public class SearchQueryNavTreeNodeInfo extends DynamicNavTreeNodeInfo
{
  public ArrayList getChildTreeNodes() throws GTClientException
  {
    ArrayList children = new ArrayList();
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(_rContext.getSession());
      IGTSearchQueryManager manager = (IGTSearchQueryManager)gtasSession.getManager(IGTManager.MANAGER_SEARCH_QUERY);
      Collection searchQueries = manager.getAll();   
      Iterator i = searchQueries.iterator();
      while(i.hasNext())
      {
        IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)i.next();
        String name = searchQuery.getFieldString(IGTSearchQueryEntity.NAME);   
        long uid = searchQuery.getUid();   

        NavTreeNode child = createNavTreeNode("ntn_search_" + uid, "/sysFoldersListView.do?folder=Search&searchUid=" + uid, name, "images/entities/gridDocument.gif");
        children.add(child);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get child info for child tree nodes", t);
    }    
    return children;
  }
}
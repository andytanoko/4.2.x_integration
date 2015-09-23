/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SysFoldersListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-22     Andrew Hill         Created
 * 2002-12-11     Andrew Hill         Override appendRefreshParameters()
 * 2002-12-30     Daniel D'Cotta      Commented out HAS_ATTACHMENT as it showed
 *                                    'Is Request'
 * 2003-02-13     Jared Low           Uncommented HAS_ATTACHMENT. Undo what
 *                                    Daniel did.
 * 2003-06-17     Andrew Hill         Remove deprecated getNavgroup() method
 * 2003-07-08     Andrew Hill         Convert from deprecated getColumns to new getColumnReferences method
 * 2003-08-25     Andrew Hill         Show userTrackingIdentitifier and processInstanceUid in listviews
 * 2003-10-19     Daniel D'Cotta      Added Sorting
 * 2003-11-17     Daniel D'Cotta      Added support for Search
 * 2007-02-12     Neo Sok Lay         Fix the folder parameter used for data filter -- need to convert
 *                                    to the actual constant.
 * 2008-11-03     Ong Eu Soon         Rearrange displayed fields and added 2 more fields on Outbound columns
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentManager;    
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.IColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;
import com.gridnode.gtas.model.document.IGridDocument;

public class SysFoldersListAction extends EntityListAction
{
  private static final Object[] _adminOnlyLinks = 
  { //20050315AH
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.R_PARTNER_ID,
    IGTGridDocumentEntity.R_CHANNEL_NAME,
    IGTGridDocumentEntity.S_PARTNER_ID,
    IGTGridDocumentEntity.PORT_NAME,
    IGTGridDocumentEntity.S_ROUTE,    
  };
  
  private static final Object[] _columns =
  { //20030708AH
    IGTGridDocumentEntity.FOLDER,
    IGTGridDocumentEntity.HAS_ATTACHMENT, //20021205AH // 20021230 DDJ: Commented out // 20030213JL Uncommented
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030825AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
  };
  
  private static final Object[] _importColumns = 
  { //20030708AH
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.HAS_ATTACHMENT, //20021205AH // 20021230 DDJ: Commented out // 20030213JL Uncommented
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.R_PARTNER_ID,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.DT_IMPORT,
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030825AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
  };
  
  private static final Object[] _outboundColumns =
  { //20030708AH
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.HAS_ATTACHMENT, //20021205AH // 20021230 DDJ: Commented out // 20030213JL Uncommented
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.R_PARTNER_ID,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.AUDIT_FILE_NAME,
    IGTGridDocumentEntity.DOC_TRANS_STATUS,
    IGTGridDocumentEntity.R_CHANNEL_NAME,
    IGTGridDocumentEntity.S_ROUTE,
    IGTGridDocumentEntity.DT_SEND_END,
    IGTGridDocumentEntity.DT_TRANSACTION_COMPLETE,
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030825AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
  };
  
  private static final Object[] _inboundColumns = 
  { //20030708AH
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.HAS_ATTACHMENT, //20021205AH // 20021230 DDJ: Commented out // 20030213JL Uncommented
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.S_PARTNER_ID,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.AUDIT_FILE_NAME,
    IGTGridDocumentEntity.DOC_TRANS_STATUS,
    IGTGridDocumentEntity.S_ROUTE,
    IGTGridDocumentEntity.DT_RECEIVE_END,
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030825AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
  };
  
  private static final Object[] _exportColumns = 
  { //20030708AH
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.HAS_ATTACHMENT, //20021205AH // 20021230 DDJ: Commented out // 20030213JL Uncommented
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.S_PARTNER_ID,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.PORT_NAME,
    IGTGridDocumentEntity.DT_EXPORT,
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030825AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
    IGTGridDocumentEntity.AUDIT_FILE_NAME,
  };
 
  protected int _sortColumn = 0;
  protected boolean _sortAscending = true;
  protected boolean _newSort = true;
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH - Previously getColumns()
    Object[] columns = null;

    String folder = getFolder(actionContext);
    if(IGTGridDocumentEntity.FOLDER_IMPORT.equalsIgnoreCase(folder))
    {
      columns = _importColumns; //20030708AH
    }
    else if(IGTGridDocumentEntity.FOLDER_OUTBOUND.equalsIgnoreCase(folder))
    {
      columns = _outboundColumns; //20030708AH
    }
    else if(IGTGridDocumentEntity.FOLDER_INBOUND.equalsIgnoreCase(folder))
    {
      columns = _inboundColumns; //20030708AH
    }
    else if(IGTGridDocumentEntity.FOLDER_EXPORT.equalsIgnoreCase(folder))
    {
      columns = _exportColumns; //20030708AH
    }
    else
    {
      columns = _columns; //20030708AH
    }
    return columns;
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  {
    IGTGridDocumentManager manager = (IGTGridDocumentManager)getManager(actionContext);
    
    // 20031019 DDJ: for Sorting
    initialiseSorting(actionContext);
    Number[] sortField = null; 
    boolean[] sortAscending = null;
    if(_sortColumn > 0)
    {
      Object[] columnReferences = getColumnReferences(actionContext);
      // Currently only support single sort field
      sortField = new Number[1]; 
      sortAscending = new boolean[1];
      sortField[0] = (Number)columnReferences[_sortColumn - 1]; 
      sortAscending[0] = _sortAscending;
    }
          
    String folder = getFolder(actionContext);
    if(folder == null)
    {
      return manager.getListPager(null, null, false, sortField, sortAscending); // 20031019 DDJ
    }
    else
    {
      // 20031117 DDJ: Added support for Search
      if("Search".equals(folder))
      {
        String searchUid = actionContext.getRequest().getParameter("searchUid");
        return manager.getSearchQueryListPager(new Long(searchUid), sortField, sortAscending); // 20031127 DDJ
      }
      else
      {
        // return manager.getAllInFolder(folder); //20030324AH
        return manager.getListPager(fixFolder(folder), IGridDocument.FOLDER, false, sortField, sortAscending); // 20031019 DDJ
      }
    }
  }

  //convert the folder to actual Folder constant
  private String fixFolder(String folder)
  {
    if ("import".equalsIgnoreCase(folder))
    {
      return IGTGridDocumentEntity.FOLDER_IMPORT;
    }
    else if ("outbound".equalsIgnoreCase(folder))
    {
      return IGTGridDocumentEntity.FOLDER_OUTBOUND;
    }
    else if ("inbound".equalsIgnoreCase(folder))
    {
      return IGTGridDocumentEntity.FOLDER_INBOUND;
    }
    else if ("export".equalsIgnoreCase(folder))
    {
      return IGTGridDocumentEntity.FOLDER_EXPORT;
    }
    return folder;
  }
  
  protected String getFolder(ActionContext actionContext)
  {
    String folder = actionContext.getRequest().getParameter("folder");
    if(folder == null)
    {
      folder = (String)actionContext.getRequest().getAttribute("gridDocument.folder");
    }
    return folder;
  }

  protected void initialiseSorting(ActionContext actionContext)
  {
    int sortColumn = StaticUtils.primitiveIntValue(actionContext.getRequest().getParameter("sortColumn"));
    int newSortColumn = StaticUtils.primitiveIntValue(actionContext.getRequest().getParameter("newSortColumn"));
    boolean sortAscending = StaticUtils.primitiveBooleanValue(actionContext.getRequest().getParameter("sortAscending"));
    if(newSortColumn > 0)
    {
      _sortColumn = newSortColumn;
      if(newSortColumn == sortColumn)
      {
        _sortAscending = !sortAscending; 
      }
      else
      {
        _sortAscending = true; 
      }
      _newSort = true;
    }
    else
    {
      _sortColumn = sortColumn;
      _sortAscending = sortAscending; 
      _newSort = false;  
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_GRID_DOCUMENT;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  { 
    return IGTEntity.ENTITY_GRID_DOCUMENT; //20030708AH
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new SysFoldersListDecoratorRenderer(rContext,getFolder(actionContext)));
  }

  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);

    //08102002 DDJ: Only Import allows Edit
    String folder = getFolder(actionContext);
    if(!IGTGridDocumentEntity.FOLDER_IMPORT.equalsIgnoreCase(folder))
    {
      listOptions.setAllowsEdit(false);
    }
    listOptions.setAllowsSorting(true);

    //20050315AH - Dual level access control
    IGTSession gtasSession = getGridTalkSession(actionContext);
    if(!gtasSession.isAdmin())
    {
      listOptions.setAllowsEdit(false);
      listOptions.setAllowsSelection(false);
      IColumnEntityAdapter cea = listOptions.getColumnAdapter();
      if(cea instanceof ColumnEntityAdapter)
      {
        for(int c=0; c < _adminOnlyLinks.length; c++)
        {
          Object disableField = _adminOnlyLinks[c];
          ((ColumnEntityAdapter)cea).setColumnLinksForFieldEnabled(disableField, false);
        }
      }
    }
    //...

    return listOptions;
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    String folder = getFolder(actionContext);
    if(folder != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "folder", folder);
      
      // 20031117 DDJ: Added support for Search
      if("Search".equals(folder))
      {
        String searchUid = actionContext.getRequest().getParameter("searchUid");
        refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "searchUid", searchUid);
      }
    }
    initialiseSorting(actionContext);      
    if(_sortColumn > 0)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "sortColumn", "" + _sortColumn);
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "sortAscending", "" + _sortAscending);
    }
    return refreshUrl;
  }
}
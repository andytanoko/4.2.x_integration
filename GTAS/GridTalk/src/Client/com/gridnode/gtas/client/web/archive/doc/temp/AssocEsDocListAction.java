/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AssocEsDocListAction.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 10 2005			Sumedh Chalermkanjana			Created
 * Oct 17 2006      Regina Zeng               Add column gdoc ID
 * Dec 14 2006      Tam Wei Xiang             To display the Remark for DocumentMetaInfo
 */
package com.gridnode.gtas.client.web.archive.doc.temp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;
import com.gridnode.gtas.client.ctrl.*;

public class AssocEsDocListAction extends EntityListAction
{ 
	public static final Number[] COLUMNS =
	{
    IGTEsDocEntity.GDOC_ID,
		IGTEsDocEntity.FOLDER,
		IGTEsDocEntity.DOCUMENT_TYPE,
		IGTEsDocEntity.DOC_DATE_CREATED,
		IGTEsDocEntity.DOC_DATE_SENT,
		IGTEsDocEntity.DOC_DATE_RECEIVED,
    IGTEsDocEntity.REMARK
  };

	/**
	 * Variables for sorting.
	 */
	protected int _sortColumn = 0;

	protected boolean _sortAscending = true;

	protected boolean _newSort = true;

	protected Object[] getColumnReferences(ActionContext actionContext)
	{
		return COLUMNS;
	}

	protected int getManagerType(ActionContext actionContext)
	{
		return IGTManager.MANAGER_ES_DOC;
	}

	protected String getResourcePrefix(ActionContext actionContext)
	{
		return IGTEntity.ENTITY_ES_DOC;
	}

	/**
	 * The implementation for this method is more complex because there is added
	 * functionality: Able to sort each column.
	 */
	protected IGTListPager getListPager(ActionContext actionContext)
			throws GTClientException
	{
		IGTEsDocManager manager = (IGTEsDocManager) getManager(actionContext);

		initialiseSorting(actionContext);
		Number[] sortField = null;
		boolean[] sortAscending = null;
		if (_sortColumn > 0)
		{
			Object[] columnReferences = getColumnReferences(actionContext);
			// Currently only support single sort field
			sortField = new Number[1];
			sortAscending = new boolean[1];
			sortField[0] = (Number) columnReferences[_sortColumn - 1];
			sortAscending[0] = _sortAscending;
		}
    return manager.getAssocEsDocListPager(getUId(actionContext), sortField, sortAscending);
	}

	private Long getUId(ActionContext actionContext) throws GTClientException
	{
    String str = actionContext.getRequest().getParameter("uid");
    return new Long(str);
	}

	protected String appendRefreshParameters(ActionContext actionContext,
			String refreshUrl) throws GTClientException
	{
	  initialiseSorting(actionContext);
		
		//TEST
		debug("add sortColumn = " + (_sortColumn > 0));
		
		if (_sortColumn > 0)
		{
			refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "sortColumn", "" + _sortColumn);
			refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,	"sortAscending", "" + _sortAscending);
		}
		
		/* add uid to refreshUrl */
		String uid = actionContext.getRequest().getParameter("uid");
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "uid", uid);
		
		//TEST
		debug("refreshUrl = " + refreshUrl);
		
		return refreshUrl;
	}
	
	protected void initialiseSorting(ActionContext actionContext)
	{
		int sortColumn = StaticUtils.primitiveIntValue(actionContext
				.getRequest().getParameter("sortColumn"));
		int newSortColumn = StaticUtils.primitiveIntValue(actionContext
				.getRequest().getParameter("newSortColumn"));
		boolean sortAscending = StaticUtils.primitiveBooleanValue(actionContext
				.getRequest().getParameter("sortAscending"));
		if (newSortColumn > 0)
		{
			_sortColumn = newSortColumn;
			if (newSortColumn == sortColumn)
			{
				_sortAscending = !sortAscending;
			} else
			{
				_sortAscending = true;
			}
			_newSort = true;
		} else
		{
			_sortColumn = sortColumn;
			_sortAscending = sortAscending;
			_newSort = false;
		}
	}
	
	private static void debug(String message)
	{
		com.gridnode.gtas.client.web.archive.helpers.Logger.debug("[AssocEsDocListAction] " + message);
	}
	
	protected IListViewOptions getListOptions(ActionContext actionContext)
	  throws GTClientException
	{
	  ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
	  listOptions.setAllowsSorting(true);
	  listOptions.setAllowsSelection(false);
		listOptions.setAllowsEdit(false);
		
		listOptions.setHeadingLabelKey("AssocEsDoc.general.heading"); 
    
	  return listOptions;
	}
	
	protected void processPipeline(ActionContext actionContext,
                                 RenderingContext rContext,
                                 IRenderingPipeline rPipe)
    throws GTClientException
  { 
    EsPiDetailRenderer renderer = new EsPiDetailRenderer(rContext,getUId(actionContext));
    rPipe.addRenderer(renderer);
    super.processPipeline(actionContext, rContext, rPipe);
  }
}

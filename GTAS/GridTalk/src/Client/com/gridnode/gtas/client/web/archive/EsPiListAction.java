/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsPiListAction.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 3 2005			  Sumedh Chalermkanjana			Created
 * Oct 20 2005			Sumedh C.									Removed DOCUMENT_DATE column
 * Oct 16 2006      Regina Zeng               Add USER_TRACKING_ID column and REMARK
 */
package com.gridnode.gtas.client.web.archive;

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
import com.gridnode.gtas.client.web.archive.helpers.*;
import com.gridnode.gtas.events.dbarchive.*;
import java.util.*;

/**
 * Implementation of this class is based on SysFoldersListAction.java
 */
public class EsPiListAction extends EntityListAction
{
	public static final Number[] COLUMNS =
	{ 
    IGTEsPiEntity.PROCESS_INSTANCE_ID,
    IGTEsPiEntity.PROCESS_DEF,
    IGTEsPiEntity.DOCUMENT_NUMBER,
    IGTEsPiEntity.PARTNER_ID,
    IGTEsPiEntity.PARTNER_NAME,
    IGTEsPiEntity.PROCESS_STATE,
    IGTEsPiEntity.DOCUMENT_DATE,
    IGTEsPiEntity.PROCESS_START_DATE,
    IGTEsPiEntity.PROCESS_END_DATE,
    IGTEsPiEntity.USER_TRACKING_ID,
    IGTEsPiEntity.REMARK,
  };

	/**
	 * Variables for sorting.
	 */
	protected int _sortColumn = 0;

	protected boolean _sortAscending = true;

	protected boolean _newSort = true;
	
	//TWX 27 Mar 2006
	private static final String DATE_TYPE_FROM = "from";
	private static final String DATE_TYPE_TO = "to";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm";
	
	protected Object[] getColumnReferences(ActionContext actionContext)
	{
		return COLUMNS;
	}

	protected int getManagerType(ActionContext actionContext)
	{
		return IGTManager.MANAGER_ES_PI;
	}

	protected String getResourcePrefix(ActionContext actionContext)
	{
		return IGTEntity.ENTITY_ES_PI;
	}

	/**
	 * The implementation for this method is more complex because there is added
	 * functionality: Able to sort each column.
	 */
	protected IGTListPager getListPager(ActionContext actionContext)
			throws GTClientException
	{
		IGTEsPiManager manager = (IGTEsPiManager) getManager(actionContext);

		initialiseSorting(actionContext);
		Number[] sortField = null;
		boolean[] sortAscending = null;
		
		//Default sort column will be Process_Start_Date
		if(_sortColumn == 0)
		{
			Object[] columnReferences = getColumnReferences(actionContext);
			// Currently only support single sort field
			sortField = new Number[1];
			sortAscending = new boolean[1];
			sortField[0] = IGTEsPiEntity.PROCESS_START_DATE;
			sortAscending[0] = false; //we sort descending
		}
		
		else if (_sortColumn > 0)
		{
			Object[] columnReferences = getColumnReferences(actionContext);
			// Currently only support single sort field
			sortField = new Number[1];
			sortAscending = new boolean[1];
			sortField[0] = (Number) columnReferences[_sortColumn - 1];
			sortAscending[0] = _sortAscending;
		}
		
		//TWX: added
		TimeZone userSelectTZ  = this.getTimeZone(actionContext);
		IGTSearchEsPiDocumentEntity entity = getSearchEsDoc(actionContext);
		EsPiSearchQuery searchQuery = Util.getEsPiSearchQuery(entity);
		log("EsPiListAction entity we get is "+ searchQuery.toString());
		return manager.getEsPiListPager(searchQuery, sortField, sortAscending, userSelectTZ);
	}
	
	
	private IGTSearchEsPiDocumentEntity getSearchEsDoc(ActionContext actionContext)
		throws GTClientException
	{
		log("[EsPiListAction.getSearchEsDoc] starting");
		IGTEntity entity = (IGTEntity)actionContext.getSession().getAttribute(IGTSearchEsPiDocumentEntity.SEARCH_PI_ENTITY);
		if(entity == null)
		{
			throw new NullPointerException("[EsPiListAction] cannot retrieve seachEsPiDoc entity from session !!!");
		}    
    return (IGTSearchEsPiDocumentEntity)entity;
	}
	
	protected String appendRefreshParameters(ActionContext actionContext,
			String refreshUrl) throws GTClientException
	{    
		initialiseSorting(actionContext);
		if (_sortColumn > 0)
		{
			refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,
					"sortColumn", "" + _sortColumn);
			refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,
					"sortAscending", "" + _sortAscending);
		}
		
		/* add espi search query parameters to refresh url */
		/*
		IGTSearchEsPiDocumentEntity entity = getSearchEsDoc(actionContext);
		EsPiSearchQuery searchQuery = Util.getEsPiSearchQuery(entity);
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docNo", searchQuery.getDocNo());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "process", searchQuery.getProcess());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "processState", searchQuery.getProcessState());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "partnerID", searchQuery.getPartnerId());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "partnerName", searchQuery.getPartnerName());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateFrom", searchQuery.getDocDateFrom());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateTo", searchQuery.getDocDateTo());
  	refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromDate", searchQuery.getProcessStartFromDate());
  	refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromTime", searchQuery.getProcessStartFromTime());
  	refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toDate", searchQuery.getProcessStartToDate());
  	refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toTime", searchQuery.getProcessStartToTime());
  	*/
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
	
  /**
   * For testing.
   */
	private void log(String message)
	{
		Logger.debug("[EsPiListAction] " + message);
	}
  
  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
    listOptions.setAllowsSorting(true);
    listOptions.setAllowsSelection(false);
		listOptions.setAllowsEdit(false);     
    
    return listOptions;
  }
  
  protected void processPipeline(ActionContext actionContext,
                                 RenderingContext rContext,
                                 IRenderingPipeline rPipe)
    throws GTClientException
  {
  	IGTSearchEsPiDocumentEntity entity = getSearchEsDoc(actionContext);
    EsPiSearchRenderer renderer = new EsPiSearchRenderer(rContext, Util.getEsPiSearchQuery(entity), getTimeZone(actionContext));
    rPipe.addRenderer(renderer);
    super.processPipeline(actionContext, rContext, rPipe);
  }
  
/**
   * Acquire the default time(00:00 or 23:59) if the time we input is empty string or null
   * @param time
   * @param dateType
   * @return
   */
  private static String getDefaultTime(String time, String dateType)
	{
		if(time != null && ! "".equals(time))
		{
			return time;
		}
		else if(DATE_TYPE_FROM.equals(dateType))
		{
			return "00:00";
		}
		else if(DATE_TYPE_TO.equals(dateType))
		{
			return "23:59";
		}
		else
		{
			throw new IllegalArgumentException("[EsPiSearchRenderer.getProcessTime] dateType "+ dateType+" is not supported.");
		}
	}
}

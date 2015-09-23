/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsDocListAction.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 6 2005		Sumedh Chalermkanjana			Created
 * 28 Mar 2006  Tam Wei Xiang             -Added new column process isntance ID.
 *                                        -Modified appendRefreshParameter to add
 *                                        in new search criteria.
 * 17 Oct 2006  Regina Zeng               Remove column UdocFilename and AuditFilename
 *                                        Add column gdoc ID, remark, doc date
 */
package com.gridnode.gtas.client.web.archive.doc;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.archive.EsDocSearchRenderer;
import com.gridnode.gtas.client.web.archive.helpers.*;
import com.gridnode.gtas.events.dbarchive.*;

import java.util.TimeZone;

/**
 * Implementation of this class is based on com.gridnode.gtas.client.web.estore.EsPiListAction
 */
public class EsDocListAction extends EntityListAction
{
	public static final Number[] COLUMNS =
	{
    IGTEsDocEntity.GDOC_ID,
    IGTEsDocEntity.FOLDER,
    IGTEsDocEntity.DOCUMENT_TYPE,
		IGTEsDocEntity.DOCUMENT_NUMBER,
		IGTEsDocEntity.PARTNER_ID,
		IGTEsDocEntity.PARTNER_NAME,
    IGTEsDocEntity.DOC_DATE,
		IGTEsDocEntity.DOC_DATE_CREATED,
		IGTEsDocEntity.DOC_DATE_SENT,
		IGTEsDocEntity.DOC_DATE_RECEIVED,
		IGTEsDocEntity.PROCESS_INSTANCE_ID,
    IGTEsDocEntity.USER_TRACKING_ID,
    IGTEsDocEntity.REMARK
  };
  //IGTEsDocEntity.FILENAME,
  //IGTEsDocEntity.UDOC_FILENAME,
	/*
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
		
		TimeZone userSelectTZ = getTimeZone(actionContext);
		
		IGTSearchEsDocDocumentEntity entity = getSearchEsDoc(actionContext);
		EsDocSearchQuery searchQuery = Util.getEsDocSearchQuery(entity);
		Logger.log("[EsDocListAction.getListPage] searchQuery = "+ searchQuery.toString());
		return manager.getEsDocListPager(searchQuery, sortField, sortAscending, userSelectTZ);
	}
	
	private IGTSearchEsDocDocumentEntity getSearchEsDoc(ActionContext actContext)
	{
    IGTEntity entity = (IGTEntity)actContext.getSession().getAttribute(IGTSearchEsDocDocumentEntity.SEARCH_DOC_ENTITY);
		if(entity == null)
		{
			throw new NullPointerException("[EsDocListAction.getSearchEsDoc] Cannot get searchEsDoc entity from session !!! ");
		}
		return (IGTSearchEsDocDocumentEntity)entity;
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
		
		/* add esdoc search query parameters to refreshUrl */
		/*
		IGTSearchEsDocDocumentEntity entity = getSearchEsDoc(actionContext);
		EsDocSearchQuery searchQuery = Util.getEsDocSearchQuery(entity);
		
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docNo", searchQuery.getDocNo());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docType", searchQuery.getDocType());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "partnerId", searchQuery.getPartnerId());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "partnerName", searchQuery.getPartnerName());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateFrom", searchQuery.getDocDateFrom());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateTo", searchQuery.getDocDateTo());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateSentFrom", searchQuery.getDocDateSentFrom());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateSentTo", searchQuery.getDocDateSentTo());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateReceivedFrom", searchQuery.getDocDateReceivedFrom());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "docDateReceivedTo", searchQuery.getDocDateReceivedTo());
		
		//TWX 27 Mar 2006 new field
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromCD", searchQuery.getFromCreateDate());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromCDT", searchQuery.getFromCreateDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toCD", searchQuery.getToCreateDate());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toCDT", searchQuery.getToCreateDateTime());
		
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromSDT", searchQuery.getFromSentDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toSDT", searchQuery.getToSentDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromRCT", searchQuery.getFromReceivedDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toRCT", searchQuery.getToReceivedDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "fromDDT", searchQuery.getFromDocDateTime());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "toDDT", searchQuery.getToDocDateTime());
		
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "folder", searchQuery.getFolder());
		refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "usrTrackID", searchQuery.getUserTrackingID());
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
  
  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
    listOptions.setAllowsSorting(true);
    
    listOptions.setAllowsEdit(false);
    listOptions.setAllowsSelection(false);
    return listOptions;
  }
  
  protected void processPipeline(ActionContext actionContext,
                                 RenderingContext rContext,
                                 IRenderingPipeline rPipe)
    throws GTClientException
  {
  	IGTSearchEsDocDocumentEntity entity = getSearchEsDoc(actionContext);
		EsDocSearchQuery searchQuery = Util.getEsDocSearchQuery(entity);
  	EsDocSearchRenderer renderer = new EsDocSearchRenderer(rContext, searchQuery,
  			                                                   getTimeZone(actionContext));
    rPipe.addRenderer(renderer);
    super.processPipeline(actionContext, rContext, rPipe);
  }
}

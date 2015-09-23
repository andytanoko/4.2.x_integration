/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGetEntityListAction.java
 * (Copy of GetEntityListAction.java which is to be phased out).
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 18 2002    Neo Sok Lay         abstraction in super class.
 * Oct 16 2003    Neo Sok Lay         Refactor to allow sub-class to override
 *                                    as small unit as necessary.
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.cursor.ICursorHome;
import com.gridnode.pdip.framework.db.cursor.ICursorObj;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import javax.ejb.Handle;

import java.util.Collection;

/**
 * This is an abstract GetList action class that provides the the generic
 * cursor handling methods for the concrete GetList actions.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 2.0
 */
public abstract class AbstractGetEntityListAction
  extends AbstractGridTalkAction
{
  private static long _lastListID;

  // *************** AbstractGridTalkAction methods ***************************

  /**
   * Construct an error event response object.
   *
   * @param event The event that was being performed but encountered error.
   * @param ex The exception that occurred.
   * @return The constructed event response object.
   *
   * @since 2.0
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    Object[] params = null;

    /**@todo TBD: parameters to be available for the message */
    params = new Object[] { event };

    return constructEventResponse(
      IErrorCode.FIND_ENTITY_LIST_ERROR,
      params,
      ex);
  }

  /**
   * Starting actual processing of an event. This is only called when the
   * semantic validation of the event passes.
   *
   * @param event The event to be processed.
   * @return The event response upon successful processing of the event.
   * @exception Throwable Any error that may occur during processing.
   * @since 2.0
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    return constructEventResponse(getNextList((GetEntityListEvent) event));
  }

  // ************************ Own methods **************************************

  /**
   * Get the prefix for the list ID.
   */
  protected abstract String getListIDPrefix();

  /**
   * Retrieves a list of entities based on a filtering condition.
   *
   * @param filter the filtering condition.
   */
  protected abstract Collection retrieveEntityList(IDataFilter filter)
    throws Exception;

  /**
   * Converts the list of entities to Map objects.
   *
   * @param entityList List of entities.
   * @return A Collection of Map objects corresponding to each entity.
   */
  protected abstract Collection convertToMapObjects(Collection entityList);

  /**
   * Create a New CursorBean for a list of entities.
   *
   * @param entityList The list of entities to be in the cursor.
   * @param listID ID of the CursorBean.
   * @return The proxy interface for the newly created CursorBean.
   *
   * @exception ServiceLookupException Unable to locate the CursorBean home.
   * @exception javax.ejb.CreateException Unable to create the CursorBean.
   */
  protected ICursorObj createNewCursor(Collection entityList, String listID)
    throws Exception
  {
    ICursorHome home =
      (ICursorHome) ServiceLocator.instance(
        ServiceLocator.CLIENT_CONTEXT).getHome(
        ICursorHome.class.getName(),
        ICursorHome.class);
    ICursorObj cursor = home.create(entityList);
    this.sm.setAttribute(getListIDPrefix() + listID, cursor.getHandle());

    return cursor;
  }

  /**
   * Retrieve the CursorBean based on the specified listID.
   *
   * @param listID ID of the CursorBean.
   * @return The proxy interface of the retrieved CursorBean.
   *
   * @exception FindEntityException Unable to retrieve the CursorBean.
   */
  protected ICursorObj retrieveCursor(String listID) throws Exception
  {
    //retrieve from cursor
    Handle cursorHandle =
      (Handle) this.sm.getAttribute(getListIDPrefix() + listID);
    if (cursorHandle == null)
      throw new FindEntityException(
        "Unable to retrieve Cursor List: " + listID);

    return (ICursorObj) cursorHandle.getEJBObject();
  }

  /**
   * Get the next list of entities to be returned, as specified by the
   * getEvent.
   *
   * @param getEvent The GetEntityListEvent which indicates the parameters for
   * returning the next list of entities.
   * @return The event response data containing the next list of entities.
   *
   * @exception FindEntityException Unable to retrieve the CursorBean.
   * @exception ServiceLookupException Unable to locate the CursorBean home.
   * @exception javax.ejb.CreateException Unable to create the CursorBean.
   */
  protected EntityListResponseData getNextList(GetEntityListEvent getEvent)
    throws Exception
  {
    String listID = getEvent.getListID();
    ICursorObj cursorObj = null;
    Collection entityList = null;

    if (listID == null)
      listID = createCursor(getEvent.getFilter());

    cursorObj = retrieveCursor(listID);
    /*031016NSL      
    if (listID != null)
    {
      cursorObj = retrieveCursor(listID);
    }
    else
    {
      // retrieve from db
      entityList = retrieveEntityList(getEvent.getFilter());
    
      // create cursor for empty result set?
    
      //new list ID
      listID = getNewListID();
      cursorObj = createNewCursor(entityList, listID);
    }
    */

    int count = getEvent.getMaxRows();
    if (count == 0)
      count = cursorObj.size();

    int start = getEvent.getStartRow();
    if (start >= cursorObj.size())
      start = 0;

    entityList = getEntityListFromCursor(cursorObj, start, count);
    /*031016NSL
    entityList = cursorObj.get(start, count);
    */
    return createResponseData(
      entityList,
      start,
      cursorObj.size() - start - entityList.size(),
      listID);
  }

  /**
   * Create a cursor based on the filter condition for the cursor list data.
   * 
   * @param filter The filter condition to obtain the cursor list data.
   * @return List ID to retrieve the cursor.
   * @throws Exception Error in obtaining the cursor list data or creating
   * a new cursor.
   */
  protected String createCursor(IDataFilter filter) throws Exception
  {
    // retrieve from db
    Collection entityList = getListDataForCursor(filter);

    // create cursor for empty result set?

    //new list ID
    String listID = getNewListID();
    createNewCursor(entityList, listID);
    return listID;
  }

  /**
   * Get the list data for putting on a cursor.
   * 
   * @param filter The filter condition to obtain the list data.
   * @return Collection of objects to be placed on the cursor.
   * @throws Exception Error obtaining the list data.
   */
  protected Collection getListDataForCursor(IDataFilter filter)
    throws Exception
  {
    return retrieveEntityList(filter);
  }

  /**
   * Get a list of Entities from a cursor in a particular window.
   * 
   * @param cursor The cursor to get list data from.
   * @param start The starting index of the cursor window.
   * @param count Number of objects from <code>start</code> to get.
   * @return Collection of Entity objects corresponding to the
   * list data obtained from the cursor window.
   * @throws Exception
   */
  protected Collection getEntityListFromCursor(
    ICursorObj cursor,
    int start,
    int count)
    throws Exception
  {
    return cursor.get(start, count);
  }

  /**
   * Get a new List ID.
   */
  protected String getNewListID()
  {
    //prevent overflow
    if (_lastListID == Long.MAX_VALUE)
      _lastListID = 1;
    else
      _lastListID++;

    return String.valueOf(_lastListID);
  }

  /**
   * Create the response data for list of "entities" as Map objects to be returned
   * to the client.
   *
   * @param entityList List of entities.
   * @param startRow Starting row of the first entity in the return list, with
   * respect to the entire list in the cursor.
   * @param rowsRemaining Number of rows that remain in the cursor list after
   * this list of entities is fetched.
   * @param listID The ListID of the cursor. This is for use in next action
   * to retrieve the next list of entities from the same cursor.
   */
  protected EntityListResponseData createResponseData(
    Collection entityList,
    int startRow,
    int rowsRemaining,
    String listID)
  {
    return new EntityListResponseData(
      convertToMapObjects(entityList),
      startRow,
      rowsRemaining,
      listID);
  }
}
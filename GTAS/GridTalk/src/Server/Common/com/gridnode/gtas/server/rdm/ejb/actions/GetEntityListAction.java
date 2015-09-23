/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEntityListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 16 2002    Neo Sok Lay         remove Validate current state (already
 *                                    auto done in super class).
 *                                    deprecate perform(GetEntityListEvent,validate)
 * Sep 07 2005    Neo Sok Lay         Deprecate the class. Replaced by AbstractGetEntityListAction                                   
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import java.util.Collection;

import javax.ejb.Handle;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.cursor.ICursorHome;
import com.gridnode.pdip.framework.db.cursor.ICursorObj;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This is an abstract GetList action class that provides the the generic
 * cursor handling methods for the concrete GetList actions.
 *
 * @author Neo Sok Lay
 * @deprecated Replaced by AbstractGetEntityListAction
 * @version 2.0
 * @since 2.0
 */
public abstract class GetEntityListAction extends GridTalkEJBAction
{
  /**
   * Get the prefix for the list ID.
   */
  protected abstract String getListIDPrefix();

  /**
   * Get a new List ID.
   */
  protected abstract String getNewListID();

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
   * Log the perform event error.
   */
  protected abstract void logEventError(Exception ex);

  /**
   * Logs the start of the event perform.
   */
  protected abstract void logEventStart();

  /**
   * Logs the end of the event perform.
   */
  protected abstract void logEventEnd();

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
  private ICursorObj createNewCursor(Collection entityList, String listID)
    throws Exception
  {
    ICursorHome home = (ICursorHome)ServiceLocator.instance(
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
  private ICursorObj retrieveCursor(String listID)
    throws Exception
  {
    //retrieve from cursor
    Handle cursorHandle = (Handle)this.sm.getAttribute(
                          getListIDPrefix() + listID);
    if (cursorHandle == null)
      throw new FindEntityException("Unable to retrieve Cursor List: "
        +listID);

    return (ICursorObj)cursorHandle.getEJBObject();
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

    int count = getEvent.getMaxRows();
    if (count == 0)
      count = cursorObj.size();

    int start = getEvent.getStartRow();
    if (start >= cursorObj.size())
      start = 0;

    entityList = cursorObj.get(start, count);

    return createResponseData(entityList, start,
             cursorObj.size()-start-entityList.size(),
             listID);
  }

  /**
   * Constructs event response for return data.
   *
   * @param data The data to return.
   * @return The event response containing the returned data.
   */
  protected IEventResponse constructEventResponse(EntityListResponseData data)
  {
    return new BasicEventResponse(
               IErrorCode.NO_ERROR,
               null,
               data);
  }

  /**
   * Constructs event response for error.
   *
   * @param event The GetEntityListEvent.
   * @param ex The Exception that occurs.
   * @param actionName Class name of the Action class.
   * @return The event response containing the exception.
   */
  protected IEventResponse constructEventResponse(
    GetEntityListEvent event, TypedException ex)
  {
    BasicEventResponse response = null;
    Object[] params = null;

    params = new Object[]
             {
               event
             };

    response = new BasicEventResponse(
                   IErrorCode.FIND_ENTITY_LIST_ERROR,
                   params,
                   ex.getType(),
                   ex.getLocalizedMessage(),
                   ex.getStackTraceString());

    logEventError(ex);
    return response;
  }

  /**
   * Perform the GetEntityListEvent.
   *
   * @param getEvent The GetEntityListEvent
   * @param validate Whether to validate the current state of the client.
   * @return The EventResponse for the result of the event performed.
   * @exception Unexpected input event, e.g. not of correct format.
   * @deprecated Replaced by perform(GetEntityListEvent)
   */
  public IEventResponse perform(GetEntityListEvent getEvent, boolean validate)
    throws EventException
  {
    return perform(getEvent);
  }

  /**
   * Perform the GetEntityListEvent.
   *
   * @param getEvent The GetEntityListEvent
   * @param validate Whether to validate the current state of the client.
   * @return The EventResponse for the result of the event performed.
   * @exception Unexpected input event, e.g. not of correct format.
   */
  public IEventResponse perform(GetEntityListEvent getEvent)
    throws EventException
  {
    logEventStart();

    IEventResponse response = null;

    try
    {
      response = constructEventResponse(getNextList(getEvent));
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(getEvent, ex);
    }
    catch (Throwable ex)
    {
      response = constructEventResponse(getEvent,
                   new SystemException(ex));
    }
    finally
    {
      logEventEnd();
    }
    return response;
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
  private EntityListResponseData createResponseData(
    Collection entityList, int startRow, int rowsRemaining, String listID)
  {
    return new EntityListResponseData(
                 convertToMapObjects(entityList),
                 startRow,
                 rowsRemaining,
                 listID);
  }
}
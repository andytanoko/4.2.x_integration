/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGetEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import java.util.Map;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This class provides an abstract implementation of a Get entity type of
 * action.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class AbstractGetEntityAction extends AbstractGridTalkAction
{
  // ******************* AbstractGridTalkAction methods **********************

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    AbstractEntity retrieved = getEntity(event);
    if (retrieved == null)
      return constructEventResponse(java.util.Collections.EMPTY_MAP);

    return constructEventResponse(convertToMap(retrieved));
  }

  // ****************** Own methods **********************************

  /**
   * Return the parameters for the error message in the event response.
   *
   * @param event The event that result in error in processing.
   * @return The parameters for the error message.
   *
   * @since 2.0
   */
  protected abstract Object[] getErrorMessageParams(IEvent event);

  /**
   * Perform the retrieval of an entity based on the event data.
   *
   * @param event The event.
   * @return The retrieved entity.
   * @exception Exception Error in retrieval.
   *
   * @since 2.0
   */
  protected abstract AbstractEntity getEntity(IEvent event) throws Exception;

  /**
   * Convert an entity to a Map for returning to the action invoker.
   *
   * @param entity The entity to convert.
   * @return The Map constructed from the entity.
   *
   * @since 2.0
   */
  protected abstract Map convertToMap(AbstractEntity entity);
}
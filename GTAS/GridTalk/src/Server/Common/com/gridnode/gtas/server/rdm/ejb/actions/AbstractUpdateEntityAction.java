/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractUpdateEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 18 2002    Neo Sok Lay         Created
 * Jul 25 2002    Neo Sok Lay         Return entity in event response.
 *                                    Retrieve entity fresh.
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import java.util.Map;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This class provides an abstract implementation of a Update entity type of
 * action.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class AbstractUpdateEntityAction extends AbstractGridTalkAction
{
  // ******************* AbstractGridTalkAction methods **********************

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.UPDATE_ENTITY_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    AbstractEntity entity = updateEntity(event);

    /**@todo TBD: what to return for message */
    Object[] params = new Object[]
                      {
                        entity.getEntityName(),
                        entity.getEntityDescr(),
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params, convertToMap(entity));
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
   * Prepare the data for update.
   *
   * @param event The event for updating the entity.
   * @return The entity to be updated.
   *
   * @since 2.0
   */
  protected abstract AbstractEntity prepareUpdateData(IEvent event);

  /**
   * Update the entity in the database.
   *
   * @param entity The entity to be updated.
   * @exception Exception Error encountered during update.
   *
   * @since 2.0
   */
  protected abstract void updateEntity(AbstractEntity entity) throws Exception;

  /**
   * Retrieve an entity based on the UID.
   *
   * @param key The UID of the entity.
   * @return The retrieved entity.
   * @exception Exception Error in retrieval.
   *
   * @since 2.0
   */
  protected abstract AbstractEntity retrieveEntity(Long key) throws Exception;

  /**
   * Perform the update of an existing entity record in the database base on
   * data from an event.
   *
   * @param event The event used to update the entity.
   * @return The updated entity.
   * @exception Exception Error encountered during the update.
   *
   * @since 2.0
   */
  protected AbstractEntity updateEntity(IEvent event) throws Exception
  {
    AbstractEntity entity = prepareUpdateData(event);
    updateEntity(entity);

    return retrieveEntity((Long)entity.getKey());
  }

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
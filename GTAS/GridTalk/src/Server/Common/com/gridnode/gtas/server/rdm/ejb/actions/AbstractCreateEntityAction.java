/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractCreateEntityAction.java
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
 * This class provides an abstract implementation of a Create entity type of
 * action.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class AbstractCreateEntityAction extends AbstractGridTalkAction
{
  // ******************* AbstractGridTalkAction methods **********************

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.CREATE_ENTITY_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    AbstractEntity entity = createEntity(event);

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
   * Prepare the data for creation.
   *
   * @param event The event for creating the entity.
   * @return The new entity to be created.
   *
   * @since 2.0
   */
  protected abstract AbstractEntity prepareCreationData(IEvent event);

  /**
   * Create the entity in the database.
   *
   * @param entity The entity to be created.
   * @return The UID of the created entity.
   * @exception Exception Error encountered during creation.
   *
   * @since 2.0
   */
  protected abstract Long createEntity(AbstractEntity entity) throws Exception;

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
   * Convert an entity to a Map for returning to the action invoker.
   *
   * @param entity The entity to convert.
   * @return The Map constructed from the entity.
   *
   * @since 2.0 I4
   */
  protected abstract Map convertToMap(AbstractEntity entity);

  /**
   * Perform the creation of a new entity record in the database base on
   * data from an event.
   *
   * @param event The event used to create the new entity.
   * @return The created entity.
   * @exception Exception Error encountered during the creation.
   *
   * @since 2.0
   */
  protected AbstractEntity createEntity(IEvent event) throws Exception
  {
    AbstractEntity entity = prepareCreationData(event);
    Long uID = createEntity(entity);

    return retrieveEntity(uID);
  }

}
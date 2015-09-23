/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractDeleteEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 18 2002    Neo Sok Lay         Created
 * Jul 14 2003    Neo Sok Lay         Handle multiple entity deletion support.
 *                                    Entity deletion check modifications:
 *                                    - checkCanDelete()
 *                                    - checkDependencies()
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.dependency.DependencyCheckEngine;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EntityActionResponseData;
import com.gridnode.pdip.framework.rpf.event.EntityListActionResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class provides an abstract implementation of a Delete entity type of
 * action.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public abstract class AbstractDeleteEntityAction extends AbstractGridTalkAction
{
  private static final boolean IS_DEBUG = false;
  private static final String UNKNOWN_DESC = "(unknown)";
  private Collection _existUids;
  private Collection _nonExistUids;
  private DeleteEntityListEvent _currentEvent;

  // ******************* AbstractGridTalkAction methods **********************

  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    return constructEventResponse(IErrorCode.DELETE_ENTITY_LIST_ERROR,
    //getErrorMessageParams(event),
    new Object[0], ex);
  }

  protected final IEventResponse doProcess(IEvent event)
    throws java.lang.Throwable
  {
    /*030714NSL
    deleteEntity(event);
    
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             getSuccessMessageParams(event));
    */
    return doDelete((DeleteEntityListEvent) event);
  }

  /**
   * Perform the deletion of entities,
   * 
   * @param event The Delete event.
   * @return Event response after performing the deletion.
   * @since GT 2.2 I1
   */
  protected synchronized final IEventResponse doDelete(DeleteEntityListEvent event)
    throws java.lang.Throwable
  {
    _currentEvent = event;
    EntityListActionResponseData listResponse =
      new EntityListActionResponseData(event.getEntityType(), event.getKeyId());

    EntityActionResponseData response = null;
    if (!_existUids.isEmpty())
    {
      ArrayList entities =
        new ArrayList(getEntityList(createFilter(event.getKeyId(), _existUids)));

//      EntityOrderComparator comparator = new EntityOrderComparator(
//                                           event.getKeyId(), event.getUids());
//      Collections.sort(entities, comparator);
    
      IEntityDescriptorListSet dependents;
      IEntity entity;
      for (Iterator i = entities.iterator(); i.hasNext();)
      {
        entity = (IEntity) i.next();
        try
        {
          // Check if Delete is enable for the entity
          if (!checkCanDelete(entity))
          {
            response = constructDeleteNotEnabledResponseData(entity);
if (IS_DEBUG) logEventStatus(event, "DeleteNotEnabled response "+response);
            continue;
          }
          else
          {
            // check for any dependent entities on the entity
            dependents = checkDependencies(entity);
            if (!dependents.isEmpty())
            {
              response =
                constructDependenciesExistResponseData(entity, dependents);
if (IS_DEBUG) logEventStatus(event, "Dependencies Exist response "+response);
              continue;
            }
          }

          // delete the entity      
          deleteEntity(entity, event.getKeyId());

          // success
          response =
            EntityActionResponseData.newSuccessResponseData(
              entity.getKey(),
              entity.getEntityDescr());
        }
        catch (TypedException ex)
        {
          response = constructUnexpectedErrorResponseData(entity, ex);
        }
        catch (Throwable t)
        {
          response = constructUnexpectedErrorResponseData(entity, t);
        }
        finally
        {
          listResponse.addEntityActionResponseData(response);
        }
      }
    }

    // construct EntityActionResponseData for nonExistUids
    Long uid;
    for (Iterator i = _nonExistUids.iterator(); i.hasNext();)
    {
      uid = (Long) i.next();
      response = constructRecordMissingResponseData(uid);
      listResponse.addEntityActionResponseData(response);
    }
if (IS_DEBUG) logEventStatus(event, "ListResponseData = "+listResponse);
    _currentEvent = null;
    return constructEventResponse(listResponse);
  }

  /**
   * Determine the valid uids and invalid uids.
   * 
   * @since GT 2.2 I1
   */
  protected final void doSemanticValidation(IEvent event) throws Exception
  {
    DeleteEntityListEvent delEvent = (DeleteEntityListEvent) event;

    Collection uids = delEvent.getUids();

    IDataFilter filter = createFilter(delEvent.getKeyId(), uids);

    _existUids = getEntityKeys(filter);

    Collection badKeys = new ArrayList();
    if (!_existUids.containsAll(uids))
    {
      badKeys.addAll(uids);
      badKeys.removeAll(_existUids);
    }

    _nonExistUids = badKeys;
  }

  // ****************** Own methods **********************************

  /**
   * Create a Domain filter on the UIDs.
   * 
   * @param keyId The FieldID of the key field (UID)
   * @param uids Collection of UIDs.
   * @return The IDataFilter created.
   * @since GT 2.2 I1
   */
  protected final IDataFilter createFilter(Number keyId, Collection uids)
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, keyId, uids, false);

    return filter;
  }

  /**
   * Construct EventResponse based on the specified EntityListActionResponseData.
   * 
   * @param returnData The ReturnData of the EventResponse.
   * @return Constructed EventResponse.
   * @since GT 2.2 I1
   */
  protected final IEventResponse constructEventResponse(EntityListActionResponseData returnData)
  {
    if (returnData.isAllSuccess())
    {
      return super.constructEventResponse(returnData);
    }
    else
    {
      return new BasicEventResponse(
                  IErrorCode.DELETE_ENTITY_LIST_ERROR,
                  (short)returnData.getOverallErrorType(),
                  returnData); 

    }
  }

  /**
   * Construct an EntityActionResponseData for successful results.
   * 
   * @param entity The Entity that was deleted successfully.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructSucessResponseData(IEntity entity)
  {
    return EntityActionResponseData.newSuccessResponseData(
      entity.getKey(),
      entity.getEntityDescr());
  }

  /**
   * Construct an EntityActionResponseData for deletion not enabled error.
   * 
   * @param entity The Entity that was not enabled for deletion.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructDeleteNotEnabledResponseData(IEntity entity)
  {
    return EntityActionResponseData
      .newFailResponseData(
        entity.getKey(),
        entity.getEntityDescr(),
        ApplicationException.APPLICATION,
        IErrorCode.DELETE_NOT_ENABLED_ERROR,
        "");
  }

  /**
   * Construct an EntityActionResponseData for dependency exist error.
   * 
   * @param entity The Entity that has dependent entities.
   * @param dependents The dependent entites.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructDependenciesExistResponseData(
    IEntity entity,
    IEntityDescriptorListSet dependents)
  {
    return EntityActionResponseData.newFailResponseData(
      entity.getKey(),
      entity.getEntityDescr(),
      ApplicationException.APPLICATION,
      IErrorCode.DEPENDENCIES_EXIST_ERROR,
      dependents);
  }

  /**
   * Construct an EntityActionResponseData for record missing error.
   * 
   * @param uid UID of the entity that was missing for deletion.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructRecordMissingResponseData(Long uid)
  {
    return EntityActionResponseData.newFailResponseData(
      uid,
      UNKNOWN_DESC,
      ApplicationException.APPLICATION,
      IErrorCode.DELETE_RECORD_NOT_EXISTS_ERROR,
      "");
  }

  /**
   * Construct an EntityActionResponseData for unexpected error.
   * 
   * @param entity The Entity that encounters error during deletion.
   * @param ex The exception that occurred.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructUnexpectedErrorResponseData(
    IEntity entity,
    TypedException ex)
  {
    return EntityActionResponseData
      .newFailResponseData(
        entity.getKey(),
        entity.getEntityDescr(),
        SystemException.SYSTEM,
        IErrorCode.UNEXPECTED_ERROR,
        ex.getStackTraceString());
  }

  /**
   * Construct an EntityActionResponseData for unexpected error.
   * 
   * @param entity The Entity that encounters error during deletion.
   * @param t The error that occurred.
   * @return The constructed EntityActionResponseData.
   * @since GT 2.2 I1
   */
  protected static EntityActionResponseData constructUnexpectedErrorResponseData(
    IEntity entity,
    Throwable t)
  {
    return constructUnexpectedErrorResponseData(entity, new SystemException(t));
  }

  /**
   * Get the current DeleteEntityListEvent that is processed by this action instance.
   * 
   * @return The current DeleteEntityListEvent being processed, or <b>null</b> if none is
   * being processed.
   * @since GT 2.2 I1
   */
  protected final DeleteEntityListEvent getCurrentEvent()
  {
    return _currentEvent;
  }

  /**
   * Perform the deletion of the specified entity.
   * 
   * @param entity The entity to be deleted.
   * @param keyId The field id of the key to be used for deletion.
   * @since GT 2.2 I1
   */
  protected abstract void deleteEntity(IEntity entity, Number keyId)
    throws Exception;

  /**
   * Get the keys of the entities that satisfy the specified filter.
   * 
   * @param filter The filter condition.
   * @return Collection of the keys retrieved.
   * @since GT 2.2 I1
   */
  protected abstract Collection getEntityKeys(IDataFilter filter)
    throws Exception;

  /**
   * Get the entities that satisfy the specified filter.
   * 
   * @param filter The filter condition.
   * @return Collection of IEntities retrieved.
   * @since GT 2.2 I1
   */
  protected abstract Collection getEntityList(IDataFilter filter)
    throws Exception;

  /**
   * Check whether the specified entity has deletion enabled.
   * 
   * @param entity The entity to be checked.
   * @return <b>true</b> if deletion is enabled for the entity, or <b>false</b> otherwise.
   * @since GT 2.2 I1
   */
  protected abstract boolean checkCanDelete(IEntity entity);

  /**
   * Check for entity dependencies on the specified entity.
   * 
   * @param entity The entity to be checked.
   * @return IEntityDescriptorListSet of IEntityDescriptorList(s) of EntityDescriptor(s) for the
   * entities that are dependent on the specified entity.
   * @since GT 2.2 I1
   */
  private IEntityDescriptorListSet checkDependencies(IEntity entity)
    throws Throwable
  {
    logEventStatus(_currentEvent, "Checking dependencies for entity: "+entity.getEntityDescr());
    IEntityDescriptorListSet set =
      DependencyCheckEngine.getInstance().getDependencies(entity);

    return set;
  }

}
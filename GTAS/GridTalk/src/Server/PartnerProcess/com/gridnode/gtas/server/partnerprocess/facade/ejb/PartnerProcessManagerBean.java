/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerProcessManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Nov 21 2002    Neo Sok Lay         Add services for managing ProcessMapping.
 * Jan 10 2003    Neo Sok Lay         Add services for managing BizCertMapping.
 * Jul 15 2003    Neo Sok Lay         Add methods:
 *                                    findBizCertMappingKeysByFilter(IDataFilter),
 *                                    findProcessMappingKeysByFilter(IDataFilter)
 * Oct 20 2005    Neo Sok Lay         Change findTriggers(String,String,String,String,Integer)
 *                                    - remove throwing of ApplicationException
 * Feb 09 2007    Neo Sok Lay         findTriggers(): to handle NULL value in database (e.g. Oracle) for 
 *                                    empty string.
 * Aug 01 2008	  Wong Yee Wah		 #38   Modified method: findBizCertMappingByFilter()                                                                      
 */
package com.gridnode.gtas.server.partnerprocess.facade.ejb;

import com.gridnode.gtas.server.partnerprocess.helpers.BizCertMappingEntityHandler;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.gtas.server.partnerprocess.helpers.ProcessMappingEntityHandler;
import com.gridnode.gtas.server.partnerprocess.entities.ejb.ITriggerLocalObj;
import com.gridnode.gtas.server.partnerprocess.helpers.TriggerEntityHandler;
import com.gridnode.gtas.server.partnerprocess.helpers.Logger;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.util.Collection;

public class PartnerProcessManagerBean
       implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2307699550171844575L;
	transient private SessionContext _sessionCtx = null;
 
  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IPartnerProcessManagerObj

  // ********************* Methods for Trigger

  /**
   * Create a new Trigger.
   *
   * @param trigger The Trigger entity.
   */
  public Long createTrigger(Trigger trigger)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[TriggerManagerBean.createTrigger] Enter");

    try
    {
      ITriggerLocalObj obj =
      (ITriggerLocalObj)getTriggerEntityHandler().create(trigger);

      Logger.log("[TriggerManagerBean.createTrigger] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[TriggerManagerBean.createTrigger] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[TriggerManagerBean.createTrigger] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.createTrigger] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.createTrigger(Trigger) Error ",
        ex);
    }
  }

  /**
   * Update a Trigger
   *
   * @param trigger The Trigger entity with changes.
   */
  public void updateTrigger(Trigger trigger)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[TriggerManagerBean.updateTrigger] Enter");

    try
    {
      getTriggerEntityHandler().update(trigger);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[TriggerManagerBean.updateTrigger] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.updateTrigger] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.updateTrigger(Trigger) Error ",
        ex);
    }

    Logger.log("[TriggerManagerBean.updateTrigger] Exit");
  }

  /**
   * Delete a Trigger.
   *
   * @param triggerUId The UID of the Trigger to delete.
   */
  public void deleteTrigger(Long triggerUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[TriggerManagerBean.deleteTrigger] Enter");

    try
    {
      getTriggerEntityHandler().remove(triggerUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[TriggerManagerBean.deleteTrigger] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[TriggerManagerBean.deleteTrigger] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.deleteTrigger] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.deleteTrigger(triggerUId) Error ",
        ex);
    }

    Logger.log("[TriggerManagerBean.deleteTrigger] Exit");
  }

  /**
   * Find a Trigger using the Trigger UID.
   *
   * @param triggerUId The UID of the Trigger to find.
   * @return The Trigger found, or <B>null</B> if none exists with that
   * UID.
   */
  public Trigger findTrigger(Long triggerUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[TriggerManagerBean.findTrigger] UID: "+
      triggerUid);

    Trigger trigger = null;

    try
    {
      trigger =
        (Trigger)getTriggerEntityHandler().
          getEntityByKeyForReadOnly(triggerUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[TriggerManagerBean.findTrigger] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[TriggerManagerBean.findTrigger] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.findTrigger] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.findTrigger(triggerUId) Error ",
        ex);
    }

    return trigger;
  }

  /**
   * Find a number of Trigger that is under the same level.
   *
   * @param triggerLevel The level of the triggers to find.
   * @return a Collection of Trigger found, or empty collection if none
   * exists.
   */
  public Collection findTriggers(Integer triggerLevel)
    throws FindEntityException, SystemException
  {
    Logger.log( "[TriggerManagerBean.findTriggers] Trigger Level: "+
      triggerLevel);

    Collection triggers = null;
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        Trigger.TRIGGER_LEVEL,
        filter.getEqualOperator(),
        triggerLevel,
        false);

      triggers =
        getTriggerEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.findTriggers(filter) Error ",
        ex);
    }

    return triggers;
  }

  /**
   * Find a number of Trigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Trigger found, or empty collection if none
   * exists.
   */
  public Collection findTriggers(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[TriggerManagerBean.findTriggers] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection triggers = null;
    try
    {
      triggers =
        getTriggerEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggers] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.findTriggers(filter) Error ",
        ex);
    }

    return triggers;
  }

  /**
   * Find a number of Trigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Trigger found, or empty collection if
   * none exists.
   */
  public Collection findTriggersKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[TriggerManagerBean.findTriggersKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection triggersKeys = null;
    try
    {
      triggersKeys =
        getTriggerEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggersKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggersKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.findTriggersKeys] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.findTriggersKeys(filter) Error ",
        ex);
    }

    return triggersKeys;
  }

  /**
   * Find the triggers based on the given document type, partner type, partner
   * group and partner id.
   *
   * @param docType The document type to trigger.
   * @param partnerType The partner type to trigger.
   * @param partnerGroup The partner group to trigger.
   * @param partnerId The partner id to trigger.
   * @param triggerType The type of trigger.
   * @return a Collecton containing the Trigger found.
   */
  public Collection findTriggers(String docType,
                              String partnerType,
                              String partnerGroup,
                              String partnerId,
                              Integer triggerType)
    throws FindEntityException, SystemException
  {
    Logger.log("[TriggerManagerBean.findPartnerFunctionToTrigger] start");

    Collection triggers = null;
    try
    {
      if (docType == null) docType = "";
      if (partnerType == null) partnerType = "";
      if (partnerGroup == null) partnerGroup = "";
      if (partnerId == null) partnerId = "";

      IDataFilter filter = new DataFilterImpl();
      /*NSL20070209
      filter.addSingleFilter(null, Trigger.DOC_TYPE,
        filter.getEqualOperator(), docType, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.PARTNER_TYPE,
        filter.getEqualOperator(), partnerType, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.PARTNER_GROUP,
        filter.getEqualOperator(), partnerGroup, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.PARTNER_ID,
        filter.getEqualOperator(), partnerId, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_TYPE,
        filter.getEqualOperator(), triggerType, false);
      */
      filter.addSingleFilter(null, Trigger.TRIGGER_TYPE,
                             filter.getEqualOperator(), triggerType, false);
      
      IDataFilter subFilter = new DataFilterImpl(getCriteriaFilter(Trigger.DOC_TYPE, docType), 
                                                 filter.getAndConnector(), 
                                                 getCriteriaFilter(Trigger.PARTNER_TYPE, partnerType));
      IDataFilter subFilter2 = new DataFilterImpl(getCriteriaFilter(Trigger.PARTNER_GROUP, partnerGroup),
                                                  filter.getAndConnector(),
                                                  getCriteriaFilter(Trigger.PARTNER_ID, partnerId));
      IDataFilter subFilter3 = new DataFilterImpl(subFilter, filter.getAndConnector(), subFilter2);
      
      filter = new DataFilterImpl(filter, filter.getAndConnector(), subFilter3);
      
      triggers = getTriggerEntityHandler().getEntityByFilterForReadOnly(filter);

      if (triggers.isEmpty())
      {
        if (!partnerId.equals(""))
        {
          triggers = findTriggers(docType, partnerType, partnerGroup, "", triggerType);
        }
        else if (!partnerGroup.equals(""))
        {
          triggers = findTriggers(docType, partnerType, "", "", triggerType);
        }
        else if (!partnerType.equals(""))
        {
          triggers = findTriggers(docType, "", "", "", triggerType);
        }
        else
        {
          triggers = findTriggers("", "", "", "", triggerType);
        }
      }
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[TriggerManagerBean.findPartnerFunctionToTrigger] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[TriggerManagerBean.findPartnerFunctionToTrigger] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[TriggerManagerBean.findPartnerFunctionToTrigger] Error ", ex);
      throw new SystemException(
        "TriggerManagerBean.findTriggers(filter) Error ",
        ex);
    }

    if (triggers.isEmpty())
    {
      throw new FindEntityException("Cannot find partner function to trigger");
    }

    Logger.log("[TriggerManagerBean.findPartnerFunctionToTrigger] end");
    return triggers;
  }

  private IDataFilter getCriteriaFilter(Number fieldId, String value)
  {
    IDataFilter filter = new DataFilterImpl();
    if (value == null || value.length() == 0)
    {
      filter.addSingleFilter(null, fieldId, filter.getEqualOperator(), "", false);
      filter.addSingleFilter(filter.getOrConnector(), fieldId, filter.getEqualOperator(), null, false);
    }
    else
    {
      filter.addSingleFilter(null, fieldId, filter.getEqualOperator(), value, false);
    }

    return filter;
  }
  
  // ******************** Methods for Process Mapping


  /**
   * Create a ProcessMapping.
   *
   * @param mapping The Process Mapping to add.
   * @return The UID of the created ProcessMapping.
   */
  public Long createProcessMapping(ProcessMapping mapping)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "createProcessMapping";
    Object[] params     = new Object[] {mapping};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long)getProcessMappingEntityHandler().createEntity(mapping).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  /**
   * Update a ProcessMapping.
   *
   * @param mapping The mapping to update.
   */
  public void updateProcessMapping(ProcessMapping mapping)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "updateProcessMapping";
    Object[] params     = new Object[] {
                            mapping};

    try
    {
      logger.logEntry(methodName, params);

      getProcessMappingEntityHandler().update(mapping);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Remove a ProcessMapping.
   *
   * @param uid The UID of the ProcessMapping to remove.
   */
  public void deleteProcessMapping(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "deletProcessMapping";
    Object[] params     = new Object[] {
                            uid};

    try
    {
      logger.logEntry(methodName, params);

      getProcessMappingEntityHandler().remove(uid);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Find a ProcessMapping.
   *
   * @param uid The UID of the ProcessMapping to find.
   * @return The ProcessMapping found, if any.
   * @throws FindEntityException No ProcessMapping with the uid found.
   */
  public ProcessMapping findProcessMappingByUID(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "findProcessMappingByUID";
    Object[] params     = new Object[] {
                            uid};

    ProcessMapping mapping = null;

    try
    {
      logger.logEntry(methodName, params);

      mapping = (ProcessMapping)getProcessMappingEntityHandler().getEntityByKeyForReadOnly(uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mapping;
  }

  /**
   * Get a number of ProcessMapping(s) using a filtering condition.
   *
   * @param filter The filtering condition of the ProcessMapping(s) to find.
   * @return A Collection of ProcessMapping(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection findProcessMappingByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "findProcessMappingByFilter";
    Object[] params     = new Object[] {
                            filter};

    Collection mappings = null;

    try
    {
      logger.logEntry(methodName, params);

      mappings = getProcessMappingEntityHandler().getEntityByFilterForReadOnly(
                   filter);//WYW 01082008
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mappings;
  }

  /**
   * Get a number of ProcessMapping(s) using a filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition of the ProcessMapping(s) to find.
   * @return A Collection of UIDs of the ProcessMapping(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection findProcessMappingKeysByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProcessMappingFacadeLogger();
    String methodName   = "findProcessMappingKeysByFilter";
    Object[] params     = new Object[] {
                            filter};

    Collection mappings = null;

    try
    {
      logger.logEntry(methodName, params);

      mappings = getProcessMappingEntityHandler().getKeyByFilterForReadOnly(
                   filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mappings;
  }

  // ******************** Methods for BizCert Mapping


  /**
   * Create a BizCertMapping.
   *
   * @param mapping The BizCert Mapping to add.
   * @return The UID of the created BizCertMapping.
   */
  public Long createBizCertMapping(BizCertMapping mapping)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "createBizCertMapping";
    Object[] params     = new Object[] {mapping};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long)getBizCertMappingEntityHandler().createEntity(mapping).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  /**
   * Update a BizCertMapping.
   *
   * @param mapping The mapping to update.
   */
  public void updateBizCertMapping(BizCertMapping mapping)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "updateBizCertMapping";
    Object[] params     = new Object[] {
                            mapping};

    try
    {
      logger.logEntry(methodName, params);

      getBizCertMappingEntityHandler().update(mapping);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Remove a BizCertMapping.
   *
   * @param uid The UID of the BizCertMapping to remove.
   */
  public void deleteBizCertMapping(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "deletBizCertMapping";
    Object[] params     = new Object[] {
                            uid};

    try
    {
      logger.logEntry(methodName, params);

      getBizCertMappingEntityHandler().remove(uid);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Find a BizCertMapping.
   *
   * @param uid The UID of the BizCertMapping to find.
   * @return The BizCertMapping found, if any.
   * @throws FindEntityException No BizCertMapping with the uid found.
   */
  public BizCertMapping findBizCertMappingByUID(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "findBizCertMappingByUID";
    Object[] params     = new Object[] {
                            uid};

    BizCertMapping mapping = null;

    try
    {
      logger.logEntry(methodName, params);

      mapping = (BizCertMapping)getBizCertMappingEntityHandler().getBizCertMappingByKey(uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mapping;
  }

  /**
   * Get a number of BizCertMapping(s) using a filtering condition.
   *
   * @param filter The filtering condition of the BizCertMapping(s) to find.
   * @return A Collection of BizCertMapping(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection findBizCertMappingByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "findBizCertMappingByFilter";
    Object[] params     = new Object[] {
                            filter};

    Collection mappings = null;

    try
    {
      logger.logEntry(methodName, params);

      mappings = getBizCertMappingEntityHandler().getBizCertMapping(
                   filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mappings;
  }

  /**
   * Get a number of BizCertMapping(s) using a filtering condition and
   * return their keys.
   *
   * @param filter The filtering condition of the BizCertMapping(s) to find.
   * @return A Collection of UIDs of the BizCertMapping(s) found, or empty collection
   * if none exists.
   */
  public Collection findBizCertMappingKeysByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getBizCertMappingFacadeLogger();
    String methodName   = "findBizCertMappingKeysByFilter";
    Object[] params     = new Object[] {
                            filter};

    Collection mappings = null;

    try
    {
      logger.logEntry(methodName, params);

      mappings = getBizCertMappingEntityHandler().getKeyByFilterForReadOnly(
                   filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return mappings;
  }

  // ********************* Methods for EntityHandler

  private TriggerEntityHandler getTriggerEntityHandler()
  {
     return TriggerEntityHandler.getInstance();
  }

  private ProcessMappingEntityHandler getProcessMappingEntityHandler()
  {
    return ProcessMappingEntityHandler.getInstance();
  }

  private BizCertMappingEntityHandler getBizCertMappingEntityHandler()
  {
    return BizCertMappingEntityHandler.getInstance();
  }

}
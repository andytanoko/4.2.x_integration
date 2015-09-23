/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizRegistryManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Add method: findBusinessEntityKey().
 * Jan 07 2003    Neo Sok Lay         findBusinessEntitiesByWhitePage() does not
 *                                    find in whitepage table.
 *                                    Refactor - use FacadeLogger.
 * Sep 03 2003    Neo Sok Lay         Implements IBizRegManager.
 *                                    Add methods:
 *                                    - submitSearchQuery()
 *                                    - retrieveSearchQuery()
 * Sep 24 2003    Neo Sok Lay         Add management for RegistryConnectInfo
 * Dec 23 2003    Neo Sok Lay         Add findBusinessEntityByDomainIdentifier().
 * Mar 03 2006    Neo Sok Lay         Use generics
 * 2008-07-17	  Teh Yu Phei		  Add markActivateBusinessEntity (Ticket 31)
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import com.gridnode.pdip.app.bizreg.exceptions.SearchRegistryException;
import com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager;
import com.gridnode.pdip.app.bizreg.helpers.BizEntityEntityHandler;
import com.gridnode.pdip.app.bizreg.helpers.Logger;
import com.gridnode.pdip.app.bizreg.helpers.RegistryConnectInfoHelper;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.app.bizreg.search.SearchRegistryHandler;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * This bean provides services to manage the Business registry.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since 2.0 I4
 */
public class BizRegistryManagerBean
  implements SessionBean, IBizRegistryManager
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6282598722991992532L;
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

  // ************************ Implementing methods in IBizRegistryManagerObj

  /**
   * Create a new BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity.
   * @return The UID of the created BusinessEntity.
   */
  public Long createBusinessEntity(BusinessEntity bizEntity)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "createBusinessEntity";
    Object[] params     = new Object[] {bizEntity};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long)getEntityHandler().createBusinessEntity(bizEntity).getKey();
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
   * Update a BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity with changes.
   */
  public void updateBusinessEntity(BusinessEntity bizEntity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "updateBusinessEntity";
    Object[] params     = new Object[] {
                            bizEntity};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().updateBusinessEntity(bizEntity);
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
   * Mark Delete a BusinessEntity. This will not physically delete the
   * BusinessEntity from the database. The State of the BusinessEntity will be
   * changed to STATE_DELETED.
   *
   * @param beUId The UID of the BusinessEntity to delete.
   */
  public void markDeleteBusinessEntity(Long beUId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "markDeleteBusinessEntity";
    Object[] params     = new Object[] {
                            beUId};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().markDeleteBusinessEntity(beUId);
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
   * Mark Delete the BusinessEntities that belong to an enterprise. This will
   * not physically delete the BusinessEntities from the database. The State
   * of the BusinessEntities will be changed to STATE_DELETED.
   *
   * @param enterpriseId ID of the enterprise whose BusinessEntities are to be
   * marked Deleted.
   */
  public void markDeleteBusinessEntities(String enterpriseId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "markDeleteBusinessEntities";
    Object[] params     = new Object[] {
                            enterpriseId};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, BusinessEntity.ENTERPRISE_ID,
        filter.getEqualOperator(), enterpriseId, false);
      filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.CAN_DELETE,
        filter.getEqualOperator(), Boolean.TRUE, false);

      getEntityHandler().markDeleteBusinesEntities(filter);
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
   * Purge all BusinessEntities that are marked deleted.
   */
  public void purgeDeletedBusinessEntities()
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "purgeDeletedBusinessEntities";
    Object[] params     = new Object[] {};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, BusinessEntity.STATE,
        filter.getEqualOperator(), new Integer(BusinessEntity.STATE_DELETED), false);

      purgeBusinessEntities(filter);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Purge all BusinessEntities of an enterprise that are marked deleted.
   *
   * @param enterpriseId ID of the enterprise.
   */
  public void purgeDeletedBusinessEntities(String enterpriseId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "purgeDeletedBusinessEntities";
    Object[] params     = new Object[] {enterpriseId};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, BusinessEntity.STATE,
        filter.getEqualOperator(), new Integer(BusinessEntity.STATE_DELETED), false);
      filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.ENTERPRISE_ID,
        filter.getEqualOperator(), enterpriseId, false);

      purgeBusinessEntities(filter);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  private void purgeBusinessEntities(DataFilterImpl filter)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "purgeDeletedBusinessEntities";
    Object[] params     = new Object[] {filter};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().removeByFilter(filter);
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

  // ********************** Finders ******************************************

  /**
   * Find a BusinessEntity using the BusinessEntity ID.
   *
   * @param enterpriseId The ID of the enterprise owning the BusinessEntity.
   * @param bizEntityID The ID of the BusinessEntity to find.
   * @return The BusinessEntity found, or <B>null</B> if none exists with that
   * name.
   */
  public BusinessEntity findBusinessEntity(String enterpriseId, String bizEntityID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntity";
    Object[] params     = new Object[] {
                            enterpriseId,
                            bizEntityID};

    BusinessEntity bizEntity = null;

    try
    {
      logger.logEntry(methodName, params);

      bizEntity = getEntityHandler().findByBizEntityId(enterpriseId, bizEntityID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return bizEntity;
  }

  /**
   * Find a BusinessEntity using the BusinessEntity ID and Enterprise ID.
   *
   * @param enterpriseId The ID of the enterprise owning the BusinessEntity.
   * @param bizEntityID The ID of the BusinessEntity to find.
   * @return The UID of the BusinessEntity found, or <B>null</B> if none
   * exists with the specified inputs.
   */
  public Long findBusinessEntityKey(String enterpriseId, String bizEntityID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntityKey";
    Object[] params     = new Object[] {
                            enterpriseId,
                            bizEntityID};

    Long key = null;

    try
    {
      logger.logEntry(methodName, params);

      BusinessEntity bizEntity = findBusinessEntity(enterpriseId, bizEntityID);

      if (bizEntity != null)
        key = (Long)bizEntity.getKey();
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  /**
   * Find a BusinessEntity using the BusinessEntity UID.
   *
   * @param uID The UID of the BusinessEntity to find.
   * @return The BusinessEntity found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public BusinessEntity findBusinessEntity(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntity";
    Object[] params     = new Object[] {
                            uID};

    BusinessEntity bizEntity = null;

    try
    {
      logger.logEntry(methodName, params);

      bizEntity = (BusinessEntity)getEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return bizEntity;
  }

  /**
   * Find a number of BusinessEntities using the state.
   *
   * @param state The state of the BusinessEntities to find.
   * @return A Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntities(int state)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntities";
    Object[] params     = new Object[] {
                            String.valueOf(state)};

    Collection bizEntities = null;

    try
    {
      logger.logEntry(methodName, params);

      bizEntities = getEntityHandler().findByState(state);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return bizEntities;
  }

  /**
   * Find a number of BusinessEntities that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of BusinessEntities found, or empty collection if none
   * exists.
   * @exception FindEntityException Error in executing the finder.
   */
  public Collection<BusinessEntity> findBusinessEntities(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntities";
    Object[] params     = new Object[] {
                            filter};

    Collection bizEntities = null;

    try
    {
      logger.logEntry(methodName, params);

      bizEntities = getEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return bizEntities;
  }

  /**
   * Find the keys of the BusinessEntities that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of BusinessEntities found, or empty
   * collection if none.
   * @excetpion FindBusinessEntityException Error in executing the finder.
   */
  public Collection<Long> findBusinessEntitiesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntitiesKeys";
    Object[] params     = new Object[] {
                            filter};

    Collection<Long> beKeys = null;

    try
    {
      logger.logEntry(methodName, params);

      beKeys = getEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return beKeys;
  }

  /**
   * Find a number of BusinessEntities whose Whitepage information
   * satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntitiesByWhitePage(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntitiesByWhitePage";
    Object[] params     = new Object[] {
                            filter};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      results = getEntityHandler().findByWhitepage(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    /*030107NSL - should retrieve base on whitepage table first!!
    try
    {
      Logger.log( "[BizRegistryManagerBean.findBusinessEntitiesByWhitePage] filter: "+
        (filter==null?"null":filter.getFilterExpr()));

      results = getEntityHandler().getEntityByFilterForReadOnly(filter);

      //two-stage retrieval
      if (!results.isEmpty())
      {
        Collection wpKeys = new ArrayList();
        for (Iterator i=results.iterator(); i.hasNext(); )
        {
          WhitePage whitePage = (WhitePage)i.next();
          wpKeys.add(whitePage.getBeUId());
        }

        DataFilterImpl beFilter = new DataFilterImpl();
        beFilter.addDomainFilter(null, BusinessEntity.UID, wpKeys, false);

        results = getEntityHandler().getEntityByFilterForReadOnly(beFilter);
      }
    }
    catch (ApplicationException ex)
    {
      Logger.err("[BizRegistryManagerBean.findBusinessEntitiesByWhitePage] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[BizRegistryManagerBean.findBusinessEntitiesByWhitePage] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[BizRegistryManagerBean.findBusinessEntitiesByWhitePage] Error ", ex);
      throw new SystemException(
        "BizRegistryManagerBean.findBusinessEntitiesByWhitePage(filter) Error ",
        ex);
    }
    */

    return results;
  }

  /**
   * Find a number of BusinessEntities that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @param excludeStateSet A BitSet containing a bit corresponding to a state, such
   * that if the bit is true, the BusinessEntities of that state will be excluded from
   * the result set.
   * <P>For example, to exclude those BusinessEntities that are marked deleted,<P>
   * <PRE>
   * BitSet stateSet = new BitSet();
   * stateSet.set(IBusinessEntity.STATE_DELETED);
   * </PRE>
   * @return a Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntities(IDataFilter filter, BitSet excludeStateSet)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntities";
    Object[] params     = new Object[] {
                            filter, excludeStateSet};

    Collection<BusinessEntity> bizEntities = new ArrayList<BusinessEntity>();

    try
    {
      logger.logEntry(methodName, params);

      Collection initBEs = getEntityHandler().getEntityByFilterForReadOnly(filter);

      for (Iterator i=initBEs.iterator(); i.hasNext(); )
      {
        BusinessEntity be = (BusinessEntity)i.next();
        if (!excludeStateSet.get(be.getState()))
        {
          bizEntities.add(be);
        }
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return bizEntities;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#findBusinessEntityByDomainIdentifier(java.lang.String, java.lang.String)
   */
  public BusinessEntity findBusinessEntityByDomainIdentifier(
    String type,
    String value)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findBusinessEntityByDomainIdentifier";
    Object[] params     = new Object[] {
                            type, value};

    BusinessEntity found = null;

    try
    {
      logger.logEntry(methodName, params);

      DomainIdentifier identifier = getEntityHandler().findDomainIdentifier(type, value);
      if (identifier != null)
      {
        found = findBusinessEntity(identifier.getBeUid());
        // don't return if the BusinessEntity is marked deleted.
        if (found.getState() == BusinessEntity.STATE_DELETED)
          found = null;
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    
    return found;
  }


  private BizEntityEntityHandler getEntityHandler()
  {
     return BizEntityEntityHandler.getInstance();
  }
  
  private RegistryConnectInfoHelper getRegistryConnectInfoEntityHandler()
  {
     return RegistryConnectInfoHelper.getInstance();
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#retrieveSearchQuery(java.lang.Long)
   */
  public SearchRegistryQuery retrieveSearchQuery(Long searchId)
    throws SearchRegistryException, SystemException
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "retrieveSearchQuery";
    Object[] params     = new Object[] {searchId};

    SearchRegistryQuery query = null;
    try
    {
      logger.logEntry(methodName, params);

      query = SearchRegistryHandler.getInstance().retrieveSearchQuery(searchId);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SearchRegistryException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return query;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#submitSearchQuery(com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria)
   */
  public Long submitSearchQuery(SearchRegistryCriteria searchCriteria)
    throws SearchRegistryException, SystemException
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "submitSearchQuery";
    Object[] params     = new Object[] {searchCriteria};

    Long searchId = null;
    try
    {
      logger.logEntry(methodName, params);

      searchId = SearchRegistryHandler.getInstance().submitSearchQuery(searchCriteria);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SearchRegistryException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return searchId;  
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#createRegistryConnectInfo(com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo)
   */
  public Long createRegistryConnectInfo(RegistryConnectInfo connInfo)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "createRegistryConnectInfo";
    Object[] params     = new Object[] {connInfo};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long)getRegistryConnectInfoEntityHandler().createEntity(connInfo).getKey();
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
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#deleteRegistryConnectInfo(java.lang.Long)
   */
  public void deleteRegistryConnectInfo(Long uId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "deleteRegistryConnectInfo";
    Object[] params     = new Object[] {
                            uId};

    try
    {
      logger.logEntry(methodName, params);

      getRegistryConnectInfoEntityHandler().remove(uId);
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
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#findRegistryConnectInfo(java.lang.Long)
   */
  public RegistryConnectInfo findRegistryConnectInfo(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findRegistryConnectInfo";
    Object[] params     = new Object[] {
                            uID};

    RegistryConnectInfo connInfo = null;

    try
    {
      logger.logEntry(methodName, params);

      connInfo = (RegistryConnectInfo)getRegistryConnectInfoEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return connInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#findRegistryConnectInfos(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  public Collection<RegistryConnectInfo> findRegistryConnectInfos(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findRegistryConnectInfos";
    Object[] params     = new Object[] {
                            filter};

    Collection connInfos = null;

    try
    {
      logger.logEntry(methodName, params);

      connInfos = getRegistryConnectInfoEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return connInfos;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#updateRegistryConnectInfo(com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo)
   */
  public void updateRegistryConnectInfo(RegistryConnectInfo connInfo)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "updateRegistryConnectInfo";
    Object[] params     = new Object[] {
                            connInfo};

    try
    {
      logger.logEntry(methodName, params);

      getRegistryConnectInfoEntityHandler().update(connInfo);
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
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#findRegistryConnectInfoKeys(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  public Collection<Long> findRegistryConnectInfoKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findRegistryConnectInfoKeys";
    Object[] params     = new Object[] {
                            filter};

    Collection<Long> keys = null;

    try
    {
      logger.logEntry(methodName, params);

      keys = getRegistryConnectInfoEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return keys;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager#findRegistryConnectInfoByName(java.lang.String)
   */
  public RegistryConnectInfo findRegistryConnectInfoByName(String name)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findRegistryConnectInfoByName";
    Object[] params     = new Object[] {
                            name};

    RegistryConnectInfo connInfo = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, RegistryConnectInfo.NAME, filter.getEqualOperator(), name, false);
      
      Collection results = findRegistryConnectInfos(filter);
      if (!results.isEmpty())
        connInfo = (RegistryConnectInfo)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return connInfo;
  }
  
  /**
   * Mark Activate a BusinessEntity. The State of the BusinessEntity will be
   * changed to STATE_NORMAL.
   *
   * @param beUId The UID of the BusinessEntity to activate.
   */
  public void markActivateBusinessEntity(Long beUId)
    throws SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "markActivateBusinessEntity";
    Object[] params     = new Object[] {beUId};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().markActivateBusinessEntity(beUId);
    }
    catch (Throwable t)
    {
      logger.logError("", methodName, params, "", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }
}
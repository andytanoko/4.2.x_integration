/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizEntityEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Jan 07 2003    Neo Sok Lay         Add findByWhitepage().
 * Dec 23 2003    Neo Sok Lay         Add findDomainIdentifier().
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay         Use generics 
 * 2008-07-17	  Teh Yu Phei		  Add markActivateBusinessEntity (Ticket 31)                                  
 */
package com.gridnode.pdip.app.bizreg.helpers;

import com.gridnode.pdip.app.bizreg.entities.ejb.IBizEntityLocalHome;
import com.gridnode.pdip.app.bizreg.entities.ejb.IBizEntityLocalObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the BizEntity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0
 */
public final class BizEntityEntityHandler
  extends          LocalEntityHandler
{
  private BizEntityEntityHandler()
  {
    super(BusinessEntity.ENTITY_NAME);
  }

  /**
   * Get an instance of a BizEntityEntityHandler.
   */
  public static BizEntityEntityHandler getInstance()
  {
    BizEntityEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(BusinessEntity.ENTITY_NAME, true))
    {
      handler = (BizEntityEntityHandler)EntityHandlerFactory.getHandlerFor(
                  BusinessEntity.ENTITY_NAME, true);
    }
    else
    {
      handler = new BizEntityEntityHandler();
      EntityHandlerFactory.putEntityHandler(BusinessEntity.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IBizEntityLocalHome.class.getName(),
      IBizEntityLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IBizEntityLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IBizEntityLocalObj.class;
  }

  // ********************** Own methods ******************************
  /**
   * Find the BusinessEntities with the specified state.
   *
   * @param state The state of the BusinessEntities.
   * @return a Collection of the BusinessEntities having the specified state.
   *
   * @see BusinessEntity#STATE_NORMAL
   * @see BusinessEntity#STATE_DELETED
   */
  public Collection<BusinessEntity> findByState(int state) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.STATE,
      filter.getEqualOperator(), new Integer(state), false);

    return getDAO().getEntityByFilter(filter);
  }

  /**
   * Find the BusinessEntity whose ID is the specified.
   *
   * @param enterpriseId The ID of the enterprise owning the BusinessEntity.
   * @param bizEntityID The BusinessEntityID of the BusinessEntity.
   * @return The BusinessEntity having the specified ID, or <B>null</B> if
   * none found.
   */
  public BusinessEntity findByBizEntityId(String enterpriseId, String bizEntityId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ID, filter.getEqualOperator(),
      bizEntityId, false);
    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.ENTERPRISE_ID,
      filter.getEqualOperator(), enterpriseId, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (BusinessEntity)result.iterator().next();
  }

  /**
   * Find the BusinessEntities whose Whitepage information match the filtering
   * condition.
   *
   * @param filter The filtering condition
   * @return Collection of BusinessEntity(s) resulting from the retrieval.
   */
  public Collection<BusinessEntity> findByWhitepage(IDataFilter filter)
    throws Throwable
  {
    Collection<BusinessEntity> results = new ArrayList<BusinessEntity>();

    // *** two-stage retrieval
    // 1. retrieve from Whitepage table
    Collection<WhitePage> wpResults = BizEntityDAOHelper.getInstance().getWhitePageDAO().
    																			getEntityByFilter(filter);

    // 2. retrieve from BusinessEntity table based on Whitepage results.
    if (!wpResults.isEmpty())
    {
      Collection<Long> wpKeys = new ArrayList<Long>();
      for (Iterator i=wpResults.iterator(); i.hasNext(); )
      {
        WhitePage whitePage = (WhitePage)i.next();
        wpKeys.add(whitePage.getBeUId());
      }

      DataFilterImpl beFilter = new DataFilterImpl();
      beFilter.addDomainFilter(null, BusinessEntity.UID, wpKeys, false);
      // also exclude the deleted entities
      beFilter.addSingleFilter(filter.getAndConnector(), BusinessEntity.STATE,
        filter.getNotEqualOperator(), new Integer(BusinessEntity.STATE_DELETED), false);

      results = getEntityByFilterForReadOnly(beFilter);
    }

    return results;
  }

  /**
   * Find the DomainIdentifier with the specified type and value.
   * 
   * @param identifierType The identifier type
   * @param identifierValue The identifier value.
   * @return The found DomainIdentifier entity, or <b>null</b> if none is found.
   * @throws Throwable Error performing the find operation.
   */
  public DomainIdentifier findDomainIdentifier(String identifierType, String identifierValue)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DomainIdentifier.TYPE, filter.getEqualOperator(), identifierType, false);
    filter.addSingleFilter(filter.getAndConnector(), DomainIdentifier.VALUE, filter.getEqualOperator(),
      identifierValue, false);
    Collection identifiers = BizEntityDAOHelper.getInstance().getDomainIdentifierDAO().getEntityByFilter(
                                filter);
    DomainIdentifier identifier = null;
    if (!identifiers.isEmpty()) 
    {                           
      // should have only 1
      identifier = (DomainIdentifier)identifiers.toArray()[0];
    }
    
    return identifier;
  }
  
  /**
   * Delete a BusinessEntity. This will not physically delete the account
   * from the database. This will only mark the BusinessEntity as deleted.
   *
   * @param uID The UID of the BusinessEntity to delete.
   */
  public void markDeleteBusinessEntity(Long uID) throws Throwable
  {
    BusinessEntity bizEntity = (BusinessEntity)getEntityByKeyForReadOnly(uID);
    if (bizEntity != null)
    {
      BizEntityDAOHelper.getInstance().checkCanDelete(bizEntity);

      bizEntity.setState(BusinessEntity.STATE_DELETED);
      update(bizEntity);
    }
  }

  public void markDeleteBusinesEntities(DataFilterImpl filter) throws Throwable
  {
    Collection uIDs = getKeyByFilterForReadOnly(filter);
    for (Iterator i=uIDs.iterator(); i.hasNext(); )
    {
      markDeleteBusinessEntity((Long)i.next());
    }
  }

  /**
   * Create a new BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity.
   * @return The created BusinessEntity.
   */
  public BusinessEntity createBusinessEntity(BusinessEntity bizEntity) throws Throwable
  {

    WhitePage whitePage = bizEntity.getWhitePage();
    if(whitePage == null)
    {
      whitePage = new WhitePage();
      bizEntity.setWhitePage(whitePage);
    }

    return (BusinessEntity)createEntity(bizEntity);
  }

  /**
   * Update a BusinessEntity to the database.
   *
   * @param bizEntity The BusinessEntity with changes.
   */
  public void updateBusinessEntity(BusinessEntity bizEntity) throws Throwable
  {
    //don't include key during check
    BizEntityDAOHelper.getInstance().checkDuplicate(bizEntity, true);
    update(bizEntity);
  }

  //use BizEntityDAOHelper as the EntityDAO.
  //everything will be taken care of by the BizEntityDAOHelper
  protected IEntityDAO getDAO()
  {
    return BizEntityDAOHelper.getInstance();
  }

  /**
   * Activate a BusinessEntity. This will only change the BE state to STATE_NORMAL.
   *
   * @param uID The UID of the BusinessEntity to activate.
   */
  public void markActivateBusinessEntity(Long uID) throws Throwable
  {
    BusinessEntity bizEntity = (BusinessEntity)getEntityByKeyForReadOnly(uID);
    if (bizEntity != null)
    {
      bizEntity.setState(BusinessEntity.STATE_NORMAL);
      update(bizEntity);
    }
  }
}
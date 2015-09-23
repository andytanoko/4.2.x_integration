/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizEntityDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Dec 22 2003    Neo Sok Lay         Persist DomainIdentifier(s) with BusinessEntity.
 * Mar 03 2006    Neo Sok Lay         Use generics
 * Jun 09 2007    Tam Wei Xiang       Support Select MIN, MAX
 */
package com.gridnode.pdip.app.bizreg.helpers;

import com.gridnode.pdip.app.bizreg.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This is a EntityDAO implementation for BizEntityBean. It takes care of
 * the BusinessEntity entity as well as its dependent entity: WhitePage.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since 2.0 I4
 */
public class BizEntityDAOHelper implements IEntityDAO
{
  private static final BizEntityDAOHelper _self = new BizEntityDAOHelper();

  private BizEntityDAOHelper()
  {
  }

  /**
   * Get the singleton instance of this DAO.
   */
  public static BizEntityDAOHelper getInstance()
  {
    return _self;
  }

  // ******************* Start Implement methods in IEntityDAO ****************

  public Long create(IEntity entity)
    throws Exception
  {
    BusinessEntity bizEntity = (BusinessEntity)entity;
    WhitePage whitePage = bizEntity.getWhitePage();
    Collection<DomainIdentifier> domainIdentifiers = bizEntity.getDomainIdentifiers();

    Long beKey = getBizEntityDAO().create(bizEntity);

    whitePage.setFieldValue(WhitePage.BE_UID, beKey);
    //Long wpKey = getWhitePageDAO().create(whitePage);
    getWhitePageDAO().create(whitePage);
    createDomainIdentifiers(beKey, domainIdentifiers);
    
    return beKey;
  }

  public IEntity load(Long beUID)
    throws Exception
  {
    BusinessEntity be = (BusinessEntity)getBizEntityDAO().load(beUID);
    loadWhitePage(be);
    loadDomainIdentifiers(be);
    return be;
  }

  public void store(IEntity bizEntity)
    throws Exception
  {
    BusinessEntity be = (BusinessEntity)bizEntity;
    WhitePage whitePage = be.getWhitePage();
    Collection<DomainIdentifier> domainIdentifiers = be.getDomainIdentifiers();
     
    getBizEntityDAO().store(be);
    getWhitePageDAO().store(whitePage);
    storeDomainIdentifiers(be, domainIdentifiers);
  }

  public void remove(Long beUID)
    throws Exception
  {
    BusinessEntity bizEntity = (BusinessEntity)load(beUID);

    WhitePage whitePage = bizEntity.getWhitePage();
    Collection<DomainIdentifier> domainIdentifiers = bizEntity.getDomainIdentifiers();

    getBizEntityDAO().remove(beUID);
    getWhitePageDAO().remove((Long)whitePage.getKey());
    removeDomainIdentifiers(domainIdentifiers);
  }

  public Long findByPrimaryKey(Long primaryKey) throws Exception
  {
    return getBizEntityDAO().findByPrimaryKey(primaryKey);
  }

  public Collection findByFilter(IDataFilter filter)
    throws Exception
  {
    return getBizEntityDAO().findByFilter(filter);
  }

  public Collection getEntityByFilter(IDataFilter filter)
    throws Exception
  {
    Collection bizEntities = getBizEntityDAO().getEntityByFilter(filter);
    for (Iterator i=bizEntities.iterator(); i.hasNext(); )
    {
      BusinessEntity bizEntity = (BusinessEntity)i.next();
      loadWhitePage(bizEntity);
      loadDomainIdentifiers(bizEntity);
    }
    return bizEntities;
  }

  public int getEntityCount(IDataFilter filter)
    throws Exception
  {
    return getBizEntityDAO().getEntityCount(filter);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#getFieldValuesByFilter(java.lang.Number, com.gridnode.pdip.framework.db.filter.IDataFilter)
	 */
	public Collection getFieldValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
	{
		return getBizEntityDAO().getFieldValuesByFilter(fieldId, filter);
	}

  // ******************* Ends Implement methods in IEntityDAO ****************


	// ******************* Persistency for WhitePage ***************************
  /**
   * Creates the whitepage for a new BusinessEntity.
   *
   * @param bizEntity The new BusinessEntity entity.
   *//*
  private void createWhitePage(BusinessEntity bizEntity)
    throws Exception
  {
    WhitePage whitePage = bizEntity.getWhitePage();

    getWhitePageDAO().create(whitePage);
  }*/

  /**
   * Loads the whitepage for a BusinessEntity
   *
   * @param bizEntity The BusinessEntity entity.
   */
  private void loadWhitePage(BusinessEntity bizEntity)
    throws Exception
  {
    Long whitePageUId = findWhitePageByBeUID(new Long(bizEntity.getUId()));
    WhitePage whitePage = (WhitePage)getWhitePageDAO().load(
                                      whitePageUId);
    bizEntity.setWhitePage(whitePage);
  }

  /**
   * Finds the whitepage record of a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity record.
   * @return The primary key to the WhitePage record.
   *
   */
  private Long findWhitePageByBeUID(Long beUID)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WhitePage.BE_UID,
      filter.getEqualOperator(), beUID, false);
    Collection result = getWhitePageDAO().findByFilter(filter);
    if(result == null || result.isEmpty())
      return null;
    return (Long) result.iterator().next();
  }

  // ***************** Persistency for DomainIdentifiers ************************
  
  /**
   * Create DomainIdentifier for newly create business entity. This domainIdentifiers
   * should not already exists in database.
   * 
   * @param beUid UID of the newly created BusinessEntity
   * @param domainIdentifiers Collection of DomainIdentifier entities of the BusinessEntity.
   */
  private void createDomainIdentifiers(Long beUid, Collection<DomainIdentifier> domainIdentifiers)
    throws Exception
  {
    IEntityDAO dao = getDomainIdentifierDAO();
    for (DomainIdentifier identifier : domainIdentifiers)
    {
      identifier.setBeUid(beUid); // need to set again here for the actual Be UID
      dao.create(identifier);
    }
  }

  /**
   * Loads DomainIdentifier(s) for the BusinessEntity. Sets the DomainIdentifier(s) into
   * the BusinessEntity.
   * 
   * @param be The BusinessEntity
   * @throws Exception Error loading the DomainIdentifier(s) from the database.
   */
  private void loadDomainIdentifiers(BusinessEntity be)
    throws Exception
  {
    Collection<DomainIdentifier> identifiers = findDomainIdentifiersByBeUID(new Long(be.getUId()));
    be.setDomainIdentifiers(identifiers);
  }
  
  /**
   * Finds the DomainIdentifiers of a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity record.
   * @return The DomainIdentifier records.
   */
  private Collection<DomainIdentifier> findDomainIdentifiersByBeUID(Long beUID)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DomainIdentifier.BUSINESS_ENTITY_UID,
      filter.getEqualOperator(), beUID, false);
    filter.setOrderFields(new Object[]{DomainIdentifier.UID});
    return getDomainIdentifierDAO().getEntityByFilter(filter);
  }
  
  /**
   * Removes the DomainIdentifier(s) for a deleted BusinessEntity.
   * 
   * @param domainIdentifiers Collection of DomainIdentifier(s) to remove
   * @throws Exception Error removing the DomainIdentifier(s) from the database.
   */
  private void removeDomainIdentifiers(Collection<DomainIdentifier> domainIdentifiers)
    throws Exception
  {
    IEntityDAO dao = getDomainIdentifierDAO();
    for (DomainIdentifier identifier : domainIdentifiers)
    {
      dao.remove(new Long(identifier.getUId()));
    }
  }
  
  /**
   * Create/Update/Remove the DomainIdentifier(s) of a BusinessEntity during Store. 
   * 
   * @param be The BusinessEntity
   * @param domainIdentifiers Collection of DomainIdentifier(s) in the BusinessEntity
   * to be updated.
   * @throws Exception Error in the store operations.
   */
  private void storeDomainIdentifiers(BusinessEntity be, Collection<DomainIdentifier> domainIdentifiers)
    throws Exception
  {
    DomainIdentifier identifier;
    IEntityDAO dao = getDomainIdentifierDAO();
    Collection<DomainIdentifier> oldSet1 = findDomainIdentifiersByBeUID(new Long(be.getUId()));
    Collection<DomainIdentifier> oldSet2 = new HashSet<DomainIdentifier>(oldSet1);
    Collection<DomainIdentifier> newSet = domainIdentifiers;

    // filter out those still in the new set, remaining will be removed
    oldSet1.removeAll(newSet);
    // keep those still in new set, will be updated
    oldSet2.retainAll(newSet); 
    
    // remove the unwanted set
    // force delete the domain identifiers if the BusinessEntity is being marked deleted
    boolean forceDelete = (be.getState() == BusinessEntity.STATE_DELETED);
    for (Iterator i=oldSet1.iterator(); i.hasNext(); )
    {
      identifier = (DomainIdentifier)i.next();
      // check CanDelete flag.
      if (forceDelete || identifier.canDelete())
        dao.remove(new Long(identifier.getUId()));   
    }

    for (Iterator i=newSet.iterator(); i.hasNext(); )
    {
      identifier = (DomainIdentifier)i.next();
      try
      {
        if (oldSet2.contains(identifier)) // existing, update
        {
          checkDuplicate(identifier, true);
          dao.store(identifier);
        }
        else //non-existing, add
        {
          checkDuplicate(identifier, false);    
          dao.create(identifier);
        }
      }
      catch (DuplicateEntityException e)
      {
        Logger.error(ILogErrorCodes.BIZREG_DUPLICATE_DOMAIN_IDENTIFIER,
                     "BizEntityDAOHelper.storeDomainIdentifiers] Duplicate Entity: "+ e.getMessage());
      }
    }
  }
  
  public void checkDuplicate(DomainIdentifier domainIdentifier, boolean checkKey)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DomainIdentifier.TYPE, filter.getEqualOperator(),
      domainIdentifier.getType(), false);
    filter.addSingleFilter(filter.getAndConnector(), DomainIdentifier.VALUE,
      filter.getEqualOperator(), domainIdentifier.getValue(), false);
    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), DomainIdentifier.UID,
        filter.getNotEqualOperator(), domainIdentifier.getKey(), false);

    if (getDomainIdentifierDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException("DomainIdentifier: Type "+domainIdentifier.getType()
        + ", Value "+domainIdentifier.getValue() + " is already in use!");    
  }
  
  /**
   * Check if the specified BusinessEntity will result in duplicate when
   * created or updated.
   *
   * @param bizEntity The business entity to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the business entity itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * BusinessEntity will result in duplicates.
   */
  public void checkDuplicate(
    BusinessEntity bizEntity, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ID, filter.getEqualOperator(),
      bizEntity.getBusEntId(), false);
    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.ENTERPRISE_ID,
      filter.getEqualOperator(), bizEntity.getEnterpriseId(), false);
    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.UID,
        filter.getNotEqualOperator(), bizEntity.getKey(), false);

    if (getEntityCount(filter) > 0)
      throw new DuplicateEntityException("BusinessEntity ID "+bizEntity.getBusEntId()
        + " already used for Enterprise: "+bizEntity.getEnterpriseId());
  }

  /**
   * Check whether a BusinessEntity can be deleted.
   *
   * @param bizEntity The BusinessEntity to check.
   *
   * @exception ApplicationException The BusinessEntity is not allowed to be
   * deleted.
   */
  public void checkCanDelete(BusinessEntity bizEntity) throws Exception
  {
    if (!bizEntity.canDelete())
      throw new ApplicationException("BusinessEntity not allowed to be deleted!");
  }

  /**
   * Get the data access object for the BusinessEntity entity.
   *
   * @return the IEntityDAO for BusinessEntity entity.
   */
  public IEntityDAO getBizEntityDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(BusinessEntity.ENTITY_NAME);
  }

  /**
   * Get the data access object for the WhitePage entity.
   *
   * @return the IEntityDAO for WhitePage entity.
   */
  public IEntityDAO getWhitePageDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(WhitePage.ENTITY_NAME);
  }

  /* (non-Javadoc)
   * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#create(com.gridnode.pdip.framework.db.entity.IEntity, boolean)
   */
  public Long create(IEntity entity, boolean useUID) throws Exception {
    throw new Exception("[BizEntityDAOHelper.create(IEntity entity, boolean useUID)] Not Supported");
  }
  
  /**
   * Get the data access object for the DomainIdentifier entity.
   * 
   * @return The IEntityDAO for DomainIdentifier entity.
   */
  public IEntityDAO getDomainIdentifierDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(DomainIdentifier.ENTITY_NAME);
  }
  
  public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getBizEntityDAO().getMinValuesByFilter(fieldId, filter);
  }
  
  public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getBizEntityDAO().getMaxValuesByFilter(fieldId, filter);
  }
}
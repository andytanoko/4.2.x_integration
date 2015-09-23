/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizRegistryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Add management for RegistryConnectInfo
 *                                    Change SearchId to Long type.
 * Dec 23 2003    Neo Sok Lay         Add findBusinessEntityByDomainIdentifier().
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 * Mar 01 2006    Neo Sok Lay         Use generics
 * 2008-07-17     Teh Yu Phei		  Add markActivateBusinessEntity (Ticket 31)
 *                                    
 */
package com.gridnode.pdip.app.bizreg.facade;

import com.gridnode.pdip.app.bizreg.exceptions.SearchRegistryException;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import java.rmi.RemoteException;
import java.util.BitSet;
import java.util.Collection;

/**
 * Interface defining the business methods for implementing a
 * Business Registry Manager.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since GT 2.2
 */
public interface IBizRegistryManager
{
  /**
   * Create a new BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity.
   * @return The UID of the created BusinessEntity
   */
  public Long createBusinessEntity(BusinessEntity bizEntity)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a BusinessEntity.
   *
   * @param bizEntity The BusinessEntity entity with changes.
   */
  public void updateBusinessEntity(BusinessEntity bizEntity)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Mark Delete a BusinessEntity. This will not physically delete the
   * BusinessEntity from the database. The State of the BusinessEntity will be
   * changed to STATE_DELETED.
   *
   * @param beUId The UID of the BusinessEntity to delete.
   */
  public void markDeleteBusinessEntity(Long beUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Mark Delete the BusinessEntities that belong to an enterprise. This will
   * not physically delete the BusinessEntities from the database. The State
   * of the BusinessEntities will be changed to STATE_DELETED.
   *
   * @param enterpriseId ID of the enterprise whose BusinessEntities are to be
   * marked Deleted.
   */
  public void markDeleteBusinessEntities(String enterpriseId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a BusinessEntity using the BusinessEntity UID.
   *
   * @param uID The UID of the BusinessEntity to find.
   * @return The BusinessEntity found, or <B>null</B> if none exists with that
   * UID.
   */
  public BusinessEntity findBusinessEntity(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Purge all BusinessEntities that are marked deleted.
   */
  public void purgeDeletedBusinessEntities()
    throws DeleteEntityException, SystemException, RemoteException;

   /**
   * Purge all BusinessEntities of an enterprise that are marked deleted.
   *
   * @param enterpriseId ID of the enterprise.
   */
  public void purgeDeletedBusinessEntities(String enterpriseId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a BusinessEntity using the BusinessEntityID.
   *
   * @param enterpriseId The ID of the enterprise owning the BusinessEntity
   * @param beId The BusinessEntity ID of the BusinessEntity to find.
   * @return The BusinessEntity found, or <B>null</B> if none exists with that
   * Id.
   */
  public BusinessEntity findBusinessEntity(String enterpriseId, String beId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a BusinessEntity using the BusinessEntity ID and Enterprise ID.
   *
   * @param enterpriseId The ID of the enterprise owning the BusinessEntity.
   * @param bizEntityID The ID of the BusinessEntity to find.
   * @return The UID of the BusinessEntity found, or <B>null</B> if none
   * exists with the specified inputs.
   */
  public Long findBusinessEntityKey(String enterpriseId, String bizEntityID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of BusinessEntities using a filtering condition
   *
   * @param filter The filtering condition of the BusinessEntities to find.
   * @return A Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntities(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of BusinessEntities using the State.
   *
   * @param state The state of the BusinessEntities to find.
   * @return A Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntities(int state)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of BusinessEntities whose Whitepage information
   * satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of BusinessEntities found, or empty collection if none
   * exists.
   */
  public Collection<BusinessEntity> findBusinessEntitiesByWhitePage(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the BusinessEntities that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of BusinessEntities found, or empty
   * collection if none.
   */
  public Collection<Long> findBusinessEntitiesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

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
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a BusinessEntity that has a DomainIdentifier of the specified
   * identifier type and value.
   * 
   * @param type The DomainIdentifier Type.
   * @param value The DomainIdentifier Value.
   * @return The BusinessEntity found, or <b>null</b> if none found.
   * @throws FindEntityException Error occurs when performing the find operation.
   * @throws SystemException Unexpected system error.
   */
  public BusinessEntity findBusinessEntityByDomainIdentifier(
    String type, String value)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Submits a search on a business registry using the specified search criteria.
   * 
   * @param searchCriteria The search criteria
   * @return A SearchId, if search request is submitted successfully, for retrieving
   * the search results later on.
   * @throws SearchRegistryException Error occurs when submitting the search request.
   * @throws SystemException Unexpected system errror.
   */
  public Long submitSearchQuery(SearchRegistryCriteria searchCriteria)
    throws SearchRegistryException, SystemException, RemoteException;

  /**
   * Retrieves a submitted search query using a specified SearchId.
   * 
   * @param searchId The SearchId.
   * @return The SearchRegistryQuery for the specified SearchId.
   * @throws SearchRegistryException Error occurs when retrieving the search query.
   * @throws SystemException Unexpected system error.
   */  
  public SearchRegistryQuery retrieveSearchQuery(Long searchId)
    throws SearchRegistryException, SystemException, RemoteException;  

  //public void publishBusinessEntity(Long beUID);

 
  /**
   * Create a new RegistryConnectInfo.
   *
   * @param connInfo The RegistryConnectInfo entity.
   * @return The UID of the created RegistryConnectInfo
   */
  public Long createRegistryConnectInfo(RegistryConnectInfo connInfo)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a RegistryConnectInfo.
   *
   * @param connInfo The RegistryConnectInfo entity with changes.
   */
  public void updateRegistryConnectInfo(RegistryConnectInfo connInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Find a RegistryConnectInfo using the RegistryConnectInfo UID.
   *
   * @param uID The UID of the RegistryConnectInfo to find.
   * @return The RegistryConnectInfo found
   * @throws FindEntityException No RegistryConnectInfo found with the specified UID
   * @throws SystemException Unexpected system error.
   */
  public RegistryConnectInfo findRegistryConnectInfo(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a RegistryConnectInfo using the RegistryConnectInfo Name.
   *
   * @param name The Name of the RegistryConnectInfo to find.
   * @return The RegistryConnectInfo found, or <B>null</B> if none exists with that
   * UID.
   */
  public RegistryConnectInfo findRegistryConnectInfoByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of RegistryConnectInfo(s) using a filtering condition
   *
   * @param filter The filtering condition of the RegistryConnectInfo(s) to find.
   * @return A Collection of RegistryConnectInfo(s) found, or empty collection if none
   * exists.
   */
  public Collection<RegistryConnectInfo> findRegistryConnectInfos(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the RegistryConnectInfo(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of RegistryConnectInfo(s) found, or empty
   * collection if none.
   */
  public Collection<Long> findRegistryConnectInfoKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Delete a RegistryConnectInfo. 
   *
   * @param uId The UID of the RegistryConnectInfo to delete.
   */
  public void deleteRegistryConnectInfo(Long uId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Mark Activate a BusinessEntity. The State of the BusinessEntity will be
   * changed to STATE_NORMAL.
   *
   * @param beUId The UID of the BusinessEntity to activate.
   */
  public void markActivateBusinessEntity(Long beUId)
    throws SystemException, RemoteException;
}
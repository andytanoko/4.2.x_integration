/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerProcessManagerObj.java
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
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method createTrigger does not throw java.rmi.RemoteException
 *                                    - The business method updateTrigger does not throw java.rmi.RemoteException
 *                                    - The business method deleteTrigger does not throw java.rmi.RemoteException
 *                                    - The business method findTrigger does not throw java.rmi.RemoteException
 *                                    - The business method findTriggers does not throw java.rmi.RemoteException
 *                                    - The business method findTriggers does not throw java.rmi.RemoteException
 *                                    - The business method findTriggers does not throw java.rmi.RemoteException
 *                                    - The business method findTriggersKeys does not throw java.rmi.RemoteException
 *                                    No corresponding business method in the bean class com.gridnode.gtas.server.partnerprocess.facade.ejb.PartnerProcessManagerBean 
 *                                    was found for method findTriggers.                                   
 */
package com.gridnode.gtas.server.partnerprocess.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for PartnerProcessManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public interface IPartnerProcessManagerObj
       extends   EJBObject
{

  /**
   * Create a new Trigger.
   *
   * @param trigger The Trigger entity.
   */
  public Long createTrigger(Trigger trigger)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a Trigger
   *
   * @param trigger The Trigger entity with changes.
   */
  public void updateTrigger(Trigger trigger)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a Trigger.
   *
   * @param triggerUId The UID of the Trigger to delete.
   */
  public void deleteTrigger(Long triggerUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a Trigger using the Trigger UID.
   *
   * @param triggerUId The UID of the Trigger to find.
   * @return The Trigger found, or <B>null</B> if none exists with that
   * UID.
   */
  public Trigger findTrigger(Long triggerUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Trigger that is under the same level.
   *
   * @param triggerLevel The level of the triggers to find.
   * @return a Collection of Trigger found, or empty collection if none
   * exists.
   */
  public Collection findTriggers(Integer triggerLevel)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Trigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Trigger found, or empty collection if none
   * exists.
   */
  public Collection findTriggers(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Trigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Trigger found, or empty collection if
   * none exists.
   */
  public Collection findTriggersKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the triggers based on the given document type, partner type, partner
   * group and partner id.
   *
   * @param docType The document type to trigger.
   * @param partnerType The partner type to trigger.
   * @param partnerGroup The partner group to trigger.
   * @param partnerId The partner id to trigger.
   * @param triggerType The triggerType to trigger.
   * @return a collection of matched triggers or a empty collection if no
   *         matched trigger is found.
   */
  public Collection findTriggers(String docType,
                                 String partnerType,
                                 String partnerGroup,
                                 String partnerId,
                                 Integer triggerType)
    throws FindEntityException, SystemException, RemoteException;


  /**
   * Create a ProcessMapping.
   *
   * @param mapping The Process Mapping to add.
   * @return The UID of the created ProcessMapping.
   */
  public Long createProcessMapping(ProcessMapping mapping)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Remove a ProcessMapping.
   *
   * @param uid The UID of the ProcessMapping to remove.
   */
  public void deleteProcessMapping(Long uid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Update a ProcessMapping.
   *
   * @param mapping The mapping to update.
   */
  public void updateProcessMapping(ProcessMapping mapping)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Get a number of ProcessMapping(s) using a filtering condition.
   *
   * @param filter The filtering condition of the ProcessMapping(s) to find.
   * @return A Collection of ProcessMapping(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection findProcessMappingByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

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
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ProcessMapping.
   *
   * @param uid The UID of the ProcessMapping to find.
   * @return The ProcessMapping found, if any.
   * @throws FindEntityException No ProcessMapping with the uid found.
   */
  public ProcessMapping findProcessMappingByUID(Long uid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a BizCertMapping.
   *
   * @param mapping The BizCert Mapping to add.
   * @return The UID of the created BizCertMapping.
   */
  public Long createBizCertMapping(BizCertMapping mapping)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Remove a BizCertMapping.
   *
   * @param uid The UID of the BizCertMapping to remove.
   */
  public void deleteBizCertMapping(Long uid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Update a BizCertMapping.
   *
   * @param mapping The mapping to update.
   */
  public void updateBizCertMapping(BizCertMapping mapping)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Get a number of BizCertMapping(s) using a filtering condition.
   *
   * @param filter The filtering condition of the BizCertMapping(s) to find.
   * @return A Collection of BizCertMapping(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection findBizCertMappingByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a number of BizCertMapping(s) using a filtering condition and
   * return their keys.
   *
   * @param filter The filtering condition of the BizCertMapping(s) to find.
   * @return A Collection of UIDs of the BizCertMapping(s) found, or empty collection
   * if none exists.
   */
  public Collection findBizCertMappingKeysByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a BizCertMapping.
   *
   * @param uid The UID of the BizCertMapping to find.
   * @return The BizCertMapping found, if any.
   * @throws FindEntityException No BizCertMapping with the uid found.
   */
  public BizCertMapping findBizCertMappingByUID(Long uid)
    throws FindEntityException, SystemException, RemoteException;



}
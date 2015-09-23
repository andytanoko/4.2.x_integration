/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 18 2002    Ang Meng Hua        Created
 * Jun 06 2002    Ang Meng Hua        Added services for managing
 *                                    PartnerType & PartnerGroup
 * Jul 15 2003    Neo Sok Lay         Add methods:
 *                                    findPartnerKeys(IDataFilter),
 *                                    findPartnerTypeKeys(IDataFilter),
 *                                    findPartnerGroupKeys(IDataFilter)
 * Mar 28 2006    Neo Sok Lay         Add method: setMaxActivePartners(int)                                   
 */
package com.gridnode.pdip.app.partner.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * The remote interface of the PartnerManagerBean. It provides the
 * app service layer implementation of the partner management module
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.1
 */
public interface IPartnerManagerObj extends EJBObject
{
  /************** Business methods for Partner Type Entity Mgmt ****************************/
  /**
   * Create a new PartnerType
   *
   * @param partnerType The PartnerType entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartnerType(PartnerType partnerType)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update an existing PartnerType
   *
   * @param partnerType The PartnerType entity value object
   * @exception UpdateEntityException
   */
  public void updatePartnerType(PartnerType partnerType)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a PartnerType.
   *
   * @param uID The UID of the PartnerType to delete.
   * @exception DeleteEntityException
   */
  public void deletePartnerType(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a Partner Type based on UID
   *
   * @param uID The UID of the partner type to find.
   * @return The Partner Type found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public PartnerType findPartnerType(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerType found, or empty collection if none
   * exists.
   */
  public Collection findPartnerType(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerType that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the PartnerTypes found, or empty collection if none
   * exists.
   */
  public Collection findPartnerTypeKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all PartnerType using the PartnerType Name.
   *
   * @param name The name of the partner type to find.
   * @return The PartnerType found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerType findPartnerTypeByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /***************** Business methods for PartnerGroup Entity Mgmt *************************/
  /**
   * Create a new PartnerGroup
   *
   * @param partnerGroup The PartnerGroup entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartnerGroup(PartnerGroup partnerGroup)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update an existing PartnerGroup
   *
   * @param partnerGroup The PartnerGroup entity value object
   * @exception UpdateEntityException
   */
  public void updatePartnerGroup(PartnerGroup partnerGroup)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a PartnerGroup.
   *
   * @param uID The UID of the PartnerGroup to delete.
   * @exception DeleteEntityException
   */
  public void deletePartnerGroup(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a Partner Group based on UID
   *
   * @param uID The UID of the partner group to find.
   * @return The Partner Group found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public PartnerGroup findPartnerGroup(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerGroup that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerGroup found, or empty collection if none
   * exists.
   */
  public Collection findPartnerGroup(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerGroup that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the PartnerGroups found, or empty collection if none
   * exists.
   */
  public Collection findPartnerGroupKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all PartnerGroup using the PartnerGroup Name.
   *
   * @param name The name of the partner group to find.
   * @return The PartnerGroup found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerGroup findPartnerGroupByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all PartnerGroup using the Partner Type.
   *
   * @param partnerType The Partner Type of the partner groups to find.
   * @return A Collection of PartnerGroup found, or empty collection if none
   * exists.
   */
  public Collection findPartnerGroupByPartnerType(String partnerType)
    throws FindEntityException, SystemException, RemoteException;

  /******************* Business methods for Partner Entity Mgmt ****************************/
  /**
   * Create a new Partner
   *
   * @param partner The Partner entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartner(Partner partner)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update an existing Partner
   *
   * @param partner The Partner entity value object
   * @exception UpdateEntityException
   */
  public void updatePartner(Partner partner)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a Partner. This will not physically delete the partner
   * from the database. This will only mark the partner as deleted
   * if markeDeleteOnly is true
   *
   * @param uID The UID of the Partner to delete.
   * @param markDeleteOnly flag to mark delete the entity
   * @exception DeleteEntityException
   */
  public void deletePartner(Long uID, boolean markDeleteOnly)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a Partner based on UID
   *
   * @param uID The UID of the partner to find.
   * @return The Partner found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public Partner findPartner(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Partner that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Partner found, or empty collection if none
   * exists.
   */
  public Collection findPartner(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Partners that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a Partner using the Partner ID.
   *
   * @param partnerID The Partner ID of the partner to find.
   * @return The Partner found, or <B>null</B> if none exists with that
   * ID.
   */
  public Partner findPartnerByID(String partnerID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all Partners using the Partner Name.
   *
   * @param name The name of the partners to find.
   * @return A Collection of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all Partners using the Partner Type.
   *
   * @param partnerType The Partner Type of the partners to find.
   * @return A Collection of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByPartnerType(String partnerType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all Partners using the Partner Group.
   *
   * @param partnerGroup The Partner Group of the partners to find.
   * @return A Collection of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByPartnerGroup(String partnerGroup)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Set the limit on the maximum number of active partners that could be created.
   * @param maxActivePartners The maximum number of active partners allowed to be created.
   * @throws SystemException
   */
  public void setMaxActivePartners(int maxActivePartners)
  	throws SystemException, RemoteException;
  
  /**
   * Get the balance count of active partners allowed to be configured
   * @param excludePartnerID The partner id to exclude from the count, or <b>null</b> if no exclusion desired
   * @return The balance count
   * @throws SystemException
   */
  public int getActivePartnersBalCount(String excludePartnerID)
  	throws SystemException, RemoteException;
}
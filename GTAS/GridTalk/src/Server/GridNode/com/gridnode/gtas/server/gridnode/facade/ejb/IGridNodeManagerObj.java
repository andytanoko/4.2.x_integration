/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridNodeManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 * Sep 11 2002    Neo Sok Lay         Add methods to get/save My CompanyProfile.
 * Sep 17 2002    Neo Sok Lay         Add methods for managing Gridnodes. These
 *                                    methods may be temporary, and may change
 *                                    to more specific and restrictive methods
 *                                    as the module evolves.
 * Oct 30 2002    Neo Sok Lay         Add updateConnectionStatus().
 */
package com.gridnode.gtas.server.gridnode.facade.ejb;

import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.gridnode.exceptions.SaveCoyProfileException;
import com.gridnode.gtas.server.gridnode.model.GnCategory;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;

import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

/**
 * EJBObject for the GridNodeManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I5
 */
public interface IGridNodeManagerObj extends EJBObject
{
  // ********************** GnCategory ****************************

  /**
   * Find GnCategory by CategoryCode.
   *
   * @param categoryCode The numerical code of the GnCategory to find.
   *
   * @return GnCategory found, or <b>null</b> if none found.
   */
  public GnCategory findGnCategoryByCode(String categoryCode)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all GnCategory(s).
   *
   * @return Collection of GnCategory(s) found.
   */
  public Collection findAllGnCategories()
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all GnCategory(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of GnCategory(s) found, or empty collection if none.
   */
  public Collection findGnCategoriesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************* CompanyProfile ***********************

  /**
   * Get the CompanyProfile for this GridTalk.
   *
   * @return The CompanyProfile entity for this GridTalk. If one does not exist
   * yet, an empty CompanyProfile will be returned.
   */
  public CompanyProfile getMyCompanyProfile()
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Save the CompanyProfile for this GridTalk.
   *
   * @pram coyProfileMap The Map object containing the modified contents for
   * the CompanyProfile.
   *
   * @throws SaveCoyProfileException Invalid modified contents of the CompanyProfile,
   * or error in saving the modified CompanyProfile.
   */
  public void saveMyCompanyProfile(Map coyProfileMap)
    throws SaveCoyProfileException, SystemException, RemoteException;

  /**
   * Get the CompanyProfile for a GridNode
   *
   * @return The CompanyProfile entity for the specified GridNode, or
   * <b>null</b> if none exists.
   */
  public CompanyProfile getCompanyProfile(Long gridnodeUID)
    throws FindEntityException, SystemException, RemoteException;

  // *********************** GridNode ************************************
  /**
   * Create the GridNode entity in the database.
   *
   * @param gridnode The GridNode entity to create.
   * @return the UID of the created GridNode entity.
   */
  public Long createGridNode(GridNode gridnode)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update the GridNode entity to the database.
   *
   * @param gridnode The GridNode entity with changes to update.
   *
   */
  public void updateGridNode(GridNode gridnode)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GridNode entity from the database.
   *
   * @param uID The UID of the GridNode entity to delete.
   */
  public void deleteGridNode(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a GridNode entity from the database.
   *
   * @param uID The UID of the GridNode entity to find.
   * @return The found GridNode entity, if any.
   * @throws FindEntityException Unable to find the GridNode entity from
   * the database with the specified UID.
   */
  public GridNode findGridNode(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a GridNode entity from the database using the GridNode ID.
   *
   * @param nodeID The GridNode ID.
   * @return The found GridNode entity.
   * @throws FindEntityException Unable to find the GridNode entity from the
   * database with the specified GridNode ID.
   */
  public GridNode findGridNodeByID(String nodeID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find GridNode entities from the database that satisfy a filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of GridNode entities found, or empty collection if none
   * found.
   */
  public Collection findGridNodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the UIDs of the GridNodes that satisfy a filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection UIDs found, or empty collection if none found.
   */
  public Collection findGridNodesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the GridNode setup for this GridTalk.
   *
   * @return The GridNode set-up for this GridTalk during Product registration,
   * if any.
   * @throws FindEntityException No GridNode setup yet.
   */
  public GridNode findMyGridNode()
    throws FindEntityException, SystemException, RemoteException;


  // ************************** ConnectionStatus ******************************
  /**
   * Find a ConnectionStatus entity from the database.
   *
   * @param uID The UID of the ConnectionStatus entity to find.
   * @return The found ConnectionStatus entity, if any.
   * @throws FindEntityException Unable to find the ConnectionStatus entity from
   * the database with the specified UID.
   */
  public ConnectionStatus findConnectionStatus(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ConnectionStatus entity from the database using the GridNode ID.
   *
   * @param nodeID The GridNode ID.
   * @return The found ConnectionStatus entity.
   * @throws FindEntityException Unable to find the ConnectionStatus entity from the
   * database with the specified GridNode ID.
   */
  public ConnectionStatus findConnectionStatusByNodeID(String nodeID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find ConnectionStatus entities from the database that satisfy a filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of ConnectionStatus entities found, or empty collection if none
   * found.
   */
  public Collection findConnectionStatusByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Update a ConnectionStatus entity to the database.
   *
   * @param connStatus The modified ConnectionStatus to update.
   * @throws UpdateEntityException Error updating ConnectionStatus to database.
   */
  public void updateConnectionStatus(ConnectionStatus connStatus)
    throws UpdateEntityException, SystemException, RemoteException;

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISharedResourceManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 * Jan 06 2004    Neo Sok Lay         Remove passing of RouteChannelUID for
 *                                    synchronization.
 *                                    Add checkSum for completeSynchronization().
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizeResourceException;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 * LocalObject for SharedResourceManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public interface ISharedResourceManagerObj
  extends        EJBObject
{

  // ******************** SHARED RESOURCE (begin) ********************** //

  /**
   * Add a shared resource.
   *
   * @param resourceType The type of the resource to share.
   * @param resourceUID The UID of the resource to share.
   * @param toEnterpriseID ID of the enterprise to share resource to.
   * @return The UID of the created SharedResource.
   */
  public Long addSharedResource(
    String resourceType, Long resourceUID, String toEnterpriseID)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Add multiple shared resources of the same type.
   *
   * @param resourceType The type of the resources to share.
   * @param resourceUIDs Collection of UIDs of the resources to share.
   * @param toEnterpriseID ID of the enterprise to share resources to.
   * @return The Collection of UIDs of the created SharedResource(s).
   */
  public Collection addSharedResources(
    String resourceType, Collection resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Add multiple shared resources of the same type, if resources have not
   * already been shared.
   *
   * @param resourceType The type of the resources to share.
   * @param resourceUIDs Collection of UIDs of the resources to share.
   * @param toEnterpriseID ID of the enterprise to share resources to.
   * @return The Collection of UIDs of the created or already shared
   * SharedResource(s).
   */
  public Collection shareResourceIfNotShared(
    String resourceType, Collection resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Add multiple shared resources of the same type, if resources have not
   * already been shared.
   *
   * @param resourceType The type of the resources to share.
   * @param resourceUIDs UIDs of the resources to share.
   * @param toEnterpriseID ID of the enterprise to share resources to.
   * @return The Collection of UIDs of the created or already shared
   * SharedResource(s).
   */
  public Collection shareResourceIfNotShared(
    String resourceType, Long[] resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update the specified SharedResource to unsync state.
   *
   * @param sharedResUID UID of the SharedResource.
   * @param checkSum The synchronization checksum.
   */
  public void setSharedResourceUnsync(Long sharedResUID, long checkSum)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Update the specified SharedResource(s) to unsync state.
   *
   * @param sharedResUIDs Collection of UIDs of the SharedResource(s).
   */
  public void setSharedResourcesUnsync(Collection sharedResUIDs)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Remove a SharedResource.
   *
   * @param sharedResourceUID The UID of the SharedResource to remove.
   */
  public void removeSharedResource(Long sharedResourceUID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Remove SharedResource(s) that match a filtering condition.
   *
   * @param filter The filtering condition.
   */
  public void removeSharedResourcesByFilter(IDataFilter filter)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Remove all SharedResource(s) for a specific resource shared to all
   * enterprises.
   *
   * @param resourceType The type of resources.
   * @param resourceUID The UID of the resources.
   */
  public void removeSharedResources(String resourceType, Long resourceUID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Remove a SharedResource for a specific resource shared to an
   * enterprise.
   *
   * @param resourceType The type of resource.
   * @param resourceUID The UID of the resource.
   * @param toEnterpriseID The ID of the Enterprise that the resource is shared
   * to.
   */
  public void removeSharedResource(
    String resourceType, Long resourceUID, String toEnterpriseID)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Purge those marked Deleted SharedResource(s) for a particular resource type.
   * This means that the SharedResource(s) will be physically deleted from the
   * database.
   *
   * @param resourceType The resource type.
   * @exception DeleteEntityException Error in performing the deletion.
   * @since 2.0 I4
   */
  public void purgeDeletedSharedResources(String resourceType)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Get the SharedResource(s) of the same resource type shared to an
   * enterprise. This excludes all SharedResource(s) with "Deleted" state.
   *
   * @param resourceType The type of the shared resource
   * @param resourceUID The UID of the shared resource
   * @param toEnterpriseID The ID of the enterprise.
   */
  public Collection getSharedResources(String resourceType, String toEnterpriseID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the SharedResource(s) for a specific resource. This excludes all
   * SharedResource(s) with "Deleted" state.
   *
   * @param resourceType The type of the shared resource
   * @param resourceUID The UID of the shared resource
   */
  public Collection getSharedResources(String resourceType, Long resourceUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a SharedResource, regardless of the state of sharing.
   *
   * @param uID The UID of the SharedResource to find.
   * @return The SharedResource found, if any.
   * @exception FindEntityException SharedResource not found.
   */
  public SharedResource getSharedResourceByUID(Long sharedResourceUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a SharedResource, regardless of the state of sharing.
   *
   * @param resourceType The type of the shared resource.
   * @param resourceUID The UID of the shared resource.
   * @param toEnterpriseID ID of the enterprise that is shared the resource.
   * @return The SharedResource found or <b>null</b> if none found.
   */
  public SharedResource getSharedResource(
    String resourceType, Long resourceUID, String toEnterpriseID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the SharedResource(s) of a certain resource type that are
   * not synchronized with the enterprise that they are shared to.
   *
   * @param resourceType The type of the shared resource.
   * @param toEnterpriseID The ID of the enterprise.
   * @return Collection of UIDs of SharedResource(s), if found,
   * or empty Collection if no SharedResource found.
   */
  public Collection getUnsyncSharedResourceUIDs(
    String resourceType, String toEnterpriseID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a number of SharedResource(s) using a filtering condition, regardless
   * of the state of sharing.
   *
   * @param filter The filtering condition of the SharedResource(s) to find.
   * @return A Collection of SharedResource(s) found, or empty collection
   * if none exists.
   */
  public Collection getSharedResourcesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************** SHARED RESOURCE (end) ************************

  // ********************* SYNCHRONIZATION (begin) ***********************

  /**
   * Initiate a synchronization process to synchronize a resource to
   * the designated Enterprise. Synchronization will only continue if the
   * resource is not yet synchronized with the destination enterprise.
   *
   * @param sharedResourceUID The UID of the SharedResource to synchronize.
   * @param destChannelUID UID of the ChannelInfo to use for transportation.
   * @exception SynchronizeResourceException Problem with synchronization.
   */
  public void synchronizeSharedResource(
    Long sharedResourceUID, Long destChannelUID)
    throws SynchronizeResourceException, SystemException, RemoteException;

  /**
   * Initiate a synchronization process to synchronize resources of a certain
   * type to the designated Enterprise. Sychronization will only continue if
   * any of the shared resources is not yet synchronized with the destination
   * enterprise.
   *
   * @param sharedResourceUIDs Collection of UIDs of the SharedResource(s) to
   * synchronize.
   * @param destChannelUID UID of the ChannelInfo to use for transportation.
   * @exception SynchronizeResourceException Problem with synchronization.
   */
  public void synchronizeSharedResources(
    Collection sharedResourceUIDs, Long destChannelUID)
    throws SynchronizeResourceException, SystemException, RemoteException;

  /**
   * Complete the synchronization process for a shared resource.
   * The shared resource must be in an un-sync state. Otherwise, the request
   * will be ignored.
   *
   * @param sharedResourceUID The UID of the shared resource.
   * @param checkSum The synchronization checksum of the synchronized resource.
   *
   * @since 2.0 I4
   */
  public void completeSynchronization(Long sharedResourceUID, long checkSum)
    throws SynchronizeResourceException, SystemException, RemoteException;

  // ********************* SYNCHRONIZATION (end) ***********************

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResourceLinkManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 * Aug 14 2002    Neo Sok Lay         Add method getResourceLinkUIDsByFilter().
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for ResourceLinkManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IResourceLinkManagerObj
  extends        EJBObject
{

  // ******************** RESOURCE HIERARCHY (begin) ********************** //

  /**
   * Add a resource link to the enterprise hierarchy
   *
   * @param resourceLink The resource link to add.
   * @return The UID of the created ResourceLink.
   */
  public Long addResourceLink(ResourceLink resourceLink)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ResourceLink.
   *
   * @param resourceLink The link to update.
   */
  public void updateResourceLink(ResourceLink resourceLink)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Update the "To" resource links for a resource. Existing resource links
   * may be updated or deleted. New Resource links will be added.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource
   * @param toType The type of the "To" resource.
   * @param toUIDs Collection of UIDs of the ultimate set of "To" resources.
   */
  public void updateToResourceLinks(String fromType, Long fromUID,
    String toType, Collection toUIDs)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Remove a resource link from the enterprise hierarchy.
   *
   * @param resourceLinkUID The UID of the ResourceLink to remove.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of this resource link.
   */
  public void removeResourceLink(Long resourceLinkUID, boolean removeNextLinks)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Remove resource link(s) that match a filtering condition.
   *
   * @param filter The filtering condition.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of the resource links that match the deletion criterion.
   */
  public void removeResourceLinksByFilter(IDataFilter filter, boolean removeNextLinks)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Remove resource link(s) specified.
   *
   * @param linksToRemove Collection of ResourceLink(s) to be removed.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of the resource links specified.
   */
  public void removeResourceLinks(Collection linksToRemove, boolean removeNextLinks)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Get the resource links from a resource to resource of specific type.
   *
   * @param fromType The type of the "From" resource
   * @param fromUID The UID of the "From" resource
   * @param toType The type of the "To" resource
   */
  public Collection getToResourceLinks(String fromType, Long fromUID, String toType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the resource links from resource of specific type to a resource.
   *
   * @param fromType The type of the "From" resource
   * @param toType The type of the "To" resource
   * @param toUID The UID of the "To" resource
   */
  public Collection getFromResourceLinks(String fromType, String toType, Long toUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a ResourceLink.
   *
   * @param uID The UID of the ResourceLink to find.
   * @param getNextLinks <b>true</b> to retrieve the descendant links
   * in to the NextLink field, <b>false</b> otherwise.
   * @return The ResourceLink found, or <B>null</B> if none exists with that
   * UID.
   */
  public ResourceLink getResourceLinkByUID(Long resourceLinkUID, boolean getNextLinks)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a ResourceLink base on the "From" and "To" resources.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource.
   * @param toType The type of the "To" resource.
   * @param toUID The UID of the "To" resource.
   * @param getDescendantLinks <b>true</b> to retrieve the descendant links
   * in to the NextLink field, <b>false</b> otherwise.
   * @return The ResourceLink, if found, or <b>null</b> if no ResourceLink found.
   */
  public ResourceLink getResourceLink(String fromType, Long fromUID,
    String toType, Long toUID, boolean getNextLinks)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get a number of ResourceLink(s) using a filtering condition.
   *
   * @param filter The filtering condition of the ResourceLink(s) to find.
   * @param getNextLinks <b>true</b> to retrieve the descendant links
   * in to the NextLink field, <b>false</b> otherwise.
   * @return A Collection of ResourceLink(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection getResourceLinksByFilter(IDataFilter filter, boolean getNextLinks)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of a number of ResourceLink(s) using a filtering condition.
   *
   * @param filter The filtering condition of the ResourceLink(s) to find.
   * @return A Collection of UIDs of ResourceLink(s) found, or empty collection
   * if none
   * exists.
   */
  public Collection getResourceLinkUIDsByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************** RESOURCE HIERARCHY (end) ************************



}
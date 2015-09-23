/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLinkEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.enterprise.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.enterprise.entities.ejb.IResourceLinkLocalHome;
import com.gridnode.gtas.server.enterprise.entities.ejb.IResourceLinkLocalObj;
import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ResourceLink.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public final class ResourceLinkEntityHandler
  extends          LocalEntityHandler
{
  private ResourceLinkEntityHandler()
  {
    super(ResourceLink.ENTITY_NAME);
  }

  /**
   * Get an instance of a ResourceLinkEntityHandler.
   */
  public static ResourceLinkEntityHandler getInstance()
  {
    ResourceLinkEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ResourceLink.ENTITY_NAME, true))
    {
      handler = (ResourceLinkEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ResourceLink.ENTITY_NAME, true);
    }
    else
    {
      handler = new ResourceLinkEntityHandler();
      EntityHandlerFactory.putEntityHandler(ResourceLink.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IResourceLinkLocalHome.class.getName(),
      IResourceLinkLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IResourceLinkLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IResourceLinkLocalObj.class;
  }

  // ********************** Own methods ******************************

  /**
   * Add a resource link into the database.
   *
   * @param link The resource link.
   * @return The UID of the created resource link.
   */
  public Long addResourceLink(ResourceLink link)
    throws Throwable
  {
    ResourceLink created = (ResourceLink)createEntity(link);
    Long key = (Long)created.getKey();

    return key;
  }

  /**
   * Remove a resource link from the database, with an option to remove all
   * NextLinks.
   *
   * @param link The resource link to remove.
   * @param removeNextLinks <b>true</b> to remove all next links, <b>false</b>
   * to just remove the resource link.
   */
  public void removeResourceLink(ResourceLink link, boolean removeNextLinks)
    throws Throwable
  {
    ArrayList toDelete = new ArrayList();

    ArrayList linkPaths = new ArrayList();
    linkPaths.add(link.getFromResource());
    checkCanDelete(link, toDelete, removeNextLinks, true, linkPaths);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, ResourceLink.UID, toDelete, false);
    removeByFilter(filter);
  }

  /**
   * Remove a collection of resource links from the database, with an option
   * to remove all NextLinks.
   *
   * @param links The collection of ResourceLink(s) to remove.
   * @param removeNextLinks <b>true</b> to remove all next links, <b>false</b>
   * to just remove the resource link.
   */
  public void removeResourceLinks(Collection links, boolean removeNextLinks)
    throws Throwable
  {
    ArrayList toDelete = new ArrayList();

    for (Iterator i=links.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      ArrayList linkPaths = new ArrayList();
      linkPaths.add(link.getFromResource());
      checkCanDelete(link, toDelete, removeNextLinks, true, linkPaths);
    }

    if (!toDelete.isEmpty())
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, ResourceLink.UID, toDelete, false);
      removeByFilter(filter);
    }
  }

  /**
   * Check whether a resource link can be deleted from the database.
   *
   * @param resourceLink The resource link to check
   * @param toDeleteList The list of UIDs of the resource links that can be
   * deleted.
   * @param rootLink The root ResourceLink that is checked.
   * @param checkNextLink <b>true</b> to check whether the NextLinks can be
   * deleted, <b>false</b> otherwise.
   * @exception ApplicationException The resourceLink cannot be deleted, or any
   * of it's NextLink cannot be deleted (if <code>checkNextLinks</code> is
   * <b>true</b>).
   */
  private void checkCanDelete(
    ResourceLink resourceLink, ArrayList toDeleteList,
    boolean checkNextLinks, boolean retrieveNextLinks, Collection traversedPath)
    throws Throwable
  {
//   System.out.println("***CheckCanDelete::"+resourceLink.getEntityDescr() +
//   "::"+traversedPath);

    if (resourceLink.canDelete())
    {
      Long key = (Long)resourceLink.getKey();
      toDeleteList.add(key);

      if (checkNextLinks)
      {
        Collection nextLinks = retrieveNextLinks?
                                 getResourceLink(key, true).getNextLinks():
                                 resourceLink.getNextLinks();

        traversedPath.add(resourceLink.getToResource());

        for (Iterator i=nextLinks.iterator(); i.hasNext(); )
        {
          ResourceLink link = (ResourceLink)i.next();
          //don't check again if already checked
          if (!traversedPath.contains(link.getToResource()))
          {
            checkCanDelete(link, toDeleteList, true, false, traversedPath);
          }
        }
      }
    }
    else
      throw new ApplicationException("ResourceLink [" +
        resourceLink.getEntityDescr() + "] not allowed to be deleted!");
  }

  /**
   * Update the "To" resource links for a resource, that means, re-prioritise the
   * resource links. Existing resource links may be updated or removed, and new
   * resource links added.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource.
   * @param toType The Type of the "To" resources to update links.
   * @param toUIDs The ultimate set of UIDs of the "To" resources.
   */
  public void updateToResourceLinks(String fromType, Long fromUID,
    String toType, Collection toUIDs)
    throws Throwable
  {
    ArrayList toUpdateLinks = new ArrayList(getToResourceLinksIncluding(
                                fromType,
                                fromUID,
                                toType, toUIDs));

    ArrayList toRemoveLinks = new ArrayList(getToResourceLinksExcluding(
                                fromType,
                                fromUID,
                                toType, toUIDs));

    ArrayList newToUIDs = new ArrayList(toUIDs);

    ArrayList toUpdateUIDs = new ArrayList();
    ArrayList toAddLinks   = new ArrayList();

    //Existing --> update priority
    for (Iterator i=toUpdateLinks.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      int index = newToUIDs.indexOf(link.getToUID());
      link.setPriority(index);
      toUpdateUIDs.add(link.getToUID());
    }

    //New links
    int priority = 1;
    for (Iterator i=toUIDs.iterator(); i.hasNext(); priority++)
    {
      Long newToUID = (Long)i.next();
      if (!toUpdateUIDs.contains(newToUID)) //new links
      {
        ResourceLink newLink = new ResourceLink();
        newLink.setFromType(fromType);
        newLink.setFromUID(fromUID);
        newLink.setToType(toType);
        newLink.setToUID(newToUID);
        newLink.setPriority(priority);
        toAddLinks.add(newLink);
      }
    }

    //first: add
    for (Iterator i=toAddLinks.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      addResourceLink(link);
    }

    //second: remove
    removeResourceLinks(toRemoveLinks, false);

    //third: update
    for (Iterator i=toUpdateLinks.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      update(link);
    }
  }

  // *********************** Finder methods ******************************

  /**
   * Get the ResourceLinks "from" a resource "to" a resources of specific type.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource.
   * @param toType The type of the "To" resource.
   * @return A Collection of ResourceLink(s) with the specified "From" resource
   * and "To" resource of type <code>toType</code>.
   */
  public Collection getToResourceLinks(String fromType, Long fromUID, String toType)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      filter.getEqualOperator(), fromUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);

    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Get the ResourceLinks "from" a resource "to" a resources of specific type,
   * excluding those "To" resources of the specified UIDs.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource.
   * @param toType The type of the "To" resource.
   * @param excludeToUIDs The Collection of UIDs to exclude from the results.
   * @return A Collection of ResourceLink(s) with the specified "From" resource
   * and "To" resource of type <code>toType</code>, excluding those specified
   * in the <code>excludeToUIDs</code>.
   */
  public Collection getToResourceLinksExcluding(
    String fromType, Long fromUID, String toType, Collection excludeToUIDs)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      filter.getEqualOperator(), fromUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);

    if (excludeToUIDs != null && !excludeToUIDs.isEmpty())
      filter.addDomainFilter(filter.getAndConnector(), ResourceLink.TO_UID,
        excludeToUIDs, true);

    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Get the ResourceLinks "from" a resource "to" a resources of specific type,
   * including only those "To" resources of the specified UIDs.
   *
   * @param fromType The type of the "From" resource.
   * @param fromUID The UID of the "From" resource.
   * @param toType The type of the "To" resource.
   * @param includeToUIDs The Collection of UIDs to include only in the results.
   * @return A Collection of ResourceLink(s) with the specified "From" resource
   * and "To" resource of type <code>toType</code>, include only those specified
   * in the <code>includeToUIDs</code>.
   */
  public Collection getToResourceLinksIncluding(
    String fromType, Long fromUID, String toType, Collection includeToUIDs)
    throws Throwable
  {
    //no results
    if (includeToUIDs == null || includeToUIDs.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      filter.getEqualOperator(), fromUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);
    filter.addDomainFilter(filter.getAndConnector(), ResourceLink.TO_UID,
      includeToUIDs, false);

    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Get the resource links from resource of specific type to a resource.
   *
   * @param fromType The type of the "From" resource
   * @param toType The type of the "To" resource
   * @param toUID The UID of the "To" resource
   * @return A Collection of the ResourceLink(s) with the specified "To" resource
   * and "From" resource of type <code>fromType</code>.
   */
  public Collection getFromResourceLinks(String fromType, String toType, Long toUID)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
      filter.getEqualOperator(), toUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);
    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Get the resource links from resource of specific type to a resource.
   *
   * @param fromType The type of the "From" resource
   * @param toType The type of the "To" resource
   * @param toUID The UID of the "To" resource
   * @param excludeToUIDs The Collection of UIDs to exclude from the results.
   * @return A Collection of the ResourceLink(s) with the specified "To" resource
   * and "From" resource of type <code>fromType</code>, excluding those specified
   * in the <code>excludeToUIDs</code>.
   */
  public Collection getFromResourceLinksExcluding(
    String fromType, String toType, Long toUID, Collection excludeFromUIDs)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
      filter.getEqualOperator(), toUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);

    if (excludeFromUIDs != null && !excludeFromUIDs.isEmpty())
      filter.addDomainFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
        excludeFromUIDs, true);

    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Get the resource links from resource of specific type to a resource.
   *
   * @param fromType The type of the "From" resource
   * @param toType The type of the "To" resource
   * @param toUID The UID of the "To" resource
   * @param includeToUIDs The Collection of UIDs to include only in the results.
   * @return A Collection of the ResourceLink(s) with the specified "To" resource
   * and "From" resource of type <code>fromType</code>, including only those specified
   * in the <code>includeToUIDs</code>.
   */
  public Collection getFromResourceLinksIncluding(
    String fromType, String toType, Long toUID, Collection includeFromUIDs)
    throws Throwable
  {
    //no results
    if (includeFromUIDs == null || includeFromUIDs.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
      filter.getEqualOperator(), toUID, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), toType, false);
    filter.addDomainFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      includeFromUIDs, false);

    filter.setOrderFields(new Object[] {ResourceLink.PRIORITY});

    return getResourceLinksByFilter(filter, false);
  }

  /**
   * Retrieve a ResourceLink from the database, with option to retrieve also the
   * NextLinks of the resource link.
   *
   * @param uID The UID of the ResourceLink.
   * @param getNextLinks <b>true</b> to retrieve all NextLinks into the
   * NextLinks field of the ResourceLink.
   *
   * @return The retrieved ResourceLink.
   */
  public ResourceLink getResourceLink(Long uID, boolean getNextLinks)
    throws Throwable
  {
    ResourceLink link = (ResourceLink)getEntityByKeyForReadOnly(uID);
    if (getNextLinks)
    {
      ArrayList linkPaths = new ArrayList();
      linkPaths.add(uID);
      link.setNextLinks(getResourceLinksRecursive(
        link.getToType(), link.getToUID(), linkPaths));
    }

    return link;
  }

  /**
   * Retrieve ResourceLink(s) from the database using a filtering condition,
   * with option to retrieve also the NextLinks of the resource links.
   *
   * @param filter The filtering condition.
   * @param getNextLinks <b>true</b> to retrieve all NextLinks into the
   * NextLinks field of the ResourceLink(s).
   *
   * @return A Collection of the retrieved ResourceLink(s).
   */
  public Collection getResourceLinksByFilter(IDataFilter filter, boolean getNextLinks)
    throws Throwable
  {
    Collection links = getEntityByFilterForReadOnly(filter);
    if (getNextLinks)
    {
      for (Iterator i=links.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        ArrayList linkPaths = new ArrayList();
        linkPaths.add(link.getKey());
        link.setNextLinks(getResourceLinksRecursive(
          link.getToType(), link.getToUID(), linkPaths));
      }
    }

    return links;
  }

  /**
   * To recursively retrieve the ResourceLink(s) that originate "from" a
   * specified resource, i.e. all NextLinks are retrieved. The method ensures
   * that infinite loop is avoided for circular links.
   *
   * @param fromType The type of "From" resource to retrieve.
   * @param fromUID The UID of the "From" resource to retrieve.
   * @param rootLink The UID of the originating ResourceLink.
   *
   * @return A Collection of the retrieved ResourceLink(s).
   */
  private Collection getResourceLinksRecursive(
    String fromType, Long fromUID, Collection linkPaths)
    throws Throwable
  {
//System.out.println("***getResourceLinksRecursive "+fromType +"," + fromUID + ","
//  + linkPaths);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      fromType, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      filter.getEqualOperator(), fromUID, false);
    //prevent infinite loop
    filter.addDomainFilter(filter.getAndConnector(), ResourceLink.UID,
      linkPaths, true);

    Collection links = getEntityByFilterForReadOnly(filter);
    for (Iterator i=links.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      ArrayList dupLinkPaths = new ArrayList(linkPaths);
      dupLinkPaths.add(link.getKey());
      link.setNextLinks(getResourceLinksRecursive(
        link.getToType(), link.getToUID(), dupLinkPaths));
    }

    return links;
  }

  /**
   * Retrieve all links with the specified "From" resource.
   *
   * @param fromType The type of the "From" resource
   * @param fromUID The UID of the "From" resource
   * @retunr A Collection of the retrieved ResourceLink(s).
   */
//  private Collection getDirectLinks(String fromType, Long fromUID)
//    throws Throwable
//  {
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
//      fromType, false);
//    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
//      filter.getEqualOperator(), fromUID, false);
//
//    return getEntityByFilterForReadOnly(filter);
//  }
}
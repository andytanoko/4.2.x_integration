/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLinkManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 * Aug 14 2002    Neo Sok Lay         Add method getResourceLinkUIDsByFilter().
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ResourceLinkEntityHandler;
import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This bean provides services to manage the Enterprise Hierarchy.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class ResourceLinkManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3719363958799572213L;
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

  // ******************** RESOURCE HIERARCHY (begin) ********************** //

  /**
   * Add a resource link to the enterprise hierarchy
   *
   * @param resourceLink The resource link to add.
   * @return The UID of the created ResourceLink.
   */
  public Long addResourceLink(ResourceLink resourceLink)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "addResourceLink";
    Object[] params     = new Object[] {resourceLink};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = getEntityHandler().addResourceLink(resourceLink);
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
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "updateResourceLink";
    Object[] params     = new Object[] {
                            fromType, fromUID, toType, toUIDs};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().updateToResourceLinks(fromType, fromUID, toType, toUIDs);
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
   * Update a ResourceLink.
   *
   * @param resourceLink The link to update.
   */
  public void updateResourceLink(ResourceLink resourceLink)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "updateResourceLink";
    Object[] params     = new Object[] {
                            resourceLink};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().update(resourceLink);
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
   * Remove a resource link from the enterprise hierarchy.
   *
   * @param resourceLinkUID The UID of the ResourceLink to remove.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of this resource link.
   */
  public void removeResourceLink(Long resourceLinkUID, boolean removeNextLinks)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "removeResourceLink";
    Object[] params     = new Object[] {
                            resourceLinkUID,
                            removeNextLinks? Boolean.TRUE : Boolean.FALSE};

    try
    {
      logger.logEntry(methodName, params);

      ResourceLink affected = getEntityHandler().getResourceLink(resourceLinkUID, false);
      getEntityHandler().removeResourceLink(affected, removeNextLinks);
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
   * Remove resource link(s) that match a filtering condition.
   *
   * @param filter The filtering condition.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of the resource links that match the deletion criterion.
   */
  public void removeResourceLinksByFilter(IDataFilter filter, boolean removeNextLinks)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "removeResourceLinksByFilter";
    Object[] params     = new Object[] {
                            filter,
                            removeNextLinks? Boolean.TRUE : Boolean.FALSE};

    try
    {
      logger.logEntry(methodName, params);

      Collection affected = getEntityHandler().getResourceLinksByFilter(
                              filter, false);
      getEntityHandler().removeResourceLinks(affected, removeNextLinks);
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
   * Remove resource link(s) specified.
   *
   * @param linksToRemove Collection of ResourceLink(s) to be removed.
   * @param removeNextLinks <b>true</b> to also remove the all "next" links
   * of the resource links specified.
   */
  public void removeResourceLinks(Collection linksToRemove, boolean removeNextLinks)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "removeResourceLinks";
    Object[] params     = new Object[] {
                            linksToRemove,
                            removeNextLinks ? Boolean.TRUE : Boolean.FALSE};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().removeResourceLinks(linksToRemove, removeNextLinks);
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


  // ************************ Finders ***************************************

  /**
   * Get a ResourceLink.
   *
   * @param uID The UID of the ResourceLink to find.
   * @param getDescendantLinks <b>true</b> to retrieve the descendant links
   * in to the NextLink field, <b>false</b> otherwise.
   * @return The ResourceLink found, or <B>null</B> if none exists with that
   * UID.
   */
  public ResourceLink getResourceLinkByUID(Long resourceLinkUID, boolean getNextLinks)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getResourceLinkByUID";
    Object[] params     = new Object[] {
                            resourceLinkUID,
                            getNextLinks?Boolean.TRUE:Boolean.FALSE};

    ResourceLink resourceLink = null;

    try
    {
      logger.logEntry(methodName, params);

      resourceLink = (ResourceLink)getEntityHandler().getResourceLink(
                       resourceLinkUID, getNextLinks);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLink;
  }

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
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getResourceLink";
    Object[] params     = new Object[] {
                            fromType, fromUID, toType, toUID,
                            getNextLinks?Boolean.TRUE:Boolean.FALSE};

    ResourceLink resourceLink = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
        fromType, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
        filter.getEqualOperator(), fromUID, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
        filter.getEqualOperator(), toType, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
        filter.getEqualOperator(), toUID, false);

      Collection results = getEntityHandler().getResourceLinksByFilter(
                              filter, getNextLinks);

      if (results != null && !results.isEmpty())
         resourceLink = (ResourceLink)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLink;
  }


  /**
   * Get the resource links from a resource to resource of specific type.
   *
   * @param fromType The type of the "From" resource
   * @param fromUID The UID of the "From" resource
   * @param toType The type of the "To" resource
   */
  public Collection getToResourceLinks(String fromType, Long fromUID, String toType)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getToResourceLinks";
    Object[] params     = new Object[] {
                            fromType, fromUID, toType};

    Collection resourceLinks = null;

    try
    {
      logger.logEntry(methodName, params);

      resourceLinks = getEntityHandler().getToResourceLinks(
                        fromType, fromUID, toType);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLinks;
  }

  /**
   * Get the resource links from resource of specific type to a resource.
   *
   * @param fromType The type of the "From" resource
   * @param toType The type of the "To" resource
   * @param toUID The UID of the "To" resource
   */
  public Collection getFromResourceLinks(String fromType, String toType, Long toUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getFromResourceLinks";
    Object[] params     = new Object[] {
                            fromType, toType, toUID};

    Collection resourceLinks = null;

    try
    {
      logger.logEntry(methodName, params);

      resourceLinks = getEntityHandler().getFromResourceLinks(
                        fromType, toType, toUID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLinks;
  }

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
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getResourceLinksByFilter";
    Object[] params     = new Object[] {
                            filter,
                            getNextLinks?Boolean.TRUE:Boolean.FALSE};

    Collection resourceLinks = null;

    try
    {
      logger.logEntry(methodName, params);

      resourceLinks = getEntityHandler().getResourceLinksByFilter(
                        filter, getNextLinks);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLinks;
  }

  /**
   * @see IResourceLinkManagerObj#getResourceLinkUIDsByFilter
   */
  public Collection getResourceLinkUIDsByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getResourceLinkFacadeLogger();
    String methodName   = "getResourceLinkUIDsByFilter";
    Object[] params     = new Object[] {
                            filter};

    Collection resourceLinks = null;

    try
    {
      logger.logEntry(methodName, params);

      resourceLinks = getEntityHandler().getKeyByFilterForReadOnly(
                        filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return resourceLinks;
  }

  // ********************** RESOURCE HIERARCHY (end) ************************


  private ResourceLinkEntityHandler getEntityHandler()
  {
     return ResourceLinkEntityHandler.getInstance();
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseHierarchyManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 * Aug 14 2002    Neo Sok Lay         Add methods to handle update of
 *                                    enterprise hierarchy on removal of
 *                                    hierarchy resources.
 * Jan 07 2003    Neo Sok Lay         Add onCreateMyEnterprise() method.
 * Jul 11 2003    Neo Sok Lay         Add methods:
 *                                    getBizEntitiesForChannel(),
 *                                    getPartnersForChannel()
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.gtas.server.bizreg.def.DefaultBizEntityCreator;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import javax.ejb.CreateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This facade provides services to manage the Enterprise Hierarchy.
 * <p>
 * Current hierarchy structure:<p>
 * <pre>
 * Business Entity(s) -> User(s)
 * Business Entity(s) -> Channel(s)
 * Partner(s)         -> Business Entity
 * Partner(s)         -> Channel
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I4
 */
public class EnterpriseHierarchyManagerBean
  implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5547790518514960519L;
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

  // ******************* Channel - BusinessEntity *************************** //

  /**
   * Get the UIDs of the ChannelInfo(s) that are associated to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   *
   * @return A Collection of the UIDs of the ChannelInfo(s) associated with
   * the specified BusinessEntity.
   */
  public Collection getChannelsForBizEntity(Long beUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getChannelsByBizEntity";
    Object[] params     = new Object[] {
                            beUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection channelLinks = linkMgr.getToResourceLinks(
                                  IResourceTypes.BUSINESS_ENTITY,
                                  beUID,
                                  IResourceTypes.CHANNEL);

      for (Iterator i=channelLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getToUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Set the ChannelInfo(s) that are associated to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @param channelUIDs The UIDs of the ChannelInfo(s).
   */
  public void setChannelsForBizEntity(Long beUID, Collection channelUIDs)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "setChannelsForBizEntity";
    Object[] params     = new Object[] {
                            beUID, channelUIDs};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      linkMgr.updateToResourceLinks(IResourceTypes.BUSINESS_ENTITY, beUID,
        IResourceTypes.CHANNEL, channelUIDs);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Check whether a ChannelInfo has been assigned to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity
   * @param channelUID The UID of the ChannelInfo entity.
   * @return <b>true</b> if the assignment exists, <b>false</b> otherwise.
   */
  public boolean isChannelAssignedToBe(Long beUID, Long channelUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "isChannelAttachedToBe";
    Object[] params     = new Object[] {
                            beUID, channelUID};

    boolean attached = false;

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      ResourceLink resLink = linkMgr.getResourceLink(
                                  IResourceTypes.BUSINESS_ENTITY,
                                  beUID,
                                  IResourceTypes.CHANNEL,
                                  channelUID,
                                  false);
      attached = resLink != null;
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

   return attached;
  }

  /**
   * Get the BusinessEntity that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @return The UID of the BusinessEntity that the Partner is associated to.
   */
  public Long getBizEntityForPartner(Long partnerUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getBizEntityForPartner";
    Object[] params     = new Object[] {
                            partnerUID};

    Long beUID = null;

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection beLinks = linkMgr.getToResourceLinks(
                                  IResourceTypes.PARTNER,
                                  partnerUID,
                                  IResourceTypes.BUSINESS_ENTITY);

      if (beLinks != null && !beLinks.isEmpty())
      {
        ResourceLink link = (ResourceLink)beLinks.iterator().next();
        beUID = link.getToUID();
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

   return beUID;

  }

  /**
   * Set the BusinessEntity that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @param beUID The UID of the BusinessEntity.
   */
  public void setBizEntityForPartner(Long partnerUID, Long beUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "setBizEntityForPartner";
    Object[] params     = new Object[] {
                            partnerUID, beUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      ArrayList beUIDs = new ArrayList();
      if (beUID != null)
        beUIDs.add(beUID);

      linkMgr.updateToResourceLinks(IResourceTypes.PARTNER, partnerUID,
        IResourceTypes.BUSINESS_ENTITY, beUIDs);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Set the ChannelInfo that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @param channelUID The UID of the ChannelInfo.
   */
  public void setChannelForPartner(Long partnerUID, Long channelUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "setChannelForPartner";
    Object[] params     = new Object[] {
                            partnerUID, channelUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      ArrayList channelUIDs = new ArrayList();
      if (channelUID != null)
        channelUIDs.add(channelUID);

      linkMgr.updateToResourceLinks(IResourceTypes.PARTNER, partnerUID,
        IResourceTypes.CHANNEL, channelUIDs);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Get the ChannelInfo that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @return The UID of the ChannelInfo.
   */
  public Long getChannelForPartner(Long partnerUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getChannelForPartner";
    Object[] params     = new Object[] {
                            partnerUID};
    Long channelUID = null;

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection channelLinks = linkMgr.getToResourceLinks(
                                  IResourceTypes.PARTNER,
                                  partnerUID,
                                  IResourceTypes.CHANNEL);

      if (channelLinks != null && !channelLinks.isEmpty())
      {
        ResourceLink link = (ResourceLink)channelLinks.iterator().next();
        channelUID = link.getToUID();
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return channelUID;
  }

  /**
   * Get the UIDs of the BusinessEntity(s) that a User is associated to.
   *
   * @param userUID The UID of the UserAccount of the User.
   * @return A Collection of the UIDs of the BusinessEntity(s).
   */
  public Collection getBizEntitiesForUser(Long userUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getBizEntitiesForUser";
    Object[] params     = new Object[] {
                            userUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection beLinks = linkMgr.getToResourceLinks(
                                  IResourceTypes.USER,
                                  userUID,
                                  IResourceTypes.BUSINESS_ENTITY);

      for (Iterator i=beLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getToUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Set the BusinessEntity(s) that a User is associated to.
   *
   * @param userUID The UID of the UserAccount of the User.
   * @param beUIDs The Collection of UIDs that the User is associated to.
   */
  public void setBizEntitiesForUser(Long userUID, Collection beUIDs)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "setBizEntitiesByUser";
    Object[] params     = new Object[] {
                            userUID, beUIDs};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      linkMgr.updateToResourceLinks(IResourceTypes.USER, userUID,
        IResourceTypes.BUSINESS_ENTITY, beUIDs);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Get the UIDs of the Partner(s) that are attached to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @return A Collection of the UIDs of the Partner(s).
   */
  public Collection getPartnersForBizEntity(Long beUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getPartnersForBizEntity";
    Object[] params     = new Object[] {
                            beUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection pnLinks = linkMgr.getFromResourceLinks(
                                  IResourceTypes.PARTNER,
                                  IResourceTypes.BUSINESS_ENTITY,
                                  beUID);

      for (Iterator i=pnLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getFromUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Get the UIDs of the Partner(s) that are attached to a BusinessEntity.
   *
   * @param enterpriseID The EnterpriseID of the BusinessEntity.
   * @param beID The ID of the BusinessEntity.
   * @return A Collection of the UIDs of the Partner(s).
   */
  public Collection getPartnersForBizEntity(String enterpriseID, String beID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getPartnersForBizEntity";
    Object[] params     = new Object[] {
                            enterpriseID,
                            beID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      Long beUID = ServiceLookupHelper.getBizRegManager().findBusinessEntityKey(
                     enterpriseID, beID);

      if (beUID != null)
        results = getPartnersForBizEntity(beUID);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Get the UIDs of the BusinessEntity(s) that are associated to a ChannelInfo.
   *
   * @param channelUID The UID of the ChannelInfo.
   *
   * @return A Collection of the UIDs of the BusinessEntity(s) associated with
   * the specified ChannelInfo.
   */
  public Collection getBizEntitiesForChannel(Long channelUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getBizEntitiesForChannel";
    Object[] params     = new Object[] {channelUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection channelLinks = linkMgr.getFromResourceLinks(
                                  IResourceTypes.BUSINESS_ENTITY,
                                  IResourceTypes.CHANNEL,
                                  channelUID);

      for (Iterator i=channelLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getFromUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Get the UIDs of the Partner(s) that are associated to a ChannelInfo.
   *
   * @param channelUID The UID of the ChannelInfo.
   *
   * @return A Collection of the UIDs of the Partner(s) associated with
   * the specified ChannelInfo.
   */
  public Collection getPartnersForChannel(Long channelUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getPartnersForChannel";
    Object[] params     = new Object[] {
                            channelUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection channelLinks = linkMgr.getFromResourceLinks(
                                  IResourceTypes.PARTNER,
                                  IResourceTypes.CHANNEL,
                                  channelUID);

      for (Iterator i=channelLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getFromUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Get the UIDs of the UserAccount(s) that are associated to.
   * BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @return A Collection of the UIDs of the UserAccount(s).
   */
  public Collection getUsersForBizEntity(Long beUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "getUsersForBizEntity";
    Object[] params     = new Object[] {
                            beUID};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      Collection beLinks = linkMgr.getFromResourceLinks(
                                  IResourceTypes.USER,
                                  IResourceTypes.BUSINESS_ENTITY,
                                  beUID);

      for (Iterator i=beLinks.iterator(); i.hasNext(); )
      {
        ResourceLink link = (ResourceLink)i.next();
        results.add(link.getFromUID());
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;  
  }


  // **************** On removal **************************

  /**
   * @param beUID UID of removed business entity.
   * @see IEnterpriseHierarchyManagerObj#onRemoveBizEntity
   */
  public void onRemoveBizEntity(Long beUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onRemoveBizEntity";
    Object[] params     = new Object[] {
                            beUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();
      ArrayList toRemoveLinks = new ArrayList();

      DataFilterImpl filter = null;
      //partner-channel link
      Collection channelUIDs = getChannelsForBizEntity(beUID);
      if (!channelUIDs.isEmpty())
      {
        filter = new DataFilterImpl();
        filter.addSingleFilter(null, ResourceLink.TO_TYPE, filter.getEqualOperator(),
          IResourceTypes.CHANNEL, false);
        filter.addDomainFilter(filter.getAndConnector(), ResourceLink.TO_UID,
          channelUIDs, false);
        filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_TYPE,
          filter.getEqualOperator(), IResourceTypes.PARTNER, false);

        toRemoveLinks.addAll(linkMgr.getResourceLinkUIDsByFilter(filter));
      }

      //user-be, partner-be links
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.TO_TYPE, filter.getEqualOperator(),
        IResourceTypes.BUSINESS_ENTITY, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
        filter.getEqualOperator(), beUID, false);
      toRemoveLinks.addAll(linkMgr.getResourceLinkUIDsByFilter(filter));

      //be-channel links
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
        IResourceTypes.BUSINESS_ENTITY, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
        filter.getEqualOperator(), beUID, false);
      toRemoveLinks.addAll(linkMgr.getResourceLinkUIDsByFilter(filter));

      //Remove the links
      if (!toRemoveLinks.isEmpty())
      {
        filter = new DataFilterImpl();
        filter.addDomainFilter(null, ResourceLink.UID, toRemoveLinks, false);
        linkMgr.removeResourceLinksByFilter(filter, false);
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @param channelUID UID of removed channel
   * @see IEnterpriseHierarchyManagerObj#onRemoveChannel
   */
  public void onRemoveChannel(Long channelUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onRemoveChannel";
    Object[] params     = new Object[] {
                            channelUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      //remove be-channel links
      //remove partner-channel links
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.TO_TYPE, filter.getEqualOperator(),
        IResourceTypes.CHANNEL, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
        filter.getEqualOperator(), channelUID, false);

      linkMgr.removeResourceLinksByFilter(filter, false);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @param partnerUID UID of removed partner
   * @see IEnterpriseHierarchyManagerObj#onRemovePartner
   */
  public void onRemovePartner(Long partnerUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onRemovePartner";
    Object[] params     = new Object[] {
                            partnerUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      //remove partner-be link
      //remove partner-channel link
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
        IResourceTypes.PARTNER, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
        filter.getEqualOperator(), partnerUID, false);

      linkMgr.removeResourceLinksByFilter(filter, false);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @param userUID UID of removed user.
   * @see IEnterpriseHierarchyManagerObj#onRemoveUser
   */
  public void onRemoveUser(Long userUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onRemoveUser";
    Object[] params     = new Object[] {
                            userUID};

    try
    {
      logger.logEntry(methodName, params);

      IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();

      //remove user-be links
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
        IResourceTypes.USER, false);
      filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
        filter.getEqualOperator(), userUID, false);

      linkMgr.removeResourceLinksByFilter(filter, false);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  // *************** On Creation ****************************
  /**
   * @param channelUID UID of master channel created
   * @see IEnterpriseHierarchyManagerObj#onCreateMasterChannel
   */
  public void onCreateMasterChannel(Long channelUID)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onCreateMasterChannel";
    Object[] params     = new Object[] {
                            channelUID};

    try
    {
      logger.logEntry(methodName, params);

      Collection myBeUIds = getMyBeUIDs();
      if (!myBeUIds.isEmpty())
      {
        IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();
        //associate to all own bes the master channel
        for (Iterator i=myBeUIds.iterator(); i.hasNext(); )
        {
          Long beUId = (Long)i.next();
          Collection channelUIds = getChannelsForBizEntity(beUId);
          if (!channelUIds.contains(channelUID))
          {
            channelUIds.add(channelUID);
            setChannelsForBizEntity(beUId, channelUIds);
          }
        }
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * When the Enterprise is created, <p>
   * <pre>
   * 1. a default Business Entity is created for the enteprise
   * 2. All existing users are assigned this default Business Entity
   * </pre>
   * @see IEnterpriseHierarchyManagerBean#onCreateMyEnterprise
   */
  public void onCreateMyEnterprise(String enterpriseID, CompanyProfile profile)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getHierarchyFacadeLogger();
    String methodName   = "onCreateMyEnterprise";
    Object[] params     = new Object[] {
                            enterpriseID,
                            profile};

    try
    {
      logger.logEntry(methodName, params);

      // create default be
      Long beUID = DefaultBizEntityCreator.createDefaultBizEntity(
                     enterpriseID,         profile.getEmail(),
                     profile.getTel(),     profile.getFax(),
                     profile.getAddress(), profile.getCity(),
                     profile.getState(),   profile.getZipCode(),
                     profile.getCountry(), profile.getLanguage());

      // assign to all existing users
      Collection userUIDs = getUserUIDs();
      if (!userUIDs.isEmpty())
      {
        IResourceLinkManagerObj linkMgr = ServiceLookupHelper.getResourceLinkMgr();
        //associate to all own bes the master channel
        for (Iterator i=userUIDs.iterator(); i.hasNext(); )
        {
          Long userUID = (Long)i.next();
          Collection beUIDs = getBizEntitiesForUser(userUID);
          if (!beUIDs.contains(beUID))
          {
            beUIDs.add(beUID);
            setBizEntitiesForUser(userUID, beUIDs);
          }
        }
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  private Collection getMyBeUIDs() throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER, filter.getEqualOperator(),
      Boolean.FALSE, false);

    return ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter);
  }

  private Collection getUserUIDs() throws Throwable
  {
    return ServiceLookupHelper.getUserManager().findUserAccountsKeys(null);
  }
}
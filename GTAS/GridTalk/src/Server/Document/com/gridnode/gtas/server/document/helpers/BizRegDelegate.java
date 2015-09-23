/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizRegDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Koh Han Sing        Created
 * Apr 24 2003    Koh Han Sing        Add in method to get default business
 *                                    entity
 * May 21 2003    Neo Sok Lay         Set partner info with checking for
 *                                    enable state. Throw exception if
 *                                    partner not found or partner not enabled.
 * Oct 01 2003    Neo Sok Lay         Sets BizEntityUuid and RegistryQueryUrl into
 *                                    PartnerInfo in getPartnerInfo(partnerId, checkEnabled)
 * Nov 05 2003    Neo Sok Lay         Add getPartnerInfoByDuns().
 * Apr 19 2006    Neo Sok Lay         Change getPartnerInfo(): Retrieve Enabled Partner(s) only.
 * Mar 15 2007    Neo Sok Lay         Add getGridNodeManager().
 * Sep 30 2010    Tam Wei Xiang       #1897 Added getBusinessEntityByBeId(..)
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.exceptions.PartnerNotEnabledException;
import com.gridnode.gtas.server.document.exceptions.PartnerNotFoundException;
import com.gridnode.gtas.server.document.model.DocChannelInfo;
import com.gridnode.gtas.server.document.model.PartnerInfo;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerHome;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo;
import com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class provides services to retrieve partner information.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class BizRegDelegate
{
  /**
   * Obtain the EJBObject for the PartnerManagerBean.
   *
   * @return The EJBObject to the PartnerManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IPartnerManagerObj getPartnerManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerManagerHome.class.getName(),
      IPartnerManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the GridNodeManagerBean.
   *
   * @return The EJBObject to the GridNodeManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 4.1
   */
  public static IGridNodeManagerObj getGridNodeManager()
    throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridNodeManagerHome.class.getName(),
      IGridNodeManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the EnterpriseHierarchyManagerBean.
   *
   * @return The EJBObject to the EnterpriseHierarchyManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IEnterpriseHierarchyManagerObj getEnterpriseHierarchyManager()
    throws ServiceLookupException
  {
    return (IEnterpriseHierarchyManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IEnterpriseHierarchyManagerHome.class.getName(),
      IEnterpriseHierarchyManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the ChannelManagerBean.
   *
   * @return The EJBObject to the ChannelManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IChannelManagerObj getChannelManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IChannelManagerHome.class.getName(),
      IChannelManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the BizRegistryManagerBean.
   *
   * @return The EJBObject to the BizRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IBizRegistryManagerObj getBizRegistryManager()
    throws ServiceLookupException
  {
    return (IBizRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IBizRegistryManagerHome.class.getName(),
      IBizRegistryManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the PublicRegistryManagerBean.
   *
   * @return The instance of the PublicRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since GT 2.2
   */
  public static IPublicRegistryManager getPublicRegistryManager()
    throws ServiceLookupException
  {
    return (IPublicRegistryManager)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPublicRegistryManagerHome.class.getName(),
      IPublicRegistryManagerHome.class,
      new Object[0]);
  }

  /**
   * This method returns a PartnerInfo containing partner information base
   * on the partnerID given.
   *
   * @param partnerID The ID of the partner whose information is to be retrieve.
   * @return The PartnerInfo of the partner.
   * @throws PartnerNotEnabledException Thrown when the retrieve partner is not
   * in the Enabled state.
   * @throws PartnerNotFoundException Thrown when there is no partner found under
   * the specified business entity.
   *
   * @since 2.0
   */
  public static PartnerInfo getPartnerInfo(String partnerID, boolean checkEnabled) throws Exception
  {
    PartnerInfo pInfo = new PartnerInfo();
    pInfo.setPartnerID(partnerID);
    pInfo.setBizEntityID("");
    pInfo.setPartnerName("");
    pInfo.setPartnerType("");
    pInfo.setPartnerGroup("");
    pInfo.setBizEntityUuid("");
    pInfo.setRegistryQueryUrl("");

    Partner partner = getPartnerManager().findPartnerByID(partnerID);

    if (partner != null)
    {
      setPartnerInfo(pInfo, partner, checkEnabled);

      Long bizEntUid = getEnterpriseHierarchyManager().getBizEntityForPartner(
                          new Long(partner.getUId()));
      if (bizEntUid != null)
      {
        BusinessEntity bizEnt =
          getBizRegistryManager().findBusinessEntity(bizEntUid);
        if (bizEnt != null)
        {
          pInfo.setBizEntityID(bizEnt.getBusEntId());
          if (bizEnt.getEnterpriseId() != null)
          {
            pInfo.setNodeID(new Long(bizEnt.getEnterpriseId()));
          }
        }
        
        setRegistryInfo(pInfo, bizEntUid);
      }
    }
    else
    {
      throw new PartnerNotFoundException("Invalid Partner Id: "+partnerID);
    }
    return pInfo;
  }
  
  /**
   * Sets the BizEntityUuid and RegistryQueryUrl into the specified PartnerInfo for the specified Business Entity.
   * 
   * @param pInfo The PartnerInfo to modify.
   * @param bizEntityUid The UID of the Business Entity.
   * @throws Exception Error retrieving from database.
   */
  public static void setRegistryInfo(PartnerInfo pInfo, Long bizEntityUid) throws Exception
  {
    Collection mappings = getPublicRegistryManager().findRegistryObjectMappingsByProprietary(
                            BusinessEntity.ENTITY_NAME, String.valueOf(bizEntityUid));
    if (!mappings.isEmpty())
    { 
      //take the first one for now.   
      RegistryObjectMapping mapping = (RegistryObjectMapping)mappings.toArray()[0];
      pInfo.setBizEntityUuid(mapping.getRegistryObjectKey());
      pInfo.setRegistryQueryUrl(mapping.getRegistryQueryUrl());
    }
  }
  
  public static RegistryObjectMapping getRegistryInfo(
    String enterpriseId, String bizEntityID) throws Exception
  {
	RegistryObjectMapping mapping = null;  	  	
	Long key = getBizRegistryManager().findBusinessEntityKey(enterpriseId, bizEntityID);
	
    if (key != null)
    {		
	  Collection mappings = getPublicRegistryManager().findRegistryObjectMappingsByProprietary(
								BusinessEntity.ENTITY_NAME, String.valueOf(key));
	  if (!mappings.isEmpty())
	  { 
	    //take the first one for now.   
		mapping = (RegistryObjectMapping)mappings.toArray()[0];
	  }    
    }

	return mapping;
  }  
  
  
  /**
   * This method returns a PartnerInfo containing partner information base
   * on the node ID and business entity ID given.
   *
   * @param nodeID The NodeID of the gridnode whose partner information is to
   * be retrieve.
   * @param bizEntityID The business entity ID of the business entity whose
   * partner information is to be retrieve.
   * @return The PartnerInfo of the partner.
   * @throws PartnerNotEnabledException Thrown when the retrieve partner is not
   * in the Enabled state.
   * @throws PartnerNotFoundException Thrown when there is no partner found under
   * the specified business entity.
   * @since 2.0
   */
  public static PartnerInfo getPartnerInfo(Long nodeID, String bizEntityID)
    throws Exception
  {
    String nodeId = null;
    if (nodeID != null)
    {
      nodeId = nodeID.toString();
    }
    PartnerInfo pInfo = new PartnerInfo();
    pInfo.setNodeID(nodeID);
    pInfo.setBizEntityID(bizEntityID);
    pInfo.setPartnerID("");
    pInfo.setPartnerName("");
    pInfo.setPartnerType("");
    pInfo.setPartnerGroup("");

    Partner partner = null;

    Collection partners = getEnterpriseHierarchyManager().getPartnersForBizEntity(
                            nodeId,
                            bizEntityID);
    if (!partners.isEmpty())
    {
      //Long partnerUid = (Long)partners.iterator().next();
      try
      {
        //partner = getPartnerManager().findPartner(partnerUid);
      	//NSL20060419 Retrieve enabled partner(s) only
      	DataFilterImpl filter = new DataFilterImpl();
      	filter.addDomainFilter(null, Partner.UID, partners, false);
      	filter.addSingleFilter(filter.getAndConnector(), Partner.STATE, filter.getEqualOperator(), Partner.STATE_ENABLED, false);
      	Collection enabledPartners = getPartnerManager().findPartner(filter);
      	if (!enabledPartners.isEmpty())
      	{
      		EntityOrderComparator comparator = new EntityOrderComparator(Partner.UID, partners);
      		ArrayList list = new ArrayList(enabledPartners);
      		Collections.sort(list, comparator);
      		partner = (Partner)list.get(0);
      	}
      }
      catch (FindEntityException ex)
      {
        //Logger.err("[BizRegDelegate.getPartnerInfo] Invalid PartnerUid: "+partnerUid, ex);
      	Logger.warn("[BizRegDelegate.getPartnerInfo] Unable to find enabled partners, uids: "+partners, ex);
      }
    }

    if (partner != null)
    {
      setPartnerInfo(pInfo, partner, true);
    }
    else
    {
      throw new PartnerNotFoundException("Partner not found for NodeId : "+
                                          nodeID+
                                          "  BizEntityId : "+
                                          bizEntityID);
    }
    return pInfo;
  }

  public static PartnerInfo getPartnerInfo(String bizEntityUuid, String registryQueryUrl, boolean findPartner)
    throws Exception
  {
    PartnerInfo pInfo = new PartnerInfo();
    pInfo.setPartnerID("");
    pInfo.setBizEntityID("");
    pInfo.setPartnerName("");
    pInfo.setPartnerType("");
    pInfo.setPartnerGroup("");
    pInfo.setBizEntityUuid(bizEntityUuid);
    pInfo.setRegistryQueryUrl(registryQueryUrl);
    
    RegistryObjectMapping mapping = getPublicRegistryManager().findRegistryObjectMapping(
                                      OrganizationInfo.getTypeDescription(OrganizationInfo.TYPE_ORGANIZATION),
                                      bizEntityUuid, registryQueryUrl);
    if (mapping != null)
    {
      // set biz entity info
      Long bizEntityUid = Long.valueOf(mapping.getProprietaryObjectKey());
      BusinessEntity bizEnt =
        getBizRegistryManager().findBusinessEntity(bizEntityUid);
      if (bizEnt != null)
      {
        pInfo.setBizEntityID(bizEnt.getBusEntId());
        if (bizEnt.getEnterpriseId() != null)
        {
          pInfo.setNodeID(new Long(bizEnt.getEnterpriseId()));
        }
      }
      
      // set Partner info
      if (findPartner)
      {
        PartnerInfo tmpInfo = getPartnerInfo(pInfo.getNodeID(), pInfo.getBizEntityID());
        pInfo.setPartnerGroup(tmpInfo.getPartnerGroup());
        pInfo.setPartnerID(tmpInfo.getPartnerID());
        pInfo.setPartnerName(tmpInfo.getPartnerName());
        pInfo.setPartnerType(tmpInfo.getPartnerType());
      }
    }
    else
    throw new PartnerNotFoundException("Partner not found for BizEntityUuid : "+
                                        bizEntityUuid+
                                        "  RegistryQueryUrl : "+
                                        registryQueryUrl);
                                        
    return pInfo;     
  }

  /**
   * Get PartnerInfo object by retrieving Business entities by Duns number.
   * 
   * @param duns The duns number.
   * @param findPartner <b>true</b> to find the Partner of the retrieved BusinessEntity, <b>false</b>
   * otherwise.
   * @return The PartnerInfo object.
   * @throws Exception No business entity found by DUNS, or no partner found.
   */  
  public static PartnerInfo getPartnerInfoByDuns(String duns, boolean findPartner)
    throws Exception
  {
    PartnerInfo pInfo = new PartnerInfo();
    pInfo.setPartnerID("");
    pInfo.setBizEntityID("");
    pInfo.setPartnerName("");
    pInfo.setPartnerType("");
    pInfo.setPartnerGroup("");
    pInfo.setBizEntityUuid("");
    pInfo.setRegistryQueryUrl("");
    
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WhitePage.DUNS, filter.getEqualOperator(), duns, false);
    Collection results = getBizRegistryManager().findBusinessEntitiesByWhitePage(filter);
    if (results.size() > 0)
    {
      BusinessEntity be = (BusinessEntity)results.iterator().next();
      pInfo.setBizEntityID(be.getBusEntId());
      if (be.getEnterpriseId() != null)
      {
        pInfo.setNodeID(new Long(be.getEnterpriseId()));
      }

      // set Partner info
      if (findPartner)
      {
        PartnerInfo tmpInfo = getPartnerInfo(pInfo.getNodeID(), pInfo.getBizEntityID());
        pInfo.setPartnerGroup(tmpInfo.getPartnerGroup());
        pInfo.setPartnerID(tmpInfo.getPartnerID());
        pInfo.setPartnerName(tmpInfo.getPartnerName());
        pInfo.setPartnerType(tmpInfo.getPartnerType());
      }
    }
    else
      throw new PartnerNotFoundException("Partner Info not found for DUNS: "+duns);
    
    return pInfo;  
  }
  
  private static void setPartnerInfo(PartnerInfo pInfo,
                                     Partner partner,
                                     boolean check)
    throws PartnerNotEnabledException
  {
    if (!check || partner.getState() == Partner.STATE_ENABLED)
    {
      pInfo.setPartnerID(partner.getPartnerID());
      pInfo.setPartnerName(partner.getName());
      Logger.debug("[BizRegDelegate.checkSetPartnerInfo] PartnerType = "+
                   partner.getPartnerType().getName());
      pInfo.setPartnerType(partner.getPartnerType().getName());
      if (partner.getPartnerGroup() != null)
      {
        pInfo.setPartnerGroup(partner.getPartnerGroup().getName());
      }
    }
    else
      throw PartnerNotEnabledException.create(partner);
  }

  /**
   * This method returns the uis of the DocChannelInfo containing channel
   * information base on the partnerId given.
   *
   * @param partnerId The id of the partner whose channel information is to be
   * retrieve.
   * @return The DocChannelInfo of the partner.
   *
   * @since 2.0
   */
  public static DocChannelInfo getPartnerDefaultChannelInfo(String partnerId)
    throws Exception
  {
    DocChannelInfo channelInfo = null;
    Partner partner = getPartnerManager().findPartnerByID(partnerId);
    if (partner != null)
    {
      Long channelUid = getEnterpriseHierarchyManager().getChannelForPartner(
                             new Long(partner.getUId()));
      if (channelUid != null)
      {
        ChannelInfo cInfo = getChannelManager().getChannelInfo(channelUid);
        if (cInfo != null)
        {
          channelInfo = new DocChannelInfo();
          channelInfo.setChannelUid(new Long(cInfo.getUId()));
          channelInfo.setChannelName(cInfo.getName());
          channelInfo.setChannelProtocol(cInfo.getTptProtocolType());
        }
      }
    }
    return channelInfo;
  }

  public static BusinessEntity getDefaultBusinessEntity()
    throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ID, filter.getEqualOperator(),
      "DEF", false);
    Collection bizEnts = getBizRegistryManager().findBusinessEntities(filter);
    if (!bizEnts.isEmpty())
    {
      return (BusinessEntity)bizEnts.iterator().next();
    }
    return null;
  }
  
  /**
   * TWX 20100930 #1897 
   * Retrieve the BusinessEntity given the beId and enterprise id.
   * @param beId The business entity id
   * @param enterpriseId The business entity Enterprise id
   * @param isPartner Indicate whether the business entity belong to own or partner
   * @return the the BusinessEntity given the beId or null if no match.
   * @throws Exception thrown if problem in retrieving the Business entity
   */
  public static BusinessEntity getBusinessEntityByBeId(String beId, String enterpriseId, boolean isPartner) throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ID, filter.getEqualOperator(), beId, false);
    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.ENTERPRISE_ID, filter.getEqualOperator(), enterpriseId, false);
    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.IS_PARTNER, filter.getEqualOperator(), isPartner, false);
    
    Logger.debug("getBusinessEntityByBeId: "+filter);
    
    Collection bizEnts = getBizRegistryManager().findBusinessEntities(filter);
    if (!bizEnts.isEmpty())
    {
      return (BusinessEntity)bizEnts.iterator().next();
    }
    return null;
  }
}
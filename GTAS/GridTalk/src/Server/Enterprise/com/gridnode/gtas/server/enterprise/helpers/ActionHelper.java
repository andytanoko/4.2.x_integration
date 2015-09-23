/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 * Aug 09 2002    Neo Sok Lay         Add methods to manipulate the enterprise
 *                                    hierarchy.
 * Aug 26 2002    Neo Sok Lay         Add method to verifyOwnBusinessEntity()
 * Nov 02 2002    Neo Sok Lay         Add methods:
 *                                    - getPartnerBusinessEntities(),
 *                                    - getPartner(),
 *                                    - getConnectionStatus()
 * Jan 23 2003    Neo Sok Lay         GNDB00012402: getPartners() to get only
 *                                    Enabled partners.
 * Jul 11 2003    Neo Sok Lay         Add methods: 
 *                                    getExistingUserAccounts(Collection of UIDs),
 *                                    getExistingPartners(Collection of UIDs),
 * Aug 14 2003    Neo Sok Lay         Add methods:
 *                                    - getOwnBusinessEntities(Collection of UIDs)
 * Sep 08 2003    Neo Sok Lay         Add methods:
 *                                    - getMyGridNodeId()
 *                                    - copyEntityFields(from,fromFieldIds,to,toFieldIds)
 *                                    - setEntityFields(entity,fieldIds,fieldValues)
 * 15 Dec 2005    SC                  Add method: verifyBusinessEntity(String businessEntityId, String enterpriseId)
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.bizreg.def.DefaultBizEntityCreator;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.enterprise.exceptions.AssociateLinkException;
import com.gridnode.gtas.model.bizreg.BizRegEntityFieldID;
import com.gridnode.gtas.model.channel.ChannelInfoEntityFieldId;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the Enterprise module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public final class ActionHelper
{
  private static final BitSet _stateSet = new BitSet();
  private static final BitSet _stateSet2 = new BitSet();
  private static final BitSet _userStateSet = new BitSet();
  static
  {
    _stateSet.set((int)BusinessEntity.STATE_DELETED);
    _stateSet2.set((int)BusinessEntity.STATE_DELETED);
    _stateSet2.set((int)IBusinessEntity.STATE_INACTIVE);
    _stateSet2.set((int)IBusinessEntity.STATE_PENDING);
    _userStateSet.set((int)UserAccountState.STATE_DELETED);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of an UserAccount based on the specified uID.
   *
   * @param uID The UID of the UserAccount.
   * @return The UserAccount retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the UserManagerBean.
   * @exception IllegalArgumentException Bad User UID. No UserAccount exists with the specified
   * uID.
   *
   * @since 2.0 I4
   */
  public static UserAccount verifyUserAccount(Long uID)
    throws Exception
  {
    try
    {
      return ServiceLookupHelper.getUserManager().findUserAccount(uID);
    }
    catch (FindEntityException ex)
    {
      throw new IllegalArgumentException("Bad User UID: "+uID);
    }
  }

  /**
   * Verify the existence of a BusinessEntity based on the specified uID.
   *
   * @param uID The UID of the BusinessEntity.
   * @return The BusinessEntity retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the BizRegManagerBean.
   * @exception IllegalArgumentException Bad BE UID. No BusinessEntity exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static BusinessEntity verifyBusinessEntity(Long uID)
    throws Exception
  {
    try
    {
      return ServiceLookupHelper.getBizRegManager().findBusinessEntity(uID);
    }
    catch (FindEntityException ex)
    {
      throw new IllegalArgumentException("Bad BusinessEntity UID: "+uID);
    }
  }

  /**
   * Verify the existence of a BusinessEntity based on the specified businessEntityId and enterpriseId.
   *
   * @param businessEntityId id of the biz ent
   * @parame enterpriseId enterprise id of the biz ent
   * @return The BusinessEntity retrieved using the specified businessEntityId and enterpriseId.
   * @exception ServiceLookupException Error in obtaining the BizRegManagerBean.
   * @exception IllegalArgumentException Bad businessEntityId and enterpriseId. No BusinessEntity exists with the specified
   * businessEntityId and enterpriseId.
   */
  public static BusinessEntity verifyBusinessEntity(String businessEntityId, String enterpriseId) throws Exception
  {
    try
    {
      BusinessEntity ret = ServiceLookupHelper.getBizRegManager().findBusinessEntity(enterpriseId, businessEntityId);
      if (ret == null)
      {
        throw new IllegalArgumentException("Bad businessEntityId = " + businessEntityId + ", enterpriseId = " + enterpriseId);
      }
	  else 
      {
		return ret;
      }
    }
    catch (FindEntityException ex)
    {
      throw new IllegalArgumentException("Bad businessEntityId = " + businessEntityId + ", enterpriseId = " + enterpriseId);
    }
  }
  
  /**
   * Verify the existence of a Partner based on the specified uID.
   *
   * @param uID The UID of the Partner.
   * @return The Partner retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the PartnerManagerBean.
   * @exception IllegalArgumentException Bad Partner UID. No Partner exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static Partner verifyPartner(Long uID)
    throws Exception
  {
    try
    {
      return ServiceLookupHelper.getPartnerManager().findPartner(uID);
    }
    catch (FindEntityException ex)
    {
      throw new IllegalArgumentException("Bad Partner UID: "+uID);
    }
  }

  /**
   * Verify the existence of a ChannelInfo based on the specified uID.
   *
   * @param uID The UID of the ChannelInfo.
   * @return The ChannelInfo retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the ChannelManagerBean.
   * @exception IllegalArgumentException Bad ChannelInfo UID. No ChannelInfo exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static ChannelInfo verifyChannelInfo(Long uID)
    throws Exception
  {
    try
    {
      return ServiceLookupHelper.getChannelManager().getChannelInfo(uID);
    }
    catch (FindEntityException ex)
    {
      throw new IllegalArgumentException("Bad Partner UID: "+uID);
    }
  }

  /**
   * Verify that a ChannelInfo has been assigned to a BusinessEntity.
   *
   * @param beUID UID of the BusinessEntity
   * @param channelUID UID of the ChannelInfo.
   * @exception AssociateLinkException ChannelInfo has not been assigned to
   * the BusinessEntity.
   */
  public static void verifyChannelAssignedToBe(Long beUID, Long channelUID)
    throws Exception
  {
    boolean assigned = ServiceLookupHelper.getEnterpriseHierarchyMgr().isChannelAssignedToBe(
                         beUID, channelUID);

    if (!assigned)
      throw new AssociateLinkException("Specified ChannelInfo does not belong the BusinessEntity!");
  }

  /**
   * Verify that the specified UIDs of BusinessEntity(s) do not belong to
   * any partner in order to associate the BusinessEntity(s) to some resource.
   *
   * @param beUIDs UIDs of the BusinessEntity(s)
   * @param assocParty The resource to associate the BusinessEntity(s) t.
   * @exception AssociateLinkException Specified <code>beUIDs</code> contain
   * UIDs of partner's BusinessEntity(s).
   */
  public static void verifyNonPartnerBusinessEntity(Collection beUIDs,
    String assocParty)
    throws Exception
  {
    if (beUIDs == null || beUIDs.isEmpty())
      return;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
      filter.getEqualOperator(), Boolean.TRUE, false);
    filter.addDomainFilter(filter.getAndConnector(), BusinessEntity.UID,
      beUIDs, false);

    Collection partnerBEs = ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter);

    if (!partnerBEs.isEmpty())
    {
      throw new AssociateLinkException("Not allowed to associate "+assocParty + " to partner's Business Entities!");
    }
  }

  /**
   * Verify that the specified BusinessEntity UIDs belong to this enterprise
   * in order to qualify for sending. All specified UIDs must exist and must not
   * belong to partner BusinessEntity(s).
   *
   * @param beUIDs The UIDs of the BusinessEntity(s) to send.
   * @exception IllegalArgumentException Specified <code>beUIDs</code> is null
   * or empty, or some of the UIDs do not exist, or belong to partner BusinessEntity(s).
   */
  public static void verifyOwnBusinessEntity(Collection beUIDs)
    throws Exception
  {
    if (beUIDs == null || beUIDs.isEmpty())
      throw new IllegalArgumentException("Invalid BusinessEntity(s) specified");

    Collection existBes = ActionHelper.getExistBeUIDs(beUIDs);

    if (!existBes.containsAll(beUIDs))
      throw new IllegalArgumentException("Invalid BusinessEntity to send! Some BusinessEntity(s) do not exist!");

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
      filter.getEqualOperator(), Boolean.TRUE, false);
    filter.addDomainFilter(filter.getAndConnector(), BusinessEntity.UID,
      beUIDs, false);

    Collection partnerBEs = ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter);

    if (!partnerBEs.isEmpty())
    {
      throw new IllegalArgumentException("Not allowed to send partner's Business Entities!");
    }
  }

  /**
   * Check if a BusinessEntity belongs to this enterprise.
   *
   * @param uid UID of the BusinessEntity
   * @return <b>true</b> if this BusinessEntity is own, <b>false</b> otherwise.
   */
  public static boolean isOwnBusinessEntity(Long uid)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
      filter.getEqualOperator(), Boolean.FALSE, false);
    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.UID,
      filter.getEqualOperator(), uid, false);

    Collection myBe = ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter);

    return !myBe.isEmpty();
  }

  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
  public static boolean isEmpty(String val)
  {
    return val==null || val.trim().length() == 0;
  }

  // ******************** Conversion Helpers ******************************

  /**
   * Convert a collection of BusinessEntity(s) to Map objects.
   *
   * @param beList The collection of BusinessEntity(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of BusinessEntity(s).
   *
   * @since 2.0 I4
   */
  public static Collection convertBEsToMapObjects(Collection beList)
  {
    return BusinessEntity.convertEntitiesToMap(
             (BusinessEntity[])beList.toArray(new BusinessEntity[beList.size()]),
             BizRegEntityFieldID.getEntityFieldID(),
             null);
  }
  
  /**
   * Convert a collection of Partner(s) to Map objects.
   *
   * @param partnerList The collection of Partner(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Partner(s).
   */
  public static Collection convertPartnersToMapObjects(Collection partnerList)
  {
  	return Partner.convertEntitiesToMap((Partner[]) partnerList
				.toArray(new Partner[partnerList.size()]), PartnerEntityFieldID
				.getEntityFieldID(), null);
  }

  /**
   * Convert a collection of ChannelInfo(s) to Map objects.
   *
   * @param channelList The collection of ChannelInfo(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ChannelInfo(s).
   *
   * @since 2.0 I4
   */
  public static Collection convertChannelsToMapObjects(Collection channelList)
  {
    return ChannelInfo.convertEntitiesToMap(
             (ChannelInfo[])channelList.toArray(new ChannelInfo[channelList.size()]),
             ChannelInfoEntityFieldId.getEntityFieldID(),
             null);
  }

  /**
   * Convert an BusinessEntity to Map object.
   *
   * @param bizEntity The BusinessEntity to convert.
   * @return A Map object converted from the specified BusinessEntity.
   *
   * @since 2.0 I4
   */
  public static Map convertBusinessEntityToMap(BusinessEntity bizEntity)
  {
    return BusinessEntity.convertToMap(
             bizEntity,
             BizRegEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a ChannelInfo to Map object.
   *
   * @param channel The ChannelInfo to convert.
   * @return A Map object converted from the specified ChannelInfo.
   *
   * @since 2.0 I4
   */
  public static Map convertChannelInfoToMap(ChannelInfo channel)
  {
    return ChannelInfo.convertToMap(
             channel,
             ChannelInfoEntityFieldId.getEntityFieldID(),
             null);
  }

  /**
   * Copy fields from a Map object into an entity.
   * @param from The Map object to copy fields from.
   * @param entity The entity object to copy fields to.
   */
  public static void copyEntityFields(Map from, AbstractEntity entity)
  {
    if (from != null)
    {
      for (Iterator i=from.keySet().iterator(); i.hasNext(); )
      {
        Number fieldID = (Number)i.next();
        entity.setFieldValue(fieldID, from.get(fieldID));
      }
    }
  }

  /**
   * Copy field values from one entity to another which may be of a different type.
   * 
   * @param from The entity to copy field values from.
   * @param fromFieldIds The FieldIds of the fields to copy.
   * @param to The entity to copy field values to.
   * @param toFieldIds The FieldIds of the fields to copy for. This must correspond to the
   * <code>fromFieldIds</code> in the number of fields to copy.
   */
  public static void copyEntityFields(AbstractEntity from, Number[] fromFieldIds, AbstractEntity to, Number[] toFieldIds)
  {
    for (int i=0; i<fromFieldIds.length; i++)
    {
      to.setFieldValue(toFieldIds[i], from.getFieldValue(fromFieldIds[i]));  
    }
  }
  
  /**
   * Sets field values to an entity.
   * 
   * @param entity The entity to set field values.
   * @param fieldIds FieldIds of the fields to set values for.
   * @param fieldValues The field values to set. The order must correspond to
   * the respective fields indicated by <code>fieldIds</code>.
   */
  public static void setEntityFields(AbstractEntity entity, Number[] fieldIds, Object[] fieldValues)
  {
    for (int i=0; i<fieldIds.length; i++)
    {
      entity.setFieldValue(fieldIds[i], fieldValues[i]);
    }
  }
  
  // ********************** Finders ****************************************
  /**
   * Get a Collection of BusinessEntity(s) based on the specified UIDs.
   * @param beUIDs Collection of UIDs of BusinessEntity(s) to return.
   * @return Collection of BusinessEntity(s) retrieved from database, excluding
   * those marked deleted.
   * @throws Exception
   */
  public static Collection getBusinessEntities(Collection beUIDs)
    throws Exception
  {
    if (beUIDs == null || beUIDs.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, BusinessEntity.UID, beUIDs, false);
    ArrayList beList = new ArrayList(ServiceLookupHelper.getBizRegManager().findBusinessEntities(
                          filter, _stateSet));

    EntityOrderComparator comparator = new EntityOrderComparator(
                                         BusinessEntity.UID, beUIDs);
    Collections.sort(beList, comparator);

    return beList;
  }

  /**
   * Determine the subset of UIDs that BusinessEntity(s) exist.
   * @param beUIDs Collection of UIDs to check.
   * @return Collection of UIDs that BusinessEntity(s) exist.
   * @throws Exception
   */
   public static Collection getExistBeUIDs(Collection beUIDs)
    throws Exception
  {
     if (beUIDs == null || beUIDs.isEmpty())
       return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, BusinessEntity.UID, beUIDs, false);

    Collection existBeUIDs = ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter);

    ArrayList orderedBeUIDs = new ArrayList(beUIDs);
    orderedBeUIDs.retainAll(existBeUIDs);

    return orderedBeUIDs;
  }

  /**
   * Determine the subset of UIDs that ChannelInfo(s) exist.
   * @param channelUIDs Collection of UIDs to check.
   * @return Collection of UIDs that ChannelInfo(s) exist.
   * @throws Exception
   */
  public static Collection getExistChannelUIDs(Collection channelUIDs)
    throws Exception
  {
    if (channelUIDs == null || channelUIDs.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, ChannelInfo.UID, channelUIDs, false);

    Collection existCnUIDs = ServiceLookupHelper.getChannelManager().getChannelInfoUIDs(filter);
    ArrayList orderedCnUIDs = new ArrayList(channelUIDs);

    orderedCnUIDs.retainAll(existCnUIDs);

    return orderedCnUIDs;
  }

  /**
   * Get the ChannelInfo(s) that exist, based on the specified list of
   * UIDs.
   * @param channelUIDs Collection of UIDs to check.
   * @return Collection of ChannelInfo(s) retrieved based on the specified
   * UID list.
   * @throws Exception
   */
  public static Collection getChannels(Collection channelUIDs)
    throws Exception
  {
    if (channelUIDs == null || channelUIDs.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, ChannelInfo.UID, channelUIDs, false);

    ArrayList channelList = new ArrayList(ServiceLookupHelper.getChannelManager().getChannelInfo(filter));

    EntityOrderComparator comparator = new EntityOrderComparator(
                                         ChannelInfo.UID, channelUIDs);
    Collections.sort(channelList, comparator);

    return channelList;
  }

  /**
   * Retrieve the UID of the Master channel of this GridTalk.
   *
   * @return UID of the Master channel, or <b>null</b> if Master channel not
   * created yet.
   */
  public static Long getMasterChannelUID() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.IS_MASTER, filter.getEqualOperator(),
      Boolean.TRUE, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.IS_PARTNER,
      filter.getEqualOperator(), Boolean.FALSE, false);

    Collection masterChannel = ServiceLookupHelper.getChannelManager().getChannelInfoUIDs(filter);
    Long uid = null;
    if (!masterChannel.isEmpty())
      uid = (Long)masterChannel.iterator().next();

    return uid;
  }

  /**
   * Get the BusinessEntity that a Partner is currently attached to.
   * @param partnerUID UID of the Partner.
   * @return The BusinessEntity that the partner is currently attached to, or
   * <b>null</b> if none exists.
   * @throws Exception
   */
  public static BusinessEntity getBeAttachedToPartner(Long partnerUID)
    throws Exception
  {
    BusinessEntity be = null;

    Long beUID = ServiceLookupHelper.getEnterpriseHierarchyMgr().getBizEntityForPartner(
                   partnerUID);

    if (beUID != null)
    {
      be = ServiceLookupHelper.getBizRegManager().findBusinessEntity(beUID);
    }

    return be;
  }

  /**
   * Get the ChannelInfo assigned to a Partner.
   *
   * @param partnerUID UID of the Partner.
   * @return The ChannelInfo assigned to the Partner, or <b>null</b> if none
   * exists.
   * @throws Exception
   */
  public static ChannelInfo getChannelAttachedToPartner(Long partnerUID)
    throws Exception
  {
    ChannelInfo channel = null;

    Long channelUID = ServiceLookupHelper.getEnterpriseHierarchyMgr().getChannelForPartner(
                        partnerUID);

    if (channelUID != null)
    {
      channel = ServiceLookupHelper.getChannelManager().getChannelInfo(channelUID);
    }

    return channel;
  }

  /**
   * Retrieve all partner BusinessEntity(s) whose states are either NORMAL or
   * ACTIVE.
   *
   * @return Collection of BusinessEntity(s) that belong to trading partners.
   */
  public static Collection getPartnerBusinessEntities()
  {
    ArrayList bes = new ArrayList();
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
        filter.getEqualOperator(), Boolean.TRUE, false);

      bes.addAll(ServiceLookupHelper.getBizRegManager().findBusinessEntities(
        filter, _stateSet2));
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.getPartnerBusinessEntities] Error ", ex);
    }

    return bes;
  }

  public static Collection getPartnersForBusinessEntity(Long beUID)
  {
    ArrayList partnerUIDs = new ArrayList();
    try
    {
      partnerUIDs.addAll(ServiceLookupHelper.getEnterpriseHierarchyMgr().
        getPartnersForBizEntity(beUID));
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.getPartnersForBusinessEntity] Error ", ex);
    }

    return partnerUIDs;
  }

  public static Collection getPartners(Collection uids)
  {
    ArrayList partners = new ArrayList();
    try
    {
      if (!uids.isEmpty())
      {
        DataFilterImpl filter = new DataFilterImpl();
        filter.addDomainFilter(null, Partner.UID, uids, false);
        filter.addSingleFilter(
          filter.getAndConnector(),
          Partner.STATE,
          filter.getEqualOperator(),
          new Short(Partner.STATE_ENABLED),
          false);

        partners.addAll(ServiceLookupHelper.getPartnerManager().
          findPartner(filter));
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.getPartners] Error ", ex);
    }

    return partners;
  }

  /**
   * Retrieve a Partner.
   *
   * @param uid The UID of the Partner to retrieve.
   * @return The Partner retrieved, or <b>null</b> if no such Partner exists.
   */
  public static Partner getPartner(Long uid)
  {
    Partner partner = null;
    try
    {
      partner = ServiceLookupHelper.getPartnerManager().findPartner(uid);
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.getPartner] Error ", ex);
    }
    return partner;
  }

  public static String[] getPartnerGridNodeIDs(Collection partnerGnUIDs)
    throws Exception
  {
    String[] gnIDs = null;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridNode.UID, partnerGnUIDs, false);
    filter.addSingleFilter(filter.getAndConnector(), GridNode.STATE,
      filter.getEqualOperator(), new Short(GridNode.STATE_ACTIVE), false);

    Object[] results = ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(
                           filter).toArray();

    if (results.length == 0)
      throw new Exception("Selected GridNodes are not partners! "+partnerGnUIDs);

    gnIDs = new String[results.length];
    for (int i=0; i<results.length; i++ )
    {
      GridNode gn = (GridNode)results[i];
      gnIDs[i] = gn.getID();
    }

    return gnIDs;
  }

  /**
   * Retrieve the UIDs of the Master channels for the specified enterprises.
   *
   * @param enterpriseIDs IDs of the enterpriseIDs (GridNodeIDs).
   * @return UIDs of the Master channels retrieved in order of the enterpriseIDs.
   * @throws Exception If any of the enterprises do not have a master channel.
   */
  public static Long[] getMasterChannelUIDs(String[] enterpriseIDs)
    throws Exception
  {
    Long[] channelUIDs = new Long[enterpriseIDs.length];

    for (int i=0; i<channelUIDs.length; i++)
    {
      channelUIDs[i] = getMasterChannelUID(enterpriseIDs[i]);
    }

    return channelUIDs;
  }

  /**
   * Retrieve the UID of the Master channel of a GridNode
   *
   * @param refID ReferenceId being the GridNodeID of the GridNode.
   * @return UID of the Master channel
   * @throws Exception No Master Channel found.
   */
  public static Long getMasterChannelUID(String refID) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.IS_MASTER, filter.getEqualOperator(),
      Boolean.TRUE, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.REF_ID,
      filter.getEqualOperator(), refID, false);

    Collection masterChannel = ServiceLookupHelper.getChannelManager().getChannelInfoUIDs(filter);
    if (masterChannel.isEmpty())
      throw new Exception("No Master Channel found for GridNode "+refID);

    Long uid = (Long)masterChannel.iterator().next();

    return uid;
  }

  /**
   * Retrieve the ConnectionStatus for a particular GridNode.
   *
   * @param gridnodeID the GridNode ID of the GridNode.
   * @return ConnectionStatus for the GridNode, or <b>null</b> if no such
   * GridNode exists.
   */
  public static ConnectionStatus getConnectionStatus(String gridnodeID)
  {
    ConnectionStatus connStatus = null;
    try
    {
      connStatus = ServiceLookupHelper.getGridNodeManager().findConnectionStatusByNodeID(
                    gridnodeID);
    }
    catch (Exception ex)
    {
      Logger.warn("[ActionHelper.getConnectionStatus] Error ", ex);
    }
    return connStatus;
  }

  public static Long getDefaultBeUID(String enterpriseId)
  {
    return DefaultBizEntityCreator.getDefaultBizEntityUID(enterpriseId);
  }

  /**
   * Get the UserAccounts based on the specified UIDs, excluding those
   * marked deleted.
   * 
   * @param uids Collection of UIDs of the UserAccounts to retrieve.
   * @return Collection of UserAccount entities retrieved.
   * @throws Throwable Error while retrieving from UserManager.
   */
  public static Collection getExistingUserAccounts(Collection uids)
    throws Throwable
  {
    if (uids == null || uids.isEmpty())
      return new ArrayList();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, UserAccount.UID, uids, false);
 
    return ServiceLookupHelper.getUserManager().findUserAccounts(
                          filter, _userStateSet);

  }

  /**
   * Get the Partners based on the specified UIDs, excluding those
   * marked deleted.
   * 
   * @param uids Collection of UIDs of the Partners to retrieve.
   * @return Collection of Partner entities retrieved.
   * @throws Throwable Error while retrieving from PartnerManager.
   */
  public static Collection getExistingPartners(Collection uids)
    throws Throwable
  {
    if (uids == null || uids.isEmpty())
      return new ArrayList();
    
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, Partner.UID, uids, false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Partner.STATE,
      filter.getNotEqualOperator(),
      new Short(Partner.STATE_DELETED),
      false);

     return ServiceLookupHelper.getPartnerManager().findPartner(filter);
  }

  /**
   * Get the BusinessEntity(s) that belong to this enterprise i.e. Non-partner
   *
   * @param beUIDs UIDs of the BusinessEntity(s)
   * @return Collection of BusinessEntity(s) retrieved. The size
   * of the return collection may not equal to the beUIDs due to 
   * repeated UIDs or invalid UIDs.
   */
  public static Collection getOwnBusinessEntities(Collection beUIDs)
    throws Exception
  {
    ArrayList myBes = new ArrayList();
    
    if (beUIDs == null || beUIDs.isEmpty())
      return myBes;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
      filter.getEqualOperator(), Boolean.FALSE, false);
    filter.addDomainFilter(filter.getAndConnector(), BusinessEntity.UID,
      beUIDs, false);

    myBes.addAll(ServiceLookupHelper.getBizRegManager().findBusinessEntitiesKeys(filter));

    return myBes; 
  }
  
  /**
   * Get the ChannelInfo(s) attached to the specified BusinessEntity (UID).
   * 
   * @param beUid UID of the BusinessEntity.
   * @return Collection of ChannelInfo(s)
   * @throws Exception Error in retrieving.
   */
  public static Collection getChannelsAttachedToBE(Long beUid)
    throws Exception
  {
    Collection channelUids = ServiceLookupHelper.getEnterpriseHierarchyMgr().getChannelsForBizEntity(beUid);
    
    return getChannels(channelUids);
  }
  
  /**
   * Gets the GridNodeID for this GridTalk.
   * 
   * @return GridNodeId for this GridTalk.
   */
  public static String getMyGridNodeId() throws Exception
  {
    return ServiceLookupHelper.getGridNodeManager().findMyGridNode().getID();
  }
 
}
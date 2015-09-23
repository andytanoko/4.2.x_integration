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
 * Jul 30 2002    Neo Sok Lay         Created
 * Sep 01 2003    Neo Sok Lay         Add conversion helper for RegistryConnectInfoList.
 * Sep 19 2003    Neo Sok Lay         Add conversion helpers:
 *                                    - convertRegConnInfoToMap(RegistryConnectInfo)
 *                                    - convertRegConnInfosToMapObjects(Collection)
 * Sep 24 2003    Neo Sok Lay         Move RegistryConnectInfo to PDIPAppServices/BizRegistry
 *                                    Remove convertRegConnectInfoListToMap()
 * Dec 23 2003    Neo Sok Lay         Add methods:
 *                                    - checkDomainIdentifiers()
 *                                    - setDomainIdentifiers()
 *                                    - getDomainByType()
 * 9 Dec 2005     SC                  Insert in _typeDomains mapping for star fish no. to star fish domain.
 * 16 Dec 2005    SC                  Change name: star fish number to starfish id.
 */
package com.gridnode.gtas.server.bizreg.helpers;

import com.gridnode.gtas.model.bizreg.BizRegEntityFieldID;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.model.bizreg.IDomainIdentifier;
import com.gridnode.gtas.model.bizreg.RegistryConnectInfoFieldID;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the BusinessRegistry module.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public final class ActionHelper
{
  private static final Hashtable _typeDomains = new Hashtable();
  static
  {
    _typeDomains.put(DomainIdentifier.TYPE_AS2_IDENTIFIER, DomainIdentifier.DOMAIN_AS2);
    _typeDomains.put(DomainIdentifier.TYPE_DISCOVERY_URL, DomainIdentifier.DOMAIN_UDDI);
    _typeDomains.put(DomainIdentifier.TYPE_DUNS_NUMBER, DomainIdentifier.DOMAIN_DUNS);
    _typeDomains.put(DomainIdentifier.TYPE_GLOBAL_LOCATION_NUMBER, DomainIdentifier.DOMAIN_UCCNET);
    _typeDomains.put(DomainIdentifier.TYPE_STARFISH_ID, DomainIdentifier.DOMAIN_STARFISH);
  }
  
  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the BizRegManagerBean.
   *
   * @return The EJBObject to the BizRegManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IBizRegistryManagerObj getBizRegManager()
    throws ServiceLookupException
  {
    return (IBizRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IBizRegistryManagerHome.class.getName(),
      IBizRegistryManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of a BusinessEntity based on the specified uID.
   *
   * @param uID The UID of the BusinessEntity.
   * @return The BusinessEntity retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the BizRegManagerBean.
   * @exception Exception Bad BE UID. No BusinessEntity exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static BusinessEntity verifyBusinessEntity(Long uID)
    throws Exception
  {
    try
    {
      return getBizRegManager().findBusinessEntity(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad BusinessEntity UID: "+uID);
    }
  }

  public static void verifyNonGtasPartnerBusinessEntity(
    Collection beUIDs, String operation)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    // check PartnerCategory only.
    filter.addSingleFilter(null,
      BusinessEntity.PARTNER_CATEGORY, filter.getEqualOperator(),
      IBusinessEntity.CATEGORY_GRIDTALK, false);
//    filter.addSingleFilter(null, BusinessEntity.IS_PARTNER,
//      filter.getEqualOperator(), Boolean.TRUE, false);
//    filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.ENTERPRISE_ID,
//      filter.getNotEqualOperator(), "", false);
    filter.addDomainFilter(filter.getAndConnector(), BusinessEntity.UID,
      beUIDs, false);

    Collection gtasPartnerBEs = ActionHelper.getBizRegManager().findBusinessEntitiesKeys(filter);

    if (!gtasPartnerBEs.isEmpty())
    {
      throw new Exception("Not allowed to "+operation+" partner's Business Entities");
    }
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
   * @param userList The collection of BusinessEntity(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of BusinessEntity(s).
   *
   * @since 2.0
   */
  public static Collection convertBEsToMapObjects(Collection beList)
  {
    return BusinessEntity.convertEntitiesToMap(
             (BusinessEntity[])beList.toArray(new BusinessEntity[beList.size()]),
             BizRegEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an BusinessEntity to Map object.
   *
   * @param bizEntity The BusinessEntity to convert.
   * @return A Map object converted from the specified BusinessEntity.
   *
   * @since 2.0
   */
  public static Map convertBusinessEntityToMap(BusinessEntity bizEntity)
  {
    return BusinessEntity.convertToMap(
             bizEntity,
             BizRegEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an RegistryConnectInfo to Map object.
   *
   * @param regConnInfo The RegistryConnectInfo to convert.
   * @return A Map object converted from the specified RegistryConnectInfo.
   *
   * @since GT 2.2
   */
  public static Map convertRegConnInfoToMap(RegistryConnectInfo regConnInfo)
  {
    return RegistryConnectInfo.convertToMap(
             regConnInfo,
             RegistryConnectInfoFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of RegistryConnectInfo(s) to Map objects.
   *
   * @param userList The collection of RegistryConnectInfo(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of RegistryConnectInfo(s).
   *
   * @since GT 2.2
   */
  public static Collection convertRegConnInfosToMapObjects(Collection infoList)
  {
    return RegistryConnectInfo.convertEntitiesToMap(
             (RegistryConnectInfo[])infoList.toArray(new RegistryConnectInfo[infoList.size()]),
             RegistryConnectInfoFieldID.getEntityFieldID(),
             null);
  }

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
   * Check the values in the specified Collection of DomainIdentifier Maps.
   * 
   * @param domainIdentifiers Collection of Map(s) for DomainIdentifier(s).
   * @param event The event instance to use to check the fields.
   * @throws EventException Invalid values detected.
   */
  public static void checkDomainIdentifiers(
    Collection domainIdentifiers, EventSupport event)
    throws EventException
  {
    Map idMap;
    String domain, value, type;
    for (Iterator i=domainIdentifiers.iterator(); i.hasNext(); )
    {
      idMap = (Map)i.next();
      type = (String)idMap.get(IDomainIdentifier.TYPE);
      value = (String)idMap.get(IDomainIdentifier.VALUE);
      domain = getDomainByType(type);
      event.checkString("DomainIdentifer Type", type);
      event.checkString("DomainIdentifer Value", value);
      event.checkString("DomainIdentifer Domain(determined)", domain);
      idMap.put(IDomainIdentifier.DOMAIN_NAME, domain);
    }
  }

  /**
   * Set the DomainIdentifier(s) from the specified Collection of Map(s)
   * into the specified BusinessEntity.
   * 
   * @param be The BusinessEntity
   * @param domainIdMaps Collection of Map(s) for DomainIdentifier(s).
   */
  public static void setDomainIdentifiers(BusinessEntity be, Collection domainIdMaps)
  {
    Collection domainIdentifiers = new ArrayList();
    DomainIdentifier identifier;
    String domain;
    for (Iterator i=domainIdMaps.iterator(); i.hasNext(); )
    {
      identifier = new DomainIdentifier();
      ActionHelper.copyEntityFields((Map)i.next(), identifier);
      domainIdentifiers.add(identifier); 
    }
    be.setDomainIdentifiers(domainIdentifiers);
  }
  
  /**
   * Get the Domain name for the specified Identifier type.
   * 
   * @param type The Identifier type.
   * @return The Domain of the identifier type, or <b>null</b> if
   * the specified type is not recognized.
   */
  public static String getDomainByType(String type)
  {
    return (String)_typeDomains.get(type);
  }
  
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS PartnerProcess module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * BizCertMapping - dependent on Certificate
 * BizCertMapping - dependent on Partner
 * ProcessMapping - dependent on Partner
 * ProcessMapping - dependent on DocumentType
 * ProcessMapping - dependent on ProcessDef
 * ProcessMapping - dependent on ChannelInfo
 * Trigger        - dependent on Partner
 * Trigger        - dependent on PartnerType
 * Trigger        - dependent on PartnerGroup
 * Trigger        - dependent on PartnerFunction
 * Trigger        - dependent on DocumentType
 * Trigger        - dependent on ProcessDef
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {

  }

  /**
   * Checks whether there are dependent BizCertMappings on the specified Certificate.
   * 
   * @param certUid The UID of the Certificate.
   * @return A Set of BizCertMappings entities that are dependent on the Certificate, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentBizCertMappingsForCertificate(Long certUid)
  {
    Set dependents = null;
    try
    {
      dependents = getBizCertMappingListByCertificate(certUid);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentBizCertMappingsForCertificate] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of BizCertMappings that have the specified Certificate.
   * 
   * @param certUid The UID of the Certificate.
   * @return A Set of BizCertMapping entities that are associated to the
   * specified Certificate.
   * @throws Throwable Error in retrieving the associations from PartnerProcessManager.
   */  
  private Set getBizCertMappingListByCertificate(Long certUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BizCertMapping.OWN_CERT,
      filter.getEqualOperator(), certUid, false);
    filter.addSingleFilter(filter.getOrConnector(), BizCertMapping.PARTNER_CERT,
      filter.getEqualOperator(), certUid, false);

    Set set = new HashSet();

    Collection mappingList = ActionHelper.getManager().findBizCertMappingByFilter(filter);

    if (mappingList != null)
    {
      set.addAll(mappingList);
    }

    return set;    
  }
 
  /**
   * Checks whether there are dependent BizCertMappings on the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of BizCertMappings entities that are dependent on the Partner, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentBizCertMappingsForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      dependents = getBizCertMappingListByPartner(partnerId);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentBizCertMappingsForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of BizCertMappings that have the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of BizCertMapping entities that are associated to the
   * specified Partner.
   * @throws Throwable Error in retrieving the associations from PartnerProcessManager.
   */  
  private Set getBizCertMappingListByPartner(String partnerId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BizCertMapping.PARTNER_ID,
      filter.getEqualOperator(), partnerId, false);

    Set set = new HashSet();

    Collection mappingList = ActionHelper.getManager().findBizCertMappingByFilter(filter);

    if (mappingList != null)
    {
      set.addAll(mappingList);
    }

    return set;    
  }


  /**
   * Checks whether there are dependent ProcessMappings on the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of ProcessMapping entities that are dependent on the Partner, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessMappingsForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ProcessMapping.PARTNER_ID,
        filter.getEqualOperator(), partnerId, false);

      dependents = getProcessMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessMappingsForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ProcessMappings on the specified DocumentType.
   * 
   * @param name The name of the DocumentType.
   * @return A Set of ProcessMapping entities that are dependent on the DocumentType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessMappingsForDocumentType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ProcessMapping.DOC_TYPE,
        filter.getEqualOperator(), name, false);

      dependents = getProcessMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessMappingsForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ProcessMappings on the specified ProcessDef.
   * 
   * @param defName The DefName of the ProcessDef.
   * @return A Set of ProcessMapping entities that are dependent on the ProcessDef, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessMappingsForProcessDef(String defName)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ProcessMapping.PROCESS_DEF,
        filter.getEqualOperator(), defName, false);

      dependents = getProcessMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessMappingsForProcessDef] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ProcessMappings on the specified ChannelInfo.
   * 
   * @param channelUid The UID of the ChannelInfo.
   * @return A Set of ProcessMapping entities that are dependent on the ChannelInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessMappingsForChannelInfo(Long channelUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ProcessMapping.SEND_CHANNEL_UID,
        filter.getEqualOperator(), channelUid, false);

      dependents = getProcessMappingList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessMappingsForChannelInfo] Error", t);
    }
    
    return dependents;
  }


  /**
   * Get the list of ProcessMappings that satisfy the specified filter condition.
   * 
   * @param filter The filtering condition.
   * @return A Set of ProcessMapping entities that satisfy the filter condition.
   * @throws Throwable Error in retrieving the ProcessingMappings from PartnerProcessManager.
   */  
  private Set getProcessMappingList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection mappingList = ActionHelper.getManager().findProcessMappingByFilter(filter);

    if (mappingList != null)
    {
      set.addAll(mappingList);
    }

    return set;    
  }

  /**
   * Checks whether there are dependent Triggers on the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of Trigger entities that are dependent on the Partner, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.PARTNER_ID,
        filter.getEqualOperator(), partnerId, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_LEVEL,
        filter.getEqualOperator(), Trigger.LEVEL_4, false);

      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Triggers on the specified PartnerType.
   * 
   * @param partnerType The name of the PartnerType.
   * @return A Set of Trigger entities that are dependent on the PartnerType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForPartnerType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.PARTNER_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_LEVEL,
        filter.getLessOperator(), Trigger.LEVEL_4, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_LEVEL,
        filter.getGreaterOperator(), Trigger.LEVEL_1, false);
        
      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForPartnerType] Error", t);
    }
    
    return dependents;
  }
  
  /**
   * Checks whether there are dependent Triggers on the specified PartnerGroup.
   * 
   * @param partnerGroup The name of the PartnerGroup.
   * @return A Set of Trigger entities that are dependent on the PartnerGroup, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForPartnerGroup(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.PARTNER_GROUP,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_LEVEL,
        filter.getGreaterOperator(), Trigger.LEVEL_2, false);
        
      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForPartnerGroup] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Triggers on the specified PartnerFunction.
   * 
   * @param partnerFnId The PartnerFunctionId of the PartnerFunction.
   * @return A Set of Trigger entities that are dependent on the PartnerFunction, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForPartnerFunction(String partnerFnId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.PARTNER_FUNCTION_ID,
        filter.getEqualOperator(), partnerFnId, false);
        
      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForPartnerFunction] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Triggers on the specified DocumentType.
   * 
   * @param name The name of the DocumentType.
   * @return A Set of Trigger entities that are dependent on the DocumentType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForDocumentType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.DOC_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_LEVEL,
        filter.getGreaterOperator(), Trigger.LEVEL_0, false);  
        
      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForDocumentType] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Triggers on the specified ProcessDef.
   * 
   * @param defName The DefName of the ProcessDef.
   * @return A Set of Trigger entities that are dependent on the ProcessDef, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentTriggersForProcessDef(String defName)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Trigger.PROCESS_ID,
        filter.getEqualOperator(), defName, false);
      filter.addSingleFilter(filter.getAndConnector(), Trigger.TRIGGER_TYPE,
        filter.getEqualOperator(), Trigger.TRIGGER_IMPORT, false);  
        
      dependents = getTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentTriggersForProcessDef] Error", t);
    }
    
    return dependents;
  }    
  /**
   * Get a list of Triggers that satisfy the specified filter condition.
   * 
   * @param filter The Filtering condition
   * @return A Set of Trigger entities.
   * @throws Throwable Error in retrieving the Triggers from PartnerProcessManager.
   */    
  private Set getTriggerList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection triggerList = ActionHelper.getManager().findTriggers(filter);

    if (triggerList != null)
    {
      set.addAll(triggerList);
    }

    return set;    
  }
                                    
}

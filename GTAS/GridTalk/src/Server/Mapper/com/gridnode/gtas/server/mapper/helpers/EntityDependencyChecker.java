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
 * Oct 31 2003    Neo Sok Lay         Fix GNDB00016047:-
 *                                      Check for empty mrUidList Collection
 *                                      before retrieve by DomainFilter                                     
 */
package com.gridnode.gtas.server.mapper.helpers;

import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Mapper module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * GridTalkMappingRule - dependent on MappingFile via MappingRule
 * GridTalkMappingRule - dependent on DocumentType
 * GridTalkMappingRule - dependent on FileType
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
   * Checks whether there are dependent GridTalkMappingRules on the specified MappingFile.
   * 
   * @param mappingFileUid The UID of the MappingFile.
   * @return A Set of GridTalkMappingRule entities that are dependent on the MappingFile, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentGtMappingRulesForMappingFile(Long mappingFileUid)
  {
    Set dependents = null;
    try
    {
      dependents = getGtMappingRuleListByMappingFile(mappingFileUid);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentGtMappingRulesForMappingFile] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent GridTalkMappingRules on the specified DocumentType.
   * 
   * @param name The Name of the DocumentType.
   * @return A Set of GridTalkMappingRule entities that are dependent on the DocumentType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentGtMappingRulesForDocumentType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridTalkMappingRule.SOURCE_DOC_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getOrConnector(), GridTalkMappingRule.TARGET_DOC_TYPE,
        filter.getEqualOperator(), name, false);
        
      dependents = getGtMappingRuleList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentGtMappingRulesForDocumentType] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent GridTalkMappingRules on the specified FileType.
   * 
   * @param name The Name of the FileType.
   * @return A Set of GridTalkMappingRule entities that are dependent on the FileType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentGtMappingRulesForFileType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridTalkMappingRule.SOURCE_DOC_FILE_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getOrConnector(), GridTalkMappingRule.TARGET_DOC_FILE_TYPE,
        filter.getEqualOperator(), name, false);
        
      dependents = getGtMappingRuleList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentGtMappingRulesForFileType] Error", t);
    }
    
    return dependents;
  }


  /**
   * Get the list of GridTalkMappingRules that have association with the specified 
   * MappingFile via MappingRule.
   * 
   * @param mappingFileUid The UID of the MappingFile.
   * @return A Set of GridTalkMappingRule entities that are associated to the
   * specified MappingFile.
   * @throws Throwable Error in retrieving the associations from GridTalkMappingManager
   * or MappingManager.
   */  
  private Set getGtMappingRuleListByMappingFile(Long mappingFileUid) throws Throwable
  {
    Set set = new HashSet();

    Collection mrUidList = getMappingRuleUidListByMappingFile(mappingFileUid);

    //031031NSL:  ensure that domainFilter will not fail
    if (mrUidList != null && !mrUidList.isEmpty()) 
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, GridTalkMappingRule.MAPPING_RULE,
        mrUidList, false);

      set = getGtMappingRuleList(filter);
    }

    return set;    
  }
  
  /**
   * Get the list of GridTalkMappingRules that satisfy the specified filter condition.
   * 
   * @param filter The filtering condition.
   * @return A Set of GridTalkMappingRule entities that satisfy the filter condition.
   * @throws Throwable Error in retrieving the GridTalkMappingRule from PartnerProcessManager.
   */  
  private Set getGtMappingRuleList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection gtMRList = MapperDelegate.getManager().findGridTalkMappingRules(filter);

    if (gtMRList != null)
    {
      set.addAll(gtMRList);
    }

    return set;    
  }

  
  /**
   * Get the UIDs of the MappingRules that are associated with the specified
   * MappingFile.
   * 
   * @param mappingFileUid UID of the MappingFile.
   * @return Collection of UID of the MappingRule entities associated.
   * @throws Throwable Error retrieving from the MappingManager.
   */
  private Collection getMappingRuleUidListByMappingFile(Long mappingFileUid)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MappingRule.MAPPING_FILE, filter.getEqualOperator(),
      mappingFileUid, false);
      
    return ServiceLookupHelper.getManager().findMappingRulesKeys(filter);  
  }
      
}

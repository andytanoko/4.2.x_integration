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
 * Jul 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.rnif.helpers;

import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * App Rnif module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * ProcessDef - dependent on MappingFile (via ProcessAct)
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
   * Checks whether there are dependent ProcessDefs on the specified MappingFile.
   * 
   * @param mappingFileUid The UID of the MappingFile.
   * @return A Set of ProcessDef entities that are dependent on the MappingFile, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessDefsForMappingFile(Long mappingFileUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ProcessAct.DICT_FILE, filter.getEqualOperator(),
        mappingFileUid, false);
      filter.addSingleFilter(filter.getOrConnector(), ProcessAct.MSG_TYPE,
        filter.getEqualOperator(), mappingFileUid, false);
      filter.addSingleFilter(filter.getOrConnector(), ProcessAct.XML_SCHEMA,
        filter.getEqualOperator(), mappingFileUid, false);

      dependents = getProcessDefListByProcessAct(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessDefsForMappingFile] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the list of ProcessDefs that have references from some ProcessActs.
   * 
   * @param filter The Filtering condition on ProcessAct.
   * @return A Set of ProcessDef entities that are associated by the ProcessActs
   * that satisfy the filter condition.
   * @throws Throwable Error in retrieving the associations from RNProcessDefManager.
   */  
  private Set getProcessDefListByProcessAct(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection processDefList = getManager().findProcessDefsByProcessAct(filter);

    if (processDefList != null)
    {
      set.addAll(processDefList);
    }

    return set;    
  }
  
  private IRNProcessDefManagerObj getManager() throws Exception
  {
    return (IRNProcessDefManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IRNProcessDefManagerHome.class.getName(),
      IRNProcessDefManagerHome.class,
      new Object[0]);
  }
}

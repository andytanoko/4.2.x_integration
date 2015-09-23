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
 * Jul 11 2003    Neo Sok Lay         Created
 * Jun 07 2004    Neo Sok Lay         Pass in ProcessDefName when checking
 *                                    for GridDocument.
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Rnif module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * ProcessInstance - dependent on ProcessDef (via GWFRtProcess)
 * ProcessInstance - dependent on GridDocument (via GWFRtProcess)
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{
  private static final Collection _OPEN_STATES = new HashSet();
  static
  {
    _OPEN_STATES.add(new Integer(GWFRtProcess.OPEN_NOTRUNNING));
    _OPEN_STATES.add(new Integer(GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED));
    _OPEN_STATES.add(new Integer(GWFRtProcess.OPEN_RUNNING));
  }
  
  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {
  }

  /**
   * Checks whether there are dependent ProcessInstances on the specified ProcessDef.
   * 
   * @param defName The DefName of the ProcessDef.
   * @return A Set of ProcessInstance entities that are dependent on the ProcessDef, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessInstancesForProcessDef(String defName)
  {
    Set dependents = null;
    try
    {
      dependents = getProcessInstanceList(defName);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessInstancesForProcessDef] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ProcessInstances on the RnProfile
   * specified in a GridDocument.
   * 
   * @param rnProfileUid UID of the RNProfile. Indicates whether the document is involved
   * in a process.
   * @param processDefId ProcessDefName of the process, if any.
   * @return A Set of ProcessInstance Map objects that are dependent on the GridDocument, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentProcessInstancesForGridDocument(
    Long rnProfileUid, String processDefId)
  {
    Set dependents = null;
    try
    {
      //rnProfileUid alone is not enough -- it may be 0
      if (rnProfileUid != null && processDefId != null && processDefId.trim().length()>0)
      {
        RNProfile profile = ProfileUtil.getRnifManager().findRNProfile(rnProfileUid);
        Collection openedProcesses = getOpenedProcessDocs(
                                       profile.getProcessOriginatorId(),
                                       profile.getProcessInstanceId());
        if (openedProcesses != null)
        {        
          dependents = getProcessInstanceList(openedProcesses, profile.getProcessDefName());
        }
      }
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentProcessInstancesForProcessDef] Error", t);
    }
    
    return dependents;
  }

  /**
   * Get the GWFRtProcessDocs that are belong to a process that is still open.
   * 
   * @param processInitiator The ProcessInitiator of the process instance.
   * @param processInstanceId The ProcessInstanceId of the process instance.
   * @return Collection of GWFRtProcessDocs that are still in an opened process.
   * @throws Throwable Error in retrieving the GWFRtProcessDocs.
   */
  private Collection getOpenedProcessDocs(String processInitiator, String processInstanceId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GWFRtProcessDoc.STATUS,
      _OPEN_STATES, false);
    filter.addSingleFilter(filter.getAndConnector(), GWFRtProcessDoc.DOCUMENT_ID,
      filter.getEqualOperator(), getBPSSDocId(processInitiator, processInstanceId), false);
      
    return ProcessInstanceActionHelper.getWorkflowMgr().getRtProcessDocList(filter);
  }
  
  /**
   * Get the list of ProcessInstances that contain the GWFRtProcessDocs.
   * 
   * @param rtProcessDocs Collection of GWFRtProcessDocs
   * @param defName DefName of the ProcessDef.
   * @return Set of ProcessInstance Map objects.
   * @throws Throwable Error in retrieving the ProcessInstances.
   */
  private Set getProcessInstanceList(
    Collection rtProcessDocs, String defName)
    throws Throwable
  {
    Set set = new HashSet();
    
    IGWFWorkflowManagerObj wfMgr = ProcessInstanceActionHelper.getWorkflowMgr();
    
    GWFRtProcessDoc processDoc;
    GWFRtProcess process;
    Map map;
    for (Iterator i=rtProcessDocs.iterator(); i.hasNext(); )
    {
      processDoc = (GWFRtProcessDoc)i.next();
      Long instUid= (Long) processDoc.getRtBinaryCollaborationUId();

      process= (GWFRtProcess)wfMgr.getProcessInstance(instUid);
      map= ProcessInstanceActionHelper.getProcessInstanceMapFromGWFProcess(wfMgr, process);
      if(map!=null)
      {
        set.add(map);
      }      
    }
    
    return set;
  }
  
  /**
   * Get a list of ProcessInstances that satisfy the specified filter condition.
   * 
   * @param filter The Filtering condition
   * @return A Set of ProcessInstance entities (Map objects).
   * @throws Throwable Error in retrieving the ProcessInstances from ProcessManager.
   */    
  private Set getProcessInstanceList(String defName) throws Throwable
  {
    Set set = new HashSet();

    Collection processInstanceList = ProcessInstanceActionHelper.findProcessInstances(defName);

    if (processInstanceList != null)
    {
      set.addAll(processInstanceList);
    }

    return set;    
  }

  /**
   * Construct the BPSS DocumentId from the ProcessInitiator and ProcessInstanceId
   * 
   * @param processInitiator The ProcessInitiator of the process instance
   * @param processInstanceId The ProcessInstanceId of the process instance
   * @return The constructed DocumentId
   */
  public String getBPSSDocId(String processInitiator, String processInstanceId)
  {
    StringBuffer buff = new StringBuffer();
    buff.append(processInstanceId).append('/').append(processInitiator);
    
    return buff.toString();
  }

}

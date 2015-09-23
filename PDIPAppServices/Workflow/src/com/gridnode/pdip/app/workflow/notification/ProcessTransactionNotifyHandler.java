/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessTransactionNotifyHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 13, 2006    Tam Wei Xiang       Created
 * Jul 25 2008     Tam Wei Xiang       #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.  
 */
package com.gridnode.pdip.app.workflow.notification;

import java.util.Collection;

import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.framework.db.filter.DataFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.util.UtilEntity;

/**
 * This class provides the services to trigger the status of the ProcessInstance (during creation or update of PI) to the 
 * Online-Tracking Component Plug-in.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessTransactionNotifyHandler
{
  /**
   * Notify the status of the ProcessInstance to OTC-Plug-in given GWFRtProcess. RTProcess correspond RTProcessDoc will also
   * be included and send to OTC plug-in.
   * @param rtprocess the GWFRtProcess instance that we want to update its status to OTC-plugin
   */
  public static void triggerProcessTransaction(GWFRtProcess rtprocess) throws SystemException
  {
    if(rtprocess == null)
    {
      NullPointerException ex = new NullPointerException("[ProcessTransactionNotifyHandler.triggerProcessTransaction] GWFRtProcess is null ! ");
      Logger.warn("[ProcessTransactionNotifyHandler.triggerProcessTransaction] Given GWFRtProcess is null !!! no ProcessTransaction notifcation will be triggered.", ex);
    }
    
    GWFRtProcess rtprocessCopy = (GWFRtProcess)rtprocess.clone();
    GWFRtProcessDoc rtprocessDoc = null;
    try
    {
      rtprocessDoc = getRtProcessDoc(rtprocessCopy);
    }
    catch(ApplicationException ex)
    {
      Logger.warn("[ProcessTransactionNotifyHandler.triggerProcessTransaction] Error in getting the GWFRtProcessDoc. No ProcessTransaction notification will be triggered !", ex);
      return;
    }
    
    ProcessTransactionNotification processTrans = createProcessTransNotification(rtprocessCopy, rtprocessDoc);
    broadcastNotification(processTrans);
  }
  
  /**
   * Notify the status of the ProcessInstance to OTC-Plug-in given GWFRtProcessDoc. RTProcessDoc correspond RTProcess will also
   * be included and send to OTC plug-in. (We based on the RTProcessDoc to lookup the RTProcessDoc)
   * @param rtprocessDoc The GWFRtProcessDoc which we will use to lookup the GWFRtProcess
   */
  public static void triggerProcessTransaction(GWFRtProcessDoc rtprocessDoc) throws SystemException
  {
    if(rtprocessDoc == null)
    {
      NullPointerException ex = new NullPointerException("[ProcessTransactionNotifyHandler.triggerProcessTransaction] GWFRtProcessDoc is null");
      Logger.warn("[ProcessTransactionNotifyHandler.triggerProcessTransaction] GWFRtProcessDoc is null !!! no ProcessTransaction notification will be triggered.", ex);
    }
    
    GWFRtProcessDoc rtprocessDocCopy = (GWFRtProcessDoc)rtprocessDoc.clone();
    GWFRtProcess rtprocess = null;
    try
    {
      rtprocess = getRtProcess(rtprocessDocCopy); //ProcessType : BpssBinaryCollaboration
    }
    catch(Throwable th)
    {
      Logger.warn("[ProcessTransactionNotifyHandler.triggerProcessTransaction] Error in getting the GWFRtProcess. No ProcessTransaction notification will be triggered !", th);
      return;
    }
    
    ProcessTransactionNotification processTrans = createProcessTransNotification(rtprocess, rtprocessDocCopy);
    broadcastNotification(processTrans);
  }
  
  /**
   * Invoke the Framework Notifier to broadcast the notification
   * @param notification The notification we want to broadcast.
   */
  private static void broadcastNotification(ProcessTransactionNotification notification) throws SystemException
  {
    try
    {
      if(JMSRetrySender.isSendViaDefMode())
      {
        Notifier.getInstance().broadcast(notification);
      }
      else
      {
        JMSRetrySender.broadcast(notification);
      }
    }
    catch(Exception ex)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  throw new SystemException("Error in broadcasting the ProcessTransaction: "+notification, ex);
      }
      Logger.error(ILogErrorCodes.WORKFLOW_BROADCAST_NOTIFICATION_ERROR,
                   "[ProcessTransactionNotifyHandler.broadcastNotification] Error in triggering the process transaction notification "+notification+"! Error: "+ex.getMessage(), ex);
    }
  }
  
  /**
   * Create the ProcessTransactionNotification instance which consist of rtprocess and rtprocessdoc. 
   * @param rtprocess
   * @param rtprocessDoc
   * @return a ProcessTransactionNotification instance.
   */
  private static ProcessTransactionNotification createProcessTransNotification(GWFRtProcess rtprocess, GWFRtProcessDoc rtprocessDoc)
  {
    return new ProcessTransactionNotification(rtprocess, rtprocessDoc);
  }
  
  /**
   * Given the rtProcessDoc to lookup its correspond GWFRtProcess 
   * @param rtprocessDoc
   * @return rtprocessDoc correspond GWFRtProcess.
   * @throws Throwable
   */
  private static GWFRtProcess getRtProcess(GWFRtProcessDoc rtprocessDoc) throws Throwable
  {
    Long rtbinaryCollaborationUID = rtprocessDoc.getRtBinaryCollaborationUId();
    GWFRtProcess rtprocess = (GWFRtProcess)UtilEntity.getEntityByKey(rtbinaryCollaborationUID.longValue(), GWFRtProcess.ENTITY_NAME, true);
    if(rtprocess == null)
    {
      throw new ApplicationException("[ProcessTransactionNotifyHandler.getRtProcess] GWFRtProcess is expected given GWFRtProcessDoc with UID "+rtprocessDoc.getKey()+" RTBinaryCollaborationUID "+rtbinaryCollaborationUID);
    }
    
    return rtprocess;
  }
  
  /**
   * Given the rtprocess to lookup its correspond GWFRtProcessDoc
   * @param rtprocess
   * @return GWFRtProcessDoc which correspond to rtprocess
   * @throws ApplicationException It will be thrown while we can't find the rtprocess correspond GWFRtProcessDoc or 
   *                              while we have problem in retrieving the GWFRtPRocessDoc from DB.
   */
  private static GWFRtProcessDoc getRtProcessDoc(GWFRtProcess rtprocess) throws ApplicationException
  {
    String engineType = rtprocess.getEngineType();
    String processType = rtprocess.getProcessType();
    
    if(BpssBinaryCollaboration.ENTITY_NAME.equals(processType) && IWorkflowConstants.BPSS_ENGINE.equals(engineType))
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IGWFRtProcessDoc.RT_BINARY_COLLABORATION_UID, filter.getEqualOperator(), (Long)rtprocess.getKey(), false);
      
      Collection rtProcessDocList = getRtProcessDocByFilter(filter);
      if(rtProcessDocList == null || rtProcessDocList.size() == 0)
      {
        throw new ApplicationException("[ProcessTransactionNotifyHandler.getRtProcessDoc] No correspond GWFRtProcessDoc can be found given GWFRtProcess's UID "+rtprocess.getKey());
      }
      else
      {
        return (GWFRtProcessDoc)rtProcessDocList.iterator().next();
      }
    }
    return null;
  }
  
  private static Collection getRtProcessDocByFilter(IDataFilter filter)
    throws ApplicationException
  {
    try
    {
      Collection c = UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);
      return c;
    }
    catch(Throwable th)
    {
      throw new ApplicationException("[ProcessTransactionNotifyHandler.getRtProcessDocByFilter]"+
                                        "Error in retrieving the GWFRtProcessDoc "+ (filter == null ? "" : ". filter is "+filter.getFilterExpr()), th);
    }
  }
}

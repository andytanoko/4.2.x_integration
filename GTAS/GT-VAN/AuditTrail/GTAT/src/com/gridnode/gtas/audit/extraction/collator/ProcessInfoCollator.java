/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInfoCollator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.extraction.collator;

import java.util.Date;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotification;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is used to collate information from the ProcessInstanceTransactionNotification
 * and generate the AuditTrailData based on the information.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessInfoCollator extends AbstractInfoCollator
{
  
  private static ProcessInfoCollator _infoCollator = new ProcessInfoCollator();
  private final String CLASS_NAME = "ProcessInfoCollator";
  
  private ProcessInfoCollator()
  {
    
  }
  
  public static ProcessInfoCollator getInstance()
  {
    return _infoCollator;
  }
  
  @Override
  public AuditTrailData gatherInfo(AbstractNotification notify) throws AuditInfoCollatorException
  {
    Object[] param = new Object[]{notify};
    String methodName = "gatherInfo";
    try
    {
      assertNotification(notify);
      ProcessTransactionNotification notification = (ProcessTransactionNotification)notify;
      return createAuditTrailData(notification);
    }
    catch(Throwable t)
    {
      getLogger().logWarn(methodName, param, "Error creating AuditTrailData", t);
      throw new AuditInfoCollatorException("Failed to create AuditTrailData. Unexpected Error: "+t.getMessage(), t);
    }
  }
  
  /**
   * Create the AuditTrailData given the notification.
   * @param notification The DocumentTransactionNotification
   * @return 
   */
  private AuditTrailData createAuditTrailData(ProcessTransactionNotification notification)
  {
    ProcessInfo processInfo = createProcessInstanceInfo(notification);
    BusinessDocument[] bizDocument = new BusinessDocument[0];
    return new AuditTrailData(processInfo, false, bizDocument);
  }
  
  private ProcessInfo createProcessInstanceInfo(ProcessTransactionNotification notification)
  {
    GWFRtProcess rtprocess = notification.getRtprocess();
    GWFRtProcessDoc rtprocessDoc = notification.getRtprocessDoc();
    
    String processID = rtprocessDoc.getDocumentId();
    Date processStartTime = convertDateToLocal(rtprocess.getStartTime());
    Date processEndTime = convertDateToLocal(rtprocess.getEndTime());
    String processStatus = getProcessStateDesc(rtprocess.getState());
    String errorType = rtprocessDoc.getExceptionSignalType();
    String errorReason = getReasonStr(rtprocessDoc.getReason());
    Long processInstanceUID = (Long)rtprocess.getKey();
    
    return new ProcessInfo(processID, processStartTime, processEndTime, processStatus, errorType,
                                   errorReason, processInstanceUID);
  }
  
  private String getProcessStateDesc(Integer state)
  {
    switch(state)
    {
      case IGWFRtProcess.OPEN_RUNNING:
        return IAuditTrailConstant.PROCESS_STATE_RUNNING;
      case IGWFRtProcess.CLOSED_COMPLETED:
        return IAuditTrailConstant.PROCESS_STATE_COMPLETED;
      case IGWFRtProcess.CLOSED_ABNORMALCOMPLETED:
        return IAuditTrailConstant.PROCESS_STATE_ABNORMALLY_COMPLETED;
      case IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED:
        return IAuditTrailConstant.PROCESS_STATE_ABNORMALLY_ABORTED;
      case IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED:
        return IAuditTrailConstant.PROCESS_STATE_ABNORMALLY_TERMINATED;
      default:
        throw new IllegalArgumentException("["+CLASS_NAME+".getProcessStateDesc] Process State "+state+" is not supported !");
    }
  }
  
  private String getReasonStr(Object reason) //method copied from ProcessInstanceActionHelper
  {
    String reasonStr= "";
    if (reason != null)
    {
      if (reason instanceof String)
      {
        reasonStr= (String) reason;
      }
      else
        if (reason instanceof Object[])
        {
          Object[] objArray= (Object[]) reason;
          for (int i= 0; i < objArray.length; i++)
            if (objArray[i] != null)
              reasonStr += objArray[i].toString();
        }
    }
    return reasonStr;
  }
  
  private Date convertDateToLocal(Date utcTime)
  {
    if(utcTime == null)
    {
      return null;
    }
    return new Date(TimeUtil.utcToLocal(utcTime.getTime()));
  }
  
  private void assertNotification(AbstractNotification notification)
  {
    if(! (notification instanceof ProcessTransactionNotification))
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".assertNotification] The given notification is not an instance of ProcessTransactionNotification !");
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}

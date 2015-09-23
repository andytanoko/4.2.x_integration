/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchivalController.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import com.gridnode.gtas.audit.archive.ArchiveCriteria;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.cluster.helper.ArchivalJobSplitter;
import com.gridnode.gtas.audit.archive.cluster.helper.ArchivalTaskDistributor;
import com.gridnode.gtas.audit.archive.cluster.helper.INodeDiscover;
import com.gridnode.gtas.audit.archive.cluster.helper.NodeDiscoverFactory;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchivalController
{
  private static final ArchivalController _instance = new ArchivalController();
  private static final String CLASS_NAME = ArchivalController.class.getSimpleName();
  
  private Logger _logger;
  
  private ArchivalController() 
  {
    _logger = getLogger();
  }
  
  public static ArchivalController getInstance()
  {
    return _instance;
  }
  
  public String archive(Hashtable archiveCriteria, boolean isEnabledMultiNode) throws ArchiveTrailDataException
  {
    String mn = "archive";
    _logger.logMessage(mn, null, "Archive Criteria "+archiveCriteria+" isEnabled MultiNode: "+isEnabledMultiNode);
    
    ArchivalTaskDistributor dist = new ArchivalTaskDistributor();
    String archiveID = dist.generateArchiveID();
    sendArchiveStatusAlert(archiveCriteria, archiveID, true, null, IArchiveConstant.ISAT_GT_TM_ARCHIVE_STARTED);
    
    String archiveActivity = (String)archiveCriteria.get(IArchiveConstant.ARCHIVE_ACTIVITY);
    String archiveName = (String)archiveCriteria.get(IArchiveConstant.ARCHIVE_NAME);
    String archiveDesc = (String)archiveCriteria.get(IArchiveConstant.ARCHIVE_DESCRIPTION);
    String groupList = (String)archiveCriteria.get(IArchiveConstant.GROUP_INFO);
    
    try
    {
      ArchiveActivityHelper.isAllGroupExist(groupList); //error code
    
      INodeDiscover nodeDiscover = NodeDiscoverFactory.getInstance().getNodeDiscover();
      int totalNodes = 1;
      if(isEnabledMultiNode)
      {
        totalNodes = nodeDiscover.getAvailableNodes();
      }
    
      _logger.logMessage(mn, null, "Total Nodes in clustered env :"+totalNodes);
    
      Hashtable<String, ArchiveCriteria> archiveJobs = ArchivalJobSplitter.getInstance().splitJob(archiveCriteria, totalNodes);
      _logger.logMessage(mn, null, "Archive jobs list :"+archiveJobs);
    
    
      dist.setArchivalCriteria(archiveCriteria);
      dist.distributeTask(archiveJobs, archiveActivity, archiveName, archiveDesc);
    
      _logger.logMessage(mn, null, "Archive distributed complete");
    
      return dist.getArchiveID();
    }
    catch(ArchiveTrailDataException ex)
    {
      sendArchiveStatusAlert(archiveCriteria, archiveID, false, ex, IArchiveConstant.ISAT_GT_TM_ARCHIVE_FAILED);
      throw ex;
    }
  }
  
  public void sendArchiveStatusAlert(Hashtable archiveCriteria, String archiveID, boolean isSuccessInit, Exception ex,
                                      String archiveType)
  {
    String mn = "sendArchiveStatusAlert";
    _logger.logMessage(mn, null, "Sending archive status alert: "+ archiveType);
    
    Long fromStartDate = (Long)archiveCriteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME); 
    Long toStartDate = (Long)archiveCriteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
    String customerList = (String)archiveCriteria.get(IArchiveConstant.GROUP_INFO);
    Boolean isArchiveOrphanRecord = (Boolean)archiveCriteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    
    
    ArrayList substitueList = new ArrayList();
    substitueList.add(ArchiveActivityHelper.formatDateToString(new Date(), ArchiveActivityHelper.getDatePattern())); //started at time....
    
    String fromStartDateInStr = fromStartDate != null? ArchiveActivityHelper.formatDateToString(new Date(fromStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria FromStart Time
    substitueList.add(fromStartDateInStr);
    
    String toStartDateInStr = toStartDate != null ? ArchiveActivityHelper.formatDateToString(new Date(toStartDate), ArchiveActivityHelper.getDatePattern()) : ""; //criteria ToStart Time
    substitueList.add(toStartDateInStr);
    
    String isArchiveOrphanRecordInStr = isArchiveOrphanRecord == null ? Boolean.FALSE.toString(): isArchiveOrphanRecord.toString();
    substitueList.add(isArchiveOrphanRecordInStr);
    
    String customerListInStr = customerList == null ? "" : customerList;
    substitueList.add(customerListInStr);
    
    substitueList.add(archiveID == null? "" : archiveID);
    
    if(IArchiveConstant.ISAT_GT_TM_ARCHIVE_COMPLETED.equals(archiveType))
    {
      substitueList.add(archiveID+".xml");
    }
    
    if(IArchiveConstant.ISAT_GT_TM_ARCHIVE_FAILED.equals(archiveType))
    {
      substitueList.add(archiveID+".xml");
      
      if(ex != null)
      {
        substitueList.add(ex.getMessage()); //Exception msg
        substitueList.add(ArchiveActivityHelper.getStackTrace(ex)); //Stack trace
      }
    }
    
    AlertUtil.getInstance().sendAlert(archiveType, substitueList.toArray());
  }
  
  private Logger getLogger()
  {
   return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME); 
  }
}

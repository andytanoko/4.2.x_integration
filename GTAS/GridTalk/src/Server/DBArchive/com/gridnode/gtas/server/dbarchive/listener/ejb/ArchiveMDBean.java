package com.gridnode.gtas.server.dbarchive.listener.ejb;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.gridnode.gtas.model.scheduler.IScheduleFrequency;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.scheduler.model.Schedule;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.exceptions.ILogErrorCodes;

public class ArchiveMDBean extends TimeInvokeMDBean
{
  private static final long serialVersionUID = -1236469597173991674L;

  protected void invoke(AlarmInfo info)
  {
    try
    {
      String archiveID=info.getTaskId();
      Schedule schedule = ArchiveHelper.getTaskSchedule(info.getAlarmUid());
      
      ArchiveMetaInfoEntityHandler handler= ArchiveMetaInfoEntityHandler.getInstance();
      ArchiveMetaInfo metaInfo= handler.findByArchiveID(archiveID);
      String archiveName = metaInfo.getArchiveID();
      String archiveDescription = metaInfo.getArchiveDescription();
      Boolean isEnabledSearchArchived = metaInfo.isEnableSearchArchived();
      Boolean isEnabledRestoreArchived = metaInfo.isEnableRestoreArchived();
      TimeZone clientTZ = TimeZone.getTimeZone(metaInfo.getClientTz());
      
      String fromDateStr = metaInfo.getFromStartDate();
      String toDateStr = metaInfo.getToStartDate();
      String fromTimeStr = metaInfo.getFromStartTime();
      String toTimeStr = metaInfo.getToStartTime();
      
      //TWX 30032009 For the frequency that is indicate as Monthly, Weekly, Daily, we will infer the
      //archive date range
      Timestamp fromStartTime = null;
      Timestamp toStartTime = null;
      
      if(schedule != null)
      {
        Logger.log("alarm uid: "+info.getAlarmUid()+" frequency: "+schedule.getFrequency()+" startDate: "+schedule.getStartDate());
      }
      
      if(schedule.getFrequency() != null && IScheduleFrequency.ONCE != schedule.getFrequency())
      {
        if(metaInfo.isArchiveFrequencyOnce())
        {
          Logger.log("ArchiveMDBean: ArchiveTask "+metaInfo.getArchiveID()+" is set as running once but ScheduledTask is not set as run once, ignored");
          return;
        }
          
        Date[] archiveDateRange = ArchiveHelper.getArchiveDataRange(schedule.getFrequency(), metaInfo.getArchiveRecordOlderThan(), clientTZ);
        fromStartTime = ArchiveHelper.convertToUTC(archiveDateRange[0]);
        toStartTime = ArchiveHelper.convertToUTC(archiveDateRange[1]);
        Logger.log("ArchiveMDBean infer start/end time: "+archiveDateRange[0]+" end time: "+archiveDateRange[1]);
      }
      else
      {
        if(! metaInfo.isArchiveFrequencyOnce())
        {
          
          Logger.log("ArchiveMDBean: ScheduledTask is set as running once but ArchiveTask "+metaInfo.getArchiveID()+" is not set as run once, ignored");
          return;
        }
        
        fromStartTime = ArchiveHelper.convertToUTC(fromDateStr, fromTimeStr, clientTZ);
        toStartTime = ArchiveHelper.convertToUTC(toDateStr, toTimeStr, clientTZ);
      }
      
      
      String archiveType = metaInfo.getArchiveType();
   
      List partnerIDForArchive =ArchiveMetaInfo.convertToVector(metaInfo.getPartnerIDForArchive());
      Logger.log("ArchiveMDBean archiveTask clientTZ: "+clientTZ.getDisplayName()+" archiveDateRange: "+fromStartTime+" "+toStartTime+" frequency: "+schedule.getFrequency());
      
      ArchiveDocumentEvent event = new ArchiveDocumentEvent();
      event.setArchiveName(archiveName);
      event.setArchiveDescription(archiveDescription);
      event.setEnableSearchArchived(isEnabledSearchArchived);
      event.setEnableRestoreArchived(isEnabledRestoreArchived);
      event.setFromStartTime(fromStartTime);
      event.setToStartTime(toStartTime);
    
      event.setIncludeIncompleteProcesses(metaInfo.isIncludeIncompleteProcesses());
      event.setPartnerIDForArchive(partnerIDForArchive);
      event.setArchiveType(archiveType);

      if (ArchiveMetaInfo.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
      {
        List processDefNameList = ArchiveMetaInfo.convertToVector(metaInfo.getProcessDefNameList());
        event.setProcessDefNameList(processDefNameList);
      }
      else if (ArchiveMetaInfo.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
      {
        List docTypeList = ArchiveMetaInfo.convertToVector(metaInfo.getDocumentTypeList());
        event.setDocumentTypeList(docTypeList);

        List folderList = ArchiveMetaInfo.convertToVector(metaInfo.getFolderList());
        event.setFolderList(folderList);
      }
      ArchiveHelper.loadProperties();
      ArchiveHelper.sendToArchiveDest(event);
    }
    catch (Throwable e)
    {
      Logger.error(ILogErrorCodes.GT_DB_ARCHIVE_TASK_MDB, "Error in processing archive task and delivering to archive handler", e);
    }
  }

  

}

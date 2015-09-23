/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchivalJobSplitter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import com.gridnode.gtas.audit.archive.ArchiveCriteria;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveProxyManagerHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveProxyManagerObj;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.dao.ProcessTransactionDAO;
import com.gridnode.gtas.audit.dao.TraceEventInfoDAO;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.1.2)
 */
public class ArchivalJobSplitter
{
  private static final ArchivalJobSplitter _splitter = new ArchivalJobSplitter();
  private static final String CLASS_NAME = "ArchivalJobSplitter";
  
  private Logger _logger;
  
  private ArchivalJobSplitter() 
  {
    _logger = getLogger();
  }
  
  public static ArchivalJobSplitter getInstance()
  {
    return _splitter;
  }
  
  /**
   * Split the criteria as apposed to the totalNodes available in the cluster env.
   * @param criteria the archive criteria
   * @param totalNodes the total number of nodes available in the cluster env.
   * @return null if no record is fulfilling the archive criteria. Else the hashtable which contain 
   *              multiple sub-portion of the given criteria.
   * @throws Exception
   */
  public Hashtable<String, ArchiveCriteria> splitJob(Hashtable<String, Object> criteria, int totalNodes) throws ArchiveTrailDataException
  {
    try
    {
      /*
      if(getTotalProcessTransCount(criteria) == 0)
      {
        return null;
      }*/
      
      Long fromStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
      Long toStartDateTime = (Long)criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
      fromStartDateTime = adjustFromStartTime(fromStartDateTime, toStartDateTime);
      
      
      Boolean archiveOrphanRecord = (Boolean)criteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
      String groupListInStr = (String)criteria.get(IArchiveConstant.GROUP_INFO);
      Collection<String> groupList = ArchiveActivityHelper.convertStrToList(groupListInStr, ";");
    
      if(fromStartDateTime == null || toStartDateTime == null)
      {
        throw new NullPointerException("fromStartDateTime or toStartDateTime can not be null.");
      }
    
      Hashtable<String, ArchiveCriteria> jobList = new Hashtable<String, ArchiveCriteria>();
      ArrayList<Date[]> dividedDate = divideDate(fromStartDateTime, toStartDateTime, totalNodes);
      for(Date[] dateRange : dividedDate)
      {
        ArchiveCriteria archiveCriteria = new ArchiveCriteria();
        archiveCriteria.setArchiveOrphanRecord(archiveOrphanRecord);
        archiveCriteria.setFromStartDate(dateRange[0]);
        archiveCriteria.setToStartDate(dateRange[1]);
        archiveCriteria.setGroup(groupListInStr);
      
        jobList.put(getJobID(), archiveCriteria);
      }
    
      return jobList;
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in splitting the archive task "+ex.getMessage(), ex);
    }
  }
  
  private int getTotalProcessTransCount(Hashtable criteria) throws Exception
  {
    IAuditTrailArchiveManagerLocalObj archiveMgr = ArchiveActivityHelper.getArchiveMgr();
    Collection processTrans = archiveMgr.retrieveProcessTransactionUID(criteria);
    if(processTrans == null || processTrans.size() == 0)
    {
      return 0;
    }
    else
    {
      return processTrans.size();
    }
  }
  
  private ArrayList<Date[]> divideDate(Long fromStartDateTime, Long toStartDateTime, int totalNodes)
  {
    ArrayList<Date[]> dateRangeList = new ArrayList<Date[]>();
    if(totalNodes == 0) //meaning no cluster is enable
    {
      dateRangeList.add(new Date[]{new Date(fromStartDateTime), new Date(toStartDateTime)});
      return dateRangeList;
    }
    
    Long diff = toStartDateTime - fromStartDateTime;
    Long portion = diff/totalNodes;
    Long startDate = fromStartDateTime;
    Long toDate = startDate;
    
    System.out.println("diff is "+diff+" .Portion is "+portion);
    System.out.println("Date diff is "+portion/(1000*60*60));
    
    for(int i = 0; i < totalNodes; i++)
    {
      if(totalNodes == (i+1))
      {
        startDate = toDate;
        toDate = toStartDateTime; 
      }
      else
      {
        startDate = toDate;
        toDate = startDate + portion;
      }
      

      Date startD = new Date(startDate);
      Date toD = new Date(toDate);
      
      System.out.println("nodes "+i+" date range startDate "+startD+" toDate "+toD);
      System.out.println("nodes "+i+" start date "+startD.getTime()+" toDate "+toD.getTime());
      System.out.println();
      
      dateRangeList.add(new Date[]{startD, toD});
    }
    
    return dateRangeList;
  }
  
  private String getJobID()
  {
    return UUID.randomUUID().toString();
  }
  
  /**
   * To make the splitting of the archival task workload more equally among the nodes which based on the date range available
   * in the isat_trace_event and isat_process_trans table. If user choose
   * 1970 1 Jan to 2007 1 March , the splitting algo will have undesired splitting behaviour and overwhelme one
   * of the nodes.
   * @param fromStartTime
   * @return
   */
  private Long adjustFromStartTime(Long fromStartTime, Long toStartTime) throws Exception
  {
    String mn = "adjustFromStartTime";
    
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    Date eventEarliestDate = eventDAO.getEarliestEventOccuredTime();
    
    ProcessTransactionDAO processDAO = new ProcessTransactionDAO();
    Date processEarliestDate = processDAO.getEarliestProcessStartTime();
    
    Long gtProcessEarliestDate = getArchiveProxyMgr().getEarliestProcessStartDate(null);
    Long gtGDocEarliestDate = getArchiveProxyMgr().getEarlistGDocDateTimCreate(null);
    
    ArrayList<Long> dates = new ArrayList<Long>();
    Long eventDateInMilli = eventEarliestDate == null ? null : eventEarliestDate.getTime();
    Long processDateInMilli = processEarliestDate == null ? null : processEarliestDate.getTime();
    
    dates.add(eventDateInMilli);
    dates.add(processDateInMilli);
    dates.add(gtProcessEarliestDate);
    dates.add(gtGDocEarliestDate);
    
    _logger.logMessage(mn, null, "TraceEventInfo earliest date: "+eventEarliestDate+" ProcessTransaction earliest date: "+processEarliestDate+" User spec date: "+new Date(fromStartTime));
    
    Long adjustedStartDate = getSmallestTime(dates);
    if(adjustedStartDate == null || adjustedStartDate < fromStartTime || adjustedStartDate > toStartTime)
    {
      adjustedStartDate = fromStartTime;
    }
    _logger.logMessage(mn, null, "Adjusted Start Date for archive :"+new Date(adjustedStartDate));
    
    return adjustedStartDate;
  }
  
  private Long getSmallestTime(ArrayList<Long> dateInMilli)
  {
    Long smallestDate = null;
    
    for(Long date: dateInMilli)
    {
      if(date == null)
      {
        continue;
      }
      
      if( smallestDate == null || date < smallestDate)
      {
        smallestDate = date;
      }
    }
    
    return smallestDate;
  }
  
  private IArchiveProxyManagerObj getArchiveProxyMgr() throws Exception
  {
    //TODO Support fail over
    JndiFinder finder = new JndiFinder(null);
    IArchiveProxyManagerHome home = (IArchiveProxyManagerHome)finder.lookup(IArchiveProxyManagerHome.class.getName(), IArchiveProxyManagerHome.class);
    return home.create();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public static void main(String[] args)
  {
    Date d = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+60);
    
    System.out.println("Start Date :"+ d+" end date :"+c.getTime());
    
    ArchivalJobSplitter jobSpliter = ArchivalJobSplitter.getInstance();
    //jobSpliter.divideDate(d.getTime(), c.getTimeInMillis(), 3);
    
    ArrayList<Long> dates = new ArrayList<Long>();
    dates.add(new Long(100002));
    dates.add(null);
    dates.add(new Long(213213));
    dates.add(new Long(3));
    
    Long smallestDate = jobSpliter.getSmallestTime(dates);
    System.out.println("smallest date "+smallestDate);
  }
}

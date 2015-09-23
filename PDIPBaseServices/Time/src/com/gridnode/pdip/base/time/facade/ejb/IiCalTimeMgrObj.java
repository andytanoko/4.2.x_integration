// %1023788046996:com.gridnode.pdip.base.time.ejb%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method alarmTriggered does not throw java.rmi.RemoteException.
 *                                    - The business method alarmMissed does not throw java.rmi.RemoteException.
 * Feb 06 2007    Neo Sok Lay         Add loadMissedTasks() and loadTasks().                                   
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.facade.ejb;

import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmObj;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.io.Reader;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

public interface IiCalTimeMgrObj
  extends EJBObject
{


  public iCalAlarm getAlarm(Long alarmUid) throws Exception, RemoteException;
  public List findAlarms(IDataFilter filter) throws Exception, RemoteException;
  public iCalEvent getEvent(Long eventUid) throws Exception, RemoteException;
  public List findEvents(IDataFilter filter) throws Exception, RemoteException;


  /**
   * DOCUMENT ME!
   * 
   * @param alarm DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public iCalAlarm addAlarm(iCalAlarm alarm)
                     throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param alarmUid DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public void cancelAlarm(Long alarmUid)
                   throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param alarm DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public iCalAlarm updateAlarm(iCalAlarm alarm, boolean resetTime)
                        throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param filter DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public void cancelAlarmByFilter(IDataFilter filter)
                           throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param event DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public iCalEvent addEvent(iCalEvent event)
                     throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param event DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public void updateEvent(iCalEvent event, boolean resetAlarm)
                   throws Exception, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param eventUid DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public void deleteEvent(Long eventUid)
                   throws Exception, RemoteException;

  public List parseMime(Reader in) throws Exception, RemoteException;
  
  public String genMime(List compList)throws Exception, RemoteException;
  
  public iCalAlarm addRecurredAlarm(
    iCalAlarm alarm,
    Date startDate,
    Date endDate,
    iCalRecurrenceV[] recurs,
    iCalRecurrenceV[] exceptRecurs)
    throws Exception, RemoteException;
                   
  public void alarmTriggered(IiCalAlarmObj alarmObj, Date dueTime) throws RemoteException;

  public void alarmMissed(IiCalAlarmObj alarmObj, Date now) throws RemoteException;
  
  /**
   * Load the missed tasks.
   * @param anchorTime The time of reference for missed tasks.
   * @param nextLoadTime The next time that the system would load tasks.
   * @throws RemoteException
   */
  public void loadMissedTasks(Date anchorTime, Date nextLoadTime) throws RemoteException;
  
  /**
   * Load alarm tasks.
   * @param lastLoadTime The last time of reference for loading tasks
   * @param curLoadTime The time of reference for loading tasks.
   * @throws RemoteException
   */
  public void loadTasks(Date lastLoadTime, Date curLoadTime) throws RemoteException;
  

}

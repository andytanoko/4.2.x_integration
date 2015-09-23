/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGenScheduleHandler.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.ejb.EJBObject;

import com.gridnode.gridtalk.genreport.model.Schedule;

/**
 * @author Regina Zeng
 * The Remote interface for GenScheduleHandlerBean.
 */
public interface IGenScheduleHandler extends EJBObject
{
  /**
   * This method will create a new schedule in the database.
   * @param schedule
   * @return scheduleNo schedule UID
   * @throws RemoteException
   */
  public String createSchedule(Schedule schedule) throws RemoteException;
  
  /**
   * This method will update schedule base on the unique scheduleId parameter.
   * @param scheduleId
   * @param reportFormat
   * @param customerList
   * @param emailList
   * @param username
   * @param group
   * @param currentDateTime - the datetime of modification
   * @return true of false whether updating of schedule is successful or not
   * @throws RemoteException
   */
  public boolean updateSchedule(int scheduleId, String reportFormat, String customerList, String emailList, String username, String group, Date currentDateTime) throws RemoteException;
  
  /**
   * This method will update schedule as a whole object as parameter. 
   * @param schedule object
   * @param scheduleNo schedule UID
   * @return Properties of the EmailSender
   * @throws RemoteException
   */
  public Properties updateSchedule(Schedule schedule, String scheduleNo) throws RemoteException;

  /**
   * Auto deleting of report history base on the default archive duration (eg. 30 days)
   * @return boolean status
   * @throws RemoteException
   */
  public boolean deleteReport() throws RemoteException;
  
  /**
   * Generate schedule report upon time of call
   * @param timeOfCall the time call for generation of report
   * @param endRangeCall the preload time
   * @throws RemoteException
   */
  public void generateReport(Date timeOfCall, int endRangeCall) throws RemoteException; 
  
  /**
   * To retrieve a specific schedule base on the schedule id
   * @param id schedule id
   * @return schedule object
   * @throws RemoteException
   */
  public Schedule getSchedule(int id) throws RemoteException;
  
  /**
   * To retrieve template name base on the reporttype 
   * @param reportType
   * @return the template name
   * @throws RemoteException
   */
  public String getTemplateName(String reportType) throws RemoteException;
  
  /**
   * To retrieve the respective datasource type base on the reporttype
   * @param reportType
   * @return datasource type (Eg. 1=DefaultDAO, 2=UserDAO)
   * @throws RemoteException
   */
  public String getDatasourceType(String reportType) throws RemoteException;
  
  /**
   * To retrieve the property value base on the category and property key
   * @param category
   * @param property key
   * @return property value
   * @throws RemoteException
   */
  public String getValue(String cat, String key) throws RemoteException;
  
  /**
   * Auto generate the next deletion of report history
   * @throws RemoteException
   */
  public void updateNextDeleteDate() throws RemoteException;
}

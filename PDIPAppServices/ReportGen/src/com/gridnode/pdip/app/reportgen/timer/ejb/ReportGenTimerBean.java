/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenTimerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    H.Sushil         Created
 */

package com.gridnode.pdip.app.reportgen.timer.ejb;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.value.IReportOptions;

import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerHome;
import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerObj;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;

import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;

import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;

import com.gridnode.pdip.base.time.entities.value.iCalTextV;
import com.gridnode.pdip.base.time.entities.value.iCalValueV;

import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import javax.ejb.CreateException;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

 /** Mesage Driven Bean which is triggered by the Alarm on
   * This Bean then delegated the request to the session facade bean for generating the reports
   *
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */


public class ReportGenTimerBean extends TimeInvokeMDBean
{
  private IiCalTimeMgrHome  mgrTimeHome;
  private IiCalTimeMgrObj   mgrTimeObject;

  private IReportGenManagerHome    _reportGenHome;
  private IReportGenManagerObj     _reportGenMgr;

  public ReportGenTimerBean()
  {
  }

  protected void invoke(AlarmInfo info)
  {
    try
    {
      Logger.debug("[ReportGenTimerBean.invoke] Alarm invoked " + info);

      // To get the Alarm entity from the Alarm info object
      // The Alarm entitys parent Uid is used to get the Event Uid
      // The Event UID is used to fetch the description property which contains the report parameters

      lookUpTimeManagerBean();
      lookupReportGenMgr();
      mgrTimeObject   =  mgrTimeHome.create();
      iCalAlarm alarm = mgrTimeObject.getAlarm(new Long(info.getAlarmUid()));

      Logger.debug("[ReportGenTimerBean.invoke] Alarm obtained " + alarm.getUId());

      Long parentUid = alarm.getParentUid();//parent Uid is the Events Uid

      Logger.debug("[ReportGenTimerBean.invoke] Alarms parent " + alarm.getParentUid());

      iCalEvent event = mgrTimeObject.getEvent(parentUid);
      iCalPropertyV descProperty =(iCalPropertyV)event.getPropertyList(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY).get(0);
      iCalTextV descValue = (iCalTextV)descProperty .getValue();
      String properties = descValue.getTextValue();

      Logger.debug(" Found parameters : " + properties);

      StringTokenizer strToken = new StringTokenizer(properties,IReportOptions.PROPERTY_SEPERATOR);

      String reportTargetFileName = (String)strToken.nextElement();
      String reportTargetPath     = (String)strToken.nextElement();
      String reportDataSource     = (String)strToken.nextElement();
      String reportTemplate       = (String)strToken.nextElement();
      String reportOutput         = (String)strToken.nextElement();

      if(reportOutput.equals(IReportOptions.OUTPUT_HTML))
      {
  	_reportGenMgr.generateHTMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
      }
      else if(reportOutput.equals(IReportOptions.OUTPUT_PDF))
      {
	_reportGenMgr.generatePDFReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
      }
      else if(reportOutput.equals(IReportOptions.OUTPUT_XML))
      {
	_reportGenMgr.generateXMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
      }
      else
      {
	throw new ApplicationException(" Exception occurred in ReportGenTimerBean.invoke Invalid Output Format specified " + reportOutput);
      }

      Logger.debug("[ReportGenTimerBean.invoke] Report Generated ");
    }
    catch (Exception ex)
    {
      Logger.err("[ReportGenTimerBean.invoke] Generated Error ", ex);
    }

  }

  private void lookUpTimeManagerBean() throws SystemException
  {
  try
    {
      mgrTimeHome = (IiCalTimeMgrHome)ServiceLookup.getInstance(
                                      ServiceLookup.CLIENT_CONTEXT).getHome(
                    IiCalTimeMgrHome.class);
      mgrTimeObject  = mgrTimeHome.create();
    }
    catch (Exception ex)
    {
      Logger.err( " Exception in ReportGenTimerBean.lookUpTimeManagerBean() : ", ex);
      throw new SystemException(" Exception in lookup of IiCalTimeMgrHome",ex);
    }
  }

  private void lookupReportGenMgr() throws Exception
  {
    try{
       _reportGenHome = (IReportGenManagerHome)ServiceLookup.getInstance(
		      ServiceLookup.CLIENT_CONTEXT).getHome(
		      IReportGenManagerHome.class);
       _reportGenMgr = _reportGenHome.create();
    }
    catch(Exception ex)
    {
      Logger.err( " Exception in ReportGenTimerBean.lookupReportGenMgr() : ", ex);
      throw new SystemException(" Exception in lookup of lookupReportGenMgr",ex);
    }
  }
}
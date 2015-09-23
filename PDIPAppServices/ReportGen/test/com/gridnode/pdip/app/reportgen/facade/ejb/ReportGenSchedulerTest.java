/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenSchedulerTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28 2002    H.Sushil            Created
 */

package com.gridnode.pdip.app.reportgen.facade.ejb;

import com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.IReportOptions;

import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventObj;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrHome;
import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrObj;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import junit.framework.*;
import java.io.File;

import java.util.StringTokenizer;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;

public class ReportGenSchedulerTest extends TestCase
{

  public static String TEST_DATA_DATASOURCE_FILE       = "test-data\\PDIPSample.sav";
  public static String TEST_DATA_TEMPLATE_FILE         = "test-data\\PDIPSample.template";
  public static String TEST_DATA_DESTINATION_DIRECTORY = "test-data";
  public static final String TEST_DATA_REPORT_FILE     = "TestReport";//File name without extension

  public static String LOGICAL_TEST_DATA_DATASOURCE_FILE       = "localhost/ReportModule/PDIPSample.sav";
  public static String LOGICAL_TEST_DATA_TEMPLATE_FILE         = "localhost/ReportModule/PDIPSample.template";
  public static String LOGICAL_TEST_DATA_DESTINATION_DIRECTORY = "localhost/ReportModule";


  final static String LogCat  = "TEST";

  private IReportGenManagerHome    _reportGenHome;
  private IReportGenManagerObj     _reportGenMgr;

  private IDocumentMgrHome _docMgrHome;
  private IDocumentMgrObj _docMgrObj;


  private IiCalEventHome entityHome;
  private IiCalEventObj entityObject;

  public ReportGenSchedulerTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ReportGenSchedulerTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.setUp] Enter");
      pathTranslator();
      lookupReportGenMgr();
      lookUpDocumentManagerBean();
      copySampleDateToFileServer();
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.setUp] Exit");
    }

  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log(LogCat, "[ReportGenManagerBeanTest.tearDown] Enter");
    _reportGenMgr   = null;
    _reportGenHome  = null;
    Log.log(LogCat, "[ReportGenManagerBeanTest.tearDown] Exit");
  }


   public void testScheduleDailyReportHTML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportHTML] Enter");

      _reportGenMgr = _reportGenHome.create();

      DailyReportScheduleParam dailyReportParam = new DailyReportScheduleParam();

      dailyReportParam.setStartDate(dtStart);
      Date endDate = new Date();

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,10);
      Log.debug(LogCat," End Date " + cal.getTime());

      dailyReportParam.setEndDate(cal.getTime());

      //dailyReportParam.setTotalOccurence(new Integer(30));

      dailyReportParam.setReportName(TEST_DATA_REPORT_FILE+"1");
      dailyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      dailyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      dailyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      dailyReportParam.setReportFormat(IReportOptions.OUTPUT_HTML);


     _reportGenMgr.scheduleDailyReport(dailyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportHTML]", ex);
      assertTrue("Error in remote.testScheduleDailyReportHTML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportHTML] Exit");
    }
  }

  public void testScheduleDailyReportPDF()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportPDF] Enter");

      _reportGenMgr = _reportGenHome.create();

      DailyReportScheduleParam dailyReportParam = new DailyReportScheduleParam();

      dailyReportParam.setStartDate(dtStart);

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,10);
      Log.debug(LogCat," End Date " + cal.getTime());

      dailyReportParam.setEndDate(cal.getTime());

      //dailyReportParam.setTotalOccurence(new Integer(30));


      dailyReportParam.setReportName(TEST_DATA_REPORT_FILE+"2");
      dailyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      dailyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      dailyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      dailyReportParam.setReportFormat(IReportOptions.OUTPUT_PDF);

      _reportGenMgr.scheduleDailyReport(dailyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportPDF]", ex);
      assertTrue("Error in remote.testScheduleDailyReportPDF",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportPDF] Exit");
    }
  }

  public void testScheduleDailyReportXML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportXML] Enter");

      DailyReportScheduleParam dailyReportParam = new DailyReportScheduleParam();

      dailyReportParam.setStartDate(dtStart);

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,10);
      Log.debug(LogCat," End Date " + cal.getTime());

      dailyReportParam.setEndDate(cal.getTime());

      //dailyReportParam.setTotalOccurence(new Integer(30));

      dailyReportParam.setReportName(TEST_DATA_REPORT_FILE+"3");
      dailyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      dailyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      dailyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      dailyReportParam.setReportFormat(IReportOptions.OUTPUT_XML);

      _reportGenMgr.scheduleDailyReport(dailyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportXML]", ex);
      assertTrue("Error in remote.testScheduleDailyReportXML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleDailyReportXML] Exit");
    }
  }

  public void testScheduleOneOffReportXML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportXML] Enter");

      OneOffReportScheduleParam oneOffReportParam = new OneOffReportScheduleParam();

      oneOffReportParam.setStartDate(dtStart);

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,12);
      Log.debug(LogCat," End Date " + cal.getTime());

      oneOffReportParam.setEndDate(cal.getTime());

      oneOffReportParam.setReportName(TEST_DATA_REPORT_FILE+"4");
      oneOffReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      oneOffReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      oneOffReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      oneOffReportParam.setReportFormat(IReportOptions.OUTPUT_XML);

      _reportGenMgr.scheduleOneOffReport(oneOffReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportXML]", ex);
      assertTrue("Error in remote.testScheduleOneOffReportXML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportXML] Exit");
    }
  }

  public void testScheduleOneOffReportPDF()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportPDF] Enter");

      OneOffReportScheduleParam oneOffReportParam = new OneOffReportScheduleParam();

      oneOffReportParam.setStartDate(dtStart);

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,12);
      Log.debug(LogCat," End Date " + cal.getTime());

      oneOffReportParam.setEndDate(cal.getTime());

      oneOffReportParam.setReportName(TEST_DATA_REPORT_FILE+"5");
      oneOffReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      oneOffReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      oneOffReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      oneOffReportParam.setReportFormat(IReportOptions.OUTPUT_PDF);

      _reportGenMgr.scheduleOneOffReport(oneOffReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportPDF]", ex);
      assertTrue("Error in remote.testScheduleOneOffReportPDF",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportPDF] Exit");
    }
  }

  public void testScheduleOneOffReportHTML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportHTML] Enter");

      OneOffReportScheduleParam oneOffReportParam = new OneOffReportScheduleParam();

      oneOffReportParam.setStartDate(dtStart);

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,12);
      Log.debug(LogCat," End Date " + cal.getTime());

      oneOffReportParam.setEndDate(cal.getTime());

      oneOffReportParam.setReportName(TEST_DATA_REPORT_FILE+"6");
      oneOffReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      oneOffReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      oneOffReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      oneOffReportParam.setReportFormat(IReportOptions.OUTPUT_HTML);

      _reportGenMgr.scheduleOneOffReport(oneOffReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportHTML]", ex);
      assertTrue("Error in remote.testScheduleOneOffReportHTML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleOneOffReportHTML] Exit");
    }
  }

  public void testScheduleMonthlyReportXML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportXML] Enter");

      MonthlyReportScheduleParam monthlyReportParam = new MonthlyReportScheduleParam();

      monthlyReportParam.setStartDate(dtStart);

      monthlyReportParam.setMonthInterval(new Integer(1));


      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      monthlyReportParam.setEndDate(cal.getTime());

      //monthlyReportParam.setTotalOccurence(new Integer(10));


      monthlyReportParam.setReportName(TEST_DATA_REPORT_FILE+"7");
      monthlyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      monthlyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      monthlyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      monthlyReportParam.setReportFormat(IReportOptions.OUTPUT_XML);

      _reportGenMgr.scheduleMonthlyReport(monthlyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportXML]", ex);
      assertTrue("Error in remote.testScheduleMonthlyReportXML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportXML] Exit");
    }
  }

  public void testScheduleMonthlyReportPDF()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportPDF] Enter");

      MonthlyReportScheduleParam monthlyReportParam = new MonthlyReportScheduleParam();

      monthlyReportParam.setStartDate(dtStart);

      monthlyReportParam.setMonthInterval(new Integer(1));
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      monthlyReportParam.setEndDate(cal.getTime());

      //monthlyReportParam.setTotalOccurence(new Integer(10));

      monthlyReportParam.setReportName(TEST_DATA_REPORT_FILE+"8");
      monthlyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      monthlyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      monthlyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      monthlyReportParam.setReportFormat(IReportOptions.OUTPUT_PDF);

      _reportGenMgr.scheduleMonthlyReport(monthlyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportPDF]", ex);
      assertTrue("Error in remote.testScheduleMonthlyReportPDF",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportPDF] Exit");
    }
  }

  public void testScheduleMonthlyReportHTML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportHTML] Enter");

      MonthlyReportScheduleParam monthlyReportParam = new MonthlyReportScheduleParam();

      monthlyReportParam.setStartDate(dtStart);

      monthlyReportParam.setMonthInterval(new Integer(2));
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      monthlyReportParam.setEndDate(cal.getTime());

      //monthlyReportParam.setTotalOccurence(new Integer(12));

      monthlyReportParam.setReportName(TEST_DATA_REPORT_FILE+"9");
      monthlyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      monthlyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      monthlyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      monthlyReportParam.setReportFormat(IReportOptions.OUTPUT_HTML);

      _reportGenMgr.scheduleMonthlyReport(monthlyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportHTML]", ex);
      assertTrue("Error in remote.testScheduleMonthlyReportHTML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleMonthlyReportHTML] Exit");
    }
  }


  public void testScheduleWeeklyReportXML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportXML] Enter");

      WeeklyReportScheduleParam weeklyReportParam = new WeeklyReportScheduleParam();

      weeklyReportParam.setStartDate(dtStart);

      weeklyReportParam.setWeekInterval(new Integer(3));

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.WEEK_OF_YEAR,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      weeklyReportParam.setEndDate(cal.getTime());

      //weeklyReportParam.setTotalOccurence(new Integer(10));

      weeklyReportParam.setReportName(TEST_DATA_REPORT_FILE+"10");
      weeklyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      weeklyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      weeklyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      weeklyReportParam.setReportFormat(IReportOptions.OUTPUT_XML);

      _reportGenMgr.scheduleWeeklyReport(weeklyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportXML]", ex);
      assertTrue("Error in remote.testScheduleWeeklyReportXML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportXML] Exit");
    }
  }

  public void testScheduleWeeklyReportPDF()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportPDF] Enter");

      WeeklyReportScheduleParam weeklyReportParam = new WeeklyReportScheduleParam();

      weeklyReportParam.setStartDate(dtStart);

      weeklyReportParam.setWeekInterval(new Integer(1));

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.WEEK_OF_YEAR,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      weeklyReportParam.setEndDate(cal.getTime());
      //weeklyReportParam.setTotalOccurence(new Integer(10));

      weeklyReportParam.setReportName(TEST_DATA_REPORT_FILE+"11");
      weeklyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      weeklyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      weeklyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      weeklyReportParam.setReportFormat(IReportOptions.OUTPUT_PDF);

      _reportGenMgr.scheduleWeeklyReport(weeklyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportPDF]", ex);
      assertTrue("Error in remote.testScheduleWeeklyReportPDF",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportPDF] Exit");
    }
  }


  public void testScheduleWeeklyReportHTML()
  {
     Date dtStart = new Date();
     iCalAlarm valueEntity = null;

    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportHTML] Enter");

      WeeklyReportScheduleParam weeklyReportParam = new WeeklyReportScheduleParam();

      weeklyReportParam.setStartDate(dtStart);

      weeklyReportParam.setWeekInterval(new Integer(1));

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.WEEK_OF_YEAR,2);
      Log.debug(LogCat," End Date " + cal.getTime());

      weeklyReportParam.setEndDate(cal.getTime());

      //weeklyReportParam.setTotalOccurence(new Integer(10));

      weeklyReportParam.setReportName(TEST_DATA_REPORT_FILE+"12");
      weeklyReportParam.setReportTargetPath(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY);
      weeklyReportParam.setReportDataSource(LOGICAL_TEST_DATA_DATASOURCE_FILE);
      weeklyReportParam.setReportTemplate(LOGICAL_TEST_DATA_TEMPLATE_FILE);
      weeklyReportParam.setReportFormat(IReportOptions.OUTPUT_HTML);

      _reportGenMgr.scheduleWeeklyReport(weeklyReportParam);

    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportHTML]", ex);
      assertTrue("Error in remote.testScheduleWeeklyReportHTML",false);
    }

    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testScheduleWeeklyReportHTML] Exit");
    }
  }


  private void lookupReportGenMgr() throws Exception
  {
   _reportGenHome = (IReportGenManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IReportGenManagerHome.class);
   _reportGenMgr = _reportGenHome.create();
   assertNotNull("ReportGenManagerHome is null", _reportGenHome);
   assertNotNull("ReportGenManagerObj is null", _reportGenMgr);
  }

  private void lookUpDocumentManagerBean() throws Exception
  {
      _docMgrHome = (IDocumentMgrHome)ServiceLookup.getInstance(
                                      ServiceLookup.CLIENT_CONTEXT).getHome(
                    IDocumentMgrHome.class);
      assertNotNull("IDocumentMgrHome is null", _docMgrHome);
      _docMgrObj = _docMgrHome.create();
      assertNotNull("ReportGenManagerObj is null", _docMgrObj);

  }



    private void copySampleDateToFileServer() throws Exception
  {
    try{

      _docMgrObj.createFolder(new Long(1),"ReportModule");
      long folderId = _docMgrObj.getFolderId("localhost/ReportModule");
      Log.log(LogCat,"FOLDER"+folderId);
      File fTemplate        = new File(TEST_DATA_TEMPLATE_FILE);
      File[] fDataSource    = new File[1];
      fDataSource[0]   = new File(TEST_DATA_DATASOURCE_FILE);

      _docMgrObj.createDocument("Report","","",new Long(folderId),fTemplate,fDataSource);
    }
    catch(Exception e)
    {

    }
  }


 /** Method to translate any relative paths to absolute paths
   * along with addition of extra escape characters
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */

  public static void pathTranslator()
  {

    File ftestDir = null;

    ftestDir =  new File(TEST_DATA_DATASOURCE_FILE);
    TEST_DATA_DATASOURCE_FILE = ftestDir.getAbsolutePath();
    TEST_DATA_DATASOURCE_FILE = pathTranslator(TEST_DATA_DATASOURCE_FILE);

    ftestDir =  new File(TEST_DATA_TEMPLATE_FILE);
    TEST_DATA_TEMPLATE_FILE = ftestDir.getAbsolutePath();
    TEST_DATA_TEMPLATE_FILE = pathTranslator(TEST_DATA_TEMPLATE_FILE);


    ftestDir =  new File(TEST_DATA_DESTINATION_DIRECTORY);
    TEST_DATA_DESTINATION_DIRECTORY = ftestDir.getAbsolutePath();
    TEST_DATA_DESTINATION_DIRECTORY = pathTranslator(TEST_DATA_DESTINATION_DIRECTORY);

   }


   public static String pathTranslator(String path)
   {
    String pathTranslated = "";
    StringTokenizer token = new StringTokenizer(path,File.separator);
    while(token.hasMoreElements())
    {
      pathTranslated = pathTranslated + token.nextElement() + File.separator + File.separator;
    }
    pathTranslated = pathTranslated.substring(0,pathTranslated.length()-2);
    return pathTranslated;
   }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    pathTranslator();
    //UserManagerBeanTest.testSerialize();
    junit.textui.TestRunner.run(suite());
  }

}
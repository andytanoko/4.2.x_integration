/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    H.Sushil         Created
 */

package com.gridnode.pdip.app.reportgen.facade.ejb;


import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrHome;
import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrObj;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;



import junit.framework.*;
import java.io.File;

import java.util.StringTokenizer;
import java.util.Date;
import java.util.Iterator;
import java.util.Collection;

/**
 * Test case for testing ReportGenManagerBean<P>
 *
 * @author H.Sushil
 * @version 1.0
 * @since 1.0
 */
public class ReportGenManagerBeanTest
  extends    TestCase
{

  public static String TEST_DATA_DATASOURCE_FILE       = "test-data\\PDIPSample.sav";
  public static String TEST_DATA_TEMPLATE_FILE         = "test-data\\PDIPSample.template";
  public static String TEST_DATA_DESTINATION_DIRECTORY = "test-data";
  public static final String TEST_DATA_REPORT_FILE     = "TestReport";//File name without extension

  public static String LOGICAL_TEST_DATA_DATASOURCE_FILE       = "localhost/ReportModule/PDIPSample.sav";
  public static String LOGICAL_TEST_DATA_TEMPLATE_FILE         = "localhost/ReportModule/PDIPSample.template";
  public static String LOGICAL_TEST_DATA_DESTINATION_DIRECTORY = "localhost/ReportModule";
  public static String LOGICAL_TEST_DOCUMENT_DIRECTORY         = "ReportDoc";


  final static String LogCat  = "TEST";
  private IReportGenManagerHome    _reportGenHome;
  private IReportGenManagerObj     _reportGenMgr;

  private IDocumentMgrHome _docMgrHome;
  private IDocumentMgrObj _docMgrObj;

  public ReportGenManagerBeanTest(String name)
  {
    super(name);
    try{
    }
    catch(Exception e)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.ReportGenManagerBeanTest] ",e);
    }
  }

  public static Test suite()
  {
    return new TestSuite(ReportGenManagerBeanTest.class);
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
    Log.log(LogCat, "[ReportGenManagerBeanTest.tearDown] Exit");
  }

  // *********************** test cases **************************** //

  public void testgenerateHTMLReport()
  {
    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReport] Enter");
      assertTrue("Error in remote.testgenerateHTMLReport",_reportGenMgr.generateHTMLReport(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"1"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReport]", ex);
      assertTrue("Error in remote.testgenerateHTMLReport",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReport] Exit");
    }
  }

  public void testgeneratePDFReport()
  {
    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReport] Enter");
      assertTrue("Error in remote.testgeneratePDFReport",_reportGenMgr.generatePDFReport(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"2"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReport]", ex);
      assertTrue("Error in remote.testgeneratePDFReport",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReport] Exit");
    }
  }

  public void testgenerateXMLReport()
  {
    try
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReport] Enter");
      assertTrue("Error in remote.testgenerateXMLReport",_reportGenMgr.generateXMLReport(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"3"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReport]", ex);
      assertTrue("Error in remote.testgenerateXMLReport",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReport] Exit");
    }
  }

  public void testgenerateHTMLReportAndSave()
  {
    try
    {
      cleanUpTestFilesFromServer(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY+"/"+TEST_DATA_REPORT_FILE+"4");
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReportAndSave] Enter");
      assertTrue("Error in remote.testgenerateHTMLReportAndSave",_reportGenMgr.generateHTMLReportAndSave(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"4"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReportAndSave]", ex);
      assertTrue("Error in remote.testgenerateHTMLReportAndSave",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateHTMLReportAndSave] Exit");
    }
  }

  public void testgeneratePDFReportAndSave()
  {
    try
    {
      cleanUpTestFilesFromServer(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY+"/"+TEST_DATA_REPORT_FILE+"5");
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReportAndSave] Enter");
      assertTrue("Error in remote.testgeneratePDFReportAndSave",_reportGenMgr.generatePDFReportAndSave(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"5"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReportAndSave]", ex);
      assertTrue("Error in remote.testgeneratePDFReportAndSave",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgeneratePDFReportAndSave] Exit");
    }
  }

  public void testgenerateXMLReportAndSave()
  {
    try
    {
      cleanUpTestFilesFromServer(LOGICAL_TEST_DATA_DESTINATION_DIRECTORY+"/"+TEST_DATA_REPORT_FILE+"6");
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReportAndSave] Enter");
      assertTrue("Error in remote.testgenerateXMLReportAndSave",_reportGenMgr.generateXMLReportAndSave(LOGICAL_TEST_DATA_DATASOURCE_FILE,LOGICAL_TEST_DATA_TEMPLATE_FILE,LOGICAL_TEST_DATA_DESTINATION_DIRECTORY,TEST_DATA_REPORT_FILE+"6"));
    }
    catch(Exception ex)
    {
      Log.err(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReportAndSave]", ex);
      assertTrue("Error in remote.testgenerateXMLReportAndSave",false);
    }
    finally
    {
      Log.log(LogCat, "[ReportGenManagerBeanTest.testgenerateXMLReportAndSave] Exit");
    }
  }




  // ******************  utility methods ****************************

  private void lookupReportGenMgr() throws Exception
  {
   _reportGenHome = (IReportGenManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IReportGenManagerHome.class);
   assertNotNull("ReportGenManagerHome is null", _reportGenHome);
   _reportGenMgr = _reportGenHome.create();
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

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
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

      _docMgrObj.createDocument(LOGICAL_TEST_DOCUMENT_DIRECTORY,"","",new Long(folderId),fTemplate,fDataSource);
    }
    catch(Exception e)
    {

    }
  }

  private void cleanUpTestFilesFromServer(String reportFileName)
  {
    try{
      _docMgrObj.deleteDocument(reportFileName);
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


}
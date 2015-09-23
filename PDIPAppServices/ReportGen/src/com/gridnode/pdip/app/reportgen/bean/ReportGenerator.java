/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002    H.Sushil            Created
 */


package com.gridnode.pdip.app.reportgen.bean;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenFileOptions;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenHelper;

import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerHome;
import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerObj;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.io.File;

import java.util.StringTokenizer;


 /** Java Bean Class for report generation
   * Helps to access the session facade from a web-Interface
   * to generate the reports
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */



public class ReportGenerator
{

  private IReportGenManagerHome    _reportGenHome;
  private IReportGenManagerObj     _reportGenMgr;

  public ReportGenerator()
  {
   try{
      lookupReportGenMgr();
    }
    catch(Exception e)
    {
      Logger.err(" Error in look up of IReportGenManagerHome ",e);
    }
  }

  public boolean generatePDFReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;
    try
    {
      flag = _reportGenMgr.generatePDFReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generatePDFReport() Error]", e);
    }
    return flag;
  }

  public boolean generateXMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;

    try
    {
      flag = _reportGenMgr.generateXMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generateXMLReport() Error]", e);
    }
    return flag;
  }

  public boolean generateHTMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;
    try
    {
      flag = _reportGenMgr.generateHTMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generateHTMLReport() Error]", e);
    }
    return flag;
  }


  public boolean generatePDFReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;
    try
    {
      flag = _reportGenMgr.generatePDFReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generatePDFReportAndSave() Error]", e);
    }
    return flag;
  }

  public boolean generateXMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;

    try
    {
      flag = _reportGenMgr.generateXMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generateXMLReportAndSaveString() Error]", e);
    }
    return flag;
  }

  public boolean generateHTMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName) throws java.io.FileNotFoundException,java.io.IOException,java.lang.Exception
  {
    boolean flag=false;
    try
    {
      flag = _reportGenMgr.generateHTMLReportAndSave(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.generateHTMLReportAndSave() Error]", e);
    }
    return flag;
  }

  public boolean copyReportToFileServer(String reportName,String reportTargetPath,File reportMainFile,File[] reportSubFiles)
  {
    boolean flag=false;
    try
    {
      flag = _reportGenMgr.copyReportToFileServer(reportName,reportTargetPath,reportMainFile,reportSubFiles);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.copyReportToFileServer() Error]", e);
    }
    return flag;
  }

  public String getReportPathFromTempDir(String reportFileNameWithExtension)
  {
    String reportAbsolutePath=null;
    try
    {
      reportAbsolutePath = ReportGenHelper.getReportPathFromTempDir(reportFileNameWithExtension);
    }
    catch(Exception e)
    {
      Logger.err( "[ReportGenerator.getReportPathFromTempDir() Error]", e);
    }
    return reportAbsolutePath;

  }


  private void lookupReportGenMgr() throws Exception
  {
   _reportGenHome = (IReportGenManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IReportGenManagerHome.class);
   _reportGenMgr = _reportGenHome.create();
  }

}
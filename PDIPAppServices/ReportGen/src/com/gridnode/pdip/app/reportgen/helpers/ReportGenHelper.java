/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    H.Sushil            Created
 */


package com.gridnode.pdip.app.reportgen.helpers;

import com.gridnode.pdip.app.reportgen.util.ReportUtility;

import com.gridnode.pdip.framework.exceptions.SystemException;

import sg.com.elixir.ReportRuntime;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Properties;

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;


 /** Helper Class for report generation
   * Helps to access the services of Reporting tools API in order
   * to generate the reports
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */

public class ReportGenHelper implements ReportGenFileOptions
{
  public static final String IMG_DIRECTORY_PROPERTY  = "IMAGEROOT";//Property name for Image directory
  public static final String IMG_DIRECTORY           = ".";//Directory for extracting img files

  public void ReportGenHelper(){}

 /**
   * This method generates the report in PDF format using the Third party tool
   * @param reportDataSource The absolute path of the datasource file
   * @param reportTemplate The absolute path of report template file which contains the report design
   * @param reportTargetPath  The destination directory where the report is to be generated
   * @param reportTargetFileName  The name of the file(report) to be generated
   * @return true if generation is successfull false otherwise
   */

  public static boolean generatePDFReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName,boolean saveFile) throws Exception
  {
     boolean flag=false;
    try
    {
      String strTempDir = ReportUtility.instance().getTempFolderPath();

      ReportGenDocServiceHelper reportGenDocService = ReportGenDocServiceHelper.instance();

      reportDataSource    = reportGenDocService.getAbsoluteFilePath(reportDataSource);
      reportTemplate      = reportGenDocService.getAbsoluteFilePath(reportTemplate);

      sg.com.elixir.ReportRuntime rt = new sg.com.elixir.ReportRuntime(null,reportDataSource);
      rt.setReportDataSourceEditable(false);
      rt.setReportTemplate(reportTemplate);
      flag = rt.saveAsPDF(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.PDF_EXTENSION);

      if(saveFile)
      {
	File mainFile = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.PDF_EXTENSION);
	flag = reportGenDocService.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,null);
      }
    }
    catch(Exception e)
    {
     throw new SystemException(
     "ReportGenHelper.generatePDFReport() Error ",
     e);
    }
    return flag;
  }


 /**
   * This method generates the report in XML format using the Third party tool
   * @param reportDataSource The absolute path of the datasource file
   * @param reportTemplate The absolute path of report template file which contains the report design
   * @param reportTargetPath  The destination directory where the report is to be generated
   * @param reportTargetFileName  The name of the file(report) to be generated
   * @return true if generation is successfull false otherwise
   */

  public static boolean generateXMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName,boolean saveFile) throws Exception
  {
    boolean flag=false;
    try
    {
      String strTempDir = ReportUtility.instance().getTempFolderPath();

      ReportGenDocServiceHelper reportGenDocService = ReportGenDocServiceHelper.instance();

      reportDataSource    = reportGenDocService.getAbsoluteFilePath(reportDataSource);
      reportTemplate      = reportGenDocService.getAbsoluteFilePath(reportTemplate);


      sg.com.elixir.ReportRuntime rt = new sg.com.elixir.ReportRuntime(null,reportDataSource);

      rt.setReportDataSourceEditable(false);
      rt.setReportTemplate(reportTemplate);
      flag = rt.saveAsXML(strTempDir + File.separator + reportTargetFileName +ReportGenFileOptions.PERIOD + ReportGenFileOptions.XML_EXTENSION);
      if(saveFile)
      {
        File mainFile = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.XML_EXTENSION);
        flag = reportGenDocService.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,null);
      }
    }
    catch(Exception e)
    {
     throw new SystemException(
     "ReportGenHelper.generateXMLReport() Error ",
     e);
    }
    return flag;
  }

 /**
   * This method generates the report in HTML format using the Third party tool
   * The output generated is a zip file and the extrated files.
   * @param reportDataSource The absolute path of the datasource file
   * @param reportTemplate The absolute path of report template file which contains the report design
   * @param reportTargetPath  The destination directory where the report is to be generated
   * @param reportTargetFileName  The name of the file(report) to be generated
   * @return true if generation is successfull false otherwise
   */

  public static boolean generateHTMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName,boolean saveFile) throws Exception
  {
    boolean flag=false;
    try
    {

      String strTempDir = ReportUtility.instance().getTempFolderPath();

      ReportGenDocServiceHelper reportGenDocService = ReportGenDocServiceHelper.instance();

      reportDataSource    = reportGenDocService.getAbsoluteFilePath(reportDataSource);
      reportTemplate      = reportGenDocService.getAbsoluteFilePath(reportTemplate);

      sg.com.elixir.ReportRuntime rt = new sg.com.elixir.ReportRuntime(null,reportDataSource);
      rt.setReportDataSourceEditable(false);
      rt.setReportTemplate(reportTemplate);

      Properties prop = new Properties();
      prop.put(IMG_DIRECTORY_PROPERTY, ReportGenFileOptions.PERIOD + IMG_DIRECTORY+File.separator);

      rt.setDynamicParameters(prop);
      ZipOutputStream zipOutStream    =  new ZipOutputStream(new FileOutputStream(strTempDir + File.separator + reportTargetFileName+ ReportGenFileOptions.PERIOD + ReportGenFileOptions.ZIP_EXTENSION));
      flag = rt.saveAsXHTML(reportTargetFileName,zipOutStream);

      ZipInputStream zin = new ZipInputStream(new FileInputStream(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.ZIP_EXTENSION));
      ZipEntry ze = null;
      while ( (ze = zin.getNextEntry() ) != null )
      {
	      File img = new  File(strTempDir + File.separator +IMG_DIRECTORY);
	      img.mkdir();
	      FileOutputStream fout = new FileOutputStream(strTempDir+File.separator+ze.getName());
	      copy(zin,fout);
	      zin.closeEntry();
	      fout.close();
      }
      zin.close();
      zipOutStream.flush();
      zipOutStream.close();
      rt = null;

      if(saveFile)
      {
	File mainFile     = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.HTML_EXTENSION);
	File[] subFiles = new File[1];
	subFiles[0] = new File(strTempDir + File.separator + reportTargetFileName + ReportGenFileOptions.PERIOD + ReportGenFileOptions.ZIP_EXTENSION);
  	flag = reportGenDocService.copyReportToFileServer(reportTargetFileName,reportTargetPath,mainFile,subFiles);
      }
    }
    catch(Exception e)
    {
      throw new SystemException(
       "ReportGenHelper.generateHTMLReport() Error ",
       e);
    }
    return flag;
  }

  public static String getReportPathFromTempDir(String reportFileNameWithExtension) throws SystemException
  {
      String tempDir      = null;
      String absolutePath = null;
      try{
	tempDir  = ReportUtility.instance().getTempFolderPath();
    	File fNew   = new File(tempDir + File.separator + reportFileNameWithExtension);
	if(fNew.exists())
	{
  	  absolutePath = fNew.getAbsolutePath();
	  absolutePath = ReportUtility.instance().pathTranslator(absolutePath);
	}
	else
	{
	  throw new SystemException("Exception in ReportGenHelper.getReportPathFromTempDir() File doesnot Exist "+tempDir + File.separator + reportFileNameWithExtension,new FileNotFoundException(" File Not Found "+tempDir + File.separator + reportFileNameWithExtension));
	}
      }
      catch(Exception e)
      {
	throw new SystemException("File doesnot Exist ",e);
      }
       return absolutePath;

  }


 /**
   * This method copies bytes from in stream to the out stream
   * @param in InputStream
   * @param out OutputStream
   * @return none
   */

    private static void copy(InputStream in, OutputStream out) throws IOException,java.lang.Exception
    {
	    synchronized (in)
	    {
		    synchronized (out)
		    {
			    byte[] buffer = new byte[256];
			    while (true)
			    {
				    int bytesRead = in.read(buffer);
				    if ( bytesRead == -1 )
					    break;
				    out.write(buffer,0,bytesRead);
			    }
		    }
	    }
    }

}
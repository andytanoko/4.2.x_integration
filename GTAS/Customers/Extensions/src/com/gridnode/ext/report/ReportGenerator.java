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
 * Mar 13 2002    Neo Sok Lay         Created
 */
package com.gridnode.ext.report;

import java.awt.Frame;
import java.io.File;
import java.util.List;
import java.util.Properties;

import sg.com.elixir.IRuntimeOptions;
import sg.com.elixir.ReportRuntime;

/**
 * Report generation that uses Elixir Report Pro Lite 3.50 as the underlying
 * report generation engine.
 *
 * This can be run standalone, or used as a library. If used as a library, it
 * is recommended that the ReportGenerator is run in a Thread.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class ReportGenerator implements Runnable
{
  public static final String ENGINE_VERSION = "Elixir Report Pro Lite 3.50";
  private static final ReportProperties _reportProps = new ReportProperties();

  private ReportRuntime _runtime;
  private String _datasourceKey;
  private String[] _inputFiles;
  private Properties _dynParams;
  private String _format;
  private String[] _outputFiles;

  /**
   * Construct an instance of the ReportGenerator.
   *
   * @param reportProps The ReportProperties to use.
   */
  public ReportGenerator(ReportProperties reportProps)
  {
    _runtime = new ReportRuntime((Frame)null);

    setDataSourceModels(reportProps.getDataSourceModels());
    _runtime.setReportDataSourceEditable(false);

    setReportTemplate(reportProps.getTemplate());

    setDatasourceKey(reportProps.getInputType(), reportProps.getDataSourceName());
    setInputFiles(reportProps.getInputFiles());

    setOutputType(reportProps.getOutputType());
    setOutputFiles(reportProps.getOutputFiles());

    setDynamicParameters(reportProps);
  }

  private void setDataSourceModels(String dsm)
  {
    checkFileExists(dsm);
    _runtime.setReportDataSourceManagerInfo(dsm);
  }

  private void setReportTemplate(String template)
  {
    checkFileExists(template);
    _runtime.setReportTemplate(template);
  }

  private void checkFileExists(String file)
  {
    if (!new File(file).exists())
      throw new RuntimeException(file + " does not exist!");
  }

  private void setDatasourceKey(String inputType, String datasourceName)
  {
    if (ReportProperties.TYPE_XML.equalsIgnoreCase(inputType))
    {
      _datasourceKey = datasourceName + "." + IRuntimeOptions.DS_XML_URL;
    }
    else
      throw new RuntimeException("Unsupported Input type: "+inputType);
  }

  private void setInputFiles(List inputFileList)
  {
    _inputFiles = (String[])inputFileList.toArray(new String[0]);
  }

  private void setOutputType(String format)
  {
    if (ReportProperties.TYPE_PDF.equalsIgnoreCase(format))
      _format = format;
    else
      throw new RuntimeException("Unsupport Output format: "+format);
  }

  private void setOutputFiles(List outputFileList)
  {
    _outputFiles = (String[])outputFileList.toArray(new String[0]);
  }

  private void setDynamicParameters(ReportProperties props)
  {
    _dynParams = new Properties();
    _dynParams.putAll(props);
    _runtime.setDynamicParameters(_dynParams);
  }

  /**
   * Generate the report(s).
   *
   * @throws RuntimeException Error encountered during report generation.
   */
  public void run()
  {
    try
    {
      if (_inputFiles.length == 0)
      {
        generateReport(_outputFiles[0]);
      }
      else
      {
        //String reportFn = null;
        for (int i=0; i<_inputFiles.length; i++)
        {
          log("Generating report for "+_inputFiles[i]);
          _dynParams.setProperty(_datasourceKey, _inputFiles[i]);

          generateReport(_outputFiles[i]);
        }
      }
    }
    catch (Throwable t)
    {
      t.fillInStackTrace();
    }
  }

  private void generateReport(String reportFn) throws Exception
  {
    boolean succeed = false;
    //String reportFn = getTempFilename(_format);
    if (ReportProperties.TYPE_PDF.equalsIgnoreCase(_format))
    {
      log("Report Format is "+_format);
      succeed = _runtime.saveAsPDF(reportFn);
    }

    if (!succeed)
    {
      err("Fail to generate "+reportFn, null);
    }
  }

  /**
   * Runs the ReportGenerator as a standalone application.
   * Accepts a single argumment:<p>
   * - the ReportProperties filename.<p>
   * Usage: java -jar gn-elixir-report-ext.jar path/to/report.properties
   */
  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      log("Usage: java -jar gn-elixir-report-ext.jar path/to/report.properties");
      log("This extension library uses "+
        ReportGenerator.ENGINE_VERSION + " as the report generation engine.");
      System.exit(1);
    }

    String reportProps = args[0];
    try
    {
      _reportProps.loadReportProperties(reportProps);
    }
    catch (Throwable t)
    {
      err("Can't load "+reportProps, t);
      System.exit(2);
    }

    try
    {
      ReportGenerator reportGen = new ReportGenerator(_reportProps);
      reportGen.run();
      System.exit(0);
    }
    catch (Throwable t)
    {
      err("Error generating reports", t);
      System.exit(3);
    }
  }

  private static void log(String msg)
  {
    System.out.println(msg);
  }

  private static void err(String msg, Throwable t)
  {
    System.err.println(msg);
    if (t != null)
      t.printStackTrace();
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13 2003    Neo Sok Lay         Created
 */
package com.gridnode.ext.report;

import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Contains the properties for report generation using Elixir Report Pro Lite 3.50.
 * The properties will be used by the ReportGenerator to configure the
 * <code>sg.com.elixir.ReportRuntime</code>.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class ReportProperties extends Properties
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3417294246514941322L;

	/**
   * Property Key for: The DataSourceManagerInfo for report generation.
   */
  public static final String DATASOURCE_MODELS = "dsm";

  /**
   * Property Key for: The name of the DataSource to use for report generation.
   */
  public static final String DATASOURCE_NAME   = "dsn";

  /**
   * Property Key for: The report template to use.
   */
  public static final String TEMPLATE          = "template";

  /**
   * Property Key for: The type of input expected. Current only "xml" is supported.
   * @see #TYPE_XML
   */
  public static final String INPUT_TYPE        = "input.type";

  /**
   * Property Key for: List of filenames of input source files to generate
   * reports from, using comma as the delimiter between the filenames.
   * If not specified, it is assumed that the source file is indicated
   * in the report template.
   */
  public static final String INPUT_FILES       = "input.files";

  /**
   * Property Key for: List of filenames for reports to be generated, using
   * comma as the delimiter. At least one name must be specified. If
   * INPUT_FILES is specified, the number of filenames must tally with this property.
   */
  public static final String OUTPUT_FILES      = "output.files";

  /**
   * Property Key for: The type of report to generate. Currently on "pdf" is
   * supported.
   * @see #TYPE_PDF
   */
  public static final String OUTPUT_TYPE       = "output.type";

  /**
   * XML File type
   */
  public static final String TYPE_XML = "xml";

  /**
   * PDF File type.
   */
  public static final String TYPE_PDF = "pdf";

  /**
   * DATASOURCE_MODELS property.
   */
  public String getDataSourceModels()
  {
    return getProperty(DATASOURCE_MODELS, null);
  }

  /**
   * DATASOURCE_NAME property.
   */
  public String getDataSourceName()
  {
    return getProperty(DATASOURCE_NAME, null);
  }

  /**
   * OUTPUT_TYPE property.
   */
  public String getOutputType()
  {
    return getProperty(OUTPUT_TYPE, null);
  }

  /**
   * TEMPLATE property.
   */
  public String getTemplate()
  {
    return getProperty(TEMPLATE, null);
  }

  /**
   * INPUT_TYPE property.
   */
  public String getInputType()
  {
    return getProperty(INPUT_TYPE, null);
  }

  /**
   * INPUT_FILES property.
   *
   * @return List of input filenames (String).
   */
  public List getInputFiles()
  {
    List inputFileList = new ArrayList();
    String inputFilesStr = getProperty(INPUT_FILES, "");

    StringTokenizer stoken = new StringTokenizer(inputFilesStr, ",", false);
    while (stoken.hasMoreTokens())
    {
      String file = stoken.nextToken();
      inputFileList.add(file);
    }
    return inputFileList;
  }

  /**
   * OUTPUT_FILES property.
   *
   * @return List of output filenames (String).
   */
  public List getOutputFiles()
  {
    List outputFileList = new ArrayList();
    String outputFilesStr = getProperty(OUTPUT_FILES, "");

    StringTokenizer stoken = new StringTokenizer(outputFilesStr, ",", false);
    while (stoken.hasMoreTokens())
    {
      String file = stoken.nextToken();
      outputFileList.add(file);
    }
    return outputFileList;
  }


  /**
   * Load the ReportProperties from a properties file.
   *
   * @param reportPropsFile The name of the properties file.
   */
  public void loadReportProperties(String reportPropsFile) throws Exception
  {
    FileInputStream fis = new FileInputStream(reportPropsFile);
    load(fis);
    check();
  }

  /**
   * Check the report properties for any missing property or incorrectly specified
   * property.
   */
  public void check()
  {
    if (isEmptyStr(getDataSourceModels()))
      throw new RuntimeException("Missing "+DATASOURCE_MODELS+" in report properties!");

    if (isEmptyStr(getDataSourceName()))
      throw new RuntimeException("Missing "+DATASOURCE_NAME+" in report properties!");

    if (isEmptyStr(getTemplate()))
      throw new RuntimeException("Missing "+TEMPLATE+" in report properties!");

    if (isEmptyStr(getOutputType()))
      throw new RuntimeException("Missing "+OUTPUT_TYPE+" in report properties!");

    if (isEmptyStr(getInputType()))
      throw new RuntimeException("Missing "+INPUT_TYPE+" in report properties!");

    int outputCnt = getOutputFiles().size();
    if (outputCnt == 0)
      throw new RuntimeException("Missing "+OUTPUT_FILES+" in report properties!");

    int inputCnt = getInputFiles().size();
    if (inputCnt != 0 && inputCnt != outputCnt)
      throw new RuntimeException("Incorrect number of Input ("+inputCnt+") and Output ("+
        outputCnt+") files!");
  }

  private boolean isEmptyStr(String val)
  {
    return val == null || val.trim().length()==0;
  }

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * 10 Mar 2003    Neo Sok Lay             Created.
 */

package com.infineon.userproc.helpers;

import com.gridnode.ext.report.ReportProperties;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReportConfig
{
  public static final String CONFIG_PATH = "alert.path.userproc";

  public static final String KEY_DOC_TYPES = "doctypes";
  public static final String KEY_TEMPLATE  = ".template";
  public static final String KEY_URL_KEY   = ".urlKey";
  public static final String KEY_FORMAT    = ".format";
  public static final String KEY_DSM       = "dsm";
  public static final String KEY_LIBRARY   = "library";

  private static final String DELIMITER = ",";

  public static final String REPORT_DATASOURCE_MODELS = ReportProperties.DATASOURCE_MODELS;
  public static final String REPORT_DATASOURCE_NAME   = ReportProperties.DATASOURCE_NAME;
  public static final String REPORT_TEMPLATE          = ReportProperties.TEMPLATE;
  public static final String REPORT_INPUT_TYPE        = ReportProperties.INPUT_TYPE;
  public static final String REPORT_INPUT_FILES       = ReportProperties.INPUT_FILES;
  public static final String REPORT_OUTPUT_TYPE       = ReportProperties.OUTPUT_TYPE;
  public static final String REPORT_OUTPUT_FILES      = ReportProperties.OUTPUT_FILES;

  private Properties _props = null;
  private ArrayList _docTypeList = null;

  private static ReportConfig _self = null;
  private static Object lock = new Object();

  private ReportConfig(String configName)
  {
    readConfig(configName);

    _docTypeList = new ArrayList();
    parseAndSetDocType(_props.getProperty(KEY_DOC_TYPES));
  }

  private void parseAndSetDocType(String docTypes)
  {
    StringTokenizer docTypeTokens = new StringTokenizer(docTypes,DELIMITER);
    while(docTypeTokens.hasMoreElements())
    {
      _docTypeList.add(docTypeTokens.nextElement());
    }
  }

  private synchronized void readConfig(String configName)
  {
    _props = new Properties();
    try
    {
      _props.load(new FileInputStream(FileUtil.getFile(CONFIG_PATH, configName)));
    }
    catch (Exception ex)
    {
      Logger.err("[ReportConfig.readConfig] Unable to load "+
        configName);
    }
  }

  public static ReportConfig getReportConfig(String configName)
  {
     if(_self == null)
     {
        synchronized(ReportConfig.class)
        {
          if(_self == null)
            _self = new ReportConfig(configName);
        }
     }
     return _self;
  }

  public String getTemplate(String docType)
  {
    String template = null;
    if(isDocTypeExists(docType))
    {
      String templateKey = docType+KEY_TEMPLATE;
      template = _props.getProperty(templateKey);
    }
    return template;
  }

  public String getUrlKey(String docType)
  {
    String urlKey = null;
    if(isDocTypeExists(docType))
    {
      String urlKeyKey = docType+KEY_URL_KEY;
      urlKey = _props.getProperty(urlKeyKey);
    }
    return urlKey;
  }

  public String getFormat(String docType)
  {
    String format = null;
    if(isDocTypeExists(docType))
    {
      String formatKey = docType+KEY_FORMAT;
      format = _props.getProperty(formatKey);
    }
    return format;
  }

  public boolean isDocTypeExists(String docType)
  {
    return (_docTypeList.contains(docType));
  }

  public String getDSM()
  {
    String dsm = _props.getProperty(KEY_DSM);

    return dsm;
  }

  public String getLibrary()
  {
    String library = _props.getProperty(KEY_LIBRARY);
    return library;
  }

  public String toString()
  {
    return "ReportConfig - DocTypeList: "+_docTypeList;
  }

}
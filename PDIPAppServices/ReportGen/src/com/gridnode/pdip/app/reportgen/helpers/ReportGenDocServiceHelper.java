/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenDocServiceHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 4 2002    H.Sushil            Created
 */

package com.gridnode.pdip.app.reportgen.helpers;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.util.ReportUtility;

import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrObj;
import com.gridnode.pdip.base.docservice.manager.ejb.IDocumentMgrHome;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.io.File;

/**
 * Helper Class to access the Document Services module
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */

public class ReportGenDocServiceHelper
{
  private IDocumentMgrHome docMgrHome;
  private IDocumentMgrObj  docMgrObj;

  private static ReportGenDocServiceHelper _reportGenDocServiceHelper = null;

  private ReportGenDocServiceHelper()
  {

  }

  static public ReportGenDocServiceHelper instance()
  {
    if (_reportGenDocServiceHelper == null)
    {
          _reportGenDocServiceHelper = new ReportGenDocServiceHelper();
    }
    return _reportGenDocServiceHelper;
  }

  public String getAbsoluteFilePath(String logicalPath)
  {
    String absolutePath = null;
    try{

      File fTemp  = null;
      lookUpDocumentManagerBean();
      fTemp = docMgrObj.getFile(logicalPath);
      absolutePath = fTemp.getAbsolutePath();
      return absolutePath;
    }
    catch(Exception e)
    {
      Logger.err(" Exception in ReportGenDocServiceHelper.getAbsoluteFilePath(String logicalPath) ",e);
      return absolutePath;
    }
  }

  public boolean copyReportToFileServer(String reportName,String reportTargetPath,File reportMainFile,File[] reportSubFiles)
  {
    boolean flag = true;
    try{
      lookUpDocumentManagerBean();
      Long folderId = new Long(docMgrObj.getFolderId(reportTargetPath));

      if(docMgrObj.exists(reportTargetPath+"/"+reportName))
      {
	  Logger.debug(" Exception in ReportGenDocServiceHelper.copyReportToFileServer Document Already exists "+reportTargetPath+"/"+reportName);
          flag = false;
      }

      if(docMgrObj.exists(reportTargetPath+"/"+reportMainFile.getName()))
      {
	Logger.debug(" Exception in ReportGenDocServiceHelper.copyReportToFileServer File Already exists "+reportTargetPath+"/"+reportMainFile.getName());
        flag = false;
      }

      if(reportSubFiles!=null)
      {
	if(docMgrObj.exists(reportTargetPath+"/"+reportSubFiles[0].getName()))
        {
	  Logger.debug(" Exception in ReportGenDocServiceHelper.copyReportToFileServer File Already exists "+reportTargetPath+"/"+reportSubFiles[0].getName());
	  flag = false;
        }
      }
      if(flag)
       	docMgrObj.createDocument(reportName,"","",folderId, reportMainFile, reportSubFiles);


    }
    catch(Exception e)
    {
      Logger.err(" Exception in ReportGenDocServiceHelper.copyReportToFileServer",e);
      flag=false;
    }
    return flag;
  }

  private void lookUpDocumentManagerBean() throws SystemException
  {
  try
    {
      docMgrHome = (IDocumentMgrHome)ServiceLookup.getInstance(
                                      ServiceLookup.CLIENT_CONTEXT).getHome(
                    IDocumentMgrHome.class);
      docMgrObj = docMgrHome.create();
    }
    catch (Exception ex)
    {
      Logger.err( " Exception in ReportGenTimerBean.lookUpDocumentManagerBean() : ", ex);
      throw new SystemException(" Exception in lookup of IDocumentMgrHome",ex);
    }
  }

}
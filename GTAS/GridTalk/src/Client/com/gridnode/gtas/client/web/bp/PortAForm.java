/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2002-05-22     Daniel D'Cotta      Added running sequence number support
 * 2005-08-23     Tam WEi Xiang       Instance Variable _attachmentDir and its
 *                                    get/set method have been removed.(To improve
 *                                    GTAS attachment feature)
 * 2006-03-03     Tam Wei Xiang       Added new field fileGrouping.                                    
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 
public class PortAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _hostDir;
  private String _isRfc;
  private String _rfc;
  private String _isDiffFileName;
  private String _isOverwrite;
  private String _fileName;
  private String _isAddFileExt;
  private String _fileExtType;
  private String _fileExtValue;
  private String _startNum;
  private String _rolloverNum;
  private String _nextNum;
  private String _isPadded;
  private String _fixNumLength ;
  private String _isExportGdoc;
  private String _fileGrouping;
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _isRfc          = "false";
    _isOverwrite    = "false";
    _isDiffFileName = "false";
    _isAddFileExt   = "false";
    _isPadded       = "false";
    _isExportGdoc   = "false";
  }

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getHostDir()
  { return _hostDir; }

  public void setHostDir(String hostDir)
  { _hostDir=hostDir; }

  public String getIsRfc()
  { return _isRfc; }

  public void setIsRfc(String isRfc)
  { _isRfc=isRfc; }

  public String getRfc()
  { return _rfc; }

  public void setRfc(String rfc)
  { _rfc=rfc; }

  public String getIsDiffFileName()
  { return _isDiffFileName; }

  public void setIsDiffFileName(String isDiffFileName)
  { _isDiffFileName=isDiffFileName; }

  public String getIsOverwrite()
  { return _isOverwrite; }

  public void setIsOverwrite(String isOverwrite)
  { _isOverwrite=isOverwrite; }

  public String getFileName()
  { return _fileName; }

  public void setFileName(String fileName)
  { _fileName=fileName; }

  public String getIsAddFileExt()
  { return _isAddFileExt; }

  public void setIsAddFileExt(String isAddFileExt)
  { _isAddFileExt=isAddFileExt; }

  public String getFileExtType()
  { return _fileExtType; }

  public void setFileExtType(String fileExtType)
  { _fileExtType=fileExtType; }

  public String getFileExtValue()
  { return _fileExtValue; }

  public void setFileExtValue(String fileExtValue)
  { _fileExtValue=fileExtValue; }

  public String getStartNum()
  { return _startNum; }

  public void setStartNum(String startNum)
  { _startNum=startNum; }

  public String getRolloverNum()
  { return _rolloverNum; }

  public void setRolloverNum(String rolloverNum)
  { _rolloverNum=rolloverNum; }

  public String getNextNum()
  { return _nextNum; }

  public void setNextNum(String nextNum)
  { _nextNum=nextNum; }

  public String getIsPadded()
  { return _isPadded; }

  public void setIsPadded(String isPadded)
  { _isPadded=isPadded; }

  public String getFixNumLength()
  { return _fixNumLength; }

  public void setFixNumLength(String fixNumLength)
  { _fixNumLength=fixNumLength; }
  
  public String getIsExportGdoc()
  { return _isExportGdoc; }

  public void setIsExportGdoc(String isExportGdoc)
  { _isExportGdoc=isExportGdoc; }
  
  public String getFileGrouping()
  { return _fileGrouping; }
  
  public void setFileGrouping(String fileGrouping)
  { _fileGrouping = fileGrouping; }
}
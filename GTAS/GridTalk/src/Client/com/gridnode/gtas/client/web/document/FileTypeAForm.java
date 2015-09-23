/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class FileTypeAForm extends GTActionFormBase
{
  private String _fileType;
  private String _description;
  private String _programName;
  private String _programPath;
  private String _parameters;
  private String _workingDirectory;

  public String getFileType()
  {
    return _fileType;
  }

  public void setFileType(String value)
  {
    _fileType = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getProgramName()
  {
    return _programName;
  }

  public void setProgramName(String value)
  {
    _programName = value;
  }

  public String getProgramPath()
  {
    return _programPath;
  }

  public void setProgramPath(String value)
  {
    _programPath = value;
  }

  public String getParameters()
  {
    return _parameters;
  }

  public void setParameters(String value)
  {
    _parameters = value;
  }

  public String getWorkingDirectory()
  {
    return _workingDirectory;
  }

  public void setWorkingDirectory(String value)
  {
    _workingDirectory = value;
  }
}
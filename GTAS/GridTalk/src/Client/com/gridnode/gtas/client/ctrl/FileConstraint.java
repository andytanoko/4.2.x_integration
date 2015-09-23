/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class FileConstraint extends AbstractConstraint implements IFileConstraint
{
  protected boolean _isPath;
  protected boolean _isDownloadable;
  protected String _pathKeyFieldName;
  protected String _subPathFieldName;
  protected String _fixedPathKey;

  FileConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_FILE, detail);
  }

  protected void initialise(int type, Properties detail)
    throws GTClientException
  {
    String key = detail.getProperty("file.pathKey",null);                               // 14102002 DDJ
    _pathKeyFieldName = (key != null) ? DefaultSharedFMI.extractFieldName(key) : null;  // 14102002 DDJ
    _fixedPathKey = detail.getProperty("file.fixedKey",null);
    if( (_pathKeyFieldName == null) && (_fixedPathKey == null) )
    {
      _isPath = true;
    }
    else
    {
      _isPath = false;
      _subPathFieldName = detail.getProperty("file.subPath",null);
      _isDownloadable = DefaultSharedFMI.makeBoolean( detail.getProperty("file.downloadable","false") );
    }
  }

  public boolean isFileName()
  {
    return !_isPath;
  }

  public boolean isPath()
  {
    return _isPath;
  }

  public String getFixedPathKey()
  {
    if(_isPath)
    {
      throw new java.lang.IllegalStateException("Is path field - refer to file field for fixedPathKey");
    }
    return _fixedPathKey;
  }

  public String getPathKeyFieldName()
  {
    if(_isPath)
    {
      throw new java.lang.IllegalStateException("Is path field - refer to file field for pathKeyFieldName");
    }
    return _pathKeyFieldName;
  }

  public String getSubPathFieldName()
  {
    if(_isPath)
    {
      throw new java.lang.IllegalStateException("Is path field - refer to file field for subPathFieldName");
    }
    return _subPathFieldName;
  }

  public boolean isDownloadable()
  {
    if(_isPath)
    {
      throw new java.lang.IllegalStateException("Is path field - refer to file field for isDownloadable");
    }
    return _isDownloadable;
  }
}
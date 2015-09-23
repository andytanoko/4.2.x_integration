/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HouseKeepingAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 * 2006-06-26     Tam Wei Xiang       Added new field '_wfRecordsDaysToKeep'
 */
package com.gridnode.gtas.client.web.archive;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class HouseKeepingAForm extends GTActionFormBase
{
  private String _tempFilesDaysToKeep;
  private String _logFilesDaysToKeep;
  private String _wfRecordsDaysToKeep;
  
  public String getTempFilesDaysToKeep()
  {
    return _tempFilesDaysToKeep;
  }

  public Integer getTempFilesDaysToKeepAsInteger()
  {
    return StaticUtils.integerValue(_tempFilesDaysToKeep);
  }

  public void setTempFilesDaysToKeep(String string)
  {
    _tempFilesDaysToKeep = string;
  }

  public String getLogFilesDaysToKeep()
  {
    return _logFilesDaysToKeep;
  }

  public Integer getLogFilesDaysToKeepAsInteger()
  {
    return StaticUtils.integerValue(_logFilesDaysToKeep);
  }

  public void setLogFilesDaysToKeep(String string)
  {
    _logFilesDaysToKeep = string;
  }
  
  public String getWfRecordsDaysToKeep()
  {
  	return _wfRecordsDaysToKeep;
  }
  
  public Integer getWfRecordsDaysToKeepAsInteger()
  {
  	return StaticUtils.integerValue(_wfRecordsDaysToKeep);
  }
  
  public void setWfRecordsDaysToKeep(String daysToKeep)
  {
  	_wfRecordsDaysToKeep = daysToKeep;
  }
}
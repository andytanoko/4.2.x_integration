/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 4, 2003      Mahesh         Created
 * Jun 27,2006    Tam Wei Xiang    Added new field 'wfRecordsDaysToKeep'
 */
package com.gridnode.gtas.server.housekeeping.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This class holds the settings for housekeeping.
 * This entity will be persistent as an Xml file instead of database table. 
 * All the interval variables are in days 
 */
public class HousekeepingInfo extends AbstractEntity implements IHousekeepingInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5078466872482515629L;
	Integer _tempFilesDaysToKeep;
  Integer _logFilesDaysToKeep;
  Integer _wfRecordsDaysToKeep;
  
  // ************** Methods from AbstractEntity ***************************
  
  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getEntityName()+"- tempFilesDaysToKeep="+_tempFilesDaysToKeep+", logFilesDaysToKeep="+_logFilesDaysToKeep+", runtimeWFRecordDaysToKeep="+_wfRecordsDaysToKeep;
  }

  public Number getKeyId()
  {
    return UID;
  }
  
  //***************** Getters & Setters *********************************

  public Integer getLogFilesDaysToKeep()
  {
    return _logFilesDaysToKeep;
  }

  public Integer getTempFilesDaysToKeep()
  {
    return _tempFilesDaysToKeep;
  }

  public void setLogFilesDaysToKeep(Integer logFilesDaysToKeep)
  {
    _logFilesDaysToKeep = logFilesDaysToKeep;
  }

  public void setTempFilesDaysToKeep(Integer tempFilesDaysToKeep)
  {
    _tempFilesDaysToKeep = tempFilesDaysToKeep;
  }

	public Integer getWfRecordsDaysToKeep()
	{
		return _wfRecordsDaysToKeep;
	}

	public void setWfRecordsDaysToKeep(Integer recordDaysToKeep)
	{
		_wfRecordsDaysToKeep = recordDaysToKeep;
	}
  
}

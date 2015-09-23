/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateSwapping
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 05-Jun-2008    Yee Wah,Wong        #38  Created.
 */
package com.gridnode.pdip.base.certificate.model;

import java.util.Date;


import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for CertificateSwapping entity.
 *
 * The Model:<BR><PRE>
 *   UId        - UID for a CertificateSwapping entity instance.
 *   Id         - Id for each CertificateSwapping.
 *   SwapDate   - Certificate Swap Date.
 *   SwapTime	- Certificate Swap Time.
 *   AlarmUID 	- Alarm UID  
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 */


public class CertificateSwapping
			extends AbstractEntity
			implements ICertificateSwapping
{
  
  private static final long serialVersionUID = -1689150779116064530L;
	  protected Date _swapDate;
	  protected String _swapTime;
	  protected long _alarmUID;
    
	
	  
	  public CertificateSwapping()
	  {
		  
	  }
	  
	  public String getEntityDescr()
	  {
	    
	    return new StringBuffer().append(getAlarmUID()).append('/').append(getSwapDate()).toString();
	  }

	  public String getEntityName()
	  {
	    return ENTITY_NAME;
	  }
    
    public Number getKeyId()
    {
      return UID;
    }
    
	  //*********************** Getter for Attribute *********************************
    
    public Date getSwapDate()
    {
      return _swapDate;
    }
	  
    public String getSwapTime()
    {
      return _swapTime;
    }
    
    public long getAlarmUID()
    {
      return _alarmUID;
    }
    
    
    
    //*********************** Setter for Attribute *********************************
    
    public void setSwapDate(Date swapDate)
    {
      _swapDate = swapDate;
    }
  
    public void setSwapTime(String swapTime)
    {
      _swapTime = swapTime;
    }
    
    public void setAlarmUID(long alarmUID)
    {
      _alarmUID = alarmUID;
    }
    
}

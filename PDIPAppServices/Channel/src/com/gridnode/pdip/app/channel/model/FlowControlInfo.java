/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FlowControlInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 24 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class FlowControlInfo extends AbstractEntity implements IFlowControlInfo
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4053479706591832350L;
	private boolean _isZip;
  private int _zipThreshold;
  private boolean _isSplit;
  private int _splitThreshold = IFlowControlInfo.DEFAULT_SPLIT_THRESHOLD;
  private int _splitSize = IFlowControlInfo.DEFAULT_SPLIT_SIZE;
  private transient int _flowControlStatus;

  public FlowControlInfo()
  {
  }

  public void setIsZip(boolean isZip)
  {
    _isZip = isZip;
  }

  public void setZipThreshold(int zipThreshold)
  {
    _zipThreshold = zipThreshold;
  }

  public void setIsSplit(boolean isSplit)
  {
    _isSplit = isSplit;
  }

  public void setSplitThreshold(int splitThreshold)
  {
    _splitThreshold = splitThreshold;
  }

  public void setSplitSize(int splitSize)
  {
    _splitSize = splitSize;
  }

  public boolean isZip()
  {
    return _isZip;
  }

  public int getZipThreshold()
  {
    return _zipThreshold;
  }

  public boolean isSplit()
  {
    return _isSplit;
  }

  public int getSplitThreshold()
  {
    return _splitThreshold;
  }

  public int getSplitSize()
  {
    return _splitSize;
  }

  public Number getKeyId()
  {
    return null;
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return isZip()
      + "/"
      + getZipThreshold()
      + "/"
      + isSplit()
      + "/"
      + getSplitThreshold()
      + "/"
      + getSplitSize();
  }

  public void setFlowControlStatus(int status)
  {
    _flowControlStatus = status;
  }

  public int getFlowControlStatus()
  {
    return _flowControlStatus;
  }

}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventDoc.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 5, 2007        i00107             Created
 */

package com.gridnode.gridtalk.httpbc.common.model;

/**
 * @author i00107
 * This bean is used in event notification to hold a document with its content.
 */
public class EventDoc
{
  private byte[] _doc;
  private String _filename;
  private boolean _isRequiredPack;
  
  public EventDoc()
  {
    
  }
  
  /**
   * @return the doc
   */
  public byte[] getDoc()
  {
    return _doc;
  }
  /**
   * @param doc the doc to set
   */
  public void setDoc(byte[] doc)
  {
    _doc = doc;
  }
  /**
   * @return the filename
   */
  public String getFilename()
  {
    return _filename;
  }
  /**
   * @param filename the filename to set
   */
  public void setFilename(String filename)
  {
    _filename = filename;
  }
  /**
   * @return the _isRequiredPack
   */
  public boolean isRequiredPack()
  {
    return _isRequiredPack;
  }
  /**
   * @param _isRequiredPack the _isRequiredPack to set
   */
  public void setRequiredPack(boolean isRequiredUnpack)
  {
    this._isRequiredPack = isRequiredUnpack;
  }
  
  
}

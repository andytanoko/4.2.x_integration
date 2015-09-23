/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileContent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.common.model;

import java.io.Serializable;

/**
 * @author i00107
 * This class serves as a data holder for the contents of a file.
 */
public class FileContent implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -6225030069093307333L;
  private byte[] _content;
  private String _filename;
  
  public FileContent()
  {
    
  }
  
  public FileContent(String filename, byte[] content)
  {
    _content = content;
    _filename = filename;
  }
  
  public byte[] getContent()
  {
    return _content;
  }
  
  public String getFilename()
  {
    return _filename;
  }
  
  /**
   * @param content The content to set.
   */
  public void setContent(byte[] content)
  {
    _content = content;
  }

  /**
   * @param filename The filename to set.
   */
  public void setFilename(String filename)
  {
    _filename = filename;
  }

  public String toString()
  {
    return _filename+" [size="+(_content!=null?_content.length:0)+"]";
  }
}

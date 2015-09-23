/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingMetaData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25, 2011   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.mapper.model;

import java.io.Serializable;

/**
 * This class serves as a data holder (transient data) for Mapping Rule that is needed 
 * during mapping.
 * 
 * @author Tam Wei Xiang 
 * @since 4.2.3.1
 */
public class MappingMetaData implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 6154829203075823297L;
  private String fileExt;
  
  public MappingMetaData()
  {
    
  }
  
  public MappingMetaData(String fileExt)
  {
    setFileExt(fileExt);
  }

  public String getFileExt()
  {
    return fileExt;
  }

  public void setFileExt(String fileExt)
  {
    this.fileExt = fileExt;
  }
}

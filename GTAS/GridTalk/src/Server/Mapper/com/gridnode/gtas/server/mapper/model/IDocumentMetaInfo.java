/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Koh Han Sing        Created
 * May 22 2003    Koh Han Sing        Add in setUdocFullPath
 * Oct 31 2003    Guo Jianyu          Added getFolder()
 * Feb 04 2004    Koh Han Sing        Added getTempUdocFilename,
 *                                          setTempUdocFilename                                         
 */
package com.gridnode.gtas.server.mapper.model;

/**
 * This interface defines the methods used to access the document
 * entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */

public interface IDocumentMetaInfo
{
  public String getUdocFilename();

  public void setUdocFilename(String udocFilename);

  public void setUdocVersion(Integer udocVersion);

  public void setUdocDocType(String udocDocType);

  public void setUdocFileType(String udocFileType);

  public void setUdocFileSize(Long udocFileSize);

  public void setUdocFullPath(String udocFullPath);

  public Object clone();

  public String getFolder();

  public String getTempUdocFilename();

  public void setTempUdocFilename(String tempUdocFilename);
  
  //TWX 08112006 Info require by the DocumentFlowNotification
  public String getRecipientBizEntityId();
  
  public String getSenderBizEntityId();
  
  public Long getGdocId();
  
  public String getTracingID();
  
  public String getUdocDocType();
  
  public Object getKey();
  
  public String getSrcFolder();
  //END
  
}

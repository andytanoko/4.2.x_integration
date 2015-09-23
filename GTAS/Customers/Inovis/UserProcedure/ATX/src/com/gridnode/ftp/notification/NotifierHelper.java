/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NotifierHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.ftp.notification;

import com.gridnode.util.SystemUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class NotifierHelper
{
  private static final String INBOUND_FOLDER="Inbound";
  private static final String OUTBOUND_FOLDER="Outbound";
  private static final String EXPORT_FOLDER="Export";
  private static final String IMPORT_FOLDER="Import";
  
  /**
   * Get the MessageID given the gdoc.
   * @param gdoc
   * @return the concatenate str of gdoc folder(short form) and gdocID
   */
  public static String getMessageID(String folder, String gdocID)
  {
    String folderAbbreviate = "";
    if(INBOUND_FOLDER.equals(folder))
    {
      folderAbbreviate = "IB";
    }
    else if(OUTBOUND_FOLDER.equals(folder))
    {
      folderAbbreviate = "OB";
    }
    else if(EXPORT_FOLDER.equals(folder))
    {
      folderAbbreviate = "EP";
    }
    else if(IMPORT_FOLDER.equals(folder))
    {
      folderAbbreviate = "IP";
    }
    else
    {
      throw new IllegalArgumentException("[NotifierHelper.getMessageID] Folder "+folder+" is not supported !");
    }
    return folderAbbreviate+"-"+gdocID;
  }
  
  public static String getUDocFilePath(String folder, String filename)
  {
    String udocFolder = SystemUtil.getWorkingDirPath();
    if(IMPORT_FOLDER.equals(folder))
    {
      return udocFolder +"/gtas/data/doc/udoc/Import/"+filename;
    }
    else if(OUTBOUND_FOLDER.equals(folder))
    {
      return udocFolder +"/gtas/data/doc/udoc/Outbound/"+filename;
    }
    else if(EXPORT_FOLDER.equals(folder))
    {
      return udocFolder +"/gtas/data/doc/udoc/Export/"+filename;
    }
    else if(INBOUND_FOLDER.equals(folder))
    {
      return udocFolder +"/gtas/data/doc/udoc/Inbound/"+filename;
    }
    else
    {
      throw new IllegalArgumentException("Folder: "+folder+" is not supported !");
    }
  }
}

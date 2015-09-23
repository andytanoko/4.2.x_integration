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
package com.gridnode.ext.util;

import com.gridnode.util.SystemUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class DocumentUtil
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
  
  public static String getOwnBEID(String folder, String recipientBizEntityID, String senderBizEntityID, String srcFolder)
  {
    if("Inbound".equals(folder))
    {
      return recipientBizEntityID;
    }
    else if("Outbound".equals(folder))
    {
      return senderBizEntityID;
    }
    else if("Inbound".equals(srcFolder) && "Export".equals(folder)) // IB -> EP case
    {
      return recipientBizEntityID;
    }
    else if("Import".equals(srcFolder) && "Export".equals(folder)) //IP -> EP case
    {
      return senderBizEntityID;
    }
    else if("Import".equals(folder))
    {
      return senderBizEntityID;
    }
    else
    {
      throw new IllegalArgumentException("[DocumentUtil.getOwnBEID] folder "+folder+" is not supported !");
    }
  }
  
  /**
   * Get the PartnerID that we receive the document from them or we sent the document to them
   * @param folder the current folder that the GDOC reside
   * @param recipientPartnerID
   * @param senderPartnerID
   * @param srcFolder the folder that the current GDOC transist from.
   * @return partnerID
   */
  public static String getPartnerID(String folder, String recipientPartnerID, String senderPartnerID, String srcFolder)
  {
    if("Inbound".equals(folder))
    {
      return senderPartnerID;
    }
    else if("Outbound".equals(folder))
    {
      return recipientPartnerID;
    }
    else if("Inbound".equals(srcFolder) && "Export".equals(folder)) // IB -> EP case
    {
      return senderPartnerID;
    }
    else if("Import".equals(srcFolder) && "Export".equals(folder)) //IP -> EP case
    {
      return recipientPartnerID;
    }
    else if("Import".equals(folder))
    {
      return recipientPartnerID;
    }
    else
    {
      throw new IllegalArgumentException("[DocumentUtil.getOwnBEID] folder "+folder+" is not supported !");
    }
  }
}


/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: DocumentUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 6, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.folder.ExportFolder;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;

/**
 * This class handle the generic Document related operation.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class DocumentUtil
{
  
  private static final String IB_FOLDER = "IB";
  private static final String OB_FOLDER = "OB";
  private static final String IP_FOLDER = "IP";
  private static final String EP_FOLDER = "EP";
  
  /**
   * Get the MessageID given the gdoc.
   * @param gdoc
   * @return the concatenate str of gdoc folder(short form) and gdocID
   */
  public static String getMessageID(String folder, String gdocID)
  {
    String folderAbbreviate = "";
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = IB_FOLDER;
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = OB_FOLDER;
    }
    else if(ExportFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = EP_FOLDER;
    }
    else if(ImportFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = IP_FOLDER;
    }
    else
    {
      throw new IllegalArgumentException("[DocumentUtil.getMessageID] Folder "+folder+" is not supported !");
    }
    return folderAbbreviate+"-"+gdocID;
  }
  
  /**
   * Derive the Folder name given the msg id
   * @param msgId The GT Document message ID (Format:  IB-123, OB-111, EP-222, IP-155)
   * @return
   */
  public static String getFolder(String msgId)
  {
    if(msgId == null || msgId.trim().length() == 0)
    {
      throw new IllegalArgumentException("Unsupported GT message ID format "+msgId);
    }
    
    String[] s = msgId.split("-");
    if(s.length != 2)
    {
      throw new IllegalArgumentException("Unsupported message ID format "+msgId+". Expecting <folder>-<running number>");
    }
    else
    {
      String folderAbbreviate = s[0];
      
      if(IP_FOLDER.equals(folderAbbreviate))
      {
        return ImportFolder.FOLDER_NAME;
      }
      else if(EP_FOLDER.equals(folderAbbreviate))
      {
        return ExportFolder.FOLDER_NAME;
      }
      else if(IB_FOLDER.equals(folderAbbreviate))
      {
        return InboundFolder.FOLDER_NAME;
      }
      else if(OB_FOLDER.equals(folderAbbreviate))
      {
        return OutboundFolder.FOLDER_NAME;
      }
      else
      {
        throw new IllegalArgumentException("Unsupported Folder abbreviation="+folderAbbreviate);
      }
    }
    
  }
  
  public static String getGdocId(String msgId)
  {
    if(msgId == null || msgId.trim().length() == 0)
    {
      throw new IllegalArgumentException("Unsupported GT message ID format "+msgId);
    }
    
    String[] s = msgId.split("-");
    if(s.length != 2)
    {
      throw new IllegalArgumentException("Unsupported message ID format "+msgId+". Expecting <folder>-<running number>");
    }
    else
    {
      return s[1];
    }
  }
  
  public static void main(String[] args)
  {
    String folder = DocumentUtil.getFolder("IB-123");
    String gdocId = DocumentUtil.getGdocId("IB-123");
    System.out.println("Folder: "+folder+" gdocId="+gdocId);
    
    folder = DocumentUtil.getFolder("OB-123");
    System.out.println("Folder: "+folder);
    
    folder = DocumentUtil.getFolder("EP-123");
    System.out.println("Folder: "+folder);
    
    folder = DocumentUtil.getFolder("IP-123");
    System.out.println("Folder: "+folder);
  }
}

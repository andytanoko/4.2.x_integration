/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 14, 2007   i00107              Created
 */

package com.gridnode.gtas.server.document.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * @author i00107
 * This is a helper class that provides helpful methods for use during import of files into GridTalk.
 */
public class ImportDocumentHelper
{
  
  public GridDocument prepareImportDoc(String userId, String userName, String senderNodeId, String senderBizEntId, String recipientPartnerId, 
                        String docType, String udocFilename, List attachmentUids, 
                        String rnProfile, String uniqueDocId, String processInstanceId, String tracingID)
    throws Throwable
  {
    boolean useXpath = false;
    if (recipientPartnerId == null)
    {
      useXpath = true;
    }
    
    String[] xpathInfo = new String[4];
    if ((senderBizEntId == null) || (senderBizEntId.equals("")))
    {
      senderBizEntId = getBusinessEntityId();
    }

    if (senderNodeId == null || senderNodeId.length()==0)
    {
      senderNodeId = getOwnGridnodeId();
    }
    String docNum = null;

    String tempUdocFilename = udocFilename.substring(udocFilename.indexOf(":")+1);
    udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
    String fileExt = getOriginalFileExt(udocFilename);
    Logger.debug("[ImportDocumentHelper.importDoc] fileExt = "+fileExt);

    if (useXpath)
    {
      Logger.debug("[ImportDocumentHelper.importDoc] Using Xpath");
      xpathInfo = getXpathInfo(tempUdocFilename);
      if (xpathInfo[0] != null)
      {
        docType = xpathInfo[0];
      }
      if (xpathInfo[1] != null)
      {
        recipientPartnerId = xpathInfo[1];
      }
      if (xpathInfo[3] != null)
      {
        docNum = xpathInfo[3];
      }
    }
    
    if (recipientPartnerId == null || recipientPartnerId.equals(""))
    {
      Logger.log("[ImportDocumentHelper.importDoc] RecipientPartner not set");
    }

    GridDocument gridDoc = new GridDocument();
    
    //SC
    gridDoc.setProcessInstanceID(processInstanceId);
    gridDoc.setTracingID(tracingID); //TWX 10112006
    
    //SC LOG
    Logger.debug("[ImportDocumentHelper.importDoc] gridDoc.getProcessInstanceID() = " + gridDoc.getProcessInstanceID());
    
    gridDoc.setUdocFilename(udocFilename);
    gridDoc.setTempUdocFilename(tempUdocFilename);
    Logger.debug("[ImportDocumentHelper.importDoc] tempUdocFilename = "+tempUdocFilename);
    gridDoc.setUdocDocType(docType);
    gridDoc.setSenderBizEntityId(senderBizEntId);
    gridDoc.setSenderUserId(userId);
    gridDoc.setSenderUserName(userName);
    gridDoc.setRecipientPartnerId(recipientPartnerId);
    gridDoc.setUdocFileType(fileExt);
    gridDoc.setSenderNodeId(new Long(senderNodeId));
    if (docNum != null)
    {
      gridDoc.setUdocNum(docNum);
    }

    if ((rnProfile != null) && (!rnProfile.equals("")))
    {
      gridDoc.setRnProfileUid(new Long(rnProfile));
    }

    if ((uniqueDocId != null) && (!uniqueDocId.equals("")))
    {
      gridDoc.setUniqueDocIdentifier(uniqueDocId);
    }

    File udoc = FileHelper.getUdocFile(gridDoc);
    if (udoc == null)
    {
      throw new Exception("Udoc not found");
    }
    long fileSize = Math.round(udoc.length()/1024);
    if (fileSize == 0)
    {
      fileSize = 1;
    }
    gridDoc.setUdocFileSize(new Long(fileSize));
    gridDoc.setUdocVersion(new Integer(1));
    gridDoc.setUdocFullPath(udoc.getAbsolutePath());

    if (attachmentUids!=null && !attachmentUids.isEmpty())
    {
      gridDoc.setHasAttachment(Boolean.TRUE);
      gridDoc.setAttachments(attachmentUids);
    }

    return gridDoc;
  }
  
  public String getBusinessEntityId() throws Exception
  {
    String bizEntId = null;
    BusinessEntity bizEnt = BizRegDelegate.getDefaultBusinessEntity();
    if (bizEnt != null)
    {
      bizEntId = bizEnt.getBusEntId();
    }
    
    return bizEntId;
  }
  
  public String getOwnGridnodeId() throws Exception
  {
    return BizRegDelegate.getGridNodeManager().findMyGridNode().getID();
  }
  
  private String getOriginalFileExt(String fullFilename)
  {
    Logger.debug("[ImportDocumentHelper.getOriginalFileExt] fullFilename = "+fullFilename);
    String ext = "";
    if ((fullFilename.indexOf("/") > 0) && (fullFilename.lastIndexOf("/")+1 != fullFilename.length()))
    {
      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("/")+1);
    }
    if ((fullFilename.indexOf("\\") > 0) && (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
    {
      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("\\")+1);
    }

    // check to see if last char is not "/" or "\"
    if ((fullFilename.lastIndexOf("/")+1 != fullFilename.length()) &&
        (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
    {
      if ((fullFilename.indexOf(".") > 0) && (fullFilename.lastIndexOf(".")+1 != fullFilename.length()))
      {
        ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
      }
    }
    Logger.debug("[ImportDocumentHelper.getOriginalFileExt] ext = "+ext);

    return ext;
  }

  private String[] getXpathInfo(String tempUdocFilename) throws Exception
  {
    // udocFilename = orgFilename:tempFilename
    String tmpdir = System.getProperty("java.io.tmpdir");
    File udoc = new File(tmpdir, tempUdocFilename);
    return MapperDelegate.getAppMappingManager().getXpathInfo(udoc);
  }

  /**
   * This method copies all the imported files from the specified folder
   * to the udoc folder
   */
  public List copyFiles(List filesToCopy, String srcPathKey, String subPath)
    throws Exception
  {
    ArrayList<String> newImportFiles = new ArrayList<String>();
    for(Iterator i = filesToCopy.iterator(); i.hasNext(); )
    {
      String filename = i.next().toString();
      Logger.debug("[ImportDocumentHelper.copyFiles] filename = "+filename);
      File udocFile = FileUtil.getFile(srcPathKey,
                                       subPath,
                                       filename);
      String newFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

      Logger.debug("[ImportDocumentHelper.copyFiles] newFilename = "+newFilename);
      newImportFiles.add(filename+":"+newFilename);
    }
    return newImportFiles;
  }

  public void deleteFiles(List files, String srcPathKey, String subPath)
  {
    try
    {
      for(Iterator i = files.iterator(); i.hasNext(); )
      {
        String filename = i.next().toString();
        long ts = System.currentTimeMillis();
        FileUtil.delete(srcPathKey, subPath, filename);
        Logger.debug("[ImportDocumentHelper.deleteFiles] Deleted file: "+filename + ", timeTodelete(ms)="+(System.currentTimeMillis()-ts));
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[ImportDocumentHelper.deleteFiles] Exception in deleting import files from: {"+srcPathKey+"}/"+subPath, ex);
    }
  }
}
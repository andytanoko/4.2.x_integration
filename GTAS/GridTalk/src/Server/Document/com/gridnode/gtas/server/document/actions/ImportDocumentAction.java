/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 16 2002    Koh Han Sing        Created
 * Nov 25 2002    Koh Han Sing        Add in attachment logic
 */
package com.gridnode.gtas.server.document.actions;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.events.document.ImportDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the importing of documents.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ImportDocumentAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 962077991556365668L;
	public static final String ACTION_NAME = "ImportDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ImportDocumentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ImportDocumentEvent importDocEvent = (ImportDocumentEvent)event;
    return new Object[]
           {
             importDocEvent.getDocumentType(),
             importDocEvent.getSenderId(),
             importDocEvent.getImportFiles(),
             importDocEvent.getRecipients(),
             importDocEvent.getAttachments()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ImportDocumentEvent impEvent = (ImportDocumentEvent)event;
    List importFiles = impEvent.getImportFiles();
    List recipients = impEvent.getRecipients();
    List attachments = impEvent.getAttachments();

    try
    {
	  List attachmentUids = null;
	  if (attachments!=null && !attachments.isEmpty())
	  {
	    attachmentUids = ActionHelper.getManager().importAttachments(
                           attachments,
                           IDocumentPathConfig.PATH_TEMP,
                           _userID+"/in/");
	  }

      for(Iterator i = recipients.iterator(); i.hasNext(); )
      {
        List newImportFiles = copyfiles(importFiles);
        String recipient = i.next().toString();
        Logger.debug("[ImportDocumentAction.doProcess] recipient = "+recipient);
        importFiles(newImportFiles, attachmentUids, recipient, impEvent);
      }

      deletefiles(importFiles);
      deletefiles(attachments);
    }
    catch (Throwable ex)
    {
      deletefiles(importFiles);
      deletefiles(attachments);
      throw ex;
    }
    return constructEventResponse();
  }

  /**
   * This method copies all the imported files from the user temp folder
   * to the udoc folder
   */
  private List copyfiles(List filesToCopy)
    throws Exception
  {
    ArrayList newImportFiles = new ArrayList();
    for(Iterator i = filesToCopy.iterator(); i.hasNext(); )
    {
      String filename = i.next().toString();
      Logger.debug("[ImportDocumentAction.copyfiles] filename = "+filename);
//      String newFilename = FileUtil.copy(IPathConfig.PATH_TEMP,
//                                         _userID+"/in/",
//                                         filename,
//                                         IDocumentPathConfig.PATH_UDOC,
//                                         "",
//                                         filename);
      File udocFile = FileUtil.getFile(IDocumentPathConfig.PATH_TEMP,
                                       _userID+"/in/",
                                       filename);
      String newFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

      Logger.debug("[ImportDocumentAction.copyfiles] newFilename = "+newFilename);
      newImportFiles.add(filename+":"+newFilename);
    }
    return newImportFiles;
  }

  /**
   * This method creates the initial griddocument for each of the
   * imported files.
   *
   * @param
   */
  private void importFiles(List importFiles,
                           List attachmentUids,
                           String recipientPartnerId,
                           ImportDocumentEvent impEvent)
    throws Throwable
  {
    String bizEntId = impEvent.getSenderId();
    String docType = impEvent.getDocumentType();
    Logger.debug("[ImportDocumentAction.importFiles] bizEntId = "+bizEntId);
    Logger.debug("[ImportDocumentAction.importFiles] docType = "+docType);
//    ArrayList gridDocs = new ArrayList();

    for(Iterator i = importFiles.iterator(); i.hasNext(); )
    {
      String udocFilename = i.next().toString();
      String tempUdocFilename = udocFilename.substring(udocFilename.indexOf(":")+1);
      udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
      String fileExt = getOriginalFileExt(udocFilename);
      Logger.debug("[ImportDocumentAction.importFiles] fileExt = "+fileExt);

      GridDocument gridDoc = new GridDocument();
      gridDoc.setUdocFilename(udocFilename);
      gridDoc.setTempUdocFilename(tempUdocFilename);
      gridDoc.setUdocDocType(docType);
      gridDoc.setSenderBizEntityId(bizEntId);
      gridDoc.setSenderUserId(_userID);
      gridDoc.setSenderUserName(getUserName());
      Logger.debug("[ImportDocumentAction.importFiles] UserName = " + getUserName());
      gridDoc.setRecipientPartnerId(recipientPartnerId);
      gridDoc.setUdocFileType(fileExt);
      gridDoc.setSenderNodeId(new Long(getEnterpriseID()));

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

      if (attachmentUids != null && !attachmentUids.isEmpty())
      {
        gridDoc.setHasAttachment(Boolean.TRUE);
        gridDoc.setAttachments(attachmentUids);
      }

//      gridDocs.add(gridDoc);
      ActionHelper.getManager().importDoc(gridDoc);
    }

//    ActionHelper.getManager().importDoc(gridDocs);
  }

  private void deletefiles(List importFiles)
    throws Exception
  {
    for(Iterator i = importFiles.iterator(); i.hasNext(); )
    {
      String filename = i.next().toString();
      FileUtil.delete(IDocumentPathConfig.PATH_TEMP, _userID+"/in/", filename);
    }
  }

  private String getOriginalFileExt(String fullFilename)
  {
    Logger.debug("[ImportDocumentAction.getOriginalFileExt] fullFilename = "+fullFilename);
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
        Logger.debug("[ImportDocumentAction.getOriginalFileExt] found . in filename");
        ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
      }
    }
    Logger.debug("[ImportDocumentAction.getOriginalFileExt] ext = "+ext);

    return ext;
  }
}

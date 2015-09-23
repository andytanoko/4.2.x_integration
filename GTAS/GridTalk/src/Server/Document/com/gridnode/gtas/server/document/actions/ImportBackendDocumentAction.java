/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportBackendDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22 2002    Koh Han Sing        Created
 * Mar 19 2003    Koh Han Sing        Don't throw exception even if recipient
 *                                    partner Id is not set.
 * Apr 24 2003    Koh Han Sing        If business entity Id is null, set to
 *                                    default business entity Id.
 * May 21 2003    Neo Sok Lay         Raise alert if partner function failure
 *                                    during import.
 * Jun 20 2003    Koh Han Sing        Set Unique Id into GridDocument if any.
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Nov 10 2005    Neo Sok Lay         DocumentManagerBean.importDoc changed interface
 *                                    to throw ApplicationException only.                                   
 * 9 Dec 2005     SC                  In importFiles method, set process instance id to gridDoc
 * Nov 10 2006    Tam Wei Xiang       Add tracingID into gridDoc. To be used in the Audit-Trail
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.events.document.ImportBackendDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.helpers.*;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the importing of documents from the backend
 * system
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ImportBackendDocumentAction
  extends    AbstractGridTalkAction
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -6471482192157626758L;
  public static final String ACTION_NAME = "ImportBackendDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ImportBackendDocumentEvent.class;
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
    ImportBackendDocumentEvent importDocEvent = (ImportBackendDocumentEvent)event;
    return new Object[]
           {
             importDocEvent.getDocumentType(),
             importDocEvent.getSenderId(),
             importDocEvent.getImportFiles(),
             importDocEvent.getRecipients(),
             importDocEvent.getAttachments(),
             importDocEvent.getRnProfile(),
             importDocEvent.getUniqueDocId(),
             importDocEvent.getProcessInstanceId()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ImportBackendDocumentEvent impEvent = (ImportBackendDocumentEvent)event;
    List importFiles = impEvent.getImportFiles();
    List recipients = impEvent.getRecipients();
    List attachments = impEvent.getAttachments();
    Logger.debug("[ImportBackendDocumentAction.doProcess] importFiles = "+importFiles);
    Logger.debug("[ImportBackendDocumentAction.doProcess] recipients = "+recipients);
    Logger.debug("[ImportBackendDocumentAction.doProcess] attachments = "+attachments);

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

      if (recipients.isEmpty())
      {
        Logger.debug("[ImportBackendDocumentAction.doProcess] no recipients found");
        List newImportFiles = copyfiles(importFiles);
        importFiles(newImportFiles, attachmentUids, null, impEvent);
      }
      else
      {
        for(Iterator i = recipients.iterator(); i.hasNext(); )
        {
          List newImportFiles = copyfiles(importFiles);
          Logger.debug("[ImportBackendDocumentAction.doProcess] newImportFiles = "+newImportFiles);
          String recipient = i.next().toString();
          Logger.debug("[ImportBackendDocumentAction.doProcess] recipient = "+recipient);
          importFiles(newImportFiles, attachmentUids, recipient, impEvent);
        }
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
      Logger.debug("[ImportBackendDocumentAction.copyfiles] filename = "+filename);
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

      Logger.debug("[ImportBackendDocumentAction.copyfiles] newFilename = "+newFilename);
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
                           ImportBackendDocumentEvent impEvent)
    throws Throwable
  {
    boolean useXpath = false;
    if (recipientPartnerId == null)
    {
      useXpath = true;
    }
    String[] xpathInfo = new String[4];

    String bizEntId = impEvent.getSenderId();
    if ((bizEntId == null) || (bizEntId.equals("")))
    {
      bizEntId = getBusinessEntityId();
    }
    String docType = impEvent.getDocumentType();
    String rnProfile = impEvent.getRnProfile();
    String uniqueDocId = impEvent.getUniqueDocId();
    
    //SC
    String processInstanceId = impEvent.getProcessInstanceId();
    String tracingID = impEvent.getTracingID(); //TWX 10112006
    
    //SC LOG
    log("processInstanceId = " + processInstanceId);
    
    Logger.debug("[ImportBackendDocumentAction.importFiles] bizEntId = "+bizEntId);
    Logger.debug("[ImportBackendDocumentAction.importFiles] docType = "+docType);
    Logger.debug("[ImportBackendDocumentAction.importFiles] rnProfile = "+rnProfile);
//    ArrayList gridDocs = new ArrayList();

    for(Iterator i = importFiles.iterator(); i.hasNext(); )
    {
      String docNum = null;
      String udocFilename = i.next().toString();

      String tempUdocFilename = udocFilename.substring(udocFilename.indexOf(":")+1);
      udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
      String fileExt = getOriginalFileExt(udocFilename);
      Logger.debug("[ImportBackendDocumentAction.importFiles] fileExt = "+fileExt);

      if (useXpath)
      {
        Logger.debug("[ImportBackendDocumentAction.importFiles] Using Xpath");
        xpathInfo = getXpathInfo(tempUdocFilename);
        if (xpathInfo[0] != null)
        {
          docType = xpathInfo[0];
        }
        if (xpathInfo[1] != null)
        {
          recipientPartnerId = xpathInfo[1];
        }
//        if (xpathInfo[2] != null)
//        {
//          recipientPartnerType = xpathInfo[2];
//        }
        if (xpathInfo[3] != null)
        {
          docNum = xpathInfo[3];
        }
      }

      if (recipientPartnerId == null || recipientPartnerId.equals(""))
      {
        Logger.log("[ImportBackendDocumentAction.importFiles] RecipientPartner not set");
        //throw new ImportException("RecipientPartner not set");
      }

      GridDocument gridDoc = new GridDocument();
      
      //SC
      gridDoc.setProcessInstanceID(processInstanceId);
      gridDoc.setTracingID(tracingID); //TWX 10112006
      
      //SC LOG
      log("gridDoc.getProcessInstanceID() = " + gridDoc.getProcessInstanceID());
      
      gridDoc.setUdocFilename(udocFilename);
      gridDoc.setTempUdocFilename(tempUdocFilename);
      Logger.debug("[ImportBackendDocumentAction.importFiles] tempUdocFilename = "+tempUdocFilename);
      gridDoc.setUdocDocType(docType);
      gridDoc.setSenderBizEntityId(bizEntId);
      gridDoc.setSenderUserId(_userID);
      gridDoc.setSenderUserName(getUserName());
      Logger.debug("[ImportBackendDocumentAction.importFiles] UserName = "
                    + getUserName());
      gridDoc.setRecipientPartnerId(recipientPartnerId);
      gridDoc.setUdocFileType(fileExt);
      gridDoc.setSenderNodeId(new Long(getEnterpriseID()));
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

//      gridDocs.add(gridDoc);
      try
      {
        ActionHelper.getManager().importDoc(gridDoc);
      }
      catch (ApplicationException ex) //NSL20051110 Throw PartnerFunctionFailure here
      {
      	new PartnerFunctionFailure(gridDoc, 
      	                           PartnerFunctionFailure.TYPE_IMPORT_DOC_FAILURE, 
      	                           PartnerFunctionFailure.REASON_GENERAL_ERROR, 
      	                           ex).raiseAlert();
      }
      /*
      catch (PartnerFunctionFailure failure)
      {
        failure.raiseAlert();
      }*/
    }

//    ActionHelper.getManager().importDoc(gridDocs);
  }

  private void deletefiles(List importFiles)
  {
    try
    {
      for(Iterator i = importFiles.iterator(); i.hasNext(); )
      {
        String filename = i.next().toString();
        FileUtil.delete(IDocumentPathConfig.PATH_TEMP, _userID+"/in/", filename);
      }
    }
    catch (Exception ex)
    {
      Logger.warn("Exception in deleting import files in user temp folder", ex);
    }
  }

  private String getOriginalFileExt(String fullFilename)
  {
    Logger.debug("[ImportBackendDocumentAction.getOriginalFileExt] fullFilename = "+fullFilename);
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
        Logger.debug("[ImportBackendDocumentAction.getOriginalFileExt] found . in filename");
        ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
      }
    }
    Logger.debug("[ImportBackendDocumentAction.getOriginalFileExt] ext = "+ext);

    return ext;
  }

  private String[] getXpathInfo(String tempUdocFilename)
    throws Exception
  {
    // udocFilename = orgFilename:tempFilename
    String tmpdir = System.getProperty("java.io.tmpdir");
    File udoc = new File(tmpdir+"/"+tempUdocFilename);
    return MapperDelegate.getAppMappingManager().getXpathInfo(udoc);
  }

  private String getBusinessEntityId()
    throws Exception
  {
    String bizEntId = null;
    BusinessEntity bizEnt = BizRegDelegate.getDefaultBusinessEntity();
    if (bizEnt != null)
    {
      bizEntId = bizEnt.getBusEntId();
    }
    else
    {
      Long userId = Long.getLong(_userID);
      Collection bizEnts = BizRegDelegate.getEnterpriseHierarchyManager().getBizEntitiesForUser(userId);
      if (!bizEnts.isEmpty())
      {
        bizEnt = (BusinessEntity)bizEnts.iterator().next();
        bizEntId = bizEnt.getBusEntId();
      }
    }
    return bizEntId;
  }
  
//SC LOG
  private void log(String message)
  {
  	Logger.log("[ImportBackendDocumentAction] " + message);
  }
}

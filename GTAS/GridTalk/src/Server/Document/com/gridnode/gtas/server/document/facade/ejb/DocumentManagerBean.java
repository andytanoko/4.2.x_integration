/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Jul 23 2002    Koh Han Sing        Added managment of FileType
 * Aug 02 2002    Koh Han Sing        Added managment of GridDocument
 * Nov 25 2002    Koh Han Sing        Added attachment feature
 * Jan 15 2003    Neo Sok Lay         receiveDocAck() renamed to handleSendDocAck()
 *                                    - to accept TrxId instead of eventID as
 *                                      parameter to use as GDocId of sent GridDoc.
 *                                    Added handleUploadDocAck().
 *                                    Added findGridDocument(folder,gdocId).
 * Mar 28 2003    Koh Han Sing        Remove temp files after exit from folders
 * May 05 2003    Neo Sok Lay         Add handleDocAccepted(), handleDocRejected()
 *                                    & handleDocSent()
 *                                    methods to allow call from Rnif module.
 *                                    Raise DOCUMENT_RECEIVED_BY_PARTNER alert
 *                                    when transaction complete.
 * May 21 2003    Neo Sok Lay         Throw PartnerFunctionFailure if error
 *                                    occurred during importDoc().
 * May 29 2003    Neo Sok Lay         GNDB00013989: Logic error in processAcceptedDoc()
 *                                    for transCompleted timestamp.
 * Jun 17 2003    Koh Han Sing        GNDB00014308: Create new temp files when
 *                                    exiting to new folders so as not to share
 *                                    temp files between folders.
 * Jun 20 2003    Neo Sok Lay         Broadcast document status change: Send Start/End,
 *                                    Trans End/Rejected.
 * Aug 20 2003    Koh Han Sing        GNDB00014712: Error occurs randomly when
 *                                    deleting documents from outbound folder.
 * Aug 07 2003    Koh Han Sing        Resume send/upload.
 * Sep 02 2003    Koh Han Sing        Added find attachments by griddocument
 * Oct 02 2003    Koh Han Sing        Manual Export
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders..
 * Oct 22 2003    Guo Jianyu          Modified uploadToGridMaster() and resumeSend()
 *                                    such that gdoc.recipientChannel, if not null,
 *                                    won't be overwritten with the default channel
 *                                    of the partner.
 * Nov 05 2003    Neo Sok Lay         To pass additionalHeaders to processNormalDoc().
 * Jan 29 2004    Neo Sok Lay         Handle updating to RecAckProcessed and DateTimeTransCompleted
 *                                    on Business level acknowledgement (e.g. RN ACK).
 * Jun 30 2004    Neo Sok Lay         GNDB00025083: During resume upload to GM (uploadToGridMaste()) 
 *                                    or resumeSend(), filter out those docs that do not have recipient 
 *                                    partner id since they are not valid for sending.
 * Sep 07 2005    Neo Sok Lay         Change createDocumentType() to return key of created entity.                                   
 * Oct 26 2005    Neo Sok Lay         Business methods throws Throwable is not
 *                                    acceptable for SAP J2EE deployment 
 *                                    Revised error handling.
 * Nov 14 2005    Neo Sok Lay         Change getCompleteGridDocument() and updateGridDocument():
 *                                    - Use FileUtil only to get gdoc file        
 * Nov 17 2005    Tam Wei Xiang       modified method saveToFolder(gdoc) 
 * Dec 26 2006    Tam Wei Xiang       Modified sendDocumentFlowNotification(...)
 *                                    i) change the type of docFlowType from string to EDocumentFlowType. 
 *                                    ii)If the gdoc hasn't been persisted, the value of the folder
 *                                       will be the src folder.   
 * Jan 10 2007    Tam Wei Xiang       Added isRetry flag in DocumentTransaction. Modified
 *                                    receiveFailedRnifDoc(...), receiveDoc(...)    
 * Jan 31 2007    Tam Wei Xiang       Added in method for reprocess doc  
 * Mar 15 2007    Neo Sok Lay         Add importDocument().   
 * Apr 17 2007    Tam Wei Xiang       The handling of the exception msg from the method invocation
 *                                    through reflection is not extracted out properly.
 *                                    The event Document Acknowledged will be fired and record down
 *                                    the error if some exception has occured.                                                                                                  
 * Nov 12 2008    Wong Yee Wah        Added method: CRUD for As2DocTypeMapping
 */
package com.gridnode.gtas.server.document.facade.ejb;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.document.entities.ejb.IAttachmentLocalObj;
import com.gridnode.gtas.server.document.entities.ejb.IFileTypeLocalObj;
import com.gridnode.gtas.server.document.exceptions.CreateAttachmentException;
import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.exceptions.SearchDocumentException;
import com.gridnode.gtas.server.document.folder.*;
import com.gridnode.gtas.server.document.helpers.*;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.TriggerInfo;
import com.gridnode.gtas.server.document.notification.DocumentTransactionHandler;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.ejb.IJMSMsgHandlerLocalHome;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;

/**
 * This bean manages the document types.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class DocumentManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4988781060220208238L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IDocumentTypeManagerLocalObj

  /**
   * Create a new DocumentType.
   *
   * @param documentType The DocumentType entity.
   */
  public Long createDocumentType(DocumentType documentType)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[DocumentManagerBean.createDocumentType] Enter");

    DocumentType docType = null;
    try
    {
      docType = (DocumentType)getDocumentTypeEntityHandler().createEntity(documentType);
    }
    catch (CreateException ex)
    {
      Logger.warn("[DocumentManagerBean.createDocumentType] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[DocumentManagerBean.createDocumentType] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.createDocumentType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.createDocumentType(DocumentType) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.createDocumentType] Exit");
    return (Long)docType.getKey();
  }

  /**
   * Update a DocumentType
   *
   * @param documentType The DocumentType entity with changes.
   */
  public void updateDocumentType(DocumentType documentType)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.updateDocumentType] Enter");

    try
    {
      getDocumentTypeEntityHandler().update(documentType);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.updateDocumentType] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.updateDocumentType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.updateDocumentType(DocumentType) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.updateDocumentType] Exit");
  }

  /**
   * Delete a DocumentType.
   *
   * @param documentTypeUId The UID of the DocumentType to delete.
   */
  public void deleteDocumentType(Long documentTypeUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.deleteDocumentType] Enter");

    try
    {
      getDocumentTypeEntityHandler().remove(documentTypeUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteDocumentType] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteDocumentType] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.deleteDocumentType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.deleteDocumentType(documentTypeUId) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.deleteDocumentType] Exit");
  }

  /**
   * Find a DocumentType using the DocumentType UID.
   *
   * @param documentTypeUId The UID of the DocumentType to find.
   * @return The DocumentType found, or <B>null</B> if none exists with that
   * UID.
   */
  public DocumentType findDocumentType(Long documentTypeUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findDocumentType] UID: "+documentTypeUId);

    DocumentType docType = null;

    try
    {
      docType =
        (DocumentType)getDocumentTypeEntityHandler().getEntityByKeyForReadOnly(
          documentTypeUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findDocumentType(documentTypeUId) Error ",
        ex);
    }

    return docType;
  }

  /**
   * Find a DocumentType using the DocumentType Name.
   *
   * @param docTypeName The DocumentType Name of the DocumentType to find.
   * @return The DocumentType found, or <B>null</B> if none exists.
   */
  public DocumentType findDocumentType(String docTypeName)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findDocumentType] Document Type Name: "
      +docTypeName);

    DocumentType docType = null;

    try
    {
      docType =
        (DocumentType)getDocumentTypeEntityHandler().findByDocumentType(
          docTypeName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findDocumentType(docTypeName) Error ",
        ex);
    }

    return docType;
  }

  /**
   * Find a number of DocumentType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of DocumentType found, or empty collection if none
   * exists.
   */
  public Collection findDocumentTypes(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findDocumentTypes] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection docTypes = null;
    try
    {
      docTypes =
        getDocumentTypeEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findDocumentTypes(filter) Error ",
        ex);
    }

    return docTypes;
  }

  /**
   * Find a number of DocumentType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of DocumentType found, or empty collection if
   * none exists.
   */
  public Collection findDocumentTypesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findDocumentTypes] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection docTypesKeys = null;
    try
    {
      docTypesKeys =
        getDocumentTypeEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findDocumentTypes] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findDocumentTypes(filter) Error ",
        ex);
    }

    return docTypesKeys;
  }

  // ********************* FileType ****************************

  /**
   * Create a new FileType.
   *
   * @param fileType The FileType entity.
   */
  public Long createFileType(FileType fileType)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[DocumentManagerBean.createFileType] Enter");

    try
    {
      IFileTypeLocalObj obj =
        (IFileTypeLocalObj)getFileTypeEntityHandler().create(fileType);

      Logger.log("[DocumentManagerBean.createFileType] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[DocumentManagerBean.createFileType] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[DocumentManagerBean.createFileType] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.createFileType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.createFileType(FileType) Error ",
        ex);
    }

  }

  /**
   * Update a FileType
   *
   * @param fileType The FileType entity with changes.
   */
  public void updateFileType(FileType fileType)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.updateFileType] Enter");

    try
    {
      getFileTypeEntityHandler().update(fileType);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.updateFileType] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.updateFileType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.updateFileType(FileType) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.updateFileType] Exit");
  }

  /**
   * Delete a FileType.
   *
   * @param fileTypeUId The UID of the FileType to delete.
   */
  public void deleteFileType(Long fileTypeUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.deleteFileType] Enter");

    try
    {
      getFileTypeEntityHandler().remove(fileTypeUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteFileType] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteFileType] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.deleteFileType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.deleteFileType(fileTypeUId) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.deleteFileType] Exit");
  }

  /**
   * Find a FileType using the FileType UID.
   *
   * @param fileTypeUId The UID of the FileType to find.
   * @return The FileType found, or <B>null</B> if none exists with that
   * UID.
   */
  public FileType findFileType(Long fileTypeUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findFileType] UID: "+fileTypeUId);

    FileType filetype = null;

    try
    {
      filetype =
        (FileType)getFileTypeEntityHandler().getEntityByKeyForReadOnly(fileTypeUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findFileType(fileTypeUId) Error ",
        ex);
    }

    return filetype;
  }

  /**
   * Find a FileType using the FileType Name.
   *
   * @param fileTypeName The FileType Name of the FileType to find.
   * @return The FileType found, or <B>null</B> if none exists.
   */
  public FileType findFileType(String fileTypeName)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findFileType] Document Type Name: "
      +fileTypeName);

    FileType filetype = null;

    try
    {
      filetype = (FileType)getFileTypeEntityHandler().findByFileType(fileTypeName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findFileType] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findFileType(fileTypeName) Error ",
        ex);
    }

    return filetype;
  }

  /**
   * Find a number of FileType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of FileType found, or empty collection if none
   * exists.
   */
  public Collection findFileTypes(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findFileTypes] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection filetypes = null;
    try
    {
      filetypes = getFileTypeEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findFileTypes(filter) Error ",
        ex);
    }

    return filetypes;
  }

  /**
   * Find a number of FileType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of FileType found, or empty collection if
   * none exists.
   */
  public Collection findFileTypesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findFileTypes] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection fileTypesKeys = null;
    try
    {
      fileTypesKeys = getFileTypeEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findFileTypes] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findFileTypes(filter) Error ",
        ex);
    }

    return fileTypesKeys;
  }

  // ********************* GridDocument ****************************

  /**
   * Create a new GridDocument.
   *
   * @param gDoc The GridDocument entity.
   */
  public GridDocument createGridDocument(GridDocument gDoc)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    return  createGridDocument(gDoc, true);
  }
  
  public GridDocument createGridDocumentWithNewTrans(GridDocument gDoc)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    return createGridDocument(gDoc, true);
  }
  
  /**
   * Create a new GridDocument.
   *
   * @param gDoc The GridDocument entity.
   * @param updateGDocId wether to generate a GDocId or use the original one
   */
  public GridDocument createGridDocument(GridDocument gDoc, boolean updateGDocId)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[DocumentManagerBean.createGridDocument] Enter");

    try
    {
      gDoc = SystemFolder.getSpecificFolder(gDoc.getFolder()).createHeader(gDoc, updateGDocId);
//      IGridDocumentLocalObj obj =
//        (IGridDocumentLocalObj)getGridDocumentEntityHandler().create(gDoc);
      Logger.log("[DocumentManagerBean.createGridDocument] Exit");
//      return (Long)obj.getData().getKey();
      //return (Long)gDoc.getKey();
      return gDoc;
    }
    catch (CreateException ex)
    {
      Logger.warn("[DocumentManagerBean.createGridDocument] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[DocumentManagerBean.createGridDocument] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.createGridDocument] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.createGridDocument(GridDocument) Error ",
        ex);
    }

  }

  /**
   * Update a GridDocument
   *
   * @param gDoc The GridDocument entity with changes.
   */
  public void updateGridDocument(GridDocument gDoc)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.updateGridDocument] Enter");

    try
    {
      getGridDocumentEntityHandler().update(gDoc);
      /*
      String gdocFullPath = FileUtil.getDomain()+File.separator+
                            FileUtil.getPath(IDocumentPathConfig.PATH_GDOC)+
                            File.separator+gDoc.getFolder()+
                            File.separator+gDoc.getGdocFilename();
      */
      //NSL20051114 Use Only FileUtil to get the filepath
      String gdocFullPath = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder()+"/", gDoc.getGdocFilename()).getCanonicalPath();
      gDoc.serialize(gdocFullPath);

    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.updateGridDocument] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.updateGridDocument] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.updateGridDocument(GridDocument) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.updateGridDocument] Exit");
  }

  /**
   * Delete a GridDocument.
   *
   * @param gDocUId The UID of the GridDocument to delete.
   */
  public void deleteGridDocument(Long gDocUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.deleteGridDocument] Enter");

    try
    {
      GridDocument delGdoc =
        (GridDocument)getGridDocumentEntityHandler().getEntityByKey(gDocUId);
      getGridDocumentEntityHandler().remove(gDocUId);

      try
      {
        boolean fileExists = FileUtil.exist(IDocumentPathConfig.PATH_GDOC,delGdoc.getFolder()+File.separator,delGdoc.getGdocFilename());
        if(fileExists)
        {
          FileUtil.delete(IDocumentPathConfig.PATH_GDOC,
                          delGdoc.getFolder()+File.separator,
                          delGdoc.getGdocFilename());
        }
        else Logger.debug("[DocumentManagerBean.deleteGridDocument] GridDocument file does not exist, GridDocumentFile="+delGdoc.getFolder()+File.separator+delGdoc.getGdocFilename());
      }
      catch (Exception delGdocEx)
      {
        Logger.warn("[DocumentManagerBean.deleteGridDocument] "+
          "Error deleting GridDocument with uid : "+gDocUId, delGdocEx);
      }

      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        GridDocument.U_DOC_FILENAME,
        filter.getEqualOperator(),
        delGdoc.getUdocFilename(),
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.FOLDER,
        filter.getEqualOperator(),
        delGdoc.getFolder(),
        false
      );
      Collection list = getGridDocumentEntityHandler().findByFilter(filter);
      if (list.isEmpty())
      {
        try
        {
          boolean fileExists = FileUtil.exist(IDocumentPathConfig.PATH_UDOC,delGdoc.getFolder()+File.separator,delGdoc.getUdocFilename());
          if(fileExists)
          {
            FileUtil.delete(IDocumentPathConfig.PATH_UDOC,
                          delGdoc.getFolder()+File.separator,
                          delGdoc.getUdocFilename());
          }
          else Logger.debug("[DocumentManagerBean.deleteGridDocument] uDoc file does not exist, uDocFile="+delGdoc.getFolder()+File.separator+delGdoc.getUdocFilename());
        }
        catch (Exception delUdocEx)
        {
          Logger.warn("[DocumentManagerBean.deleteGridDocument] "+
            "Error deleting uDoc : "+delGdoc.getUdocFilename(), delUdocEx);
        }
      }
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteGridDocument] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteGridDocument] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.deleteGridDocument] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.deleteGridDocument(gDocUId) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.deleteGridDocument] Exit");
  }

  /**
   * Find a GridDocument using the GridDocument UID.
   *
   * @param gDocUId The UID of the GridDocument to find.
   * @return The GridDocument found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridDocument findGridDocument(Long gDocUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findGridDocument] UID: "+gDocUId);

    GridDocument gDoc = null;

    try
    {
      gDoc =
        (GridDocument)getGridDocumentEntityHandler().getEntityByKey(gDocUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocument] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocument] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocument] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findGridDocument(gDocUId) Error ",
        ex);
    }

    return gDoc;
  }

  /**
   * Find a number of GridDocument that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridDocument found, or empty collection if none
   * exists.
   */
  public Collection findGridDocuments(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findGridDocuments] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gDocs = null;
    try
    {
      gDocs = getGridDocumentEntityHandler().getEntityByFilter(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findGridDocuments(filter) Error ",
        ex);
    }

    return gDocs;
  }

  /**
   * Find a number of GridDocument that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridDocument found, or empty collection if
   * none exists.
   */
  public Collection findGridDocumentsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findGridDocuments] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gDocsKeys = null;
    try
    {
      gDocsKeys = getGridDocumentEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findGridDocuments(filter) Error ",
        ex);
    }

    return gDocsKeys;
  }

  /**
   * Find a GridDocument in a specific folder.
   *
   * @param folder The folder to look for the GridDocument
   * @param gdocID GridDoc ID of the GridDocument to look for.
   * @return The GridDocument found, or <b>null</b> if none exists.
   */
  public GridDocument findGridDocument(String folder, Long gdocID)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findGridDocument] folder:"+folder +
     ", GdocID:"+gdocID);

    GridDocument gDoc = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridDocument.FOLDER, filter.getEqualOperator(),
        folder, false);
      filter.addSingleFilter(filter.getAndConnector(), GridDocument.G_DOC_ID,
        filter.getEqualOperator(), gdocID, false);
      Collection gDocs = getGridDocumentEntityHandler().getEntityByFilter(filter);

      if (!gDocs.isEmpty())
        gDoc = (GridDocument)gDocs.toArray()[0];
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocument] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocument] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findGridDocuments(folder,gdocId) Error ",
        ex);
    }

    return gDoc;
  }


  // ********************* Attachment ****************************

  /**
   * Create a new Attachment.
   *
   * @param attachment The Attachment entity.
   */
  public Long createAttachment(Attachment attachment)
    throws CreateEntityException, SystemException//, DuplicateEntityException
  {
    Logger.log("[DocumentManagerBean.createAttachment] Enter");

    try
    {
      IAttachmentLocalObj obj =
        (IAttachmentLocalObj)getAttachmentEntityHandler().create(attachment);

      Logger.log("[DocumentManagerBean.createAttachment] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[DocumentManagerBean.createAttachment] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
//    catch (DuplicateEntityException ex)
//    {
//      Logger.err("[DocumentManagerBean.createAttachment] BL Exception", ex);
//      throw ex;
//    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.createAttachment] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.createAttachment(Attachment) Error ",
        ex);
    }

  }

  /**
   * Update a Attachment
   *
   * @param attachment The Attachment entity with changes.
   */
  public void updateAttachment(Attachment attachment)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.updateAttachment] Enter");

    try
    {
      getAttachmentEntityHandler().update(attachment);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.updateAttachment] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.updateAttachment] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.updateAttachment(Attachment) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.updateAttachment] Exit");
  }

  /**
   * Delete a Attachment.
   *
   * @param attachmentUid The UID of the Attachment to delete.
   */
  public void deleteAttachment(Long attachmentUid)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.deleteAttachment] Enter");

    try
    {
      Attachment delAttachment =
        (Attachment)getAttachmentEntityHandler().getEntityByKey(attachmentUid);
      getAttachmentEntityHandler().remove(attachmentUid);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteAttachment] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[DocumentManagerBean.deleteAttachment] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.deleteAttachment] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.deleteAttachment(attachmentUid) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.deleteGridDocument] Exit");
  }

  /**
   * Find a Attachment using the Attachment UID.
   *
   * @param attachmentUid The UID of the Attachment to find.
   * @return The Attachment found, or <B>null</B> if none exists with that
   * UID.
   */
  public Attachment findAttachment(Long attachmentUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findAttachment] UID: "+attachmentUid);

    Attachment attachment = null;

    try
    {
      attachment =
        (Attachment)getAttachmentEntityHandler().getEntityByKeyForReadOnly(attachmentUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachment] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachment] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachment] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAttachment(attachmentUid) Error ",
        ex);
    }

    return attachment;
  }

  /**
   * Find a number of Attachment that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Attachment found, or empty collection if none
   * exists.
   */
  public Collection findAttachments(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findAttachments] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection attachments = null;
    try
    {
      attachments = getAttachmentEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAttachments(filter) Error ",
        ex);
    }

    return attachments;
  }

  /**
   * Find the Attachments linked to this GridDocument.
   *
   * @param gdoc The GridDocument.
   * @return a Collection of Attachment found, or empty collection if none
   * exists.
   */
  public Collection findAttachments(GridDocument gdoc)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findAttachments] gdoc uid: "+gdoc.getUId());

    ArrayList attachments = new ArrayList();
    try
    {
      List attachmentUids = gdoc.getAttachments();
      for (Iterator i = attachmentUids.iterator(); i.hasNext(); )
      {
        Long attachmentUid = new Long(i.next().toString());
        Attachment attachment =
          (Attachment)getAttachmentEntityHandler().getEntityByKeyForReadOnly(attachmentUid);
        if (attachment != null)
        {
          attachments.add(attachment);
        }
      }
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findAttachments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAttachments(filter) Error ",
        ex);
    }

    return attachments;
  }

  /**
   * Find a number of Attachments that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridDocument found, or empty collection if
   * none exists.
   */
  public Collection findAttachmentsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findGridDocuments] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gDocsKeys = null;
    try
    {
      gDocsKeys = getGridDocumentEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.findGridDocuments] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findGridDocuments(filter) Error ",
        ex);
    }

    return gDocsKeys;
  }

  // ********************* Document Flow ****************************

  /**
   * View the user document represented by the GridDocument
   *
   * @param gdocID the gdocID of the GridDocument whose uDoc is to
   * be viewed.
   */
  public void viewDoc(Long gdocUid)
    throws Exception
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "viewDoc";
    Object[] params     = new Object[] {gdocUid};

    try
    {
    	logger.logEntry(methodName, params);
	    GridDocument gdoc =
	      (GridDocument)getGridDocumentEntityHandler().getEntityByKey(gdocUid);
	    gdoc.setDateTimeView(new Date(TimeUtil.localToUtc()));
	    gdoc.setViewed(Boolean.TRUE);
	    getGridDocumentEntityHandler().update(gdoc);
    }
    catch (EntityModifiedException ex)
    {
    	logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException("Unable to update state of document: "+ex.getMessage());
    }
    catch (ApplicationException ex)
    {
    	logger.logFinderError(methodName, params, ex);
    	throw new FindEntityException("Unable to retrieve document for viewing: "+ex.getMessage());
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to viewDoc", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
    	logger.logWarn(methodName, params, "Unable to viewDoc", t);
    	throw new SystemException("Unexpected problems encountered in viewDoc", t);
    }
    finally
    {
    	logger.logExit(methodName, params);
    }
  }

  /**
   * Edit the user document represented by the GridDocument
   *
   * @param gdocID the gdocID of the GridDocument whose uDoc is to
   *                  be edited.
   * @param userID the user ID of the user editing the document
   */
  public void editDoc(Long gdocUid, String userId)
    throws Exception
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "editDoc";
    Object[] params     = new Object[] {gdocUid, userId};

    try
    {
    	logger.logEntry(methodName, params);
      GridDocument gdoc =
        (GridDocument)getGridDocumentEntityHandler().getEntityByKey(gdocUid);

      int uDocVersion = gdoc.getUdocVersion().intValue();
      uDocVersion++;
      gdoc.setUdocVersion(new Integer(uDocVersion));

      String udocFilename = gdoc.getUdocFilename();
      FileUtil.delete(IDocumentPathConfig.PATH_UDOC,
                      gdoc.getFolder()+File.separator,
                      udocFilename);
      FileUtil.move(IDocumentPathConfig.PATH_TEMP,
                    userId+File.separator+"in"+File.separator,
                    IDocumentPathConfig.PATH_UDOC,
                    gdoc.getFolder()+File.separator,
                    udocFilename);

      getGridDocumentEntityHandler().update(gdoc);
    }
    catch (EntityModifiedException ex)
    {
    	logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException("Unable to update state of document: "+ex.getMessage());
    }
    catch (FileAccessException ex)
    {
    	logger.logWarn(methodName, params, "Error fetching document for editing", ex);
    	throw new ApplicationException("Error fetching document for editing", ex);
    }
    catch (ApplicationException ex)
    {
    	logger.logFinderError(methodName, params, ex);
    	throw new FindEntityException("Unable to retrieve document for viewing: "+ex.getMessage());
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to editDoc", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
    	logger.logWarn(methodName, params, "Unable to editDoc", t);
    	throw new SystemException("Unexpected problems encountered in editDoc", t);
    }
    finally
    {
    	logger.logExit(methodName, params);
    }
  }

  /**
   * Import the list of GridDocuments
   */
//  public void importDoc(Collection gdocs)
//    throws Throwable
//  {
//    Logger.debug("[DocumentManagerBean.importDoc] Start");
//    for(Iterator i = gdocs.iterator(); i.hasNext(); )
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      importDoc(gdoc);
//      gdoc = importFolder.doEnter(gdoc);
//      importFolder.doActivityBegin(gdoc);
//    }
//    Logger.debug("[DocumentManagerBean.importDoc] End");
//  }

  public void importDoc(GridDocument gdoc)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.importDoc] Start importing single gdoc");
    ImportFolder importFolder = new ImportFolder();
    boolean isRequiredDoc = isRequireIncludeImportedDoc(gdoc);
    try
    {
      Throwable importEx = null;
      boolean isEnterImportSuccess = true;
      try
      {
        gdoc = importFolder.doEnter(gdoc);
      }
      catch(Throwable th)
      {
    	importEx = th;
        isEnterImportSuccess = false;
        throw th;
      }
      finally
      {
        Logger.log("[DocumentManagerBean.importDoc] isRequiredDoc "+isRequiredDoc+" udocFile is "+gdoc.getUdocFilename());
        //TWX 31102006 send out the status of the import activity
        sendDocumentFlowNotification(gdoc, EDocumentFlowType.DOCUMENT_IMPORT, isEnterImportSuccess, (importEx != null ? importEx.getMessage() : ""), new Date(), isRequiredDoc, false, false, true, "", importEx);
      }
      
      importFolder.doActivityBegin(gdoc);
    }
    catch (Throwable t)
    {
      Logger.warn("[DocumentManagerBean.importDoc] Error import", t);
      throw new ApplicationException("Unable to import document", t);
    }
  }

  /**
   * Manual send of a list of GridDocuments
   */
//  public void sendDoc(Collection gdocs)
//    throws Throwable
//  {
//    Logger.debug("[DocumentManagerBean.sendDoc] Start");
//    OutboundFolder outboundFolder = new OutboundFolder();
//    for(Iterator i = gdocs.iterator(); i.hasNext(); )
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      exitFromCurrentFolder(gdoc);
//      gdoc = outboundFolder.doEnter(gdoc);
//      outboundFolder.doActivityBegin(gdoc);
//    }
//    Logger.debug("[DocumentManagerBean.sendDoc] End");
//  }

 /**
   * Manual send a GridDocument
   *
   * @param gdoc the GridDocument to send
   */
  public void sendDoc(GridDocument gdoc)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.sendDoc] Start");
    OutboundFolder outboundFolder = new OutboundFolder();
    boolean isSuccess = true;
    Throwable sendDocEx = null;
    
    try
    {
    	
      try
      {
        exitFromCurrentFolder(gdoc);
        gdoc = outboundFolder.doEnter(gdoc);
        addTracingID(gdoc);
      }
      catch(Throwable th)
      {
        isSuccess = false;
        
        if(th instanceof InvocationTargetException)
        {
          if(th.getCause() != null)
          {
        	  sendDocEx = th.getCause();
          }
        }
        else
        {
        	sendDocEx = th;
        }
        
        throw th;
      }
      finally
      {
        sendDocumentFlowNotification(gdoc, EDocumentFlowType.OUTBOUND_PROCESSING_START, isSuccess, (sendDocEx != null ? sendDocEx.getMessage(): ""), new Date(), false, false, false, false, "", sendDocEx);
      }
    	outboundFolder.doActivityBegin(gdoc);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.sendDoc] Error send", t);
    	throw new ApplicationException("Unable to send document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.sendDoc] End");
    }
  }

 /**
   * Manual export a GridDocument
   *
   * @param gdoc the GridDocument to export
   */
  public void exportDoc(GridDocument gdoc)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exportDoc] Start");
    ExportFolder exportFolder = new ExportFolder();
    boolean isSuccess = true;
    Throwable exportDocEx = null;
    
    try
    {
      try
      {
        exitFromCurrentFolder(gdoc);
        gdoc = exportFolder.doEnter(gdoc);
        addTracingID(gdoc);
      }
      catch(Throwable th)
      {
        isSuccess = false;
        if(th instanceof InvocationTargetException)
        {
          if(th.getCause() != null)
          {
        	  exportDocEx = th.getCause();
          }
        }
        else
        {
        	exportDocEx = th;
        }
        throw th;
      }
      finally
      {
        sendDocumentFlowNotification(gdoc, EDocumentFlowType.EXPORT_PROCESSING_START, isSuccess, (exportDocEx != null? exportDocEx.getMessage(): ""), new Date(), false, false, false, false, "", exportDocEx);
      }
    	exportFolder.doActivityBegin(gdoc);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exportDoc] Error export", t);
    	throw new ApplicationException("Unable to export document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exportDoc] End");
    }
  }

  /**@todo to be deleted
  public void receiveDoc(String eventID,
                         String[] dataReceived,
                         GridDocument gdoc,
                         File udocFile)
                         throws Throwable
  {
    Logger.debug("[DocumentManagerBean.receiveDoc] Start");

    String udocFilename = udocFile.getName();
    String tempUdocFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

    gdoc.setUdocFilename(udocFilename+":"+tempUdocFilename);
    gdoc.setUdocFileSize(new Long(Math.round(udocFile.length()/1000)));

    InboundFolder inboundFolder = new InboundFolder();
    gdoc = inboundFolder.doEnter(gdoc);
    inboundFolder.doActivityBegin(gdoc);
    Logger.debug("[DocumentManagerBean.receiveDoc] End");
  }
  */

  /**
   * This method will process normal non-RosettaNet documents
   */
  public GridDocument processNormalDoc(String[] header,
                                       String[] dataReceived,
                                       File[] filesReceived,
                                       Hashtable additionalHeaders)
                                       throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.processNormalDoc] Start");
    try
    {
    return ReceiveDocumentHelper.receiveNormalDoc(header,
                                                  dataReceived,
                                                  filesReceived,
                                                  additionalHeaders);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.processNormalDoc] Error", t);
    	throw new ApplicationException("Unable to process document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.processNormalDoc] Exit");
    }
    //receiveDoc(gdoc);
    //Logger.debug("[DocumentManagerBean.processNormalDoc] End");
  }

  /**
   * This method will process a RosettaNet document
   */
  public GridDocument processRnifDoc(GridDocument gdoc,
                                     String[] header,
                                     String[] dataReceived,
                                     File[] filesReceived)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.processRnifDoc] Start");
    try
    {
    	return ReceiveDocumentHelper.processRnifDoc(gdoc, header, dataReceived, filesReceived);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.processRnifDoc] Error", t);
    	throw new ApplicationException("Unable to process RN document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.processRnifDoc] Exit");
    }
  }

  /**
   * This method will process in RosettaNet document, it can either be a
   * document that failed validation or a retry send document. The processing
   * will stop at the inbound folder and it will not trigger any partner
   * function.
   *
   * @param gdoc the GridDocument representing the RosettaNet document
   * @param isValidationFailed the flag indicating whether this is a validation
   *                           failed document
   * @param isRetry the flag indicating whether this is a retry send document
   */
  public void receiveFailedRnifDoc(GridDocument gdoc,
                                   boolean isValidationFailed,
                                   boolean isRetry)
                                   throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.receiveFailedRnifDoc] Start");
    InboundFolder inboundFolder = new InboundFolder();
    try
    {
    	gdoc = inboundFolder.doEnter(gdoc);
      
      DocumentTransactionHandler.triggerDocumentTransaction(gdoc, isRetry, false);
      
// commented by AMH on 23/01/2003
//    gdoc = inboundFolder.doActivityEnd(gdoc);
//    inboundFolder.doExit(gdoc);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.receiveFailedRnifDoc] Error", t);
    	throw new ApplicationException("Unable to receive Failed RN document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.receiveFailedRnifDoc] End");
    }
  }

  public void receiveDoc(GridDocument gdoc)
                         throws Exception
  {
    Logger.debug("[DocumentManagerBean.receiveDoc] Start");
    InboundFolder inboundFolder = new InboundFolder();
    try
    {
    	gdoc = inboundFolder.doEnter(gdoc);
      
      DocumentTransactionHandler.triggerDocumentTransaction(gdoc, false, false);
      
    	inboundFolder.doActivityBegin(gdoc);
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.receiveDoc] Error", t);
    	throw new Exception("Unable to receive document", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.receiveDoc] End");
    }
  }

  /**
   * Handle the SEND_GRIDDOC_ACK message.
   *
   * @param trxID Transaction ID of the acknowledgement. This should tally with
   *              the Transaction ID in SEND_GRIDDOC message. trxID will be
   *              used as the GridDoc ID of the sent GridDocument.
   * @param dataReceived Data in the message.
   *                     Element 0 - Whether the send was successful (true/false).
   *                     Element 1 - Recipient GridDoc ID if successful, otherwise null.
   *                     Element 2 - null.
   */
  public void handleSendDocAck(String trxID,
                               String[] dataReceived)
                               throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleSendDocAck";
    Object[] params     = new Object[] {trxID};

    try
    {
      logger.logEntry(methodName, params);

      Long gdocId = new Long(trxID);

      GridDocument gdoc = findGridDocument(GridDocument.FOLDER_OUTBOUND, gdocId);
      if (gdoc != null)
      {
        gdoc = getCompleteGridDocument(gdoc);

        processDocSent(gdoc);

        boolean completeNow = isCompleteTransNow(gdoc);
        if (dataReceived[1] == null)
          processRejectedDoc(gdoc, completeNow);
        else
          processAcceptedDoc(gdoc, dataReceived[1], null, true, completeNow);
        /*
        gdoc.setDateTimeSendEnd(new Date(TimeUtil.localToUtc()));
        gdoc.setSent(Boolean.TRUE);
        gdoc.setLocalPending(Boolean.FALSE);

        gdoc.setRecAckProcessed(Boolean.TRUE);

        if (dataReceived[1] != null)
        {
          gdoc.setRecipientGdocId(new Long(dataReceived[1]));
          gdoc.setDateTimeTransComplete(new Date(TimeUtil.localToUtc()));
        }
        else
        {
          //@todo isRejected flag
        }
        */
        updateGridDocument(gdoc);

        /*
        // update IMPORT document IS_SENT to True
        GridDocument ipGdoc = findGridDocument(GridDocument.FOLDER_IMPORT, gdoc.getRefGdocId());
        if (ipGdoc != null)
        {
          ipGdoc.setSent(Boolean.TRUE);
          updateGridDocument(ipGdoc);
        }
        */
      }
      else
      {
        logger.logMessage(methodName, params, "Unable to find Outbound Griddoc:"
          +gdocId);
      }
    }
    catch (ApplicationException ex)
    {
    	logger.logWarn(methodName, params, "Unable to send document ack", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to send document ack", ex);
    	throw ex;
    }
    catch (Throwable ex)
    {
    	logger.logWarn(methodName, params, "Unable to send document ack", ex);
    	throw new SystemException("Unable to send acknowledgement for received document", ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the UPLOAD_GRIDDOC_ACK message.
   *
   * @param trxID Transaction ID of the acknowledgement. This should tally with
   *              the Transaction ID in UPLOAD_GRIDDOC message. trxID will be
   *              used as the GridDoc ID of the uploaded GridDocument.
   * @param dataReceived Data in the message.
   *                     Element 0 - Whether the upload was successful (true/false).
   *                     Element 1 - null.
   *                     Element 2 - null.
   */
  public void handleUploadDocAck(String trxID)
                               throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleUploadDocAck";
    Object[] params     = new Object[] {trxID};

    try
    {
      logger.logEntry(methodName, params);

      Long gdocId = new Long(trxID);

      GridDocument gdoc = findGridDocument(GridDocument.FOLDER_OUTBOUND, gdocId);
      if (gdoc != null)
      {
        /*
        gdoc = getCompleteGridDocument(gdoc);

        gdoc.setDateTimeSendEnd(TimeUtil.localToUtcTimestamp());
        gdoc.setSent(Boolean.TRUE);
        gdoc.setLocalPending(Boolean.FALSE);

        updateGridDocument(gdoc);

        // update IMPORT document IS_SENT to True
        GridDocument ipGdoc = findGridDocument(GridDocument.FOLDER_IMPORT, gdoc.getRefGdocId());
        if (ipGdoc != null)
        {
          ipGdoc.setSent(Boolean.TRUE);
          updateGridDocument(ipGdoc);
        }
        */
        handleDocSent(gdoc);
      }
      else
      {
        logger.logMessage(methodName, params, "Unable to find Outbound Griddoc:"
          +gdocId);
      }
    }
    catch (ApplicationException ex)
    {
    	logger.logWarn(methodName, params, "Unable to upload document ack", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to upload document ack", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
    	logger.logWarn(methodName, params, "Unable to upload document ack", t);
    	throw new SystemException("Unable to upload document acknowledgement", t);    	
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }


  /**
   * Handle the TRANS_COMPLETED message.
   *
   * @param trxID Transaction ID of the message.
   * @param dataReceived Data in the message.
   *                     Element 0 - Whether the document was downloaded and processed
   *                                 successfully by partner (true/false).
   *                     Element 1 - Recipient GridDoc ID if successful, otherwise null.
   *                     Element 2 - Download End timestamp if successful, otherwise null.
   */
  public void handleDocTransCompleted(String trxID,
                                      String[] dataReceived)
                                      throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleDocTransCompleted";
    Object[] params     = new Object[] {trxID, dataReceived};

    boolean success = false;
    try
    {
      logger.logEntry(methodName, params);

      Long gdocId = new Long(dataReceived[0]);

      GridDocument gdoc = findGridDocument(GridDocument.FOLDER_OUTBOUND, gdocId);
      if (gdoc != null)
      {
        /*
        gdoc = getCompleteGridDocument(gdoc);

        gdoc.setRecAckProcessed(Boolean.TRUE);

        if (dataReceived[1] != null)
        {
          gdoc.setRecipientGdocId(new Long(dataReceived[1]));
          gdoc.setDateTimeTransComplete(Timestamp.valueOf(dataReceived[2]));
        }
        else
        {
          ///@todo isRejected flag
        }

        updateGridDocument(gdoc);
        */

        boolean completeNow = isCompleteTransNow(gdoc);
        if (dataReceived[1] == null)
          handleDocRejected(gdoc, completeNow);
        else
          handleDocAccepted(gdoc, dataReceived[1], dataReceived[2], true, completeNow);
        success = true;
      }
      else
      {
        logger.logMessage(methodName, params, "Unable to find Outbound Griddoc:"
          +gdocId);
      }
    }
    catch (ApplicationException ex)
    {
    	logger.logWarn(methodName, params, "Unable to complete document trans", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to complete document trans", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
    	logger.logWarn(methodName, params, "Unable to complete document trans", t);
    	throw new SystemException("Unable to complete document transaction", t);    	
    }
    finally
    {
      logger.logMessage(methodName, params,"Ended with success="+success);
      try
      {
      	doSendTransCompletedAck(trxID, new Boolean(success));
      }
      catch (Exception ex)
      {
      	logger.logWarn(methodName, params, "Unable to send ack for complete trans", ex);
      	throw new SystemException("Unable to send ack for complete trans", ex);
      }
      logger.logExit(methodName, params);
    }
  }

  private boolean isCompleteTransNow(GridDocument gdoc)
  {
    boolean now = true;
    if (gdoc.getRnProfileUid() != null) // is RN doc
    {
      List docTypes = ConfigurationManager.getInstance().getConfig(IDocumentConfig.CONFIG_NAME).getList(
                        IDocumentConfig.P2P_ACK_RNDOCTYPES, ",");
      now = (docTypes.contains(gdoc.getUdocDocType()));
    }
    return now;
  }

  /**
   * Handle the event when an outbound document is processed and rejected by
   * the recipient partner. This method persists the changes to the document.
   *
   * @param gdoc The recipient partner.
   * @param completeTransNow Whether to update the Trans to completed now. If <b>true</b>,
   * this means that the RecAckProcessed will be updated to <b>true</b>.
   */
  public void handleDocRejected(GridDocument gdoc, boolean completeTransNow) throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleDocRejected";
    Object[] params     = new Object[] {gdoc, Boolean.valueOf(completeTransNow)};
    
    boolean isHandleSuccess = true;
    Throwable handleDocEx = null;
    try
    {
      logger.logEntry(methodName, params);
      
      String receiptAuditFilename = gdoc.getReceiptAuditFileName();
      
      gdoc = getCompleteGridDocument(gdoc);
      gdoc.setReceiptAuditFileName(receiptAuditFilename);
      
      processRejectedDoc(gdoc, completeTransNow);
      updateGridDocument(gdoc);
      
    }
    catch (ApplicationException ex)
    {
      isHandleSuccess = false;
      handleDocEx = ex;
      
    	logger.logWarn(methodName, params, "Unable to handle rejected doc", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
      isHandleSuccess = false;
      handleDocEx = ex;
      
    	logger.logWarn(methodName, params, "Unable to handle rejected doc", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
      isHandleSuccess = false;
      handleDocEx = t;
      
    	logger.logWarn(methodName, params, "Unable to handle rejected doc", t);
    	throw new SystemException("Unable to handle rejected doc", t);    	
    }
    finally
    {    
      // TWX 08112006 Indicate the receive of the ACK for the OB doc we sent out
      sendDocumentFlowNotification(gdoc, EDocumentFlowType.DOCUMENT_ACKNOWLEDGED, isHandleSuccess, (handleDocEx != null ? handleDocEx.getMessage(): ""), new Date(), false, true, false, false, "", handleDocEx);
      logger.logExit(methodName, params);
    }

  }

  /**
   * Handle the event when an outbound document has been processed and accepted
   * by the recipient partner. This method persists the changes to the document.
   *
   * @param gdoc The outbound GridDocument.
   * @param recipientGdocId The GridDocId of the corresponding Inbound document
   * at the recipient GridNode. <b>Null</b> if the document recipient is not a GridNode.
   * @param transCompleteTS Timestamp string for the time when transaction is completed.
   * <b>null</b> if not applicable, in which case, the current time will be used.
   * @param raiseAlert <b>true</b> to raise alert if the document is not
   * rejected, i.e. transaction has completed. <b>false</false> otherwise.
   * @param completeTransNow Whether to update the Trans to completed now. If <b>true</b>,
   * this means that the RecAckProcessed and the DateTimeTransCompleted will be updated.
   */
  public void handleDocAccepted(GridDocument gdoc,
                                String recipientGdocId,
                                String transCompleteTS,
                                boolean raiseAlert,
                                boolean completeTransNow)
                                throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleDocAccepted";
    Object[] params     = new Object[] {gdoc,
                                        recipientGdocId,
                                        transCompleteTS,
                                        new Boolean(raiseAlert),
                                        Boolean.valueOf(completeTransNow)};
    
    boolean isHandleSuccess = true;
    Throwable handleDocEx = null;
    try
    {
      logger.logEntry(methodName, params);
      
      //TWX 08112006
      String transStatus = gdoc.getDocTransStatus();
      String receiptAuditFilename = gdoc.getReceiptAuditFileName();
      
      gdoc = getCompleteGridDocument(gdoc);
      
//    TWX08112006
      gdoc.setDocTransStatus(transStatus); 
      gdoc.setReceiptAuditFileName(receiptAuditFilename);
      
      Logger.log("[DocManager.handleDocAccepted] docTransStatus "+gdoc.getDocTransStatus()+". ReceiptAuditFilename "+gdoc.getReceiptAuditFileName());
      
      processAcceptedDoc(gdoc, recipientGdocId, transCompleteTS, raiseAlert, completeTransNow);
      updateGridDocument(gdoc);
      
    }
    catch (ApplicationException ex)
    {
      isHandleSuccess = false;
      handleDocEx = ex;
    	logger.logWarn(methodName, params, "Unable to handle accepted doc", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
      isHandleSuccess = false;
      handleDocEx = ex;
    	logger.logWarn(methodName, params, "Unable to handle accepted doc", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
      isHandleSuccess = false;
      handleDocEx = t;
    	logger.logWarn(methodName, params, "Unable to handle accepted doc", t);
    	throw new SystemException("Unable to handle accepted doc", t);    	
    }
    finally
    {
      //TWX 08112006 Indicate the receive of the ACK for the OB doc we sent out
      sendDocumentFlowNotification(gdoc, EDocumentFlowType.DOCUMENT_ACKNOWLEDGED, isHandleSuccess, (handleDocEx != null ? handleDocEx.getMessage(): ""), new Date(), false, true, false, false, "", handleDocEx);
      logger.logExit(methodName, params);
    }
  }

  /**
   * Update an outbound document for transaction completed. This method
   * does not persist the changes to the document.
   *
   * @param gdoc The outbound GridDocument.
   * @param recipientGdocId The GridDocId of the corresponding Inbound document
   * at the recipient GridNode. <b>Null</b> if the document was rejected or
   * the recipient is not a GridNode.
   * @param rejectIfNoRGdocId If <b>true</b>, the document is treated as rejected when
   * <code>recipientGdocId</code> is <b>null</null>. Otherwise, the document is
   * treated as sent to a non-GridNode recipient.
   * @param transCompleteTS Timestamp string for the time when transaction is completed.
   * <b>null</b> if not applicable, in which case, the current time will be used.
   * @param completeTransNow Whether to update the Trans to completed now. If <b>true</b>,
   * this means that the RecAckProcessed and the DateTimeTransCompleted will be updated.
   */
  private void processAcceptedDoc(GridDocument gdoc,
                                 String recipientGdocId,
                                 String transCompleteTS,
                                 boolean raiseAlert,
                                 boolean completeTransNow)
                                 throws Throwable
  {
    if (recipientGdocId != null)
      gdoc.setRecipientGdocId(new Long(recipientGdocId));

    if (completeTransNow)
    {
      gdoc.setRecAckProcessed(Boolean.TRUE);

      gdoc.setDateTimeTransComplete(
        (transCompleteTS == null)      ?
        TimeUtil.localToUtcTimestamp() :
        Timestamp.valueOf(transCompleteTS));

      if (raiseAlert)
        AlertDelegate.raiseDocReceivedByPartnerAlert(gdoc);

      DocumentStatusBroadcaster.documentTransCompleted(gdoc);
    }
  }

  /**
   * Update an outbound document for rejected transaction.
   * This method does not persist the changes to the document.
   *
   * @param gdoc The outbound GridDocument.
   * @param recAckProcessed Whether the ACK is being processed for the sent doc. This
   * is based on the business level ACK. E.g. for RN business docs, this will be <b>true</b>
   * only when the RosettaNet Acknowledgement is received.
   */
  private void processRejectedDoc(GridDocument gdoc, boolean recAckProcessed) throws Throwable
  {
    Logger.log("[DocumentManagerBean.processRejectedDoc] Gdoc is rejected: "+
      gdoc.getGdocId());

    if (recAckProcessed)
      gdoc.setRecAckProcessed(Boolean.TRUE);
    gdoc.setRejected(Boolean.TRUE);

    DocumentStatusBroadcaster.documentTransRejected(gdoc);
  }

  /**
   * Handle the event when an outbound document has been sent out of the system.
   *
   * @param gdoc The outbound GridDocument.
   */
  public void handleDocSent(GridDocument gdoc)
    throws ApplicationException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "handleDocSent";
    Object[] params     = new Object[] {gdoc};

    try
    {
      logger.logEntry(methodName, params);

      gdoc = getCompleteGridDocument(gdoc);
      processDocSent(gdoc);
      updateGridDocument(gdoc);
    }
    catch (ApplicationException ex)
    {
    	logger.logWarn(methodName, params, "Unable to handle sent doc", ex);
    	throw ex;
    }
    catch (SystemException ex)
    {
    	logger.logWarn(methodName, params, "Unable to handle sent doc", ex);
    	throw ex;
    }
    catch (Throwable t)
    {
    	logger.logWarn(methodName, params, "Unable to handle sent doc", t);
    	throw new SystemException("Unable to handle sent doc", t);    	
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * Update an outbound document when it is sent. The reference Import document
   * is also updated as sent.
   *
   * @param gdoc The Outbound GridDocument.
   */
  private void processDocSent(GridDocument gdoc) throws Throwable
  {
    gdoc.setDateTimeSendEnd(TimeUtil.localToUtcTimestamp());
    gdoc.setSent(Boolean.TRUE);
    gdoc.setLocalPending(Boolean.FALSE);

    DocumentStatusBroadcaster.documentSendEnd(gdoc);

    // update IMPORT document IS_SENT to True
    GridDocument ipGdoc = findGridDocument(GridDocument.FOLDER_IMPORT, gdoc.getRefGdocId());
    if (ipGdoc != null)
    {
      ipGdoc.setSent(Boolean.TRUE);
      updateGridDocument(ipGdoc);
    }
  }

  /**
   * Get the GridDocument loaded with all fields.
   *
   * @param gdoc The GridDocument that may not contain all fields. At the
   * minium, this must contain the correct UID, and GdocFilename. Note
   * that the Version of <code>gdoc</code> will be retained.
   *
   * @return The GridDocument that contain all fields.
   */
  public GridDocument getCompleteGridDocument(GridDocument gdoc)
    throws Exception
  {
    long uid = gdoc.getUId();
    double version = gdoc.getVersion();
    String gdocFilename = gdoc.getGdocFilename();
    /*
    String gdocFullPath = FileUtil.getDomain()+File.separator+
                          FileUtil.getPath(IDocumentPathConfig.PATH_GDOC)+
                          File.separator+gdoc.getFolder()+
                          File.separator+gdocFilename;
    */
    //NSL20051114 Get File path using FileUtil only
    String gdocFullPath = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gdoc.getFolder()+"/", gdocFilename).getCanonicalPath();
    gdoc = (GridDocument)gdoc.deserialize(gdocFullPath);
    gdoc.setUId(uid);
    gdoc.setVersion(version);

    return gdoc;
  }

  /**
   * Send the ACK for TransactionCompleted event to GridMaster.
   *
   * @param trxId The Transaction Id of the TransactionCompleted event.
   * @param success Whether the event was successfully processed.
   */
  private void doSendTransCompletedAck(
    String trxId, Boolean success)
    throws Exception
  {
    Logger.debug("[DocumentManagerBean.doSendTransCompletedAck] Start");

    String[] dataToSend = {
                            success.toString(),
                          };

    ChannelDelegate.sendToGridMaster(
      dataToSend,
      IDocumentEvents.TRANS_COMPLETED_ACK,
      trxId);

    Logger.debug("[DocumentManagerBean.doSendTransCompletedAck] End");
  }

  public Collection exitToImport(Collection gdocs)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exitToImport] Start");
    GridDocument gdoc = null;
    try
    {
      ArrayList returnList = new ArrayList();
    	ImportFolder importFolder = new ImportFolder();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		gdoc = (GridDocument)i.next();
    		
        boolean isSuccess = true;
        Throwable importEx = null;
        try
        {
          gdoc = FileHelper.duplicateTempUdocFile(gdoc);
          gdoc = importFolder.doEnter(gdoc);
        }
        catch(Throwable th)
        {
          isSuccess = false;
          importEx = th;
          throw th;
        }
        finally
        {
//        TWX 31102006
          sendDocumentFlowNotification(gdoc, EDocumentFlowType.DOCUMENT_IMPORT, isSuccess, (importEx != null? importEx.getMessage(): ""), new Date(), false, false, false, false, "", importEx);
        }
    		returnList.add(gdoc);
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitToImport] Error", t);
    	throw new ApplicationException("Failure to push document to Import folder", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exitToImport] End");
    }
  }

  public Collection exitToOutbound(Collection gdocs)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exitToOutbound] Start");
    GridDocument gdoc = null;
    try
    {
    	ArrayList returnList = new ArrayList();
    	OutboundFolder outboundFolder = new OutboundFolder();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		gdoc = (GridDocument)i.next();
    		boolean isSuccess = true;
    		
        Throwable exitOBEx = null;
        
        try
        {
          exitFromCurrentFolder(gdoc);
          gdoc = FileHelper.duplicateTempUdocFile(gdoc);
          gdoc = outboundFolder.doEnter(gdoc);
        }
        catch(Throwable th)
        {
          isSuccess = false;
          
          if(th instanceof InvocationTargetException)
          {
            if(th.getCause() != null)
            {
            	exitOBEx = th.getCause();
            }
          }
          else
          {
        	  exitOBEx = th;
          }
          
          throw th;
        }
        finally
        {
          //TWX 31102006
          sendDocumentFlowNotification(gdoc, EDocumentFlowType.OUTBOUND_PROCESSING_START, isSuccess, (exitOBEx != null ? exitOBEx.getMessage(): ""), new Date(), false, false, false, false, "", exitOBEx);
        }
         
    		returnList.add(gdoc);
    	}
      return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitToOutbound] Error", t);
    	throw new ApplicationException("Unable to push document to Outbound folder", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exitToOutbound] End");
    }
  }

//  public Collection exitToExport(Collection gdocs)
//    throws Throwable
//  {
//    Logger.debug("[DocumentManagerBean.exitToExport] Start");
//    ArrayList returnList = new ArrayList();
//    ExportFolder exportFolder = new ExportFolder();
//    for(Iterator i = gdocs.iterator(); i.hasNext(); )
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      exitFromCurrentFolder(gdoc);
//      gdoc = exportFolder.doEnter(gdoc);
//      returnList.add(gdoc);
//    }
//    Logger.debug("[DocumentManagerBean.exitToExport] End");
//    return returnList;
//  }

  public Collection exitToExport(Collection gdocs)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exitToExport] Start");
    GridDocument gdoc = null;
    try
    {
    	ArrayList returnList = new ArrayList();
    	ExportFolder exportFolder = new ExportFolder();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		gdoc = (GridDocument)i.next();
    		
        boolean isSuccess = true;
        Throwable exportEx = null;
        try
        {
          exitFromCurrentFolder(gdoc);
          gdoc = FileHelper.duplicateTempUdocFile(gdoc);
          gdoc = exportFolder.doEnter(gdoc);
        }
        catch(Throwable th)
        {
          isSuccess = false;
          
          if(th instanceof InvocationTargetException)
          {
            if(th.getCause() != null)
            {
            	exportEx = th.getCause();
            }
          }
          else
          {
        	  exportEx = th;
          }
          throw th;
        }
        finally
        {
//        TWX 31102006 send out DocumentFlow notification
          sendDocumentFlowNotification(gdoc, EDocumentFlowType.EXPORT_PROCESSING_START,isSuccess, (exportEx != null ? exportEx.getMessage(): ""), new Date(), false, false, false, false, "", exportEx);
        }
    		returnList.add(gdoc);
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitToExport] Error", t);
    	throw new ApplicationException("Unable to push document to Export folder", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exitToExport] End");
    }
  }

//  public Collection exitToExport(Collection gdocs, Long portUid)
//    throws Throwable
//  {
//    Logger.debug("[DocumentManagerBean.exitToExport] Start");
//    ArrayList returnList = new ArrayList();
//    ExportFolder exportFolder = new ExportFolder();
//    for(Iterator i = gdocs.iterator(); i.hasNext(); )
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      if (portUid != null)
//      {
//        gdoc.setPortUid(portUid);
//        String portName = BackendDelegate.getManager().findPort(portUid).getName();
//        gdoc.setPortName(portName);
//      }
//      exitFromCurrentFolder(gdoc);
//      gdoc = exportFolder.doEnter(gdoc);
//      returnList.add(gdoc);
//    }
//    Logger.debug("[DocumentManagerBean.exitToExport] End");
//    return returnList;
//  }

  public Collection exitFolderActivity(Collection gdocs)
    throws ApplicationException
  {
//    Logger.debug("[DocumentManagerBean.exitFolderActivity] Start");
//    ArrayList returnList = new ArrayList();
//    Class[] param = {GridDocument.class};
//    for(Iterator i = gdocs.iterator(); i.hasNext(); )
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      String folder = gdoc.getFolder();
//      Object aFolder = Class.forName("com.gridnode.gtas.server.document.folder."+folder+"Folder").newInstance();
//      Object[] args = {gdoc};
//      gdoc = (GridDocument)
//        aFolder.getClass().getMethod("doActivityEnd", param).invoke(aFolder, args);
//      returnList.add(gdoc);
//    }
//    Logger.debug("[DocumentManagerBean.exitFolderActivity] End");
//    return returnList;

    Logger.debug("[DocumentManagerBean.exitFolderActivity] Start");
    GridDocument gdoc = null;
    try
    {
    	ArrayList returnList = new ArrayList();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
        gdoc = (GridDocument)i.next();
        
        boolean isSuccess = true;
        String errReason = "";
        try
        {
          gdoc = FileHelper.delTempUdocFile(gdoc);
          returnList.add(gdoc);
        }
        catch(Exception ex)
        {
          isSuccess = false;
          errReason = ex.getMessage();
          throw ex;
        }
        finally
        {
          
          Logger.log("[DocumentManagerBean.exitFolderActivity] Exist to workflow "+gdoc.getFolder()+ ". Gdoc filename is "+gdoc.getGdocFilename()+" auditFilename "+gdoc.getAuditFileName());
          if(OutboundFolder.FOLDER_NAME.equals(gdoc.getFolder()) || ExportFolder.FOLDER_NAME.equals(gdoc.getFolder()))
          {
            EDocumentFlowType docFlowType = getExitFolderFlowType(gdoc.getFolder());
            sendDocumentFlowNotification(gdoc, docFlowType, isSuccess, errReason, new Date(), false, false, false, false, "", null);
          }
        }
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitFolderActivity] Error", t);
    	throw new ApplicationException("Unable to perform exit folder activity", t);
    }
    finally 
    {
      Logger.debug("[DocumentManagerBean.exitFolderActivity] End");
    }
  }

  public Collection exitToPort(Collection gdocs, Long portUid)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exitToPort] Start");
    GridDocument gdoc = null;
    try
    {
    	ArrayList returnList = new ArrayList();
    	ExportFolder exportFolder = new ExportFolder();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		gdoc = (GridDocument)i.next();
    		/*
    		 gdoc.setPortUid(portUid);
    		 if (BackendDelegate.getManager().findPort(portUid) != null)
    		 {
    		 String portName = BackendDelegate.getManager().findPort(portUid).getName();
    		 gdoc.setPortName(portName);
    		 }
    		 else
    		 {
    		 throw new ExportException("Port Uid:"+portUid+" not found");
    		 }
    		 */
    		ExportDocumentHelper helper = new ExportDocumentHelper();
        
        boolean isSuccess = true;
        Throwable exitPortEx = null;
        try
        {
    		  gdoc = helper.prepareExportInfo(gdoc, portUid);
    		  gdoc = exportFolder.saveInFolder(gdoc);
    		  gdoc = helper.doExport(gdoc);
    		  gdoc = exportFolder.updateHeader(gdoc);
    		  returnList.add(gdoc);
        }
        catch(Throwable th)
        {
          isSuccess = false;
          exitPortEx = th;
          throw th;
        }
        finally
        {
          sendDocumentFlowNotification(gdoc, EDocumentFlowType.DOCUMENT_EXPORT, isSuccess, (exitPortEx != null? exitPortEx.getMessage(): ""), new Date(), isSuccess, false, isSuccess, isSuccess, gdoc.getPortName(), exitPortEx);
        }
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitToPort] Unable to push document to port", t);
    	throw new ApplicationException("Unable to push document to port", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exitToPort] End");
    }
  }

  public Collection saveToFolder(Collection gdocs)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.saveToFolder] Start");
    try
    {
    	ArrayList returnList = new ArrayList();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		GridDocument gdoc = (GridDocument)i.next();
//  		gdoc = folder.createHeader(gdoc);
          //TWX: 17 NOV 2005 User explicitly specify save a file to system folder in partner function.
          //                 EStore will not need such a file
          gdoc.setOriginalDoc(Boolean.FALSE);

    		gdoc = SystemFolder.getSpecificFolder(gdoc.getFolder()).saveInFolder(gdoc);
    		
    		returnList.add(gdoc);
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.saveToFolder] Error", t);
    	throw new ApplicationException("Unable to save document in folder", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.saveToFolder] End");
    }
  }

  public Collection exitToChannel(Collection gdocs)
    throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.exitToChannel] Start");
    GridDocument gdoc = null;
    try
    {
    	ArrayList returnList = new ArrayList();
    	OutboundFolder outboundFolder = new OutboundFolder();
    	for(Iterator i = gdocs.iterator(); i.hasNext(); )
    	{
    		gdoc = (GridDocument)i.next();
    		SendDocumentHelper helper = new SendDocumentHelper();
        
        Throwable saveFolderEx = null;
        boolean isExitToChannelSuccess = true;
        gdoc = helper.prepareSendingInfo(gdoc);
          
          try
          {
            gdoc = outboundFolder.saveInFolder(gdoc);
          }
          catch(Throwable th)
          {
        	saveFolderEx = th;
            isExitToChannelSuccess = false;
            //TWX 08012007 The success case will be taken care by SenDocumentHelper.send(gdoc)
            sendDocumentFlowNotification(gdoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, isExitToChannelSuccess, (saveFolderEx != null ? saveFolderEx.getMessage(): ""), new Date(), false, false, false, false, "", saveFolderEx);
            throw th;
          }
        helper.doSend(gdoc);
        //gdoc = outboundFolder.updateHeader(gdoc);
        returnList.add(gdoc);
    	}
    	return returnList;
    }
    catch (Throwable t)
    {
    	Logger.warn("[DocumentManagerBean.exitToChannel] Unable to push document to channel", t);
    	throw new ApplicationException("Unable to push document to channel", t);
    }
    finally
    {
    	Logger.debug("[DocumentManagerBean.exitToChannel] End");
    }
  }

  public List importAttachments(List attachmentFilenames,
                                String srcPathKey,
                                String srcSubPath)
                                throws CreateAttachmentException
  {
    ArrayList attachmentUids = new ArrayList();
    for (Iterator i = attachmentFilenames.iterator(); i.hasNext(); )
    {
      String orgFilename = i.next().toString();
      try
      {
        String newFilename = FileUtil.copy(srcPathKey,
                                           srcSubPath,
                                           orgFilename,
                                           IDocumentPathConfig.PATH_ATTACHMENT,
                                           "",
                                           orgFilename);
        Attachment newAttachment = new Attachment();
        newAttachment.setFilename(newFilename);
        //newAttachment.setIsSendInitiated(Boolean.FALSE);
        newAttachment.setOriginalFilename(orgFilename);
        newAttachment.setOutgoing(Boolean.TRUE);
        //newAttachment.setPartnerId(partnerId);
        Long uid = createAttachment(newAttachment);
        attachmentUids.add(uid);
      }
      catch (Exception ex)
      {
        throw new CreateAttachmentException("Error while creating attachment "+orgFilename, ex);
      }
    }
    return attachmentUids;
  }

  /**
   * Upload documents to GridMaster when connected
   */
  public void uploadToGridMaster()
  {
    try
    {
      Logger.debug("[DocumentManagerBean.uploadToGridMaster] Start");
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        GridDocument.IS_SENT,
        filter.getEqualOperator(),
        Boolean.FALSE,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.IS_LOCAL_PENDING,
        filter.getEqualOperator(),
        Boolean.FALSE,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.FOLDER,
        filter.getEqualOperator(),
        GridDocument.FOLDER_OUTBOUND,
        false
      );
      //040630NSL: filter out those not have recipient partner
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.R_PARTNER_ID,
        filter.getNotEqualOperator(),
        null,
        false);
       filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.R_PARTNER_ID,
        filter.getNotEqualOperator(),
        "",
        false);
      
      resumeSendDocs(filter);      
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentManagerBean.uploadToGridMaster] Unable to resume upload docs to GridMaster", ex);    
    }
    finally
    {
      Logger.debug("[DocumentManagerBean.uploadToGridMaster] End");
    }
    /*
    Collection list = getGridDocumentEntityHandler().getEntityByFilter(filter);
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      GridDocument gdoc = (GridDocument)i.next();
      
      if (gdoc.getRecipientChannelUid() != null)
      {
        Logger.debug("[DocumentManagerBean.uploadToGridMaster] resume upload to GM");

        SendDocumentHelper helper = new SendDocumentHelper();
        helper.doSend(gdoc, true);
      }
      else
      {
        DocChannelInfo cInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gdoc.getRecipientPartnerId());
        if (cInfo != null)
        {
          gdoc.setRecipientChannelUid(cInfo.getChannelUid());
          gdoc.setRecipientChannelName(cInfo.getChannelName());
          gdoc.setRecipientChannelProtocol(cInfo.getChannelProtocol());

          Logger.debug("[DocumentManagerBean.uploadToGridMaster] resume upload to GM");
          SendDocumentHelper helper = new SendDocumentHelper();
          helper.doSend(gdoc, true);
        }
        else
        {
          UploadToGridMasterException ex =
            new UploadToGridMasterException("No channel found for partner "+
                                           gdoc.getRecipientPartnerId());
          Logger.err("[DocumentManagerBean.uploadToGridMaster] Error uploading document", ex);
        }
      }
    }
    Logger.debug("[DocumentManagerBean.uploadToGridMaster] End");
    */
  }

  private void resumeSendDocs(IDataFilter filter) throws Throwable
  {
    Collection list = getGridDocumentEntityHandler().getEntityByFilter(filter);
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      GridDocument gdoc = (GridDocument)i.next();
      try
      {      
        SendDocumentHelper helper = new SendDocumentHelper();
        if (gdoc.getProcessDefId() != null && gdoc.getProcessDefId().length()==0)
          helper.prepareSendingInfo(gdoc);
        helper.doSend(gdoc, true);
      }
      catch (Throwable ex)
      {
        Logger.warn("[DocumentManagerBean.resumeSendDocs] Unable to resume send gdoc: "+gdoc.getGdocId(), ex);
      }
    }    
  }
  /**
   * Resume send when partner comes online
   *
   * @param nodeId the recipient node id to resume sending the documents to
   */
  public void resumeSend(String nodeId)
  {
    try
    {
      Logger.debug("[DocumentManagerBean.resumeSend] Start");
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        GridDocument.IS_SENT,
        filter.getEqualOperator(),
        Boolean.FALSE,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.IS_LOCAL_PENDING,
        filter.getEqualOperator(),
        Boolean.TRUE,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.R_NODE_ID,
        filter.getEqualOperator(),
        nodeId,
        false
      );
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.FOLDER,
        filter.getEqualOperator(),
        GridDocument.FOLDER_OUTBOUND,
        false
      );
      //040630NSL: filter out those not have recipient partner
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.R_PARTNER_ID,
        filter.getNotEqualOperator(),
        null,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        GridDocument.R_PARTNER_ID,
        filter.getNotEqualOperator(),
        "",
        false);
        
      resumeSendDocs(filter);    
    }
    catch (Throwable t)
    {
      Logger.warn("[DocumentManagerBean.resumeSend] Unable to resume send docs to partner "+nodeId, t);
    }
    finally
    {
      Logger.debug("[DocumentManagerBean.resumeSend] End");
    }
    /*
    Collection list = getGridDocumentEntityHandler().getEntityByFilter(filter);
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      GridDocument gdoc = (GridDocument)i.next();
      
      if (gdoc.getRecipientChannelUid() != null)
      {
        SendDocumentHelper helper = new SendDocumentHelper();
        helper.doSend(gdoc, true);
      }
      else
      {
        DocChannelInfo cInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gdoc.getRecipientPartnerId());
        if (cInfo != null)
        {
          gdoc.setRecipientChannelUid(cInfo.getChannelUid());
          gdoc.setRecipientChannelName(cInfo.getChannelName());
          gdoc.setRecipientChannelProtocol(cInfo.getChannelProtocol());
          SendDocumentHelper helper = new SendDocumentHelper();
          helper.doSend(gdoc, true);
        }
        else
        {
          ResumeSendException ex =
            new ResumeSendException("No channel found for partner "+
                                   gdoc.getRecipientPartnerId());
          Logger.err("[DocumentManagerBean.resumeSend] Error sending document", ex);
        }
      }
    }
    Logger.debug("[DocumentManagerBean.resumeSend] End");
    */
  }

  /**
   * Query the GridDocuments using the SearchQuery and returns the list of
   * matching GridDocument keys
   *
   * @param queryUid the SearchQuery that contains the search condition
   * @returns the list of matching GridDocument keys
   */
  public Collection searchDoc(IDataFilter filter, Long queryUid)
    throws SearchDocumentException
  {
  	try
  	{
  		return SearchQueryDelegate.getInstance().searchDoc(filter, queryUid);
  	}
  	catch (SearchDocumentException ex)
  	{
  		throw ex;
  	}
  	catch (Throwable t)
  	{
  		throw new SearchDocumentException("Unexpected error performing search operation", t);
  	}
  }

  protected void exitFromCurrentFolder(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[DocumentManagerBean.exitFromCurrentFolder] Start");
    String folder = gdoc.getFolder();
    Object aFolder = Class.forName("com.gridnode.gtas.server.document.folder."+folder+"Folder").newInstance();
    Class[] param = {GridDocument.class};
    Object[] args = {gdoc};
    aFolder.getClass().getMethod("doExit", param).invoke(aFolder, args);
    Logger.debug("[DocumentManagerBean.exitFromCurrentFolder] End");
  }
  
  /**
   * TWX 31102006 Send out the document flow activity that we have performed on the business document (Signal included).
   * @param gdoc The GridDocument that has associated a business document
   * @param docFlowType The type of the doc flow activity
   * @param status The status of performing the doc flow activity
   * @param errorReason If the status is false, an errorReason will be provided
   * @param occurDate The date we performed the doc flow activity
   * @param isIncludedUserDoc To specify whether we need to include the user doc in the notification
   * @param isIncludeReceiptAudit To specify whether we need to include the ReceiptAudit in the notification
   * @param isAttachmentIncluded To indicate we need to include the attachment.
   * @param isRequiredPack To indicate the given doc (the udoc) whether to package into mime format
   * @param docFlowAddInfo To include additional information about the docFlowType. Eg we can include
   *                       the port name for the docFlowType IDocumentFlow.DOCUMENT_EXPORT  
   */
  private void sendDocumentFlowNotification(GridDocument gdoc, EDocumentFlowType docFlowType, boolean status,
                                            String errorReason, Date occurDate, boolean isIncludedUserDoc,
                                            boolean isIncludedReceiptAudit, boolean isAttachmentIncluded,
                                            boolean isRequiredPack, String docFlowAddInfo, Throwable th)
    throws ApplicationException, SystemException
  {
    if(gdoc == null)
    {
      Logger.warn("[DocumentManagerBean.sendDocumentFlowNotification] GDOC is null !");
      return;
    }
    String tracingID = gdoc.getTracingID();
    File doc = null;
    
    if(isIncludedUserDoc)
    {
      doc = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, gdoc.getFolder()+"/",gdoc.getUdocFilename());
      if(doc == null)
      {
        Logger.warn("[DocumentManagerBean.sendDocumentFlowNotification] userDoc "+gdoc.getUdocFilename()+" doesn't exist. No notification will be trggered !!!");
        return;
      }
    }
    
    if(isIncludedReceiptAudit)
    {
      doc = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, gdoc.getReceiptAuditFileName());
      if(doc == null)
      {
        Logger.warn("[DocumentManagerBean.sendDocumentFlowNotification] receiptAudit file "+gdoc.getReceiptAuditFileName()+" doesn't exist. No notification will be trggered !!!");
        return;
      }
    }
    
    List attachmentUIDs = null;
    if(isAttachmentIncluded)
    {
      if(gdoc.hasAttachment())
      {
        attachmentUIDs = gdoc.getAttachments();
      }
    }
    
    String docFilePath = doc == null ? "" : doc.getAbsolutePath();
    String recipientBeID = gdoc.getRecipientBizEntityId();
    String senderBeID = gdoc.getSenderBizEntityId();
    
    //For exit to OB, exit to export, the gdoc still haven't been persisted, thus we
    //will make use of the gdoc's src folder as the folder
    String folder = "";
    if( gdoc.getKey() == null || 0 == (Long)gdoc.getKey())
    {
      folder = gdoc.getSrcFolder();
      //Special Handling for IB->IP,  IB -> OB
      if(InboundFolder.FOLDER_NAME.equals(gdoc.getSrcFolder()) && ImportFolder.FOLDER_NAME.equals(gdoc.getFolder())
          || (InboundFolder.FOLDER_NAME.equals(gdoc.getSrcFolder()) && OutboundFolder.FOLDER_NAME.equals(gdoc.getFolder())))
      {
        recipientBeID = gdoc.getSenderBizEntityId();
        senderBeID = gdoc.getRecipientBizEntityId();
      }
    }
    else
    {
      folder = gdoc.getFolder();
    }
    
    if(_sessionCtx.getRollbackOnly())
    {
    	Logger.debug("DocumentManagerBean.sendDocumentFlowNotification: transaction mark as Rollback");
    }
    DocumentFlowNotifyHandler.triggerNotification(docFlowType, occurDate, folder, gdoc.getGdocId(), status,
                                                  errorReason, tracingID, gdoc.getUdocDocType(), docFilePath,
                                                  recipientBeID, senderBeID,
                                                  attachmentUIDs, isRequiredPack, docFlowAddInfo, (Long)gdoc.getKey(), gdoc.getSrcFolder(),
                                                  th);
  }
  
  /**
   * TWX 31102006 To determine the document flow (while we exit from workflow) given the folder type.
   * Currently, the folder type supported is OutboundFolder.FOLDER_NAME and
   * ExportFolder.FOLDER_NAME
   * @param folder The GridDocument folder type.
   * @return the type of the DocumentFlow
   */
  private EDocumentFlowType getExitFolderFlowType(String folder)
  {
    if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return EDocumentFlowType.OUTBOUND_PROCESSING_END;
    }
    else if(ExportFolder.FOLDER_NAME.equals(folder))
    {
      return EDocumentFlowType.EXPORT_PROCESSING_END;
    }
    else
    {
      throw new IllegalArgumentException("[DocumentManagerBean.getExitFolderFlowType] Folder "+folder+" is not supported !");
    }
  }
  
  /**
   * TWX 01112006 If the document is directly imported via GT UI, we need to include the imported doc
   * in the DocumentFlowNotification.
   * @param gdoc
   * @return true if not tracingID be created, false otherwise.
   */
  private boolean isRequireIncludeImportedDoc(GridDocument gdoc)
  {
    String tracingID = gdoc.getTracingID();
    if(tracingID == null || "".equals(tracingID))
    {
      return true;
    }
    return false;
  }
  
  private GridDocument addTracingID(GridDocument gdoc)
  {
    String tracingID = gdoc.getTracingID();
    if(tracingID == null || "".equals(tracingID))
    {
      gdoc.setTracingID(UUIDUtil.getRandomUUIDInStr());
    }
    return gdoc;
  }
  
  /**
   * TWX Handling the reprocessing for both the action msg and signal msg
   * @param tracingID an unique identifier that identified a list of event that has been acted
   *        on the transaction document.
   */
  public void reprocessDoc(String tracingID) throws ApplicationException
  {
    try
    {
      Collection gdocList = getGdocSortByUID(tracingID);
      if(gdocList == null || gdocList.size() == 0)
      {
        throw new ApplicationException("[DocumentManagerBean.reprocessDoc] No gdoc can be found given [tracingID: "+tracingID+"]");
      }
      
      //the first gdoc can be in folder IP, OB, IB
      GridDocument gdoc = (GridDocument)gdocList.iterator().next();
      if(gdoc.getRefGdocId() != null && gdoc.getRefGdocId() > 0)
      {
        throw new ApplicationException("[DocumentManagerBean.reprocessDoc] the gdoc retrieve [gdocID :"+gdoc.getGdocId()+" folder: "+gdoc.getFolder()+" is not the first doc");
      }
      else
      {
        Logger.debug("Retrieve first PF GDOC is [folder: "+gdoc.getFolder()+", gdocID "+gdoc.getGdocId());
        
        String folder = gdoc.getFolder();
        String udocType = gdoc.getUdocDocType();
        
        if(InboundFolder.FOLDER_NAME.equals(folder) || ImportFolder.FOLDER_NAME.equals(folder))
        {
          PartnerProcessDelegate.triggerPartnerFunction(gdoc, getTriggerInfo(gdoc));
        }
        else if(OutboundFolder.FOLDER_NAME.equals(folder) && ("RN_ACK".equals(udocType) 
                                                                 || "RN_EXCEPTION".equals(udocType)))
        {
          //Send OB via SendDocHelper
          sendOBSignal(gdoc);
        }
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw new ApplicationException("[DocumentManagerBean.reprocessDoc] Error in re-trigger the partner function given tracingID ["+tracingID+"]", ex);
    }
  }
  
  /**
   * Directly send the gdoc correspond signal msg to partner. No extra OB gdoc will be created.
   * @param gdoc
   * @throws ApplicationException
   */
  private void sendOBSignal(GridDocument gdoc) throws ApplicationException
  {
    try
    {
      SendDocumentHelper sendDocHelper = new SendDocumentHelper();
      sendDocHelper.doSend(gdoc,true);
    }
    catch(Throwable ex)
    {
      throw new ApplicationException("[DocumentManagerBean.sendOBSignal] Error in sending the OB signal to WF.", ex);
    }
  }
  
  /**
   * Retrieve the list of gdoc that sharing the same tracingID. The lsit will be sort by Gdoc UID.
   * @param tracingID
   * @return
   * @throws Exception
   */
  private Collection getGdocSortByUID(String tracingID) throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, GridDocument.TRACING_ID, filter.getEqualOperator(), tracingID,false);
    filter.addOrderField(GridDocument.UID, true);
    Collection gdocList = findGridDocuments(filter);
    return gdocList;
  }
  
  /**
   * Get the TriggerInfo given the gdoc
   * @param gdoc
   * @return
   */
  private TriggerInfo getTriggerInfo(GridDocument gdoc)
  {
    TriggerInfo triggerInfo = new TriggerInfo();
    triggerInfo.setDocType(gdoc.getUdocDocType());
    
    
    String folder = gdoc.getFolder();
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      triggerInfo.setPartnerGroup(gdoc.getSenderPartnerGroup());
      triggerInfo.setPartnerId(gdoc.getSenderPartnerId());
      triggerInfo.setPartnerType(gdoc.getSenderPartnerType());
      triggerInfo.setTriggerType(TriggerInfo.TRIGGER_RECEIVE);
    }
    else if(ImportFolder.FOLDER_NAME.equals(folder))
    {
      triggerInfo.setPartnerGroup(gdoc.getRecipientPartnerGroup());
      triggerInfo.setPartnerId(gdoc.getRecipientPartnerId());
      triggerInfo.setPartnerType(gdoc.getRecipientPartnerType());
      triggerInfo.setTriggerType(TriggerInfo.TRIGGER_IMPORT);
    }
    
    return triggerInfo;
  }
  //end reprocess
  
  
  public void importDocument(String userId,
                             String userName,
                             String senderEnterpriseId,
                             String senderBizEntId,
                             List recipients,
                             String docType,
                             List importFiles,
                             List attachments,
                             String sourcePathKey, 
                             String sourceSubPath,
                             String rnProfile,
                             String uniqueDocId,
                             String processInstanceId,
                             String tracingID) throws ApplicationException
  {
    Logger.debug("[DocumentManagerBean.importDocument] Enter");
    ImportDocumentHelper helper = new ImportDocumentHelper();
    try
    {
      List attachmentUids = null;
      if (attachments!=null && !attachments.isEmpty())
      {
        attachmentUids = importAttachments(attachments, sourcePathKey, sourceSubPath);
      }
      
      List newImportFiles = helper.copyFiles(importFiles, sourcePathKey, sourceSubPath);
      
      if (senderEnterpriseId == null || senderEnterpriseId.length()==0)
      {
        senderEnterpriseId = helper.getOwnGridnodeId();
      }
      GridDocument gdoc = null;
      if (recipients.isEmpty())
      {
        Logger.debug("[DocumentManagerBean.importDocument] no recipients found");
        for(Iterator i = newImportFiles.iterator(); i.hasNext(); )
        {
          String udocFilename = i.next().toString();
          gdoc = helper.prepareImportDoc(userId, userName, senderEnterpriseId, senderBizEntId, null, 
                                  docType, udocFilename, attachmentUids, 
                                  rnProfile, uniqueDocId, processInstanceId, tracingID);
          try
          {
            importDoc(gdoc);
          }
          catch (ApplicationException ex) //NSL20051110 Throw PartnerFunctionFailure here
          {
            new PartnerFunctionFailure(gdoc, 
                                       PartnerFunctionFailure.TYPE_IMPORT_DOC_FAILURE, 
                                       PartnerFunctionFailure.REASON_GENERAL_ERROR, 
                                       ex).raiseAlert();
            throw ex; //TWX 13 Dec 2007 so that the upper layer can handle the fail to import exception.
          }
        }
      }
      else
      {
        for(Iterator i = recipients.iterator(); i.hasNext(); )
        {
          Logger.debug("[DocumentManagerBean.importDocument] newImportFiles = "+newImportFiles);
          String recipient = i.next().toString();
          Logger.debug("[DocumentManagerBean.importDocument] recipient = "+recipient);
          
          for(Iterator j = newImportFiles.iterator(); j.hasNext(); )
          {
            String udocFilename = j.next().toString();
            gdoc = helper.prepareImportDoc(userId, userName, senderEnterpriseId, senderBizEntId, recipient, 
                                    docType, udocFilename, attachmentUids, 
                                    rnProfile, uniqueDocId, processInstanceId, tracingID);
            
            try
            {
              importDoc(gdoc);
            }
            catch (ApplicationException ex) //NSL20051110 Throw PartnerFunctionFailure here
            {
              new PartnerFunctionFailure(gdoc, 
                                         PartnerFunctionFailure.TYPE_IMPORT_DOC_FAILURE, 
                                         PartnerFunctionFailure.REASON_GENERAL_ERROR, 
                                         ex).raiseAlert();
              throw ex; //TWX 13 Dec 2007 so that the upper layer can handle the fail to import exception.
            }
          }
        }
      }
      
    }
    catch (Throwable t)
    {
      Logger.warn("[DocumentManagerBean.importDocument] Unable to import document", t);
      throw new ApplicationException("Unable to import document", t);
    }
    finally
    {
      //cleanup
      helper.deleteFiles(importFiles, sourcePathKey, sourceSubPath);
      helper.deleteFiles(attachments, sourcePathKey, sourceSubPath);
      Logger.debug("[DocumentManagerBean.importDocument] End");
    }
  }
  
  // ********************* EntityHandlers ****************************

  private DocumentTypeEntityHandler getDocumentTypeEntityHandler()
  {
     return DocumentTypeEntityHandler.getInstance();
  }

  private FileTypeEntityHandler getFileTypeEntityHandler()
  {
     return FileTypeEntityHandler.getInstance();
  }

  private GridDocumentEntityHandler getGridDocumentEntityHandler()
  {
     return GridDocumentEntityHandler.getInstance();
  }

  private AttachmentEntityHandler getAttachmentEntityHandler()
  {
     return AttachmentEntityHandler.getInstance();
  }
  
  private AS2DocTypeMappingEntityHandler getAS2DocTypeMappingEntityHandler()
  {
     return AS2DocTypeMappingEntityHandler.getInstance();
  }
  
  /**
   * Find a AS2DocTypeMapping by using the AS2DocTypeMapping UID.
   *
   * @param AS2DocTypeMappingUId The UID of the AS2DocTypeMapping to find.
   * @return The DocumentType found, or <B>null</B> if none exists with that
   * UID.
   */
  public AS2DocTypeMapping findAS2DocTypeMapping(Long as2DocTypeMappingUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.findAS2DocTypeMapping] UID: "+as2DocTypeMappingUId);

    AS2DocTypeMapping docTypeMapping = null;

    try
    {
      docTypeMapping =(AS2DocTypeMapping)getAS2DocTypeMappingEntityHandler().getEntityByKeyForReadOnly(as2DocTypeMappingUId);
    }
    catch (ApplicationException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMapping] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMapping] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMapping] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAS2DocTypeMapping(as2DocTypeMappingUId) Error ",
        ex);
    }

    return docTypeMapping;
  }
  
  /**
   * Find AS2 document type mapping by filter.
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   */
  public Collection  findAS2DocTypeMappingByFilter(IDataFilter filter)
                      throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findAS2DocTypeMappingByFilter] filter: "+
      (filter==null?"null":filter.getFilterExpr()));
  
    Collection docTypeMappings = null;
    try
    {
      docTypeMappings = getAS2DocTypeMappingEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingByFilter] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingByFilter] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingByFilter] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAS2DocTypeMappingByFilter(filter) Error ",
        ex);
    }
  
    return docTypeMappings;
  }
  
  /**
   * Find a number of DocumentType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of DocumentType found, or empty collection if
   * none exists.
   */
  public Collection findAS2DocTypeMappingKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[DocumentManagerBean.findAS2DocTypeMappingKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection docTypeMappingKeys = null;
    try
    {
      docTypeMappingKeys =
        getAS2DocTypeMappingEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.findAS2DocTypeMappingKeys] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.findAS2DocTypeMappingKeys(filter) Error ",
        ex);
    }

    return docTypeMappingKeys;
  }
  
  
  
  
  /**
   * Create a new AS2DocTypeMapping.
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   */
  public Long createAS2DocTypeMapping(AS2DocTypeMapping documentTypeMapping)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[DocumentManagerBean.addAS2DocTypeMapping] Enter");
    AS2DocTypeMapping docTypeMapping = null;
    try
    {
      docTypeMapping = (AS2DocTypeMapping)getAS2DocTypeMappingEntityHandler().createEntity(documentTypeMapping);
    }
    catch (CreateException ex)
    {
      Logger.err("[DocumentManagerBean.addAS2DocTypeMapping] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.err("[DocumentManagerBean.addAS2DocTypeMapping] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.addAS2DocTypeMapping] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.addAS2DocTypeMapping(AS2DocTypeMapping) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.addAS2DocTypeMapping] Exit");
   
    return (Long)docTypeMapping.getKey();
  }
  
  /**
   * Delete a AS2DocTypeMapping.
   *
   * @param AS2DocTypeMapping The UID of the AS2DocTypeMapping to delete.
   */
  public void deleteAS2DocTypeMapping(Long as2DocTypeMappingUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.deleteAS2DocTypeMapping] Enter");

    try
    {
      getAS2DocTypeMappingEntityHandler().remove(as2DocTypeMappingUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.err("[DocumentManagerBean.deleteAS2DocTypeMapping] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.err("[DocumentManagerBean.deleteAS2DocTypeMapping] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.deleteAS2DocTypeMapping] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.deleteAS2DocTypeMapping(deleteAS2DocTypeMapping) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.deleteAS2DocTypeMapping] Exit");
  }
  
  /**
   * Update a AS2DocTypeMapping
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity with changes.
   */
  public void updateAS2DocTypeMapping(AS2DocTypeMapping documentTypeMapping)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[DocumentManagerBean.updateAS2DocTypeMapping] Enter");

    try
    {
      getAS2DocTypeMappingEntityHandler().update(documentTypeMapping);
    }
    catch (EntityModifiedException ex)
    {
      Logger.err("[DocumentManagerBean.updateAS2DocTypeMapping] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.err("[DocumentManagerBean.updateAS2DocTypeMapping] Error ", ex);
      throw new SystemException(
        "DocumentManagerBean.updateAS2DocTypeMapping(AS2DocTypeMapping) Error ",
        ex);
    }

    Logger.log("[DocumentManagerBean.updateAS2DocTypeMapping] Exit");
  }
  
  

}

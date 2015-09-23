/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Jul 23 2002    Koh Han Sing        Added management of FileType
 * Aug 02 2002    Koh Han Sing        Added management of GridDocument
 * Nov 25 2002    Koh Han Sing        Added attachment feature
 * Jan 15 2002    neo Sok Lay         receiveDocAck() renamed to handleSendDocAck()
 *                                    - to accept TrxId as parameter. TrxId will
 *                                      be used as GDocId of sent GridDoc.
 *                                    Added handleUploadDocAck() and handleDocTransCompleted()
 *                                    Modify processRnifDoc() to accept header
 *                                    and data payload parameters.
 * Sep 02 2003    Koh Han Sing        Added find attachments by griddocument
 * Oct 02 2003    Koh Han Sing        Manual Export
 * Nov 05 2003    Neo Sok Lay         To pass additionalHeaders to processNormalDoc()
 * Jan 29 2004    Neo Sok Lay         Add completeTransNow parameter for:
 *                                    - handleDocAccepted(), handleDocReject().
 * Sep 07 2005    Neo Sok Lay         Change createDocumentType() to return Long.       
 *                                    All remote methods must throw RemoteException.                            
 * Oct 20 2005    Neo Sok Lay         No corresponding business method in the bean class 
 *                                    com.gridnode.gtas.server.document.facade.ejb.DocumentManagerBean 
 *                                    was found for method:
 *                                    - createDocumentType
 *                                    - createFileType
 *                                    Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method createGridDocument does not throw java.rmi.RemoteException                                   
 * Oct 26 2005    Neo Sok Lay         Business methods throwing Throwable is not acceptable
 *                                    for SAP J2EE deployment
 * Jan 31 2007    Tam Wei Xiang       Add in method for reprocess doc      
 * Mar 15 2007    Neo Sok Lay         Add importDocument().                             
 * Nov 12 2008    Wong Yee Wah        added interface for as2DocTypeMapping CRUD    
*/
package com.gridnode.gtas.server.document.facade.ejb;

import com.gridnode.gtas.server.document.exceptions.CreateAttachmentException;
import com.gridnode.gtas.server.document.exceptions.SearchDocumentException;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import javax.ejb.EJBObject;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Collection;

/**
 * LocalObject for DocumentManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IDocumentManagerObj
  extends        EJBObject
{
  /**
   * Create a new DocumentType
   *
   * @param documentType The DocumentType entity.
   */
  public Long createDocumentType(DocumentType documentType)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a DocumentType
   *
   * @param documentType The DocumentType entity with changes.
   */
  public void updateDocumentType(DocumentType documentType)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a DocumentType.
   *
   * @param documentTypeUId The UID of the DocumentType to delete.
   */
  public void deleteDocumentType(Long documentTypeUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a DocumentType using the DocumentType UID.
   *
   * @param documentTypeUId The UID of the DocumentType to find.
   * @return The DocumentType found, or <B>null</B> if none exists with that
   * UID.
   */
  public DocumentType findDocumentType(Long documentTypeUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a DocumentType using the DocumentType Name.
   *
   * @param docTypeName The DocumentType Name of the DocumentType to find.
   * @return The DocumentType found, or <B>null</B> if none exists.
   */
  public DocumentType findDocumentType(String docTypeName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of DocumentType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of DocumentType found, or empty collection if none
   * exists.
   */
  public Collection findDocumentTypes(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of DocumentType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of DocumentType found, or empty collection if
   * none exists.
   */
  public Collection findDocumentTypesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;




  // ************************* FileType **************************

  /**
   * Create a new FileType
   *
   * @param fileType The FileType entity.
   */
  public Long createFileType(FileType fileType)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a FileType
   *
   * @param fileType The FileType entity with changes.
   */
  public void updateFileType(FileType fileType)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a FileType.
   *
   * @param fileTypeUId The UID of the FileType to delete.
   */
  public void deleteFileType(Long fileTypeUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a FileType using the FileType UID.
   *
   * @param fileTypeUId The UID of the FileType to find.
   * @return The FileType found, or <B>null</B> if none exists with that
   * UID.
   */
  public FileType findFileType(Long fileTypeUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a FileType using the FileType Name.
   *
   * @param fileTypeName The FileType Name of the FileType to find.
   * @return The FileType found, or <B>null</B> if none exists.
   */
  public FileType findFileType(String fileTypeName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of FileType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of FileType found, or empty collection if none
   * exists.
   */
  public Collection findFileTypes(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of FileType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of FileType found, or empty collection if
   * none exists.
   */
  public Collection findFileTypesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;




  // ********************* GridDocument ****************************

  /**
   * Create a new GridDocument.
   *
   * @param gDoc The GridDocument entity.
   * @param updateGDocId wether to generate a GDocId or use the original one
   */
  public GridDocument createGridDocument(GridDocument gDoc, boolean updateGDocId)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Create a new GridDocument.
   *
   * @param gDoc The GridDocument entity.
   */
  public GridDocument createGridDocument(GridDocument gDoc)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;
  
  /**
   * Create a GridDocument with transaction 'RequiresNew';
   * @param gDoc
   * @return
   * @throws CreateEntityException
   * @throws SystemException
   * @throws DuplicateEntityException
   * @throws RemoteException
   */
  public GridDocument createGridDocumentWithNewTrans(GridDocument gDoc)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;
  
  /**
   * Update a GridDocument
   *
   * @param gDoc The GridDocument entity with changes.
   */
  public void updateGridDocument(GridDocument gDoc)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GridDocument.
   *
   * @param gDocUId The UID of the GridDocument to delete.
   */
  public void deleteGridDocument(Long gDocUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a GridDocument using the GridDocument UID.
   *
   * @param gDocUId The UID of the GridDocument to find.
   * @return The GridDocument found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridDocument findGridDocument(Long gDocUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridDocument that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridDocument found, or empty collection if none
   * exists.
   */
  public Collection findGridDocuments(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridDocument that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridDocument found, or empty collection if
   * none exists.
   */
  public Collection findGridDocumentsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a GridDocument in a specific folder.
   *
   * @param folder The folder to look for the GridDocument
   * @param gdocID GridDoc ID of the GridDocument to look for.
   * @return The GridDocument found, or <b>null</b> if none exists.
   */
  public GridDocument findGridDocument(String folder, Long gdocID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the complete GridDocument i.e. all fields loaded.
   *
   * @param gdoc The GridDocument
   * @return The GridDocument loaded with all fields.
   */
  public GridDocument getCompleteGridDocument(GridDocument gdoc)
    throws Exception, RemoteException;

  // ********************* Attachment ****************************

  /**
   * Create a new Attachment.
   *
   * @param attachment The Attachment entity.
   */
  public Long createAttachment(Attachment attachment)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a Attachment
   *
   * @param attachment The Attachment entity with changes.
   */
  public void updateAttachment(Attachment attachment)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a Attachment.
   *
   * @param attachmentUid The UID of the Attachment to delete.
   */
  public void deleteAttachment(Long attachmentUid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a Attachment using the Attachment UID.
   *
   * @param attachmentUid The UID of the Attachment to find.
   * @return The Attachment found, or <B>null</B> if none exists with that
   * UID.
   */
  public Attachment findAttachment(Long attachmentUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Attachment that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Attachment found, or empty collection if none
   * exists.
   */
  public Collection findAttachments(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the Attachments linked to this GridDocument.
   *
   * @param gdoc The GridDocument.
   * @return a Collection of Attachment found, or empty collection if none
   * exists.
   */
  public Collection findAttachments(GridDocument gdoc)
    throws FindEntityException, SystemException, RemoteException;;

  /**
   * Find a number of GridDocument that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridDocument found, or empty collection if
   * none exists.
   */
  public Collection findAttachmentsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************* Document Flow ****************************

  /**
   * View the user document represented by the GridDocument
   *
   * @param gdocID the gdocID of the GridDocument whose uDoc is to
   * be viewed.
   */
  public void viewDoc(Long gdocID) throws Exception, RemoteException;

  /**
   * Edit the user document represented by the GridDocument
   *
   * @param gdocID the gdocID of the GridDocument whose uDoc is to
   * be edited.
   */
  public void editDoc(Long gdocID, String userId) throws Exception, RemoteException;

  /**
   * Import the list of GridDocuments
   */
//  public void importDoc(Collection gdocs) throws Throwable;

  /**
   * Import a single GridDocument
   */
  public void importDoc(GridDocument gdoc) throws ApplicationException, RemoteException;

//  public void sendDoc(Collection gdocs) throws Throwable;

 /**
   * Manual send a GridDocument
   *
   * @param gdoc the GridDocument to send
   */
  public void sendDoc(GridDocument gdoc) throws ApplicationException, RemoteException;

 /**
   * Manual export a GridDocument
   *
   * @param gdoc the GridDocument to export
   */
  public void exportDoc(GridDocument gdoc) throws ApplicationException, RemoteException;

  /**
   * Query the GridDocuments using the SearchQuery and returns the list of
   * matching GridDocument keys
   *
   * @param queryUid the SearchQuery that contains the search condition
   * @returns the list of matching GridDocument keys
   */
  public Collection searchDoc(IDataFilter filter, Long queryUid)
    throws SearchDocumentException, RemoteException;

  /*
  public void receiveDoc(String eventID,
                         String[] dataReceived,
                         GridDocument gdoc,
                         File udocFile)
                         throws Throwable;
  */

  public GridDocument processNormalDoc(String[] header,
                                       String[] dataReceived,
                                       File[] filesReceived,
                                       Hashtable additionalHeaders)
                                       throws ApplicationException, RemoteException;

  public GridDocument processRnifDoc(GridDocument gdoc,
                                     String[] header,
                                     String[] dataReceived,
                                     File[] filesReceived)
                                     throws ApplicationException, RemoteException;

  public void receiveFailedRnifDoc(GridDocument gdoc,
                                   boolean isValidationFailed,
                                   boolean isRetry)
                                   throws ApplicationException, RemoteException;

  public void receiveDoc(GridDocument gdoc) throws Exception, RemoteException;

  /*
  public void receiveDocAck(String eventID,
                            String[] dataReceived)
                            throws Throwable;
  */
  public void handleSendDocAck(String trxID,
                               String[] dataReceived)
                               throws ApplicationException, SystemException, RemoteException;

  public void handleUploadDocAck(String trxID)
                               throws ApplicationException, SystemException, RemoteException;

  public void handleDocTransCompleted(String trxID,
                                      String[] dataReceived)
                                      throws ApplicationException, SystemException, RemoteException;

  public Collection exitToImport(Collection gdocs) throws ApplicationException, RemoteException;

  public Collection exitToOutbound(Collection gdocs) throws ApplicationException, RemoteException;

//  public Collection exitToExport(Collection gdocs, Long portUid) throws Throwable;
  public Collection exitToExport(Collection gdocs) throws ApplicationException, RemoteException;

  public Collection exitFolderActivity(Collection gdocs) throws ApplicationException, RemoteException;

  public Collection exitToPort(Collection gdocs, Long portUid) throws ApplicationException, RemoteException;

  public Collection saveToFolder(Collection gdocs) throws ApplicationException, RemoteException;

  public Collection exitToChannel(Collection gdocs) throws ApplicationException, RemoteException;

  public List importAttachments(List attachmentFilenames,
                                String srcPathKey,
                                String srcSubPath)
                                throws CreateAttachmentException, RemoteException;

  public void handleDocAccepted(GridDocument gdoc,
                                String recipientGdocId,
                                String transCompleteTS,
                                boolean raiseAlert,
                                boolean completeTransNow)
                                throws ApplicationException, SystemException, RemoteException;

  public void handleDocRejected(GridDocument gdoc, boolean completeTransNow) throws ApplicationException, SystemException, RemoteException;

  public void handleDocSent(GridDocument gdoc)
                            throws ApplicationException, SystemException, RemoteException;


  /**
   * Upload documents to GridMaster when connected
   */
  public void uploadToGridMaster()
    throws RemoteException;

  /**
   * Resume send when partner comes online
   *
   * @param nodeId the recipient node id to resume sending the documents to
   */
  public void resumeSend(String nodeId)
    throws RemoteException;
  
  /**
   * Handling the reprocessing for both the action msg and signal msg
   * @param tracingID an unique identifier that identified a list of event that has been acted
   *        on the transaction document.
   */
  public void reprocessDoc(String tracingID) throws ApplicationException, RemoteException;
  
  /**
   * Import files into GridTalk
   * @param userId User id of user that performs the import
   * @param userName Name of the user that performs the import
   * @param senderEnterpriseId EnterpriseId of the sender's business entity
   * @param senderBizEntId Business entity id of the sender. May be empty, which means will use default business entity id.
   * @param recipients List of recipient partner ids to send to. May be empty, which means will be determined from the import files.
   * @param docType Document type of the files to be imported.
   * @param importFiles List of filenames of the files to import in the source path locations.
   * @param attachments List of filenames of the attachment files to import in the source path locations. 
   * @param sourcePathKey Path key of the source path location.
   * @param sourceSubPath Sub path under the source path location where the files are located.
   * @param rnProfile UID of the RNProfile that the import files should tie in the process instance.
   * @param uniqueDocId Unique doc identifier for customer backend system tracking.
   * @param processInstanceId Process intance id to use for the import files when the files are required to send out to trading partner.
   * @param tracingID Tracing ID to use for audit trail.
   * @throws ApplicationException Error during import of any of the files specified.
   * @throws RemoteException
   */
  public void importDocument(String userId, String userName, String senderEnterpriseId, String senderBizEntId, List recipients,
                             String docType, List importFiles, List attachments, String sourcePathKey, String sourceSubPath,
                             String rnProfile, String uniqueDocId, String processInstanceId, String tracingID) 
    throws ApplicationException, RemoteException;
  
  /**
   * Find a AS2DocTypeMapping by using the AS2DocTypeMapping UID.
   *
   * @param AS2DocTypeMappingUId The UID of the AS2DocTypeMapping to find.
   * @return The DocumentType found, or <B>null</B> if none exists with that
   * UID.
   */
  public AS2DocTypeMapping findAS2DocTypeMapping(Long as2DocTypeMappingUId)
    throws FindEntityException, SystemException, RemoteException;
  
  /**
   * Find AS2 document type mapping by filter.
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   */
  public Collection findAS2DocTypeMappingByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;
  
  
  /**
   * Find AS2 document type mapping keys by filter.
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   */
  public Collection findAS2DocTypeMappingKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;
  
  /**
   * Create a new AS2DocTypeMapping.
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity.
   */
  public Long createAS2DocTypeMapping(AS2DocTypeMapping documentTypeMapping)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;
  
  /**
   * Delete a AS2DocTypeMapping.
   *
   * @param AS2DocTypeMapping The UID of the AS2DocTypeMapping to delete.
   */
  public void deleteAS2DocTypeMapping(Long as2DocTypeMappingUId)
    throws DeleteEntityException, SystemException, RemoteException;
  
  /**
   * Update a AS2DocTypeMapping
   *
   * @param AS2DocTypeMapping The AS2DocTypeMapping entity with changes.
   */
  public void updateAS2DocTypeMapping(AS2DocTypeMapping documentTypeMapping)
    throws UpdateEntityException, SystemException, RemoteException;
  
  
}

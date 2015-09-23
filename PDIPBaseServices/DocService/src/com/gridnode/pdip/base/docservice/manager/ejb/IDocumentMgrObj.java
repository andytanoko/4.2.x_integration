/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentMgrObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.ejb;

import java.util.*;
import java.rmi.RemoteException;

import javax.ejb.*;

import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public interface IDocumentMgrObj extends EJBObject
{

    /**
     * Method to find the id of a domain
     */
    public long getDomainId(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Method to find the id of a folder, given the absolute path of the folder
     * The location syntax is <domain-name>/<folder-path>
     */
    public long getFolderId(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Method to find the id of a document, given the absolute path of the
     * document.
     * The location syntax is <domain-name>/<folder-path>/<document-name>
     */
    public long getDocumentId(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Method to find the id of a file, given the absolute path of the
     * file.
     * The location syntax is <domain-name>/<folder-path>/<file-name>
     */
    public long getFileId(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the domain specified
     * @param oldName The present name of the domain
     * @param newName The new name
     * @exception DocumentServiceException If there is no domain by the name
     */
    public Domain renameDomain(String oldName, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the domain specified by the id
     * @param id The id of the domain
     * @param newName The new name
     * @exception DocumentServiceException If there is no domain by the id
     */
    public Domain renameDomain(Long id, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Creates a folder by the given name under the domain specified
     * by the id
     * @param domainId The id of the domain
     * @param name The name of the folder
     * @exception DocumentServiceException If the domain does not exist or
     * if the domain already contains a folder by the name
     */
    public Folder createFolder(Long domainId, String name)
        throws DocumentServiceException, RemoteException;

    /**
     * Creates a subfolder by the given name under the folder specified
     * by the id
     * @param folderId The id of the parent folder
     * @param name The name of the subfolder
     * @exception DocumentServiceException If the parent folder does not
     * exist or if the parent already contains a subfolder by the name
     */
    public Folder createSubFolder(Long folderId, String name)
        throws DocumentServiceException, RemoteException;

    /**
     * Deleted the folder specified by the id
     * @param folderId The id of the folder
     * @exception DocumentServiceException If the folder does not exist
     */
    public void deleteFolder(Long folderId)
        throws DocumentServiceException, RemoteException;

    /**
     * Creates a document by the given name under the folder specified
     * by the id. The method creates the files by the same name as the
     * arguments supplied
     * @param name The name of the document
     * @param docType The type of the document
     * @param author The author of the document
     * @param folderId The id of the parent folder
     * @param main The main file of the document
     * @param attachments The attachments of the document
     * @exception DocumentServiceException If the folder does not exist or
     * if the folder already contains a document or file by the same name
     */
    public Document createDocument(String name, String docType, String author,
        Long folderId, java.io.File main, java.io.File[] attachments)
        throws DocumentServiceException, RemoteException;

    /**
     * Creates a document by the given name under the folder specified
     * by the id. The method creates the files by the names specified as
     * arguments
     * @param name The name of the document
     * @param docType The type of the document
     * @param author The author of the document
     * @param folderId The id of the parent folder
     * @param main The main file of the document
     * @param mainFileName The file name to be given for the main file
     * @param attachments The attachments of the document
     * @param attachmentFileNames The file names to be given for the attachment files
     * @exception DocumentServiceException If the folder does not exist or
     * if the folder already contains a document or file by the same name
     */
    public Document createDocument(String name, String docType, String author,
        Long folderId, java.io.File main, String mainFileName,
        java.io.File[] attachments, String[] attachmentFileNames)
        throws DocumentServiceException, RemoteException;

    /**
     * Deletes the document specified by the id
     * @param documentId The id of the document
     * @exception DocumentServiceException If the document does not exist
     */
    public void deleteDocument(Long documentId)
        throws DocumentServiceException, RemoteException;

    /**
     * Deletes the document specified by the path
     * @param documentPath The path of the document
     * @exception DocumentServiceException If the document does not exist
     */
    public void deleteDocument(String documentPath)
        throws DocumentServiceException, RemoteException;

    /**
     * Moves the folder specified by the folderId to another location
     * specified by the destFolderId
     * @param folderId The id of the folder
     * @param destFolderId The id of the destination folder
     * @exception DocumentServiceException If the folders specified by the
     * ids do not exist or if the destination folder contains a folder by the
     * same name
     */
    public Folder moveFolder(Long folderId, Long destFolderId)
        throws DocumentServiceException, RemoteException;

    /**
     * Moves the folder specified by the folderId to the parent domain
     * @param folderId The id of the folder
     * @param domainId The id of the domain
     * @exception DocumentServiceException If the folder specified by the
     * id does not exist or if the domain specified is not the parent domain
     */
    public Folder moveFolderToDomain(Long folderId, Long domainId)
        throws DocumentServiceException, RemoteException;

    /**
     * Moves the folder from one location to another.
     * @param src The location of the folder
     * @param dest The location of the destination
     * @exception DocumentServiceException If the folders specified by the
     * location do not exist or if the destination folder or domain contains a
     * folder by the same name
     */
    public Folder moveFolder(String src, String dest)
        throws DocumentServiceException, RemoteException;

    /**
     * Rename the folder specified by the id.
     * @param folderId The id of the folder
     * @param newName The new name
     * @exception DocumentServiceException If the folder does not exist or
     * if the folder by the new name alerady exists
     */
    public Folder renameFolder(Long folderId, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Rename the folder specified by the location.
     * @param oldPath The location of the folder
     * @param newName The new name
     * @exception DocumentServiceException If the folder does not exist or
     * if the folder by the new name already exists
     */
    public Folder renameFolder(String oldPath, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Moves the document specified by the documentId to the folder
     * specified by the folderId
     * @param documentId The id of the document
     * @param folderId The id of the destination folder
     * @exception DocumentServiceException If the document does not exist or
     * if the folder already contains a document or file by the same name
     */
    public Document moveDocument(Long documentId, Long folderId)
        throws DocumentServiceException, RemoteException;

    /**
     * Moves the document specified by the path to the sepcified folder
     * @param documentId The id of the document
     * @param folderId The id of the destination folder
     * @exception DocumentServiceException If the document does not exist or
     * if the folder already contains a document or file by the same name
     */
    public Document moveDocument(String docPath, String destPath)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the document specified by the id
     * @param documentId The id of the document
     * @param newName The new name
     * @exception DocumentServiceException If the document does not exist or
     * if a document by the new name already exists
     */
    public Document renameDocument(Long documentId, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the document specified by the path
     * @param docPath The path of the document
     * @param newName The new name
     * @exception DocumentServiceException If the document does not exist or
     * if a document by the new name already exists
     */
    public Document renameDocument(String docPath, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the file specified by the id
     * @param fileId The id of the file
     * @param newName The new name
     * @exception DocumentServiceException If the file does not exist or
     * if a file by the new name already exists
     */
    public File renameFile(Long fileId, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Renames the file specified by the path
     * @param filePath The path of the file
     * @param newName The new name
     * @exception DocumentServiceException If the file does not exist or
     * if a file by the new name already exists
     */
    public File renameFile(String filePath, String newName)
        throws DocumentServiceException, RemoteException;

    /**
     * Copies the folder specified by the folderId to a location
     * specified by the destFolderId
     * @param folderId The id of the folder
     * @param destFolderId The id of the destination folder
     * @exception DocumentServiceException If the folders specified by the
     * ids do not exist or if the destination folder contains a folder by the
     * same name
     */
    public Folder copyFolder(Long folderId, Long destFolderId)
        throws DocumentServiceException, RemoteException;

    /**
     * Copies the folder specified by the folderId to the parent domain
     * @param folderId The id of the folder
     * @param domainId The id of the domain
     * @exception DocumentServiceException If the folder specified by the
     * id does not exist or if the domain specified is not the parent domain
     */
    public Folder copyFolderToDomain(Long folderId, Long domainId)
        throws DocumentServiceException, RemoteException;

    /**
     * Copies the folder from one location to another.
     * @param srcFolderPath The location of the folder
     * @param destFolderPath The location of the destination
     * @exception DocumentServiceException If the folders specified by the
     * locations do not exist or if the destination folder or domain contains a
     * folder by the same name
     */
    public Folder copyFolder(String srcFolderPath, String destFolderPath)
        throws DocumentServiceException, RemoteException;

    /**
     * Copies the document specified by the documentId to a location
     * specified by the destFolderId
     * @param documentId The id of the document
     * @param destFolderId The id of the destination folder
     * @exception DocumentServiceException If the document and folder specified
     * by the ids do not exist or if the destination folder contains a document
     * by the same name
     */
    public Document copyDocument(Long documentId, Long destFolderId)
        throws DocumentServiceException, RemoteException;

    /**
     * Copies the document from one location to another.
     * @param docPath The location of the document
     * @param destPath The location of the destination
     * @exception DocumentServiceException If the document and folder specified
     * by the locations do not exist or if the destination folder contains a
     * document by the same name
     */
    public Document copyDocument(String docPath, String destPath)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the tree model of the file system
     */
    public DocumentTreeModel getDocumentTree()
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the file (java.io.File) specified by the location
     * @param location The file path
     * @exception DocumentServiceException If the file specified by the location
     * does not exist
     */
    public java.io.File getFile(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the file (java.io.File) specified by the id
     * @param fileId The id of the file
     * @exception DocumentServiceException If the file specified by the id
     * does not exist
     */
    public java.io.File getFile(Long fileId)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the collection of Files
     * (com.gridnode.pdip.base.docservice.filesystem.model.File)
     * present in the document specified by the id
     * @param documentId The id of the document
     * @exception DocumentServiceException If the document specified
     * by the id does not exist
     */
    public Collection getDocumentFiles(Long documentId)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the collection of Files
     * (com.gridnode.pdip.base.docservice.filesystem.model.File)
     * present in the document specified by the location
     * @param location The location of the document
     * @exception DocumentServiceException If the document and folder specified
     */
    public Collection getDocumentFiles(String location)
        throws DocumentServiceException, RemoteException;

    /**
     * Method to check whether the specified path exists in the file system.
     * The method will return true if a Domain or Folder or Document or File
     * matching the path exists
     * @param location The complete path
     */
    public boolean exists(String location) throws RemoteException;

    /**
     * Returns the Domains matching the specified filter
     */
    public Collection findDomainsByFilter(IDataFilter filter)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the Folders matching the specified filter
     */
    public Collection findFoldersByFilter(IDataFilter filter)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the Documents matching the specified filter
     */
    public Collection findDocumentsByFilter(IDataFilter filter)
        throws DocumentServiceException, RemoteException;

    /**
     * Returns the Files matching the specified filter
     */
    public Collection findFilesByFilter(IDataFilter filter)
        throws DocumentServiceException, RemoteException;
}

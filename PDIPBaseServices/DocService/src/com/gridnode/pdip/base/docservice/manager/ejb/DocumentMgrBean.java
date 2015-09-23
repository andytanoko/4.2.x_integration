/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentMgrBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.ejb;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.base.docservice.manager.helpers.DocumentMgrDelegate;
import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.exceptions.*;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class DocumentMgrBean implements SessionBean
{

    private SessionContext ctx;

    public void setSessionContext(SessionContext context)
    {
        ctx = context;
    }

    public void ejbActivate()
    {
    }

    public void ejbPassivate()
    {
    }

    public void ejbRemove()
    {
    }

    public void ejbCreate()
    {
    }

    public long getDomainId(String location) throws DocumentServiceException
    {
        if (location == null || location.equals(""))
        {
            throw new DocumentServiceException("location null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getDomainId(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public long getFolderId(String location) throws DocumentServiceException
    {
        if (location == null || location.equals(""))
        {
            throw new DocumentServiceException("location null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getFolderId(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public long getDocumentId(String location)
        throws DocumentServiceException
    {
        if (location == null || location.equals(""))
        {
            throw new DocumentServiceException("location null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getDocumentId(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public long getFileId(String location)
        throws DocumentServiceException
    {
        if (location == null || location.equals(""))
        {
            throw new DocumentServiceException("location null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getFileId(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Domain renameDomain(String oldName, String newName)
        throws DocumentServiceException
    {
        if (oldName == null || newName == null || newName.equals(""))
        {
            throw new DocumentServiceException("Name null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameDomain(oldName, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Domain renameDomain(Long id, String newName)
        throws DocumentServiceException
    {
        if (newName == null || newName.equals(""))
        {
            throw new DocumentServiceException("Name null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameDomain(id, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder createFolder(Long domainId, String name)
        throws DocumentServiceException
    {
        if (domainId == null || name == null || name.equals(""))
        {
            throw new DocumentServiceException("Name null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.createFolder(domainId, name);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public void deleteFolder(Long folderId)
        throws DocumentServiceException
    {
        if (folderId == null)
        {
            throw new DocumentServiceException("Folder id null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            DocumentMgrDelegate.deleteFolder(folderId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder createSubFolder(Long folderId, String name)
        throws DocumentServiceException
    {
        if (name == null || name.equals(""))
        {
            throw new DocumentServiceException("Name null",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.createSubFolder(folderId, name);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document createDocument(String name, String docType, String author,
        Long folderId, java.io.File main, java.io.File[] attachments)
        throws DocumentServiceException
    {
        if (name == null || folderId == null ||
            folderId.longValue() == 0 || main == null)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.createDocument(name, docType, author,
                folderId, main, attachments);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document createDocument(String name, String docType, String author,
        Long folderId, java.io.File main, String mainFileName,
        java.io.File[] attachments, String[] attachmentFileNames)
        throws DocumentServiceException
    {
        if (name == null || folderId == null ||
            folderId.longValue() == 0 || main == null ||
            mainFileName == null)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.createDocument(name, docType, author,
                folderId, main, mainFileName, attachments, attachmentFileNames);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public void deleteDocument(Long documentId) throws DocumentServiceException
    {
        if (documentId == null || documentId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid document id",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            DocumentMgrDelegate.deleteDocument(documentId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public void deleteDocument(String documentPath)
        throws DocumentServiceException
    {
        if (documentPath == null || documentPath.equals(""))
        {
            throw new DocumentServiceException("Invalid document path",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            DocumentMgrDelegate.deleteDocument(documentPath);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder moveFolder(Long folderId, Long destFolderId)
        throws DocumentServiceException
    {
        if (folderId == null || destFolderId == null ||
            folderId.longValue() == 0 || destFolderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.moveFolder(folderId, destFolderId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder moveFolderToDomain(Long folderId, Long domainId)
        throws DocumentServiceException
    {
        if (folderId == null || domainId == null ||
            folderId.longValue() == 0 || domainId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.moveFolderToDomain(folderId, domainId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder moveFolder(String src, String dest)
        throws DocumentServiceException
    {
        if (src == null || dest == null || src.equals("") ||
            dest.equals("") || src.equals(dest))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.moveFolder(src, dest);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder renameFolder(Long folderId, String newName)
        throws DocumentServiceException
    {
        if (folderId == null || newName == null ||
            folderId.longValue() == 0 || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameFolder(folderId, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder renameFolder(String oldPath, String newName)
        throws DocumentServiceException
    {
        if (oldPath == null || newName == null ||
            oldPath.equals("") || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameFolder(oldPath, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document moveDocument(Long documentId, Long folderId)
        throws DocumentServiceException
    {
        if (documentId == null || folderId == null ||
            documentId.longValue() == 0 || folderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.moveDocument(documentId, folderId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document moveDocument(String docPath, String destPath)
        throws DocumentServiceException
    {
        if (docPath == null || destPath == null ||
            docPath.equals("") || destPath.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.moveDocument(docPath, destPath);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document renameDocument(Long documentId, String newName)
        throws DocumentServiceException
    {
        if (documentId == null || newName == null ||
            documentId.longValue() == 0 || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameDocument(documentId, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document renameDocument(String docPath, String newName)
        throws DocumentServiceException
    {
        if (docPath == null || newName == null ||
            docPath.equals("") || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameDocument(docPath, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public File renameFile(Long fileId, String newName)
        throws DocumentServiceException
    {
        if (fileId == null || newName == null ||
            fileId.longValue() == 0 || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameFile(fileId, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public File renameFile(String filePath, String newName)
        throws DocumentServiceException
    {
        if (filePath == null || newName == null ||
            filePath.equals("") || newName.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.renameFile(filePath, newName);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder copyFolder(Long folderId, Long destFolderId)
        throws DocumentServiceException
    {
        if (folderId == null || destFolderId == null ||
            folderId.longValue() == 0 || destFolderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.copyFolder(folderId, destFolderId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder copyFolderToDomain(Long folderId, Long domainId)
        throws DocumentServiceException
    {
        if (folderId == null || domainId == null ||
            folderId.longValue() == 0 || domainId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.copyFolderToDomain(folderId, domainId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Folder copyFolder(String srcFolderPath, String destFolderPath)
        throws DocumentServiceException
    {
        if (srcFolderPath == null || destFolderPath == null ||
            srcFolderPath.equals("") || destFolderPath.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.copyFolder(srcFolderPath, destFolderPath);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document copyDocument(Long documentId, Long destFolderId)
        throws DocumentServiceException
    {
        if (documentId == null || destFolderId == null ||
            documentId.longValue() == 0 || destFolderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.copyDocument(documentId, destFolderId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Document copyDocument(String docPath, String destPath)
        throws DocumentServiceException
    {
        if (docPath == null || destPath == null ||
            docPath.equals("") || destPath.equals(""))
        {
            throw new DocumentServiceException("Invalid arguments supplied",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.copyDocument(docPath, destPath);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }
    public DocumentTreeModel getDocumentTree()
        throws DocumentServiceException
    {
        try
        {
            return DocumentMgrDelegate.getDocumentTree();
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public java.io.File getFile(String location)
        throws DocumentServiceException
    {
        if (location == null || location.trim().equals(""))
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getFile(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public java.io.File getFile(Long fileId)
        throws DocumentServiceException
    {
        if (fileId == null || fileId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getFile(fileId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Collection getDocumentFiles(Long documentId)
        throws DocumentServiceException
    {
        if (documentId == null || documentId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getDocumentFiles(documentId);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Collection getDocumentFiles(String location)
        throws DocumentServiceException
    {
        if (location == null || location.trim().equals(""))
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.getDocumentFiles(location);
        }
        catch (DocumentServiceException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public boolean exists(String location)
    {
        if (location == null || location.trim().equals(""))
        {
            return false;
        }
        return DocumentMgrDelegate.exists(location);
    }

    public Collection findDomainsByFilter(IDataFilter filter)
        throws DocumentServiceException
    {
        if (filter == null)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.findDomainsByFilter(filter);
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Collection findFoldersByFilter(IDataFilter filter)
        throws DocumentServiceException
    {
        if (filter == null)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.findFoldersByFilter(filter);
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Collection findDocumentsByFilter(IDataFilter filter)
        throws DocumentServiceException
    {
        if (filter == null)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.findDocumentsByFilter(filter);
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }

    public Collection findFilesByFilter(IDataFilter filter)
        throws DocumentServiceException
    {
        if (filter == null)
        {
            throw new DocumentServiceException("Invalid argument",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            return DocumentMgrDelegate.findFilesByFilter(filter);
        }
        catch (Throwable t)
        {
            throw new DocumentServiceException(t,
                DocumentServiceException.SYSTEM_ERROR);
        }
    }
}

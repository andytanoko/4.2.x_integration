/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentMgrDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import java.util.*;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.file.access.FileAccess;
import com.gridnode.pdip.framework.util.*;

import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.exceptions.*;

import com.gridnode.pdip.base.docservice.filesystem.tree.*;

public class DocumentMgrDelegate
{

    public static long getDomainId(String location) throws Throwable
    {
        location = formatPath(location);
        AbstractEntityHandler handler = DomainEntityHandler.getInstance();
        Collection c = findEntityByName(Domain.DOMAINNAME, location, handler);

        if (c == null || c.size() == 0)
        {
            throw new DocumentServiceException("Domain not found " + location,
                DocumentServiceException.SOURCE_NOT_FOUND);
        }
        else if (c.size() != 1)
        {
            throw new DocumentServiceException("Duplicate domains with " +
                "the same name " + location,
                DocumentServiceException.SYSTEM_ERROR);
        }

        Iterator it = c.iterator();
        Domain domain = (Domain) it.next();
        return domain.getUId();
    }

    public static long getFolderId(String location) throws Throwable
    {
        location = formatPath(location);
        StringTokenizer tokenizer = new StringTokenizer(location, "" + java.io.File.separatorChar);
        if (tokenizer.countTokens() == 1)
        {
            throw new DocumentServiceException("Wrong folder path: " + location,
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        String domainStr = tokenizer.nextToken();
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Collection c = findEntityByName(Domain.DOMAINNAME, domainStr, dHandler);

        if (c == null || c.size() == 0)
        {
            throw new DocumentServiceException("Domain not found " + domainStr,
                DocumentServiceException.SOURCE_NOT_FOUND);
        }
        else if (c.size() != 1)
        {
            throw new DocumentServiceException("Duplicate domains with " +
                "the same name " + domainStr, DocumentServiceException.SYSTEM_ERROR);
        }

        Iterator it = c.iterator();
        Domain domain = (Domain) it.next();
        Long domainId = new Long(domain.getUId());
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        long folderId = 0;
        while (tokenizer.hasMoreTokens())
        {
            String name = tokenizer.nextToken();
            Collection fc = findFoldersByNameAndIds(name, domainId,
                new Long(folderId));

            if (fc == null || fc.size() == 0)
            {
                throw new DocumentServiceException("Folder not found:: Name: " +
                    name + " Domain ID: " + domainId + " Folder ID: " + folderId,
                    DocumentServiceException.SOURCE_NOT_FOUND);
            }
            else if (fc.size() != 1)
            {
                throw new DocumentServiceException("Duplicate folders with " +
                    "the same name " + name, DocumentServiceException.SYSTEM_ERROR);
            }

            Iterator iterator = fc.iterator();
            Folder folder = (Folder) iterator.next();
            folderId = folder.getUId();
        }
        return folderId;
    }

    public static long getDocumentId(String docPath) throws Throwable
    {
        docPath = formatPath(docPath);
        int index = docPath.lastIndexOf(java.io.File.separatorChar);
        if (index == -1)
        {
            throw new DocumentServiceException("Wrong document path " + docPath,
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        String documentName = docPath.substring(index + 1);
        String folderPath = docPath.substring(0, index);
        long folderId = getFolderId(folderPath);

        Collection c = findDocumentsByNameAndIds(documentName,
            null, new Long(folderId));
        if (c == null || c.size() == 0)
        {
            throw new DocumentServiceException("Document not found:: Name: " +
                documentName + " Folder ID: " + folderId,
                DocumentServiceException.SOURCE_NOT_FOUND);
        }
        if (c.size() != 1)
        {
            throw new DocumentServiceException("Duplicate documents with " +
                "the same name exist " + documentName,
                DocumentServiceException.SYSTEM_ERROR);
        }
        Iterator it = c.iterator();
        Document document = (Document) it.next();
        return document.getUId();
    }

    public static long getFileId(String filePath) throws Throwable
    {
        filePath = formatPath(filePath);
        int index = filePath.lastIndexOf(java.io.File.separatorChar);
        if (index == -1)
        {
            throw new DocumentServiceException("Wrong file path: " + filePath,
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        String fileName = filePath.substring(index + 1);
        String folderPath = filePath.substring(0, index);
        long folderId = getFolderId(folderPath);

        Collection col = findFilesByNamesAndIds(new String[] { fileName },
            null, new Long(folderId), null);
        if (col == null || col.size() == 0)
        {
            throw new DocumentServiceException("No file found:: Name: " +
                fileName + " Folder ID: " + folderId,
                DocumentServiceException.SOURCE_NOT_FOUND);
        }
        if (col.size() != 1)
        {
            throw new DocumentServiceException("Duplicate files found " +
                fileName, DocumentServiceException.SYSTEM_ERROR);
        }
        Iterator it = col.iterator();
        File file = (File) it.next();
        return file.getUId();
    }

    public static Domain renameDomain(String oldName, String newName) throws Throwable
    {
        //Find the domain having the name
        AbstractEntityHandler handler = DomainEntityHandler.getInstance();
        Collection domainColl = findEntityByName(Domain.DOMAINNAME, oldName, handler);
        if (domainColl == null || domainColl.size() == 0)
        {
            throw new DocumentServiceException("Domain not found: " + oldName,
                DocumentServiceException.SOURCE_NOT_FOUND);
        }
        else if (domainColl.size() != 1)
        {
            throw new DocumentServiceException("Duplicate domains with " +
                "the same name: " + oldName, DocumentServiceException.SYSTEM_ERROR);
        }

        //Check if a domain with the name same as the new name already exists
        Collection c = null;
        try
        {
            c = findEntityByName(Domain.DOMAINNAME, newName, handler);
        }
        catch(Exception e)
        {
        }
        if (c != null && c.size() != 0)
        {
            throw new DocumentServiceException("Domain with the name " +
                "already exists: " + newName,
                DocumentServiceException.NAME_EXISTS);
        }

        Iterator iterator = domainColl.iterator();
        Domain domain = (Domain) iterator.next();
        domain.setDomainName(newName);
        handler.update(domain);
        //getDocumentTree().rename(DocumentTreeNode.DOMAIN_NODE,
        //    domain.getUId(), newName);
        return domain;
    }

    public static Domain renameDomain(Long id, String newName) throws Throwable
    {
        AbstractEntityHandler handler = DomainEntityHandler.getInstance();
        Domain domain = (Domain) handler.getEntityByKey(id);

        //Check if a domain with the name same as the new name already exists
        Collection c = null;
        try
        {
            c = findEntityByName(Domain.DOMAINNAME, newName, handler);
        }
        catch(Exception e)
        {
        }
        if (c != null && c.size() != 0)
        {
            throw new DocumentServiceException("Domain with the name " +
                "already exists: " + newName,
                DocumentServiceException.NAME_EXISTS);
        }

        domain.setDomainName(newName);
        handler.update(domain);
        //getDocumentTree().rename(DocumentTreeNode.DOMAIN_NODE,
        //    domain.getUId(), newName);
        return domain;
    }

    public static Folder createFolder(Long domainId, String name) throws Throwable
    {
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Domain domain = (Domain) dHandler.getEntityByKey(domainId);

        //Check if a folder with the same name already exists
        checkIfFolderExists(name, domainId, new Long(0));

        //Create the folder using FileManager
        FileManagerHandler.createFolder(domain.getPropertyName(), name);

        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        Folder folder = createFolder(name, domainId.longValue(), 0, 0,
            fHandler);
        domain.setChildCount(domain.getChildCount() + 1);
        dHandler.update(domain);
        //getDocumentTree().addChild(DocumentTreeNode.FOLDER_NODE,
        //    folder.getName(), folder.getUId(), folder.getDomainId(),
        //    folder.getParentId());
        return folder;
    }

    public static Folder createSubFolder(Long folderId, String name) throws Throwable
    {
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        Folder parent = (Folder) fHandler.getEntityByKey(folderId);

        //Check if a folder with the same name already exists
        checkIfFolderExists(name, new Long(parent.getDomainId()), folderId);

        //Create the folder using FileManager
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Domain domain = (Domain) dHandler.getEntityByKey(new Long(
            parent.getDomainId()));
        FileManagerHandler.createFolder(domain.getPropertyName(), getRelativePath(folderId) +
            java.io.File.separatorChar + name);

        Folder folder = createFolder(name, parent.getDomainId(),
            folderId.longValue(), 0, fHandler);
        parent.setChildCount(parent.getChildCount() + 1);
        fHandler.update(parent);
        //getDocumentTree().addChild(DocumentTreeNode.FOLDER_NODE,
        //    folder.getName(), folder.getUId(), folder.getDomainId(),
        //    folder.getParentId());
        return folder;
    }

    public static void deleteFolder(Long folderId) throws Throwable
    {
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();

        Folder folder = (Folder) fHandler.getEntityByKey(folderId);

        //Remove all children
        Collection children = findFoldersByNameAndIds(null, null, folderId);
        Iterator it = children.iterator();
        while (it.hasNext())
        {
            Folder child = (Folder) it.next();
            deleteFolder(new Long(child.getUId()));
        }
        children = findDocumentsByNameAndIds(null, null, folderId);
        it = children.iterator();
        while (it.hasNext())
        {
            Document child = (Document) it.next();
            deleteDocument(new Long(child.getUId()));
        }

        //Delete the folder using FileManager
        Domain domain = (Domain) dHandler.getEntityByKey(
            new Long(folder.getDomainId()));
        FileManagerHandler.deleteFolder(domain.getPropertyName(), getRelativePath(folderId));

        //Update child count of the parent
        if (folder.getParentId() == 0)
        {
            domain.setChildCount(domain.getChildCount() - 1);
            dHandler.update(domain);
        }
        else
        {
            Folder parent = (Folder) fHandler.getEntityByKey(
                new Long(folder.getParentId()));
            parent.setChildCount(parent.getChildCount() - 1);
            fHandler.update(parent);
        }

        //Remove the folder
        fHandler.remove(folderId);
        //getDocumentTree().deleteChild(DocumentTreeNode.FOLDER_NODE,
        //    folder.getUId());

    }

    private static Folder createFolder(String name, long domainId,
        long parentId, long childCount, AbstractEntityHandler handler)
        throws Throwable
    {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setDomainId(domainId);
        folder.setParentId(parentId);
        folder.setChildCount(childCount);
        handler.create(folder);
        return folder;
    }

    private static Document createDocument(String name, String author,
        int fileCount, String docType, long parentId, long domainId,
        long size, Date createdOn, Date accessed,
        AbstractEntityHandler docHandler) throws Throwable
    {
        Document document = new Document();
        document.setName(name);
        document.setAuthor(author);
        document.setFileCount(fileCount);
        document.setDocType(docType);
        document.setParentId(parentId);
        document.setDomainId(domainId);
        document.setSize(size);
        document.setCreatedOnDate(createdOn);
        document.setLastAccessedDate(accessed);
        docHandler.create(document);
        return document;
    }

    private static File createFile(String name, Boolean isMainFile, long docId,
        long parentId, long domainId, AbstractEntityHandler handler)
        throws Throwable
    {
        File file = new File();
        file.setName(name);
        file.setIsMainFile(isMainFile);
        file.setDocumentId(docId);
        file.setParentId(parentId);
        file.setDomainId(domainId);
        handler.create(file);
        return file;
    }

    private static Collection findFoldersByNamesAndIds(String[] names,
        Long domainId, Long folderId) throws Throwable
    {
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        IDataFilter filter = new DataFilterImpl();

        if (domainId != null)
        {
            filter.addSingleFilter(null, Folder.DOMAINID,
                filter.getEqualOperator(), domainId, false);
        }
        else
        {
            filter.addSingleFilter(null, Folder.DOMAINID,
                filter.getNotEqualOperator(), new Long(0), false);
        }
        if (folderId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), Folder.PARENTID,
                filter.getEqualOperator(), folderId, false);
        }
        if (names != null)
        {
            boolean flag = false;
            IDataFilter inner = new DataFilterImpl();
            for (int i = 0; i < names.length; i++)
            {
                if (names[i] != null)
                {
                    if (!flag)
                    {
                        inner.addSingleFilter(null, Folder.FOLDERNAME,
                            filter.getEqualOperator(), names[i], false);
                    }
                    else
                    {
                        inner.addSingleFilter(filter.getOrConnector(),
                            Folder.FOLDERNAME, filter.getEqualOperator(),
                            names[i], false);
                    }
                    flag = true;

                }
            }
            if (flag)
            {
                filter.addFilter(filter.getAndConnector(), inner);
            }
        }
        return fHandler.getEntityByFilter(filter);
    }

    //If folder exists, exception will be thrown
    private static void checkIfFolderExists(String name, Long domainId,
        Long folderId) throws Exception
    {
        checkIfDocumentExists(name, domainId, folderId);
        if (name == null)
        {
            return;
        }
        String[] temp = new String[1];
        temp[0] = name;
        Collection c = null;
        try
        {
            c = findFilesByNamesAndIds(temp, null, folderId, domainId);
        }
        catch(Throwable t)
        {
        }
        checkIfFileCollectionIsNotEmpty(c,
            "File(s) with the following name(s) already exist:");
    }

    //If document exists, exception will be thrown
    private static void checkIfDocumentExists(String name, Long domainId,
        Long folderId) throws Exception
    {
        Collection c = null;
        try
        {
            c = findDocumentsByNameAndIds(name, domainId, folderId);
        }
        catch(Throwable t)
        {
        }
        checkIfDocumentCollectionIsNotEmpty(c,
            "Document(s) with the following name(s) already exist:");
        try
        {
            c = findFoldersByNameAndIds(name, domainId, folderId);
        }
        catch(Throwable t)
        {
        }
        checkIfFolderCollectionIsNotEmpty(c,
            "Folder(s) with the following name(s) already exist:");
    }

    //If files exists, exception will be thrown
    private static void checkIfFileExists(String[] names, Long docId,
        Long folderId, Long domainId) throws Exception
    {
        Collection c = null;
        try
        {
            c = findFilesByNamesAndIds(names, docId, folderId, domainId);
        }
        catch(Throwable t)
        {
        }
        checkIfFileCollectionIsNotEmpty(c,
            "File(s) with the following name(s) already exist:");
        try
        {
            c = findFoldersByNamesAndIds(names, domainId, folderId);
        }
        catch(Throwable t)
        {
        }
        checkIfFolderCollectionIsNotEmpty(c,
            "Folder(s) with the following name(s) already exist:");
    }

    //If collection is not empty, exception will be thrown
    private static void checkIfFileCollectionIsNotEmpty(Collection c,
        String message) throws Exception
    {
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                File file = (File) it.next();
                message += " " + file.getName();
            }
            throw new DocumentServiceException(message,
                DocumentServiceException.NAME_EXISTS);
        }
    }

    //If collection is not empty, exception will be thrown
    private static void checkIfFolderCollectionIsNotEmpty(Collection c,
        String message) throws Exception
    {
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Folder folder = (Folder) it.next();
                message += " " + folder.getName();
            }
            throw new DocumentServiceException(message,
                DocumentServiceException.NAME_EXISTS);
        }
    }

    //If collection is not empty, exception will be thrown
    private static void checkIfDocumentCollectionIsNotEmpty(Collection c,
        String message) throws Exception
    {
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Document doc = (Document) it.next();
                message += " " + doc.getName();
            }
            throw new DocumentServiceException(message,
                DocumentServiceException.NAME_EXISTS);
        }
    }

    public static Document createDocument(String name, String docType,
        String author, Long folderId, java.io.File main,
        java.io.File[] attachments) throws Throwable
    {
        String[] attachmentNames = null;
        if (attachments != null)
        {
            attachmentNames = new String[attachments.length];
            for (int i = 0; i < attachments.length; i++)
            {
                attachmentNames[i] = attachments[i].getName();
            }
        }
        return createDocument(name, docType, author, folderId, main, main.getName(),
            attachments, attachmentNames);
    }

    public static Document createDocument(String name, String docType,
        String author, Long folderId, java.io.File main,
        String mainFileName, java.io.File[] attachments,
        String[] attachmentFileNames) throws Throwable
    {
        //check whether folder exists
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        Folder folder = (Folder) fHandler.getEntityByKey(folderId);
        //check whether document by the same name exists
        checkIfDocumentExists(name, null, folderId);

        //check whether any file exists
        String[] names  = new String[1];
        if (attachments != null)
        {
            names = new String[attachments.length + 1];
        }
        names[0] = mainFileName;
        if (attachments != null)
        {
            for (int i = 0; i < attachments.length; i++)
            {
                names[i + 1] = attachmentFileNames[i];
            }
        }
        checkIfFileExists(names, null, folderId, null);

        //create the files
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Domain domain = (Domain) dHandler.getEntityByKey(
            new Long(folder.getDomainId()));
        long size = FileManagerHandler.createDocument(domain.getPropertyName(),
            getRelativePath(folderId) + java.io.File.separatorChar,
            main, attachments, names);
        //update the parent's child count
        folder.setChildCount(folder.getChildCount() + 1);
        fHandler.update(folder);

        //create the document in database
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();
        Date date = new Date(System.currentTimeMillis());
        Document document = createDocument(name, author,
            attachments == null ? 1 : 1 + attachments.length,
            docType, folder.getUId(), domain.getUId(), size, date, date,
            docHandler);

        //create the files in database
        /*
        DocumentNode docNode =
            (DocumentNode) getDocumentTree().addChild(DocumentTreeNode.DOCUMENT_NODE,
            document.getName(), document.getUId(), document.getDomainId(),
            document.getParentId());
        docNode.setAuthor(author);
        docNode.setSize(size);
        docNode.setCreatedOnDate(date);
        docNode.setLastAccessedDate(date);
        */
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        File f = createFile(mainFileName, new Boolean(true), document.getUId(),
            folder.getUId(), domain.getUId(), fileHandler);
        /*
        FileNode fileNode = new FileNode(f.getName(), f.getUId());
        fileNode.setIsMainFile(f.getIsMainFile());
        docNode.addChild(fileNode);
        */
        if (attachments != null)
        {
            for (int i = 0; i < attachments.length; i++)
            {
                f = createFile(attachmentFileNames[i], new Boolean(false),
                    document.getUId(), folder.getUId(), domain.getUId(),
                    fileHandler);
                /*
                fileNode = new FileNode(f.getName(), f.getUId());
                fileNode.setIsMainFile(f.getIsMainFile());
                docNode.addChild(fileNode);
                */
            }
        }
        return document;
    }

    public static void deleteDocument(Long documentId) throws Throwable
    {
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        //Check if the Document with the specified id exists
        Document document = (Document) docHandler.getEntityByKey(documentId);
        Folder folder =
            (Folder) fHandler.getEntityByKey(new Long(document.getParentId()));

        //Remove all files in the document
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(document.getDomainId()));
        Collection children = findFilesByNamesAndIds(null, documentId, null, null);
        Iterator it = children.iterator();
        while (it.hasNext())
        {
            File file = (File) it.next();
            fileHandler.remove(new Long(file.getUId()));
        }
        String path = getRelativePath(new Long(document.getParentId()));
        FileManagerHandler.deleteDocument(children, domain.getPropertyName(),
            path + java.io.File.separatorChar);

        //Remove the document entry from database
        docHandler.remove(new Long(document.getUId()));

        //update child count of parent
        folder.setChildCount(folder.getChildCount() - 1);
        //getDocumentTree().deleteChild(DocumentTreeNode.DOCUMENT_NODE, document.getUId());
    }

    public static void deleteDocument(String docPath) throws Throwable
    {
        deleteDocument(new Long(getDocumentId(docPath)));
    }

    //Can't move a folder from one domain to another
    public static Folder moveFolder(Long folderId, Long destFolderId) throws Throwable
    {
        //check if both the folders exist
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        Folder folder = (Folder) fHandler.getEntityByKey(folderId);
        Folder destFolder = (Folder) fHandler.getEntityByKey(destFolderId);
        if (folder.getDomainId() != destFolder.getDomainId())
        {
            throw new DocumentServiceException("Cannot move a folder from " +
                "one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }
        Domain domain = (Domain) dHandler.getEntityByKey(new Long(folder.getDomainId()));

        //Check if the destination folder contains a folder of the same name
        checkIfFolderExists(folder.getName(), null, destFolderId);

        //move the folder. Have to check whether the dest is a
        //subfolder of source?
        String src = getRelativePath(folderId);
        String dest = getRelativePath(destFolderId);
        if (dest.indexOf(src) != -1)
        {
            throw new DocumentServiceException("Destination folder is a sub folder",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        FileManagerHandler.moveFolder(domain.getPropertyName(),
            src, dest + java.io.File.separatorChar + folder.getName());

        //The parent id of the folder, and the child count of the
        //parent should be changed.
        if (folder.getParentId() == 0)
        {
            domain.setChildCount(domain.getChildCount() - 1);
            dHandler.update(domain);
        }
        else
        {
            Folder parent =
                (Folder) fHandler.getEntityByKey(new Long(folder.getParentId()));
            parent.setChildCount(parent.getChildCount() - 1);
            fHandler.update(parent);
        }
        folder.setParentId(destFolder.getUId());
        fHandler.update(folder);
        destFolder.setChildCount(destFolder.getChildCount() + 1);
        fHandler.update(destFolder);

        /*
        DocumentTreeNode srcNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            folder.getUId());
        srcNode.deleteChild(DocumentTreeNode.FOLDER_NODE, folder.getUId());
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            destFolder.getUId());
        destNode.addChild(srcNode);
        */

        return folder;
    }

    public static Folder moveFolder(String source, String destination)
        throws Throwable
    {
        long folderId = getFolderId(source);
        boolean isDomain = false;
        long destFolderId = 0;
        long destDomainId = 0;
        try
        {
            destFolderId = getFolderId(destination);
        }
        catch (DocumentServiceException e)
        {
            destDomainId = getDomainId(destination);
            isDomain = true;
        }
        if (isDomain)
        {
            if (destDomainId == 0)
            {
                throw new DocumentServiceException("Invalid destination path: " +
                    destination, DocumentServiceException.ILLEGAL_ARGUMENTS);
            }
            return moveFolderToDomain(new Long(folderId), new Long(destDomainId));
        }
        else
        {
            if (destFolderId == 0)
            {
                throw new DocumentServiceException("Invalid destination path: " +
                    destination, DocumentServiceException.ILLEGAL_ARGUMENTS);
            }
            return moveFolder(new Long(folderId), new Long(destFolderId));
        }
    }

    public static Folder moveFolderToDomain(Long folderId, Long domainId)
        throws Throwable
    {
        //check if both the folders exist
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        Folder folder = (Folder) fHandler.getEntityByKey(folderId);
        if (folder.getDomainId() != domainId.longValue())
        {
            throw new DocumentServiceException("Cannot move a folder from " +
                "one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }
        if (folder.getParentId() == 0)
        {
            throw new DocumentServiceException("Nothing to move",
                DocumentServiceException.SAME_SOURCE_AND_DESTINATION);
        }
        Domain domain = (Domain) dHandler.getEntityByKey(domainId);

        //Check if the destination folder contains a folder of the same name
        checkIfFolderExists(folder.getName(), domainId, new Long(0));

        //move the folder
        FileManagerHandler.moveFolder(domain.getPropertyName(),
            getRelativePath(folderId), folder.getName());

        //The parent id of the folder, and the child count of the
        //parent should be changed.
        Folder parent =
            (Folder) fHandler.getEntityByKey(new Long(folder.getParentId()));
        parent.setChildCount(parent.getChildCount() - 1);
        fHandler.update(parent);
        folder.setParentId(0);
        fHandler.update(folder);
        domain.setChildCount(domain.getChildCount() + 1);
        dHandler.update(domain);

        /*
        DocumentTreeNode srcNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            folder.getUId());
        srcNode.deleteChild(DocumentTreeNode.FOLDER_NODE, folder.getUId());
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.DOMAIN_NODE,
            domain.getUId());
        destNode.addChild(srcNode);
        */
        return folder;
    }

    public static Folder renameFolder(Long folderId, String newName)
        throws Throwable
    {
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Folder folder = (Folder) fHandler.getEntityByKey(folderId);

        //Check whether a folder of the same name exists
        checkIfFolderExists(newName, new Long(folder.getDomainId()),
                new Long(folder.getParentId()));

        //rename the folder
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(folder.getDomainId()));
        FileManagerHandler.renameFolder(domain.getPropertyName(),
            getRelativePath(folderId), newName);

        //change the entry in the DB
        folder.setName(newName);
        fHandler.update(folder);

        //getDocumentTree().rename(DocumentTreeNode.FOLDER_NODE, folder.getUId(), newName);

        return folder;
    }

    public static Folder renameFolder(String oldPath, String newName)
        throws Throwable
    {
        oldPath = formatPath(oldPath);
        long folderId = getFolderId(oldPath);
        return renameFolder(new Long(folderId), newName);
    }

    public static Document moveDocument(Long documentId, Long folderId)
        throws Throwable
    {

        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        //Check if the document and the folder exists
        Document document = (Document) docHandler.getEntityByKey(documentId);
        Folder parent =
            (Folder) fHandler.getEntityByKey(new Long(document.getParentId()));
        Folder destFolder = (Folder) fHandler.getEntityByKey(folderId);
        if (document.getDomainId() != destFolder.getDomainId())
        {
            throw new DocumentServiceException("Cannot move a document " +
                "from one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }
        if (document.getParentId() == destFolder.getUId())
        {
            throw new DocumentServiceException("Nothing to move",
                DocumentServiceException.SAME_SOURCE_AND_DESTINATION);
        }
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(destFolder.getDomainId()));

        //Check if the Document having the same name exists in the destination
        //folder. Check if any of the files having the same name exists in
        //the destination folder
        checkIfDocumentExists(document.getName(), null, folderId);

        //check if the files exist
        Collection children = findFilesByNamesAndIds(null, documentId, null, null);
        Iterator it = children.iterator();
        String[] names = new String[children.size()];
        int i = 0;
        while (it.hasNext())
        {
            File file = (File) it.next();
            names[i] = file.getName();
        }

        checkIfFileExists(names, null, folderId, null);

        //Move the files
        String src = getRelativePath(new Long(document.getParentId())) + java.io.File.separatorChar;
        String dest = getRelativePath(new Long(destFolder.getUId())) + java.io.File.separatorChar;
        FileManagerHandler.moveDocument(domain.getPropertyName(), src, dest, children);

        //Update the DB entries
        parent.setChildCount(parent.getChildCount() - 1);
        fHandler.update(parent);
        destFolder.setChildCount(destFolder.getChildCount() + 1);
        fHandler.update(destFolder);
        it = children.iterator();
        while (it.hasNext())
        {
            File file = (File) it.next();
            file.setParentId(destFolder.getUId());
            fileHandler.update(file);
        }
        document.setParentId(destFolder.getUId());
        docHandler.update(document);

        /*
        DocumentTreeNode srcNode = getDocumentTree().getChild(DocumentTreeNode.DOCUMENT_NODE,
            document.getUId());
        srcNode.deleteChild(DocumentTreeNode.DOCUMENT_NODE, document.getUId());
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            destFolder.getUId());
        destNode.addChild(srcNode);
        */
        return document;
    }

    public static Document moveDocument(String documentPath, String destPath)
        throws Throwable
    {
        documentPath = formatPath(documentPath);
        destPath = formatPath(destPath);
        long documentId = getDocumentId(documentPath);
        long folderId = getFolderId(destPath);
        return moveDocument(new Long(documentId), new Long(folderId));
    }

    public static Document renameDocument(Long documentId, String newName)
        throws Throwable
    {
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();
        Document document = (Document) docHandler.getEntityByKey(documentId);

        checkIfDocumentExists(newName, null, new Long(document.getParentId()));

        document.setName(newName);
        docHandler.update(document);
        //getDocumentTree().rename(DocumentTreeNode.DOCUMENT_NODE, document.getUId(), newName);
        return document;
    }

    public static Document renameDocument(String docPath, String newName)
        throws Throwable
    {
        docPath = formatPath(docPath);
        long documentId = getDocumentId(docPath);
        return renameDocument(new Long(documentId), newName);
    }

    public static File renameFile(Long fileId, String newName)
        throws Throwable
    {
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        File file = (File) fileHandler.getEntityByKey(fileId);

        //Check whether a file with the same name exists
        checkIfFileExists(new String[] { newName }, null,
                new Long(file.getParentId()), null);

        //rename file
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(file.getDomainId()));
        String src = getRelativePath(new Long(file.getParentId())) + java.io.File.separatorChar +
            file.getName();
        FileManagerHandler.renameFile(domain.getPropertyName(), src, newName);

        //rename file in DB
        file.setName(newName);
        fileHandler.update(file);
        //getDocumentTree().rename(DocumentTreeNode.FILE_NODE, file.getUId(), newName);
        return file;
    }

    public static File renameFile(String oldPath, String newName)
        throws Throwable
    {
        oldPath = formatPath(oldPath);
        long fileId = getFileId(oldPath);
        return renameFile(new Long(fileId), newName);
    }

    public static Folder copyFolderToDomain(Long folderId, Long domainId)
        throws Throwable
    {
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        Folder folder = (Folder) fHandler.getEntityByKey(folderId);
        if (folder.getDomainId() != domainId.longValue())
        {
            throw new DocumentServiceException("Cannot copy a folder from " +
                "one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }
        if (folder.getParentId() == 0)
        {
            throw new DocumentServiceException("Nothing to copy",
                DocumentServiceException.SAME_SOURCE_AND_DESTINATION);
        }
        Domain domain = (Domain) dHandler.getEntityByKey(domainId);

        //Check if the destination folder contains a folder of the same name
        checkIfFolderExists(folder.getName(), domainId, new Long(0));

        //copy the folder
        FileManagerHandler.copyFolder(domain.getPropertyName(),
            getRelativePath(folderId), folder.getName());

        //Create the folder and all children in the DB
        //Create the folder, go recursively, get all subfolders and create them
        Folder parent = createFolder(folder.getName(), domain.getUId(), 0,
            folder.getChildCount(), fHandler);
        domain.setChildCount(domain.getChildCount() + 1);
        dHandler.update(domain);

        FolderNode parentNode = new FolderNode(parent.getName(), parent.getUId());

        if (folder.getChildCount() != 0)
        {
            copySubFolders(folder.getUId(), parent.getUId(),
                domain.getUId(), fHandler, docHandler, fileHandler, parentNode);
            copyDocuments(folder.getUId(), parent.getUId(),
                domain.getUId(), docHandler, fileHandler, parentNode);
        }
        /*
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.DOMAIN_NODE,
            domain.getUId());
        destNode.addChild(parentNode);
        */

        return parent;
    }

    public static Folder copyFolder(Long folderId, Long destFolderId)
        throws Throwable
    {
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        //Check if the folders exist and the domains are the same
        Folder folder = (Folder) fHandler.getEntityByKey(folderId);
        Folder destFolder = (Folder) fHandler.getEntityByKey(destFolderId);
        if (folder.getDomainId() != destFolder.getDomainId())
        {
            throw new DocumentServiceException("Cannot copy a folder from " +
                "one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }

        //Check if the folder having the same name is present in dest
        checkIfFolderExists(folder.getName(), null, destFolderId);

        //Copy the folder
        String src = getRelativePath(folderId);
        String dest = getRelativePath(destFolderId);
        if (dest.indexOf(src) != -1)
        {
            throw new DocumentServiceException("Destination folder is a sub folder",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(folder.getDomainId()));
        FileManagerHandler.copyFolder(domain.getPropertyName(),
            src, dest + java.io.File.separatorChar + folder.getName());

        //Create the folder and all children in the DB
        //Create the folder, go recursively, get all subfolders and create them
        Folder parent = createFolder(folder.getName(), domain.getUId(),
            destFolder.getUId(), folder.getChildCount(), fHandler);
        destFolder.setChildCount(destFolder.getChildCount() + 1);
        fHandler.update(destFolder);

        FolderNode parentNode = new FolderNode(parent.getName(), parent.getUId());

        if (folder.getChildCount() != 0)
        {
            copySubFolders(folder.getUId(), parent.getUId(),
                domain.getUId(), fHandler, docHandler, fileHandler, parentNode);
            copyDocuments(folder.getUId(), parent.getUId(),
                domain.getUId(), docHandler, fileHandler, parentNode);
        }
        /*
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            destFolder.getUId());
        destNode.addChild(parentNode);
        */

        return parent;
    }

    private static void copySubFolders(long srcFolderId, long destFolderId,
        long domainId, AbstractEntityHandler fHandler,
        AbstractEntityHandler docHandler,
        AbstractEntityHandler fileHandler, FolderNode parentNode) throws Throwable
    {
        Collection c = findFoldersByNameAndIds(null, null, new Long(srcFolderId));
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Folder folder = (Folder) it.next();
                Folder parent = createFolder(folder.getName(), domainId,
                    destFolderId, folder.getChildCount(), fHandler);
                FolderNode folderNode = new FolderNode(parent.getName(), parent.getUId());
                parentNode.addChild(folderNode);

                if (folder.getChildCount() != 0)
                {
                    copySubFolders(folder.getUId(), parent.getUId(),
                        domainId, fHandler, docHandler, fileHandler, folderNode);
                    copyDocuments(folder.getUId(), parent.getUId(),
                        domainId, docHandler, fileHandler, folderNode);
                }
            }
        }
    }

    private static void copyDocuments(long srcFolderId, long destFolderId,
        long domainId, AbstractEntityHandler docHandler,
        AbstractEntityHandler fileHandler, FolderNode parentNode) throws Throwable
    {
        Collection c = findDocumentsByNameAndIds(null, null, new Long(srcFolderId));
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Document src = (Document) it.next();
                Document document = createDocument(src.getName(),
                    src.getAuthor(), src.getFileCount(), src.getDocType(),
                    destFolderId, domainId, src.getSize(),
                    src.getCreatedOnDate(), src.getLastAccessedDate(),
                    docHandler);
                DocumentNode docNode = new DocumentNode(document.getName(), document.getUId());
                docNode.setSize(document.getSize());
                docNode.setAuthor(document.getAuthor());
                docNode.setCreatedOnDate(document.getCreatedOnDate());
                docNode.setLastAccessedDate(document.getLastAccessedDate());
                parentNode.addChild(docNode);

                copyFiles(src.getUId(), document.getUId(), destFolderId,
                    domainId, fileHandler, docNode);
            }
        }
    }

    private static void copyFiles(long srcDocId, long destDocId,
        long destFolderId, long domainId, AbstractEntityHandler fileHandler,
        DocumentNode docNode) throws Throwable
    {
        Collection c = findFilesByNamesAndIds(null, new Long(srcDocId), null, null);
        if (c != null && c.size() != 0)
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                File srcFile = (File) it.next();
                File file = createFile(srcFile.getName(), srcFile.getIsMainFile(),
                    destDocId, destFolderId, domainId, fileHandler);
                FileNode fileNode = new FileNode(file.getName(), srcFile.getUId());
                fileNode.setIsMainFile(file.getIsMainFile());
                docNode.addChild(fileNode);
            }
        }
    }

    public static Folder copyFolder(String srcPath, String destPath)
        throws Throwable
    {
        srcPath = formatPath(srcPath);
        destPath = formatPath(destPath);
        long folderId = getFolderId(srcPath);
        try
        {
            long destFolderId = getFolderId(destPath);
            return copyFolder(new Long(folderId), new Long(destFolderId));
        }
        catch (DocumentServiceException e)
        {
            if (e.getType() != DocumentServiceException.ILLEGAL_ARGUMENTS)
            {
                throw e;
            }
        }
        try
        {
            long destDomainId = getDomainId(destPath);
            return copyFolderToDomain(new Long(folderId), new Long(destDomainId));
        }
        catch (DocumentServiceException e)
        {
        }
        throw new DocumentServiceException("Unknown source or destination path",
            DocumentServiceException.ILLEGAL_ARGUMENTS);
    }

    public static Document copyDocument(Long documentId, Long destFolderId)
        throws Throwable
    {
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();

        //Check if the document and folder exist and the domains are the same
        Document document = (Document) docHandler.getEntityByKey(documentId);
        Folder destFolder = (Folder) fHandler.getEntityByKey(destFolderId);
        if (document.getDomainId() != destFolder.getDomainId())
        {
            throw new DocumentServiceException("Cannot move a folder from " +
                "one domain to another",
                DocumentServiceException.INTER_DOMAIN_TRANSFER);
        }

        //Check if the document having the same name is present in dest
        checkIfDocumentExists(document.getName(), null, destFolderId);

        //Check if any of the files are present in the destination
        Collection c = findFilesByNamesAndIds(null, documentId, null, null);
        if (c == null || c.size() == 0)
        {
            throw new DocumentServiceException("Files in the document not found",
                DocumentServiceException.SYSTEM_ERROR);
        }
        String[] names = new String[c.size()];
        Iterator it = c.iterator();
        int i = 0;
        while (it.hasNext())
        {
            File file = (File) it.next();
            names[i] = file.getName();
            i++;
        }

        checkIfFileExists(names, null, destFolderId, null);

        //Copy files
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(document.getDomainId()));
        String src = getRelativePath(new Long(document.getParentId())) +
            java.io.File.separatorChar;
        String dest = getRelativePath(destFolderId) +
            java.io.File.separatorChar;
        FileManagerHandler.copyDocument(domain.getPropertyName(), src, dest, c);

        //Create the document in DB
        Document cpDocument = createDocument(document.getName(),
            document.getAuthor(), document.getFileCount(),
            document.getDocType(), destFolderId.longValue(), domain.getUId(),
            document.getSize(), document.getCreatedOnDate(),
            document.getLastAccessedDate(), docHandler);
        destFolder.setChildCount(destFolder.getChildCount() + 1);
        fHandler.update(destFolder);

        it = c.iterator();
        FileNode fileNodes[] = new FileNode[c.size()];
        i = 0;
        while (it.hasNext())
        {
            File srcFile = (File) it.next();
            File file = createFile(srcFile.getName(), srcFile.getIsMainFile(),
                cpDocument.getUId(), destFolderId.longValue(), domain.getUId(),
                fileHandler);
            fileNodes[i] = new FileNode(file.getName(), file.getUId());
            fileNodes[i].setIsMainFile(file.getIsMainFile());
            i++;
        }

        //Should create a new DocumentNode, add all file nodes and add it to the
        //parent folder node
        /*
        DocumentNode docNode = new DocumentNode(cpDocument.getName(), cpDocument.getUId());
        docNode.setSize(cpDocument.getSize());
        docNode.setAuthor(cpDocument.getAuthor());
        docNode.setCreatedOnDate(cpDocument.getCreatedOnDate());
        docNode.setLastAccessedDate(cpDocument.getLastAccessedDate());
        for (i = 0; i < fileNodes.length; i++)
        {
            docNode.addChild(fileNodes[i]);
        }
        DocumentTreeNode destNode = getDocumentTree().getChild(DocumentTreeNode.FOLDER_NODE,
            destFolder.getUId());
        destNode.addChild(docNode);
        */

        return cpDocument;
    }

    public static Document copyDocument(String docPath, String destPath)
        throws Throwable
    {
        long documentId = getDocumentId(docPath);
        long destId = getFolderId(destPath);

        return copyDocument(new Long(documentId),
            new Long(destId));
    }

    public static Collection findEntityByName(Number field,
        String name, AbstractEntityHandler handler) throws Throwable
    {
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, field,
            filter.getEqualOperator(), name, false);
        return handler.getEntityByFilter(filter);
    }

    public static Collection findFoldersByNameAndIds(String name, Long domainId,
        Long parentId) throws Throwable
    {
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        IDataFilter filter = new DataFilterImpl();
        if (name != null)
        {
            filter.addSingleFilter(null, Folder.FOLDERNAME,
                filter.getEqualOperator(), name, false);
        }
        else
        {
            filter.addSingleFilter(null, Folder.FOLDERNAME,
                filter.getNotEqualOperator(), "", false);
        }
        if (domainId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), Folder.DOMAINID,
                filter.getEqualOperator(), domainId, false);
        }
        if (parentId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), Folder.PARENTID,
                filter.getEqualOperator(), parentId, false);
        }
        return fHandler.getEntityByFilter(filter);
    }

    public static Collection findDocumentsByNameAndIds(String name, Long domainId,
        Long parentId) throws Throwable
    {
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();
        IDataFilter filter = new DataFilterImpl();
        if (name != null)
        {
            filter.addSingleFilter(null, Document.DOCUMENTNAME,
                filter.getEqualOperator(), name, false);
        }
        else
        {
            filter.addSingleFilter(null, Document.DOCUMENTNAME,
                filter.getNotEqualOperator(), "", false);
        }
        if (domainId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), Document.DOMAINID,
                filter.getEqualOperator(), domainId, false);
        }
        if (parentId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), Document.PARENTID,
                filter.getEqualOperator(), parentId, false);
        }
        return docHandler.getEntityByFilter(filter);
    }

    private static Collection findFilesByNamesAndIds(String[] names, Long documentId,
        Long folderId, Long domainId) throws Throwable
    {
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        IDataFilter filter = new DataFilterImpl();

        if (documentId != null)
        {
            filter.addSingleFilter(null, File.DOCUMENTID,
                filter.getEqualOperator(), documentId, false);
        }
        else
        {
            filter.addSingleFilter(null, File.DOCUMENTID,
                filter.getNotEqualOperator(), new Long(0), false);
        }

        if (folderId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), File.PARENTID,
                filter.getEqualOperator(), folderId, false);
        }
        if (domainId != null)
        {
            filter.addSingleFilter(filter.getAndConnector(), File.DOMAINID,
                filter.getEqualOperator(), domainId, false);
        }
        if (names != null)
        {
            boolean flag = false;
            IDataFilter inner = new DataFilterImpl();
            for (int i = 0; i < names.length; i++)
            {
                if (names[i] != null)
                {
                    if (!flag)
                    {
                        inner.addSingleFilter(null, File.FILENAME,
                            filter.getEqualOperator(), names[i], false);
                    }
                    else
                    {
                        inner.addSingleFilter(filter.getOrConnector(),
                            File.FILENAME, filter.getEqualOperator(),
                            names[i], false);
                    }
                    flag = true;

                }
            }
            if (flag)
            {
                filter.addFilter(filter.getAndConnector(), inner);
            }
        }
        return fileHandler.getEntityByFilter(filter);
    }

    public static String getFolderPath(Long folderId) throws Throwable
    {
        String path = "";
        Folder folder = null;
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        if (folderId == null || folderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid folder id",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        while (folderId.longValue() != 0)
        {
            folder = (Folder) fHandler.getEntityByKey(folderId);
            path = java.io.File.separatorChar + folder.getName() + path;
            folderId = new Long(folder.getParentId());
        }

        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        Domain domain = (Domain) dHandler.getEntityByKey(
            new Long(folder.getDomainId()));
        return java.io.File.separatorChar + domain.getPropertyName() + path;
    }

    private static String getRelativePath(Long folderId) throws Throwable
    {
        String path = "";
        Folder folder = null;
        AbstractEntityHandler fHandler = FolderEntityHandler.getInstance();
        if (folderId == null || folderId.longValue() == 0)
        {
            throw new DocumentServiceException("Invalid folder id",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        while (folderId.longValue() != 0)
        {
            folder = (Folder) fHandler.getEntityByKey(folderId);
            if (path.equals(""))
            {
                path = folder.getName();
            }
            else
            {
                path = folder.getName() + java.io.File.separatorChar + path;
            }
            folderId = new Long(folder.getParentId());
        }
        return path;
    }

    private static String formatPath(String location) throws Throwable
    {
        if (java.io.File.separatorChar == '/')
        {
            location = location.replace('\\', '/');
        }
        else
        {
            location = location.replace('/', java.io.File.separatorChar);
        }
        if (location.endsWith("" + java.io.File.separatorChar))
        {
            location = location.substring(0, location.length() - 1);
        }
        return location;
    }

    public static DocumentTreeModel getDocumentTree() throws Throwable
    {
        //DocumentTreeModel treeModel = DocumentTreeModel.getInstance();
        DocumentTreeModel treeModel = new DocumentTreeModel("Repository");
        /*
        if (treeModel.getChildren().size() != 0)
        {
            return treeModel;
        }
        */
        HashMap domainMap = new HashMap();
        HashMap folderMap = new HashMap();
        HashMap docMap = new HashMap();
        HashMap fileMap = new HashMap();
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, Domain.DOMAINNAME,
            filter.getNotEqualOperator(), "", false);
        Collection col = findDomainsByFilter(filter);
        if (col == null || col.size() == 0)
        {
            return treeModel;
        }
        Iterator it = col.iterator();
        while (it.hasNext())
        {
            Domain domain = (Domain) it.next();
            DomainNode dNode = new DomainNode(domain.getDomainName(),
                domain.getUId());
            treeModel.addChild(dNode);
            domainMap.put(new Long(domain.getUId()), dNode);
        }

        //Find all folders
        col = findFoldersByNameAndIds(null, null, null);
        if (col == null || col.size() == 0)
        {
            return treeModel;
        }
        it = col.iterator();
        while (it.hasNext())
        {
            Folder folder = (Folder) it.next();
            FolderNode fNode = new FolderNode(folder.getName(),
                folder.getUId());
            if (folder.getParentId() == 0)
            {
                DomainNode domain =
                    (DomainNode) domainMap.get(new Long(folder.getDomainId()));
                domain.addChild(fNode);
            }
            else
            {
                fNode.setParentId(folder.getParentId());
            }
            folderMap.put(new Long(folder.getUId()), fNode);
        }
        it = folderMap.values().iterator();
        while (it.hasNext())
        {
            FolderNode fNode = (FolderNode) it.next();
            if (fNode.getParentId() != 0)
            {
                FolderNode parent =
                    (FolderNode) folderMap.get(new Long(fNode.getParentId()));
                parent.addChild(fNode);
            }
        }

        //Find all documents
        col = findDocumentsByNameAndIds(null, null, null);
        if (col == null || col.size() == 0)
        {
            return treeModel;
        }
        it = col.iterator();
        while (it.hasNext())
        {
            Document document = (Document) it.next();
            DocumentNode docNode = new DocumentNode(document.getName(),
                document.getUId());
            docNode.setSize(document.getSize());
            docNode.setAuthor(document.getAuthor());
            docNode.setCreatedOnDate(document.getCreatedOnDate());
            docNode.setLastAccessedDate(document.getLastAccessedDate());
            FolderNode fNode =
                (FolderNode) folderMap.get(new Long(document.getParentId()));
            fNode.addChild(docNode);
            docMap.put(new Long(document.getUId()), docNode);
        }

        //Find all files
        col = findFilesByNamesAndIds(null, null, null, null);
        if (col == null || col.size() == 0)
        {
            throw new DocumentServiceException("No files found in " +
                "the documents", DocumentServiceException.SYSTEM_ERROR);
        }
        it = col.iterator();
        while (it.hasNext())
        {
            File file = (File) it.next();
            FileNode fileNode = new FileNode(file.getName(), file.getUId());
            fileNode.setIsMainFile(file.getIsMainFile());
            DocumentNode docNode =
                (DocumentNode) docMap.get(new Long(file.getDocumentId()));
            docNode.addChild(fileNode);
        }
        return treeModel;
    }

    public static java.io.File getFile(String location)
        throws Throwable
    {
        location = formatPath(location);
        long fileId = getFileId(location);
        return getFile(new Long(fileId));
    }

    public static java.io.File getFile(Long fileId)
        throws Throwable
    {
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        AbstractEntityHandler dHandler = DomainEntityHandler.getInstance();
        File file = (File) fileHandler.getEntityByKey(fileId);
        Domain domain =
            (Domain) dHandler.getEntityByKey(new Long(file.getDomainId()));
        String relPath = getRelativePath(new Long(file.getParentId()));
        return FileManagerHandler.getFile(domain.getPropertyName(),
            relPath + java.io.File.separatorChar + file.getName());
    }

    public static Collection getDocumentFiles(Long documentId)
        throws Throwable
    {
        return findFilesByNamesAndIds(null, documentId, null, null);
    }

    public static Collection getDocumentFiles(String documentPath)
        throws Throwable
    {
        long docId = getDocumentId(documentPath);
        return getDocumentFiles(new Long(docId));
    }

    public static boolean exists(String location)
    {
        try
        {
            long id = getDomainId(location);
            return true;
        }
        catch (Throwable t)
        {
        }
        try
        {
            long id = getFolderId(location);
            return true;
        }
        catch (Throwable t)
        {
        }
        try
        {
            long id = getDocumentId(location);
            return true;
        }
        catch (Throwable t)
        {
        }
        try
        {
            long id = getFileId(location);
            return true;
        }
        catch (Throwable t)
        {
            return false;
        }
    }

    public static Collection findFilesByFilter(IDataFilter filter)
        throws Throwable
    {
        AbstractEntityHandler fileHandler = FileEntityHandler.getInstance();
        return fileHandler.getEntityByFilter(filter);
    }

    public static Collection findDocumentsByFilter(IDataFilter filter)
        throws Throwable
    {
        AbstractEntityHandler docHandler = DocumentEntityHandler.getInstance();
        return docHandler.getEntityByFilter(filter);
    }

    public static Collection findFoldersByFilter(IDataFilter filter)
        throws Throwable
    {
        AbstractEntityHandler folderHandler = FolderEntityHandler.getInstance();
        return folderHandler.getEntityByFilter(filter);
    }

    public static Collection findDomainsByFilter(IDataFilter filter)
        throws Throwable
    {
        AbstractEntityHandler domainHandler = DomainEntityHandler.getInstance();
        return domainHandler.getEntityByFilter(filter);
    }
}

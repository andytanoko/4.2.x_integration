/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileManagerHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 15 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import java.util.*;

import com.gridnode.pdip.base.docservice.filesystem.model.File;
import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.framework.file.access.FileAccess;

public class FileManagerHandler
{
    public static void createFolder(String domainName, String folderName) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String path = fileAccess.createFolder(folderName);
        if (path == null)
        {
            throw new DocumentServiceException("Folder not created!!",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static void deleteFolder(String domainName, String folderName) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String path = fileAccess.deleteFolder(folderName);
        if (path == null)
        {
            throw new DocumentServiceException("Folder not deleted!!",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static long createDocument(String domainName, String path,
        java.io.File main, java.io.File[] attachments) throws Throwable
    {
        long size = main.length();
        FileAccess fileAccess = new FileAccess(domainName);
        if (fileAccess.createFile(path + main.getName(), main, true) == null)
        {
            throw new DocumentServiceException("File could not be created",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }

        if (attachments != null)
        {
            for (int i = 0; i < attachments.length; i++)
            {
                size += attachments[i].length();
                if (fileAccess.createFile(path + attachments[i].getName(),
                    attachments[i], true) == null)
                {
                    throw new DocumentServiceException("File could not be created",
                        DocumentServiceException.FILE_MANAGER_ERROR);
                }
            }
        }
        return size;
    }

    public static long createDocument(String domainName, String path,
        java.io.File main, java.io.File[] attachments, String[] fileNames)
        throws Throwable
    {
        long size = main.length();
        FileAccess fileAccess = new FileAccess(domainName);
        if (fileAccess.createFile(path + fileNames[0], main, true) == null)
        {
            throw new DocumentServiceException("File could not be created",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
        if (attachments != null)
        {
            for (int i = 0; i < attachments.length; i++)
            {
                size += attachments[i].length();
                if (fileAccess.createFile(path + fileNames[i + 1],
                    attachments[i], true) == null)
                {
                    throw new DocumentServiceException("File could not be created",
                        DocumentServiceException.FILE_MANAGER_ERROR);
                }
            }
        }
        return size;
    }

    public static void deleteDocument(Collection files, String domainName,
        String path) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        Iterator it = files.iterator();
        while (it.hasNext())
        {
            File file = (File) it.next();
            String result = fileAccess.deleteFile(path + file.getName());
            if (result == null)
            {
                throw new DocumentServiceException("File could not be deleted",
                    DocumentServiceException.FILE_MANAGER_ERROR);
            }
        }
    }

    public static void moveFolder(String domainName, String src,
        String dest) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String result = fileAccess.moveFolder(src, dest, true);
        if (result == null)
        {
            throw new DocumentServiceException("Could not move the folder",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static void renameFolder(String domainName, String path,
        String newName) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String result = fileAccess.renameFolder(path, newName);
        if (result == null)
        {
            throw new DocumentServiceException("Could not rename the folder",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static void moveDocument(String domainName, String srcPath,
        String destPath, Collection files) throws Throwable
    {
        Iterator it = files.iterator();
        FileAccess fileAccess = new FileAccess(domainName);
        while (it.hasNext())
        {
            File file = (File) it.next();
            String result = fileAccess.moveFile(srcPath + file.getName(),
                destPath + file.getName(), true);
            if (result == null)
            {
                throw new DocumentServiceException("Could not move the files",
                    DocumentServiceException.FILE_MANAGER_ERROR);
            }
        }
    }

    public static void renameFile(String domainName, String path, String newName) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String result = fileAccess.renameFile(path, newName);
        if (result == null)
        {
            throw new DocumentServiceException("File could not be renamed",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static void copyFolder(String domainName, String srcPath,
        String destPath) throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        String result = fileAccess.copyFolder(srcPath, destPath, true);
        if (result == null)
        {
            throw new DocumentServiceException("Could not copy the folder",
                DocumentServiceException.FILE_MANAGER_ERROR);
        }
    }

    public static void copyDocument(String domainName, String srcPath,
        String destPath, Collection files) throws Throwable
    {
        Iterator it = files.iterator();
        FileAccess fileAccess = new FileAccess(domainName);
        while (it.hasNext())
        {
            File file = (File) it.next();
            String result = fileAccess.copyFile(srcPath + file.getName(),
                destPath + file.getName(), true);
            if (result == null)
            {
                throw new DocumentServiceException("File could not be copied",
                    DocumentServiceException.FILE_MANAGER_ERROR);
            }
        }
    }

    public static java.io.File getFile(String domainName, String path)
        throws Throwable
    {
        FileAccess fileAccess = new FileAccess(domainName);
        return fileAccess.getFile(path);
    }
}

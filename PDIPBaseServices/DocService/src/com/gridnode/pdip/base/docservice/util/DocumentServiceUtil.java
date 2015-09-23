/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentServiceUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.util;

import com.gridnode.pdip.base.docservice.manager.ejb.*;
import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.base.docservice.view.*;
import com.gridnode.pdip.base.docservice.action.*;
import com.gridnode.pdip.base.docservice.upload.FileUploadUtil;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.gridnode.pdip.base.docservice.upload.*;

public class DocumentServiceUtil
{

    public static TreeView getTreeView(int filterLevel) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        DocumentTreeModel treeModel = remote.getDocumentTree();
        return new TreeView(treeModel, filterLevel);
    }

    private static IDocumentMgrObj getRemote() throws Exception
    {
        IDocumentMgrHome mgrHome =
            (IDocumentMgrHome) ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
            IDocumentMgrHome.class);
        return mgrHome.create();
    }

    public static DocumentTreeModel getTreeModel() throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.getDocumentTree();
    }

    public static Action[] getRequiredActions(HttpServletRequest request)
        throws Exception
    {
        if (request.getMethod().equals("POST") &&
            request.getContentType().indexOf("multipart/form-data") != -1)
        {
            DSMultipartRequest mp =
                FileUploadUtil.getMultipartRequest(request);
            String actionType = mp.getParameter("actionType");
            String parentId = mp.getParameter("parentId");
            String parentNodeType = mp.getParameter("parentNodeType");
            String documentName = mp.getParameter("nodeName");
            String docType = mp.getParameter("docType");
            String author = mp.getParameter("author");
            if (actionType == null || !actionType.equals("adddocument") ||
                parentId == null || parentNodeType == null ||
                documentName == null || documentName.trim().equals(""))
            {
                return null;
            }
            int folderId = Integer.parseInt(parentId);
            int nodeType = Integer.parseInt(parentNodeType);
            if (folderId == 0 || nodeType != DocumentTreeNode.FOLDER_NODE)
            {
                return null;
            }
            String mainFileName = null;
            String[] attachmentFileNames = null;
            java.io.File main = null;
            java.io.File[] attachments = null;
            Enumeration files = mp.getFileNames();
            mainFileName = mp.getOriginalFileName("mainFile");
            if (mainFileName == null)
            {
                return null;
            }
            main = mp.getFile("mainFile");
            int i = 0;
            while (files.hasMoreElements())
            {
                String str =
                    mp.getOriginalFileName((String) files.nextElement());
                if (str != null && !str.trim().equals(""))
                {
                    i++;
                }
            }
            attachmentFileNames = new String[i - 1];
            attachments = new java.io.File[i - 1];

            i = 0;
            files = mp.getFileNames();
            while (files.hasMoreElements())
            {
                String str = (String) files.nextElement();
                if (str.startsWith("attachment"))
                {
                    if (mp.getOriginalFileName(str) != null
                        && !mp.getOriginalFileName(str).trim().equals(""))
                    {
                        attachments[i] = mp.getFile(str);
                        attachmentFileNames[i] = mp.getOriginalFileName(str).trim();
                        i++;
                    }
                }
            }
            Action[] actions = new AddDocumentAction[1];
            actions[0] = new AddDocumentAction(nodeType, folderId,
                documentName, docType, author, main, mainFileName,
                attachments, attachmentFileNames);
            return actions;
        }
        String actionType = request.getParameter("actionType");
        if (actionType == null)
        {
            return null;
        }
        Enumeration enum = request.getParameterNames();
        Vector v = new Vector();
        while (enum.hasMoreElements())
        {
            String str = (String) enum.nextElement();
            if (str.startsWith("check"))
            {
                v.add(request.getParameter(str));
            }
        }
        HttpSession session = request.getSession(true);
        if (actionType.equals("cut"))
        {
            Action[] actions = new Action[v.size()];
            for (int i = 0; i < v.size(); i++)
            {
                String str = (String) v.get(i);
                int index = str.indexOf(';');
                int type = Integer.parseInt(str.substring(0, index));
                long id = Long.parseLong(str.substring(index + 1));
                actions[i] = new CutAction(type, id);
            }
            session.removeAttribute("cut");
            session.removeAttribute("copy");
            session.setAttribute("cut", actions);
            return actions;
        }
        else if (actionType.equals("copy"))
        {
            Action[] actions = new Action[v.size()];
            for (int i = 0; i < v.size(); i++)
            {
                String str = (String) v.get(i);
                int index = str.indexOf(';');
                int type = Integer.parseInt(str.substring(0, index));
                long id = Long.parseLong(str.substring(index + 1));
                actions[i] = new CopyAction(type, id);
            }
            session.removeAttribute("cut");
            session.removeAttribute("copy");
            session.setAttribute("copy", actions);
            return actions;
        }
        else if (actionType.equals("paste"))
        {
            int nodeType = Integer.parseInt(request.getParameter("nodeType"));
            if (nodeType == DocumentTreeNode.DOMAIN_NODE ||
                nodeType == DocumentTreeNode.FOLDER_NODE)
            {
                long id = Long.parseLong(request.getParameter("id"));
                Action[] actions = new Action[1];
                Action[] first = null;
                first = (Action[]) session.getAttribute("cut");
                if (first == null)
                {
                    first = (Action[]) session.getAttribute("copy");
                }
                if (first == null)
                {
                    return null;
                }
                actions[0] = new PasteAction(nodeType, id, first);
                return actions;
            }
            return null;
        }
        else if (actionType.equals("delete"))
        {
            if (v.size() == 0)
            {
                return null;
            }
            Action[] actions = new Action[v.size()];
            for (int i = 0; i < v.size(); i++)
            {
                String str = (String) v.get(i);
                int index = str.indexOf(';');
                int type = Integer.parseInt(str.substring(0, index));
                long id = Long.parseLong(str.substring(index + 1));
                actions[i] = new DeleteAction(type, id);
            }
            return actions;
        }
        else if (actionType.equals("rename"))
        {
            if (v.size() != 1)
            {
                return null;
            }
            String newName = request.getParameter("newNodeName");
            if (newName == null || newName.trim().equals(""))
            {
                return null;
            }
            Action[] actions = new Action[1];
            String str = (String) v.get(0);
            int index = str.indexOf(';');
            int type = Integer.parseInt(str.substring(0, index));
            long id = Long.parseLong(str.substring(index + 1));
            actions[0] = new RenameAction(type, id, newName.trim());
            return actions;
        }
        else if (actionType.equals("createfolder"))
        {
            String newName = request.getParameter("newNodeName");
            if (newName == null || newName.trim().equals(""))
            {
                return null;
            }
            Action[] actions = new Action[1];
            int nodeType = Integer.parseInt(request.getParameter("nodeType"));
            if (nodeType != DocumentTreeNode.DOMAIN_NODE &&
                nodeType != DocumentTreeNode.FOLDER_NODE)
            {
                return null;
            }
            long id = Long.parseLong(request.getParameter("id"));
            actions[0] = new CreateFolderAction(nodeType, id, newName.trim());
            return actions;
        }
        return null;
    }

    public static Document moveDocument(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.moveDocument(new Long(srcId), new Long(destId));
    }

    public static Document copyDocument(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.copyDocument(new Long(srcId), new Long(destId));
    }

    public static Folder moveFolder(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.moveFolder(new Long(srcId), new Long(destId));
    }

    public static Folder moveFolderToDomain(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.moveFolderToDomain(new Long(srcId), new Long(destId));
    }

    public static Folder copyFolder(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.copyFolder(new Long(srcId), new Long(destId));
    }

    public static Folder copyFolderToDomain(long srcId, long destId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        return remote.copyFolderToDomain(new Long(srcId), new Long(destId));
    }

    public static void deleteFolder(long srcId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.deleteFolder(new Long(srcId));
    }

    public static void deleteDocument(long srcId) throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.deleteDocument(new Long(srcId));
    }

    public static void renameDomain(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.renameDomain(new Long(srcId), newName);
    }

    public static void renameFolder(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.renameFolder(new Long(srcId), newName);
    }

    public static void renameDocument(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.renameDocument(new Long(srcId), newName);
    }

    public static void renameFile(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.renameFile(new Long(srcId), newName);
    }

    public static void createSubFolder(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.createSubFolder(new Long(srcId), newName);
    }

    public static void createFolder(long srcId, String newName)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.createFolder(new Long(srcId), newName);
    }

    public static void createDocument(String name, String type, String author,
        long folderId, java.io.File main, String mainFileName,
        java.io.File[] attachments, String[] attachmentFileNames)
        throws Exception
    {
        IDocumentMgrObj remote = getRemote();
        remote.createDocument(name, type, author, new Long(folderId), main,
            mainFileName, attachments, attachmentFileNames);
    }
}

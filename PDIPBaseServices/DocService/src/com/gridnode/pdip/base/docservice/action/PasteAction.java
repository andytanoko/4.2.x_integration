/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PasteAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002   Girish R         Created
 */

package com.gridnode.pdip.base.docservice.action;

import com.gridnode.pdip.base.docservice.filesystem.model.*;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.base.docservice.util.*;
import com.gridnode.pdip.base.docservice.exceptions.*;

public class PasteAction extends Action
{

    private Action[] actions;

    public PasteAction(int nodeType, long id, Action[] actions)
    {
        super(Action.PASTE, nodeType, id);
        this.actions = actions;
    }

    public void doAction() throws Exception
    {
        if (actions == null || actions.length == 0)
        {
            throw new DocumentServiceException("Nothing to paste",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }

        if (nodeType != DocumentTreeNode.DOMAIN_NODE &&
            nodeType != DocumentTreeNode.FOLDER_NODE)
        {
            throw new DocumentServiceException("Cannot paste",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        for (int i = 0; i < actions.length; i++)
        {
            Action action = actions[i];
            if (action.getNodeType() == DocumentTreeNode.DOCUMENT_NODE &&
                nodeType == DocumentTreeNode.DOMAIN_NODE)
            {
                throw new DocumentServiceException("Cannot copy a document to domain",
                    DocumentServiceException.DOCUMENT_PARENT);
            }
            if (action.getNodeType() == DocumentTreeNode.DOCUMENT_NODE)
            {
                if (action.getActionType() == Action.CUT)
                {
                    Document document = DocumentServiceUtil.moveDocument(action.getId(), id);
                    if (document == null)
                    {
                        throw new DocumentServiceException("Could not move the document",
                            DocumentServiceException.SYSTEM_ERROR);
                    }
                }
                else if (action.getActionType() == Action.COPY)
                {
                    Document document = DocumentServiceUtil.copyDocument(action.getId(), id);
                    if (document == null)
                    {
                        throw new DocumentServiceException("Could not copy the document",
                            DocumentServiceException.SYSTEM_ERROR);
                    }
                }
            }
            else if (action.getNodeType() == DocumentTreeNode.FOLDER_NODE)
            {
                if (action.getActionType() == Action.CUT)
                {
                    if (nodeType == DocumentTreeNode.DOMAIN_NODE)
                    {
                        Folder folder =
                            DocumentServiceUtil.moveFolderToDomain(action.getId(), id);
                        if (folder == null)
                        {
                            throw new DocumentServiceException("Could not move the folder",
                                DocumentServiceException.SYSTEM_ERROR);
                        }
                    }
                    else
                    {
                        Folder folder = DocumentServiceUtil.moveFolder(action.getId(), id);
                        if (folder == null)
                        {
                            throw new DocumentServiceException("Could not move the folder",
                                DocumentServiceException.SYSTEM_ERROR);
                        }
                    }
                }
                else if (action.getActionType() == Action.COPY)
                {
                    if (nodeType == DocumentTreeNode.DOMAIN_NODE)
                    {
                        Folder folder =
                            DocumentServiceUtil.copyFolderToDomain(action.getId(), id);
                        if (folder == null)
                        {
                            throw new DocumentServiceException("Could not copy the folder",
                                DocumentServiceException.SYSTEM_ERROR);
                        }
                    }
                    else
                    {
                        Folder folder = DocumentServiceUtil.copyFolder(action.getId(), id);
                        if (folder == null)
                        {
                            throw new DocumentServiceException("Could not copy the folder",
                                DocumentServiceException.SYSTEM_ERROR);
                        }
                    }
                }
            }
        }
    }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CopyAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.action;

import com.gridnode.pdip.base.docservice.filesystem.tree.DocumentTreeNode;
import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.util.DocumentServiceUtil;

public class CreateFolderAction extends Action
{

    private String newName;

    public CreateFolderAction(int nodeType, long id, String newName)
    {
        super(Action.CREATE_FOLDER, nodeType, id);
        this.newName = newName;
    }

    public void doAction() throws Exception
    {
        if (newName == null || newName.trim().equals("") ||
            nodeType == DocumentTreeNode.FILE_NODE ||
            nodeType == DocumentTreeNode.DOCUMENT_NODE)
        {
            throw new DocumentServiceException("Invalid arguments",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        if (nodeType == DocumentTreeNode.DOMAIN_NODE)
        {
            DocumentServiceUtil.createFolder(id, newName);
        }
        else if (nodeType == DocumentTreeNode.FOLDER_NODE)
        {
            DocumentServiceUtil.createSubFolder(id, newName);
        }
    }

}

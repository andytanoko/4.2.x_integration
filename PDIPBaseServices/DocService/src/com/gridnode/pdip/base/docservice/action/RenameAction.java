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

public class RenameAction extends Action
{

    private String newName;

    public RenameAction(int nodeType, long id, String newName)
    {
        super(Action.RENAME, nodeType, id);
        this.newName = newName;
    }

    public void doAction() throws Exception
    {
        if (newName == null || newName.trim().equals(""))
        {
            throw new DocumentServiceException("Invalid arguments",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        if (nodeType == DocumentTreeNode.DOMAIN_NODE)
        {
            DocumentServiceUtil.renameDomain(id, newName);
        }
        else if (nodeType == DocumentTreeNode.FOLDER_NODE)
        {
            DocumentServiceUtil.renameFolder(id, newName);
        }
        else if (nodeType == DocumentTreeNode.DOCUMENT_NODE)
        {
            DocumentServiceUtil.renameDocument(id, newName);
        }
        else if (nodeType == DocumentTreeNode.FILE_NODE)
        {
            DocumentServiceUtil.renameFile(id, newName);
        }
    }

}

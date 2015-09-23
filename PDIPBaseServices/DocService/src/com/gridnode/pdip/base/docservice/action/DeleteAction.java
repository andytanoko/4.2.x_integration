/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.action;

import com.gridnode.pdip.base.docservice.util.DocumentServiceUtil;
import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;

public class DeleteAction extends Action
{

    public DeleteAction(int nodeType, long id)
    {
        super(Action.DELETE, nodeType, id);
    }

    public void doAction() throws Exception
    {
        if (nodeType == DocumentTreeNode.FOLDER_NODE)
        {
            DocumentServiceUtil.deleteFolder(id);
        }
        else if (nodeType == DocumentTreeNode.DOCUMENT_NODE)
        {
            DocumentServiceUtil.deleteDocument(id);
        }
        else
        {
            throw new DocumentServiceException("Delete action not supported",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
    }

}

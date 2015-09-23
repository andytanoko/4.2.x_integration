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

import java.io.File;

import com.gridnode.pdip.base.docservice.filesystem.tree.DocumentTreeNode;
import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.util.DocumentServiceUtil;

public class AddDocumentAction extends Action
{

    private String name;
    private String docType;
    private String author;
    private java.io.File main;
    private String mainFileName;
    private java.io.File[] attachments;
    private String[] attachmentFileNames;

    public AddDocumentAction(int nodeType, long id, String name, String docType,
        String author, File main, String mainFileName, File[] attachments,
        String[] attachmentFileNames)
    {
        super(Action.ADD_DOCUMENT, nodeType, id);
        this.name = name;
        this.docType = docType;
        this.author = author;
        this.main = main;
        this.mainFileName = mainFileName;
        this.attachments = attachments;
        this.attachmentFileNames = attachmentFileNames;
    }

    public void doAction() throws Exception
    {
        if (name == null || name.trim().equals("") ||
            nodeType == DocumentTreeNode.FILE_NODE ||
            nodeType == DocumentTreeNode.DOMAIN_NODE ||
            nodeType == DocumentTreeNode.DOCUMENT_NODE)
        {
            throw new DocumentServiceException("Invalid arguments",
                DocumentServiceException.ILLEGAL_ARGUMENTS);
        }
        try
        {
            DocumentServiceUtil.createDocument(name, docType, author, id, main,
                mainFileName, attachments, attachmentFileNames);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try
            {
                main.delete();
                for (int i = 0; i < attachments.length; i++)
                {
                    attachments[i].delete();
                }
            }
            catch (Exception e)
            {
            }
        }
    }

}

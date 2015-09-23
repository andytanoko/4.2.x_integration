/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FolderNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

public class FolderNode extends DocumentTreeNode
{

    private long parentId = 0;

    public FolderNode(String name, long id)
    {
        super(DocumentTreeNode.FOLDER_NODE, name, id);
    }

    public long getParentId()
    {
        return this.parentId;
    }

    public void setParentId(long parentId)
    {
        this.parentId = parentId;
    }

    public int getLevel()
    {
        if (this.parent instanceof DomainNode)
        {
            return 2;
        }
        else
        {
            return this.parent.getLevel() + 1;
        }
    }

    public DocumentTreeNode addChild(int type, String name, long id,
        long domainId, long parentId)
    {
        if (type == DocumentTreeNode.FOLDER_NODE)
        {
            if (this.getId() == parentId)
            {
                DocumentTreeNode treeNode = new FolderNode(name, id);
                addChild(treeNode);
                return treeNode;
            }
        }
        else if (type == DocumentTreeNode.DOCUMENT_NODE)
        {
            if (this.getId() == parentId)
            {
                DocumentTreeNode treeNode = new DocumentNode(name, id);
                addChild(treeNode);
                return treeNode;
            }
        }
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                DocumentTreeNode child = treeNode.addChild(type, name, id, domainId, parentId);
                if (child != null)
                {
                    return child;
                }
            }
        }
        return null;
    }

    public DocumentTreeNode deleteChild(int type, long id)
    {
        if (type == DocumentTreeNode.FOLDER_NODE && id == this.getId())
        {
            parent.deleteChild(this);
            return this;
        }
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                DocumentTreeNode child = treeNode.deleteChild(type, id);
                if (child != null)
                {
                    return child;
                }
            }
        }
        return null;
    }
}

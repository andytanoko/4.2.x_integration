/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomainNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

public class DomainNode extends DocumentTreeNode
{

    public DomainNode(String name, long id)
    {
        super(DocumentTreeNode.DOMAIN_NODE, name, id);
    }

    public void setParent(DocumentTreeNode parent)
    {
    }

    public DocumentTreeNode getParent()
    {
        return null;
    }

    public String getCanonicalPath()
    {
        return this.name;
    }

    public int getLevel()
    {
        return 1;
    }

    public DocumentTreeNode addChild(int type, String name, long id,
        long domainId, long parentId)
    {
        if (parentId == 0 &&
            type == DocumentTreeNode.FOLDER_NODE)
        {
            if (domainId == this.id)
            {
                DocumentTreeNode treeNode = new FolderNode(name, id);
                addChild(treeNode);
                return treeNode;
            }
            return null;
        }
        else
        {
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
    }

    public DocumentTreeNode deleteChild(int type, long id)
    {
        if (type == DocumentTreeNode.DOMAIN_NODE ||
            type == DocumentTreeNode.FILE_NODE)
        {
            return null;
        }
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                DocumentTreeNode delNode = treeNode.deleteChild(type, id);
                if (delNode != null)
                {
                    return delNode;
                }
            }
        }
        return null;
    }
}

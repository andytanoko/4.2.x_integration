/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RootNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 27 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

public class RootNode extends DocumentTreeNode
{

    public RootNode(String name)
    {
        super(DocumentTreeNode.ROOT_NODE, name, 0);
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
        return 0;
    }

    public DocumentTreeNode addChild(int type, String name, long id,
        long domainId, long parentId)
    {
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DomainNode dNode = (DomainNode) children.get(i);
                DocumentTreeNode treeNode = dNode.addChild(type, name, id,
                    domainId, parentId);
                if (treeNode != null)
                {
                    return treeNode;
                }
            }
        }
        return null;
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

    public boolean rename(int type, long id, String newName)
    {
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                if (treeNode.rename(type, id, newName))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /*public DocumentTreeNode getChild(int type, long id)
    {
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                DocumentTreeNode child = treeNode.getChild(type, id);
                if (child != null)
                {
                    return child;
                }
            }
        }
        return null;
    }*/
}

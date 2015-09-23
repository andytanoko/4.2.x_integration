/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTreeNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

import java.util.Vector;
import java.util.Collection;
import java.io.Serializable;

/**
 * Each node in the file system tree
 */
public abstract class DocumentTreeNode implements Serializable
{
    public static int ROOT_NODE = -1;
    public static int DOMAIN_NODE = 0;
    public static int FOLDER_NODE = 1;
    public static int DOCUMENT_NODE = 2;
    public static int FILE_NODE = 3;

    protected int type;
    protected String name;
    protected long id;
    protected Vector children;
    protected DocumentTreeNode parent;

    public DocumentTreeNode(int type, String name, long id)
    {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void addChild(DocumentTreeNode child)
    {
        child.setParent(this);
        if (children == null)
        {
            children = new Vector();
        }
        children.add(child);
    }

    public void deleteChild(DocumentTreeNode treeNode)
    {
        if (children != null)
        {
            children.remove(treeNode);
        }
    }

    public Collection getChildren()
    {
        if (children == null)
        {
            return new Vector();
        }
        return children;
    }

    public void setParent(DocumentTreeNode parent)
    {
        this.parent = parent;
    }

    public DocumentTreeNode getParent()
    {
        return this.parent;
    }

    public String getCanonicalPath()
    {
        return this.parent.getCanonicalPath() + '/' +
            this.name;
    }

    public abstract int getLevel();

    public abstract DocumentTreeNode addChild(int type, String name, long id,
        long domainId, long parentId);

    public abstract DocumentTreeNode deleteChild(int type, long id);

    /**
     * To rename a node.
     * Checks whether the specified node is the current node. If not
     * checks whether the node to be renamed is any of the child nodes
     * or the sub-child nodes
     */
    public boolean rename(int type, long id, String newName)
    {
        if (type == this.getType() && id == this.getId())
        {
            this.name = newName;
            return true;
        }
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

    /**
     * Its assumed that the parent of this node calls this method.
     * If the specified node corresponds to the current node, the current
     * node is returned. Otherwise child and sub-child nodes are checked
     */
    public DocumentTreeNode getChild(int type, long id)
    {
        if (type == this.getType() && id == this.getId())
        {
            return this;
        }
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) children.get(i);
                DocumentTreeNode childNode = treeNode.getChild(type, id);
                if (childNode != null)
                {
                    return childNode;
                }
            }
        }
        return null;
    }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TreeNodeView.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 15 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.view;

import java.util.*;

import com.gridnode.pdip.base.docservice.filesystem.tree.*;

public class TreeNodeView
{

    private TreeNodeView currentNode;
    private Vector children;
    private DocumentTreeNode treeNode;
    private int current = -1;

    public TreeNodeView(DocumentTreeNode treeNode)
    {
        this.treeNode = treeNode;
        Collection c = treeNode.getChildren();
        if (c != null)
        {
            children = new Vector();
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                DocumentTreeNode subNode = (DocumentTreeNode) it.next();
                TreeNodeView nodeView =
                    new TreeNodeView(subNode);
                children.add(nodeView);
            }
        }
    }

    public DocumentTreeNode getDocumentTreeNode()
    {
        return treeNode;
    }

    public TreeNodeView getNextNode()
    {
        if (hasNextNode())
        {
            if (current == -1)
            {
                current = 0;
                this.currentNode = (TreeNodeView) children.get(current);
                return this.currentNode;
            }
            if (currentNode.hasNextNode())
            {
                return currentNode.getNextNode();
            }
            if (current < children.size() - 1)
            {
                current++;
                this.currentNode = (TreeNodeView) children.get(current);
                return this.currentNode;
            }
            return null;
        }
        return null;
    }

    public boolean hasNextNode()
    {
        if (children != null && children.size() != 0)
        {
            if (current == -1)
            {
                return true;
            }
            if (currentNode.hasNextNode())
            {
                return true;
            }
            if (current < children.size() - 1)
            {
                return true;
            }
            return false;
        }
        return false;
    }

    public void reset()
    {
        current = -1;
        if (children != null)
        {
            for (int i = 0; i < children.size(); i++)
            {
                TreeNodeView nodeView = (TreeNodeView) children.get(i);
                nodeView.reset();
            }
        }
    }
}

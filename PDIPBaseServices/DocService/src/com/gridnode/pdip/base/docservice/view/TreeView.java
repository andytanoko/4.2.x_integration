/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TreeView.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 15 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.view;

import java.util.*;

import com.gridnode.pdip.base.docservice.filesystem.tree.*;

/**
 * TreeView to use the DocumentTreeModel. The view provides methods to
 * walk the TreeModel and filter it. Each child in the tree view is
 * a TreeNodeView which in turn contains other TreeNodeView's
 */
public class TreeView
{

    private TreeNodeView currentNodeView = null;
    private DocumentTreeNode nextNode = null;
    private int current = -1;

    private DocumentTreeModel treeModel;
    private int filterLevel = FILE_FILTER;
    private Vector children;

    public static int FOLDER_FILTER = 1;
    public static int DOCUMENT_FILTER = 2;
    public static int FILE_FILTER = 3;

    public TreeView(DocumentTreeModel treeModel, int filterLevel)
    {
        this.treeModel = treeModel;
        this.filterLevel = filterLevel;
        if (this.filterLevel < FOLDER_FILTER || this.filterLevel > FILE_FILTER)
        {
            this.filterLevel = FILE_FILTER;
        }
        Collection c = treeModel.getChildren();
        if (c != null)
        {
            children = new Vector();
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                DocumentTreeNode treeNode = (DocumentTreeNode) it.next();
                TreeNodeView nodeView =
                    new TreeNodeView(treeNode);
                children.add(nodeView);
            }
        }
    }

    public String getName()
    {
        return treeModel.getName();
    }

    public boolean hasNextNode()
    {
        if (children != null && children.size() != 0)
        {
            if (current == -1)
            {
                return true;
            }
            return nextNode != null;
        }
        else
        {
            return false;
        }
    }

    private TreeNodeView getFilteredTreeNode(TreeNodeView nodeView)
    {
        while (nodeView.hasNextNode())
        {
            if (filterLevel == FILE_FILTER)
            {
                return nodeView.getNextNode();
            }
            if (filterLevel == DOCUMENT_FILTER)
            {
                TreeNodeView temp = (TreeNodeView) nodeView.getNextNode();
                if (!(temp.getDocumentTreeNode() instanceof FileNode))
                {
                    return temp;
                }
            }
            if (filterLevel == FOLDER_FILTER)
            {
                TreeNodeView temp = (TreeNodeView) nodeView.getNextNode();
                if (!(temp.getDocumentTreeNode() instanceof FileNode) &&
                    !(temp.getDocumentTreeNode() instanceof DocumentNode))
                {
                    return temp;
                }
            }
        }
        return null;
    }

    public DocumentTreeNode getNextNode()
    {
        if (hasNextNode())
        {
            if (current == -1)
            {
                current++;
                currentNodeView = (TreeNodeView) children.get(current);
                DocumentTreeNode treeNode = currentNodeView.getDocumentTreeNode();
                TreeNodeView nextNodeView = getFilteredTreeNode(currentNodeView);
                if (nextNodeView == null)
                {
                    nextNode = null;
                    if (current < children.size() - 1)
                    {
                        currentNodeView = (TreeNodeView) children.get(++current);
                        nextNode = currentNodeView.getDocumentTreeNode();
                    }
                }
                else
                {
                    nextNode = nextNodeView.getDocumentTreeNode();
                }
                return treeNode;
            }
            if (nextNode == null)
            {
                return null;
            }
            if (currentNodeView.hasNextNode())
            {
                DocumentTreeNode treeNode = nextNode;
                TreeNodeView nextNodeView = getFilteredTreeNode(currentNodeView);
                if (nextNodeView == null)
                {
                    nextNode = null;
                    if (current < children.size() - 1)
                    {
                        currentNodeView = (TreeNodeView) children.get(++current);
                        nextNode = currentNodeView.getDocumentTreeNode();
                    }
                }
                else
                {
                    nextNode = nextNodeView.getDocumentTreeNode();
                }
                return treeNode;
            }
            else
            {
                DocumentTreeNode treeNode = nextNode;
                nextNode = null;
                if (current < children.size() - 1)
                {
                    currentNodeView = (TreeNodeView) children.get(++current);
                    nextNode = currentNodeView.getDocumentTreeNode();
                }
                return treeNode;
            }
        }
        else
        {
            return null;
        }
    }
}

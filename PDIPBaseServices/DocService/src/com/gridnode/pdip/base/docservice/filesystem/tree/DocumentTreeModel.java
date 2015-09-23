/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTreeModel.java
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
 * The file system tree model.
 * Each child in the tree model is a DocumentTreeNode. DocumentTreeModel contains
 * a RootNode which has DomainNodes as children. DomainNode contains FolderNodes
 * which in turn contains DocumentNodes and FolderNodes as children. DocumentNodes
 * contains FileNodes
 */
public class DocumentTreeModel implements Serializable
{

    //private static DocumentTreeModel treeModel = new DocumentTreeModel("Repository");
    private RootNode rootNode;

    public DocumentTreeModel(String rootName)
    {
        rootNode = new RootNode(rootName);
    }

    /*
    public static DocumentTreeModel getInstance()
    {
        return treeModel;
    }
    */

    public String getName()
    {
        return this.rootNode.getName();
    }

    public synchronized void addChild(DomainNode child)
    {
        this.rootNode.addChild(child);
    }

    public Collection getChildren()
    {
        return this.rootNode.getChildren();
    }

    public  synchronized DocumentTreeNode addChild(int type, String name,
        long id, long domainId, long parentId)
    {
        return this.rootNode.addChild(type, name, id, domainId, parentId);
    }

    public synchronized DocumentTreeNode deleteChild(int type, long id)
    {
        return this.rootNode.deleteChild(type, id);
    }

    public synchronized boolean rename(int type, long id, String newName)
    {
        return this.rootNode.rename(type, id, newName);
    }

    public synchronized DocumentTreeNode getChild(int type, long id)
    {
        return this.rootNode.getChild(type, id);
    }
}

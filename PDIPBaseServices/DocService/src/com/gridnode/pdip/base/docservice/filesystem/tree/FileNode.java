/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

public class FileNode extends DocumentTreeNode
{

    private Boolean isMainFile = new Boolean(false);

    public FileNode(String name, long id)
    {
        super(DocumentTreeNode.FILE_NODE, name, id);
    }

    public void addChild(DocumentTreeNode child)
    {
    }

    public Boolean getIsMainFile()
    {
        return isMainFile;
    }

    public void setIsMainFile(Boolean isMainFile)
    {
        this.isMainFile = isMainFile;
    }

    public int getLevel()
    {
        return this.parent.getLevel() + 1;
    }

    public DocumentTreeNode addChild(int type, String name, long id,
        long domainId, long parentId)
    {
        return null;
    }

    public DocumentTreeNode deleteChild(int type, long id)
    {
        return null;
    }

    public String getCanonicalPath()
    {
        return this.parent.getParent().getCanonicalPath() + '/' +
            this.name;
    }
}

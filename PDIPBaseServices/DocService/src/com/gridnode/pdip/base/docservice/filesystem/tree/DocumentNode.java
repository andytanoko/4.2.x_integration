/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.tree;

import java.util.Date;

public class DocumentNode extends DocumentTreeNode
{

    private long size;
    private String author = "";
    private Date createdOn;
    private Date lastAccessed;

    public DocumentNode(String name, long id)
    {
        super(DocumentTreeNode.DOCUMENT_NODE, name, id);
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public long getSize()
    {
        return this.size;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setCreatedOnDate(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public void setLastAccessedDate(Date lastAccessed)
    {
        this.lastAccessed = lastAccessed;
    }

    public Date getCreatedOnDate()
    {
        return createdOn;
    }

    public Date getLastAccessedDate()
    {
        return lastAccessed;
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
        if (type == DocumentTreeNode.DOCUMENT_NODE && id == this.getId())
        {
            parent.deleteChild(this);
            return this;
        }
        return null;
    }
}

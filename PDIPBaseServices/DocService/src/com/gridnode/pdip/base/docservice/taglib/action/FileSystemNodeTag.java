/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileSystemNodeTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.action;

import java.util.Collection;

import javax.servlet.jsp.JspTagException;

import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.base.docservice.util.*;

public class FileSystemNodeTag extends AbstractActionTag
{

    private int nodeType;
    private long id;
    private Collection c;

    public void setNodeType(String sNodeType)
    {
        if (sNodeType == null)
        {
            this.nodeType = 0;
        }
        else
        {
            this.nodeType = Integer.parseInt(sNodeType);
        }
    }

    public void setId(String sId)
    {
        if (sId == null)
        {
            this.id = 0;
        }
        else
        {
            this.id = Long.parseLong(sId);
        }
    }

    public int doStartTag() throws JspTagException
    {
        try
        {
            DocumentTreeModel treeModel = DocumentServiceUtil.getTreeModel();
            DocumentTreeNode treeNode = treeModel.getChild(nodeType, id);
            if (treeNode == null)
            {
                result = NO_DATA_FOUND;
            }
            else
            {
                c = treeNode.getChildren();
                if (c == null || c.size() == 0)
                {
                    result = NO_DATA_FOUND;
                }
            }
        }
        catch (DocumentServiceException e)
        {
            result = e.getType();
            message = e.getMessage();
        }
        catch (Exception e)
        {
            result = FAILURE;
            message = e.getMessage();
        }
        return EVAL_BODY_INCLUDE;

    }

    public Collection getChildren()
    {
        return c;
    }
}

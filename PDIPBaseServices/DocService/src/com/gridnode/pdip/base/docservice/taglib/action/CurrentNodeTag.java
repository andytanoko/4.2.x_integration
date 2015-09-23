/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CurrentNodeTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.action;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;
import com.gridnode.pdip.base.docservice.filesystem.tree.*;
import com.gridnode.pdip.base.docservice.util.*;

public class CurrentNodeTag extends TagSupport
{

    private int nodeType;
    private long id;
    private String name;

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

    public void setName(String name)
    {
        this.name = name;
    }

    public int doStartTag() throws JspTagException
    {
        try
        {
            DocumentTreeModel treeModel = DocumentServiceUtil.getTreeModel();
            DocumentTreeNode treeNode = treeModel.getChild(nodeType, id);
            if (treeNode == null)
            {
                return SKIP_BODY;
            }
            pageContext.setAttribute(name, treeNode);
            return EVAL_BODY_INCLUDE;
        }
        catch (Exception e)
        {
            return EVAL_BODY_INCLUDE;
        }
    }
}

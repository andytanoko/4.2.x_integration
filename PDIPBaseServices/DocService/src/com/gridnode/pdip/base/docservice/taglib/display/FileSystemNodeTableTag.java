/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileSystemNodeTableTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.display;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;

import com.gridnode.pdip.base.docservice.taglib.action.*;


public class FileSystemNodeTableTag extends AbstractCollectionTag
{
    public int doStartTag() throws JspTagException
    {

        AbstractActionTag actionTag = getParentTag();

        if (actionTag.getResult() == AbstractActionTag.SUCCESS)
        {
            if (actionTag instanceof FileSystemNodeTag)
            {
                FileSystemNodeTag nodeTag = (FileSystemNodeTag) actionTag;
                c = nodeTag.getChildren();
                it = c.iterator();
            }
            else
            {
                throw new JspTagException("This tag should be used only as a child tag");
            }
        }
        else
        {
            return SKIP_BODY;
        }
        return super.doStartTag();
    }
}

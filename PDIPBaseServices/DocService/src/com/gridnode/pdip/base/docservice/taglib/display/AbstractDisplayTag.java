/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractDisplayTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.display;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;

import com.gridnode.pdip.base.docservice.taglib.action.AbstractActionTag;


public class AbstractDisplayTag extends TagSupport
{


    protected AbstractActionTag getParentTag() throws JspTagException
    {
        AbstractActionTag actionTag =
            (AbstractActionTag) TagSupport.findAncestorWithClass(this,
             AbstractActionTag.class);
        if (actionTag == null)
        {
            throw new JspTagException("This tag should be used only as a child tag");
        }

        return actionTag;

    }

}
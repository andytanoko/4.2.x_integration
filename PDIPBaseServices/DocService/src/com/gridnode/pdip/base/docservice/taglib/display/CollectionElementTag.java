/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CollectionElementTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.display;

import java.util.*;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class CollectionElementTag extends TagSupport
{

    Collection c;
    Iterator iterator;
    String id;

    public void setId(String id)
    {
        this.id = id;
    }

    public int doStartTag() throws JspTagException
    {

        CollectionIteratorTag iteratorTag = getCollectionIteratorTag();

        if (iteratorTag == null)
        {
            throw new JspTagException("This tag should be used only as a child tag");
        }
        pageContext.setAttribute(this.id, iteratorTag.getNextObject());
        return EVAL_BODY_INCLUDE;
    }

    private CollectionIteratorTag getCollectionIteratorTag()
    {
        CollectionIteratorTag iteratorTag =
            (CollectionIteratorTag) TagSupport.findAncestorWithClass(this,
             CollectionIteratorTag.class);
        return iteratorTag;
    }

}
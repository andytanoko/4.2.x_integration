/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CollectionIteratorTag.java
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

public class CollectionIteratorTag extends TagSupport
{

    Collection c;
    Iterator iterator;
    String id;
    AbstractCollectionTag collectionTag;

    public int doStartTag() throws JspTagException
    {

        collectionTag = getCollectionTag();

        if (collectionTag == null)
        {
            throw new JspTagException("This tag should be used only as a child tag");
        }
        if (collectionTag.hasNext())
        {
            return EVAL_BODY_INCLUDE;
        }
        else
        {
            return SKIP_BODY;
        }
    }

    public int doAfterBody()
    {
        if (collectionTag.hasNext())
        {
            return EVAL_BODY_AGAIN;
        }
        else
        {
            return SKIP_BODY;
        }
    }

    private AbstractCollectionTag getCollectionTag()
    {
        AbstractCollectionTag collectionTag =
            (AbstractCollectionTag) TagSupport.findAncestorWithClass(this,
             AbstractCollectionTag.class);
        return collectionTag;
    }

    public Object getNextObject()
    {
        if (collectionTag.hasNext())
        {
            return collectionTag.getNextObject();
        }
        else
        {
            return null;
        }
    }

}
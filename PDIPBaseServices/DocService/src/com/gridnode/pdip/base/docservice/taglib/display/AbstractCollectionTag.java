/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractCollectionTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.display;

import java.util.*;
import javax.servlet.jsp.JspTagException;

public abstract class AbstractCollectionTag extends AbstractDisplayTag
{

    protected Collection c;
    protected Iterator it;

    public int doStartTag() throws JspTagException
    {
        if (getRowCount() > 0)
        {
            return EVAL_BODY_INCLUDE;
        }
        else
        {
            return SKIP_BODY;
        }
    }

    public int getRowCount()
    {
        if (c == null)
        {
            return 0;
        }
        else
        {
            return c.size();
        }
    }

    public Object getNextObject()
    {
        if (it == null)
        {
            return null;
        }
        return it.next();
    }

    public boolean hasNext()
    {
        if (it == null)
        {
            return false;
        }
        return it.hasNext();
    }

}

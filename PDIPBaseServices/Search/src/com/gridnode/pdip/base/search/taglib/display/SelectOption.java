/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SelectOption.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;

public class SelectOption extends TagSupport
{
    private String dbFieldName;

    public int doStartTag() throws JspTagException
    {
        try
        {

	  return SKIP_BODY;
        }
        catch (Exception e)
        {
            throw new JspTagException(e.getMessage());
        }
    }

    public void setDbFieldName(String dbFieldName)
    {
      this.dbFieldName = dbFieldName;
    }
}
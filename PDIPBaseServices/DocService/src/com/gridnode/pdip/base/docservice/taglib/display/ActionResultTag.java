/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionResultTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.display;

import javax.servlet.jsp.JspTagException;

import com.gridnode.pdip.base.docservice.taglib.action.AbstractActionTag;

public class ActionResultTag extends AbstractDisplayTag
{

    private String resultType;
    private String id;

    public static String[] RESULT_TYPES = { "ILLEGAL_ARGUMENTS",
        "SYSTEM_ERROR", "SOURCE_NOT_FOUND", "DESTINATION_NOT_FOUND",
        "NAME_EXISTS", "INTER_DOMAIN_TRANSFER", "SAME_SOURCE_AND_DESTINATION",
        "FILE_MANAGER_ERROR", "DOCUMENT_PARENT", "SUCCESS", "FAILURE",
        "NO_DATA" };

    public void setResultType(String type)
    {
        this.resultType = type;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int doStartTag() throws JspTagException
    {
        try
        {
            AbstractActionTag actionTag = getParentTag();

            if (compare(actionTag.getResult(), resultType))
            {
                pageContext.setAttribute(this.id, actionTag.getMessage());
                return EVAL_BODY_INCLUDE;
            }
            else
            {
                return SKIP_BODY;
            }
        }
        catch (Exception e)
        {
            throw new JspTagException(e.getMessage());
        }
    }

    private boolean compare(int result, String resultType)
    {
        for (int i = 0; i < RESULT_TYPES.length; i++)
        {
            if (resultType.equals(RESULT_TYPES[i]))
            {
                if (result == i)
                {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

}
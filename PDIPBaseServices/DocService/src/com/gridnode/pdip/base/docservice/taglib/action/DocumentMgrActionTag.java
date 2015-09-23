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
 * Nov 25 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;

import com.gridnode.pdip.base.docservice.action.Action;
import com.gridnode.pdip.base.docservice.util.*;
import com.gridnode.pdip.base.docservice.exceptions.DocumentServiceException;

public class DocumentMgrActionTag extends AbstractActionTag
{

    public int doStartTag() throws JspTagException
    {
        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();
        try
        {
            Action[] actions = DocumentServiceUtil.getRequiredActions(request);
            if (actions == null || actions.length == 0)
            {
                result = FAILURE;
            }
            else
            {
                for (int i = 0; i < actions.length; i++)
                {
                    actions[i].doAction();
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
            message = "Unexpected Error!";
        }
        return EVAL_BODY_INCLUDE;
    }

}

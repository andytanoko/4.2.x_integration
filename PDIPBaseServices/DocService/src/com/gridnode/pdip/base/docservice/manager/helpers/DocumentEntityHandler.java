/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.base.docservice.filesystem.model.Document;

public class DocumentEntityHandler extends LocalEntityHandler
{

    private DocumentEntityHandler()
    {
        super(Document.ENTITY_NAME);
    }

    public static DocumentEntityHandler getInstance()
    {
        DocumentEntityHandler handler = null;

        if (EntityHandlerFactory.hasEntityHandlerFor(Document.ENTITY_NAME, true))
        {
            handler = (DocumentEntityHandler)
                EntityHandlerFactory.getHandlerFor(Document.ENTITY_NAME, true);
        }
        else
        {
            handler = new DocumentEntityHandler();
            EntityHandlerFactory.putEntityHandler(Document.ENTITY_NAME, true, handler);
        }

        return handler;
    }

    protected Class getProxyInterfaceClass() throws java.lang.Exception
    {
        return com.gridnode.pdip.base.docservice.filesystem.ejb.IDocumentObj.class;
    }

}

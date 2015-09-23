/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.base.docservice.filesystem.model.File;

public class FileEntityHandler extends LocalEntityHandler
{

    private FileEntityHandler()
    {
        super(File.ENTITY_NAME);
    }

    public static FileEntityHandler getInstance()
    {
        FileEntityHandler handler = null;

        if (EntityHandlerFactory.hasEntityHandlerFor(File.ENTITY_NAME, true))
        {
            handler = (FileEntityHandler)
                EntityHandlerFactory.getHandlerFor(File.ENTITY_NAME, true);
        }
        else
        {
            handler = new FileEntityHandler();
            EntityHandlerFactory.putEntityHandler(File.ENTITY_NAME, true, handler);
        }

        return handler;
    }

    protected Class getProxyInterfaceClass() throws java.lang.Exception
    {
        return com.gridnode.pdip.base.docservice.filesystem.ejb.IFileObj.class;
    }

}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FolderEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.base.docservice.filesystem.model.Folder;

public class FolderEntityHandler extends LocalEntityHandler
{

    private FolderEntityHandler()
    {
        super(Folder.ENTITY_NAME);
    }

    public static FolderEntityHandler getInstance()
    {
        FolderEntityHandler handler = null;

        if (EntityHandlerFactory.hasEntityHandlerFor(Folder.ENTITY_NAME, true))
        {
            handler = (FolderEntityHandler)
                EntityHandlerFactory.getHandlerFor(Folder.ENTITY_NAME, true);
        }
        else
        {
            handler = new FolderEntityHandler();
            EntityHandlerFactory.putEntityHandler(Folder.ENTITY_NAME, true, handler);
        }

        return handler;
    }

    protected Class getProxyInterfaceClass() throws java.lang.Exception
    {
        return com.gridnode.pdip.base.docservice.filesystem.ejb.IFolderObj.class;
    }

}

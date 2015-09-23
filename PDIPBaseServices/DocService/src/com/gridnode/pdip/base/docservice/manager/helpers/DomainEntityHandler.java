/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomainEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.manager.helpers;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.base.docservice.filesystem.model.Domain;

public class DomainEntityHandler extends LocalEntityHandler
{

    private DomainEntityHandler()
    {
        super(Domain.ENTITY_NAME);
    }

    public static DomainEntityHandler getInstance()
    {
        DomainEntityHandler handler = null;

        if (EntityHandlerFactory.hasEntityHandlerFor(Domain.ENTITY_NAME, true))
        {
            handler = (DomainEntityHandler)
                EntityHandlerFactory.getHandlerFor(Domain.ENTITY_NAME, true);
        }
        else
        {
            handler = new DomainEntityHandler();
            EntityHandlerFactory.putEntityHandler(Domain.ENTITY_NAME, true, handler);
        }

        return handler;
    }

    protected Class getProxyInterfaceClass() throws java.lang.Exception
    {
        return com.gridnode.pdip.base.docservice.filesystem.ejb.IDomainObj.class;
    }

}

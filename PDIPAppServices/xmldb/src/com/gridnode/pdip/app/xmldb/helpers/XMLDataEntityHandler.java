/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDataEntityHandler.java
 *
 ****************************************************************************
 * Date           Author          Changes
 ****************************************************************************
 * Dec 20 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import com.gridnode.pdip.framework.db.*;

/**
 * The entity handler for all entities used by XMLDB
 */
public class XMLDataEntityHandler extends LocalEntityHandler
{

    private XMLDataEntityHandler(String entityName)
    {
        super(entityName);
    }

    public static XMLDataEntityHandler getInstance(String entityName)
    {
        XMLDataEntityHandler handler = null;

        if (XMLDBEntityHandlerFactory.hasEntityHandlerFor(entityName, true))
        {
            handler = (XMLDataEntityHandler)
                XMLDBEntityHandlerFactory.getHandlerFor(entityName, true);
        }
        else
        {
            handler = new XMLDataEntityHandler(entityName);
            XMLDBEntityHandlerFactory.putEntityHandler(entityName, true, handler);
        }

        return handler;
    }

    protected Class getProxyInterfaceClass() throws java.lang.Exception
    {
        return com.gridnode.pdip.framework.db.ejb.IEntityLocalObject.class;
    }

}

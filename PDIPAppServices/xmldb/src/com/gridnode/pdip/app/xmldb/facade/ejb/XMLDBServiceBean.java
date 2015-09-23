/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDBServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 24 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.facade.ejb;

import com.gridnode.pdip.app.xmldb.helpers.Logger;
import com.gridnode.pdip.app.xmldb.helpers.XMLDBServiceDelegate;
import com.gridnode.pdip.app.xmldb.exceptions.XMLDBException;

import javax.ejb.SessionBean;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;



public class XMLDBServiceBean implements SessionBean
{
    SessionContext ctx;

    public void ejbCreate()
    {
    }

    public void ejbRemove()
    {
    }

    public void ejbPassivate()
    {
    }

    public void ejbActivate()
    {
    }

    public void setSessionContext(SessionContext scx)
    {
        this.ctx = scx;
    }

    public Collection insertDataFromXML(String xmlFile, String dtdFile,
        String mappingFile) throws XMLDBException
    {
        try
        {
            return XMLDBServiceDelegate.insertDataFromXML(xmlFile, dtdFile, mappingFile);
        }
        catch (XMLDBException xe)
        {
            xe.printStackTrace();
            Logger.err("Exception thrown by insertDataFromXML", xe);
            throw xe;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            Logger.err("Exception thrown by insertDataFromXML", t);
            throw new XMLDBException(t.toString());
        }
    }

    public File generateXML(String mappingFile, HashMap inputs, String dtdFile)
        throws XMLDBException
    {
        try
        {
            return XMLDBServiceDelegate.generateXML(mappingFile, inputs, dtdFile);
        }
        catch (XMLDBException xe)
        {
            Logger.err("Exception thrown by generateXML", xe);
            throw xe;
        }
        catch (Throwable t)
        {
            Logger.err("Exception thrown by generateXML", t);
            throw new XMLDBException(t.toString());
        }
    }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentElement.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

import org.jdom.Element;

public class DocumentElement implements Serializable
{

    private String name;
    private DBXMLMappings mappings;
    private ArrayList children;
    private DocumentElement parent;

    public DocumentElement(Element element, DocumentElement parent)
    {
        this.parent = parent;
        this.name =
            element.getAttribute(DBXMLMappingFile.ELEMENT_NAME).getValue();
        this.mappings = getMappings(element);
        this.children = getChildren(element);
    }

    private DBXMLMappings getMappings(Element element)
    {
        Element me = element.getChild(IDBXMLMappingFile.MAPPINGS);
        if (me == null)
        {
            return null;
        }
        return new DBXMLMappings(me);
    }

    private ArrayList getChildren(Element element)
    {
        Iterator it = element.getChildren(IDBXMLMappingFile.ELEMENT).iterator();
        ArrayList list = null;
        while (it.hasNext())
        {
            DocumentElement de = new DocumentElement((Element) it.next(), this);
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(de);
        }
        return list;
    }

    public String getName()
    {
        return this.name;
    }

    public DocumentElement getParent()
    {
        return this.parent;
    }

    public DBXMLMappings getMappings()
    {
        return this.mappings;
    }

    public ArrayList getChildren()
    {
        return this.children;
    }

    public DocumentElement getChild(String name)
    {
        if (children == null)
        {
            return null;
        }
        else
        {
            Iterator it = children.iterator();
            while (it.hasNext())
            {
                DocumentElement de = (DocumentElement) it.next();
                if (de.getName().equals(name))
                {
                    return de;
                }
            }
            return null;
        }
    }

    public boolean isMapped()
    {
        if (getMappings() != null)
        {
            if (getMappings().getEntities() != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}

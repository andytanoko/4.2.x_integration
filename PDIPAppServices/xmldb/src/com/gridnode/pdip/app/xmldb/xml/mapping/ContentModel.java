/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ContentModel.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import java.util.*;
import java.io.Serializable;

import org.jdom.Element;

import com.gridnode.pdip.app.xmldb.exceptions.InvalidMappingFileException;
import com.gridnode.pdip.app.xmldb.xml.ElementContent;

public class ContentModel implements Serializable
{

    private ArrayList children;

    public ContentModel(Element element)
    {
        this.children = getChildren(element);
    }

    private ArrayList getChildren(Element element)
    {
        ArrayList list = null;
        Iterator it = element.getChildren(DBXMLMappingFile.CHILD_ELEMENT).iterator();
        while (it.hasNext())
        {
            ChildElement ce = new ChildElement((Element) it.next());
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(ce);
        }
        return list;
    }

    public ArrayList getBody(ElementContent ec) throws Throwable
    {
        ArrayList list = new ArrayList();
        Iterator it = children.iterator();
        while (it.hasNext())
        {
            ChildElement ce = (ChildElement) it.next();
            List temp = ce.getBody(ec);
            ec.removeChildren(temp);
            list.addAll(temp);
        }
        return list;
    }

}

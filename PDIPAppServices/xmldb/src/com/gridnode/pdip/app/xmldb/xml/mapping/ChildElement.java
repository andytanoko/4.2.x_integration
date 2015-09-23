/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChildElement.java
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

public class ChildElement implements Serializable
{

    private String elementName;
    private boolean required = false;
    private boolean multiples = false;

    public ChildElement(Element element)
    {
        this.elementName =
            element.getAttribute(DBXMLMappingFile.CE_NAME).getValue();
        this.required = isRequired(element);
        this.multiples = isMultiplicityAllowed(element);
    }

    public String getElementName()
    {
        return this.elementName;
    }

    private boolean isRequired(Element element)
    {
        String req =
            element.getAttribute(DBXMLMappingFile.CE_REQUIRED).getValue();
        if (req == null)
        {
            return false;
        }
        if (req.equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isMultiplicityAllowed(Element element)
    {
        String m =
            element.getAttribute(DBXMLMappingFile.CE_MULTIPLES).getValue();
        if (m == null)
        {
            return false;
        }
        if (m.equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public boolean isMultiplicityAllowed()
    {
        return multiples;
    }

    public List getBody(ElementContent ec) throws Throwable
    {
        List c = ec.getChildren(elementName);
        if (c.size() == 0)
        {
            if (isRequired())
            {
                ElementContent element = createDefaultElement(ec, elementName);
                ArrayList list = new ArrayList();
                list.add(element);
                return list;
            }
            else
            {
                return new ArrayList();
            }
        }
        else
        {
            if (isMultiplicityAllowed())
            {
                ArrayList list = new ArrayList();
                list.addAll(c);
                return list;
            }
            else
            {
                if (c.size() > 1)
                {
                    //Only one element is possible, so check whether the
                    //parent element can be multiples. If so create as many
                    //parent elements as there are children and set each child
                    //to each parent element created

                    ArrayList list = new ArrayList();
                    Iterator it = c.iterator();
                    list.add(it.next());
                    ArrayList temp = new ArrayList();
                    while (it.hasNext())
                    {
                        temp.add(it.next());
                    }
                    ec.transferElements(temp);
                    return list;
                }
                else
                {
                    ArrayList list = new ArrayList();
                    list.addAll(c);
                    return list;
                }
            }
        }
    }

    private ElementContent createDefaultElement(ElementContent ec,
        String element) throws Throwable
    {
        DocumentElement de = ec.getDocumentElement().getChild(element);
        if (de == null)
        {
            throw new InvalidMappingFileException("Element not defined in " +
                "mapping file " + element);
        }
        return new ElementContent(de, new HashMap(), ec.getInputs());
    }

}

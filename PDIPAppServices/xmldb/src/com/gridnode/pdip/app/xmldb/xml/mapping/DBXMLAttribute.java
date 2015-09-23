/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DBXMLAttribute.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import org.jdom.Element;
import java.io.Serializable;

public class DBXMLAttribute implements Serializable
{

    private String name;
    private EntityMember em;

    public DBXMLAttribute(Element element)
    {
System.out.println("The DBXMLAttribute element = " + element);
		this.name =
            element.getAttribute(DBXMLMappingFile.A_NAME).getValue();
System.out.println("The DBXMLAttribute this.name = " + this.name);
        Element e = (Element) element.getChildren().iterator().next();
System.out.println("The DBXMLAttribute e.getName() = " + e.getName());
        if (e.getName().equals(DBXMLMappingFile.ENTITY_FIELD))
        {
            em = new EntityField(e);
        }
        else if (e.getName().equals(DBXMLMappingFile.VALUE_LITERAL))
        {
            em = new ValueLiteral(e);
        }
        else
        {
            em = new ValueInput(e);
        }
    }

    public String getAttributeName()
    {
        return this.name;
    }

    public EntityMember getAttributeType()
    {
        return this.em;
    }
}

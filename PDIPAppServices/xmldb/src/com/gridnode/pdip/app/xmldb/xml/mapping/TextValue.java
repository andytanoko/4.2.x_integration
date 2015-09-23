/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TextValue.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import java.io.Serializable;

import org.jdom.Element;

public class TextValue implements Serializable
{

    private EntityMember em;

    public TextValue(Element element)
    {
        Element e = (Element) element.getChildren().iterator().next();
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

    public EntityMember getValueType()
    {
        return this.em;
    }
}

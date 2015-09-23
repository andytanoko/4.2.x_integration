/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValueLiteral.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import org.jdom.Element;
import org.jdom.Attribute;

import com.gridnode.pdip.app.xmldb.xml.ElementContent;

public class ValueLiteral extends EntityMember
{

    private String value;
	private String fieldFormat=null;

    public ValueLiteral(Element element)
    {
        super(EntityMember.VALUE_LITERAL);
        this.value =
            element.getAttribute(DBXMLMappingFile.VL_VALUE).getValue().intern();

		Attribute attrib = element.getAttribute(DBXMLMappingFile.VL_FIELD_FORMAT);
		if(attrib != null)
			this.fieldFormat = attrib.getValue();
	}

    public String getValue()
    {
        return this.value;
    }

    public Object getValue(ElementContent element) throws Throwable
    {
        return getValue();
    }

	public String getFieldFormat()
	{
		return this.fieldFormat;
	}
}

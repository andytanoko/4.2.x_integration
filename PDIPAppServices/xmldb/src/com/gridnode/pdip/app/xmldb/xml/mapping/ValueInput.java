/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValueInput.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import org.jdom.Element;
import org.jdom.Attribute;
import java.util.HashMap;

import com.gridnode.pdip.app.xmldb.exceptions.MissingInputException;
import com.gridnode.pdip.app.xmldb.xml.ElementContent;

public class ValueInput extends EntityMember
{

    private String inputName;
	private String fieldFormat = null;

    public ValueInput(Element element)
    {
		super(EntityMember.VALUE_INPUT);
//		System.out.println("In Valuye 1 " + element);
//		System.out.println("In Valuye 2 " + DBXMLMappingFile.VI_INPUT);
//		System.out.println("In Valuye 3 " + element.getAttribute(DBXMLMappingFile.VI_INPUT));
//		System.out.println("In Valuye 4 " + element.getAttribute(DBXMLMappingFile.VI_INPUT).getValue());
//		System.out.println("In Valuye 5 " + element.getAttribute(DBXMLMappingFile.VI_INPUT).getValue().intern());
//        System.out.println("************** FILTER NAME  =" + this.inputName);
		
		this.inputName =
            element.getAttribute(DBXMLMappingFile.VI_INPUT).getValue().intern();

		Attribute attrib = element.getAttribute(DBXMLMappingFile.VI_FIELD_FORMAT);
		if(attrib != null)
			this.fieldFormat = attrib.getValue();

//		System.out.println("************** FILTER NAME  =" + this.inputName);
//		System.out.println("************** FILTER NAME  =" + this.fieldFormat);
    }

    public String getInputName()
    {
        return this.inputName;
    }

    public String getFieldFormat()
    {
        return this.fieldFormat;
    }

    public Object getValue(ElementContent element) throws Throwable
    {
//System.out.println("element inside ValueInput.getValue: " + element);
			HashMap map = element.getInputs();
//System.out.println("map inside ValueInput.getValue: " + map);
			Object obj = map.get(getInputName());

//			System.out.println("Object obj : " + obj);
			if (obj == null)
			{
				throw new MissingInputException("Missing input " + getInputName());
			}
			else
			{
				return obj;
			}
    }

}

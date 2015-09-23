/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityFilter.java
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
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import org.jdom.Element;

import com.gridnode.pdip.app.xmldb.exceptions.UnmatchingFieldTypeException;
import com.gridnode.pdip.app.xmldb.helpers.EntityUtil;
import com.gridnode.pdip.app.xmldb.xml.ElementContent;

public class EntityFilter implements Serializable
{

    private String fieldName;
    private EntityMember filterType;

    public EntityFilter(Element element)
    {
        this.fieldName =
            element.getAttribute(DBXMLMappingFile.FILTER_FIELD_NAME).getValue();
//System.out.println("The filter field name is : " + this.fieldName);
        Element e = (Element) element.getChildren().iterator().next();
//System.out.println("The e.getName()  is : " + e.getName());

        if (e.getName().equals(DBXMLMappingFile.FOREIGN_KEY))
        {
            filterType = new ForeignKey(e);
//			System.out.println("Foreing key type is "+filterType);
        }
        else if (e.getName().equals(DBXMLMappingFile.VALUE_LITERAL))
        {
            filterType = new ValueLiteral(e);
//			System.out.println("Foreing Value Literal is "+filterType);
        }
        else
        {
            filterType = new ValueInput(e);
//			System.out.println("Filter Type is "+filterType);
        }
    }

    public String getFieldName()
    {
        return this.fieldName;
    }

    public EntityMember getFilterType()
    {
        return this.filterType;
    }

    public Object getFieldValue(String entityName, ElementContent element)
        throws Throwable
    {
//		System.out.println("Inside get field value "+entityName+" *** "+element);
        if (filterType.getType() == EntityMember.VALUE_LITERAL ||
            filterType.getType() == EntityMember.VALUE_INPUT)
        {
			String fieldText = null;
            String valueClass = EntityUtil.getValueClass(entityName,
                getFieldName());
            Object fieldValue = getFilterType().getValue(element);
//System.out.println("fieldValue in EntityFilter = " + fieldValue);
            if(fieldValue != null)
            {
            	fieldText = fieldValue.toString();
			}
            return parseFieldValue(fieldText, valueClass, getFilterType().getFieldFormat());
        }
        else
        {
            return getFilterType().getValue(element);
        }
    }

    private Object parseFieldValue(String str, String valueClass, String fieldFormat) throws Exception
    {
//		System.out.println("Inside parse field value "+str+" ***  "+valueClass);
        if (valueClass.equals("java.lang.String"))
        {
			if (str == null|| str.equals(""))
            {
                return "";
            }
            return str;
        }
        else if (valueClass.equals("java.lang.Integer"))
        {
            if (str == null|| str.equals(""))
            {
                return null;
            }
            return new Integer(Integer.parseInt(str));
        }
        else if (valueClass.equals("java.lang.Float"))
        {
            if (str == null|| str.equals(""))
            {
                return null;
            }
            return new Float(Float.parseFloat(str));
        }
        else if (valueClass.equals("java.util.Date"))
        {
            if (str == null|| str.equals(""))
            {
                return null;
            }
			if(fieldFormat == null)
				return str;
			else if(fieldFormat.equals(""))
				return str;
			else
			{
				try
				{
					Timestamp time = Timestamp.valueOf(str);
					SimpleDateFormat sdf = new SimpleDateFormat(fieldFormat);
					return sdf.format(time);
				}
				catch(Exception e)
				{
					System.out.println("Exceptionm while converting into datestamp " + e);
					return null;
				}
			}
        }
        else if (valueClass.equals("java.lang.Long"))
        {
            if (str == null|| str.equals(""))
            {
                return null;
            }
            return new Long(Long.parseLong(str));
        }
        else if (valueClass.equals("java.lang.Boolean"))
        {
            if (str == null|| str.equals(""))
            {
                return null;
            }
            else if (str.equals("true"))
            {
                return new Boolean(true);
            }
            else
            {
                return new Boolean(false);
            }
        }
        else
        {
            throw new UnmatchingFieldTypeException("Field type mismatch. " +
                "Type: " + valueClass + " Value: " + str);
        }
    }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ForeignKey.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import org.jdom.Element;
import org.jdom.Attribute;

import java.util.StringTokenizer;

import com.gridnode.pdip.app.xmldb.exceptions.UnresolvedForeignKeyException;
import com.gridnode.pdip.app.xmldb.helpers.EntityUtil;
import com.gridnode.pdip.app.xmldb.xml.ElementContent;

import com.gridnode.pdip.framework.db.entity.IEntity;

public class ForeignKey extends EntityMember
{

    private String entityName;
    private String fieldName;
    private String elementRef;
	private String fieldFormat=null;

    public ForeignKey(Element element)
    {
        super(EntityMember.FOREIGN_KEY);
        this.entityName =
            element.getAttribute(DBXMLMappingFile.FK_ENTITY).getValue().intern();
        this.fieldName =
            element.getAttribute(DBXMLMappingFile.FK_FIELD).getValue().intern();
        this.elementRef =
            element.getAttribute(DBXMLMappingFile.FK_ELEMENT).getValue().intern();

		Attribute attrib = element.getAttribute(DBXMLMappingFile.FK_FIELD_FORMAT);
		if(attrib != null)
			this.fieldFormat = attrib.getValue();
    }

    public String getEntityName()
    {
        return this.entityName;
    }

    public String getFieldName()
    {
        return this.fieldName;
    }

    public String getFieldFormat()
    {
        return this.fieldFormat;
    }

    public String getElementRef()
    {
        return this.elementRef;
    }

    public Object getValue(ElementContent element) throws Throwable
    {
        if (element == null)
        {
            throw new UnresolvedForeignKeyException("Root element " +
                "cannot map to an entity having a foreign key");
        }

        //In foreign key, the element arg is the parent
        ElementContent re = findReferredElement(element, this.elementRef);
        Long uId = re.getEntityId(this.entityName);
        if (uId == null)
        {
            throw new UnresolvedForeignKeyException("Element " + re.getName() +
                " does not have an entity mapping " + this.entityName);
        }
        IEntity entity = EntityUtil.findEntityByPK(this.entityName, uId);
        //return EntityUtil.getFieldValue(entity, fieldName);
        return EntityUtil.getFieldValue(entity, fieldName, this.fieldFormat);
    }

    private ElementContent findReferredElement(ElementContent element,
        String elementRef) throws Exception
    {
        if (elementRef.equals(""))
        {
            return element;
        }
        StringTokenizer st = new StringTokenizer(elementRef, "/");
        while (st.hasMoreTokens())
        {
            String str = st.nextToken();
            if (str.equals(".."))
            {
                element = (ElementContent) element.getParent();
            }
            else
            {
                element = (ElementContent) element.getChild(str);
                if (element == null)
                {
                    throw new UnresolvedForeignKeyException("Unable to " +
                        "resolve foreign key " + getEntityName() +
                        " " + getFieldName());
                }
            }
        }
        return element;
    }

}

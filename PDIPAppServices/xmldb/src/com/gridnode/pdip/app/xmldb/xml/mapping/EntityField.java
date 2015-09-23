/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityField.java
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
import com.gridnode.pdip.app.xmldb.helpers.EntityUtil;

import com.gridnode.pdip.framework.db.entity.IEntity;

public class EntityField extends EntityMember
{

    private String entityName;
    private String fieldName;
	private String fieldFormat = null;

    public EntityField(Element element)
    {
        super(EntityMember.ENTITY_FIELD);
        this.entityName =
            element.getAttribute(DBXMLMappingFile.EF_ENTITY_NAME).
            getValue().intern();
        this.fieldName =
            element.getAttribute(DBXMLMappingFile.EF_FIELD_NAME).
            getValue().intern();
		Attribute attrib = element.getAttribute(DBXMLMappingFile.EF_FIELD_FORMAT);
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

    public Object getValue(ElementContent element) throws Throwable
    {
        //Get the entity from the element, return the value of the field
        Long uId = element.getEntityId(this.entityName);
        if (uId == null)
        {
            return null;
        }
        else
        {
            IEntity entity = EntityUtil.findEntityByPK(this.entityName, uId);
            //return EntityUtil.getFieldValue(entity, fieldName);
            return EntityUtil.getFieldValue(entity, fieldName, this.fieldFormat);
        }
    }

}

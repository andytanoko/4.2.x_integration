/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDBMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

import org.jdom.*;

import java.util.*;

import com.gridnode.pdip.app.xmldb.exceptions.*;
import com.gridnode.pdip.app.xmldb.helpers.Logger;

public class XMLDBMappingFile implements IXMLDBMappingFile
{

    private ArrayList entities = new ArrayList();
    private String rootElement;

    public XMLDBMappingFile(Document document) throws Exception
    {
        Element root = document.getRootElement();
        Attribute attr = root.getAttribute(ROOT_ENTITY);
        rootElement = attr.getValue();
        getEntities(root.getChildren());
    }

    public ArrayList getEntities()
    {
        return entities;
    }

    public EntityElement getEntityElement(String entityName)
    {
        Iterator it = entities.iterator();
        while (it.hasNext())
        {
            EntityElement entity = (EntityElement) it.next();
            if (entity.getName().equals(entityName))
            {
                return entity;
            }
        }
        return null;
    }

    private void getEntities(List children) throws Exception
    {
        Iterator it = children.iterator();
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            Attribute attr = element.getAttribute(ENTITY_NAME);
            EntityElement entity = new EntityElement(attr.getValue());
            Logger.debug("XMLDBMappingFile: Entity: " + attr.getValue());
            attr = element.getAttribute(ENTITY_XPATH);
            entity.setXPath(attr.getValue());
            entity.setUnique(getBooleanValue(element.getAttribute(ENTITY_UNIQUE)));
            getFields(element.getChildren(), entity);
            entities.add(entity);
        }
    }

    private boolean getBooleanValue(Attribute attrib)
    {
        if (attrib == null)
        {
            return false;
        }
        else if (attrib.getValue().equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void getFields(List children, EntityElement entity) throws Exception
    {
        Iterator it = children.iterator();
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            Attribute attr = element.getAttribute(FIELD_NAME);
            Attribute formatAttr = element.getAttribute(FIELD_FORMAT);
            Attribute positionAttr = element.getAttribute(POSITION_FORMAT);
            FieldElement field = new FieldElement(attr.getValue());
            if(formatAttr != null)
              field.setFormat(formatAttr.getValue());
            else field.setFormat("");
            if(positionAttr != null)
              field.setPosition(Integer.parseInt(positionAttr.getValue()));
            field.setFieldType(getFieldType(element.getChildren()));
            entity.addField(field);
        }
    }

    private FieldType getFieldType(List list) throws Exception
    {
        if (list.size() != 1)
        {
            throw new InvalidMappingFileException("Field type not specified");
        }
        Element element = (Element) list.iterator().next();
        Logger.debug("XMLDBMappingFile: Field: " + element.getName());
        if (element.getName().equals(FIELD_TYPE_CE))
        {
            ChildEntity childEntity = new ChildEntity();
            Attribute attr = element.getAttribute(FIELD_TYPE_CE_NAME);
            childEntity.setEntityName(attr.getValue());
            childEntity.setOptional(getBooleanValue(
                element.getAttribute(FIELD_TYPE_CE_OPTIONAL)));
            childEntity.setMultiplicity(getBooleanValue(
                element.getAttribute(FIELD_TYPE_CE_MULTIPLICITY)));
            attr = element.getAttribute(FIELD_TYPE_CE_REL_XPATH);
            childEntity.setRelativeXPath(attr.getValue());
            return childEntity;
        }
        else if (element.getName().equals(FIELD_TYPE_FK))
        {
            ForeignKey foreignKey = new ForeignKey();
            Attribute attr = element.getAttribute(FIELD_TYPE_FK_NAME);
            foreignKey.setEntityName(attr.getValue());
            attr = element.getAttribute(FIELD_TYPE_FK_FIELD_NAME);
            foreignKey.setFieldName(attr.getValue());
            attr = element.getAttribute(FIELD_TYPE_FK_REL_XPATH);
            foreignKey.setRelativeXPath(attr.getValue());
            return foreignKey;
        }
        else if (element.getName().equals(FIELD_TYPE_XE))
        {
            XPathExpr xpathExpr = new XPathExpr();
            Attribute attr = element.getAttribute(FIELD_TYPE_XE_XPATH);
            xpathExpr.setXPath(attr.getValue());
            return xpathExpr;
        }
        else
        {
            ValueLiteral literal = new ValueLiteral();
            Attribute attr = element.getAttribute(FIELD_TYPE_VL_VALUE);
            literal.setValue(attr.getValue());
            return literal;
        }
    }

    public String toString()
    {
        Iterator values = entities.iterator();
        String str = "";
        while (values.hasNext())
        {
            str += ((EntityElement) values.next()).toString() + '\n';
        }
        return str;
    }
}

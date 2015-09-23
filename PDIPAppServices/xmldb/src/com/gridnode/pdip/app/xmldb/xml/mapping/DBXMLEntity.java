/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DBXMLEntity.java
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

import com.gridnode.pdip.app.xmldb.xml.ElementContent;
import com.gridnode.pdip.app.xmldb.helpers.EntityUtil;

public class DBXMLEntity implements Serializable
{

    private String name;
    private ArrayList entityFilters;

    public DBXMLEntity(Element element)
    {
        this.name =
            element.getAttribute(DBXMLMappingFile.ENTITY_NAME).getValue();
        this.entityFilters = getEntityFilters(element);
    }

    private ArrayList getEntityFilters(Element element)
    {
        ArrayList list = null;
        Iterator it =
            element.getChildren(DBXMLMappingFile.ENTITY_FILTER).iterator();
        while (it.hasNext())
        {
            EntityFilter ef = new EntityFilter((Element) it.next());
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(ef);
        }
        return list;
    }

    public String getEntityName()
    {
        return this.name;
    }

    public ArrayList getEntityFilters()
    {
        return this.entityFilters;
    }

    public Collection getEntities(ElementContent ec) throws Throwable
    {
        if (getEntityFilters() != null)
        {
            Iterator it = getEntityFilters().iterator();
            HashMap map = new HashMap();
            while (it.hasNext())
            {
                EntityFilter filter = (EntityFilter) it.next();
                map.put(EntityUtil.getFieldId(getEntityName(),
                    filter.getFieldName()),
                    filter.getFieldValue(getEntityName(), ec));
            }
            return EntityUtil.findEntities(getEntityName(), map);
        }
        else
        {
            return EntityUtil.findAllEntities(getEntityName());
        }
    }
}

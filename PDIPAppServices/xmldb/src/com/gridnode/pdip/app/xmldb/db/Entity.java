/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Entity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db;

import java.util.*;

import com.gridnode.pdip.app.xmldb.db.mapping.EntityElement;
import com.gridnode.pdip.app.xmldb.helpers.EntityUtil;
import com.gridnode.pdip.app.xmldb.helpers.Logger;

import com.gridnode.pdip.framework.db.meta.*;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * Models the value object, which maps to xpath in the xml document.
 */
public class Entity
{
    private String xpath;
    private EntityElement entityElement;
    private HashMap fieldValues = new HashMap();
    private boolean created = false;
    private Long uId;
    private boolean unique;

    public Entity(EntityElement entityElement, String xpath, boolean unique)
    {
        this.entityElement = entityElement;
        this.xpath = xpath;
        this.unique = unique;
    }

    public EntityElement getEntityElement()
    {
        return this.entityElement;
    }

    public String getName()
    {
        return entityElement.getName();
    }

    public String getXPath()
    {
        return xpath;
    }

    public boolean isCreated()
    {
        return created;
    }

    public IEntity getIEntity() throws Throwable
    {
        if (!isCreated())
        {
            return null;
        }
        else
        {
            return EntityUtil.findEntityByPK(getName(), uId);
        }
    }
	public Long getUId()
	{
		return uId;
	}


    public void setFieldValue(Number id, Object obj)
    {
        fieldValues.put(id, obj);
    }

    public Object getFieldValue(Number id) throws Throwable
    {
        if (fieldValues.keySet().contains(id))
        {
            return fieldValues.get(id);
        }
        if (!created)
        {
            if (create() == null)
            {
                return null;
            }
        }
        IEntity entity = EntityUtil.findEntityByPK(getName(), uId);
        return entity.getFieldValue(id);
    }

    public boolean isValueAvailable(Number id)
    {
        if (created)
        {
            return true;
        }
        Set set = fieldValues.keySet();
        return set.contains(id);
    }

    public IEntity create() throws Throwable
    {
/*
        if (created)
        {
            return true;
        }
*/
        EntityMetaInfo entityInfo =
            EntityUtil.getEntityMetaInfo(entityElement.getName());
        FieldMetaInfo fieldMetaInfo[] = entityInfo.getFieldMetaInfo();
        if (fieldValues.values().size() < fieldMetaInfo.length - 1)
        {
            return (IEntity)null;
        }
        if (unique)
        {
            IEntity entity = checkIfExists();
            if (entity != null)
            {
                uId = (Long) entity.getFieldValue(new Integer(0));
                created = true;
                return entity;
            }
        }
        IEntity entity = EntityUtil.createEntity(entityElement.getName(),
            fieldValues);
        uId = (Long) entity.getFieldValue(new Integer(0));
        Logger.debug("Created Entity " + getName() + " " + uId);
        created = true;
        return entity;
    }

    private IEntity checkIfExists() throws Throwable
    {
        Collection c = EntityUtil.findEntities(entityElement.getName(),
            fieldValues);
        if (c.isEmpty())
        {
            return null;
        }
        else
        {
            Iterator it = c.iterator();
            return (IEntity) it.next();
        }
    }

    public String toString()
    {
        String str = entityElement.getName() + " : " +
            xpath + " : " + unique;
        Iterator it = fieldValues.keySet().iterator();
        while (it.hasNext())
        {
            Number id = (Number) it.next();
            str += "\n      " + id + " : " + fieldValues.get(id);
        }
        return str;
    }

}

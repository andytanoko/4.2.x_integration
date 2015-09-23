/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002   Girish R         Created
 * Mar 25 2003    Neo Sok Lay         EntityMetaInfoLoader is no longer available.
 *                                    Replaced by MetaInfoFactory.
 */
package com.gridnode.pdip.app.xmldb.helpers;



import com.gridnode.pdip.app.xmldb.exceptions.UnmatchingFieldTypeException;
import com.gridnode.pdip.app.xmldb.db.mapping.XMLDBMappingFile;

import com.gridnode.pdip.framework.db.meta.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.db.*;

import java.util.*;
import java.io.*;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

/**
 * Handles all entity-bean method calls
 */
public class EntityUtil
{

    public static EntityMetaInfo getEntityMetaInfo(String entityName)
        throws Exception
    {
        //return EntityMetaInfoLoader.getEntityMetaInfo(entityName);
      return MetaInfoFactory.getInstance().getEntityMetaInfo(entityName);
    }

    /**
     * Returns the value class of a specific field in an entity
     */
    public static String getValueClass(String entityName, String fieldName)
        throws Throwable
    {
//			System.out.println("Inside getValueClass="+fieldName);
        EntityMetaInfo entityInfo = getEntityMetaInfo(entityName);
//System.out.println("Inside entityInfo="+entityInfo);
        FieldMetaInfo fieldInfo = entityInfo.findFieldMetaInfo(fieldName);
//System.out.println("Inside fieldInfo="+fieldInfo);
        return fieldInfo.getValueClass();
    }

    public static Number getFieldId(String entityName, String fieldName)
        throws Throwable
    {
        EntityMetaInfo entityInfo = getEntityMetaInfo(entityName);
//System.out.println("Inside entityInfo="+entityInfo);
        FieldMetaInfo fieldInfo = entityInfo.findFieldMetaInfo(fieldName);
//System.out.println("Inside fieldInfo="+fieldInfo);
        return fieldInfo.getFieldId();
    }

    public static Object getFieldValue(IEntity entity, String fieldName, String fieldFormat)
        throws Throwable
    {
    System.out.println("Inside getFieldValue="+fieldName); // + " entity : " + entity);
        EntityMetaInfo metaInfo =
            getEntityMetaInfo(entity.getMetaInfo().getObjectName());
        FieldMetaInfo fieldInfo = metaInfo.findFieldMetaInfo(fieldName);
//System.out.println("Inside fieldInfo.getFieldId()= "+fieldInfo.getFieldId());
//System.out.println("Inside entity.getFieldValue(fieldInfo.getFieldId()) = "+entity.getFieldValue(fieldInfo.getFieldId()));

    if(entity.getFieldValue(fieldInfo.getFieldId()) == null)
      return "";
    Object obj = entity.getFieldValue(fieldInfo.getFieldId());

    EntityUtil util = new EntityUtil();
    return util.parseValue(obj, getValueClass(entity.getMetaInfo().getObjectName(), fieldName), fieldFormat);
    }

    /**
     * Creates an entity of the specified type using the values supplied in
     * the HashMap. Returns the value object
     */
    public static IEntity createEntity(String entityName, HashMap fieldValues)
        throws Throwable
    {
//		System.out.println("Inside Create Entity="+entityName);
        IEntity entity = (IEntity) Class.forName(entityName).newInstance();
        Iterator it = fieldValues.keySet().iterator();
        while (it.hasNext())
        {
            Object obj = it.next();
            entity.setFieldValue((Number) obj, fieldValues.get(obj));
        }
        AbstractEntityHandler entityHandler =
            XMLDataEntityHandler.getInstance(entity.getEntityName());
        entityHandler.create(entity);
        return entity;
    }

    /**
     * Retuns the collection of entities matching the specified entityName and
     * the values matching the HashMap
     */
    public static Collection findEntities(String entityName, HashMap fieldValues)
        throws Throwable
    {
//		System.out.println("Finding Entities " +entityName);
//	    System.out.println("Finding Entities Field Values=" +fieldValues);

        IEntity entity = (IEntity) Class.forName(entityName).newInstance();


        IDataFilter filter = new DataFilterImpl();
//		System.out.println("Filter "+filter);
        Set set = fieldValues.keySet();
//		System.out.println("Field Values= "+set);


        if (set.isEmpty())
        {
            return new ArrayList();
        }
        Iterator it = set.iterator();
        boolean flag = true;
        while (it.hasNext())
        {
            Object obj = it.next();
            if (flag)
            {
//				System.out.println("Add single entity filter="+(Number) obj);
                flag = false;

                filter.addSingleFilter(null, (Number) obj,
                    filter.getEqualOperator(), fieldValues.get(obj), false);
//				System.out.println("After Calling single filter in EntityUtil.java");
            }
            else
            {
                filter.addSingleFilter(filter.getAndConnector(), (Number) obj,
                    filter.getEqualOperator(), fieldValues.get(obj), false);
            }
        }

    AbstractEntityHandler entityHandler =
            XMLDataEntityHandler.getInstance(entity.getEntityName());
    return entityHandler.getEntityByFilter(filter);
    }

    public static IEntity findEntityByPK(String entityName, Long uId)
        throws Throwable
    {
        IEntity entity = (IEntity) Class.forName(entityName).newInstance();
        AbstractEntityHandler entityHandler =
            XMLDataEntityHandler.getInstance(entity.getEntityName());
        return entityHandler.getEntityByKey(uId);
    }

    /**
     * Returns all entities of the specified type
     */
    public static Collection findAllEntities(String entityName) throws Throwable
    {
        IEntity entity = (IEntity) Class.forName(entityName).newInstance();
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, entity.getKeyId(),
            filter.getNotEqualOperator(), new Long(-1), false);
        AbstractEntityHandler entityHandler =
            XMLDataEntityHandler.getInstance(entity.getEntityName());
        return entityHandler.getEntityByFilter(filter);
    }



    private Object parseValue(Object str1, String valueClass, String fieldFormat)
    {
    String str = str1.toString();
    //String fieldFormat = "yyyyMMdd'T'HHmmss";
    System.out.println("Inside parse field value "+str+" ***  "+valueClass + " the fieldFormat = " + fieldFormat);
        if (valueClass.equals("java.lang.String"))
        {
            return str;
        }
        else if (valueClass.equals("java.lang.Integer"))
        {
            if (str.equals("null") || str.equals(""))
            {
                return null;
            }
            return new Integer(Integer.parseInt(str));
        }
        else if (valueClass.equals("java.lang.Float"))
        {
            if (str.equals("null") || str.equals(""))
            {
                return null;
            }
            return new Float(Float.parseFloat(str));
        }
        else if (valueClass.equals("java.util.Date"))
        {
            if (str.equals("null") || str.equals(""))
            {
                return null;
            }
      if(fieldFormat == null)
        return str;
      else if(fieldFormat.equals(""))
        return str;
      else
      {
        SimpleDateFormat sdf = new SimpleDateFormat(fieldFormat);
        if(str1 instanceof java.util.Date)
          return sdf.format((Date) str1);
        else if(str1 instanceof java.sql.Timestamp)
          return sdf.format((Timestamp) str1);
        else
          return str;
      }
        }
        else if (valueClass.equals("java.lang.Long"))
        {
            if (str.equals("null") || str.equals(""))
            {
                return null;
            }
            return new Long(Long.parseLong(str));
        }
        else if (valueClass.equals("java.lang.Boolean"))
        {
            if (str.equals("null") || str.equals(""))
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
            return str;
        }
    }
}

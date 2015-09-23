/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DBXMLMappings.java
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
import com.gridnode.pdip.app.xmldb.exceptions.*;
import com.gridnode.pdip.framework.db.entity.*;

public class DBXMLMappings implements Serializable
{

    private ContentModel contentModel;
    private ArrayList entities;
    private ArrayList attributes;
    private TextValue text;

    public DBXMLMappings(Element element)
    {
        this.contentModel = getContentModel(element);
        this.entities = getEntities(element);
        this.attributes = getAttributes(element);
        this.text = getText(element);
    }

    private ContentModel getContentModel(Element element)
    {
//		System.out.println("Inside Content Model");
        Element cm = element.getChild(DBXMLMappingFile.CONTENT_MODEL);
//		System.out.println("Content Model is = "+cm);
        if (cm == null)
        {
            return null;
        }
        else
        {
            return new ContentModel(cm);
        }
    }

    public ContentModel getContentModel()
    {
        return this.contentModel;
    }

    private ArrayList getEntities(Element element)
    {
//System.out.println("Inside DBXML Mapping "+element);
        ArrayList list = null;
        Iterator it = element.getChildren(DBXMLMappingFile.ENTITY).iterator();
        while (it.hasNext())
        {
            DBXMLEntity entity = new DBXMLEntity((Element) it.next());
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(entity);
        }
//System.out.println("Inside DBXML Mapping. Entities are ="+list);
        return list;
    }

    private ArrayList getAttributes(Element element)
    {
        ArrayList list = null;
        Iterator it = element.getChildren(DBXMLMappingFile.ATTRIBUTE).iterator();
        while (it.hasNext())
        {
            DBXMLAttribute attr = new DBXMLAttribute((Element) it.next());
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(attr);
        }
        return list;
    }

    private TextValue getText(Element element)
    {

        Element te = element.getChild(DBXMLMappingFile.TEXT);
        if (te == null)
        {
            return null;
        }
        else
        {
            return new TextValue(te);
        }
    }

    public ArrayList getEntities()
    {
        return this.entities;
    }

    public ArrayList getAttributes()
    {
        return this.attributes;
    }

    public TextValue getText()
    {
        return this.text;
    }

    //Returns a Collection of (HashMap of (Entity-name - UID))
    public Collection getEntityMaps(ElementContent ec) throws Throwable
    {
//		System.out.println("Inside Get entity map. DBXML Mapping ="+ec);

        if (getEntities() == null)
        {
            return new ArrayList();
        }
        else
        {
            //Entities defined. Each element must point to only a unique
            //entity of a specific type. If there are more than one type
            //of entities mapped to an element...

            ArrayList list = new ArrayList();
            Iterator it = getEntities().iterator();
            boolean flag = true;
            int max = 0;
            Collection maxEntities = null;
            while (it.hasNext())
            {
                DBXMLEntity entity = (DBXMLEntity) it.next();
                Collection entities = entity.getEntities(ec);
                if (entities.isEmpty())
                {
                    return new ArrayList();
                }

                //At the first occurance of multiple entities
                if (flag && entities.size() > 1)
                {
                    flag = false;
                }

                //At the second occurance
                else if (!flag && entities.size() > 1)
                {
                    throw new UnsolvableRelationsException("Multiple " +
                        "entities mapped to element");
                }
                list.add(entities);

                //Find the max number of similar entities
                max = Math.max(max, entities.size());

                //find the type having max no available
                if (entities.size() == max)
                {
                    maxEntities = entities;
                }
            }


            //Initialize the arraylist
            ArrayList result = new ArrayList();
            for (int i = 0; i < max; i++)
            {
                HashMap map = new HashMap();
                result.add(map);
            }

            Iterator eit = list.iterator();
            while (eit.hasNext())
            {
                Collection entities = (Collection) eit.next();
                if (entities.size() == 1)
                {
                    IEntity entity = (IEntity) entities.iterator().next();
                    Iterator rit = result.iterator();
                    while (rit.hasNext())
                    {
                        HashMap map = (HashMap) rit.next();
                        map.put(entity.getMetaInfo().getObjectName(),
                            entity.getFieldValue(entity.getKeyId()));
                    }
                }
                else
                {
                    Iterator rit = result.iterator();
                    Iterator temp = entities.iterator();
                    while (rit.hasNext())
                    {
                        HashMap map = (HashMap) rit.next();
                        IEntity entity = (IEntity) temp.next();
                        map.put(entity.getMetaInfo().getObjectName(),
                            entity.getFieldValue(entity.getKeyId()));
                    }
                }
            }
//			System.out.println("Result is "+result);
            return result;

        }
    }
}
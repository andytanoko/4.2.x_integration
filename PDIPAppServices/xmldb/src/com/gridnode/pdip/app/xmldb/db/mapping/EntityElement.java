/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityElement.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

import java.util.*;

public class EntityElement
{

    private String name;
    private String xpath;
    private boolean unique;
    private HashMap fields = new HashMap();

    public EntityElement(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getXPath()
    {
        return this.xpath;
    }

    public boolean isUnique()
    {
        return this.unique;
    }

    public Collection getFields()
    {
        return this.fields.values();
    }

    public FieldElement getField(String name)
    {
        return (FieldElement) fields.get(name);
    }

    public void setXPath(String xpath)
    {
        this.xpath = xpath;
    }

    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }

    public void addField(FieldElement field)
    {
        fields.put(field.getName(), field);
    }

    /*public String toString()
    {
        String str = name + " : " + xpath + " : " + unique;
        Iterator it = fields.values().iterator();
        while (it.hasNext())
        {
            FieldElement fe = (FieldElement) it.next();
            str = str + "\n     " + fe.toString();
        }
        return str;
    }*/
}
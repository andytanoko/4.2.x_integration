/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldElement.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public class FieldElement
{

    private String name;
    private String format;
    private int position = 0;
    private FieldType fieldType;

    public FieldElement(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public FieldType getFieldType()
    {
        return this.fieldType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getFormat()
    {
        return this.format;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public void setFieldType(FieldType fieldType)
    {
        this.fieldType = fieldType;
    }

    public String toString()
    {
        return name + " : " + fieldType.getType();
    }
}

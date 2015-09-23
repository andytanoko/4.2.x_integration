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
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public class ForeignKey extends FieldType
{

    private String entityName;
    private String fieldName;
    private String relativeXPath;

    public ForeignKey()
    {
        super(FieldType.FOREIGN_KEY);
    }

    public String getEntityName()
    {
        return this.entityName;
    }

    public String getFieldName()
    {
        return this.fieldName;
    }

    public String getRelativeXPath()
    {
        return relativeXPath;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public void setRelativeXPath(String relativeXPath)
    {
        this.relativeXPath = relativeXPath;
    }
}

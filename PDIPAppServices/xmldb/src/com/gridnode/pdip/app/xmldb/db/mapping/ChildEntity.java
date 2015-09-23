/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChildEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public class ChildEntity extends FieldType
{

    private String name;
    private boolean optional;
    private boolean multiplicity;
    private String relativeXPath;

    public ChildEntity()
    {
        super(FieldType.CHILD_ENTITY);
    }

    public String getEntityName()
    {
        return this.name;
    }

    public boolean isOptional()
    {
        return this.optional;
    }

    public boolean isMultiplicity()
    {
        return multiplicity;
    }

    public String getRelativeXPath()
    {
        return this.relativeXPath;
    }

    public void setEntityName(String name)
    {
        this.name = name;
    }

    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }

    public void setMultiplicity(boolean multiplicity)
    {
        this.multiplicity = multiplicity;
    }

    public void setRelativeXPath(String relativeXPath)
    {
        this.relativeXPath = relativeXPath;
    }
}

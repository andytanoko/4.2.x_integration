/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public abstract class FieldType
{
    protected int type = 0;

    public static final int CHILD_ENTITY = 0;
    public static final int FOREIGN_KEY = 1;
    public static final int XPATH_EXPR = 2;
    public static final int VALUE_LITERAL = 3;

    public FieldType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return this.type;
    }
}

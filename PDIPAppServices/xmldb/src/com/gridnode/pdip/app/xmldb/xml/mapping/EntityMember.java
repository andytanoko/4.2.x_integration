/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityMember.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import java.io.Serializable;

import com.gridnode.pdip.app.xmldb.xml.ElementContent;

public abstract class EntityMember implements Serializable
{

    public static int FOREIGN_KEY = 1;
    public static int VALUE_LITERAL = 2;
    public static int VALUE_INPUT = 3;
    public static int ENTITY_FIELD = 4;

    private int type;

    public EntityMember(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return this.type;
    }

    public abstract Object getValue(ElementContent element) throws Throwable;
	public abstract String getFieldFormat() throws Throwable;
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValueLiteral.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public class ValueLiteral extends FieldType
{

    private String value;

    public ValueLiteral()
    {
        super(FieldType.VALUE_LITERAL);
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}

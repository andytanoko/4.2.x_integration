/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XPathExpr.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public class XPathExpr extends FieldType
{

    private String xpath;

    public XPathExpr()
    {
        super(FieldType.XPATH_EXPR);
    }

    public String getXPath()
    {
        return this.xpath;
    }

    public void setXPath(String xpath)
    {
        this.xpath = xpath;
    }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXMLDBMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.db.mapping;

public interface IXMLDBMappingFile
{
    public static String ROOT_ENTITY = "root";
    public static String ENTITY_NAME = "name";
    public static String ENTITY_XPATH = "xpath";
    public static String ENTITY_UNIQUE = "unique";
    public static String FIELD_NAME = "name";
    public static String FIELD_FORMAT = "format";
    public static String POSITION_FORMAT = "position";

    public static String FIELD_TYPE_CE = "ChildEntity";
    public static String FIELD_TYPE_CE_NAME = "entity-name";
    public static String FIELD_TYPE_CE_OPTIONAL = "optional";
    public static String FIELD_TYPE_CE_MULTIPLICITY = "multiplicity";
    public static String FIELD_TYPE_CE_REL_XPATH = "relative-xpath";

    public static String FIELD_TYPE_FK = "ForeignKey";
    public static String FIELD_TYPE_FK_NAME = "entity-name";
    public static String FIELD_TYPE_FK_FIELD_NAME = "field-name";
    public static String FIELD_TYPE_FK_REL_XPATH = "relative-xpath";

    public static String FIELD_TYPE_XE = "XPathExpr";
    public static String FIELD_TYPE_XE_XPATH = "relative-xpath";

    public static String FIELD_TYPE_VL_VALUE = "value";
}

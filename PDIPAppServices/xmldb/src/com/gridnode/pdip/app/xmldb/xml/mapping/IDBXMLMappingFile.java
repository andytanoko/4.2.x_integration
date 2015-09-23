/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDBXMLMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import java.io.Serializable;

public interface IDBXMLMappingFile extends Serializable
{

    public static String ELEMENT = "DocumentElement";
    public static String ELEMENT_NAME = "name";
    public static String MAPPINGS = "Mappings";
    public static String ENTITY = "Entity";
    public static String ENTITY_NAME = "name";
    public static String CONTENT_MODEL = "ContentModel";

    public static String ATTRIBUTE = "Attribute";
    public static String A_NAME = "name";

    public static String ENTITY_FIELD = "EntityField";
    public static String EF_ENTITY_NAME = "entity-name";
    public static String EF_FIELD_NAME = "field-name";
	public static String EF_FIELD_FORMAT = "format";

    public static String TEXT = "Text";
    public static String TEXT_VALUE = "value";

    public static String ENTITY_FILTER = "EntityFilter";
    public static String FILTER_FIELD_NAME = "field-name";
    public static String FOREIGN_KEY = "ForeignKey";
    public static String VALUE_LITERAL = "ValueLiteral";

    public static String VALUE_INPUT = "ValueInput";

    public static String FK_ENTITY = "entity-name";
    public static String FK_FIELD = "field-name";
    public static String FK_ELEMENT = "element-ref";
	public static String FK_FIELD_FORMAT = "format";

    public static String VL_VALUE = "value";
	public static String VL_FIELD_FORMAT = "format";

    public static String VI_INPUT = "input";
    public static String VI_FIELD_FORMAT = "format";

    public static String CHILD_ELEMENT = "ChildElement";
    public static String CE_NAME = "element-name";
    public static String CE_REQUIRED = "required";
    public static String CE_MULTIPLES = "multiplicity";

    public static String REPEATABLE = "Repeatable";
    public static String R_TYPE = "type";
    public static String R_ONE_MANY = "one_many";
    public static String R_NONE_MANY = "none_many";

    public static String INCLUDE = "Include";
    public static String I_OPERATOR = "operator";


    public static String CARDINALITY = "Cardinality";
    public static String C_REQUIRED = "required";
    public static String C_MULTIPLES = "multiples";

    public static String CONDITION = "Condition";
    public static String C_OPERATOR = "operator";
    public static String C_ELEMENT_NAME = "conditional-element";

    public static String AND = "and";
    public static String OR = "or";
}

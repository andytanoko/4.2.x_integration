/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXMLDBConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 16 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.config;

public interface IXMLDBConfig
{
    public static final String XMLDB_CONFIG = "xmldb";

    /**
     * The directory where dtds are present
     */
    public static final String DTDS_DIR = "dtds.dir";

    /**
     * The mapping files directory
     */
    public static final String MAPPINGS_DIR = "mappings.dir";

    /**
     * XML to DB Mapping file dtd
     */
    public static final String XMLDB_MAPPING_FILE_DTD = "xmldb.mapping.file.dtd";

    /**
     * DB to XML mapping file dtd
     */
    public static final String DBXML_MAPPING_FILE_DTD = "dbxml.mapping.file.dtd";
}
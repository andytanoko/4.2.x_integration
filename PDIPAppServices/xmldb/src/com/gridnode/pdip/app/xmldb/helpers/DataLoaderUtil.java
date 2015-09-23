/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataLoaderUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import java.util.*;

import java.io.*;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.gridnode.pdip.app.xmldb.db.mapping.XMLDBMappingFile;

public class DataLoaderUtil
{

    private static final String PARSER = "org.apache.xerces.parsers.SAXParser";

    /**
     * Returns the XML to DB mapping file model.
     */
    public static XMLDBMappingFile getMappingFileModel(String mappingFile) throws Exception
    {
        SAXBuilder builder = new SAXBuilder(PARSER, true);
        String src = XMLDBConfigUtil.getMappingsFolderPath() +
            File.separatorChar;
        Document document = builder.build(new FileInputStream(src + mappingFile),
            XMLDBConfigUtil.getXMLDBMappingFileDTD().getCanonicalPath());
        return new XMLDBMappingFile(document);
    }

}

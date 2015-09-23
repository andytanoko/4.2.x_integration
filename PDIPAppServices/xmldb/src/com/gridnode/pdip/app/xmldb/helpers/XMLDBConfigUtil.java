/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDBConfigUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 16 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import com.gridnode.pdip.app.xmldb.config.IXMLDBConfig;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

import java.io.File;

/**
 * Utility class to get the file objects of the directories configured
 * in the properties file
 */
public class XMLDBConfigUtil
{

    public static String getMappingsFolderPath()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(IXMLDBConfig.XMLDB_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(IXMLDBConfig.MAPPINGS_DIR));
            if (!file.exists())
            {
                file.mkdir();
            }
            return file.getCanonicalPath();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDTDsFolderPath()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(IXMLDBConfig.XMLDB_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(IXMLDBConfig.DTDS_DIR));
            if (!file.exists())
            {
                file.mkdir();
            }
            return file.getCanonicalPath();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static File getXMLDBMappingFileDTD()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(IXMLDBConfig.XMLDB_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(IXMLDBConfig.XMLDB_MAPPING_FILE_DTD));
            if (!file.exists())
            {
                return null;
            }
            return file;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static File getDBXMLMappingFileDTD()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(IXMLDBConfig.XMLDB_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(IXMLDBConfig.DBXML_MAPPING_FILE_DTD));
            if (!file.exists())
            {
                return null;
            }
            return file;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}

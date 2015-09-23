/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileUploadUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 26 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.upload;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class which will be used to retrieve the files uploaded from
 * the client using browser. Uses the MultipartRequest class. The temp
 * directory to save the uploaded files is configured in a properties file
 * configured in ITempDirConfig interface
 */
public class FileUploadUtil
{

    public static String getTempFolderPath()
    {
        try
        {
            ConfigurationManager configManager =
                ConfigurationManager.getInstance();
            Configuration config =
                configManager.getConfig(ITempDirConfig.TEMP_DIR_CONFIG);

            java.io.File file =
                new java.io.File(config.getString(ITempDirConfig.TEMP_DIR));
            if (!file.exists())
            {
                file.mkdir();
            }
            return file.getCanonicalPath();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static DSMultipartRequest getMultipartRequest(HttpServletRequest request)
        throws Exception
    {
        DSMultipartRequest mp = new DSMultipartRequest();
        mp.handleRequest(request);
        return mp;
    }
}

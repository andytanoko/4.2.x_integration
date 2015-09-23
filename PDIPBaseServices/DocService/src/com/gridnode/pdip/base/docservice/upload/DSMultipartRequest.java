/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MultipartRequestHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 04 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.upload;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

import org.apache.struts.upload.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * This class handles the multipart request. Hides the
 * DiskMultipartRequestHandler from other packages
 */
public class DSMultipartRequest extends DiskMultipartRequestHandler
{

    protected void retrieveTempDir()
    {
        tempDir = FileUploadUtil.getTempFolderPath();
    }

    protected long getMaxSizeFromServlet()
    {
        return 1024 * 1024;
    }

    public String getParameter(String name)
    {
        if (textElements == null)
        {
            return null;
        }
        String[] values = (String[]) textElements.get(name);
        if (values == null || values.length == 0)
        {
            return null;
        }
        return values[0];
    }

    public Enumeration getFileNames()
    {
        if (fileElements == null)
        {
            return null;
        }
        return fileElements.keys();
    }

    public int getFileCount()
    {
        if (fileElements == null)
        {
            return 0;
        }
        return fileElements.size();
    }

    public String getOriginalFileName(String name)
    {
        if (fileElements == null)
        {
            return null;
        }
        DiskFile file = (DiskFile) fileElements.get(name);
        if (file == null)
        {
            return null;
        }
        return file.getFileName();
    }

    public File getFile(String name)
    {
        if (fileElements == null)
        {
            return null;
        }
        DiskFile file = (DiskFile) fileElements.get(name);
        if (file == null)
        {
            return null;
        }
        return new File(file.getFilePath());
    }

    public void handleRequest(HttpServletRequest request) throws ServletException {

        retrieveTempDir();

        MultipartIterator iterator = new MultipartIterator(request, -1,
                                                            getMaxSizeFromServlet(),
                                                            tempDir);
        MultipartElement element;

        textElements = new Hashtable();
        fileElements = new Hashtable();
        allElements = new Hashtable();

        try {
            while ((element = iterator.getNextElement()) != null) {
                if (!element.isFile()) {

                    if (request instanceof MultipartRequestWrapper) {
                        ((MultipartRequestWrapper) request).setParameter(element.getName(),
                                                                         element.getValue());
                    }
                    String[] textValues = (String[]) textElements.get(element.getName());

                    if (textValues != null) {
                        String[] textValues2 = new String[textValues.length + 1];
                        System.arraycopy(textValues, 0, textValues2, 0, textValues.length);
                        textValues2[textValues.length] = element.getValue();
                        textValues = textValues2;
                    }
                    else {
                        textValues = new String[1];
                        textValues[0] = element.getValue();
                    }
                    textElements.put(element.getName(), textValues);
                    allElements.put(element.getName(), textValues);
                }
                else {

                     File tempFile = element.getFile();
                     if (tempFile.exists()) {
                         DiskFile theFile = new DiskFile(tempFile.getAbsolutePath());
                         theFile.setContentType(element.getContentType());
                         theFile.setFileName(element.getFileName());
                         theFile.setFileSize((int) tempFile.length());
                         fileElements.put(element.getName(), theFile);
                         allElements.put(element.getName(), theFile);
                     }
                }
            }
        }
        catch (UnsupportedEncodingException uee) {
            throw new ServletException("Encoding \"ISO-8859-1\" not supported");
        }

    }
}

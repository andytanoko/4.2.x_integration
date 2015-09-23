/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLGeneratorUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import org.jdom.*;
import org.saxpath.*;
import org.saxpath.helpers.*;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;

import java.util.*;
import java.io.*;

import com.gridnode.pdip.app.xmldb.xml.mapping.*;
import com.gridnode.pdip.app.xmldb.db.Entity;
import com.gridnode.pdip.app.xmldb.exceptions.*;
import com.gridnode.pdip.app.xmldb.xml.ElementContent;

import com.gridnode.pdip.framework.db.meta.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

public class XMLGeneratorUtil
{

    private static final String PARSER = "org.apache.xerces.parsers.SAXParser";

    /**
     * Returns the DB to XML mapping file model
     */
    public static DBXMLMappingFile getMappingFileModel(String mappingFile) throws Exception
    {
        SAXBuilder builder = new SAXBuilder(PARSER, true);
        String src = XMLDBConfigUtil.getMappingsFolderPath() +
            File.separatorChar;
        Document document = builder.build(new FileInputStream(src + mappingFile),
            XMLDBConfigUtil.getDBXMLMappingFileDTD().getCanonicalPath());
        return new DBXMLMappingFile(document);
    }

    /**
     * Generates the xml file. Creates the root element and calls the
     * findChildren method.
     */
    public static Element generateXMLDocument(String mappingFile,
        HashMap input) throws Throwable
    {
		System.out.println("Inside Generate XML Docuement " +mappingFile);
	    System.out.println("Inside Generate XML Docuement input= " +input);
        
		DBXMLMappingFile mapping =
            XMLGeneratorUtil.getMappingFileModel(mappingFile);

//System.out.println("MApping is= " +mapping);

        DocumentElement docElement = mapping.getRoot();

//System.out.println("Root is= " +docElement);

        DBXMLMappings mappings = docElement.getMappings();
//System.out.println("mappings is= " +mappings);

        ElementContent rootElement = null;
        if (mappings == null)
        {

            rootElement = new ElementContent(docElement, null, input);
        }
        else
        {
            if (mappings.getEntities() == null)
            {
                rootElement = new ElementContent(docElement, null, input);
            }
            else
            {
				//Modified by sri
				rootElement = new ElementContent(docElement, null, input);
                Collection c = mappings.getEntityMaps(rootElement);
//System.out.println("Collection is= " +c);

                if (c.isEmpty())
                {
                    throw new EntityNotFoundException("Entity matching " +
                        "root not found ");
                }
                else if (c.size() > 1)
                {
                    throw new MultipleRootEntitiesException("Multiple " +
                        "entities matching root found ");
                }
                HashMap eMap = (HashMap) c.iterator().next();
                rootElement = new ElementContent(docElement, eMap,
                    input);
            }
        }
//		System.out.println("Before Find Children in XML Generator util");
        rootElement.findChildren();
//		System.out.println("Root Element in XML Generator util is "+rootElement);
        return rootElement;
    }

    /**
     * Writes the DOM representation to file.
     */
    public static File generateXMLFile(Element root, String dtdFile)
        throws Exception
    {
        root = root.detach();
        Document doc = new Document(root);
        if (dtdFile != null && !dtdFile.equals(""))
        {
            DocType docType = new DocType(root.getName(), null, dtdFile);
            doc.setDocType(docType);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLOutputter xmlout = new XMLOutputter();
        xmlout.setEncoding("UTF-8");
        xmlout.setNewlines(true);
        xmlout.setIndent(true);
        xmlout.output(doc, baos);
        File file = File.createTempFile("xmldb", ".xml");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.close();
        return file;
    }
}
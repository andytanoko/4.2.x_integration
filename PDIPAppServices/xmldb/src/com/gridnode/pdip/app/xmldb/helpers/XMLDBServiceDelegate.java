/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDBServiceDelegate.java
 *
 ****************************************************************************
 * Date           Author          Changes
 ****************************************************************************
 * Dec 30 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import com.gridnode.pdip.app.xmldb.db.DataLoader;
import com.gridnode.pdip.app.xmldb.db.mapping.XMLDBMappingFile;
import com.gridnode.pdip.framework.log.Log;

import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;

/**
 * Handles all method calls from session bean
 */
public class XMLDBServiceDelegate
{
	static final String LogCat = "XMLDB";
    public static Collection insertDataFromXML(String xmlFile, String dtdFile,
        String mappingFile) throws Throwable
    {
		Log.debug(LogCat, "Inside XMLDBServiceDelegate.insertDataFromXML");
		Log.debug(LogCat, "Inside XMLDBServiceDelegate.xmlFile" + xmlFile);
		Log.debug(LogCat, "Inside XMLDBServiceDelegate.dtdFile" + dtdFile);
		Log.debug(LogCat, "Inside XMLDBServiceDelegate.mappingFile" + mappingFile);

		try
		{
			XMLDBMappingFile mapping = DataLoaderUtil.getMappingFileModel(mappingFile);

			Log.debug(LogCat, "After mapping file " + mappingFile);
			Document doc = null;
			doc = XMLDocumentUtility.getDocument(xmlFile);
			Log.debug(LogCat, "Before loading the DataLoader in XMLDBServiceDelegate.insertDataFromXML" + doc);
	/*
			if (dtdFile == null || dtdFile.equals(""))
			{
				doc = XMLDocumentUtility.getDocument(xmlFile);
			}
			else
			{
				doc = XMLDocumentUtility.getDocument(xmlFile, dtdFile);
			}
	*/

			DataLoader loader = new DataLoader(doc, mapping);
			Collection coll = loader.populate();
//	System.out.println("the final collection is : " + coll);
			return coll;
		}
		catch(Exception e)
		{
			System.out.println("Exception occured inside serviceDelegate : " + e);
			e.printStackTrace();
			throw e;
		}
//		Log.debug(LogCat, "After loading the DataLoader in XMLDBServiceDelegate.insertDataFromXML");
    }

    public static File generateXML(String mappingFile, HashMap inputs, String dtdFile)
        throws Throwable
    {
		try
		{
System.out.println("mappingFile = " + mappingFile);
System.out.println("dtdFile = " + dtdFile);
System.out.println("inputs = " + inputs);
	        Element root = XMLGeneratorUtil.generateXMLDocument(mappingFile, inputs);
//System.out.println("root = " + root);
	        return XMLGeneratorUtil.generateXMLFile(root, dtdFile);
		}
		catch(Throwable th)
		{
			System.out.println("Exception thrown while trying to build the XML file" + th);
			throw th;
		}
    }


	public void generateXML(String dtdFile, String mappingFile)
	{
		System.out.println("Inside generateXML(String dtdFile, String mappingFile)");
	}


}

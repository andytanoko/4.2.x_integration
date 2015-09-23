/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaZipFileEntryHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.mapper.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * A handler class for extracting the zip entry from the batch import schema zip file. It help to 
 * categorize the filename(s) based on the path(s) represent by the zip entry(s).
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class SchemaZipFileEntryHandler
{
	private static SchemaZipFileEntryHandler _zipEntryHandler = new SchemaZipFileEntryHandler();
	
	private SchemaZipFileEntryHandler() {}
	
	public static SchemaZipFileEntryHandler getInstance()
	{
		return _zipEntryHandler;
	}
	
	/**
	 * Return a hashMap with the path as the key, value as a collection of filename(s) 
	 * @param schemaZipFile
	 * @return
	 */
	public HashMap<String, ArrayList<String> > getZipFileEntry(File schemaZipFile)
		throws ApplicationException, IOException
	{
		ZipFile schemaZip = null;
		try
		{
			HashMap<String, ArrayList<String> > zipEntryMap = new HashMap();
			schemaZip = new ZipFile(schemaZipFile);
			Enumeration zipEntries = schemaZip.entries();
			while(zipEntries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry)zipEntries.nextElement();
				addZipEntry(zipEntryMap, entry);
			}
			return zipEntryMap;
		}
		catch(Exception ex)
		{
			Logger.warn("[SchemaZipFileEntryHandler.getZipFileEntry] Error occured while extracting the zip entries from file "+schemaZipFile.getAbsolutePath());
			throw new ApplicationException("Error getting zip entries from file "+schemaZipFile.getAbsolutePath(), ex);
		}
		finally
		{
			if(schemaZip != null)
			{
				schemaZip.close();
			}
		}
	}
	
	/**
	 * Add the zip entry into the hashmap. ZipEntry will be seperated into path(the key) and filename(the value) if any
	 * @param zipEntryMap
	 * @param entry
	 */
	private void addZipEntry(HashMap<String, ArrayList<String> > zipEntryMap, ZipEntry entry)
	{
		String zipEntryName = entry.getName();
		String path = getZipEntryPath(zipEntryName);
		String filename = getZipEntryFilename(zipEntryName);
		
		if(zipEntryMap.containsKey(path))
		{
			addToFilenameList(zipEntryMap.get(path), filename);
		}
		else
		{
			ArrayList<String> filenameList = new ArrayList<String>();
			filenameList.add(filename);
			zipEntryMap.put(path, filenameList);
		}
	}
	
	private void addToFilenameList(ArrayList<String> filenameList, String filename)
	{
		filenameList.add(filename);
	}
	
	/**
	 * Get the path of the zip entry if any
	 * @param zipEntryName
	 * @return
	 */
	private String getZipEntryPath(String zipEntryName)
	{
		int index = Math.max(zipEntryName.lastIndexOf("/"), 
		                           zipEntryName.lastIndexOf("\\"));
		if(index >=0 )
		{
			return zipEntryName.substring(0, index+1);
		}
		else
		{
			return ""; //zip entry is the filename. dun have path
		}
	}
	
	/**
	 * Get the filename of the zip entry
	 * @param zipEntryName
	 * @return
	 */
	private String getZipEntryFilename(String zipEntryName)
	{
		int index = Math.max(zipEntryName.lastIndexOf("/"), 
                         zipEntryName.lastIndexOf("\\"));
		if(index >= 0)
		{
			return zipEntryName.substring(index+1);
		}
		else
		{
			return zipEntryName; //zip entry is the filename. dun have path
		}
	}
	
	/*
	public static void main(String args[])
		throws Exception
	{
		SchemaZipFileEntryHandler handler  = SchemaZipFileEntryHandler.getInstance();
		HashMap<String, ArrayList<String>> map = handler.getZipFileEntry(new File("E:/wei xiang/test;.gts.zip"));
		
		Set<String> keys = map.keySet();
		for(String s : keys)
		{
			System.out.println("Entry path is "+s);
			
			ArrayList<String> filenameList = map.get(s);
			for(String name : filenameList)
			{
				System.out.println("filename "+ name);
			}
		}
	} */ 
}

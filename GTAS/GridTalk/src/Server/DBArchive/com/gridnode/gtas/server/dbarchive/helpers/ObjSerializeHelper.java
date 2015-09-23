/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjSerializeHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21, 2005   Tam Wei Xiang       Created
 * Aug 30, 2006   Tam Wei Xiang       Moved from estore stream.
 * Aug 31, 2006   Tam Wei Xiang       To make the method serialize/deserialize
 *                                    support parameterized type     
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 *
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since GT 2.4.7
 */
public class ObjSerializeHelper
{	
	public static<T> void serialize(ArrayList<T> objToSerialize, String filename, String pathKey)
		throws Exception
	{
		FileOutputStream output = null;
		ObjectOutputStream objOut = null;
		try
  	{
  		File serializeF = FileUtil.createNewLocalFile(pathKey,"",filename);
  		
  		output = new FileOutputStream(serializeF);
  		objOut = new ObjectOutputStream(output);
  		for(int i = 0; i < objToSerialize.size(); i++)
  		{
  			objOut.writeObject(objToSerialize.get(i));
  		}
  	}
  	catch(Exception ex)
  	{
  		throw new ApplicationException("[ObjSerializeHelper.serialize] Unable to serialize obj to a file.", ex);
  	}
  	finally
  	{
  		if(output != null)
  		{
  			output.close();
  		}
  		if(objOut != null)
  		{
  			objOut.flush();
  			objOut.close();
  		}
  	}
	}
	
	public static<T> ArrayList<T> deserialize(String filename, String pathKey, Class<T> castType)
		throws Exception
	{
		FileInputStream input = null;
		ObjectInputStream inputObj = null;
		try
		{
			File fileToDeserialize = FileUtil.getFile(pathKey, filename);
			input = new FileInputStream(fileToDeserialize);
			inputObj = new ObjectInputStream(input);
			ArrayList<T> deserializeObj = new ArrayList<T>(2000);
		
			while(true)
			{
				try
				{
					T obj = castType.cast(inputObj.readObject());
					deserializeObj.add(obj);
				}
				catch(EOFException ex)
				{
					Logger.log("[ObjSerializeHelper.deserialize] file "+fileToDeserialize.getAbsolutePath()+" has reached end of file.");
					break;
				}
			}
			return deserializeObj;
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ObjSerializeHelper.deserialize] Unable to deserialize obj from a file "+filename, ex);
		}
		finally
		{
			if(input != null)
			{
				input.close();
			}
			if(inputObj != null)
			{
				inputObj.close();
			}
		}
	}	
}

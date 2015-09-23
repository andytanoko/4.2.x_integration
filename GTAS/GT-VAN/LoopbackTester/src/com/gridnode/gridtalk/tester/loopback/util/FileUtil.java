/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileUtil.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 23, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.util;

import java.io.File;

/**
 * Manager class to handle file system
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class FileUtil
{
	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_CONF = 1;
	public static final int TYPE_DATA = 2;
	
	private static final String DEFAULT_DIR = "."+File.separator;
	private static final String BASE_DIR = DEFAULT_DIR+"glbt"+File.separator;
	private static final String DATA_DIR = BASE_DIR+"data"+File.separator;
	private static final String CONF_DIR = BASE_DIR+"conf"+File.separator;
	
	/**
	 * 
	 */
	public FileUtil()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public static File getFile(int type, String fileName)
	{
		File f = null;
		switch(type)
		{
			case TYPE_DEFAULT:
				f = new File(BASE_DIR+fileName);
				break;
			case TYPE_CONF:
				f = new File(CONF_DIR+fileName);
				break;
			case TYPE_DATA:
				f = new File(DATA_DIR+fileName);
				break;
			default:
				f = new File(DEFAULT_DIR+fileName);
		}
		return f;
	}
}

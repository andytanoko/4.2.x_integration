/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CodeValueHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * A helper class to insert the CodeValue obj to DB, retrieve a list of them.
 * 
 * @author Tam Wei Xiang
 * @since GT 2.4.9
 */
public class CodeValueHelper
{
	private static CodeValueHelper cvHelper;
	
	private CodeValueHelper() {}
	
	public static CodeValueHelper getInstance()
	{
		if(cvHelper ==null)
		{
			cvHelper = new CodeValueHelper();
		}
		return cvHelper;
	}
	
	/**
	 * Add the CodeValue obj into DB
	 * @param cv
	 * @throws CreateEntityException
	 */
	public void addCodeValue(CodeValue cv)
		throws CreateEntityException
	{
		try
		{
			if(cv!=null)
			{				
				getCodeValueHome().create(cv);
			}
		}
		catch(Exception ex)
		{
			throw new CreateEntityException("[CodeValueHelper.addCodeValue] Error occured while adding CodeValue with entityType "+cv.getEntityType()+" and fieldID "+cv.getFieldID(), ex);
		}
	}
	
	/**
	 * Return a list of CodeValue obj given the entiyType and fieldID.
	 * @param entityType The entity name
	 * @param fieldID The fieldID of the entity that we wanna populate to the list box or drop down list.
	 * @return Collection of CodeValue objects found.
	 * @throws FindEntityException
	 */
	public Collection<CodeValue> retrieveByEntityTypeAndFieldID(String entityType, Integer fieldID)
		throws FindEntityException
	{
		try
		{
			//Log.log(Log.DB, "Entity type = "+entityType+" fieldID = "+fieldID);
			ArrayList<CodeValue> codeValue = new ArrayList<CodeValue>();
			Collection c = getCodeValueHome().findByEntityTypeAndFieldID(entityType, fieldID);
			Iterator i = c.iterator();
			while(i.hasNext())
			{
				ICodeValueObj cv = (ICodeValueObj)i.next();
				codeValue.add(new CodeValue(cv.getCode(), cv.getDescription(), cv.getEntityType(),
						                        cv.getFieldID()));
			}
			//Log.log(Log.DB, "size is "+codeValue.size());
			return codeValue;
		}
		catch(Exception ex)
		{
			throw new FindEntityException("[CodeValueHelper.retrieveByEntityTypeAndFieldID] Cannot retrieve code value obj with **entityType = "+ entityType+" and **fieldID = "+ fieldID, ex);
		}
	}
	
	/**
	 * Retrieve a list of code value obj given the code, entityType, fieldID
	 * @param code the code value eg for DocumentMetaInfo's DocType, its code can be 3A4_Maxtor
	 * @param entityType the class name of an entity
	 * @param fieldID The fieldID of the entity that we wanna populate to the list box or drop down list.
	 * @return Collection of CodeValue objects found.
	 * @throws FindEntityException
	 */
	public Collection<CodeValue> retrieveByCodeEntityTypeAndFieldID(String code, String entityType,
			                                                   Integer fieldID)
		throws FindEntityException
	{
		try
		{
			ArrayList<CodeValue> codeValue = new ArrayList<CodeValue>();
			Collection c = getCodeValueHome().findByCodeEntTypeAndFieldID(code, entityType, fieldID);
			Iterator i = c.iterator();
			while(i.hasNext())
			{
				ICodeValueObj cv = (ICodeValueObj)i.next();
				codeValue.add(new CodeValue(cv.getCode(), cv.getDescription(), cv.getEntityType(),
						                        cv.getFieldID()));
			}
			return codeValue;
		}
		catch(Exception ex)
		{
			throw new FindEntityException("[CodeValueHelper.retrieveByCodeEntityTypeAndFieldID] Cannot retrieve code value obj with ** code = "+code+" **entityType = "+ entityType+" and **fieldID = "+ fieldID, ex);
		}
	}
	
	/**
	 * retrieve the code value record using its PK - the UID
	 * @param UID
	 * @return The CodeValue found based on the specified UID
	 * @throws FindEntityException
	 */
	public CodeValue retrieveByUID(Long UID)
		throws FindEntityException
	{
		try
		{
			ICodeValueObj cv = getCodeValueHome().findByPrimaryKey(UID);
			if(cv == null)
			{
				return null;
			}
			return new CodeValue(cv.getCode(), cv.getDescription(), cv.getEntityType(), cv.getFieldID());
		}
		catch(Exception ex)
		{
			throw new FindEntityException("[CodeValueHelper.retrieveByUID] Unable to find CodeValue with UID "+UID);
		}
	}
	
	/**
	 * Check if the codevalue exist in db or not
	 * @param cv The CodeValue to check
	 * @return <b>true</b> if CodeValue record exists in db, <b>false</b> otherwise.
	 */
	public boolean isExist(CodeValue cv)
		throws FindEntityException
	{
		try
		{
			Collection c = this.retrieveByCodeEntityTypeAndFieldID(cv.getCode(), cv.getEntityType(),cv.getFieldID());
			if(c!= null && c.size()>0)
			{
				return true;
			}
			return false;
		}
		catch(Exception ex)
		{
			throw new FindEntityException("[CodeValueHelper.isExist]", ex);
		}
	}
	
	
	/**
	 * Obtain the home interface of CodeValueBean
	 *
	 */
	private ICodeValueHome getCodeValueHome()
		throws ServiceLookupException
	{
		return (ICodeValueHome)ServiceLocator.instance(
				ServiceLocator.CLIENT_CONTEXT).getHome(
						ICodeValueHome.class.getName(),ICodeValueHome.class);
	}
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args)
		throws Exception
	{
		CodeValueHelper cvh = new CodeValueHelper();
		CodeValue cv = new CodeValue("3A4_MAxtor", "3a4 des", "GridDocument", new Integer(12));
		cvh.addCodeValue(cv);
		
		cv = new CodeValue("3C3", "3c3 des", "GridDocument", new Integer(12));
		cvh.addCodeValue(cv);
		
		cv = new CodeValue("4C1", "4c1 des", "GridDocument", new Integer(12));
		cvh.addCodeValue(cv);
		
		Collection c = cvh.retrieveByEntityTypeAndFieldID("GridDocument", new Integer(12));
		printC(c);
	}

	public static void printC(Collection c)
	{
		System.out.println("entering c's size is "+c.size());
		Iterator i = c.iterator();
		while(i.hasNext())
		{
			CodeValue cv = (CodeValue)i.next();
			System.out.println("des is "+cv.getDescription());
		}
	} */
}

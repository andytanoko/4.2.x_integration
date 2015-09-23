/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KeyGenBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2005    i00107              Created
 * Nov 13 2006    i00107              GNDB00027928: change getNextId(String,Collection)
 *                                    to getNextId(String, Long)
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                   
 */
package com.gridnode.pdip.framework.db.keygen;

import java.util.Collection;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * Replaces the KeyGeneratorBean which is a CMP bean. 
 * 
 * @author i00107
 * @version GT 4.0 VAN
 */
public class KeyGenBean implements SessionBean
{
	private SessionContext sc;
	
	public void setSessionContext(SessionContext arg0)
	{
		sc = arg0;
	}

	public void ejbRemove()
	{
		sc = null;
	}

	public void ejbActivate()
	{
	}

	public void ejbPassivate()
	{
	}

	public void ejbCreate()
	{
		
	}
	
	/**
	 * Get the next id for the specified reference name
	 * @param refName The reference name
	 * @return The next id for the reference
	 * @throws SystemException
	 */
	public long getNextId(String refName) throws SystemException
	{
		try
		{
			return KeyGenDAO.getInstance().getNextId(refName);
		}
		catch (SystemException ex)
		{
			Log.warn("[KeyGenBean.getNextId] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.warn("[KeyGenBean.getNextId] Unexpected error", ex);
			throw new SystemException(ex);
		}
	}
	
	/**
	 * Get the next id for the specified reference name
	 * @param refName The reference name
	 * @param excludeKeySet The set of ids to exclude from return
	 * @return The next id for the reference, guaranteed to be not in the excludeKeySet
	 * @throws SystemException
	 *//*
	public long getNextId(String refName, Collection excludeKeySet) throws SystemException
	{
		try
		{
			return KeyGenDAO.getInstance().getNextId(refName, excludeKeySet);
		}
		catch (SystemException ex)
		{
			Log.err("[KeyGenBean.getNextId] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.err("[KeyGenBean.getNextId] Unexpected error", ex);
			throw new SystemException(ex);
		}
	}*/

  /**
   * Get the next id for the specified reference name
   * @param refName The reference name
   * @param currMaxId The current max Id for the reference name, may be <b>null</b>.
   * @return The next id for the reference, guaranteed to be greater than the specified
   * currMaxId, if specified.
   * @throws SystemException
   */
  public long getNextId(String refName, Long currMaxId) throws SystemException
  {
    try
    {
      return KeyGenDAO.getInstance().getNextId(refName, currMaxId);
    }
    catch (SystemException ex)
    {
      Log.warn("[KeyGenBean.getNextId] SystemException", ex);
      throw ex;
    }
    catch (Exception ex)
    {
      Log.warn("[KeyGenBean.getNextId] Unexpected error", ex);
      throw new SystemException(ex);
    }
  }

}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MetaInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 */
package com.gridnode.pdip.framework.db.meta;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ReflectionUtility;

/**
 * 
 * @author Neo Sok Lay
 * @since
 * @version GT 4.0 VAN
 */
public class MetaInfoBean implements SessionBean
{
	private SessionContext sc = null;
	
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
	
	private EntityMetaInfoDAO getEmiDAO() throws Exception
	{
		return EntityMetaInfoDAO.getInstance();
	}
	
	private FieldMetaInfoDAO getFmiDAO() throws Exception
	{
		return FieldMetaInfoDAO.getInstance();
	}
	
	/**
	 * Find EntityMetaInfo by object name
	 * @param objectName The object name
	 * @return The EntityMetaInfo found
	 * @throws SystemException
	 */
	public EntityMetaInfo findByObjectName(String objectName) throws SystemException
	{
		EntityMetaInfo emi = null;
		try
		{
			emi = getEmiDAO().findByObjectName(objectName);
	    loadFmi(emi);
	    
	    return emi;
		}
		catch(ClassNotFoundException e)
		{
			Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, "[MetaInfoBean.findByObjectName] class not found "+objectName, e);
		}
		catch (SystemException ex)
		{
			Log.warn("[MetaInfoBean.findByObjectName] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.warn("[MetaInfoBean.findByObjectName] Unexpected error", ex);
			throw new SystemException(ex);
		}
    return emi;
	}
	
	private void loadFmi(EntityMetaInfo emi) throws Exception
	{
    Class entityClass = null;
    
    entityClass = Class.forName(emi.getObjectName());
    Collection fieldMeta = getFieldMetaInfo(emi.getObjectName());
    if(fieldMeta!=null)
    {
    	for (Iterator i=fieldMeta.iterator(); i.hasNext(); )
    	{
    		FieldMetaInfo fieldMetaInfo = (FieldMetaInfo)i.next();
    		
    		// determine fieldID
    		Number fieldId = getFieldId(entityClass, fieldMetaInfo.getFieldName());
    		fieldMetaInfo.setFieldId(fieldId);
    		
    		emi.addFieldMetaInfo(fieldMetaInfo);
    	}
    }
	}
	
  /**
   * Retrieve the FieldMetaInfo(s) with the specified entity object name
   *
   * @return Collection of IFieldMetaInfoObj(s)
   */
  private Collection getFieldMetaInfo(String entityObjectName)
  {
    try
    {
      return getFmiDAO().findByEntityName(entityObjectName);
    }
    catch(Exception ex)
    {
      Log.error(ILogErrorCodes.FIELD_META_INFO_RETRIEVE, Log.DB, "[MetaInfoBean.getFieldMetaInfo] Error in getting related FieldMetaInfo for Entity "+entityObjectName, ex);
    }
    return null;
  }

  /**
   * Find EntityMetaInfo by entity name
   * @param entityName The entity name
   * @return The EntityMetaInfo found
   * @throws SystemException
   */
	public EntityMetaInfo findByEntityName(String entityName) throws SystemException
	{
		EntityMetaInfo emi = null;
		String objectName = null;
		try
		{
			emi = getEmiDAO().findByEntityName(entityName);
			objectName = emi.getObjectName();
	    loadFmi(emi);
		}
		catch(ClassNotFoundException e)
		{
			Log.error(ILogErrorCodes.ENTITY_META_INFO_READ,"[MetaInfoBean.findByEntityName] class not found "+objectName, e);
		}
		catch (SystemException ex)
		{
			Log.warn("[MetaInfoBean.findByEntityName] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.warn("[MetaInfoBean.findByEntityName] Unexpected error", ex);
			throw new SystemException(ex);
		}
    return emi;
		
	}
	
	/**
	 * Find all EntityMetaInfo in the system
	 * @return Collection of EntityMetaInfo
	 * @throws SystemException
	 */
	public Collection findAllEntityMetaInfo() throws SystemException
	{
		Collection emiColls = null;
		String objectName = null;
		try
		{
			emiColls = getEmiDAO().findAll();
			if (emiColls != null)
			{
				for (Iterator i=emiColls.iterator(); i.hasNext(); )
				{
					EntityMetaInfo emi = (EntityMetaInfo)i.next();
					objectName = emi.getObjectName();
			    loadFmi(emi);
				}
			}
	    
		}
		catch(ClassNotFoundException e)
		{
			Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, "[MetaInfoBean.findAllEntityMetaInfo] class not found "+objectName, e);
		}
		catch (SystemException ex)
		{
			Log.warn("[MetaInfoBean.findAllEntityMetaInfo] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.warn("[MetaInfoBean.findAllEntityMetaInfo] Unexpected error", ex);
			throw new SystemException(ex);
		}
    return emiColls;		
	}
	
	/**
	 * Find FieldMetaInfo by label
	 * @param label The label
	 * @return Collection of FieldMetaInfo
	 * @throws SystemException
	 */
	public Collection findFieldMetaInfoByLabel(String label) throws SystemException
	{
		try
		{
			return getFmiDAO().findByLabel(label);
		}
		catch (SystemException ex)
		{
			Log.warn("[MetaInfoBean.findFieldMetaInfoByLabel] SystemException", ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Log.warn("[MetaInfoBean.findFieldMetaInfoByLabel] Unexpected error", ex);
			throw new SystemException(ex);
			
		}
	}
	
	
  /**
   * Get the FieldId for a field in the Entity class. The FieldId must be declared
   * as a Number type.
   *
   * @param entityClass The Entity class.
   * @param fieldName The name of the Field
   * @return the FieldId determined from the entityClass if one is found, or
   * <b>null</b> if no field exists with the specified fieldName.
   */
  private Number getFieldId(Class entityClass, String fieldName)
  {
    Number fieldId = null;
    if(entityClass != null)
    {
      Field f = ReflectionUtility.getAccessibleField(entityClass, fieldName);
      if(f != null)
      {
        try
        {
          fieldId=(Number)f.get(null); // the Field must be static
        }
        catch(IllegalAccessException e)
        {
          Log.error(ILogErrorCodes.ENTITY_CREATE,
                    Log.DB,
            "[MetaInfoBean.getFieldId] IllegalAccessException "+
            fieldName+" in class "+entityClass.getName(),
            e);
        }
      }
      else
        Log.debug(Log.DB,
          "[MetaInfoBean.getFieldId] No field for "+fieldName+
          "in class "+entityClass.getName());
    }
    return fieldId;
  }
}

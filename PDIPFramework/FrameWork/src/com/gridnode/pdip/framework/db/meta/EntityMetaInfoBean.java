/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDAOImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 25 2002    Mahesh              To use ObjectName from EntityMetaInfo since
 *                                    entityName is only ShortName and ObjectName is
 *                                    Class name.
 * Dec 11 2002    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup.
 *                                    Refactor.
 * Feb 07 2007		Alain Ah Ming				Use new error codes                                   
 */
package com.gridnode.pdip.framework.db.meta;

import com.gridnode.pdip.framework.db.ObjectMappingRegistry;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ReflectionUtility;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.lang.reflect.Field;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.*;

/**
 * Entity bean to manage persistency of the EntityMetaInfo.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public abstract class EntityMetaInfoBean implements EntityBean
{
  private EntityContext _ctx;

  public EntityMetaInfoBean()
  {
  }

  public void setEntityContext(EntityContext ctx)
  {
    _ctx = ctx;
  }

  public void unsetEntityContext()
  {
    _ctx = null;
  }

  public String ejbCreate(EntityMetaInfo data)
    throws CreateException
  {
    setEntityName(data.getEntityName());
    setSqlName(data.getSqlName());
    setObjectName(data.getObjectName());
    return null;
  }

  public void ejbPostCreate(EntityMetaInfo data)
  {
    FieldMetaInfo[] fieldMeta = data.getFieldMetaInfo();
    for (int i=0; fieldMeta!=null && i<fieldMeta.length; i++)
    {
      createFieldMeta(fieldMeta[i]);
    }
  }

  public void ejbRemove()
  {
    try
    {
      Collection fieldMeta = getFieldMetaInfo();
      if(fieldMeta!=null)
      {
        for (Iterator i=fieldMeta.iterator(); i.hasNext(); )
        {
          ((IFieldMetaInfoLocalObj)i.next()).remove();
        }
      }
    }
    catch(Exception ex)
    {
      Log.error(
        Log.DB,
        "[EntityMetaInfoBean.ejbRemove] Error in removing related FieldMetaInfo",
        ex);
    }
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbLoad()
  {
  }

  public void ejbStore()
  {
  }

  public abstract String getEntityName();
  public abstract void setEntityName(String entityName);

  public abstract String getSqlName();
  public abstract void setSqlName(String sqlName);

  public abstract String getObjectName();
  public abstract void setObjectName(String objectName);

  /**
   * Retrieve the FieldMetaInfo(s) that belong to the current EntityMetaInfo.
   *
   * @return Collection of IFieldMetaInfoObj(s)
   */
  public Collection getFieldMetaInfo()
  {
  	String mn = "getFieldMetaInfo";
    try
    {
      return getFieldMetaInfoHome().findByEntityName(getObjectName());
    }
		catch (ServiceLookupException e)
		{
      logError(ILogErrorCodes.FIELD_META_INFO_RETRIEVE,
                mn, 
                "Entity: "+getObjectName()+". EJB Home lookup error: "+e.getMessage(),e);			
		}
		catch (FinderException e)
		{
      logError(ILogErrorCodes.FIELD_META_INFO_RETRIEVE,
               mn, 
                "Entity: "+getObjectName()+". EJB Finder error: "+e.getMessage(),e);
		}
    catch(Throwable ex)
    {
      logError(ILogErrorCodes.FIELD_META_INFO_RETRIEVE,
               mn, 
                "Entity "+getObjectName()+". Unexpected error: "+ex.getMessage(),
        ex);
    }
    return null;
  }

  /**
   * Create a FieldMetaInfo in the database.
   *
   * @param fieldMeta The FieldMetaInfo to create.
   */
  private void createFieldMeta(FieldMetaInfo fieldMeta)
  {
    try
    {
      IFieldMetaInfoLocalObj fieldMetaInfo = getFieldMetaInfoHome().create(fieldMeta);
      getFieldMetaInfo().add(fieldMetaInfo);
    }
		catch (ServiceLookupException e)
		{
			Log.error(ILogErrorCodes.FIELD_META_INFO_CREATE, 
			          Log.DB,
			          "[EntityMetaInfoBean.createFieldMeta] Failed to lookup field meta info EJB Home interface. Error: "+e.getMessage(),
			          e);
		}
		catch (CreateException e)
		{
			Log.error(ILogErrorCodes.FIELD_META_INFO_CREATE, 
			          Log.DB,
			          "[EntityMetaInfoBean.createFieldMeta] Failed to create EJB Local Obj. Error: "+e.getMessage(),
			          e);
		}
    catch (Exception ex)
    {
      Log.error(
                ILogErrorCodes.FIELD_META_INFO_CREATE,
        Log.DB,
        "[EntityMetaInfoBean.createFieldMeta] Unexpected Error: "+ex.getMessage(),
        ex);
    }
  }

  /**
   * Obtain the Home interface for the FieldMetaInfo bean.
   * @return the Home interface of the FieldMetaInfo bean.
   */
  private IFieldMetaInfoLocalHome getFieldMetaInfoHome()
    throws ServiceLookupException
  {
    return (IFieldMetaInfoLocalHome)ServiceLocator.instance(
             ServiceLocator.LOCAL_CONTEXT).getHome(
              IFieldMetaInfoLocalHome.class.getName(),
              IFieldMetaInfoLocalHome.class);
  }

  /**
   * Get the current EntityMetaInfo value object of this EntityMetaInfoBean.
   *
   * @return The EntityMetaInfo value object.
   */
  public EntityMetaInfo getData()
  {
    EntityMetaInfo data = new EntityMetaInfo();
    data.setEntityName(getEntityName());
    data.setObjectName(getObjectName());
    data.setSqlName(getSqlName());

    Class entityClass = null;
    try
    {
      entityClass = Class.forName(getObjectName());
      Collection fieldMeta = getFieldMetaInfo();
      if(fieldMeta!=null)
      {
        for (Iterator i=fieldMeta.iterator(); i.hasNext(); )
        {
          FieldMetaInfo fieldMetaInfo = ((IFieldMetaInfoLocalObj)i.next()).getData();

          // determine fieldID
          Number fieldId = getFieldId(entityClass, fieldMetaInfo.getFieldName());
          fieldMetaInfo.setFieldId(fieldId);

          data.addFieldMetaInfo(fieldMetaInfo);
        }
      }
    }
    catch(ClassNotFoundException e)
    {
      Log.debug(Log.DB,
        "[EntityMetaInfoBean.getData] class not found "+getObjectName(),
        e);
    }

    return data;
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
          Log.error(
                  ILogErrorCodes.ENTITY_META_INFO_READ,
                  Log.DB,
            "[EntityMetaInfoBean.getFieldId] IllegalAccessException "+
            fieldName+" in class "+entityClass.getName(),
            e);
        }
      }
      else
        Log.debug(Log.DB,
          "[EntityMetaInfoBean.getFieldId] No field for "+fieldName+
          "in class "+entityClass.getName());
    }
    return fieldId;
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(EntityMetaInfoBean.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }

//  private static void logError(String errorCode, String methodName, String msg,)
}
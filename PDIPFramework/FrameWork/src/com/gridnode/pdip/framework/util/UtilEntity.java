/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 * Jul 15 2003    Neo Sok Lay         Add method: getKeyByFilterForReadOnly(IDataFilter).
 * Dec 13 2005   Tam Wei Xiang        To add in updateEntity(IEntity, boolean)
 * Feb 06 2007    Neo Sok Lay         Check system property 'entity.ejb.use.remote' to
 *                                    decide whether to use local or remote access when 
 *                                    'true' is passed in to useDef.
 */

package com.gridnode.pdip.framework.util;


import java.util.*;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.log.*;

public class UtilEntity
{
  public static final boolean DEF_REMOTE_MODE = Boolean.getBoolean("entity.ejb.use.remote");
  
    public static IEntity getEntityByKey(long uId, String entityName, boolean useDefMode) throws Throwable
    {
        return getEntityByKey(new Long(uId), entityName, useDefMode);
    }

    public static IEntity getEntityByKey(Long uId, String entityName, boolean useDefMode) throws Throwable
    {
        try
        {
            return getHandlerFor(entityName, useDefMode).getEntityByKey(uId);
        }
        catch (Throwable th)
        {
            logWarn("[UtilEntity.getEntityByKey] Exception :uId=" + uId + ", entityName=" + entityName, th);
            throw th;
        }
    }

    public static Collection getEntityByFilter(IDataFilter filter, String entityName, boolean useDefMode) throws Throwable
    {
        try
        {
            return getHandlerFor(entityName, useDefMode).getEntityByFilter(filter);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.getEntityByFilter] Exception :filter=" + filter + ", entityName=" + entityName, th);
            throw th;
        }
    }

    public static IEntity getEntityByKeyForReadOnly(Long uId, String entityName, boolean useDefMode) throws Throwable
    {
        try
        {
            return getHandlerFor(entityName, useDefMode).getEntityByKeyForReadOnly(uId);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.getEntityByKeyForReadOnly] Exception :uId=" + uId + ", entityName=" + entityName, th);
            throw th;
        }
    }

    public static Collection getEntityByFilterForReadOnly(IDataFilter filter, String entityName, boolean useDefMode) throws Throwable
    {
        try
        {
            return getHandlerFor(entityName, useDefMode).getEntityByFilterForReadOnly(filter);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.getEntityByFilterForReadOnly] Exception :filter=" + filter + ", entityName=" + entityName, th);
            throw th;
        }
    }
   	  
	public static Collection getKeyByFilterForReadOnly(IDataFilter filter, String entityName, boolean useDefMode) throws Throwable
	{
		try
		{
			return getHandlerFor(entityName, useDefMode).getKeyByFilterForReadOnly(filter);
		}
		catch (Throwable th)
		{
			logWarn("[UtilEntity.getEntityByFilterForReadOnly] Exception :filter=" + filter + ", entityName=" + entityName, th);
			throw th;
		}
	}    

    public static void update(IEntity entity, boolean useDefMode) throws Throwable
    {
        try
        {
            getHandlerFor(entity.getEntityName(), useDefMode).update(entity);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.update] Exception : entity=" + entity, th);
            throw th;
        }
    }
    
    /**
     * TWX: similiar functionality as update(IEntity entity, boolean useDefMode) except
     *      it will return the entity that we just updated.
     * @param entity
     * @param useDefMode
     * @return
     * @throws Throwable
     */
    public static IEntity updateEntity(IEntity entity, boolean useDefMode) throws Throwable
    {
        try
        {
        	  logInfo("[UtilEntity.updateEntity]");
            return getHandlerFor(entity.getEntityName(), useDefMode).updateEntity(entity);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.updateEntity] Exception : entity=" + entity, th);
            throw th;
        }
    }
    
    public static void remove(long uId, String entityName, boolean useDefMode) throws Throwable
    {
        remove(new Long(uId), entityName, useDefMode);
    }

    public static void remove(Long uId, String entityName, boolean useDefMode) throws Throwable
    {
        logInfo("[UtilEntity.remove] Enter uId=" + uId + ",entityName=" + entityName);
        try
        {
            getHandlerFor(entityName, useDefMode).remove(uId);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.remove] Exception :uId=" + uId + ", entityName=" + entityName, th);
            throw th;
        }
    }

    public static void remove(IDataFilter filter, String entityName, boolean useDefMode) throws Throwable
    {
        logInfo("[UtilEntity.remove] Enter filter=" + filter + ",entityName=" + entityName);
        try
        {
            AbstractEntityHandler handler = getHandlerFor(entityName, useDefMode);

            handler.removeByFilter(filter);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.remove] Exception :filter=" + filter + ", entityName=" + entityName, th);
            throw th;
        }
    }

    public static void createAll(List entityList, boolean useDefMode) throws Throwable
    {
        if (entityList == null) return;
        try
        {
            Iterator iterator = entityList.iterator();

            while (iterator.hasNext())
            {
                IEntity entity = (IEntity) iterator.next();

                getHandlerFor(entity.getEntityName(), useDefMode).create(entity);
            }
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.createAll] Exception :List=" + entityList, th);
            throw th;
        }
    }

    public static void create(IEntity entity, boolean useDefMode) throws Throwable
    {
        try
        {
            getHandlerFor(entity.getEntityName(), useDefMode).create(entity);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.create] Exception :entity=" + entity, th);
            throw th;
        }

    }

    public static List createEntities(List entityList, boolean useDefMode) throws Throwable
    {
        if (entityList == null) return null;
        try
        {
            List createdList = new LinkedList();
            Iterator iterator = entityList.iterator();

            while (iterator.hasNext())
            {
                IEntity entity = (IEntity) iterator.next();

                createdList.add(getHandlerFor(entity.getEntityName(), useDefMode).createEntity(entity));
            }
            return createdList;
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.createEntities] Exception :List=" + entityList, th);
            throw th;
        }
    }

    public static IEntity createEntity(IEntity entity, boolean useDefMode) throws Throwable
    {
        try
        {
            return getHandlerFor(entity.getEntityName(), useDefMode).createEntity(entity);
        }
        catch (Throwable th)
        {
        	logWarn("[UtilEntity.createEntity] Exception :entity=" + entity, th);
            throw th;
        }
    }

    public static IEntity createNewInstance(String entityName, boolean useDefMode) throws Throwable
    {
        return getHandlerFor(entityName, useDefMode).createNewInstance();
    }

    public static AbstractEntityHandler getHandlerFor(String entityName, boolean useDefMode)
    {
      boolean isLocal = !DEF_REMOTE_MODE && useDefMode;
        return EntityHandlerFactory.getHandlerFor(entityName, isLocal);
    }

    private static void logInfo(String msg)
    {
        Log.log(Log.FRAMEWORK, msg);
    }

    /**
     * Log a warning message with a Throwable object
     * @param msg The warning message
     * @param t The Throwable object of the error
     */
    private static void logWarn(String msg, Throwable t)
    {
    	Log.warn(Log.FRAMEWORK, msg, t);
    }
    
    private static void logError(String errorCode, String msg, Throwable th)
    {
        Log.error(errorCode, Log.FRAMEWORK, msg, th);
    }

}


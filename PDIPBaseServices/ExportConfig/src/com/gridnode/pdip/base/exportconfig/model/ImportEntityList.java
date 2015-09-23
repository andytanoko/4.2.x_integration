/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2003    Koh Han Sing        Created
 * Feb 27 2004    Jagadeesh           Modified: old and new uid check in getEntity method.
 */
package com.gridnode.pdip.base.exportconfig.model;

import com.gridnode.pdip.base.exportconfig.helpers.Logger;
import com.gridnode.pdip.framework.db.entity.IEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This class is use to store entities during importing.
 */

public class ImportEntityList implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5485189216161507384L;
	private Hashtable _entityTable = new Hashtable();

  public ImportEntityList()
  {
  }

  public Hashtable getEntityTable()
  {
    return _entityTable;
  }

  /**
   * This constructor adds a collection of ImportEntity into the hashtable
   * with the entityName as the key.
   */
  public ImportEntityList(Collection importEntities, String entityName)
  {
    Object obj = _entityTable.get(entityName);
    if (obj != null)
    {
      Collection entityList = (Collection)obj;
      entityList.addAll(importEntities);
      _entityTable.put(entityName, entityList);
    }
    else
    {
      ImportEntity aEntity = (ImportEntity)importEntities.iterator().next();
      if (!aEntity.isEmbedded())
      {
        ArrayList entityList = new ArrayList(importEntities);
        _entityTable.put(entityName, entityList);
      }
    }
  }

  public void addEntities(Collection importEntities, String entityName)
  {
    Object obj = _entityTable.get(entityName);
    if (obj != null)
    {
      Collection entityList = (Collection)obj;
      entityList.addAll(importEntities);
      _entityTable.put(entityName, entityList);
    }
    else
    {
      ImportEntity aEntity = (ImportEntity)importEntities.iterator().next();
      if (!aEntity.isEmbedded())
      {
        ArrayList entityList = new ArrayList(importEntities);
        _entityTable.put(entityName, entityList);
      }
    }
  }

  /**
   * This method adds a ImportEntity into its respective list in the hashtable.
   */
  public void addImportEntity(ImportEntity importEntity)
  {
    //Logger.debug("[ImportEntityList.addImportEntity] Start");
    if (!importEntity.isEmbedded())
    {
      String entityName = importEntity.getEntity().getEntityName();
      //Logger.debug("[ImportEntityList.addImportEntity] entityName ="+entityName);
      Object obj = _entityTable.get(entityName);
      if (obj != null)
      {
        Collection entityList = (Collection)obj;
        entityList.add(importEntity);
        _entityTable.put(entityName, entityList);
      }
      else
      {
        ArrayList entityList = new ArrayList();
        entityList.add(importEntity);
        _entityTable.put(entityName, entityList);
      }
    }
  }

  public ImportEntity getEntity(IEntity entity)
  {
    String entityName = entity.getEntityName();
    Long entityUid = (Long)entity.getKey();
    return getEntity(entityName, entityUid);
  }


  public ImportEntity getEntity(String entityName, Long oldUid)
  {
    Logger.debug("[ImportEntityList.getEntity] entityName ="+entityName+" oldUid ="+oldUid);
    Object obj = _entityTable.get(entityName);
    if (obj != null)
    {
      Collection entityList = (Collection)obj;
      for (Iterator i = entityList.iterator(); i.hasNext(); )
      {
        ImportEntity importEntity = (ImportEntity)i.next();
        Long entityInListUid = importEntity.getOldUid();
        Logger.debug("[ImportEntityList.getEntity] entityInListUid ="+entityInListUid);
        if ((entityInListUid != null) && entityInListUid.equals(oldUid))
        {
          return importEntity;
        }
      }
    }
    return null;
  }

  public Collection getEntity(String entityName, Number fieldId, Object value)
  {
    ArrayList results = new ArrayList();
    Object obj = _entityTable.get(entityName);
    if (obj != null)
    {
      Collection entityList = (Collection)obj;
      for (Iterator i = entityList.iterator(); i.hasNext(); )
      {
        ImportEntity importEntity = (ImportEntity)i.next();
        Object valueInEntity = null;
        Object newUID=null;
        if (fieldId.equals(new Integer(0)))
        {
          // The uid field will be containing the new uid, so cannot match
          // using the getFieldValue
          valueInEntity = importEntity.getOldUid();
          newUID = importEntity.getNewUid();
          if ((valueInEntity != null) && valueInEntity.equals(value))
          {
            //Logger.debug("[ImportEntityList.getEntity]----OldUID Check" );
            results.add(importEntity);
          }
          else if ((newUID != null) && newUID.equals(value))
          {
            //Logger.debug("[ImportEntityList.getEntity]----NewUID Check" );
            results.add(importEntity);
          }
        }
        else
        {
          valueInEntity = importEntity.getEntity().getFieldValue(fieldId);
          if ((valueInEntity != null) && valueInEntity.equals(value))
          {
            //Logger.debug("[ImportEntityList.getEntity]----In Main Else block OldUID Check" );
            results.add(importEntity);
          }
        }

      }
    }
    return results;
  }

  public Collection getEntities()
  {
    ArrayList allEntities = new ArrayList();
    Enumeration enu = _entityTable.elements();
    while (enu.hasMoreElements())
    {
      Collection entityList = (Collection)enu.nextElement();
      allEntities.addAll(entityList);
    }
    return allEntities;
  }

  public void removeImportEntity(ImportEntity importEntity)
  {
    String entityName = importEntity.getEntity().getEntityName();
    ArrayList newList = new ArrayList();
    Object obj = _entityTable.get(entityName);
    if (obj != null)
    {
      Collection importEntities = (Collection)obj;
      for (Iterator i = importEntities.iterator(); i.hasNext(); )
      {
        ImportEntity aEntity = (ImportEntity)i.next();
        if (!aEntity.getOldUid().equals(importEntity.getOldUid()))
        {
          newList.add(aEntity);
        }
      }
      _entityTable.remove(entityName);
      _entityTable.put(entityName, newList);
    }
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    Enumeration enu = _entityTable.keys();
    while (enu.hasMoreElements())
    {
      String entityName = enu.nextElement().toString();
      Collection entityList = (Collection)_entityTable.get(entityName);
      if (!entityList.isEmpty())
      {
        sb.append("<<<<<");
        sb.append(entityName);
        sb.append(":");
      }
      for (Iterator i = entityList.iterator(); i.hasNext(); )
      {
        ImportEntity entity = (ImportEntity)i.next();
        sb.append(entity.getEntity().getEntityDescr());
        sb.append("|||||");
      }
      sb.append(">>>>>");
    }
    return sb.toString();
  }
}
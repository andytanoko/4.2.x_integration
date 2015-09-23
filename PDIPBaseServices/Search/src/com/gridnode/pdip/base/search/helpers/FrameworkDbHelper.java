/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FrameworkDbHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.helpers;

import com.gridnode.pdip.framework.db.meta.IEntityMetaInfoObj;
import com.gridnode.pdip.framework.db.meta.IEntityMetaInfoHome;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import com.gridnode.pdip.framework.exceptions.SystemException;

/** Helper class for accessing Framework Layer
 *
 *
 */

public class FrameworkDbHelper
{

  private IEntityMetaInfoHome entityMetaHome;
  private IEntityMetaInfoObj  entityMetaObj;

  private FieldMetaInfo fieldMetaInfo;
  private EntityMetaInfo  entityMetaInfo;

  private static FrameworkDbHelper _frameworkDbHelper = null;

  private FrameworkDbHelper()
  {

  }

  static public FrameworkDbHelper instance()
  {
    if (_frameworkDbHelper == null)
    {
          _frameworkDbHelper = new FrameworkDbHelper();
    }
    return _frameworkDbHelper;
  }

 /** Method for obtaining field meta information for a given entity name
  *
  * @param entityName Entity Name
  *
  * @return array of field meta info object
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public FieldMetaInfo[] getFieldMetaInfo(String entityName) throws SystemException
  {
    try{
      lookUpEntityMetaInfo();
      entityMetaObj = entityMetaHome.findByPrimaryKey(entityName);
      entityMetaInfo = entityMetaObj.getData();
      return entityMetaInfo.getFieldMetaInfo();
    }
    catch (Exception ex)
    {
      Logger.err( " Exception in FrameworkDbHelper.getFieldMetaInfo() : ", ex);
      throw new SystemException(" Exception in finding fields for Entity "+entityName,ex);
    }

  }

 /** Method for obtaining entity meta information for a given entity name
  *
  * @param entityName Entity Name
  *
  * @return Entity Meta info object
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public EntityMetaInfo getEntityMetaInfo(String entityName) throws SystemException
  {
    try{
      lookUpEntityMetaInfo();
      entityMetaObj = entityMetaHome.findByPrimaryKey(entityName);
      entityMetaInfo = entityMetaObj.getData();
      return entityMetaInfo;
    }
    catch (Exception ex)
    {
      Logger.err( " Exception in FrameworkDbHelper.getEntityMetaInfo() : ", ex);
      throw new SystemException(" Exception in finding Entity "+entityName,ex);
    }


  }

  private void lookUpEntityMetaInfo() throws SystemException
  {
      try
      {
	entityMetaHome = (IEntityMetaInfoHome)ServiceLookup.getInstance(
					ServiceLookup.CLIENT_CONTEXT).getHome(
 		      IEntityMetaInfoHome.class);
      }
      catch (Exception ex)
      {
	Logger.err( " Exception in FrameworkDbHelper.lookUpEntityMetaInfo() : ", ex);
	throw new SystemException(" Exception in lookup of IEntityMetaInfoHome",ex);
      }
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 * Jul 21 2003    Neo Sok Lay         Add various commonly used methods.
 */
package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2 I1
 * @since 2.0.2
 */
public class PartnerTypeTestHelper implements IEntityTestHelper
{
  public static final String NAME = "T";
  public static final String DESC = "Test Type";

  IPartnerManagerObj _manager;

  PartnerType[] _types;
  Long[] _typeUids; 
  PartnerType _defaultPartnerType;

  public PartnerTypeTestHelper()
  {
    try
    {
      _manager = getManager();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerTypeTestHelper:PartnerTypeTestHelper()] Exit", ex);
    }
  }

  public IEntity create(IEntity entity) throws Exception
  {
    Long uid = _manager.createPartnerType((PartnerType)entity);
    
    return _manager.findPartnerType(uid);
  }

  public void delete(Long uID) throws Exception
  {
    _manager.deletePartnerType(uID);
  }

  public void deleteAll() throws Exception
  {
    Collection collection = getAll();
    if ((collection == null) ||(collection.isEmpty()))
      return;
    for (Iterator i=collection.iterator(); i.hasNext();)
      delete((Long)((IEntity)i.next()).getKey());
  }

  public IEntity get(Long uID) throws Exception
  {
    return _manager.findPartnerType(uID);
  }

  public IEntity getByName(String name) throws Exception
  {
    return _manager.findPartnerTypeByName(name);
  }

  public Collection getAll() throws Exception
  {
    return _manager.findPartnerType((IDataFilter)null);
  }

  private IPartnerManagerObj getManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IPartnerManagerHome.class.getName(),
               IPartnerManagerHome.class,
               new Object[0]);
  }
  
  protected void createPartnerTypes(int num) throws Exception
  {
    _types = new PartnerType[num];
    _typeUids = new Long[num];
    for (int i=0; i<num; i++)
    {
      _types[i] = createPartnerType(NAME+i, DESC+i, true);
      _typeUids[i] = new Long(_types[i].getUId());
    }
  }

  protected void deletePartnerTypes(int num)
  {
    for (int i=0; i<num; i++)
    {
      deletePartnerType(NAME+i);
    }
  }
  
  protected void deletePartnerType(String name)
  {
    try
    {
      PartnerType deleted = (PartnerType)getByName(name);
      if (deleted != null)
         delete(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
    }
  }
  
  protected PartnerType createPartnerType(String name, String description, boolean canDelete)
    throws Exception
  {
    PartnerType type = new PartnerType();
    type.setCanDelete(canDelete);
    type.setDescription(description);
    type.setName(name);
    
    return (PartnerType)create(type);
  }  
  
  private void createDefaultPartnerType() throws Exception
  {
    if (_defaultPartnerType == null)
    {
      _defaultPartnerType = createPartnerType(
                              NAME,
                              DESC,
                              true);
    }
  }
  
  public PartnerType getDefaultPartnerType() throws Exception
  {
    createDefaultPartnerType();
    return _defaultPartnerType;
  }
  
  public void deleteDefaultPartnerType()
  {
    deletePartnerType(NAME);
  }
  
}
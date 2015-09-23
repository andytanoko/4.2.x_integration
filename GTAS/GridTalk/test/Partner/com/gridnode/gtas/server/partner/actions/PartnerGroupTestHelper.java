/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 12 2002    Ang Meng Hua        Created
 * Jul 21 2003    Neo Sok Lay         Add various commonly used methods.
 */
package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.server.actions.IEntityTestHelper;

import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
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
public class PartnerGroupTestHelper implements IEntityTestHelper
{
  public static final String NAME = "GP";
  public static final String DESC = "Test Group";
  
  IPartnerManagerObj    _manager;
  PartnerTypeTestHelper _helper;
  PartnerTestHelper _partnerHelper;
  
  PartnerGroup[] _groups;
  Long[] _groupUids; 
  
  public PartnerGroupTestHelper()
  {
    try
    {
      _manager = getManager();
      _helper  = new PartnerTypeTestHelper();
      _partnerHelper = new PartnerTestHelper();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerGroupTestHelper:PartnerGroupTestHelper()] Exit", ex);
    }
  }

  public IEntity create(IEntity entity) throws Exception
  {
    Long uid = _manager.createPartnerGroup((PartnerGroup)entity);
    
    return _manager.findPartnerGroup(uid);
  }

  public PartnerType createPartnerType(IEntity entity) throws Exception
  {
    return (PartnerType)_helper.create(entity);
  }

  public void delete(Long uID) throws Exception
  {
    _manager.deletePartnerGroup(uID);
  }

  public void deleteAll() throws Exception
  {
    Collection collection = getAll();
    if ((collection != null) && (!collection.isEmpty()))
    {
      for (Iterator i=collection.iterator(); i.hasNext();)
        delete((Long)((IEntity)i.next()).getKey());
    }

   // delete all partner type
    _helper.deleteAll();
  }


  public IEntity get(Long uID) throws Exception
  {
    return _manager.findPartnerGroup(uID);
  }

  public IEntity getByName(String name) throws Exception
  {
    return _manager.findPartnerGroupByName(name);
  }

  public Collection getAll() throws Exception
  {
    return _manager.findPartnerGroup((IDataFilter)null);
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
  
  public void createPartnerGroups(int numCanDelete, int numCannotDelete) throws Exception
  {
    PartnerType defaultPartnerType = getDefaultPartnerType();
    
    _groups = new PartnerGroup[numCanDelete+numCannotDelete];
    _groupUids = new Long[_groups.length];
    for (int i=0; i<numCanDelete; i++)
    {
      _groups[i] = createPartnerGroup(NAME+i, DESC+i, defaultPartnerType, true);
      _groupUids[i] = new Long(_groups[i].getUId());
    }

    for (int i=numCanDelete; i<_groups.length; i++)
    {
      _groups[i] = createPartnerGroup(NAME+i, DESC+i, defaultPartnerType, false);
      _groupUids[i] = new Long(_groups[i].getUId());
    }
  }

  public void deletePartnerGroups(int num)
  {
    for (int i=0; i<num; i++)
    {
      deletePartnerGroup(NAME+i);
    }
  }

  public void deletePartnerGroup(String name)
  {
    try
    {
      PartnerGroup deleted = (PartnerGroup)getByName(name);
      if (deleted != null)
         delete(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
    }
  }
  
  public PartnerGroup createPartnerGroup(String name, String description, 
    PartnerType type, boolean canDelete)
    throws Exception
  {
    PartnerGroup group = new PartnerGroup();
    group.setCanDelete(canDelete);
    group.setDescription(description);
    group.setName(name);
    group.setPartnerType(type);
    
    return (PartnerGroup)create(group);
  }
  
  public PartnerType getDefaultPartnerType() throws Exception
  {
    return _helper.getDefaultPartnerType();
  }
  
  public void deleteDefaultPartnerType()
  {
    _helper.deleteDefaultPartnerType();
  }
  
  public Long[] createPartners(int num, PartnerType type, PartnerGroup group)
    throws Exception
  {
    Long[] uids = new Long[num];
    
    Partner partner;
    for (int i=0; i<num; i++)
    {
      partner = _partnerHelper.createPartner(
                  _partnerHelper.PID+i,
                  _partnerHelper.NAME+i,
                  _partnerHelper.DESC+i,
                  type,
                  group,
                  true);
      uids[i] = new Long(partner.getUId());              
    }
    return uids; 
  }  
  
  public void deletePartners(int num)
  {
    _partnerHelper.deletePartners(num);
  }
  
  public void updatePartner(Long uid, Number[] fieldIds, Object[] values)
    throws Exception
  {
    _partnerHelper.updatePartner(uid, fieldIds, values);
  } 
  
  public void deletePartner(Long uid) throws Exception
  {
    _partnerHelper.delete(uid);
  }
  
}
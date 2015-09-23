/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 12 2002    Ang Meng Hua        Created
 * Jul 21 2003    Neo Sok Lay         Add various commonly used methods.
 */
package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.server.actions.EntityTestHelper;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 *
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2 I1
 * @since 2.0.2
 */
public class PartnerTestHelper extends EntityTestHelper
{
  private final String PARTNER_TYPE_HELPER = "Partner Type Helper";
  private final String PARTNER_GROUP_HELPER = "Partner Group Helper";

  public static final String PID = "PID";
  public static final String NAME = "Partner Name";
  public static final String DESC = "Partner Description";
  
  IPartnerManagerObj _manager;

  public PartnerTestHelper()
  {
    try
    {
      _manager = getManager();
      //addHelper(PARTNER_TYPE_HELPER, new PartnerTypeTestHelper());
      //addHelper(PARTNER_GROUP_HELPER, new PartnerGroupTestHelper());
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerTestHelper:PartnerTestHelper()] Exit", ex);
    }
  }

  public IEntity create(IEntity entity) throws Exception
  {
    Long uid = _manager.createPartner((Partner)entity);
    return _manager.findPartner(uid);
  }

  public PartnerType createPartnerType(IEntity entity) throws Exception
  {
    return (PartnerType)getHelper(PARTNER_TYPE_HELPER).create(entity);
  }

  public PartnerGroup createPartnerGroup(IEntity entity) throws Exception
  {
     return (PartnerGroup)getHelper(PARTNER_GROUP_HELPER).create(entity);
  }

  public void delete(Long uID) throws Exception
  {
    _manager.deletePartner(uID, false);
  }

  public IEntity get(Long uID) throws Exception
  {
    return _manager.findPartner(uID);
  }

  public IEntity getByPartnerID(String partnerID) throws Exception
  {
    return _manager.findPartnerByID(partnerID);
  }

  public Collection getAll() throws Exception
  {
    return _manager.findPartner((IDataFilter)null);
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


  public Partner createPartner(String partnerID, String name, String description, 
    PartnerType type, PartnerGroup group, boolean canDelete)
    throws Exception
  {
    Partner p = new Partner();
    p.setCanDelete(true);
    p.setDescription(description);
    p.setName(name);
    p.setPartnerType(type);
    p.setPartnerID(partnerID);
    p.setPartnerGroup(group);

    return (Partner)create(p);
  }
  
  public void deletePartners(int num)
  {
    for (int i=0; i<num; i++)
    {
      deletePartner(PID+i);
    }
  }
  
  public void deletePartner(String partnerId)
  {
    try
    {
      Partner deleted = (Partner)getByPartnerID(partnerId);
      if (deleted != null)
         delete(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
    }
  }
  
  public void updatePartner(Long uid, Number[] fieldIds, Object[] values)
    throws Exception
  {
    Partner partner = (Partner)get(uid); 
    
    for (int i=0; i<fieldIds.length; i++)
    {
      partner.setFieldValue(fieldIds[i], values[i]);
    }
    
    _manager.updatePartner(partner);
  }   
}
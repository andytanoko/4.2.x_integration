/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerBean.java
 *
 *********************************************************************************************
 * Date           Author              Changes
 *********************************************************************************************
 * Apr 18 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.entities.ejb;


import com.gridnode.pdip.app.partner.model.Partner;

// import framework related package
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.log.Log;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

/**
 * This Entity Bean represents the Partner. It provides the persistency
 * services for Partner entity.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public class PartnerBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6015859373697731738L;

	public String getEntityName()
  {
    return Partner.ENTITY_NAME;
  }

  /**
   * Call when create() is called on Home. Overwrite to set create time.
   *
   * @param entity The entity to create.
   * @return The primary key of the created entity.
   * @exception CreateException Create failed due to data error.
   * @exception EJBException Create failed due to system service errors.
   *
   * @since 2.0
   */
  public Long ejbCreate(IEntity entity)
    throws CreateException
  {
    _entity=entity;
    try
    {
      checkDuplicate(_entity);
      ((Partner)_entity).setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
      return getDAO().create(_entity);
    }
    catch(ApplicationException ex)
    {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbCreate] Error Exit ", ex);
      throw new CreateException(ex.getLocalizedMessage());
    }
    catch(Exception ex)
    {
      throw new EJBException(ex);
    }
  }

  protected void checkDuplicate(IEntity entity)
    throws Exception
  {
    String partnerID = (String)entity.getFieldValue(Partner.PARTNER_ID);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Partner.PARTNER_ID, filter.getEqualOperator(), partnerID, false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new ApplicationException("Partner already used: " + partnerID);
  }
}
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 13 2002    Goh Kan Mun             Created
 * Mar 05 2004    Neo Sok Lay             Implement Check duplicate.
 */

package com.gridnode.pdip.app.channel.entities.ejb;

import com.gridnode.pdip.app.channel.helpers.DAOHelper;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * The class implements the Entity Bean for the CommInfo.
 *
 * @author Goh Kan Mun
 *
 * @since 2.0
 * @version GT 2.2.10
 */

public class CommInfoBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1998377082964512205L;

	public CommInfoBean()
  {
  }

  /**
   * To retrieve the entity name of this bean.
   *
   * @return the entity name.
   *
   */
  public String getEntityName()
  {
    return CommInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#checkDuplicate(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  protected void checkDuplicate(IEntity entity) throws Exception
  {
    DAOHelper.getInstance().checkDuplicate((CommInfo)entity, false);
  }

}
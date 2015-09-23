/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Mar 05 2004    Neo Sok Lay             Implement Check Duplicate
 */
package com.gridnode.pdip.app.channel.entities.ejb;

import com.gridnode.pdip.app.channel.helpers.DAOHelper;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * The class implements the Entity Bean for the ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @since 2.0
 * @version GT 2.2.10
 */
public class ChannelInfoBean extends AbstractEntityBean
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7356717460837443419L;

	public ChannelInfoBean()
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
    return ChannelInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#checkDuplicate(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  protected void checkDuplicate(IEntity entity) throws Exception
  {
    DAOHelper.getInstance().checkDuplicate((ChannelInfo)entity, false);
  }

}
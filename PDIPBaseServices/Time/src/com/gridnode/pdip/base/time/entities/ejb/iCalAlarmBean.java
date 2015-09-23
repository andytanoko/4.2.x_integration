// %1023788044168:com.gridnode.pdip.base.time.ejb%
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
 * Jun 11 2002    Liu Xiao Hua	      Created
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.ejb;

import javax.ejb.CreateException;

import com.gridnode.pdip.base.time.entities.helpers.AlarmCaculator;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;

public class iCalAlarmBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1803586749018772152L;
	//Abstract methods of AbstractEntityBean
  protected static final String LogCat = iCalAlarmBean.class.getName();

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String getEntityName()
  {
    return iCalAlarm.class.getName();
  }

  /**
   * DOCUMENT ME!
   *
   * @param entity DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws CreateException DOCUMENT ME!
   */
  public Long ejbCreate(IEntity entity) throws CreateException
  {
    iCalAlarm alarm = (iCalAlarm) entity;
    new AlarmCaculator(alarm).caculateNextDueTime();
    Long uid = super.ejbCreate(alarm);
    return uid;
  }


  /**
   * DOCUMENT ME!
   *
   * @param aentity DOCUMENT ME!
   */
  public void setData(IEntity aentity)
  {
    this._entity = (IEntity) aentity.clone();
    iCalAlarm alarm = (iCalAlarm) _entity;
    new AlarmCaculator(alarm).caculateNextDueTime();
    Log.debug(LogCat, "setData called for Alarm "+ alarm.getUId() + " count=" + alarm.getCount()
    + "nextDueTime=" + alarm.getNextDueTime());
  }

}

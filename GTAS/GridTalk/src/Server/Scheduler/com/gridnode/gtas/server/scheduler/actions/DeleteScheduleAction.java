/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteScheduleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2004    Koh Han Sing        Created
 * Aug 15 2009    Tam Wei Xiang       #122 - enable the deletion for schedule archive
 *                                           task.
 */
package com.gridnode.gtas.server.scheduler.actions;

import com.gridnode.gtas.events.scheduler.DeleteScheduleEvent;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.helpers.Logger;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;

import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This Action class handles the deletion of a Schedule(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class DeleteScheduleAction extends AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4302560594890578612L;
	private static final String ACTION_NAME = "DeleteScheduleAction";

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   * @since GT 2.2 I1
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    String taskType = (String)entity.getFieldValue(Schedule.TASK_TYPE);
    //allow delete only for user procedure and DB archive task, at the moment
    if (Schedule.TASK_USER_PROCEDURE.equals(taskType) || Schedule.TASK_DB_ARCHIVE.equals(taskType))
      return true;
    return false;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity,Number)
   * @since GT 2.2 I1
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    Logger.log("[DeleteScheduleAction.deleteEntity] keyId ="+keyId);
    iCalAlarm alarm = ActionHelper.getManager().getAlarm((Long)entity.getFieldValue(keyId));
    Logger.log("[DeleteScheduleAction.deleteEntity] alarm.getParentUid ="+alarm.getParentUid());
    Long eventUid = alarm.getParentUid();
    ActionHelper.getManager().deleteEvent(eventUid);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    ArrayList uids = new ArrayList();
    List alarms = ActionHelper.getManager().findAlarms(filter);
    for (Iterator i = alarms.iterator(); i.hasNext(); )
    {
      iCalAlarm alarm = (iCalAlarm)i.next();
      uids.add(new Long(alarm.getUId()));
    }
    return uids;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   * @since GT 2.2 I1
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.findSchedulesByFilter(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   * @since GT 2.2 I1
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   * @since GT 2.2 I1
   */
  protected Class getExpectedEventClass()
  {
    return DeleteScheduleEvent.class;
  }

}

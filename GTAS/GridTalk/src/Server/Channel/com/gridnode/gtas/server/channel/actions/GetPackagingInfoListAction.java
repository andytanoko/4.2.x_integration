/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 21 2002    Jagadeesh               Created
 */


package com.gridnode.gtas.server.channel.actions;

import java.util.Collection;

import com.gridnode.gtas.events.channel.GetPackagingInfoListEvent;
import com.gridnode.gtas.model.channel.PackagingInfoEntityFieldId;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class GetPackagingInfoListAction extends GetEntityListAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3658772033740329507L;
	private static final String CLASS_NAME = "GetPackagingInfoListAction";
  private static int _listID = 0;

  public IEventResponse perform(IEvent event) throws EventException
  {
    return perform((GetPackagingInfoListEvent)event);
  }

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]);
  }

  // ********************* Methods from GetEntityListAction ******************

  protected void logEventStart()
  {
    ChannelLogger.debugLog(CLASS_NAME, "perform", "Start ");
  }

  protected void logEventError(Exception ex)
  {
    ChannelLogger.warnLog(CLASS_NAME, "perform", "Event Error ", ex);
  }

  protected Collection retrieveEntityList(IDataFilter filter) throws java.lang.Exception
  {
    return getManager().getPackagingInfo(filter);
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

  protected void logEventEnd()
  {
    ChannelLogger.debugLog(CLASS_NAME, "perform", "End ");
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return PackagingInfo.convertEntitiesToMap(
             (PackagingInfo[])entityList.toArray(new PackagingInfo[entityList.size()]),
             PackagingInfoEntityFieldId.getEntityFieldID(),
             null);
  }

  protected String getListIDPrefix()
  {
    return "PackagingInfoListCursor.";
  }

}


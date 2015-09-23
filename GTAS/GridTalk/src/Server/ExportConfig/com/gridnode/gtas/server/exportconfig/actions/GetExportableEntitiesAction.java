/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetExportableEntitiesAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.exportconfig.actions;

import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.gtas.events.exportconfig.GetExportableEntitiesEvent;
import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;

import com.gridnode.gtas.server.exportconfig.helpers.ExportConfigHelper;

import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the retrieving of all exportable entities.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class GetExportableEntitiesAction
  extends    AbstractGridTalkAction
  implements IExportConfigConstants
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7861271330749990804L;
	public static final String ACTION_NAME = "GetExportableEntitiesAction";

  protected Class getExpectedEventClass()
  {
    return GetExportableEntitiesEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ConfigEntitiesContainer container = ExportConfigHelper.getExportableEntities();

//    ConfigEntitiesContainer container = new ConfigEntitiesContainer();
//
//    ConfigEntityList list1 = new ConfigEntityList();
//    list1.setEntityName("Entity Type 1");
//
//    ConfigEntityDescriptor desc1 = new ConfigEntityDescriptor();
//    desc1.setDescription("Entity 1");
//    desc1.setUid(new Long(1));
//    list1.addConfigEntityDescriptor(desc1);
//
//    ConfigEntityDescriptor desc2 = new ConfigEntityDescriptor();
//    desc2.setDescription("Entity 2");
//    desc2.setUid(new Long(2));
//    list1.addConfigEntityDescriptor(desc2);
//
//    container.addConfigEntityList(list1);
//
//    ConfigEntityList list2 = new ConfigEntityList();
//    list2.setEntityName("Entity Type 2");
//
//    ConfigEntityDescriptor desc3 = new ConfigEntityDescriptor();
//    desc3.setDescription("Entity 3");
//    desc3.setUid(new Long(1));
//    list2.addConfigEntityDescriptor(desc3);
//
//    ConfigEntityDescriptor desc4 = new ConfigEntityDescriptor();
//    desc4.setDescription("Entity 4");
//    desc4.setUid(new Long(2));
//    list2.addConfigEntityDescriptor(desc4);
//
//    container.addConfigEntityList(list2);

    Object[] params = {};
    return constructEventResponse(IErrorCode.NO_ERROR,
                                  params,
                                  container);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    return new Object[] {};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    return new Object[] {};
  }
}
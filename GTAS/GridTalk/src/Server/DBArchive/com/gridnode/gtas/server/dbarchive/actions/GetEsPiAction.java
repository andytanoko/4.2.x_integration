/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsPiAction.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 7 Oct 2005	      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.server.dbarchive.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.dbarchive.EsPiEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.*;
import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.gtas.server.dbarchive.model.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.gtas.server.dbarchive.helpers.*;

/**
 * This Action class handles the retrieving of one ProcessInstance.  It is used for estore.
 * The implementation is based on  GetProcessInstanceAction.java
 */
public class GetEsPiAction extends AbstractGridTalkAction
{
  public static final String ACTION_NAME= "GetEsPiAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    return constructEventResponse(
      IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
      getErrorMessageParams(event),
      ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
  	Map retrieved = null;
  	GetEsPiEvent getInstEvent = (GetEsPiEvent) event;
  	ProcessInstanceMetaInfo pimi = ActionHelper.getManager().findEsPIByUID(getInstEvent.getUid());
  	retrieved = AbstractEntity.convertToMap(pimi, EsPiEntityFieldID.getEntityFieldID(), null);
//    GetEsPiEvent getInstEvent= (GetEsPiEvent) event;
    
//    Map retrieved=
//      ProcessInstanceActionHelper.getProcessInstance(
//        getInstEvent.getInstUID(),
//        getInstEvent.getDefName());
    if (retrieved == null)
    {
      return constructEventResponse(java.util.Collections.EMPTY_MAP);
    }

    return constructEventResponse(retrieved);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
  	GetEsPiEvent getEvent= (GetEsPiEvent) event;
    return new Object[] { String.valueOf(getEvent.getUid()) };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetEsPiEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

//  /**
//   * For testing.
//   */
//  private Map getEsPi()
//  {
////  	Map map = new HashMap();
////  	map.put(IProcessInstanceMetaInfo.UID, new Long(1));
////  	map.put(IProcessInstanceMetaInfo.Process_Instance_ID, "id 123");
////  	map.put(IProcessInstanceMetaInfo.Process_State, "state1");
////  	map.put(IProcessInstanceMetaInfo.Process_Start_Date, new Date());
////  	map.put(IProcessInstanceMetaInfo.Process_End_Date, new Date());
////  	map.put(IProcessInstanceMetaInfo.Partner_Duns, "Partner_Duns 1");
////  	map.put(IProcessInstanceMetaInfo.Process_Def, "Process_Def 1");
////  	map.put(IProcessInstanceMetaInfo.Role_Type, "Role_Type 1");
////  	map.put(IProcessInstanceMetaInfo.Partner_ID, "Partner_ID 1");
////  	map.put(IProcessInstanceMetaInfo.Partner_Name, "Partner_Name 1");
////  	map.put(IProcessInstanceMetaInfo.Doc_Number, "Doc_Number 1");
////  	map.put(IProcessInstanceMetaInfo.Doc_Date_Generated, new Long(1));
////  	map.put(IProcessInstanceMetaInfo.Originator_ID, "Originator_ID 1");
////  	ArrayList ar = new ArrayList();
////  	ar.add(new Long(111));
////  	map.put(IProcessInstanceMetaInfo.ASSOC_DOCS, ar);
//  	
//  	//TEST
//  	ProcessInstanceMetaInfo pi = EsUtil.getEsPi(1);
//  	return AbstractEntity.convertToMap(pi, EsPiEntityFieldID.getEntityFieldID(), null);
//  }
}
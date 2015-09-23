/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMethodsFromWSDLAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 07 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.userprocedure.actions;

import java.util.Collection;

import com.gridnode.gtas.events.userprocedure.GetMethodsFromWSDLEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of all methods in a class.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */
public class GetMethodsFromWSDLAction
  extends    AbstractGridTalkAction
  implements IUserProcedureConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2265737987819218927L;
	public static final String ACTION_NAME = "GetMethodsFromWSDLAction";

  protected Class getExpectedEventClass()
  {
    return GetMethodsFromWSDLEvent.class;
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
    GetMethodsFromWSDLEvent getMethodsEvent = (GetMethodsFromWSDLEvent)event;
    Long wsdlProDefUid = getMethodsEvent.getProcedureDefUid();
    Collection methods = getManager().getMethodDetailsFromWSDL(wsdlProDefUid);

    return constructEventResponse(methods);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    return new Object[] {};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    return new Object[] {};
  }

  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }

  public static void main(String[] args)
  {
    try
    {
      GetMethodsFromWSDLAction action = new GetMethodsFromWSDLAction();
      action.test();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void test() throws Exception
  {
//    Long procDefUid = new Long("2");
//    Collection methods = getManager().getMethodDetailsFromWSDL(procDefUid);
//    System.out.println("methods.size() = "+methods.size());
//    for (java.util.Iterator i = methods.iterator(); i.hasNext(); )
//    {
//      Object[] objs = (Object[])i.next();
//      System.out.println("==============================");
//      System.out.println("Method name = "+objs[0]);
//      System.out.println("Method return type = "+objs[1]);
//      String[] paramnames = (String[])objs[2];
//      for (int k = 0; k < paramnames.length; k++)
//      System.out.println("Method param names = "+paramnames[k]);
//      String[] params = (String[])objs[3];
//      for (int j = 0; j < params.length; j++)
//        System.out.println("Method param types = "+params[j]);
//      System.out.println("Method id = "+objs[4]);
//    }
  }
}
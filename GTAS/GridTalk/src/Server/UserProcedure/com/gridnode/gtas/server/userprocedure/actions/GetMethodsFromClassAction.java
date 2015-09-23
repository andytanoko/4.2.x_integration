/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMethodsFromClassAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 07 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.userprocedure.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.events.userprocedure.GetMethodsFromClassEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.classloader.AbstractMultiClassLoader;
import com.gridnode.pdip.framework.util.classloader.JarClassLoader;

/**
 * This Action class handles the retrieving of all methods in a class.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */
public class GetMethodsFromClassAction
  extends    AbstractGridTalkAction
  implements IUserProcedureConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2395278133562964173L;
	public static final String ACTION_NAME = "GetMethodsFromClassAction";

  protected Class getExpectedEventClass()
  {
    return GetMethodsFromClassEvent.class;
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
    AbstractMultiClassLoader loader = (AbstractMultiClassLoader)sm.getAttribute(CLASSLOADER_KEY);
    GetMethodsFromClassEvent getMethodsEvent = (GetMethodsFromClassEvent)event;
    String className = getMethodsEvent.getClassName();
    Collection methods = new ArrayList();
    try
    {
      methods = loader.getMethods(className);
    }
    catch (Exception ex)
    {
    }

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
      GetMethodsFromClassAction action = new GetMethodsFromClassAction();
      action.test();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void test() throws Exception
  {
    Long procUid = new Long("1");
    ProcedureDefFile procDefFile =
      getManager().getUserProcedure(procUid).getProcedureDefFile();
    File jarFile = FileUtil.getFile(procDefFile.getFilePath(),
                                    procDefFile.getFileName());

    String className = "com.gridnode.gtas.backend.sender.Locker";

    JarClassLoader loader = new JarClassLoader(jarFile.getAbsolutePath(),
                                               jarFile.getName());
//    Collection methods = getManager().getMethodSignature(loader, className);
    Collection methods = loader.getMethods(className);
    System.out.println(methods.size());
    for (java.util.Iterator i = methods.iterator(); i.hasNext(); )
    {
      Object[] objs = (Object[])i.next();
      System.out.println("==============================");
      System.out.println("Method name = "+objs[0]);
      System.out.println("Method return type = "+objs[1]);
      String[] paramNames = (String[])objs[2];
      for (int j = 0; j < paramNames.length; j++)
        System.out.println("Method param names = "+paramNames[j]);
      String[] params = (String[])objs[3];
      for (int j = 0; j < params.length; j++)
        System.out.println("Method param types = "+params[j]);
      System.out.println("Method id = "+objs[4]);
    }
  }
}
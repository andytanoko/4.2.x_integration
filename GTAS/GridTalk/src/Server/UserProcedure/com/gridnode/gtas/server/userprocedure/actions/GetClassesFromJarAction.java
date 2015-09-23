/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetClassesFromJarAction.java
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

import com.gridnode.gtas.events.userprocedure.GetClassesFromJarEvent;
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
import com.gridnode.pdip.framework.util.classloader.JarClassLoader;
import com.gridnode.pdip.framework.util.classloader.LocalClassLoader;

/**
 * This Action class handles the retrieving of all classes in a jar file.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */
public class GetClassesFromJarAction
  extends    AbstractGridTalkAction
  implements IUserProcedureConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3785199819464547664L;
	public static final String ACTION_NAME = "GetClassesFromJarAction";

  protected Class getExpectedEventClass()
  {
    return GetClassesFromJarEvent.class;
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
    GetClassesFromJarEvent getClassEvent = (GetClassesFromJarEvent)event;
    Long procDefUid = getClassEvent.getProcedureDefUid();
    ProcedureDefFile procDefFile =
      getManager().getProcedureDefinitionFile(procDefUid);
    File javaFile = FileUtil.getFile(procDefFile.getFilePath(),
                                     procDefFile.getFileName());
    String filename = javaFile.getName();

    if (filename.endsWith(".class"))
    {
      LocalClassLoader classLoader = new LocalClassLoader(javaFile.getAbsolutePath(),
                                                          getClass().getClassLoader()); //NSL20051228 To allow use of classes in application
      Collection classList = new ArrayList();
      classList.add(classLoader.getClassName());
      sm.setAttribute(CLASSLOADER_KEY, classLoader);
      return constructEventResponse(classList);
    }
    else
    {
      JarClassLoader loader = new JarClassLoader(javaFile.getParent(),
                                                 javaFile.getName(),
                                                 getClass().getClassLoader()); //NSL20051228 To allow use of classes in application
      Collection classList = loader.getAllClassName();
      sm.setAttribute(CLASSLOADER_KEY, loader);
      return constructEventResponse(classList);
    }
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

}
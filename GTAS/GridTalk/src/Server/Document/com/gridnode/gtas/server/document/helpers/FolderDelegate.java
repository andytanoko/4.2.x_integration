/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FolderDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.partnerfunction.model.IWorkflowActivity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This class provides system folders services.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class FolderDelegate
{
  /**
   * Obtain the EJBObject for the DocumentManagerBean.
   *
   * @return The EJBObject to the DocumentManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IDocumentManagerObj getManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  /**
   * This method will be called by the workflow module, based on the type of
   * exit, it will called the relevent method.
   *
   * @param exitType  The type of exit.
   * @param param     The parameter passed in if any, currently only used to
   *                  store the export port uid.
   * @param gdocs     The collection of griddocuments to process.
   * @return the collection of processed griddocuments.
   *
   * @since 2.0
   */
  public static Collection exit(String exitType, String param, Collection gdocs)
    throws Throwable
  {
    Logger.debug("[FolderDelegate.exit] exitType = "+exitType);
    Logger.debug("[FolderDelegate.exit] param = "+param);
    Integer type = new Integer(exitType);
    Long paramIn = new Long(-1);
    if ((param != null) && (!param.equals("")))
    {
      try
      {
        paramIn = new Long(param);
      }
      catch (NumberFormatException ex)
      {
        paramIn = new Long(-1);
      }
    }
    Collection returnGdocs = null;
    if (type.equals(IWorkflowActivity.EXIT_TO_EXPORT))
    {
      returnGdocs = getManager().exitToExport(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_TO_IMPORT))
    {
      returnGdocs = getManager().exitToImport(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_TO_OUTBOUND))
    {
      returnGdocs = getManager().exitToOutbound(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_WORKFLOW))
    {
      returnGdocs = getManager().exitFolderActivity(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_TO_PORT))
    {
      returnGdocs = getManager().exitToPort(gdocs, paramIn);
    }
    if (type.equals(IWorkflowActivity.SAVE_TO_FOLDER))
    {
      returnGdocs = getManager().saveToFolder(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_TO_CHANNEL))
    {
      returnGdocs = getManager().exitToChannel(gdocs);
    }
    if (type.equals(IWorkflowActivity.EXIT_POINT))
    {
      returnGdocs = getManager().exitFolderActivity(gdocs);
    }

    return returnGdocs;
  }

}
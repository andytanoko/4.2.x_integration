/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RaiseAlertDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2003    Neo Sok Lay         Created
 * Nov 23 2005    Neo Sok Lay         Change interface in DocAlertManager
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.notify.IAlertTypes;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.providers.IDataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 * This Delegate handles call in from the Workflow engine.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class RaiseAlertDelegate
{
  /**
   * Call in from Workflow engine. Raise a document related alert.
   *
   * @param alertType The type of document alert to raise.
   * @param inGdocs The list of GridDocument(s) for which to raise alert.
   * @return List of GridDocument(s) -- same as inGdocs.
   */
  public static Collection raiseAlert(String alertType,
                                      String userDefinedAlert,
                                      Collection inGdocs)
    throws Throwable
  {
    ArrayList outGdocs = new ArrayList(inGdocs);
    try
    {
      Logger.debug("[RaiseAlertDelegate.raiseAlert] Enter("+alertType+","+inGdocs+")");

      IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();

      if (IAlertTypes.DOCUMENT_RESPONSE_RECEIVED.equals(alertType))
      {
        for (Iterator i=inGdocs.iterator(); i.hasNext(); )
        {
          GridDocument gdoc = (GridDocument)i.next();
          mgr.invalidateResponseTracking(gdoc);
        }
      }
      else if (IAlertTypes.USER_DEFINED.equals(alertType))
      {
        Long alertUID = new Long(userDefinedAlert);
        for (Iterator i=inGdocs.iterator(); i.hasNext(); )
        {
          GridDocument gdoc = (GridDocument)i.next();
          // default no attachment, no other data providers
          //List result = mgr.raiseDocAlert(alertUID, gdoc, null, null, null);
          List result = mgr.raiseDocAlert(alertUID, gdoc, null, null); //NSL20051123
          Logger.log("[RaiseAlertDelegate.raiseAlert] Result="+result);
        }
      }
      else
      {
        for (Iterator i=inGdocs.iterator(); i.hasNext(); )
        {
          GridDocument gdoc = (GridDocument)i.next();
          List result = mgr.raiseDocAlert(alertType, gdoc, (IDataProvider[])null); //NSL20051123
          Logger.log("[RaiseAlertDelegate.raiseAlert] Result="+result);
        }
      }
    }
    finally
    {
      Logger.debug("[RaiseAlertDelegate.raiseAlert] Exit");
    }
    return outGdocs;
  }

}

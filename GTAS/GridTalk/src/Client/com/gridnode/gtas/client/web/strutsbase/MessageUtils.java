/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-30     Andrew Hill         Created
 * 2002-10-30     Andrew Hill         Use log for dump method
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Some static utilities to assist with using struts ActionMessages
 */
public class MessageUtils
{
  private static final Log _log = LogFactory.getLog(MessageUtils.class); // 20031209 DDJ
  
  /**
   * Will return the first action message stored in relation
   * to property, or null if not found.
   * Obviously this is not not suited to situations where there may be more than one ActionMessage
   * associated with a property. In such case make use of the get() method in ActionMessages class
   * to obtain an iterator over all the ActionMessage objects.
   * @param actionMessages
   * @param property
   * @return actionMessage
   */
  public static ActionMessage getFirstMessage(ActionMessages aMessages, String property)
  {
    if(aMessages != null)
    {
      Iterator i = aMessages.get(property);
      if(i.hasNext())
      {
        ActionMessage msg = (ActionMessage)i.next();
        return msg;
      }
    }
    return null;
  }

  public static String getFirstErrorKey(ActionErrors errors, String property)
  {
    ActionError error = getFirstError(errors,property);
    if(error != null)
    {
      return error.getKey();
    }
    else
    {
      return "";
    }
  }

  public static ActionError getFirstError(ActionErrors aErrors, String property)
  {
    if(aErrors != null)
    {
      Iterator i = aErrors.get(property);
      if(i.hasNext())
      {
        ActionError err = (ActionError)i.next();
        return err;
      }
    }
    return null;
  }

  public static void dumpActionErrors(ActionErrors errors)
  {
    if(_log.isDebugEnabled())
    {
      _log.debug("Started ------------------------------------------");
      if(errors == null)
      {
        _log.debug("errors is null");
      }
      else
      {
        Iterator p = errors.properties();
        while(p.hasNext())
        {
          String property = (String)p.next();
          Iterator i = errors.get(property);
          while(i.hasNext())
          {
            ActionError err = (ActionError)i.next();
            _log.debug("Property='" + property + "' key='" + err.getKey() + "'");
          }
        }
        _log.debug("Completed ----------------------------------------");
      }
    }
  }
}
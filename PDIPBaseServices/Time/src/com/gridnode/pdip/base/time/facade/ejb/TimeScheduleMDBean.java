// %1023788047699:com.gridnode.pdip.base.time.ejb%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.facade.ejb;

import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.time.facade.util.Alarm;
import com.gridnode.pdip.base.time.facade.util.AlarmAction;
import com.gridnode.pdip.framework.log.Log;

import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class TimeScheduleMDBean
  implements MessageDrivenBean,
             MessageListener,
             AlarmAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8438892392202621625L;
	static final String LogCat = "BASE.TIME";
  public MessageDrivenContext _mdx = null;

  /**
   * DOCUMENT ME!
   *
   * @param _ctx DOCUMENT ME!
   */
  public void setMessageDrivenContext(MessageDrivenContext _ctx)
  {
    this._mdx = _ctx;
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbCreate()
  {
    Log.debug(LogCat, "TimeScheduleMDBean is Created ");
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    Log.debug(LogCat, "TimeScheduleMDBean Removed ");
  }

  /**
   * Creates a new TimeScheduleMDBean object.
   */
  public TimeScheduleMDBean()
  {
  }

  /**
   * DOCUMENT ME!
   *
   * @param l_message DOCUMENT ME!
   */
  public void onMessage(Message l_message)
  {
    try
    {
      Log.debug(LogCat, "TimeScheduleMDBean Callback ");
      Object[] message = (Object[])((ObjectMessage)l_message).getObject();
      Integer action = (Integer)message[0];
      if (ADD.equals(action))
      {
        Alarm.instance().addAlarm((Long)message[1], (Date)message[2]);
      }
      else if (CANCEL.equals(action))
      {
        Alarm.instance().cancelAlarm((Long)message[1]);
      }
      else if (UPDATE.equals(action))
      {
        Alarm.instance().updateAlarm((Long)message[1], (Date)message[2]);
      }
      else
      {
        throw new EJBException("unspecified Schedule Message");
      }
      Log.debug(LogCat, "Result Message is " + message[0] + "_" +
                message[1]);
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.TIME_SCHEDULE_MDB_ONMESSAGE_ERROR,
                LogCat, "[TimeScheduleMDBean.onMessage] Could not perform onMessage, error: "+ex.getMessage(), ex);
    }
  }

}

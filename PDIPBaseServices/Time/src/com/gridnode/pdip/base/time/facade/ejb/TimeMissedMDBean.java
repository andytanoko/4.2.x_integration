// %1023788047340:com.gridnode.pdip.base.time.ejb%
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

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.time.facade.util.AlarmAction;
import com.gridnode.pdip.framework.log.Log;

public class TimeMissedMDBean implements MessageDrivenBean, MessageListener, AlarmAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8423161428503572113L;
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
    Log.debug(getClass().getName(), "TimeMissedMDBean is Created ");
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    Log.debug(getClass().getName(), "TimeMissedMDBean Removed ");
  }

  /**
   * Creates a new TimeMissedMDBean object.
   */
  public TimeMissedMDBean()
  {}

  /**
   * DOCUMENT ME!
   *
   * @param l_message DOCUMENT ME!
   */
  public void onMessage(Message l_message)
  {
    try
    {
      Log.debug(LogCat, "TimeMissedMDBean Callback ");
      Object[] message = (Object[]) ((ObjectMessage) l_message).getObject();
      Integer action = (Integer) message[0];
      Object[] additionInfo = (Object[]) message[2];
      if (MISSED.equals(action))
      {
        AlarmInfo info =
          new AlarmInfo(
            ((Long) additionInfo[0]).longValue(),
            (String) additionInfo[1],
            (String) additionInfo[2],
            (String) additionInfo[3],
            null,
            (String) additionInfo[4]);
        Object[] missedDates = (Object[]) message[1];
        invoke(info, missedDates);
      }
      else
      {
        throw new EJBException("unspecified Schedule Message");
      }
      //     Log.debug(LogCat, "Result Message is " + message);
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.TIME_MISSED_MDB_ONMESSAGE_ERROR,
                LogCat, "[TimeMissedMDBean.onMessage] Could not perform onMessage, error: "+ex.getMessage(), ex);
    }
  }

  protected void invoke(AlarmInfo info, Object[] missedDates)
  {
    Log.debug(
      LogCat,
      "Missed alarm for alarm " + info + " missedDates is" + arrayToString(missedDates));
  }

  static String arrayToString(Object[] objArray)
  {
    StringBuffer resStrBuf = new StringBuffer(200);
    resStrBuf.append('[');
    int count = objArray.length;
    for (int i = 0; i < count; i++)
    {
      resStrBuf.append(objArray[i]);
      resStrBuf.append(' ');
    }
    resStrBuf.append(']');
    return resStrBuf.toString();
  }

}

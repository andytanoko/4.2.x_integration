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
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.facade.ejb;

import com.gridnode.pdip.base.time.facade.util.AlarmAction;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;

import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class TimeInvokeMDBean
  implements MessageDrivenBean,
             MessageListener,
             AlarmAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9179054377501152332L;
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
    Log.debug(getClass().getName(), "TimeInvokeMDBean is Created ");
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    Log.debug(getClass().getName(), "TimeInvokeMDBean Removed ");
  }

  /**
   * Creates a new TimeInvokeMDBean object.
   */
  public TimeInvokeMDBean()
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
      Log.debug(LogCat, "TimeInvokeMDBean Callback ");
      if(l_message.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        Log.log(LogCat, "Redelivered msg found, ignored it. Message: "+l_message);
        return;
      }
      
      Object[] message = (Object[])((ObjectMessage)l_message).getObject();
      Integer action = (Integer)message[0];
      Object[] additionInfo = (Object[])message[2];
      if (INVOKE.equals(action))
      {
        AlarmInfo info = new AlarmInfo(((Long)additionInfo[0]).longValue(),
                                       (String)additionInfo[1], (String)additionInfo[2],
                                       (String)additionInfo[3], (Date)message[1],
                                       (String)additionInfo[4]);
        invoke(info);
      }
      else
      {
        throw new EJBException("unspecified Schedule Message");
      }
      //     Log.debug(LogCat, "Result Message is " + message);
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.TIME_INVOKE_ONMESSAGE_ERROR,
                LogCat, "[TimeInvokeMDBean.onMessage] Could not perform onMessage, error: "+ex.getMessage(), ex);
    }
  }

  protected void invoke(AlarmInfo info)
  {
    Log.debug(LogCat, "Invoke alarm for alarm " + info);
  }
}

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseStateMonitorMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28, 2006   i00107             Created
 */

package com.gridnode.gtas.server.partner.listener.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.notify.LicenseStateNotification;
import com.gridnode.gtas.server.partner.helpers.Logger;
import com.gridnode.gtas.server.partner.helpers.ServiceLookupHelper;

/**
 * @author i00107
 * Monitors the license state to grab the number of business connections
 * allowed by the license.
 */
public class LicenseStateMonitorMDBean implements
																			MessageListener,
																			MessageDrivenBean
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5468165735885030519L;
	private MessageDrivenContext _mdx = null;

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message arg0)
	{
		try
		{
			LicenseStateNotification notification = (LicenseStateNotification)((ObjectMessage)arg0).getObject();
			int max;
			if (notification.getState() == LicenseStateNotification.STATE_LICENSE_VALID)
			{
				max = notification.getNumBizConnections();
			}
			else
			{
				max = 0;
			}
			ServiceLookupHelper.getPartnerManager().setMaxActivePartners(max);
		}
		catch (Exception ex)
		{
			Logger.error(ILogErrorCodes.GT_LICENSE_STATE_MONITOR_MDB,
			             "[LicenseStateMonitorMDBean.onMessage] Error: "+ex.getMessage(), ex);
		}
	}

	/**
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	public void ejbRemove()
	{
		_mdx = null;
	}

	public void ejbCreate()
	{
	}

	/**
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	public void setMessageDrivenContext(MessageDrivenContext arg0)
	{
		_mdx = arg0;
	}

}

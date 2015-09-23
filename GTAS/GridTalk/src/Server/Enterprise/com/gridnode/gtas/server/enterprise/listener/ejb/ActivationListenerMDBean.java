/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationListenerMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.listener.ejb;

import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.gtas.server.enterprise.facade.ejb.ISharedResourceManagerObj;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.notify.ActivationNotification;

import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This MDB listens to the Notifier topic for notifications of Activation
 * activities.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4671874032593920684L;
	private MessageDrivenContext _mdx = null;

  public ActivationListenerMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove() throws javax.ejb.EJBException
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getActivationListenerFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      ActivationNotification notification = (ActivationNotification)((ObjectMessage)message).getObject();

      handleNotification(notification);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the ActivationNotification for a GridNode Partner:
   * <p>Share/Unshare the BusinessEntity(s) exchanged during Activation.
   * <p>Enable/Disable the Partner(s) under the GridNode partner.
   */
  private void handleNotification(ActivationNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getActivationListenerFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      if (notification.getState() == ActivationNotification.STATE_ACTIVATED)
      {
        // share be
        ServiceLookupHelper.getSharedResourceMgr().shareResourceIfNotShared(
          IResourceTypes.BUSINESS_ENTITY,
          notification.getMyBeUIDs(),
          notification.getPartnerNode());

        // update partner enable (if any)
        updatePartnerStates(notification.getPartnerBeUIDs(), Partner.STATE_ENABLED);
      }
      else
      {
        // remove the share be
        Long[] unshareBes = notification.getMyBeUIDs();
        ISharedResourceManagerObj shareMgr = ServiceLookupHelper.getSharedResourceMgr();
        for (int i=0; i<unshareBes.length; i++)
        {
          shareMgr.removeSharedResource(
            IResourceTypes.BUSINESS_ENTITY,
            unshareBes[i],
            notification.getPartnerNode());
        }

        // update partner disable (if any)
        updatePartnerStates(notification.getPartnerBeUIDs(), Partner.STATE_DISABLED);
      }

    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * Update the State of the Partner(s) that belong to each of the specified
   * partner BusinessEntity(s).
   *
   * @param partnerBes UIDs of the partner BusinessEntity(s)
   * @param state The state to update the partner to.
   */
  private void updatePartnerStates(Long[] partnerBes, short state) throws Throwable
  {
    IEnterpriseHierarchyManagerObj entMgr = ServiceLookupHelper.getEnterpriseHierarchyMgr();

    ArrayList partners = new ArrayList();
    for (int i=0; i<partnerBes.length; i++)
    {
      Collection partnerUIDs = entMgr.getPartnersForBizEntity(partnerBes[i]);
      partners.addAll(partnerUIDs);
    }

    if (partners.size() > 0)
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, Partner.UID, partners, false);

      IPartnerManagerObj partnerMgr = ServiceLookupHelper.getPartnerManager();

      Object[] result = partnerMgr.findPartner(filter).toArray();
      for (int i=0; i<result.length; i++)
      {
        Partner p = (Partner)result[i];
        p.setState(state);
        partnerMgr.updatePartner(p);
      }
    }
  }

}
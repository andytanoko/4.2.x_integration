/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityChangeListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.gwfbase.listener.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.base.gwfbase.notification.ProcessDefChangeNotification;
import com.gridnode.pdip.base.gwfbase.util.Logger;
import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * For handling the Process Def change notification. It will invalid the BpssDefinitionCache
 * that corresponding to the process def. This is to avoid the cache within the nodes in
 * the cluster out of sync with the new set of process def related value.
 * 
 * See GNDB00028507 for detail
 * 
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 * @since GT 4.1.2
 */
public class ProcessDefChangeListenerMDBean implements
                                       MessageDrivenBean,
                                       MessageListener
{
  /**
   * 
   */
  private static final long serialVersionUID = 6602877958058200875L;
  private MessageDrivenContext _mdCt;
  
  public void ejbCreate()
  {
    
  }
  
  public void ejbRemove() throws EJBException
  {
    // TODO Auto-generated method stub

  }

  public void setMessageDrivenContext(MessageDrivenContext ctxt) throws EJBException
  {
    _mdCt = ctxt;
  }

  public void onMessage(Message msg)
  {
    try
    {
      AbstractNotification notification = (AbstractNotification)((ObjectMessage)msg).getObject();
      if(notification instanceof ProcessDefChangeNotification)
      {
        BpssProcessSpecification processSpec = ((ProcessDefChangeNotification)notification).getProcessSpec();
        handleBpssProcessSpecificationUpdated(processSpec);
      }
      else
      {
        throw new IllegalArgumentException("Expecting ProcessDefChangeNotification obj.");
      }
    }
    catch(Throwable th)
    {
      Logger.debug("ProcessDefChangeListenerMDBean: error in handling the entity change event",th);
    }
    finally
    {
      Logger.debug("ProcessDefChangeListenerMDBean exit");
    }
  }
  
  private void handleBpssProcessSpecificationUpdated(BpssProcessSpecification processSpec)
  {
    Logger.log("ProcessDefChangeListenerMDBean.handleBpssProcessSpecificationUpdated: Start invalidate BpssDefinitionCache");
    try
    {
      BpssDefinitionCache.removeBpssDefinitionCache(processSpec.getSpecName(), processSpec.getSpecVersion(), processSpec.getSpecUUId());
    }
    catch(Exception ex)
    {
      Logger.debug("ProcessDefChangeListenerMDBean.handleBpssProcessSpecificationUpdated: Error in invalidating the BpssDefinitionCache", ex);
    }
  }
}

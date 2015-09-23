/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsgBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.jms;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.log.Log;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public abstract class JMSFailedMsgBean implements EntityBean
{
  private transient EntityContext _ctx = null;
  
  /**
   * @param failedMsg
   * @return
   * @throws CreateException
   */
  
  public Long ejbCreate(JMSFailedMsg failedMsg) throws CreateException
  {
    
    try
    {
      Long uid = KeyGen.getNextId("JmsFailedMsg", false);
      setUID(uid);
    }
    catch(Exception e)
    {
      Log.warn("[JMSFailedMsgBean.ejbCreate] error in getting the uid.",e);
      throw new CreateException(e.toString());
    }
    
    setDestinationType(failedMsg.getDestinationType());
    setJmsConfigProps(failedMsg.getConfigPrope());
    setDestName(failedMsg.getDestName());
    setMsgObj(failedMsg.getMsgObj());
    setMsgProps(failedMsg.getMsgProps());
    setCreatedDate(failedMsg.getCreatedDate());
    setRetryCount(failedMsg.getRetryCount());
    
    return null;
  }
  
  public Long ejbHomeGetNextUID(Integer retryCount, Long processedUID) throws FinderException
  {
    return ejbSelectNextUID(retryCount, processedUID);
  }
  
  public Long ejbHomeGetMinFailedMsgUID() throws FinderException
  {
    return ejbSelectMinFailedMsgUID();
  }
  
  public abstract Long ejbSelectNextUID(Integer retryCount, Long processedUIDs) throws FinderException;
  
  public abstract Long ejbSelectMinFailedMsgUID() throws FinderException;
  
  private String convertCollectionToStr(Collection<Long> processedUID)
  {
    
    String uidInStr = "";
    
    if(processedUID != null && processedUID.size() > 0)
    {
      for(Iterator<Long> ite = processedUID.iterator(); ite.hasNext(); )
      {
        Long uid = ite.next();
        uidInStr += ","+uid;
      }
      uidInStr = uidInStr.substring(1);
    }
    
    return uidInStr; 
    //return 1;
  }
  
  public void ejbPostCreate(JMSFailedMsg failedMsg)
  {
    
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#ejbActivate()
   */
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#ejbLoad()
   */
  public void ejbLoad() throws EJBException, RemoteException
  {
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#ejbPassivate()
   */
  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#ejbRemove()
   */
  public void ejbRemove() throws RemoveException, EJBException, RemoteException
  {
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#ejbStore()
   */
  public void ejbStore() throws EJBException, RemoteException
  {
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
   */
  public void setEntityContext(EntityContext ctx) throws EJBException,
                                                  RemoteException
  {
    _ctx = ctx;
  }

  /* (non-Javadoc)
   * @see javax.ejb.EntityBean#unsetEntityContext()
   */
  public void unsetEntityContext() throws EJBException, RemoteException
  {
    _ctx = null;
  }
  
  //Vitual Persistence fields getter and setter
  public abstract Long getUID();
  public abstract void setUID(Long uid);
  
  public abstract String getDestinationType();
  public abstract void setDestinationType(String destType);
  
  public abstract Hashtable<String, String> getJmsConfigProps();
  public abstract void setJmsConfigProps(Hashtable<String, String> jmsConfigProps);
  
  public abstract String getDestName();
  public abstract void setDestName(String destName);
  
  public abstract Serializable getMsgObj();
  public abstract void setMsgObj(Serializable msgObj);
  
  public abstract Hashtable getMsgProps();
  public abstract void setMsgProps(Hashtable msgProps);
  
  public abstract Timestamp getCreatedDate();
  public abstract void setCreatedDate(Timestamp createdDate);
  
  public abstract Integer getRetryCount();
  public abstract void setRetryCount(Integer retryCount);
  
}
